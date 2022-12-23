package com.nutcracker.config.interceptor;

import com.nutcracker.entity.sys.SysMenu;
import com.nutcracker.service.sys.SysMenuService;
import com.nutcracker.util.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AccessAuthHandlerInterceptor
 *
 * @author 胡桃夹子
 * @date 2022/12/23 08:25
 */
@Slf4j
@Component
public class AccessAuthHandlerInterceptor implements HandlerInterceptor {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<SysMenu> secondMenu = sysMenuService.getSecondMenu();
        List<String> urlList = secondMenu.stream().map(o -> o.getMenuHref()).collect(Collectors.toList());
        String servletPath = StringUtils.replace(request.getServletPath(), "/", "", 1);
        if (urlList.contains(servletPath)) {
            // 判断权限
            String username = (String) SecurityUtils.getCurrentUserAuthentication().getPrincipal();
            List<SysMenu> sysMenuList = sysMenuService.findMenuListByUser(username);
            if (sysMenuList.stream().anyMatch(o -> servletPath.equals(o.getMenuHref()))) {
                return true;
            }
            response.sendRedirect(contextPath + "/403");
            return false;
        }
        return true;
    }

}
