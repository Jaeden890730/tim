package com.mxic.oiplus.eif;

/**
 * <p>Title:EifAFSyncAPLStatus.java</p>
 * <p>Description: Used to Sync APL Apply Status with AgentFlow Status</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: MXIC</p>
 * <p>Author: Robin Mao</p>
 * @author Robin Mao
 * @version 1.0
 */

import java.sql.*;
import java.util.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.apl.*;
import com.mxic.oiplus.apl.bean.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
//import mic.af.aplrms.*;

public class EifAFSyncAPLStatus extends Action {

  static private Connection conn = null;
  //static APLManager aplManager = null;
  static int WS_INHOUSE = 1;
  static int FT_INHOUSE = 2;
  static int WS_SUBCON  = 4;
  static int FT_SUBCON  = 8;
  static int AVI  = 16;
  static int FVI  = 32;
  static int MARK  = 64;


  public static void main(String[] args) {
	  /*
    String apl;
    ArrayList APLList = getProcessedAPL();

    String AFServerIP = (String) TDSResource.getProperties("APL").get("af_server_ip");
    aplManager = APLManager.getInstance(AFServerIP);
    if (!aplManager.isAlive()) {
      TDSLogger.println("AgentFlow Server is not alive now !!");
      return;
    }

    if (APLList == null) {
      TDSLogger.println("APL Status Verify: Nothing to do !!");
      aplManager.destroy();
      return;
    }
    Iterator i = APLList.iterator();
    TaskProgress tp = null;
    while (i.hasNext()) {
      apl = (String) i.next();
      String id_list[] = apl.split(",");

      // if apply form is just filled by user
      if (id_list[1].equals("null"))
        continue;

      TDSLogger.println("APL Status Checking : APP_NO=" + id_list[0] +
                        ", AF_TASKID=" + id_list[1]);
      try {
        tp = aplManager.getApplyStatus(id_list[1]);
        TDSLogger.println("APL Status get TaskProgress...");
        updateAppMaster(tp, id_list[0], id_list[2], id_list[3]);
      }
      catch (Exception ex) {
        TDSLogger.println("Fail to get APL Status : APP_NO=" + id_list[0] +
                          ", AF_TASKID=" + id_list[1]);
      }
      finally {
      }
      tp = null;
    }
    aplManager.destroy();
    APLList = null;
    aplManager = null;
    return;
    	*/
  }

