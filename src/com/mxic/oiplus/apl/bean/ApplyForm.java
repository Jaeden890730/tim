package com.mxic.oiplus.apl.bean;

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.au.*;
import com.mxic.oiplus.apl.*;

public class ApplyForm {
  public final static short KEY_LENGTH = 11;

  public ApplyForm() {}

  private String applyNo = null;
  public void setApplyNo(String applyNo) {
    this.applyNo = applyNo;
  }
  public String getApplyNo() {
    return applyNo;
  }

  private String procType = null; // WS or FT
  public void setProcType(String procType) {
    this.procType = procType;
  }
  public String getProcType() {
    return procType;
  }

  private String applyType = "A"; // Apply/Hold/Release
  public void setApplyType(String applyType) {
    this.applyType = applyType;
  }
  public String getApplyType() {
    return applyType;
  }

  // Initial/iNitial2/Process/ReJecting/Reject/Complete
  private String applyStatus = String.valueOf(APLDef.ApplyState.INITIAL);
  public void setApplyStatus(String applyStatus) {
    this.applyStatus = applyStatus;
  }
  public String getApplyStatus() {
    return applyStatus;
  }

  private String batchType = "P"; // Product/Tester
  public void setBatchType(String batchType) {
    this.batchType = batchType;
  }
  public String getBatchType() {
    return batchType;
  }

  private String queryString = null;
  public void setQueryString(String queryString) {
    this.queryString = queryString;
  }
  public String getQueryString() {
    return queryString;
  }

  private String reason = null;
  public void setReason(String reason) {
    this.reason = reason;
  }
  public String getReason() {
    return reason;
  }
  // 20061101 add TaskID/AF_URL for AgentFlow
  private String af_taskid = null;
  public void setAf_taskid(String af_taskid) {
    this.af_taskid = af_taskid;
  }
  public String getAf_taskid() {
    return af_taskid;
  }

  private String af_url = null;
  public void setAf_url(String af_url) {
    this.af_url = af_url;
  }
  public String getAf_url() {
    return af_url;
  }

  // 20061102 add Attachment for attached file name (must reserve the extension name)
  private String attachment = null;
  public void setAttachment(String attachment) {
    this.attachment = attachment;
  }
  public String getAttachment() {
    return attachment;
  }

  private ArrayList data = new ArrayList();
  public Integer[] getData() {
    Integer[] sid = new Integer[data.size()];
    for ( int i = 0; i < data.size(); i++ )
      sid[i] = Integer.valueOf(data.get(i).toString());
    return sid;
  }

  // key(Integer:role|level)->value(Member)
  private HashMap members = new HashMap();

  public APLUser getApplicant() {
    APLUser app = (APLUser) members.get(new Integer(APLDef.Role.APPLICANT|0));
    return app == null ? APLUser.EMPTY : app;
  }
  public void setApplicant(User user) {
    APLUser mm = new APLUser(user);
    members.put(new Integer(mm.role|0), mm);
  }
  public void setApplicant(APLUser user) {
    APLUser mm = new APLUser(user);
    members.put(new Integer(mm.role|0), mm);
  }
  public APLUser getManager() {
    APLUser app = (APLUser) members.get(new Integer(APLDef.Role.MANAGER|0));
    return app == null ? APLUser.EMPTY : app;
  }
  public void setManager(User user) {
    APLUser mm = new APLUser(user, APLDef.Role.MANAGER, (short)0);
    members.put(new Integer(mm.role|0), mm);
  }
  public void setManager(APLUser user) {
    APLUser mm = new APLUser(user, APLDef.Role.MANAGER, (short)0);
    members.put(new Integer(mm.role|0), mm);
  }
  public APLUser getDirector() {
    APLUser app = (APLUser) members.get(new Integer(APLDef.Role.DIRECTOR|0));
    return app == null ? APLUser.EMPTY : app;
  }
  public void setDirector(User user) {
    APLUser mm = new APLUser(user, APLDef.Role.DIRECTOR, (short)0);
    members.put(new Integer(mm.role|0), mm);
  }
  public void setDirector(APLUser user) {
    APLUser mm = new APLUser(user, APLDef.Role.DIRECTOR, (short)0);
    members.put(new Integer(mm.role|0), mm);
  }

