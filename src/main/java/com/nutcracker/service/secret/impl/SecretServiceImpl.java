package com.nutcracker.service.secret.impl;

import com.alibaba.fastjson.JSON;
import com.nutcracker.enums.SecretStrategyEnum;
import com.nutcracker.service.secret.SecretService;
import com.nutcracker.strategy.StrategyFactory;
import com.nutcracker.strategy.secret.BaseStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 加密解密操作
 *
 * @author 胡桃夹子
 * @date 2021-11-18 10:17
 */
@Service
public class SecretServiceImpl implements SecretService {

    private static final Logger LOG = LoggerFactory.getLogger(SecretServiceImpl.class);

    @Autowired
    private StrategyFactory strategyFactory;

    @Override
    public String encrypt(SecretStrategyEnum secretStrategyEnum, String param) {
        BaseStrategy strategy = strategyFactory.getSecretStrategy(secretStrategyEnum);
        if (null != strategy) {
            return strategy.encrypt(param);
        }
        LOG.error(" 加密失败, {}, {}", secretStrategyEnum, param);
        return null;
    }

    @Override
    public String decrypt(SecretStrategyEnum secretStrategyEnum, String param) {
        BaseStrategy strategy = strategyFactory.getSecretStrategy(secretStrategyEnum);
        if (null != strategy) {
            return strategy.decrypt(param);
        }
        LOG.error(" 解密失败, {}, {}", secretStrategyEnum, param);
        return null;
    }

    @Override
    public String encrypt(SecretStrategyEnum secretStrategyEnum, List<String> list) {
        BaseStrategy strategy = strategyFactory.getSecretStrategy(secretStrategyEnum);
        if (null != strategy) {
            return strategy.execute(list, true);
        }
        LOG.error(" 加密失败, {}, {}", secretStrategyEnum, JSON.toJSONString(list));
        return null;
    }

    @Override
    public String decrypt(SecretStrategyEnum secretStrategyEnum, List<String> list) {
        BaseStrategy strategy = strategyFactory.getSecretStrategy(secretStrategyEnum);
        if (null != strategy) {
            return strategy.execute(list, false);
        }
        LOG.error(" 解密失败, {}, {}", secretStrategyEnum, JSON.toJSONString(list));
        return null;
    }
}
