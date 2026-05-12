package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.*;
import java.sql.*;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;


public class GPRSDB {
  /**
   *
   * @param con
   * @param prepareSql
   * @param objs
   * @return
   */
	
  public static ArrayList qryListBySql(Connection con, String prepareSql,
                                       Object[] objs) throws Exception {
    ArrayList result = new ArrayList();
    try {
        TDSLogger.println(prepareSql);
    	PreparedStatement ps = con.prepareStatement(prepareSql);
    	TDSLogger.println("------------------------");
		for (int i = 0; i < objs.length; i++) {
	    	if(objs[i] == null) {
	    		ps.setNull(i + 1, java.sql.Types.VARCHAR);
	    	}else{
	    	    ps.setObject(i + 1, objs[i]);
	    	}
			//ps.setObject(i + 1, objs[i]);
	    	TDSLogger.println(objs[i]);
		}
    	TDSLogger.println("------------------------");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			result.add(rs.getString(1));
		}
		if (ps != null) {
			ps.close();
			ps = null;
		}
		if (rs != null) {
			rs.close();
			rs = null;
		}
    }catch (Exception e) {
      TDSLogger.println(prepareSql);
      TDSLogger.println(e);
      throw e;
    }finally{
    	TDSLogger.println("Query Method:"+new Exception().getStackTrace()[1].getClassName()+"."+new Exception().getStackTrace()[1].getMethodName());
   }
    return result;
  }

  /**
   *
   * @param con
   * @param prepareSql
   * @param objs
   * @return
   */
  public static int qryCnt(Connection con, String prepareSql, Object[] objs) throws
      Exception {
    int result = 0;
    try {
    	TDSLogger.println(prepareSql);
      PreparedStatement ps = con.prepareStatement(prepareSql);
  	TDSLogger.println("------------------------");
      for (int i = 0; i < objs.length; i++) {
    	if(objs[i] == null) {
    		ps.setNull(i + 1, java.sql.Types.VARCHAR);
    	}else{
    	    ps.setObject(i + 1, objs[i]);
    	}
        //ps.setObject(i + 1, objs[i]);
    	TDSLogger.println(objs[i]);
      }
  	TDSLogger.println("------------------------");
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
    	if(rs.getString(1) != null)  
    		result = Integer.parseInt(rs.getString(1));
      }
      if (ps != null) {
        ps.close();
        ps = null;
      }
      if (rs != null) {
        rs.close();
        rs = null;
      }
    }
    catch (Exception e) {
    	TDSLogger.println(prepareSql);
    	TDSLogger.println(e);
      throw e;
    }finally{
    	TDSLogger.println("Query Method:"+new Exception().getStackTrace()[1].getClassName()+"."+new Exception().getStackTrace()[1].getMethodName());
    }
    return result;
  }

  /**
   *
   * @param rs
   * @return
   * @throws java.lang.Exception
   */
  public static HashMap setRStoHashMap(ResultSet rs) throws Exception {
    HashMap hm = new HashMap();
    ResultSetMetaData rsmd = rs.getMetaData();
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      String columnName = rsmd.getColumnName(i);
      hm.put(columnName, rs.getString(columnName));
    }
    return hm;
  }

  /**
   *
   * @param conn
   * @param prepareSql
   * @param values
   * @return
   */
  public static HashMap[] qryHashMapBySql(Connection conn, String prepareSql,
                                          Object[] values) throws Exception {
    ArrayList tmp = new ArrayList();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      TDSLogger.println(prepareSql);
      ps = conn.prepareStatement(prepareSql);
    	TDSLogger.println("------------------------");
      for (int i = 0; i < values.length; i++) {
        //ps.setObject(i + 1, values[i]);
    	if(values[i] == null) {
    		ps.setNull(i + 1, java.sql.Types.VARCHAR);
    	}else{
    	    ps.setObject(i + 1, values[i]);
    	}
      	TDSLogger.println(values[i]);
      }
    	TDSLogger.println("------------------------");
      rs = ps.executeQuery();
      while (rs.next()) {
        tmp.add(setRStoHashMap(rs));
      }  
    }
    catch (Exception e) {
    	TDSLogger.println(prepareSql);
    	TDSLogger.println(e);
      throw e;
    }finally{
    	 if (ps != null) {
             ps.close();
             ps = null;
           }
         if(rs!=null){
            rs.close();
            rs = null;
         }   
    	TDSLogger.println("Query Method:"+new Exception().getStackTrace()[1].getClassName()+"."+new Exception().getStackTrace()[1].getMethodName());
    }

    return (HashMap[]) tmp.toArray(new HashMap[0]);
  }
  
	public static HashMap[] qryHashMapBySqladdTimeStamp(Connection conn, String prepareSql, Object[] values) throws Exception {
		TDSLogger.println("開始執行:" + DateUtil.getNow());
		HashMap[] h = new HashMap[0];
		h = qryHashMapBySql(conn, prepareSql, values);
		TDSLogger.println("結束執行:" + DateUtil.getNow());
		return h;
	}

  /**
   *
   * @param conn
   * @param sql
   * @param conditions
   * @return
   */
  public static HashMap[] qryHashMapBySql(Connection con, String sql,
                                          HashMap conditions) throws Exception {
    StringBuffer sqlb = new StringBuffer(sql);
    Object[] fieldValues = new Object[conditions.size()];
    Iterator itr = conditions.keySet().iterator();
    int index = 0;
    while (itr.hasNext()) {
      String fieldName = (String) itr.next();
      if (index == 0) {
        sqlb.append(" where ");
      }
      else {
        sqlb.append(" and ");
      }
      sqlb.append(fieldName);
      sqlb.append(" =? ");
      fieldValues[index++] = conditions.get(fieldName);
    }
    return qryHashMapBySql(con, sqlb.toString(), fieldValues);
  }

  /**
   *
   * @param conn
   * @param sql
   * @param objs
   * @return
   * @throws SQLException
   */
  public static ResultSet qryRSBySql(Connection conn, String sql,
                                     Object[] objs) throws SQLException {
	  TDSLogger.println(sql);
    PreparedStatement ps = conn.prepareStatement(sql);
	TDSLogger.println("------------------------");
    for (int i = 0; i < objs.length; i++) {
    	if(objs[i] == null) {
    		ps.setNull(i + 1, java.sql.Types.VARCHAR);
    	}else{
    	    ps.setObject(i + 1, objs[i]);
    	}
  	TDSLogger.println(objs[i]);
    }
	TDSLogger.println("------------------------");
    return ps.executeQuery();
  }

  /**
   * insert date into the table, with HaspMap ("filedName", Object)
   * @param conn Connection
   * @param tablename
   * @param values
   * @return true for success, false for fail
   * @throws Exception
   */
  public static boolean insert(Connection conn, String tablename,
                               HashMap values) throws Exception {
    // Generate an array of question marks for the SQL template parameters
    String[] paras = new String[values.size()];
    for (int i = 0; i < paras.length; i++) {
      paras[i] = "?";
    }
    String[] fieldnames = new String[values.size()];
    Object[] fieldvalues = new Object[values.size()];
    Iterator itr = values.keySet().iterator();
    int index = 0;
    while (itr.hasNext()) {
      fieldnames[index++] = (String) itr.next();
      fieldvalues[index - 1] = values.get(fieldnames[index - 1]);
    }
    // Prepare the template
    String SQLstr = "INSERT INTO " + tablename + " ( " +
        getSQLList(fieldnames) + " ) VALUES ( " +
        getSQLList(paras) + " ) ";
    TDSLogger.println(SQLstr);
    PreparedStatement pstmt = conn.prepareStatement(SQLstr);
	TDSLogger.println("------------------------");
    // set parameter values
    for (int i = 0; i < fieldnames.length; i++) {
      if(fieldvalues[i] == null) {
    	  pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
      }else if (fieldvalues[i] instanceof String) {
    	  String data = (String)fieldvalues[i];
    	  //String encoding = System.getProperty("file.encoding");
    	  //TDSLogger.println("Encoding : " + encoding );
    	  if(data.getBytes().length>=1000){
    		  //TDSLogger.println("1 : test : " + data + "," + data.getBytes().length);
    		  pstmt.setCharacterStream(i + 1, new InputStreamReader(new ByteArrayInputStream(data.getBytes())), data.getBytes().length);
    		  //pstmt.setBinaryStream(i + 1, new ByteArrayInputStream(data.getBytes()), data.getBytes().length);
    		  //ok--pstmt.setCharacterStream(i + 1,  new StringReader(data.toString()), data.getBytes().length);
    	  }else if(data.length()==0){
    		  pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
    	  }else{
    		  //TDSLogger.println("2 : test : " + data + "," + data.getBytes().length);
    		  pstmt.setCharacterStream(i + 1, new InputStreamReader(new ByteArrayInputStream(data.getBytes())), data.getBytes().length);
    		  //pstmt.setObject(i + 1, fieldvalues[i]);
    		  //ok--pstmt.setCharacterStream(i + 1,  new StringReader(data.toString()), data.getBytes().length);
    	  }
      }else{	  
    	  pstmt.setObject(i + 1, fieldvalues[i]);
      }	 
  		TDSLogger.println(fieldvalues[i]); 
    }
	TDSLogger.println("------------------------");
    // execute SQL statement
    boolean res = pstmt.execute();
    pstmt.close();
    return res;
  }

  /**
   *
   * @param con
   * @param insSQL
   * @return
   * @throws java.lang.Exception
   */
  public static boolean execDML(Connection con, String prepareStatementSQL,
                                Object[] values) throws Exception {

  	TDSLogger.println(prepareStatementSQL);
    PreparedStatement pstmt = con.prepareStatement(prepareStatementSQL);
	TDSLogger.println("------------------------");
    for (int i = 0; i < values.length; i++) {
    	if(values[i]==null)
    		values[i]="";
      pstmt.setObject(i + 1, values[i]);
  	TDSLogger.println(values[i]);
    }
	TDSLogger.println("------------------------");
    boolean succ = pstmt.execute();
    pstmt.close();
    return succ;
  }

  /**
   *
   * @param fields
   * @return
   * @throws java.lang.Exception
   */
  private static String getSQLList(String[] fields) throws Exception {

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fields.length - 1; i++) {
      result.append(fields[i]);
      result.append(" , ");
    }
    result.append(fields[fields.length - 1]);
    return result.toString();
  }

  /**
   *
   * @param conn
   * @param tablename
   * @param conditions
   * @return
   * @throws java.lang.Exception
   */
  public static boolean delete(Connection conn, String tablename,
                               HashMap conditions) throws Exception {

    // Generate an array of question marks for the SQL template parameters
    String[] paras = new String[conditions.size()];
    for (int i = 0; i < paras.length; i++) {
      paras[i] = "?";
    }
    String[] fieldnames = new String[conditions.size()];
    Object[] fieldvalues = new Object[conditions.size()];
    Iterator itr = conditions.keySet().iterator();
    int index = 0;
    while (itr.hasNext()) {
      fieldnames[index++] = (String) itr.next();
      fieldvalues[index - 1] = conditions.get(fieldnames[index - 1]);
    }

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fieldnames.length - 1; i++) {
      result.append("(" + fieldnames[i] + " = ? )");
      result.append(" AND ");
    }
    if(fieldnames.length!=0)
    result.append("(" + fieldnames[fieldnames.length - 1] + " = ? )");

    // Prepare the template
    String SQLstr = "DELETE " + tablename ;
    if(result.length()!=0)
    	SQLstr += " WHERE ( " + result.toString() + " ) ";
    
    TDSLogger.println(SQLstr);
    PreparedStatement pstmt = conn.prepareStatement(SQLstr);
	TDSLogger.println("------------------------");
    // set parameter values
    for (int i = 0; i < fieldnames.length; i++) {
      pstmt.setObject(i + 1, fieldvalues[i]);
  	  TDSLogger.println(fieldvalues[i]);
    }
	TDSLogger.println("------------------------");
    // execute SQL statement
    boolean succ = pstmt.execute();
    pstmt.close();
    return succ;
  }

  /**
   *
   * @param con
   * @param facility
   * @param vendorNo
   * @param prodBody
   * @return
   * @throws java.lang.Exception
   */
  protected static boolean delSE_BUFFER_PRODUCT(Connection con, int facility,
                                                String vendorNo,
                                                String prodBody) throws
      Exception {
    HashMap hm = new HashMap();
    hm.put("FACILITY", String.valueOf(facility));
    hm.put("SAP_PLANT_NO", vendorNo);
    hm.put("PRODUCT_BODY", prodBody);
    return delete(con, "SE_BUFFER_PRODUCT", hm);
  }

  /**
   *
   * @param rs
   * @param somebean
   * @return
   * @throws java.lang.Exception
   */
  public static Object RStoObjectBean(ResultSet rs, Class somebean) throws
      Exception {
    if (rs == null) {
      return null;
    }
    Object bean = somebean.newInstance();
    ResultSetMetaData rsmd = rs.getMetaData();
    String cloumnname = null;
    Object[] value = new Object[1];
    Method[] methods = bean.getClass().getMethods();

    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      cloumnname = rsmd.getColumnName(i);
      value[0] = rs.getObject(i);

      for (int j = 0; j < methods.length; j++) {
        if ( (methods[j].getName().startsWith("set")) &&
            (methods[j].getName().substring(3).
             compareToIgnoreCase(cloumnname) == 0)) {

          if (value[0] != null &&
              !value[0].getClass().getName().equals(methods[j].
              getParameterTypes()[0].getName())) {

            String str = value[0].toString();
            if (value[0].getClass().getName().equals("java.sql.Date") ||
                value[0].getClass().getName().equals("java.sql.Timestamp")) {
            	Timestamp date = rs.getTimestamp(i);
				str = date.toString();
              if (str.length() > 10 &&
                  str.substring(11).equals("00:00:00.0")) {
                str = str.substring(0, 10);
              }
              else if (str.length() > 19) {
                str = str.substring(0, 19);
              }
            }
            value[0] = str;
          }
          methods[j].invoke(bean, value);
        }
      }
    }
    return bean;
  }

  /**
   *
   * @param conn
   * @param tablename
   * @param values
   * @param conditions
   * @return
   * @throws java.lang.Exception
   */
  public static boolean update(Connection conn, String tablename,
                               HashMap values, HashMap conditions) throws
      Exception {
    // Generate an array of question marks for the SQL template parameters
    String[] paras = new String[values.size()];
    for (int i = 0; i < paras.length; i++) {
      paras[i] = "?";
    }
    String[] fieldnames = new String[values.size()];
    Object[] fieldvalues = new Object[values.size()];
    Iterator itr = values.keySet().iterator();
    int index = 0;
    while (itr.hasNext()) {
      fieldnames[index++] = (String) itr.next();
      fieldvalues[index - 1] = values.get(fieldnames[index - 1]);
    }
    String[] condnames = new String[conditions.size()];
    Object[] condvalues = new Object[conditions.size()];
    itr = conditions.keySet().iterator();
    index = 0;
    while (itr.hasNext()) {
      condnames[index++] = (String) itr.next();
      condvalues[index - 1] = conditions.get(condnames[index - 1]);
    }

    // Prepare the template
    String SQLstr = "UPDATE " + tablename + " SET " +
        getSETList(fieldnames, fieldvalues) + " WHERE  (" +
        getANDList(condnames) + ") ";
    TDSLogger.println("GPRSDB.update(): " + SQLstr);
    PreparedStatement pstmt = conn.prepareStatement(SQLstr);
	TDSLogger.println("------------------------");
    // set parameter values
    for (int i = 0; i < fieldnames.length; i++) {
      pstmt.setObject(i + 1, fieldvalues[i]);
  	  TDSLogger.println(fieldvalues[i]);
    }
    for (int i = fieldnames.length; i < fieldnames.length + condnames.length; i++) {
      pstmt.setObject(i + 1, condvalues[i - fieldnames.length]);
  	  TDSLogger.println(condvalues[i - fieldnames.length]);
    }
	TDSLogger.println("------------------------");
    // execute SQL statement
    boolean succ = pstmt.execute();
    pstmt.close();
    return succ;
  }

  /**
   * Assembly "arg1=? , arg2=? , ...argn=?" type of string  to use in
   * SQL statements from the input array of strings.
   * @param fields
   * @return
   * @throws Exception
   */
  protected static String getSETList(String[] fields, Object[] vaules) throws Exception {

    StringBuffer result = new StringBuffer();
    boolean tf = true;
    for (int i = 0; i < fields.length; i++) {
      
      tf = true;
      if(fields[i].toLowerCase().endsWith("date")){
    	  String date = String.valueOf(vaules[i]);
    	  //TDSLogger.println("GPRSDB.getSETList() - date: " + date);
    	  if(date!=null && !date.equals("")){
    		  if(date.length() == 10){
	        	  result.append(fields[i] + " = to_date(?, 'yyyy-mm-dd') ");
	    	  } else if(date.length() >= 11){
	        	  result.append(fields[i] + " = to_date(?, 'yyyy-mm-dd HH24:MI:SS') ");
	    	  }
    	  }else{
    		  result.append(fields[i] + " = ? ");
    		  //tf = false;
    	  }
      }else if(fields[i].toLowerCase().endsWith("_time")){
    	  String date = String.valueOf(vaules[i]);
    	  if(date!=null && !date.equals("")){
    		  if(date.length() == 10){
	        	  result.append(fields[i] + " = to_date(?, 'yyyy-mm-dd') ");
	    	  } else if(date.length() >= 11){
	        	  result.append(fields[i] + " = to_date(?, 'yyyy-mm-dd HH24:MI:SS') ");
	    	  }
    	  }else{
    		  result.append(fields[i] + " = ? ");
    		  //tf = false;
    	  }
      }else{
    	  result.append(fields[i] + " = ? ");
      }
      
      if(tf){
    	  result.append(" , ");
      }
    }
    if(result.length()!=0){
    	result = new StringBuffer(result.substring(0, result.length()-2));
    }
    //result.append(  fields[fields.length-1] + " = ? "  );
    return result.toString();
  }

  /**
   * Assembly "arg1=? AND arg2=? AND ...argn=?" type of string  to use in
   * SQL statements from the input array of strings.
   * @param fields
   * @return
   * @throws Exception
   */
  protected static String getANDList(String[] fields) throws Exception {

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fields.length; i++) {
      if (i > 0) {
        result.append(" AND ");
      }
      result.append("(" + fields[i] + " = ? )");
    }
