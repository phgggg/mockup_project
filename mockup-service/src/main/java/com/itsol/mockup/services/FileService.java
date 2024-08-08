package com.itsol.mockup.services;

import com.itsol.mockup.entity.FileEntity;
import com.itsol.mockup.web.dto.file.FileSearchDTO;
import com.itsol.mockup.web.dto.file.FileShareDTO;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService {
    ArrayResultDTO<FileEntity> findAll();
    BaseResultDTO addFile(MultipartFile file, HttpServletRequest httpServletRequest, Long newVerOf, Long project_id, String token);
    BaseResultDTO deleteFiles(Long id);
    BaseResultDTO findFileById(Long id, Long userId);
    BaseResultDTO searchFileByKeyword(FileSearchDTO key);
    BaseResultDTO searchFileByKeyword2(FileSearchDTO key);
    BaseResultDTO searchFileByKeywordTest();
    BaseResultDTO shareToSingleUser(FileShareDTO fileShareDTO, String token, HttpServletRequest httpServletRequest);
    BaseResultDTO shareToTeam(FileShareDTO fileShareDTO, String token, HttpServletRequest httpServletRequest);
    BaseResultDTO OpenPreviousVer(Long id);
    BaseResultDTO AddFileType(Long fileId, int[] fileTypeList);
//    Resource loadFileAsResource(String fileName);
    Resource loadFileAsResource(String filename, HttpServletRequest request, String token);
    Resource loadFileAsResource(String filename, HttpServletRequest request);
}
