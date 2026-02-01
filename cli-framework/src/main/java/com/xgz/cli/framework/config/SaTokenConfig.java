package com.xgz.cli.framework.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.xgz.cli.framework.properties.SaTokenProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Configuration
@ConditionalOnProperty(value = "sa-token.enable", havingValue = "true", matchIfMissing = true)
public class SaTokenConfig implements WebMvcConfigurer {

    public static final String SESSION_USER_KEY = "UserInfo";

    @Autowired
    private SaTokenProperties saTokenProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        Set<String> finalExcludeSet = new HashSet<>();
        List<String> excludePathPatterns = saTokenProperties.getExcludePathPatterns();
        if (CollectionUtils.isNotEmpty(excludePathPatterns)) {
            finalExcludeSet.addAll(excludePathPatterns);
        }

        registry.addInterceptor(new SaInterceptor(handle -> checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(finalExcludeSet.toArray(new String[0]));
    }

    private void checkLogin() {
        // 校验是否登录
        StpUtil.checkLogin();
    }
}
