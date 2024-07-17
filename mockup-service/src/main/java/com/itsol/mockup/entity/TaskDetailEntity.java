package com.itsol.mockup.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TASK_DETAIL")
@Getter
@Setter
public class TaskDetailEntity {
    @Id
    @Column(name = "TASK_DETAILS_ID")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "task_detail_seq")
    @SequenceGenerator(name = "task_detail_seq", sequenceName = "task_detail_seq", allocationSize = 1)
    private Long id;

    @Column(name = "TASK_NAME")
    private String taskName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HOURS_SPENT")
    private Double hoursSpent;

    @Column(name = "ESTIMATED_HOURS")
    private Double estimatedHours;

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DETAIL_DATE")
    private Timestamp detailDate;

    @Column(name = "COMMENTS")
    private String comments;

    @ManyToOne
    @JoinColumn(name = "ASSIGNED_USER_ID")
    private UsersEntity assignedUser;

    @ManyToOne
    @JoinColumn(name = "TIMESHEET_ID")
    private TimeSheetEntity timeSheetEntity;
}
