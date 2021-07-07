package com.example.BoardSystem.Service;

import com.example.BoardSystem.Entity.Board;
import com.example.BoardSystem.Repository.BoardRepository;
import com.example.BoardSystem.dto.BoardDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    @Transactional
    public List<BoardDto> getBoardList(){
        List<Board> list = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();

        for(Board board : list) {
            BoardDto boardDto = BoardDto.builder()
                    .id(board.getId())
                    .author(board.getAuthor())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .createdDate(board.getCreatedDate())
                    .build();
            boardDtoList.add(boardDto);
        }
        return boardDtoList;
    }

    @Transactional
    public BoardDto getBoard(Long id){
        Board board = boardRepository.getById(id);
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .author(board.getAuthor())
                .content(board.getContent())
                .title(board.getTitle())
                .createdDate(board.getCreatedDate())
                .fileID(board.getFileID())
                .build();
        return boardDto;
    }

    @Transactional
    public void deletePost(Long id){
        boardRepository.deleteById(id);
    }
}
