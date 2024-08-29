package com.itsol.mockup.web.dto.risk;

import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class RiskDTO {
    private Long id;
    private String name;
    private Timestamp create_date;
    private String note;
    private List<TimesheetDTO> riskOfTimesheet;
}
