package com.nutcracker.strategy;

import com.nutcracker.enums.SecretStrategyEnum;
import com.nutcracker.strategy.secret.BaseStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略工厂
 *
 * @author 胡桃夹子
 * @date 2021-11-18 09:24
 */
@Service
public class StrategyFactory {

    private static final Logger LOG = LoggerFactory.getLogger(StrategyFactory.class);


    private final Map<String, BaseStrategy> secretStrategyMap = new ConcurrentHashMap<>();

    /**
     * 注册策略
     *
     * @param strategy 策略
     */
    public void registerSecretStrategy(BaseStrategy strategy) {
        SecretStrategyEnum secretStrategyEnum = strategy.getSecretStrategyEnum();
        if (null != secretStrategyEnum) {
            LOG.info("# register strategy, {}", strategy.getClass().getName());
            secretStrategyMap.put(secretStrategyEnum.name(), strategy);
        }
    }


    /**
     * 获取策略
     *
     * @param secretStrategyEnum 加解密渠道枚举
     * @return 处理器
     */
    public BaseStrategy getSecretStrategy(SecretStrategyEnum secretStrategyEnum) {
        BaseStrategy strategy = null;
        if (null != secretStrategyEnum) {
            strategy = secretStrategyMap.get(secretStrategyEnum.name());
        } else {
            LOG.warn("# not fund strategy, {}", secretStrategyEnum);
        }
        return strategy;
    }
}
