package com.topwulian.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云配置
 * 
 * @author 小威老师
 *
 */
@Configuration
public class AliyunConfig {

	@Value("${file.aliyun.endpoint}")
	private String endpoint;
	@Value("${file.aliyun.accessKeyId}")
	private String accessKeyId;
	@Value("${file.aliyun.accessKeySecret}")
	private String accessKeySecret;

	/**
	 * 阿里云文件存储client
	 * 
	 */
	@Bean
	public OSSClient ossClient() {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		return ossClient;
	}


	public static void main(String[] args) throws FileNotFoundException {

		OSSClient ossClient = new OSSClient("oss-cn-beijing.aliyuncs.com", "LTAI3jTQMjLamd0v", "aOR1ZFUoJCKmiSUUQopZcwZDu0uei6");
		InputStream inputStream = new FileInputStream("D://ssfw.sql");

		ossClient.putObject("topwulian", "upload/" + "ss11fw.sql", inputStream);

		Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
		// 生成URL
		URL url = ossClient.generatePresignedUrl("topwulian", "upload/" + "ss11fw.sql", expiration);

		System.out.println(url);


	}



}
