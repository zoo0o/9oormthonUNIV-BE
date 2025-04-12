package com.goormthonuniv.backend.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * AWS S3에 파일 업로드 및 삭제를 담당하는 컴포넌트
 */
@Service
public class S3Uploader {

    // application.yml에 설정된 버킷 이름 주입
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    /**
     * AmazonS3 클라이언트 주입
     */
    public S3Uploader(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * 파일을 S3에 업로드하고 URL을 반환
     *
     * @param file 업로드할 파일
     * @return 업로드된 파일의 접근 가능한 URL
     */
    public String upload(MultipartFile file) {
        try {
            // 파일 이름에 UUID 추가하여 중복 방지
            String fileName = "images/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 파일 메타데이터 설정
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // S3에 파일 업로드
            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

            // 업로드된 파일의 URL 반환
            return amazonS3.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 중 오류 발생", e);
        }
    }

    /**
     * S3에 업로드된 이미지 삭제
     *
     * @param imageUrl 삭제할 이미지의 전체 URL
     */
    public void delete(String imageUrl) {
        // 비어 있는 경우 삭제하지 않음
        if (imageUrl == null || imageUrl.isBlank()) return;

        // S3 key 추출 후 삭제
        String key = extractKey(imageUrl);
        amazonS3.deleteObject(bucket, key);
    }

    /**
     * 이미지 URL에서 S3 key 추출
     * (버킷 주소 이후 경로만 잘라냄)
     *
     * @param imageUrl 전체 URL
     * @return S3 key (폴더/파일명 형태)
     */
    private String extractKey(String imageUrl) {
        return imageUrl.substring(imageUrl.indexOf(".com/") + 5);
    }
}
