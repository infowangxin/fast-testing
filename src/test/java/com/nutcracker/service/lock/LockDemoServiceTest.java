package com.nutcracker.service.lock;

import com.nutcracker.BaseTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * 分布式锁测试接口
 *
 * @author 胡桃夹子
 * @date 2022-12-03 14:38
 */
public class LockDemoServiceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LockDemoServiceTest.class);

    @Resource
    private LockDemoService lockDemoService;

    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    @Test
    public void redisLockDemo() {
        try {
            for (int i = 1; i <= 5; i++) {
                taskExecutor.execute(() -> {
                    boolean resp = lockDemoService.redisLockDemo("123");
                    log.debug("# resp={}\n", resp);
                });
            }
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void redisLockByAop() {
        try {
            for (int i = 1; i <= 5; i++) {
                taskExecutor.execute(() -> {
                    Boolean resp = lockDemoService.redisLockByAop("456");
                    log.debug("# resp={}\n", resp);
                });
            }
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void redisLockNoResultByAop() {
        try {
            for (int i = 1; i <= 5; i++) {
                taskExecutor.execute(() -> {
                    lockDemoService.redisLockNoResultByAop("789");
                });
            }
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
