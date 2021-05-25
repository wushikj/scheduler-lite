package com.wushi.scheduler.core.interfaces;

import com.wushi.scheduler.core.ExecuteContext;
import com.wushi.scheduler.common.entity.JobBean;
import org.quartz.Scheduler;

/**
 * 重试后回调接口
 *
 * @author yulianghua
 * @date 2020/1/18 11:52 AM
 * @description
 */
@FunctionalInterface
public interface RetryAfterCallback {
    /**
     * 执行回调
     *
     * @param context           执行上下文
     * @param scheduler         任务调度器
     * @param jobBean           当前job信息
     * @param exception         异常信息
     * @param maxRetryCount     最大重试次数
     * @param currentRetryCount 当前重试次数
     */
    void invoke(ExecuteContext context, Scheduler scheduler, JobBean jobBean, Exception exception,
                int maxRetryCount, int currentRetryCount);
}
