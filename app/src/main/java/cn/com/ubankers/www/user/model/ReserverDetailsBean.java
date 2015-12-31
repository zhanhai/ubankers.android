package cn.com.ubankers.www.user.model;

//预约产品详情实体类
public class ReserverDetailsBean {
	private String reserveId;
	private String reserveName;
	private String reserveMobile;
	private String productName;
	private String reserveQuota;
	private String reserveVoucherId;
	private String idcardBackFileId;
	private String idcardFrontFileId;
	private String userId;
	public String getReserveName() {
		return reserveName;
	}
	public void setReserveName(String reserveName) {
		this.reserveName = reserveName;
	}
	public String getReserveMobile() {
		return reserveMobile;
	}
	public void setReserveMobile(String reserveMobile) {
		this.reserveMobile = reserveMobile;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getReserveQuota() {
		return reserveQuota;
	}
	public void setReserveQuota(String reserveQuota) {
		this.reserveQuota = reserveQuota;
	}
	public String getReserveVoucherId() {
		return reserveVoucherId;
	}
	public void setReserveVoucherId(String reserveVoucherId) {
		this.reserveVoucherId = reserveVoucherId;
	}
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getIdcardBackFileId() {
		return idcardBackFileId;
	}
	public void setIdcardBackFileId(String idcardBackFileId) {
		this.idcardBackFileId = idcardBackFileId;
	}
	public String getIdcardFrontFileId() {
		return idcardFrontFileId;
	}
	public void setIdcardFrontFileId(String idcardFrontFileId) {
		this.idcardFrontFileId = idcardFrontFileId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
