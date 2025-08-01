package com.back.domain.post.post.controller;

import com.back.domain.member.entity.Member;
import com.back.domain.member.service.MemberService;
import com.back.domain.post.post.dto.PostDto;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.repository.PostRepository;
import com.back.domain.post.post.service.PostService;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "글 다건 조회")
    public List<PostDto> getItems() {
        List<Post> items = postService.findAll();

        return items
                .stream()
                .map(p -> new PostDto(p))
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostDto getItem(@PathVariable int id) {
        Post item = postService.findById(id).get();

        return new PostDto(item);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> deletePost(
            @PathVariable int id
    ) {
        Post post = postService.findById(id).get();

        postService.delete(post);

        return new RsData<>(
                "200-1",
                "%d번 글이 삭제되었습니다.".formatted(id)
        );
    }

    record PostWriteReqBody(
            @NotBlank
            @Size(min = 2, max = 100)
            String title,
            @NotBlank
            @Size(min = 2, max = 5000)
            String content
    ) {
    }

    record PostWriteResBody(
            long totalCount,
            PostDto post
    ) {
    }

    // 글 작성
    @PostMapping
    @Transactional
    public RsData<PostWriteResBody> write(
            @Valid @RequestBody PostWriteReqBody reqBody
    ) {
        Member author = memberService.findByUsername("user1").get(); // 임시로 user1으로 작성

        Post post = postService.write(author ,reqBody.title, reqBody.content);

        return new RsData<>(
                "201-1",
                "%d번 글이 작성되었습니다.".formatted(post.getId()),
                new PostWriteResBody(postService.count(), new PostDto(post))
        );
    }


    record PostModifyReqBody(
            @NotBlank
            @Size(min = 2, max = 100)
            String title,
            @NotBlank
            @Size(min = 2, max = 5000)
            String content
    ) {
    }

    // 글 수정
    @PutMapping("/{id}")
    @Transactional
    public RsData<Void> modify(
            @PathVariable int id,
            @Valid @RequestBody PostModifyReqBody reqBody
    ) {
        Post post = postService.findById(id).get();

        postService.modify(post, reqBody.title(), reqBody.content());

        return new RsData<>(
                "200-1",
                "%d번 글이 수정되었습니다.".formatted(id)
        );
    }

    private final MemberService memberService;
}
