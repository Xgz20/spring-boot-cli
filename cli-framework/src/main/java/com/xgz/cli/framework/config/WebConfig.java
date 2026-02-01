package com.xgz.cli.framework.config;

import com.xgz.cli.framework.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Value("${access-log.enable:false}")
    private boolean accessLogEnable;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 当前开启调用日志开关后，才会打印接口调用日志
        if (accessLogEnable) {
            registry.addInterceptor(loggingInterceptor);
        }
    }
}
