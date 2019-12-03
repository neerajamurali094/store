package com.diviso.graeshoppe.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import io.minio.MinioClient;

@Service
public class ImageService {

	private final Logger log = LoggerFactory.getLogger(ImageService.class);
	
	@Autowired
	private MinioClient minioClient;

	@Value("${minio.buckets.store}")
	private String storeBucketName;

	@Value("${minio.buckets.banner}")
	private String bannerBucketName;

	@Value("${minio.configuration.contentType}")
	private String contentType;
	
	@Value("${minio.server.url}")
	private String url;


	@SuppressWarnings("deprecation")
	public String saveFile(String type, String entityId, byte[] data) {
		InputStream streamData = new ByteArrayInputStream(data);
		String bucket = null;
		String imageName = null; 
		String imageLink = url;
		if (type.equals("store")) {
			bucket = storeBucketName;
			imageName = entityId+"-store-image.png";
			imageLink = url+"/"+bucket+"/"+imageName;
			log.info("Saving the store Image in bucket "+bucket+" imagename is "+imageName+" image link is "+imageLink);
		} else if (type.equals("banner")) {
			bucket = bannerBucketName;
			imageName = entityId+"-banner-image.png";
			imageLink = url+"/"+bucket+"/"+imageName;
			log.info("Saving the banner Image in bucket "+bucket+" imagename is "+imageName+"  image link is "+imageLink);
		}
		try {
			minioClient.putObject(bucket, imageName, streamData, contentType);
			log.info("Image has been saved successfully");

		} catch (Exception e) {
			log.error("Something went wrong while saving image to miniob server "+ e.getMessage());
		}
		
		return imageLink;
	}

}
