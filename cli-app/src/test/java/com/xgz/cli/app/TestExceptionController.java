package com.xgz.cli.app;

import com.xgz.cli.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试专用 Controller：用于稳定触发全局异常处理。
 */
@RestController
@RequestMapping("/__test")
class TestExceptionController {

    @GetMapping("/custom")
    public String throwCustom() {
        throw new CustomException("test custom exception");
    }
}
