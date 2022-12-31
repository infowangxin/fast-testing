package com.nutcracker.web.rest;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nutcracker.entity.ApiResponse;
import com.nutcracker.entity.sys.SysLog;
import com.nutcracker.service.sys.SysLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SysLogRestController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 10:41
 */
@RestController
@RequestMapping("/sysLog")
public class SysLogRestController {

    @Resource
    private SysLogService sysLogService;

    @GetMapping("/geSysLoglist")
    public ApiResponse<JSONObject> getMenulist(@RequestParam("page") int page, @RequestParam("page_size") int pageSize) {
        IPage<SysLog> sysLogPage = sysLogService.findSysLogPage(new Page<>(page, pageSize));
        JSONObject data = new JSONObject(16);
        data.put("total", sysLogPage.getTotal());
        data.put("sysLogList", sysLogPage.getRecords());
        data.put("page", sysLogPage.getCurrent());
        data.put("page_size", sysLogPage.getSize());
        return ApiResponse.ofSuccess(data);
    }
}
