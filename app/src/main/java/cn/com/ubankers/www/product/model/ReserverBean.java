package cn.com.ubankers.www.product.model;

//产品预约实体类
public class ReserverBean {
	private int examineState;//财富师审批状态
	private String serviceStaff;//接线客服
	private int cfmpId;//财富师id
	private int reserveId;//预约的id
	private String reserveName;//预约名称
	private String reserveQuota;//预约额度
	private String reserveMobile;//预约电话
	private int reserveRole;//预约角色
	private String reserveTime;//预约时间
	private String remarks;//预约备注
	private int  endContactTime;//客服最后联系时间
	private int isNotContact;//客服是否已联系
	public int getExamineState() {
		return examineState;
	}
	public void setExamineState(int examineState) {
		this.examineState = examineState;
	}
	public String getServiceStaff() {
		return serviceStaff;
	}
	public void setServiceStaff(String serviceStaff) {
		this.serviceStaff = serviceStaff;
	}
	public int getCfmpId() {
		return cfmpId;
	}
	public void setCfmpId(int cfmpId) {
		this.cfmpId = cfmpId;
	}
	public int getReserveId() {
		return reserveId;
	}
	public void setReserveId(int reserveId) {
		this.reserveId = reserveId;
	}
	public String getReserveName() {
		return reserveName;
	}
	public void setReserveName(String reserveName) {
		this.reserveName = reserveName;
	}
	public String getReserveQuota() {
		return reserveQuota;
	}
	public void setReserveQuota(String reserveQuota) {
		this.reserveQuota = reserveQuota;
	}
	public String getReserveMobile() {
		return reserveMobile;
	}
	public void setReserveMobile(String reserveMobile) {
		this.reserveMobile = reserveMobile;
	}
	public int getReserveRole() {
		return reserveRole;
	}
	public void setReserveRole(int reserveRole) {
		this.reserveRole = reserveRole;
	}
	public String getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getEndContactTime() {
		return endContactTime;
	}
	public void setEndContactTime(int endContactTime) {
		this.endContactTime = endContactTime;
	}
	public int getIsNotContact() {
		return isNotContact;
	}
	public void setIsNotContact(int isNotContact) {
		this.isNotContact = isNotContact;
	}
	
	
}
