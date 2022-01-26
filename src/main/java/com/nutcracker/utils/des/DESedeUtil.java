package com.nutcracker.utils.des;


import com.nutcracker.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 3DES算法工具类
 *
 * @author 胡桃夹子
 * @date 2021/11/18 18:06
 */
public class DESedeUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DESedeUtil.class);

    private static final String KEY_ALGORITEM = "DESede";
    private static final String CIPHER_ALGORITEM = "DESede/ECB/PKCS5Padding";

    /**
     * 随机生成密钥
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            String key = byte2hex(secretKey.getEncoded());
            LOG.info("# key={}", key);
            return key;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("# 随机生成密钥失败, error msg={}", e.getLocalizedMessage());
            return null;
        }
    }


    private static Key getKey(String key) throws Exception {
        byte[] keyByte = hex2byte(key);
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
    public static String encrypt(String key, String data) throws RuntimeException {
        try {
            byte[] encryptByte = data.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            return byte2hex(cipher.doFinal(encryptByte));
        } catch (Exception e) {
            LOG.error("# 加密异常,data={},error msg={}", data, e.getLocalizedMessage());
            throw new RuntimeException("加密失败");
        }
    }

    /**
     * 解密
     *
     * @param key  密钥
     * @param data 业务参数
     * @return 解密后业务参数
     */
    public static String decrypt(String key, String data) throws RuntimeException {
        try {
            byte[] hexByte = hex2byte(data);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITEM);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            assert hexByte != null;
            return new String(cipher.doFinal(hexByte), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("# 解密异常,data={},error msg={}", data, e.getLocalizedMessage());
            throw new RuntimeException("解密失败");
        }
    }

    /**
     * byte二进制 转 hex 十六进制
     *
     * @param bytes byte二进制
     * @return hex 十六进制
     */
    private static String byte2hex(byte[] bytes) {
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
     */
    private static byte[] hex2byte(String hex) {
        if (StringUtil.isBlank(hex)) {
            return null;
        }
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bts;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {
        //String key = "098f6bcd4621d373cade4e832627b4f62017121611345734";
        String key = generateKey();

        System.out.println(key);
        String param = "18202146378";
        String encr = encrypt(key, param);
        String decr = decrypt(key, encr);
        System.out.println("key:" + key);
        System.out.println(param + " ==>> " + encr);
        System.out.println(encr + " ==>> " + decr);
    }
}
