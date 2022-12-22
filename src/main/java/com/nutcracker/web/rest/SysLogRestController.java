package com.nutcracker.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.entity.SysLog;
import com.thyme.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author thyme
 * @ClassName SysLogRestController
 * @Description TODO
 * @Date 2020/1/13 10:46
 */
@RestController
@RequestMapping("/sysLog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysLogRestController {

    private final SysLogService sysLogService;

    @GetMapping("/geSysLoglist")
    public ApiResponse getMenulist(@RequestParam("page") int page,
                                   @RequestParam("page_size") int pageSize){
        IPage<SysLog> sysLogPage = sysLogService.findSysLogPage(new Page(page, pageSize));
        JSONObject data = new JSONObject(16);
        data.put("total",sysLogPage.getTotal());
        data.put("sysLogList",sysLogPage.getRecords());
        data.put("page",sysLogPage.getCurrent());
        data.put("page_size",sysLogPage.getSize());
        return ApiResponse.ofSuccess(data);
    }
}
