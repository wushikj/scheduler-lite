package com.wushi.scheduler.core;

import com.wushi.scheduler.core.interfaces.RetryAfterCallback;
import com.wushi.scheduler.common.constants.VariableKeys;
import com.wushi.scheduler.common.utitls.ExceptionUtils;
import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.exceptions.JobIntervalCastException;
import com.wushi.scheduler.core.utitls.IntervalUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 任务重试器
 *
 * @author yulianghua
 * @date 2020/1/18 11:28 AM
 * @description
 */
class JobRetryer {

    private static final Logger logger = LoggerFactory.getLogger(JobRetryer.class);
    private static final List<JobKey> JOB_KEYS = new ArrayList<>();
    private static final List<TriggerKey> JOB_TRIGGER_KEYS = new ArrayList<>();

    /**
     * @param context            执行上下文
     * @param scheduler          计划任务
     * @param jobBean            当前job信息
     * @param exception          异常信息
     * @param retryAfterCallback 重试后回调
     * @author yulianghua
     * @date 2020/3/24 5:36 PM
     * @description
     */
    protected void retry(ExecuteContext context, Scheduler scheduler, JobBean jobBean, Exception exception, RetryAfterCallback retryAfterCallback) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String retryCountKey = VariableKeys.JOB_RETRY_COUNT;
        int maxRetryCount = jobBean.getRetryCount();
        int currentRetryCount = jobDataMap.containsKey(retryCountKey) ? jobDataMap.getIntValue(retryCountKey) : 1;
        if (currentRetryCount <= maxRetryCount) {
            jobDataMap.put(retryCountKey, currentRetryCount + 1);

            JobKey identity = new JobKey(context.getJobDetail().getKey().getName() + "_" + currentRetryCount, "FailingJobsGroup");
            JobDetail job = context.getJobDetail().getJobBuilder().withIdentity(identity).usingJobData(jobDataMap).build();
            JOB_KEYS.add(identity);

            //默认为1m
            long retryInterval = 60 * 1000;
            Trigger trigger = null;
            try {
                TriggerKey triggerKey = new TriggerKey(context.getJobDetail().getKey().getName() + "_" + currentRetryCount + "_Trigger", "FailingJobsGroup");
                retryInterval = IntervalUtils.getInterval(jobBean, jobBean.getRetryInterval()) * 1000L;
                trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).forJob(job).startAt(new Date(context.getFireTime().getTime() + retryInterval)).build();
                JOB_TRIGGER_KEYS.add(triggerKey);
            } catch (JobIntervalCastException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            }

            try {
                logger.info("重试" + currentRetryCount + "(retryInterval=" + jobBean.getRetryInterval() + ")");
                context.getScheduler().scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            } finally {
                retryAfterCallback.invoke(context, scheduler, jobBean, exception, maxRetryCount, currentRetryCount);
            }
        } else {
            try {
                logger.error("任务异常重试结束：" + ExceptionUtils.getStackTrace(exception));
                scheduler.deleteJobs(JOB_KEYS);
                scheduler.unscheduleJobs(JOB_TRIGGER_KEYS);
            } catch (SchedulerException e) {
                logger.error(ExceptionUtils.getStackTrace(e));
            } finally {
                JOB_KEYS.clear();
                JOB_TRIGGER_KEYS.clear();
            }
        }
    }
}

