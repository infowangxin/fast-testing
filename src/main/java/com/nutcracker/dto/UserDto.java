package com.nutcracker.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 胡桃夹子
 * @date 2022-12-21 08:29
 */
@Data
@TableName("user")
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -1052415026190093316L;
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
