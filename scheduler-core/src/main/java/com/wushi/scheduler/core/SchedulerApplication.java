package com.wushi.scheduler.core;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.function.Consumer;

/**
 * @author yulianghua
 * @date 2020/2/14 3:28 PM
 * @description
 */
public class SchedulerApplication  {

    /**
     * 项目初始化方法
     *
     * @param args 参数
     * @return T
     * @author psc
     * @date 2020/1/21 8:55
     */
    public static <T> void init(String[] args, Class<T> primarySource) {
        init(args, primarySource, null);
    }

    /**
     * 项目初始化方法
     *
     * @param args      参数
     * @param afterInit 调用run方法后的回调方法，可以设置ApplicationContext，可以传null，也可以传lambda表达式如：configurableApplicationContext->{}
     * @return T
     * @author psc
     * @date 2020/1/21 8:55
     */
    public static <T> void init(String[] args, Class<T> primarySource, Consumer<ConfigurableApplicationContext> afterInit) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(primarySource, args);
        if (afterInit != null) {
            afterInit.accept(applicationContext);
        }
    }
}

