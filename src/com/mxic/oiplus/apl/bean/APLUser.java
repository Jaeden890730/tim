package com.mxic.oiplus.apl.bean;

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.apl.*;
import com.mxic.oiplus.au.*;
import com.mxic.oiplus.resource.*;

public class APLUser extends User {

  public static APLUser EMPTY = createUser();
  short role = APLDef.Role.APPLICANT;
  short level = 0;
  String userId;
  String userName;

  String empNo;
  public String getEmpNo() {
    return empNo;
  }

  String realName;
  public String getRealName() {
    return realName;
  }

  String email;
  public String getEmail() {
    return email;
  }

  String ws_inhouse;
  public String getWs_inhouse() {
    return ws_inhouse;
  }

  String ft_inhouse;
  public String getFt_inhouse() {
    return ft_inhouse;
  }

  String ws_subcon;
  public String getWs_subcon() {
    return ws_subcon;
  }

  String ft_subcon;
  public String getFt_subcon() {
    return ft_subcon;
  }
  
  String avi;
  public String getAvi() {
    return avi;
  }
  
  String fvi;
  public String getFvi() {
    return fvi;
  }
  
  String mark;
  public String getMark() {
    return mark;
  }

  String deptNo;
  String deptName;

  protected APLUser() {
    super();
    this.setUserId("");
    this.setUserName("");
    this.empNo = "";
    this.realName = "";
    this.email = "";
    this.ws_inhouse = "";
    this.ft_inhouse = "";
    this.ws_subcon = "";
    this.ft_subcon = "";
    this.avi = "";
    this.fvi = "";
    this.mark = "";
    this.setDeptNo("");
    this.setDeptName("");
  }

  public APLUser(User user) {
    this(user, APLDef.Role.APPLICANT, (short) 0);
  }

  public APLUser(User user, short role, short level) {
    super();
    this.role = role;
    this.level = level;
    this.setUserId(user.getUserId());
    this.setUserName(user.getUserName());
    this.setDeptNo(user.getDeptNo());
    this.setDeptName(user.getDeptName());
  }

  public APLUser(APLUser user) {
    this(user, APLDef.Role.APPLICANT, (short) 0);
  }

  public APLUser(APLUser user, short role, short level) {
    super();
    this.role = role;
    this.level = level;
    this.setUserId(user.getUserId());
    this.setUserName(user.getUserName());
    this.empNo = user.getEmpNo();
    this.realName = user.getRealName();
    this.email = user.getEmail();
    this.setDeptNo(user.getDeptNo());
    this.setDeptName(user.getDeptName());
  }

  public static APLUser createUser(AP_APP_USER rawData) {
    APLUser user = new APLUser();
    user.setUserId(rawData.USER_ID.toString());
    user.setUserName(rawData.USER_NAME);
    user.empNo = rawData.EMPLOYEE_NO;
    user.realName = rawData.REAL_NAME;
    user.email = rawData.EMAIL;
    user.ws_inhouse = rawData.WS_INHOUSE;
    user.ft_inhouse = rawData.FT_INHOUSE;
    user.ws_subcon = rawData.WS_SUBCON;
    user.ft_subcon = rawData.FT_SUBCON;
    user.avi = rawData.AVI;
    user.fvi = rawData.FVI;
    user.mark = rawData.MARK;
    user.setDeptNo(rawData.DEPT_ID);
    user.setDeptName(rawData.DEPT_NAME);
    return user;
  }

  public static APLUser getUserById(Connection conn, Integer userId) throws Exception {
    return getUserById(conn, new Integer[] {userId})[0];
  }

