package com.topwulian.dao;

import com.topwulian.model.FileInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FileDao {

	@Select("select * from file_info t where t.id = #{id}")
    FileInfo getById(String id);

	@Insert("insert into file_info(id, name, isImg, contentType, size, path, url, source, createTime) "
			+ "values(#{id}, #{name}, #{isImg}, #{contentType}, #{size}, #{path}, #{url}, #{source}, #{createTime})")
	int save(FileInfo fileInfo);

	@Delete("delete from file_info where id = #{id}")
	int delete(String id);

	int count(Map<String, Object> params);

	List<FileInfo> findData(Map<String, Object> params);
}
