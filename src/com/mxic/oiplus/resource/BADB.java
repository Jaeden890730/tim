package com.mxic.oiplus.resource;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import com.mxic.oiplus.util.*;


public class BADB {

  public BADB() {
  }
  public static void main(String[] args) {
    BADB BADB1 = new BADB();
  }

  /**
   *
   * @param conn
   * @param tablename
   * @param values
   * @param conditions
   * @return
   * @throws Exception
   */
  protected static int update(Connection conn,
                              String tablename,
                              HashMap values,
                              HashMap conditions) throws Exception{
    // Generate an array of question marks for the SQL template parameters
    String [] paras = new String[values.size()];
    for (int i = 0; i < paras.length; i++) {
      paras[i] = "?";
    }
    String [] fieldnames = new String[values.size()];
    Object [] fieldvalues = new Object[values.size()];
    Iterator itr = values.keySet().iterator();
    int index=0;
    while (itr.hasNext()){
      fieldnames[index++]=(String)itr.next();
      fieldvalues[index-1]=values.get(fieldnames[index-1]);
    }
    String [] condnames = new String[conditions.size()];
    Object [] condvalues = new Object[conditions.size()];
    itr = conditions.keySet().iterator();
    index=0;
    while (itr.hasNext()){
      condnames[index++]=(String)itr.next();
      condvalues[index-1]=conditions.get(condnames[index-1]);
    }

    // Prepare the template
    String SQLstr = "UPDATE " + tablename + " SET " +
        getSETList(fieldnames) + " WHERE  (" +
        getANDList(condnames) + ") ";

    PreparedStatement pstmt = conn.prepareStatement(SQLstr);
    // set parameter values
    for (int i = 0; i < fieldnames.length ; i++) {
      pstmt.setObject(i + 1, fieldvalues[i]);
    }
    for (int i = fieldnames.length; i < fieldnames.length + condnames.length ; i++) {
      pstmt.setObject(i + 1, condvalues[i-fieldnames.length]);
    }
    // execute SQL statement
    int succ = pstmt.executeUpdate();
    pstmt.close();
    return succ;
  }

  /**
   * insert date into the table, with HaspMap ("filedName", Object)
   * @param conn Connection
   * @param tablename
   * @param values
   * @return true for success, false for fail
   * @throws Exception
   */
  protected static int insert(Connection conn,
                              String tablename,
                              HashMap values) throws Exception{
    // Generate an array of question marks for the SQL template parameters
    String [] paras = new String[values.size()];
    for (int i = 0; i < paras.length; i++) {
      paras[i] = "?";
    }
    String [] fieldnames = new String[values.size()];
    Object [] fieldvalues = new Object[values.size()];
    Iterator itr = values.keySet().iterator();
    int index=0;
    while(itr.hasNext()){
      fieldnames[index++]=(String)itr.next();
      fieldvalues[index-1]=values.get(fieldnames[index-1]);
    }
    // Prepare the template
    String SQLstr = "INSERT INTO " + tablename + " ( " +
        getSQLList(fieldnames) + " ) VALUES ( " +
        getSQLList(paras) + " ) ";

    PreparedStatement pstmt = conn.prepareStatement(SQLstr);
    TDSLogger.println(SQLstr);
    // set parameter values
    for (int i = 0; i < fieldnames.length; i++) {
      pstmt.setObject(i + 1, fieldvalues[i]);
      //TDSLogger.println(i + ":" + (String)fieldvalues[i]);
    }
    // execute SQL statement
    int succ = pstmt.executeUpdate();
    pstmt.close();
    return succ;
  }

  /**
   *
   * @param conn
   * @param tablename
   * @param conditions
   * @return
   * @throws Exception
   */
  protected static int delete(Connection conn,
                              String tablename,
                              HashMap conditions) throws Exception{

    // Generate an array of question marks for the SQL template parameters
    String [] paras = new String[conditions.size()];
    for (int i = 0; i < paras.length; i++) {
      paras[i] = "?";
    }
    String [] fieldnames = new String[conditions.size()];
    Object [] fieldvalues = new Object[conditions.size()];
    Iterator itr = conditions.keySet().iterator();
    int index=0;
    while(itr.hasNext()){
      fieldnames[index++]=(String)itr.next();
      fieldvalues[index-1]=conditions.get(fieldnames[index-1]);
    }

    // Prepare the template
    String SQLstr = "DELETE " + tablename + " WHERE ( " +
        getANDList(fieldnames) + " ) ";

    PreparedStatement pstmt = conn.prepareStatement(SQLstr);
    // set parameter values
    for (int i = 0; i < fieldnames.length; i++) {
      pstmt.setObject(i + 1, fieldvalues[i]);
    }
    // execute SQL statement
    int succ = pstmt.executeUpdate();
    pstmt.close();
    return succ;
  }

