package com.wushi.scheduler.jobs;

import com.wushi.scheduler.core.AbstractJob;
import com.wushi.scheduler.core.ExecuteContext;
import com.wushi.scheduler.core.entity.FileChangeDetail;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yulianghua
 * @date 2020/1/16 4:12 PM
 * @description
 */
public class FileListenerJob extends AbstractJob {

    private static final Logger logger = LoggerFactory.getLogger(FileListenerJob.class);

    @Override
    public void executeJob(ExecuteContext context) throws JobExecutionException {
        FileChangeDetail fileChangeDetail = context.getFileChangeDetail();
        logger.info("文件或者目录发生变化");
    }
}
