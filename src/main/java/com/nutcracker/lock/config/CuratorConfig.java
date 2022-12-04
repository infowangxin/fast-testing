package com.nutcracker.lock.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Curator 配置
 *
 * @author 胡桃夹子
 * @date 2022-12-04 12:24
 */
@Configuration
public class CuratorConfig {

    private static final Logger log = LoggerFactory.getLogger(CuratorConfig.class);

    @Bean
    public CuratorFramework curatorFramework(
            @Value("${locks.zookeeper.connect-server}") String connectString,
            @Value("${locks.zookeeper.base-sleep-time-ms}") int baseSleepTimeMs,
            @Value("${locks.zookeeper.max-retries}") int maxRetries,
            @Value("${locks.zookeeper.connection-timeout}") int sessionTimeoutMs,
            @Value("${locks.zookeeper.session-timeout}") int connectionTimeoutMs) {
        log.debug("# connectString={},baseSleepTimeMs={},maxRetries={},sessionTimeoutMs={},connectionTimeoutMs={},",
                connectString, baseSleepTimeMs, maxRetries, sessionTimeoutMs, connectionTimeoutMs);
        // 配置zookeeper连接的重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        // 构建
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .retryPolicy(retryPolicy)
                .build();
        // 连接zookeeper
        curatorFramework.start();
        return curatorFramework;
    }
}
