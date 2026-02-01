-- Flyway migration
-- 创建 userEntity 表

CREATE TABLE IF NOT EXISTS `t_user`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username`    VARCHAR(64)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL DEFAULT '' COMMENT '密码(BCrypt)',
    `nickname`    VARCHAR(64)           DEFAULT NULL COMMENT '昵称',
    `phone`       VARCHAR(32)           DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(128)          DEFAULT NULL COMMENT '邮箱',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   VARCHAR(64)           DEFAULT NULL COMMENT '创建人',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   VARCHAR(64)           DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';


-- 初始化管理员账号：admin / 123456（BCrypt）
-- 说明：
-- 1) 脚本幂等：仅当 admin 不存在时插入
-- 2) password 是 BCrypt 哈希，可直接被 Spring Security PasswordEncoder.matches 校验

INSERT INTO `user` (`username`, `password`, `nickname`, `status`, `create_time`, `update_time`)
SELECT 'admin', '$2a$10$C6UzMDM.H6dfI/f/IKcXe.0pEB8Fqv4G8CqLQp5y9YuqbWNo1R6bK', '管理员', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `username` = 'admin');