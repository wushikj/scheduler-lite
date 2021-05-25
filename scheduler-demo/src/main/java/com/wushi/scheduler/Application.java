package com.wushi.scheduler;

import com.wushi.scheduler.core.SchedulerApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wushi.*"})
public class Application {
    public static void main(String[] args) {
        SchedulerApplication.init(args, Application.class, (configurableApplicationContext) -> {
        });
    }
}