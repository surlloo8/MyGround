package com.study.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity // class가 db에 있는 테이블이라는 의미의 어노테이션
@Data
public class Post {
    @Id // primary key를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY가 MariaDB에서 사용하는 것, sequence는 오라클
    @Column(name = "post_id")
    private Integer postId;

    private String user_id;

    private String title;

    private String content;

    private String url_path;

    private String img_path;

    @CreationTimestamp
    private LocalDateTime create_date = LocalDateTime.now();

    @UpdateTimestamp
    private LocalDateTime update_date = LocalDateTime.now();
}
