package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.WxPlatform;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WxPlatformDao {

    @Select("select * from wx_platform t where t.id = #{id}")
    WxPlatform getById(Long id);

    @Select("select * from wx_platform t where t.farm_id = #{id}")
    WxPlatform getByFarmId(Long id);

    @Delete("delete from wx_platform where id = #{id}")
    int delete(Long id);

    int update(WxPlatform wxPlatform);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into wx_platform(ID, FARM_ID, ISACTIVE, APPID, APPSECRET) values(#{ID}, #{FARMID}, #{ISACTIVE}, #{APPID}, #{APPSECRET})")
    int save(WxPlatform wxPlatform);
    
    int count(@Param("params") Map<String, Object> params);

    List<WxPlatform> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
