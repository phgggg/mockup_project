package com.authen.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class BaseDTO implements Serializable {
    protected Integer pageSize;
    protected Integer page;
    protected String sort;
}
