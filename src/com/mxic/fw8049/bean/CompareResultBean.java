package com.mxic.fw8049.bean;

public class CompareResultBean {
	private String product_code;
	private String ncl_form_no;
	private String prod_body;
	private String be_option;	
	private String formno;	
	private String reason;
	private String content;
	private String pending;
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getProd_body() {
		return prod_body;
	}
	public void setProd_body(String product_body) {
		this.prod_body = product_body;
	}
	public String getBe_option() {
		return be_option;
	}
	public void setBe_option(String be_option) {
		this.be_option = be_option;
	}
	public String getNcl_form_no() {
		return ncl_form_no;
	}
	public void setNcl_form_no(String ncl_form_no) {
		this.ncl_form_no = ncl_form_no;
	}
	public String getFormno() {
		return formno;
	}
	public void setFormno(String fromno) {
		this.formno = fromno;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPending() {
		return pending;
	}
	public void setPending(String pending) {
		this.pending = pending;
	}
}
