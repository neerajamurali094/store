package com.diviso.graeshoppe.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

@Configuration
public class MinioServerConfiguration {
	
	
	private final Logger log = LoggerFactory.getLogger(MinioServerConfiguration.class);

	@Value("${minio.server.url}")
	private String url;

	@Value("${minio.server.accessKey}")
	private String accesskey;

	@Value("${minio.server.secretKey}")
	private String secretKey;

	@Value("${minio.buckets.store}")
	private String storeBucketName;

	@Value("${minio.buckets.banner}")
	private String bannerBucketName;

	@Bean
	public MinioClient getMinioClient() throws InvalidEndpointException, InvalidPortException {
		MinioClient minioClient = new MinioClient(url, accesskey, secretKey);
		try {
			boolean storeBucketFound = minioClient.bucketExists(storeBucketName);
			boolean bannerBucketFound = minioClient.bucketExists(bannerBucketName);
			if (storeBucketFound) {
				log.info("StoreBucket already exists " + storeBucketName);
			} else {
				minioClient.makeBucket(storeBucketName);
				log.info("StoreBucket created " + storeBucketName);
			}

			if (bannerBucketFound) {
				log.info("BannerBucket already exists " + bannerBucketName);
			} else {
				minioClient.makeBucket(bannerBucketName);
				log.info("BannerBucket created " + bannerBucketName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return minioClient;
	}

}
