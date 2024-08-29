package com.itsol.mockup.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "RISK_FROM_USER")
@Getter
@Setter
public class RiskFromUserEntity {
    @Id
    @Column(name="RISK_FROM_USER_ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "risk_from_user_seq")
    @SequenceGenerator(name = "risk_from_user_seq", sequenceName = "risk_from_user_seq",allocationSize = 1)
    private Long riskFromUserId;

    @Column(name="NAME")
    private String name;

    @Column(name="CREATE_DATE")
    private Timestamp create_date;

    @Column(name="MULTIPLIER")
    private Double multiplier;

    @Column(name="NOTE")
    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "riskFromUser")
    private List<UsersEntity> usersEntityList;
}
