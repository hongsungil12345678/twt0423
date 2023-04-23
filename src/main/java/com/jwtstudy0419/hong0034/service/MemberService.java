package com.jwtstudy0419.hong0034.service;

import com.jwtstudy0419.hong0034.domain.Member;
import com.jwtstudy0419.hong0034.dto.MemberDto;
import com.jwtstudy0419.hong0034.dto.MemberResponseDto;
import com.jwtstudy0419.hong0034.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto findMemberByUsername(MemberDto dto){
        if(!memberRepository.existsByUsername(dto.getUsername())){
            throw new RuntimeException("해당 검색 결과가 없습니다 ."+dto.getUsername());
        }
        Optional<Member> result = memberRepository.findByUsername(dto.getUsername());
        return new MemberDto(result.get().getUsername(),result.get().getPassword(),result.get().getAuthority());
    }

//    public MemberResponseDto findMemberByUsername(String username){
//        return  memberRepository.findByUsername(username)
//                .map(MemberResponseDto::createResponse)
//                .orElseThrow(() -> new RuntimeException("해당 결과 값이 없습니다. : "+username));
//    }

    public MemberResponseDto signup(MemberResponseDto memberResponseDto){
        if(memberRepository.findByUsername(memberResponseDto.getUsername()).orElse(null) !=null){
            throw new RuntimeException("중복되는 회원 이름입니다. "+memberResponseDto.getUsername());
        }
        Member result = Member.builder()
                .username(memberResponseDto.getUsername())
                .password(memberResponseDto.getPassword())
                .authority(memberResponseDto.getAuthority())
                .build();
        return MemberResponseDto.createResponse(memberRepository.save(result));
    }

}
