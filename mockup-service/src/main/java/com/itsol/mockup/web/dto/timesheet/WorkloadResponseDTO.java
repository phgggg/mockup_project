package com.itsol.mockup.web.dto.timesheet;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class WorkloadResponseDTO {
    private Long totalWorkingTimeEstimated;
    private Long totalWorkingTimeToDo;
    private Long totalWorkingTimeSpent;
    private Long totalWorkingTimeSpentPerTaskDone;
    private Integer totalWorkloadStatus;
    private Double actualProgressByTime;
    private Timestamp timestamp;

}
