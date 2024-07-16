package com.authen.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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