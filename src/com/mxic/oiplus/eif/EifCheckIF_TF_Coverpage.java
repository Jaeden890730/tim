package com.mxic.oiplus.eif;

import java.sql.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

public class EifCheckIF_TF_Coverpage {

  static private Connection conn = null;
  static ResultSet wrongdata = null;

  public static void main(String[] args) {

    boolean cheskstatus = getWrongData();

    //SystemMonitor.UpdateStartTime("PEIS","MQ","mxtsteif","TIMDataQualityCheck","");
    if (wrongdata == null && cheskstatus) {
      TDSLogger.println("IF_TF_Coverpage Interface OK !!");
      //SystemMonitor.UpdateEndTime("PEIS","MQ","mxtsteif","TIMDataQualityCheck","OK","","");
      return;
    }

    int i = 0;
    try {
      String message = "";

      if (cheskstatus) {
        while (wrongdata.next()) {
          i++;
          message = message + "Invalid data found (" + i + "): \n" +
              "    LOGDATE=" + StringUtil.NullConvert(wrongdata.getString("LOG_DATE")) + "\n" +
              "    APPLICANT=" + StringUtil.NullConvert(wrongdata.getString("APPLICANT")) + "\n" +
              "    CASENO=" + StringUtil.NullConvert(wrongdata.getString("CASENO")) + "\n" +
              "    DOCNO=" + StringUtil.NullConvert(wrongdata.getString("DOCNO")) + "\n" +
              "    EFFECTIVEDATE=" + StringUtil.NullConvert(wrongdata.getString("EFFECTIVEDATE")) + "\n" +
              "    REV=" + StringUtil.NullConvert(wrongdata.getString("REV")) + "\n\n";
        }
        wrongdata.close();
      } else message = "TIM 資料庫連線異常";

      if (i == 0 && cheskstatus) {
        TDSLogger.println("IF_TF_Coverpage Interface OK !!");
        //SystemMonitor.UpdateEndTime("PEIS","MQ","mxtsteif","TIMDataQualityCheck","OK","","");
        return;
      }

      //SystemMonitor.UpdateEndTime("PEIS","MQ","mxtsteif","TIMDataQualityCheck","Open","",message);
      TDSLogger.println(message);

      String mailto = (String) TDSResource.getProperties("EIF").get("if_tf_coverpage.mailto");
      String mailfrom = "TIM@mxic.com.tw";
      String mailsubject = "TIM-Notes interface:IF_TF_COVERPAGE 資料異常通知";
      String mailbody = message;
      SendMail.send(mailto, mailfrom, mailsubject, mailbody,
                    "TIM-Notes interface:IF_TF_COVERPAGE 資料異常通知", null);

    } catch (Exception ex) {
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
      wrongdata = null;
      DBConnection.close(conn);
    }

    return;
  }

  // 取得 IF_TF_Coverpage 中 Caseno, docno 或 version 值不正常的資料
  public static boolean getWrongData() {
    String sql;
    boolean result = true;
    String interval = (String) TDSResource.getProperties("EIF").get("if_tf_coverpage.interval");

    try {
      conn = DBConnection.getConnection();
      sql = "SELECT * FROM IF_TF_COVERPAGE WHERE STATUS != '作廢' AND" +
            "(LENGTH(TRIM(NVL(CASENO,'1'))) != 6 OR  " +
            "(LENGTH(TRIM(NVL(DOCNO,'1'))) != 9 AND LENGTH(TRIM(NVL(DOCNO,'1'))) != 10) OR " +
            "REV IS NULL OR (EFFECTIVEDATE IS NULL AND STATUS = '生效')) AND LOG_DATE > sysdate-"+interval;
      TDSLogger.println(sql);

      PreparedStatement ps = conn.prepareStatement(sql);
      wrongdata = ps.executeQuery();

    }
    catch (Exception ex) {
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      result = false;
    }
    finally {
    }
    return result;
  }
}