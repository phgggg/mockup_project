package com.itsol.mockup.services;

import com.itsol.mockup.entity.FileEntity;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {
    ArrayResultDTO<FileEntity> findAll();
    BaseResultDTO addFile(MultipartFile file, HttpServletRequest httpServletRequest, Long userId, Long newVerOf);
    BaseResultDTO deleteFiles(Long id);
    BaseResultDTO findFileById(Long id, Long userId);
    BaseResultDTO searchFileByKeyword(String key);
    BaseResultDTO shareToOtherUser(Long id, Long teamId);

    Resource loadFileAsResource(String fileName);
}
