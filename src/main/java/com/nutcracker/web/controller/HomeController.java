package com.nutcracker.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 胡桃夹子
 * @date 2021-09-23 11:12
 */
@Slf4j
@Controller
public class HomeController {

    @GetMapping(value = {"/", "index"})
    public String index() {
        return "redirect:/secret/index";
    }

    @GetMapping("/403")
    @ResponseBody
    public String error() {
        log.info("# 403");
        return "您暂无访问权限！";
    }
}
