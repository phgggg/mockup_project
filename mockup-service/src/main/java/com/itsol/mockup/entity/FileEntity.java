package com.itsol.mockup.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "FILE")
@Getter
@Setter
public class FileEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "file_seq")
    @SequenceGenerator(name = "file_seq", sequenceName = "file_seq",allocationSize = 1)
    @Column(name = "FILE_ID")
    private Long fileId;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_URL")
    private String fileUrl;

    @Column(name = "ALLOWED_USER")
    private ArrayList<Long> allowedUser;

    @Column(name= "PREVIOUS_VER")
    private Long previousVer = 0L;

    @Column(name= "PREVIOUS_URL")
    private String previousUrl;

    @Column(name= "NEXT_VER")
    private Long nextVer = 0L;

    @Column(name= "NEXT_URL")
    private String nextUrl;

    public FileEntity() {
    }

    public FileEntity(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }


}