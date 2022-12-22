package com.nutcracker.config.security.handler;

import com.alibaba.fastjson.JSON;
import com.thyme.common.base.ApiResponse;
import com.thyme.common.base.Constants;
import com.thyme.common.utils.RequestUtils;
import com.thyme.common.utils.ResponseUtils;
import com.thyme.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author thyme
 * @ClassName AuthenticationFailureHandler
 * @Description TODO
 * @Date 2019/12/11 11:35
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final SysLogService sysLogService;

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
            sysLogService.saveLoginLog(request,message,username);
            ResponseUtils.print(response, JSON.toJSONString(ApiResponse.fail(message)));
        } else {
            super.onAuthenticationFailure(request, response, e);
        }
    }
}
