package com.authen.repository;

import com.authen.domain.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
//    UsersEntity getUsersEntitiesByUserId (Long id);
//    Page<UsersEntity> getAllByUserNameAndFullName(String userName, String fullName, Pageable pageable);
//    List<UsersEntity> findAll();
    UsersEntity getUsersByUserName(String username); //function for generate token
    UsersEntity findUsersEntityByUserName(String userName);
//    UsersEntity findUsersEntityByEmail(String email);
//    UsersEntity findUsersEntityByUserId(Long id);
}
