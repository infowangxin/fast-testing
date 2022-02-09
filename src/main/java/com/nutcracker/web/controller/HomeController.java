package com.nutcracker.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 胡桃夹子
 * @date 2021-09-23 11:12
 */
@Controller
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @GetMapping(value = {"/", "home", "index"})
    public String redirect() {
        return "redirect:/secret";
    }

    @GetMapping("/403")
    @ResponseBody
    public String error() {
        LOG.info("# 403");
        return "您暂无访问权限！";
    }
}
