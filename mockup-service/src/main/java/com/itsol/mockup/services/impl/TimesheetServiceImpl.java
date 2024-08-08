package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.ProjectEntity;
import com.itsol.mockup.entity.SubTaskEntity;
import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.repository.SubTaskRepositoryCustom;
import com.itsol.mockup.services.TimesheetService;
import com.itsol.mockup.utils.Constants;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import com.itsol.mockup.web.dto.timesheet.SubTaskDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import com.itsol.mockup.web.dto.timesheet.WorkloadResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.itsol.mockup.utils.Constants.*;
import static com.itsol.mockup.utils.GeneticAlgorithm.geneticAlgorithm;

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
        ArrayResultDTO arrayResultDTO = new ArrayResultDTO<>();
        try {
            List<TimesheetDTO> lstResult = new ArrayList<>();
            Page<TimeSheetEntity> rawsData = timesheetRepository.findAll(PageRequest.of(page - 1, pageSize));
            if (rawsData != null) {
                if (rawsData.getContent().size() > 0) {
                    rawsData.getContent().forEach(i -> {
                        TimesheetDTO dto = modelMapper.map(i, TimesheetDTO.class);
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
    public BaseResultDTO addTimesheet(TimesheetDTO timesheetDTO, String token) {
        logger.info("ADD NEW TIMESHEET");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        ProjectEntity projectEntity = projectRepository.getProjectEntityByProjectId(timesheetDTO.getProjectId());
        UsersEntity usersEntity = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
        try {
            if(projectEntity.getDeadline().before(timesheetDTO.getFinishDateExpected())){
                timesheetDTO.setCreatedDate(getCurTimestamp());
                timesheetDTO.setStatus(0);
                TimeSheetEntity timeSheetEntity = modelMapper.map(timesheetDTO, TimeSheetEntity.class);
                timeSheetEntity.setUsersEntity(usersEntity);
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
    public BaseResultDTO updateTimesheet(TimesheetDTO timesheetDTO) {
        logger.info("UPDATE TIMESHEET");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try {
            TimeSheetEntity timeSheetEntity = timesheetRepository.getTimeSheetEntityByTimesheetId(timesheetDTO.getTimesheetId());
            if (timeSheetEntity.getTimesheetId() != null) {
                timeSheetEntity = modelMapper.map(timesheetDTO, TimeSheetEntity.class);
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
        ArrayResultDTO<TimesheetDTO> arrayResultDTO = new ArrayResultDTO<>();
        List<TimesheetDTO> list = new ArrayList<>();
        try {
            UsersEntity usersEntity = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
            Page<TimeSheetEntity> rawData = timesheetRepository.findTimeSheetEntitiesByUsersEntity(usersEntity, PageRequest.of(page, pageSize));
        if (rawData != null){
            if(rawData.getContent().size() > 0){
                rawData.getContent().forEach(timeSheetEntity -> {
                    TimesheetDTO timesheetDTO = modelMapper.map(timeSheetEntity, TimesheetDTO.class);
                    list.add(timesheetDTO);
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
        UsersEntity usersEntity = timeSheetEntity.getUsersEntity();
        try {
            if(usersEntity!=null){
                SubTaskEntity subTask = modelMapper.map(subTaskDTO, SubTaskEntity.class);
                SingleResultDTO weeklyWorkload = (SingleResultDTO) weeklyWorkloadTrackingByUser(userName,timeSheetEntity.getFinishDateExpected());
                WorkloadResponseDTO responseDTO = (WorkloadResponseDTO) weeklyWorkload.getData();
                long userWorkloadInWeek = (long) (responseDTO.getTotalWorkingTimeToDo() + subTask.getEstimatedHours());
                //TODO xem p này có thể thêm gì vào
                if(userWorkloadInWeek >= workingTimePerWeek*1.1){
                    logger.info("Quá tải");
                    singleResultDTO.setFail("overload");
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

    @Override
    public BaseResultDTO weeklyWorkloadTrackingByUser(String userName, Timestamp timestamp) {
        SingleResultDTO result = new SingleResultDTO<>();
        Timestamp mon = DataUtils.getDayOfWeek(timestamp, DayOfWeek.MONDAY);
        Timestamp sat = DataUtils.getDayOfWeek(timestamp, DayOfWeek.SATURDAY);
        logger.info("t2 la {} t7 la {}", mon, sat);
        logger.info("get user and user tasks");
        UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
        Long totalWorkingTimeEstimated= 0L, totalWorkingTimeSpent = 0L, totalWorkingTimeSpentPerTaskDone = 0L;
        Long totalWorkingTimeToDo;
        int totalWorkloadStatus;
        double actualProgressByTime;
        logger.info("get estimated working hours");

        try{
            totalWorkingTimeEstimated += subTaskRepositoryCustom.getSumOfEstimatedHoursByUserId(user.getUserId(),mon,sat);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        try{
            totalWorkingTimeSpent += subTaskRepositoryCustom.getSumOfHoursSpentByUserId(user.getUserId(),mon,sat);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        try{
            totalWorkingTimeSpentPerTaskDone += subTaskRepositoryCustom.getSumOfHoursSpentPerTaskDoneByUserId(user.getUserId(),mon,sat);
        }catch (Exception e){
            logger.info(e.getMessage());
        }

        logger.info("total working time estimated           = {}", totalWorkingTimeEstimated);
        logger.info("total working time spent               = {}", totalWorkingTimeSpent);
        logger.info("total working time spent per task done = {}", totalWorkingTimeSpentPerTaskDone);
        actualProgressByTime = (double) Math.round((float) (100 * totalWorkingTimeSpent) / totalWorkingTimeEstimated) /100;
//        if(totalWorkingTimeSpent > totalWorkingTimeSpentPerTaskDone){
//            logger.info("Slower than expected");
//
//        }
        logger.info("working time to do                     = {}", totalWorkingTimeEstimated - totalWorkingTimeSpent);
        totalWorkingTimeToDo = (totalWorkingTimeEstimated - totalWorkingTimeSpent);
        totalWorkloadStatus = (totalWorkingTimeEstimated < workingTimePerWeek*0.9) ? 1 :
                                (totalWorkingTimeEstimated > workingTimePerWeek*1.1) ? -1 : 0;
        String cmt = (totalWorkloadStatus == -1) ? " more than" : (totalWorkloadStatus == 1) ? " less than" : "";
        logger.info("Total workload is{} expected", cmt);
        if(totalWorkloadStatus==-1) logger.info("should be {} hours less", totalWorkingTimeEstimated-workingTimePerWeek);
        WorkloadResponseDTO response = new WorkloadResponseDTO
                (
                        totalWorkingTimeEstimated, totalWorkingTimeToDo, totalWorkingTimeSpent, totalWorkingTimeSpentPerTaskDone,
                        totalWorkloadStatus, actualProgressByTime, getCurTimestamp()
                );

        result.setSuccess(response);
        return result;
    }

    @Override
    public BaseResultDTO monthlyWorkloadTrackingByUser(String userName, Timestamp timestamp) {
        SingleResultDTO result = new SingleResultDTO<>();
        int month = DataUtils.getMonthFromTimestamp(timestamp);
        int year = 2024;
        logger.info("Workload tracking for {}, year {}", months[month-1], year);
        logger.info("get user and user tasks");
        UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
        Long totalWorkingTimeEstimated= 0L, totalWorkingTimeSpent = 0L, totalWorkingTimeSpentPerTaskDone = 0L;
        Long totalWorkingTimeToDo;
        int totalWorkloadStatus;
        double actualProgressByTime;
        logger.info("get monthly estimated working hours");

        try{
            totalWorkingTimeEstimated += subTaskRepositoryCustom.getSumOfMonthlyEstimatedHoursByUserId(user.getUserId(),month,year);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        try{
            totalWorkingTimeSpent += subTaskRepositoryCustom.getSumOfMonthlyHoursSpentByUserId(user.getUserId(),month,year);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        try{
            totalWorkingTimeSpentPerTaskDone += subTaskRepositoryCustom.getSumOfMonthlyHoursSpentPerTaskDoneByUserId(user.getUserId(),month,year);
        }catch (Exception e){
            logger.info(e.getMessage());
        }

        logger.info("total working time estimated monthly           = {}", totalWorkingTimeEstimated);
        logger.info("total working time spent monthly               = {}", totalWorkingTimeSpent);
        logger.info("total working time spent per task done monthly = {}", totalWorkingTimeSpentPerTaskDone);
        actualProgressByTime = (double) Math.round((float) (100 * totalWorkingTimeSpent) / totalWorkingTimeEstimated) /100;
        logger.info("working time to do monthly                     = {}", totalWorkingTimeEstimated - totalWorkingTimeSpent);
        totalWorkingTimeToDo = (totalWorkingTimeEstimated - totalWorkingTimeSpent);
        totalWorkloadStatus = (totalWorkingTimeEstimated < workingTimePerMonth*0.9) ? 1 :
                (totalWorkingTimeEstimated > workingTimePerMonth*1.1) ? -1 : 0;
        String cmt = (totalWorkloadStatus == -1) ? " more than" : (totalWorkloadStatus == 1) ? " less than" : "";
        logger.info("Total monthly workload is{} expected", cmt);
        if(totalWorkloadStatus==-1) logger.info("should be {} hours less monthly", totalWorkingTimeEstimated-workingTimePerWeek);
        WorkloadResponseDTO response = new WorkloadResponseDTO
                (
                        totalWorkingTimeEstimated, totalWorkingTimeToDo, totalWorkingTimeSpent, totalWorkingTimeSpentPerTaskDone,
                        totalWorkloadStatus, actualProgressByTime, getCurTimestamp()
                );

        result.setSuccess(response);
        return result;
    }

    //assign task based on project id
    @Override
    public BaseResultDTO optimizedSubTaskAssignmentByProjectId(Long id, Timestamp ts) {
        SingleResultDTO result = new SingleResultDTO<>();
        try {
            ProjectEntity project = projectRepository.getProjectEntityByProjectId(id);
            List<UsersEntity> users = usersRepository.findUsersInTeamByProjectId(id);
            int subTaskCount = Math.toIntExact(subTaskRepository.getSubTaskCountByProjectId(id));
            List<SubTaskEntity> subTaskEntities = subTaskRepository.getSubTaskByProjectId(id);//subtask

            Iterator<SubTaskEntity> iterator = subTaskEntities.iterator();
            while (iterator.hasNext()) {
                SubTaskEntity subTask = iterator.next();
                if (subTask.getHoursSpent() == 0) {
                    subTask.setAssignedUser(null);
                    subTaskRepository.save(subTask);
                } else {
                    iterator.remove();
                    subTaskCount--;
                }
            }

            //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
            int[] bestAssignment = assignSubTasks(subTaskCount, users, subTaskEntities, 200, 200, ts);
            result.setSuccess(bestAssignment);
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO transferSubTasksOfUser(Long id, String name, Timestamp ts) {
        SingleResultDTO result = new SingleResultDTO<>();
        try {
            UsersEntity userToRemove = usersRepository.findUsersEntityByUserName(name);
            List<UsersEntity> users = usersRepository.findUsersInTeamByProjectId(id);
            users.remove(userToRemove);
            int subTaskCount = Math.toIntExact(subTaskRepository.getSubTaskCountByProjectIdAndUserId(id, userToRemove.getUserId()));
            List<SubTaskEntity> subTaskEntities = subTaskRepository.getSubTaskByProjectIdAndUserId(id, userToRemove.getUserId());//subtask

            //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
            int[] bestAssignment = assignSubTasks(subTaskCount, users, subTaskEntities, 200, 200, ts);
            result.setSuccess(bestAssignment);
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    private int[] assignSubTasks(int subTaskCount, List<UsersEntity> users, List<SubTaskEntity> subTaskEntities,
                                 int numGen, int populationSize, Timestamp ts){
        int[] bestAssignment;
        int[] currentWorkloads = new int[users.size()];
        int[] subTaskIdList = new int[subTaskCount];
        int[] subTaskEstimatedHour = new int[subTaskCount];
        int[] taskLevel = new int[subTaskCount];
        int[] userLevel = new int[users.size()];
        for(int i = 0;i<subTaskCount;i++){
            subTaskIdList[i] = Math.toIntExact(subTaskEntities.get(i).getId());
            subTaskEstimatedHour[i] = subTaskEntities.get(i).getEstimatedHours().intValue();
            taskLevel[i] = Math.toIntExact(subTaskEntities.get(i).getTimeSheetEntity().getLevelId());
//            logger.info("subTaskIdList " + subTaskIdList[i] + " subTaskEstimatedHour " + subTaskEstimatedHour[i]);
        }

        for(int i = 0;i<users.size();i++){
            userLevel[i] = Math.toIntExact(users.get(i).getLevelId());
            SingleResultDTO currentUserWorkload = (SingleResultDTO) monthlyWorkloadTrackingByUser(users.get(i).getUserName(), ts);
            WorkloadResponseDTO response = (WorkloadResponseDTO) currentUserWorkload.getData();
            currentWorkloads[i] += response.getTotalWorkingTimeToDo();
        }

        for(int i = 0;i<users.size();i++){
            logger.info(users.get(i).getUserName() + " workload " + currentWorkloads[i]);
        }

        //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
        bestAssignment = geneticAlgorithm(subTaskCount, users.size(), numGen, populationSize, currentWorkloads, subTaskEstimatedHour, taskLevel, userLevel);

        System.out.println("Best assignment:");
        reportSubTasks(bestAssignment, users, subTaskIdList, subTaskEstimatedHour);

        return bestAssignment;
    }

    private void reportSubTasks(int[] bestAssignment, List<UsersEntity> users, int[] subTaskIdList, int[] subTaskEstimatedHour){
        int[] tasksPerMem = new int[users.size()];//tasks per member
        int[] totalWorkloadOfMem = new int[users.size()];//total workload
        for (int i = 0; i < bestAssignment.length; i++) {
            System.out.printf("Task %d - %d hours assigned to %s\n", subTaskIdList[i], subTaskEstimatedHour[i], users.get(bestAssignment[i]).getUserName());

            tasksPerMem[bestAssignment[i]]++;
            totalWorkloadOfMem[bestAssignment[i]]+=subTaskEstimatedHour[i];

            subTaskServices.assignSubTaskToUser(users.get(bestAssignment[i]).getUserName(), (long) subTaskIdList[i]);
        }
        String rep = "\n";
        for(int i = 0; i < users.size(); i++){
            rep += "Mem " + i + " " + tasksPerMem[i] + " " + totalWorkloadOfMem[i]+"\n";
        }
        logger.info(rep);
    }

    @Override
    public BaseResultDTO optimizedTaskAssignmentByProjectId(Long id, Timestamp ts) {
        SingleResultDTO result = new SingleResultDTO<>();
        try {
            ProjectEntity project = projectRepository.getProjectEntityByProjectId(id);
            List<UsersEntity> users = usersRepository.findUsersInTeamByProjectId(id);
            int taskCount = Math.toIntExact(timesheetRepository.getTotalTaskCountByProjectId(id));
            List<TimeSheetEntity> taskEntities = timesheetRepository.findTimeSheetEntitiesByProjectId(id);//subtask

            Iterator<TimeSheetEntity> iterator = taskEntities.iterator();
            while (iterator.hasNext()) {
                TimeSheetEntity task = iterator.next();
                if (timesheetRepository.getSumOfHoursSpentByTimeSheetId(task.getTimesheetId()) == 0) {
                    task.setUsersEntity(null);
                    timesheetRepository.save(task);
                } else {
                    iterator.remove();
                    taskCount--;
                }
            }

            //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
            int[] bestAssignment = assignTasks(taskCount, users, taskEntities, 200, 200, ts);
            result.setSuccess(bestAssignment);
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO optimizedTaskAssignmentByProjectIdWeekly(Long id, Timestamp ts) {
        SingleResultDTO result = new SingleResultDTO<>();
        Timestamp mon = DataUtils.getDayOfWeek(ts, DayOfWeek.MONDAY);
        Timestamp sat = DataUtils.getDayOfWeek(ts, DayOfWeek.SATURDAY);
        try {

            ProjectEntity project = projectRepository.getProjectEntityByProjectId(id);
            List<UsersEntity> users = usersRepository.findUsersInTeamByProjectId(id);
            int taskCount = Math.toIntExact(timesheetRepository.getTotalTaskCountByProjectId(id));
            List<TimeSheetEntity> taskEntities = timesheetRepository.findTimeSheetEntitiesByProjectId(id);//subtask
            if(!taskEntities.isEmpty()){
                Iterator<TimeSheetEntity> iterator = taskEntities.iterator();
                while (iterator.hasNext()) {
                    TimeSheetEntity task = iterator.next();
                    if (timesheetRepository.getSumOfHoursSpentByTimeSheetId(task.getTimesheetId()) == 0
                            && (task.getStartDateExpected().after(mon) && task.getFinishDateExpected().before(sat))
                    ) {
                        task.setUsersEntity(null);
                        timesheetRepository.save(task);
                    } else {
                        iterator.remove();
                        taskCount--;
                    }
                }
                //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
                int[] bestAssignment = assignTasks(taskCount, users, taskEntities, 200, 200, ts);
                result.setSuccess(bestAssignment);
            }else {
                logger.info("empty");
                result.setFail("empty list");
            }
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO transferTasksOfUser(Long id, String name, Timestamp ts) {
        SingleResultDTO result = new SingleResultDTO<>();
        try {
            UsersEntity userToRemove = usersRepository.findUsersEntityByUserName(name);
            List<UsersEntity> users = usersRepository.findUsersInTeamByProjectId(id);
            users.remove(userToRemove);
            int taskCount = Math.toIntExact(timesheetRepository.getTaskCountByProjectIdAndUserId(id, userToRemove.getUserId()));
            List<TimeSheetEntity> taskEntities = timesheetRepository.getTaskByProjectIdAndUserId(id, userToRemove.getUserId());//task

            //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
            int[] bestAssignment = assignTasks(taskCount, users, taskEntities, 200, 200, ts);
            result.setSuccess(bestAssignment);
        }catch (Exception e){
            result.setFail("error while assigning tasks");
            logger.info(e.getMessage());
        }
        return result;
    }

    private int[] assignTasks(int taskCount, List<UsersEntity> users, List<TimeSheetEntity> taskEntities,
                                 int numGen, int populationSize, Timestamp ts){
        int[] bestAssignment;
        int[] currentWorkloads = new int[users.size()];
        int[] taskIdList = new int[taskCount];
        int[] taskEstimatedHour = new int[taskCount];
        int[] taskLevel = new int[taskCount];
        int[] userLevel = new int[users.size()];
        for(int i = 0;i<taskCount;i++){
            Long id = taskEntities.get(i).getTimesheetId();
            taskIdList[i] = Math.toIntExact(id);
            taskEstimatedHour[i] = timesheetRepository.getSumOfEstimatedHoursByTimeSheetId(id);
            taskLevel[i] = Math.toIntExact(taskEntities.get(i).getLevelId());
//            logger.info("taskIdList " + taskIdList[i] + " taskEstimatedHour " + taskEstimatedHour[i]);
        }

        for(int i = 0;i<users.size();i++){
            SingleResultDTO currentUserWorkload = (SingleResultDTO) monthlyWorkloadTrackingByUser(users.get(i).getUserName(), ts);
            WorkloadResponseDTO response = (WorkloadResponseDTO) currentUserWorkload.getData();
            currentWorkloads[i] += response.getTotalWorkingTimeToDo();
        }
        for(int i = 0;i<users.size();i++){
            userLevel[i] = Math.toIntExact(users.get(i).getLevelId());
            logger.info(users.get(i).getUserName() + " workload " + currentWorkloads[i]);
            System.out.println(users.get(i).getUserName());
        }

        //result bestAssignment[i]: task i is assigned to member with idx num bestAssignment[i]
        bestAssignment = geneticAlgorithm(taskCount, users.size(), numGen, populationSize, currentWorkloads, taskEstimatedHour, taskLevel, userLevel);

        System.out.println("Best assignment:");
        reportTasks(bestAssignment, users, taskIdList, taskEstimatedHour);

        return bestAssignment;
    }

    private void reportTasks(int[] bestAssignment, List<UsersEntity> users, int[] subTaskIdList, int[] subTaskEstimatedHour){
        int[] tasksPerMem = new int[users.size()];//tasks per member
        int[] totalWorkloadOfMem = new int[users.size()];//total workload
        for (int i = 0; i < bestAssignment.length; i++) {
            System.out.printf("Task %d - %d hours assigned to %s\n", subTaskIdList[i], subTaskEstimatedHour[i], users.get(bestAssignment[i]).getUserName());

            tasksPerMem[bestAssignment[i]]++;
            totalWorkloadOfMem[bestAssignment[i]]+=subTaskEstimatedHour[i];

            assignTaskToUser(users.get(bestAssignment[i]).getUserName(), (long) subTaskIdList[i]);
        }
        String rep = "\n";
        for(int i = 0; i < users.size(); i++){
            rep += "Mem " + i + " " + tasksPerMem[i] + " " + totalWorkloadOfMem[i]+"\n";
        }
        logger.info(rep);
    }

    @Override
    public BaseResultDTO assignTaskToUser(String userName, Long timeSheetId) {
        SingleResultDTO result = new SingleResultDTO();
        try{
            UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
            TimeSheetEntity task;
            if(timesheetRepository.findById(timeSheetId).isPresent()){
                task = timesheetRepository.findById(timeSheetId).get();
                task.setUsersEntity(user);
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
