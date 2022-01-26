package com.nutcracker.utils;

/**
 * 16进制工具类
 *
 * @author 胡桃夹子
 * @date Dec 13, 2017 1:35:26 PM
 */
public class HexUtil {

    /**
     * byte二进制 转 hex 十六进制
     *
     * @param bytes byte二进制
     * @return hex 十六进制
     * @author 胡桃夹子
     */
    public static String byte2hex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        for (byte aByte : bytes) {
            hexStr.append(Integer.toHexString(0x0100 + (aByte & 0x00FF)).substring(1).toUpperCase());
        }
        return hexStr.toString();
    }

    /**
     * hex十六进制 转 byte二进制
     *
     * @param hex hex十六进制
     * @return byte二进制
     * @author 胡桃夹子
     */
    public static byte[] hex2byte(String hex) {
        if (StringUtil.isBlank(hex)) {
            return null;
        }
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }

}
