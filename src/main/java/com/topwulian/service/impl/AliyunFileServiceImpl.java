package com.topwulian.service.impl;

import com.aliyun.oss.OSSClient;
import com.topwulian.dao.FileDao;
import com.topwulian.model.FileInfo;
import com.topwulian.model.FileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云存储文件
 * 
 * @author 小威老师
 *
 */
@Service("aliyunFileServiceImpl")
public class AliyunFileServiceImpl extends AbstractFileService {

	@Autowired
	private FileDao fileDao;

	@Override
	protected FileDao getFileDao() {
		return fileDao;
	}

	@Override
	protected FileSource fileSource() {
		return FileSource.ALIYUN;
	}

	@Autowired
	private OSSClient ossClient;

	@Value("${file.aliyun.bucketName}")
	private String bucketName;
	@Value("${file.aliyun.domain}")
	private String domain;

	@Override
	protected void uploadFile(MultipartFile file, FileInfo fileInfo) throws Exception {
		ossClient.putObject(bucketName, fileInfo.getName(), file.getInputStream());
		fileInfo.setUrl(domain + "/" + fileInfo.getName());
	}

	@Override
	protected boolean deleteFile(FileInfo fileInfo) {
		ossClient.deleteObject(bucketName, fileInfo.getName());
		return true;
	}

}
