package com.back.global.initData;

import com.back.domain.member.entity.Member;
import com.back.domain.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    @Autowired
    @Lazy
    private BaseInitData self;
    @Autowired
    private final PostService postService;
    @Autowired
    private final MemberService memberService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        // 유저 생성
        if (memberService.count() > 0) return;

        memberService.join("system", "1234", "시스템");
        memberService.join("admin", "1234", "관리자");
        memberService.join("user1", "1234", "유저1");
        memberService.join("user2", "1234", "유저2");
        memberService.join("user3", "1234", "유저3");

        // 게시글 생성
        if (postService.count() > 0) return;

        Member member1 = memberService.findByUsername("user1").get();
        Member member2 = memberService.findByUsername("user2").get();
        Member member3 = memberService.findByUsername("user3").get();

        Post post1 = postService.write(member1, "루이", "루이는 바보다.");
        Post post2 = postService.write(member2, "레오", "레오는 겁쟁이다.");
        Post post3 = postService.write(member2, "워니", "산니미는 워니꺼다.");

        post1.addComment(member1, "루이 귀요미");
        post1.addComment(member1, "루이 멍충이");
        post1.addComment(member2, "루이 장난꾸러기");
        post2.addComment(member3, "레오 바보");
        post2.addComment(member3, "레오 쨔잉나");
    }


}