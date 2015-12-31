package cn.com.ubankers.www.user.model;

import java.io.Serializable;

public class MoneyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879536349425805418L;
	private String totalAmount;// 总金额
	private String totalBalance;//可用余额
	private String totalFroze;//
	
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getTotalBalance() {
		return totalBalance;
	}
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}
	public String getTotalFroze() {
		return totalFroze;
	}
	public void setTotalFroze(String totalFroze) {
		this.totalFroze = totalFroze;
	}
	
	
}
