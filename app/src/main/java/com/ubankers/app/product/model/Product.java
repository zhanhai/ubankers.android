package com.ubankers.app.product.model;

import com.google.gson.annotations.SerializedName;

import cn.com.ubankers.www.product.model.ProductDetail;

public class Product {

    @SerializedName("id")
	private String productId;/* 产品id */
	private String productName;
	private String moduleId; // 产品的模板ID
	private int state; // 产品发布状态
    private boolean isHot; // 是否热门-
    private String productTerm; // 产品期限
    private String countProductRate; // 预期收益率(计算字段)
    private String minSureBuyPrice; // 显示的起投金额-
    private int raisedProcessShow;// 产品募集显示进度-
	private String face; // 产品头像-

	private ProductSaleOptions saleOptions = new ProductSaleOptions();
	

	
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


	public String getCountProductRate() {
		return countProductRate;
	}

	public ProductSaleOptions getSaleOptions() {
		return saleOptions;
	}

	public void setSaleOptions(ProductSaleOptions saleOptions) {
		if(saleOptions == null){
            return;
        }

        this.saleOptions = saleOptions;
	}

    public boolean isHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

	public boolean canReserve(){
		return saleOptions.getReserveOptions().isCanReserve();
	}

    public int getMinMoney(){
        return saleOptions.getReserveOptions().getMinMoney();
    }

    public int getMaxMoney(){
        return saleOptions.getReserveOptions().getMaxMoney();
    }

    public int getIncrementalMoney(){
        return saleOptions.getReserveOptions().getIncrementalMoney();
    }

    public String getMinMoneyYuan(){
        return saleOptions.getReserveOptions().getMinMoneyYuan();
    }

    public static Product from(final ProductDetail detail){
        Product product = new Product();

        product.setProductId(detail.getProductId());
        product.setProductName(detail.getProductName());
        product.setModuleId(detail.getModuleId());
        product.setState(detail.getState());
        product.setIsHot(detail.getIsHot() == 0 ? false : true);
        product.setProductTerm(detail.getProductTerm());
        product.setCountProductRate(detail.getCountProductRate());
        product.setMinSureBuyPrice(detail.getMinSureBuyPrice());
        product.setRaisedProcessShow(detail.getRaisedProcessShow());
        product.setFace(detail.getFace());

        ProductReserveOptions reserveOptions = product.saleOptions.getReserveOptions();
        reserveOptions.setPayType(detail.getPayType());
        reserveOptions.setEndTime(detail.getEndTime());
        reserveOptions.setMaxMoney(detail.getMaxMoney());
        reserveOptions.setMinMoney(detail.getMinMoney());
        reserveOptions.setIncrementalMoney(detail.getIncrementalMoney());
        reserveOptions.setCanReserve(detail.isCanReserve());
        reserveOptions.setMinMoneyYuan(detail.getMinMoneyYuan());

        return product;
    }
}