  public static ApplyForm getApplyForm(Connection conn, String applyNo) throws Exception {
    ApplyForm form = null;
    String sql = "SELECT APPLY.* FROM AP_APP_MASTER APPLY WHERE APPLY.APP_NO = ? ";
    AP_APP_MASTER[] temp = new AP_APP_MASTER[1];
    if ( applyNo != null ) {
      temp = (AP_APP_MASTER[]) APLUtil.getData(conn, sql.toString(),
                                               AP_APP_MASTER.class, new Object[]{applyNo}).toArray(temp);
      if (temp == null || temp.length == 0 || temp[0] == null)
        return form;
    }
    String[] userEmpNo = new String[] {temp[0].INITIAL_USER, temp[0].PROCESS_USER, temp[0].COMPLETE_USER};
    APLUser[] members = APLUser.getUserByEmpNo(conn, userEmpNo);
    if (temp[0] != null && members.length > 0) {
      form = new ApplyForm();
      form.setApplyNo(applyNo);
      form.setProcType(temp[0].PROCESS_TYPE);
      form.setApplyType(temp[0].APP_TYPE);
      form.setApplyStatus(temp[0].APP_STATUS);
      form.setBatchType(temp[0].BATCH_TYPE);
      form.setQueryString(temp[0].QUERY_STRING);
      form.setReason(temp[0].REASON);
      form.setAf_taskid(temp[0].AF_TASKID);
      form.setAttachment(temp[0].ATTACHMENT);
      for ( int i = 0; i < userEmpNo.length; i++ ) {
        if ( userEmpNo[i] == null || userEmpNo[i].length() == 0 )
          continue;
        if ( userEmpNo[i].trim().equals("?") ) {
          if ( i == 0 )
            form.setApplicant(new APLUser());
          if ( i == 1 )
            form.setManager(new APLUser());
          if ( i == 2 )
            form.setDirector(new APLUser());
          continue;
        }
        for ( int j = 0; j < members.length; j++ ) {
          if ( members[j].getEmpNo().equals(userEmpNo[i].trim()) ) {
            if ( i == 0 )
              form.setApplicant(members[j]);
            if ( i == 1 )
              form.setManager(members[j]);
            if ( i == 2 )
              form.setDirector(members[j]);
          }
        }
      }
      sql = "SELECT APL_SID SID FROM AP_APP_DETAIL WHERE APP_NO = ? ";
      AP_APP_DETAIL[] sid = (AP_APP_DETAIL[]) APLUtil.getData(conn, sql.toString(),
          AP_APP_DETAIL.class, new Object[]{applyNo}).toArray(new AP_APP_DETAIL[0]);
      for ( int i = 0; i < sid.length; i++ ) {
        form.data.add(sid[i].SID);
      }
    }
    return form;
  }
  public static ApplyForm getApplyFormNPI(Connection conn, String applyNo, String appType) throws Exception {
	    ApplyForm form = null;
	    String sql = "";
	    if(appType.equals("NPI")){
	      sql = "SELECT APP_ID APP_NO, APPLICANT INITIAL_USER, STATUS APP_STATUS, 'A' APP_TYPE, substr(APP_ID,1,2) PROCESS_TYPE FROM TDS.NP_WSNPI  WHERE APP_ID = ? ";
	    }else if(appType.equals("TIMFT")){
	      sql = "SELECT DISTINCT APL_NO APP_NO, '99015' INITIAL_USER, 'R' APP_STATUS, 'A' APP_TYPE, 'FT' PROCESS_TYPE FROM AP_APL  WHERE APL_NO = ? ";  
	    }else if(appType.equals("SEMWS")){
	      sql = "SELECT DISTINCT APP_ID APP_NO, APPLICANT INITIAL_USER, DECODE(STATUS,7,'R') APP_STATUS, 'A' APP_TYPE, 'WS' PROCESS_TYPE FROM TS_WSAPL  WHERE APP_ID = ? ";
	    }else if(appType.equals("SEMAVI")){
	      sql = "SELECT APP_ID APP_NO, APPLICANT INITIAL_USER, STATUS APP_STATUS, 'A' APP_TYPE, 'AVI' PROCESS_TYPE FROM TS_AVIAPL  WHERE APP_ID = ? ";
	    }else if(appType.equals("SEMFVI")){
	      sql = "SELECT APP_ID APP_NO, APPLICANT INITIAL_USER, STATUS APP_STATUS, 'A' APP_TYPE, 'FVI' PROCESS_TYPE FROM TS_FVIAPL  WHERE APP_ID = ? ";
	    }else if(appType.equals("SEMMARK")){
	      sql = "SELECT APP_ID APP_NO, APPLICANT INITIAL_USER, STATUS APP_STATUS, 'A' APP_TYPE, 'MARK' PROCESS_TYPE FROM TS_MARKAPL  WHERE APP_ID = ? ";
	    }
	    AP_APP_MASTER[] temp = new AP_APP_MASTER[1];
	    if ( applyNo != null ) {
	      temp = (AP_APP_MASTER[]) APLUtil.getData(conn, sql.toString(),
	                                               AP_APP_MASTER.class, new Object[]{applyNo}).toArray(temp);
	      if (temp == null || temp.length == 0 || temp[0] == null)
	        return form;
	    }
	    String[] userEmpNo = new String[] {temp[0].INITIAL_USER};
	    APLUser[] members = APLUser.getUserByEmpNo(conn, userEmpNo);
	    if (temp[0] != null && members.length > 0) {
	      form = new ApplyForm();
	      form.setApplyNo(applyNo);
	      form.setProcType(temp[0].PROCESS_TYPE);
	      form.setApplyType(temp[0].APP_TYPE);
	      form.setApplyStatus(temp[0].APP_STATUS);
	       for ( int i = 0; i < userEmpNo.length; i++ ) {
	        if ( userEmpNo[i] == null || userEmpNo[i].length() == 0 )
	          continue;
	        if ( userEmpNo[i].trim().equals("?") ) {
	          if ( i == 0 )
	            form.setApplicant(new APLUser());
	          if ( i == 1 )
	            form.setManager(new APLUser());
	          if ( i == 2 )
	            form.setDirector(new APLUser());
	          continue;
	        }
	        for ( int j = 0; j < members.length; j++ ) {
	          if ( members[j].getEmpNo().equals(userEmpNo[i].trim()) ) {
	            if ( i == 0 )
	              form.setApplicant(members[j]);
	            if ( i == 1 )
	              form.setManager(members[j]);
	            if ( i == 2 )
	              form.setDirector(members[j]);
	          }
	        }
	      }
	      sql = "SELECT APL_SID SID FROM AP_APP_DETAIL WHERE APP_NO = ? ";
	      AP_APP_DETAIL[] sid = (AP_APP_DETAIL[]) APLUtil.getData(conn, sql.toString(),
	          AP_APP_DETAIL.class, new Object[]{applyNo}).toArray(new AP_APP_DETAIL[0]);
	      for ( int i = 0; i < sid.length; i++ ) {
	        form.data.add(sid[i].SID);
	      }
	    }
	    return form;
	  }

  public static class AP_APP_MASTER {
    public String APP_NO;
    public String PROCESS_TYPE;
    public String APP_TYPE;
    public String APP_STATUS;
    public String BATCH_TYPE;
    public String INITIAL_USER;
    public java.util.Date INITIAL_TIME;
    public String PROCESS_USER;
    public java.util.Date PROCESS_TIME;
    public String COMPLETE_USER;
    public java.util.Date COMPLETE_TIME;
    public String QUERY_STRING;
    public String REASON;
    public String AF_TASKID;
    public String ATTACHMENT;
  }

  public static class AP_APP_DETAIL {
    public Number SID;
  }
}
