package com.topwulian.controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.beidouapp.et.client.api.ISDK;
import com.beidouapp.et.client.callback.ICallback;
import com.topwulian.annotation.LogAnnotation;
import com.topwulian.dao.SysLogsDao;
import com.topwulian.dto.ResponseInfo;
import com.topwulian.model.SysLogs;
import com.topwulian.model.User;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.topwulian.dao.TemperatureDao;
import com.topwulian.model.Temperature;
import com.topwulian.config.EtContextConfig;

import io.swagger.annotations.ApiOperation;
@Api("温度控制")
@RestController
@RequestMapping("/temperatures")
public class TemperatureController {

    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private TemperatureDao temperatureDao;

    @Autowired
    private ISDK sdk;

    @GetMapping("/findAll")
    @ApiOperation(value = "获取最近10个温度数据")
    public List<Temperature> findAll() {
        System.out.println("异步定时访问了数据库:"+new Date());
        return temperatureDao.findAll();
    }

    @PostMapping
    @ApiOperation(value = "保存")
    public Temperature save(@RequestBody Temperature temperature) {
        temperatureDao.save(temperature);
        return temperature;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public Temperature get(@PathVariable Long id) {
        return temperatureDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Temperature update(@RequestBody Temperature temperature) {
        temperatureDao.update(temperature);

        return temperature;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return temperatureDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<Temperature> list(PageTableRequest request) {
                return temperatureDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        temperatureDao.delete(id);
    }
}
