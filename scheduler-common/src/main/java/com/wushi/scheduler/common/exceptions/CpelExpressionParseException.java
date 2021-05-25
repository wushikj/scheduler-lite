package com.wushi.scheduler.common.exceptions;

/**
 * @author yulianghua
 * @date 2020/1/8 10:40 AM
 * @description
 */
public class CpelExpressionParseException extends Exception {
    private final String inputExpression;

    public CpelExpressionParseException(String message, String expression) {
        super(message);
        this.inputExpression = expression;
    }
}
