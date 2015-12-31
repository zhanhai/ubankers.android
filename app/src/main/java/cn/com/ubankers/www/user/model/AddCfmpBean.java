package cn.com.ubankers.www.user.model;

import java.io.Serializable;

public class AddCfmpBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1355676285464221849L;
	private String addTime;
	private String cfmpId;
	private int begin;
	private int start;
	private int end;
	private String id;
	private String investorId;
	private int isBInvestor;
	private int limit;
	private String memo;
	private String sortName;
	private String dir;
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getCfmpId() {
		return cfmpId;
	}
	public void setCfmpId(String cfmpId) {
		this.cfmpId = cfmpId;
	}
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public int getIsBInvestor() {
		return isBInvestor;
	}
	public void setIsBInvestor(int isBInvestor) {
		this.isBInvestor = isBInvestor;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	

}
