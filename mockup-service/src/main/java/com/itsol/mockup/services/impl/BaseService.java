package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.repository.*;
import com.itsol.mockup.repository.impl.FileRepositoryCustomImpl;
import com.itsol.mockup.services.EmailService;
import com.itsol.mockup.services.TaskDetailServices;
import com.itsol.mockup.services.TimesheetService;
import com.itsol.mockup.services.UsersService;
import com.itsol.mockup.utils.DataUtils;
import com.itsol.mockup.utils.ExcelUtil;
import com.itsol.mockup.utils.TokenUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BaseService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected ExcelUtil excelUtil;

    @Autowired
    protected FileRepository fileRepository;

    @Autowired
    protected TeamRepository teamRepository;

    @Autowired
    protected UsersRepository usersRepository;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected FileRepositoryCustomImpl fileRepositoryCustom;

    @Autowired
    protected TimesheetRepository timesheetRepository;

    @Autowired
    protected UsersRepositoryCustom usersRepositoryCustom;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected UserDetailsService userDetailsService;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected FileTypeRepository fileTypeRepository;

    @Autowired
    protected SubTaskRepository subTaskRepository;

    @Autowired
    protected TokenUtils tokenUtils;

    @Autowired
    protected UsersService usersService;

    @Autowired
    protected TaskDetailServices subTaskServices;

    @Autowired
    protected TimesheetService timesheetService;

    protected Timestamp getCurTimestamp(){
        return new Timestamp(new Date().getTime());
    }

    protected String TaskComment(TimeSheetEntity timeSheetEntity) {
        String result = null;
        String level = "";
        String status;

        if(timeSheetEntity.getStatus() == 0) result = "Task is pending";
        else if (timeSheetEntity.getStatus() == 1) {
            result = "Task is ongoing";
        } else if (timeSheetEntity.getStatus() == 2) {
            long diff = DataUtils.getDateDiff(timeSheetEntity.getActualFinishDate(), timeSheetEntity.getFinishDateExpected(), TimeUnit.DAYS);
            switch (Math.abs((int) diff)) {
                case 0:
                case 1:
                    level += " ";
                    break;
                case 2:
                case 3:
                    level += " pretty ";
                    break;
                case 4:
                case 5:
                    level += " very ";
                    break;
                default:
                    level += " extremely ";
            }
            status = (diff < 0) ? "late" : (diff == 0) ?"on time" : "early";
            result = "Task is done" + level + status;
        }
        return result;
    }

    protected String[] splitInfo(String taskCommTmp2){
        String[] taskDescs = taskCommTmp2.split(" ");
        if(taskDescs.length==2){
            taskDescs = new String[]{taskDescs[0], null, taskDescs[1]};
        }else if(taskDescs.length==1){
            taskDescs = new String[]{taskDescs[0], null, null};
        }
        return taskDescs;
    }

}
