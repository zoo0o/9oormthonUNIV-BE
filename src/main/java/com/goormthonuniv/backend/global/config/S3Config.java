package com.goormthonuniv.backend.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * AmazonS3 클라이언트를 생성하여 Bean으로 등록합니다.
     * Spring Boot 애플리케이션에서 S3에 접근하기 위해 필요한 클라이언트입니다.
     *
     * @return AmazonS3 클라이언트 객체
     */
    @Bean
    public AmazonS3 amazonS3() {
        // AWS 자격 증명 객체 생성
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // S3 클라이언트 생성 및 리전, 자격 증명 설정
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)  // S3의 리전 설정
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))  // 자격 증명 설정
                .build();  // S3 클라이언트 빌드
    }
}
