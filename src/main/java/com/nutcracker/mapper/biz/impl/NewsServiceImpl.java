package com.nutcracker.mapper.biz.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nutcracker.constant.DataSourceTagger;
import com.nutcracker.entity.biz.News;
import com.nutcracker.mapper.biz.NewsMapper;
import com.nutcracker.service.biz.NewsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * news service
 *
 * @author 胡桃夹子
 * @date 2022-12-30 18:01
 */
@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    //@DS(DataSourceTagger.AUTH)
    @DS(DataSourceTagger.BIZ)
    @Override
    public List<News> findNewsByKeywords(String keywords) {
        log.info("# keywords={}", keywords);
        return newsMapper.findNewsByKeywords(keywords);
    }

    @DS(DataSourceTagger.BIZ)
    @Override
    public IPage<News> getAll(Integer page, Integer pageSize, String keywords) {
        log.debug("# page={},pageSize={},keywords={}", page, pageSize, keywords);
        if (Objects.isNull(page)) {
            page = 1;
        }
        if (Objects.isNull(pageSize)) {
            page = 10;
        }
        log.debug("# page={},pageSize={}", page, pageSize);
        return newsMapper.findNewsByPage(new Page<>(page, pageSize), keywords);
    }
}
