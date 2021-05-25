package com.wushi.scheduler.common.enums;

/**
 * 任务触发类型
 *
 * @author yulianghua
 * @date 2020/1/13 7:06 PM
 * @description
 */
public enum TriggerType {
    /**
     * 时间轮询触发
     */
    INTERVAL,
    /**
     * Cron表达式触发
     */
    CRON,
    /**
     * 文件或目录触发
     */
    FILE
}
