package com.itsol.mockup.repository;

import com.itsol.mockup.entity.UsersEntity;
import org.apache.catalina.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import javax.jws.soap.SOAPBinding;
import java.util.List;


@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    UsersEntity getUsersEntitiesByUserId (Long id);
    Page<UsersEntity> getAllByUserNameAndFullName(String userName, String fullName, Pageable pageable);
    List<UsersEntity> findAll();
    UsersEntity getUsersByUserName(String username); //function for generate token
    UsersEntity findUsersEntityByUserName(String userName);
    UsersEntity findUsersEntityByEmail(String email);
    UsersEntity findUsersEntityByUserId(Long id);

    @Query(value = "select u.* " +
            "from team t\n" +
            "join users u\n" +
            "on t.team_id = u.team_id\n" +
            "where t.team_id = :id",nativeQuery = true)
    List<UsersEntity> findUserEntitiesByTeamId(Long id);

    @Query(value = "select u.* from team t " +
            "join team_users tu on t.team_id = tu.team_id " +
            "join users u on tu.users_id = u.users_id " +
            "where project_id = :id",nativeQuery = true)
    List<UsersEntity> findUsersInTeamByProjectId(Long id);
}
