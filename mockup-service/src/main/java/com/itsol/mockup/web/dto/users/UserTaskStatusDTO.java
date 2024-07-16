package com.itsol.mockup.web.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTaskStatusDTO {
    private String taskName;
    private Long totalDays;
    private Long daysLeft;
    private int status;
}
