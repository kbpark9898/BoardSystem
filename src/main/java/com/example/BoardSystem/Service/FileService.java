package com.example.BoardSystem.Service;

import com.example.BoardSystem.Entity.File;
import com.example.BoardSystem.Repository.FileRepository;
import com.example.BoardSystem.dto.FileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long saveFile(FileDto fileDto){
        return fileRepository.save(fileDto.toEntity()).getId();
    }
    @Transactional
    public FileDto getFile(Long id){
        File file = fileRepository.findById(id).get();

        FileDto fileDto= FileDto.builder()
                .filename(file.getFilename())
                .filePath(file.getFilePath())
                .origFilename(file.getOrigiFilename())
                .id(file.getId())
                .build();

        return fileDto;
    }
}
