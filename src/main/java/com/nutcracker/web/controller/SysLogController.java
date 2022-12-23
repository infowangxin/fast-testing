package com.nutcracker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SysLogController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 15:20
 */
@Controller
@RequestMapping("/sys_log")
public class SysLogController {

    @GetMapping("/list")
    public String index() {
        return "module/syslog/syslog";
    }
}
