package com.xgz.cli.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParamsErrorVO {

    /** 字段名 */
    private String fieldName;

    /** 错误信息 */
    private String errorMsg;
}
