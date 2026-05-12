package com.mxic.oiplus.oimaintain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

// 這個 file 可能已不使用了
public class OIReleaseService {

  public OIReleaseService() {
  }

  // NO USE ???
  public static void PD_insert(String sid,
                               String pd_body,
                               String brand,
                               String version) {

    StringBuffer SelSQL = new StringBuffer();
    boolean flag = false;
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();

      conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT * FROM  tf_product_route_tx where product_body='" +
          pd_body +
          "' and brand='" + brand + "' and version='" +
          version + "' and sid='" + sid + "'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        conn_check = DBConnection.getConnection();

        //StringBuffer sql_check = new StringBuffer();
        sql_check = "insert into tf_product_route (sid,product_body,brand,version,ROUTE_NAME,step_seq,step_name,temperature,remark,test_time,time_unit,sampling_test) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        TDSLogger.println(sql_check);
        PreparedStatement ps_check = conn_check.prepareStatement(
            sql_check);
        ps_check.setString(1, sid);
        ps_check.setString(2, pd_body);
        ps_check.setString(3, brand);
        ps_check.setString(4, version);
        ps_check.setString(5, rs.getString("ROUTE_NAME"));
        ps_check.setString(6, rs.getString("step_seq"));
        ps_check.setString(7, rs.getString("step_name"));
        ps_check.setString(8, rs.getString("temperature"));
        ps_check.setString(9, rs.getString("remark"));
        ps_check.setString(10, rs.getString("test_time"));
        ps_check.setString(11, rs.getString("time_unit"));
        ps_check.setString(12, rs.getString("sampling_test"));
        TDSLogger.println(ps_check.toString());
        TDSLogger.println("tx");
        ps_check.executeUpdate();

        //DBConnection.commit(conn_check);
        DBConnection.close(conn_check);
        flag = true;

      }
      DBConnection.close(conn);

      conn_del = DBConnection.getConnection();
      StringBuffer sql_del = new StringBuffer();
      sql_del.append("delete from tf_product_route_tx where sid = ? and product_body=? and brand=? and version=? ");
      PreparedStatement ps_del = conn_del.prepareStatement(
          sql_del.toString());
      ps_del.setString(1, sid);
      ps_del.setString(2, pd_body);
      ps_del.setString(3, brand);
      ps_del.setString(4, version);

      TDSLogger.println("delete");
      ps_del.executeUpdate();
      DBConnection.close(conn_del);

      // return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  // NO USE ???
 /*****************************************************************
   *主題:搬移BOM資料
   *****************************************************************/

  public static void BOM_insert(String sid,
                                String pd_body, String brand,
                                String version) {
    StringBuffer SelSQL = new StringBuffer();
    boolean flag = false;
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();

      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_bom_route_tx where product_body='" +
                    pd_body +
                    "' and brand='" + brand + "' and version='" +
                    version + "' and sid='" + sid + "'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        conn_check = DBConnection.getConnection();

        //StringBuffer sql_check = new StringBuffer();
        sql_check = "insert into tf_bom_route (sid,product_body,brand,version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,ft_route_code,ft_route_add,tf_ws_comment) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        TDSLogger.println(sql_check);
        PreparedStatement ps_check = conn_check.prepareStatement(
            sql_check);
        ps_check.setString(1, sid);
        ps_check.setString(2, pd_body);
        ps_check.setString(3, brand);
        ps_check.setString(4, version);
        ps_check.setString(5, rs.getString("backend_option"));
        ps_check.setString(6, rs.getString("fg_with_code"));
        ps_check.setString(7, rs.getString("pin_count"));
        ps_check.setString(8, rs.getString("package_type"));
        //ps_check.setString(9, rs.getString("grade"));

        ps_check.setString(9, rs.getString("ft_route"));
        ps_check.setString(10, rs.getString("mask_option"));
        ps_check.setString(11, rs.getString("sort_route_code"));
        ps_check.setString(12, rs.getString("db_with_code"));
        ps_check.setString(13, rs.getString("ws_route"));
        ps_check.setString(14, rs.getString("ws_route_add"));
        ps_check.setString(15, rs.getString("tf_comment"));
        ps_check.setString(16, rs.getString("ft_route_code"));
        ps_check.setString(17, rs.getString("ft_route_add"));
        ps_check.setString(18, rs.getString("tf_ws_comment"));

        ps_check.executeUpdate();
        DBConnection.close(conn_check);
        flag = true;

      }
      DBConnection.close(conn);

