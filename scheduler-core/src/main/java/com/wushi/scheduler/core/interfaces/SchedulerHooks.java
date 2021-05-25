package com.wushi.scheduler.core.interfaces;

import org.quartz.SchedulerException;

/**
 * 任务调度程序回调集合
 *
 * @author yulianghua
 * @date 2020/6/23 11:28 AM
 * @description
 */
public interface SchedulerHooks {

    /**
     * 调度器开始关闭回调
     *
     * @author yulianghua
     * @date 2020/6/23 1:55 PM
     * @description
     */
    default void shuttingdown() {

    }

    /**
     * 调度器关闭后回调
     *
     * @author yulianghua
     * @date 2020/6/23 1:53 PM
     * @description
     */
    default void shutdown() {

    }

    /**
     * 调度器发生错误时回调
     *
     * @param message
     * @param schedulerException
     * @author yulianghua
     * @date 2021/5/24 2:50 PM
     * @description
     */
    default void error(String message, SchedulerException schedulerException) {

    }
}
