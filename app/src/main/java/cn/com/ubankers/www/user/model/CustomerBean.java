package cn.com.ubankers.www.user.model;

import java.io.Serializable;
//我的客户
@SuppressWarnings("serial")
public class CustomerBean implements Serializable{
	
	private String    id;//主键
	private String   nickName;//昵称
	private String   mobile;//手机号
	private String   email;//邮箱
	private String   addTime;//创建时间时间戳
	private int      userType;//用户类型 1资本2财富3投资
	private String   bankAccountNo;//银行卡号
	private String   bankAccountName;//银行卡持卡人姓名（用扩展表的真实姓名）
	private String   bankAccountStatus;//银行卡号认证情况
	private String   productRealName;//真实姓名
	private String   productRealNameStatus;//真实姓名认证情况
	private String   idcardFrontFileId;//身份证正面文件id
	private String   idcardBackFileId;//身份证背面面文件id
	private String   bindId;//投资者绑定财富师的id
	private String   memo;//投资者绑定财富师备注
	private int start;
	private int limit;
	private String  amount;
	private String unRegistedUserId;
	private String realName;
	private String idcard;
	private int delFlag;
	private String userFaceFileId;

	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUserFaceFileId() {
		return userFaceFileId;
	}
	public void setUserFaceFileId(String userFaceFileId) {
		this.userFaceFileId = userFaceFileId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int i) {
		this.userType = i;
	}
	public String getBankAccountNo() {
		return bankAccountNo;
	}
	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBankAccountStatus() {
		return bankAccountStatus;
	}
	public void setBankAccountStatus(String bankAccountStatus) {
		this.bankAccountStatus = bankAccountStatus;
	}
	public String getProductRealName() {
		return productRealName;
	}
	public void setProductRealName(String productRealName) {
		this.productRealName = productRealName;
	}
	public String getProductRealNameStatus() {
		return productRealNameStatus;
	}
	public void setProductRealNameStatus(String productRealNameStatus) {
		this.productRealNameStatus = productRealNameStatus;
	}
	public String getIdcardFrontFileId() {
		return idcardFrontFileId;
	}
	public void setIdcardFrontFileId(String idcardFrontFileId) {
		this.idcardFrontFileId = idcardFrontFileId;
	}
	public String getIdcardBackFileId() {
		return idcardBackFileId;
	}
	public void setIdcardBackFileId(String idcardBackFileId) {
		this.idcardBackFileId = idcardBackFileId;
	}
	public String getBindId() {
		return bindId;
	}
	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getUnRegistedUserId() {
		return unRegistedUserId;
	}
	public void setUnRegistedUserId(String unRegistedUserId) {
		this.unRegistedUserId = unRegistedUserId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public int getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}
	@Override
	public String toString() {
		return "ClientBean [id=" + id + ", nickName=" + nickName + ", mobile="
				+ mobile + ", email=" + email + ", addTime=" + addTime
				+ ", userType=" + userType + ", bankAccountNo=" + bankAccountNo
				+ ", bankAccountName=" + bankAccountName
				+ ", bankAccountStatus=" + bankAccountStatus
				+ ", productRealName=" + productRealName
				+ ", productRealNameStatus=" + productRealNameStatus
				+ ", idcardFrontFileId=" + idcardFrontFileId
				+ ", idcardBackFileId=" + idcardBackFileId + ", bindId="
				+ bindId + ", memo=" + memo + ", userFaceFileId=" + userFaceFileId +"]";
	}

}
