package com.nutcracker.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Redis分布式锁注解
 *
 * @author 胡桃夹子
 * @date 2022-12-03 17:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonLock {

    /**
     * 空间
     */
    String cacheName() default "";

    /**
     * 分布式锁key
     */
    String key() default "";

    /**
     * 获取锁等待时间（默认2000毫秒，还没获取到锁即放弃）
     */
    long waitTime() default 2000;

    /**
     * 锁的过期时间，默认60000毫秒，超时自动失效
     * 60000毫秒=60秒=1分钟
     * 120000毫秒=120秒=2分钟
     * 300000毫秒=300秒=5分钟
     * 600000毫秒=600秒=10分钟
     * 900000毫秒=900秒=15分钟
     * 1200000毫秒=1200秒=20分钟
     */
    long expire() default 60000;
}