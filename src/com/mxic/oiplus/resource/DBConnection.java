package com.mxic.oiplus.resource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.DataSource;

import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.resource.TDSResource;

public class DBConnection{

  private static Driver driver = null;
  //private static Properties properties = TDSResource.getProperties("TDS");
  private static ConnectionRepository connectionRepository = null;
  private static Connection recCon = null;

  static{
    try{
      recCon = getConnection();
      recCon.setAutoCommit(true);
    } catch (Exception e){
      TDSLogger.println(e);
    }
  }

  public DBConnection() {
  }

  /**
   * You should not use this method.
   * @throws SQLException
   */
  private static synchronized void loadDriver() throws SQLException {
    Properties properties = TDSResource.getProperties("TDS");
    if (driver != null){
      return;
    }
    try {
      Class c = Class.forName(properties.getProperty("database.driver"));
      DriverManager.setLoginTimeout(60);
    } catch (Exception e) {
      throw new SQLException("Can't found oracle JDBC Driver");
    }
  }

  /**
   * get a Connection based on the resource boundles
   * @return
   * @throws Exception
   */
  public synchronized static Connection getConnection() throws Exception {
    Properties properties = TDSResource.getProperties("TDS");
    try {
      throw new Exception();
    } catch (Exception e) {
      String errmsg = com.mxic.oiplus.util.StringUtil.makeStackTrace(e);
      errmsg = errmsg.substring(errmsg.indexOf("at ") + 3);
      errmsg = errmsg.substring(errmsg.indexOf("at ") + 3);
      //TDSLogger.println("Connection is been asking from " + errmsg);
    }

    try{
      if (properties.getProperty("database.connection.type") != null) {
        if(properties.getProperty("database.connection.type").equals("1")) {
          return getWebLogicConnection();
        } else if(properties.getProperty("database.connection.type").equals("2")) {
          return getOracleConnection();
        } else if(properties.getProperty("database.connection.type").equals("5")) {
          return getJavaConnection();
        }  else if(properties.getProperty("database.connection.type").equals("4")) {
          return getTomcatConnection();
        } else {
          return getOracleConnection();
        }
      } else {
          return getOracleConnection();
        }
      } catch (Exception e){
        TDSLogger.println(e);
        return getOracleConnection();
      }
    }
  public synchronized static Connection getDBLinkConnection() throws Exception {
	  Properties properties = TDSResource.getProperties("TDS");
	    try{
	      throw new Exception();
	    }catch(Exception e)
	    {
	     // String errmsg = StringUtil.makeStackTrace(e);
	     /// errmsg = errmsg.substring(errmsg.indexOf("at ") + 3);
	     // errmsg = errmsg.substring(errmsg.indexOf("at ") + 3);
	      TDSLogger.println("Connection is been asking from " + e.getMessage());
	    }


	         try{
	           if(properties.getProperty("database.connection.type")!=null)
	           {
	             if(properties.getProperty("database.connection.type").equals("1"))
	             {
	               return getWebLogicDBLinkConnection();
	             }
	             else if(properties.getProperty("database.connection.type").equals("2"))
	             {
	               return getOracleConnection();
	             }
	             else if(properties.getProperty("database.connection.type").equals("5"))
	             {
	               return getJavaConnection();
	             }  else if(properties.getProperty("database.connection.type").equals("4")) {
	                 return getTomcatConnection();
	             }
	             else
	             {
	               return getOracleConnection();
	             }
	           }
	           else
	           {
	             return getOracleConnection();
	           }
	         }
	         catch(Exception e){
	           TDSLogger.println(e);
	           return getOracleConnection();
	         }
	     }
  public synchronized static Connection getWebLogicDBLinkConnection() throws Exception {
	  Properties properties = TDSResource.getProperties("TDS");
       Context ctx = null;
       javax.sql.DataSource ds = null;
       java.sql.Connection conn = null;
       // Put connection properties in to a hashtable.
       Hashtable ht = new Hashtable();
       ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");

       ht.put(Context.PROVIDER_URL, "t3://" + properties.getProperty("database.weblogic.host") +
              ":" + properties.getProperty("database.weblogic.port"));
       try {
         // Get a context for the JNDI look up
         ctx = new InitialContext(ht);
         ds = (javax.sql.DataSource) ctx.lookup(properties.getProperty("database.weblogic.dblink.poolid"));
         conn = ds.getConnection();
         conn.setAutoCommit(true);
         ds = null;
         TDSLogger.println("Get WebLogic DBLink connection : " + conn + " " +conn.isClosed() + " ");
         recConnectionGot(conn);
       } catch (NamingException e) {
         TDSLogger.println(e);
       }

       if (conn == null) {
         TDSLogger.println("direct conn");
         return getOracleConnection();
       } else {
         return conn;
       }

       // return getOracleConnection();
     }
  public synchronized static Connection getWebLogicConnection() throws Exception {
    Properties properties = TDSResource.getProperties("TDS");
    Context ctx = null;
    javax.sql.DataSource ds = null;
    java.sql.Connection conn = null;
    // Put connection properties in to a hashtable.
    Hashtable ht = new Hashtable();
    ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");

    ht.put(Context.PROVIDER_URL, "t3://" + properties.getProperty("database.weblogic.host") +
           ":" + properties.getProperty("database.weblogic.port"));
    try {
      // Get a context for the JNDI look up
      ctx = new InitialContext(ht);
      ds = (javax.sql.DataSource) ctx.lookup(properties.getProperty("database.weblogic.poolid"));
      conn = ds.getConnection();
      conn.setAutoCommit(true);
      ds = null;
      TDSLogger.println("Get WebLogic connection : " + conn + " " +conn.isClosed() + " ");
      recConnectionGot(conn);
    } catch (NamingException e) {
      TDSLogger.println(e);
    }

    if (conn == null) {
      TDSLogger.println("direct conn");
      return getOracleConnection();
    } else {
      return conn;
    }

    // return getOracleConnection();
  }

