package com.wushi.scheduler.jobs;

import com.wushi.scheduler.core.AbstractJob;
import com.wushi.scheduler.core.ExecuteContext;
import com.wushi.scheduler.common.entity.JobBean;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class  Pojo{
    private   int id;
    private   String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


public class TestJob2 extends AbstractJob {

    private static final Logger logger = LoggerFactory.getLogger(TestJob2.class);

    @Override
    public void executeJob(ExecuteContext context) throws JobExecutionException {

        Integer i = context.getStaticData("i", Integer.class);
        String a = context.getCustomData("a", String.class);
        LocalDateTime d = context.getCustomData("d", LocalDateTime.class);
        boolean b = context.getStaticData("b", boolean.class);
        List<String> list = context.getCustomData("list", List.class);
        List<Integer> list1 = context.getCustomData("list1", List.class);
        List<BigDecimal> list2 = context.getCustomData("list2", List.class);
        Pojo pojo = context.getCustomData("pojo", Pojo.class);
        int aa = 1 / 0;
    }


    @Override
    public void exceptionHandler(ExecuteContext context, Scheduler scheduler, JobBean job, Exception exception) {
        super.exceptionHandler(context, scheduler, job, exception);
    }
}
