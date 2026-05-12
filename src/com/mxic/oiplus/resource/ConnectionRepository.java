/******************************************************************************************************/
//	Author	: 	J. Jeyandran
//	Date	:	May 15, 2003.
/******************************************************************************************************/

package com.mxic.oiplus.resource;

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.util.*;

public class ConnectionRepository implements Runnable
{
  private static String driver="sun.jdbc.odbc.JdbcOdbcDriver";
  private static String username="pgm";
  private static String password="pgm";
  private static String url="jdbc:odbc:pgmdsn";
  private static int initialConnections = 1;
  private static int maxConnections = 1;
  private static boolean waitIfBusy = false;

  private Vector availableConnections, busyConnections;
  private boolean connectionPending = false;

  private static ConnectionRepository instance = null;

  public static ConnectionRepository getInstance()
      throws ConnectionRepositoryException,Exception
  {
    if (instance == null)
    {
      synchronized(ConnectionRepository.class)
      {
        if(instance == null)
        {
          initializeConnectionParameters();
          instance = new ConnectionRepository();
        }
      }
    }
    return instance;
  }

  public ConnectionRepository() throws ConnectionRepositoryException,Exception
  {
    initializeConnectionParameters();
    initConnections();
  }

  public ConnectionRepository(String driver, String url,
                              String username, String password,
                              int initialConnections,
                              int maxConnections,
                              boolean waitIfBusy) throws Exception
  {
    this.driver = driver;
    this.url = url;
    this.username = username;
    this.password = password;
    this.maxConnections = maxConnections;
    this.waitIfBusy = waitIfBusy;

    initConnections();
  }

  public static void initializeConnectionParameters() throws ConnectionRepositoryException
  {

    Properties properties = TDSResource.getProperties("TDS");

    String temp = properties.getProperty("connectionpooling.driver");
    if (temp != null && temp.trim().length() > 0)
      driver = temp.trim();
    temp = properties.getProperty("connectionpooling.username");
    if (temp != null && temp.trim().length() > 0)
      username = temp.trim();
    temp = properties.getProperty("connectionpooling.password");
    if (temp != null && temp.trim().length() > 0)
      password = temp.trim();
    temp = properties.getProperty("connectionpooling.dburl");
    if (temp != null && temp.trim().length() > 0)
      url = temp.trim();
    temp = properties.getProperty("connectionpooling.initialconnections");
    if (temp != null && temp.trim().length() > 0)
    {
      initialConnections = Integer.parseInt(temp.trim());
    }
    temp = properties.getProperty("connectionpooling.maxconnections");
    if (temp != null && temp.trim().length() > 0)
    {
      maxConnections = Integer.parseInt(temp.trim());
    }
    temp = properties.getProperty("connectionpooling.waitifbusy");
    if (temp != null &&
        temp.trim().length() > 0 &&
        (temp.equalsIgnoreCase("true") || temp.equalsIgnoreCase("false")))
    {
      waitIfBusy = (new Boolean(temp.trim())).booleanValue();
    }
  }

  public void initConnections() throws Exception
  {
    if (initialConnections > maxConnections)
    {
      initialConnections = maxConnections;
    }

    availableConnections = new Vector(initialConnections);
    busyConnections = new Vector();

    //TDSLogger.println("Creating "+initialConnections+"Connections as follows");
    for(int i = 0; i < initialConnections; i++)
    {
      availableConnections.addElement(makeNewConnection(0));
      //TDSLogger.println("Connection No : "+i+" is Successfully Created");
    }
    //TDSLogger.println(""+initialConnections+" connection are Created Successfully");
  }


