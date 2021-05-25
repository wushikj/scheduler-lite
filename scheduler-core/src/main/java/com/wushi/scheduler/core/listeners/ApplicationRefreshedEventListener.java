package com.wushi.scheduler.core.listeners;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import com.google.common.io.Files;
import com.wushi.scheduler.common.autoconfigure.MixConfiguration;
import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import com.wushi.scheduler.common.utitls.PathUtils;
import com.wushi.scheduler.core.entity.FileChangeEvent;
import com.wushi.scheduler.core.entity.JobDataExt;
import com.wushi.scheduler.core.utitls.IntervalUtils;
import com.wushi.scheduler.common.ConfigurationFactory;
import com.wushi.scheduler.common.constants.VariableKeys;
import com.wushi.scheduler.common.interfaces.JobStorage;
import com.wushi.scheduler.common.utitls.ExceptionUtils;
import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.enums.TriggerType;
import com.wushi.scheduler.common.exceptions.JobConfigSerializationException;
import com.wushi.scheduler.common.exceptions.JobIntervalCastException;
import com.wushi.scheduler.common.exceptions.NotImplementedException;
import com.wushi.scheduler.core.entity.FileChangeDetail;
import com.wushi.scheduler.core.interfaces.SchedulerHooks;
import org.quartz.*;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author yulianghua
 * @date 2019/12/27 5:18 PM
 * @description
 */
