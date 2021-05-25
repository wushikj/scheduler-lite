package com.wushi.scheduler.core.listeners;

import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import com.wushi.scheduler.common.constants.VariableKeys;
import com.wushi.scheduler.common.utitls.ExceptionUtils;
import com.wushi.scheduler.core.enums.SchedulerState;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author yulianghua
 * @date 2019/12/27 5:20 PM
 * @description
 */
@Component
class ApplicationClosedEventListener implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationClosedEventListener.class);
    private final Scheduler scheduler;
    private  final SchedulerConfiguration schedulerConfiguration;

    public ApplicationClosedEventListener(Scheduler scheduler, SchedulerConfiguration schedulerConfiguration) {
        this.scheduler = scheduler;
        this.schedulerConfiguration = schedulerConfiguration;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        try {
            this.scheduler.getContext().put(VariableKeys.SCHEDULER_STATE, SchedulerState.STOPPING);
            this.scheduler.shutdown(this.schedulerConfiguration.isShutdownImmediate());
            logger.info("计划任务程序关闭。");
        } catch (SchedulerException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}