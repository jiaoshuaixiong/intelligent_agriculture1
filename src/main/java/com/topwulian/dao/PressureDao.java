package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.Pressure;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PressureDao {

    @Select("select * from t_pressure t where t.id = #{id}")
    Pressure getById(Long id);

    @Delete("delete from t_pressure where id = #{id}")
    int delete(Long id);

    int update(Pressure pressure);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_pressure(p, ddatetime, obtid, createTime, updateTime) values(#{p}, #{ddatetime}, #{obtid}, #{createTime}, #{updateTime})")
    int save(Pressure pressure);
    
    int count(@Param("params") Map<String, Object> params);

    List<Pressure> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from (SELECT  * from t_pressure t ORDER BY t.ddatetime desc limit 10) t2 order by t2.ddatetime")
    List<Pressure> findAll();
}
