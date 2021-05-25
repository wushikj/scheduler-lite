package com.wushi.scheduler.core.listeners;

import com.wushi.scheduler.common.autoconfigure.MixConfiguration;
import com.wushi.scheduler.common.autoconfigure.SchedulerConfiguration;
import com.wushi.scheduler.common.constants.VariableKeys;
import com.wushi.scheduler.common.entity.SchedulerMixConfig;
import com.wushi.scheduler.common.utitls.ExceptionUtils;
import com.wushi.scheduler.common.utitls.JsonUtils;
import com.wushi.scheduler.core.enums.SchedulerState;
import com.wushi.scheduler.core.interfaces.SchedulerHooks;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

class GlobalSchedulerListener implements org.quartz.SchedulerListener {

    private static final Logger logger = LoggerFactory.getLogger(GlobalSchedulerListener.class);
    private final SchedulerHooks schedulerHooks;
    private final Scheduler scheduler;
    private final SchedulerConfiguration schedulerConfiguration;
    private final MixConfiguration mixConfiguration;

    public GlobalSchedulerListener(Scheduler scheduler, SchedulerHooks schedulerHooks, SchedulerConfiguration schedulerConfiguration, MixConfiguration mixConfiguration) {
        this.scheduler = scheduler;
        this.schedulerHooks = schedulerHooks;
        this.schedulerConfiguration = schedulerConfiguration;
        this.mixConfiguration = mixConfiguration;
    }

    @Override
    public void jobScheduled(Trigger trigger) {

    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {

    }

    @Override
    public void triggerFinalized(Trigger trigger) {

    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {

    }

    @Override
    public void triggersPaused(String s) {

    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {

    }

    @Override
    public void triggersResumed(String s) {

    }

    @Override
    public void jobAdded(JobDetail jobDetail) {

    }

    @Override
    public void jobDeleted(JobKey jobKey) {

    }

    @Override
    public void jobPaused(JobKey jobKey) {

    }

    @Override
    public void jobsPaused(String s) {

    }

    @Override
    public void jobResumed(JobKey jobKey) {

    }

    @Override
    public void jobsResumed(String s) {

    }

    @Override
    public void schedulerError(String s, SchedulerException e) {
        logger.error("调度器发生错误。" + e.getMessage() + e.getUnderlyingException().toString(), ExceptionUtils.getStackTrace(e));
        schedulerHooks.error(s, e);
    }

    @Override
    public void schedulerInStandbyMode() {

    }

    @Override
    public void schedulerStarted() {
        logger.info("启动成功");
        try {
            this.scheduler.getContext().put(VariableKeys.SCHEDULER_STATE, SchedulerState.RUNNING);
            register2Mix(mixConfiguration);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void schedulerStarting() {
        logger.info("正在启动");
    }

    @Override
    public void schedulerShutdown() {
        this.schedulerHooks.shutdown();
        logger.info("停止完成");
    }

    @Override
    public void schedulerShuttingdown() {
        this.schedulerHooks.shuttingdown();
        logger.info("正在停止");
    }

    @Override
    public void schedulingDataCleared() {

    }

    private void register2Mix(MixConfiguration mixConfiguration) {
        int timeout = mixConfiguration.getTimeout();
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
        HttpPost httpPost = new HttpPost(mixConfiguration.getMixEndPoint() + "/mix/api/v1/scheduler/config");
        httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("Accept", "application/json");

        SchedulerMixConfig config = new SchedulerMixConfig()
                .setCustomerId(mixConfiguration.getCustomerId())
                .setProjectId(mixConfiguration.getProjectId())
                .setTargetIp(null)
                .setHost(null)
                .setPort(9001);

        String json = JsonUtils.toJSONString(config);
        httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
        CloseableHttpAsyncClient httpClient = HttpAsyncClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        httpClient.start();
        httpClient.execute(httpPost, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    String responseContent = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                    System.out.println(responseContent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void failed(Exception e) {
                System.out.println(httpPost.getRequestLine() + "->" + e);
                System.out.println(" callback thread id is : " + Thread.currentThread().getId());
            }

            @Override
            public void cancelled() {
                System.out.println(httpPost.getRequestLine() + " cancelled");
                System.out.println(" callback thread id is : " + Thread.currentThread().getId());
            }
        });
    }
}
