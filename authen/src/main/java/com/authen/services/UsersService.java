package com.authen.services;

import com.authen.domain.response.BaseResultDTO;


public interface UsersService {

//    BaseResultDTO findAllUsers(Integer pageSize, Integer page);
//    BaseResultDTO findUsersByFullNameAndUserName(SearchUsersRequestDTO requestDTO);
//    BaseResultDTO addUser(UsersDTO usersDTO);
//    BaseResultDTO updateUser(UsersDTO usersDTO);
    BaseResultDTO findUserEntityByUserName(String token);

//    BaseResultDTO findAllUsersNotListId(IdRequestDTO requestDTO);
//    BaseResultDTO updateActiceUser(String userName);
//    BaseResultDTO updateImageID(String userName);
//    BaseResultDTO deleteUser(Long id);
//
//    BaseResultDTO findUserbyId(Long id);
////    @PreAuthorize("hasAuthority('MANAGER')")
//    BaseResultDTO findAll();
//    BaseResultDTO updateRoleUser(Long id, Long roleId);
////    BaseResultDTO findUserByUserName(String userName);
//
//
//    // ====== START SERVICES FOR AUTHENTICATION ======
//    AuthResponseDTO generateToken(AuthRequestDTO userForAuthentication);

    // ====== END ======
}
