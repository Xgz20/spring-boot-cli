package com.xgz.cli.util;

import java.util.UUID;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
public class UUIDUtil {
    /**
     * 生成UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成去掉中划线的UUID字符串
     */
    public static String generateUUIDWithoutHyphens() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
