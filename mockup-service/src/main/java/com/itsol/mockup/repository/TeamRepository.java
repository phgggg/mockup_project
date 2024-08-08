package com.itsol.mockup.repository;

import com.itsol.mockup.entity.TeamEntity;
import com.itsol.mockup.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    TeamEntity findTeamEntityByTeamId(Long id);


}
