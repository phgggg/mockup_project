package com.itsol.mockup.web.dto.timesheet;

import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.entity.UsersEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class SubTaskDTO {
    private Long id;
    private String taskName;
    private String description;
    private Double hoursSpent;
    private Double estimatedHours;
    private String priority;
    private String status;
    private Timestamp detailDate;
    private String comments;
    private UsersEntity assignedUser;
    private TimeSheetEntity timeSheetEntity;
}