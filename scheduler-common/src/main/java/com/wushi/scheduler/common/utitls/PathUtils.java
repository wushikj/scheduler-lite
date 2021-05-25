package com.wushi.scheduler.common.utitls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author yulianghua
 * @date 2020/1/15 3:06 PM
 * @description
 */
public class PathUtils {
    private static final String FLAG_RUN_WITH_JAR = "BOOT-INF";
    private static final String FLAG_RUN_WITH_TOMCAT = "WEB-INF";
    private static final String FLAG_RUN_WITH_IDEA = "classes";

    public static String getBasePath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        try {
            //在java中获取文件路径的时候，有时候会获取到空格，但是在中文编码环境下，空格会变成“%20”从而使得路径错误.
            path = URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (path.contains(FLAG_RUN_WITH_JAR)) {
            //内置Tomcat
            path = System.getProperty("user.dir");
        } else if (path.contains(FLAG_RUN_WITH_TOMCAT)) {
            //外置Tomcat
            path = new File(path).getParentFile().getParentFile().getPath();
        } else if (path.contains(FLAG_RUN_WITH_IDEA)) {
            // IDEA开发模式
            path = new File(path).getPath();
        } else {
            // 外置部署可执行jar包
            path = new File(path).getParentFile().getPath();
        }
        return path;
    }
}
