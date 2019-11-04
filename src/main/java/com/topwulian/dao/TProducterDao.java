package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import com.topwulian.model.TProducter;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TProducterDao {

    @Select("select * from t_producter t where t.id = #{id}")
    TProducter getById(Long id);

    @Select("select * from t_producter t where t.username = #{username}")
    TProducter getByUserName(String username);

    @Select("select * from t_producter t where t.phone = #{phone}")
    TProducter getByPhone(String phone);

    @Delete("delete from t_producter where id = #{id}")
    int delete(Long id);

    int update(TProducter tProducter);

    @Update("update t_producter set openid=#{openid} where id=#{id} ")
    int updateOpenid(TProducter tProducter);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_producter(isactive, username, phone, wechat, usercard, createdate, sys_user_id, producerTypeId, password,farm_id) values(#{isactive}, #{username}, #{phone}, #{wechat}, #{usercard}, #{createdate}, #{sysUserId}, #{producerTypeId}, #{password},#{farmId})")
    int save(TProducter tProducter);


    @Select("select id,username from t_producter where farm_id=#{farm_id} and isactive='Y'")
    List<Map<String,Object>> getProducterList(Integer farm_id);

    @Select("select count(1) from t_producter where farm_id=#{params.farm_id}")
    int count(@Param("params") Map<String, Object> params);

    @Select("select * from t_producter where farm_id=#{params.farm_id} limit #{offset},#{limit}")
    List<TProducter> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
