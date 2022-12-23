package com.nutcracker.web.controller;

import com.nutcracker.constant.Constants;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ErrorPageController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 14:44
 */
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
}
