package com.wushi.scheduler.common.utitls;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author yulianghua
 * @date 2020/1/8 10:10 AM
 * @description
 */
public class ExceptionUtils {
    public static String getStackTrace(Exception t) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            return sw.toString();
        }
    }
}