      conn_del = DBConnection.getConnection();
      StringBuffer sql_del = new StringBuffer();
      sql_del.append("delete from tf_bom_route_tx where sid = ? and product_body=? and brand=? and version=? ");
      PreparedStatement ps_del = conn_del.prepareStatement(
          sql_del.toString());
      ps_del.setString(1, sid);
      ps_del.setString(2, pd_body);
      ps_del.setString(3, brand);
      ps_del.setString(4, version);

      TDSLogger.println("delete");
      ps_del.executeUpdate();
      DBConnection.close(conn_del);

      // return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  // NO USE ???
  public static void WS_insert(String sid,
                               String pd_body, String brand,
                               String version) {
    boolean flag = false;
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();

      conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT * FROM tf_test_parameter_ws_tx where product_body='" +
          pd_body +
          "' and brand='" + brand + "' and version='" +
          version + "' and sid='" + sid + "'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        conn_check = DBConnection.getConnection();

        //StringBuffer sql_check = new StringBuffer();
        sql_check = "insert into tf_test_parameter_ws (sid,pgm_id,product_body,brand,version,mask_option,test_type,tester,site,program_name,TEMPERATURE,tf_comment,hw_configure) values (?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        TDSLogger.println(sql_check);
        PreparedStatement ps_check = conn_check.prepareStatement(
            sql_check);
        ps_check.setString(1, sid);
        ps_check.setString(2, rs.getString("pgm_id"));
        ps_check.setString(3, pd_body);
        ps_check.setString(4, brand);
        ps_check.setString(5, version);
        ps_check.setString(6, rs.getString("mask_option"));
        ps_check.setString(7, rs.getString("test_type"));
        ps_check.setString(8, rs.getString("tester"));
        ps_check.setString(9, rs.getString("site"));
        ps_check.setString(10, rs.getString("program_name"));
        ps_check.setString(11, rs.getString("TEMPERATURE"));

        ps_check.setString(12, rs.getString("tf_comment"));
        ps_check.setString(13, rs.getString("hw_configure"));
        TDSLogger.println(ps_check.toString());
        TDSLogger.println("tx");
        ps_check.executeUpdate();

        //DBConnection.commit(conn_check);
        DBConnection.close(conn_check);
        flag = true;

      }
      DBConnection.close(conn);

      conn_del = DBConnection.getConnection();
      StringBuffer sql_del = new StringBuffer();
      sql_del.append("delete from tf_test_parameter_ws_tx where sid = ? and product_body=? and brand=? and version=? ");
      PreparedStatement ps_del = conn_del.prepareStatement(
          sql_del.toString());
      ps_del.setString(1, sid);
      ps_del.setString(2, pd_body);
      ps_del.setString(3, brand);
      ps_del.setString(4, version);
      TDSLogger.println(ps_del.toString());
      TDSLogger.println("tttttre");
      TDSLogger.println("delete");
      ps_del.executeUpdate();
      DBConnection.close(conn_del);

