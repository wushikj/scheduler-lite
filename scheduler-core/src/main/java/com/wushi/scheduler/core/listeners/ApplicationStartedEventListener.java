package com.wushi.scheduler.core.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * @author yulianghua
 * @date 2019/12/27 5:20 PM
 * @description
 */
@Component
class ApplicationStartedEventListener implements ApplicationListener<ContextStartedEvent> {
    @Override
    public void onApplicationEvent(ContextStartedEvent contextRefreshedEvent) {
        System.out.println(contextRefreshedEvent);
        System.out.println("ContextStartedEvent............................");
    }
}
