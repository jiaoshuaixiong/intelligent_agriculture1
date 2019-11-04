package com.topwulian.service.impl;

import com.alibaba.fastjson.JSON;
import com.topwulian.annotation.LogAnnotation;
import com.topwulian.config.EtContextConfig;
import com.topwulian.dao.DeviceGatherDao;
import com.topwulian.dao.DeviceLogDao;
import com.topwulian.dao.SysLogsDao;
import com.topwulian.model.*;
import com.topwulian.service.MailService;
import com.topwulian.service.UserService;
import com.topwulian.utils.MessageUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * activemq消息接收,监听开发快传递过来的传感器数据,类型为byte[]
 */
@Service
public class MessageConsumerService {

    //@Autowired
    //private DeviceGatherDao deviceGatherDao;

    public static void main(String[] args) {
        MessageConsumerService service=new MessageConsumerService();
        String s = "5a5ab02a041002001a04110203d00217020054021802023603190200b0012002004f051204000021b299";
        System.out.println("【*** 接收消息,转换为字符串: ***】" + s);
        if("7b55".equals(s)){
            SysLogs sysLogs = new SysLogs();
            sysLogs.setRemark("接收到心跳消息:"+s);
            sysLogs.setFlag(true);
            System.out.println("接收到心跳消息:"+s);
        }else {
            String[] strings = MessageUtil.handleStr2ArrStr(s);
            Integer[] integers = MessageUtil.handleStr2ArrInt(strings);

            boolean isValid = service.checkDataPacketIsValid(integers);


            Date date = new Date();


            //空气温度传感器
            DeviceGather deviceGather = new DeviceGather();
            deviceGather.setGatherTime(date);
            deviceGather.setCreateTime(date);
            deviceGather.setUpdateTime(date);


            strings[7]="ff";
            //判断空气温度是否为负数
            Integer basicData = 0;
            if (strings[7].startsWith("f")) {
                basicData = ((Integer.valueOf(strings[7] + strings[8], 16).shortValue()) / 256 - 1);
            } else {
                basicData = Integer.valueOf(strings[7] + strings[8], 16);
            }

            deviceGather.setBasicData(basicData.floatValue() / 10);

        }

    }

    //从redis中获取数据的阈值,阈值放在数据库中,后台可以进行维护,维护完毕后同步数据库和redis
    //在批量导入的同时对阈值进行比较,有问题直接发短信,发邮件
    //邮件短信的报警联系人也可以在后台进行设置
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private DeviceLogDao deviceLogDao;

    //批量插入
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private MailService mailService;

    @Autowired
    private SysLogsDao sysLogsDao;



