package com.goormthonuniv.backend.domain.post.controller;

import com.goormthonuniv.backend.domain.post.dto.*;
import com.goormthonuniv.backend.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @PostMapping
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 작성합니다.")
    public ResponseEntity<PostCreateResponse> createPost(
            @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(postService.createPost(request, username));
    }

    @GetMapping
    @Operation(summary = "전체 게시글 조회", description = "모든 게시글을 최신순으로 조회합니다.")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/me")
    @Operation(summary = "내 게시글 조회", description = "로그인한 사용자의 게시글을 조회합니다.")
    public ResponseEntity<List<PostResponse>> getMyPosts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(postService.getMyPosts(username));
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다. 작성자 본인만 수정할 수 있습니다.")
    public ResponseEntity<PostUpdateResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(postService.updatePost(postId, request, username));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다. 작성자 본인만 삭제할 수 있으며, 이미지도 함께 삭제됩니다.")
    public ResponseEntity<PostDeleteResponse> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        PostDeleteResponse response = postService.deletePost(postId, username);
        return ResponseEntity.ok(response);
    }

}
