package com.nutcracker.service.biz;

import com.nutcracker.entity.biz.News;

import java.util.List;

/**
 * news service
 * @author 胡桃夹子
 * @date 2022-12-30 18:00
 */
public interface NewsService {

    /**
     * 根据关键字查询新闻
     *
     * @param keywords 关键字
     * @return 新闻数据
     */
    List<News> findNewsByKeywords(String keywords);

}
