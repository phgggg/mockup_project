package com.itsol.mockup.web.dto.project;

import com.itsol.mockup.entity.ProjectEntity;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProjectStatusDTO extends BaseResultDTO {
    private ProjectDTO projectDTO;
    private Long totalDays;
    private Long daysLeft;
    private double progressByTime;
    private double taskDone;
    private double taskOnGoing;
    private double taskPending;
    private List<TimesheetDTO> tasksStatus;
    private String statusInfo;

    protected void setSuccess(ProjectDTO projectDTO) {
        this.projectDTO = projectDTO;
    }

    protected void setSuccess(double taskDone, double taskOngoing, double taskPending, long daysLeft) {
        super.setSuccess();
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskPending = taskPending;
        this.daysLeft = daysLeft;
    }

    protected void setSuccess(List<TimesheetDTO> tasksStatus, double taskDone, double taskOngoing, double taskPending, long daysLeft) {
        super.setSuccess();
        this.tasksStatus = tasksStatus;
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskPending = taskPending;
        this.daysLeft = daysLeft;
    }

}
