package cn.com.ubankers.www.user.model;
//投资者预约订单实体类
public class InvestorOrderBean {
	private String reserveId;//预约id
	private String productName;//产品名称
	private int examineState;//预约状态
	private String reserveTimeShow;//预约时间
	private String reserveQuota;//预约金额
	private String productId;//产品id
	private String reserveName;//预约名称
	private String totalProNum;//总记录
	private String totalAmount;//总金额
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getExamineState() {
		return examineState;
	}
	public void setExamineState(int examineState) {
		this.examineState = examineState;
	}
	public String getReserveTimeShow() {
		return reserveTimeShow;
	}
	public void setReserveTimeShow(String reserveTimeShow) {
		this.reserveTimeShow = reserveTimeShow;
	}
	public String getReserveQuota() {
		return reserveQuota;
	}
	public void setReserveQuota(String reserveQuota) {
		this.reserveQuota = reserveQuota;
	}
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getReserveName() {
		return reserveName;
	}
	public void setReserveName(String reserveName) {
		this.reserveName = reserveName;
	}
	public String getTotalProNum() {
		return totalProNum;
	}
	public void setTotalProNum(String totalProNum) {
		this.totalProNum = totalProNum;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
