package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.ProjectEntity;
import com.itsol.mockup.entity.TeamEntity;
import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.services.ProjectService;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.web.dto.project.ProjectDTO;
import com.itsol.mockup.web.dto.project.ProjectStatusDTO;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.itsol.mockup.utils.Constants.months;

@Service
public class ProjectServiceImpl extends BaseService implements ProjectService {

    @Override
    public ArrayResultDTO<ProjectEntity> findAll(Integer pageSize, Integer page) {
        logger.info("=== START FIND ALL PROJECT");
        ArrayResultDTO arrayResultDTO = new ArrayResultDTO();
        List<ProjectDTO> lists = new ArrayList<>();
        try{
            Page<ProjectEntity> datas = projectRepository.findAll(PageRequest.of(page - 1, pageSize));
            if(datas != null) {
                if (datas.getContent().size() >0){
                    for (ProjectEntity projectEntity : datas.getContent()){
                        ProjectDTO projectDTO = modelMapper.map(projectEntity, ProjectDTO.class);
                        lists.add(projectDTO);
                    }
                    arrayResultDTO.setSuccess(lists,datas.getTotalElements(),datas.getTotalPages());
                    logger.info("=== FIND ALL PROJECT WITH RESPONSE: "+arrayResultDTO.getErrorCode());
                }
            }
        }catch (Exception e){
            arrayResultDTO.setFail("Fail");
            logger.error(e.getMessage());
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO addProject(String token, ProjectDTO projectDTO) {
        logger.info("===START ADD NEW PROJECT");
        BaseResultDTO baseResultDTO = new BaseResultDTO();
        try{
            String userName = tokenUtils.getUsernameFromToken(token);
            UsersEntity usersEntity = usersRepository.findUsersEntityByUserName(userName);
            if (usersEntity != null){
                projectDTO.setCreatedBy(usersEntity.getFullName());
                projectDTO.setCreatedDate(getCurTimestamp());
                ProjectEntity projectEntity = modelMapper.map(projectDTO, ProjectEntity.class);

                projectEntity = projectRepository.save(projectEntity);
                List<TeamEntity> lstTeam = projectDTO.getTeams();

                if(lstTeam != null){
                    for(TeamEntity team : lstTeam){
                        team.setProjectEntity(projectEntity);
                        teamRepository.save(team);
                    }

                }
                if(projectEntity.getProjectId() != null){
                    baseResultDTO.setSuccess();
                }
            }
            logger.info("=== ADD NEW PROJECT RESPONSE: "+ baseResultDTO.getErrorCode());
        }catch (Exception e){
            baseResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage());
        }
        return baseResultDTO;
    }

    @Override
    public BaseResultDTO updateProject(ProjectDTO projectDTO) {
        logger.info("=== START UPDATE PROJECT: ");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try{
            ProjectEntity projectEntity = projectRepository.getProjectEntityByProjectId(projectDTO.getProjectId());
            if(projectEntity.getProjectId() != null){
//                projectDTO.setCreatedDate(projectEntity.getCreatedDate());
//                projectDTO.setCreatedBy(projectEntity.getCreatedBy());
                projectEntity.setProjectName(projectDTO.getProjectName());
                projectEntity.setActualStartDate(projectDTO.getActualStartDate());
                projectEntity.setActualFinishDate(projectDTO.getActualFinishDate());
                projectEntity.setModifiedDate(getCurTimestamp());
                projectEntity.setModifiedBy("");
                projectEntity.setDeadline(projectDTO.getDeadline());
                projectEntity.setStartDate(projectDTO.getStartDate());
                projectEntity.setStatus(projectDTO.getStatus());
                projectEntity.setDescription(projectDTO.getDescription());
//                projectEntity = modelMapper.map(projectDTO, ProjectEntity.class);
                projectRepository.save(projectEntity);
                singleResultDTO.setSuccess();
            }else {
                singleResultDTO.setFail("Fail");
            }
            logger.info("=== UPDATE PROJECT RESPONSE: "+ singleResultDTO.getErrorCode());
        }catch (Exception e) {
                singleResultDTO.setFail(e.getMessage());
                logger.error(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO deleteProject(Long id) {
        logger.info("=== START DELETE PROJECT");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try {
            if (id != null) {
                projectRepository.deleteById(id);
                singleResultDTO.setSuccess();
            } else {
                singleResultDTO.setFail("Fail");
            }
            logger.info("=== DELETE PROJECT RESPONE: "+ singleResultDTO.getErrorCode());
        }catch (Exception e){
            singleResultDTO.setFail(e.getMessage());
            logger.info(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO findProjectById(Long id) {
        logger.info("START FIND PROJECT BY ID");
        SingleResultDTO<ProjectEntity> resultDTO = new SingleResultDTO<>();
        try {
            ProjectEntity projectEntity = projectRepository.getProjectEntityByProjectId(id);
            if (projectEntity != null){
                resultDTO.setSuccess(projectEntity);
            }
            logger.info(" FIND PROJECT BY ID RESPONSE: {}", resultDTO.getErrorCode());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            resultDTO.setFail(e.getMessage());
        }
        return resultDTO;
    }

    @Override
    public BaseResultDTO getProjectStatus(Long id, int month, int page, int pageSize) {
        SingleResultDTO result = new SingleResultDTO<>();
        ProjectStatusDTO projectStatusDTO = null;
        String des = "";
        logger.info("Tiến độ dự án của tháng " + month);
        try{
            logger.info("khoi tao va truy van du lieu");
            ProjectEntity currentPrj = projectRepository.getProjectEntityByProjectId(id);
            if(DataUtils.getMonthFromTimestamp((Timestamp) currentPrj.getCreatedDate()) > month){
                result.setFail("project chua duoc tao vao thang nay");
                return result;
            }
            List<TimeSheetEntity> timesheetList = timesheetRepository.findTimeSheetEntitiesByProjectId(currentPrj.getProjectId());
            Date deadline = currentPrj.getDeadline();
            Date currentDate = new Date();
            long daysLeft = 0;
            daysLeft = DataUtils.dayDiff(deadline, currentDate);
            des += "Project status of " + months[month-1]+"\n";
            if (currentDate.before(deadline)) {
                des+= "project is ongoing\n";
            } else {
                des+= "project is past the deadline by " + daysLeft*(-1) + "\n";
            }

            long totalDays = DataUtils.dayDiff(deadline, currentPrj.getActualStartDate());
            double progress = (double) ((int) (((double) (totalDays - daysLeft) / totalDays) * 100)) /100;
            double taskDone = 0, taskOnGoing = 0, monthTaskDone = 0, monthTaskOnGoing = 0, monthTaskToDo = 0;
            List<TimesheetDTO> tasksStatus = new ArrayList<>();

            logger.info("lay ket qua tung task");
            for (TimeSheetEntity timesheetObj : timesheetList) {
                switch (timesheetObj.getStatus()){
                    case 2:
                        taskDone++;break;
                    case 1:
                        taskOnGoing++;break;
                }
                long totalDaysOfTask = 0, daysLeftOfTask  = 0;
                totalDaysOfTask = DataUtils.getDateDiff(timesheetObj.getStartDateExpected(), (Timestamp) deadline, TimeUnit.DAYS);
                daysLeftOfTask = DataUtils.getDateDiff(getCurTimestamp(), (Timestamp) deadline, TimeUnit.DAYS);
                if(daysLeftOfTask<0 ){
                    logger.info("past the deadline");
                    daysLeftOfTask = 0;
                }
                double progressOfTaskByTime = (double) ((int) (((double) (totalDaysOfTask - daysLeftOfTask) / totalDaysOfTask) * 100)) /100;
                if(DataUtils.getMonthFromTimestamp(timesheetObj.getFinishDateExpected()) == month ||
                        DataUtils.getMonthFromTimestamp(timesheetObj.getActualFinishDate()) == month){

                    des += "Task " + timesheetObj.getTask()
                            + " of user " + timesheetObj.getUsersEntity().getUserName()
                            + " status: " + timesheetObj.getStatus() + "\n";
                    String comment = TaskComment(timesheetObj);
                    String[] splitComment = splitInfo(comment.replace(", Task is ",""));
                    TimesheetDTO timesheetDTO = modelMapper.map(timesheetObj, TimesheetDTO.class);
                    timesheetDTO.setTaskComment(splitComment);
                    timesheetDTO.setAssignedUser(timesheetObj.getUsersEntity());
                    timesheetDTO.setDaysLeft(daysLeftOfTask);
                    timesheetDTO.setDescription("none");
                    timesheetDTO.setCreatedBy(null);
                    timesheetDTO.setProgressByTime(progressOfTaskByTime);
                    switch (timesheetObj.getStatus()){
                        case 2:
                            monthTaskDone++;
                            timesheetDTO.setProgressBySubTask(1);
                            break;
                        case 1:
                            monthTaskOnGoing++;
                            try{
                                timesheetDTO.setProgressBySubTask(subTaskRepository.subTaskDoneCount(timesheetObj.getTimesheetId()));
                            }catch (Exception e){
                                timesheetDTO.setProgressBySubTask(0);
                            }
                            break;
                        default:
                            monthTaskToDo++;
                            timesheetDTO.setProgressBySubTask(0);
                            break;
                    }
                    logger.info("task " + timesheetObj.getTimesheetId() + " nay hoan thanh duoc " + timesheetDTO.getProgressBySubTask());
                    tasksStatus.add(timesheetDTO);
                }
            }

            logger.info("tinh % hoan thanh theo tung tieu chi");
            taskDone = Math.round(taskDone/timesheetList.size() * 100.0) / 100.0;
            taskOnGoing = Math.round(taskOnGoing/timesheetList.size() * 100.0) / 100.0;
            double taskToDo =Math.round((1 - taskDone - taskOnGoing) * 100.0) / 100.0;

            double taskTotalInMonth = monthTaskDone+monthTaskToDo+monthTaskOnGoing;
            logger.info("tinh rieng trong thang"+taskTotalInMonth + "\t" + monthTaskDone + "\t" + monthTaskOnGoing + "\t" + monthTaskToDo);
            double taskDonePercentInMonth = (double) Math.round(monthTaskDone * 100 / taskTotalInMonth) /100;
            double taskOnGoingPercentInMonth = (double) Math.round(monthTaskOnGoing * 100 / taskTotalInMonth) /100;
            double taskToDoPercentInMonth = (double) Math.round(monthTaskToDo * 100 / taskTotalInMonth) /100;
            logger.info("%done " + taskDonePercentInMonth + "\t% ongoing " + taskOnGoingPercentInMonth+ "\t% pending " + taskToDoPercentInMonth);

            monthTaskDone = Math.round(monthTaskDone/timesheetList.size() * 100.0) / 100.0;
            monthTaskOnGoing = Math.round(monthTaskOnGoing/timesheetList.size() * 100.0) / 100.0;
            monthTaskToDo = Math.round(monthTaskToDo/timesheetList.size() * 100.0) / 100.0;

            des += Math.round(monthTaskDone * 100) + "% tasks done\n"
                    + Math.round(monthTaskOnGoing * 100) + "% tasks on going\n"
                    + Math.round(monthTaskToDo * 100) + "% tasks pending\n";
            List<TimesheetDTO> tasksStatusPage = null;
            try{
                tasksStatusPage  = tasksStatus.subList(page*pageSize,(page+1)*pageSize);
            }catch (Exception ee){
                try{
                    tasksStatusPage  = tasksStatus.subList(page*pageSize,tasksStatus.size());
                }catch (Exception r){
                    logger.info("error while getting sublist, get full list instead");
                    tasksStatusPage = tasksStatus;
                }
            }

            projectStatusDTO = new ProjectStatusDTO(
                    modelMapper.map(currentPrj, ProjectDTO.class),
                    totalDays,daysLeft,progress,
                    taskDone,taskOnGoing,taskToDo,
                    monthTaskDone,monthTaskOnGoing,monthTaskToDo,
                    taskDonePercentInMonth, taskOnGoingPercentInMonth, taskToDoPercentInMonth,
                    tasksStatusPage, des

            );
            result.setSuccess(projectStatusDTO);
            logger.info("export to excel: ");
            Sheet sheet = excelUtil.sheetCreate("project status", projectStatusDTO);
            excelUtil.sheetWriteSingleData(sheet, projectStatusDTO);
            logger.info("create sheet task status");
            Sheet taskStatusSheet = excelUtil.sheetCreate("taskStatus", new TimesheetDTO());
            logger.info("write into task status");
            excelUtil.sheetWriteListData(taskStatusSheet, tasksStatus);
            logger.info("out");
            excelUtil.out("result");

        } catch (Exception e){
            result.setFail("error while getting list of timesheets{}",e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.info("\n{}", projectStatusDTO.getStatusInfo());
        return result;
    }

}
