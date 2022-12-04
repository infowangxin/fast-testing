package com.nutcracker.lock.aop;

import com.nutcracker.exception.BusinessException;
import com.nutcracker.lock.annotation.ZookeeperLock;
import com.nutcracker.lock.util.SpringELUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * XXX
 *
 * @author wangxin65
 * @date 2022-12-04 20:05
 */
@Aspect
@Component
public class ZookeeperLockAspect {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperLockAspect.class);

    @Resource
    private CuratorFramework curatorFramework;

    private static final String KEY_PREFIX = "DISTRIBUTED_LOCK_";

    private static final String KEY_SEPARATOR = "/";


    /**
     * 环绕操作
     * 注意：使用切面分布式锁的方法，只能使用包装类型，不能使用基本(byte/short/int/long/float/double/char/boolean)，否则会报空指针异常或类型转换异常
     *
     * @param proceedingJoinPoint 切入点
     * @param distributedLock     锁参数
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, ZookeeperLock distributedLock) throws Throwable {
        String lockPath = distributedLock.lockPath();

        StringBuffer fullLockPath = new StringBuffer();
        try {
            Signature signature = proceedingJoinPoint.getSignature();
            String methodName = signature.getName();
            Object[] objects = proceedingJoinPoint.getArgs();
            Class[] parameterTypes = new Class[objects.length];
            //打印输入的参数
            for (int i = 0; i < objects.length; i++) {
                Object obj = objects[i];
                parameterTypes[i] = obj.getClass();
            }
            Method method = signature.getDeclaringType().getMethod(methodName, parameterTypes);
            Parameter[] parameters = method.getParameters();

            if (StringUtils.isNotBlank(lockPath)) {
                //利用spring el表达式转换key
                Map<String, Object> variables = new HashMap<>();
                if (null != parameters && parameters.length > 0) {
                    for (int i = 0; i < parameters.length; i++) {
                        variables.put(parameters[i].getName(), objects[i]);
                    }
                }
                fullLockPath.append(SpringELUtil.parse(lockPath, variables, String.class));
            }
            //else {
            //    fullkey.append(keyGenerator.generate(proceedingJoinPoint.getTarget(), method, objects).toString());
            //}
        } catch (Exception e) {
            throw new BusinessException("系统异常", e);
        }

        if (StringUtils.isBlank(fullLockPath)) {
            throw new RuntimeException("分布式锁键不能为空");
        }
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, fullLockPath.toString());
        try {
            // 假设上锁成功，以后拿到的都是 false
            if (lock.acquire(distributedLock.expire(), distributedLock.timeUnit())) {
                return proceedingJoinPoint.proceed();
            } else {
                throw new RuntimeException("请勿重复提交");
            }
        } finally {
            lock.release();
        }
    }

}
