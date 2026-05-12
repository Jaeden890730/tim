package com.mxic.fw8049.action;

import java.util.ArrayList;

import com.mxic.fw8049.dao.Fw8049mationDao;


public class Fw8049MaintainenceActionForm extends BaseActionForm {

	private String actionType;
	private String productCode;
	private String formNo;
	private String reasonDetails;
	private String annotationIssues;
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getFormNo() {
		return formNo;
	}
	public void setFormNo(String formNo) {
		this.formNo = formNo;
	}
	public String getReasonDetails() {
		return reasonDetails;
	}
	public void setReasonDetails(String reasonDetails) {
		this.reasonDetails = reasonDetails;
	}
	public String getAnnotationIssues() {
		return annotationIssues;
	}
	public void setAnnotationIssues(String annotationIssues) {
		this.annotationIssues = annotationIssues;
	}
	public ArrayList getFwBasicListTx(){
		return Fw8049mationDao.getFwBasicListTx(this.getSid());
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
}
