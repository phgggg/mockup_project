package com.itsol.mockup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;

@Entity
@Indexed
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

    @Column(name = "ACTUAL_NAME")
    @Field(termVector = TermVector.YES)
    private String actualName;

    @Column(name = "FILE_TYPE_LIST")
    private int[] fileTypeList;

    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

    @Column(name = "FILE_URL")
    private String fileUrl;

    @Column(name = "UPLOADED_BY")
    private String uploadedBy;

    @Column(name = "UPLOADED_DATE")
    private Timestamp uploadedDate;

    @Column(name = "ALLOWED_USER")
    private ArrayList<Long> allowedUser;

    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;

    @Column(name= "PREVIOUS_VER")
    private Long previousVer = 0L;

    @Column(name= "PREVIOUS_URL")
    private String previousUrl;

    @Column(name= "NEXT_VER")
    private Long nextVer = 0L;

    @Column(name= "NEXT_URL")
    private String nextUrl;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    @JsonIgnore
    private ProjectEntity project;

    public FileEntity() {
    }

    public FileEntity(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public FileEntity(String actualName, String fileName,
                      int[] fileTypeList, String fileExtension, String fileUrl,
                      String uploadedBy, Timestamp uploadedDate,
                      ArrayList<Long> allowedUser,
                      String lastModifiedBy, Timestamp lastModifiedDate,
                      ProjectEntity project) {
        this.actualName = actualName;
        this.fileName = fileName;
        this.fileTypeList = fileTypeList;
        this.fileExtension = fileExtension;
        this.fileUrl = fileUrl;
        this.uploadedBy = uploadedBy;
        this.uploadedDate = uploadedDate;
        this.allowedUser = allowedUser;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.project = project;
    }
}