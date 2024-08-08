package com.itsol.mockup.services;

import com.itsol.mockup.entity.FileTypeEntity;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;

public interface FileTypeService {
    ArrayResultDTO<FileTypeEntity> findAll();
    BaseResultDTO addFileType(String fileTypeName);
}
