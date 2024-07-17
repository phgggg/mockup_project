package com.itsol.mockup.web.dto.timesheet;

import com.itsol.mockup.web.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddTaskDTO extends BaseDTO {
    private String userName;
    private TimesheetDTO timesheetDTO;
    private Long projectId;
}
