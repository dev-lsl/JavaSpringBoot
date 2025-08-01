package com.back.domain.member.entity;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    private String nickName;
    @Column(unique = true)
    private String apiKey;

    // 생성자
    public Member(String username, String password, String nickName) {
        this.username = username;
        this.password = password;
        this.nickName = nickName;
        this.apiKey = UUID.randomUUID().toString(); // 랜덤 API 키 생성
    }

    public String getName() {
        return nickName;
    }
}
