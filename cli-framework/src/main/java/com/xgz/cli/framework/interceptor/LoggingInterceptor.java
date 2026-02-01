package com.xgz.cli.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.xgz.cli.framework.log.LogBean;
import com.xgz.cli.framework.util.IPUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Component
public class LoggingInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LogBean logBean = LogBean.start();
        logBean.setIp(IPUtil.getRealRequestIp(request));
        logBean.setPath(request.getRequestURI());
        logBean.setHeader(getHeaderJson(request));
        logBean.setParams(getParameterJson(request));
        logBean.setMethod(request.getMethod());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        LogBean.end();
    }

    /**
     * 获取 RequestHeader 参数
     *
     * @param request
     * @return
     */
    private Object getHeaderJson(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            json.put(name, request.getHeader(name));
        }
        return json;
    }

    /**
     * 获取 RequestParam 参数
     *
     * @param request
     * @return
     */
    private JSONObject getParameterJson(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            json.put(name, request.getParameter(name));
        }
        return json;
    }
}
