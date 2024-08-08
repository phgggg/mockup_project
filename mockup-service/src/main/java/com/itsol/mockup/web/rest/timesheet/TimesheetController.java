package com.itsol.mockup.web.rest.timesheet;

import com.itsol.mockup.services.TimesheetService;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.timesheet.SubTaskDTO;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import com.itsol.mockup.web.dto.timesheet.WorkLoadRequestDTO;
import com.itsol.mockup.web.rest.BaseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
@Scope("request")
public class TimesheetController extends BaseRest {
    @Autowired
    TimesheetService timesheetService;

    @CrossOrigin
    @RequestMapping("/timesheet/list")
    public ResponseEntity<BaseResultDTO> findAll(@RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("page") Integer page){
        BaseResultDTO result = timesheetService.findAll(pageSize, page);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin
     @RequestMapping("/timesheet/listByUser")
    public ResponseEntity<BaseResultDTO> findAllTimeSheetById(@RequestHeader HttpHeaders headers,
                                                              @RequestParam("pageSize") Integer pageSize,
                                                              @RequestParam("page") Integer page){
        BaseResultDTO result = timesheetService.searchTimesheetByUser(retrieveToken(headers), pageSize, page);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet",method = RequestMethod.POST)
    public ResponseEntity<BaseResultDTO> addTimesheet(@RequestBody TimesheetDTO timesheetDTO,
                                                      @RequestHeader HttpHeaders headers){
        BaseResultDTO result = timesheetService.addTimesheet(timesheetDTO, retrieveToken(headers));
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/taskDetail",method = RequestMethod.POST)
    public ResponseEntity<BaseResultDTO> addTaskDetail(@RequestBody SubTaskDTO subTaskDTO,
                                                      @RequestParam("userName") String userName,
                                                      @RequestParam("timeSheetId") Long timeSheetId){
        BaseResultDTO result = timesheetService.addTaskDetail(subTaskDTO, userName, timeSheetId);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet",method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> updateTimesheet(@RequestBody TimesheetDTO timesheetDTO){
        BaseResultDTO result = timesheetService.updateTimesheet(timesheetDTO);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet",method = RequestMethod.DELETE)
    public ResponseEntity<BaseResultDTO> deleteTimesheet(Long id){
        BaseResultDTO result = timesheetService.deleteTimesheet(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheetById",method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> getTimesheetById(@RequestParam Long id){
        BaseResultDTO result = timesheetService.getTimesheetById(id);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheetByName",method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> getTimesheetByName(@RequestParam String name){
        BaseResultDTO result = timesheetService.getTimesheetByName(name);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet/updateStatus",method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> updateStatus(@RequestParam("id") Long id,
                                                      @RequestParam("status") Integer status){
        BaseResultDTO result = timesheetService.updateStatusTimeSheet(id, status);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet/workload",method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> weeklyWorkloadTrackingByUser(@RequestBody WorkLoadRequestDTO workLoadRequestDTO){
        String userName = workLoadRequestDTO.getUserName();
        Timestamp timestamp = workLoadRequestDTO.getTimestamp();
        BaseResultDTO result = timesheetService.weeklyWorkloadTrackingByUser(userName, timestamp);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet/workload/month",method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> monthlyWorkloadTrackingByUser(@RequestBody WorkLoadRequestDTO workLoadRequestDTO){
        String userName = workLoadRequestDTO.getUserName();
        Timestamp timestamp = workLoadRequestDTO.getTimestamp();
        BaseResultDTO result = timesheetService.monthlyWorkloadTrackingByUser(userName, timestamp);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet/optimizedSubTaskAssignment",method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> optimizedSubTaskAssignmentByProjectId(@RequestParam("projectId") Long id,
                                                                            @RequestParam("timestamp") Timestamp ts){
        BaseResultDTO result = timesheetService.optimizedSubTaskAssignmentByProjectId(id, ts);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet/optimizedTaskAssignment",method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> optimizedTaskAssignmentByProjectId(@RequestParam("projectId") Long id,
                                                                            @RequestParam("timestamp") Timestamp ts){
        BaseResultDTO result = timesheetService.optimizedTaskAssignmentByProjectId(id, ts);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet/optimizedTaskWeekly",method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> optimizedTaskAssignmentByProjectIdWeekly(@RequestParam("projectId") Long id,
                                                                            @RequestParam("timestamp") Timestamp ts){
        BaseResultDTO result = timesheetService.optimizedTaskAssignmentByProjectIdWeekly(id, ts);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    //test
    @CrossOrigin
    @RequestMapping(value = "/timesheet/transferSubTasksOfOldUser",method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> transferSubTasksOfUser(@RequestParam("projectId") Long id,
                                                                              @RequestParam("name") String name,
                                                                              @RequestParam("timestamp") Timestamp ts){
        BaseResultDTO result = timesheetService.transferSubTasksOfUser(id, name, ts);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/timesheet/transferTasksOfOldUser",method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> transferTasksOfUser(@RequestParam("projectId") Long id,
                                                                @RequestParam("name") String name,
                                                                @RequestParam("timestamp") Timestamp ts){
        BaseResultDTO result = timesheetService.transferTasksOfUser(id, name, ts);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
