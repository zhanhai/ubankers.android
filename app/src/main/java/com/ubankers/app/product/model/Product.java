package com.ubankers.app.product.model;

public class Product {
	private String productId;/* 产品id */
	private String productName;
	private int state; // 产品发布状态
	private String moduleId; // 产品的模板ID
	private String face; // 产品头像-
	private String countProductRate; // 预期收益率(计算字段)
	private String productTerm; // 产品期限
	private int raisedProcessShow;// 产品募集显示进度-
	private String minSureBuyPrice; // 显示的起投金额-
	private int isHot; // 是否热门-

	private ProductSaleOptions saleOptions;
	
 
	
	
	public int getRaisedProcessShow() {
		return raisedProcessShow;
	}

	public void setRaisedProcessShow(int raisedProcessShow) {
		this.raisedProcessShow = raisedProcessShow;
	}









	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}


	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public void setCountProductRate(String countProductRate) {
		this.countProductRate = countProductRate;
	}

	public String getProductTerm() {
		return productTerm;
	}

	public void setProductTerm(String productTerm) {
		this.productTerm = productTerm;
	}


	public String getMinSureBuyPrice() {
		return minSureBuyPrice;
	}

	public void setMinSureBuyPrice(String minSureBuyPrice) {
		this.minSureBuyPrice = minSureBuyPrice;
	}

	public int getIsHot() {
		return isHot;
	}

	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}


	public String getCountProductRate() {
		return countProductRate;
	}

	public ProductSaleOptions getSaleOptions() {
		return saleOptions;
	}

	public void setSaleOptions(ProductSaleOptions saleOptions) {
		this.saleOptions = saleOptions;
	}
}
