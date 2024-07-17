package com.itsol.mockup.repository;

import com.itsol.mockup.entity.TimeSheetEntity;
import com.itsol.mockup.entity.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimesheetRepository extends JpaRepository<TimeSheetEntity,Long> {

    TimeSheetEntity getTimeSheetEntityByTimesheetId(Long id);
    Page<TimeSheetEntity> findTimeSheetEntitiesByUsersEntity(UsersEntity usersEntity, Pageable pageable);
    @Query(value = "SELECT t.* FROM timesheet t JOIN project p ON t.project_id = p.project_id WHERE p.project_id = :id",
           nativeQuery = true)
    List<TimeSheetEntity> findTimeSheetEntitiesByProjectId(Long id);
    @Query(value = "SELECT t.* FROM timesheet t JOIN project p ON t.project_id = p.project_id WHERE t.user_id = :id",
            nativeQuery = true)
    List<TimeSheetEntity> findTimeSheetEntitiesByUserId(Long id);

    @Query(value = "SELECT t.* FROM timesheet t JOIN project p ON t.project_id = p.project_id WHERE t.user_id = :id and p.project_id = :projectId",
            nativeQuery = true)
    List<TimeSheetEntity> findTimeSheetEntitiesByUserIdAndProjectId(Long id, Long projectId);

}
