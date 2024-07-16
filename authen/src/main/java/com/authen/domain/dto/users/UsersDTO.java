package com.authen.domain.dto.users;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Getter
@Setter
public class UsersDTO {
    private Long userId;
    private String userName;
    private String passWord;
    private String fullName;
    private String email;
    private String phone;
    private String skypeName;
    private String facebookUrl;
    private String address;
    private String education;
    private String university;
    private String faculty;
    private Date graduationDate;
    private Integer active;
    private String reason;
    private Integer imageId;
    private Integer levelId;


}