  public static APLUser[] getUserById(Connection conn, Integer[] userId) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    ArrayList wheres = new ArrayList();
    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP ");
    sql.append("WHERE (");
    for ( int i = 0; i < userId.length; i++ ) {
      if (userId[i] != null && userId[i].intValue() >= 0) {
        sql.append(" USR.USER_ID = ? OR");
        wheres.add(userId[i]);
      }
    }
    if ( userId.length == 0 ) {
      sql.append("TRIM(USR.EMPLOYEE_NO) IS NOT NULL AND USR.DEPT_ID <> 'DUMMY' OR");
      wheres.add("");
    }
    sql.replace(sql.length() - 2, sql.length(), ") AND USR.DEPT_ID = DEP.DEPT_ID ");
    if ( wheres.size() > 0 ) {
      if ( userId.length == 0 ) wheres.clear();
      AP_APP_USER[] users = new AP_APP_USER[0];
      users =
          (AP_APP_USER[]) APLUtil.getData(conn,sql.toString(),AP_APP_USER.class,wheres.toArray()).toArray(users);
      wheres.clear();
      for ( int i = 0; i < users.length; i++ )
        wheres.add(createUser(users[i]));
    }
    return (APLUser[]) wheres.toArray(new APLUser[0]);
  }

  public static APLUser[] getUserByEmpNo(Connection conn, String[] empNo) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    ArrayList wheres = new ArrayList();
    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP ");
    sql.append("WHERE (");
    for ( int i = 0; i < empNo.length; i++ ) {
      if (empNo[i] != null && empNo[i].length() > 0) {
        if (empNo[i].trim().equals("?"))
          continue;
        sql.append(" TRIM(USR.EMPLOYEE_NO) = ? OR");
        wheres.add(empNo[i].trim());
      }
    }
    if ( empNo.length == 0 ) {
      sql.append("TRIM(USR.EMPLOYEE_NO) IS NOT NULL AND USR.DEPT_ID <> 'DUMMY' OR");
      wheres.add("");
    }
    sql.replace(sql.length() - 2, sql.length(), ") AND USR.DEPT_ID = DEP.DEPT_ID ");
    if ( wheres.size() > 0 ) {
      if ( empNo.length == 0 )
        wheres.clear();
      AP_APP_USER[] rawData = new AP_APP_USER[0];
      rawData =
          (AP_APP_USER[]) APLUtil.getData(conn,sql.toString(),AP_APP_USER.class,wheres.toArray()).toArray(rawData);
      wheres.clear();
      for ( int i = 0; i < rawData.length; i++ )
        wheres.add(createUser(rawData[i]));
    }
    return (APLUser[]) wheres.toArray(new APLUser[0]);
  }

  public static APLUser[] getUserByDeptID(Connection conn, String dept_id) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    ArrayList wheres = new ArrayList();

    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP ");
    sql.append("WHERE ");
    if ( dept_id.length() == 0 )
      sql.append("1 = 1");
    else
      sql.append("DEP.DEPT_ID = '" + dept_id + "' AND DEP.DEPT_ID = USR.DEPT_ID ");

      AP_APP_USER[] rawData = new AP_APP_USER[0];
      rawData =
          (AP_APP_USER[]) APLUtil.getData(conn,sql.toString(),AP_APP_USER.class,wheres.toArray()).toArray(rawData);

      for ( int i = 0; i < rawData.length; i++ )
        wheres.add(createUser(rawData[i]));
    return (APLUser[]) wheres.toArray(new APLUser[0]);
  }

  public static String getUserByEmpNo(String empNo) {
    String realName = null;
    if (empNo == null || empNo.equals(""))
      return "";

    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      String sql = "SELECT REAL_NAME FROM AU_USER_ACCOUNT " +
                   "WHERE EMPLOYEE_NO = '" + empNo + "'";
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        realName = rs.getString("REAL_NAME");
      }
    } catch (Exception ex) {
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    if (realName == null)
      return "";

    return realName;
  }

  public static String getUserLoginByEmpNo(String empNo) {
    String userName = null;
    if (empNo == null || empNo.equals(""))
      return "";

    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      String sql = "SELECT USER_NAME FROM AU_USER_ACCOUNT " +
                   "WHERE EMPLOYEE_NO = '" + empNo + "'";
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()){
        userName = rs.getString("USER_NAME");
      }
    } catch (Exception ex) {
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    if (userName == null)
      return "";

    return userName;
  }

  public static APLUser[] getNoticeList(Connection conn, String watch_group) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    ArrayList wheres = new ArrayList();
    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME, NOTICE.EMAIL ");
    sql.append(",NOTICE.WS_INHOUSE,NOTICE.FT_INHOUSE,NOTICE.WS_SUBCON,NOTICE.FT_SUBCON ");
    sql.append(",NOTICE.AVI,NOTICE.FVI,NOTICE.MARK ");
    sql.append("FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP, AP_NOTICE NOTICE ");
    sql.append("WHERE USR.DEPT_ID <> 'DUMMY' AND USR.DEPT_ID = DEP.DEPT_ID AND ");
    sql.append("USR.EMPLOYEE_NO = TRIM(NOTICE.EMPLOYEE_NO) ");
    if (watch_group.equals("WSIN"))
      sql.append("AND NOTICE.WS_INHOUSE = 'Y' ");
    else if (watch_group.equals("FTIN"))
      sql.append("AND NOTICE.FT_INHOUSE = 'Y' ");
    else if (watch_group.equals("WSSUB"))
      sql.append("AND NOTICE.WS_SUBCON = 'Y' ");
    else if (watch_group.equals("FTSUB"))
      sql.append("AND NOTICE.FT_SUBCON = 'Y' ");
    else if (watch_group.equals("AVI"))
        sql.append("AND NOTICE.AVI = 'Y' ");
    else if (watch_group.equals("FVI"))
        sql.append("AND NOTICE.FVI = 'Y' ");
    else if (watch_group.equals("MARK"))
        sql.append("AND NOTICE.MARK = 'Y' ");
    sql.append("ORDER BY NOTICE.EMPLOYEE_NO ");

    AP_APP_USER[] users = new AP_APP_USER[0];
    wheres.clear();
    users =
        (AP_APP_USER[]) APLUtil.getData(conn,sql.toString(),AP_APP_USER.class,wheres.toArray()).toArray(users);
    wheres.clear();
    for ( int i = 0; i < users.length; i++ )
      wheres.add(createUser(users[i]));

    return (APLUser[]) wheres.toArray(new APLUser[0]);
  }

  // used in release process
  public static APLUser[] getNoticeList(Connection conn, int watch_group) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    StringBuffer watch = new StringBuffer(256);

    ArrayList wheres = new ArrayList();
    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME, NOTICE.EMAIL ");
    sql.append(",NOTICE.WS_INHOUSE,NOTICE.FT_INHOUSE,NOTICE.WS_SUBCON,NOTICE.FT_SUBCON ");
    sql.append(",NOTICE.AVI,NOTICE.FVI,NOTICE.MARK ");
    sql.append("FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP, AP_NOTICE NOTICE ");
    sql.append("WHERE USR.DEPT_ID <> 'DUMMY' AND USR.DEPT_ID = DEP.DEPT_ID AND ");
    sql.append("USR.EMPLOYEE_NO = TRIM(NOTICE.EMPLOYEE_NO) AND ( ");
    if ((watch_group & 1) > 0)
      watch.append("NOTICE.WS_INHOUSE = 'Y' ");
    if ((watch_group & 2) > 0) {
      if (watch.length() > 0) watch.append("OR ");
      watch.append("NOTICE.FT_INHOUSE = 'Y' ");
    }
    if ((watch_group & 4) > 0){
      if (watch.length() > 0)
        watch.append("OR ");
      watch.append("NOTICE.WS_SUBCON = 'Y' ");
    }
    if ((watch_group & 8) > 0){
      if (watch.length() > 0)
        watch.append("OR ");
      watch.append("NOTICE.FT_SUBCON = 'Y' ");
    }
    if ((watch_group & 16) > 0){
        if (watch.length() > 0)
          watch.append("OR ");
        watch.append("NOTICE.AVI= 'Y' ");
    }
    if ((watch_group & 32) > 0){
        if (watch.length() > 0)
          watch.append("OR ");
        watch.append("NOTICE.FVI= 'Y' ");
    }
    if ((watch_group & 64) > 0){
        if (watch.length() > 0)
          watch.append("OR ");
        watch.append("NOTICE.MARK= 'Y' ");
    }
    sql.append(watch);
    sql.append(") ORDER BY NOTICE.EMPLOYEE_NO ");

    AP_APP_USER[] users = new AP_APP_USER[0];
    wheres.clear();
    users =
        (AP_APP_USER[]) APLUtil.getData(conn,sql.toString(),AP_APP_USER.class,wheres.toArray()).toArray(users);
    wheres.clear();
    for ( int i = 0; i < users.length; i++ )
      wheres.add(createUser(users[i]));

    return (APLUser[]) wheres.toArray(new APLUser[0]);
  }