    @Transactional(rollbackFor = Exception.class)
    @JmsListener(destination = "${jzwl.mq.queue}")
    public void receiveMessage(byte[] payload) {    // 进行消息接收处理
        //System.out.println("【*** 接收消息 ***】" + payload);
        String s=EtContextConfig.toChangeByHex(payload);
        //String s = "5a5ab02a041002001a04110203d00217020054021802023603190200b0012002004f051204000021b299";
        System.out.println("【*** 接收消息,转换为字符串: ***】" + s);
        if("7b55".equals(s)){
            SysLogs sysLogs = new SysLogs();
            sysLogs.setRemark("接收到心跳消息:"+s);
            sysLogs.setFlag(true);
            sysLogsDao.save(sysLogs);
            System.out.println("接收到心跳消息:"+s);
        }else {
            String[] strings = MessageUtil.handleStr2ArrStr(s);
            Integer[] integers = MessageUtil.handleStr2ArrInt(strings);

            boolean isValid = checkDataPacketIsValid(integers);

            //从redis中将设备的阈值取出来,此处redis可能为空
            Map deviceThreshold = redisTemplate.opsForHash().entries("deviceThreshold");

            Date date = new Date();

            SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
            DeviceGatherDao deviceGatherDao = sqlSession.getMapper(DeviceGatherDao.class);

            //空气温度传感器
            DeviceGather deviceGather = new DeviceGather();
            deviceGather.setGatherTime(date);
            deviceGather.setCreateTime(date);
            deviceGather.setUpdateTime(date);

            //判断空气温度是否为负数
            Integer basicData = 0;
            if (strings[7].startsWith("f")) {
                basicData = ((Integer.valueOf(strings[7] + strings[8], 16).shortValue()) / 256 - 1);
            } else {
                basicData = Integer.valueOf(strings[7] + strings[8], 16);
            }

            deviceGather.setBasicData(basicData.floatValue() / 10);

            //获取当前设备的阈值,并进行阈值比较
            Device d1 = (Device) deviceThreshold.get(integers[5] + "");
            compareThreshold(jmsMessagingTemplate, d1, deviceGather.getBasicData());

            deviceGather.setDeviceId(integers[5]);
            deviceGather.setDeviceName("空气温度传感器");
            deviceGather.setDeviceSn(integers[5] + "");
            deviceGather.setDeviceType(integers[5] + "");
            deviceGather.setMeasurementUnitId(1);
            deviceGather.setMeasurementUnitName("°C");
            deviceGather.setMeasureUnitType("空气温度");
            deviceGatherDao.save(deviceGather);

            //空气湿度传感器
            basicData = Integer.valueOf(strings[12] + strings[13], 16);
            deviceGather.setBasicData(basicData.floatValue() / 10);

            //获取当前设备的阈值,并进行阈值比较
            Device d2 = (Device) deviceThreshold.get(integers[10] + "");
            compareThreshold(jmsMessagingTemplate, d2, deviceGather.getBasicData());

            deviceGather.setDeviceId(integers[10]);
            deviceGather.setDeviceName("空气湿度传感器");
            deviceGather.setDeviceSn(integers[10] + "");
            deviceGather.setDeviceType(integers[10] + "");
            deviceGather.setMeasurementUnitId(2);
            deviceGather.setMeasurementUnitName("%");
            deviceGather.setMeasureUnitType("空气湿度");
            deviceGatherDao.save(deviceGather);

            //土壤温度传感器
            if (strings[17].startsWith("f")) {
                basicData = ((Integer.valueOf(strings[17] + strings[18], 16).shortValue()) / 256 - 1);
            } else {
                basicData = Integer.valueOf(strings[17] + strings[18], 16);
            }

            deviceGather.setBasicData(basicData.floatValue() / 10);

            //获取当前设备的阈值,并进行阈值比较
            Device d3 = (Device) deviceThreshold.get(integers[15] + "");
            compareThreshold(jmsMessagingTemplate, d3, deviceGather.getBasicData());

            deviceGather.setDeviceId(integers[15]);
            deviceGather.setDeviceName("土壤温度传感器");
            deviceGather.setDeviceSn(integers[15] + "");
            deviceGather.setDeviceType(integers[15] + "");
            deviceGather.setMeasurementUnitId(3);
            deviceGather.setMeasurementUnitName("°C");
            deviceGather.setMeasureUnitType("土壤温度");
            deviceGatherDao.save(deviceGather);

            //土壤湿度传感器
            if (strings[22].startsWith("f")) {
                basicData = ((Integer.valueOf(strings[22] + strings[23], 16).shortValue()) / 256 - 1);
            } else {
                basicData = Integer.valueOf(strings[22] + strings[23], 16);
            }

            deviceGather.setBasicData(basicData.floatValue() / 10);

            //获取当前设备的阈值,并进行阈值比较
            Device d4 = (Device) deviceThreshold.get(integers[20] + "");
            compareThreshold(jmsMessagingTemplate, d4, deviceGather.getBasicData());

            deviceGather.setDeviceId(integers[20]);
            deviceGather.setDeviceName("土壤湿度传感器");
            deviceGather.setDeviceSn(integers[20] + "");
            deviceGather.setDeviceType(integers[20] + "");
            deviceGather.setMeasurementUnitId(4);
            deviceGather.setMeasurementUnitName("%");
            deviceGather.setMeasureUnitType("土壤湿度");
            deviceGatherDao.save(deviceGather);

            //电导率传感器
            if (strings[27].startsWith("f")) {
                basicData = ((Integer.valueOf(strings[27] + strings[28], 16).shortValue()) / 256 - 1);
            } else {
                basicData = Integer.valueOf(strings[27] + strings[28], 16);
            }

            deviceGather.setBasicData(basicData.floatValue());

            //获取当前设备的阈值,并进行阈值比较
            Device d5 = (Device) deviceThreshold.get(integers[25] + "");
            compareThreshold(jmsMessagingTemplate, d5, deviceGather.getBasicData());

            deviceGather.setDeviceId(integers[25]);
            deviceGather.setDeviceName("电导率传感器");
            deviceGather.setDeviceSn(integers[25] + "");
            deviceGather.setDeviceType(integers[25] + "");
            deviceGather.setMeasurementUnitId(5);
            deviceGather.setMeasurementUnitName("EC/cm");
            deviceGather.setMeasureUnitType("电导率");
            deviceGatherDao.save(deviceGather);

            //ph值传感器

            basicData = Integer.valueOf(strings[32] + strings[33], 16);
            deviceGather.setBasicData(basicData.floatValue() / 10);

            //获取当前设备的阈值,并进行阈值比较
            Device d6 = (Device) deviceThreshold.get(integers[30] + "");
            compareThreshold(jmsMessagingTemplate, d6, deviceGather.getBasicData());

            deviceGather.setDeviceId(integers[30]);
            deviceGather.setDeviceName("PH值传感器");
            deviceGather.setDeviceSn(integers[30] + "");
            deviceGather.setDeviceType(integers[30] + "");
            deviceGather.setMeasurementUnitId(6);
            deviceGather.setMeasurementUnitName("PH");
            deviceGather.setMeasureUnitType("PH值");
            deviceGatherDao.save(deviceGather);


            //光照传感器

            basicData = Integer.valueOf(strings[37] + strings[38] + strings[39] + strings[40], 16);
            deviceGather.setBasicData(basicData.floatValue());

            //获取当前设备的阈值,并进行阈值比较
            Device d7 = (Device) deviceThreshold.get(integers[35] + "");
            compareThreshold(jmsMessagingTemplate, d7, deviceGather.getBasicData());

            deviceGather.setDeviceId(integers[35]);
            deviceGather.setDeviceName("光照传感器");
            deviceGather.setDeviceSn(integers[35] + "");
            deviceGather.setDeviceType(integers[35] + "");
            deviceGather.setMeasurementUnitId(7);
            deviceGather.setMeasurementUnitName("Lux");
            deviceGather.setMeasureUnitType("光照");
            deviceGatherDao.save(deviceGather);

            sqlSession.flushStatements();
        }
    }



