package com.itsol.mockup.web.dto.timesheet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedDTO {
    private Double risk;
    private String description;
    private Integer status;
    private Integer totalWorkloadEstimated;
    private TimesheetDTO timesheetDTO;
//    private Long userLevel;
//    private Long userRole;

}
