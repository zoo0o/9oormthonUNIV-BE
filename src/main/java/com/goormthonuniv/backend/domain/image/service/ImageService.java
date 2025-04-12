package com.goormthonuniv.backend.domain.image.service;

import com.goormthonuniv.backend.domain.image.dto.ImageUploadResponse;
import com.goormthonuniv.backend.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final S3Uploader s3Uploader;

    /**
     * 이미지 업로드
     */
    public ImageUploadResponse uploadImage(MultipartFile file) {
        String imageUrl = s3Uploader.upload(file);
        return new ImageUploadResponse(imageUrl);
    }


}
