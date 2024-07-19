package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.*;
import com.itsol.mockup.repository.*;
import com.itsol.mockup.services.EmailService;
import com.itsol.mockup.services.UsersService;
import com.itsol.mockup.utils.Constants;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.utils.TokenUtils;
import com.itsol.mockup.web.dto.request.EmailRequest;
import com.itsol.mockup.web.dto.request.IdRequestDTO;
import com.itsol.mockup.web.dto.request.SearchUsersRequestDTO;
import com.itsol.mockup.web.dto.request.auth.AuthRequestDTO;
import com.itsol.mockup.web.dto.response.*;
import com.itsol.mockup.web.dto.response.auth.AuthResponseDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import com.itsol.mockup.web.dto.users.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class UsersServiceImpl extends BaseService implements UsersService {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UsersRepositoryCustom usersRepositoryCustom;

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    EmailService emailService;

    @Autowired
    TokenUtils tokenUtils;


    @Override
    public BaseResultDTO findAllUsers(Integer pageSize, Integer page) {
        logger.info("=== START FIND ALL USERS::");
        ArrayResultDTO<UsersDTO> responseResultDTO = new ArrayResultDTO<>();
        List<UsersDTO> lstUsers = new ArrayList<>();
        try {
            Page<UsersEntity> rawDatas = usersRepository.findAll(PageRequest.of(page, pageSize));
            if (rawDatas != null) {
                if (!rawDatas.getContent().isEmpty()) {
                    rawDatas.getContent().forEach(user -> {
                        UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
                        lstUsers.add(usersDTO);
                    });
                }
                responseResultDTO.setSuccess(lstUsers, rawDatas.getTotalElements(), rawDatas.getTotalPages());
                logger.info("=== FIND ALL USERS RESPONSE::" + responseResultDTO.getErrorCode());
            }
        } catch (Exception ex) {
            responseResultDTO.setFail(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return responseResultDTO;
    }

    @Override
    public BaseResultDTO findUsersByFullNameAndUserName(SearchUsersRequestDTO requestDTO) {
        logger.info("=== START FIND ALL USERS BY FULL_NAME AND USER_NAME::");
        ArrayResultDTO<UsersDTO> respoonseSingleResultDTO = new ArrayResultDTO<>();
        try {
            Page<UsersDTO> rawDatas = usersRepositoryCustom.findUsersByFullNameAndUserName(requestDTO);
            if(rawDatas == null){
                respoonseSingleResultDTO.setFail("null result");
            }
            else if (!rawDatas.getContent().isEmpty()) {
                respoonseSingleResultDTO.setSuccess(rawDatas.getContent());
            }
            logger.info("=== FIND ALL USERS BY FULL_NAME AND USER_NAME RESPONSE::" + respoonseSingleResultDTO.getErrorCode());
        } catch (Exception ex) {
            respoonseSingleResultDTO.setFail(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return respoonseSingleResultDTO;
    }


    @Override
    public BaseResultDTO findAllUsersNotListId(IdRequestDTO requestDTO) {
        logger.info("=== SRART FIND ALL USER NOT LIST ID");
        ArrayResultDTO<UsersDTO> arrayResultDTO = new ArrayResultDTO<>();
        try {
            Page<UsersDTO> rawDatas = usersRepositoryCustom.findUserNotRequest(requestDTO);
//            if (rawDatas.getContent().size() > 0){
//                if (rawDatas.getContent().size() > 0){
//                    rawDatas.getContent().forEach(user -> {
//                        UsersDTO usersDTO = modelMapper.map(user, UsersDTO.class);
//                        lstUser.add(usersDTO);
//                    });
//                }
            arrayResultDTO.setSuccess(rawDatas.getContent(), rawDatas.getTotalElements(), rawDatas.getTotalPages());
//            }
            logger.info("=== FIND ALL USERS NOT LIST USER_ID: " + arrayResultDTO.getErrorCode());

        } catch (Exception e) {
            arrayResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO updateActiveUser(String userName) {
        logger.info("START UPDATE ACTIVE USER");
        BaseResultDTO baseResultDTO = new BaseResultDTO();

        try {
            UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
            if (user != null) {
                user.setActive(1);
                usersRepository.save(user);
            }
            logger.info("UPDATE ACTIVE USER RESPONSE" + baseResultDTO.getErrorCode());
        } catch (Exception e) {
            logger.error("UPDATE ACTIVE USER ERR" + e.getMessage(), e);
            baseResultDTO.setFail(e.getMessage());
        }
        return baseResultDTO;
    }

    @Override
    public BaseResultDTO updateImageID(String userName) {
        logger.info("START UPDATE image USER");
        BaseResultDTO baseResultDTO = new BaseResultDTO();

        try {
            UsersEntity user = usersRepository.findUsersEntityByUserName(userName);
            if (user != null) {
                user.setActive(1);
                usersRepository.save(user);
            }
            logger.info("UPDATE image USER RESPONSE" + baseResultDTO.getErrorCode());
        } catch (Exception e) {
            logger.error("UPDATE image USER ERR" + e.getMessage(), e);
            baseResultDTO.setFail(e.getMessage());
        }
        return baseResultDTO;
    }

    @Override
    public BaseResultDTO deleteUser(Long id) {
        BaseResultDTO baseResultDTO = new BaseResultDTO();
        try {
            UsersEntity usersEntity = usersRepository.getUsersEntitiesByUserId(id);
            if (usersEntity != null) {
                usersRepository.delete(usersEntity);
                baseResultDTO.setSuccess();
                logger.info("DELETE USER REPONSE" + baseResultDTO.getErrorCode());
            }
        } catch (Exception e) {
            logger.error("DELETE USER ERR" + e.getMessage(), e);
            baseResultDTO.setFail(e.getMessage());
        }
        return baseResultDTO;
    }

    @Override
    public BaseResultDTO findUserbyId(Long id) {
        SingleResultDTO<UsersEntity> singleResultDTO = new SingleResultDTO<>();
        try {
            UsersEntity usersEntity = usersRepository.findUsersEntityByUserId(id);
            if (usersEntity != null) {
                singleResultDTO.setSuccess(usersEntity);
            }
        } catch (Exception e) {
            logger.error("FIND USER BY ID ERR");
            singleResultDTO.setFail(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO addUser(UsersDTO requestDTO) {
        logger.info("=== START ADD NEW USER::");
        BaseResultDTO responseResultDTO = new BaseResultDTO();
//        String generatedString = RandomStringUtils.randomAlphabetic(10);
        try {
//            UsersEntity usersEntity = null;
            if ((usersRepository.findUsersEntityByUserName(requestDTO.getUserName())) != null) {
                responseResultDTO.setFail("-3", "người dùng này đã tồn tại !!!!");
                logger.info("=== ADD NEW USER STOP RESPONES: " + responseResultDTO.getErrorCode());
                return responseResultDTO;
            }
            if ((usersRepository.findUsersEntityByEmail(requestDTO.getEmail())) != null) {
                responseResultDTO.setFail("-4", "Email đã được sử dụng !!!!");
                logger.info("=== ADD NEW USER STOP RESPONES: " + responseResultDTO.getErrorCode());
                return responseResultDTO;
            }
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            System.out.println("a\t\t"+requestDTO.getUserName()+"\t\t");
            System.out.println("a\t\t"+requestDTO.getEmail()+"\t\t");
            System.out.println("a\t\t"+requestDTO.getPassWord()+"\t\t");
            requestDTO.setPassWord(passwordEncoder.encode(requestDTO.getPassWord()));

            UsersEntity user = modelMapper.map(requestDTO, UsersEntity.class);
            user = usersRepository.save(user);
            if (user != null) {
                logger.info("new user" + user.getUserId());
                responseResultDTO.setSuccess();
                logger.info("Send Email start");
                EmailRequest emailRequest = new EmailRequest();
                emailRequest.setToEmail(user.getEmail());
                emailRequest.setSubject("Confirm Account");
                emailRequest.setMessage("Please confirm active account" +
                        "click link active: http://localhost:4200/web/confirm/" + user.getUserName());
                emailService.sendEmail(emailRequest);
            }

            logger.info("=== ADD NEW USER RESPONSE::" + responseResultDTO.getErrorCode());
        } catch (Exception ex) {
            responseResultDTO.setFail(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return responseResultDTO;
    }

    @Override
    public BaseResultDTO updateUser(UsersDTO usersDTO) {
        logger.info("=== START UPDATE USER::" + usersDTO.getUserId());
        BaseResultDTO baseResultDTO = new BaseResultDTO();
        try {
            UsersEntity user = usersRepository.getUsersEntitiesByUserId(usersDTO.getUserId());
            if (user.getUserId() != null) {
                UsersEntity usersEntity = modelMapper.map(usersDTO, UsersEntity.class);
//                user.setUserName(usersDTO.getUserName());
//                user.setFullName(usersDTO.getFullName());
//                user.setPassWord(usersDTO.getPassWord());
                usersRepository.save(usersEntity);
                baseResultDTO.setSuccess();
            }
            logger.info("=== UPDATE USER RESPONSE::" + baseResultDTO.getErrorCode());
        } catch (Exception ex) {
            baseResultDTO.setFail(ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        return baseResultDTO;
    }

    @Override
    public BaseResultDTO findUserEntityByUserName(String token) {
        logger.info("=== START SEARCH USER NAME");
        SingleResultDTO<UsersEntity> resultDTO = new SingleResultDTO<>();
        try {
            UsersEntity user = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
            if (user != null) {
                resultDTO.setSuccess(user);
            }
            logger.info("=== SEARCH USERNAME RESPONSE:" + resultDTO.getErrorCode());
        } catch (Exception e) {
            resultDTO.setFail(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return resultDTO;
    }


    @Override
    public BaseResultDTO findAll() {
        ArrayResultDTO<UsersEntity> response = new ArrayResultDTO<>();
        try {
            ArrayList<UsersEntity> ls = (ArrayList<UsersEntity>) usersRepository.findAll();
            if (ls.size() > 0) {
                response.setSuccess(ls);
            }
        } catch (Exception e) {
            response.setFail(e.getMessage());
        }
        return response;
    }

    @Override
    public BaseResultDTO updateRoleUser(Long id, Long roleId) {
        BaseResultDTO baseResultDTO = new BaseResultDTO();
        List<RoleEntity> list = new ArrayList<>();
        try {
            UsersEntity usersEntity = usersRepository.findUsersEntityByUserId(id);
            if (usersEntity != null) {
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setRoleId(roleId);
                list.add(roleEntity);
                usersEntity.setRoles(list);
                usersRepository.save(usersEntity);
                baseResultDTO.setSuccess();
            }
        } catch (Exception e) {
            logger.error("UPDATE ROLE USER ERR");
            baseResultDTO.setFail(e.getMessage());
        }
        return baseResultDTO;
    }

    @Override
    public BaseResultDTO addTaskToUser(String userName, TimeSheetEntity taskToAdd, Long projectId, String token) {//dto
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try {

            ProjectEntity projectEntity = projectRepository.getProjectEntityByProjectId(projectId);
            UsersEntity user = usersRepository.findUsersEntityByUserName(userName);

            boolean userInProperTeam = false;
            List<TeamEntity> teamList = user.getTeamEntityList();
            for(TeamEntity t : teamList){
                if(t.getProjectEntity().getProjectName().equals(projectEntity.getProjectName())){
                    logger.info("user in proper team");
                    userInProperTeam = true;
                    break;
                }
            }

            if(!userInProperTeam){
                singleResultDTO.setFail("User not in proper team");
            }
            else if(taskToAdd.getStartDateExpected().after(projectEntity.getDeadline())){
                singleResultDTO.setFail("Start date after deadline");
            }
            else if (user != null) {
                UsersEntity createdBy = null;
                try{
                    createdBy = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
                }catch (Exception tk){
                    logger.info("token error {}",tk.getMessage());
                }
                taskToAdd.setUsersEntity(user);
                taskToAdd.setProjectId(projectId);
                taskToAdd.setCreatedDate(getCurTimestamp());
                taskToAdd.setStatus(0);
                taskToAdd.setCreatedBy(createdBy.getUserName());
                taskToAdd.setLastUpdate(getCurTimestamp());
                taskToAdd.setDetails("have to do " + taskToAdd.getTask() + " of project " + projectEntity.getProjectName());
                taskToAdd.setNote("No response from user");
                timesheetRepository.save(taskToAdd);
                usersRepository.save(user);
                singleResultDTO.setSuccess(taskToAdd);
            }else {
                singleResultDTO.setFail("Cannot find user");
            }
        } catch (Exception e) {
            logger.error("ADD TASK USER ERR");
            singleResultDTO.setFail(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO updateUserTask(String userName, TimeSheetEntity timeSheetEntity, Long projectId, String token) {
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try {
            UsersEntity usersEntity = usersRepository.findUsersEntityByUserName(userName);
            TimeSheetEntity taskToUpd = timesheetRepository.getTimeSheetEntityByTimesheetId(timeSheetEntity.getTimesheetId());
            UsersEntity createdBy = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
            logger.info("user lay tu token {}", createdBy.getUserName());
            if(taskToUpd!=null){
                timeSheetEntity.setUsersEntity(usersEntity);
                timeSheetEntity.setCreatedBy(createdBy.getUserName());
                timeSheetEntity.setLastUpdate(getCurTimestamp());
                timeSheetEntity.setProjectId(projectId);
                timeSheetEntity.setNote(DataUtils.TaskComment(timeSheetEntity));
                timesheetRepository.save(timeSheetEntity);
                singleResultDTO.setSuccess(timeSheetEntity);
            }else {
                singleResultDTO.setFail("can't find task that need to update");
            }
        } catch (Exception e) {
            logger.error("UPDATE TASK USER ERR");
            singleResultDTO.setFail(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO getUserTaskStatus(String userName) {
        UserTaskStatusDTO resultDTO = new UserTaskStatusDTO();
        try {
            Timestamp currentTimeStamp = getCurTimestamp();
            UsersEntity user = usersRepository.getUsersByUserName(userName);
            List<ProjectEntity> userProjects = projectRepository.getProjectEntitiesByUserId(user.getUserId());
            List<TimesheetDTO> userTasksTotal = new ArrayList<>();
            if (user != null && !userProjects.isEmpty()) {
                double taskDone = 0, taskOnGoing = 0;
                int daysUntilNearestDeadline = Integer.MAX_VALUE, totalTaskCount = 0, currentProjectTaskCount;
                String description = "";
                for(ProjectEntity p : userProjects){
                    UserTaskStatusDTO projectResult = (UserTaskStatusDTO) getUserTaskStatusByProjectId(userName, p.getProjectId());
                    currentProjectTaskCount = projectResult.getTasksStatus().size();
                    totalTaskCount+=currentProjectTaskCount;
                    taskDone    +=projectResult.getTaskDone()   *currentProjectTaskCount;
                    taskOnGoing +=projectResult.getTaskOnGoing()*currentProjectTaskCount;
                    if(projectResult.getDaysLeft() < daysUntilNearestDeadline) daysUntilNearestDeadline = Math.toIntExact(projectResult.getDaysLeft());
                    description+=projectResult.getStatusInfo()+"\n";
                    userTasksTotal.addAll(projectResult.getTasksStatus());
                }

                taskDone = Math.round(taskDone/totalTaskCount* 100.0) / 100.0;
                taskOnGoing = Math.round(taskOnGoing/totalTaskCount * 100.0) / 100.0;
                double taskPending =Math.round((1 - taskDone - taskOnGoing) * 100.0) / 100.0;
                description+="In total, there are:\n";
                description+=taskDone*100 + "% tasks done\n";
                description+=taskOnGoing*100 + "% tasks on going\n";
                description+=taskPending*100 + "% tasks pending\n";

                resultDTO.setSuccess(convertToDto(user), taskDone, taskOnGoing, taskPending, daysUntilNearestDeadline, currentTimeStamp);
                resultDTO.setStatusInfo(description);
                resultDTO.setTasksStatus(userTasksTotal);
                logger.info("\n{}",description);
            }
        }
        catch (Exception e) {
            logger.error("GET USER/PROJECT ERR");
            resultDTO.setFail(e.getMessage());
        }
        return resultDTO;
    }

    @Override
    public BaseResultDTO getUserTaskStatusByProjectId(String userName, Long projectId) {
        UserTaskStatusDTO resultDTO = new UserTaskStatusDTO();
        try {
            Timestamp currentTimeStamp = getCurTimestamp();
            UsersEntity user = usersRepository.getUsersByUserName(userName);
            List<TimeSheetEntity> userTasksByProject = timesheetRepository.findTimeSheetEntitiesByUserIdAndProjectId(user.getUserId(), projectId);
            List<TimesheetDTO> userTasksByProjectDTO = new ArrayList<>();
            ProjectEntity currentPrj = projectRepository.getProjectEntityByProjectId(projectId);
            if (user != null) {
                String description = "";
                double taskDone = 0, taskOnGoing = 0;
                Date currentDate = new Date();
                int daysUntilNearestDeadline = Integer.MAX_VALUE, daysLeft, daysTotal;
                description+="Tasks of user " + user.getUserName()+"\n";
                Date deadline = currentPrj.getDeadline();
                description+= "Project " + currentPrj.getProjectName()+":\n";
                for(TimeSheetEntity timesheetObj : userTasksByProject){
                    Date taskCreateDate = timesheetObj.getCreatedDate();
                    daysTotal = (int) DataUtils.dayDiff(deadline, taskCreateDate);
                    daysLeft =  (int) DataUtils.dayDiff(deadline, currentDate);
                    if(daysLeft < daysUntilNearestDeadline) daysUntilNearestDeadline = daysLeft;

                    description+= "Task " + timesheetObj.getTask() + "(" + daysTotal + " days)" + " status: "+ timesheetObj.getStatus();
                    try{
                        description+= DataUtils.TaskComment(timesheetObj);
                    }catch (Exception e){
                        description += "Finish date not updated";
                    }
                    description+= ", " + daysLeft + " days until deadline\n";

                    if(timesheetObj.getStatus() == 2) taskDone++;
                    if(timesheetObj.getStatus() == 1) taskOnGoing++;
                    userTasksByProjectDTO.add(convertToDto(timesheetObj));
                }

                taskDone = Math.round(taskDone/userTasksByProject.size()* 100.0) / 100.0;
                taskOnGoing = Math.round(taskOnGoing/userTasksByProject.size() * 100.0) / 100.0;
                double taskPending =Math.round((1 - taskDone - taskOnGoing) * 100.0) / 100.0;

                description+=taskDone*100 + "% tasks done\n";
                description+=taskOnGoing*100 + "% tasks on going\n";
                description+=taskPending*100 + "% tasks pending\n";

                resultDTO.setSuccess(convertToDto(user), userTasksByProjectDTO, taskDone, taskOnGoing, taskPending, daysUntilNearestDeadline, currentTimeStamp);
                resultDTO.setStatusInfo(description);
//                logger.info("\n{}", description);
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultDTO.setFail(e.getMessage());
        }
        return resultDTO;
    }

    private TimesheetDTO convertToDto(TimeSheetEntity timeSheetEntity) {
        TimesheetDTO timesheetDTO = modelMapper.map(timeSheetEntity, TimesheetDTO.class);
        return timesheetDTO;
    }

    private UsersDTO convertToDto(UsersEntity usersEntity) {
        UsersDTO usersDTO = modelMapper.map(usersEntity, UsersDTO.class);
        return usersDTO;
    }

    // ====== START SERVICES FOR AUTHENTICATION ======
    @Override
    public AuthResponseDTO generateToken(AuthRequestDTO userForAuthentication) {
        logger.info("=== START GENERATE TOKEN::");
        AuthResponseDTO responseDTO = new AuthResponseDTO();
        try {
            if (isRequestDataValid(userForAuthentication)) {
                UserDetails springSecurityUser = userDetailsService.loadUserByUsername(userForAuthentication.getUsername());
                if (springSecurityUser != null && (
                        springSecurityUser.getUsername().equals(userForAuthentication.getUsername()) &&
                                new BCryptPasswordEncoder().matches(userForAuthentication.getPassword(), springSecurityUser.getPassword())
//                            springSecurityUser.getPassword().equals(userForAuthentication.getPassword())
                )) {
//                    UsersEntity user = usersRepository.getUsersByUserName(userForAuthentication.getUsername());
//
//                    List<RoleEntity> lst = user.getRoles();
//                    StringBuilder test = new StringBuilder();
//                    lst.forEach(i -> {
//                        test.append(i.getName());
//                    });
                    responseDTO.setToken(tokenUtils.generateToken(springSecurityUser));
                    responseDTO.setUsername(springSecurityUser.getUsername());
                    responseDTO.setPassword(springSecurityUser.getPassword());
                    responseDTO.setRoles(springSecurityUser.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.joining(",")));
                    responseDTO.setErrorCode(Constants.SUCCESS);
                    return responseDTO;
                }
                responseDTO.setErrorCode(Constants.ERROR_401);
            } else {
                throw new BadCredentialsException("Invalid username or password");
            }
        } catch (Exception ex) {
            responseDTO.setErrorCode(Constants.ERROR);
            logger.error(ex.getMessage(), ex);
        }
        logger.info("=== GENERATE TOKEN RESPONSE::" + responseDTO.getErrorCode());
        return responseDTO;
    }


    private boolean isRequestDataValid(AuthRequestDTO userForAuthentication) {
        return userForAuthentication != null &&
                userForAuthentication.getUsername() != null &&
                userForAuthentication.getPassword() != null &&
                !userForAuthentication.getUsername().isEmpty() &&
                !userForAuthentication.getPassword().isEmpty();
    }
    // ====== END ======


}
