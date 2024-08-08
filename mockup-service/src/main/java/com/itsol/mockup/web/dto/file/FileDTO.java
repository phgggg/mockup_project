package com.itsol.mockup.web.dto.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itsol.mockup.entity.ProjectEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.TermVector;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;

@Getter
@Setter
public class FileDTO {
    private Long fileId;
    private String fileName;
    private String actualName;
    private String fileUrl;
    private String uploadedBy;
    private Timestamp uploadedDate;
    private ArrayList<Long> allowedUser;
    private String lastModifiedBy;
    private Timestamp lastModifiedDate;
    private Long previousVer = 0L;
    private String previousUrl;
    private Long nextVer = 0L;
    private String nextUrl;
    private String projectName;
}
