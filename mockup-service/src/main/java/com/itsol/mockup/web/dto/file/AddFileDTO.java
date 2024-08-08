package com.itsol.mockup.web.dto.file;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFileDTO {
    private String username;
    private Long newVerOf;
    private Long projectId;

}
