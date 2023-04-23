package com.jwtstudy0419.hong0034.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OncePerRequestFilter -> 요청 시 한번만 실행
 * JwtFilter -> RequestHeader 에서 AccessToken 을 전달 받아서 유효성 검사 후, SecurityContext 저장하는 역할 수행
 */
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. RequestHeader 로 부터 Header 값 꺼낸다 (토큰)
        String result = requestHeaderResult(request);

        // 2. 유효성 검증 ( tokenProvider 구현한 validateToken 메서드)
        if(StringUtils.hasText(result) && tokenProvider.validateToken(result)){

            //  3-1. 참일경우 (정상 토큰) -> 해당 토큰으로 Authentication 가져와서 SecurityContext 저장
            Authentication authentication = tokenProvider.getAuthentication(result);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    // 헤더 추출 ( Token 값 반환)
    private String requestHeaderResult(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // Header -> "Authorization" 이고 , "Bearer " 로 시작 시
        if( StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(7);// "Bearer " -> 인덱스 0~6
        }
        return null;
    }
}
