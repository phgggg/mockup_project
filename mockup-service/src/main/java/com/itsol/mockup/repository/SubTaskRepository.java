package com.itsol.mockup.repository;

import com.itsol.mockup.entity.SubTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubTaskRepository extends JpaRepository<SubTaskEntity, Long> {

    @Query(value = "SELECT count(id) / " +
            "(select count(id) FROM subtask" +
            " where timesheet_id = :id)" +
            "FROM subtask where status = \"done\"  and timesheet_id = :id",nativeQuery = true)
    Double subTaskDoneCount(long id);

    @Query(value = "select count(st.id) from subtask st join timesheet t on st.timesheet_id = t.timesheet_id where t.project_id = :id", nativeQuery = true)
    Long getSubTaskCountByProjectId(Long id);

    @Query(value = "select st.* from subtask st join timesheet t on st.timesheet_id = t.timesheet_id where t.project_id = :id", nativeQuery = true)
    List<SubTaskEntity> getSubTaskByProjectId(Long id);

    @Query(value = "select count(st.id) " +
            "from subtask st join timesheet t on st.timesheet_id = t.timesheet_id " +
            "where t.project_id = :id and st.assigned_user_id = :uid", nativeQuery = true)
    Long getSubTaskCountByProjectIdAndUserId(Long id, Long uid);

    @Query(value = "select st.* " +
            "from subtask st join timesheet t on st.timesheet_id = t.timesheet_id " +
            "where t.project_id = :id and st.assigned_user_id = :uid", nativeQuery = true)
    List<SubTaskEntity> getSubTaskByProjectIdAndUserId(Long id, Long uid);

}
