package com.nutcracker.lock.aop;

import com.nutcracker.exception.BusinessException;
import com.nutcracker.lock.annotation.RedissonLock;
import com.nutcracker.lock.exception.LockException;
import com.nutcracker.lock.util.SpringELUtil;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.interceptor.KeyGenerator;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * redisson distributed lock
 *
 * @author 胡桃夹子
 * @date 2022/12/3 17:15
 */
@Component
@Aspect
public class RedissonLockAspect {

    private static final Logger log = LoggerFactory.getLogger(RedissonLockAspect.class);

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private KeyGenerator keyGenerator;

    /**
     * 环绕操作
     * 注意：使用切面分布式锁的方法，只能使用包装类型，不能使用基本(byte/short/int/long/float/double/char/boolean)，否则会报空指针异常或类型转换异常
     *
     * @param proceedingJoinPoint 切换方法对象
     * @param distributedLock     锁参数
     * @return 原方法返回值
     */
    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint proceedingJoinPoint, RedissonLock distributedLock) {
        // key
        String key = distributedLock.key();
        // 缓存空间
        String cacheName = distributedLock.cacheName();
        // 最大等待时间 毫秒
        long waitTime = distributedLock.waitTime();
        // 失效时间(防止线程意外无法释放锁) 毫秒
        long expire = distributedLock.expire();

        waitTime = waitTime < 0 ? 0 : waitTime;
        expire = expire < 0 ? 0 : expire;

        StringBuffer fullLockKey = new StringBuffer();
        try {
            Signature signature = proceedingJoinPoint.getSignature();
            String methodName = signature.getName();
            Object[] objects = proceedingJoinPoint.getArgs();
            Class[] parameterTypes = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                Object obj = objects[i];
                parameterTypes[i] = obj.getClass();
            }
            Method method = signature.getDeclaringType().getMethod(methodName, parameterTypes);
            Parameter[] parameters = method.getParameters();
            if (StringUtils.isNotBlank(cacheName)) {
                fullLockKey.append(cacheName).append("::");
            }
            if (StringUtils.isNotBlank(key)) {
                //利用spring el表达式转换key
                Map<String, Object> variables = new HashMap<>();
                if (null != parameters && parameters.length > 0) {
                    for (int i = 0; i < parameters.length; i++) {
                        variables.put(parameters[i].getName(), objects[i]);
                    }
                }
                fullLockKey.append(SpringELUtil.parse(key, variables, String.class));
            } else {
                fullLockKey.append(keyGenerator.generate(proceedingJoinPoint.getTarget(), method, objects).toString());
            }
        } catch (Exception e) {
            throw new BusinessException("系统异常", e);
        }

        RLock lock = redissonClient.getLock(fullLockKey.toString());
        boolean locked;
        try {
            // 尝试加锁
            locked = lock.tryLock(waitTime, expire, distributedLock.timeUnit());
        } catch (InterruptedException e) {
            log.error("redisson加锁失败,fullkey={}, error msg={}", fullLockKey, e.getLocalizedMessage());
            throw new LockException("加锁失败", e);
        }
        if (!locked) {
            log.error("redisson加锁失败 fullkey={}", fullLockKey);
            return null;
        }
        try {
            log.debug("redisson加锁成功，fullkey={}", fullLockKey);
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error("redisson加锁后执行业务逻辑报错,fullkey={}, error msg={}", fullLockKey, e.getLocalizedMessage());
            throw new BusinessException("异常系统", e);
        } finally {
            lock.unlock();
            log.debug("redisson释放锁成功:{}", fullLockKey);
        }
    }
}
