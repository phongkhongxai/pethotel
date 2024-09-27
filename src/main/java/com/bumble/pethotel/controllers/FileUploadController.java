package com.bumble.pethotel.controllers;

import com.bumble.pethotel.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class FileUploadController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") List<MultipartFile> files,
                                                    @RequestParam("folderName") String folderName) {
        List<String> uploadedFileUrls = cloudinaryService.uploadFiles(files, folderName);
        return ResponseEntity.ok(uploadedFileUrls);
    }
}