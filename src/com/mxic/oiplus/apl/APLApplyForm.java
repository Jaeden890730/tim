package com.mxic.oiplus.apl;

import org.apache.struts.action.*;

public class APLApplyForm extends ActionForm {
  private String applyNo = null;
  public void setApplyNo(String applyNo) {
    this.applyNo = applyNo;
  }
  public String getApplyNo() {
    return applyNo;
  }

  private String actionType = "applyNew";
  public void setActionType(String actionType) {
    this.actionType = actionType;
  }
  public String getActionType() {
    return actionType;
  }

  private String manager;
  public String getManager() {
    return manager;
  }
  public void setManager(String manager) {
    this.manager = manager;
  }

  private String director;
  public String getDirector() {
    return director;
  }
  public void setDirector(String director) {
    this.director = director;
  }

  private String counterSignManager;
  public String getCounterSignManager() {
    return counterSignManager;
  }
  public void setCounterSignManager(String manager) {
    this.counterSignManager = manager;
  }

  private String counterSignDirector;
  public String getCounterSignDirector() {
    return counterSignDirector;
  }
  public void setCounterSignDirector(String director) {
    this.counterSignDirector = director;
  }

  private String remarks = null;
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
  public String getRemarks() {
    return remarks;
  }
}