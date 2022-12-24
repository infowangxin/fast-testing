package com.nutcracker.mapper.sys;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nutcracker.constant.Constants;
import com.nutcracker.entity.sys.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 胡桃夹子
 * @date 2022-12-22 14:37
 */
@Slf4j
@SpringBootTest
public class SysUserMapperTest {

    @Resource
    private SysUserMapper sysUserMapper;

    @Test
    public void getAll() {
        try {
            Page page = new Page(1, 10);
            IPage<SysUser> sysUserIPage = sysUserMapper.getAll(page);
            log.debug("# {}", JSON.toJSONString(sysUserIPage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void password() {
        String encode = new BCryptPasswordEncoder().encode(Constants.CZMM);
        log.debug("# {}", encode);
    }
}
