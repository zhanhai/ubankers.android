package cn.com.ubankers.www.user.model;

import java.io.Serializable;

public class AttributesBean implements Serializable{

	/**
	 * implements user extend attributes
	 */
	private static final long serialVersionUID = -8490131862674489081L;
    private String catalog;
    private String name;
    private String userId;
    private  String value;
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
