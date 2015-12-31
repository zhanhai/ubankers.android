package cn.com.ubankers.www.user.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserNewBean implements Serializable {

	private BaseBean base;
	private List<AttributesBean> extAttributes;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BaseBean getBase() {
		return base;
	}

	public void setBase(BaseBean base) {
		this.base = base;
	}

	public List<AttributesBean> getExtAttributes() {
		return extAttributes;
	}

	public void setExtAttributes(List<AttributesBean> extAttributes) {
		this.extAttributes = extAttributes;
	}

	

}
