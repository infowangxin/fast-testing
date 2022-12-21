package com.nutcracker.lock.exception;

import com.nutcracker.exception.BusinessException;

import java.io.Serial;

/**
 * <p>获取锁运行时异常</p>
 *
 * @author 胡桃夹子
 */
public class LockException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -2086914156447989987L;

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }
}
