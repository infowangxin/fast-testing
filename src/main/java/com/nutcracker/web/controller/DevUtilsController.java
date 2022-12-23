package com.nutcracker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * DevUtilsController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 14:44
 */
@Controller
@RequestMapping("/devUtils")
public class DevUtilsController {

    @GetMapping("/menuIcon")
    public String menuIcon() {
        return "module/devutils/menuIcon";
    }

    @GetMapping("/vCharts")
    public String vcharts() {
        return "module/devutils/vCharts";
    }
}
