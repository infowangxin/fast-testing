package com.nutcracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Start Application
 *
 * @author 胡桃夹子
 * @date 2020-02-24 13:10
 */
@EnableAsync
@ServletComponentScan(basePackages = "com.nutcracker")
@SpringBootApplication(scanBasePackages = "com.nutcracker")
public class MainApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MainApplication.class);
        springApplication.run(args);
        LOG.info(">>>>>>>>>>>>>>>>>>>> start successful <<<<<<<<<<<<<<<<<<<<");
    }

}
