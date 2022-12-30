package com.nutcracker.mapper.biz.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.nutcracker.constant.DataSourceTagger;
import com.nutcracker.entity.biz.News;
import com.nutcracker.mapper.biz.NewsMapper;
import com.nutcracker.service.biz.NewsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * news service
 * @author 胡桃夹子
 * @date 2022-12-30 18:01
 */
@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    //@DS(value = DataSourceTagger.AUTH)
    @DS(value = DataSourceTagger.BIZ)
    @Override
    public List<News> findNewsByKeywords(String keywords) {
        log.info("# keywords={}", keywords);
        return newsMapper.findNewsByKeywords(keywords);
    }
}