  /**
   *
   * @param conn
   * @param sqlstring
   * @return
   * @throws Exception
   */
  protected static ResultSet queryBySQL(Connection conn,
                                        String sqlstring) throws Exception{
    PreparedStatement pstmt = conn.prepareStatement(sqlstring);
    TDSLogger.println(sqlstring);
    return pstmt.executeQuery();
  }

  /**
   *
   * @param conn
   * @param sqlstring
   * @return
   * @throws Exception
   */
  protected static ResultSet queryByPrepareSQL(Connection conn,
                                               String preparesqlstring,
                                               Object[] objs) throws Exception{
    PreparedStatement pstmt = conn.prepareStatement(preparesqlstring);
    // set parameter values
    if (objs != null){
      for (int i = 0; i < objs.length; i++) {
        pstmt.setObject(i + 1, objs[i]);
      }
    }
    // execute SQL statement
    return pstmt.executeQuery();
  }

  /**
   *
   * @param conn
   * @param sqlstring
   * @return
   * @throws Exception
   */
  protected static boolean executeByPrepareSQL(Connection conn,
                                               String preparesqlstring,
                                               Object[] objs) throws Exception{

    PreparedStatement pstmt = conn.prepareStatement(preparesqlstring);
    // set parameter values
    for (int i = 0; i < objs.length; i++) {
      pstmt.setObject(i + 1, objs[i]);
    }
    // execute SQL statement
    return pstmt.execute();
  }

  /**
   * Assembly "arg1,arg2,...argn" type of string  to use in
   * SQL statements from the input array of strings.
   * @param fields
   * @return
   * @throws Exception
   */
  protected static String getSQLList(String [] fields) throws Exception {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fields.length -1; i++) {
      result.append( fields[i] );
      result.append( " , " );
    }
    result.append( fields[fields.length-1] );
    return result.toString();
  }

  /**
   * Assembly "arg1=? AND arg2=? AND ...argn=?" type of string  to use in
   * SQL statements from the input array of strings.
   * @param fields
   * @return
   * @throws Exception
   */
  protected static String getANDList(String [] fields) throws Exception {

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fields.length -1; i++) {
      result.append( "(" + fields[i] + " = ? )" );
      result.append( " AND " );
    }
    result.append(  "(" + fields[fields.length-1] + " = ? )"  );
    return result.toString();
  }

  /**
   * Assembly "arg1=? , arg2=? , ...argn=?" type of string  to use in
   * SQL statements from the input array of strings.
   * @param fields
   * @return
   * @throws Exception
   */
  protected static String getSETList(String [] fields) throws Exception {

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fields.length -1; i++) {
      result.append( fields[i] + " = ? " );
      result.append( " , " );
    }
    result.append(  fields[fields.length-1] + " = ? "  );
    return result.toString();
  }

  protected static Object RStoObjectBean(ResultSet rs,
                                         Class somebean)  throws Exception{
    long s1 = System.currentTimeMillis();

    if (rs == null){
      return null;
    }
    Object bean = somebean.newInstance();
    ResultSetMetaData rsmd = rs.getMetaData();
    String cloumnname = null;
    Object[] value = new Object[1];
    Method[] methods = bean.getClass().getMethods();
    for(int i = 1; i <= rsmd.getColumnCount(); i++){
      cloumnname = rsmd.getColumnName(i);
      value[0] = rs.getObject(i);
      for(int j=0; j<methods.length; j++){
        if ((methods[j].getName().startsWith("set")) &&
            (methods[j].getName().substring(3,methods[j].getName().length()).compareToIgnoreCase(cloumnname) == 0)){
          methods[j].invoke(bean, value);
        }
      }
    }
    long s2 = System.currentTimeMillis();
    //TDSLogger.println(s2-s1);

    return bean;
  }

  protected static Object RSStringtoObjectBean(ResultSet rs,
                                               Class somebean)  throws Exception{

    long s1 = System.currentTimeMillis();
    if (rs == null){
      return null;
    }
    Object bean = somebean.newInstance();
    ResultSetMetaData rsmd = rs.getMetaData();
    String cloumnname = null;
    Object[] value = new Object[1];
    Method[] methods = bean.getClass().getMethods();
    for(int i = 1; i <= rsmd.getColumnCount(); i++){
      cloumnname = rsmd.getColumnName(i);
      value[0] = rs.getString(i);

      for(int j = 0;j < methods.length; j++){
        if((methods[j].getName().startsWith("set")) &&
           (methods[j].getName().substring(3,methods[j].getName().length()).compareToIgnoreCase(cloumnname) == 0)){
          methods[j].invoke(bean, value);
        }
      }
    }
    long s2 = System.currentTimeMillis();
    //TDSLogger.println(s2-s1);

    return bean;
  }
}
