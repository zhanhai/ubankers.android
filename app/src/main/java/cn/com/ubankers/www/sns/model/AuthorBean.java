package cn.com.ubankers.www.sns.model;

import java.io.Serializable;

public class AuthorBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3101471978283480047L;
	private String author_id;
	private String author_name;
	private String userface;
	private int identifying;
	private int role;
	public String getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
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
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
}
