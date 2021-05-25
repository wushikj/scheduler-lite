package com.wushi.scheduler.core;

import com.wushi.scheduler.common.utitls.JsonUtils;
import com.wushi.scheduler.core.entity.FileChangeDetail;
import com.wushi.scheduler.core.entity.JobDataExt;
import com.wushi.scheduler.core.enums.SchedulerState;
import com.wushi.scheduler.common.constants.VariableKeys;
import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.utitls.ExceptionUtils;
import com.wushi.scheduler.common.utitls.PathUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author yulianghua
 * @date 2019/12/31 11:34 AM
 * @description
 */
public class ExecuteContext implements JobExecutionContext {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteContext.class);
    private final JobExecutionContext jobExecutionContext;

    /**
     * 构造方法
     *
     * @param jobExecutionContext 原始上下文
     * @author yulianghua
     * @date 2020/3/24 1:52 PM
     * @description
     */
    public ExecuteContext(JobExecutionContext jobExecutionContext) {
        this.jobExecutionContext = jobExecutionContext;
    }

    /**
     * 获取计划任务程序的运行状态
     *
     * @return {@link SchedulerState}
     * @throws SchedulerException 计划任务异常
     */
    public SchedulerState getSchedulerState() throws SchedulerException {
        return (SchedulerState) this.jobExecutionContext.getScheduler().getContext().get(VariableKeys.SCHEDULER_STATE);
    }

    /**
     * 获取任务静态配置数据
     *
     * @param key  配置key
     * @param type 要转换的类型
     * @param <T>  目标类型
     * @return T
     */
    public <T> T getStaticData(String key, Class<T> type) {
        return getCustomData(key, type);
    }

    /**
     * 获取任务配置数据，过期的，请改用getStaticData()方法
     *
     * @param key  配置key
     * @param type 要转换的类型
     * @param <T>  目标类型
     * @return T
     */
    @Deprecated
    public <T> T getCustomData(String key, Class<T> type) {
        Map<String, Object> map = getCurrentJobBean().getData();
        if (map == null || !map.containsKey(key)) {
            //单独处理boolean类型，其它类型暂不考虑
            String booleanTypeName = "boolean";
            if (booleanTypeName.equals(type.getName())) {
                Boolean data = Boolean.FALSE;
                return (T) data;
            }
            return null;
        }

        Object data = map.get(key);
        if (type.isInstance(data)) {
            return type.cast(data);
        }

        if (type == LocalDateTime.class) {
            return (T) LocalDateTime.parse(data.toString());
        }

        if (Map.class.isInstance(data)) {
            //T admin = JSONObject.parseObject(JSONObject.toJSONString(data), type);
            T admin = JsonUtils.parseObject(JsonUtils.toJSONString(data), type);
            return admin;
        }

        return (T) data;
    }

    /**
     * 获取traceId
     *
     * @return traceId
     * @author yulianghua
     * @date 2020/1/7 11:05 AM
     * @description
     */
    public String getCurrentTraceId() {
        return jobExecutionContext.getJobDetail().getJobDataMap().getString(VariableKeys.TRACE_ID);
    }

    /**
     * 获取Job编号（id）
     *
     * @return jobId
     * @author yulianghua
     * @date 2020/1/7 11:04 AM
     * @description
     */
    public String getCurrentJobId() {
        return jobExecutionContext.getJobDetail().getJobDataMap().getString(VariableKeys.JOB_ID);
    }

    /**
     * 获取当前job的Key（包含name和group）
     *
     * @return {@link JobKey}
     * @author yulianghua
     * @date 2020/1/7 11:08 AM
     * @description
     */
    public JobKey getCurrentJobKey() {
        return jobExecutionContext.getJobDetail().getKey();
    }

    /**
     * 获取当前job触发器的key
     *
     * @return {@link TriggerKey}
     * @author yulianghua
     * @date 2020/2/6 1:12 PM
     * @description
     */
    public TriggerKey getCurrentJobTriggerKey() {
        return jobExecutionContext.getTrigger().getKey();
    }

    /**
     * 获取当前Job的信息
     *
     * @return {@link JobBean}
     * @author yulianghua
     * @date 2020/1/7 11:12 AM
     * @description
     */
    public JobBean getCurrentJobBean() {
        return (JobBean) jobExecutionContext.getJobDetail().getJobDataMap().get(VariableKeys.JOB_ENTITY);
    }

    /**
     * 获取文件变更的详细信息 {@link FileChangeDetail}
     *
     * @return 文件变更的详细信息
     */
    public FileChangeDetail getFileChangeDetail() {
        JobDataExt dataExt = (JobDataExt) jobExecutionContext.getJobDetail().getJobDataMap().get(VariableKeys.JOB_DATA_EXT);
        return dataExt.getFileChangeDetail();
    }

    /**
     * 停止当前任务
     *
     * @author yulianghua
     * @date 2020/9/16 10:16 AM
     * @description
     */
    public void stop() {
        final JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        final TriggerKey jobTriggerKey = jobExecutionContext.getTrigger().getKey();
        try {
            jobExecutionContext.getScheduler().pauseJob(jobKey);
            jobExecutionContext.getScheduler().pauseTrigger(jobTriggerKey);
        } catch (SchedulerException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 获取全局Scheduler
     *
     * @return {@link Scheduler}
     * @author yulianghua
     * @date 2020/1/7 11:11 AM
     * @description
     */
    @Override
    public Scheduler getScheduler() {
        return jobExecutionContext.getScheduler();
    }

    @Override
    public Trigger getTrigger() {
        return jobExecutionContext.getTrigger();
    }

    @Override
    public Calendar getCalendar() {
        return jobExecutionContext.getCalendar();
    }

    @Override
    public boolean isRecovering() {
        return jobExecutionContext.isRecovering();
    }

    @Override
    public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
        return jobExecutionContext.getRecoveringTriggerKey();
    }

    @Override
    public int getRefireCount() {
        return jobExecutionContext.getRefireCount();
    }

    @Override
    public JobDataMap getMergedJobDataMap() {
        return jobExecutionContext.getMergedJobDataMap();
    }

    @Override
    public JobDetail getJobDetail() {

        return jobExecutionContext.getJobDetail();
    }

    @Override
    public Job getJobInstance() {
        return jobExecutionContext.getJobInstance();
    }

    @Override
    public Date getFireTime() {
        return jobExecutionContext.getFireTime();
    }

    @Override
    public Date getScheduledFireTime() {
        return jobExecutionContext.getScheduledFireTime();
    }

    @Override
    public Date getPreviousFireTime() {
        return jobExecutionContext.getPreviousFireTime();
    }

    @Override
    public Date getNextFireTime() {
        return jobExecutionContext.getNextFireTime();
    }

    @Override
    public String getFireInstanceId() {
        return jobExecutionContext.getFireInstanceId();
    }

    @Override
    public Object getResult() {
        return jobExecutionContext.getResult();
    }

    @Override
    public void setResult(Object o) {
        jobExecutionContext.setResult(o);
    }

    @Override
    public long getJobRunTime() {
        return jobExecutionContext.getJobRunTime();
    }

    @Override
    public void put(Object o, Object o1) {
        jobExecutionContext.put(o, o1);
    }

    @Override
    public Object get(Object o) {
        return jobExecutionContext.get(o);
    }

    /**
     * 获取当前jobId为前缀所有的动态配置数据
     * <pre>
     * {@code
     * 假设jobId为testJob，配置如下:
     * testJob.b.c=23
     * testJob.b.d=24
     *
     * 获取配置：
     * context.getActiveData()
     * }
     * </pre>
     *
     * @return map
     */
    public Map<String, String> getActiveData() {
        String jobId = getCurrentJobId();
        return getActiveDataByPrefix(jobId);
    }

    /**
     * 获取动态配置数据
     *
     * @param key 配置key
     * @return String
     */
    public <T> T getActiveData(String key, Class<T> type) {
        String input = getActiveDataSource().getProperty(key);
        return getValue(input, type);
    }

    /**
     * 获取动态配置数据
     *
     * @param prefix 配置前缀
     * @return map
     */
    public Map<String, String> getActiveDataByPrefix(String prefix) {
        Properties dataSource = getActiveDataSource();
        HashMap<String, String> result = new HashMap<>();
        for (String name : dataSource.stringPropertyNames()) {
            if (name.startsWith(prefix)) {
                result.put(name, dataSource.getProperty(name));
            }
        }
        return result;
    }

    /**
     * 修改动态配置数据
     *
     * @param key   配置key
     * @param value 配置值
     */
    public <T> void setActiveData(String key, T value, Class<T> type) {
        final String activeDataFileName = "jobs_active_data";
        String basePath = PathUtils.getBasePath();
        Path source = Paths.get(basePath, "config", activeDataFileName + ".properties");
        Path target = Paths.get(basePath, "config", activeDataFileName + ".properties_backup");
        try {
            logger.info("备份 " + activeDataFileName + " jobs.properties -> " + activeDataFileName + ".properties_backup.");
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("备份 " + activeDataFileName + ".properties 失败." + ExceptionUtils.getStackTrace(e), e);
        }

        try {
            Properties properties = getActiveDataSource();
            properties.setProperty(key, value.toString());
            try (OutputStream outputStream = new FileOutputStream(source.toFile())) {
                properties.store(outputStream, "updated");
                logger.info(MessageFormat.format("更新 " + activeDataFileName + ".properties: key-> {0}, value-> {1}.", key, value));
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private Properties getActiveDataSource() {
        Properties properties = new Properties();
        try {
            String basePath = PathUtils.getBasePath();
            String path = Paths.get(basePath, "config", "jobs_active_data.properties").toUri().getPath();
            try (InputStream inputStream = new FileInputStream(path)) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return properties;
    }

    private <T> T getValue(String input, Class<T> type) {
        if (type.isInstance(input)) {
            return type.cast(input);
        } else if (type == int.class || type == Integer.class) {
            return (T) Integer.valueOf(input);
        } else if (type == long.class || type == Long.class) {
            return (T) Long.valueOf(input);
        } else if (type == LocalDateTime.class) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (input.contains("T")) {
                formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            }
            return (T) LocalDateTime.parse(input, formatter);
        } else if (type == boolean.class || type == Boolean.class) {
            return (T) Boolean.valueOf(input);
        } else if (type == float.class || type == Float.class) {
            return (T) Float.valueOf(input);
        } else if (type == double.class || type == Double.class) {
            return (T) Double.valueOf(input);
        } else {
            return (T) input;
        }
    }
}
