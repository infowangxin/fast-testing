package com.nutcracker.config.security.handler;

import com.nutcracker.constant.Constants;
import com.nutcracker.service.sys.SysLogService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomLogoutSuccessHandler
 *
 * @author 胡桃夹子
 * @date 2022/12/23 08:27
 */
@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${server.servlet.context-path}")
    private String path;

    @Resource
    private SysLogService sysLogService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String name = token.getName();
        //保存日志
        sysLogService.saveLoginLog(httpServletRequest, Constants.LOGOUT_SUCCESS, name);
        httpServletResponse.sendRedirect(path == null ? Constants.LOGIN_URL : path + Constants.LOGIN_URL);
    }
}
