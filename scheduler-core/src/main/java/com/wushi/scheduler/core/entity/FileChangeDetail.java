package com.wushi.scheduler.core.entity;

import com.wushi.scheduler.common.enums.TriggerType;

import java.nio.file.Path;

/**
 * @author yulianghua
 * @date 2020/1/16 4:55 PM
 * @description
 */
public class FileChangeDetail {
    private  String watchMonitorName;
    private FileChangeEvent fileChangeEvent;
    private Path targetPath;
    private  String with;
    private TriggerType triggerType;

    public FileChangeEvent getFileChangeEvent() {
        return fileChangeEvent;
    }

    public void setFileChangeEvent(FileChangeEvent fileChangeEvent) {
        this.fileChangeEvent = fileChangeEvent;
    }

    public Path getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(Path targetPath) {
        this.targetPath = targetPath;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public String getWatchMonitorName() {
        return watchMonitorName;
    }

    public void setWatchMonitorName(String watchMonitorName) {
        this.watchMonitorName = watchMonitorName;
    }

    public String getWith() {
        return with;
    }

    public void setWith(String with) {
        this.with = with;
    }
}