  public synchronized static Connection getOracleConnection() throws Exception {
    Properties properties = TDSResource.getProperties("TDS");
    loadDriver();
    Connection dbConnection = DriverManager.getConnection(properties.getProperty("database.url"),
        properties.getProperty("database.id"),
        properties.getProperty("database.passwd"));
    dbConnection.setAutoCommit(true);
    TDSLogger.println("Get Oracle connection : " + dbConnection + " " +dbConnection.isClosed());
    recConnectionGot(dbConnection);
    return dbConnection;
  }

 /* Method Added by J. Jeyandran for Getting the Java Connection from the Connection Pool */
  public synchronized static Connection getJavaConnection() throws Exception
  {
    Connection dbConnection = null;
    try {
      connectionRepository = ConnectionRepository.getInstance();
      dbConnection = connectionRepository.getConnection();
      TDSLogger.println("Get Java connection : " + dbConnection + " " +dbConnection.isClosed());
    }
    catch(Exception e) {
      TDSLogger.println(e);
    }
    
    if (dbConnection == null) {
        TDSLogger.println("direct conn");
        dbConnection = getOracleConnection();
    }
    
    return dbConnection;
  }

 /* Method Added by J. Jeyandran for Freeing the Java Connection of the Connection Pool */
  public synchronized static void free(Connection connection) throws Exception
  {
    try {
      connectionRepository.free(connection);
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }

 /* Method Added by J. Jeyandran for Closing all the Java Connections of the Connection Pool */
  public synchronized static void closeAllConnections() throws Exception
  {
    try {
      connectionRepository.closeAllConnections();
    } catch(Exception e) {
      TDSLogger.println(e);
    }
  }

  /**
   * set the property AutoCommit of a connection
   * @param connection
   * @param commit
   * @throws SQLException
   */
  public synchronized static void setConnectionAutoCommit(Connection connection,
                                                          boolean commit) throws SQLException{
    connection.setAutoCommit(commit);
  }

  public static void main(String args[]) {
    try {
      TDSLogger.println(DBConnection.getConnection());
    } catch (Exception e) {
      TDSLogger.println(e);
    }
  }

 /*
  *<p>This method used to close connection</p>
  */

 /* Method Modified by J. Jeyandran for Freeing the Java Connection of the Connection Pool */

  public static void close(Connection connection){
    Properties properties = TDSResource.getProperties("TDS");
    if (connection == null){
      return ;
    }
    try {
    	connection.commit();
      if (properties.getProperty("database.connection.type") != null) {
        if (properties.getProperty("database.connection.type").equals("1")) {
          connection.close();
          TDSLogger.println("Close connection : " + connection + " " +connection.isClosed());
          recConnectionClosed(connection);
        } else if (properties.getProperty("database.connection.type").equals("2")) {
          connection.close();
          TDSLogger.println("Close connection : " + connection + " " +connection.isClosed());
          recConnectionClosed(connection);
        } else if(properties.getProperty("database.connection.type").equals("5")) {
          free(connection);
          TDSLogger.println("Free connection : " + connection);
          recConnectionClosed(connection);
        } else if (properties.getProperty("database.connection.type").equals("4")) {
        	TDSLogger.print("Close connection : " + connection.hashCode());
            connection.close();
            TDSLogger.println(" " +connection.isClosed());
            recConnectionClosed(connection);
        } else {
          connection.close();
          TDSLogger.println("Close connection : " + connection + " " +connection.isClosed());
          recConnectionClosed(connection);
        }
      } else {
        connection.close();
        TDSLogger.println("Close connection : " + connection + " " +connection.isClosed());
        recConnectionClosed(connection);
      }
    } catch (Exception ex) {
      TDSLogger.println(ex);
    } finally{
      connection = null;
    }
  }

 /*
  *<p>This method used to rollback connection</p>
  */
  public static void rollback(Connection connection){
    if(connection == null){
      return ;
    }
    try {
      connection.rollback();
    } catch (Exception ex) {
      TDSLogger.println(ex);
    }
  }

  public static int getSequence(Connection connection,
                                String sequenceName) throws SQLException{

    Statement stmt = connection.createStatement();
    stmt.execute("select " + sequenceName + ".nextval from dual");
    ResultSet rs = stmt.getResultSet();
    rs.next();
    int ret = rs.getInt(1);
    rs.close();
    stmt.close();
    return ret;
  }

  public static int getSequence(String sequenceName) throws Exception {
    Connection connection = null;
    int ret = -1;
    try {
      connection = DBConnection.getConnection();
      ret = getSequence(connection,sequenceName);
    } finally {
      DBConnection.close(connection);
    }
    return ret;
  }

  private static void recConnectionGot(Connection con){
    try {
      Properties properties = TDSResource.getProperties("TDS");
      String toRec = properties.getProperty("Connection.record");
      if (toRec == null || !toRec.equalsIgnoreCase("on")){
        return;
      }
      String sql = "INSERT INTO AU_TDS_CONNECTION ( " +
          "   CID, LOGTIME, IS_CLOSED) "+
          " VALUES ( ?, sysdate , 0)";
      PreparedStatement stmnt = recCon.prepareStatement(sql);
      stmnt.setString(1, con.toString());
      stmnt.executeUpdate();
    } catch (Exception e){
//      TDSLogger.println(e);
    }
  }

  private static void recConnectionClosed(Connection con){
    try {
      Properties properties = TDSResource.getProperties("TDS");
      String toRec = properties.getProperty("Connection.record");
      if (toRec == null || !toRec.equalsIgnoreCase("on")){
        return;
      }
      String sql = "DELETE au_tds_connection WHERE cid = ?";
      PreparedStatement stmnt = recCon.prepareStatement(sql);
      stmnt.setString(1, con.toString());
      stmnt.executeUpdate();
    } catch (Exception e){
    }
  }
  
	public static void commit(Connection con) {
		try {
			if (con == null)
				return;
			con.commit();
		} catch (SQLException e) {
			TDSLogger.println(e);
		}
	}
  
  public synchronized static Connection getTomcatConnection() throws Exception {
	  Connection conn = null;
	  try {
		  Properties properties = TDSResource.getProperties("TDS");
		  String tomcatPoolName = properties.getProperty("tomcat.pool.name");
		  if (tomcatPoolName == null)
			  tomcatPoolName = "jdbc/devtds";
		  Context initContext = new InitialContext();    
		  Context envContext = (Context) initContext.lookup("java:/comp/env");    
		  DataSource ds = (DataSource) envContext.lookup(tomcatPoolName);
		  conn = ds.getConnection();
		  conn.setAutoCommit(true);
		  TDSLogger.println("Get Tomcat connection : " + conn.hashCode() + " " +conn.isClosed());
		  recConnectionGot(conn);	  
	  } catch (NamingException e) {
		  TDSLogger.println(e);
	  } catch (SQLException e) {
		  TDSLogger.println(e);
	  }
	  return conn;
  }
  
	public static void closeResultSet(ResultSet rs) {
	    Statement st = null;
	    try {
            if (rs != null) {
                st = rs.getStatement();
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            TDSLogger.println(e);
        }
	}  
  
}