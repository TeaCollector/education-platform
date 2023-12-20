package ru.binarshiki.binmedia.config;

import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfig {

    @Value("${spring.minio.url}")
    private String url;

    @Value("${spring.minio.access-key}")
    private String userName;

    @Value("${spring.minio.secret-key}")
    private String password;

    @Bean
    public MinioAsyncClient minioClient() {
        log.info("URL: {}. username: {}. password: {}", url, userName, password);
        return MinioAsyncClient.builder()
                .endpoint(url)
                .credentials(userName, password)
                .build();
    }
}
