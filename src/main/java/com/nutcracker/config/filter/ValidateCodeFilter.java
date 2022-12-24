package com.nutcracker.config.filter;

import com.nutcracker.config.exception.ValidateCodeException;
import com.nutcracker.config.security.handler.AuthenticationFailureHandler;
import com.nutcracker.constant.Constants;
import com.nutcracker.service.sys.RedisService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Validate Code Filter
 *
 * @author 胡桃夹子
 * @date 2022/12/22 15:36
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Resource
    private RedisService redisService;

    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;

    private static final PathMatcher PATHMATCHER = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (Constants.REQUEST_MODE_POST.equals(request.getMethod()) && PATHMATCHER.match(Constants.LOGIN_URL, request.getServletPath())) {
            try {
                codeValidate(request);
            } catch (ValidateCodeException e) {
                //验证码不通过，跳到错误处理器处理
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        doFilter(request, response, filterChain);
    }

    private void codeValidate(HttpServletRequest request) {
        //获取传入的验证码
        String code = request.getParameter("code");
        String uuidCode = request.getParameter("uuidCode");
        if (StringUtils.isEmpty(code)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        String codeVal = redisService.getCodeVal(uuidCode);
        if (StringUtils.isEmpty(codeVal)) {
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StringUtils.equals(codeVal, code)) {
            throw new ValidateCodeException("验证码不匹配");
        }
    }
}

