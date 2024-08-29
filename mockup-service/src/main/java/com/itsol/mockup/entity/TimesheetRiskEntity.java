package com.itsol.mockup.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "timesheet_risk")
@Getter
@Setter
public class TimesheetRiskEntity {
    @Id
    @Column(name="TIMESHEET_RISK_ID")
    private Long timesheetRiskId;

    @Column(name="TIMESHEET_ID")
    private Long timesheet_id;

    @Column(name="ID")
    private Long id;


}
