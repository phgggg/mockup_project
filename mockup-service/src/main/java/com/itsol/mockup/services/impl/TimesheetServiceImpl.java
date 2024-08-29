package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.*;
import com.itsol.mockup.repository.SubTaskRepositoryCustom;
import com.itsol.mockup.services.TimesheetService;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import com.itsol.mockup.web.dto.timesheet.*;
import com.itsol.mockup.web.dto.users.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.itsol.mockup.utils.Constants.*;
import static com.itsol.mockup.utils.GeneticAlgorithm.*;

/**
 *
 */
@Service
public class TimesheetServiceImpl extends BaseService implements TimesheetService {

    @Autowired
    SubTaskRepositoryCustom subTaskRepositoryCustom;

    @Override
    public BaseResultDTO findAll(Integer pageSize, Integer page) {
        logger.info("=== START FIND ALL TIMESHEET::");
        ArrayResultDTO<Object> arrayResultDTO = new ArrayResultDTO<>();
        try {
            List<Object> lstResult = new ArrayList<>();
            Page<TimeSheetEntity> rawsData = timesheetRepository.findAll(PageRequest.of(page - 1, pageSize));
            if (rawsData != null) {
                if (rawsData.getContent().size() > 0) {
                    rawsData.getContent().forEach(i -> {
                        TimesheetStatusDTO dto = modelMapper.map(i, TimesheetStatusDTO.class);
                        lstResult.add(dto);
                    });
                }
                arrayResultDTO.setSuccess(lstResult,rawsData.getTotalElements(),rawsData.getTotalPages());
                logger.info("=== FIND ALL TIMESHEET WITH RESPONSE: {}", arrayResultDTO.getErrorCode());
            }

        }catch (Exception e){
            logger.error("TIMESHEET Exception{}", e.getMessage(), e);
            arrayResultDTO.setFail(e.getMessage());
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO addTimesheet(TimesheetStatusDTO timesheetStatusDTO, String token) {
        logger.info("ADD NEW TIMESHEET");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        ProjectEntity projectEntity = projectRepository.getProjectEntityByProjectId(timesheetStatusDTO.getProjectId());
        UsersEntity usersEntity = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
        try {
            if(projectEntity.getDeadline().before(timesheetStatusDTO.getFinishDateExpected())){
                timesheetStatusDTO.setCreatedDate(getCurTimestamp());
                timesheetStatusDTO.setStatus(0);
                TimeSheetEntity timeSheetEntity = modelMapper.map(timesheetStatusDTO, TimeSheetEntity.class);
                timeSheetEntity.setAssignedUser(usersEntity);
                timeSheetEntity = timesheetRepository.save(timeSheetEntity);
                singleResultDTO.setSuccess(timeSheetEntity);
            }else {
                singleResultDTO.setFail("finish date after project deadline");
            }

        } catch (Exception e) {
            singleResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO updateTimesheet(TimesheetStatusDTO timesheetStatusDTO) {
        logger.info("UPDATE TIMESHEET");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try {
            TimeSheetEntity timeSheetEntity = timesheetRepository.getTimeSheetEntityByTimesheetId(timesheetStatusDTO.getTimesheetId());
            if (timeSheetEntity.getTimesheetId() != null) {
                timeSheetEntity = modelMapper.map(timesheetStatusDTO, TimeSheetEntity.class);
                timesheetRepository.save(timeSheetEntity);
                singleResultDTO.setSuccess(timeSheetEntity);
            }
            logger.info("UPDATE TIMESHEET RESPONSE: {}", singleResultDTO.getErrorCode());
        } catch (Exception e) {
            singleResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO deleteTimesheet(Long id) {
        logger.info("DELETE TIMESHEET");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try {
            if (id != null) {
                timesheetRepository.deleteById(id);
                singleResultDTO.setSuccess();
            }
        } catch (Exception e) {
            singleResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO searchTimesheetByUser(String token, Integer pageSize, Integer page) {
        ArrayResultDTO<TimesheetStatusDTO> arrayResultDTO = new ArrayResultDTO<>();
        List<TimesheetStatusDTO> list = new ArrayList<>();
        try {
            UsersEntity usersEntity = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
            Page<TimeSheetEntity> rawData = timesheetRepository.findTimeSheetEntitiesByAssignedUser(usersEntity, PageRequest.of(page, pageSize));
        if (rawData != null){
            if(rawData.getContent().size() > 0){
                rawData.getContent().forEach(timeSheetEntity -> {
                    TimesheetStatusDTO timesheetStatusDTO = modelMapper.map(timeSheetEntity, TimesheetStatusDTO.class);
                    list.add(timesheetStatusDTO);
                });
            }
            arrayResultDTO.setSuccess(list, rawData.getTotalElements(), rawData.getTotalPages());
            logger.info("=== FIND TIMESHEET BY ID USER RESPONSE::{}", arrayResultDTO.getErrorCode());
        }
        }catch (Exception e){
            logger.error("ERR searchTimesheetByUser{}", e.getMessage(), e);
            arrayResultDTO.setFail(e.getMessage());
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO updateStatusTimeSheet(Long id, Integer status) {
        BaseResultDTO baseResultDTO = new BaseResultDTO();
        try {
            TimeSheetEntity timeSheetEntity = timesheetRepository.getTimeSheetEntityByTimesheetId(id);
            if (timeSheetEntity != null){
                timeSheetEntity.setStatus(status);
                timeSheetEntity.setLastUpdate(getCurTimestamp());

                timesheetRepository.save(timeSheetEntity);
                baseResultDTO.setSuccess();
            }
        }catch (Exception e){
            logger.error("UPDATE STATUS TIMESHEET ERR: {}", e.getMessage(), e);
            baseResultDTO.setFail(e.getMessage());
        }
        return baseResultDTO;
    }

    @Override
    public BaseResultDTO getTimesheetById(Long id) {
        SingleResultDTO resultDTO = new SingleResultDTO<>();
        try{
            TimeSheetEntity timeSheetEntity = timesheetRepository.getTimeSheetEntityByTimesheetId(id);
            if(timeSheetEntity==null){
                resultDTO.setItemNotfound("No data");
            }else resultDTO.setSuccess(timeSheetEntity);
        }catch (Exception e){
            logger.info(e.getMessage());
            resultDTO.setFail(e.getMessage());
        }
        return resultDTO;
    }

    @Override
    public BaseResultDTO getTimesheetByName(String name) {
        ArrayResultDTO resultDTO = new ArrayResultDTO<>();
        try{
            List<TimeSheetEntity> timeSheetEntity = timesheetRepository.findTimeSheetEntitiesByTask(name);
            if(timeSheetEntity==null){
                resultDTO.setItemNotfound("No data");
            }else resultDTO.setSuccess(timeSheetEntity, (long) timeSheetEntity.size(),1);
        }catch (Exception e){
            logger.info(e.getMessage());
            resultDTO.setFail(e.getMessage());
        }
        return resultDTO;
    }

    @Override
    public BaseResultDTO addTaskDetail(SubTaskDTO subTaskDTO, String userName, Long timeSheetId) {
        logger.info("ADD TASK DETAIL");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        TimeSheetEntity timeSheetEntity = timesheetRepository.getTimeSheetEntityByTimesheetId(timeSheetId);
//        UsersEntity usersEntity = usersRepository.findUsersEntityByUserName(userName);
        UsersEntity usersEntity = timeSheetEntity.getAssignedUser();
        try {
            if(usersEntity!=null){
                SubTaskEntity subTask = modelMapper.map(subTaskDTO, SubTaskEntity.class);
                int month = DataUtils.getMonthFromTimestamp(timeSheetEntity.getFinishDateExpected());
                SingleResultDTO monthlyWorkload = (SingleResultDTO) monthlyWorkloadTrackingByUser(userName, month);
                WorkloadResponseDTO responseDTO = (WorkloadResponseDTO) monthlyWorkload.getData();
                long userWorkloadInWeek = (long) (responseDTO.getTotalWorkingTimeRemains() + subTask.getEstimatedHours());
                if(userWorkloadInWeek >= workingHoursPerMonth *1.1){
                    logger.info("Quá tải");
                    singleResultDTO.setFail("overwork");
                }else {
                    subTask.setLastUpdated(getCurTimestamp());
                    subTask.setUpdatedBy("");
                    subTask.setAssignedUser(usersEntity);
                    subTask.setTimeSheetEntity(timeSheetEntity);
                    subTask = subTaskRepository.save(subTask);
                    singleResultDTO.setSuccess(subTask);
                }
            }else {
                singleResultDTO.setFail("finish date after project deadline");
            }

        } catch (Exception e) {
            singleResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return singleResultDTO;
    }

//    @Override
//    public BaseResultDTO weeklyWorkloadTrackingByUser(String userName, Timestamp timestamp) {
//        SingleResultDTO<Object> result = new SingleResultDTO<>();
//        Timestamp mon = DataUtils.getDayOfWeek(timestamp, DayOfWeek.MONDAY);
//        Timestamp sat = DataUtils.getDayOfWeek(timestamp, DayOfWeek.SATURDAY);
//        logger.info("t2 la {} t7 la {}", mon, sat);
//        logger.info("get user and user tasks");
//        UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
//        Long totalWorkingTimeEstimated= 0L, totalWorkingTimeSpent = 0L, totalWorkingTimeSpentPerTaskDone = 0L;
//        logger.info("get estimated working hours");
//
//        try{
//            totalWorkingTimeEstimated = subTaskRepositoryCustom.getSumOfEstimatedHoursByUserId(user.getUserId(),mon,sat);
//            totalWorkingTimeSpent = subTaskRepositoryCustom.getSumOfHoursSpentByUserId(user.getUserId(),mon,sat);
//            totalWorkingTimeSpentPerTaskDone = subTaskRepositoryCustom.getSumOfHoursSpentPerTaskDoneByUserId(user.getUserId(),mon,sat);
//        }catch (Exception e){
//            logger.info(e.getMessage());
//        }
//        logger.info("weekly workload tracking");
//        WorkloadResponseDTO response = workloadCalc(workingHoursPerWeek, totalWorkingTimeEstimated,totalWorkingTimeSpent,totalWorkingTimeSpentPerTaskDone);
//
//        result.setSuccess(response);
//        return result;
//    }

    @Override
    public BaseResultDTO monthlyWorkloadTrackingByUser(String userName, int month) {
        SingleResultDTO<Object> result = new SingleResultDTO<>();
        int year = 2024;
        logger.info("Workload tracking for {}, year {}", months[month-1], year);
        logger.info("get month user and user tasks");
        UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
        Long totalWorkingTimeEstimated= 0L, totalWorkingTimeSpent = 0L, totalWorkingTimeSpentPerTaskDone = 0L;
        logger.info("get monthly estimated working hours");
        try{
            totalWorkingTimeEstimated = subTaskRepositoryCustom.getSumOfMonthlyEstimatedHoursByUserId(user.getUserId(),month,year);
            totalWorkingTimeSpent = subTaskRepositoryCustom.getSumOfMonthlyHoursSpentByUserId(user.getUserId(),month,year);
            totalWorkingTimeSpentPerTaskDone = subTaskRepositoryCustom.getSumOfMonthlyHoursSpentPerTaskDoneByUserId(user.getUserId(),month,year);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        logger.info("monthly workload tracking");
        WorkloadResponseDTO response = workloadCalc(workingHoursPerMonth, totalWorkingTimeEstimated, totalWorkingTimeSpent, totalWorkingTimeSpentPerTaskDone);

        result.setSuccess(response);
        return result;
    }

    private WorkloadResponseDTO workloadCalc(long workingHours ,Long totalWorkingTimeEstimated, Long totalWorkingTimeSpent, Long totalWorkingTimeSpentPerTaskDone){
        long totalWorkingTimeToDo;
        int totalWorkloadStatus;
        double actualProgressByTime;
        logger.info("Total estimated hours           = {}", totalWorkingTimeEstimated);
        logger.info("Total hours spent               = {}", totalWorkingTimeSpent);
        logger.info("Total hours spent per task done = {}", totalWorkingTimeSpentPerTaskDone);
        actualProgressByTime = (double) Math.round((float) (100 * totalWorkingTimeSpent) / totalWorkingTimeEstimated) / 100;
        logger.info("Hours remaining                 = {}", totalWorkingTimeEstimated - totalWorkingTimeSpent);
        totalWorkingTimeToDo = (totalWorkingTimeEstimated - totalWorkingTimeSpent);
        totalWorkloadStatus = (totalWorkingTimeEstimated < workingHours * 0.9) ? 1 :
                              (totalWorkingTimeEstimated > workingHours * 1.1) ? -1 : 0;
        String cmt = (totalWorkloadStatus == -1) ? " more than" : (totalWorkloadStatus == 1) ? " less than" : "";
        logger.info("Total workload is{} expected", cmt);
        if (totalWorkloadStatus == -1) {
            logger.info("Should be {} hours less", totalWorkingTimeEstimated - workingHours);
        }
        WorkloadResponseDTO response = new WorkloadResponseDTO(
                totalWorkingTimeEstimated, totalWorkingTimeToDo, totalWorkingTimeSpent, totalWorkingTimeSpentPerTaskDone,
                totalWorkloadStatus, actualProgressByTime, getCurTimestamp()
        );
        return response;

    }

    @Override
    public BaseResultDTO optimizedTaskAssignmentForUser(String userName, Timestamp ts) {
        int month = DataUtils.getMonthFromTimestamp(ts);
        logger.info("MONTH: {}",months[month]);
        SingleResultDTO<Object> result = new SingleResultDTO<>();
        try {
            UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
            List<TimeSheetEntity> taskEntities = timesheetRepository.getTimeSheetEntitiesByMonth(month);//subtask
            List<TimesheetDTO> taskDTOs = taskEntities.stream()
                    .map(taskEntity -> modelMapper.map(taskEntity, TimesheetDTO.class))
                    .collect(Collectors.toList());
            Iterator<TimeSheetEntity> iterator = taskEntities.iterator();
            while (iterator.hasNext()) {
                TimeSheetEntity task = iterator.next();
                if (timesheetRepository.getSumOfHoursSpentByTimeSheetId(task.getTimesheetId()) == 0 &&
                    user.getRoles().contains(task.getTimesheetRole()) &&  task.getStatus() == 0
                ) {
                    if(task.getOldAssignedUser() == null) task.setOldAssignedUser(task.getAssignedUser());
                    task.setAssignedUser(null);
                    timesheetRepository.save(task);
                } else {
                    iterator.remove();
                }
            }
            int taskCount = taskEntities.size();
            logger.info("timesheet SIZE {}", taskEntities.size());
            //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
            TaskAssignedListDTO rep = assignTasksForSingleUser(taskCount, user, taskEntities, ts, taskDTOs);
            result.setSuccess(rep);
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO optimizedTaskAssignmentByMonth(Long id, Timestamp ts) {
        int month = DataUtils.getMonthFromTimestamp(ts);
        logger.info("MONTH: {}.",months[month]);
        SingleResultDTO<Object> result = new SingleResultDTO<>();
        try {
            List<UsersEntity> users = usersRepository.findUsersInTeamByProjectId(id);
            List<TimeSheetEntity> taskEntities = timesheetRepository.getTimeSheetEntitiesByMonth(month);//subtask

            Iterator<TimeSheetEntity> iterator = taskEntities.iterator();
            while (iterator.hasNext()) {
                TimeSheetEntity task = iterator.next();
                if (timesheetRepository.getSumOfHoursSpentByTimeSheetId(task.getTimesheetId()) == 0 && task.getStatus() == 0)
                {
                    task.setAssignedUser(null);
                    timesheetRepository.save(task);
                } else
                {
                    iterator.remove();
                }
            }
            int taskCount = taskEntities.size();
            logger.info("timesheet SIZE {}.", taskEntities.size());
            //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
            TaskAssignedToTeamDTO bestAssignment = assignTasks(taskCount, users, taskEntities, ts);
            result.setSuccess(bestAssignment);
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO transferTasksOfUser(Long id, String name, Timestamp ts) {
        SingleResultDTO<Object> result = new SingleResultDTO<>();
        try {
            UsersEntity userToRemove = usersRepository.findUsersEntityByUserName(name);
            List<UsersEntity> users = usersRepository.findUsersInTeamByProjectId(id);
            users.remove(userToRemove);
            SingleResultDTO response = (SingleResultDTO) confirmUserTaskForMonth(name, ts, 0, 1);
            List<TimeSheetEntity> taskEntities = (List<TimeSheetEntity>) response.getData();
            int taskCount = taskEntities.size();
            for(TimeSheetEntity timeSheetEntity : taskEntities) {
                logger.info("{} {}", timeSheetEntity.getTimesheetId(), timeSheetEntity.getAssignedUser().getUserName());
                timeSheetEntity.setOldAssignedUser(userToRemove);
                timesheetRepository.save(timeSheetEntity);
                logger.info("task entity updated");
            }
            //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
            TaskAssignedToTeamDTO bestAssignment = assignTasks(taskCount, users, taskEntities, ts);
            result.setSuccess(bestAssignment);
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO confirmUserTaskForMonth(String userName, Timestamp ts, int status, int curStatus) {
        SingleResultDTO result = new SingleResultDTO<>();
        int month = DataUtils.getMonthFromTimestamp(ts);
        if(!(status >= 0 && status <=2) || !(curStatus >= 0 && curStatus <=2) ){
            result.setFail("status is not correct");
        }else {
            try {
                UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
                List<TimeSheetEntity> taskEntities = timesheetRepository.getAssignedTaskByMonthAndStatusAndUserId(month, curStatus, user.getUserId());//subtask
                logger.info("{} tasks not confirmed", taskEntities.size());
                for(TimeSheetEntity task : taskEntities){
                    task.setStatus(status);
                    logger.info("Task {} confirmed", task.getTimesheetId());
                    timesheetRepository.save(task);
                }
                result.setSuccess(taskEntities);
            }catch (Exception e){
                logger.info(e.getMessage());
                result.setFail(e.getMessage());
            }
        }
        return result;
    }

    private TaskAssignedListDTO assignTasksForSingleUser(int taskCount, UsersEntity user, List<TimeSheetEntity> taskEntities, Timestamp ts, List<TimesheetDTO> taskDTOs){
        int month = DataUtils.getMonthFromTimestamp(ts);
        int[] bestAssignment, taskRole = new int[taskCount], taskIdList = new int[taskCount],
                taskEstimatedHour = new int[taskCount], taskLevel = new int[taskCount];
        double[] taskRisk = new double[taskCount];
        List<RoleEntity> roleList;
        int currentWorkloads, userLevel;
        RiskFromUserEntity risk = user.getRiskFromUser();
        double riskMultiplier = risk.getMultiplier();

        for(int i = 0;i<taskCount;i++){
            Long a = taskEntities.get(i).getTimesheetRisk().get(0).getId();
            Long id = taskEntities.get(i).getTimesheetId();
            taskIdList[i] = Math.toIntExact(id);
            taskEstimatedHour[i] = timesheetRepository.getSumOfEstimatedHoursByTimeSheetId(id);
            LevelsEntity levelOfTask = levelsRepository.findLevelsEntityByLevelId(taskEntities.get(i).getLevelId());
            taskLevel[i] = Math.toIntExact(levelOfTask.getLevelValue());
            taskRole[i] = Math.toIntExact(taskEntities.get(i).getTimesheetRole().getRoleId());
            taskRisk[i] = riskRepository.getRiskMultiplierTotalByTimesheetId(taskEntities.get(i).getTimesheetId());
        }

        SingleResultDTO currentUserWorkload = (SingleResultDTO) monthlyWorkloadTrackingByUser(user.getUserName(), DataUtils.getMonthFromTimestamp(ts));
        WorkloadResponseDTO response = (WorkloadResponseDTO) currentUserWorkload.getData();
        currentWorkloads = Math.toIntExact(response.getTotalWorkingTimeRemains());

        LevelsEntity levelOfUser = levelsRepository.findLevelsEntityByLevelId(user.getLevelId());
        userLevel = Math.toIntExact(levelOfUser.getLevelValue());

        roleList = user.getRoles();
        logger.info("single user {} workload {}", user.getUserName(), currentWorkloads);

        //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
        bestAssignment = geneticAlgorithmSingleUser(taskCount, 1000, 1000, currentWorkloads, taskEstimatedHour,
                taskLevel, userLevel, taskRisk, riskMultiplier);
        logger.info("Best assignment:");
        logger.info("task id list " + Arrays.toString(taskIdList));
        return reportTasksForSingleUser(bestAssignment, user, taskIdList, taskEstimatedHour, taskDTOs);
    }

    private TaskAssignedToTeamDTO assignTasks(int taskCount, List<UsersEntity> users, List<TimeSheetEntity> taskEntities, Timestamp ts){
//        int month = DataUtils.getMonthFromTimestamp(ts);
        int[] bestAssignment, currentWorkloads = new int[users.size()], taskRole = new int[taskCount],
                taskIdList = new int[taskCount], taskEstimatedHour = new int[taskCount],
                taskLevel = new int[taskCount], userLevel = new int[users.size()];
        List<RoleEntity> roleList;
        int[][] userRoles = new int[users.size()][(int) roleRepository.count()];
        double[] riskFromUsers = new double[users.size()];
        double[] taskRisk = new double[taskCount];
        for(int i = 0;i<taskCount;i++){
            Long id = taskEntities.get(i).getTimesheetId();
            taskIdList[i] = Math.toIntExact(id);
            taskEstimatedHour[i] = timesheetRepository.getSumOfEstimatedHoursByTimeSheetId(id);
            LevelsEntity levelOfTask = levelsRepository.findLevelsEntityByLevelId(taskEntities.get(i).getLevelId());
            taskLevel[i] = Math.toIntExact(levelOfTask.getLevelValue());
            taskRole[i] = Math.toIntExact(taskEntities.get(i).getTimesheetRole().getRoleId());
            taskRisk[i] = riskRepository.getRiskMultiplierTotalByTimesheetId(taskEntities.get(i).getTimesheetId());
        }

        for(int i = 0;i<users.size();i++){
            SingleResultDTO currentUserWorkload = (SingleResultDTO) monthlyWorkloadTrackingByUser(users.get(i).getUserName(), DataUtils.getMonthFromTimestamp(ts));
            WorkloadResponseDTO response = (WorkloadResponseDTO) currentUserWorkload.getData();
            currentWorkloads[i] += response.getTotalWorkingTimeRemains();
            riskFromUsers[i] = users.get(i).getRiskFromUser().getMultiplier();
        }
        for(int i = 0;i<users.size();i++){
            LevelsEntity levelOfUser = levelsRepository.findLevelsEntityByLevelId(users.get(i).getLevelId());
            userLevel[i] = Math.toIntExact(levelOfUser.getLevelValue());
            roleList = users.get(i).getRoles();
            for(int k = 0;k < roleList.size();k++){
                userRoles[i][k] = Math.toIntExact(roleList.get(k).getRoleId());
            }
            logger.info(users.get(i).getUserName() + " workload " + currentWorkloads[i]);
            System.out.println(users.get(i).getUserName());
        }

        //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
        bestAssignment = geneticAlgorithmForTeam(taskCount, users.size(), 1000, 10000, currentWorkloads, taskEstimatedHour,
                taskLevel, userLevel, taskRole, userRoles, taskRisk, riskFromUsers);

        System.out.println("Best assignment:");

        return reportTasks(bestAssignment, users, taskIdList, taskEstimatedHour);
    }
//TODO đang làm p này
    private TaskAssignedToTeamDTO reportTasks(int[] bestAssignment, List<UsersEntity> users, int[] subTaskIdList, int[] subTaskEstimatedHour){
        List<UsersDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .collect(Collectors.toList());
        List<TaskAssignedToTeamListDTO> taskAssignedToTeamListDTOS = new ArrayList<>();
        int[] tasksPerMem = new int[users.size()];//tasks per member
        int[] totalWorkloadOfMem = new int[users.size()];//total workload
        String res = "";
        for (int i = 0; i < bestAssignment.length; i++) {
            String tmp = "";
            int status = -1;
            TimeSheetEntity currentTask = timesheetRepository.getTimeSheetEntityByTimesheetId((long) subTaskIdList[i]);
            double risk = riskOfSingleTask(1,  riskRepository.getRiskMultiplierTotalByTimesheetId((long) subTaskIdList[i]));
            if(bestAssignment[i] == users.size()){

                tmp = "Task " + subTaskIdList[i] + " - " + subTaskEstimatedHour[i] + " hours not assigned";
                res += tmp;
                logger.info(tmp);
                status = 2;

                int workload = timesheetRepository.getSumOfEstimatedHoursByTimeSheetId(currentTask.getTimesheetId());
                TimesheetDTO timesheetDTO = modelMapper.map(timesheetRepository.getTimeSheetEntityByTimesheetId((long) subTaskIdList[i]), TimesheetDTO.class);
                TaskAssignedToTeamListDTO tasks = new TaskAssignedToTeamListDTO(null, risk, tmp, status, workload, timesheetDTO);
                taskAssignedToTeamListDTOS.add(tasks);
                continue;
            }
            UsersEntity currentAssignedUser = users.get(bestAssignment[i]);
            tmp = "Task " + subTaskIdList[i] + " - " + subTaskEstimatedHour[i] + " hours assigned to " + users.get(bestAssignment[i]).getUserName();
            res += tmp;
            logger.info(tmp);
            status = 1;

            tasksPerMem[bestAssignment[i]]++;
            totalWorkloadOfMem[bestAssignment[i]]+=subTaskEstimatedHour[i];
            assignTaskToUser(currentAssignedUser.getUserName(), (long) subTaskIdList[i]);
//            int levelValue = Math.toIntExact(levelsRepository.findLevelsEntityByLevelId(currentTask.getLevelId()).getLevelValue());
//            int userLevelValue = Math.toIntExact(levelsRepository.findLevelsEntityByLevelId(currentAssignedUser.getLevelId()).getLevelValue());

            UsersDTO currentAssignedUserDTO = modelMapper.map(users.get(bestAssignment[i]), UsersDTO.class);
//            double risk = riskOfSingleTask(1,  riskRepository.getRiskMultiplierTotalByTimesheetId((long) subTaskIdList[i]));
            int workload = timesheetRepository.getSumOfEstimatedHoursByTimeSheetId(currentTask.getTimesheetId());
            TimesheetDTO timesheetDTO = modelMapper.map(timesheetRepository.getTimeSheetEntityByTimesheetId((long) subTaskIdList[i]), TimesheetDTO.class);
            TaskAssignedToTeamListDTO tasks = new TaskAssignedToTeamListDTO(currentAssignedUserDTO, risk, tmp, status, workload, timesheetDTO);
            taskAssignedToTeamListDTOS.add(tasks);
        }
        String rep = "\n";
        for(int i = 0; i < users.size(); i++){
            rep += "User " + users.get(i).getUserName() + " " + tasksPerMem[i] + " " + totalWorkloadOfMem[i]+"\n";
        }
        logger.info(rep);
        TaskAssignedToTeamDTO result = new TaskAssignedToTeamDTO( userDTOs, taskAssignedToTeamListDTOS);
        return result;
    }

    private TaskAssignedListDTO reportTasksForSingleUser(int[] bestAssignment, UsersEntity user, int[] subTaskIdList, int[] subTaskEstimatedHour, List<TimesheetDTO> taskDTOs){
        int tasksPerMem = 0; //tasks per member
        int totalWorkloadOfMem = 0; //total workload
        List<TaskAssignedDTO> taskAssignedDTOS = new ArrayList<>();
        List<Integer> assignedTaskId = new ArrayList<>();
        TaskAssignedDTO taskAssignedDTO;
        String res = "";
//        int userLevelValue = Math.toIntExact(levelsRepository.findLevelsEntityByLevelId(user.getLevelId()).getLevelValue());
        for (int i = 0; i < taskDTOs.size(); i++){
            TimesheetDTO timesheetDTO = taskDTOs.get(i);
            int status = 0,workload = timesheetRepository.getSumOfEstimatedHoursByTimeSheetId(timesheetDTO.getTimesheetId());
//            int levelValue = Math.toIntExact(levelsRepository.findLevelsEntityByLevelId(timesheetDTO.getLevelId()).getLevelValue());
            double risk = riskOfSingleTask(1,  riskRepository.getRiskMultiplierTotalByTimesheetId(timesheetDTO.getTimesheetId()));
            String description = "Task does not fit user's roles/ Task assigned to other user";
            for(int k = 0; k < subTaskIdList.length; k++){
                if(taskDTOs.get(i).getTimesheetId() == subTaskIdList[k]){
                    //in available tasks for user
                    if(bestAssignment[k] == 1){
                        assignedTaskId.add(subTaskIdList[k]);
                        String tmp = "Task " + subTaskIdList[k] + " - " + subTaskEstimatedHour[k] + " hours assigned to " + user.getUserName() ;
                        res += tmp + "\n";
                        logger.info(tmp);
                        tasksPerMem++;
                        totalWorkloadOfMem+=subTaskEstimatedHour[k];
                        assignTaskToUser(user.getUserName(), (long) subTaskIdList[k]);
                        taskDTOs.get(i).setAssignedUser(user);
                        description = "Task is assigned to user";
                        status = 1;
                        //assigned
                    }
                    else {
                        String tmp = "Task " + subTaskIdList[k] + " - " + subTaskEstimatedHour[k] + " hours not assigned";
                        res += tmp + "\n";logger.info(tmp);
                        description = "Task is not assigned to user";
                        status = 2;
                        //not assigned
                    }
                    break;
                }

            }
            taskAssignedDTO = new TaskAssignedDTO(risk, description, status, workload, taskDTOs.get(i));
            taskAssignedDTOS.add(taskAssignedDTO);
        }
//        for (int i = 0; i < bestAssignment.length; i++) {
//
//            if(bestAssignment[i] == 1){
//                String tmp = "Task " + subTaskIdList[i] + " - " + subTaskEstimatedHour[i] + " hours assigned to " + user.getUserName() ;
//                res += tmp + "\n";
//                logger.info(tmp);
//                tasksPerMem++;
//                totalWorkloadOfMem+=subTaskEstimatedHour[i];
//
//                assignTaskToUser(user.getUserName(), (long) subTaskIdList[i]);
//            }
//            else {
//                String tmp = "Task " + subTaskIdList[i] + " - " + subTaskEstimatedHour[i] + " hours not assigned";
//                res += tmp + "\n";logger.info(tmp);
//            }
//        }
        TaskAssignedListDTO result = new TaskAssignedListDTO(modelMapper.map(user, UsersDTO.class), assignedTaskId, taskAssignedDTOS);
        double workloadPercent =  (double) Math.round((float) (100 * totalWorkloadOfMem) / 160) /100;
        String rep = "Mem " + " " + tasksPerMem + " " + workloadPercent;
        logger.info(rep);
        return result;
    }

    @Override
    public BaseResultDTO assignTaskToUser(String userName, Long timeSheetId) {
        SingleResultDTO<Object> result = new SingleResultDTO<>();
        try{
            UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
            TimeSheetEntity task;
            if(timesheetRepository.findById(timeSheetId).isPresent()){
                task = timesheetRepository.findById(timeSheetId).get();
                task.setAssignedUser(user);
                task.setLastUpdate(getCurTimestamp());
                timesheetRepository.save(task);
                result.setSuccess(task);
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
