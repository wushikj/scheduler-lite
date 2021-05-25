package com.wushi.scheduler.core.utitls;

import com.wushi.scheduler.common.entity.JobBean;
import com.wushi.scheduler.common.exceptions.JobIntervalCastException;

/**
 * @author yulianghua
 * @date 2020/2/6 10:37 AM
 * @description
 */
public class IntervalUtils {
    private final static String DAY = "d";
    private final static String HOUR = "h";
    private final static String MINUTE = "m";
    private final static String SECOND = "s";
    private final static String EMPTY = "";
    private final  static int DEFAULT_SECOND =5;

    /**
     * @param jobBean
     * @param input
     * @return 秒数
     * @author yulianghua
     * @date 2021/5/24 2:16 PM
     * @description
     */
    public static int getInterval(JobBean jobBean, String input) throws JobIntervalCastException {
        int result = DEFAULT_SECOND;
        String interval = input.toLowerCase();
        try {
            if (interval.endsWith(DAY)) {
                int day = Integer.parseInt(interval.replace(DAY, EMPTY));
                result = day * 24 * 60 * 60;
            } else if (interval.endsWith(HOUR)) {
                int hour = Integer.parseInt(interval.replace(HOUR, EMPTY));
                result = hour * 60 * 60;
            } else if (interval.endsWith(MINUTE)) {
                int minute = Integer.parseInt(interval.replace(MINUTE, EMPTY));
                result = minute * 60;
            } else if (interval.endsWith(SECOND)) {
                result = Integer.parseInt(interval.replace(SECOND, EMPTY));
            } else {
                result = Integer.parseInt(interval);
            }

            return result;
        } catch (Exception ex) {
            throw new JobIntervalCastException("任务" + jobBean.getId() + "的interval配置不正确，请检查。", interval);
        }
    }
}
