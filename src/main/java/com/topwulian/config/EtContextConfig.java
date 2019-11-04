package com.topwulian.config;

import com.beidouapp.et.client.EtFactory;
import com.beidouapp.et.client.api.IContext;
import com.beidouapp.et.client.api.ISDK;
import com.beidouapp.et.client.callback.ICallback;
import com.beidouapp.et.client.callback.IReceiveListener;
import com.beidouapp.et.client.callback.IServerTimeListener;
import com.beidouapp.et.client.domain.EtServerInfo;
import com.beidouapp.et.exception.EtExceptionCode;
import com.topwulian.dao.*;
import com.topwulian.model.Humidity;
import com.topwulian.model.Pm25;
import com.topwulian.model.Pressure;
import com.topwulian.model.Temperature;
import com.topwulian.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.core.JmsMessagingTemplate;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: szz
 * @Date: 2018/10/11 上午10:26
 * @Version 1.0
 */
@Configuration
public class EtContextConfig {
    /*
	private static final String APP_KEY = "iLink 云平台应用标识码";
    private static final String SECURE_KEY = "iLink 应用安全识别码";
    private static final String UID ="iLink 平台标识码（系统唯一，相当于账号） ";
    public static final String DOMAIN = "iLink 平台负载均衡服务器域名";
    public static final int PORT = iLink 平台负载均衡服务器端口;
    */
    public static final String CHARACTER_UTF8 = "UTF-8";
    public static final int PORT = 8085;
    public static final short DEFAULT_KEEP_ALIVE = 60;
    public static final int TIMEOUT = 5000;
    public static String DOMAIN = "127.0.0.1";
    public static String APP_KEY = "xxx";

    public static String SECRET_KEY = "xxx";


    //我自己申请的uid
    public static String UID = "xxx";
    public static String RECEIVE_UID = "xxx";

    public static final String MSG_RESPONSE_CODE = "ret";
    public static final String MSG_RESPONSE_CONTENT = "message";

    public static final String MSG_CONTENT = "msg content";

    private static final String[] HEX_TABLE = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0a", "0b", "0c", "0d", "0e", "0f", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "1a", "1b", "1c", "1d", "1e", "1f", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "2a", "2b", "2c", "2d", "2e", "2f", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3a", "3b", "3c", "3d", "3e", "3f", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4a", "4b", "4c", "4d", "4e", "4f", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5a", "5b", "5c", "5d", "5e", "5f", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6a", "6b", "6c", "6d", "6e", "6f", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7a", "7b", "7c", "7d", "7e", "7f", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8a", "8b", "8c", "8d", "8e", "8f", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9a", "9b", "9c", "9d", "9e", "9f", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "aa", "ab", "ac", "ad", "ae", "af", "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8", "b9", "ba", "bb", "bc", "bd", "be", "bf", "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "ca", "cb", "cc", "cd", "ce", "cf", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "da", "db", "dc", "dd", "de", "df", "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9", "ea", "eb", "ec", "ed", "ee", "ef", "f0", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "fa", "fb", "fc", "fd", "fe", "ff"};

    private static EtServerInfo serverInfo = null;

    /**
     * 读取配置文件传参
     */
    @Value("${jzwl.kfk.domain}")
    public void setDOMAIN(String domain) {
        DOMAIN = domain;
    }
    @Value("${jzwl.kfk.app_key}")
    public void setAPP_KEY(String app_key) {
        APP_KEY = app_key;
    }
    @Value("${jzwl.kfk.secret_key}")
    public void setSECRET_KEY(String secret_key) {
        SECRET_KEY = secret_key;
    }
    @Value("${jzwl.kfk.uid}")
    public void setUID(String uid) {
        UID = uid;
    }

    @Value("${jzwl.kfk.receive_uid}")
    public void setRECEIVE_UID(String receive_uid) {
        RECEIVE_UID = receive_uid;
    }

