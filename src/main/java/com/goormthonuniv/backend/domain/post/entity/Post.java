package com.goormthonuniv.backend.domain.post.entity;

import com.goormthonuniv.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Entity
@Getter
// protected 설정 → 외부에서 직접 생성 못 하게 함
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    // 지연 로딩 설정: Post 조회 시 User는 실제 접근할 때까지 로딩하지 않음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    public Post(String title, String content, String imageUrl, User author) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
