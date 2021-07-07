package com.example.BoardSystem.Entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String origiFilename;

    @Column(nullable = false)
    private String Filename;

    @Column(nullable = false)
    private String filePath;

    @Builder
    public File(Long id, String origiFilename, String Filename, String filePath){
        this.id = id;
        this.origiFilename = origiFilename;
        this.Filename = Filename;
        this.filePath = filePath;
    }

}
