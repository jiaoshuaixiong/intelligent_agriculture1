package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.Vedio;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VedioDao {

    @Select("select * from t_vedio t where t.id = #{id}")
    Vedio getById(Long id);

    @Delete("delete from t_vedio where id = #{id}")
    int delete(Long id);

    int update(Vedio vedio);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_vedio(name, brand, accessToken, deviceSerial, channelNo, userId, farmId, state, createTime, updateTime, location, url,appKey,appSecret) values(#{name}, #{brand}, #{accessToken}, #{deviceSerial}, #{channelNo}, #{userId}, #{farmId}, #{state}, #{createTime}, #{updateTime}, #{location}, #{url},#{appKey},#{appSecret})")
    int save(Vedio vedio);
    
    int count(@Param("params") Map<String, Object> params);

    List<Vedio> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from t_vedio where userId=#{userId}")
    List<Vedio> getVedioListByUserId(@Param("userId") Long userId);

    @Select("select * from t_vedio where farmId=#{farmId}")
    List<Vedio> getVedioListByFarmId(@Param("farmId") Long farmId);
}
