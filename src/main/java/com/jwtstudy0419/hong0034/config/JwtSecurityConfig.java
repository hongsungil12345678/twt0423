package com.jwtstudy0419.hong0034.config;

import com.jwtstudy0419.hong0034.jwt.JwtFilter;
import com.jwtstudy0419.hong0034.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// JwtFilter 등록 (Security Filter 앞에 적용)
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    // tokenProvider 주입받아서 -> jwtfilter 를 Security 로직 등록
    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtFilter filter =new JwtFilter(tokenProvider);
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
