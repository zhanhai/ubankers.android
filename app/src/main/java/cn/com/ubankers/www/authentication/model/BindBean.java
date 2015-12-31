package cn.com.ubankers.www.authentication.model;

import java.io.Serializable;

public class BindBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -801414404180882117L;
	private String BundleId;
	private String PlatfromName;
	private String PlatfromUserName;
	public String getBundleId() {
		return BundleId;
	}
	public void setBundleId(String bundleId) {
		BundleId = bundleId;
	}
	public String getPlatfromName() {
		return PlatfromName;
	}
	public void setPlatfromName(String platfromName) {
		PlatfromName = platfromName;
	}
	public String getPlatfromUserName() {
		return PlatfromUserName;
	}
	public void setPlatfromUserName(String platfromUserName) {
		PlatfromUserName = platfromUserName;
	}
}
