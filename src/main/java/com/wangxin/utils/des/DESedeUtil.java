package com.wangxin.utils.des;

import com.wangxin.utils.HexUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * 3DES算法工具类
 * @author 胡桃夹子
 * @date 2021/11/18 18:06
 */
public class DESedeUtil {

    private static final String KEY_ALGORITEM = "DESede";
    private static final String CIPHER_ALGORITEM = "DESede/ECB/PKCS5Padding";

    private static Key getKey(String key) throws Exception {
        byte[] keyByte = HexUtil.hex2byte(key);
        assert keyByte != null;
        DESedeKeySpec dks = new DESedeKeySpec(keyByte);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITEM);
        return skf.generateSecret(dks);
    }

    /**
     * 加密
     *
     * @param key  密钥
     * @param data 业务参数
     * @return 加密后的业务参数
     */
    public static String encrypt(String key, String data) {
        try {
            byte[] encryptByte = data.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            return HexUtil.byte2hex(cipher.doFinal(encryptByte));
        } catch (Exception e) {
            System.err.println("# 3DES加密失败");
            return null;
        }
    }

    /**
     * 解密
     *
     * @param key  密钥
     * @param data 业务参数
     * @return 解密后业务参数
     */
    public static String decrypt(String key, String data) {
        try {
            byte[] hexByte = HexUtil.hex2byte(data);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            assert hexByte != null;
            return new String(cipher.doFinal(hexByte), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("# 3DES解密失败");
            return null;
        }
    }

    public static void main(String[] args)  {
        // 48个长度的字符串即可
        String key = "098f6bcd4621d373cade4e832627b4f62017121611345734";
        //String param = "RjQ1RTg3MERBMTBBMkZFMzFEMjY4MjAwMUUwNUJGNkU=";
        String param = "18202146378";
        String encr = encrypt(key, param);
        String decr = decrypt(key, encr);
        System.out.println("key:" + key);
        System.out.println(param + " ==>> " + encr);
        System.out.println(encr + " ==>> " + decr);
    }
}
