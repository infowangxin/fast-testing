package com.nutcracker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SystemInfoController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 15:21
 */
@Controller
@RequestMapping("/system")
public class SystemInfoController {

    @GetMapping("/serverInfo")
    public String serverInfo() {
        return "module/system/server";
    }

    @RequestMapping("/introduce")
    public String introduce() {
        return "module/system/introduce";
    }
}
