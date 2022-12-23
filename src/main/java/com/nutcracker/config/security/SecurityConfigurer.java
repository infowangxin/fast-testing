package com.nutcracker.config.security;

import com.nutcracker.config.filter.ValidateCodeFilter;
import com.nutcracker.config.security.handler.AuthenticationFailureHandler;
import com.nutcracker.config.security.handler.AuthenticationSuccessHandler;
import com.nutcracker.config.security.handler.CustomLogoutSuccessHandler;
import com.nutcracker.constant.Constants;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @author thyme
 * @ClassName SecurityConfigurer
 * @Description TODO
 * @Date 2019/12/11 10:47
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

        RequestMatcher requestMatcher = new AntPathRequestMatcher("/login");
        RequestMatcher requestMatcher1 = new OrRequestMatcher(
                AntPathRequestMatcher.antMatcher("/users/**"),
                AntPathRequestMatcher.antMatcher("/settings/**")
        );
        //ExpressionUrlAuthorizationConfigurer cfg = new CustomAuthenticationProvider()
        http.authorizeRequests()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/login")).access("permitAll")
                .requestMatchers(new AndRequestMatcher(
                        AntPathRequestMatcher.antMatcher("/users/**"),
                        AntPathRequestMatcher.antMatcher("/settings/**")
                )).hasAuthority("Admin")
                .notify("Admin", "Editor", "Salesperson")
                .notify("Admin", "Editor", "Salesperson", "Shipper")
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and()
                .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
                .and()
                .logout().permitAll();

        http.headers().frameOptions().sameOrigin();

        return http.build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable();

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                //放行所有的 css和js文件
                .antMatchers("/static/**", "/favicon.ico", "/actuator/**", "/code", "/invalid_session", "/expired", "/logout", "/403").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403")
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
    }

    /**
     * 校验用户信息
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
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
