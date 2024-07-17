package com.itsol.mockup.services;

import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.timesheet.TaskDetailDTO;

public interface TaskDetailServices {
    BaseResultDTO addTaskDetail(String token, TaskDetailDTO taskDetailDTO);
    BaseResultDTO updateTaskDetail(String token, TaskDetailDTO taskDetailDTO);
    BaseResultDTO findById(Long id);
    BaseResultDTO searchAllTaskDetail(Integer pageSize, Integer page);
    BaseResultDTO deleteTaskDetail(Long id);
}
