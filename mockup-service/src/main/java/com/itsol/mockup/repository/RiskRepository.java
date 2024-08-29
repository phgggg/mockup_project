package com.itsol.mockup.repository;

import com.itsol.mockup.entity.RiskDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RiskRepository extends JpaRepository<RiskDTO, Long> {

    @Query(value = "select sum(r.multiplier) from risk r join timesheet_risk tr on r.id = tr.id where timesheet_id = :timesheetId",nativeQuery = true)
    Double getRiskMultiplierTotalByTimesheetId(Long timesheetId);
}
