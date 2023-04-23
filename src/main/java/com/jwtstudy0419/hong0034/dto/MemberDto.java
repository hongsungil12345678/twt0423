package com.jwtstudy0419.hong0034.dto;

import com.jwtstudy0419.hong0034.domain.Authority;
import com.jwtstudy0419.hong0034.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String username;
    private String password;
    private Authority authority;

    public Member saveMember(PasswordEncoder passwordEncoder){
        return Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .authority(authority)
                .build();
    }
    public UsernamePasswordAuthenticationToken generateAuthenticationToken(){
        return new UsernamePasswordAuthenticationToken(username,password);
    }

}
