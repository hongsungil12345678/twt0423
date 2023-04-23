package com.jwtstudy0419.hong0034.dto;

import com.jwtstudy0419.hong0034.domain.Authority;
import com.jwtstudy0419.hong0034.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberResponseDto {
    private String username;
    private String password;
    private Authority authority;

    public static MemberResponseDto createResponse(Member member){
        if(member==null){
            return null;
        }
        return MemberResponseDto.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .authority(member.getAuthority())
                .build();
    }
}
