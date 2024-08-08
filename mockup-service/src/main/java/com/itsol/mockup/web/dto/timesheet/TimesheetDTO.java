package com.itsol.mockup.web.dto.timesheet;

import com.itsol.mockup.entity.SubTaskEntity;
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
    private String details;
    private Integer status;
    private Long projectId;
    private UsersEntity createdBy;
    private Timestamp lastUpdate;
    private UsersEntity assignedUser;
    private List<SubTaskEntity> subTasks;

    private Long daysLeft;
    private double progressByTime;
    private String description;
    private String[] taskComment;
    private double progressBySubTask;

    public TimesheetDTO(){
        this.task = "none";
        this.result = "none";
        this.note = "none";
        this.details = "none";
        this.status = 0;
        this.createdBy = null;
        this.assignedUser = null;
        this.daysLeft = (long) -1;
        this.progressBySubTask = -1;
        this.progressByTime = -1;
        this.description = "none";
    }

}
