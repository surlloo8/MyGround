package com.study.board.controller;

import com.study.board.entity.Post;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/board/list")
    public String getBoardList(@PageableDefault(page = 0, size = 6, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword, Model model) {

        Page<Post> posts = boardService.getPostsByPage(pageable, searchKeyword);


        int blockLimit = 5;
        int nowPage = posts.getPageable().getPageNumber() + 1;
        int startPage = ((nowPage - 1) / blockLimit) * blockLimit + 1;
        int endPage = Math.min(startPage + blockLimit - 1, posts.getTotalPages());

        model.addAttribute("posts", posts);
        model.addAttribute("blockLimit", blockLimit);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }
    @GetMapping("/board/write")
    public String boardWriteForm() {

        return "boardwrite";

    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Post post, Model model, MultipartFile file) throws Exception {

        if (post.getTitle() == null || post.getTitle().isEmpty()) {
            model.addAttribute("message", "제목을 입력하세요.");
            model.addAttribute("searchUrl", "/board/write");
            return "message";
        } if (post.getContent() == null || post.getContent().isEmpty()) {
            model.addAttribute("message", "내용을 입력하세요.");
            model.addAttribute("searchUrl", "/board/write");
            return "message";
        } if (file == null || file.isEmpty()) {
            model.addAttribute("message", "파일을 등록하세요.");
            model.addAttribute("searchUrl", "/board/write");
            return "message";
        }

        boardService.write(post, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {

        model.addAttribute("post", boardService.boardView(id));
        return "boardlist";

    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {

        boardService.boardDelete(id);

        // 삭제완료 메시지창
        model.addAttribute("message", "삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("post", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Post post, MultipartFile file, Model model) throws Exception {

        if (post.getTitle() == null || post.getTitle().isEmpty()) {
            model.addAttribute("message", "제목을 입력하세요.");
            model.addAttribute("searchUrl", "/board/modify/" +id);
            return "message";
        } if (post.getContent() == null || post.getContent().isEmpty()) {
            model.addAttribute("message", "내용을 입력하세요.");
            model.addAttribute("searchUrl", "/board/modify/" +id);
            return "message";
        } if (file == null || file.isEmpty()) {
            model.addAttribute("message", "파일을 등록하세요.");
            model.addAttribute("searchUrl", "/board/modify/" +id);
            return "message";
        }

        Post postTemp = boardService.boardView(id);
        postTemp.setTitle(post.getTitle());
        postTemp.setContent(post.getContent());
        postTemp.setUser_id(post.getUser_id());
        postTemp.setImg_path(post.getImg_path());
        postTemp.setUrl_path(post.getUrl_path());

        boardService.updatePost(postTemp);
        boardService.write(postTemp, file);

        // 수정완료 메시지창
        model.addAttribute("message", "수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    // 작성시간
    @PostMapping("/board/view")
    public String create_date(Post post, Model model) {
        boardService.savePostwithCurrentTime(post);
        return "redirect:/board/view";
    }


}