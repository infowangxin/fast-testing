package com.nutcracker.web.controller;

import com.nutcracker.constant.Constants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ErrorPageController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 14:44
 */
@Slf4j
@Controller
public class ErrorPageController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String error(HttpServletResponse response) {
        if (Constants.INT_PAGE_ERROR == response.getStatus()) {
            return Constants.STRING_PAGE_ERROR;
        }
        return Constants.STRING_PAGE_NOT_FOUND;
    }
    @GetMapping("/403")
    @ResponseBody
    public String error() {
        log.info("# 403");
        return "您暂无访问权限！";
    }
}
