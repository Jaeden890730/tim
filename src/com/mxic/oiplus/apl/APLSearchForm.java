package com.mxic.oiplus.apl;

import java.util.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.util.CalendarUtil;

public class APLSearchForm extends ActionForm {

  private java.sql.Date nowDate = new java.sql.Date(System.currentTimeMillis());
  private CalendarUtil cd = new CalendarUtil();
  private String currentDate = nowDate.toString();
  private String beforeDate = cd.addDay(-7);
  private String startTime = beforeDate;
  private String endTime = currentDate;

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
  public String getStartTime() {
    return startTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
  public String getEndTime() {
    return endTime;
  }

  private HashMap search = new HashMap();
  public HashMap getSearch() {
    return search;
  }

  public static HashMap getSearch(String[] queryItems) { // query item : COULMN=VALUE
    HashMap wheres = new HashMap(queryItems.length * 4);
    for (int i = 0; i < queryItems.length; i++) {
      String key = queryItems[i].substring(0, queryItems[i].indexOf('='));
      String value = queryItems[i].substring(key.length() + 1);
      if (!wheres.containsKey(key))
        wheres.put(key, new ArrayList());
      ((ArrayList) wheres.get(key)).add(value);
    }
    for (Iterator iter = wheres.keySet().iterator(); iter.hasNext(); ) {
      String key = (String) iter.next();
      ArrayList temp = (ArrayList) wheres.get(key);
      wheres.put(key, temp.size() == 1 ? temp.get(0) : (String[]) temp.toArray(new String[0]));
    }
    return wheres;
  }

  public boolean isValid() {
    search.clear();
    if ((dNull(APLType).length() > 0) && (APLType.equals("AVI"))) procType = "AVI";
    if ((dNull(APLType).length() > 0) && (APLType.equals("FVI"))) procType = "FVI";
    if ((dNull(APLType).length() > 0) && (APLType.equals("MARK"))) procType = "MARK";
    if (dNull(procType).length() > 0) search.put("PROCESS_TYPE", procType);
    if (dNull(prodBody).length() > 0) search.put("PRODUCT_BODY", prodBody);
    if (dNull(option).length() > 0) search.put("OPTIONS", option);
    if (dNull(packCode).length() > 0) search.put("PACKAGE_TYPE", packCode);
    if (dNull(pinCount).length() > 0) search.put("PIN_COUNT", pinCount);
    if (join(testMode, ",").length() > 0) search.put("TEST_MODE", testMode);
    if (dNull(testerType).length() > 0) search.put("TESTER_TYPE", testerType);
    if ( join(vendorNo, ",").length() > 0 ) search.put("VENDOR_NO", vendorNo);
    if (dNull(site).length() > 0) search.put("SITE", site);
    if (dNull(bodySize).length() > 0) search.put("BODY_SIZE", bodySize);
    if (dNull(markingSpecNo).length() > 0) search.put("MARKING_SPEC_NO", markingSpecNo);
    if (dNull(markingSpecVersion).length() > 0) search.put("MARKING_SPEC_VERSION", markingSpecVersion);
    if (dNull(carrierType).length() > 0) search.put("CARRIER_TYPE", carrierType);
    if (dNull(ink).length() > 0) search.put("INK", ink);
    if (dNull(aplStatus).length() > 0) search.put("APL_STATUS", aplStatus);
    if (dNull(issueNumber).length() > 0) search.put("APP_NO", issueNumber);
    if (dNull(issueStatus).length() > 0) search.put("APP_STATUS", issueStatus);
    if (dNull(startTime).length() > 0) search.put("START_TIME", startTime);
    if (dNull(endTime).length() > 0) search.put("END_TIME", endTime);
    if (dNull(checkTime).length() > 0) search.put("CHECK_TIME", checkTime);
    return search.size() > 0;
  }

  public String dNull(Object value) {
    return value == null ? "" : value.toString().trim();
  }

  public String join(String[] values, String connector) {
    if (values == null || values.length == 0)
      return "";
    StringBuffer sb = new StringBuffer(values.length << 3);
    for (int i = 0; i < values.length; i++) {
      if ( dNull(values[i]).length() == 0 )
        continue;
      sb.append( values[i].trim() + ( i < values.length - 1 ? connector : "" ) );
    }
    return sb.toString();
  }

  private String searchTarget = "data";
  public void setSearchTarget(String searchTarget) {
    this.searchTarget = searchTarget;
  }
  public String getSearchTarget() {
    return searchTarget;
  }

  private String searchOption = "query";
  public void setSearchOption(String searchOption) {
    this.searchOption = searchOption;
  }
  public String getSearchOption() {
    return searchOption;
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

  private String[] testMode = null;
  public void setTestMode(String[] testMode) {
    this.testMode = testMode;
  }
  public String[] getTestMode() {
    return testMode;
  }

  private String testerType = null;
  public void setTesterType(String testerType) {
    this.testerType = testerType;
  }
  public String getTesterType() {
    return testerType;
  }

  private String[] vendorNo = null;
  public void setVendorNo(String[] vendorNo) {
    this.vendorNo = vendorNo;
  }
  public String[] getVendorNo() {
    return vendorNo;
  }

  private String aplStatus = null;
  public void setAplStatus(String status) {
    this.aplStatus = status;
  }
  public String getAplStatus() {
    return aplStatus;
  }

  private String issueNumber = null;
  public void setIssueNumber(String issueNumber) {
    this.issueNumber = issueNumber;
  }
  public String getIssueNumber() {
    return issueNumber;
  }

  private String issueStatus = null;
  public void setIssueStatus(String issueStatus) {
    this.issueStatus = issueStatus;
  }
  public String getIssueStatus() {
    return issueStatus;
  }

  private String checkTime = null;
  public void setCheckTime(String checkTime) {
    this.checkTime = checkTime;
  }
  public String getCheckTime() {
    return checkTime;
  }
  
  private String APLType = null;
  public void setAPLType(String APLType) {
    this.APLType = APLType;
  }
  public String getAPLType() {
    return APLType;
  }
  
  private String site = null;
  public void setSite(String site) {
    this.site = site;
  }
  public String getSite() {
    return site;
  }
  
  private String bodySize = null;
  public void setBodySize(String bodySize) {
    this.bodySize = bodySize;
  }
  public String getBodySize() {
    return bodySize;
  }
  
  private String markingSpecNo = null;
  public void setMarkingSpecNo(String markingSpecNo) {
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
  
  private String carrierType = null;
  public void setCarrierType(String carrierType) {
    this.carrierType = carrierType;
  }
  public String getCarrierType() {
    return carrierType;
  }
  
  private String ink = null;
  public void setInk(String ink) {
    this.ink = ink;
  }
  public String getInk() {
    return ink;
  }
}
