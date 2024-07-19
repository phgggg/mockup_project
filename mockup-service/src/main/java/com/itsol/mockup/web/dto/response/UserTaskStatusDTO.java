package com.itsol.mockup.web.dto.response;

import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.web.dto.project.ProjectDTO;
import com.itsol.mockup.web.dto.project.ProjectStatusDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import com.itsol.mockup.web.dto.users.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class UserTaskStatusDTO extends BaseResultDTO {
    private UsersDTO user;
    private Timestamp requestDate;
    private String requestBy;
    private Long daysLeft;
    private double taskDone;
    private double taskOnGoing;
    private double taskPending;
    private List<TimesheetDTO> tasksStatus;
    private String statusInfo;

    public void setSuccess(UsersDTO user, double taskDone, double taskOngoing, double taskPending, long daysLeft, Timestamp currentTimestamp) {
        super.setSuccess();
        this.user = user;
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskPending = taskPending;
        this.daysLeft = daysLeft;
        this.requestDate = currentTimestamp;
    }

    public void setSuccess(UsersDTO user,List<TimesheetDTO> tasksStatus, double taskDone, double taskOngoing, double taskPending, long daysLeft, Timestamp currentTimestamp) {
        super.setSuccess();
        this.user = user;
        this.tasksStatus = tasksStatus;
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskPending = taskPending;
        this.daysLeft = daysLeft;
        this.requestDate = currentTimestamp;
    }

}
