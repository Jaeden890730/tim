package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.TDSLogger;

public class DocLinkageService {
  public DocLinkageService() {
  }

  /*****************************************************************
   *主題:get第一版資料
   *****************************************************************/
  public static DocLinkageActionForm[] getInitialInfo(int sid) {
    Connection conn = null;
    Connection conn1 = null;
    String product = "";
    String product_body = "";
    String brand = "";
    int version = 0;
    boolean check_tx_boolean = true;
    boolean check_first_boolean = true;
    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
    String releasePath = pdfProp.getValue("jpg.path") + File.separator;
    String txPath = pdfProp.getValue("jpg_tx.path") + File.separator;

    String InsertSQL = null;
    try {
      String sql = null;
      product = getInfo1(sid);
      String product1[] = product.split(",");
      product_body = product1[0];
      brand = product1[1];
      version = Integer.parseInt(product1[2]);
      check_tx_boolean = check_tx(sid);
      if (check_tx_boolean && 
    		  !com.mxic.oiplus.util.OIinformation.isSubmitted(""+sid, "TF_DOCUMENT_LINKAGE")) {
        //_tx中無資料
        check_first_boolean = check_first(product_body, brand, version - 1);
        if (check_first_boolean) {
          ArrayList tmp = new ArrayList();
          return (DocLinkageActionForm[]) tmp.toArray(new DocLinkageActionForm[0]);
        } else {
          //為第二版資料,取得sid version-1的資料,讀出資料存入tf_test_parameter_ft_tx,tag=0
          int version_two = version - 1;
          ArrayList tmp = new ArrayList();

          sql = "SELECT * FROM tf_document_linkage a where " +
              "product_body='" + product_body +
              "' and brand='" + brand +
              "' and version ='" + version_two +
              "' order by doc_type desc, seq ";

          conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql);
          ResultSet rs = ps.executeQuery();
          conn1 = DBConnection.getConnection();
          conn1.setAutoCommit(false);

          while (rs.next()) {
            DocLinkageActionForm bean = new DocLinkageActionForm();
            if (rs.getString("doc_type").equals("Y")) {
              bean.setCategory("Yield Definition");
              bean.setDoc_type("Y");
            } else if (rs.getString("doc_type").equals("T")) {
              bean.setCategory("Test Flow");
              bean.setDoc_type("T");
            } else if (rs.getString("doc_type").equals("M")) {
              bean.setCategory("Comment");
              bean.setDoc_type("M");
          }

            if (rs.getString("doc_type").equals("Y") &&
                Integer.toString(rs.getInt("seq")).equals("1")) {
              bean.setTag("1");
            } else {
              bean.setTag("0");
            }

            String file_name_be = "";
            String file_name_af = "";

            file_name_be = rs.getString("file_name");
            int seq_length = String.valueOf(rs.getInt("seq")).length();
            String seq = String.valueOf(rs.getInt("seq"));
            if (seq_length == 1) {
              seq = "00" + seq;
            } else if (seq_length == 2) {
              seq = "0" + seq;
            }

            File file = new File(releasePath + file_name_be);
            if (rs.getString("doc_type").equals("M"))
              //file_name_af = product_body + "_" + brand + "_" + version + "_" +
              file_name_af = product_body + "_" + version + "_" +
                  rs.getString("doc_type") + ".png";
            else
              //file_name_af = product_body + "_" + brand + "_" + version + "_" +
              file_name_af = product_body + "_" + version + "_" +
                  rs.getString("doc_type") + "_0_" + seq + ".png";
            File file2 = new File(txPath + file_name_af);
            copy(file, file2);

            InsertSQL = "Insert into tf_document_linkage_tx " +
                "(sid,tag,product_body,brand,version,doc_type,seq,doc_name," +
                "file_name,tf_comment) values (?,?,?,?,?,?,?,?,?,?) ";

            PreparedStatement ps_insert = conn1.prepareStatement(InsertSQL);
            ps_insert.clearParameters();
            ps_insert.setInt(1, sid);
            ps_insert.setString(2, "0");
            ps_insert.setString(3, product_body);
            ps_insert.setString(4, brand);
            ps_insert.setInt(5, version);
            ps_insert.setString(6, rs.getString("doc_type"));
            ps_insert.setInt(7, Integer.parseInt(rs.getString("seq")));
            ps_insert.setString(8, rs.getString("doc_name").trim());
            ps_insert.setString(9, file_name_af);
            if (rs.getString("tf_comment") == null)
              ps_insert.setString(10, rs.getString("tf_comment"));
            else
              ps_insert.setString(10, rs.getString("tf_comment").trim());
            ps_insert.executeUpdate();

            if (!rs.getString("doc_type").equals("M")) {
              bean.setDoc_name(rs.getString("doc_name"));
              bean.setComment(rs.getString("tf_comment"));
              bean.setFile_name_old(file_name_af);
              bean.setFile_name_new(" ");
              //bean.setSeq(rs.getInt("seq"));
              bean.setSeq(rs.getString("seq"));
              bean.setShow_new("no");
              bean.setShow_old("show");
              //bean.setSeq(rs.getInt("seq"));
              bean.setSeq(rs.getString("seq"));
              tmp.add(bean);
            }
          }
          conn1.commit();
          conn1.setAutoCommit(true);
          DBConnection.close(conn1);
          DBConnection.close(conn);
          return (DocLinkageActionForm[]) tmp.toArray(new DocLinkageActionForm[0]);
        }
      } else {
        //tx中有資料
        ArrayList tmp = new ArrayList();
        sql = "SELECT distinct seq,sid,product_body,brand,version,doc_type,tf_comment," +
            "doc_name FROM tf_document_linkage_tx where doc_type='Y' and sid='" +
            sid + "' order by seq ";
        conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        conn1 = DBConnection.getConnection();
        while (rs.next()) {
          DocLinkageActionForm bean = new DocLinkageActionForm();
          //bean.setSeq(rs.getInt("seq"));
          bean.setSeq(rs.getString("seq"));
          //bean.setSid(sid);
          bean.setSid(String.valueOf(sid));
          bean.setPd_body(product_body);
          bean.setBrand(brand);
          bean.setVersion(String.valueOf(version));
          bean.setComment(rs.getString("tf_comment"));
          bean.setDoc_name(rs.getString("doc_name"));
          bean.setCategory("Yield Definition");
          bean.setDoc_type("Y");
          if (rs.getString("seq").equals("1") &&
              rs.getString("doc_type").equals("Y")) {
            bean.setTag("1");
          } else {
            bean.setTag("0");
          }

          boolean zero_exist = count_seq(sid, "Y", rs.getString("seq"), "0");

          if (zero_exist == false) {
            bean.setFile_name_old(" ");
            bean.setShow_old("no");
          } else if (zero_exist == true) {
            String sql_check = null;
            sql_check = "SELECT file_name FROM tf_document_linkage_tx where " +
                "sid='" + sid + "' and tag=0 and " +
                "seq='" + rs.getString("seq") +
                "' and doc_type='Y'";

            PreparedStatement ps_check = conn1.prepareStatement(sql_check);
            ResultSet rs_check = ps_check.executeQuery();
            while (rs_check.next()) {
              bean.setFile_name_old(rs_check.getString("file_name"));
              bean.setShow_old("show");
            }
            rs_check.close();
            ps_check.close();
            rs_check = null;
            ps_check = null;
          }
          boolean one_exist = count_seq(sid, "Y", rs.getString("seq"), "1");
          if (one_exist == false) {
            bean.setFile_name_new(" ");
            bean.setShow_new("no");
          } else if (one_exist == true) {
            String sql_check = null;
            sql_check = "SELECT file_name FROM tf_document_linkage_tx where " +
                "sid='" + sid + "' and tag=1 and " +
                "seq='" + rs.getString("seq") +
                "' and doc_type='Y' ";

            PreparedStatement ps_check = conn1.prepareStatement(sql_check);
            ResultSet rs_check = ps_check.executeQuery();
            while (rs_check.next()) {
              bean.setFile_name_new(rs_check.getString("file_name"));
              bean.setShow_new("show");
            }
            rs_check.close();
            ps_check.close();
            rs_check = null;
            ps_check = null;
          }
          tmp.add(bean);
        }

        sql = null;
        sql = "SELECT distinct seq,sid,product_body,brand,version,doc_type,tf_comment," +
            "doc_name FROM tf_document_linkage_tx where doc_type='T' and sid='" +
            sid + "' order by seq";
        PreparedStatement ps1 = conn.prepareStatement(sql);
        ResultSet rs1 = ps1.executeQuery();

        while (rs1.next()) {
          DocLinkageActionForm bean = new DocLinkageActionForm();
          //bean.setSeq(rs1.getInt("seq"));
          bean.setSeq(rs1.getString("seq"));
          //bean.setSid(sid);
          bean.setSid(String.valueOf(sid));
          bean.setPd_body(product_body);
          bean.setBrand(brand);
          bean.setVersion(String.valueOf(version));
          bean.setComment(rs1.getString("tf_comment"));
          bean.setDoc_name(rs1.getString("doc_name"));
          bean.setCategory("Test Flow");
          bean.setTag("0");
          bean.setDoc_type("T");
          boolean zero_exist = count_seq(sid, "T", rs1.getString("seq"), "0");
          if (zero_exist == false) {
            bean.setFile_name_old(" ");
            bean.setShow_old("no");
          } else if (zero_exist == true) {
            String sql_check = null;
            sql_check = "SELECT file_name FROM tf_document_linkage_tx where " +
                "sid='" + sid + "' and tag=0 and " +
                "seq='" + rs1.getString("seq") +
                "' and doc_type='T'";

            PreparedStatement ps_check = conn1.prepareStatement(sql_check);
            ResultSet rs_check = ps_check.executeQuery();
            while (rs_check.next()) {
              bean.setFile_name_old(rs_check.getString("file_name"));
              bean.setShow_old("show");
            }
            rs_check.close();
            ps_check.close();
            rs_check = null;
            ps_check = null;
          }

          boolean one_exist = count_seq(sid, "T", rs1.getString("seq"), "1");

          if (one_exist == false) {
            bean.setFile_name_new(" ");
            bean.setShow_new("no");
          } else if (one_exist == true) {
            String sql_check = null;
            sql_check = "SELECT file_name FROM tf_document_linkage_tx where " +
                "sid='" + sid + "' and tag=1 and " +
                "seq='" + rs1.getString("seq") +
                "' and doc_type='T' ";

            PreparedStatement ps_check = conn1.prepareStatement(sql_check);
            ResultSet rs_check = ps_check.executeQuery();
            while (rs_check.next()) {
              bean.setFile_name_new(rs_check.getString("file_name"));
              bean.setShow_new("show");
            }
            rs_check.close();
            ps_check.close();
            rs_check = null;
            ps_check = null;
          }
          tmp.add(bean);
        }
        return (DocLinkageActionForm[]) tmp.toArray(new DocLinkageActionForm[0]);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn1);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
      DBConnection.close(conn1);
    }
  }

  /*****************************************************************
   *主題:取出product_body,brand,version
   *****************************************************************/
  public static String getInfo1(int sid) {
      Connection conn = null;
      boolean flag = true;
      String product = "";
      try {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT product_body,brand,version " +
                   " FROM tf_information where sid='" + sid + "'");

        conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ResultSet rs = ps.executeQuery();
        ArrayList tmp = new ArrayList();
        while (rs.next()) {
          product = rs.getString("product_body") + "," +
              rs.getString("brand") + "," + rs.getString("version");
        }
        return product;
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        flag = false;
        return null;
      } finally {
        DBConnection.close(conn);
      }
    }

    public static boolean check_tx(int sid) {
      Connection conn_check_first = null;
      boolean flag = true;

      try {
        String sql_check_first =
            "SELECT count(*) as total_count FROM tf_document_linkage_tx where sid='" +
            sid + "'";
        conn_check_first = DBConnection.getConnection();
        PreparedStatement ps_check_first =
            conn_check_first.prepareStatement(sql_check_first.toString());
        ResultSet rs_leave_day = ps_check_first.executeQuery();
        while (rs_leave_day.next()) {
          if (rs_leave_day.getString("total_count").equals("0") == true) {
            //tf_test_parameter_ft_tx中已有資料,直接存取tf_test_parameter_ft_tx中的資料即可
            flag = true;
          } else {
            flag = false;
          }
        }

        return flag;
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        flag = false;
      } finally {
        DBConnection.close(conn_check_first);
        return flag;
      }
    }

    public static boolean count_seq(int sid,
                                    String doc_type,
                                    String seq,
                                    String tag) {

      Connection conn_check_first = null;
      boolean flag = false;
      try {
        String sql_check_first =
            "SELECT count(*)as total_count FROM tf_document_linkage_tx " +
            "where sid='" + sid +
            "' and tag='" + tag +
            "' and seq='" + seq +
            "' and doc_type='" + doc_type + "'";

        conn_check_first = DBConnection.getConnection();
        PreparedStatement ps_check_first =
            conn_check_first.prepareStatement(sql_check_first.toString());
        ResultSet rs_leave_day = ps_check_first.executeQuery();

        while (rs_leave_day.next()) {
          if (rs_leave_day.getInt("total_count") == 0) {
            flag = false;
          } else {
            flag = true;
          }
        }

        return flag;
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        flag = false;
      } finally {
        DBConnection.close(conn_check_first);
        return flag;
      }
    }

    /*****************************************************************
     *主題:檢查FTTest是否為第一版==>New,version已減過1
     *****************************************************************/
    public static boolean check_first(String product_body,
                                      String brand,
                                      int version) {

      Connection conn_check_first = null;
      boolean flag = true;

      try {
        String sql_check_first =
            "SELECT count(*) as total_count FROM tf_document_linkage " +
            "where product_body='" + product_body +
            "' and brand='" + brand +
            "' and version='" + version + "'";
        conn_check_first = DBConnection.getConnection();
        PreparedStatement ps_check_first =
            conn_check_first.prepareStatement(sql_check_first.toString());
        ResultSet rs_leave_day = ps_check_first.executeQuery();

        while (rs_leave_day.next()) {
          if (rs_leave_day.getString("total_count").equals("0") == true) {
            //tf_test_parameter_ft中有資料表示不為第一版
            flag = true;
          } else {
            flag = false;
          }
        }

        return flag;
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        flag = false;
      } finally {
        DBConnection.close(conn_check_first);
        return flag;
      }
    }

    /*****************************************************************
     *主題:刪除row資料
     *****************************************************************/
    public static String delete_row(String record_id,
                                    int sid,
                                    String brand,
                                    String version,
                                    String pd_body) {

      Connection conn = null;
      Connection conn1 = null;
      String InsertSQL = null;
      Connection conn_exit = null;
      boolean flag = true;
      String category = "";
      String doc_type = "";
      int seq = 0;
      String sql = null;
      String seq_str = "";
      try {
        String record_id_str[] = record_id.split(",");
        seq = Integer.parseInt(record_id_str[0]);
        TDSLogger.println(seq);
        TDSLogger.println("seq");
        category = record_id_str[1];
        String old_file = record_id_str[2];
        String new_file = record_id_str[3];
        if (category.equals("Test Flow")) {
          doc_type = "T";
        } else if (category.equals("Yield Definition")) {
          doc_type = "Y";
        }

        conn = DBConnection.getConnection();
        //(sid,tag,product_body,brand,version,tf_option,tester,good_bin,fail_bin,soak_time)
        sql = "delete from tf_document_linkage_tx where sid = ? and doc_type=? and seq=? ";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, sid);
        ps.setString(2, doc_type);
        ps.setInt(3, seq);

        ps.executeUpdate();

        String path = TDSResource.getProperties("TIMPdf").getValue("jpg_tx.path") + File.separator;
        if (old_file.equals("") || old_file == null) {
        } else {
          boolean success = (new File(path + old_file)).delete();
          TDSLogger.println(success);
          TDSLogger.println("success");
        }
        if (new_file.equals("") || new_file == null) {
        } else {
          boolean success1 = (new File(path + new_file)).delete();
          TDSLogger.println(success1);
          TDSLogger.println("success1");
        }

        conn_exit = DBConnection.getConnection();
        StringBuffer sql1 = new StringBuffer();
        sql1.append("SELECT * FROM tf_document_linkage_tx where sid='" +
                    sid + "'  and doc_type='" + doc_type +
                    "' and seq > '" + seq + "'");
        PreparedStatement ps1 = conn_exit.prepareStatement(sql1.toString());
        ResultSet rs = ps1.executeQuery();

        conn1 = DBConnection.getConnection();
        InsertSQL = "update tf_document_linkage_tx set seq=?,file_name=? " +
            "where sid = ? and  doc_type=? and seq=? ";
        PreparedStatement ps_insert = conn1.prepareStatement(InsertSQL);

        while (rs.next()) {
          String file_name = update_file_addr(rs.getString("seq"), sid,
                                              brand, version,
                                              pd_body, rs.getString("tag"), path, doc_type);

          ps_insert.setInt(1, rs.getInt("seq") - 1);
          ps_insert.setString(2, file_name);
          ps_insert.setInt(3, sid);
          ps_insert.setString(4, doc_type);
          ps_insert.setInt(5, rs.getInt("seq"));

          ps_insert.executeUpdate();
        }
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        flag = false;
      } finally {
        DBConnection.close(conn);
        DBConnection.close(conn1);
        DBConnection.close(conn_exit);
        return seq_str;
      }
      //return false;
    }

    public static String update_file_addr(String seq,
                                          int sid,
                                          String brand,
                                          String version,
                                          String pd_body,
                                          String tag,
                                          String path,
                                          String doc_type) {

      String seq_str = "";
      String file_name = "";
      try {
        int seq_int_de = Integer.parseInt(seq) - 1;
        String seq_str_de = String.valueOf(seq_int_de);
        int seq_length = seq.length();
        int seq_length_de = seq_str_de.length();
        if (seq_length == 1) {
          seq = "00" + seq;
        } else if (seq_length == 2) {
          seq = "0" + seq;
        }

        if (seq_length_de == 1) {
          seq_str_de = "00" + seq_str_de;
        } else if (seq_length == 2) {
          seq_str_de = "0" + seq_str_de;
        }

        String file_name_be = "";
        String file_name_af = "";

        //file_name_be = pd_body + "_" + brand + "_" + version + "_" +
        file_name_be = pd_body + "_" + version + "_" +
            doc_type + "_" + tag + "_" + seq + ".png";
        File file = new File(path + file_name_be);

        //file_name_af = pd_body + "_" + brand + "_" + version + "_" +
        file_name_af = pd_body + "_" + version + "_" +
            doc_type + "_" + tag + "_" + seq_str_de + ".png";
        file_name = file_name_af;

        File file2 = new File(path + file_name_af);
        boolean success = file.renameTo(file2);
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
      } finally {
      }
      return file_name;
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
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
    }
  }

  public static boolean update_data(String[] sid,
                                    String[] pd_body,
                                    String[] brand,
                                    String[] version,
                                    String[] doc_name,
                                    String[] doc_name_be,
                                    String[] comment,
                                    String[] comment_be,
                                    String[] seq,
                                    String[] doc_type,
                                    String flag) {

    String InsSQL = null;
    String InsSQL1 = null;
    Connection conn = null;
    boolean result = false;

    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);

      if (doc_name != null) {
    	  InsSQL = "update tf_document_linkage_tx set seq=?, doc_name=?, tf_comment=? \n" +
    	  		   "where sid=? and product_body=? and brand=? and version=? and doc_type=? " +
    	  		   "and seq=?";
    	  PreparedStatement ps2 = conn.prepareStatement(InsSQL);

    	  for (int i = 0; i < seq.length; i++) {
    		  ps2.setString(1, seq[i]);
    		  ps2.setString(2, doc_name[i].trim());
    		  ps2.setString(3, comment[i].trim());
    		  ps2.setString(4, sid[i]);
    		  ps2.setString(5, pd_body[i]);
    		  ps2.setString(6, brand[i]);
    		  ps2.setString(7, version[i]);
    		  ps2.setString(8, doc_type[i]);
    		  ps2.setString(9, seq[i]);
    		  ps2.executeUpdate();
    	  }
      }
      if (flag.equals("submit_cmd")) {
        InsSQL1 = "update tf_information set TF_DOCUMENT_LINKAGE = ? " +
            "where sid=? and product_body=? and brand=? and version=? ";
        PreparedStatement ps3 = conn.prepareStatement(InsSQL1);
        ps3.setString(1, "Y");
        ps3.setInt(2, Integer.parseInt(sid[0]));
        ps3.setString(3, pd_body[0]);
        ps3.setString(4, brand[0]);
        ps3.setString(5, version[0]);
        ps3.executeUpdate();
      }
      conn.commit();
      result = true;
    } catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
    }
    return result;
  }

  /*****************************************************************
   *主題:reset資料,即delete tf_document_linkage_tx 中之相資關資料
   ******************************************************************/
  public static boolean reset_tx(int sid,
                                 String brand,
                                 String version,
                                 String pd_body) {

    Connection conn = null;
    Connection conn1 = null;
    Connection conn2 = null;
    boolean flag = true;
    String sql = null;
    String sql2 = null;
    String InsSQL1 = null;
    try {
      conn2 = DBConnection.getConnection();
      sql2 = "SELECT * FROM tf_document_linkage_tx a where sid='" + sid + "' ";

      PreparedStatement ps2 = conn2.prepareStatement(sql2);
      ResultSet rs2 = ps2.executeQuery();

      while (rs2.next()) {
        boolean success = (new File(TDSResource.getProperties("TIMPdf").getValue("jpg_tx.path") + File.separator + rs2.getString("file_name"))).delete();
        TDSLogger.println(success);
      }
      DBConnection.close(conn2);

      TDSLogger.println(sid);
      TDSLogger.println("sid");
      conn = DBConnection.getConnection();
      sql = "delete from tf_document_linkage_tx where sid = ? ";

      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.executeUpdate();

      conn1 = DBConnection.getConnection();
      InsSQL1 = "update tf_information set TF_DOCUMENT_LINKAGE = ? " +
          "where sid=? and product_body=? and brand=? and version=? ";

      PreparedStatement ps3 = conn1.prepareStatement(InsSQL1);
      ps3.setString(1, "N");
      ps3.setInt(2, sid);
      ps3.setString(3, pd_body);
      ps3.setString(4, brand);
      ps3.setString(5, version);
      ps3.executeUpdate();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn2);
      DBConnection.close(conn1);
      DBConnection.close(conn);
    }
    return false;
  }

  public static DocLinkageActionForm[] getInitialInfo_comment(int sid) {

    Connection conn = null;
    boolean check_tx_boolean = true;
    try {
      String sql = null;
      check_tx_boolean = check_tx(sid);
      ArrayList tmp = new ArrayList();
      if (check_tx_boolean) {
        DocLinkageActionForm bean = new DocLinkageActionForm();
        bean.setFile_name_comment("");
        bean.setShow_comment("no");
        tmp.add(bean);
      } else {
        //tx中有資料
        sql = "SELECT file_name FROM tf_document_linkage_tx " +
            "where doc_type='M' and sid='" + sid + "'";

        conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
          count = count + 1;
          DocLinkageActionForm bean = new DocLinkageActionForm();
          bean.setFile_name_comment(rs.getString("file_name"));
          bean.setShow_comment("show");
          tmp.add(bean);
        }
        if (count == 0) {
          DocLinkageActionForm bean = new DocLinkageActionForm();
          bean.setFile_name_comment("");
          bean.setShow_comment("no");
          tmp.add(bean);
        }
      }
      return (DocLinkageActionForm[]) tmp.toArray(new DocLinkageActionForm[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  public static void main(String[] args) {
    DocLinkageService docLinkageService = new DocLinkageService();
  }
}
