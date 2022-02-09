package com.nutcracker.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * 上下文设置
 *
 * @author 胡桃夹子
 * @date 2022/2/9 10:49
 */
@Component
public class ApplicationContext implements ServletContextAware {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContext.class);

    @Override
    public void setServletContext(ServletContext context) {
        String datetime = String.valueOf(System.currentTimeMillis());
        String contextPath = context.getContextPath();
        context.setAttribute("v_css", datetime);
        context.setAttribute("v_js", datetime);
        log.info("# version={} , contextPath={}", datetime, contextPath);
        context.setAttribute("ctx", contextPath);
    }

}
