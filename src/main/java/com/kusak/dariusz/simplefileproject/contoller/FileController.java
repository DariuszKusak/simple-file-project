package com.kusak.dariusz.simplefileproject.contoller;

import com.kusak.dariusz.simplefileproject.model.UploadFileResponseVO;
import com.kusak.dariusz.simplefileproject.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {

        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/uploadFile")
    public UploadFileResponseVO uploadFile(@RequestParam MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/file/downloadFile/").path(fileName)
                .toUriString();

        return UploadFileResponseVO.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();
    }

    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity<List<UploadFileResponseVO>> uploadFiles(@RequestParam MultipartFile[] files) {
        return new ResponseEntity<>(Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList()), HttpStatus.OK);
    }


}
