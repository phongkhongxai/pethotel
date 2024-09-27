package com.bumble.pethotel.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryService {
    List<String> uploadFiles(List<MultipartFile> files, String folderName);
}
