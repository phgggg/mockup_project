package com.itsol.mockup.web.dto.timesheet;

import lombok.Getter;

import java.sql.Timestamp;
@Getter
public class WorkLoadRequestDTO {
    private String userName;
    private Timestamp timestamp;
}
