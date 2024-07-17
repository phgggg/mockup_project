package com.itsol.mockup.web.dto.timesheet;

import com.itsol.mockup.entity.TaskDetailEntity;
import com.itsol.mockup.entity.UsersEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class TimesheetDTO {
    private Long timesheetId;
    private String task;
    private Timestamp createdDate;
    private Timestamp startDateExpected;
    private Timestamp actualStartDate;
    private Timestamp finishDateExpected;
    private Timestamp actualFinishDate;
    private String result;
    private String note;
    private Integer status;
    private Long projectId;
    private UsersEntity createdBy;
    private Timestamp lastUpdate;
    private UsersEntity assignedUser;
    private List<TaskDetailEntity> taskDetails;
}
