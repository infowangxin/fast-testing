package com.nutcracker.lock.exception;

import com.nutcracker.exception.BusinessException;

import java.io.Serial;

/**
 * <p>解锁运行时异常</p>
 *
 * @author 胡桃夹子
 */
public class UnLockException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 8521984814937746047L;

    public UnLockException(String message) {
        super(message);
    }

    public UnLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
