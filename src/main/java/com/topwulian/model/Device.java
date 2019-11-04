package com.topwulian.model;

import java.util.Date;

public class Device extends BaseEntity<Long> {

	private String sn;
	private String name;
	private Integer typeId;
	private String location;
	private String image;
	private Date installTime;
	private String state;
	private String standard;
	private String manufacturer;
	private Integer userId;
	private Integer farmId;
	private String remark;
	private String softwareVersion;
	private Float thresholdMin;
	private Float thresholdMax;

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }

    public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getInstallTime() {
		return installTime;
	}
	public void setInstallTime(Date installTime) {
		this.installTime = installTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public Float getThresholdMin() {
		return thresholdMin;
	}
	public void setThresholdMin(Float thresholdMin) {
		this.thresholdMin = thresholdMin;
	}
	public Float getThresholdMax() {
		return thresholdMax;
	}
	public void setThresholdMax(Float thresholdMax) {
		this.thresholdMax = thresholdMax;
	}

}
