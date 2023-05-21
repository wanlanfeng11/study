package com.wansir.media.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data        //提供set方法完成属性注入
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    /**
     * MinIO的服务地址
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 存储bucket名称
     */
    private String bucketName;

    /**
    * 声明MinioClient组件
    */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}