package cn.com.ubankers.www.sns.model;

import java.io.Serializable;

public class ContentSourceBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9074057563346725237L;
	private String source_type;
	private String source_detail;
	
	
	public String getSource_type() {
		return source_type;
	}
	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}
	public String getSource_detail() {
		return source_detail;
	}
	public void setSource_detail(String source_detail) {
		this.source_detail = source_detail;
	}


}
