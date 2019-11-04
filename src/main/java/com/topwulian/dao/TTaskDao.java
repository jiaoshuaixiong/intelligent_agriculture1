package com.topwulian.dao;

import java.util.List;
import java.util.Map;

import com.topwulian.dto.TTaskDto;
import com.topwulian.dto.TaskDto;
import com.topwulian.model.ProductBatches;
import org.apache.ibatis.annotations.*;

import com.topwulian.model.TTask;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TTaskDao {

    @Select("select * from t_task t where t.product_batches_id = #{id}")
    List<TTask> getAllProductBatchId(Long id);

    @Select("select * from t_task t where t.id = #{id}")
    TTask getById(Long id);


    @Delete("delete from t_task where id = #{id}")
    int delete(Long id);

    @Delete("delete from t_task where product_batches_id = #{id}")
    int deleteByProductBatchesId(Long id);

    int update(TTask tTask);

    @Update("update t_task set img1=#{img1},img2=#{img2},img3=#{img3},state=2,area=#{area} where id = #{id}")
    int updatePush(TTask tTask);

    @Update("update t_task set desc_content=#{descContent},activity_date=#{activityDate},finish_date=#{finishDate},create_time=#{createDate},producter_id=#{producterId},state=1 where id = #{id}")
    int updatePull(TTask tTask);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_task(product_batches_id, content, state, img1, img2, img3, img4, producter_id, sys_user_id,farm_id) values(#{productBatchesId}, #{content}, #{state}, #{img1}, #{img2}, #{img3}, #{img4}, #{producterId}, #{sysUserId},#{farmId})")
    int save(TTask tTask);

    @Select("select id,content from t_task where farm_id=#{farmId} and product_batches_id=#{product_batches_id}")
    List<Map<String,Object>> getTaskContentList(@Param("farmId") Integer farmId, @Param("product_batches_id") Integer product_batches_id);

    @Select("select id,content from t_task where farm_id=#{farmId} and product_batches_id=#{product_batches_id} AND producter_id IS NULL")
    List<Map<String,Object>> getTaskByProducter(@Param("farmId") Integer farmId, @Param("product_batches_id") Integer product_batches_id);

    @Select("select img1,img2,img3,img4 from t_task where farm_id=#{farmId} and product_batches_id=#{product_batches_id}")
    List<Map<String,Object>> getImgsList(@Param("farmId") Integer farmId, @Param("product_batches_id") Integer product_batches_id);


    @Select("select prod.username,task.* from t_task task inner join t_producter prod  where prod.id = task.producter_id and  task.farm_id=#{params.farm_id} AND product_batches_id=#{params.product_batches_id} order by finish_date desc limit #{offset},#{limit} ")
    List<TaskDto> getTaskAllList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select count(1) from t_task where farm_id=#{params.farm_id} and product_batches_id=#{params.product_batches_id} AND producter_id IS NOT NULL")
    int countByBatchId(@Param("params") Map<String, Object> params);


    @Select("select count(1) from t_task where farm_id=#{params.farm_id} and state = 3 ")
    int count(@Param("params") Map<String, Object> params);

    @Select("select count(1) from t_task where producter_id=#{params.producter_id} and state is not null")
    int countByProducterId(@Param("params") Map<String, Object> params);

    /*
        生产人员所有的任务列表
     */
    @Select("select * from t_task where producter_id=#{params.producter_id} and state is not null limit #{offset},#{limit}")
    List<TTask> getTaskByProducterId(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);


    @Select("select * from t_task where farm_id=#{params.farm_id} and state=2 limit #{offset},#{limit}")
    List<TTask> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select prod.username,task.* from t_task task inner join t_producter prod  where prod.id = task.producter_id and  task.farm_id=#{params.farm_id} and state = 3 order by finish_date desc limit #{offset},#{limit} ")
    List<TaskDto> getTaskDtoList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Results({@Result( property="productBatches",javaType=ProductBatches.class,column="product_batches_id",one=@One(select="com.topwulian.dao.ProductBatchesDao.getById"))})
    @Select("select * from t_task where producter_id=#{params.producter_id} and state=#{params.state} limit #{offset},#{limit}")
    List<TTask> getByProducterIdList(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select({"select * from t_task where product_batches_id=#{id} and state=3"})
    @ResultType(TTaskDao.class)
    List<TTaskDto> getByMobileList(Long id);

    @Select("select count(1) from t_task where producter_id=#{params.producter_id} and state=#{params.state}")
    Integer getByProducterIdCount(@Param("params") Map<String, Object> params);

    @Select("select * from t_task where farm_id=#{params.farm_id} and state=3 limit #{offset},#{limit}")
    List<TTask> listSuccess(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
