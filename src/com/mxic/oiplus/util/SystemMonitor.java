package com.mxic.oiplus.util;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: MXIC</p>
 * @author :
 * @version 1.0 : 2008/12/02
 * Purpose: for System Monitor data input
 * arg[ 0] = SYSTEM	VARCHAR(8)
 * arg[ 1] = CATALOG	VARCHAR(16)
 * arg[ 2] = HOSTNAME	VARCHAR(32)
 * arg[ 3] = OBJECTNAME	VARCHAR(32)
 * arg[ 4] = STATUS	VARCHAR(8)
 * arg[ 5] = DESCRIPTION	VARCHAR(1024)
 * arg[ 6] = STARTTIME	DATE (yyyymmdd hh24miss)
 * arg[ 7] = ENDTIME	DATE (yyyymmdd hh24miss)
 * arg[ 8] = PROGRESS	NUMBER(3)
 * arg[ 9] = NUMBER1	NUMBER
 * arg[10] = NUMBER2	NUMBER
 * arg[11] = FILENAME	VARCHAR(64)
 *
 */

import java.sql.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.eif.*;

public class SystemMonitor {
private static final int arg_number = 12;
private static boolean fromShell = false;
private static String tab_sm_status = "tab_sm_status";

private static final String eifName = "SystemMonitor";
  public SystemMonitor(String arg[]) {
    UpdateSystemMonitor(arg);
  }

  // 使用於 jar file
  public static void dbclose(Connection connection){
    if (connection == null)
      return;
    if (!fromShell)
      DBConnection.close(connection);
    else {
      try {
        connection.close();
      }
      catch (Exception ex) {
        System.out.print("Connection close fail !!");
      }
      finally {
        connection = null;
      }
    }
  }

  public static void rollback(Connection connection){
    if(connection == null){
      return ;
    }
    try {
      connection.rollback();
    } catch (Exception ex) {
      if (!fromShell)
        TDSLogger.println(ex);
    }
  }

  public synchronized static Connection getConnection() throws Exception {
    if (fromShell) {
      try {
        Class c = Class.forName("oracle.jdbc.driver.OracleDriver");
        DriverManager.setLoginTimeout(60);
      }
      catch (Exception e) {
        throw new SQLException("Can't found oracle JDBC Driver");
      }
      Connection dbConnection = DriverManager.getConnection(
          "jdbc:oracle:thin:@tstfwdev:1521:devtds", "monitor", "monitor");
      dbConnection.setAutoCommit(true);
      return dbConnection;
    } else
      return DBConnection.getConnection();
  }

  public static boolean UpdateSystemMonitor(String arg[]) {
    if (arg.length < 6)
      return false;
    String param[] = new String[arg_number];
    int i;
    for ( i=0; i<arg_number && i<arg.length; i++) {
      param[i] = arg[i];
    }
    for (;i<arg_number; i++)
      param[i] = "";

    if (!param[4].equals("OK") && !param[4].equals("Open")) {
      if (!fromShell) {
        LogWriter log = new LogWriter(eifName);
        log.WriterToLog(eifName + "-VAL", " Invalid Status",
                        arg[0] + "." + arg[1] + "." + arg[2] + "." + arg[3] +
                        " status=" + arg[4]);
        log.close();
      }
      return false;
    } else {
      if (arg[6].equals(""))
        arg[6] = "to_char(sysdate,'yyyymmdd hh24miss')";
      if (arg[7].equals(""))
        arg[7] = "to_char(sysdate,'yyyymmdd hh24miss')";
      if (arg[8].equals(""))
        arg[8] = "0";
      if (arg[9].equals(""))
        arg[9] = "0";
      if (arg[10].equals(""))
        arg[10] = "0";
      return UpdateSystemMonitors(arg[0],arg[1],arg[2],arg[3],arg[4],arg[5],
                                  arg[6],arg[7],arg[8],arg[9],arg[10],arg[11]);
    }
  }

