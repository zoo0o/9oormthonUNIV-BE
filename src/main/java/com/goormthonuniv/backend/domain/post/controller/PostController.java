package com.goormthonuniv.backend.domain.post.controller;

import com.goormthonuniv.backend.domain.post.dto.PostCreateRequest;
import com.goormthonuniv.backend.domain.post.dto.PostCreateResponse;
import com.goormthonuniv.backend.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 작성합니다.")
    public ResponseEntity<PostCreateResponse> createPost(@RequestBody PostCreateRequest request,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.createPost(request, userDetails.getUsername()));
    }
}
