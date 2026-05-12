package com.mxic.fw8049.bean;

public class Fw8049maintainenceBean {
	private String tag;	
	private String product_code;	
	private String form_no;
	private String reason_details;
	private String annotation_issues;
	
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getForm_no() {
		return form_no;
	}
	public void setForm_no(String form_no) {
		this.form_no = form_no;
	}
	public String getReason_details() {
		return reason_details;
	}
	public void setReason_details(String reason_details) {
		this.reason_details = reason_details;
	}
	public String getAnnotation_issues() {
		return annotation_issues;
	}
	public void setAnnotation_issues(String annotation_issues) {
		this.annotation_issues = annotation_issues;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

}
