package com.wushi.scheduler.jobs;

import com.wushi.scheduler.common.constants.VariableKeys;
import com.wushi.scheduler.core.AbstractJob;
import com.wushi.scheduler.core.ExecuteContext;
import com.wushi.scheduler.core.entity.FileChangeDetail;
import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.core.enums.SchedulerState;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;



public class TestJob extends AbstractJob {

    private static final Logger logger = LoggerFactory.getLogger(TestJob.class);


    @Override
    public void executeJob(ExecuteContext context) throws JobExecutionException {

        Map<String, String> aa = context.getActiveDataByPrefix("wushi");
        String key = context.getActiveData("wushi.scheduler.config.remote.key", String.class);
        context.setActiveData("wushi.scheduler.config.remote.key", "232", String.class);
        key = context.getActiveData("wushi.scheduler.config.remote.key", String.class);
        int count = context.getActiveData("wushi.scheduler.config.remote.count", int.class);
        int count1 = context.getActiveData("wushi.scheduler.config.remote.count", Integer.class);
        Integer count2 = context.getActiveData("wushi.scheduler.config.remote.count", Integer.class);
        Integer count3 = context.getActiveData("wushi.scheduler.config.remote.count", int.class);
        boolean flag = context.getActiveData("wushi.scheduler.config.remote.flag", boolean.class);
        aa = context.getActiveDataByPrefix("wushi");

        LocalDateTime time = context.getActiveData("TestJob2.data.time", LocalDateTime.class);


        FileChangeDetail detail = context.getFileChangeDetail();
        //此处为业务逻辑代码...
        Integer i = context.getStaticData("i", Integer.class);
        String a = context.getCustomData("a", String.class);
        LocalDateTime d = context.getCustomData("d", LocalDateTime.class);
        boolean b = context.getStaticData("b", boolean.class);
        List<String> list = context.getCustomData("list", List.class);
        List<Integer> list1 = context.getCustomData("list1", List.class);
        List<BigDecimal> list2 = context.getCustomData("list2", List.class);
        Pojo pojo = context.getCustomData("pojo", Pojo.class);
        logger.info("run");
        int iss = 1 / 0;
        while (true) {
            try {
                if (context.getSchedulerState() == SchedulerState.STOPPING) {
                    logger.info(context.getSchedulerState().toString());
                    break;
                }
                logger.info(context.getScheduler().getContext().get(VariableKeys.SCHEDULER_STATE).toString());
                Thread.sleep(1000);
            } catch (SchedulerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionHandler(ExecuteContext context, Scheduler scheduler, JobBean job, Exception exception) {
        logger.warn("无异常处理策略，继续执行...");
        //scheduler.pauseJob(context.getJobDetail().getKey());
    }

    @Override
    public void afterRetry(ExecuteContext context, Scheduler scheduler, JobBean jobBean, Exception exception, int maxRetryCount, int currentRetryCount) {
        //super.afterRetry(context, scheduler, jobBean, exception, maxRetryCount, currentRetryCount);
        logger.info("重试后调用" + currentRetryCount + "/" + maxRetryCount);
    }
}

