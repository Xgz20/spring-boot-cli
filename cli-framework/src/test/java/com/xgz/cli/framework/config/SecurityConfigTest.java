package com.xgz.cli.framework.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigTest {

    @Test
    void testPasswordEncoding() {
        SecurityConfig securityConfig = new SecurityConfig();
        BCryptPasswordEncoder encoder = (BCryptPasswordEncoder) securityConfig.passwordEncoder();

        String rawPassword = "123456";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword), "Password should match after encoding");
    }

    @Test
    void bcrypt_hash_should_match_plain_password_123456() {
        String hash = "$2a$10$pkO.x0jrtVDfKDHJIpZP4u/fNc8i/ipNyYxrtbDMlO2vvsFEyC6Y2";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("123456", hash));
    }
}
