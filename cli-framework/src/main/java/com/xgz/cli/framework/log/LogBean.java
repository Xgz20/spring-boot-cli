package com.xgz.cli.framework.log;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Data
@Slf4j
public class LogBean {
    private static final ThreadLocal<LogBean> LOG_BEAN_THREAD_LOCAL = ThreadLocal.withInitial(LogBean::new);

    private String method;
    private String path;
    private Object header;
    // 请求参数
    private Object params;

    // 响应码
    private String code;
    // 响应结果
    private Object result;
    private long startTime;
    // 消耗时间（单位ms）
    private long consumeTime;
    private String error;
    private String ip;


    public static LogBean start() {
        LogBean logBean = LOG_BEAN_THREAD_LOCAL.get();
        logBean.setStartTime(System.currentTimeMillis());
        return logBean;
    }

    public static LogBean get() {
        return LOG_BEAN_THREAD_LOCAL.get();
    }

    public static void end() {
        LogBean logBean = get();
        logBean.setConsumeTime(System.currentTimeMillis() - logBean.getStartTime());
        logBean.print();
        LOG_BEAN_THREAD_LOCAL.remove();
    }

    private void print() {
        if (log.isInfoEnabled()) {
            log.info(JSON.toJSONString(this));
        }
    }

}
