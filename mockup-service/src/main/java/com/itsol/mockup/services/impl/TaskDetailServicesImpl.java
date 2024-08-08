package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.SubTaskEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.services.TaskDetailServices;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import com.itsol.mockup.web.dto.timesheet.SubTaskDTO;
import org.springframework.stereotype.Service;

@Service
public class TaskDetailServicesImpl extends BaseService implements TaskDetailServices {
    @Override
    public BaseResultDTO addTaskDetail(String token, SubTaskDTO subTaskDTO) {
        return null;
    }

    @Override
    public BaseResultDTO updateTaskDetail(String token, SubTaskDTO subTaskDTO) {
        return null;
    }

    @Override
    public BaseResultDTO findById(Long id) {
        return null;
    }

    @Override
    public BaseResultDTO searchAllTaskDetail(Integer pageSize, Integer page) {
        return null;
    }

    @Override
    public BaseResultDTO deleteTaskDetail(Long id) {
        return null;
    }

    @Override
    public BaseResultDTO assignSubTaskToUser(String userName, Long id) {
        SingleResultDTO result = new SingleResultDTO();
        try{
            UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
            SubTaskEntity subTask;
            if(subTaskRepository.findById(id).isPresent()){
                subTask = subTaskRepository.findById(id).get();
                subTask.setAssignedUser(user);
                subTask.setLastUpdated(getCurTimestamp());
                subTaskRepository.save(subTask);
                result.setSuccess(subTask);
                return result;
            }
            result.setFail("ERROR WHILE GETTING SUBTASK");

        } catch (Exception e){
            result.setFail("ERROR WHILE GETTING USER/SUBTASK");
            logger.info(e.getMessage());
        }
        return null;
    }
}
