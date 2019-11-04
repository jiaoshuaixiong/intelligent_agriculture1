package com.topwulian.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topwulian.dao.FarmDao;
import com.topwulian.dao.UserDao;
import com.topwulian.model.RespEntiry;
import com.topwulian.model.User;
import com.topwulian.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableResponse;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;

import com.topwulian.dao.TProducterDao;
import com.topwulian.model.TProducter;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tProducters")
public class TProducterController {

    @Autowired
    private TProducterDao tProducterDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public RespEntiry save(@RequestBody TProducter tProducter) {
        TProducter tProducterSure = tProducterDao.getByUserName(tProducter.getUsername());
        if(tProducterSure != null) {
            throw new IllegalArgumentException(tProducterSure.getUsername() + "已存在");
        }
        TProducter tProducterSure1 = tProducterDao.getByPhone(tProducter.getPhone());
        if(tProducterSure1 != null) {
            throw new IllegalArgumentException(tProducterSure1.getPhone() + "已存在");
        }
        User user = UserUtil.getCurrentUser();
        tProducter.setSysUserId(Integer.valueOf(user.getId().toString()));
        tProducter.setIsactive("Y");
        //获取基地
        int farmId = UserUtil.getUserFarmId();
        tProducter.setFarmId(farmId);
        tProducterDao.save(tProducter);
        return RespEntiry.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public TProducter get(@PathVariable Long id) {
        return tProducterDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public TProducter update(@RequestBody TProducter tProducter) {
        TProducter tProducterSure = tProducterDao.getByUserName(tProducter.getUsername());
        if(tProducterSure != null && tProducterSure.getId() != tProducter.getId()) {
            throw new IllegalArgumentException(tProducterSure.getUsername() + "已存在");
        }
        TProducter tProducterSure1 = tProducterDao.getByPhone(tProducter.getPhone());
        if(tProducterSure1 != null && tProducter.getId().intValue() != tProducterSure1.getId().intValue()) {
            throw new IllegalArgumentException(tProducterSure1.getPhone() + "已存在");
        }
        tProducterDao.update(tProducter);

        return tProducter;
    }


    @GetMapping("/getProducterList")
    @ApiOperation(value = "推送查询可用的生产人员")
    List<Map<String,Object>> getProducterList(){
        int farmId = UserUtil.getUserFarmId();
        List<Map<String, Object>> producterList = tProducterDao.getProducterList(farmId);
        return producterList;
    }


    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                //获取基地id
                int farmId = UserUtil.getUserFarmId();
                Map<String,Object> map = new HashMap();
                map.put("farm_id",farmId);
                request.setParams(map);
                return tProducterDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<TProducter> list(PageTableRequest request) {
                //获取基地id
                int farmId = UserUtil.getUserFarmId();
                Map<String,Object> map = new HashMap();
                map.put("farm_id",farmId);
                request.setParams(map);
                return tProducterDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        tProducterDao.delete(id);
    }
}
