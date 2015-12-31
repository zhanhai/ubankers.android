package cn.com.ubankers.www.product.model;

import java.io.Serializable;



public class ProductDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5791925065369789283L;
	/* 产品类 */
	private String productId;/* 产品id */
	private String productName;
	private int state; // 产品发布状态
	private String tags; // 产品标签数组
	private String moduleId; // 产品的模板ID
	private String face; // 产品头像-
	private String productRate; // 预期收益率(描述)-
	private String countProductRate; // 预期收益率(计算字段)
	private String productTerm; // 产品期限
	private String fundraiserId; // 资本师id
	private int raisedProcessShow;// 产品募集显示进度-
	private String minSureBuyPrice; // 显示的起投金额-
	private int isHot; // 是否热门-
	private String commission; // 佣金率
	private String raisedProcess;// 产品目前进度
	private String Picturepath;//图片路径
	private String form ;//产品类型
	private String scopes;//计划规模（）
	private String IssuingScale;//产品类型
	private String investmentManager;//投资经理
	private String investmentRestriction;//投资期限
	private String scopeInvestment;//投资范围
	private String strategy;//开放日
	private String objective;//成立日
	private String scopes1;//计划规模
	private String expectedreturn;//保管人/托管人 
	private String scale;//投资顾问
	private String Custodian;//受托人 
	private String trusteeAgency;//托管机构
	private String ThelatestNet;//最新进值
	private String accumulatedIncome;//累计收益
	private String briefIntroduction;//投资简介
	private String bondsman;//担保人
	private String guarantee;//	抵押物（下）
	private String conters;//运作方式
	private String payType;//支付类型
	private String endTime;//预定结束时间
	private int maxMoney;//最大金额
	private int minMoney;//最小金额
	private int incrementalMoney;//递增金额
	private boolean canReserve;//能否预约
	private String minMoneyYuan; //预约的起投金额
	
 
	
	
	public String getMinMoneyYuan() {
		return minMoneyYuan;
	}

	public void setMinMoneyYuan(String minMoneyYuan) {
		this.minMoneyYuan = minMoneyYuan;
	}

	public int getRaisedProcessShow() {
		return raisedProcessShow;
	}

	public void setRaisedProcessShow(int raisedProcessShow) {
		this.raisedProcessShow = raisedProcessShow;
	}


	public String getBriefIntroduction() {
		return briefIntroduction;
	}

	public void setBriefIntroduction(String briefIntroduction) {
		this.briefIntroduction = briefIntroduction;
	}

	public String getBondsman() {
		return bondsman;
	}

	public void setBondsman(String bondsman) {
		this.bondsman = bondsman;
	}

	public String getGuarantee() {
		return guarantee;
	}

	public void setGuarantee(String guarantee) {
		this.guarantee = guarantee;
	}

	public String getConters() {
		return conters;
	}

	public void setConters(String conters) {
		this.conters = conters;
	}

	public String getThelatestNet() {
		return ThelatestNet;
	}

	public void setThelatestNet(String thelatestNet) {
		ThelatestNet = thelatestNet;
	}

	public String getAccumulatedIncome() {
		return accumulatedIncome;
	}

	public void setAccumulatedIncome(String accumulatedIncome) {
		this.accumulatedIncome = accumulatedIncome;
	}

	public String getTrusteeAgency() {
		return trusteeAgency;
	}

	public void setTrusteeAgency(String trusteeAgency) {
		this.trusteeAgency = trusteeAgency;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public String getIssuingScale() {
		return IssuingScale;
	}

	public void setIssuingScale(String issuingScale) {
		IssuingScale = issuingScale;
	}

	public String getInvestmentManager() {
		return investmentManager;
	}

	public void setInvestmentManager(String investmentManager) {
		this.investmentManager = investmentManager;
	}

	public String getInvestmentRestriction() {
		return investmentRestriction;
	}

	public void setInvestmentRestriction(String investmentRestriction) {
		this.investmentRestriction = investmentRestriction;
	}

	public String getScopeInvestment() {
		return scopeInvestment;
	}

	public void setScopeInvestment(String scopeInvestment) {
		this.scopeInvestment = scopeInvestment;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getScopes1() {
		return scopes1;
	}

	public void setScopes1(String scopes1) {
		this.scopes1 = scopes1;
	}

	public String getExpectedreturn() {
		return expectedreturn;
	}

	public void setExpectedreturn(String expectedreturn) {
		this.expectedreturn = expectedreturn;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getCustodian() {
		return Custodian;
	}

	public void setCustodian(String custodian) {
		Custodian = custodian;
	}


	public String getPicturepath() {
		return Picturepath;
	}

	public void setPicturepath(String picturepath) {
		Picturepath = picturepath;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public String getProductRate() {
		return productRate;
	}

	public void setProductRate(String productRate) {
		this.productRate = productRate;
	}

	public String getCountProductRate() {
		return countProductRate;
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

	public String getFundraiserId() {
		return fundraiserId;
	}

	public void setFundraiserId(String fundraiserId) {
		this.fundraiserId = fundraiserId;
	}


	public String getMinSureBuyPrice() {
		return minSureBuyPrice;
	}

	public void setMinSureBuyPrice(String minSureBuyPrice) {
		this.minSureBuyPrice = minSureBuyPrice;
	}

	public int getIsHot() {
		return isHot;
	}

	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getRaisedProcess() {
		return raisedProcess;
	}

	public void setRaisedProcess(String raisedProcess) {
		this.raisedProcess = raisedProcess;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(int maxMoney) {
		this.maxMoney = maxMoney;
	}

	public int getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(int minMoney) {
		this.minMoney = minMoney;
	}

	public int getIncrementalMoney() {
		return incrementalMoney;
	}

	public void setIncrementalMoney(int incrementalMoney) {
		this.incrementalMoney = incrementalMoney;
	}

	public boolean isCanReserve() {
		return canReserve;
	}

	public void setCanReserve(boolean canReserve) {
		this.canReserve = canReserve;
	}
	
	
}
