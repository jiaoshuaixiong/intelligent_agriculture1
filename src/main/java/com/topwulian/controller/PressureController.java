package com.topwulian.controller;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.beidouapp.et.client.api.ISDK;
import com.beidouapp.et.client.callback.ICallback;
import com.topwulian.annotation.LogAnnotation;
import com.topwulian.config.EtContextConfig;
import com.topwulian.dto.ResponseInfo;
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

import com.topwulian.dao.PressureDao;
import com.topwulian.model.Pressure;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/pressures")
public class PressureController {

    @Autowired
    private PressureDao pressureDao;

    @Autowired
    private ISDK sdk;

    @GetMapping("/findAll")
    @ApiOperation(value = "获取最近10个气压数据")
    public List<Pressure> findAll() {
        return pressureDao.findAll();
    }

    @LogAnnotation
    @ApiOperation(value = "气压传感器开关")
    @PostMapping("/switch/{alert_value}")
    public ResponseInfo switchPressure(@PathVariable Long alert_value) {
        System.out.println("气压传感器控制:"+alert_value);

        //消息发送
        String msg = EtContextConfig.MSG_CONTENT;
        String msg1 = "e68891e698afe4b880e58faae78caa";
        msg = EtContextConfig.toChangeByHex(msg.getBytes());

        final CountDownLatch cdl3 = new CountDownLatch(1);
        sdk.chatTo(EtContextConfig.RECEIVE_UID, msg.getBytes(), new ICallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("~~~~~~~~~chatTo(" + EtContextConfig.RECEIVE_UID + ")成功");
                cdl3.countDown();
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("*********chatTo(" + EtContextConfig.RECEIVE_UID + ")失败. " + ex.getLocalizedMessage());
                cdl3.countDown();
            }
        });
        return new ResponseInfo("1", "气压传感器操作成功!");
    }

    @PostMapping
    @ApiOperation(value = "保存")
    public Pressure save(@RequestBody Pressure pressure) {
        pressureDao.save(pressure);

        return pressure;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public Pressure get(@PathVariable Long id) {
        return pressureDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Pressure update(@RequestBody Pressure pressure) {
        pressureDao.update(pressure);

        return pressure;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return pressureDao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<Pressure> list(PageTableRequest request) {
                return pressureDao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        pressureDao.delete(id);
    }
}
