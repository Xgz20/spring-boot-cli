package com.xgz.cli.framework.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Slf4j
public class IPUtil {
    /**
     * 获取真实IP
     *
     * @param request
     * @return
     */
    public static String getRealRequestIp(HttpServletRequest request) {
        try {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (null != ip && ip.contains(",")) {
                ip = ip.substring(0, ip.indexOf(","));
            }
            return ip;
        } catch (Exception e) {
            log.error("获取IP地址失败", e);
        }
        return StringUtils.EMPTY;
    }
}
