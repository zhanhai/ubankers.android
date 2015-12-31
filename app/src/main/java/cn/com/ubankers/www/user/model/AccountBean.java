package cn.com.ubankers.www.user.model;

public class AccountBean {
	
private String description;//产品名称
private String status;//交易装填
private int amount;//交易金额
private long startTime;//日期
private String type;//交易类型

public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public int getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}
public long getStartTime() {
	return startTime;
}
public void setStartTime(long startTime) {
	this.startTime = startTime;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}



}
