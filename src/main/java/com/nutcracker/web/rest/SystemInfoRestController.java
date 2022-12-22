package com.nutcracker.web.rest;

import com.alibaba.fastjson.JSONObject;
import com.nutcracker.entity.sys.systeminfo.HeapInfo;
import com.nutcracker.entity.sys.systeminfo.MemInfo;
import com.nutcracker.entity.sys.systeminfo.SysFileInfo;
import com.nutcracker.entity.sys.systeminfo.SysInfo;
import com.thyme.common.base.ApiResponse;
import com.thyme.system.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;

/**
 * @author thyme
 * @ClassName SystemInfoController
 * @Description TODO
 * @Date 2019/12/25 11:08
 */
@RestController
@RequestMapping("system/rest")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemInfoRestController {

    private final SystemInfoService systemInfoService;

    /**
     * 获取硬件信息
     */
    @GetMapping("/getServerStaticInfo")
    public ApiResponse getInfo(){
        SystemInfo si = new SystemInfo();
        // 获取磁盘信息
        List<SysFileInfo> sysFileInfo = systemInfoService.getSysFileInfo(si.getOperatingSystem());
        // 获取服务器信息
        SysInfo sysInfo = systemInfoService.getSysInfo();
        JSONObject jsonObject = new JSONObject(16);
        jsonObject.put("sysFileInfo",sysFileInfo);
        jsonObject.put("sysInfo",sysInfo);
        return ApiResponse.ofSuccess(jsonObject);
    }

    /**
     * 动态获取硬件信息
     */
    @GetMapping("/getServerDynamicInfo")
    public ApiResponse getDynamicInfo(){
        JSONObject jsonObject = new JSONObject(16);
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        // 获取CPU信息
        CpuInfo cpuInfo = systemInfoService.getCpuInfo(hal.getProcessor());
        // 获取堆内存信息
        HeapInfo heapInfo = systemInfoService.getHeapInfo();
        // 获取JVM信息
        JvmInfo jvmInfo = systemInfoService.getJvmInfo();
        // 获取内存信息
        MemInfo memInfo = systemInfoService.getMemInfo(hal.getMemory());
        jsonObject.put("cpuInfo",cpuInfo);
        jsonObject.put("heapInfo",heapInfo);
        jsonObject.put("jvmInfo",jvmInfo);
        jsonObject.put("memInfo",memInfo);
        return ApiResponse.ofSuccess(jsonObject);
    }
}
