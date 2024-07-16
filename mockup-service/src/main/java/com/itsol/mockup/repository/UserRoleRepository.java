package com.itsol.mockup.repository;

import com.itsol.mockup.entity.UserRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRolesEntity, Long> {
}
