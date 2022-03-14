package com.nutcracker.strategy.secret.impl;

import com.nutcracker.enums.SecretStrategyEnum;
import com.nutcracker.strategy.secret.BaseStrategy;
import com.nutcracker.util.secret.RsaUtil;
import org.springframework.stereotype.Component;

/**
 * RSA加解密
 *
 * @author 胡桃夹子
 * @date 2021/11/17 18:04
 */
@Component
public class RSAStrategy extends BaseStrategy {

    @Override
    public SecretStrategyEnum getSecretStrategyEnum() {
        return SecretStrategyEnum.RSA;
    }

    @Override
    public String encrypt(String param) {
        return RsaUtil.encrypt(param);
    }

    @Override
    public String decrypt(String param) {
        return RsaUtil.decrypt(param);
    }
}
