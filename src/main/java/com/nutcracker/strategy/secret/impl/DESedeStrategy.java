package com.nutcracker.strategy.secret.impl;

import com.nutcracker.enums.SecretStrategyEnum;
import com.nutcracker.strategy.secret.BaseStrategy;
import com.nutcracker.util.secret.DESedeUtil;
import org.springframework.stereotype.Component;

/**
 * DESede加解密
 *
 * @author 胡桃夹子
 * @date 2021/11/17 18:04
 */
@Component
public class DESedeStrategy extends BaseStrategy {


    @Override
    public SecretStrategyEnum getSecretStrategyEnum() {
        return SecretStrategyEnum.DESede;
    }

    private String getKey() {
        return DESedeUtil.KEY;
    }

    @Override
    public String encrypt(String param) {
        return DESedeUtil.encrypt(getKey(), param);
    }

    @Override
    public String decrypt(String param) {
        return DESedeUtil.decrypt(getKey(), param);
    }
}
