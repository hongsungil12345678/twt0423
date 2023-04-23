package com.jwtstudy0419.hong0034.controller;

import com.jwtstudy0419.hong0034.dto.MemberDto;
import com.jwtstudy0419.hong0034.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/user")
    public ResponseEntity<MemberDto> findMemberByUserName(@RequestBody MemberDto dto){
        MemberDto result = memberService.findMemberByUsername(dto);
        return ResponseEntity.ok(result);
    }
}
