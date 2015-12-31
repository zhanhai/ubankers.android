package cn.com.ubankers.www.sns.model;

public class CommentBean {
	private String id;//发表评论的id
	private String commenter_name;//评论者
	private String comment_content;//评论的内容
	private String comment_date;//评论的日期
	private int reply_id;//回复的id的下标
	private String replier_name;//回复的用户名
	private String reply_content;//回复的内容
	private String reply_date;//回复的日期
	private String commenter_id;//评论者的ID，这条评论的作者
	private int replier_id;//回复的id
	private String userFaceId;//评论者的头像
	private String reply_userFaceId;//回复者的头像
	private int reply_count;//回复的条数
	private int comment_identifying;//评论的v标识
	private int reply_identifying;//回复的v标识
	private int comment_count;//评论的总数
	
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	public String getReply_userFaceId() {
		return reply_userFaceId;
	}
	public void setReply_userFaceId(String reply_userFaceId) {
		this.reply_userFaceId = reply_userFaceId;
	}
	public String getUserFaceId() {
		return userFaceId;
	}
	public void setUserFaceId(String userFaceId) {
		this.userFaceId = userFaceId;
	}
	public int getReplier_id() {
		return replier_id;
	}
	public void setReplier_id(int replier_id) {
		this.replier_id = replier_id;
	}
	public String getCommenter_id() {
		return commenter_id;
	}
	public void setCommenter_id(String commenter_id) {
		this.commenter_id = commenter_id;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCommenter_name() {
		return commenter_name;
	}
	public void setCommenter_name(String commenter_name) {
		this.commenter_name = commenter_name;
	}
	public String getComment_content() {
		return comment_content;
	}
	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}
	public String getComment_date() {
		return comment_date;
	}
	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}
	public int getReply_id() {
		return reply_id;
	}
	public void setReply_id(int reply_id) {
		this.reply_id = reply_id;
	}
	public String getReplier_name() {
		return replier_name;
	}
	public void setReplier_name(String replier_name) {
		this.replier_name = replier_name;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}
	public String getReply_date() {
		return reply_date;
	}
	public void setReply_date(String reply_date) {
		this.reply_date = reply_date;
	}
	public int getComment_identifying() {
		return comment_identifying;
	}
	public void setComment_identifying(int comment_identifying) {
		this.comment_identifying = comment_identifying;
	}
	public int getReply_identifying() {
		return reply_identifying;
	}
	public void setReply_identifying(int reply_identifying) {
		this.reply_identifying = reply_identifying;
	}
	
	
}
