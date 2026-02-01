package com.xgz.cli.framework.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@Component
@ConfigurationProperties(prefix = "sa-token")
@Data
public class SaTokenProperties {
    private boolean enable = true;

    private List<String> excludePathPatterns;
}
