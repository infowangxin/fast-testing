package com.nutcracker.config.security;

import com.nutcracker.config.filter.ValidateCodeFilter;
import com.nutcracker.config.security.handler.AuthenticationFailureHandler;
import com.nutcracker.config.security.handler.AuthenticationSuccessHandler;
import com.nutcracker.config.security.handler.CustomLogoutSuccessHandler;
import com.nutcracker.constant.Constants;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * SecurityConfigurer
 *
 * @author 胡桃夹子
 * @date 2022/12/23 11:04
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer {

    /**
     * 最大登录数
     */
    @Value("${security.max-session}")
    private Integer maxSession;

    /**
     * 超出最大登录数，是否阻止登录
     */
    @Value("${security.prevents-login}")
    private Boolean preventsLogin;

    @Resource
    private UserDetailServiceImpl userDetailService;

    @Resource
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Resource
    private ValidateCodeFilter validateCodeFilter;

    @Resource
    private CustomInvalidSessionStrategy customInvalidSessionStrategy;

    @Resource
    private CustomExpiredSessionStrategy customExpiredSessionStrategy;

    @Resource
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Resource
    private SessionRegistry sessionRegistry;

    @Resource
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            try {
                authorize
                        // 放行接口
                        .requestMatchers("/favicon.ico", "/code", "/invalid_session", "/expired", "/logout", "/403").permitAll()
                        // 放行目录
                        .requestMatchers("/static/**", "/actuator/**").permitAll()
                        // 其余的都需要权限校验
                        .anyRequest().authenticated()
                        .and().exceptionHandling().accessDeniedPage("/403")
                        // 防跨站请求伪造
                        //.and().csrf(csrf -> csrf.disable());
                        .and()
                        .formLogin()
                        .loginProcessingUrl(Constants.LOGIN_URL)
                        .loginPage(Constants.LOGIN_URL)
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
                        .permitAll()
                        .and()
                        .csrf().disable()
                        .cors()
                        .and()
                        .logout()
                        .logoutUrl(Constants.LOGOUT_URL)
                        //.logoutSuccessUrl(Constants.LOGIN_URL)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .and()
                        .sessionManagement()
                        //.invalidSessionUrl("/invalid_session")
                        //失效处理
                        .invalidSessionStrategy(customInvalidSessionStrategy)
                        //同一账号同时允许多个设备在线
                        .maximumSessions(maxSession)
                        //新用户挤走前用户
                        .maxSessionsPreventsLogin(preventsLogin)
                        //.expiredUrl("/expired")
                        //超时处理
                        .expiredSessionStrategy(customExpiredSessionStrategy)
                        .sessionRegistry(sessionRegistry);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // 校验用户信息
        http.authenticationProvider(customAuthenticationProvider);
        return http.build();
    }

    @Bean
    public SessionRegistry getSessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return new CorsFilter(source);
    }
}
