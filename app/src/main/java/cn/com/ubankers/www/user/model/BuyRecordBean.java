package cn.com.ubankers.www.user.model;

public class BuyRecordBean {
	private String  cfmpName;//财富师"
	private String	productRate;//预期年化收益率 -1表示浮动，
	private long	valueDate; //起息日（毫秒）
	private long  expiryDateForInterest;  //到期日（毫秒） 
	private String  productName;  //购买产品名称
	private String  purchaseAmount;  //购买金额
	private long   purchaseDate;  //购买时间（毫秒） 
    private int  state;  	//状态  
	public String getCfmpName() {
		return cfmpName;
	}
	public void setCfmpName(String cfmpName) {
		this.cfmpName = cfmpName;
	}
	public String getProductRate() {
		return productRate;
	}
	public void setProductRate(String productRate) {
		this.productRate = productRate;
	}
	public long getValueDate() {
		return valueDate;
	}
	public void setValueDate(long valueDate) {
		this.valueDate = valueDate;
	}
	public long getExpiryDateForInterest() {
		return expiryDateForInterest;
	}
	public void setExpiryDateForInterest(long expiryDateForInterest) {
		this.expiryDateForInterest = expiryDateForInterest;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(String purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public long getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(long purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
    
}
