package com.peer.bean;

/**
 * 查询结果BEAN
 * 
 * @author zhangzg
 * 
 */
public class SearchBean {
	private String searchtype;
	private String searchname;
	private String callbacklabel;
	private static SearchBean searchutil = null;

	private SearchBean() {
	}

	public static SearchBean getInstance() {
		if (searchutil == null) {
			searchutil = new SearchBean();
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
