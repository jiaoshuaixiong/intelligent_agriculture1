package com.topwulian.service.impl;

import com.topwulian.dao.ProductBatchesDao;
import com.topwulian.dao.TTaskDao;
import com.topwulian.model.ProductBatches;
import com.topwulian.model.TTask;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.service.ProductBatchesService;
import com.topwulian.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ProductBatchesServiceImpl implements ProductBatchesService{

    @Autowired
    private ProductBatchesDao productBatchesDao;

    @Autowired
    private TTaskDao tTaskDao;

    @Override
    @Transactional
    public void insert(ProductBatches productBatches, Map<String, String> tasks) {

        log.info("插入产品批次数据:{}",productBatches);
        //获取基地id
        int farmId = UserUtil.getUserFarmId();

        productBatches.setFramId(farmId);

        productBatchesDao.save(productBatches);
        //拿到id 依次组装,插入task表

        for(int i =1;i<= tasks.size();i++){
            TTask tTask = new TTask();
            tTask.setContent(tasks.get("task"+i));
            tTask.setProductBatchesId(productBatches.getId().intValue());
            tTask.setSysUserId(productBatches.getSysUserId());
            tTask.setFarmId(farmId);
            log.info("插入任务数据:{}",tTask);
            tTaskDao.save(tTask);
        }
    }

    @Override
    @Transactional
    public void update(ProductBatches productBatches, Map<String, String> tasks) {

        log.info("插入产品批次数据:{}",productBatches);
        int farmId = UserUtil.getUserFarmId();
        //更新图片的操作有问题，如果图片为null，会覆盖数据库已经有的数据
        productBatchesDao.update(productBatches);
        //拿到id 依次组装,插入task表
        List<TTask> allProductBatchId = tTaskDao.getAllProductBatchId(productBatches.getId());
        for (int i = 0; i < allProductBatchId.size(); i++) {
            TTask tTask = allProductBatchId.get(i);
            log.debug("{}",tasks.get("task"+(i+1)));
            tTask.setContent(tasks.get("task"+(i+1)));
            tTaskDao.update(tTask);
        }
        int size = allProductBatchId.size() - tasks.size();
        if(size >= 0) {
            int mbs = Math.abs(size);
            //不能删除
        } else {//新增加
            int mbs = Math.abs(size);
            for (int i = 0; i < mbs; i++) {
                TTask tTask = new TTask();
                int n = i + allProductBatchId.size();
                tTask.setContent(tasks.get("task"+(n+1)));
                tTask.setProductBatchesId(productBatches.getId().intValue());
                tTask.setSysUserId(productBatches.getSysUserId());
                tTask.setFarmId(farmId);
                tTaskDao.save(tTask);
            }
        }

    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        log.info("删除产品批次id:{}",id);
        productBatchesDao.delete(id);
        log.info("删除产品批次任务product_batches_id:{}",id);
        tTaskDao.deleteByProductBatchesId(id);
        log.info("删除产品批次id:{}，成功!",id);
    }

    @Override
    public List<?> list(PageTableRequest request) {

        //获取基地id
        int farmId = UserUtil.getUserFarmId();
        Map<String,Object> map = new HashMap();
        map.put("farm_id",farmId);
        request.setParams(map);
        return productBatchesDao.list(request.getParams(), request.getOffset(), request.getLimit());
    }

    @Override
    public int count(PageTableRequest request) {
        //获取基地id
        int farmId = UserUtil.getUserFarmId();
        Map<String,Object> map = new HashMap();
        map.put("farm_id",farmId);
        request.setParams(map);

        return productBatchesDao.count(request.getParams());
    }
}
