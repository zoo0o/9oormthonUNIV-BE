package com.goormthonuniv.backend.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * AWS S3에 파일을 업로드하는 기능을 담당하는 클래스
 */
@Service
public class S3Uploader {

    // application.yml에서 S3 버킷 이름을 주입받음
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    // 생성자 주입
    public S3Uploader(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * 파일을 S3에 업로드하고 업로드된 파일의 URL을 반환
     *
     * @param file 업로드할 파일
     * @return 업로드된 파일의 URL
     * @throws IOException 파일 입출력 중 예외 발생 시
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 이름에 UUID를 붙여 중복 방지
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // 파일의 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        // S3에 파일 업로드
        amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);

        // 업로드된 파일의 URL 반환
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
