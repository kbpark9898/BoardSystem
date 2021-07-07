package com.example.BoardSystem.Controller;

import com.example.BoardSystem.Service.BoardService;
import com.example.BoardSystem.Service.FileService;
import com.example.BoardSystem.dto.BoardDto;
import com.example.BoardSystem.dto.FileDto;
import com.example.BoardSystem.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class BoardController {
    private BoardService boardService;
    private FileService fileService;
    public BoardController(BoardService boardService, FileService fileService){
        this.fileService = fileService;
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
    public String write(@RequestParam("file") MultipartFile file, BoardDto boardDto) {
        try {
            String origFilename = file.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "/files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch(Exception e){
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "/" + filename;
            file.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOrigFilename(origFilename);
            fileDto.setFilename(filename);
            fileDto.setFilePath(filePath);

            Long fileId = fileService.saveFile(fileDto);
            boardDto.setFileID(fileId);
            boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable("id") Long id, Model model){
        BoardDto boardDto = boardService.getBoard(id);
        Long fileid = boardDto.getFileID();
        FileDto fileDto = fileService.getFile(fileid);
        String fileName = fileDto.getOrigFilename();
        model.addAttribute("post", boardDto);
        model.addAttribute("filename", fileName);
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

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }
}
