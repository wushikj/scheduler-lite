package com.wushi.scheduler.api.entity;

/**
 * @author yulianghua
 * @date 2020/9/16 5:59 PM
 * @description
 */
public class ControlInfo {

    public Integer getOpCode() {
        return opCode;
    }

    public void setOpCode(Integer opCode) {
        this.opCode = opCode;
    }

    public String getConsoleKey() {
        return consoleKey;
    }

    public void setConsoleKey(String consoleKey) {
        this.consoleKey = consoleKey;
    }

    /**
     * 操作码
     */
    private Integer opCode;

    /**
     * 控制验证码
     */
    private String consoleKey;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    private String jobId;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private String group = "default";
}
