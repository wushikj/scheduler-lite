package com.wushi.scheduler.common.entity;

import com.wushi.scheduler.common.expressions.RetryConfigExpression;
import com.wushi.scheduler.common.enums.ExceptionStrategy;
import com.wushi.scheduler.common.enums.TriggerType;
import com.wushi.scheduler.common.exceptions.CpelExpressionParseException;
import com.wushi.scheduler.common.expressions.SimpleConfigExpression;
import de.beosign.snakeyamlanno.property.YamlProperty;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author yulianghua
 * @date 2019/12/30 4:50 PM
 * @description
 */
public class JobBean {
    public LocalDateTime getPreTime() {
        return preTime;
    }

    public void setPreTime(LocalDateTime preTime) {
        this.preTime = preTime;
    }

    public LocalDateTime getNextTime() {
        return nextTime;
    }

    public void setNextTime(LocalDateTime nextTime) {
        this.nextTime = nextTime;
    }

    public LocalDateTime preTime;
    public LocalDateTime nextTime;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    private LocalDateTime startTime;
    private String id;
    private String desc;
    private String className;
    private String group = "default";
    private boolean enabled;
    private TriggerType triggerType = TriggerType.INTERVAL;
    private String triggerConfig;
    private ExceptionStrategy exceptionStrategy;
    private String exceptionConfig;
    private String interval = "5s";
    private String cron;
    private String targetFile;
    private boolean immediate = true;
    private boolean interrupt = false;
    private boolean retry = false;
    private int retryCount = 3;
    private String retryInterval = "1m";
    private Map<String, Object> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @YamlProperty(key = "class")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isRetry() {
        return retry;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public String getRetryInterval() {
        return retryInterval;
    }

    public boolean isInterrupt() {
        return interrupt;
    }

    @YamlProperty(key = "trigger")
    public String getTriggerConfig() {
        return triggerConfig;
    }

    public void setTriggerConfig(String triggerConfig) throws CpelExpressionParseException {
        this.triggerConfig = triggerConfig;
        SimpleConfigExpression<String> configExpression = new SimpleConfigExpression<>(triggerConfig);
        this.triggerType = TriggerType.valueOf(configExpression.getType());
        if (triggerType == TriggerType.INTERVAL) {
            this.interval = configExpression.getValue(String.class);
        } else if (triggerType == TriggerType.CRON) {
            this.cron = configExpression.getValue(String.class);
        } else if (triggerType == TriggerType.FILE) {
            this.targetFile = configExpression.getValue(String.class);
        }
    }

    @YamlProperty(key = "exception")
    public String getExceptionConfig() {
        return exceptionConfig;
    }

    public void setExceptionConfig(String exceptionConfig) throws CpelExpressionParseException {
        this.exceptionConfig = exceptionConfig;
        if (exceptionConfig != null && !"".equals(exceptionConfig)) {
            String exceptionConfigCase = exceptionConfig.toLowerCase();
            if (exceptionConfigCase.startsWith("retry")) {
                RetryConfigExpression expression = new RetryConfigExpression(exceptionConfigCase);
                this.exceptionStrategy = ExceptionStrategy.valueOf(expression.getType());
                this.retry = true;
                this.retryCount = expression.getRetryCount();
                this.retryInterval = expression.getRetryInterval();
            } else if (exceptionConfigCase.startsWith("interrupt")) {
                SimpleConfigExpression<Boolean> expression = new SimpleConfigExpression<>(exceptionConfigCase);
                this.exceptionStrategy = ExceptionStrategy.valueOf(expression.getType());
                this.interrupt = expression.getValue(Boolean.class);
            }
        }
    }

    public String getInterval() {
        return interval;
    }

    public String getCron() {
        return cron;
    }

    public String getTargetFile() {
        return targetFile;
    }

    @Override
    public String toString() {
        return "JobBean{" + "id=" + id + ", className=" + className + ", enabled=" + enabled + "}";
    }

    public ExceptionStrategy getExceptionStrategy() {
        return exceptionStrategy;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    private String triggerState;

    public boolean isHasException() {
        return hasException;
    }

    public void setHasException(boolean hasException) {
        this.hasException = hasException;
    }

    public String getExceptionMessage() {
        return ExceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        ExceptionMessage = exceptionMessage;
    }

    private  boolean hasException;
    private  String ExceptionMessage;
}
