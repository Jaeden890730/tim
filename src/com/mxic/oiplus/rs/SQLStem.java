package com.mxic.oiplus.rs;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.mxic.oiplus.util.*;

public class SQLStem {
  public SQLStem() {
  }

  public static int insert(Connection conn, String tablename, HashMap values) throws Exception{
    return BADB.insert(conn,tablename,values);
  }

  public static int update(Connection conn, String tablename, HashMap values, HashMap conditions) throws Exception{
    return BADB.update(conn,tablename,values,conditions);
  }

  public static int delete(Connection conn, String tablename,HashMap conditions) throws Exception{
    return BADB.delete(conn,tablename,conditions);
  }

  // start of sql_util
  /*fieldName : fls.product_code,fls.test_mode
      value     : 6577M / FT1,6577M / FT2
      delim0    : ,     ---- filedName だ筳才腹
      delim1    : ,     ---- value だ筳才腹
      delim2    :  /    ---- value だ筳才腹
      return    : ( ( fls.product_code = '6577M' and fls.test_mode = 'FT1' ) or ( fls.product_code = '6577M' and fls.test_mode = 'FT2' ) )
   */
  public static String getSpecialWhereStm(String fieldName,
                                          String value,
                                          String delim0,
                                          String delim1,
                                          String delim2,
                                          boolean whereNeed){
    StringBuffer whereStm = new StringBuffer();
    HashMap map = new HashMap();
    String[] fields = StringUtil.parse2StringsStr(fieldName,delim0);
    String[] values = StringUtil.parse2StringsStr(value,delim1);
    if (fields != null && fields.length > 0){
      if (values != null && values.length > 0){
        for(int i = 0; i < values.length; i++){
          String[] colValues = StringUtil.parse2StringsStr(values[i],delim2);
          if (colValues!=null && colValues.length==fields.length){
            for (int j = 0; j < fields.length; j++){
              map.put(fields[j],colValues[j]);
            }
            if (i > 0)
              whereStm.append(" or ");
            whereStm.append(" ( ");
            whereStm.append(SQLStem.getSpecialWhereStmt(map));
            whereStm.append(" ) ");
          }
        }
      }
    }
    if (whereStm.length() > 0){
      whereStm.insert(0," ( ");
      whereStm.append(" ) ");
      if (whereNeed){
        whereStm.insert(0," where ");
      } else {
        whereStm.insert(0," and ");
      }
    }
    return whereStm.toString();
  }

  public static StringBuffer getWhereStmt(HashMap map,
                                          boolean whereNeed){
    StringBuffer whereBuffer = getWhereStmt(map);
    if (whereBuffer.length() > 0){
      if (!whereNeed){
        whereBuffer.delete(0,7);
        whereBuffer.insert(0," and ");
      }
    }
    return whereBuffer;
  }

  public static StringBuffer getSpecialWhereStmt(HashMap map){
    StringBuffer whereBuffer = getWhereStmt(map);
    whereBuffer.delete(0,7);
    return whereBuffer;
  }

  public static StringBuffer getWhereStmt(HashMap map){
    StringBuffer whereBuffer = new StringBuffer();
    Set key = map.keySet();
    int size = key.size();
    if (size > 0){
      Iterator iter = key.iterator();
      String col,fieldName,whereStmt;
      while(iter.hasNext()){
        col = (String)iter.next();
        if (col.startsWith("&")){
          fieldName = col.substring(1);
        } else{
          fieldName = col;
        }
        whereStmt = getFieldSqlStmt(fieldName,map.get(col));
        if (whereStmt.length() > 0){
          whereBuffer.append(whereStmt);
          whereBuffer.append(" and ");
        }
      }
      if (whereBuffer.length() > 0){
        whereBuffer.insert(0," where ");
        whereBuffer = whereBuffer.delete(whereBuffer.length()-4,whereBuffer.length());
      }
    }
    return whereBuffer;
  }