    /**
     *
     * @param device 从redis中取出来的设备对象,包含阈值范围 10-100
     * @param basicData 要校验的采集数据,如果数据超出阈值范围就发消息报警
     */
    private void compareThreshold(JmsMessagingTemplate jmsMessagingTemplate,Device device, Float basicData) {
        if (basicData>device.getThresholdMax()||basicData<device.getThresholdMin()) {
            Map msgMap=new HashMap();
            msgMap.put(basicData+"",JSON.toJSONString(device));
            jmsMessagingTemplate.convertAndSend("device_threshold_alerm", msgMap);
        }
    }

    @JmsListener(destination = "device_threshold_alerm")
    public void deviceThresholdAlerm(Map<String,String> deviceMap){
        Set<String> keySet = deviceMap.keySet();
        for (String basicData : keySet) {
            Device device = JSON.parseObject(deviceMap.get(basicData),Device.class);
            String msg = device.getLocation() + device.getName() + "当前值为:[" + basicData + "],不在设备阈值范围:[" + device.getThresholdMin() + "-" + device.getThresholdMax() + "],特此报警";
            //将告警日志保存到数据库,并且发短信或邮件
            DeviceLog deviceLog=new DeviceLog();
            deviceLog.setContent(msg);
            deviceLog.setDeviceId(device.getId().intValue());
            deviceLog.setDeviceName(device.getName());
            deviceLog.setLevel("5");
            deviceLog.setFarmId(device.getFarmId());
            deviceLog.setUserId(device.getUserId());

            deviceLogDao.save(deviceLog);
            List<String> toUser=new ArrayList<>();
            toUser.add("17673125001@163.com");
            Mail mail=new Mail();
            mail.setUserId(device.getUserId().longValue());
            mail.setContent(msg);
            mail.setSubject(device.getLocation()+device.getName()+"设备异常,告警!!!");
            mail.setToUsers(JSON.toJSONString(toUser));


            //mailService.save(mail,toUser);
        }
    }

    /**
     * 对数据包进行校验
     *
     * @param integers
     * @return
     */
    @LogAnnotation
    private boolean checkDataPacketIsValid(Integer[] integers) {
        boolean flag = true;
        if (integers.length < 1) {
            flag = false;
            throw new RuntimeException("数据包长度不足");
        }

        if (integers[0] != 0x5a && integers[1] != 0x5a) {
            flag = false;
            throw new RuntimeException("数据包头格式不正确");
        }

        Integer dataLength = integers[3];//数据包除了校验位的长度
        if (integers.length != dataLength) {
            flag = false;
            throw new RuntimeException("数据包长度校验未通过");
        }
        //获取所有除了校验位所有字节相加取低字节的值
        Integer dataSum = 0;
        for (int i = 0; i < integers.length - 1; i++) {
            dataSum += integers[i];
        }
        if (integers[dataLength - 1] != (dataSum & 0xFF)) {
            flag = false;
            throw new RuntimeException("数据包内容校验未通过");
        }
        return flag;
    }
}