package com.authen.domain.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author anhvd_itsol
 */

@Getter
@Setter
public class ResultDTO {
    private String errorCode;
    private String description;
    private Object data;
    private Long totalRow = 0L;
    private Integer totalPage = 0;
}
