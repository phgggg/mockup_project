package com.itsol.mockup.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "TIMESHEET")
@Getter
@Setter
public class TimeSheetEntity {

    @Id
    @Column(name="TIMESHEET_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "timesheet_seq")
    @SequenceGenerator(name = "timesheet_seq", sequenceName = "timesheet_seq",allocationSize = 1)
    private Long timesheetId;

    @Column(name = "TASK")
    private String task;

    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;

    @Column(name = "START_DATE_EXPECTED")
    private Timestamp startDateExpected;

    @Column(name = "ACTUAL_START_DATE")
    private Timestamp actualStartDate;

    @Column(name = "FINISH_DATE_EXPECTED")
    private Timestamp finishDateExpected;

    @Column(name = "ACTUAL_FINISH_DATE")
    private Timestamp actualFinishDate;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "PROJECT_ID")
    private Long projectId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "LAST_UPDATE")
    private Timestamp lastUpdate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "USERS_ID")
    private UsersEntity usersEntity;

    @OneToMany(mappedBy = "timeSheetEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TaskDetailEntity> taskDetails;

    @Override
    public String toString() {
        return "TimeSheetEntity{" +
                "timesheetId=" + timesheetId +
                ", task='" + task + '\'' +
                ", createdDate=" + createdDate +
                ", startDateExpected=" + startDateExpected +
                ", actualStartDate=" + actualStartDate +
                ", finishDateExpected=" + finishDateExpected +
                ", actualFinishDate=" + actualFinishDate +
                ", result='" + result + '\'' +
                ", note='" + note + '\'' +
                ", status=" + status +
                ", projectId=" + projectId +
                ", addByUser=" + createdBy +
                ", lastUpdate=" + lastUpdate +
                ", assignedUser=" + usersEntity +
                ", taskDetails=" + taskDetails +
                '}';
    }
}