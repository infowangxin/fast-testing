package com.nutcracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Start Application
 *
 * @author 胡桃夹子
 * @date 2020-02-24 13:10
 */
@Slf4j
@EnableAsync
@EnableCaching
@ServletComponentScan(basePackages = "com.nutcracker")
@SpringBootApplication(scanBasePackages = "com.nutcracker")
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        log.info(">>>>>>>>>>>>>>>>>>>> start successful <<<<<<<<<<<<<<<<<<<<");
    }

}
