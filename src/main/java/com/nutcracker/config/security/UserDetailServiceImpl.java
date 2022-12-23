package com.nutcracker.config.security;

import com.nutcracker.entity.sys.SysRole;
import com.nutcracker.entity.sys.SysUser;
import com.nutcracker.service.sys.SysRoleService;
import com.nutcracker.service.sys.SysUserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * UserDetailServiceImpl
 *
 * @author 胡桃夹子
 * @date 2022/12/23 10:47
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 查询用户
        SysUser sysUser = sysUserService.findByName(username);
        if (sysUser != null) {
            // 查询权限
            SysRole sysRole = sysRoleService.findByUserId(sysUser.getId());
            authorities.add(new SimpleGrantedAuthority(sysRole.getAuthority()));
            return new User(username, sysUser.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("用户名不存在");
        }

    }
}
