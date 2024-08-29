package com.itsol.mockup.web.dto.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import com.itsol.mockup.web.dto.users.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleDTO {
    private Long roleId;
    private String name;
    @JsonIgnore
    private List<UsersDTO> usersEntities;
    @JsonIgnore
    private List<TimesheetDTO> timeSheetEntities;
}