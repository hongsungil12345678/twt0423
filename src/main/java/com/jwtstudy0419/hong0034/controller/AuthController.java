package com.jwtstudy0419.hong0034.controller;

import com.jwtstudy0419.hong0034.dto.MemberDto;
import com.jwtstudy0419.hong0034.dto.TokenDto;
import com.jwtstudy0419.hong0034.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberDto> signup(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(authService.signup(memberDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberDto memberDto) {
        return ResponseEntity.ok(authService.login(memberDto));
    }
    @PostMapping("/renew")
    public ResponseEntity<TokenDto> renew(@RequestBody TokenDto tokenDto){
        return ResponseEntity.ok(authService.renew(tokenDto));
    }
}
