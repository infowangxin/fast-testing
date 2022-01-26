package com.nutcracker.utils;

/**
 * String工具类
 *
 * @author 胡桃夹子
 * @date 2022-01-26 09:03
 */
public class StringUtil {

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
}