  private static String getFieldSqlStmt(String fieldName,
                                        Object limit){
    if (limit == null){
      return "";
    } else if (limit instanceof List){
      return getFieldStmt(fieldName,(List)limit);
    } else if (limit instanceof Boolean){
      if (fieldName.startsWith("!")){
        return fieldName.substring(1) + " is not null";
      } else {
        return fieldName + " is null";
      }
    } else{
      String tmp = limit.toString();
      if (tmp.length() < 1){
        return "";
      }
      if (tmp.indexOf(",") == -1){
        return getFieldStmt(fieldName,tmp);
      }
      StringTokenizer token = new StringTokenizer(tmp,",");
      ArrayList list = new ArrayList();
      StringBuffer result = new StringBuffer();
      String value = "";
      while (token.hasMoreTokens()){
        value = token.nextToken();
        if (value.indexOf("*") == -1){
          list.add(value);
        } else {
          result.append(getFieldStmt(fieldName,value));
          result.append(" or ");
        }
      }
      value = getFieldStmt(fieldName,list);
      if (value.length() <= 0){
        result.delete(result.length()-4,result.length());
      }
      result.append(value);
      if (result.length() > 0){
        result.insert(0,"( ");
        result.insert(result.length()," )");
      }
      return result.toString();
    }
  }

