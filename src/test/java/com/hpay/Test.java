package com.hpay;

import com.wangxin.utils.Micro3DES;

public class Test {

    public static void main(String[] args) throws Exception {
        String key = "098f6bcd4621d373cade4e832627b4f62017121611345734";// 48个长度的字符串即可
        String param = "王鑫";
        String encr = Micro3DES.encr(key, param);
        String decr = Micro3DES.decr(key, encr);
        System.out.println("key:" + key);
        System.out.println(param + " ==>> " + encr);
        System.out.println(encr + " ==>> " + decr);
    }
}