      //  return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  public static void FT_insert(String sid, String pd_body,
                               String brand, String version) {
    boolean flag = false;
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {

      ArrayList tmp2 = new ArrayList();
      //HashMap whereStem = new HashMap();

      conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT * FROM tf_test_parameter_ft_tx where product_body='" +
          pd_body +
          "' and brand='" + brand + "' and version='" +
          version + "' and sid='" + sid + "'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        conn_check = DBConnection.getConnection();
        sql_check = "Insert into tf_test_parameter_ft (sid,pgm_id,product_body,brand,version,test_type,backend_option,pin_count,package_type,body_size,tester,site,program_name,i_grade,c_grade,w_grade,y_grade,j_grade,k_grade,l_grade, n_grade,b_grade,e_grade, s_grade,tf_comment,hw_configure) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        PreparedStatement ps_check = conn_check.prepareStatement(
            sql_check);
        ps_check.setString(1, sid);
        ps_check.setString(2, rs.getString("pgm_id"));
        ps_check.setString(3, pd_body);
        ps_check.setString(4, brand);
        ps_check.setString(5, version);
        ps_check.setString(6, rs.getString("TEST_TYPE"));
        ps_check.setString(7, rs.getString("backend_option"));
        ps_check.setString(8, rs.getString("PIN_COUNT"));
        ps_check.setString(9, rs.getString("PACKAGE_TYPE"));
        ps_check.setString(10, rs.getString("body_size"));
        ps_check.setString(11, rs.getString("TESTER"));
        ps_check.setString(12, rs.getString("site"));
        ps_check.setString(13, rs.getString("PROGRAM_NAME"));
        ps_check.setString(14, rs.getString("i_grade"));
        ps_check.setString(15, rs.getString("c_grade"));
        ps_check.setString(16, rs.getString("w_grade"));
        ps_check.setString(17, rs.getString("y_grade"));
        ps_check.setString(18, rs.getString("j_grade"));
        ps_check.setString(19, rs.getString("k_grade"));
        ps_check.setString(20, rs.getString("l_grade"));
        ps_check.setString(21, rs.getString("n_grade"));
        ps_check.setString(21, rs.getString("b_grade"));
        ps_check.setString(21, rs.getString("e_grade"));
        ps_check.setString(22, rs.getString("s_grade"));
        ps_check.setString(23, rs.getString("tf_comment"));
        ps_check.setString(24, rs.getString("hw_configure"));
        ps_check.executeUpdate();

        //DBConnection.commit(conn_check);
        DBConnection.close(conn_check);
        flag = true;
      }
      DBConnection.close(conn);
      conn_del = DBConnection.getConnection();
      StringBuffer sql_del = new StringBuffer();
      sql_del.append("delete from tf_test_parameter_ft_tx where sid = ? and product_body=? and brand=? and version=? ");
      PreparedStatement ps_del = conn_del.prepareStatement(
          sql_del.toString());
      ps_del.setString(1, sid);
      ps_del.setString(2, pd_body);
      ps_del.setString(3, brand);
      ps_del.setString(4, version);

      TDSLogger.println("delete");
      ps_del.executeUpdate();
      DBConnection.close(conn_del);

      //  return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      //flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  public static void BA_insert(String sid, String pd_body,
                               String brand, String version) {
    boolean flag = false;
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {

      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();

      conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT * FROM tf_basic_information_tx where product_body='" +
          pd_body +
          "' and brand='" + brand + "' and version='" +
          version + "' and sid='" + sid + "'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        conn_check = DBConnection.getConnection();

        //StringBuffer sql_check = new StringBuffer();
        sql_check = "Insert into tf_basic_information (sid,product_body,brand,version,tf_option,tester,good_bin,fail_bin,soak_time) values (?,?,?,?,?,?,?,?,?) ";

        TDSLogger.println(sql_check);
        PreparedStatement ps_check = conn_check.prepareStatement(
            sql_check);
        ps_check.setString(1, sid);
        ps_check.setString(2, pd_body);
        ps_check.setString(3, brand);
        ps_check.setString(4, version);
        ps_check.setString(5, rs.getString("tf_option"));
        ps_check.setString(6, rs.getString("tester"));
        ps_check.setString(7, rs.getString("good_bin"));
        ps_check.setString(8, rs.getString("fail_bin"));
        ps_check.setString(9, rs.getString("soak_time"));
        TDSLogger.println(ps_check.toString());
        TDSLogger.println("tx");
        ps_check.executeUpdate();

        //DBConnection.commit(conn_check);
        DBConnection.close(conn_check);
        flag = true;
      }
      DBConnection.close(conn);
      conn_del = DBConnection.getConnection();
      StringBuffer sql_del = new StringBuffer();
      sql_del.append("delete from tf_basic_information_tx where sid = ? and product_body=? and brand=? and version=? ");
      PreparedStatement ps_del = conn_del.prepareStatement(
          sql_del.toString());
      ps_del.setString(1, sid);
      ps_del.setString(2, pd_body);
      ps_del.setString(3, brand);
      ps_del.setString(4, version);

      TDSLogger.println("delete");
      ps_del.executeUpdate();
      DBConnection.close(conn_del);

      //return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      //    flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    //  return flag;
  }

  public static void DL_insert(String sid,
                               String pd_body, String brand,
                               String version, String path) {
    boolean flag = false;
    StringBuffer SelSQL = new StringBuffer();
    String SelSQL1 = null;
    String SelSQL2 = null;
    Connection conn = null;
    Connection conn1 = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {

      //1.將有新舊版本的資料,留下新資料,tag=0砍掉
      //2.刪檔
      //4.改檔名
      //3.update資料

      conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT * FROM tf_document_linkage_tx where  sid='" + sid +
          "' and tag=1 order by doc_type desc,seq");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {

        conn1 = DBConnection.getConnection();
        SelSQL1 = " SELECT * FROM tf_document_linkage_tx where  sid='" +
            sid + "' and tag=0 and doc_type='" +
            rs.getString("doc_type") + "' and seq='" +
            rs.getInt("seq") + "' ";

        TDSLogger.println(SelSQL1.toString());
        PreparedStatement ps1 = conn.prepareStatement(SelSQL1);

        ResultSet rs1 = ps1.executeQuery();
        StringBuffer sql_del = new StringBuffer();
        while (rs1.next()) {

          conn_del = DBConnection.getConnection();

          sql_del.append("delete from tf_document_linkage_tx where sid = ? and doc_type=? and tag=0 and seq=? ");
          PreparedStatement ps_del = conn_del.prepareStatement(
              sql_del.toString());
          ps_del.setString(1, sid);
          ps_del.setString(2, rs.getString("doc_type"));
          ps_del.setInt(3, rs.getInt("seq"));

          TDSLogger.println("tttttre");
          TDSLogger.println("delete");
          ps_del.executeUpdate();

          boolean success = (new File(path + "/File/" +
                                      rs1.getString("file_name"))).
              delete();
          DBConnection.close(conn_del);
        }
        DBConnection.close(conn1);

      }
      DBConnection.close(conn);

      conn = DBConnection.getConnection();
      SelSQL2 = "SELECT * FROM tf_document_linkage_tx where  sid='" + sid +
          "'  order by seq ";

      PreparedStatement ps2 = conn.prepareStatement(SelSQL2);
      ResultSet rs2 = ps2.executeQuery();
      while (rs2.next()) {
        int seq_length = String.valueOf(rs2.getInt("seq")).length();
        String seq = String.valueOf(rs2.getInt("seq"));
        if (seq_length == 1) {
          seq = "00" + seq;
        }
        else if (seq_length == 2) {
          seq = "0" + seq;

        }

        String new_file_name = "";
        new_file_name = pd_body + "_" + brand + "_" + version + "_" +
            rs2.getString("doc_type") + "_" + seq + ".png";
        conn_check = DBConnection.getConnection();

        //StringBuffer sql_check = new StringBuffer();
        sql_check = "insert into tf_document_linkage (sid,product_body,brand,version,doc_type,seq,doc_name,file_name,tf_comment) values (?,?,?,?,?,?,?,?,?) ";

        TDSLogger.println(sql_check);
        PreparedStatement ps_check = conn_check.prepareStatement(
            sql_check);
        ps_check.setString(1, sid);

        ps_check.setString(2, pd_body);
        ps_check.setString(3, brand);
        ps_check.setString(4, version);
        ps_check.setString(5, rs2.getString("doc_type"));
        ps_check.setInt(6, rs2.getInt("seq"));
        ps_check.setString(7, rs2.getString("doc_name"));
        ps_check.setString(8, new_file_name);
        ps_check.setString(9, rs2.getString("tf_comment"));

        TDSLogger.println(ps_check.toString());
        TDSLogger.println("tx");
        ps_check.executeUpdate();
        DBConnection.close(conn_check);

        String file_name_be = "";
        String file_name_af = "";

        file_name_be = rs2.getString("file_name");
        TDSLogger.println(file_name_be);
        TDSLogger.println("file_name_be");
        File file = new File(path + "/File/" + file_name_be);

        file_name_af = pd_body + "_" + brand + "_" + version + "_" +
            rs2.getString("doc_type") + "_" + seq + ".png";

        File file2 = new File(path + "/File/" + file_name_af);
        File file3 = new File(path + "/Release/" + file_name_af);
        boolean success = file.renameTo(file2);

        copy(file2, file3);
        boolean success1 = (new File(path + "/File/" + file_name_af)).
            delete();

      }

      DBConnection.close(conn);

      conn_del = DBConnection.getConnection();
      StringBuffer sql_del = new StringBuffer();
      sql_del.append("delete from tf_document_linkage_tx where sid = ? ");
      PreparedStatement ps_del = conn_del.prepareStatement(
          sql_del.toString());
      ps_del.setString(1, sid);

      TDSLogger.println(ps_del.toString());
      TDSLogger.println("tttttre");
      TDSLogger.println("delete");
      ps_del.executeUpdate();
      DBConnection.close(conn_del);

      //  return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  public static void reset_tx(String sid, String pd_body, String brand,
                              String version) {

    Connection conn = null;
    Connection conn1 = null;
    boolean flag = true;
    String sql = null;
    String InsSQL1 = null;
    String InsertSQL = null;
    try {

      conn1 = DBConnection.getConnection();
      InsSQL1 = "update tf_information set status = ? where sid = ? and product_body=? and brand=? and version=? ";
      PreparedStatement ps3 = conn1.prepareStatement(InsSQL1);
      ps3.setString(1, "R");
      ps3.setInt(2, Integer.parseInt(sid));
      ps3.setString(3, pd_body);
      ps3.setString(4, brand);
      ps3.setString(5, version);
      TDSLogger.println(ps3.toString());
      TDSLogger.println("submit");

      ps3.executeUpdate();

      flag = true;
      // return flag;
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      // return null;
    }
    finally {
      DBConnection.close(conn);
      DBConnection.close(conn1);
      //  return false;
    }

  }

  public static void exit_tx(String sid, String pd_body,
                             String brand, String version) {
    boolean flag = false;
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;

    try {

      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();

      conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT status FROM tf_information where product_body='" +
          pd_body +
          "' and brand='" + brand + "' and version='" +
          version + "' and sid='" + sid + "'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        if (rs.getString("status").equals("P")) {
          flag = true;

        }
        else {

          flag = false;
          //狀態為Process才能update
        }

      }
      DBConnection.close(conn);

      //return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      //flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  public static void copy(File src, File dst) {

    String seq_str = "";
    String file_name = "";
    try {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dst);

