package com.nutcracker.mapper.user;

import com.alibaba.fastjson.JSON;
import com.nutcracker.dto.UserDto;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 胡桃夹子
 * @date 2022-12-21 08:58
 */
@Slf4j
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void selectList() {
        try {
            List<UserDto> userList = userMapper.selectAll();
            log.debug("# size={}", userList.size());
            log.debug("# {}", JSON.toJSONString(userList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
