package com.wushi.scheduler.common.exceptions;

/**
 * @author yulianghua
 * @date 2020/2/5 5:28 PM
 * @description
 */
public class JobConfigSerializationException extends Exception {
    public JobConfigSerializationException(String message, Exception exception) {
        super(message, exception);
    }
}
