package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.MeasurementUnit;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MeasurementUnitDao {

    @Select("select * from t_measurement_unit t where t.id = #{id}")
    MeasurementUnit getById(Long id);

    @Delete("delete from t_measurement_unit where id = #{id}")
    int delete(Long id);

    int update(MeasurementUnit measurementUnit);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_measurement_unit(unitTypes, cnName, enName, createTime, updateTime, operator) values(#{unitTypes}, #{cnName}, #{enName}, #{createTime}, #{updateTime}, #{operator})")
    int save(MeasurementUnit measurementUnit);
    
    int count(@Param("params") Map<String, Object> params);

    List<MeasurementUnit> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
