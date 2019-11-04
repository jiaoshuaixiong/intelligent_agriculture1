package com.topwulian.model;

public class VedioImages extends BaseEntity<Long> {

	private Integer vedioId;
	private String path;
	private String url;
	private String deviceSerial;
	private Integer farmId;

	public Integer getVedioId() {
		return vedioId;
	}
	public void setVedioId(Integer vedioId) {
		this.vedioId = vedioId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDeviceSerial() {
		return deviceSerial;
	}
	public void setDeviceSerial(String deviceSerial) {
		this.deviceSerial = deviceSerial;
	}
	public Integer getFarmId() {
		return farmId;
	}
	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

}
