package com.mxic.oiplus.eif;

import java.sql.Date;

public class TFCoverPageBean {
	private String product_body;
	private String brand;
	private Integer version;
	private String applicant;
	private String approve_no;
	private Date approve_date;
	private String remark;
	private String sponsor;
	/**
	 * @return the applicant
	 */
	public String getApplicant() {
		return applicant;
	}
	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	/**
	 * @return the approve_date
	 */
	public Date getApprove_date() {
		return approve_date;
	}
	/**
	 * @param approve_date the approve_date to set
	 */
	public void setApprove_date(Date approve_date) {
		this.approve_date = approve_date;
	}
	/**
	 * @return the approve_no
	 */
	public String getApprove_no() {
		return approve_no;
	}
	/**
	 * @param approve_no the approve_no to set
	 */
	public void setApprove_no(String approve_no) {
		this.approve_no = approve_no;
	}
	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}
	/**
	 * @param brand the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}
	/**
	 * @return the product_body
	 */
	public String getProduct_body() {
		return product_body;
	}
	/**
	 * @param product_body the product_body to set
	 */
	public void setProduct_body(String product_body) {
		this.product_body = product_body;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the sponsor
	 */
	public String getSponsor() {
		return sponsor;
	}
	/**
	 * @param sponsor the sponsor to set
	 */
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	/**
	 * @return the version
	 */
	public Number getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}
}