  public static boolean UpdateSystemMonitors(String Systems, String Catalog, String Hostname, String Objectname, String Status,
                                             String Descript, String StartTime, String EndTime, String Progress,
                                             String Number1, String Number2, String Filename) {

    String TAB_SM_STATUS;
    if (fromShell)
      TAB_SM_STATUS = tab_sm_status;
    else
      TAB_SM_STATUS = TDSResource.getProperties("EIF").getProperty("TAB_SM_STATUS");
    String sql = "update "+TAB_SM_STATUS+" set logtime=sysdate, status=?, description=?,\n";
    if (StartTime.trim().equals(""))
      sql += "starttime=sysdate,\n";
    else
      sql += "starttime=to_date(?,'yyyymmdd hh24miss'),\n";
    if (EndTime.trim().equals(""))
      sql += "endtime=sysdate,\n";
    else
      sql += "endtime=to_date(?,'yyyymmdd hh24miss'),\n";

    sql += "progress=?,number1=?,number2=?,filename=?\n" +
        "where system=? and catalog=? and hostname=? and objectname=?";

    Connection conn = null;
    LogWriter log = null;
    if (fromShell)
      log = new LogWriter(eifName);
    try{
      if (Progress.trim().equals("")) Progress = "0";
      if (Number1.trim().equals("")) Number1 = "0";
      if (Number2.trim().equals("")) Number2 = "0";
      conn = getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      int i = 2;
      ps.setString(1,Status);
      ps.setString(2,Descript);
      if (!StartTime.trim().equals(""))
        ps.setString(++i,StartTime);
      if (!EndTime.trim().equals(""))
        ps.setString(++i,EndTime);
      ps.setInt(++i,Integer.parseInt(Progress));
      ps.setInt(++i,Integer.parseInt(Number1));
      ps.setInt(++i,Integer.parseInt(Number2));
      ps.setString(++i,Filename);
      ps.setString(++i,Systems);
      ps.setString(++i,Catalog);
      ps.setString(++i,Hostname);
      ps.setString(++i,Objectname);
      ps.executeUpdate();
    }catch(Exception ex){
      rollback(conn);
      System.out.println(ex.toString());
      if (!fromShell) {
        TDSLogger.println(ex);
        log.WriterToLog(eifName + "-UPD", " ACTION FAILED",
                        " Update fail for " + Systems + "." + Catalog + "." +
                        Hostname + "." + Objectname);
      }
      return false;
    }finally{
      dbclose(conn);
      if (!fromShell)
        log.close();
    }

    return true;
  }

  /* 1. 更新 Start Time，End Time 自動設為 null
     2. 若 StartTime == "", 則用 sysdate 取代 */
  public static boolean UpdateStartTime(String Systems, String Catalog, String Hostname, String Objectname,
                                        String StartTime) {

    String TAB_SM_STATUS;
    if (fromShell)
      TAB_SM_STATUS = tab_sm_status;
    else
      TAB_SM_STATUS = TDSResource.getProperties("EIF").getProperty("TAB_SM_STATUS");
    String sql = "update "+TAB_SM_STATUS+" set logtime=sysdate, \n";
    if (StartTime.trim().equals(""))
      sql += "starttime=sysdate\n";
    else
      sql += "starttime=to_date(?,'yyyymmdd hh24miss')\n";
    sql += ", endtime=null\n where system=? and catalog=? and hostname=? and objectname=?";

    Connection conn = null;
    LogWriter log = null;
    if (!fromShell)
      log = new LogWriter(eifName);
    int i = 0;
    try{
      conn = getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      if (!StartTime.trim().equals(""))
        ps.setString(++i,StartTime);
      ps.setString(++i,Systems);
      ps.setString(++i,Catalog);
      ps.setString(++i,Hostname);
      ps.setString(++i,Objectname);
      ps.executeUpdate();
    }catch(Exception ex){
      rollback(conn);
      System.out.println(ex.toString());
      if (!fromShell) {
        TDSLogger.println(ex);
        log.WriterToLog(eifName + "-UPD", " ACTION FAILED",
                        " Update StartTime fail for " + Systems + "." + Catalog +
                        "." + Hostname + "." + Objectname);
      }
      return false;
    }finally{
      dbclose(conn);
      if (!fromShell)
        log.close();
    }
    return true;
  }

