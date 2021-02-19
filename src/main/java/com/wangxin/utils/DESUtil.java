package com.wangxin.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * DES/CBC/NoPadding 加密
 */
public class DESUtil {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";

    /**
     * NoPadding
     * API或算法本身不对数据进行处理，加密数据由加密双方约定填补算法。例如若对字符串数据进行加解密，可以补充\0或者空格，然后trim
     * PKCS5Padding
     * 加密前：数据字节长度对8取余，余数为m，若m>0,则补足8-m个字节，字节数值为8-m，即差几个字节就补几个字节，字节数值即为补充的字节数，若为0则补充8个字节的8
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/NoPadding";


    public static void main(String[] args) throws Exception {
        String KEY = "12345678";
        String IV = "12345678";
        String mobile = "13918492887";
        System.out.println(mobile);
        String enString = encrypt(mobile, KEY, IV);
        System.out.println(enString);
        String deString = decrypt(enString, KEY, IV);
        System.out.println(deString);
    }

    private static Key getKey(String KEY) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        return secretKeySpec;
    }

    private static AlgorithmParameterSpec getIV(String IV) {
        IvParameterSpec parameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        return parameterSpec;
    }

    //此函数是pkcs7padding填充函数
    public static String pkcs7padding(String data) {
        int bs = 16;
        int padding = bs - (data.length() % bs);
        String padding_text = "";
        for (int i = 0; i < padding; i++) {
            padding_text += (char) padding;
        }
        return data + padding_text;
    }

    public static String encrypt(String data, String key, String iv) throws Exception {
        String input = pkcs7padding(data);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = input.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key), getIV(iv));

            byte[] encrypted = cipher.doFinal(plaintext);
            String hexStr = MicroHexUtils.byte2hex(encrypted);
            return string2base64(hexStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String decrypt(String data, String key, String iv) throws Exception {
        String base64 = base642string(data);
        byte[] hexStr = MicroHexUtils.hex2byte(base64);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKey(key), getIV(iv));
        byte[] original = cipher.doFinal(hexStr);
        return new String(original, StandardCharsets.UTF_8);
    }


    public static String base642string(String str) throws IOException {
        String resp = new String(new BASE64Decoder().decodeBuffer(str), StandardCharsets.UTF_8);
        return resp;
    }

    public static String string2base64(String str) {
        String resp = new BASE64Encoder().encode(str.getBytes(StandardCharsets.UTF_8));
        return resp;
    }

}
