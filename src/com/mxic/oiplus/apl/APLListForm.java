package com.mxic.oiplus.apl;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

public class APLListForm extends ActionForm {

  private String operatorId = null;
  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }
  public String getOperatorId() {
    return operatorId;
  }

  private String procType = null;
  public void setProcType(String procType) {
    this.procType = procType;
  }
  public String getProcType() {
    return procType;
  }

  private String batchType = null;
  public void setBatchType(String batchType) {
    this.batchType = batchType;
  }
  public String getBatchType() {
    return batchType;
  }

  private String[] SID = null;
  public void setSID(String[] SID) {
    this.SID = SID;
  }
  public String[] getSID() {
    return SID;
  }

  private String remarks = null;
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
  public String getRemarks() {
    return remarks;
  }

  private FormFile file = null;
  public void setFile(FormFile file) {
    this.file = file;
  }
  public FormFile getFile() {
    return file;
  }

  private String fileName = null;
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getFileName() {
    return fileName;
  }

  private String actionType = "applyNew";
  public void setActionType(String actionType) {
    this.actionType = actionType;
  }
  public String getActionType() {
    return actionType;
  }

  private String applyNo = null;
  public void setApplyNo(String applyNo) {
    this.applyNo = applyNo;
  }
  public String getApplyNo() {
    return applyNo;
  }

  private String aplNo = null;
  public void setAplNo(String aplNo) {
    this.aplNo = aplNo;
  }
  public String getAplNo() {
    return aplNo;
  }

  private String reapply = null;
  public void setReapply(String reapply) {
    this.reapply = reapply;
  }
  public String getReapply() {
    return reapply;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }
}