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

    int saveLoginLog(HttpServletRequest request, String message, String name);

    IPage<SysLog> findSysLogPage(Page page);

}
