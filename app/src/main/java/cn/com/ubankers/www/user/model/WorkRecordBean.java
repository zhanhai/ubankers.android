package cn.com.ubankers.www.user.model;

import java.io.Serializable;

public class WorkRecordBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 756886223772236136L;
	private String productName;
	private String saleMoney;
	private String getMoney;
	private String createTime;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSaleMoney() {
		return saleMoney;
	}
	public void setSaleMoney(String saleMoney) {
		this.saleMoney = saleMoney;
	}
	public String getGetMoney() {
		return getMoney;
	}
	public void setGetMoney(String getMoney) {
		this.getMoney = getMoney;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
