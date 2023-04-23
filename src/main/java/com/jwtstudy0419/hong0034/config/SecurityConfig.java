package com.jwtstudy0419.hong0034.config;

import com.jwtstudy0419.hong0034.jwt.JwtAccessDeniedHandler;
import com.jwtstudy0419.hong0034.jwt.JwtAuthenticationEntryPoint;
import com.jwtstudy0419.hong0034.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {//h2 데이터베이스 사용을 위해서
        return (web)->web.ignoring()
                .antMatchers("/h2-console/**","/favicon.ico");
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                
                //예외 처리 작성한 예외처리 등록
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console
                .and().headers().frameOptions().sameOrigin()

                // Security 는 기본적으로 세션을 사용한다. 허나 JWT를 사용하기 때문에 STATELESS 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS.STATELESS)

                // 로그인, 회원가입 -> 토큰이 없는 상태 이므로 예외처리 , 나머지는 모두 필요.
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()

                // JwtFilter를 적용한 ( JwtSecurityConfig 클래스 등록 )
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }
}
