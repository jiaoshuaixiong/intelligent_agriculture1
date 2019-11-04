package com.topwulian.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FileInfo implements Serializable {

	private static final long serialVersionUID = -1438078028040922174L;

	private String id;
	/** 原始文件名 */
	private String name;
	private Boolean isImg;
	private String contentType;
	private long size;
	private String path;
	private String url;
	/**
	 * 文件来源
	 * 
	 * @see FileSource
	 */
	private String source;
	private Date createTime;
}
