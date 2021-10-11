package com.hd.permissiondetect.util;

/**
 * @Description: 时间定义
 * @Author: 作者名
 * @CreateDate: 2019/11/4 0004 10:27
 */
public class TimeConst {
    private TimeConst() {
    }

    // 毫秒
    public static final int MILLION_SECOND = 1;
    // 秒
    public static final int SECOND = 1000 * MILLION_SECOND;
    // 分钟
    public static final int MINUTER = 60 * SECOND;
    // 小时
    public static final int HOUR = 60 * MINUTER;
    // 天
    public static final int DAY = 24 * HOUR;
    // 秒每毫秒
    public static final double MILL_SEC_PER_SEC = 1000d;


    /**
     * 毫秒转秒
     *
     * @param millionSecond 毫秒
     * @return 秒
     */
    public static long millionSecondToSecond(long millionSecond) {
        return (long) (millionSecond / SECOND);
    }

    /**
     * 秒转毫秒
     *
     * @param second 秒
     * @return 毫秒
     */
    public static long secondToMillionSecond(long second) {
        return second * SECOND;
    }

    public static long secondToMillionSecond(float second) {
        return (long) second * SECOND;
    }

    public static long secondToMillionSecond(double second) {
        return (long) second * SECOND;
    }
}
