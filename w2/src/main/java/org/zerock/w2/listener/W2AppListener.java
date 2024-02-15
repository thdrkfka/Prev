package org.zerock.w2.listener;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Log4j2
@WebListener
public class W2AppListener implements ServletContextListener {

    @Override //프로젝트 실행 시, 로그가 기록
    public void contextInitialized(ServletContextEvent sce) {
        log.info("----------init--------------------");
        log.info("----------init--------------------");
        log.info("----------init--------------------");

        ServletContext servletContext = sce.getServletContext();

        servletContext.setAttribute("appName", "W2");
    }

    @Override //프로젝트 종료 시, 로그가 기록
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("----------destroy--------------------");
        log.info("----------destroy--------------------");
        log.info("----------destroy--------------------");
    }
}
