package com.mxic.oiplus.apl;

import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import com.mxic.oiplus.apl.bean.*;
import com.mxic.oiplus.resource.*;

public class ManageAction extends Action {
  private Connection conn = null;

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) {
    ActionForward forward = null;
    try {
      String watch_group = "ALL";

      if (request.getParameterValues("grp2") != null)
        watch_group = ((String)request.getParameterValues("grp2")[0]);

      conn = DBConnection.getConnection();
      if ( request.getParameter("uid") == null ) {
        listUsers(request,watch_group);
      }
      else {
        // user input department ID
        if ( request.getParameter("uid").equals("s") ) {
          selectUsers(request);
//          APLUser[] users = new APLUser[0];
//          request.setAttribute("all", users);
        }
        // the first time enter user choose
        else if ( request.getParameter("uid").equals("*") ) {
          APLUser[] users = new APLUser[0];
          request.setAttribute("all", users);
        }
        else {
          String[] users = request.getParameterValues("userId");
          String[] modify = request.getParameterValues("uid");
          String[] ws_inhouseT = request.getParameterValues("ws_inhouse");
          String[] ft_inhouseT = request.getParameterValues("ft_inhouse");
          String[] ws_subconT = request.getParameterValues("ws_subcon");
          String[] ft_subconT = request.getParameterValues("ft_subcon");
          String[] aviT = request.getParameterValues("avi");
          String[] fviT = request.getParameterValues("fvi");
          String[] markT = request.getParameterValues("mark");
          String[] ws_inhouse = new String[users.length+1];
          String[] ft_inhouse = new String[users.length+1];
          String[] ws_subcon = new String[users.length+1];
          String[] ft_subcon = new String[users.length+1];
          String[] avi = new String[users.length+1];
          String[] fvi = new String[users.length+1];
          String[] mark = new String[users.length+1];
          for (int i = 0; users != null && i < users.length; i++) {
            if (ws_inhouse[i] == null) ws_inhouse[i] = "N";
            if (ws_inhouseT != null && ws_inhouseT.length > i)
              ws_inhouse[Integer.parseInt(ws_inhouseT[i])] = "Y";
            if (ft_inhouse[i] == null) ft_inhouse[i] = "N";
            if (ft_inhouseT != null && ft_inhouseT.length > i)
              ft_inhouse[Integer.parseInt(ft_inhouseT[i])] = "Y";
            if (ws_subcon[i] == null) ws_subcon[i] = "N";
            if (ws_subconT != null && ws_subconT.length > i)
              ws_subcon[Integer.parseInt(ws_subconT[i])] = "Y";
            if (ft_subcon[i] == null) ft_subcon[i] = "N";
            if (ft_subconT != null && ft_subconT.length > i)
              ft_subcon[Integer.parseInt(ft_subconT[i])] = "Y";
            if (avi[i] == null) avi[i] = "N";
            if (aviT != null && aviT.length > i)
            	avi[Integer.parseInt(aviT[i])] = "Y";
            if (fvi[i] == null) fvi[i] = "N";
            if (fviT != null && fviT.length > i)
            	fvi[Integer.parseInt(fviT[i])] = "Y";
            if (mark[i] == null) mark[i] = "N";
            if (markT != null && markT.length > i)
            	mark[Integer.parseInt(markT[i])] = "Y";
          }

//          if ( modify.length == 1 && modify[0].length() == 0 )
          if ( modify.length == 1)
            if (modify[0].length() > 0 && modify[0].startsWith("-"))
              delete(modify[0]);
            else
              save(request, users, ws_inhouse, ft_inhouse, ws_subcon, ft_subcon, avi, fvi, mark);
// change 20070105          listUsers(request, users, modify, watch_group);
          listUsers(request, watch_group);
        }
      }
      forward = mapping.findForward("success");
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return forward;
  }

  private void selectUsers(HttpServletRequest request) throws Exception {
//    APLUser[] users = APLUser.getUserByEmpNo(conn, new String[0]);
    APLUser[] users = APLUser.getUserByDeptID(conn, (String)request.getParameter("dept_id"));
    request.setAttribute("all", users);
  }

  private void listUsers(HttpServletRequest request, String watch_group) throws Exception {
//  Integer[0] ¥Î¤£¨ì APLUser[] users = APLUser.getNoticeList(conn, new Integer[0]);
    APLUser[] users = APLUser.getNoticeList(conn, watch_group);
    request.setAttribute("noticeList", users);
  }

  // list all user with watch group
  private void listUsers(HttpServletRequest request,
                         String[] userId,
                         String[] update,
                         String watch_group) throws Exception {
    int i = (userId == null ? 0 : userId.length) + 10;
    HashSet users = new HashSet(i);
    for ( i = 0; userId != null && i < userId.length; i++ ) {
      users.add( Integer.valueOf(userId[i]) );
    }
    for ( i = 0; update != null && i < update.length; i++ ) {
      if ( update[i].length() == 0 ) continue;
      Integer uid = Integer.valueOf(update[i]);
      if ( uid.intValue() < 0 )
        users.remove( new Integer(-uid.intValue()) );
      else users.add(uid);
    }
    APLUser[] list = null;
    if ( users.size() > 0 ) {
      i = 0;
      Integer[] uid = new Integer[users.size()];
      for ( Iterator iter = users.iterator(); iter.hasNext(); ) {
        uid[i++] = (Integer) iter.next();
      }
      list = APLUser.getNoticeList(conn, watch_group);
    } else
      list = new APLUser[0];
      // find new users (not in the list)
    for ( i = 0; list != null && i < list.length; i++ ) {
      Integer uid = Integer.valueOf( list[i].getUserId() );
      users.remove(uid);
    }
    // result = users in the list + users not in the list
    ArrayList result = new ArrayList(list.length + users.size());
    for ( i = 0; list != null && i < list.length; i++ )
      result.add(list[i]);
    if ( users.size() > 0 ) {
      i = 0;
      Integer[] uid = new Integer[users.size()];
      for ( Iterator iter = users.iterator(); iter.hasNext(); ) {
        uid[i++] = (Integer) iter.next();
      }
      APLUser[] add = APLUser.getUserById(conn, uid);
      for ( i = 0; add != null && i < add.length; i++ )
        result.add(add[i]);
    }
    request.setAttribute("noticeList", result.toArray(new APLUser[result.size()]));
/* Old program 20070105
      private void listUsers(HttpServletRequest request,
                             String[] userId,
                             String[] update,
                             String watch_group) throws Exception {
    int i = (userId == null ? 0 : userId.length) + 10;
    HashSet users = new HashSet(i);
    for ( i = 0; userId != null && i < userId.length; i++ ) {
      users.add( Integer.valueOf(userId[i]) );
    }
    for ( i = 0; update != null && i < update.length; i++ ) {
      if ( update[i].length() == 0 ) continue;
      Integer uid = Integer.valueOf(update[i]);
      if ( uid.intValue() < 0 )
        users.remove( new Integer(-uid.intValue()) );
      else users.add(uid);
    }
    APLUser[] list = null;
    if ( users.size() > 0 ) {
      i = 0;
      Integer[] uid = new Integer[users.size()];
      for ( Iterator iter = users.iterator(); iter.hasNext(); ) {
        uid[i++] = (Integer) iter.next();
      }
      list = APLUser.getNoticeList(conn, uid);
    } else
      list = new APLUser[0];
      // find new users (not in the list)
    for ( i = 0; list != null && i < list.length; i++ ) {
      Integer uid = Integer.valueOf( list[i].getUserId() );
      users.remove(uid);
    }
    // result = users in the list + users not in the list
    ArrayList result = new ArrayList(list.length + users.size());
    for ( i = 0; list != null && i < list.length; i++ )
      result.add(list[i]);
    if ( users.size() > 0 ) {
      i = 0;
      Integer[] uid = new Integer[users.size()];
      for ( Iterator iter = users.iterator(); iter.hasNext(); ) {
        uid[i++] = (Integer) iter.next();
      }
      APLUser[] add = APLUser.getUserById(conn, uid);
      for ( i = 0; add != null && i < add.length; i++ )
        result.add(add[i]);
    }
    request.setAttribute("noticeList", result.toArray(new APLUser[result.size()]));*/
  }

  // Save all users with group setings
  // in fact, add new user & update group setting only
  private void save(HttpServletRequest request,
                   String[] userId,
                   String[] ws_inhouse,
                   String[] ft_inhouse,
                   String[] ws_subcon,
                   String[] ft_subcon,
                   String[] avi,
                   String[] fvi,
                   String[] mark) throws Exception {
    HashMap wheres = new HashMap();
    wheres.put("1","1");
    conn.setAutoCommit(false);
    // remove all user info and re-insert
//    APLUtil.delete(conn, "AP_NOTICE", wheres);
    String sql = "UPDATE AP_NOTICE set ws_inhouse = ?,ft_inhouse = ?,ws_subcon = ?,ft_subcon = ?,avi = ?,fvi = ?,mark = ? " +
          "WHERE EMPLOYEE_NO = (SELECT EMPLOYEE_NO FROM AU_USER_ACCOUNT WHERE USER_ID = ?) ";
    java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
    for ( int i = 0; userId != null && i < userId.length; i++ ) {
      stmt.setString(1, ws_inhouse[i]);
      stmt.setString(2, ft_inhouse[i]);
      stmt.setString(3, ws_subcon[i]);
      stmt.setString(4, ft_subcon[i]);
      stmt.setString(5, avi[i]);
      stmt.setString(6, fvi[i]);
      stmt.setString(7, mark[i]);
      stmt.setInt(8, Integer.parseInt(userId[i]));
      stmt.addBatch();
    }
    if ( userId != null && 0 < userId.length )
      stmt.executeBatch();
    stmt.close();
    stmt = null;

    sql = "INSERT INTO AP_NOTICE (employee_no,email,ws_inhouse,ft_inhouse,ws_subcon,ft_subcon,avi,fvi,mark) " +
        "SELECT EMPLOYEE_NO, USER_NAME||'@mxic.com.tw' AS EMAIL, ?, ?, ?, ?, ?, ?, ? "+
        "FROM AU_USER_ACCOUNT WHERE USER_ID = ? ";
    stmt = conn.prepareStatement(sql);

    // append new user
    String newid = (String)request.getParameterValues("uid")[0];
    if (!newid.equals("")) {
      stmt.setString(1, "N");
      stmt.setString(2, "N");
      stmt.setString(3, "N");
      stmt.setString(4, "N");
      stmt.setString(5, "N");
      stmt.setString(6, "N");
      stmt.setString(7, "N");
      stmt.setInt(8, Integer.parseInt(newid));
      stmt.addBatch();
      stmt.executeBatch();
      stmt.close();
    }

    conn.setAutoCommit(true);
    conn.commit();
  }

  // Remove a user from Notice List
  private void delete(String userId)
      throws Exception {

    String sql = "DELETE AP_NOTICE WHERE EMPLOYEE_NO = " +
                 "(SELECT EMPLOYEE_NO FROM AU_USER_ACCOUNT WHERE USER_ID = ?) ";
    java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, -Integer.parseInt(userId));
    stmt.addBatch();
    stmt.executeBatch();
    stmt.close();
    conn.commit();
  }
}