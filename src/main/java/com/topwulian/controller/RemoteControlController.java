package com.topwulian.controller;

import com.beidouapp.et.client.api.ISDK;
import com.beidouapp.et.client.callback.ICallback;
import com.topwulian.annotation.LogAnnotation;
import com.topwulian.config.EtContextConfig;
import com.topwulian.dto.ResponseInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CountDownLatch;

/**
 * /remote_control/switch/shade_net/
 * 远程控制
 */
@Api("远程控制")
@RestController
@RequestMapping("/remote_control")
public class RemoteControlController {

    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Autowired
    private ISDK sdk;

    @LogAnnotation
    @ApiOperation(value = "遮阳网开关")
    @PostMapping("/switch/shade_net/{alert_value}")
    public ResponseInfo switchShadeNet(@PathVariable Long alert_value) {
        System.out.println("遮阳网控制:"+alert_value);
        byte[] msg ={1};
        if (alert_value==1L){
            msg=new byte[]{1};
        }else if(alert_value==2L){
            msg=new byte[]{2};
        }
        System.out.println(msg);

        final CountDownLatch cdl3 = new CountDownLatch(1);
        sdk.chatTo(EtContextConfig.RECEIVE_UID, msg, new ICallback<Void>() {
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
        return new ResponseInfo("1", "遮阳网操作成功!");
    }

    @LogAnnotation
    @ApiOperation(value = "风机开关")
    @PostMapping("/switch/twyer/{alert_value}")
    public ResponseInfo switchTwyer(@PathVariable Long alert_value) {
        System.out.println("风机控制:"+alert_value);
        byte[] msg ={3};
        if (alert_value==1L){
            msg=new byte[]{3};
        }else if(alert_value==2L){
            msg=new byte[]{4};
        }

        final CountDownLatch cdl3 = new CountDownLatch(1);
        sdk.chatTo(EtContextConfig.RECEIVE_UID, msg, new ICallback<Void>() {
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
        return new ResponseInfo("1", "风机操作成功!");
    }

    @LogAnnotation
    @ApiOperation(value = "灌溉泵开关")
    @PostMapping("/switch/irrigation_pump/{alert_value}")
    public ResponseInfo switchIrrigationPump(@PathVariable Long alert_value) {
        System.out.println("灌溉泵控制:"+alert_value);
        byte[] msg ={5};
        if (alert_value==1L){
            msg=new byte[]{5};
        }else if(alert_value==2L){
            msg=new byte[]{6};
        }

        final CountDownLatch cdl3 = new CountDownLatch(1);
        sdk.chatTo(EtContextConfig.RECEIVE_UID, msg, new ICallback<Void>() {
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
        return new ResponseInfo("1", "灌溉泵操作成功!");
    }

    @LogAnnotation
    @ApiOperation(value = "增氧泵开关")
    @PostMapping("/switch/oxygen_pump/{alert_value}")
    public ResponseInfo switchOxygenPump(@PathVariable Long alert_value) {
        System.out.println("增氧泵控制:"+alert_value);
        byte[] msg ={7};
        if (alert_value==1L){
            msg=new byte[]{7};
        }else if(alert_value==2L){
            msg=new byte[]{8};
        }

        final CountDownLatch cdl3 = new CountDownLatch(1);
        sdk.chatTo(EtContextConfig.RECEIVE_UID, msg, new ICallback<Void>() {
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
        return new ResponseInfo("1", "增氧泵操作成功!");
    }

}