package com.nutcracker.util;

import java.util.UUID;

/**
 * uuid utils
 *
 * @author 胡桃夹子
 * @date 2022/12/22 13:36
 */
public class UUIDUtils {

    /**
     * 生成UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成UUID不去除-
     */
    public static String getUUIDPLUS() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成16位UUID
     */
    public static String getSixteenUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(16);
    }
}
