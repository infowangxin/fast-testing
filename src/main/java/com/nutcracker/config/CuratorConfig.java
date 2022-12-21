package com.nutcracker.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Curator 配置
 *
 * @author 胡桃夹子
 * @date 2022-12-04 12:24
 */
@Slf4j
@Configuration
public class CuratorConfig {

    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework(
            @Value("${locks.zookeeper.connect-server}") String connectString,
            @Value("${locks.zookeeper.base-sleep-time-ms}") int baseSleepTimeMs,
            @Value("${locks.zookeeper.max-retries}") int maxRetries,
            @Value("${locks.zookeeper.connection-timeout}") int sessionTimeoutMs,
            @Value("${locks.zookeeper.session-timeout}") int connectionTimeoutMs) {
        log.debug("# connectString={},baseSleepTimeMs={},maxRetries={},sessionTimeoutMs={},connectionTimeoutMs={},",
                connectString, baseSleepTimeMs, maxRetries, sessionTimeoutMs, connectionTimeoutMs);
        return CuratorFrameworkFactory
                .builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .retryPolicy(new RetryNTimes(baseSleepTimeMs, maxRetries))
                .build();
    }
}
