package cn.com.ubankers.www.user.model;

//财富师推荐产品
public class RecommendPtsBean {
	private String addTime;//推荐时间
	private String productName;//产品名称
	private String productRate;//预期收益率
	private String minSureBuyPrice;//起投金额
	private String productId;//产品id
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductRate() {
		return productRate;
	}
	public void setProductRate(String productRate) {
		this.productRate = productRate;
	}
	public String getMinSureBuyPrice() {
		return minSureBuyPrice;
	}
	public void setMinSureBuyPrice(String minSureBuyPrice) {
		this.minSureBuyPrice = minSureBuyPrice;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
}
