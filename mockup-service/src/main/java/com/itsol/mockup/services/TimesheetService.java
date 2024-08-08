package com.itsol.mockup.services;

import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.timesheet.SubTaskDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;

import java.sql.Timestamp;

public interface TimesheetService {
    BaseResultDTO findAll(Integer pageSize, Integer page);
    BaseResultDTO addTimesheet(TimesheetDTO timesheetDTO, String token);
    BaseResultDTO updateTimesheet(TimesheetDTO timesheetDTO);
    BaseResultDTO deleteTimesheet(Long id);
    BaseResultDTO searchTimesheetByUser(String token, Integer pageSize, Integer page);
    BaseResultDTO updateStatusTimeSheet(Long id, Integer status);
    BaseResultDTO getTimesheetById(Long id);
    BaseResultDTO getTimesheetByName(String name);
    BaseResultDTO addTaskDetail(SubTaskDTO subTaskDTO,
                                String userName,
                                Long timeSheetId);
    BaseResultDTO weeklyWorkloadTrackingByUser(String userName, Timestamp timestamp);
    BaseResultDTO monthlyWorkloadTrackingByUser(String userName, Timestamp timestamp);

    BaseResultDTO optimizedSubTaskAssignmentByProjectId(Long id, Timestamp ts);
    BaseResultDTO transferSubTasksOfUser(Long id, String name, Timestamp ts);

    BaseResultDTO optimizedTaskAssignmentByProjectId(Long id, Timestamp ts);
    BaseResultDTO transferTasksOfUser(Long id, String name, Timestamp ts);

    BaseResultDTO assignTaskToUser(String userName, Long timeSheetId);

    BaseResultDTO optimizedTaskAssignmentByProjectIdWeekly(Long id, Timestamp ts);
}
