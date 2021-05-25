package com.wushi.scheduler.common.exceptions;

/**
 * @author yulianghua
 * @date 2020/1/8 10:40 AM
 * @description
 */
public class JobIntervalCastException extends Exception {
    private final String rawInterval;

    public JobIntervalCastException(String message, String interval) {
        super(message);
        this.rawInterval = interval;
    }

    /**
     * 获取原始执行间隔配置
     *
     * @return 获取原始的执行间隔（带单位）
     * @author yulianghua
     * @date 2020/9/15 11:06 AM
     * @description
     */
    public String getRawInterval() {
        return rawInterval;
    }
}