  /* 1. 更新 End Time，若 Status/Description 有改變亦可更新
     2. 同步更新 Number1 (執行時間)
     3. 若 EndTime == "", 則用 sysdate 取代 */
  public static boolean UpdateEndTime(String Systems, String Catalog, String Hostname, String Objectname,
                                        String Status, String Descript, String EndTime) {

      String TAB_SM_STATUS;
      if (fromShell)
        TAB_SM_STATUS = tab_sm_status;
      else
        TAB_SM_STATUS = TDSResource.getProperties("EIF").getProperty("TAB_SM_STATUS");
      String sql = "update "+TAB_SM_STATUS+" set logtime=sysdate,\n";
      if (EndTime.trim().equals(""))
        sql += "endtime=sysdate, number1=(sysdate-starttime)*1440\n";
      else
        sql += "endtime=to_date(?,'yyyymmdd hh24miss'), number1=(to_date(?,'yyyymmdd hh24miss')-starttime)*1440\n";
      if (!Status.trim().equals(""))
        sql += ",status=?\n";
      if (!Descript.trim().equals(""))
        sql += ",description=?\n";

      sql += "where system=? and catalog=? and hostname=? and objectname=?";

      Connection conn = null;
      LogWriter log = null;
      if (!fromShell)
        log = new LogWriter(eifName);
      int i=0;
      try{
        conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        if (!EndTime.trim().equals("")) {
          ps.setString(++i, EndTime);
          ps.setString(++i, EndTime);
        }
        if (!Status.trim().equals(""))
          ps.setString(++i,Status);
        if (!Descript.trim().equals(""))
          ps.setString(++i,Descript);
        ps.setString(++i,Systems);
        ps.setString(++i,Catalog);
        ps.setString(++i,Hostname);
        ps.setString(++i,Objectname);
        ps.executeUpdate();
      }catch(Exception ex){
        rollback(conn);
        System.out.println(ex.toString());
        if (!fromShell) {
          TDSLogger.println(ex);
          log.WriterToLog(eifName + "-UPD", " ACTION FAILED",
                          " Update EndTime fail for " + Systems + "." + Catalog +
                          "." + Hostname + "." + Objectname);
        }
        return false;
      }finally{
        dbclose(conn);
        if (!fromShell)
          log.close();
      }
      return true;
    }

    /* 1. 設定 status (for 只要設定 status 的 Server/DB/AP)
       2. 若 Description 有改變亦可更新
    */
    public static boolean SetStatus(String Systems, String Catalog, String Hostname, String Objectname,
                                          String Status, String Descript) {

        String TAB_SM_STATUS = "";
        if (fromShell)
          TAB_SM_STATUS = tab_sm_status;
        else
          TAB_SM_STATUS = TDSResource.getProperties("EIF").getProperty("TAB_SM_STATUS");

        System.out.println("make sql string");

        String sql = "update "+TAB_SM_STATUS+" set logtime=sysdate,status=?\n";
        if (!Descript.trim().equals(""))
          sql += ",description=?\n";

        sql += "where system=? and catalog=? and hostname=? and objectname=?";

        Connection conn = null;
        LogWriter log = null;
        if (!fromShell)
          log = new LogWriter(eifName);
        int i=0;
        try{
          conn = getConnection();
          PreparedStatement ps = conn.prepareStatement(sql);
          ps.setString(++i,Status);
          if (!Descript.trim().equals(""))
            ps.setString(++i,Descript);
          ps.setString(++i,Systems);
          ps.setString(++i,Catalog);
          ps.setString(++i,Hostname);
          ps.setString(++i,Objectname);
          ps.executeUpdate();
        }catch(Exception ex){
          rollback(conn);
          System.out.println(ex.toString());
          if (!fromShell) {
            TDSLogger.println(ex);
            log.WriterToLog(eifName + "-UPD", " ACTION FAILED",
                            " Set Status fail for " + Systems + "." + Catalog +
                            "." + Hostname + "." + Objectname);
          }
          return false;
        }finally{
          dbclose(conn);
          if (!fromShell)
            log.close();
        }
        return true;
      }

    // call jar:
    // 1. start system Catalog Hostname Objectname [StartTime]
    // 2. shell start System Catalog Hostname Objectname [StartTime]
    // 3. end System Catalog Hostname Objectname Status [Descript [EndTime]]
    // 4. shell end System Catalog Hostname Objectname Status [Descript [EndTime]]
    // 5. set System Catalog Hostname Objectname Status [Descript]
    // 6. shell set System Catalog Hostname Objectname Status [Descript]
    public static void main(String arg[]){
      int pos = 0;
      String timeValue;
      String Description;

      if (arg.length < 5) return;
      if (arg[0].equals("shell")) {
        fromShell = true;
        pos = 1;
      }
      System.out.println("starting...");
      if (arg[pos].equals("start")) {
        if (arg.length > 5 + pos)
          timeValue = arg[5 + pos];
        else timeValue = "";
        UpdateStartTime(arg[++pos],arg[++pos],arg[++pos],arg[++pos],timeValue);
      } else if (arg[pos].equals("end")) {
        if (arg.length > 6 + pos)
          Description = arg[6 + pos];
        else Description = "";
        if (arg.length > 7 + pos)
          timeValue = arg[7 + pos];
        else timeValue = "";
        UpdateEndTime(arg[++pos],arg[++pos],arg[++pos],arg[++pos],arg[++pos],Description,timeValue);
      } else if (arg[pos].equals("set")) {
        if (arg.length > 6 + pos)
          Description = arg[6 + pos];
        else Description = "";
        SetStatus(arg[++pos],arg[++pos],arg[++pos],arg[++pos],arg[++pos],Description);
      }
//      UpdateSystemMonitor(arg);
    }
}
