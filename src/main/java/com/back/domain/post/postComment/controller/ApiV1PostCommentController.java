package com.back.domain.post.postComment.controller;

import com.back.domain.member.entity.Member;
import com.back.domain.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.domain.post.postComment.service.PostCommentService;
import com.back.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ApiV1PostCommentController {

    private final PostService postService;
    private final PostCommentService postCommentService;
    private final MemberService memberService;

    // 다건 조회
    @GetMapping
    @Transactional(readOnly = true)
    public List<PostCommentDto> getItems(
            @PathVariable int postId
    ) {
        Post post = postService.findById(postId).get();

        List<PostComment> postComments = post.getComments();

        return postComments
                .stream()
                .map(PostCommentDto::new)
                .toList();
    }

    // 단건 조회
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostCommentDto getItem(
            @PathVariable int postId,
            @PathVariable int id,
            RedirectAttributes redirectAttributes) {
        Post post = postService.findById(postId).get();

        PostComment postComment = post.findCommentById(id).get();

        return new PostCommentDto(postComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> deleteComment(
            @PathVariable int postId,
            @PathVariable int id
    ) {
        Post post = postService.findById(postId).get(); // 글찾기

        PostComment postComment = post.findCommentById(id).get(); // 댓글 찾기

        postService.deleteComment(post, postComment); // 이 삭제는 객체수준의 삭제이므로 DB수준의 삭제 하려먼 @Transactional 추가

        // 응답
        return new RsData<>(
                "200-1",
                "%d번 댓글이 삭제 되었습니다.".formatted(id)
        );
    }

    record PosrCommentReqBody(
            @NotNull
            @Size(min = 2, max = 100)
            String content
    ) {
    }

    // 댓글 작성
    @PostMapping
    public RsData<PostCommentDto> writeComment(
            @PathVariable int postId,
            @Valid @RequestBody PosrCommentReqBody reqBody
    ) {
        Member author = memberService.findByUsername("user1").get(); // 임시로 user1으로 작성
        Post post = postService.findById(postId).get();
        PostComment postComment = postCommentService.write(author, post, reqBody.content);

        return new RsData<>(
                "200-1",
                "%d번 글에 댓글이 작성되었습니다.".formatted(postId),
                new PostCommentDto(postComment)
        );
    }


}
