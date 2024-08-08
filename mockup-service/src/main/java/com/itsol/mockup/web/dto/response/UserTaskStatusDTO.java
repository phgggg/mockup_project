package com.itsol.mockup.web.dto.response;

import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import com.itsol.mockup.web.dto.users.UsersDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
@Getter
@Setter
public class UserTaskStatusDTO{
    private UsersDTO user;
    private Timestamp requestDate;
    private String requestBy;
    private Long daysLeft;
    private double taskDone;
    private double taskOnGoing;
    private double taskPending;
    private double monthTaskDoneInTotal;
    private double monthTaskOnGoingInTotal;
    private double monthTaskPendingInTotal;
    private double monthTaskDoneInMonth;
    private double monthTaskOnGoingInMonth;
    private double monthTaskPendingInMonth;
    private List<TimesheetDTO> tasks;
    private int taskTotal;
    private String statusInfo;
//    private double monthTaskDoneInMonthCount;
//    private double monthTaskOnGoingInMonthCount;
//    private double monthTaskPendingInMonthCount;
//xoa
    public UserTaskStatusDTO(UsersDTO user, double taskDone, double taskOngoing, double taskPending, long daysLeft, Timestamp currentTimestamp) {
        this.user = user;
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskPending = taskPending;
        this.daysLeft = daysLeft;
        this.requestDate = currentTimestamp;
    }

    public UserTaskStatusDTO(UsersDTO user, List<TimesheetDTO> tasksStatus,
                             double taskDone, double taskOngoing, double taskPending,
                             long daysLeft, Timestamp currentTimestamp,
                             double monthTaskDoneInTotal, double monthTaskOnGoingInTotal, double monthTaskPendingInTotal,
                             int taskTotal, String description, String requestBy) {
        this.user = user;
        this.tasks = tasksStatus;
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskPending = taskPending;
        this.daysLeft = daysLeft;
        this.requestDate = currentTimestamp;
        this.monthTaskDoneInTotal = monthTaskDoneInTotal;
        this.monthTaskOnGoingInTotal = monthTaskOnGoingInTotal;
        this.monthTaskPendingInTotal = monthTaskPendingInTotal;
        this.taskTotal = taskTotal;
        this.statusInfo = description;
        this.requestBy = requestBy;
    }

    public UserTaskStatusDTO(UsersDTO user, List<TimesheetDTO> tasksStatus,
                             double taskDone, double taskOngoing, double taskPending,
                             long daysLeft, Timestamp currentTimestamp,
                             double monthTaskDoneInTotal, double monthTaskOnGoingInTotal, double monthTaskPendingInTotal,
                             double monthTaskDoneInMonth, double monthTaskOnGoingInMonth, double monthTaskPendingInMonth,
                             int taskTotal, String description, String requestBy) {
        this.user = user;
        this.tasks = tasksStatus;
        this.taskDone = taskDone;
        this.taskOnGoing = taskOngoing;
        this.taskPending = taskPending;
        this.daysLeft = daysLeft;
        this.requestDate = currentTimestamp;
        this.monthTaskDoneInTotal = monthTaskDoneInTotal;
        this.monthTaskOnGoingInTotal = monthTaskOnGoingInTotal;
        this.monthTaskPendingInTotal = monthTaskPendingInTotal;
        this.monthTaskDoneInMonth = monthTaskDoneInMonth;
        this.monthTaskOnGoingInMonth = monthTaskOnGoingInMonth;
        this.monthTaskPendingInMonth = monthTaskPendingInMonth;
        this.taskTotal = taskTotal;
        this.statusInfo = description;
        this.requestBy = requestBy;
    }

//    public UserTaskStatusDTO(UsersDTO user, List<TimesheetDTO> tasksStatus,
//                             double taskDone, double taskOngoing, double taskPending,
//                             long daysLeft, Timestamp currentTimestamp,
//                             double monthTaskDoneInTotal, double monthTaskOnGoingInTotal, double monthTaskPendingInTotal,
//                             double monthTaskDoneInMonth, double monthTaskOnGoingInMonth, double monthTaskPendingInMonth,
//                             int taskTotal, String description, String requestBy,
//                             double monthTaskDoneInMonthCount, double monthTaskOnGoingInMonthCount, double monthTaskPendingInMonthCount) {
//        this.user = user;
//        this.tasks = tasksStatus;
//        this.taskDone = taskDone;
//        this.taskOnGoing = taskOngoing;
//        this.taskPending = taskPending;
//        this.daysLeft = daysLeft;
//        this.requestDate = currentTimestamp;
//        this.monthTaskDoneInTotal = monthTaskDoneInTotal;
//        this.monthTaskOnGoingInTotal = monthTaskOnGoingInTotal;
//        this.monthTaskPendingInTotal = monthTaskPendingInTotal;
//        this.monthTaskDoneInMonth = monthTaskDoneInMonth;
//        this.monthTaskOnGoingInMonth = monthTaskOnGoingInMonth;
//        this.monthTaskPendingInMonth = monthTaskPendingInMonth;
//        this.taskTotal = taskTotal;
//        this.statusInfo = description;
//        this.requestBy = requestBy;
//        this.monthTaskDoneInMonthCount = monthTaskDoneInMonthCount;
//        this.monthTaskOnGoingInMonthCount = monthTaskOnGoingInMonthCount;
//        this.monthTaskPendingInMonthCount = monthTaskPendingInMonthCount;
//    }

}
