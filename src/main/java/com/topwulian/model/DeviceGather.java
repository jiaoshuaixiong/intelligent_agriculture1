package com.topwulian.model;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;

public class DeviceGather extends BaseEntity<Long> {

    @Excel(name = "设备id")
	private Integer deviceId;
    @Excel(name = "设备序列号",width = 20)
	private String deviceSn;
    @Excel(name = "设备名称",width = 30)
	private String deviceName;
	private String deviceType;
    @Excel(name = "数据")
	private Float basicData;
	private Integer measurementUnitId;
    @Excel(name = "数据单位")
	private String measurementUnitName;
	private String measureUnitType;
    @Excel(name = "采集时间",format = "yyyy-MM-dd HH:mm:ss", width = 35)
	private Date gatherTime;

	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Float getBasicData() {
		return basicData;
	}
	public void setBasicData(Float basicData) {
		this.basicData = basicData;
	}
	public Integer getMeasurementUnitId() {
		return measurementUnitId;
	}
	public void setMeasurementUnitId(Integer measurementUnitId) {
		this.measurementUnitId = measurementUnitId;
	}
	public String getMeasurementUnitName() {
		return measurementUnitName;
	}
	public void setMeasurementUnitName(String measurementUnitName) {
		this.measurementUnitName = measurementUnitName;
	}
	public String getMeasureUnitType() {
		return measureUnitType;
	}
	public void setMeasureUnitType(String measureUnitType) {
		this.measureUnitType = measureUnitType;
	}
	public Date getGatherTime() {
		return gatherTime;
	}
	public void setGatherTime(Date gatherTime) {
		this.gatherTime = gatherTime;
	}

}
