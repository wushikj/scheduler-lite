package com.wushi.scheduler.common.expressions;

import com.wushi.scheduler.common.exceptions.CpelExpressionParseException;

public class SimpleConfigExpression<T> extends AbstractConfigExpression {
    private final String value;

    /**
     *
     * @param expression 配置表达式
     */
    public SimpleConfigExpression(String expression) throws CpelExpressionParseException {
        String[] tokens = parseTokens(expression);
        this.value = tokens[1];
    }

    public T getValue(Class<T> type) {
        if (type == Integer.class) {
            return (T) Integer.valueOf(this.value);
        }

        if (type == Boolean.class) {
            return (T) Boolean.valueOf(this.value);
        }
        return (T) this.value;
    }
}
