package com.mxic.oiplus.apl;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

//import mic.af.aplrms.*;

//import com.mxic.tdsplus.util.DataHandlerUtil;
//import com.mxic.tdsplus.ws.helper.DataHandlerException;

public class APLUtil {
  public static Collection getData(Connection conn,
                                   String query,
                                   Class loadedBean,
                                   Object[] whereConditions) throws Exception {
    return new MyDataHandlerUtil().getData(conn, query, loadedBean, whereConditions);
  }

  public static int update(Connection conn,
                           String preparesqlstring,
                           Object[] objs) throws Exception{
    return new MyDataHandlerUtil().updateByPrepareSQL(conn, preparesqlstring, objs);
  }

  public static int delete(Connection conn,
                           String tablename,
                           HashMap conditions) throws Exception{
    return new MyDataHandlerUtil().delete(conn, tablename, conditions);
  }

  public static String[] split(String str, String delimiter) {
    if ( str == null )
      return null;
    StringTokenizer parser = new StringTokenizer( str, delimiter );
    String[] value = new String[ parser.countTokens() ];
    int i = 0;
    while (parser.hasMoreTokens()) {
      value[i++] = parser.nextToken();
    }
    return value;
  }

  public static String join(String[] values, String connector) {
    if ( values == null || values.length == 0 )
      return "";
    StringBuffer sb = new StringBuffer( values.length << 3 );
    for ( int i = 0; i < values.length; i++ ) {
      sb.append( ( values[i] == null ? "" : values[i] ) + ( i < values.length - 1 ? connector : "" ) );
    }
    return sb.toString();
  }

  public static String toString(String val, String enc1, String enc2) { // "Big5", "ISO8859_1"
    try {
      if (val != null)
        val = new String(val.getBytes(enc1), enc2);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return val;
  }

  public static String getExt(String filename) { // "Big5", "ISO8859_1"
    String extName = "";
    String[] exts = filename.split("\\.");
    if (exts.length > 1)
      extName = "."+exts[exts.length-1];
    return extName;
  }
  /*
  public static boolean setReject(String TaskID) { // 知會 AF 退件
    String AFServerIP = (String) TDSResource.getProperties("APL").get("af_server_ip");
    APLManager aplManager = APLManager.getInstance(AFServerIP);

    if (!aplManager.isAlive()) {
      TDSLogger.println("AgentFlow Server is not alive now !!");
      return false;
    }
    try {
      TaskProgress tp = aplManager.getApplyStatus(TaskID);
      aplManager.abort(tp.getTaskID());
      tp = null;
    } catch (Exception ex) {
      return false;
    }
    return true;
  }
  */
	/**
	 * 重送
	 * @param contextPath
	 * @param appNo
	 * @return
	 */
	public static boolean resend(String contextPath, String appNo) {
		APLAppSignAction action = new APLAppSignAction();
		APLAppSignActionForm fm = new APLAppSignActionForm();
		fm.setApp_no(appNo);
		HashMap flow = new HashMap();
	    Connection con = null;
	    try {
	    	con = DBConnection.getConnection();
	    	action.doLoad(fm);//讀取表單
			flow = action.getNextRouteFlow(fm);//取得下站流程
			action.doSendMail(con, contextPath, flow, fm);//寄mail
		} catch (Exception e) {
			TDSLogger.println(e);
			return false;
		} finally {
			DBConnection.close(con);
		}
		return true;
	}	
}

class MyDataHandlerUtil extends Object {

  public void setStatement(PreparedStatement preparedStatement,int index,Object value,int sqlType)
  {
    try
    {
      TDSLogger.println(index+" "+value);

      if(sqlType == Types.VARBINARY || sqlType == Types.VARCHAR) {
        if(value!=null){
          preparedStatement.setString(index,value.toString());
        } else {
          preparedStatement.setNull(index,Types.VARCHAR);
        }
      }
      else if(sqlType == Types.NUMERIC || sqlType == Types.INTEGER || sqlType == Types.FLOAT || sqlType == Types.DOUBLE || sqlType == Types.DECIMAL || sqlType == Types.BINARY) {
        if(value!=null) {
          preparedStatement.setObject(index,value);
        } else {
          preparedStatement.setNull(index,Types.NUMERIC);
        }
      }
      else if(sqlType == Types.DATE || sqlType == Types.TIME || sqlType == Types.TIMESTAMP) {
        if(value!=null) {
          if(value instanceof Timestamp) {
            preparedStatement.setTimestamp(index,(Timestamp) value);
          } else {
            preparedStatement.setTimestamp(index,new Timestamp(((java.sql.Date)value).getTime()));
          }
        } else {
          preparedStatement.setTimestamp(index,null);
        }
      }
    }
    catch(SQLException sqlException) {
      TDSLogger.println(sqlException.toString());
      sqlException.printStackTrace();
    }
  }

