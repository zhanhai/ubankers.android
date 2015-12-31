package cn.com.ubankers.www.user.model;

import java.io.Serializable;



public class UserBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5441049431143325640L;
	
	
	private String userId;
	private String userName;
	private String userRole;//用户角色
	private String userkey;
	private String userFaceID;//用户头像的ID
	private String extAttributes_name;
	private String email;//邮箱
	private String idcard_value;//身份证
	private String bankcard_status;//银行卡状态
	private String bankcard_no;//银行卡号
	private String idcard_frontfid;//身份证正面ID
	private String idcard_backfid;//身份证反面ID
    private String idcard_catalog;//身份证登记
    private String bankcard_catalong;//银行卡登记
    private String role_catalong;//角色登记
    private String catalog;//固定的登记
    private String idcard_name;//真实姓名
    private String idcard_no;//身份证号
    private String idcard_status;//身份证状态
    private String userMobile;//登录的手机号
    private String recommenderUrl;//財富師推薦鏈接
    private String  businessCard;//名片是否绑定
    private String  fileid;//名片图片ID
    private String loginToken;
    private String  openid;
    private String nickname;
    private String provider;
    private String bankcard_Failure_reason;//银行卡失败原因
    private String idcard_Failure_reason;//身份证失败原因
    private String Catalog_Failure_reason;//名片失败原因
	
	public String getCatalog_Failure_reason() {
		return Catalog_Failure_reason;
	}
	public void setCatalog_Failure_reason(String catalog_Failure_reason) {
		Catalog_Failure_reason = catalog_Failure_reason;
	}
	public String getBankcard_Failure_reason() {
		return bankcard_Failure_reason;
	}
	public void setBankcard_Failure_reason(String bankcard_Failure_reason) {
		this.bankcard_Failure_reason = bankcard_Failure_reason;
	}
	public String getIdcard_Failure_reason() {
		return idcard_Failure_reason;
	}
	public void setIdcard_Failure_reason(String idcard_Failure_reason) {
		this.idcard_Failure_reason = idcard_Failure_reason;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getLoginToken() {
		return loginToken;
	}
	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public String getBusinessCard() {
		return businessCard;
	}
	public void setBusinessCard(String businessCard) {
		this.businessCard = businessCard;
	}
	public String getUserFaceID() {
		return userFaceID;
	}
	public void setUserFaceID(String userFaceID) {
		this.userFaceID = userFaceID;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getUserkey() {
		return userkey;
	}
	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}
	public String getExtAttributes_name() {
		return extAttributes_name;
	}
	public void setExtAttributes_name(String extAttributes_name) {
		this.extAttributes_name = extAttributes_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdcard_value() {
		return idcard_value;
	}
	public void setIdcard_value(String idcard_value) {
		this.idcard_value = idcard_value;
	}
	public String getBankcard_status() {
		return bankcard_status;
	}
	public void setBankcard_status(String bankcard_status) {
		this.bankcard_status = bankcard_status;
	}
	public String getBankcard_no() {
		return bankcard_no;
	}
	public void setBankcard_no(String bankcard_no) {
		this.bankcard_no = bankcard_no;
	}
	public String getIdcard_frontfid() {
		return idcard_frontfid;
	}
	public void setIdcard_frontfid(String idcard_frontfid) {
		this.idcard_frontfid = idcard_frontfid;
	}
	public String getIdcard_backfid() {
		return idcard_backfid;
	}
	public void setIdcard_backfid(String idcard_backfid) {
		this.idcard_backfid = idcard_backfid;
	}
	public String getIdcard_catalog() {
		return idcard_catalog;
	}
	public void setIdcard_catalog(String idcard_catalog) {
		this.idcard_catalog = idcard_catalog;
	}
	public String getBankcard_catalong() {
		return bankcard_catalong;
	}
	public void setBankcard_catalong(String bankcard_catalong) {
		this.bankcard_catalong = bankcard_catalong;
	}
	public String getRole_catalong() {
		return role_catalong;
	}
	public void setRole_catalong(String role_catalong) {
		this.role_catalong = role_catalong;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getIdcard_name() {
		return idcard_name;
	}
	public void setIdcard_name(String idcard_name) {
		this.idcard_name = idcard_name;
	}
	public String getIdcard_no() {
		return idcard_no;
	}
	public void setIdcard_no(String idcard_no) {
		this.idcard_no = idcard_no;
	}
	public String getIdcard_status() {
		return idcard_status;
	}
	public void setIdcard_status(String idcard_status) {
		this.idcard_status = idcard_status;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public String getRecommenderUrl() {
		return recommenderUrl;
	}
	public void setRecommenderUrl(String recommenderUrl) {
		this.recommenderUrl = recommenderUrl;
	}
	
	
	
	
	

}
