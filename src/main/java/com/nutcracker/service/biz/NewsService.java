package com.nutcracker.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nutcracker.entity.biz.News;

import java.util.List;

/**
 * news service
 *
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

    /**
     * 查询所有用户
     *
     * @param page 当前页
     * @param pageSize  每页显示条数
     * @return 所有用户集合
     */
    IPage<News> getAll(Integer page, Integer pageSize, String keywords);

}
