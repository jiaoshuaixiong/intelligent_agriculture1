package com.topwulian.model;

import java.io.Serializable;

public class RespEntiry implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String message;
	private Object data;
	private String redirectUri;
	private Integer count;
	
	public static RespEntiry fail(){
		RespEntiry formResp = new RespEntiry();
		formResp.setEcode(RespCode.FAIL);
		return formResp;
	}
	
	public static RespEntiry fail(String massage){
		RespEntiry formResp = new RespEntiry();
		formResp.setEcode(RespCode.FAIL);
		formResp.setMessage(massage);
		return formResp;
	}
	
	public static RespEntiry fail(String code,String massage){
		RespEntiry formResp = new RespEntiry();
		formResp.setCode(code);
		formResp.setMessage(massage);
		return formResp;
	}
	
	public static RespEntiry fail(String code,String massage,Object data){
		RespEntiry formResp = new RespEntiry();
		formResp.setCode(code);
		formResp.setMessage(massage);
		formResp.setData(data);
		return formResp;
	}
	
	public static RespEntiry fail(RespCode respCode){
		RespEntiry formResp = new RespEntiry();
		formResp.setCode(respCode.getCode());
		formResp.setMessage(respCode.getMessage());
		return formResp;
	}
	
	public static RespEntiry fail(RespCode respCode,Object data){
		RespEntiry formResp = new RespEntiry();
		formResp.setCode(respCode.getCode());
		formResp.setMessage(respCode.getMessage());
		formResp.setData(data);
		return formResp;
	}
	
	public static RespEntiry success(){
		RespEntiry formResp = new RespEntiry();
		formResp.setEcode(RespCode.SUCCESS);
		return formResp;
	}
	
	public static RespEntiry success(String massage){
		RespEntiry formResp = new RespEntiry();
		formResp.setEcode(RespCode.SUCCESS);
		formResp.setMessage(massage);
		return formResp;
	}
	
	public static RespEntiry success(String massage, Object data){
		RespEntiry formResp = new RespEntiry();
		formResp.setEcode(RespCode.SUCCESS);
		formResp.setData(data);
		formResp.setMessage(massage);
		return formResp;
	}
	
	public static RespEntiry success(Object data){
		RespEntiry formResp = new RespEntiry();
		formResp.setEcode(RespCode.SUCCESS);
		formResp.setData(data);
		return formResp;
	}
	
	public static RespEntiry success(Object data, Integer count){
		RespEntiry formResp = new RespEntiry();
		formResp.setEcode(RespCode.SUCCESS);
		formResp.setData(data);
		formResp.setCount(count);
		return formResp;
	}

	
	public String getRedirectUri() {
		return redirectUri;
	}

	public RespEntiry setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
		return this;
	}

	public RespEntiry setEcode(RespCode formCode) {
		this.code = formCode.getCode();
		this.message = formCode.getMessage();
		return this;
	}
	
	public String getCode() {
		return code;
	}
	public RespEntiry setCode(String code) {
		this.code = code;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public RespEntiry setMessage(String message) {
		this.message = message;
		return this;
	}
	public Object getData() {
		return data;
	}
	public RespEntiry setData(Object data) {
		this.data = data;
		return this;
	}

	public Integer getCount() {
		return count;
	}

	public RespEntiry setCount(Integer count) {
		this.count = count;
		return this;
	}
}
