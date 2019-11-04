package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.DeviceType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeviceTypeDao {

    @Select("select * from t_device_type t where t.id = #{id}")
    DeviceType getById(Long id);

    @Delete("delete from t_device_type where id = #{id}")
    int delete(Long id);

    int update(DeviceType deviceType);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_device_type(name, description, remark, operator, createTime, updateTime) values(#{name}, #{description}, #{remark}, #{operator}, #{createTime}, #{updateTime})")
    int save(DeviceType deviceType);
    
    int count(@Param("params") Map<String, Object> params);

    List<DeviceType> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from t_device_type")
    List<DeviceType> getAllDeviceType();
}
