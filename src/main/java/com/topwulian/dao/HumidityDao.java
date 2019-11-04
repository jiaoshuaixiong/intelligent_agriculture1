package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.Humidity;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HumidityDao {

    @Select("select * from t_humidity t where t.id = #{id}")
    Humidity getById(Long id);

    @Delete("delete from t_humidity where id = #{id}")
    int delete(Long id);

    int update(Humidity humidity);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_humidity(h, ddatetime, obtid, createTime, updateTime) values(#{h}, #{ddatetime}, #{obtid}, #{createTime}, #{updateTime})")
    int save(Humidity humidity);
    
    int count(@Param("params") Map<String, Object> params);

    List<Humidity> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from (SELECT  * from t_humidity t ORDER BY t.ddatetime desc limit 10) t2 order by t2.ddatetime")
    List<Humidity> findAll();
}
