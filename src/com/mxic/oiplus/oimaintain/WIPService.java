/*****************************************************************
 * Purpose : for Wip Control
 * Author : Robin Mao
 * Date : 2008/04/23
 *****************************************************************/

package com.mxic.oiplus.oimaintain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class WIPService {

  public WIPService() {
  }

  public static WIPActionForm[] getWipInfo(String sid) {
    Connection conn = null;
    boolean flag = true;

    try {
      String sql = "SELECT sid,optionlist,ctrl_type,pgname1,pgname2,pgname3,pgname4,pgname5, \n"+
          " decode(p1,1,'readonly','') p1, \n" +
          " decode(p2,1,'readonly','') p2, \n" +
          " decode(p3,1,'readonly','') p3, \n" +
          " decode(p4,1,'readonly','') p4, \n" +
          " decode(p5,1,'readonly','') p5 \n" +
          " FROM tf_wip_tx a where sid= ? and ctrl_type = '3-1' order by pgname1 ";
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1,Integer.parseInt(sid));
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        WIPActionForm bean = new WIPActionForm();
        bean.setSid(Integer.parseInt(sid));
        bean.setSid(rs.getInt("SID"));
        bean.setOption_list(rs.getString("OPTIONLIST"));
        bean.setCtrl_type(rs.getString("CTRL_TYPE"));
        bean.setPgname1(rs.getString("PGNAME1"));
        bean.setPgname2(rs.getString("PGNAME2"));
        bean.setPgname3(rs.getString("PGNAME3"));
        bean.setPgname4(rs.getString("PGNAME4"));
        bean.setPgname5(rs.getString("PGNAME5"));
        bean.setP1(rs.getString("P1"));
        bean.setP2(rs.getString("P2"));
        bean.setP3(rs.getString("P3"));
        bean.setP4(rs.getString("P4"));
        bean.setP5(rs.getString("P5"));
        tmp.add(bean);
      }
      return (WIPActionForm[]) tmp.toArray(new WIPActionForm[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static WIPActionForm[] getOptions(String sid) {
    Connection conn = null;

    try {
      String sql =
          "select distinct mask_option,\n" +
          "  decode(instr(b.optionlist,a.mask_option),0,'',\n" +
          "  decode(instr(b.optionlist,a.mask_option),null,'','checked')) tag \n" +
          "from (select distinct sid,mask_option \n" +
          "from tf_test_parameter_ws_tx \n" +
          "where sid = ?) a, \n" +
          "(select distinct sid,optionlist \n" +
          "from tf_wip_tx \n" +
          "where sid = ?) b\n" +
          "where a.sid = b.sid (+)";

      conn = null;
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1,Integer.parseInt(sid));
      ps.setInt(2,Integer.parseInt(sid));
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        WIPActionForm bean = new WIPActionForm();
        bean.setOption_list(rs.getString("MASK_OPTION"));
        bean.setTag(rs.getString("TAG"));
        tmp.add(bean);
      }
      return (WIPActionForm[]) tmp.toArray(new WIPActionForm[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static WIPCtrlBean[] getCtrlType(String sid) {
    Connection conn = null;

    try {
      String sql =
          "select distinct \n" +
          "decode(ctrl_type,'0','checked','') M0, \n" +
          "decode(ctrl_type,'1','checked','') M1, \n" +
          "decode(ctrl_type,'2-1','checked','') M21, \n" +
          "decode(ctrl_type,'2-2','checked','') M22, \n" +
          "decode(ctrl_type,'2-3','checked','') M23, \n" +
          "decode(ctrl_type,'2-4-1','checked','') M241, \n" +
          "decode(ctrl_type,'3-1','checked','') M31, \n" +
          "decode(ctrl_type,'2-4-1',pgname1,'') PGNAME1 \n" +
          "from tf_wip_tx a, tf_information b\n" +
          "where b.sid = ? and b.sid = a.sid (+)";

      conn = null;
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1,Integer.parseInt(sid));
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        WIPCtrlBean bean = new WIPCtrlBean();
        bean.setM0(rs.getString("M0"));
        bean.setM1(rs.getString("M1"));
        bean.setM21(rs.getString("M21"));
        bean.setM22(rs.getString("M22"));
        bean.setM23(rs.getString("M23"));
        bean.setM241(rs.getString("M241"));
        bean.setM31(rs.getString("M31"));
        bean.setSteps(rs.getString("PGNAME1"));
        tmp.add(bean);
      }
      return (WIPCtrlBean[]) tmp.toArray(new WIPCtrlBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static WIPLotsBean[] getWipLots(String sid) {
    Connection conn = null;
    boolean flag = true;

    try {
      String sql = "SELECT lotid,ipn,prodbody,options,prodgroup,route,lotstatus1,\n" +
          "hotlotflag,lotowner,waferqty,chipqty,steps,stage,\n" +
          "nvl(valdata1,'-') valdata1,nvl(valdata2,'-') valdata2,nvl(valdata3, '-') valdata3,\n" +
          "nvl(saprwno,'-') saprwno\n" +
          " FROM tf_wip_lots a where sid= ? ";
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1,Integer.parseInt(sid));
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();

      while (rs.next()) {
        WIPLotsBean bean = new WIPLotsBean();
        bean.setLotid(rs.getString("LOTID"));
        bean.setIpn(rs.getString("IPN"));
        bean.setProdbody(rs.getString("PRODBODY"));
        bean.setOptions(rs.getString("OPTIONS"));
        bean.setProdgroup(rs.getString("PRODGROUP"));
        bean.setRoute(rs.getString("ROUTE"));
        bean.setLotstatus1(rs.getString("LOTSTATUS1"));
        bean.setHotlotflag(rs.getString("HOTLOTFLAG"));
        bean.setLotowner(rs.getString("LOTOWNER"));
        bean.setWaferqty(rs.getString("WAFERQTY"));
        bean.setChipqty(rs.getString("CHIPQTY"));
        bean.setSteps(rs.getString("STEPS"));
        bean.setStage(rs.getString("STAGE"));
        bean.setValdata1(rs.getString("VALDATA1"));
        bean.setValdata2(rs.getString("VALDATA2"));
        bean.setValdata3(rs.getString("VALDATA3"));
        bean.setSaprwno(rs.getString("SAPRWNO"));
        tmp.add(bean);
      }
      return (WIPLotsBean[]) tmp.toArray(new WIPLotsBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
  }
}
