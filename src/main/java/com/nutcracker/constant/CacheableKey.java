package com.nutcracker.constant;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 缓存时长
 *
 * @author 胡桃夹子
 * @date 2020-09-21 13:12
 */
public class CacheableKey {

    /**
     * SysUser CacheManager
     */
    public static final String SYS_USER = "SysUser";

    /**
     *  1周
     */
    public static final long CACHE_1_WEEK = 604800L;
    /**
     * 1天
     */
    public static final long CACHE_1_DAY = 86400;
    /**
     * 4小时
     */
    public static final long CACHE_4_HOUR = 14400;
    /**
     * 2小时
     */
    public static final long CACHE_2_HOUR = 7200;
    /**
     * 1小时
     */
    public static final long CACHE_1_HOUR = 3600;
    /**
     * 30分钟
     */
    public static final long CACHE_30_MIN = 1800;
    /**
     * 10分钟
     */
    public static final long CACHE_10_MIN = 600;
    /**
     * 5分钟
     */
    public static final long CACHE_5_MIN = 300;
    /**
     * 1分钟
     */
    public static final long CACHE_1_MIN = 60;
    /**
     * 10秒钟
     */
    public static final long CACHE_10_SECOND = 10;
    /**
     * 5秒钟
     */
    public static final long CACHE_5_SECOND = 5;

    private static final Map<String, Long> cacheableKeyMap = new LinkedHashMap<>();

    static {
        // 设置缓存时长
        cacheableKeyMap.put(SYS_USER, CACHE_2_HOUR);
    }

    public static Map<String, Long> getCacheableKeyMap() {
        return cacheableKeyMap;
    }
}
