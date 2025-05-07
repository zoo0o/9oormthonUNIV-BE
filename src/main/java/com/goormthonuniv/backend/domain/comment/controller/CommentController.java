package com.goormthonuniv.backend.domain.comment.controller;

import com.goormthonuniv.backend.domain.comment.dto.CommentCreateRequest;
import com.goormthonuniv.backend.domain.comment.dto.CommentCreateResponse;
import com.goormthonuniv.backend.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "로그인한 사용자가 댓글을 작성합니다.")
    public ResponseEntity<CommentCreateResponse> createComment(
            @RequestBody CommentCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        return ResponseEntity.ok(commentService.createComment(request, username));
    }
}
