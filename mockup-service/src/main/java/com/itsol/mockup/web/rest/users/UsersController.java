package com.itsol.mockup.web.rest.users;

import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.services.UsersService;
import com.itsol.mockup.web.dto.request.IdRequestDTO;
import com.itsol.mockup.web.dto.request.SearchUsersRequestDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.timesheet.AddTaskDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import com.itsol.mockup.web.dto.users.UsersDTO;
import com.itsol.mockup.web.rest.BaseRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequestMapping("/api")
@Scope("request")
@CrossOrigin
public class UsersController extends BaseRest {
    @Autowired
    UsersService usersService;
    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(value = "/findAllUser", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> users(@RequestParam("pageSize") Integer pageSize,
                                               @RequestParam("page") Integer page) {
        BaseResultDTO result = usersService.findAllUsers(pageSize, page);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<BaseResultDTO> addUser(@RequestBody UsersDTO requestDTO) {
        BaseResultDTO result = usersService.addUser(requestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> updateUser(@RequestBody UsersDTO requestDTO) {
        BaseResultDTO result = usersService.updateUser(requestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.DELETE)
    public ResponseEntity<BaseResultDTO> deleteUser(@RequestParam("id") Long id) {
        BaseResultDTO result = usersService.deleteUser(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/find-by-fullname-username", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> findByFullNameAndUserName(@RequestBody SearchUsersRequestDTO requestDTO) {
        BaseResultDTO result = usersService.findUsersByFullNameAndUserName(requestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/findAll")
    public ResponseEntity<BaseResultDTO> findAllUser() {
        BaseResultDTO resultDTO = usersService.findAll();
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/get-allUser-not-listId")
    public ResponseEntity<BaseResultDTO> searchAllUserNotIds(@RequestBody IdRequestDTO idRequestDTO) {
        BaseResultDTO baseResultDTO = usersService.findAllUsersNotListId(idRequestDTO);
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/info")
    public ResponseEntity<BaseResultDTO> UserInfo(@RequestHeader HttpHeaders header) {
        BaseResultDTO baseResultDTO = usersService.findUserEntityByUserName(retrieveToken(header));
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/updateActiveUser")
    public ResponseEntity<BaseResultDTO> updateActiveUser(@RequestParam("userName") String userName) {
        BaseResultDTO baseResultDTO = usersService.updateActiveUser(userName);
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/userById")
    public ResponseEntity<BaseResultDTO> userById(@RequestParam("id") Long id) {
        BaseResultDTO baseResultDTO = usersService.findUserbyId(id);
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/updateActiveUser")
    public ResponseEntity<BaseResultDTO> updateRoleUser(@RequestParam("id") Long id,
                                                        @RequestParam("role") Long role) {
        BaseResultDTO baseResultDTO = usersService.updateRoleUser(id, role);
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/addTaskToUser")
    public ResponseEntity<BaseResultDTO> addTaskToUser(@RequestBody AddTaskDTO addTaskDTO,
                                                       @RequestHeader HttpHeaders header) throws ParseException {
//        BaseResultDTO baseResultDTO = usersService.addTaskToUser(userName, timesheetDTO, projectId, retrieveToken(header));
        TimeSheetEntity taskToAdd = convertToEntity(addTaskDTO.getTimesheetDTO());
        BaseResultDTO baseResultDTO = usersService.addTaskToUser(addTaskDTO.getUserName(), taskToAdd, addTaskDTO.getProjectId(), retrieveToken(header));
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @PutMapping(value = "/updateTask")
    public  ResponseEntity<BaseResultDTO> updateUserTask(@RequestBody AddTaskDTO updateTask,
                                                         @RequestHeader HttpHeaders header) throws ParseException {
        TimeSheetEntity taskToUpdate = convertToEntity(updateTask.getTimesheetDTO());
        BaseResultDTO baseResultDTO = usersService.updateUserTask(updateTask.getUserName(), taskToUpdate, updateTask.getProjectId(), retrieveToken(header));
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/taskStatus")
    public ResponseEntity<BaseResultDTO> getUserTaskStatus(@RequestParam("username") String userName) {
        BaseResultDTO baseResultDTO = usersService.getUserTaskStatus(userName);
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/taskStatusByProject")
    public ResponseEntity<BaseResultDTO> getUserTaskStatusByProjectId(@RequestParam("username") String userName, @RequestParam("projectId") Long projectId) {
        BaseResultDTO baseResultDTO = usersService.getUserTaskStatusByProjectId(userName, projectId);
        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
    }

//    @GetMapping(value = "/updateImageId")
//    public ResponseEntity<BaseResultDTO> updateImage(@RequestParam("userName") String userName){
//        BaseResultDTO baseResultDTO  = usersService.updateActiceUser(userName);
//        return new ResponseEntity<>(baseResultDTO, HttpStatus.OK);
//    }

    private TimeSheetEntity convertToEntity(TimesheetDTO timesheetDTO) throws ParseException {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        TimeSheetEntity timeSheetEntity = modelMapper.map(timesheetDTO, TimeSheetEntity.class);
        System.out.println(timeSheetEntity.toString());
//        if (timesheetDTO.getTimesheetId() != null) {
//            TimeSheetEntity oldTimeSheet = postService.getPostById(timesheetDTO.getTimesheetId());
//            timeSheetEntity.setRedditID(oldTimeSheet.getRedditID());
//            timeSheetEntity.setSent(oldTimeSheet.isSent());
//        }
        return timeSheetEntity;
    }

}
