package com.wushi.scheduler.core;

import com.wushi.scheduler.common.constants.VariableKeys;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

@Component
public class JobFactory extends AdaptableJobFactory {
    private final AutowireCapableBeanFactory capableBeanFactory;

    public JobFactory(AutowireCapableBeanFactory capableBeanFactory) {
        this.capableBeanFactory = capableBeanFactory;
    }

    /**
     * 创建Job实例
     *
     * @param bundle {@link TriggerFiredBundle}
     * @return Object
     * @author yulianghua
     * @date 2020/3/25 8:30 AM
     * @description
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        String traceId = TraceIdGenerator.create();
        bundle.getJobDetail().getJobDataMap().put(VariableKeys.TRACE_ID, traceId);
        Object job = super.createJobInstance(bundle);
        capableBeanFactory.autowireBean(job);
        return job;
    }
}
