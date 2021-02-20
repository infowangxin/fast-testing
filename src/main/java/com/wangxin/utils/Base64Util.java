package com.wangxin.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * BASE Util
 *
 * @author 王鑫
 * @date 2021-02-20 09:01
 */
public class Base64Util {

    /**
     * BASE64转字符串
     *
     * @param str 普通字符串
     * @return 字符串
     * @throws IOException
     * @author 王鑫
     */
    public static String base642string(String str) throws IOException {
        String resp = new String(new BASE64Decoder().decodeBuffer(str), StandardCharsets.UTF_8);
        return resp;
    }

    /**
     * 字符串转BASE64
     *
     * @param str 普通字符串
     * @return BASE64字符串
     * @author 王鑫
     */
    public static String string2base64(String str) {
        String resp = new BASE64Encoder().encode(str.getBytes(StandardCharsets.UTF_8));
        return resp;
    }
}
