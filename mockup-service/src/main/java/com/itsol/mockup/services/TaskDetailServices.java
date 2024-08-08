package com.itsol.mockup.services;

import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.timesheet.SubTaskDTO;

public interface TaskDetailServices {
    BaseResultDTO addTaskDetail(String token, SubTaskDTO subTaskDTO);
    BaseResultDTO updateTaskDetail(String token, SubTaskDTO subTaskDTO);
    BaseResultDTO findById(Long id);
    BaseResultDTO searchAllTaskDetail(Integer pageSize, Integer page);
    BaseResultDTO deleteTaskDetail(Long id);
    BaseResultDTO assignSubTaskToUser(String userName, Long id);
}
