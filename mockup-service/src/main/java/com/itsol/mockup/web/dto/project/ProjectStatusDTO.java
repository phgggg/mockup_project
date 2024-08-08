package com.itsol.mockup.web.dto.project;

import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProjectStatusDTO {
    private ProjectDTO projectDTO;
    private Long totalDays;
    private Long daysLeft;
    private double progressByTime;
    private double taskDone;
    private double taskOnGoing;
    private double taskToDo;
    private double monthTaskDoneInTotal;
    private double monthTaskOnGoingInTotal;
    private double monthTaskToDoInTotal;
    private double monthTaskDoneInMonth;
    private double monthTaskOnGoingInMonth;
    private double monthTaskToDoInMonth;
    private List<TimesheetDTO> tasksStatus;
    private String statusInfo;

    public ProjectStatusDTO(ProjectDTO projectDTO) {
        this.projectDTO = projectDTO;
    }

    public ProjectStatusDTO(double taskDone, double taskOngoing, double taskToDo, long daysLeft) {

        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskToDo = taskToDo;
        this.daysLeft = daysLeft;
    }

    public ProjectStatusDTO(List<TimesheetDTO> tasksStatus, double taskDone, double taskOngoing, double taskToDo, long daysLeft) {

        this.tasksStatus = tasksStatus;
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskToDo = taskToDo;
        this.daysLeft = daysLeft;
    }

    public ProjectStatusDTO(){

    }



}