  // get in-processed Apply List
  // 由 Ap_App_Master 抓資料，只抓撰寫中、處理中、重送中的資料，
  public static ArrayList getProcessedAPL() {
    String sql;
    ArrayList vl = null;
    Connection conn = null;
    String brand;

    try {
      conn = DBConnection.getConnection();
      sql = "SELECT APP_NO,AF_TASKID,INITIAL_USER,PROCESS_TYPE FROM AP_APP_MASTER " +
          "WHERE APP_STATUS IN ('I','N','P') ";

      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        if (vl == null)
          vl = new ArrayList();
        vl.add(rs.getString("APP_NO") + "," + rs.getString("AF_TASKID") + "," + rs.getString("INITIAL_USER")+ "," + rs.getString("PROCESS_TYPE"));
      }
      rs.close();
      rs = null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
    }
    return vl;
  }

  // update status of Apply Master data with AgentFlow status
  /*
  static boolean updateAppMaster(TaskProgress tp, String App_No, String empNo, String Process) {
    boolean result = false;
    ArrayList wheres2 = new ArrayList();
    java.sql.Timestamp ts = new java.sql.Timestamp(tp.getStartTime());
    String taskStatusName = tp.getTaskStatusName().trim();
    String formState = tp.getFormState().trim();
    String gateState = tp.getGateName().trim();

    TDSLogger.println("Update Application Master ...");
    try {
      conn = DBConnection.getConnection();
      if (formState.equals("作廢")) {
        conn.setAutoCommit(false);
        String sql = "DELETE FROM AP_APP_DETAIL WHERE APP_NO = ?";
        wheres2.add(App_No);
        int cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());

        sql = "DELETE FROM AP_APP_MASTER WHERE APP_NO = ?";
        cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());
        conn.commit();
        conn.setAutoCommit(true);
      }
      else if (formState.equals("申請者送件")) {
        // 申請單已送件，可以拿到第一層主管名稱，APP Status = 會簽中
        String sql =
            "UPDATE AP_APP_MASTER SET APP_STATUS = ?, PROCESS_USER = ?  WHERE APP_NO = ?";

        wheres2.add("P");
        wheres2.add(tp.getExecutorID());
        wheres2.add(App_No);
        int cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());
        // --- if error should write log here
      }
      else if (gateState.equals("退件處理")||
               formState.equals("第一層主管退件")||
               formState.equals("第二層主管退件")||
               formState.equals("平行簽核人員退件")) {
        // 申請單退件，APP Status = 退件中，是否退件或重送，由 user 在 jsp 中決定
        TDSLogger.println("Reject: " + App_No);
        do_reject(tp, App_No, empNo);
      }
      else if (formState.equals("第一層主管核准")) {
        // 第一層主管已核准，可以拿到第二層主管名稱，APP Status = 會簽中，可以設定簽核結束時間
        String sql = "UPDATE AP_APP_MASTER SET APP_STATUS = ?, COMPLETE_USER = ?, PROCESS_TIME = ? WHERE APP_NO = ?";

        wheres2.add("P");
        wheres2.add(tp.getExecutorID());
        wheres2.add(ts);
        wheres2.add(App_No);
        int cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());
      }
      else if (formState.equals("觸發平簽子流程")) {
        // 第二層主管已核准，APP Status = 會簽中，可以設定簽核結束時間
        String sql =
            "UPDATE AP_APP_MASTER SET APP_STATUS = ?, COMPLETE_TIME = ? WHERE APP_NO = ?";

        wheres2.add("P");
        wheres2.add(ts);
        wheres2.add(App_No);
        int cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());
      }
      else if (formState.equals("所有平行簽核人員核准")) {
        // 平行簽的人都簽完了，APP Status = 已完成
        do_release(tp, App_No, empNo, Process);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }

    return result;
  }
	*/
  
  
  // just update APP_STATUS to "C" (Completed)
  // AP_APP_MASTER's trigger BF_UPDATE_AP_APP_MASTER() will change AP_APL status
  //     and move data to history
  //
  // Step 1: Set AP_APP_MASTER STATUS to C
  // Step 2: BF_UPDATE_AP_APP_MASTER will insert History Data
  // Step 3: Mail Notice to related users
  /*
  static boolean do_release(TaskProgress tp, String App_No, String empNo, String Process) {
    boolean result = false;
    ArrayList wheres2 = new ArrayList();
    java.sql.Timestamp ts = new java.sql.Timestamp(tp.getStartTime());

    try {
      conn.setAutoCommit(false);

      ApplyForm applyForm = ApplyForm.getApplyForm(conn, App_No);

      String sql = "UPDATE AP_APP_MASTER SET APP_STATUS = ? WHERE APP_NO = ?";
      wheres2.add("C");
      wheres2.add(App_No);
      int cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());
      conn.commit();
      conn.setAutoCommit(true);

//     APLUser[] users = APLUser.getNoticeList(conn, "ALL");
      APLUser[] users = APLUser.getNoticeList(conn, getAPLType(App_No));
      String mailto = APLUser.getUserLoginByEmpNo(applyForm.getApplicant().getEmpNo()) + "@twsmtp01.mxic.com.tw";
      for (int i = 0; i < users.length; i++) {
          mailto = mailto + "," + users[i].getEmail();
      }

      TDSLogger.println(App_No + " Mail to : " + mailto);

      String mailfrom = (String) TDSResource.getProperties("APL").get(
          "mailfrom");

      String mailsubject = "APL 結案通知 ("+Process+")：APP_NO=" + App_No;
      String mailbody = "Information = \r\n" +
                        "http://" +
                        (String) TDSResource.
                        getProperties("APL").get(
                        "server_ip_port").toString().trim() +
                        "APLShow.do?appNo=" +
                        App_No;
      */
      /*+
                               "\r\nSigning Info. = http://" +
           (String)TDSResource.getProperties("APL").get("af_server_ip").toString().trim() +
                               ":" +
           (String)TDSResource.getProperties("APL").get("af_web_port").toString().trim() +
                               "/WebAgenda/SingleSignon.do?loginName=" +
                               applyForm.getApplicant().getEmpNo() +
           "&checkpass=false&action=/preAction.do?artInsID=" +
                               tp.getFormID() +
                               "&readOnly=true" */
  	  /*
      SendMail.send(mailto, mailfrom, mailsubject, mailbody,
                    "APL 結案通知：APP_NO=" + App_No, null);
    }
    catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    }
    finally {
      result = true;
    }
    return result;
  }
  */

  // 送退件信給申請人，申請人應可重送退件信
  // 原申請者要在 AgentFlow 選擇作廢，或到 APL 重新申請
  /*
  static boolean do_reject(TaskProgress tp, String App_No, String empNo) {
    boolean result = false;

    String sql = "UPDATE AP_APP_MASTER SET ";
    String formState = tp.getFormState().trim();
    ArrayList wheres2 = new ArrayList();
    java.sql.Timestamp ts = new java.sql.Timestamp(tp.getStartTime());

    sql = sql + "APP_STATUS = ? ";
    wheres2.add("J");

    if (formState.equals("第一層主管退件")) {
      sql = sql + ",PROCESS_TIME = ? ";
      wheres2.add(ts);
    }
    else if (formState.equals("第二層主管退件")) {
      sql = sql + ",COMPLETE_TIME = ? ";
      wheres2.add(ts);
    }
    sql = sql + "WHERE APP_NO = ? ";
    wheres2.add(App_No);

    try {

      // update App Master Status to reJecting
      TDSLogger.println("Reject: Updating...");
      int cnt = APLUtil.update(conn, sql.toString(), wheres2.toArray());
      TDSLogger.println("Reject: Update Completed! ");

      // sent Mail to notify applicant
      ApplyForm applyForm = ApplyForm.getApplyForm(conn, App_No);
      String mailto = APLUser.getUserLoginByEmpNo(applyForm.getApplicant().getEmpNo()) + "@twsmtp01.mxic.com.tw";

      String mailfrom = (String) TDSResource.getProperties("APL").get("mailfrom");
      String mailsubject = "APL 退件通知：APP_NO=" + App_No;
      String mailbody = "Information = \r\n " +
                        "http://" +
                        (String) TDSResource.
                        getProperties("APL").get("server_ip_port").toString().trim() +
                        "APLShow.do?appNo=" +
                        App_No;
      */
                    /*
                        +
                        "\r\nCancel => http://" +
                        (String) TDSResource.
                        getProperties("APL").get("af_server_ip").toString().trim() +
                         ":" +
                        (String) TDSResource.
                        getProperties("APL").get("af_web_port").toString().trim() +
                        "/WebAgenda/SingleSignon.do?loginName=" +
                        applyForm.getApplicant().getEmpNo() +
                        "&checkpass=false&action=/eform.do?taskID=" +
                        tp.getTaskID();
          */
  	/*
      TDSLogger.println("Reject: Send notify...");
      SendMail.send(mailto, mailfrom, mailsubject, mailbody,
                    "退件通知 Notice：APP_NO=" + App_No, null);
    }
    catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    }
    finally {
      result = true;
    }
    return result;
  }
	*/
  
  // get APL type
  public static int getAPLType( String App_No ) {
    String sql;
    Connection conn = null;
    int appType = 0;

    try {
      conn = DBConnection.getConnection();
      sql = "select c.process_type, 'INHOUSE' CO, count(*) CNT from ap_app_master a, ap_app_detail b, ap_apl c " +
          "where a.app_no = b.app_no " +
          "and b.apl_sid = c.sid " +
          "and a.app_no = '" + App_No + "' and c.vendor_no = '0000101008' " +
          "group by c.process_type " +
          "UNION " +
          "select c.process_type, 'SUBCON' CO, count(*) CNT from ap_app_master a, ap_app_detail b, ap_apl c " +
          "where a.app_no = b.app_no " +
          "and b.apl_sid = c.sid " +
          "and a.app_no = '" + App_No + "' and c.vendor_no != '0000101008' group by c.process_type";

      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        if (rs.getString("PROCESS_TYPE").equals("WS") && rs.getString("CO").equals("INHOUSE")  )
          appType += WS_INHOUSE;
        if (rs.getString("PROCESS_TYPE").equals("FT") && rs.getString("CO").equals("INHOUSE")  )
          appType += FT_INHOUSE;
        if (rs.getString("PROCESS_TYPE").equals("WS") && rs.getString("CO").equals("SUBCON")  )
          appType += WS_SUBCON;
        if (rs.getString("PROCESS_TYPE").equals("FT") && rs.getString("CO").equals("SUBCON")  )
          appType += FT_SUBCON;
        if (rs.getString("PROCESS_TYPE").equals("AVI") )
            appType += AVI;
        if (rs.getString("PROCESS_TYPE").equals("FVI") )
            appType += FVI;
        if (rs.getString("PROCESS_TYPE").equals("MARK") )
            appType += MARK;
      }
      rs.close();
      rs = null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return appType;
    }
    finally {
      DBConnection.close(conn);
    }
    return appType;
  }

}