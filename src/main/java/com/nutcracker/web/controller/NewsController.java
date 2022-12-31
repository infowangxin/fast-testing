package com.nutcracker.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 胡桃夹子
 * @date 2022-12-31 11:33
 */
@Slf4j
@Controller
public class NewsController {

    @GetMapping("news/news")
    public String newsIndex(){
        log.debug("# news/news");
        return "module/news/news";
    }

}
