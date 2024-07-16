package com.itsol.mockup.repository;

import com.itsol.mockup.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
    ProjectEntity getProjectEntityByProjectId(Long id);

    @Query(value = "select p.* from project p join timesheet t on p.project_id = t.project_id join users u on t.user_id = u.users_id where u.users_id = :userId group by project_id",
            nativeQuery = true)
    List<ProjectEntity> getProjectEntitiesByUserId(Long userId);
}
