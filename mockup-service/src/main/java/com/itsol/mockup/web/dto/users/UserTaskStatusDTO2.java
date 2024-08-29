package com.itsol.mockup.web.dto.users;

import com.itsol.mockup.web.dto.timesheet.TimesheetStatusDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class UserTaskStatusDTO2 extends UserTaskStatusDTO{
    private double workload;
    private int workloadStatus;

    public UserTaskStatusDTO2(UsersDTO user, double taskDone, double taskOngoing, double taskPending, long daysLeft, Timestamp currentTimestamp) {
        super(user, taskDone, taskOngoing, taskPending, daysLeft, currentTimestamp);
    }

    public UserTaskStatusDTO2(UsersDTO user, List<TimesheetStatusDTO> tasksStatus, double taskDone, double taskOngoing, double taskPending, long daysLeft, Timestamp currentTimestamp, double monthTaskDoneInTotal, double monthTaskOnGoingInTotal, double monthTaskPendingInTotal, int taskTotal, String description, String requestBy) {
        super(user, tasksStatus, taskDone, taskOngoing, taskPending, daysLeft, currentTimestamp, monthTaskDoneInTotal, monthTaskOnGoingInTotal, monthTaskPendingInTotal, taskTotal, description, requestBy);
    }

    public UserTaskStatusDTO2(UsersDTO user, List<TimesheetStatusDTO> tasksStatus, double taskDone, double taskOngoing, double taskPending, long daysLeft, Timestamp currentTimestamp, double monthTaskDoneInTotal, double monthTaskOnGoingInTotal, double monthTaskPendingInTotal, double monthTaskDoneInMonth, double monthTaskOnGoingInMonth, double monthTaskPendingInMonth, int taskTotal, String description, String requestBy) {
        super(user, tasksStatus, taskDone, taskOngoing, taskPending, daysLeft, currentTimestamp, monthTaskDoneInTotal, monthTaskOnGoingInTotal, monthTaskPendingInTotal, monthTaskDoneInMonth, monthTaskOnGoingInMonth, monthTaskPendingInMonth, taskTotal, description, requestBy);
    }
}
