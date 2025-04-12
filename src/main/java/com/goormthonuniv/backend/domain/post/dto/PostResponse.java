package com.goormthonuniv.backend.domain.post.dto;

import com.goormthonuniv.backend.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private String nickname;

    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getImageUrl(),
                post.getAuthor().getNickname()
        );
    }
}