package com.mxic.oiplus.oimaintain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class CancellationAsignService {
  public CancellationAsignService() {
  }

  /*****************************************************************
   *主題:取得sid之基本資料
   *****************************************************************/
  public static boolean getInfo(int sid) {
    String InsSQL = null;
    String InsSQL1 = null;
    Connection conn = null;
    Connection conn1 = null;

    try {
      conn = DBConnection.getConnection();
      InsSQL = "update tf_information set status=? where sid = ? ";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      ps2.setString(1, "P");
      ps2.setInt(2, sid);
      ps2.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
    }
    return false;
  }

  public static boolean user_action(String user_id, String action_do) {
    Connection conn = null;
    boolean flag = false;
    String InsSQL3 = null;
    try {
      conn = DBConnection.getConnection();
      StringBuffer sql = new StringBuffer();

      //***記得有少找出option及tester的條件喔~~
      sql.append("SELECT count(*) as total_count " +
                 "FROM au_user_action a, au_user_account b, au_actions c " +
                 "where a.user_sid=b.user_id and a.act_sid=c.act_sid and " +
                 "b.user_id='" + user_id + "' and c.act_action='" + action_do + "'");

      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        if (rs.getString("total_count").equals("0") == true) {
          flag = false;
        } else {
          flag = true;
        }
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn);
      return flag;
    }
  }

  public static void main(String[] args) {
    EditiionCompareService editiionCompareService = new EditiionCompareService();
  }
}
