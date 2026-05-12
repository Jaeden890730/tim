package com.mxic.fw8049.action;

import org.apache.struts.action.ActionForm;

public class BaseActionForm extends ActionForm {
	protected String sid;	
	protected String product_body;
	protected String brand;
	protected String version;
	protected String status;
	protected String status_display;
	protected String customer_no;
	protected String creator;
	protected String activeflag;
	protected String createtime;
	protected String updatetime;
	protected String showTime;

	protected String sponsor_1;
	protected String sponsor_2;
	
	
	
	public String getSponsor_1() {
		return sponsor_1;
	}

	public void setSponsor_1(String sponsor_1) {
		this.sponsor_1 = sponsor_1;
	}

	public String getSponsor_2() {
		return sponsor_2;
	}

	public void setSponsor_2(String sponsor_2) {
		this.sponsor_2 = sponsor_2;
	}

	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String date) {
		this.createtime = date;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	protected String loginUserName;
	protected boolean isAuthority;
	

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getProduct_body() {
		return product_body;
	}

	public void setProduct_body(String product_body) {
		this.product_body = product_body;
	}

	public String getCustomer_no() {
		return customer_no;
	}

	public void setCustomer_no(String customer_no) {
		this.customer_no = customer_no;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public boolean getIsAuthority() {
		return isAuthority;
	}

	public void setIsAuthority(boolean isAuthority) {
		this.isAuthority = isAuthority;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getStatus_display() {
		return status_display;
	}

	public void setStatus_display(String status_display) {
		this.status_display = status_display;
	}

	public String getShowTime() {
		return showTime;
	}

	public String getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(String activeflag) {
		this.activeflag = activeflag;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
}
