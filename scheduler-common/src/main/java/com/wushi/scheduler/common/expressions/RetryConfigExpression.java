package com.wushi.scheduler.common.expressions;

import com.wushi.scheduler.common.exceptions.CpelExpressionParseException;

/**
 * @author yulianghua
 * @date 2020/2/3 11:33 AM
 * @description
 */
public class RetryConfigExpression extends AbstractConfigExpression {
    private int retryCount;
    private String retryInterval;

    public RetryConfigExpression(String expression) throws CpelExpressionParseException {
        String[] tokens = parseTokens(expression);
        retryCount = Integer.parseInt(tokens[1]);
        retryInterval = tokens[2];
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public String getRetryInterval() {
        return this.retryInterval;
    }
}
