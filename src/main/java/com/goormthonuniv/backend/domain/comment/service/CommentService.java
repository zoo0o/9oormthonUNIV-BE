package com.goormthonuniv.backend.domain.comment.service;

import com.goormthonuniv.backend.domain.comment.dto.CommentCreateRequest;
import com.goormthonuniv.backend.domain.comment.dto.CommentCreateResponse;
import com.goormthonuniv.backend.domain.comment.entity.Comment;
import com.goormthonuniv.backend.domain.comment.repository.CommentRepository;
import com.goormthonuniv.backend.domain.post.entity.Post;
import com.goormthonuniv.backend.domain.post.repository.PostRepository;
import com.goormthonuniv.backend.domain.user.entity.User;
import com.goormthonuniv.backend.domain.user.repository.UserRepository;
import com.goormthonuniv.backend.global.kafka.CommentProducer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentProducer commentProducer;

    @Transactional
    public CommentCreateResponse createComment(CommentCreateRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("❌ 사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("❌ 게시글을 찾을 수 없습니다."));

        Comment comment = new Comment(request.getContent(), post, user);
        Comment saved = commentRepository.save(comment);

        // Kafka 메시지 전송
        String kafkaMessage = String.format(
                "User[%s] commented on Post[%d]: %s",
                user.getUsername(), post.getId(), request.getContent()
        );
        commentProducer.sendComment(kafkaMessage);

        return new CommentCreateResponse(saved.getId());
    }
}
