package com.itsol.mockup.web.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsersDTO2 extends UsersDTO{
    private int workloadStatus;
    private double workloadPercent;
    private double freeTime = 1 - workloadPercent;

    public void workloadInp(int workloadStatusInp, double workloadPercentInp){
        this.workloadStatus = workloadStatusInp;
        this.workloadPercent = (double) Math.round(workloadPercentInp * 100) /100;
        this.freeTime = (double) Math.round(( 1 - workloadPercent) * 100) /100;
    }
}
