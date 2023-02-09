package com.nutcracker.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
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
            @NacosValue(value = "${locks.zookeeper.connect-server}",  autoRefreshed = true) String connectString,
            @NacosValue(value = "${locks.zookeeper.base-sleep-time-ms}",  autoRefreshed = true) int baseSleepTimeMs,
            @NacosValue(value = "${locks.zookeeper.max-retries}",  autoRefreshed = true) int maxRetries,
            @NacosValue(value = "${locks.zookeeper.connection-timeout}",  autoRefreshed = true) int sessionTimeoutMs,
            @NacosValue(value = "${locks.zookeeper.session-timeout}",  autoRefreshed = true) int connectionTimeoutMs) {
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