@Component
class ApplicationRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationRefreshedEventListener.class);
    private final SchedulerConfiguration schedulerConfiguration;
    private final SchedulerHooks schedulerHooks;
    private final Scheduler scheduler;
    private final ConfigurationFactory jobConfigurationFactory;
    private final MixConfiguration mixConfiguration;

    public ApplicationRefreshedEventListener(SchedulerConfiguration schedulerConfiguration, Scheduler scheduler, ConfigurationFactory jobConfigurationFactory, SchedulerHooks schedulerHooks, MixConfiguration mixConfiguration) {
        this.schedulerConfiguration = schedulerConfiguration;
        this.scheduler = scheduler;
        this.jobConfigurationFactory = jobConfigurationFactory;
        this.schedulerHooks = schedulerHooks;
        this.mixConfiguration = mixConfiguration;
    }

    private void createSchedulerIdentity() throws SchedulerException {
        String basePath = PathUtils.getBasePath();
        String identityFile = Paths.get(basePath, "config", "identity").toUri().getPath();
        File file = new File(identityFile);
        String identity = UUID.randomUUID().toString();
        if (file.exists()) {
            List<String> content = FileUtil.readUtf8Lines(file);
            identity = content.get(0);
        } else {
            file = FileUtil.writeString(identity, file, StandardCharsets.UTF_8);
        }

        this.scheduler.getContext().put(VariableKeys.IDENTITY, identity);
        logger.info("The scheduler identity: {}", identity);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            createSchedulerIdentity();
            addSchedulerListeners();
            loadJobFromPackages();
        } catch (SchedulerException | IOException | ClassNotFoundException | JobIntervalCastException | JobConfigSerializationException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            try {
                scheduler.shutdown(true);
            } catch (SchedulerException ex) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }

            AnnotationConfigServletWebServerApplicationContext context = (AnnotationConfigServletWebServerApplicationContext) contextRefreshedEvent.getApplicationContext();
            int exitCode = SpringApplication.exit(context, () -> 0);
            System.exit(exitCode);
        }
    }

    private FileChangeEvent getFileChangeEvent(WatchEvent.Kind kind) {
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            return FileChangeEvent.CREATE;
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            return FileChangeEvent.MODIFY;
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            return FileChangeEvent.DELETE;
        }
        return FileChangeEvent.UNKNOW;
    }

    private void addFileListener(String targetFile, JobDetail detail) {
        File file = FileUtil.file(targetFile);
        String watchMonitorName = UUID.randomUUID().toString();
        WatchMonitor watchMonitor = WatchMonitor.create(file, WatchMonitor.ENTRY_MODIFY, WatchMonitor.ENTRY_CREATE, WatchMonitor.ENTRY_DELETE, WatchMonitor.OVERFLOW);
        watchMonitor.setName(watchMonitorName);
        watchMonitor.setWatcher(new Watcher() {
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                watcher(event, detail, currentPath, watchMonitorName);
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                watcher(event, detail, currentPath, watchMonitorName);
            }

            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                watcher(event, detail, currentPath, watchMonitorName);
            }

            @Override
            public void onOverflow(WatchEvent<?> watchEvent, Path path) {
                try {
                    throw new NotImplementedException(ApplicationRefreshedEventListener.class.getSimpleName() + "#addFileListener#onOverflow");
                } catch (NotImplementedException e) {
                    logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
                }
            }
        });

        watchMonitor.setMaxDepth(1);
        watchMonitor.start();
    }

    private void watcher(WatchEvent<?> event, JobDetail detail, Path path, String watchMonitorName) {
        try {
            JobBean jobBean = (JobBean) detail.getJobDataMap().get(VariableKeys.JOB_ENTITY);
            JobDataExt jobBeanExt = (JobDataExt) detail.getJobDataMap().get(VariableKeys.JOB_DATA_EXT);
            FileChangeDetail fileChangeDetail = new FileChangeDetail();
            fileChangeDetail.setWatchMonitorName(watchMonitorName);
            fileChangeDetail.setTriggerType(jobBean.getTriggerType());
            fileChangeDetail.setTargetPath(path);
            fileChangeDetail.setWith(jobBean.getTargetFile());
            fileChangeDetail.setFileChangeEvent(getFileChangeEvent(event.kind()));
            jobBeanExt.setFileChangeDetail(fileChangeDetail);
            scheduler.triggerJob(detail.getKey());
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    private void loadJobFromPackages() throws SchedulerException, IOException, ClassNotFoundException, JobIntervalCastException, JobConfigSerializationException {
        JobStorage configuration = jobConfigurationFactory.getConfigurationSource();
        List<JobBean> jobs = configuration.load();

        Reflections reflections = new Reflections("com.wushi");
        Set<Class<? extends Job>> classes = reflections.getSubTypesOf(Job.class);

        for (JobBean job : jobs) {
            if (!job.isEnabled()) {
                logger.warn("跳过任务创建：Job[" + job + "]");
                continue;
            }

            if (classes.stream().anyMatch(t -> t.getName().equals(job.getClassName()))) {
                Class jobClass = Class.forName(job.getClassName());
                JobDetail detail = JobBuilder.newJob(jobClass).withIdentity(job.getId(), job.getGroup()).storeDurably().build();
                detail.getJobDataMap().put(VariableKeys.JOB_ID, job.getId());
                detail.getJobDataMap().put(VariableKeys.JOB_ENTITY, job);
                detail.getJobDataMap().put(VariableKeys.JOB_DATA_EXT, new JobDataExt());
                detail.getJobDataMap().put(VariableKeys.SCHEDULER, scheduler);

                TriggerType triggerType = job.getTriggerType();
                if (triggerType == null) {
                    logger.warn("任务的trigger属性值设置无效，请检查，有效的值为interval、cron、file。");
                    continue;
                }

                if (triggerType == TriggerType.INTERVAL || triggerType == TriggerType.CRON) {
                    Trigger trigger = getTrigger(job, detail, triggerType);
                    scheduler.scheduleJob(detail, trigger);

                    if (job.isImmediate() && triggerType == TriggerType.CRON) {
                        scheduler.triggerJob(detail.getKey());
                    }
                } else {
                    addFileListener(job.getTargetFile(), detail);
                    scheduler.addJob(detail, true);
                }
            } else {
                logger.warn("未找到有效的类名：" + job.getClassName());
            }
        }
    }

    private Trigger getTrigger(JobBean job, JobDetail detail, TriggerType triggerType) {
        final String name = job.getId() + "_Trigger";
        final String group = "defaultTriggerGroup";

        Trigger trigger = null;
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(name, group).startNow();
        if (triggerType == TriggerType.INTERVAL) {
            try {
                int interval = 5;
                interval = IntervalUtils.getInterval(job, job.getInterval());
                trigger = triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(interval)).build();
            } catch (JobIntervalCastException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }
        } else if (triggerType == TriggerType.CRON) {
            trigger = triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(job.getCron())).build();
        }
        return trigger;
    }

    private void addSchedulerListeners() throws SchedulerException {
        scheduler.getListenerManager().addSchedulerListener(new GlobalSchedulerListener(scheduler, schedulerHooks, schedulerConfiguration, mixConfiguration));
    }
}
