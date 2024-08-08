package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.FileEntity;
import com.itsol.mockup.entity.ProjectEntity;
import com.itsol.mockup.entity.RoleEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.repository.FileRepository;
import com.itsol.mockup.services.FileService;
import com.itsol.mockup.web.FileStorageException;
import com.itsol.mockup.web.FileStorageProperties;
import com.itsol.mockup.web.MyFileNotFoundException;
import com.itsol.mockup.web.dto.file.FileDTO;
import com.itsol.mockup.web.dto.file.FileSearchDTO;
import com.itsol.mockup.web.dto.file.FileShareDTO;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.ShareStatusDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

@Service
public class FileServiceImpl extends BaseService implements FileService {
    private final Path fileStorageLocation; //Đường dẫn lưu file

    private static String UPLOAD_DIR = "Upload";

    @Autowired
    public FileServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize(); //get ra đường dẫn lưu file tuyệt đối trong file cấu hình property

        try {
            Files.createDirectories(this.fileStorageLocation); //Tạo đường dẫn thư mục
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public ArrayResultDTO<FileEntity> findAll() {
        ArrayResultDTO<FileEntity> arrayResultDTO = new ArrayResultDTO<>();
        List<FileEntity> list = fileRepository.findAll();
        arrayResultDTO.setSuccess(list, (long) list.size(), 1);
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO addFile(MultipartFile file, HttpServletRequest httpServletRequest, Long newVerOf, Long project_id, String token) {
        logger.info("ADD FILES");

        SingleResultDTO singleResultDTO = new SingleResultDTO();
        String actualName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = UUID.randomUUID()+"_"+actualName;
        ArrayList<Long> allowedUser = new ArrayList<>();

        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            UsersEntity user = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
            List<String> roles = new ArrayList<>();
            for(RoleEntity r : user.getRoles()){
                roles.add(r.getName());
            }

//            if(roles.contains("user") && !extension.equalsIgnoreCase("txt")){
//                //user chi up file txt
//                singleResultDTO.setFail("khong duoc up file dang nay");
//                return singleResultDTO;
//            }

            ProjectEntity project = projectRepository.getProjectEntityByProjectId(project_id);
            String host = "http://" + httpServletRequest.getHeader("host") + "/" + UPLOAD_DIR + "/";
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("api/downloadFile/")
//                    .path(fileName)
//                    .toUriString(); //ghép nối các phần để tạo thành link download file
            allowedUser.add(user.getUserId());
            FileEntity fileEntity = new FileEntity( actualName, fileName,
                                                    new int[]{0}, extension, host + fileName,
                                                    user.getUserName(), getCurTimestamp(),
                                                    allowedUser,
                                                    user.getUserName(), getCurTimestamp(),
                                                    project);

            if(newVerOf>0){
                FileEntity previousVer = fileRepository.findFileEntitiesByFileId(newVerOf);
                fileEntity.setPreviousVer(newVerOf);
                fileEntity.setPreviousUrl(previousVer.getFileUrl());

                previousVer.setNextVer(fileEntity.getFileId());
                previousVer.setNextUrl(fileEntity.getFileUrl());
                previousVer.setLastModifiedDate(getCurTimestamp());
                previousVer.setLastModifiedBy(user.getUserName());
                fileRepository.save(previousVer);
            }
            fileEntity = fileRepository.save(fileEntity);

            singleResultDTO.setSuccess(fileEntity);
        } catch (Exception e) {
            singleResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO deleteFiles(Long id) {
        logger.info("DELETE IMAGES");
        SingleResultDTO singleResultDTO = new SingleResultDTO();
        try {
            if (id != null) {
                fileRepository.deleteById(id);
                singleResultDTO.setSuccess();
            }
            logger.info("DELETE IMAGES FROM:" + singleResultDTO.getErrorCode());
        } catch (Exception e) {
            singleResultDTO.setFail(e.getMessage());
            logger.error(e.getMessage());
        }

        return singleResultDTO;
    }

    @Override
    public BaseResultDTO findFileById(Long id, Long userId) {
        SingleResultDTO<FileEntity> singleResultDTO = new SingleResultDTO<>();
        try {
            FileEntity fileEntity = fileRepository.findFileEntitiesByFileId(id);
            if (fileEntity != null && fileEntity.getAllowedUser().contains(userId)) {
                singleResultDTO.setSuccess(fileEntity);
            }
            logger.info("SEARCH IMAGE BY ID RESPONSE: {}", singleResultDTO.getErrorCode());
        } catch (Exception e) {
            logger.error("findImageById err{}", e.getMessage(), e);
            singleResultDTO.setFail(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO searchFileByKeyword(FileSearchDTO key) {
        ArrayResultDTO<FileEntity> arrayResultDTO = new ArrayResultDTO<>();
        Page<FileEntity> res = fileRepositoryCustom.searchForFile(key);
        if(res.getContent().isEmpty()){
            arrayResultDTO.setFail("No result!");
        }else{
            arrayResultDTO.setSuccess(res.getContent(), Long.valueOf(key.getPageSize()), key.getPage());
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO searchFileByKeyword2(FileSearchDTO key) {
        ArrayResultDTO<FileDTO> arrayResultDTO = new ArrayResultDTO<>();
        Page<FileDTO> res = fileRepositoryCustom.searchData(key);
        if(res.getContent().isEmpty()){
            arrayResultDTO.setFail("No result!");
        }else{
            arrayResultDTO.setSuccess(res.getContent(), Long.valueOf(key.getPageSize()), key.getPage());
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO searchFileByKeywordTest() {
        ArrayResultDTO<FileDTO> arrayResultDTO = new ArrayResultDTO<>();
        FileEntity res = fileRepositoryCustom.test();
        if(res == null){
            arrayResultDTO.setFail("No result!");
        }else{
            arrayResultDTO.setSuccess();
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO shareToSingleUser(FileShareDTO fileShareDTO, String token, HttpServletRequest httpServletRequest) {
        SingleResultDTO result = new SingleResultDTO<>();
        ShareStatusDTO shareStatusDTO;
        Long id = fileShareDTO.getFileId();
        Long userId = fileShareDTO.getUserId();
        Timestamp curDate = getCurTimestamp();
        try{
            FileEntity fileToShare = fileRepository.findFileEntitiesByFileId(id);
            UsersEntity usersEntity = usersRepository.getUsersEntitiesByUserId(userId);
            ArrayList<Long> allowed = fileToShare.getAllowedUser();
            List<RoleEntity> userRoles = usersEntity.getRoles();
            List<Long> roleId = new ArrayList<>();
            for(RoleEntity r : userRoles){
                roleId.add(r.getRoleId());
            }
            if(allowed(fileToShare.getFileTypeList(), roleId)){
                allowed.add(userId);
                fileToShare.setAllowedUser(allowed);
                fileToShare.setLastModifiedBy(tokenUtils.getUsernameFromToken(token));
                fileToShare.setLastModifiedDate(curDate);
//            fileToShare.setProject(projectRepository.getProjectEntityByProjectId(8L));//lỗi
                fileRepository.save(fileToShare);
                String linkdown = fileToShare.getFileUrl();
                shareStatusDTO = new ShareStatusDTO(id,userId,curDate, tokenUtils.getUsernameFromToken(token),linkdown);
                result.setSuccess(shareStatusDTO);
            }
            else {
                result.setFail("not allowed");
            }
        } catch (Exception e){
            logger.error("error while finding file or team by id{}", e.getMessage(), e);
            result.setFail(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO shareToTeam(FileShareDTO fileShareDTO, String token, HttpServletRequest httpServletRequest) {
        SingleResultDTO result = new SingleResultDTO<>();
        ShareStatusDTO shareStatusDTO = null;
        List<ShareStatusDTO> teamShareStatusDTO = new ArrayList<>();
        Long fileId = fileShareDTO.getFileId();
        Long teamId = fileShareDTO.getTeamId();
        Timestamp curDate = getCurTimestamp();
        try{
            List<UsersEntity> usersEntities = usersRepository.findUserEntitiesByTeamId(teamId);
            FileEntity fileToShare = fileRepository.findFileEntitiesByFileId(fileId);
            for(UsersEntity user : usersEntities){
                shareStatusDTO = new ShareStatusDTO();
                ArrayList<Long> allowed = fileToShare.getAllowedUser();
                allowed.add(user.getUserId());
                fileToShare.setAllowedUser(allowed);
                fileToShare.setLastModifiedBy(tokenUtils.getUsernameFromToken(token));
                fileToShare.setLastModifiedDate(curDate);
                fileToShare.setProject(projectRepository.getProjectEntityByProjectId(8L));//lỗi
                fileRepository.save(fileToShare);
                String linkdown = "http://" + httpServletRequest.getHeader("host") + "/api/file"+"/downloadFile/"+fileToShare.getFileName();
                shareStatusDTO = new ShareStatusDTO(fileId,user.getUserId(),curDate, tokenUtils.getUsernameFromToken(token),linkdown);
                teamShareStatusDTO.add(shareStatusDTO);
            }
            result.setSuccess(teamShareStatusDTO);
        } catch (Exception e){
            logger.error("error while finding file or user list", e.getMessage(), e);
            result.setFail(e.getMessage());
        }
        return result;
    }

    @Override
    public BaseResultDTO OpenPreviousVer(Long id) {
        SingleResultDTO<FileEntity> singleResultDTO = new SingleResultDTO<>();
        try {

            FileEntity fileEntity = fileRepository.findFileEntitiesByFileId(id);

            if (fileEntity.getPreviousVer()!= 0) {
                FileEntity result = fileRepository.findFileEntitiesByFileId(fileEntity.getPreviousVer());
                singleResultDTO.setSuccess(result);
            }
            logger.info("SEARCH FILE BY ID RESPONSE: {}", singleResultDTO.getErrorCode());
        } catch (Exception e) {
            logger.error("findFileById err{}", e.getMessage(), e);
            singleResultDTO.setFail(e.getMessage());
        }
        return singleResultDTO;
    }

    @Override
    public BaseResultDTO AddFileType(Long fileId, int[] fileTypeList) {
        SingleResultDTO singleResultDTO = new SingleResultDTO<>();
        try {
            FileEntity file = fileRepository.findFileEntitiesByFileId(fileId);
            file.setFileTypeList(fileTypeList);
            fileRepository.save(file);
            singleResultDTO.setSuccess(file);
        } catch (Exception e){
            singleResultDTO.setFail(e.getMessage());
        }
        return singleResultDTO;
    }

//    @Override
//    public Resource loadFileAsResource(String fileName) {
//        try {
//            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//            Resource resource = new UrlResource(filePath.toUri());
//            if (resource.exists()) {
//                return resource;
//            } else {
//                throw new MyFileNotFoundException("File not found " + fileName);
//            }
//        } catch (MalformedURLException ex) {
//            throw new MyFileNotFoundException("File not found " + fileName, ex);
//        }
//    }

    @Override
    public Resource loadFileAsResource(String filename, HttpServletRequest request, String token) {
        UsersEntity user = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
        FileEntity file = fileRepository.findFileEntityByActualFileName(filename);
        int[] fileTypeList = file.getFileTypeList();
        List<RoleEntity> userRoles = user.getRoles();
        List<Long> roleId = new ArrayList<>();
        for(RoleEntity r : userRoles){
            roleId.add(r.getRoleId());
        }
        ArrayList<Long> allowedUser = file.getAllowedUser();
//        if(true){
        if(allowed(fileTypeList, roleId) && allowedUser.contains(user.getUserId())){
            Path filePath = Paths.get("src/main/webapp/Upload").resolve(filename).normalize();
            Resource resource = null;
            try {
                resource = new UrlResource(filePath.toUri());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            if (resource.exists()) {
                // Log thông tin người truy cập
                String userIP = request.getRemoteAddr();
                logger.info("File {} opened by IP: {}", filename, userIP);
                return resource;
            } else {
                logger.info("khong co file");
                return null;
            }
        }else {
            logger.info("Khong hop le");
        }
        return null;
    }

    @Override
    public Resource loadFileAsResource(String filename, HttpServletRequest request) {
//        UsersEntity user = usersRepository.findUsersEntityByUserName(tokenUtils.getUsernameFromToken(token));
        FileEntity file = fileRepository.findFileEntityByActualFileName(filename);
        int[] fileTypeList = file.getFileTypeList();
//        List<RoleEntity> userRoles = user.getRoles();
        List<Long> roleId = new ArrayList<>();
//        for(RoleEntity r : userRoles){
//            roleId.add(r.getRoleId());
//        }
        ArrayList<Long> allowedUser = file.getAllowedUser();
        if(true){
//        if(allowed(fileTypeList, roleId) && allowedUser.contains(user.getUserId())){
            Path filePath = Paths.get("src/main/webapp/Upload").resolve(filename).normalize();
            Resource resource = null;
            try {
                resource = new UrlResource(filePath.toUri());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            if (resource.exists()) {
                // Log thông tin người truy cập
                String userIP = request.getRemoteAddr();
                logger.info("File {} opened by IP: {}", filename, userIP);
                return resource;
            } else {
                logger.info("khong co file");
                return null;
            }
        }else {
            logger.info("Khong hop le");
        }
        return null;
    }

    private boolean allowed(int[] fileTypeList, List<Long> roleId){
        if(roleId.contains(1L) || roleId.contains(3L)) {
            logger.info("la manager/admin");
            return true;
        }
        for(int i = 0; i< fileTypeList.length;i++){
            switch (fileTypeList[i]){
                case 0:
                    logger.info("ai cung xem duoc");
                    return true;
                case 1://user k xem dc
                    if(roleId.contains(2L) && roleId.size() == 1){
                        logger.info("Chi user khong xem duoc");
                        return false;
                    }
                    else return true;
                case 2://dev
                    logger.info("dev xem duoc");
                    if(roleId.contains(5L)) return true;
                    break;
                case 3://test
                    logger.info("test xem duoc");
                    if(roleId.contains(4L)) return true;
                    break;
                case 4:
                    logger.info("Manager moi xem duoc");
                    break;
                case 5://ba
                    logger.info("ba xem duoc");
                    if(roleId.contains(6L)) return true;
                default:
                    return false;
            }
        }
        return false;
    }
}
