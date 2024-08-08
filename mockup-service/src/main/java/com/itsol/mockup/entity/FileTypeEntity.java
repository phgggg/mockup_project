package com.itsol.mockup.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "FileType")
@Getter
@Setter
public class FileTypeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "file_type_seq")
    @SequenceGenerator(name = "file_type_seq", sequenceName = "file_type_seq",allocationSize = 1)
    @Column(name = "FILE_TYPE_ID")
    private Long fileTypeId;
    @Column(name = "FILE_TYPE_NAME")
    private String fileTypeName;
    @Column(name = "FILE_TYPE_INFO")
    private String fileTypeInfo;

    public FileTypeEntity() {
    }

    public FileTypeEntity(String fileTypeName, String fileTypeInfo) {
        this.fileTypeName = fileTypeName;
        this.fileTypeInfo = fileTypeInfo;
    }
}
