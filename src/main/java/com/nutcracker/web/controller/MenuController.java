package com.nutcracker.web.controller;

import com.nutcracker.entity.sys.SysMenu;
import com.nutcracker.service.sys.SysMenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * MenuController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 15:20
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private SysMenuService sysMenuService;

    @GetMapping("/list")
    public String index() {
        return "module/menu/menu";
    }

    @GetMapping("/update")
    public String update(String id, Model model) {
        SysMenu sysMenu = sysMenuService.getById(id);
        model.addAttribute("sysMenu", sysMenu);
        return "module/menu/updateMenu";
    }

    @GetMapping("/add")
    public String add() {
        return "module/menu/addMenu";
    }
}
