package com.goormthonuniv.backend.domain.post.repository;

import com.goormthonuniv.backend.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc(); // 전체 조회 (최신순)
    List<Post> findAllByAuthor_UsernameOrderByIdDesc(String username); // 내가 쓴 글만 조회
}
