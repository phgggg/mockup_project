package com.authen.domain.request;

import com.authen.domain.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUsersRequestDTO extends BaseDTO {
    private String userName;
    private String fullName;
}
