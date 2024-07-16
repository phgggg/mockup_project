package com.itsol.mockup.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "USERS_ROLE")
@Getter
@Setter
public class UserRolesEntity {

    @Column(name="ROLE_ID")
    private Long roleId;

    @Id
    @Column(name="USERS_ID")
    private Long userId;
}