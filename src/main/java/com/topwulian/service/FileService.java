package com.topwulian.service;

import com.topwulian.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	FileInfo upload(MultipartFile file) throws Exception;

	void delete(FileInfo fileInfo);

}
