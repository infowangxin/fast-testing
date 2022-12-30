package com.nutcracker.mapper.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nutcracker.entity.biz.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * news mapper
 *
 * @author 胡桃夹子
 * @date 2022-12-30 17:56
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

    /**
     * 根据关键字查询新闻
     *
     * @param keywords 关键字
     * @return 新闻数据
     */
    List<News> findNewsByKeywords(@Param("keywords") String keywords);

}
