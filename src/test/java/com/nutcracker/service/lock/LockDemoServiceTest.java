package com.nutcracker.service.lock;

import com.nutcracker.BaseTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Test
    public void redisLockDemo() {
        try {
            int count = 5;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            ExecutorService executorService = Executors.newFixedThreadPool(count);
            for (int i = 1; i <= count; i++) {
                executorService.submit(() -> {
                    boolean resp = lockDemoService.redisLockDemo("123");
                    log.debug("# resp={}\n", resp);
                    //调用countDown方法，表示该线程执行完毕
                    countDownLatch.countDown();
                });
            }
            //使该方法阻塞住
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void redisLockByAop() {
        try {
            int count = 5;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            ExecutorService executorService = Executors.newFixedThreadPool(count);
            for (int i = 1; i <= count; i++) {
                executorService.submit(() -> {
                    Boolean resp = lockDemoService.redisLockByAop("456");
                    log.debug("# resp={}\n", resp);
                    //调用countDown方法，表示该线程执行完毕
                    countDownLatch.countDown();
                });
            }
            //使该方法阻塞住
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void redisLockNoResultByAop() {
        try {
            int count = 5;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            ExecutorService executorService = Executors.newFixedThreadPool(count);
            for (int i = 1; i <= count; i++) {
                executorService.submit(() -> {
                    lockDemoService.redisLockNoResultByAop("789");
                    //调用countDown方法，表示该线程执行完毕
                    countDownLatch.countDown();
                });
            }
            //使该方法阻塞住
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void zookeeperLockDemo() {
        try {
            int count = 5;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            ExecutorService executorService = Executors.newFixedThreadPool(count);
            for (int i = 1; i <= count; i++) {
                executorService.submit(() -> {
                    Boolean resp = lockDemoService.zookeeperLockDemo("abc");
                    log.debug("# resp={}\n", resp);
                    //调用countDown方法，表示该线程执行完毕
                    countDownLatch.countDown();
                });
            }
            //使该方法阻塞住
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
