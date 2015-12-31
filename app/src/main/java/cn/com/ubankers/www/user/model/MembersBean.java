package cn.com.ubankers.www.user.model;

import java.io.Serializable;

public class MembersBean implements Serializable{
     /**
	  * 
	  */
	private static final long serialVersionUID = 3213083339771817695L;
		private String userface ;
		private String realname ;
		private String userId ; 
		private Long joinDate ; 
		private String nickName ;
		private String mobile ;
		private int leaderFlag;
		
		public int getLeaderFlag() {
			return leaderFlag;
		}
		public void setLeaderFlag(int leaderFlag) {
			this.leaderFlag = leaderFlag;
		}
		public String getUserface() {
			return userface;
		}
		public void setUserface(String userface) {
			this.userface = userface;
		}
		public String getRealname() {
			return realname;
		}
		public void setRealname(String realname) {
			this.realname = realname;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Long getJoinDate() {
			return joinDate;
		}
		public void setJoinDate(Long joinDate) {
			this.joinDate = joinDate;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
}
