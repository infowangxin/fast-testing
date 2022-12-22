package com.nutcracker.config.security.handler;

import com.thyme.common.base.Constants;
import com.thyme.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author thyme
 * @ClassName CustomLogoutSuccessHandler
 * @Description TODO
 * @Date 2019/12/18 18:00
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${server.servlet.context-path}")
    private String path;

    private final SysLogService sysLogService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String name = token.getName();
        //保存日志
        sysLogService.saveLoginLog(httpServletRequest,Constants.LOGOUT_SUCCESS,name);
        httpServletResponse.sendRedirect(path==null? Constants.LOGIN_URL :path + Constants.LOGIN_URL);
    }
}
