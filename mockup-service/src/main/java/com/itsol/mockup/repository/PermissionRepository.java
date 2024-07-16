package com.itsol.mockup.repository;

import com.itsol.mockup.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity,Long> {
    PermissionEntity getPermissionEntityByPermissionId(Long id);
}
