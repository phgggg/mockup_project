package com.itsol.mockup.web.rest.file;

import com.itsol.mockup.services.FileService;
import com.itsol.mockup.web.dto.file.AddFileTypeDTO;
import com.itsol.mockup.web.dto.file.FileSearchDTO;
import com.itsol.mockup.web.dto.file.FileShareDTO;
import com.itsol.mockup.web.dto.response.BaseResultDTO;
import com.itsol.mockup.web.rest.BaseRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
@Scope("request")
@CrossOrigin
public class FileController extends BaseRest {
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
                                                , @RequestParam("newVerOf") Long newVerOf
                                                , @RequestParam("projectId") Long projectId
                                                , @RequestHeader HttpHeaders header)
            throws IOException {
        BaseResultDTO result = fileService.addFile(file, request, newVerOf, projectId, retrieveToken(header));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<BaseResultDTO> deleteFile(@RequestParam Long id) {
        BaseResultDTO result = fileService.deleteFiles(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/addType", method = RequestMethod.POST)
    public ResponseEntity<BaseResultDTO> addFileType(@RequestBody AddFileTypeDTO addFileTypeDTO)
            throws IOException {
        Long fileId = addFileTypeDTO.getFileId();
        int[] fileTypeList = addFileTypeDTO.getFileTypeList();
        BaseResultDTO result = fileService.AddFileType(fileId, fileTypeList);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> searchFile(@RequestBody FileSearchDTO key) {
        BaseResultDTO result = fileService.searchFileByKeyword(key);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/search2", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> searchFile2(@RequestBody FileSearchDTO key) {
        BaseResultDTO result = fileService.searchFileByKeyword2(key);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchtest", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> searchFileTest() {
        BaseResultDTO result = fileService.searchFileByKeywordTest();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/share", method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> shareToOtherUser(@RequestBody FileShareDTO fileShareDTO,
                                                          @RequestHeader HttpHeaders header
                                                        , HttpServletRequest request) {
        BaseResultDTO result = fileService.shareToSingleUser(fileShareDTO, retrieveToken(header), request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/shareToTeam", method = RequestMethod.PUT)
    public ResponseEntity<BaseResultDTO> shareToTeam(@RequestBody FileShareDTO fileShareDTO,
                                                          @RequestHeader HttpHeaders header
            , HttpServletRequest request) {
        BaseResultDTO result = fileService.shareToTeam(fileShareDTO, retrieveToken(header), request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/openPrevious", method = RequestMethod.GET)
    public ResponseEntity<BaseResultDTO> openPreviousVer(@RequestParam Long id) {
        BaseResultDTO result = fileService.OpenPreviousVer(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @RequestMapping(value = "/downloadFile/{fileName:.+}",method = RequestMethod.GET)
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
//
//        Resource resource = fileService.loadFileAsResource(fileName);
//        // Try to determine file's content type
//        String contentType = null;
//        try {
//            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
//        } catch (IOException ex) {
//           ex.getMessage();
//        }
//        // Fallback to the default content type if type could not be determined
//        if(contentType == null) {
//            contentType = "application/octet-stream";
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(contentType))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                .body(resource);
//    }

}
