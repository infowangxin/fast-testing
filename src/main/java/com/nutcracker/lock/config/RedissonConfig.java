package com.nutcracker.lock.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * redisson configuration
 *
 * @author wangxin65
 * @date 2022-12-04 16:40
 */
@Configuration
public class RedissonConfig {

    private static final Logger log = LoggerFactory.getLogger(RedissonConfig.class);

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(@Value("${spring.redis.redisson.config}") String path) throws IOException {
        log.info("# redissonConfig={}", path);
        String config = StringUtils.replace(path, "classpath:", "");
        log.info("# config={}", config);
        return Redisson.create(Config.fromYAML(new ClassPathResource(config).getInputStream()));
    }
}
