package com.jwtstudy0419.hong0034.service;

import com.jwtstudy0419.hong0034.domain.Member;
import com.jwtstudy0419.hong0034.domain.RefreshToken;
import com.jwtstudy0419.hong0034.dto.MemberDto;
import com.jwtstudy0419.hong0034.dto.TokenDto;
import com.jwtstudy0419.hong0034.jwt.TokenProvider;
import com.jwtstudy0419.hong0034.repository.MemberRepository;
import com.jwtstudy0419.hong0034.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    // signup
    @Transactional
    public MemberDto signup(MemberDto dto){
        if(memberRepository.existsByUsername(dto.getUsername())){
            throw new RuntimeException("이미 존재하는 회웡 입니다.  : "+dto.getUsername());
        }
        // 회원가입
        Member member = dto.saveMember(passwordEncoder);
        Member result = memberRepository.save(member);// 저장 완료
        return new MemberDto(result.getUsername(),result.getPassword(),result.getAuthority());
    }
    // login
    @Transactional
    public TokenDto login(MemberDto dto){
        // 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken = dto.generateAuthenticationToken();

        // 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 토큰을 생성
        TokenDto tokenDto = tokenProvider.createToken(authentication);

        // 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }
    // 갱신 -> tokenProvider 에서 token 관련된 메서드 구현
    @Transactional
    public TokenDto renew(TokenDto tokenDto){
        // 1. refreshToken 검증
        if(!tokenProvider.validateToken(tokenDto.getRefreshToken())){
            throw new RuntimeException("유효하지 않은 RefreshToken 입니다. :"+tokenDto.getRefreshToken());
        }
        // 2. accessToken 을 이용해서 memberID 가져오기 -> Authentication 객체를 통해서
        Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
        System.out.println(" **** Authentication.getName()"+authentication.getName()+"auth : "+authentication.getAuthorities());

        // 3. 저장소에서 ID 기반 검색
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("결과 값이 없습니다. " + authentication.getName() + "   /   " + authentication.getAuthorities()));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto reNewtokenDto = tokenProvider.createToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateToken(reNewtokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return reNewtokenDto;

    }

}
