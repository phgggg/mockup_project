package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.FileEntity;
import com.itsol.mockup.entity.TeamEntity;
import com.itsol.mockup.entity.UsersEntity;
import com.itsol.mockup.repository.FileRepository;
import com.itsol.mockup.repository.TeamRepository;
import com.itsol.mockup.services.FileService;
import com.itsol.mockup.web.FileStorageException;
import com.itsol.mockup.web.FileStorageProperties;
import com.itsol.mockup.web.MyFileNotFoundException;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl extends BaseService implements FileService {
    private final Path fileStorageLocation; //Đường dẫn lưu file

    private static String UPLOAD_DIR = "Upload";

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TeamRepository teamRepository;

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
        arrayResultDTO.setSuccess(fileRepository.findAll(), 1L, 2);
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO addFile(MultipartFile file, HttpServletRequest httpServletRequest, Long userId, Long newVerOf) {
        logger.info("ADD FILES");
        ArrayList<Long> allowedUser = new ArrayList<>();

        SingleResultDTO singleResultDTO = new SingleResultDTO();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String host = "http://" + httpServletRequest.getHeader("host") + "/" + UPLOAD_DIR + "/";
        FileEntity previousVer = fileRepository.findFileEntitiesByFileId(newVerOf);

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("api/downloadFile/")
//                    .path(fileName)
//                    .toUriString(); //ghép nối các phần để tạo thành link download file
            FileEntity fileEntity = new FileEntity(fileName, host + fileName);
            allowedUser.add(userId);
            fileEntity.setAllowedUser(allowedUser);
            if(newVerOf!=0){
                fileEntity.setPreviousVer(newVerOf);
                fileEntity.setPreviousUrl(previousVer.getFileUrl());
                previousVer.setNextVer(fileEntity.getFileId());
                previousVer.setNextUrl(fileEntity.getFileUrl());
                previousVer = fileRepository.save(previousVer);
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
    public BaseResultDTO searchFileByKeyword(String key) {
        ArrayResultDTO<FileEntity> arrayResultDTO = new ArrayResultDTO<>();
        List<FileEntity> resultList = new ArrayList<>();
        for(FileEntity imageObj : fileRepository.findAll()){
            if(imageObj.getFileName().toLowerCase().contains(key.toLowerCase())){
                resultList.add(imageObj);
            }
        }
        if(resultList.isEmpty()){
            arrayResultDTO.setFail("No result!");
        }else{
            arrayResultDTO.setSuccess(resultList);
        }
        return arrayResultDTO;
    }

    @Override
    public BaseResultDTO shareToOtherUser(Long id, Long teamId) {
        BaseResultDTO baseResultDTO = new BaseResultDTO();
        try{
            FileEntity fileToShare = fileRepository.findFileEntitiesByFileId(id);
            TeamEntity teamEntity = teamRepository.findTeamEntityByTeamId(teamId);
            ArrayList<Long> allowed = new ArrayList<>();
            for(UsersEntity user : teamEntity.getUsersEntities()){
                allowed.add(user.getUserId());
            }
            fileToShare.setAllowedUser(allowed);
            fileRepository.save(fileToShare);
            baseResultDTO.setSuccess();
            baseResultDTO.setDescription("http://localhost:8888"+"/api/file"+"/downloadFile/"+fileToShare.getFileName());
        } catch (Exception e){
            logger.error("error while finding file or team by id{}", e.getMessage(), e);
            baseResultDTO.setFail(e.getMessage());
        }
        return baseResultDTO;
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
