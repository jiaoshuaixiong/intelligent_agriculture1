package com.topwulian.job;

import com.alibaba.fastjson.JSON;
import com.topwulian.dao.DeviceDao;
import com.topwulian.dao.SysLogsDao;
import com.topwulian.dao.SysYs7AccountDao;
import com.topwulian.dao.VedioDao;
import com.topwulian.dto.Data;
import com.topwulian.dto.HttpResult;
import com.topwulian.model.*;
import com.topwulian.service.impl.HttpAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: szz
 * @Date: 2019/1/4 上午12:07
 * @Version 1.0
 */
@Component
public class SpringTaskJobs {

    @Autowired
    private SysLogsDao sysLogsDao;

    @Autowired
    private HttpAPIService httpAPIService;

    @Autowired
    private SysYs7AccountDao sysYs7AccountDao;

    @Autowired
    private VedioDao vedioDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定时为萤石云账号获取视频设备的accessToken,因为这个token一周会过期一次
     * 接口参考地址:https://open.ys7.com/doc/zh/book/index/user.html
     * 每周一23点15分执行任务
     * @param
     * @return
     */
    @Scheduled(cron = "0 15 23 ? * MON")
    @Transactional
    public void getAccessTokenAtEveryWeek()throws Exception{

        SysLogs sysLogs=new SysLogs();
        sysLogs.setModule("定时任务");

        sysLogs.setFlag(true);
        sysLogs.setCreateTime(new Date());
        sysLogs.setUpdateTime(new Date());

        //萤石云获取摄像头控制key的接口,一周需要执行一次
        String url="https://open.ys7.com/api/lapp/token/get";

        //得到所有的萤石云开发账号
        List<SysYs7Account> sysYs7AccountList = sysYs7AccountDao.list(null, null, null);
        for (SysYs7Account sysYs7Account : sysYs7AccountList) {
            Map<String, Object> param=new HashMap<>();
            param.put("appKey",sysYs7Account.getAppKey());
            param.put("appSecret",sysYs7Account.getAppSecret());
            //这个接口不能频繁请求,为防止多账号情况下的请求次数过多,需要sleep
            Thread.sleep(30000);
            HttpResult httpResult = httpAPIService.doPost(url, param);
            Map mapMsg = JSON.parseObject(httpResult.getBody(), Map.class);
            Data data = JSON.parseObject(mapMsg.get("data").toString(),Data.class);
            sysYs7Account.setAccessToken(data.getAccessToken());
            //更新数据库
            sysYs7AccountDao.update(sysYs7Account);

            //根据userId查询该用户所拥有的全部摄像头,并将它们的accessKey也一起更新
            Map<String, Object> vedioParam = new HashMap<>();
            vedioParam.put("", sysYs7Account.getUserId());
            List<Vedio> vedioList = vedioDao.list(vedioParam, null, null);
            for (Vedio vedio : vedioList) {
                vedio.setAccessToken(sysYs7Account.getAccessToken());
                vedio.setUpdateTime(new Date());
                vedioDao.update(vedio);
            }

            //记录日志
            sysLogs.setRemark(data.toString());
            User user=new User();
            user.setId(new Long(sysYs7Account.getUserId()));
            sysLogs.setUser(user);
            sysLogsDao.save(sysLogs);
        }
    }

    /**
     * 项目启动后从数据库将各设备的阈值查询出来放入redis
     * 五分钟一次
     */
    //0 */5 * * * ?
    @Scheduled(cron = "0 */5 * * * ?")
    public void getDeviceThresholdFromRedis() {
        //redisTemplate.delete("deviceThreshold");
        Set deviceThreshold = redisTemplate.opsForHash().keys("deviceThreshold");
        if (deviceThreshold.size() < 1) {
            List<Device> deviceList = deviceDao.list(null, null, null);
            for (Device device : deviceList) {
                redisTemplate.opsForHash().put("deviceThreshold",device.getId(),device);
            }
        }
        System.out.println(deviceThreshold.size());
    }
}
