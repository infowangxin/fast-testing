package com.nutcracker.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 胡桃夹子
 * @date 2022-12-21 08:29
 */
@Data
@TableName("user")
public class UserDto {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
