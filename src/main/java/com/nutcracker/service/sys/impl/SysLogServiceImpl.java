package com.nutcracker.service.sys.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nutcracker.entity.sys.SysLog;
import com.nutcracker.mapper.sys.SysLogMapper;
import com.nutcracker.service.sys.SysLogService;
import com.nutcracker.util.IpInfoUtils;
import com.nutcracker.util.UUIDUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * sys log service
 *
 * @author 胡桃夹子
 * @date 2022/12/22 13:37
 */
@Slf4j
@Service
public class SysLogServiceImpl implements SysLogService {

    @Resource
    private SysLogMapper sysLogDao;

    @Override
    public int saveLoginLog(HttpServletRequest request, String message, String name) {
        try {
            // 获取ip地址
            String ipAddr = IpInfoUtils.getIpAddr(request);
            // 获取ip来源
            String ipSource = IpInfoUtils.getipSource(ipAddr);
            //获取浏览器信息
            String browser = IpInfoUtils.getBrowser(request);
            // 获取系统名称
            String systemName = IpInfoUtils.getSystemName(request);
            SysLog sysLog = SysLog.builder()
                    .username(name)
                    .browserName(browser)
                    .createDate(new Date())
                    .id(UUIDUtils.getUUID())
                    .ipAddress(ipAddr)
                    .ipSource(ipSource)
                    .message(message)
                    .systemName(systemName)
                    .build();
            return sysLogDao.insert(sysLog);
        } catch (Exception e) {
            log.error("获取ip来源出错");
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public IPage<SysLog> findSysLogPage(Page page) {
        return sysLogDao.findSysLogPage(page);
    }
}