// Transfer bytes from in to out
      byte[] buf = new byte[1024];
      int len;
      while ( (len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      in.close();
      out.close();

    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());

    }
    finally {

    }

  }

  public static void PDF_insert_internal(String sid,
                                         String pd_body, String brand,
                                         String version) {
    StringBuffer SelSQL = new StringBuffer();
    boolean flag = false;
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {

      conn_check = DBConnection.getConnection();
      String file_name = "";
      file_name = pd_body + "V" + version + ".pdf";
      sql_check = "insert into release_pdf (sid,product_body,brand,version,type,file_name) values (?,?,?,?,?,?)";
      TDSLogger.println(sql_check);
      PreparedStatement ps_check = conn_check.prepareStatement(
          sql_check);
      ps_check.setString(1, sid);
      ps_check.setString(2, pd_body);
      ps_check.setString(3, brand);
      ps_check.setString(4, version);
      ps_check.setString(5, "pdf_internal");
      ps_check.setString(6, file_name);

      TDSLogger.println(ps_check.toString());
      TDSLogger.println("tx");
      ps_check.executeUpdate();

      //DBConnection.commit(conn_check);
      DBConnection.close(conn_check);
      flag = true;

    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  public static void PDF_insert_compare(String sid,
                                        String pd_body, String brand,
                                        String version) {
    StringBuffer SelSQL = new StringBuffer();
    boolean flag = false;
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {

      conn_check = DBConnection.getConnection();
      String file_name = "";
      file_name = pd_body + "V" + version + "diff.pdf";
      sql_check = "insert into release_pdf (sid,product_body,brand,version,type,file_name) values (?,?,?,?,?,?)";
      TDSLogger.println(sql_check);
      PreparedStatement ps_check = conn_check.prepareStatement(
          sql_check);
      ps_check.setString(1, sid);
      ps_check.setString(2, pd_body);
      ps_check.setString(3, brand);
      ps_check.setString(4, version);
      ps_check.setString(5, "pdf_compare");
      ps_check.setString(6, file_name);

      TDSLogger.println(ps_check.toString());
      TDSLogger.println("tx");
      ps_check.executeUpdate();

      //DBConnection.commit(conn_check);
      DBConnection.close(conn_check);
      flag = true;

    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  public static void PDF_insert_cover(String sid,
                                      String pd_body, String brand,
                                      String version) {
    StringBuffer SelSQL = new StringBuffer();
    boolean flag = false;
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {

      conn_check = DBConnection.getConnection();
      String file_name = "";
      file_name = pd_body + "V" + version + "C.pdf";
      sql_check = "insert into release_pdf (sid,product_body,brand,version,type,file_name) values (?,?,?,?,?,?)";
      TDSLogger.println(sql_check);
      PreparedStatement ps_check = conn_check.prepareStatement(
          sql_check);
      ps_check.setString(1, sid);
      ps_check.setString(2, pd_body);
      ps_check.setString(3, brand);
      ps_check.setString(4, version);
      ps_check.setString(5, "pdf_cover");
      ps_check.setString(6, file_name);

      TDSLogger.println(ps_check.toString());
      TDSLogger.println("tx");
      ps_check.executeUpdate();

      //DBConnection.commit(conn_check);
      DBConnection.close(conn_check);
      flag = true;

    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    // return flag;
  }

  public static void PDF_by_site(String sid, String pd_body,
                                 String brand, String version) {
    boolean flag = false;
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    Connection conn_check = null;
    Connection conn_del = null;
    String sql_check = null;
    try {

      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();

      conn = DBConnection.getConnection();
      SelSQL.append(
          "SELECT distinct site FROM tf_test_parameter_ft where sid='" +
          sid +
          "' union  SELECT distinct site FROM tf_test_parameter_ws where sid='" +
          sid + "'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        conn_check = DBConnection.getConnection();

        //insert外包廠使用之PDF
        String file_name = "";
        file_name = pd_body + "V" + version + "_" + rs.getString("site") +
            ".pdf";
        sql_check = "insert into release_pdf (sid,product_body,brand,version,type,file_name) values (?,?,?,?,?,?)";

        TDSLogger.println(sql_check);
        PreparedStatement ps_check = conn_check.prepareStatement(
            sql_check);
        ps_check.setString(1, sid);
        ps_check.setString(2, pd_body);
        ps_check.setString(3, brand);
        ps_check.setString(4, version);
        ps_check.setString(5, "pdf_site");
        ps_check.setString(6, file_name);

        TDSLogger.println(ps_check.toString());
        TDSLogger.println("tx");
        ps_check.executeUpdate();

        DBConnection.close(conn_check);

        conn_check = DBConnection.getConnection();

        //insert外包廠使用之PDF
        String file_name1 = "";
        sql_check = "";
        file_name1 = pd_body + "V" + version + "C_" +
            rs.getString("site") + ".pdf";
        sql_check = "insert into release_pdf (sid,product_body,brand,version,type,file_name) values (?,?,?,?,?,?)";

        PreparedStatement ps_check1 = conn_check.prepareStatement(
            sql_check);
        ps_check1.setString(1, sid);
        ps_check1.setString(2, pd_body);
        ps_check1.setString(3, brand);
        ps_check1.setString(4, version);
        ps_check1.setString(5, "pdf_site_compare");
        ps_check1.setString(6, file_name1);
        TDSLogger.println("tx");
        ps_check1.executeUpdate();
        DBConnection.close(conn_check);

      }
      DBConnection.close(conn);

      //return flag;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      //    flag = false;
    }
    finally {
      DBConnection.close(conn);
    }
    //  return flag;
  }

  public static OIReleaseActionForm[] pdf_show(String sid, String type,
                                               String path) {

    Connection conn = null;

    String InsertSQL = null;
    try {
      String sql = null;
      conn = DBConnection.getConnection();
      ArrayList tmp = new ArrayList();

      sql = "SELECT * FROM release_pdf a where sid='" +
          sid + "' and type='" + type + "'";

      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      //int yield_count = 0;

      while (rs.next()) {

        // yield_count = yield_count + 1;
        OIReleaseActionForm bean = new
            OIReleaseActionForm();

        if (type.equals("pdf_internal")) {
          bean.setTitle("OI PDF");
        }
        else if (type.equals("pdf_compare")) {
          bean.setTitle("OI PDF DIFF");
        }
        else if (type.equals("pdf_cover")) {
          bean.setTitle("OI PDF Cover Page");
        }
        else if (type.equals("pdf_site")) {
          bean.setTitle("OI PDF：" + rs.getString("file_name"));
        }
        else if (type.equals("pdf_site_compare")) {

          bean.setTitle("OI PDF DIFF：" + rs.getString("file_name"));

        }

        bean.setFile_name(path + "/Release/" + rs.getString("file_name"));

        tmp.add(bean);

      }
      if (tmp.isEmpty()) {
        return (OIReleaseActionForm[])null;
      }
      else {
        return (OIReleaseActionForm[]) tmp.toArray(new
            OIReleaseActionForm[0]);
      }

    }

    catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());

      return null;
    }
    finally {
      DBConnection.close(conn);

    }

  }

  public static void main(String[] args) {
    OIReleaseService oIReleaseService = new
        OIReleaseService();

  }

}
