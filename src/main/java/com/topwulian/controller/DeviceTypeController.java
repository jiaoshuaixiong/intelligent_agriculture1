package com.topwulian.controller;

import com.topwulian.dao.DeviceTypeDao;
import com.topwulian.dao.FarmDao;
import com.topwulian.dao.UserDao;
import com.topwulian.model.DeviceType;
import com.topwulian.model.Farm;
import com.topwulian.model.User;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deviceTypes")
public class DeviceTypeController {

    @Autowired
    private DeviceTypeDao deviceTypeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FarmDao farmDao;

    @GetMapping("/all")
    @ApiOperation(value = "全部的设备类型列表")
    public Map<String,Object> getAllDeviceType(){
        List<User> userList = userDao.list(null, null, null);
        List<Farm> farmList = farmDao.list(null, null, null);
        List<DeviceType> deviceTypeList = deviceTypeDao.getAllDeviceType();
        Map<String,Object> map=new HashMap<>();
        map.put("userList",userList);
        map.put("farmList",farmList);
        map.put("deviceTypeList",deviceTypeList);
        return map;
    }

    @PostMapping
    @ApiOperation(value = "保存")
    public DeviceType save(@RequestBody DeviceType deviceType) {
        deviceTypeDao.save(deviceType);

        return deviceType;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public DeviceType get(@PathVariable Long id) {
        return deviceTypeDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public DeviceType update(@RequestBody DeviceType deviceType) {
        deviceTypeDao.update(deviceType);

        return deviceType;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return deviceTypeDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<DeviceType> list(PageTableRequest request) {
                return deviceTypeDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        deviceTypeDao.delete(id);
    }
}
