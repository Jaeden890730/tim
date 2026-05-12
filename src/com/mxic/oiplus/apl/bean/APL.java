package com.mxic.oiplus.apl.bean;

import com.mxic.oiplus.apl.*;
import com.mxic.stoptest.util.StringUtil;

import java.text.SimpleDateFormat;

public class APL {
  public APL(AP_APL apl) {
    this.SID = apl.SID;
    this.procType = apl.PROCESS_TYPE;
    this.prodBody = apl.PRODUCT_BODY;
    this.option = apl.OPTIONS;
    this.packCode = apl.PACKAGE_TYPE;
    this.pinCount = apl.PIN_COUNT;
    this.testMode = apl.TEST_MODE;
    this.testerType = apl.TESTER_TYPE;
    this.vendorNo = apl.VENDOR_NO;
    this.vendorName = apl.VENDOR_NAME;
    this.aplStatus = apl.APL_STATUS;
    this.isexists8049 = apl.ISEXISTS8049;
    this.bodySize = apl.BODY_SIZE;
    this.applyNo = apl.APP_NO;
    this.aplNo = apl.APL_NO;
    this.checked = apl.CHECKED;
    this.pimReleaseDate = apl.PIM_RELEASEDATE;
    this.log_time = apl.LOG_TIME;
    this.site = apl.SITE;
    this.ink = apl.INK;
    this.carrierType = apl.CARRIER_TYPE;
    this.markingSpecNo = apl.MARKING_SPEC_NO;
    this.markingSpecVersion = apl.MARKING_SPEC_VERSION;
  }

  public String toString() {
    char c = ',';
    String str = null;
    String status = null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String log_time_value = sdf.format(log_time);
    
    if (aplStatus.equals("R"))
      status = "Release";
    else if (aplStatus.equals("H"))
      status = "Hold";
    else
      status = "Wait";

    //if (testMode.startsWith("S"))
    if(procType.equals("WS"))
      str = prodBody + c +
            option + c +
            testMode + c +
            testerType + c +
            site + c +
            vendorName + c +
            status + c +
            applyNo + c +
            log_time_value;
      else if(procType.equals("FT"))
        str = prodBody + c +
              option + c +
              pinCount + c +
              packCode + c +
              bodySize + c +
              testMode + c +
              testerType + c +
              vendorName + c +
              status + c +
              StringUtil.NullConvert(isexists8049)+ c +
              applyNo+ c +
              log_time_value;
      else if(procType.equals("AVI"))
          str = vendorName + c +
                prodBody + c +
                option + c +
                ink + c +
                testerType + c +
                status + c +
                applyNo+ c +
                log_time_value;
      else if(procType.equals("FVI"))
          str = vendorName + c + 
        	    pinCount + c +
                packCode + c +
                bodySize + c +
                testerType + c +
                carrierType + c +
                status + c +
                applyNo+ c +
                log_time_value;
      else 
          str = vendorName + c +
                markingSpecNo + c +
                markingSpecVersion + c +
                pinCount + c +
                packCode + c +
                bodySize + c +
                testerType + c +
                status + c +
                applyNo+ c +
                log_time_value;
    return str;
  }

  private Number SID = null;
  public void setSID(Number SID) {
    this.SID = SID;
  }
  public Number getSID() {
    return SID;
  }

  private String procType = null;
  public void setProcType(String procType) {
    this.procType = procType;
  }
  public String getProcType() {
    return procType;
  }

  private String prodBody = null;
  public void setProdBody(String prodBody) {
    this.prodBody = prodBody;
  }
  public String getProdBody() {
    return prodBody;
  }

  private String option = null;
  public void setOption(String option) {
    this.option = option;
  }
  public String getOption() {
    return option;
  }

  private String packCode = null;
  public void setPackCode(String packCode) {
    this.packCode = packCode;
  }
  public String getPackCode() {
    return packCode;
  }

  private String pinCount = null;
  public void setPinCount(String pinCount) {
    this.pinCount = pinCount;
  }
  public String getPinCount() {
    return pinCount;
  }

  private String testMode = null;
  public void setTestMode(String testMode) {
    this.testMode = testMode;
  }
  public String getTestMode() {
    return testMode;
  }

  private String testerType = null;
  public void setTesterType(String testerType) {
    this.testerType = testerType;
  }
  public String getTesterType() {
    return testerType;
  }

  private String vendorNo = null;
  public void setVendorNo(String vendorNo) {
    this.vendorNo = vendorNo;
  }
  public String getVendorNo() {
    return vendorNo;
  }

  private String vendorName = null;
  public void setVendorName(String vendorName) {
    this.vendorName = vendorName;
  }
  public String getVendorName() {
    return vendorName;
  }

  private String aplStatus = String.valueOf(APLDef.AplState.WAIT);
  public void setAplStatu(String status) {
    this.aplStatus = status;
  }
  public String getAplStatus() {
    return aplStatus;
  }
  
  private String isexists8049 = null;
  public void setIsexists8049(String isexists8049) {
    this.isexists8049 = isexists8049;
  }
  public String getIsexists8049() {
    return isexists8049;
  }

  private String bodySize = null;
  public void setBodySize(String bodySize) {
    this.bodySize = bodySize;
  }
  public String getBodySize() {
    return bodySize;
  }
  
  private String site = null;
  public void setSite(String site) {
    this.site = site;
  }
  public String getSite() {
    return site;
  }
  
  private String carrierType = null;
  public void setCarrierType(String carrierType) {
    this.carrierType = carrierType;
  }
  public String getCarrierType() {
    return carrierType;
  }
  
  private String markingSpecNo = null;
  public void setMarkingSpec_no(String markingSpecNo) {
    this.markingSpecNo = markingSpecNo;
  }
  public String getMarkingSpecNo() {
    return markingSpecNo;
  }
  
  private String markingSpecVersion = null;
  public void setMarkingSpecVersion(String markingSpecVersion) {
    this.markingSpecVersion = markingSpecVersion;
  }
  public String getMarkingSpecVersion() {
    return markingSpecVersion;
  }
  
  private String ink = null;
  public void setInk(String ink) {
    this.ink = ink;
  }
  public String getInk() {
    return ink;
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

  private String checked = null;
  public void setChecked(String checked) {
    this.aplNo = checked;
  }
  public String getChecked() {
    if (checked == null)
      return "";
    return checked;
  }

  public java.util.Date pimReleaseDate;

  public java.util.Date getPimReleaseDate() {
    return pimReleaseDate;
  }
  
  public java.util.Date log_time;

  public java.util.Date getLog_time() {
    return log_time;
  }

  public static class AP_APL {
    public Number SID;
    public String PROCESS_TYPE;
    public String PRODUCT_BODY;
    public String OPTIONS;
    public String PACKAGE_TYPE;
    public String PIN_COUNT;
    public String TEST_MODE;
    public String TESTER_TYPE;
    public String VENDOR_NO;
    public String VENDOR_NAME;
    public String APL_STATUS;
    public String BODY_SIZE;
    public String APP_NO;
    public String APL_NO;
    public String CHECKED;  // used in reApply
    public java.sql.Date LOG_TIME;  // used in reApply
    public java.sql.Date PIM_RELEASEDATE;  // used in reApply
    public String SITE;
    public String INK;
    public String CARRIER_TYPE;
    public String CARRIER_DESC;
    public String MARKING_SPEC_NO;
    public String MARKING_SPEC_VERSION;
    public String ISEXISTS8049;
  }
}