package com.mxic.oiplus.rs;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.mxic.oiplus.util.TDSLogger;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class DBConnection {
  private static Driver driver = null;
  public DBConnection() {
  }

  /**
   * You should not use this method.
   * @throws SQLException
   */
  private static synchronized void loadDriver() throws SQLException {
    if (driver != null){
      return;
    }
    try {
      Class c = Class.forName("com.mysql.jdbc.Driver");
      DriverManager.setLoginTimeout(60);
    } catch (Exception e) {
      throw new SQLException("Can't found MySQL JDBC Driver");
    }
  }

  /**
  * get a Connection based on the resource boundles
  * @return
  * @throws Exception
  */
 public synchronized static Connection getConnection() throws Exception {
  //return getMySQLConnectionPool();
  return getMySQLConnection();
 }

  public synchronized static Connection getMySQLConnectionPool() throws Exception{
    Connection dbConnection = null;
    Context initContext = new InitialContext();
    Context envContext = (Context) initContext.lookup("java:comp/env");
    //DataSource ds = (DataSource)initContext.lookup("java:comp/env/jdbc/myDataSource");
    DataSource ds = (DataSource) envContext.lookup("jdbc/MySQLDB");
    dbConnection = ds.getConnection();
    TDSLogger.println("Get MySQL Connection Pool");
    return dbConnection;
  }

  public synchronized static Connection getMySQLConnection() throws Exception{
    Connection dbConnection = null;
    loadDriver();
    dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oi?user=root&password=4321");
    TDSLogger.println("Get MySQL JDBC Connection");
    return dbConnection;
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

  /*
   *<p>This method used to rollback connection</p>
   */
  public static void commit(Connection connection){
    if (connection == null){
      return ;
    }
    try {
      connection.commit();
    } catch (Exception ex) {
      TDSLogger.println("Exception: " + ex.getMessage());
    }
  }

  /*
   *<p>This method used to rollback connection</p>
   */
  public static void rollback(Connection connection){
    if (connection == null){
      return ;
    }
    try {
      connection.rollback();
    } catch (Exception ex) {
      TDSLogger.println("Exception: " + ex.getMessage());
    }
  }

  public static void close(Connection connection){
    if (connection == null){
      return ;
    }
    try{
      connection.close();
    } catch (Exception ex){
      TDSLogger.println("Exception: " + ex.getMessage());
    }
  }

  public static void main(String args[]){
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      String sql = "";
      PreparedStatement ps = null;
      ResultSet rs = null;
      ArrayList tmp = new ArrayList();
      sql = "SELECT * FROM ts_employbasic";
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()){
        TDSLogger.println(rs.getString(2));
      }
    } catch (Exception ex){
      TDSLogger.println(ex.getMessage());
    }
    DBConnection.close(conn);
  }
}