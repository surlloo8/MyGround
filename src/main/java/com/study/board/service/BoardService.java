package com.study.board.service;

import com.study.board.entity.Post;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired // 스프링 빈 알아서 읽어와서 선언하지 않아도 주입해주는 어노테이션
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Page<Post> getPostsByPage(Pageable pageable, String searchKeyword) {
        // 검색어에 따른 게시물을 페이지별로 가져오는 메서드
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            return boardRepository.findByTitleContaining(searchKeyword, pageable);
        } else {
            return boardRepository.findAll(pageable);
        }
    }
    // 글 작성 처리
    public void write(Post post, MultipartFile file) throws Exception{

        String projectPath = System.getProperty("user.dir") + "\\src\\main\\webapp";

        UUID uuid = UUID.randomUUID();

        String img_path = uuid + "_" + file.getOriginalFilename();

        File saveFile = new File(projectPath, img_path);

        file.transferTo(saveFile);

        post.setImg_path("/webapp/" + img_path);

        boardRepository.save(post);
    }

    // 특정 게시글 불러오기
    public Post boardView(Integer id) {

        return boardRepository.findById(id).get();
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        boardRepository.deleteById(id);
    }

    // 게시글 작성 시간
    public void savePostwithCurrentTime(Post post) {
        post.setCreate_date(LocalDateTime.now());
        boardRepository.save(post);
    }

    // 게시글 수정 시간
    public void updatePost(Post post) {
        post.setUpdate_date(LocalDateTime.now());
        boardRepository.save(post);
    }

}