package com.itsol.mockup.repository;

import com.itsol.mockup.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleByRoleId(Long id);
    RoleEntity findRoleByName(Long id);
}
