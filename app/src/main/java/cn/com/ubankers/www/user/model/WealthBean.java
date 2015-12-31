package cn.com.ubankers.www.user.model;

import java.io.Serializable;

public class WealthBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1438919629555462755L;

	private String addDate;//创建日期
	private String addUserId;//创建人id
	private String auditDate;//审核日期
	private String auditMemo;//审核备注
	private int auditStatus;//审核状态(1未提交，2已提交，未审核，3审核通过，4回退修改，审核不通过)
	private String auditUserId;//审核人用户id
	private String begin;
	private String dir;
	private int disband;//是否解散 ( 1正常 , 2解散 )
	private String end;
	private String leaderId;//工作室领袖id
	private String limit;
	private String slogan;//口号宣言
	private String sortName;
	private String start;
	private String studioId;//工作室主键
	private String studioName;//工作室名称
	private String updateDate;//修改日期
	private String updateUserId;//修改人id
	private int auditNeedDays;
	
	
	public int getAuditNeedDays() {
		return auditNeedDays;
	}
	public void setAuditNeedDays(int auditNeedDays) {
		this.auditNeedDays = auditNeedDays;
	}
	public String getAddDate() {
		return addDate;
	}
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}
	public String getAddUserId() {
		return addUserId;
	}
	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getAuditMemo() {
		return auditMemo;
	}
	public void setAuditMemo(String auditMemo) {
		this.auditMemo = auditMemo;
	}
	public int getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public int getDisband() {
		return disband;
	}
	public void setDisband(int disband) {
		this.disband = disband;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getSlogan() {
		return slogan;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getStudioId() {
		return studioId;
	}
	public void setStudioId(String studioId) {
		this.studioId = studioId;
	}
	public String getStudioName() {
		return studioName;
	}
	public void setStudioName(String studioName) {
		this.studioName = studioName;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	

}
