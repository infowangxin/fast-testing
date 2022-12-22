package com.nutcracker.config.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * Validate Code Exception
 *
 * @author 胡桃夹子
 * @date 2022/12/22 14:54
 */
public class ValidateCodeException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 2247846470407548051L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