  private static String getFieldStmt(String fieldName,
                                     List queryList){
    StringBuffer result = new StringBuffer();
    int size = queryList.size();
    if (size == 0){
      return "";
    } else if (size == 1){
      result.append(getFieldStmt(fieldName,queryList.get(0).toString()));
    } else if (queryList.get(0) instanceof Date && size == 2){
      result.append(fieldName);
      result.append(" between");
      result.append(" to_date('");
      result.append(((Date)queryList.get(0)).toString());
      if (((Date)queryList.get(0)).toString().indexOf(":") < 0){
        result.append(" 00:00:00','yyyy-mm-dd HH24:MI:SS') and to_date('");
      } else {result.append("','yyyy-mm-dd HH24:MI:SS') and to_date('");}
      result.append(((Date)queryList.get(1)).toString());
      if (((Date)queryList.get(0)).toString().indexOf(":") < 0){
        result.append(" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
      } else {result.append("','yyyy-mm-dd HH24:MI:SS')");}
    } else if (queryList.get(0) instanceof DateString && size == 2){
      result.append(fieldName);
      result.append(" between");
      result.append(" to_date('");
      result.append(((DateString)queryList.get(0)).toString());
      if (((DateString)queryList.get(0)).toString().indexOf(":") < 0){
        result.append(" 00:00:00','yyyy-mm-dd HH24:MI:SS') and to_date('");
      } else {result.append("','yyyy-mm-dd HH24:MI:SS') and to_date('");}
      result.append(((DateString)queryList.get(1)).toString());
      if (((DateString)queryList.get(1)).toString().indexOf(":")<0){
        result.append(" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
      } else {result.append("','yyyy-mm-dd HH24:MI:SS')");}
    } else{
      if (fieldName.startsWith("!")){
        result.append(fieldName.substring(1));
        result.append(" not ");
      } else {
        result.append(fieldName);
      }
      result.append(" in (");
      for(int i = 0;i < size; i++){
        result.append("'");
        result.append(replace(queryList.get(i)));
        result.append("',");
        //result.append(getFieldStmt(fieldName,queryList.get(i).toString()));
        //result.append(" or ");
      }
      result = result.delete(result.length() - 1 ,result.length());
      //result = result.delete(result.length() - 3 ,result.length());
      result.append(")");
    }
    return result.toString();
  }

  private static String getFieldStmt(String fieldName,
                                     String limit){
    StringBuffer result = new StringBuffer();
    boolean notEquit = false;
    if (fieldName.startsWith("!")){
      result.append(fieldName.substring(1));
      notEquit = true;
    } else if (fieldName.startsWith("#")){
      result.append(fieldName.substring(1));
      result.append(" = ");
      result.append(limit);
      return result.toString();
    } else {
      result.append(fieldName);
    }
    if (limit == null || limit.equals("*") || limit.length() < 1){
      return "";
    }
    if (limit.indexOf("*") == -1 && limit.indexOf('%') == -1){
      if (notEquit){
        result.append(" <> '");
      } else {
        result.append(" = '");
      }
      result.append(replace(limit));
      result.append("'");
    } else {
      limit = limit.replace('*','%');
      if (notEquit)
        result.append(" not ");
      result.append(" like '");
      result.append(replace(limit));
      result.append("'");
    }
    return result.toString();
  }

  private static String replace(Object value){
    return StringUtil.replace((String)value,"'","''",true);
    //return StringUtil.replace(((String)value).trim(),"'","''",true);
  }

  protected static void addSubQuery(StringBuffer sql,
                                    String field,
                                    String table,
                                    String subQueryField,
                                    HashMap map){
    sql.append(" and ");
    sql.append(field);
    sql.append(" in (select ");
    sql.append(subQueryField);
    sql.append(" from ");
    sql.append(table);
    sql.append(getWhereStmt(map));
    sql.append(")");
  }

  public static List getAllBasicData(Connection connection,
                                     String tableName,
                                     String columnName,
                                     HashMap map) throws Exception{
    List tmp = new ArrayList();
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("select distinct " + columnName + " from " +
                                       tableName + " " + getWhereStmt(map).toString());
      while (rs.next()){
        tmp.add(rs.getString(1));
      }
      rs.close();
      stmt.close();
    } finally {
      return tmp;
    }
  }

  public static List getAllBasicData(String tableName,
                                     String columnName) throws Exception{
    Connection connection = null;
    List tmp = new ArrayList();
    try {
      connection = DBConnection.getConnection();
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("select distinct " +
                                       columnName + " from " + tableName);
      while (rs.next()){
        tmp.add(rs.getString(1));
      }
      rs.close();
      stmt.close();
    } finally {
      DBConnection.close(connection);
    }
    return tmp;
  }

  public static List getAllBasicData(String tableName,
                                     String columnName,
                                     HashMap crit) throws Exception{
    Connection connection = null;
    List tmp = new ArrayList();
    try {
      connection = DBConnection.getConnection();
      Statement stmt = connection.createStatement();
      String sql = "select distinct " + columnName + " from " +
          tableName + " " + getWhereStmt(crit).toString();
      ResultSet rs = stmt.executeQuery(sql);
      TDSLogger.println(sql);
      while (rs.next()){
        tmp.add(rs.getString(1));
      }
      rs.close();
      stmt.close();
    } finally {
      DBConnection.close(connection);
    }
    return tmp;
  }

  public static ArrayList getDateList(String start,
                                      String end){
    if (start == null || start.length()<=0){
      return null;
    }
    ArrayList list = new ArrayList();
    list.add(DateUtil.valueOf(start));
    list.add(DateUtil.valueOf(end));
    return list;
  }

  // end of sql util
  public static void main(String[] args) {
    Connection conn = null;
    try {
      StringBuffer sql = new StringBuffer();
      conn = DBConnection.getConnection();
      //Insert Data
      sql.append("insert into ts_leavetype (name,description) values (?,?)");
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ps.setString(1,StringUtil.Big5ToUtf8("ㄆ安"));
      ps.setString(2,StringUtil.Big5ToUtf8("3ぱ玡ビ叫"));
      ps.executeUpdate();

      sql.delete(0,sql.length());
      sql.append("select * from ts_leavetype ");
      ResultSet rs = ps.executeQuery(sql.toString());
      if (rs != null){
        while (rs.next()){
        	TDSLogger.println(StringUtil.Utf8ToBig5(rs.getString("name")));
        }
      }
    } catch (Exception ex){
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
    }
  }
}
