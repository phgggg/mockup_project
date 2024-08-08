package com.itsol.mockup.web.rest.file;

import com.itsol.mockup.services.FileService;
import com.itsol.mockup.web.dto.project.ProjectStatusDTO;
import com.itsol.mockup.web.rest.BaseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class OpenFileController extends BaseRest {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileService fileService;

    @GetMapping("/Upload")
    public String test(){
        logger.info("chay bt");
        List<Field> allFields = Arrays.asList(ProjectStatusDTO.class.getDeclaredFields());
        for(Field f : allFields){
            logger.info("a\t"+f.getName());
        }
        return "chay bt";
    }

    @GetMapping("/Upload/{filename}")
    public ResponseEntity<Resource> openFile(@PathVariable String filename,
                                             HttpServletRequest request,
                                             @RequestHeader HttpHeaders header) {
        Resource resource = fileService.loadFileAsResource(filename, request, retrieveToken(header));

        if (resource == null) {
            logger.info("Khong lay duoc file");
            return ResponseEntity.notFound().build();
        }

        // Determine the file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/Upload/test/{filename}")
    public ResponseEntity<Resource> openFile(@PathVariable String filename,
                                             HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(filename, request);

        if (resource == null) {
            logger.info("Khong lay duoc file");
            return ResponseEntity.notFound().build();
        }

        // Determine the file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
