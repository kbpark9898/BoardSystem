package com.example.BoardSystem.Controller;

import com.example.BoardSystem.Service.BoardService;
import com.example.BoardSystem.dto.BoardDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BoardController {
    private BoardService boardService;
    public BoardController(BoardService boardService){
        this.boardService = boardService;
    }
    @GetMapping("/")
    public String list(Model model) {
        List<BoardDto> list = boardService.getBoardList();
        model.addAttribute("postList", list);
        return "board/list.html";
    }

    @GetMapping("/post")
    public String post() {
        return "board/post.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model){
        BoardDto boardDto = boardService.getBoard(id);
        model.addAttribute("post", boardDto);
        return "board/detail.html";
    }

    @GetMapping("/post/edit/{id}")
    public String getEditPage(@PathVariable("id") Long id, Model model){
        BoardDto boardDto = boardService.getBoard(id);
        model.addAttribute("post", boardDto);
        return "board/edit.html";
    }

    @PutMapping("/post/edit/{id}")
    public String update(BoardDto boardDto){
        boardService.savePost(boardDto);
        return "redirect:/";

    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id, Model model){
        boardService.deletePost(id);
        return "redirect:/";
    }
}
