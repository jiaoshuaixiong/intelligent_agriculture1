package com.topwulian.dto;

import com.topwulian.config.EtContextConfig;
import com.topwulian.model.Humidity;
import com.topwulian.model.Pm25;
import com.topwulian.model.Pressure;
import com.topwulian.model.Temperature;
import com.topwulian.utils.MessageUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * 机器上报-消息包实体类
 * @Author: szz
 * @Date: 2018/10/20 上午11:10
 * @Version 1.0
 */

public class MessagePacket implements Serializable {

    public static void main(String[] args) {

    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    //字符串转字节数组
    public static byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private String packetHead;//包头  0x5a0x5a
    private String packetTail;//包尾  校验和（前面数据累加和，仅留低8 位）
    private String packetLen;//包长度
    //数据类型和数据为key:value,可能会有多个,准备封装到map中
    //温度4字节  湿度1字节  光照强度字节  气压字节
    private String dataType;//数据类型 0x15:光照强度 0x45:温度,气压,湿度,海拔 0x55:IIC地址
    private String delimiter;//数据分隔符,暂时没用
    private String dataEnd;//数据结束标识符,暂时没用
    private Date timeStamp;//数据上报的时间,通常为当前时间,如果页面显示间隔比较短,只显示 MM:ss
    /**
     * 0x10 返回机器所有传感器当前数值,设备端返回的报文包含当前返回设备连接的所有传感器的当前数值
     * 0x11 返回机器当前状态,返回设备端返回的报文包含机器运行状态，设备外设连接状态等
     */
    private String command;//机器上报命令编码
    /**
     * asc对照
     * 43:+  0x2b
     * 45:-  0x2d
     * 46:.  0x2e
     */
    private String md5;//md5校验

    //判断数据是否有效,要以0x7b开头,以0xa0结尾,包长度比对,最后会换成md5校验
    public boolean isValid(){
        if (StringUtils.isNotEmpty(packetHead) && StringUtils.isNotEmpty(packetTail)) {
            if ("7b".equals(packetHead) && "0a".equals(packetTail)) {
                return true;
            }
        }
        return false;
    }

    //对包体传感器数据进行处理
    //数据类型 0x01:温度传感器 0x02:湿度 0x03:光照 0x04:气压...

    //将有效数据的数组传入到此方法中进行处理,先判断包头,拆分,分别封装
    public String handelData(String[] dataStr){
        String firstDataType = dataStr[0];


        if ("01".equals(firstDataType)) {

        }else if ("02".equals(firstDataType)) {

        }else if ("03".equals(firstDataType)) {

        }else if ("04".equals(firstDataType)) {

        } else {

        }
        return null;
    }


    public String getPacketHead() {
        return packetHead;
    }

    public void setPacketHead(String packetHead) {
        this.packetHead = packetHead;
    }

    public String getPacketTail() {
        return packetTail;
    }

    public void setPacketTail(String packetTail) {
        this.packetTail = packetTail;
    }

    public String getPacketLen() {
        return packetLen;
    }

    public void setPacketLen(String packetLen) {
        this.packetLen = packetLen;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDataEnd() {
        return dataEnd;
    }

    public void setDataEnd(String dataEnd) {
        this.dataEnd = dataEnd;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }


    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
