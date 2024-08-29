package com.itsol.mockup.entity;

import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TIMESHEET_ROLE")
@Getter
@Setter
public class TimeSheetRoleEntity {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "timesheet_role_seq")
    @SequenceGenerator(name = "timesheet_role_seq", sequenceName = "timesheet_role_seq",allocationSize = 1)
    private Long id;

    @Column(name="ROLE_ID")
    private Long roleId;

    @Column(name="TIMESHEET_ID")
    private Long timesheetId;
}
