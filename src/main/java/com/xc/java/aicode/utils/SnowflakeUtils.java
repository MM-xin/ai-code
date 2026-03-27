package com.xc.java.aicode.utils;

import cn.hutool.core.lang.Snowflake;

/**
 * 雪花ID工具类（单例）
 */
public class SnowflakeUtils {

    /**
     * 雪花算法实例，workerId=1，datacenterId=1
     */
    private static final Snowflake SNOWFLAKE = new Snowflake(1, 1);

    private SnowflakeUtils() {
    }

    /**
     * 生成下一个雪花ID
     *
     * @return 雪花ID
     */
    public static long nextId() {
        return SNOWFLAKE.nextId();
    }
}
