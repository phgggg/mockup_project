package com.itsol.mockup.repository;

import com.itsol.mockup.entity.TaskDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDetailRepository extends JpaRepository<TaskDetailEntity, Long> {
}
