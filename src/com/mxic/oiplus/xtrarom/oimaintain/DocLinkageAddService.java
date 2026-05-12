package com.mxic.oiplus.xtrarom.oimaintain;

import com.mxic.oiplus.resource.*;
//import com.mxic.oiplus.rs.*;

import java.sql.*;
import java.util.*;
import java.lang.*;
import com.mxic.oiplus.util.StringUtil.*;
import com.mxic.oiplus.util.*;
import java.util.Date;
import java.text.*;
import java.io.*;
import javax.swing.*;

public class DocLinkageAddService {
  public DocLinkageAddService() {
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_test_parameter_ft_tx中之相資關資料
   ******************************************************************/
  public static String file_name(int sid,
                                 String category,
                                 int seq) {

    Connection conn = null;
    boolean flag = true;
    String file_name = "";
    try {
      StringBuffer sql = new StringBuffer();
      //若有讀到則復蓋file_name...,若無讀到直接新增
      if (category.equals("Yield Definition")) {
        sql.append("SELECT file_name FROM tf_document_linkage_tx where sid='" + sid +
                   "' and tag='1' and doc_type='Y' and seq='" + seq + "'");
      } else if (category.equals("Test Flow")) {
        sql.append("SELECT file_name FROM tf_document_linkage_tx where sid='" + sid +
                   "' and tag='1' and doc_type='T' and seq='" + seq + "'");
      } else {
        sql.append("SELECT file_name FROM tf_document_linkage_tx where sid='" + sid +
                   "' and doc_type='M' ");
      }

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        file_name = rs.getString("file_name");
      }
//      DBConnection.close(conn);
      return file_name;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_test_parameter_ft_tx中之相資關資料
   ******************************************************************/
  public static void update_file_name(int sid,
                                      String pd_body,
                                      String brand,
                                      String version,
                                      int seq,
                                      String doc_name,
                                      String file_name,
                                      String comment,
                                      String category,
                                      String doc_flag) {

    Connection conn = null;
    Connection conn1 = null;
    Connection conn2 = null;
    boolean flag = true;
    String InsSQL3 = null;
    try {
      if (doc_flag.equals("M")){
        String doc_type = "M";
        conn2 = DBConnection.getConnection();
        InsSQL3 = "Insert into tf_document_linkage_tx (sid,tag,product_body,brand,version,doc_type,seq,doc_name,file_name,tf_comment) values (?,?,?,?,?,?,?,?,?,?) ";
        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);
        ps_insert3.setInt(1, sid);
        ps_insert3.setString(2, "1");
        ps_insert3.setString(3, pd_body);
        ps_insert3.setString(4, brand);
        ps_insert3.setString(5, version);
        ps_insert3.setString(6, doc_type);
        ps_insert3.setInt(7, seq);
        ps_insert3.setString(8, doc_name.trim());
        ps_insert3.setString(9, file_name);
        ps_insert3.setString(10, comment.trim());
        ps_insert3.executeUpdate();
      } else {
        StringBuffer sql = new StringBuffer();
        String doc_type = "";
        if (category.equals("Yield Definition")) {
          doc_type = "Y";
        } else if (category.equals("Test Flow")) {
          doc_type = "T";
        }

        //***記得有少找出option及tester的條件喔~~
         sql.append("SELECT count(*) as total_count FROM tf_document_linkage_tx " +
                    "where sid='" + sid +
                    "' and doc_type='" + doc_type +
                    "' and tag=1 and seq='" + seq + "'");
        conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ResultSet rs = ps.executeQuery();

        conn2 = DBConnection.getConnection();
        InsSQL3 = "Insert into tf_document_linkage_tx (sid,tag,product_body,brand,version,doc_type,seq,doc_name,file_name,tf_comment) values (?,?,?,?,?,?,?,?,?,?) ";
        PreparedStatement ps_insert3 = conn2.prepareStatement(InsSQL3);

        while (rs.next()) {
          if (rs.getString("total_count").equals("0") == true) {
            ps_insert3.setInt(1, sid);
            ps_insert3.setString(2, "1");
            ps_insert3.setString(3, pd_body);
            ps_insert3.setString(4, brand);
            ps_insert3.setString(5, version);
            ps_insert3.setString(6, doc_type);
            ps_insert3.setInt(7, seq);
            ps_insert3.setString(8, doc_name.trim());
            ps_insert3.setString(9, file_name);
            ps_insert3.setString(10, comment.trim());
            ps_insert3.executeUpdate();
          }
        }
      }
//      DBConnection.close(conn2);
//      DBConnection.close(conn);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn2);
      DBConnection.close(conn);
      //return flag;
    }
  }

  public static DocLinkageAddActionForm[] getbean(int sid) {
    ArrayList tmp = new ArrayList();
    DocLinkageAddActionForm bean = new DocLinkageAddActionForm();
    bean.setSid(sid);
    tmp.add(bean);
    return (DocLinkageAddActionForm[]) tmp.toArray(new DocLinkageAddActionForm[0]);
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_test_parameter_ft_tx中之相資關資料
   ******************************************************************/
  public static String add_new(int sid,
                               String pd_body,
                               String brand,
                               String version,
                               String doc_name,
                               String comment,
                               String doc_type) {

    Connection conn = null;
    String InsSQL3 = null;
    String file_name="";
    int seq = 1;
    String seq_add_str = null;

    if ((comment == null) || (comment.length() == 0))
      comment = " ";
    try {
      StringBuffer sql = new StringBuffer();

      /* get next seq number */
      sql.append("SELECT NVL(MAX(SEQ), 0) + 1 SEQ FROM TF_DOCUMENT_LINKAGE_TX WHERE SID = " + sid + " ");
      sql.append("AND DOC_TYPE = '" + doc_type + "' ");
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        seq = rs.getInt("SEQ");
      }

      if (seq < 10)
        seq_add_str = "00" + Integer.toString(seq);
      else
        seq_add_str = "0" + Integer.toString(seq);
      ps.close();

      //file_name = pd_body + "_" + brand + "_" + version +"_" + doc_type +"_1_"+seq_add_str+".png";
      file_name = pd_body +  "_" + version +"_" + doc_type +"_1_"+seq_add_str+".png";
      InsSQL3 = "INSERT INTO TF_DOCUMENT_LINKAGE_TX (SID,TAG,PRODUCT_BODY,BRAND,VERSION,DOC_TYPE,SEQ,DOC_NAME,FILE_NAME,TF_COMMENT) VALUES (?,?,?,?,?,?,?,?,?,?) ";

      PreparedStatement ps_insert3 = conn.prepareStatement(InsSQL3);
      ps_insert3.setInt(1, sid);
      ps_insert3.setString(2, "1");
      ps_insert3.setString(3, pd_body);
      ps_insert3.setString(4, brand);
      ps_insert3.setString(5, version);
      ps_insert3.setString(6, doc_type);
      ps_insert3.setInt(7, seq);
      ps_insert3.setString(8, doc_name.trim());
      ps_insert3.setString(9, file_name);
      ps_insert3.setString(10, comment.trim());
      ps_insert3.executeUpdate();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
    }
    return file_name;
  }

  public static void main(String[] args) {
    DocLinkageAddService docLinkageAddService = new DocLinkageAddService();
  }
}
