package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.*;

import com.topwulian.model.ProductBatches;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProductBatchesDao {


    @Results({@Result( property="tasks",javaType=List.class,column="id",many=@Many(select="com.topwulian.dao.TTaskDao.getAllProductBatchId"))})
    @Select("select * from product_batches t where t.id = #{id}")
    ProductBatches getById(Long id);

    @Delete("delete from product_batches where id = #{id}")
    int delete(Long id);

    int update(ProductBatches productBatches);
    
    @SelectKey(statement="SELECT LAST_INSERT_ID()",keyProperty="id",before=false,resultType=Long.class)
    @Insert({"insert into product_batches(product_name, product_batch, area, recoverydate, img, feature, sys_user_id,farm_id) values(#{productName}, #{productBatch}, #{area}, #{recoverydate}, #{img}, #{feature}, #{sysUserId},#{framId})"})
    int save(ProductBatches productBatches);

    @Select("select count(1) from product_batches where farm_id=#{params.farm_id}")
    int count(@Param("params") Map<String, Object> params);

    @Select("select id,product_batch from product_batches where farm_id=#{farm_id}")
    List<Map<String,Object>> getProductBatchList(Integer farm_id);

    @Select("select id,product_batch from product_batches where farm_id=#{farm_id} AND isactive = #{isactive}")
    List<Map<String,Object>> getProductBatchByActive(@Param("farm_id") Integer farm_id, @Param("isactive") char isactive);

    @Select("select * from product_batches where farm_id=#{params.farm_id} limit #{offset},#{limit}")
    List<ProductBatches> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
