package com.wushi.scheduler.common.entity;

import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;

import java.util.List;

/**
 * 服务信息
 *
 * @author yulianghua
 * @date 2020/9/15 11:58 AM
 * @description
 */
public class SchedulerDetail {
    /**
     * 调度服务名称
     */
    private String name;

    /// <summary>
    /// 服务描述
    /// </summary>
    private String description;

    /// <summary>
    /// 服务描述
    /// </summary>
    private String displayName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSchedulerPath() {
        return schedulerPath;
    }

    public void setSchedulerPath(String schedulerPath) {
        this.schedulerPath = schedulerPath;
    }

    public boolean isShutdownImmediate() {
        return shutdownImmediate;
    }

    public void setShutdownImmediate(boolean shutdownImmediate) {
        this.shutdownImmediate = shutdownImmediate;
    }

    public String getCurrentSchedulerState() {
        return currentSchedulerState;
    }

    public void setCurrentSchedulerState(String currentSchedulerState) {
        this.currentSchedulerState = currentSchedulerState;
    }

    public boolean isRefreshJobConfig() {
        return refreshJobConfig;
    }

    public void setRefreshJobConfig(boolean refreshJobConfig) {
        this.refreshJobConfig = refreshJobConfig;
    }

    /// <summary>
    /// 服务所在路径
    /// </summary>
    private String schedulerPath;

    /// <summary>
    /// 是否立即停止服务，不等任务执行完成，默认为false
    /// </summary>
    private boolean shutdownImmediate;

    public SchedulerConfiguration.ThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(SchedulerConfiguration.ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    /// <summary>
    /// 线程配置
    /// </summary>
    private SchedulerConfiguration.ThreadPool threadPool;

    public SchedulerConfiguration.Console getConsole() {
        return console;
    }

    public void setConsole(SchedulerConfiguration.Console console) {
        this.console = console;
    }

    /**
     * 控制端口配置信息
     */
    private SchedulerConfiguration.Console console;

    private String currentSchedulerState;

    private boolean refreshJobConfig;

    public SchedulerConfiguration.Source getSource() {
        return source;
    }

    public void setSource(SchedulerConfiguration.Source source) {
        this.source = source;
    }

    private SchedulerConfiguration.Source source;

    public SchedulerConfiguration.Remote getRemote() {
        return remote;
    }

    public void setRemote(SchedulerConfiguration.Remote remote) {
        this.remote = remote;
    }

    private SchedulerConfiguration.Remote remote;

    public List<JobBean> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobBean> jobs) {
        this.jobs = jobs;
    }

    private List<JobBean> jobs;
}
