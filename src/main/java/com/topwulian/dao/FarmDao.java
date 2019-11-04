package com.topwulian.dao;

import com.topwulian.model.Farm;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FarmDao {

    @Select("select * from t_farm t where t.id = #{id}")
    Farm getById(Long id);

    @Delete("delete from t_farm where id = #{id}")
    int delete(Long id);

    int update(Farm farm);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_farm(province, city, district, type, name, linkman, telephone, remark, createTime, updateTime, userId, mainCrop, area, img) values(#{province}, #{city}, #{district}, #{type}, #{name}, #{linkman}, #{telephone}, #{remark}, #{createTime}, #{updateTime}, #{userId}, #{mainCrop}, #{area}, #{img})")
    int save(Farm farm);
    
    int count(@Param("params") Map<String, Object> params);

    List<Farm> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from t_farm where userId=#{userId}")
    List<Farm> getFarmListByUserId(@Param("userId") Long userId);

    @Select("select * from t_farm f inner join t_user_farm uf on f.id = uf.farmId where uf.userId = #{userId}")
    List<Farm> listByUserId(Long userId);



}
