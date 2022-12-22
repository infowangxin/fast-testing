package com.nutcracker.service.sys.impl;

import com.nutcracker.entity.sys.SysUserRole;
import com.nutcracker.mapper.sys.SysUserRoleMapper;
import com.nutcracker.service.sys.SysUserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * sys user role service
 *
 * @author 胡桃夹子
 * @date 2022/12/22 13:41
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleDao;

    @Override
    public int insert(SysUserRole sysUserRole) {
        return sysUserRoleDao.insert(sysUserRole);
    }

    @Override
    public int deleteByUserId(String userId) {
        return sysUserRoleDao.deleteByUserId(userId);
    }
}
