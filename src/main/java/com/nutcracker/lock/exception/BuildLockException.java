package com.nutcracker.lock.exception;

import com.nutcracker.exception.BusinessException;

/**
 * <p>创建锁运行时异常</p>
 *
 * @author 胡桃夹子
 * @date 2020/1/17 12:47
 */
public class BuildLockException extends BusinessException {

    private static final long serialVersionUID = -8238014866385255218L;

    public BuildLockException(String message) {
        super(message);
    }

    public BuildLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