  public Collection getData(Connection connection,
                            String query,
                            Class loadedBean,
                            Object[] whereConditions) throws Exception {
    Collection lists = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
      if (connection == null || query == null) {
        throw new Exception("Provide Proper Parameters for getData Method.");
      }

      TDSLogger.println("==========================================================================================================");
      TDSLogger.println(query);
      TDSLogger.println("==========================================================================================================");

      preparedStatement = connection.prepareStatement(query);

      if (whereConditions != null) {
        int number = whereConditions.length;

        for (int i = 0; i < number; i++) {
          Object value = whereConditions[i];

// 20061218, marked this for speed
//          log("Parameter ---------------> " + (i + 1) + " = \'" + value + "\'");

          if (value != null) {
            if (value instanceof String) {
              //log("Setting as String");
              setStatement(preparedStatement, i + 1, value.toString().trim(), java.sql.Types.VARBINARY);
            } else if (value instanceof Number) {
              //log("Setting as Number");
              setStatement(preparedStatement, i + 1, value, java.sql.Types.NUMERIC);
            } else if (value instanceof java.sql.Date) {
              //log("Setting as Date");
              setStatement(preparedStatement, i + 1, value, java.sql.Types.DATE);
            } else {
              //log("Finally String");
              setStatement(preparedStatement, i + 1, value.toString().trim(), java.sql.Types.VARBINARY);
            }
          } else {
            preparedStatement.setNull(i + 1, java.sql.Types.VARBINARY);
          }
        }
      }

      TDSLogger.println("Get data --- executeQuery ");

      resultSet = preparedStatement.executeQuery();

      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

      int columnCount = resultSetMetaData.getColumnCount();
      String columnName[] = new String[columnCount+1];
      for (int c = 1; c <= columnCount; c++)
        columnName[c] = resultSetMetaData.getColumnName(c);

      Field field[] = new Field[columnCount+1];
      for (int c = 1; c <= columnCount; c++) {
        try {
          field[c] = loadedBean.getField(columnName[c]);
        }
        catch (NoSuchFieldException e) {
          field[c] = null;
        }
      }


      lists = new ArrayList();
      TDSLogger.println("Get data --- begin ");
      while (resultSet.next()) {
        Object object = loadedBean.newInstance();
        for (int c = 1; c <= columnCount; c++) {

          if (field[c] == null)
            continue;

/*          try {
          }
          catch (NoSuchFieldException e) {
            field = null;
          }
          if (field == null) {
            continue;
          }
*/

          int sqlType = resultSetMetaData.getColumnType(c);

          if (sqlType == Types.VARCHAR ||
              sqlType == Types.CHAR ||
              sqlType == Types.VARBINARY) {
            field[c].set(object, resultSet.getString(c));
          } else if (sqlType == Types.NUMERIC ||
                     sqlType == Types.INTEGER ||
                     sqlType == Types.FLOAT ||
                     sqlType == Types.DOUBLE ||
                     sqlType == Types.DECIMAL ||
                     sqlType == Types.BINARY) {
            field[c].set(object, (Number) resultSet.getObject(c));
          } else if (sqlType == Types.TIMESTAMP ||
                     sqlType == Types.DATE ||
                     sqlType == Types.TIME) {
            Timestamp value = resultSet.getTimestamp(c);

            if (value != null) {
              field[c].set(object, new java.sql.Date(value.getTime()));
            }
          }
        }
        lists.add(object);

      }
      TDSLogger.println("Get data --- end ");
      columnName = null;
    }
    catch (Exception e) {
      TDSLogger.println(e.toString());
      throw new Exception(e.getMessage());
    }
    finally {
      try {
        if (resultSet != null) {
          resultSet.close();
          resultSet = null;
        }
        if (preparedStatement != null) {
          preparedStatement.close();
          preparedStatement = null;
        }
      }
      catch (Exception e) {
      }
    }

    if (lists.size() < 1) {
      TDSLogger.println("----------------------------------------------------------------------------------------------");
      TDSLogger.println("<< Warning >> No Rows has Been Selected in this query. Please Check the query and Conditions");
      TDSLogger.println("----------------------------------------------------------------------------------------------");
    } else {
      TDSLogger.println("----------------------------------------------------------------------------------------------");
      TDSLogger.println("<< Message >> " + lists.size() + " Rows Selected.");
      TDSLogger.println("----------------------------------------------------------------------------------------------");
    }

    return lists;
  }

  public int updateByPrepareSQL(Connection conn,
                                String preparesqlstring,
                                Object[] objs) throws Exception{
    PreparedStatement pstmt = conn.prepareStatement(preparesqlstring);
    // set parameter values
    if(objs!=null){
      for (int i = 0; i < objs.length; i++) {
        pstmt.setObject(i + 1, objs[i]);
      }
    }
    // execute SQL statement
    return pstmt.executeUpdate();
  }

  public int delete(Connection conn,
                    String tablename,
                    HashMap conditions) throws Exception{
    // return super.delete(conn, tablename, conditions);
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

  protected String getANDList(String [] fields) throws Exception {

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < fields.length -1; i++) {
      result.append( "(" + fields[i] + " = ? )" );
      result.append( " AND " );
    }
    result.append(  "(" + fields[fields.length-1] + " = ? )"  );
    return result.toString();
  }
}
