package com.topwulian.model;

import java.util.Date;

/**
 * 生产人员表
 */
public class TProducter extends BaseEntity<Long> {

	private String isactive;
	private String username;
	private String phone;
	private String wechat;
	private String usercard;
	private Date createdate;
	private Integer sysUserId;
	private Integer producerTypeId;
	private String password;
	private Integer farmId;
	private String openid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWechat() {
		return wechat;
	}
	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	public String getUsercard() {
		return usercard;
	}
	public void setUsercard(String usercard) {
		this.usercard = usercard;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Integer getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}
	public Integer getProducerTypeId() {
		return producerTypeId;
	}
	public void setProducerTypeId(Integer producerTypeId) {
		this.producerTypeId = producerTypeId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "TProducter{" +
				"isactive='" + isactive + '\'' +
				", username='" + username + '\'' +
				", phone='" + phone + '\'' +
				", wechat='" + wechat + '\'' +
				", usercard='" + usercard + '\'' +
				", createdate=" + createdate +
				", sysUserId=" + sysUserId +
				", producerTypeId=" + producerTypeId +
				", password='" + password + '\'' +
				", farmId=" + farmId +
				", openid='" + openid + '\'' +
				'}';
	}
}
