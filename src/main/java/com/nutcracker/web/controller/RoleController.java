package com.nutcracker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * RoleController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 15:20
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @GetMapping("/list")
    public String index() {
        return "module/role/role";
    }

    @GetMapping("/update")
    public String update(String name, String authority, String id, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("authority", authority);
        model.addAttribute("id", id);
        return "module/role/updateRole";
    }

    @GetMapping("/add")
    public String add() {
        return "module/role/addRole";
    }
}
