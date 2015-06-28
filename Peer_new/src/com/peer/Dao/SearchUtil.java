package com.peer.Dao;

public class SearchUtil {
	private String searchtype;
	private String searchname;
	private String callbacklabel;
	private static SearchUtil searchutil=null;
	private SearchUtil(){}
	public static SearchUtil getInstance(){
		if(searchutil==null){
			searchutil=new SearchUtil();
		}
		return searchutil;
	}
	
	public String getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}
	public String getSearchname() {
		return searchname;
	}
	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}
	public String getCallbacklabel() {
		return callbacklabel;
	}
	public void setCallbacklabel(String callbacklabel) {
		this.callbacklabel = callbacklabel;
	}

}
