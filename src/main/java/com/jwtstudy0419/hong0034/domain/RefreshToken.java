package com.jwtstudy0419.hong0034.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken extends BaseTimeEntity {
    @Id @Column(name = "refresh_key")
    private String key;

    @Column(name = "refresh_value")
    private String value;
    @Builder
    public RefreshToken(String key,String value){
        this.key=key;
        this.value=value;
    }
    //  RefreshToken 객체 반환
    public RefreshToken updateToken(String token){
        this.value=token;
        return this;
    }
}
