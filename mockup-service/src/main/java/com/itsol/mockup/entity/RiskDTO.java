package com.itsol.mockup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RISK")
@Getter
@Setter
public class RiskDTO {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "risk_seq")
    @SequenceGenerator(name = "risk_seq", sequenceName = "risk_seq",allocationSize = 1)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="CREATE_DATE")
    private Timestamp create_date;

    @Column(name="MULTIPLIER")
    private Double multiplier;

    @Column(name="NOTE")
    private String note;

    @JsonIgnore
    @ManyToMany(mappedBy = "timesheetRisk")
    private List<TimeSheetEntity> riskOfTimesheet = new ArrayList<>();

}
