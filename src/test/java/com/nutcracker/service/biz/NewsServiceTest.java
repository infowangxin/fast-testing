package com.nutcracker.service.biz;

import com.alibaba.fastjson2.JSON;
import com.nutcracker.entity.biz.News;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author 胡桃夹子
 * @date 2022-12-30 18:06
 */
@Slf4j
@SpringBootTest
public class NewsServiceTest {

    @Resource
    private NewsService newsService;

    @Test
    public void findNewsByKeywords() {
        try {
            String keywords = null;
            List<News> list = newsService.findNewsByKeywords(keywords);
            log.info("# size:{}", CollectionUtils.size(list));
            for (News news : list) {
                log.info("# {}", JSON.toJSONString(news));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
