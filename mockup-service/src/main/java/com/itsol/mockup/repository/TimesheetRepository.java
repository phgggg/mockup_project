package com.itsol.mockup.repository;

import com.itsol.mockup.entity.SubTaskEntity;
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
    @Query(value = "SELECT t.* FROM timesheet t JOIN project p ON t.project_id = p.project_id WHERE t.users_id = :id",
            nativeQuery = true)
    List<TimeSheetEntity> findTimeSheetEntitiesByUserId(Long id);

    @Query(value = "SELECT t.* FROM timesheet t WHERE t.timesheet_id = :id",
            nativeQuery = true)
    TimeSheetEntity findTimeSheetEntitiesByTimeSheetId(Long id);

    @Query(value = "SELECT t.* FROM timesheet t WHERE t.task = :name",
            nativeQuery = true)
    List<TimeSheetEntity> findTimeSheetEntitiesByTask(String name);

    @Query(value = "SELECT t.* FROM timesheet t JOIN project p ON t.project_id = p.project_id WHERE t.users_id = :id and p.project_id = :projectId",
            nativeQuery = true)
    List<TimeSheetEntity> findTimeSheetEntitiesByUserIdAndProjectId(Long id, Long projectId);

    @Query(value = "SELECT count(*) FROM xe.timesheet where users_id = :id and project_id = :projectId",
            nativeQuery = true)
    int getTotalTaskCountByUserIdAndProjectId(Long id, Long projectId);

    @Query(value = "SELECT count(*) FROM xe.timesheet where project_id = :id",
            nativeQuery = true)
    int getTotalTaskCountByProjectId(Long id);

    @Query(value = "SELECT sum(st.hours_spent) " +
            "FROM timesheet t join subtask st on t.timesheet_id = st.timesheet_id " +
            "where t.timesheet_id = :id group by t.timesheet_id",
            nativeQuery = true)
    int getSumOfHoursSpentByTimeSheetId(Long id);

    @Query(value = "SELECT sum(st.estimated_hours) " +
            "FROM timesheet t join subtask st on t.timesheet_id = st.timesheet_id " +
            "where t.timesheet_id = :id group by t.timesheet_id",
            nativeQuery = true)
    int getSumOfEstimatedHoursByTimeSheetId(Long id);

    @Query(value = "select count(t.timesheet_id) from timesheet t where t.users_id = :uid and t.project_id = :id", nativeQuery = true)
    Long getTaskCountByProjectIdAndUserId(Long id, Long uid);

    @Query(value = "select t.* from timesheet t where t.users_id = :uid and t.project_id = :id", nativeQuery = true)
    List<TimeSheetEntity> getTaskByProjectIdAndUserId(Long id, Long uid);
}
