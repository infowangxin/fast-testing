package com.nutcracker.web.rest;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nutcracker.constant.Constants;
import com.nutcracker.entity.ApiResponse;
import com.nutcracker.entity.sys.SysUser;
import com.nutcracker.entity.sys.SysUserRole;
import com.nutcracker.service.sys.SysRoleService;
import com.nutcracker.service.sys.SysUserRoleService;
import com.nutcracker.service.sys.SysUserService;
import com.nutcracker.util.UUIDUtils;
import com.nutcracker.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * UserRestControllerUserRestController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 10:42
 */
@RestController
@RequestMapping("/user")
public class UserRestController {

    @Resource
    private SysUserService userService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @GetMapping("/getUserInfo")
    public ApiResponse<JSONObject> getUserInfo(@RequestParam("page") int page, @RequestParam("page_size") int pageSize) {
        JSONObject jsonObject = new JSONObject();
        List<UserVO> userList = new ArrayList<>(16);
        IPage<SysUser> sysUserList = userService.getAll(new Page<>(page, pageSize));
        if (sysUserList.getRecords() != null && sysUserList.getRecords().size() > 0) {
            for (SysUser sysUser : sysUserList.getRecords()) {
                //根据用户id查询角色名称
                String roleName = sysRoleService.getById(sysUser.getId());
                UserVO userVO = new UserVO(roleName);
                userVO.setId(sysUser.getId());
                userVO.setName(sysUser.getName());
                userVO.setNickName(sysUser.getNickName());
                userVO.setSex(sysUser.getSex());
                userVO.setMobile(sysUser.getMobile());
                userVO.setEmail(sysUser.getEmail());
                userList.add(userVO);
            }
        }
        jsonObject.put("total", sysUserList.getTotal());
        jsonObject.put("page", sysUserList.getCurrent());
        jsonObject.put("page_size", sysUserList.getSize());
        jsonObject.put("sysUserList", userList);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/deleteUser")
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ApiResponse<JSONObject> deleteUser(@RequestParam("id") String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            sysUserRoleService.deleteByUserId(id);
            userService.deleteById(id);
            jsonObject.put("code", 200);
        } catch (Exception e) {
            jsonObject.put("code", 500);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/updateUser")
    @ResponseBody
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ApiResponse<JSONObject> updateRole(@RequestBody UserVO userVO) {
        JSONObject jsonObject = new JSONObject();
        SysUser sysUser = new SysUser(userVO.getId(), userVO.getName(), null, userVO.getNickName(), userVO.getSex(), userVO.getMobile(),
                userVO.getEmail(), userVO.getBirthday(), userVO.getHobby(), userVO.getLiveAddress(), null, new Date());
        try {
            sysUserRoleService.deleteByUserId(userVO.getId());
            sysUserRoleService.insert(new SysUserRole(userVO.getId(), sysRoleService.getIdByName(userVO.getUserRole())));
            userService.updateById(sysUser);
            jsonObject.put("code", 200);
        } catch (Exception e) {
            jsonObject.put("code", 500);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/addUser")
    @ResponseBody
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public ApiResponse<JSONObject> addRole(@RequestBody UserVO userVO) {
        JSONObject jsonObject = new JSONObject();
        SysUser user = userService.findByName(userVO.getName());
        if (user == null) {
            //用户id
            String userId = UUIDUtils.getUUID();
            //角色id
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(sysRoleService.getIdByName(userVO.getUserRole()));
            sysUserRole.setUserId(userId);
            sysUserRoleService.insert(sysUserRole);
            SysUser sysUser = new SysUser(userId, userVO.getName(), new BCryptPasswordEncoder().encode(userVO.getPassword()), userVO.getNickName(), userVO.getSex(),
                    userVO.getMobile(), userVO.getEmail(), userVO.getBirthday(), userVO.getHobby(), userVO.getLiveAddress(), new Date(), null);
            if (userService.insert(sysUser) > 0) {
                jsonObject.put("code", 200);
            } else {
                jsonObject.put("code", 500);
            }
        } else {
            // 501 用户已存在
            jsonObject.put("code", 501);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/editPassword")
    public ApiResponse<JSONObject> editPassword(String id) {
        JSONObject jsonObject = new JSONObject();
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setPassword(new BCryptPasswordEncoder().encode(Constants.CZMM));
        try {
            if (userService.updateById(sysUser) > 0) {
                jsonObject.put("code", 200);
            }
        } catch (Exception e) {
            jsonObject.put("code", 500);
        }
        return ApiResponse.ofSuccess(jsonObject);
    }

    @GetMapping("/getAllRoleName")
    public ApiResponse<JSONObject> getAllRoleName() {
        JSONObject jsonObject = new JSONObject();
        List<String> allRoleName = sysRoleService.getAllRoleName();
        jsonObject.put("allRoleName", allRoleName);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/editUser")
    @ResponseBody
    public ApiResponse<JSONObject> editUser(@RequestBody UserVO userVO) {
        JSONObject jsonObject = new JSONObject();
        SysUser sysUser = new SysUser();
        sysUser.setId(userVO.getId());
        sysUser.setName(userVO.getName());
        sysUser.setNickName(userVO.getNickName());
        sysUser.setEmail(userVO.getEmail());
        sysUser.setSex(userVO.getSex());
        sysUser.setMobile(userVO.getMobile());
        sysUser.setBirthday(userVO.getBirthday());
        sysUser.setHobby(userVO.getHobby());
        sysUser.setLiveAddress(userVO.getLiveAddress());
        sysUser.setUpdateTime(new Date());
        if (userService.updateById(sysUser) > 0) {
            jsonObject.put("code", 200);
            return ApiResponse.ofSuccess(jsonObject);
        } else {
            return ApiResponse.fail("更新基本资料失败");
        }
    }

}
