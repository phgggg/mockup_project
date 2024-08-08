package com.itsol.mockup.web.dto.file;

import com.itsol.mockup.web.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileSearchDTO  extends BaseDTO {
//    protected Integer pageSize;
//    protected Integer page;
//    protected String sort;
    protected String keyword;
    protected String projectName;

}
