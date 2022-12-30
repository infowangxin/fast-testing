package com.nutcracker.web.controller;

import com.alibaba.fastjson2.JSON;
import com.nutcracker.entity.sys.SysUser;
import com.nutcracker.service.sys.SysRoleService;
import com.nutcracker.service.sys.SysUserService;
import com.nutcracker.util.SecurityUtils;
import jakarta.annotation.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * UserController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 15:21
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @GetMapping("/list")
    public String index() {
        return "module/user/user";
    }

    @GetMapping("/update")
    public String update(String id, Model model) {
        SysUser sysUser = sysUserService.getById(id);
        String roleName = sysRoleService.getById(sysUser.getId());
        model.addAttribute("sysUser", sysUser);
        model.addAttribute("roleName", roleName);
        return "module/user/updateUser";
    }

    @GetMapping("/add")
    public String add() {
        return "module/user/addUser";
    }

    @GetMapping("/changePassword")
    public String changePassword() {
        return "module/user/changePassword";
    }

    @GetMapping("/personal")
    public String personal(Model model) {
        Authentication authentication = SecurityUtils.getCurrentUserAuthentication();
        String username = (String) authentication.getPrincipal();
        SysUser sysUser = sysUserService.findByName(username);
        String roleName = sysRoleService.getById(sysUser.getId());
        //model.addAttribute("sysUser", net.sf.json.JSONObject .fromObject(sysUser));
        model.addAttribute("sysUser", JSON.toJSONString(sysUser));
        model.addAttribute("roleName", roleName);
        return "module/user/personal";
    }
}
