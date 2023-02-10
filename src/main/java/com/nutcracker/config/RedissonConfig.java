package com.nutcracker.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Redisson Config
 *
 * @author 胡桃夹子
 * @date 2023-02-09 21:13
 */
@Slf4j
@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(@Value("${spring.redis.redisson.config}") String path) throws IOException {
        log.info("# redissonConfig={}", path);
        String config = StringUtils.replace(path, "classpath:", "");
        log.info("# config={}", config);
        return Redisson.create(Config.fromYAML(new ClassPathResource(config).getInputStream()));
    }
}
