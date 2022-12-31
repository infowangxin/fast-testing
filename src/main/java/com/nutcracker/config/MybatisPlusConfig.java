package com.nutcracker.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 胡桃夹子
 * @date 2022-12-31 20:20
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 新的分页插件，一缓和二缓遵循mybatis的规则，需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题（该属性会在旧插件移除后一同移除）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 向Mybatis过滤器链中添加分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.SQLITE));
        // 添加乐观锁插件
        //interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 还可以添加其他的拦截器
        return interceptor;
    }
}
