package com.nutcracker.web.rest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nutcracker.entity.ApiResponse;
import com.nutcracker.entity.biz.News;
import com.nutcracker.service.biz.NewsService;
import com.nutcracker.vo.NewsVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        log.debug("getNewsList resp={}", JSON.toJSONString(resp));
        return ApiResponse.ofSuccess(resp);
    }

    @GetMapping("/news/deleteNews/{id}")
    public ApiResponse<JSONObject> deleteNews(@PathVariable String id) {
        log.debug("deleteNews id={}", id);
        JSONObject jsonObject = new JSONObject();
        boolean resp = newsService.deleteNews(id);
        log.debug("deleteNews resp={}", resp);
        jsonObject.put("status", resp);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/news/updateNews")
    public ApiResponse<JSONObject> updateNews(@RequestBody News news) {
        log.debug("updateNews news={}", JSON.toJSONString(news));
        JSONObject jsonObject = new JSONObject();
        boolean resp = newsService.updateNews(news);
        log.debug("updateNews resp={}", resp);
        jsonObject.put("status", resp);
        return ApiResponse.ofSuccess(jsonObject);
    }

    @PostMapping("/news/addNews")
    public ApiResponse<JSONObject> addNews(@RequestBody News news) {
        log.debug("addNews news={}", JSON.toJSONString(news));
        JSONObject jsonObject = new JSONObject();
        boolean resp = newsService.addNews(news);
        log.debug("addNews resp={}", resp);
        jsonObject.put("status", resp);
        return ApiResponse.ofSuccess(jsonObject);
    }



}
