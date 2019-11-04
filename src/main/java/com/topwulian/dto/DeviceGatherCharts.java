package com.topwulian.dto;

/**
 * @Author: szz
 * @Date: 2019/1/13 上午12:28
 * @Version 1.0
 * 每个设备最近一段时间的统计数据
 */
public class DeviceGatherCharts {

    private Long deviceId;
    private String HOUR;
    private String DAY;
    private Float avg;
    private String MONTH;
    private String YEAR;

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getHOUR() {
        return HOUR;
    }

    public void setHOUR(String HOUR) {
        this.HOUR = HOUR;
    }

    public String getDAY() {
        return DAY;
    }

    public void setDAY(String DAY) {
        this.DAY = DAY;
    }

    public Float getAvg() {
        return avg;
    }

    public void setAvg(Float avg) {
        this.avg = avg;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getYEAR() {
        return YEAR;
    }

    public void setYEAR(String YEAR) {
        this.YEAR = YEAR;
    }
}
