package com.topwulian.controller;

import com.beidouapp.et.client.api.ISDK;
import com.beidouapp.et.client.callback.ICallback;
import com.topwulian.annotation.LogAnnotation;
import com.topwulian.config.EtContextConfig;
import com.topwulian.dao.Pm25Dao;
import com.topwulian.dto.ResponseInfo;
import com.topwulian.model.Pm25;
import com.topwulian.page.table.PageTableHandler;
import com.topwulian.page.table.PageTableHandler.CountHandler;
import com.topwulian.page.table.PageTableHandler.ListHandler;
import com.topwulian.page.table.PageTableRequest;
import com.topwulian.page.table.PageTableResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/pm25s")
public class Pm25Controller {

    @Autowired
    private Pm25Dao pm25Dao;

    @Autowired
    private ISDK sdk;

    @GetMapping("/findAll")
    @ApiOperation(value = "获取最近10个pm2.5数据")
    public List<Pm25> findAll() {
        return pm25Dao.findAll();
    }

    @LogAnnotation
    @ApiOperation(value = "pm2.5传感器开关")
    @PostMapping("/switch/{alert_value}")
    public ResponseInfo switchPm25(@PathVariable Long alert_value) {
        System.out.println("pm2.5传感器控制:"+alert_value);

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
        return new ResponseInfo("1", "pm2.5传感器操作成功!");
    }

    @PostMapping
    @ApiOperation(value = "保存")
    public Pm25 save(@RequestBody Pm25 pm25) {
        pm25Dao.save(pm25);

        return pm25;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public Pm25 get(@PathVariable Long id) {
        return pm25Dao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public Pm25 update(@RequestBody Pm25 pm25) {
        pm25Dao.update(pm25);

        return pm25;
    }

    @GetMapping
    @ApiOperation(value = "列表")
    public PageTableResponse list(PageTableRequest request) {
        return new PageTableHandler(new CountHandler() {

            @Override
            public int count(PageTableRequest request) {
                return pm25Dao.count(request.getParams());
            }
        }, new ListHandler() {

            @Override
            public List<Pm25> list(PageTableRequest request) {
                return pm25Dao.list(request.getParams(), request.getOffset(), request.getLimit());
            }
        }).handle(request);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        pm25Dao.delete(id);
    }
}
