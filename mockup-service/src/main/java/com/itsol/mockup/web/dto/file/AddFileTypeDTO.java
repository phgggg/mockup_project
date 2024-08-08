package com.itsol.mockup.web.dto.file;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFileTypeDTO {
    private Long fileId;
    private int[] fileTypeList;
}
