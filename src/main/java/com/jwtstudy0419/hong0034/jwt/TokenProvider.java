package com.jwtstudy0419.hong0034.jwt;

import com.jwtstudy0419.hong0034.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일
    private final Key key;

    public TokenProvider(@Value("${jwt.secret}")String secretKey){
        byte[] decode = Decoders.BASE64.decode(secretKey);
        this.key= Keys.hmacShaKeyFor(decode);   // secretKey 복호화
    }
    
    // Token 생성 메소드
    public TokenDto createToken(Authentication authentication){
        // Authentication 객체로부터 권한 획득
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        System.out.println("    ***    TokenProvider createToken Method - authorities : "+ authorities);

        long now = (new Date()).getTime();//현재 시간
        Date accessTokenExpiredIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);// accessToken 만료시간
        Date refreshTokenExpiredIn = new Date (now + REFRESH_TOKEN_EXPIRE_TIME);

        // accessToken, refreshToken 생성

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) // payload "sub"  : "name"
                .claim(AUTHORITIES_KEY,authorities) // payload "auth", "ROLE_USER"
                .setExpiration(accessTokenExpiredIn) // payload "exp"
                .signWith(key,SignatureAlgorithm.HS512) // 암호화
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiredIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(accessTokenExpiredIn.getTime())
                .build();
    }
// accessToken을 통해서 Authentication 객체 획득, 복호화
    public Authentication getAuthentication(String accessToken){
        Claims claims = accessTokenParseClaims(accessToken);

        if(claims.get(AUTHORITIES_KEY)==null){
            throw new RuntimeException("권한 정보가 없는 토큰 입니다. : "+accessToken);
        }
        // 권한
        //Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        //UserDetails 객체 생성
        UserDetails userDetails = new User(claims.getSubject(),"",authorities);
        
        // Authentication 객체 반환 (UsernamePasswordAuthenticationToken)
        return new UsernamePasswordAuthenticationToken(userDetails,"",authorities);
    }

    // accessToken 을 이용해서 파싱, Claims
    private Claims accessTokenParseClaims(String accessToken){
        try{
            return Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }
    // 토큰 값 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
