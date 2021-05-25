package com.wushi.scheduler.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author yulianghua
 * @date 2021/4/1 4:50 PM
 * @description {
 * "customer_id": "12342",
 * "project_id": "123",
 * "target_id": "192.168.1.1",
 * "host": "192.168.1.1",
 * "port": 9001
 * }
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class SchedulerMixConfig {
    private String customerId;
    private String projectId;
    private String targetIp;
    private String host;
    private int port;
}
