package com.nutcracker.service.lock.impl;

import com.nutcracker.annotation.RedisLock;
import com.nutcracker.service.lock.LockDemoService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁测试接口实现
 *
 * @author 胡桃夹子
 * @date 2022-12-03 14:28
 */
@Service
public class LockDemoServiceImpl implements LockDemoService {

    private static final Logger log = LoggerFactory.getLogger(LockDemoServiceImpl.class);

    @Resource
    private RedissonClient redissonClient;

    @Override
    public Boolean redisLockDemo(String key) {
        log.info("#　redisLockDemo　==> key={}", key);
        RLock lock = redissonClient.getLock("secret-util-lock:" + key);
        boolean locked;
        try {
            // 尝试加锁，最多等待0秒，上锁以后120秒自动解锁
            locked = lock.tryLock(0, 120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("# trylock fail, error msg={}", e.getLocalizedMessage());
            return Boolean.FALSE;
        }
        if (!locked) {
            log.info("# lock fail");
            return Boolean.FALSE;
        }

        log.info("# lock success");
        Boolean resp = Boolean.FALSE;
        try {
            log.info("# business begin");
            Thread.sleep(1000 * 5);
            resp = Boolean.TRUE;
            log.info("# business end");
        } catch (Exception e) {
            log.error("# error, {}", e.getLocalizedMessage());
        } finally {
            lock.unlock();
            log.info("# unlock success");
        }
        log.info("#　redisLockDemo　<== key={},resp={}", key, resp);
        return resp;
    }

    //@RedisLock(waitTime = 0, expire = 120000)
    //@RedisLock(cacheName = "secret-util-lock", waitTime = 0, expire = 120000)
    @RedisLock(cacheName = "secret-util-lock", key = "'key-'+#key", waitTime = 0, expire = 120000)
    @Override
    public Boolean redisLockByAop(String key) {
        try {
            log.info("# business begin");
            Thread.sleep(1000 * 5);
            log.info("# business end");
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("# error, {}", e.getLocalizedMessage());
            return Boolean.FALSE;
        }
    }

    @RedisLock(cacheName = "secret-util-lock", key = "'no-'+#key", waitTime = 0, expire = 120000)
    @Override
    public void redisLockNoResultByAop(String key) {
        redisLockByAop(key);
    }
}
