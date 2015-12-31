package cn.com.ubankers.www.product.model;

public class MyInvestor {
	private long id;//投资者id
	private String nickName;//投资者昵称
	private String mobile;//投资者手机号
	private String productRealName;//真实姓名
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProductRealName() {
		return productRealName;
	}
	public void setProductRealName(String productRealName) {
		this.productRealName = productRealName;
	}
	
	
}
