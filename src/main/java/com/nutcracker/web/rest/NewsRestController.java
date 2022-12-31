package com.nutcracker.web.rest;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nutcracker.entity.ApiResponse;
import com.nutcracker.entity.biz.News;
import com.nutcracker.service.biz.NewsService;
import com.nutcracker.vo.NewsVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 胡桃夹子
 * @date 2022-12-31 13:21
 */
@Slf4j
@RestController
public class NewsRestController {

    @Resource
    private NewsService newsService;

    @PostMapping("/news/getNewsList")
    public ApiResponse<IPage<News>> getNewsList(@RequestBody NewsVo newsVo) {
        IPage<News> resp = newsService.getAll(newsVo.getPage(), newsVo.getPageSize(), newsVo.getKeywords());
        log.debug("# resp={}", JSON.toJSONString(resp));
        return ApiResponse.ofSuccess(resp);
    }

}
