package com.wushi.scheduler.common.expressions;

import com.wushi.scheduler.common.exceptions.CpelExpressionParseException;
import com.wushi.scheduler.common.interfaces.ConfigExpression;

/**
 * CEPL表达工抽象实现
 *
 * @author yulianghua
 * @date 2020/3/25 9:57 AM
 * @description
 */
public abstract class AbstractConfigExpression implements ConfigExpression {
    private String rawExpression;
    private String type;

    /**
     * 解析CEPL表达式
     *
     * @param expression 表达式
     * @return String[]
     * @author yulianghua
     * @date 2020/3/25 9:58 AM
     * @description
     */
    protected String[] parseTokens(String expression) throws CpelExpressionParseException {
        String splitChar = "#";
        if (expression == null || expression.length() == 0 || !expression.contains(splitChar)) {
            throw new CpelExpressionParseException("配置参数表达式无效。", expression);
        }

        this.rawExpression = expression;
        String cleanExpression = expression.trim();
        String[] tokens = cleanExpression.split(splitChar);
        this.type = tokens[0];
        return tokens;
    }

    /**
     * 获取表达式类型
     *
     * @return String
     * @author yulianghua
     * @date 2020/3/25 9:59 AM
     * @description
     */
    @Override
    public String getType() {
        return type.toUpperCase();
    }

    /**
     * 获取原始表达式
     *
     * @return String
     * @author yulianghua
     * @date 2020/3/25 9:59 AM
     * @description
     */
    public String getRawExpression() {
        return rawExpression;
    }
}