    public static void sleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void blockIn() {
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得十六进制数据.
     *
     * @param originalData
     * @return
     */
    public static String toChangeByHex(byte[] originalData) {
        byte[] data = originalData;
        StringBuilder rc = new StringBuilder(originalData.length * 2);
        for (int i = 0; i < originalData.length; ++i) {
            rc.append(HEX_TABLE[255 & data[i]]);
        }
        return rc.toString();
    }

    public static void main(String[] args) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        BASE64Decoder decode = new BASE64Decoder();
        String a = "a";
        String encode = base64Encoder.encode(a.getBytes());
        try {
            byte[] bytes = decode.decodeBuffer(encode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hex = toChangeByHex("我是一只猪".getBytes());
        System.out.println(hex);
    }

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private TemperatureDao temperatureDao;
    @Autowired
    private HumidityDao humidityDao;
    @Autowired
    private PressureDao pressureDao;

    @Autowired
    private Pm25Dao pm25Dao;

    @Value("${jzwl.mq.queue}")
    private String QUEUE_GATHER;

    @Bean
    public ISDK iSDK() throws Exception {
        EtFactory f = new EtFactory();
        IContext ctx = f.createContext();
        ctx.setAppKey(APP_KEY).setSecretKey(SECRET_KEY).setUserName(UID).setServerDomain(DOMAIN).setServerPort(PORT);
        final ISDK sdk = f.createSDK(ctx);
       /* sdk.setCallback(new IReceiveListener() {

            //通过手动重连方法来实现断网自动重连功能
            @Override
            public void connectionLost(int reasonCode, Throwable cause) {
                System.err.println("~~~~~~~~~~~~~reasonCode=" + reasonCode);
                //如果是其他客户端把当前客户端顶下线了,那么直接返回,reasonCode=10601
                if (EtExceptionCode.SYS_KICK == reasonCode) {
                    return;
                }

                //无网,掉线5秒后悔自动重连
                sleep(10000);
                sdk.reConnect();
            }

            @Override
            public void onMessage(String topic, byte[] payload) {
                try {
                    System.out.println("~~~~~~~~~~~~~topic=" + topic + ". payload=" + new String(payload,"UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // 此处编写您的业务代码，注意不要在此处出现耗时操作，如果消息量大时，会占用服务器内存且出现阻塞超时等问题。
                //7b3007160000000000022fbf58d9
                //saveData(payload);
                //此处采集到的数据，直接发送到消息队列中，由消息队列处理存入数据库的操作
                jmsMessagingTemplate.convertAndSend(QUEUE_GATHER, payload);
            }
        });

        sdk.setServerTimeListener(new IServerTimeListener() {
            @Override
            public void getServerTime(long time) {
                System.out.println("~~~~~~~~~~~~~time=" + time);
            }
        });

        final CountDownLatch cdl = new CountDownLatch(1);
        sdk.discoverServers(TIMEOUT, new ICallback<EtServerInfo>() {
            @Override
            public void onSuccess(EtServerInfo etServerInfo) {
                System.err.println(String.format("onSuccess return EtServerInfo = %s", etServerInfo.toString()));
                serverInfo = etServerInfo;
                cdl.countDown();
            }

            @Override
            public void onFailure(Throwable value) {
                System.err.println(String.format("discoverServers() is failed, occur = %s", value.getLocalizedMessage()));
                cdl.countDown();
            }
        });
        cdl.await();

        final CountDownLatch cdl2 = new CountDownLatch(1);
        sdk.connect(serverInfo, (short) 15, true, 8, new ICallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println(Thread.currentThread().getName() + "~~~~~~~~~connect()成功");
                cdl2.countDown();
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println(Thread.currentThread().getName() + ex);
            }
        });
        cdl2.await();

        //用户登录成功后.可根据自己的业务调此方法,调用后,服务器会将当前用户收到的离线消息推送过来。
        sdk.requestOfflineMessage(new ICallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println(String.format("~~~~~~~~~requestOfflineMessage()成功threadName=%s",Thread.currentThread().getName()));
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });*/


        /*//消息发送
        String msg = MSG_CONTENT;
        String msg1 = MSG_CONTENT;
        msg = toChangeByHex(msg.getBytes());

        final CountDownLatch cdl3 = new CountDownLatch(1);
        sdk.chatTo(RECEIVE_UID, msg.getBytes(), new ICallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("~~~~~~~~~chatTo(" + RECEIVE_UID + ")成功");
                cdl3.countDown();
            }

            @Override
            public void onFailure(Throwable ex) {
                System.err.println("*********chatTo(" + RECEIVE_UID + ")失败. " + ex.getLocalizedMessage());
                cdl3.countDown();
            }
        });
        cdl3.await();*/
        return sdk;
    }

    private void saveData(byte[] payload) {
        //7b3007160000000000022fbf58d9
        //将byte数组变成字符串
        String s = EtContextConfig.toChangeByHex(payload);

        //将字符串转换成数字
        Integer[] integers = MessageUtil.handleStr2ArrInt(MessageUtil.handleStr2ArrStr(s));

        //这是根据双方约定，这一个数字代表要实现的功能，0x45就是温度，气压，湿度，海拔数据上报
        if (integers[2] == 0x45) {
            Date date = new Date();
            //温度
            double t=((integers[4]<<8)|integers[5])/100;
            System.out.println("温度为:"+t);
            Temperature temperature=new Temperature();
            temperature.setDdatetime(date);
            temperature.setT(t);
            temperature.setObtid("11111");
            temperature.setCreateTime(date);
            temperature.setUpdateTime(date);
            temperatureDao.save(temperature);
            //气压
            double p=((integers[6]<<24)|(integers[7]<<16)|(integers[8]<<8)|integers[9])/10000;
            System.out.println("气压为:"+p);
            Pressure pressure=new Pressure();
            pressure.setDdatetime(date);
            pressure.setP(new Double(p).intValue());
            pressure.setObtid("11111");
            pressure.setCreateTime(date);
            pressure.setUpdateTime(date);
            pressureDao.save(pressure);
            //湿度
            int h=((integers[10]<<8)|integers[11])/100;
            System.out.println("湿度为:"+h);
            Humidity humidity=new Humidity();
            humidity.setDdatetime(date);
            humidity.setH(h);
            humidity.setObtid("11111");
            humidity.setCreateTime(date);
            humidity.setUpdateTime(date);
            humidityDao.save(humidity);
            //海拔
            int hign=(integers[12]<<8)|integers[13];
            System.out.println("海拔为:"+hign);

        }
        if (integers[2]==0x15) {
            Date date = new Date();
            //光照
            Integer lux=((integers[4]<<24)|(integers[5]<<16)|(integers[6]<<8)|integers[7])/100;
            System.out.println("光照为:"+lux);
            Pm25 pm25=new Pm25();
            pm25.setDdatetime(date);
            pm25.setObtid("11111");
            pm25.setPm(lux);
            pm25.setCreateTime(date);
            pm25.setUpdateTime(date);
            pm25Dao.save(pm25);
        }

        if (integers[2] == 0x55) {
            int iix_add = integers[4];
        }
    }
}