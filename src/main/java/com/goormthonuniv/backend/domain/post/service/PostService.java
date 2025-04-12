package com.goormthonuniv.backend.domain.post.service;

import com.goormthonuniv.backend.domain.post.dto.*;
import com.goormthonuniv.backend.domain.post.entity.Post;
import com.goormthonuniv.backend.domain.post.repository.PostRepository;
import com.goormthonuniv.backend.domain.user.entity.User;
import com.goormthonuniv.backend.domain.user.repository.UserRepository;

import com.goormthonuniv.backend.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    /**
     * 게시글 작성
     *
     */
    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request, MultipartFile image, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + username));

        String imageUrl = (image != null && !image.isEmpty()) ? s3Uploader.upload(image) : null;

        Post post = new Post(
                request.getTitle(),
                request.getContent(),
                imageUrl,
                user
        );

        Post savedPost = postRepository.save(post);

        return new PostCreateResponse(savedPost.getId());
    }

    /**
     * 전체 게시글 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 내 게시글 조회
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getMyPosts(String username) {
        return postRepository.findAllByAuthor_UsernameOrderByIdDesc(username).stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest request, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }

        post.update(request.getTitle(), request.getContent(), request.getImageUrl());

        return new PostUpdateResponse(post.getId());
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public PostDeleteResponse deletePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다.");
        }

        // 이미지 삭제
        s3Uploader.delete(post.getImageUrl());

        // 삭제 후 ID 기억
        Long deletedId = post.getId();

        // 게시글 삭제
        postRepository.delete(post);

        return new PostDeleteResponse(deletedId);
    }
}