/* marked at 20070105 for update getNoticeList()
  public static APLUser[] getNoticeList(Connection conn, Integer[] userId) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    ArrayList wheres = new ArrayList();
    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME, NOTICE.EMAIL ");
    sql.append(",NOTICE.WS_INHOUSE,NOTICE.FT_INHOUSE,NOTICE.WS_SUBCON,NOTICE.FT_SUBCON ");
    sql.append("FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP, AP_NOTICE NOTICE ");
    sql.append("WHERE USR.DEPT_ID <> 'DUMMY' AND USR.DEPT_ID = DEP.DEPT_ID AND (");
    for ( int i = 0; i < userId.length; i++ ) {
      if (userId[i] != null && userId[i].intValue() >= 0) {
        sql.append(" USR.USER_ID = ? OR");
        wheres.add(userId[i]);
      }
    }
    if ( userId.length == 0 ) {
      sql.append("TRIM(USR.EMPLOYEE_NO) IS NOT NULL OR");
      wheres.add("");
    }
    sql.replace(sql.length() - 2, sql.length(), ") AND USR.EMPLOYEE_NO = TRIM(NOTICE.EMPLOYEE_NO)");
    sql.append(" ORDER BY NOTICE.EMPLOYEE_NO ");
    if ( wheres.size() > 0 ) {
      if ( userId.length == 0 )
        wheres.clear();
      AP_APP_USER[] users = new AP_APP_USER[0];
      users =
          (AP_APP_USER[]) APLUtil.getData(conn,sql.toString(),AP_APP_USER.class,wheres.toArray()).toArray(users);
      wheres.clear();
      for ( int i = 0; i < users.length; i++ )
        wheres.add(createUser(users[i]));
    }
    return (APLUser[]) wheres.toArray(new APLUser[0]);
  }
*/

  public static APLUser[] getNoticeListByAppNo(Connection conn,
                                               Integer[] userId,
                                               String AppNo) throws Exception {
    StringBuffer sql = new StringBuffer(256);
    ArrayList wheres = new ArrayList();
    boolean ws_inhouse = false;
    boolean ft_inhouse = false;
    boolean ws_subcon = false;
    boolean ft_subcon = false;
    boolean avi = false;
    boolean fvi = false;
    boolean mark = false;

    // select AppNo APL attribute : WS or FT / INHOUSE or SUBCONS
    sql.append("select a.process_type, 'SUBCON' PLANT ");
    sql.append("from ap_apl a, ap_app_detail b, ap_app_master c ");
    sql.append("where c.app_no = '"+AppNo+"' ");
    sql.append("and c.app_no = b.app_no ");
    sql.append("and b.apl_sid = a.sid ");
    sql.append("and a.vendor_no != '0000101008' ");
    sql.append("group by a.process_type ");
    sql.append("having count(*) > 0 ");
    sql.append("union ");
    sql.append("select a.process_type, 'INHOUSE' PLANT ");
    sql.append("from ap_apl a, ap_app_detail b, ap_app_master c ");
    sql.append("where c.app_no = '"+AppNo+"' ");
    sql.append("and c.app_no = b.app_no ");
    sql.append("and b.apl_sid = a.sid ");
    sql.append("and a.vendor_no = '0000101008' ");
    sql.append("group by a.process_type ");
    sql.append("having count(*) > 0 ");

    PreparedStatement ps = conn.prepareStatement(sql.toString());
    ResultSet appattr = ps.executeQuery();
    while (appattr.next()) {
      if (appattr.getString("PROCESS_TYPE").equals("WS")){
        if (appattr.getString("PLANT").equals("INHOUSE"))
          ws_inhouse = true;
        else
          ws_subcon = true;
      }
      if (appattr.getString("PROCESS_TYPE").equals("FT")){
        if (appattr.getString("PLANT").equals("INHOUSE"))
          ft_inhouse = true;
        else
          ft_subcon = true;
      }
      if (appattr.getString("PROCESS_TYPE").equals("AVI")){
          avi = true;
      }
      if (appattr.getString("PROCESS_TYPE").equals("FVI")){
          fvi = true;
      }
      if (appattr.getString("PROCESS_TYPE").equals("MARK")){
          mark = true;
      }
    }
    sql.delete(0,sql.length());
    sql.append("SELECT USR.USER_ID, USR.USER_NAME, USR.EMPLOYEE_NO, USR.REAL_NAME, ");
    sql.append("USR.DEPT_ID, DEP.DEPT_NAME, NOTICE.EMAIL ");
    sql.append(",NOTICE.WS_INHOUSE,NOTICE.FT_INHOUSE,NOTICE.WS_SUBCON,NOTICE.FT_SUBCON ");
    sql.append(",NOTICE.AVI,NOTICE.FVI,NOTICE.MARK ");
    sql.append("FROM AU_USER_ACCOUNT USR, AU_USER_DEPARTMENT DEP, AP_NOTICE NOTICE ");
    sql.append("WHERE USR.DEPT_ID <> 'DUMMY' AND USR.DEPT_ID = DEP.DEPT_ID AND (");
    for ( int i = 0; i < userId.length; i++ ) {
      if (userId[i] != null && userId[i].intValue() >= 0) {
        sql.append(" USR.USER_ID = ? OR");
        wheres.add(userId[i]);
      }
    }
    if ( userId.length == 0 ) {
      sql.append("TRIM(USR.EMPLOYEE_NO) IS NOT NULL OR");
      wheres.add("");
    }
    sql.replace(sql.length() - 2, sql.length(), ") AND USR.EMPLOYEE_NO = TRIM(NOTICE.EMPLOYEE_NO)");
    sql.append(" AND ( 1 = 1 ");
    if (ws_inhouse) sql.append(" AND WS_INHOUSE = 'Y'");
    if (ft_inhouse) sql.append(" AND FT_INHOUSE = 'Y'");
    if (ws_subcon) sql.append(" AND WS_SUBCON = 'Y'");
    if (ft_subcon) sql.append(" AND FT_SUBCON = 'Y'");
    if (avi) sql.append(" AND AVI = 'Y'");
    if (fvi) sql.append(" AND FVI = 'Y'");
    if (mark) sql.append(" AND MARK = 'Y'");
    sql.append(") ORDER BY NOTICE.EMPLOYEE_NO ");
    if ( wheres.size() > 0 ) {
      if ( userId.length == 0 )
        wheres.clear();
      AP_APP_USER[] users = new AP_APP_USER[0];
      users =
          (AP_APP_USER[]) APLUtil.getData(conn,sql.toString(),AP_APP_USER.class,wheres.toArray()).toArray(users);
      wheres.clear();
      for ( int i = 0; i < users.length; i++ )
        wheres.add(createUser(users[i]));
    }
    return (APLUser[]) wheres.toArray(new APLUser[0]);
  }

  public static class AP_APP_USER {
    public Number USER_ID;
    public String USER_NAME;
    public String EMPLOYEE_NO;
    public String REAL_NAME;
    public String EMAIL;
    public String DEPT_ID;
    public String DEPT_NAME;
    public String WS_INHOUSE;
    public String FT_INHOUSE;
    public String WS_SUBCON;
    public String FT_SUBCON;
    public String AVI;
    public String FVI;
    public String MARK;
  }

  private static APLUser _EMPTY = null;
  public static APLUser createUser() {
    if (_EMPTY == null ) _EMPTY = new APLUser();
    return _EMPTY;
  }
}