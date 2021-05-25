package com.wushi.scheduler.api.autoconfigure;

import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yulianghua
 * @date 2021/3/24 6:04 PM
 * @description
 */
@Configuration
public class TomcatConfiguration {
    @Autowired
    private SchedulerConfiguration schedulerConfiguration;

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                factory.setPort(schedulerConfiguration.getConsole().getPort());
            }
        };
    }
}