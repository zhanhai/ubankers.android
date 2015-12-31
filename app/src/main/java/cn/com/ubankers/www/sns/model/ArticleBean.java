package cn.com.ubankers.www.sns.model;

import java.io.Serializable;

import org.json.JSONObject;

public class ArticleBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8212493344114779365L;
	private String _id;
	private String _contentType;
	private String title;
	private String content;
	private String cover;
	private int __v;
	private String sign;
	private String resource_address;
	private int favor_count;
	private int refer_count;
	private int vote_count;
	private int comment_count;
	private int read_count;
	private String create_date;
	private String tags;
	private AuthorBean author;
	private ContentSourceBean sourceBean;	
	private boolean is_voted;
	private boolean is_favored;
	private String abstracting;
	private int identifying;
	
	public ContentSourceBean getSourceBean() {
		return sourceBean;
	}
	public void setSourceBean(ContentSourceBean sourceBean) {
		this.sourceBean = sourceBean;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String get_contentType() {
		return _contentType;
	}
	public void set_contentType(String _contentType) {
		this._contentType = _contentType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public int get__v() {
		return __v;
	}
	public void set__v(int __v) {
		this.__v = __v;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResource_address() {
		return resource_address;
	}
	public void setResource_address(String resource_address) {
		this.resource_address = resource_address;
	}
	public int getFavor_count() {
		return favor_count;
	}
	public void setFavor_count(int favor_count) {
		this.favor_count = favor_count;
	}
	public int getRefer_count() {
		return refer_count;
	}
	public void setRefer_count(int refer_count) {
		this.refer_count = refer_count;
	}
	public int getVote_count() {
		return vote_count;
	}
	public void setVote_count(int vote_count) {
		this.vote_count = vote_count;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getRead_count() {
		return read_count;
	}
	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	} 
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	public boolean isIs_voted() {
		return is_voted;
	}
	public void setIs_voted(boolean is_voted) {
		this.is_voted = is_voted;
	}
	public boolean isIs_favored() {
		return is_favored;
	}
	public void setIs_favored(boolean is_favored) {
		this.is_favored = is_favored;
	}
	public String getAbstracting() {
		return abstracting;
	}
	public void setAbstracting(String abstracting) {
		this.abstracting = abstracting;
	}
	public int getIdentifying() {
		return identifying;
	}
	public void setIdentifying(int identifying) {
		this.identifying = identifying;
	}
	
}
