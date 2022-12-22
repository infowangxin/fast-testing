package com.nutcracker.service.sys;

import com.nutcracker.entity.sys.systeminfo.CpuInfo;
import com.nutcracker.entity.sys.systeminfo.HeapInfo;
import com.nutcracker.entity.sys.systeminfo.JvmInfo;
import com.nutcracker.entity.sys.systeminfo.MemInfo;
import com.nutcracker.entity.sys.systeminfo.NoHeapInfo;
import com.nutcracker.entity.sys.systeminfo.SysFileInfo;
import com.nutcracker.entity.sys.systeminfo.SysInfo;
import com.nutcracker.entity.sys.systeminfo.SystemHardwareInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import java.util.List;

/**
 * system info service
 *
 * @author 胡桃夹子
 * @date 2022/12/22 13:30
 */
public interface SystemInfoService {

    SystemHardwareInfo getSystemInfo();

    /**
     * 获取CPU相关信息
     */
    CpuInfo getCpuInfo(CentralProcessor processor);

    /**
     * 获取内存相关信息
     */
    MemInfo getMemInfo(GlobalMemory memory);

    /**
     * 获取JVM相关信息
     */
    JvmInfo getJvmInfo();

    /**
     * 获取堆内存信息
     */
    HeapInfo getHeapInfo();

    /**
     * 获取非堆内存信息
     */
    NoHeapInfo getNoHeapInfo();

    /**
     * 获取系统信息
     */
    SysInfo getSysInfo();

    /**
     * 获取磁盘信息
     */
    List<SysFileInfo> getSysFileInfo(OperatingSystem os);

}
