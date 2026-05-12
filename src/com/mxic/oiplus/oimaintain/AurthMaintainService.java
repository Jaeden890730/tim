package com.mxic.oiplus.oimaintain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class AurthMaintainService {
  public AurthMaintainService() {
  }

  public static AurthMaintainActionForm[] getInfo(int sid) {
    Connection conn = null;
    boolean flag = true;

    try {
      StringBuffer sql = new StringBuffer();
      sql.append("SELECT * FROM tf_information where sid='" + sid + "'");

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        AurthMaintainActionForm bean = new AurthMaintainActionForm();
        bean.setPd_body(rs.getString("product_body"));
        bean.setBrand(rs.getString("brand"));
        bean.setVersion(rs.getString("version"));
        bean.setCreator(rs.getString("creator"));
        bean.setSponsor_1(rs.getString("sponsor_1"));
        bean.setSponsor_2(rs.getString("sponsor_2"));
        bean.setProductType(rs.getString("product_type"));
        tmp.add(bean);
      }

      return (AurthMaintainActionForm[]) tmp.toArray(new AurthMaintainActionForm[0]);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  /********************************************************************
   *主題:get Add資料
   *********************************************************************/
  public static boolean update_data(int sid,
                                    String sponsor_1,
                                    String sponsor_2) {
    String InsSQL = null;
    String InsSQL1 = null;
    Connection conn = null;
    Connection conn1 = null;

    try {
      conn = DBConnection.getConnection();
      InsSQL = "update tf_information set sponsor_1=?, sponsor_2=? where sid = ? ";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      ps2.setString(1, sponsor_1);
      ps2.setString(2, sponsor_2);
      ps2.setInt(3, sid);
      ps2.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
    }
    return false;
  }

  public static void main(String[] args) {
    AurthMaintainService aurthMaintainService = new AurthMaintainService();
  }
}
