package com.jwtstudy0419.hong0034.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {
    private SecurityUtils() {}

    public static Long getInstance() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( authentication == null || authentication.getName()==null){
            throw new RuntimeException("해당 정보가 없습니다.  "+authentication+"Authentication.getName"+authentication.getName());
        }
        return Long.parseLong(authentication.getName());


    }
}
