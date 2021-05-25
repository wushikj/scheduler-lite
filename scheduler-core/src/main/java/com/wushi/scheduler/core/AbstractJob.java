package com.wushi.scheduler.core;

import com.wushi.scheduler.common.utitls.ExceptionUtils;
import com.wushi.scheduler.common.entity.JobBean;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author yulianghua
 * @date 2019/12/31 11:30 AM
 * @description
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class AbstractJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(AbstractJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        ExecuteContext context = new ExecuteContext(jobExecutionContext);
        MDC.put("traceId", context.getCurrentTraceId());
        MDC.put("jobId", context.getCurrentJobId());

        JobBean jobBean = context.getCurrentJobBean();
        JobKey jobKey = context.getCurrentJobKey();
        TriggerKey jobTriggerKey = context.getCurrentJobTriggerKey();
        Scheduler scheduler = context.getScheduler();
        try {
            executeBefore(context);
            executeJob(context);
            executeAfter(context);
        } catch (Exception exception) {
            if (jobBean.isInterrupt()) {
                try {
                    logger.error("任务执行出现异常，已停止(job.interrupt=true)", exception);
                    scheduler.pauseJob(jobKey);
                    scheduler.pauseTrigger(jobTriggerKey);
                } catch (SchedulerException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            } else if (jobBean.isRetry()) {
                try {
                    scheduler.pauseJob(jobKey);
                    scheduler.pauseTrigger(jobTriggerKey);

                    JobRetryer jobRetryer = new JobRetryer();
                    jobRetryer.retry(context, scheduler, jobBean, exception, this::afterRetry);
                } catch (SchedulerException e) {
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
            } else {
                exceptionHandler(context, scheduler, jobBean, exception);
            }
        }
    }

    /**
     * 任务执行前回调
     *
     * @param context 执行上下文
     */
    public void executeBefore(ExecuteContext context) {
    }

    /**
     * 任务执行方法
     *
     * @param context 执行下上文
     * @throws JobExecutionException 任务执行异常
     */
    public abstract void executeJob(ExecuteContext context) throws JobExecutionException;

    /**
     * 任务执行后回调
     *
     * @param context 执行上下文
     */
    public void executeAfter(ExecuteContext context) {
    }

    /**
     * 自定义的异常信息处理，用户可重写此方法
     *
     * @param context   执行上下文
     * @param scheduler 任务执行器
     * @param job       当前任务的基本信息
     * @param exception 捕获的异常
     * @author yulianghua
     * @date 2020/1/6 5:13 PM
     * @description
     */
    public void exceptionHandler(ExecuteContext context, Scheduler scheduler, JobBean job, Exception exception) {
        logger.error("使用默认的异常处理器exceptionHandler: " + ExceptionUtils.getStackTrace(exception));
    }

    /**
     * 异常重试后的回调
     *
     * @param context           执行上下文
     * @param scheduler         任务执行器
     * @param jobBean           当前Job的信息
     * @param exception         捕获的异常
     * @param maxRetryCount     最大重试次数
     * @param currentRetryCount 当前重试次数
     */
    public void afterRetry(ExecuteContext context, Scheduler scheduler, JobBean jobBean, Exception exception,
                           int maxRetryCount, int currentRetryCount) {
    }
}
