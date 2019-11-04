package com.topwulian.model;



public class WxPlatform extends BaseEntity<Long> {

	private String ID;
	private Integer FARMID;
	private String ISACTIVE;
	private String APPID;
	private String APPSECRET;

	public String getID() {
		return ID;
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	public Integer getFARMID() {
		return FARMID;
	}
	public void setFARMID(Integer FARMID) {
		this.FARMID = FARMID;
	}
	public String getISACTIVE() {
		return ISACTIVE;
	}
	public void setISACTIVE(String ISACTIVE) {
		this.ISACTIVE = ISACTIVE;
	}
	public String getAPPID() {
		return APPID;
	}
	public void setAPPID(String APPID) {
		this.APPID = APPID;
	}
	public String getAPPSECRET() {
		return APPSECRET;
	}
	public void setAPPSECRET(String APPSECRET) {
		this.APPSECRET = APPSECRET;
	}

}
