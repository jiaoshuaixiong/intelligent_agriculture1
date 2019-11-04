package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.Pm25;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface Pm25Dao {

    @Select("select * from t_pm25 t where t.id = #{id}")
    Pm25 getById(Long id);

    @Delete("delete from t_pm25 where id = #{id}")
    int delete(Long id);

    int update(Pm25 pm25);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_pm25(pm, ddatetime, obtid, createTime, updateTime) values(#{pm}, #{ddatetime}, #{obtid}, #{createTime}, #{updateTime})")
    int save(Pm25 pm25);
    
    int count(@Param("params") Map<String, Object> params);

    List<Pm25> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from (SELECT  * from t_pm25 t ORDER BY t.ddatetime desc limit 10) t2 order by t2.ddatetime")
    List<Pm25> findAll();
}
