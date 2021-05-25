package com.wushi.scheduler.common.interfaces;

/**
 * 任务调度程序回调集合
 *
 * @author yulianghua
 * @date 2020/6/23 11:28 AM
 * @description
 */
public interface SchedulerHooks {

    /**
     * 任务调度程序开始关闭回调
     *
     * @param
     * @return
     * @author yulianghua
     * @date 2020/6/23 1:55 PM
     * @description
     */
    default void shuttingdown() {

    }

    /**
     * 任务调度程序关闭后回调
     *
     * @param
     * @author yulianghua
     * @date 2020/6/23 1:53 PM
     * @description
     */
    default void shutdown() {

    }
}
