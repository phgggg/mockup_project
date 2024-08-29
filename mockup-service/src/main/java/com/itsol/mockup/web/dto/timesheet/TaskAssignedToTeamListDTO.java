package com.itsol.mockup.web.dto.timesheet;

import com.itsol.mockup.web.dto.users.UsersDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAssignedToTeamListDTO {
    private UsersDTO userAssigned;
    private Double risk;
    private String description;
    private Integer status;
    private Integer totalWorkloadEstimated;
    private TimesheetDTO timesheetDTO;

    public TaskAssignedToTeamListDTO(UsersDTO userAssigned, Double risk, String description, Integer status, Integer totalWorkloadEstimated, TimesheetDTO timesheetDTO) {
        this.userAssigned = userAssigned;
        this.risk = risk;
        this.description = description;
        this.status = status;
        this.totalWorkloadEstimated = totalWorkloadEstimated;
        this.timesheetDTO = timesheetDTO;
    }
}
