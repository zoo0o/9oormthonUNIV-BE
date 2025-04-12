package com.goormthonuniv.backend.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @Schema(description = "게시글 제목", example = "오늘의 기록")
    @NotBlank
    private String title;

    @Schema(description = "게시글 내용", example = "햇살이 따뜻한 하루였어요.")
    @NotBlank
    private String content;

    public PostCreateRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
