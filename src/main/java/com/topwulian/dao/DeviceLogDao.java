package com.topwulian.dao;

import com.topwulian.model.DeviceLog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DeviceLogDao {

    @Select("select * from t_device_log t where t.id = #{id}")
    DeviceLog getById(Long id);

    @Delete("delete from t_device_log where id = #{id}")
    int delete(Long id);

    int update(DeviceLog deviceLog);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_device_log(userId, deviceId, farmId, deviceName, level, content, createTime, updateTime) values(#{userId}, #{deviceId}, #{farmId}, #{deviceName}, #{level}, #{content}, #{createTime}, #{updateTime})")
    int save(DeviceLog deviceLog);
    
    int count(@Param("params") Map<String, Object> params);

    List<DeviceLog> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from t_device_log t where userId=#{userId} order by createTime desc limit #{offset},#{row} ")
    List<DeviceLog> deviceNotice(@Param("offset") Integer offset, @Param("row") Integer row, @Param("userId") Long userId);

    @Select("select * from t_device_log t order by createTime desc limit #{offset},#{row} ")
    List<DeviceLog> deviceNewNotice(@Param("offset") Integer offset, @Param("row") Integer row);
}
