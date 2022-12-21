package com.nutcracker.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡桃夹子
 * @date 2022-12-21 09:42
 */
@Configuration
@MapperScan("com.nutcracker.mapper")
public class MybatisPlusConfig {

}
