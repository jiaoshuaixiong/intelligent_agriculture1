package com.topwulian.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Temperature extends BaseEntity<Long> {

	private Double t;
	private Date ddatetime;
	private String obtid;//测量站id
	private String timeStr;//因前端需要显示报表,为防止报表的时间值太长,特使用此字段处理简短化的日期格式,默认为小时

	public String getTimeStr() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		timeStr = simpleDateFormat.format(ddatetime);
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}

	public Double getT() {
		return t;
	}
	public void setT(Double t) {
		this.t = t;
	}
	public Date getDdatetime() {
		return ddatetime;
	}
	public void setDdatetime(Date ddatetime) {
		this.ddatetime = ddatetime;
	}
	public String getObtid() {
		return obtid;
	}
	public void setObtid(String obtid) {
		this.obtid = obtid;
	}

}
