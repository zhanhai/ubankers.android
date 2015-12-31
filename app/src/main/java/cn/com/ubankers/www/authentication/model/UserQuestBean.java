package cn.com.ubankers.www.authentication.model;

public class UserQuestBean {
	
	private int[] tags;
	private String[] stateSqlIn;
	private String userRole;
	private	int currentPage;
	private	int pageCount;
	public int[] getTags() {
		return tags;
	}
	public void setTags(int tags[]) {
		this.tags = tags;
	}
	public String[] getStateSqlIn() {
		return stateSqlIn;
	}
	public void setStateSqlIn(String[] stateSqlIn) {
		this.stateSqlIn = stateSqlIn;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
   
	public UserQuestBean( int[] tags, String[] stateSqlIn , String userRole,int currentPage, int pageCount) {
		// TODO Auto-generated constructor stub
		this.tags=tags;
		this.stateSqlIn=stateSqlIn;
		this.userRole=userRole;
		this.currentPage=currentPage;
		this.pageCount=pageCount;
	}

}
