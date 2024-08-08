package com.itsol.mockup.repository;

import com.itsol.mockup.entity.FileEntity;
import com.itsol.mockup.web.dto.file.FileDTO;
import com.itsol.mockup.web.dto.file.FileSearchDTO;
import org.springframework.data.domain.Page;

public interface FileRepositoryCustom {
    Page<FileEntity> searchForFile(FileSearchDTO fileSearchDTO);
    Page<FileDTO> searchData(FileSearchDTO fileSearchDTO);
//    FileEntity getFileByFileIdAndTeamId(FileShareDTO fileShareDTO);
}
