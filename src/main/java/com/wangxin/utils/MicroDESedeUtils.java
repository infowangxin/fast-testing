package com.wangxin.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * @Description 3DES算法工具类
 * @author 王鑫
 * @date Dec 13, 2017 1:32:32 PM
 */
public class MicroDESedeUtils {

    private static final String KEY_ALGORITEM = "DESede";
    private static final String CIPHER_ALGORITEM = "DESede/ECB/PKCS5Padding";

    private static Key toKey(byte[] key) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITEM);
        return skf.generateSecret(dks);
    }

    /**
     * @Description 3DES加密
     * @author 王鑫
     * @param key
     *            密钥
     * @param data
     *            业务参数
     * @return 加密后的业务参数
     */
    public static byte[] encr(byte[] key, byte[] data) {
        try {
            Key k = toKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.ENCRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("# 3DES加密失败");
            return null;
        }
    }

    /**
     * @Description 3DES解密
     * @author 王鑫
     * @param key
     *            密钥
     * @param data
     *            业务参数
     * @return 解密后的业务参数
     */
    public static byte[] decr(byte[] key, byte[] data) {
        try {
            Key k = toKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.DECRYPT_MODE, k);
            return cipher.doFinal(data);
        } catch (Exception e) {
            System.err.println("# 3DES解密失败");
            return null;
        }
    }
}
