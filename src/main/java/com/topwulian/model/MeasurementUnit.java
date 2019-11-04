package com.topwulian.model;

import java.util.Date;

public class MeasurementUnit extends BaseEntity<Long> {

	private String unitTypes;
	private String cnName;
	private String enName;
	private String operator;

	public String getUnitTypes() {
		return unitTypes;
	}
	public void setUnitTypes(String unitTypes) {
		this.unitTypes = unitTypes;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

}
