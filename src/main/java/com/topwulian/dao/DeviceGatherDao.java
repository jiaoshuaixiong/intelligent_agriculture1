package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import com.topwulian.dto.DeviceGatherCharts;
import com.topwulian.page.table.PageTableRequest;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.DeviceGather;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeviceGatherDao {

    @Select("select * from t_device_gather t where t.id = #{id}")
    DeviceGather getById(Long id);

    @Delete("delete from t_device_gather where id = #{id}")
    int delete(Long id);

    int update(DeviceGather deviceGather);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_device_gather(deviceId, deviceSn, deviceName, deviceType, basicData, measurementUnitId, measurementUnitName, measureUnitType, gatherTime, createTime, updateTime) values(#{deviceId}, #{deviceSn}, #{deviceName}, #{deviceType}, #{basicData}, #{measurementUnitId}, #{measurementUnitName}, #{measureUnitType}, #{gatherTime}, #{createTime}, #{updateTime})")
    int save(DeviceGather deviceGather);
    
    int count(@Param("params") Map<String, Object> params);
    int count1(@Param("params") Map<String, Object> params, @Param("farmId") int farmId);
    List<DeviceGather> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
    //@Select("SELECT t.*  FROM t_device_gather t LEFT JOIN t_device tt ON tt.id=t.deviceId WHERE tt.farmId=#{farmId}")
    List<DeviceGather> list1(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("farmId") int farmId);
    //ORDER BY id DESC LIMIT 1
    @Select("select * from t_device_gather where deviceId=#{deviceId} ORDER BY id DESC LIMIT #{rows}")
    List<DeviceGather> getByDeviceId(@Param("deviceId") Long deviceId, @Param("rows") Integer rows);

    List<DeviceGatherCharts> echartsShow(PageTableRequest pageTableRequest);
}
