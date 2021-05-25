package com.wushi.scheduler.core.autoconfigure;

import com.wushi.scheduler.core.JobFactory;
import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;


@Configuration
public class QuartzConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    private final JobFactory jobFactory;

    final SchedulerConfiguration schedulerConfiguration;

    public QuartzConfiguration(JobFactory jobFactory, SchedulerConfiguration schedulerConfiguration) {
        this.jobFactory = jobFactory;
        this.schedulerConfiguration = schedulerConfiguration;
        printConfiguration();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);

        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(schedulerConfiguration.getThreadPool().getThreadCount()));
        properties.setProperty("org.quartz.jobStore.misfireThreshold", String.valueOf(schedulerConfiguration.getThreadPool().getMisfireThreshold()));
        schedulerFactoryBean.setQuartzProperties(properties);

        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setStartupDelay(1);
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

    private void printConfiguration() {
        logger.info("------------------------------------");
        logger.info(schedulerConfiguration.toString());
        logger.info("------------------------------------");
    }
}