//   result.append(  "(" + fields[fields.length-1] + " = ? )"  );
    return result.toString();
  }

  public static Object[] qryObjBySql(Connection conn, String prepareSql,
                                     Object[] values, Class somebean) throws Exception {
    ArrayList tmp = new ArrayList();
    try {
      PreparedStatement ps = null;
      ResultSet rs = null;
  	  TDSLogger.println(prepareSql);
      ps = conn.prepareStatement(prepareSql);
  	  TDSLogger.println("------------------------");
      for (int i = 0; i < values.length; i++) {
        ps.setObject(i + 1, values[i]);
    	TDSLogger.println(values[i]);
      }
  	  TDSLogger.println("------------------------");
      rs = ps.executeQuery();
      while (rs.next()) {
        tmp.add(RStoObjectBean(rs, somebean));
      }
      if (ps != null) {
          ps.close();
          ps = null;
      }
      if (rs != null) {
          rs.close();
          rs = null;
      }
    }
    catch (Exception e) {
    	TDSLogger.println(prepareSql);
    	TDSLogger.println(e);
      throw e;
    }
    return (Object[]) tmp.toArray(new Object[tmp.size()]);

  }
  
//DataListMulti, 顯示多個欄位
  
	public static String[][] getStringArraysBySql(Connection con, String prepareSql, Object[] objs) throws Exception {
		ArrayList tmp = new ArrayList();
		try {

			PreparedStatement ps = null;
			ResultSet rs = null;
		  	TDSLogger.println(prepareSql);
			ps = con.prepareStatement(prepareSql);
		  	TDSLogger.println("------------------------");
			for (int i = 0; i < objs.length; i++) {
				ps.setObject(i + 1, objs[i]);
			  	TDSLogger.println(objs[i]);
			}
		  	TDSLogger.println("------------------------");
			rs = ps.executeQuery();
			while (rs.next()) {
				ArrayList a = setRStoStringArray(rs);
				tmp.add((String[]) a.toArray(new String[0]));
			}
		} catch (Exception e) {
			TDSLogger.println(prepareSql);
			TDSLogger.println(e);
			throw e;
		}
		return (String[][]) tmp.toArray(new String[0][0]);
	}
	public static ArrayList setRStoStringArray(ResultSet rs) throws Exception {
		ArrayList a = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String columnName = rsmd.getColumnName(i);
			a.add(rs.getString(columnName));
		}
		return a;
	}

	public static HashMap[] qryLinkedHashMapBySql(Connection conn, String prepareSql, Object[] values) throws Exception {
		ArrayList tmp = new ArrayList();
		try {
			PreparedStatement ps = null;
			ResultSet rs = null;
		  	  TDSLogger.println(prepareSql);
			ps = conn.prepareStatement(prepareSql);
		  	  TDSLogger.println("------------------------");
			for (int i = 0; i < values.length; i++) {
				ps.setObject(i + 1, values[i]);
			  	  TDSLogger.println(values[i]);
			}
		  	  TDSLogger.println("------------------------");
			rs = ps.executeQuery();
			while (rs.next()) {
				tmp.add(setRStoLinkedHashMap(rs));
			}
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			TDSLogger.println(e);
			throw e;
		} finally {
			TDSLogger.println(prepareSql);

		}

		return (HashMap[]) tmp.toArray(new HashMap[0]);
	}

	public static HashMap setRStoLinkedHashMap(ResultSet rs) throws Exception {
		LinkedHashMap hm = new LinkedHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String columnName = rsmd.getColumnName(i);
			hm.put(columnName, rs.getString(columnName));
		}
		return hm;
	}
}