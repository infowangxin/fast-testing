package com.nutcracker.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nutcracker.dto.UserDto;

import java.util.List;

/**
 * @author 胡桃夹子
 * @date 2022-12-21 08:25
 */
public interface UserMapper extends BaseMapper<UserDto> {

    /**
     * 查询所有用户信息
     *
     * @return 用户信息
     */
    List<UserDto> selectAll();
}
