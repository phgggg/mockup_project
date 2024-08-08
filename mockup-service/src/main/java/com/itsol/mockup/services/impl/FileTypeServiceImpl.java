package com.itsol.mockup.services.impl;

import com.itsol.mockup.entity.FileTypeEntity;
import com.itsol.mockup.repository.FileTypeRepository;
import com.itsol.mockup.services.FileTypeService;
import com.itsol.mockup.web.dto.response.ArrayResultDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.dto.response.SingleResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileTypeServiceImpl extends BaseService implements FileTypeService {

    @Override
    public ArrayResultDTO<FileTypeEntity> findAll() {
        ArrayResultDTO<FileTypeEntity> result = new ArrayResultDTO<>();
        List<FileTypeEntity> list = fileTypeRepository.findAll();
        result.setSuccess(list, (long)list.size(), 1);
        return result;
    }

    @Override
    public BaseResultDTO addFileType(String fileTypeName) {
        SingleResultDTO result = new SingleResultDTO<>();
        try{
            String info = "File dạng này có thể xem được bởi "+ fileTypeName;
            FileTypeEntity fileType = new FileTypeEntity(fileTypeName, info);
            fileTypeRepository.save(fileType);
            result.setSuccess(fileType);
        }catch (Exception e){
            result.setFail(e.getMessage());
        }
        return result;
    }
}
