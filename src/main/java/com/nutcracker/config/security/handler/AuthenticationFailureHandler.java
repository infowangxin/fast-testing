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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AuthenticationFailureHandler
 *
 * @author 胡桃夹子
 * @date 2022/12/23 08:26
 */
@Component
@Slf4j
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    private SysLogService sysLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (RequestUtils.isAjax(request)) {
            String username = request.getParameter("username");
            String message;
            if (e instanceof SessionAuthenticationException) {
                //Object username = request.getAttribute("SPRING_SECURITY_LAST_USERNAME_KEY")
                message = Constants.LOGIN_MAX_LIMIT;
                ResponseUtils.print(response, JSON.toJSONString(ApiResponse.fail(message)));
            }
            message = e.getMessage();
            // 保存日志
            sysLogService.saveLoginLog(request, message, username);
            ResponseUtils.print(response, JSON.toJSONString(ApiResponse.fail(message)));
        } else {
            super.onAuthenticationFailure(request, response, e);
        }
    }
}
