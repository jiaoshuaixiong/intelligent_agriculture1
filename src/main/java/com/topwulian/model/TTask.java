package com.topwulian.model;


import java.util.Date;

public class TTask extends BaseEntity<Long> {

	private Integer productBatchesId;
	private String content;
	/**
	 * 1.下发成功。2.待审核。3.完成。4.异常。
	 */
	private Integer state;
	private String img1;
	private String img2;
	private String img3;
	private String img4;
	private Integer producterId;
	private Integer sysUserId;
	private ProductBatches productBatches;
	private Integer farmId;
	private Date activityDate;
	private String area;
	private String descContent;
	private Date finishDate;
	private Date createDate;


	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescContent() {
		return descContent;
	}

	public void setDescContent(String descContent) {
		this.descContent = descContent;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Date getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	public Integer getFarmId() {
		return farmId;
	}

	public void setFarmId(Integer farmId) {
		this.farmId = farmId;
	}

	public ProductBatches getProductBatches() {
		return productBatches;
	}

	public void setProductBatches(ProductBatches productBatches) {
		this.productBatches = productBatches;
	}

	public Integer getProductBatchesId() {
		return productBatchesId;
	}
	public void setProductBatchesId(Integer productBatchesId) {
		this.productBatchesId = productBatchesId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getImg1() {
		return img1;
	}
	public void setImg1(String img1) {
		this.img1 = img1;
	}
	public String getImg2() {
		return img2;
	}
	public void setImg2(String img2) {
		this.img2 = img2;
	}
	public String getImg3() {
		return img3;
	}
	public void setImg3(String img3) {
		this.img3 = img3;
	}
	public String getImg4() {
		return img4;
	}
	public void setImg4(String img4) {
		this.img4 = img4;
	}
	public Integer getProducterId() {
		return producterId;
	}
	public void setProducterId(Integer producterId) {
		this.producterId = producterId;
	}
	public Integer getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	@Override
	public String toString() {
		return "TTask{" +
				"productBatchesId=" + productBatchesId +
				", content='" + content + '\'' +
				", state=" + state +
				", img1='" + img1 + '\'' +
				", img2='" + img2 + '\'' +
				", img3='" + img3 + '\'' +
				", img4='" + img4 + '\'' +
				", producterId=" + producterId +
				", sysUserId=" + sysUserId +
				", productBatches=" + productBatches +
				", farmId=" + farmId +
				", activityDate=" + activityDate +
				'}';
	}
}
