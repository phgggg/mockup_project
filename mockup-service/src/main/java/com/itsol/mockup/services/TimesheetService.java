package com.itsol.mockup.services;

import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.timesheet.SubTaskDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetStatusDTO;

import java.sql.Timestamp;

public interface TimesheetService {
    BaseResultDTO findAll(Integer pageSize, Integer page);
    BaseResultDTO addTimesheet(TimesheetStatusDTO timesheetStatusDTO, String token);
    BaseResultDTO updateTimesheet(TimesheetStatusDTO timesheetStatusDTO);
    BaseResultDTO deleteTimesheet(Long id);
    BaseResultDTO searchTimesheetByUser(String token, Integer pageSize, Integer page);
    BaseResultDTO updateStatusTimeSheet(Long id, Integer status);
    BaseResultDTO getTimesheetById(Long id);
    BaseResultDTO getTimesheetByName(String name);
    BaseResultDTO addTaskDetail(SubTaskDTO subTaskDTO, String userName, Long timeSheetId);
    BaseResultDTO monthlyWorkloadTrackingByUser(String userName, int month);

    BaseResultDTO optimizedTaskAssignmentForUser(String userName, Timestamp ts);
    BaseResultDTO optimizedTaskAssignmentByMonth(Long id, Timestamp ts);
    BaseResultDTO confirmUserTaskForMonth(String userName, Timestamp ts, int status, int curStatus);

    BaseResultDTO transferTasksOfUser(Long id, String name, Timestamp ts);

    BaseResultDTO assignTaskToUser(String userName, Long timeSheetId);

}
