package com.nutcracker.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nutcracker.entity.sys.SysLog;
import jakarta.servlet.http.HttpServletRequest;


/**
 * sys log service
 *
 * @author 胡桃夹子
 * @date 2022/12/22 13:29
 */
public interface SysLogService {

    /**
     * 保存登录日志
     *
     * @param request HttpServletRequest
     * @param message 描述
     * @param name    登录账号
     */
    void saveLoginLog(HttpServletRequest request, String message, String name);

    /**
     * 分页查询登录日志
     *
     * @param page 分页对象
     * @return 登录日志
     */
    IPage<SysLog> findSysLogPage(Page<SysLog> page);

}
