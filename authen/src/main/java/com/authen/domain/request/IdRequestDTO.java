package com.authen.domain.request;

import com.authen.domain.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IdRequestDTO extends BaseDTO {
    Long id;
    List<Long> ids;
}
