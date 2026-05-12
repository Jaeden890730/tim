package com.mxic.oiplus.common;
import org.apache.struts.action.ActionForm;

public class Conv2ExcelForm extends ActionForm
{
  private String reportResult;
  private String rptResult;
  private String action;
  private String fileName;
  private String sid;
  private String type;

  public void setAction(String action)
  {
    this.action = action;
  }

  public String getAction()
  {
    return this.action;
  }

  public String getReportResult() {
    return reportResult;
  }

  public void setReportResult(String reportResult) {
    this.reportResult = reportResult;
  }

  public String getRptResult() {
    return rptResult;
  }

  public void setRptResult(String rptResult) {
    this.rptResult = rptResult;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
