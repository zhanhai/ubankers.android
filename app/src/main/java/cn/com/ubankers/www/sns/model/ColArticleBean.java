package cn.com.ubankers.www.sns.model;

import java.io.Serializable;

public class ColArticleBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6859791641809128980L;
	
	private String favor_cover;
	private int favor_commentCount;
	private String favor_author_id;
	private String favor_author;
	private String favor_abstract;
	private String favor_url;
	private String favor_content_title;
	private String favor_date;
	private String favor_content_id;
	private String _id;
	private String favor_tags;
	private String userface;
	private int identifying;
	public String getFavor_cover() {
		return favor_cover;
	}
	public void setFavor_cover(String favor_cover) {
		this.favor_cover = favor_cover;
	}
	public int getFavor_commentCount() {
		return favor_commentCount;
	}
	public void setFavor_commentCount(int favor_commentCount) {
		this.favor_commentCount = favor_commentCount;
	}
	public String getFavor_author_id() {
		return favor_author_id;
	}
	public void setFavor_author_id(String favor_author_id) {
		this.favor_author_id = favor_author_id;
	}
	public String getFavor_author() {
		return favor_author;
	}
	public void setFavor_author(String favor_author) {
		this.favor_author = favor_author;
	}
	public String getFavor_abstract() {
		return favor_abstract;
	}
	public void setFavor_abstract(String favor_abstract) {
		this.favor_abstract = favor_abstract;
	}
	public String getFavor_url() {
		return favor_url;
	}
	public void setFavor_url(String favor_url) {
		this.favor_url = favor_url;
	}
	public String getFavor_content_title() {
		return favor_content_title;
	}
	public void setFavor_content_title(String favor_content_title) {
		this.favor_content_title = favor_content_title;
	}
	public String getFavor_date() {
		return favor_date;
	}
	public void setFavor_date(String favor_date) {
		this.favor_date = favor_date;
	}
	public String getFavor_content_id() {
		return favor_content_id;
	}
	public void setFavor_content_id(String favor_content_id) {
		this.favor_content_id = favor_content_id;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getFavor_tags() {
		return favor_tags;
	}
	public void setFavor_tags(String favor_tags) {
		this.favor_tags = favor_tags;
	}
	public String getUserface() {
		return userface;
	}
	public void setUserface(String userface) {
		this.userface = userface;
	}
	public int getIdentifying() {
		return identifying;
	}
	public void setIdentifying(int identifying) {
		this.identifying = identifying;
	}
	
}
