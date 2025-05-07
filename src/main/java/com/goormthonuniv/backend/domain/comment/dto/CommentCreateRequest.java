package com.goormthonuniv.backend.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {

    @Schema(description = "댓글 내용", example = "좋은 글이네요!")
    @NotBlank
    private String content;

    @Schema(description = "게시글 ID", example = "1")
    @NotNull
    private Long postId;
}
