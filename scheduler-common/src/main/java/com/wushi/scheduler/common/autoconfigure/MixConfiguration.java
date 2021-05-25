package com.wushi.scheduler.common.autoconfigure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yulianghua
 * @date 2021/3/24 2:13 PM
 * @description
 */
@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "wushi.mix")
public class MixConfiguration {
    private String customerId;
    private String projectId;
    private String mixEndPoint = "http://mix.wushiai.com";
    private String mixEndPointKey;
    private String env = "dev";
    private int timeout = 5000;
}
