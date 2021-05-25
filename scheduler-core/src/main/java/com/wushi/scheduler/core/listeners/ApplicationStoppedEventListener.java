package com.wushi.scheduler.core.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @author yulianghua
 * @date 2019/12/27 5:20 PM
 * @description
 */
@Component
class ApplicationStoppedEventListener implements ApplicationListener<ContextStoppedEvent> {
    @Override
    public void onApplicationEvent(ContextStoppedEvent contextRefreshedEvent) {
        System.out.println(contextRefreshedEvent);
        System.out.println("ContextStoppedEvent............................");
    }
}