package com.topwulian.dao;

import com.topwulian.model.VedioImages;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface VedioImagesDao {

    @Select("select * from t_vedio_images t where t.id = #{id}")
    VedioImages getById(Long id);

    @Delete("delete from t_vedio_images where id = #{id}")
    int delete(Long id);

    int update(VedioImages vedioImages);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_vedio_images(vedioId, path, url, deviceSerial, farmId, createTime, updateTime) values(#{vedioId}, #{path}, #{url}, #{deviceSerial}, #{farmId}, #{createTime}, #{updateTime})")
    int save(VedioImages vedioImages);
    
    int count(@Param("params") Map<String, Object> params);

    List<VedioImages> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
