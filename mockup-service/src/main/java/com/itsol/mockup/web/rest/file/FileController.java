package com.itsol.mockup.web.rest.file;

import com.itsol.mockup.services.FileService;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
@Scope("request")
@CrossOrigin
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> findAll() {
        BaseResultDTO result = fileService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> searchFileById(@RequestParam("id") Long id, @RequestParam("userId") Long userId) {
        BaseResultDTO result = fileService.findFileById(id, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //    headers = "Content-Type= multipart/form-data"
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResultDTO> addFile(@RequestPart("file") MultipartFile file, HttpServletRequest request
                                                , @RequestParam("id") Long userId, @RequestParam("newVerOf") Long newVerOf)
            throws IOException {
        BaseResultDTO result = fileService.addFile(file, request, userId, newVerOf);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<BaseResultDTO> deleteFile(@RequestParam Long id) {
        BaseResultDTO result = fileService.deleteFiles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> searchFile(@RequestParam String key) {
        BaseResultDTO result = fileService.searchFileByKeyword(key);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/share", method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> shareToOtherUser(@RequestParam("fileId") Long id, @RequestParam("teamId") Long teamId
                                                        , HttpServletRequest request) {
        BaseResultDTO result = fileService.shareToOtherUser(id, teamId, request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/downloadFile/{fileName:.+}",method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){

        Resource resource = fileService.loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
           ex.getMessage();
        }
        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