  public synchronized Connection getConnection()
      throws SQLException {
    if (!availableConnections.isEmpty()) {
      Connection existingConnection =
          (Connection)availableConnections.lastElement();
      int lastIndex = availableConnections.size() - 1;
      //TDSLogger.println("Some Connections are available : Taking "+lastIndex+"Connection for Use");
      availableConnections.removeElementAt(lastIndex);
      // If connection on available list is closed (e.g.,
      // it timed out), then remove it from available list
      // and repeat the process of obtaining a connection.
      // Also wake up threads that were waiting for a
      // connection because maxConnection limit was reached.
      if (existingConnection.isClosed()) {
        notifyAll(); // Freed up a spot for anybody waiting
        return(getConnection());
      } else {
        //TDSLogger.println("Adding "+lastIndex+"Connection to BusyConnectionPool...");
        busyConnections.addElement(existingConnection);
        return(existingConnection);
      }
    } else {
      TDSLogger.println("There are no Available Connections");
      // Three possible cases:
      // 1) You haven't reached maxConnections limit. So
      //    establish one in the background if there isn't
      //    already one pending, then wait for
      //    the next available connection (whether or not
      //    it was the newly established one).
      // 2) You reached maxConnections limit and waitIfBusy
      //    flag is false. Throw SQLException in such a case.
      // 3) You reached maxConnections limit and waitIfBusy
      //    flag is true. Then do the same thing as in second
      //    part of step 1: wait for next available connection.

      if ((totalConnections() < maxConnections) &&
          !connectionPending) {
        makeBackgroundConnection();
      } else if (!waitIfBusy) {
    	  TDSLogger.println("Connection limit reached");
        throw new SQLException("Connection limit reached");
      }
      // Wait for either a new connection to be established
      // (if you called makeBackgroundConnection) or for
      // an existing connection to be freed up.
      try {
        TDSLogger.println("Waiting to get the Connection until other processes released");
        wait();
      } catch (InterruptedException ie) {}
      // Someone freed up a connection, so try again.
      return (getConnection());
    }
  }

  // You can't just make a new connection in the foreground
  // when none are available, since this can take several
  // seconds with a slow network connection. Instead,
  // start a thread that establishes a new connection,
  // then wait. You get woken up either when the new connection
  // is established or if someone finishes with an existing
  // connection.

  private void makeBackgroundConnection() {
    connectionPending = true;
    try {
      Thread connectThread = new Thread(this);
      connectThread.start();
    } catch(OutOfMemoryError oome) {
      // Give up on new connection
    }
  }

  public void run() {
    try {
      Connection connection = makeNewConnection(0);
      synchronized(this) {
        availableConnections.addElement(connection);
        TDSLogger.println("Connection Pool size : " + (availableConnections.size() + busyConnections.size()));
        connectionPending = false;
        notifyAll();
      }
    } catch(Exception e) { // SQLException or OutOfMemory
      // Give up on new connection and wait for existing one
      // to free up.
    }
  }

  // This explicitly makes a new connection. Called in
  // the foreground when initializing the ConnectionPool,
  // and called in the background when running.

  private Connection makeNewConnection(int retryCount)
      throws SQLException {
    try {
    	 retryCount++;
      // Load database driver if not already loaded
      Class.forName(driver);
      // Establish network connection to database
      Connection connection =
        DriverManager.getConnection(url, username, password);
       //System.out.print(" \t New Connection Created \n");
      return(connection);
    } catch(ClassNotFoundException cnfe) {
      // Simplify try/catch blocks of people using this by
      // throwing only one exception type.
      throw new SQLException("Can't find class for driver: " + driver);
    }catch (Exception e) {
        if (retryCount < 3) {
            return makeNewConnection(retryCount++);
        } else {
            throw e;
        }
    }
  }

  public synchronized void free(Connection connection) {
		if (busyConnections.contains(connection)) {
    busyConnections.removeElement(connection);
    availableConnections.addElement(connection);
    //TDSLogger.println("Freeing Connection");
    // Wake up threads that are waiting for a connection
    notifyAll();
		} else {
			if (connection == null) {
				return;
			}
			try {
				connection.close();
			} catch (SQLException e) {
				TDSLogger.println(e);
			}
			connection = null;
		}
  }

  public synchronized int totalConnections() {
    return(availableConnections.size() +
           busyConnections.size());
  }

  /** Close all the connections. Use with caution:
   *  be sure no connections are in use before
   *  calling. Note that you are not <I>required</I> to
   *  call this when done with a ConnectionPool, since
   *  connections are guaranteed to be closed when
   *  garbage collected. But this method gives more control
   *  regarding when the connections are closed.
   */

  public synchronized void closeAllConnections() {
    closeConnections(availableConnections);
    availableConnections = new Vector();
    closeConnections(busyConnections);
    busyConnections = new Vector();
  }

  private void closeConnections(Vector connections) {
    try {
      for(int i = 0; i < connections.size(); i++) {
        Connection connection =
            (Connection)connections.elementAt(i);
        if (!connection.isClosed()) {
          connection.close();
        }
      }
    } catch(SQLException sqle) {
      // Ignore errors; garbage collect anyhow
    }
  }

  public synchronized String toString()
  {
    String info =
        "ConnectionRepository(" + url + "," + username + ")" +
        ", available=" + availableConnections.size() +
        ", busy=" + busyConnections.size() +
        ", max=" + maxConnections;
    return(info);
  }
}
