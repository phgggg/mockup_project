package com.itsol.mockup.web.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectStatusDTO extends BaseResultDTO{
    private double taskDone;
    private double taskOngoing;
    private int daysLeft;

    public void setSuccess(double taskDone, double taskOngoing, int daysLeft){
        super.setSuccess();
        this.taskDone = taskDone;
        this.taskOngoing = taskOngoing;
        this.daysLeft = daysLeft;
    }
}
