package com.wushi.scheduler.hooks;

import com.wushi.scheduler.core.interfaces.SchedulerHooks;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author yulianghua
 * @date 2020/6/23 11:28 AM
 * @description
 */
@Primary
@Component
public class SchedulerShuttingdownImpl implements  SchedulerHooks {
    @Override
    public void shuttingdown() {
        System.out.println("shuttingdown....");
    }

    @Override
    public void shutdown() {
        System.out.println("shutdown....");

    }
}
