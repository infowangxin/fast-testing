package com.nutcracker.web.controller;

import com.nutcracker.entity.biz.News;
import com.nutcracker.service.biz.NewsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 胡桃夹子
 * @date 2022-12-31 11:33
 */
@Slf4j
@Controller
public class NewsController {

    @Resource
    private NewsService newsService;

    @GetMapping("news/news")
    public String newsIndex(){
        log.debug("# news/news");
        return "module/news/news";
    }

    @GetMapping("/news/updateNews/{id}")
    public String updateNews(@PathVariable String id, Model model) {
        News news =newsService.findNewsById(id);
        model.addAttribute("news", news);
        return "module/news/updateNews";
    }

    @GetMapping("/news/addNews")
    public String addNews() {
        return "module/news/addNews";
    }

}
