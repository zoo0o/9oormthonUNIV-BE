package com.goormthonuniv.backend.domain.post.service;

import com.goormthonuniv.backend.domain.post.dto.PostCreateRequest;
import com.goormthonuniv.backend.domain.post.dto.PostCreateResponse;
import com.goormthonuniv.backend.domain.post.entity.Post;
import com.goormthonuniv.backend.domain.post.repository.PostRepository;
import com.goormthonuniv.backend.domain.user.entity.User;
import com.goormthonuniv.backend.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 게시글 작성
     *
     * @param request   게시글 요청 DTO
     * @param username  로그인한 사용자명 (UserDetails에서 가져온 값)
     */
    public PostCreateResponse createPost(PostCreateRequest request, String username) {
        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + username));

        // 게시글 생성
        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                request.getImageUrl(),
                user
        );

        // 저장
        Post savedPost = postRepository.save(post);

        // 응답 반환
        return new PostCreateResponse(savedPost.getId());
    }
}
