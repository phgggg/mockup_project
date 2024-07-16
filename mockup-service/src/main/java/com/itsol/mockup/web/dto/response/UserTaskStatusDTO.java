package com.itsol.mockup.web.dto.response;

import com.itsol.mockup.entity.TimeSheetEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserTaskStatusDTO extends ProjectStatusDTO{
    private List<TimeSheetEntity> taskStatus;

    public void setSuccess(List<TimeSheetEntity> taskStatus, double taskDone, double taskOngoing, int daysLeft){
        super.setSuccess(taskDone, taskOngoing, daysLeft);
        this.setTaskStatus(taskStatus);
    }

    public void setSuccess(double taskDone, double taskOngoing, int daysLeft){
        super.setSuccess(taskDone, taskOngoing, daysLeft);
    }
}
