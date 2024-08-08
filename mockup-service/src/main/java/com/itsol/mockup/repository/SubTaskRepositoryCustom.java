package com.itsol.mockup.repository;

import com.itsol.mockup.entity.SubTaskEntity;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface SubTaskRepositoryCustom {
    //weekly
    Long getSumOfEstimatedHoursByUserId(Long id, Timestamp mon, Timestamp sat);

    Long getSumOfHoursSpentPerTaskDoneByUserId(Long id, Timestamp mon, Timestamp sat);

    Long getSumOfHoursSpentByUserId(Long id, Timestamp mon, Timestamp sat);

    //monthly
    Long getSumOfMonthlyEstimatedHoursByUserId(Long id, int month, int year);

    Long getSumOfMonthlyHoursSpentPerTaskDoneByUserId(Long id, int month, int year);

    Long getSumOfMonthlyHoursSpentByUserId(Long id, int month, int year);

}
