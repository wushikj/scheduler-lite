package com.wushi.scheduler.api.exceptions;

/**
 * @author yulianghua
 * @date 2020/9/18 5:51 PM
 * @description
 */
public class SchedulerControlException extends Exception {

    public SchedulerControlException(String message) {
        super(message);
    }

    public SchedulerControlException(String message, Exception exception) {
        super(message, exception);
    }
}
