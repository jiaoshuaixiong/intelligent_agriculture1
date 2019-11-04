package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.topwulian.model.FileInfo;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileInfoDao {

	@Update("update file_info t set t.updateTime = now() where t.id = #{id}")
	int update(FileInfo fileInfo);

	List<FileInfo> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
                        @Param("limit") Integer limit);




    @Select("select * from file_info t where t.id = #{id}")
    FileInfo getById(String id);

    @Insert("insert into file_info(id, name, isImg, contentType, size, path, url, source, createTime) "
            + "values(#{id}, #{name}, #{isImg}, #{contentType}, #{size}, #{path}, #{url}, #{source}, #{createTime})")
    int save(FileInfo fileInfo);

    @Delete("delete from file_info where id = #{id}")
    int delete(String id);

    int count(@Param("params") Map<String, Object> params);

    List<FileInfo> findData(Map<String, Object> params);
}
