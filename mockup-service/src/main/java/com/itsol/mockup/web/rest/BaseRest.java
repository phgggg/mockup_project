package com.itsol.mockup.web.rest;

import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.web.dto.timesheet.TimesheetDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.text.ParseException;

public class BaseRest {

    @Autowired
    protected ModelMapper modelMapper;

    protected String retrieveToken(HttpHeaders header){
        String token = header.get("Auth-token").toString();
        token = token.substring(1, token.length() - 1);
        return token;
    }
    protected TimeSheetEntity convertToEntity(TimesheetDTO timesheetDTO) throws ParseException {
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
