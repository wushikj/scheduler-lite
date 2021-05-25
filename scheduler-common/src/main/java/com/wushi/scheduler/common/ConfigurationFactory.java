package com.wushi.scheduler.common;

import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import com.wushi.scheduler.common.jobstorages.LocalJobStorage;
import com.wushi.scheduler.common.interfaces.JobStorage;
import com.wushi.scheduler.common.jobstorages.RemoteJobStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author yulianghua
 * @date 2019/12/30 5:09 PM
 * @description
 */
@Component
public class ConfigurationFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFactory.class);
    private final SchedulerConfiguration schedulerConfiguration;

    public ConfigurationFactory(SchedulerConfiguration schedulerConfiguration) {
        this.schedulerConfiguration = schedulerConfiguration;
    }

    /**
     * 获取Job配置来源
     *
     * @return {@link JobStorage}
     * @author yulianghua
     * @date 2019/12/31 8:34 AM
     * @description
     */
    public JobStorage getConfigurationSource() {
        if (schedulerConfiguration.getSource() == SchedulerConfiguration.Source.LOCAL) {
            return new LocalJobStorage();
        }

        return new RemoteJobStorage();
    }
}
