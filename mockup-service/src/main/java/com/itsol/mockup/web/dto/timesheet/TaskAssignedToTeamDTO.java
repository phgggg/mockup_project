package com.itsol.mockup.web.dto.timesheet;

import com.itsol.mockup.web.dto.users.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskAssignedToTeamDTO {
    private List<UsersDTO> user;
    private List<TaskAssignedToTeamListDTO> taskAssignedToTeamListDTOS;

    public TaskAssignedToTeamDTO() {
    }

    public TaskAssignedToTeamDTO(List<UsersDTO> user, List<TaskAssignedToTeamListDTO> taskAssignedToTeamListDTOS) {
        this.user = user;
        this.taskAssignedToTeamListDTOS = taskAssignedToTeamListDTOS;
    }
}
