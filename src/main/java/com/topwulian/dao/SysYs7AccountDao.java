package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.topwulian.model.SysYs7Account;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysYs7AccountDao {

    @Select("select * from sys_ys7_account t where t.id = #{id}")
    SysYs7Account getById(Long id);

    @Delete("delete from sys_ys7_account where id = #{id}")
    int delete(Long id);

    int update(SysYs7Account sysYs7Account);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into sys_ys7_account(username, password, appKey, appSecret, accessToken, appName, userId, remark, createTime, updateTime) values(#{username}, #{password}, #{appKey}, #{appSecret}, #{accessToken}, #{appName}, #{userId}, #{remark}, #{createTime}, #{updateTime})")
    int save(SysYs7Account sysYs7Account);
    
    int count(@Param("params") Map<String, Object> params);

    List<SysYs7Account> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
