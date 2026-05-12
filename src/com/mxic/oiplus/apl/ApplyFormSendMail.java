package com.mxic.oiplus.apl;


import java.sql.*;
import java.util.*;
import java.util.Date;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.rs.TDSResource;
import com.mxic.oiplus.util.MD5;
import com.mxic.oiplus.util.SendMail;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.oiplus.util.TDSLogger;
/**
 *
 * <p>Title: 表單寄信</p>
 * <p>Description: MAIL的內容以及連結回表單的URL路徑</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ApplyFormSendMail {
  public final String LINKCODE_TABLE = "REQ_APPLYFORM";
  public final String MAIL_ADRESS = "@mxic.com.tw";
//  public final String Default_URL = "/req/ShowPageAction.do?key=";
  //public final String Default_URL = "/login.jsp?redirectUrl=";
  public final String Default_URL = "/Login/login.jsp?redirectUrl=";
  public final String MAIL_HEAD = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\"></head><body>Dear Sir,<br>";
  public final String MAIL_HEAD_LINKCODE = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=big5\"></head><body>Dear Sir,<br>";
  private String mailFrom = "GPRSAdmin@mxic.com.tw";
  private String mailSubject = "";
  private String mailBody = "";
  private String empId = "";
  private String mailTo;
  private String mailCC;

  public String getMailCC() {
	return mailCC;
}

public void setMailCC(String mailCC) {
	this.mailCC = mailCC;
}

public ApplyFormSendMail() {
  }

  public void setMailSubject(String mailSubject) {
  //  this.mailSubject = "ANPI 作業及會簽流程系統–" + mailSubject;
    this.mailSubject =  mailSubject;
  }

  public void setMailBody(String mailBody) {
    this.mailBody = mailBody;
  }

  public void setEmpId(String empId) {
    this.empId = empId;
  }
  public String getMailSubject() {
	  return mailSubject;
	  }
  public String getMailTo() {
	    return mailTo;
	  }

  public void setMailTo(String mailTo) {
	this.mailTo = mailTo;
}

public String getMailBody() {
    return mailBody;
  }
  /**
   * 產生信件內容
   * @param contextPath
   * @param appId
   * @param con
   * @throws java.lang.Exception
   */
  public void createMailBody( String empMail) throws
      Exception { 
	Properties properties = TDSResource.getProperties("SEM");
	 String sysAdmin = properties.getProperty("SysAdmin");
  //  String sysAdmin = BARConfig.getMainPropKeyValue("SysAdmin");
    if (sysAdmin != null && sysAdmin.length()>0)
      sysAdmin = StringUtil.unicodeToBig5(sysAdmin);
    mailTo = empMail; //get mail adress
    mailBody = 
         this.mailSubject +
        "<br><br>使用上如有任何問題歡迎洽詢：<br>系統負責人-"
        + sysAdmin + "</body></html>";
  }
  /**
   * 產生信件內容
   * @param contextPath
   * @param appId
   * @param con
   * @throws java.lang.Exception
   */
	public void createMailBody(String contextPath, String appId, Connection con) throws Exception {
		String  url = getredirectUrl(appId, contextPath);//更改為登入後進入表單
		url =  getAppIdUrl(url ,appId);
		//String url = getUrl(empId, appId, contextPath, con); //get url
		
		Properties properties = TDSResource.getProperties("TDS");
		String sysAdmin = properties.getProperty("SysAdmin");
		
		if (sysAdmin != null && sysAdmin.length()>0)
			sysAdmin = StringUtil.unicodeToBig5(sysAdmin);
		
		//mailTo = getNoteId(empId, con); //get mail adress
		
		mailBody = MAIL_HEAD_LINKCODE + url + this.mailSubject +
        //"</A><br><br>使用上如有任何問題歡迎洽詢：<br>系統負責人-"
        "<br><br>使用上如有任何問題歡迎洽詢：<br>系統負責人-" + sysAdmin + "</body></html>";
	}

	public void createMailBody(String contextPath, String appId, Connection con, String content) throws Exception {
		String  url = getredirectUrl(appId, contextPath);//更改為登入後進入表單
		url =  getAppIdUrl(url ,appId);
		//String url = getUrl(empId, appId, contextPath, con); //get url
		
		Properties properties = TDSResource.getProperties("TDS");
		String sysAdmin = properties.getProperty("SysAdmin");
		
		if (sysAdmin != null && sysAdmin.length()>0)
			sysAdmin = StringUtil.unicodeToBig5(sysAdmin);
		
		//mailTo = getNoteId(empId, con); //get mail adress
		
		mailBody = MAIL_HEAD_LINKCODE + url + this.mailSubject; 
		if(content != null && !content.equals(""))
			mailBody += "<br>" + content;
        //"</A><br><br>使用上如有任何問題歡迎洽詢：<br>系統負責人-"
		mailBody += "<br><br>使用上如有任何問題歡迎洽詢：<br>系統負責人-" + sysAdmin + "</body></html>";
	}
	
  public void createLoginMailBody(String url) throws Exception {

	Properties properties = TDSResource.getProperties("SEM");
	 String sysAdmin = properties.getProperty("SysAdmin");
	if (sysAdmin != null && sysAdmin.length()>0)
	  sysAdmin = StringUtil.unicodeToBig5(sysAdmin);
	mailBody = MAIL_HEAD_LINKCODE + url +
	     this.mailSubject +
	    "<br><br>使用上如有任何問題歡迎洽詢：<br>系統負責人-"
	    + sysAdmin + "</body></html>";
}
  public void createMailBodyByWafer(String contextPath, String appId, Connection con) throws
  Exception {

	String url = getUrl(empId, appId, contextPath, con); //get url
	Properties properties = TDSResource.getProperties("SEM");
	 String sysAdmin = properties.getProperty("SysAdmin");
	if (sysAdmin != null && sysAdmin.length()>0)
	  sysAdmin = StringUtil.unicodeToBig5(sysAdmin);
	mailBody = MAIL_HEAD_LINKCODE + url +
	    "'>" + this.mailSubject +
	    "</A><br><br>使用上如有任何問題歡迎洽詢：<br>系統負責人-"
	    + sysAdmin + "</body></html>";
}
  /**
   * 寄信
   * @throws java.lang.Exception
   */
  public void sendMail() throws Exception {
	  Properties properties = TDSResource.getProperties("TDS");
	  String sendMail = properties.getProperty("mailserver_send");
	  
	  settestString();
	  TDSLogger.println("mailTo: " + mailTo);
	  TDSLogger.println("mailSubject: " + mailSubject);
	  TDSLogger.println("mailBody: " + mailBody);
	  TDSLogger.println("sendMail switch: " + sendMail);
      
      if(sendMail.equals("on")){
    	  Calendar startTime = Calendar.getInstance();
    	  startTime.setTime(new Date(System.currentTimeMillis()));

    	  SendMail.sendHtml(mailTo, mailFrom, mailSubject, mailBody);
    	  Calendar endTime = Calendar.getInstance();
    	  endTime.setTime(new Date(System.currentTimeMillis()));
    	  
    	  TDSLogger.println("sendMail() - Total: " +  + ((endTime.getTimeInMillis() - startTime.getTimeInMillis())/1000));
      }
  }
  
  public void settestString(){
	  String testLetter="";
		Properties properties = TDSResource.getProperties("TDS");
		testLetter = properties.getProperty("mailSubject");
	 // testLetter=BARConfig.getMainPropKeyValue("mailSubject");
	  if(testLetter ==null)
		  testLetter ="";
	  else
		  testLetter = StringUtil.unicodeToBig5(testLetter);
	  this.setMailSubject(testLetter+this.getMailSubject());
  }
	 //url LINKCODE
  public String getAppIdUrl(String url,String ca_number){
		  String title="<A HREF='http://";
		  title+=url+"'>";
		  title+=ca_number;
		  title+= "</A>";
		  
		  return title;
	  }
  
  public String getredirectUrl( String appId, String contextPath) throws Exception {
	    String url = new String();
	    if (appId == null || appId.equals("")) 
	    	throw new Exception("單號錯誤，無法產生連結。");
	    
	    url = "/apl/APLAppSignAction.do?act=query&app_no=" + appId;
	    url = java.net.URLEncoder.encode(url, "UTF-8");
	    url = contextPath + Default_URL+ url;
	  
	    return url;
	  }
  /**
   * 寄信有加cc
   * @throws java.lang.Exception
   */
  public void sendMailCC() throws Exception {
	  Properties properties = TDSResource.getProperties("TDS");
	  String sendMail = properties.getProperty("mailserver_send");
	  settestString();
	  TDSLogger.println("mailTo: " + mailTo);
	  TDSLogger.println("mailCC:" + mailCC);
	  TDSLogger.println("mailSubject: " + mailSubject);
	  TDSLogger.println("mailBody: " + mailBody);
	  TDSLogger.println("sendMail switch: " + sendMail);
      
      if(sendMail.equals("on")){
    	  Calendar startTime = Calendar.getInstance();
    	  startTime.setTime(new Date(System.currentTimeMillis()));

    	  SendMail.sendCCHTML(mailTo,this.mailCC , mailFrom, mailSubject, mailBody);  	   
    	  Calendar endTime = Calendar.getInstance();
    	  endTime.setTime(new Date(System.currentTimeMillis()));
    	  
    	  TDSLogger.println("sendMailCC() - Total: " + ((endTime.getTimeInMillis() - startTime.getTimeInMillis())/1000));
	  }else {
		  System.out.println("mailTo: " + mailTo);
		  System.out.println("mailCC:" + mailCC);
		  System.out.println("mailSubject: " + mailSubject);
		  System.out.println("mailBody: " + mailBody);
		  System.out.println("sendMail switch: " + sendMail);  
	  }
  }
  /**
   * 儲存mail資訊
   * @throws Exception
   */
  public void saveMailList() throws Exception {
	  Properties properties = TDSResource.getProperties("TDS");
	  String sendMail = properties.getProperty("mailserver_send");
	  settestString();
	  
	  TDSLogger.println("mailTo: " + mailTo);
	  TDSLogger.println("mailCC:" + mailCC);
	  TDSLogger.println("mailSubject: " + mailSubject);
	  TDSLogger.println("mailBody: " + mailBody);
	  TDSLogger.println("sendMail switch: " + sendMail);

      if(sendMail.equals("on")){
    	  Calendar startTime = Calendar.getInstance();
    	  startTime.setTime(new Date(System.currentTimeMillis()));

    	  SendMail.sendHtml(mailTo, mailFrom, mailSubject, mailBody);
    	  
    	  Calendar endTime = Calendar.getInstance();
    	  endTime.setTime(new Date(System.currentTimeMillis()));
    	  
    	  TDSLogger.println("saveMailList() - Total: " +  + ((endTime.getTimeInMillis() - startTime.getTimeInMillis())/1000));
      }
      /*
	    if (mailBody.indexOf("localhost:")>-1) {
	      System.out.println("mailto:" + mailTo);
	      System.out.println("mailSubject:" + mailSubject);
	      System.out.println(mailBody);
	    }
	    else {
	      SendMail.sendHtml(mailTo, mailFrom, mailSubject, mailBody);
	    }
	  */ 
  }
  /**
   * 取得連結路徑
   * @param empNo
   * @param appId
   * @param contextPath
   * @param con
   * @return
   */
  public String getUrl(String empNo, String appId, String contextPath,
                        Connection con) {
    String url = new String();
    try {
      String linkCode = getLinkCode(empNo, appId, con);
      if (linkCode == null || linkCode.length() == 0) {
        linkCode = MD5.getMD5String(empNo + appId);
        insertLinkCode(empNo, appId, linkCode, con);
      }
      url = contextPath + Default_URL + linkCode;
    }
    catch (Exception e) {
      TDSLogger.println(e);
    }
    return url;
  }
  /**
   * 取得由userid及單號所產生的md5編碼
   * @param userId
   * @param appId
   * @param con
   * @return
   * @throws java.lang.Exception
   */
  public String getLinkCode(String userId, String appId, Connection con) throws
      Exception {
    String linkCode = new String();
    String sql =
        "select t.link_code from "+this.LINKCODE_TABLE+" t\n" +
        "where t.current_user='" + userId + "'\n" +
        "and t.app_id='" + appId + "'";
    try {
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        linkCode = rs.getString("LINK_CODE");
      }
    }
    catch (Exception e) {
    	TDSLogger.println(e);
    }
    return linkCode;
  }
  /**
   * 儲存md5編碼
   * @param userId
   * @param appId
   * @param linkCode
   * @param con
   * @return
   * @throws java.lang.Exception
   */
  private boolean insertLinkCode(String userId, String appId,
                                 String linkCode,
                                 Connection con) throws Exception {
    String sql = "insert into "+this.LINKCODE_TABLE+"\n" +
        " (LINK_CODE,CURRENT_USER,APP_ID)\n" +
        "values (?, ?, ?)";
    boolean result = false;
    PreparedStatement ps = con.prepareStatement(sql);
    int i = 1;
    ps.setString(i++, linkCode);
    ps.setString(i++, userId);
    ps.setString(i++, appId);
    i = ps.executeUpdate();
    ps.close();
    result = true;
    return result;
  }
  /**
   * 取得noteid
   * @param empNo
   * @return
   */
  public String getNoteId(String empNo) {
    Connection con = null;
    String result = new String();
    try {
      con = DBConnection.getConnection();
      result = getNoteId(empNo, con);
    }
    catch (Exception ex) {
      DBConnection.rollback(con);
      TDSLogger.println(ex);
    }
    finally {
      DBConnection.close(con);
    }
    return result;
  }
  /**
   * 取得noteid
   * @param empNo
   * @param con
   * @return
   */
  public String getNoteId(String empNo, Connection con) {
    String noteId = new String();
    String sql = "select a.emp_name_e		\n" +
        		"from ba_mxic_emp a			\n" +
        		"where a.emp_no in ('" + empNo + "')";
    try {
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        noteId = rs.getString("emp_name_e") + MAIL_ADRESS;
      }
      ps.close();
      rs.close();
    }
    catch (Exception e) {
    	TDSLogger.println(e);
    }
    return noteId;
  }

}