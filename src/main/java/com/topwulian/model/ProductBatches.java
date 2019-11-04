package com.topwulian.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductBatches extends BaseEntity<Long> {

	private String productName;
	private String isactive;
	private String productBatch;
	private Integer area;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date recoverydate;
	private String img;
	private String feature;
	private Integer sysUserId;
	private List<TTask> tasks = new ArrayList<>();
	private Integer framId;


	public Integer getFramId() {
		return framId;
	}

	public void setFramId(Integer framId) {
		this.framId = framId;
	}

	public List<TTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<TTask> tasks) {
		this.tasks = tasks;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductBatch() {
		return productBatch;
	}

	public void setProductBatch(String productBatch) {
		this.productBatch = productBatch;
	}

	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	public Integer getArea() {
		return area;
	}
	public void setArea(Integer area) {
		this.area = area;
	}

	public Date getRecoverydate() {
		return recoverydate;
	}

	public void setRecoverydate(Date recoverydate) {
		this.recoverydate = recoverydate;
	}

	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public Integer getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

}
