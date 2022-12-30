package com.nutcracker.config.security.handler;

import com.alibaba.fastjson2.JSON;
import com.nutcracker.constant.Constants;
import com.nutcracker.entity.ApiResponse;
import com.nutcracker.service.sys.SysLogService;
import com.nutcracker.util.RequestUtils;
import com.nutcracker.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AuthenticationSuccessHandler
 *
 * @author 胡桃夹子
 * @date 2022/12/23 08:27
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    @Resource
    private SysLogService sysLogService;


    private static final String LOGIN_SUCCESS = JSON.toJSONString(ApiResponse.success(Constants.LOGIN_SUCCESS));

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (RequestUtils.isAjax(request)) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            String name = token.getName();
            //保存日志
            sysLogService.saveLoginLog(request, Constants.LOGIN_SUCCESS, name);
            ResponseUtils.print(response, LOGIN_SUCCESS);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }


}
