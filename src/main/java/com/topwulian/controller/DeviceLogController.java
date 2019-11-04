package com.topwulian.controller;

import java.util.List;

import com.topwulian.model.User;
import org.apache.shiro.SecurityUtils;
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

import com.topwulian.dao.DeviceLogDao;
import com.topwulian.model.DeviceLog;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/deviceLogs")
public class DeviceLogController {

    @Autowired
    private DeviceLogDao deviceLogDao;

    @PostMapping
    @ApiOperation(value = "保存")
    public DeviceLog save(@RequestBody DeviceLog deviceLog) {
        deviceLogDao.save(deviceLog);

        return deviceLog;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public DeviceLog get(@PathVariable Long id) {
        return deviceLogDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public DeviceLog update(@RequestBody DeviceLog deviceLog) {
        deviceLogDao.update(deviceLog);

        return deviceLog;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return deviceLogDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<DeviceLog> list(PageTableRequest request) {
                return deviceLogDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @GetMapping("/deviceNotice")
    public List<DeviceLog> deviceNotice(){
        User User = (User) SecurityUtils.getSubject().getPrincipal();
        return deviceLogDao.deviceNotice(0,3,User.getId());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        deviceLogDao.delete(id);
    }
}
