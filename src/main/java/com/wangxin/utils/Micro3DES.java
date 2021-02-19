package com.wangxin.utils;

/**
 * @Description DES加密工具类
 * @author 王鑫
 * @date Dec 13, 2017 1:31:40 PM
 */
public class Micro3DES {

    /**
     * 加密
     * 
     * @param key
     *            密钥
     * @param data
     *            业务参数
     * @return 加密后的业务参数
     */
    public static String encr(String key, String data) {
        try {
            byte[] srckey = MicroHexUtils.hex2byte(key);
            return MicroHexUtils.byte2hex(Micro3DESedeUtils.encr(srckey, data.getBytes("UTF-8")));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     * 
     * @param key
     *            密钥
     * @param data
     *            业务参数
     * @return 解密后业务参数
     */
    public static String decr(String key, String data) {
        try {
            byte[] srckey = MicroHexUtils.hex2byte(key);
            return (new String(Micro3DESedeUtils.decr(srckey, MicroHexUtils.hex2byte(data)), "UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }

}
