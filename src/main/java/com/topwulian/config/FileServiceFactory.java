package com.topwulian.config;

import com.topwulian.model.FileSource;
import com.topwulian.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * FileService工厂<br>
 * 将各个实现类放入map
 * 
 * @author 小威老师
 *
 */
@Configuration
public class FileServiceFactory {

	private Map<FileSource, FileService> map = new HashMap<>();

	@Autowired
	private FileService localFileServiceImpl;
	@Autowired
	private FileService aliyunFileServiceImpl;

	@PostConstruct
	public void init() {
		map.put(FileSource.LOCAL, localFileServiceImpl);
		map.put(FileSource.ALIYUN, aliyunFileServiceImpl);
	}

	public FileService getFileService(String fileSource) {
		if (StringUtils.isBlank(fileSource)) {
			return localFileServiceImpl;
		}

		return map.get(FileSource.valueOf(fileSource));
	}
}
