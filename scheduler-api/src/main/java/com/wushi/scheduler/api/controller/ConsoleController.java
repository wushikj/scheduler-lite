package com.wushi.scheduler.api.controller;

import com.wushi.scheduler.api.entity.*;
import com.wushi.scheduler.common.ConfigurationFactory;
import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import com.wushi.scheduler.common.constants.VariableKeys;
import com.wushi.scheduler.common.entity.SchedulerDetail;
import com.wushi.scheduler.common.utitls.DateUtils;
import com.wushi.scheduler.common.utitls.ExceptionUtils;
import com.wushi.scheduler.common.utitls.PathUtils;
import com.wushi.scheduler.api.exceptions.SchedulerControlException;
import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.exceptions.JobConfigSerializationException;
import com.wushi.scheduler.api.interfaces.ApiResult;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author yulianghua
 * @date 2020/9/15 11:22 AM
 * @description
 */
@RestController
@RequestMapping("scheduler/api/v1")
public class ConsoleController {
    private final Scheduler scheduler;
    private final SchedulerConfiguration schedulerConfiguration;
    private final ConfigurationFactory jobConfigurationFactory;
    private static final Logger logger = LoggerFactory.getLogger(ConsoleController.class);

    public ConsoleController(Scheduler scheduler, SchedulerConfiguration schedulerConfiguration, ConfigurationFactory jobConfigurationFactory) {
        this.scheduler = scheduler;
        this.schedulerConfiguration = schedulerConfiguration;
        this.jobConfigurationFactory = jobConfigurationFactory;
    }

    @GetMapping("/detail")
    public JsonResult getSchedulerDetail() throws SchedulerException, IOException, JobConfigSerializationException {
        SchedulerDetail schedulerDetail = new SchedulerDetail();
        schedulerDetail.setName(this.schedulerConfiguration.getName());
        schedulerDetail.setDescription(this.schedulerConfiguration.getDescription());
        schedulerDetail.setDisplayName(this.schedulerConfiguration.getDisplayName());
        schedulerDetail.setSchedulerPath(PathUtils.getBasePath());
        schedulerDetail.setShutdownImmediate(this.schedulerConfiguration.isShutdownImmediate());
        schedulerDetail.setThreadPool(this.schedulerConfiguration.getThreadPool());
        schedulerDetail.setConsole(this.schedulerConfiguration.getConsole());
        schedulerDetail.setSource(this.schedulerConfiguration.getSource());
        schedulerDetail.setRemote(this.schedulerConfiguration.getRemote());
        schedulerDetail.setCurrentSchedulerState(this.scheduler.getContext().get(VariableKeys.SCHEDULER_STATE).toString());

        List<JobBean> jobs = jobConfigurationFactory.getConfigurationSource().load();
        for (JobBean job : jobs) {
            JobKey jobKey = new JobKey(job.getId(), job.getGroup());
            List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
            if (triggers.size() <= 0) {
                continue;
            }
            Trigger trigger = triggers.get(0);
            job.setTriggerState(scheduler.getTriggerState(trigger.getKey()).toString());

            Date startTime = trigger.getStartTime();
            if (startTime != null) {
                job.setStartTime(DateUtils.asLocalDateTime(startTime));
            }

            Date previousFireTime = trigger.getPreviousFireTime();
            if (previousFireTime != null) {
                job.setPreTime(DateUtils.asLocalDateTime(previousFireTime));
            }

            Date nextFireTime = trigger.getNextFireTime();
            if (nextFireTime != null) {
                job.setNextTime(DateUtils.asLocalDateTime(nextFireTime));
            }
        }
        schedulerDetail.setJobs(jobs);
        return new JsonResult(schedulerDetail);
    }

    @PutMapping("/control")
    public ApiResult controlScheduler(@RequestBody ControlInfo data) throws SchedulerControlException {
        if (data == null) {
            throw new SchedulerControlException("控制参数无效");
        }
        if (data.getJobId() == null || data.getGroup() == null || data.getConsoleKey() == null || data.getOpCode() == null) {
            return throwException("必要的参数，控制信息必须包括：jobId、group、consoleKey、opCode。");
        }

        JsonResult controlResult = new JsonResult();
        String opText = data.getOpCode() == 0 ? "停止" : "启动";

        try {
            String consoleKey = schedulerConfiguration.getConsole().getKey();
            if (!data.getConsoleKey().equals(consoleKey)) {
                return throwException("不合法的参数，consoleKey不匹配。");
            }

            Integer opCode = data.getOpCode();
            JobKey jobKey = new JobKey(data.getJobId(), data.getGroup());
            List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
            if (scheduler.checkExists(jobKey) && triggers != null && triggers.size() > 0) {
                Trigger trigger = triggers.get(0);
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

                if (opCode == 0 && triggerState == Trigger.TriggerState.NORMAL) {
                    scheduler.pauseJob(jobKey);
                }

                if (opCode == 1 && triggerState == Trigger.TriggerState.PAUSED) {
                    scheduler.resumeJob(jobKey);
                    scheduler.triggerJob(jobKey);
                }
            }
            return controlResult;
        } catch (Exception ex) {
            return throwException("controlScheduler发生异常。", ex);
        }
    }


    private ErrorResult throwException(String message) {
        return throwException(message, null);
    }

    private ErrorResult throwException(String message, Exception ex) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        assert response != null;
        ErrorMessageContent content = new ErrorMessageContent(request, response);
        content.setMessage(message);
        if (ex != null) {
            content.setStack(ExceptionUtils.getStackTrace(ex));
        }
        return new ErrorResult(content);
    }
}
