package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.TProducterType;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TProducterTypeDao {

    @Select("select * from t_producter_type t where t.id = #{id}")
    TProducterType getById(Long id);

    @Delete("delete from t_producter_type where id = #{id}")
    int delete(Long id);

    int update(TProducterType tProducterType);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_producter_type(typeName) values(#{typeName})")
    int save(TProducterType tProducterType);
    
    int count(@Param("params") Map<String, Object> params);

    List<TProducterType> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
