package com.itsol.mockup.web.dto.timesheet;

import com.itsol.mockup.web.dto.users.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskAssignedListDTO {
    private List<Integer> assignedTaskIdList;
    private UsersDTO user;
    private List<TaskAssignedDTO> taskAssignedDTOS;

    public TaskAssignedListDTO() {
    }

    public TaskAssignedListDTO(UsersDTO user, List<Integer> assignedTaskIdList, List<TaskAssignedDTO> taskAssignedDTOS) {
        this.user = user;
        this.assignedTaskIdList = assignedTaskIdList;
        this.taskAssignedDTOS = taskAssignedDTOS;
    }
}
