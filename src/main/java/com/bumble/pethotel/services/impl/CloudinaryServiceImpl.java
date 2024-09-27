package com.bumble.pethotel.services.impl;

import com.bumble.pethotel.services.CloudinaryService;
import com.cloudinary.Cloudinary;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Resource
    private Cloudinary cloudinary;
    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String folderName) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                HashMap<Object, Object> options = new HashMap<>();
                options.put("folder", folderName);
                Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), options);
                String publicId = (String) uploadedFile.get("public_id");
                String url = cloudinary.url().secure(true).generate(publicId);
                urls.add(url);
            } catch (IOException e) {
                e.printStackTrace();
                urls.add("default"); // Hoặc bạn có thể bỏ qua tệp này nếu có lỗi
            }
        }
        return urls;
    }
}
