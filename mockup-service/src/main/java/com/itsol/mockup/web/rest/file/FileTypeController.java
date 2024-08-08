package com.itsol.mockup.web.rest.file;
import com.itsol.mockup.services.FileService;
import com.itsol.mockup.services.FileTypeService;
import com.itsol.mockup.web.dto.file.FileSearchDTO;
import com.itsol.mockup.web.dto.file.FileShareDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.rest.BaseRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/file/type")
public class FileTypeController extends BaseRest {
    @Autowired
    FileTypeService fileTypeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> findAll() {
        BaseResultDTO result = fileTypeService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResultDTO> addFile(@RequestParam("fileTypeName") String fileTypeName)
            throws IOException {
        BaseResultDTO result = fileTypeService.addFileType(fileTypeName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
