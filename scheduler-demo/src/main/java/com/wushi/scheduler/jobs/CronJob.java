package com.wushi.scheduler.jobs;

import com.wushi.scheduler.core.AbstractJob;
import com.wushi.scheduler.core.ExecuteContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yulianghua
 * @date 2020/1/17 11:30 AM
 * @description
 */
public class CronJob extends AbstractJob {

    private static final Logger logger = LoggerFactory.getLogger(TestJob.class);

    @Override
    public void executeJob(ExecuteContext context) throws JobExecutionException {
        logger.info("hello scheduler");
    }
}
