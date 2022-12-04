package com.nutcracker.service.lock.impl;

import com.nutcracker.lock.annotation.RedissonLock;
import com.nutcracker.service.lock.LockDemoService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
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

    @Resource
    private CuratorFramework curatorFramework;

    @Override
    public Boolean redisLockDemo(String key) {
        log.info("#　redisLockDemo　==> key={}", key);
        RLock lock = redissonClient.getLock("secret-util-lock:" + key);
        boolean locked = false;
        Boolean resp = Boolean.FALSE;
        try {
            // 尝试加锁，最多等待0秒，上锁以后120秒自动解锁
            locked = lock.tryLock(0, 120, TimeUnit.SECONDS);
            if (locked) {
                log.info("# lock success");
                log.info("# business begin");
                Thread.sleep(1000 * 5);
                resp = Boolean.TRUE;
                log.info("# business end");
            }
        } catch (Exception e) {
            log.error("# error, {}", e.getLocalizedMessage());
        } finally {
            if (locked) {
                lock.unlock();
                log.info("# unlock success");
            }
        }
        log.info("#　redisLockDemo　<== key={},resp={}", key, resp);
        return resp;
    }

    //@RedisLock(waitTime = 0, expire = 120000)
    //@RedisLock(cacheName = "secret-util-lock", waitTime = 0, expire = 120000)
    @RedissonLock(cacheName = "secret-util-lock", key = "'key-'+#key", waitTime = 0, expire = 120000)
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

    @RedissonLock(cacheName = "secret-util-lock", key = "'no-'+#key", waitTime = 0, expire = 120000)
    @Override
    public void redisLockNoResultByAop(String key) {
        redisLockByAop(key);
    }

    @Override
    public Boolean zookeeperLockDemo(String key) {
        log.info("#　zookeeperLockDemo　==> key={}", key);
        String lockKey = "/secret-util-lock-" + key;
        log.debug("# lockKey={}", lockKey);
        // 可重入共性锁
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, lockKey);
        boolean locked = false;
        Boolean resp = Boolean.FALSE;
        try {
            // 尝试获取锁，最长等待3s，超时放弃获取
            locked = mutex.acquire(3, TimeUnit.SECONDS);
            if (locked) {
                log.info("# lock success");
                log.info("# business begin");
                Thread.sleep(1000 * 5);
                resp = Boolean.TRUE;
                log.info("# business end");
            }
        } catch (Exception e) {
            log.error("# error, {}", e.getLocalizedMessage());
        } finally {
            if (locked) {
                try {
                    mutex.release();
                } catch (Exception e) {
                    log.info("# unlock fail");
                }
                log.info("# unlock success");
            }
        }
        log.info("#　zookeeperLockDemo　<== key={},resp={}", key, resp);
        return resp;
    }
}
