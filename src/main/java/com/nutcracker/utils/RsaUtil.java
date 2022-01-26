package com.nutcracker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * RSA加解密工具类
 *
 * @author 胡桃夹子
 * @date 2021/11/17 15:51
 */
public class RsaUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RsaUtil.class);

    private final static String PUBLIC_KEY_STRING = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI1eP+v4FFBaAAO/KiqeMCg/ISaH5yTXucqyPL4FmffmRkireUY4cBT9td0F1qQf0TP5qPcJYcDvr2VzsFN0vXkCAwEAAQ==";
    private final static String PRIVATE_KEY_STRING = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAjV4/6/gUUFoAA78qKp4wKD8hJofnJNe5yrI8vgWZ9+ZGSKt5RjhwFP213QXWpB/RM/mo9wlhwO+vZXOwU3S9eQIDAQABAkBvm/KBfPmkunl6e8lxnjRNAhJNFkyx9LMVSYTVTRzS7sE7tDFMZj6icRYSfPke5VpK8SZuPCPHwiX8kdTKlH+BAiEA73i3GGMz2qlxWJHa2cgw0GpXaXvFpaRWzZDxivObqjECIQCXIB7TL6P79U3aNfi+3WVSaeXtDjbJW3VVg5+7SImtyQIgdtAp58uwepj/R7Z2Iv+UWJOqcweZ9Kko+C0l6o8beMECIBsbRMwmcGhiAHvSUHKTrXfRzM7vTL5jLDGRcVYbVa6ZAiEA0mG+YqXqYVx2WXEzSJR5mD0s3c+dBIgN9sGCooKDUww=";

    /**
     * 随机生成密钥对
     */
    public static void genKeyPair() throws RuntimeException {
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(512, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // 得到私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到私钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            String publicKeyString = new String(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            // 得到私钥字符串
            String privateKeyString = new String(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            // 将公钥和私钥保存到Map
            //0表示公钥
            LOG.info("publicKeyString:{}", publicKeyString);
            //1表示私钥
            LOG.info("privateKeyString:{}", privateKeyString);
        } catch (Exception e) {
            LOG.error("# 生成密钥对异常,error msg={}", e.getLocalizedMessage());
            throw new RuntimeException("生成密钥对失败");
        }
    }

    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @return 密文
     * @throws RuntimeException 加密过程中的异常信息
     */
    public static String encrypt(String str) throws RuntimeException {
        String outStr = "";
        try {
            //base64编码的公钥
            byte[] decoded = Base64.getDecoder().decode(PUBLIC_KEY_STRING);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            LOG.error("# 加密异常,data={},error msg={}", str, e.getLocalizedMessage());
            throw new RuntimeException("加密失败");
        }
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @return 铭文
     * @throws RuntimeException 解密过程中的异常信息
     */
    public static String decrypt(String str) throws RuntimeException {
        String outStr = "";
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
            //base64编码的私钥
            byte[] decoded = Base64.getDecoder().decode(PRIVATE_KEY_STRING);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            outStr = new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            LOG.error("# 解密异常,data={},error msg={}", str, e.getLocalizedMessage());
            throw new RuntimeException("解密失败");
        }
        return outStr;
    }

    public static void main(String[] args) {
        String keyword = "123";
        String secret = encrypt(keyword);
        System.out.println(secret);
        System.out.println(decrypt(secret));
        //genKeyPair();
    }
}
