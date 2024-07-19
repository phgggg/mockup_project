package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.ProjectEntity;
import com.itsol.mockup.entity.TeamEntity;
import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.repository.ProjectRepository;
import com.itsol.mockup.repository.TeamRepository;
import com.itsol.mockup.repository.TimesheetRepository;
import com.itsol.mockup.repository.UsersRepository;
import com.itsol.mockup.services.ProjectService;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.utils.TokenUtils;
import com.itsol.mockup.web.dto.project.ProjectDTO;
import com.itsol.mockup.web.dto.project.ProjectStatusDTO;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProjectServiceImpl extends BaseService implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ModelMapper modelMapper;

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
                projectDTO.setCreatedDate(projectEntity.getCreatedDate());
                projectDTO.setCreatedBy(projectEntity.getCreatedBy());
                projectEntity = modelMapper.map(projectDTO, ProjectEntity.class);
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
    public BaseResultDTO getProjectStatus(Long id) {
        ProjectStatusDTO resultDTO = new ProjectStatusDTO();

        try{
            ProjectEntity currentPrj = projectRepository.getProjectEntityByProjectId(id);
            List<TimeSheetEntity> timesheetList = timesheetRepository.findTimeSheetEntitiesByProjectId(currentPrj.getProjectId());

            Date deadline = currentPrj.getDeadline();
            Date currentDate = new Date();

            long daysLeft = 0;
            daysLeft = DataUtils.dayDiff(deadline, currentDate);
            if (currentDate.before(deadline)) {
                resultDTO.setStatusInfo(daysLeft + " days until the deadline\n");
            } else {
                resultDTO.setStatusInfo(resultDTO.getStatusInfo()+ "project is past the deadline by " + daysLeft*(-1) + "\n");
            }
            resultDTO.setDaysLeft(daysLeft);

            long totalDays = DataUtils.dayDiff(deadline, currentPrj.getStartDate());

            resultDTO.setTotalDays(totalDays);
            resultDTO.setProgressByTime((double) (totalDays - daysLeft) / totalDays);

            double taskDone = 0, taskOnGoing = 0;
            for (TimeSheetEntity timesheetObj : timesheetList) {
                resultDTO.setStatusInfo(resultDTO.getStatusInfo()+
                        "Task " + timesheetObj.getTask()
                        + " of user " + timesheetObj.getUsersEntity().getUserName()
                        + " status: " + timesheetObj.getStatus() + "\n");
                if (timesheetObj.getStatus() == 2) taskDone++;
                if (timesheetObj.getStatus() == 1) taskOnGoing++;
            }

            taskDone = Math.round(taskDone/timesheetList.size() * 100.0) / 100.0;
            taskOnGoing = Math.round(taskOnGoing/timesheetList.size() * 100.0) / 100.0;
            double taskPending =Math.round((1 - taskDone - taskOnGoing) * 100.0) / 100.0;

            resultDTO.setTaskDone(taskDone);
            resultDTO.setTaskOnGoing(taskOnGoing);
            resultDTO.setTaskPending(taskPending);

            resultDTO.setStatusInfo(resultDTO.getStatusInfo()+ taskDone * 100 + "% tasks done\n");
            resultDTO.setStatusInfo(resultDTO.getStatusInfo()+ taskOnGoing * 100 + "% tasks on going\n");
            resultDTO.setStatusInfo(resultDTO.getStatusInfo()+ taskPending * 100 + "% tasks pending\n");
            resultDTO.setProjectDTO(convertToDto(currentPrj));
            resultDTO.setSuccess();
        } catch (Exception e){
            resultDTO.setFail("error while getting list of timesheets{}",e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.info("\n{}", resultDTO.getStatusInfo());
        return resultDTO;
    }

    private ProjectDTO convertToDto(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = modelMapper.map(projectEntity, ProjectDTO.class);
        return projectDTO;
    }
}
