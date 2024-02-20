package com.study.board.repository;

import com.study.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Post, Integer> { // extends(상속받는다는 뜻) JpaRepository<엔티티, 지정한 primary key의 타입>

    Page<Post> findByTitleContaining(String searchKeyword, Pageable pageable);
}
