package com.nutcracker.lock.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper分布式锁注解
 *
 * @author 胡桃夹子
 * @date 2022/12/4 16:10
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZookeeperLock {

    /**
     * 你要锁定的编码
     */
    String lockPath() default "";

    /**
     * 是否控制同时操作数
     */
    boolean isOpLimitedCount() default false;

    /**
     * 是否是永远等待锁
     */
    boolean isAlwaysWaitLock() default false;

    boolean isKeepLockPath() default false;

    /**
     * 实现类的springid
     */
    String ZookeepLockServiceSpringID() default "";

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

    /**
     * 获取锁等待时间/锁的过期时间 的 单位，默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
