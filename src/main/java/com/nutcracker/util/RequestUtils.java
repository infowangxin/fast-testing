package com.nutcracker.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * request util
 *
 * @author 胡桃夹子
 * @date 2022/12/23 08:24
 */
public class RequestUtils {
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}
