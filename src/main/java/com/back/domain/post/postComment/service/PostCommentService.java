package com.back.domain.post.postComment.service;

import com.back.domain.member.entity.Member;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.domain.post.postComment.repository.PostCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;

    @Transactional
    public PostComment write(Member author, Post post, String content) {
        PostComment postComment = post.addComment(author, content);
        postCommentRepository.save(postComment);
        return postComment;
    }
}
