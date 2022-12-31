package com.nutcracker.web.rest;

import com.alibaba.fastjson2.JSONObject;
import com.nutcracker.entity.ApiResponse;
import com.nutcracker.entity.sys.systeminfo.CpuInfo;
import com.nutcracker.entity.sys.systeminfo.HeapInfo;
import com.nutcracker.entity.sys.systeminfo.JvmInfo;
import com.nutcracker.entity.sys.systeminfo.MemInfo;
import com.nutcracker.entity.sys.systeminfo.SysFileInfo;
import com.nutcracker.entity.sys.systeminfo.SysInfo;
import com.nutcracker.service.sys.SystemInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;

/**
 * SystemInfoController
 *
 * @author 胡桃夹子
 * @date 2022/12/23 10:42
 */
@RestController
@RequestMapping("system/rest")
public class SystemInfoRestController {

    @Resource
    private SystemInfoService systemInfoService;

    /**
     * 获取硬件信息
     */
    @GetMapping("/getServerStaticInfo")
    public ApiResponse<JSONObject> getInfo() {
        SystemInfo si = new SystemInfo();
        // 获取磁盘信息
        List<SysFileInfo> sysFileInfo = systemInfoService.getSysFileInfo(si.getOperatingSystem());
        // 获取服务器信息
        SysInfo sysInfo = systemInfoService.getSysInfo();
        JSONObject jsonObject = new JSONObject(16);
        jsonObject.put("sysFileInfo", sysFileInfo);
        jsonObject.put("sysInfo", sysInfo);
        return ApiResponse.ofSuccess(jsonObject);
    }

    /**
     * 动态获取硬件信息
     */
    @GetMapping("/getServerDynamicInfo")
    public ApiResponse<JSONObject> getDynamicInfo() {
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
        jsonObject.put("cpuInfo", cpuInfo);
        jsonObject.put("heapInfo", heapInfo);
        jsonObject.put("jvmInfo", jvmInfo);
        jsonObject.put("memInfo", memInfo);
        return ApiResponse.ofSuccess(jsonObject);
    }
}
