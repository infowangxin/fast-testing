package com.nutcracker.lock.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

}
