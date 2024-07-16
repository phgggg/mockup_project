package com.itsol.mockup.web.dto.issue;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class IssueDTO {
    private Long issueId;

    private String issueName;

    private Date createdDate;

    private Integer status;

    private Date modifiedDate;
}
