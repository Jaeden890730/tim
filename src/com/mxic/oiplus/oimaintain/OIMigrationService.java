package com.mxic.oiplus.oimaintain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.oiplus.util.TDSLogger;

public class OIMigrationService {
  public OIMigrationService() {
  }

  public static String getInfo(String pd_body,
                               String brand,
                               String version) {
    Connection conn = null;
    String sid_str = "";
    try {
      String sql = "SELECT sid FROM tf_information where " +
          "product_body='" + pd_body +
          "' and brand='" + brand +
          "' and version='" + version + "' ";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        sid_str = rs.getString("sid");
      }
      return sid_str;
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static boolean exit_version(String brand,
                                     String version,
                                     String pd_body) {

    Connection conn = null;
    boolean flag = true;
    try {
      int version_be = Integer.parseInt(version);
      String sql = "SELECT * FROM tf_information where " +
          "product_body='" + pd_body +
          "' and brand='" + brand +
          "' and version <'" + version_be + "'";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      int count = 0;
      while (rs.next()) {
        count = count + 1;
        flag = false;
      }
      return flag;
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      //flag = false;;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return flag;
  }

  public static String version_upper(String brand,
                                     String pd_body) {

    Connection conn = null;
    String version_str = "";
    try {
      String sql = "SELECT version FROM tf_information where " +
          "product_body='" + pd_body +
          "' and brand='" + brand +
          "' order by version desc";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      int count = 0;
      while (rs.next()) {
        count = count + 1;
        version_str = rs.getString("version");
      }
      if (count == 0) {
        version_str = "No Data!!";
      }
      return version_str;
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return version_str;
  }

  public static OIMigrationActionForm[] getProInfo(String brand,
                                                   String pd_body,
                                                   String version,
                                                   String creator,
                                                   String productType) {

    Connection conn = null;
    String InsertSQL = null;
    try {
      ArrayList tmp = new ArrayList();
      String sql = null;
      sql = "SELECT * FROM tf_information where " +
          "product_body='" + pd_body +
          "' and brand='" + brand +
          "' and version='" + version + "'";

      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      int count = 0;
      while (rs.next()) {
        count = count + 1;
      }
      if (count == 0) {
        InsertSQL = "Insert into tf_information " +
            "(sid,product_body,brand,version,status,creator,log_time,TF_PRODUCT_ROUTE," +
            "TF_BOM_ROUTE,TF_TEST_PARAMETER_WS,TF_TEST_PARAMETER_FT,TF_BASIC_INFORMATION," +
            "TF_DOCUMENT_LINKAGE, TF_TEST_PARAMETER_PBC, PRODUCT_TYPE) values (test_seq.nextval,?,?,?,?,?,sysdate,?,?,?,?,?,?,?,?) ";
        PreparedStatement ps_insert = conn.prepareStatement(InsertSQL);
        ps_insert.setString(1, pd_body.trim());
        ps_insert.setString(2, brand.trim());
        ps_insert.setString(3, version);
        ps_insert.setString(4, "P");
        ps_insert.setString(5, creator.trim());
        ps_insert.setString(6, "N");
        ps_insert.setString(7, "N");
        ps_insert.setString(8, "N");
        ps_insert.setString(9, "N");
        ps_insert.setString(10, "N");
        ps_insert.setString(11, "N");
        ps_insert.setString(12, "N");
        ps_insert.setString(13, productType);
        //TDSLogger.println(ps_insert.toString());
        ps_insert.executeUpdate();
      }
      OIMigrationActionForm bean = new OIMigrationActionForm();
      TDSLogger.println(pd_body);
      TDSLogger.println("pd_body");
      bean.setPd_body(pd_body);
      bean.setBrand(brand);
      bean.setVersion(version);
      tmp.add(bean);
      return (OIMigrationActionForm[]) tmp.toArray(new OIMigrationActionForm[0]);
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  /*****************************************************************
   *主題:第二版資料,自tf_basic_information取得sid version-1的資料,讀出資料存入tf_test_parameter_ft_tx,tag=0
   *****************************************************************/
  public static boolean ins_basic_info(String product_body,
                                       String brand,
                                       String version,
                                       String file_name) {
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      String sid = getInfo(product_body, brand, version);
      //************************************************//
      String str = "";
      del_row(sid, "tf_basic_info_tx");

      FileReader fr = new FileReader(file_name);
      BufferedReader br = new BufferedReader(fr);
      int count = 0;
      String sql_check = null;
      sql_check = "insert into tf_basic_info_tx (sid,tag,product_body,brand,version," +
          "tester,good_bin,fail_bin,remark,options,grade,bin_type,ipn_action,epn_speed,test_speed,down_grade,inkless_grade,ktd_bin_flag) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

      PreparedStatement ps_check = conn.prepareStatement(sql_check);

      while ( (str = br.readLine()) != null) {
        if (count == 0) {
        } else {
          String[] insert_str = getExcelCell(str, 16);

          ps_check.setString(1, sid);
          ps_check.setString(2, "0");
          ps_check.setString(3, product_body.trim());
          ps_check.setString(4, brand.trim());
          ps_check.setString(5, version.trim());

          // tester 6
          ps_check.setString(6, processString(insert_str[5], 15, ""));
          // good_bin 7
          ps_check.setString(7, processString(insert_str[6], 128, ""));
          // fail_bin 8
          ps_check.setString(8, processString(insert_str[7], 32, ""));
          // remark 9
          ps_check.setString(9, processString(insert_str[8], 256, ""));
          // options 10
          ps_check.setString(10, processString(insert_str[9], 1, ""));
          // grade 11
          ps_check.setString(11, processString(insert_str[10], 1, ""));
          // bin_type 12
          if (insert_str[11].equals("BIN only"))
            ps_check.setString(12, processString("0", 2, ""));
          else if (insert_str[11].equals("Speed BIN"))
            ps_check.setString(12, processString("1", 2, ""));
          else
            ps_check.setString(12, processString("2", 2, ""));
          // ipn_action 13
          if (insert_str[12].equals("NA"))
            ps_check.setString(13, processString("0", 2, ""));
          else if (insert_str[12].startsWith("Change IPN"))
            ps_check.setString(13, processString("1", 2, ""));
          else
            ps_check.setString(13, processString("2", 2, ""));
          // epn_speed 14
          ps_check.setString(14, processString(insert_str[13], 4, ""));
          // test_speed 15
          ps_check.setString(15, processString(insert_str[14], 3, ""));
          // down_grade 16
          ps_check.setString(16, processString(insert_str[15], 32, ""));
          // inkless_grade 17
          ps_check.setString(17, processString(insert_str[16], 10, ""));

          ps_check.setString(18, processString(insert_str[17], 4, ""));

          ps_check.executeUpdate();
        }
        count++;
      }
      br.close();
      conn.commit();
      DBConnection.close(conn);
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return true;
  }

  /*****************************************************************
   *主題:ASM 第二版資料,自tf_basic_information取得sid version-1的資料,讀出資料存入tf_test_parameter_ft_tx,tag=0
   *****************************************************************/
  public static boolean ins_basic_info_ASM(String product_body, String brand, String version, String file_name) {
	  Connection conn = null;
	  Workbook workbook = null;
	  try {
		  conn = DBConnection.getConnection();
		  conn.setAutoCommit(false);
		  String sid = getInfo(product_body, brand, version);
		  String[] insert_str = new String[11];
		  //************************************************//
		  del_row(sid, "tf_basic_info_tx");
		  workbook = Workbook.getWorkbook(new File(file_name));
		  Sheet sheet = workbook.getSheet(0);
		  int rowCount = sheet.getRows();

          String sql_check = null;
          sql_check = "insert into tf_basic_info_tx (sid,tag,product_body,brand,version," +
          "tester,good_bin,fail_bin,remark,auto_ship_yield,stop_test_yield,auto_scrap_yield,mrb_yield,sample_yeild) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

          PreparedStatement ps_check = conn.prepareStatement(sql_check);

		  for (int j=1; j<rowCount; j++) {
			  Cell[] cells = sheet.getRow(j);
			  for (int i=0; i<insert_str.length; i++) {
				  if (i < cells.length)
					  insert_str[i] = cells[i].getContents();
				  else
					  insert_str[i] = null;
				  if (insert_str[i] != null)
					  insert_str[i] = insert_str[i].replaceAll("\n", " ");
			  }
			  ps_check.setString(1, sid);
			  ps_check.setString(2, "0");
			  ps_check.setString(3, product_body.trim());
			  ps_check.setString(4, "MX");
			  ps_check.setString(5, version.trim());

			  // tester 6
			  ps_check.setString(6, processString(insert_str[2], 15, ""));
			  // good_bin 7
			  ps_check.setString(7, processString(insert_str[3], 128, ""));
			  // fail_bin 8
			  ps_check.setString(8, processString(insert_str[4], 32, ""));
			  // remark 9
			  ps_check.setString(9, processString(insert_str[5], 128, ""));
			  // auto_ship_yield 10
			  ps_check.setString(10, processString(insert_str[6], 6, ""));
			  // stop_test_yield 11
			  ps_check.setString(11, processString(insert_str[7], 6, ""));
			  // auto_scrap_yield 12
			  ps_check.setString(12, processString(insert_str[8], 6, ""));
			  // mrb_yield 13
			  ps_check.setString(13, processString(insert_str[9], 6, ""));
			  // sample_yield 14
			  ps_check.setString(14, processString(insert_str[10], 6, ""));

			  ps_check.executeUpdate();
		  }
		  conn.commit();
		  DBConnection.close(conn);
	  }
	  catch (Exception ex) {
		  DBConnection.rollback(conn);
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return false;
	  }
	  finally {
		  DBConnection.close(conn);
		  workbook.close();
		  conn = null;
	  }
	  return true;
  }

  public static boolean ins_parameter_ft_info(String product_body,
                                              String brand,
                                              String version,
                                              String file_name,
                                              Vector vendorList,
                                              Vector testerType) {
    Connection conn = null;
    String InsertSQL = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      String sid = getInfo(product_body, brand, version);
      //************************************************//
      String str = "";
      del_row(sid, "tf_test_parameter_ft_tx");

      FileReader fr = new FileReader(file_name);
      BufferedReader br = new BufferedReader(fr);
      int count = 0;
      InsertSQL = "Insert into tf_test_parameter_ft_tx " +
          "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +

          "pin_count,package_type,body_size,tester,site,program_name,i_grade,c_grade,w_grade,y_grade,j_grade,k_grade,l_grade, n_grade, b_grade, e_grade, s_grade," +
          "tf_comment,actual_file,hw_configure) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
      PreparedStatement ps_insert = conn.prepareStatement(InsertSQL);

      while ((str = br.readLine()) != null) {
        if (count == 0) {
        } else {
          String[] insert_str = getExcelCell(str, 24);

          if (!vendorList.contains(insert_str[12])) {
        	  Exception e = new Exception("Vendor not exists : " + insert_str[12]);
        	  throw e;
          }
          if (!testerType.contains(insert_str[11])) {
        	  Exception e = new Exception("Tester not exists : " + insert_str[11]);
        	  throw e;
          }

          if ((!insert_str[2].equals("0")) && (!isPIMExists(insert_str[2], product_body, insert_str[7], insert_str[6], insert_str[9], insert_str[8], insert_str[13], insert_str[12], insert_str[11]))) {
        	  Exception e = new Exception("Program not exists : " + insert_str[2]);
        	  throw e;
          }

          ps_insert.setString(1, sid);  //sid
          ps_insert.setString(2, "0");
          ps_insert.setInt(3, Integer.parseInt(insert_str[2])); //pgm_id
          ps_insert.setString(4, product_body); //product_body
          ps_insert.setString(5, brand); //brand
          ps_insert.setString(6, version); //version

          ps_insert.setString(7, processString(insert_str[6], 5, "")); //test_type
          ps_insert.setString(8, processString(insert_str[7], 1, "0")); //backend_option
          ps_insert.setString(9, processString(insert_str[8], 3, ""));  //pin_count
          ps_insert.setString(10, processString(insert_str[9], 6, "")); //package_type
          ps_insert.setString(11, processString(insert_str[10], 40, "")); //body_size
          ps_insert.setString(12, processString(insert_str[11], 15, "")); //tester
          ps_insert.setString(13, processString(insert_str[12], 15, "")); //site
          ps_insert.setString(14, processString(insert_str[13], 30, "")); //program_name
          ps_insert.setString(15, processString(insert_str[14], 12, "0")); //i_grade
          ps_insert.setString(16, processString(insert_str[15], 12, "0")); //c_grade
          ps_insert.setString(17, processString(insert_str[16], 12, "0")); //w_grade
          ps_insert.setString(18, processString(insert_str[17], 12, "0")); //y_grade
          ps_insert.setString(19, processString(insert_str[18], 12, "0")); //j_grade
          ps_insert.setString(20, processString(insert_str[19], 12, "0")); //k_grade
          ps_insert.setString(21, processString(insert_str[20], 12, "0")); //l_grade
          ps_insert.setString(22, processString(insert_str[21], 12, "0")); //n_grade
          ps_insert.setString(23, processString(insert_str[22], 12, "0")); //b_grade
          ps_insert.setString(24, processString(insert_str[23], 12, "0")); //e_grade
          ps_insert.setString(25, processString(insert_str[24], 12, "0")); //s_grade
          ps_insert.setString(26, StringUtil.Utf8ToBig5(processString(insert_str[25], 64, ""))); //tf_comment
          ps_insert.setString(27,processString(insert_str[26], 15, "")); //actual_file
          ps_insert.setString(28,processString(insert_str[27], 40, "NA")); //hw_configure

          ps_insert.executeUpdate();
        }
        count++;
      }
      br.close();
      conn.commit();
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return true;
  }

  public static boolean ins_parameter_ft_info_ASM(String product_body, String brand, String version, String file_name) {
	  Connection conn = null;
	  String InsertSQL = null;
	  Workbook workbook = null;
	  try {
		  conn = DBConnection.getConnection();
		  conn.setAutoCommit(false);
		  String sid = getInfo(product_body, brand, version);
		  String[] insert_str = new String[14];

//		  ************************************************//
		  del_row(sid, "tf_test_parameter_ft_tx");
		  workbook = Workbook.getWorkbook(new File(file_name));
		  Sheet sheet = workbook.getSheet(0);
		  int rowCount = sheet.getRows();
          InsertSQL = "Insert into tf_test_parameter_ft_tx " +
              "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
              "pin_count,package_type,body_size,tester,site,program_name,i_grade,c_grade," +
              "tf_comment, actual_file, hw_configure) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
          PreparedStatement ps_insert = conn.prepareStatement(InsertSQL);

		  for (int j=1; j<rowCount; j++) {
			  Cell[] cells = sheet.getRow(j);
			  for (int i=0; i<insert_str.length; i++) {
				  if (i < cells.length)
					  insert_str[i] = cells[i].getContents();
				  else
					  insert_str[i] = null;
				  if (insert_str[i] != null)
					  insert_str[i] = insert_str[i].replaceAll("\n", " ");
			  }

			  ps_insert.setString(1, sid);
			  ps_insert.setString(2, "0");
			  ps_insert.setInt(3, Integer.parseInt(insert_str[0]));
			  ps_insert.setString(4, product_body);
			  ps_insert.setString(5, "MX");
			  ps_insert.setString(6, version);

			  // test_type 7
			  ps_insert.setString(7, processString(insert_str[3], 5, ""));
			  // backend_option 8
			  ps_insert.setString(8, processString(insert_str[4], 1, ""));
			  // pin_count 9
			  ps_insert.setString(9, processString(insert_str[5], 3, "0"));
			  // package_type 10
			  ps_insert.setString(10, processString(insert_str[6], 6, ""));
			  // body_size 11
			  ps_insert.setString(11, processString(insert_str[7], 40, ""));
			  // tester 12
			  ps_insert.setString(12, processString(insert_str[8], 15, ""));
			  // site 13
			  ps_insert.setString(13, processString(insert_str[9], 15, ""));
			  // program_name 14
			  ps_insert.setString(14, processString(insert_str[10], 30, ""));
			  // i_grade 15
			  ps_insert.setString(15, processString(insert_str[11], 12, ""));
			  // c_grade 16
			  ps_insert.setString(16, "");
			  //tf_comment 17
			  ps_insert.setString(17, StringUtil.Utf8ToBig5(processString(insert_str[12], 64, "")));
			  // program_name 18
			  ps_insert.setString(18, processString(insert_str[13], 15, ""));
			  //hw_configure 19
			  ps_insert.setString(19, processString(insert_str[14], 40, ""));

			  ps_insert.executeUpdate();
		  }
		  conn.commit();
	  }
	  catch (Exception ex) {
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return false;
	  }
	  finally {
		  DBConnection.close(conn);
		  workbook.close();
		  conn = null;
	  }
	  return true;
  }

  public static boolean ins_parameter_pbc_info(String product_body,
		  String brand,
		  String version,
		  String file_name,
		  Vector vendorList) {
	  Connection conn = null;
	  String InsertSQL = null;

	  try {
		  conn = DBConnection.getConnection();
		  conn.setAutoCommit(false);
		  String sid = getInfo(product_body, brand, version);
//		  ************************************************//
		  String str = "";
		  del_row(sid, "tf_test_parameter_pbc_tx");
		  FileReader fr = new FileReader(file_name);
		  BufferedReader br = new BufferedReader(fr);
		  int count = 0;
          InsertSQL = "Insert into tf_test_parameter_pbc_tx " +
          "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
          "pin_count,package_type,body_size,tester,site,program_name,i_grade,c_grade,w_grade,y_grade,j_grade,k_grade,l_grade, n_grade,b_grade,e_grade, s_grade," +
          "tf_comment,hw_configure) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
          PreparedStatement ps_insert = conn.prepareStatement(InsertSQL);

		  while ((str = br.readLine()) != null) {
			if (count == 0) {
			} else {
			  String[] insert_str = getExcelCell(str, 25);

	          if (!vendorList.contains(insert_str[12])) {
	        	  Exception e = new Exception("Vendor not exists : " + insert_str[12]);
	        	  throw e;
	          }

			  ps_insert.setString(1, sid);
			  ps_insert.setString(2, "0");
			  ps_insert.setInt(3, Integer.parseInt(insert_str[2]));
			  ps_insert.setString(4, product_body);
			  ps_insert.setString(5, brand);
			  ps_insert.setString(6, version);

			  ps_insert.setString(7, processString(insert_str[6], 5, ""));
			  ps_insert.setString(8, processString(insert_str[7], 1, ""));
			  ps_insert.setString(9, processString(insert_str[8], 3, "0"));
			  ps_insert.setString(10, processString(insert_str[9], 6, ""));
			  ps_insert.setString(11, processString(insert_str[10], 40, ""));
			  ps_insert.setString(12, processString(insert_str[11], 15, ""));
			  ps_insert.setString(13, processString(insert_str[12], 15, ""));
			  ps_insert.setString(14, processString(insert_str[13], 30, ""));
			  ps_insert.setString(15, processString(insert_str[14], 12, "0"));
			  ps_insert.setString(16, processString(insert_str[15], 12, ""));
			  ps_insert.setString(17, processString(insert_str[16], 12, ""));//w_grade
			  ps_insert.setString(18, processString(insert_str[17], 12, ""));//y_grade
	          ps_insert.setString(19, processString(insert_str[18], 12, ""));//j_grade
	          ps_insert.setString(20, processString(insert_str[19], 12, ""));//k_grade
	          ps_insert.setString(21, processString(insert_str[20], 12, ""));//l_grade
	          ps_insert.setString(22, processString(insert_str[21], 12, ""));//n_grade
	          ps_insert.setString(22, processString(insert_str[22], 12, ""));//b_grade
	          ps_insert.setString(22, processString(insert_str[23], 12, ""));//e_grade
			  ps_insert.setString(23, processString(insert_str[24], 12, ""));//s_grade
			  ps_insert.setString(24, StringUtil.Utf8ToBig5(processString(insert_str[25], 64, "")));
			  ps_insert.setString(25, StringUtil.Utf8ToBig5(processString(insert_str[26], 40, "")));//hw_configure

			  ps_insert.executeUpdate();
			}
			count++;
		  }
		  br.close();
		  conn.commit();
	  }
	  catch (Exception ex) {
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return false;
	  }
	  finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return true;
  }

  public static boolean ins_parameter_pbc_info_ASM(String product_body, String brand, String version, String file_name) {
	  Connection conn = null;
	  String InsertSQL = null;
	  Workbook workbook = null;
	  try {
		  conn = DBConnection.getConnection();
		  conn.setAutoCommit(false);
		  String sid = getInfo(product_body, brand, version);
		  String[] insert_str = new String[14];

//		  ************************************************//
		  del_row(sid, "tf_test_parameter_pbc_tx");
		  workbook = Workbook.getWorkbook(new File(file_name));
		  Sheet sheet = workbook.getSheet(0);
		  int rowCount = sheet.getRows();

          InsertSQL = "Insert into tf_test_parameter_pbc_tx " +
              "(sid,tag,pgm_id,product_body,brand,version,test_type,backend_option," +
              "pin_count,package_type,body_size,tester,site,program_name,i_grade,c_grade," +
              "tf_comment,hw_configure) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
          PreparedStatement ps_insert = conn.prepareStatement(InsertSQL);

		  for (int j=1; j<rowCount; j++) {
			  Cell[] cells = sheet.getRow(j);
			  for (int i=0; i<insert_str.length; i++) {
				  if (i < cells.length)
					  insert_str[i] = cells[i].getContents();
				  else
					  insert_str[i] = null;
				  if (insert_str[i] != null)
					  insert_str[i] = insert_str[i].replaceAll("\n", " ");
			  }

			  ps_insert.setString(1, sid);
			  ps_insert.setString(2, "0");
			  ps_insert.setInt(3, Integer.parseInt(insert_str[0]));
			  ps_insert.setString(4, product_body);
			  ps_insert.setString(5, "MX");
			  ps_insert.setString(6, version);

			  // test_type 7
			  ps_insert.setString(7, processString(insert_str[3], 5, ""));
			  // backend_option 8
			  ps_insert.setString(8, processString(insert_str[4], 1, ""));
			  // pin_count 9
			  ps_insert.setString(9, processString(insert_str[5], 3, "0"));
			  // package_type 10
			  ps_insert.setString(10, processString(insert_str[6], 6, ""));
			  // body_size 11
			  ps_insert.setString(11, processString(insert_str[7], 40, ""));
			  // tester 12
			  ps_insert.setString(12, processString(insert_str[8], 15, ""));
			  // site 13
			  ps_insert.setString(13, processString(insert_str[9], 15, ""));
			  // program_name 14
			  ps_insert.setString(14, processString(insert_str[10], 30, ""));
			  // i_grade 15
			  ps_insert.setString(15, processString(insert_str[11], 12, "0"));
			  // c_grade 16
			  ps_insert.setString(16, "");
			  //tf_comment 17
			  ps_insert.setString(17, StringUtil.Utf8ToBig5(processString(insert_str[13], 64, "")));
			  //hw_configure 18
			  ps_insert.setString(18, StringUtil.Utf8ToBig5(processString(insert_str[14], 40, "")));

			  ps_insert.executeUpdate();
		  }
		  conn.commit();
	  }
	  catch (Exception ex) {
		  DBConnection.rollback(conn);
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return false;
	  }
	  finally {
		  DBConnection.close(conn);
		  workbook.close();
		  conn = null;
	  }
	  return true;
  }

  public static boolean ins_parameter_ws_info(String product_body,
                                              String brand,
                                              String version,
                                              String file_name,
                                              Vector vendorList,
                                              Vector testerType) {
    Connection conn = null;
    String InsertSQL = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      String sid = getInfo(product_body, brand, version);
      //************************************************//
      String str = "";
      del_row(sid, "tf_test_parameter_ws_tx");
      FileReader fr = new FileReader(file_name);
      BufferedReader br = new BufferedReader(fr);
      int count = 0;
      InsertSQL = "insert into tf_test_parameter_ws_tx " +
          "(sid,tag,pgm_id,product_body,brand,version,mask_option,test_type,tester," +
          "site,program_name,temperature,tf_comment,id,hw_configure) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval,?)";
      PreparedStatement ps2 = conn.prepareStatement(InsertSQL);

      while ((str = br.readLine()) != null) {
        if (count == 0) {
        } else {
          String[] insert_str = getExcelCell(str, 13);
          if (!vendorList.contains(insert_str[9])) {
        	  Exception e = new Exception("Vendor not exists : " + insert_str[9]);
        	  throw e;
          }
          if (!testerType.contains(insert_str[8])) {
        	  Exception e = new Exception("Tester not exists : " + insert_str[8]);
        	  throw e;
          }
          if ((!insert_str[2].equals("0")) && (!isPIMExists(insert_str[2], product_body, insert_str[6], insert_str[7], null, null, insert_str[10], insert_str[9], insert_str[8]))) {
        	  Exception e = new Exception("Program not exists : " + insert_str[2]);
        	  throw e;
          }

          ps2.setString(1, sid);
          ps2.setString(2, "0");
          ps2.setInt(3, Integer.parseInt(insert_str[2]));  //pgm_id
          ps2.setString(4, product_body); //product_body
          ps2.setString(5, brand); //brand
          ps2.setString(6, version); //version

          ps2.setString(7, processString(insert_str[6], 1, "")); //mask_option
		  ps2.setString(8, processString(insert_str[7], 5, "")); //test_type
		  ps2.setString(9, processString(insert_str[8], 15, "")); //tester
		  ps2.setString(10, processString(insert_str[9], 15, "")); //site
		  ps2.setString(11, processString(insert_str[10], 30, "")); //program_name
		  ps2.setString(12, processString(insert_str[11], 12, "")); //temperature
		  ps2.setString(13, StringUtil.Utf8ToBig5(processString(insert_str[12], 64, ""))); //tf_comment
		  ps2.setString(14, StringUtil.Utf8ToBig5(processString(insert_str[13], 40, ""))); //hw_configure

          ps2.executeUpdate();
        }
        count++;
      }
      br.close();
      conn.commit();
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return true;
  }

  public static boolean ins_parameter_ws_info_ASM(String product_body, String brand, String version, String file_name) {
	  Connection conn = null;
	  String InsertSQL = null;
	  Workbook workbook = null;

	  try {
		  conn = DBConnection.getConnection();
		  conn.setAutoCommit(false);
		  String sid = getInfo(product_body, brand, version);
		  String[] insert_str = new String[11];

//		  ************************************************//
		  del_row(sid, "tf_test_parameter_ws_tx");
		  workbook = Workbook.getWorkbook(new File(file_name));
		  Sheet sheet = workbook.getSheet(0);
		  int rowCount = sheet.getRows();

          InsertSQL = "insert into tf_test_parameter_ws_tx " +
          "(sid,tag,pgm_id,product_body,brand,version,mask_option,test_type,tester," +
          "site,program_name,temperature,tf_comment,id,hw_configure) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval,?)";
          PreparedStatement ps2 = conn.prepareStatement(InsertSQL);

		  for (int j=1; j<rowCount; j++) {
			  Cell[] cells = sheet.getRow(j);
			  for (int i=0; i<insert_str.length; i++) {
				  if (i < cells.length)
					  insert_str[i] = cells[i].getContents();
				  else
					  insert_str[i] = null;
				  if (insert_str[i] != null)
					  insert_str[i] = insert_str[i].replaceAll("\n", " ");
			  }
			  ps2.setString(1, sid);
			  ps2.setString(2, "0");
			  ps2.setInt(3, Integer.parseInt(insert_str[0]));
			  ps2.setString(4, product_body);
			  ps2.setString(5, "MX");
			  ps2.setString(6, version);

			  // mask_option 7
			  ps2.setString(7, processString(insert_str[3], 1, ""));
			  // test_type 8
			  ps2.setString(8, processString(insert_str[4], 5, ""));
			  // tester 9
			  ps2.setString(9, processString(insert_str[5], 15, ""));
			  // site 10
			  ps2.setString(10, processString(insert_str[6], 15, ""));
			  // program_name 11
			  ps2.setString(11, processString(insert_str[7], 30, ""));
			  // temperature 12
			  ps2.setString(12, processString(insert_str[8], 12, ""));
			  // tf_comment 13
			  ps2.setString(13, StringUtil.Utf8ToBig5(processString(insert_str[9], 64, "")));
			  // hw_configure 14
			  ps2.setString(14, StringUtil.Utf8ToBig5(processString(insert_str[10], 40, "")));


			  ps2.executeUpdate();
		  }
		  conn.commit();
	  }
	  catch (Exception ex) {
		  DBConnection.rollback(conn);
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return false;
	  }
	  finally {
		  DBConnection.close(conn);
		  workbook.close();
		  conn = null;
	  }
	  return true;
  }

  public static boolean ins_bom_route_info(String product_body,
                                           String brand,
                                           String version,
                                           String file_name) {
    Connection conn = null;
    String InsertSQL = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      String sid = getInfo(product_body, brand, version);
      String[] insert_str = null;

      //String sid = "128";
      //************************************************//
      String str = "";
      del_row(sid, "tf_bom_route_tx");
      FileReader fr = new FileReader(file_name);
      BufferedReader br = new BufferedReader(fr);
      int count = 0;
      InsertSQL = "insert into tf_bom_route_tx " +
          "(sid,tag,product_body,brand,version,backend_option,fg_with_code," +
          "pin_count,package_type,ft_route_code,ft_route,FT_ROUTE_ADD,mask_option," +
          "sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,tf_ws_comment,sales_form,id)" +
          " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'NVM',test_seq.nextval)";
      PreparedStatement ps2 = conn.prepareStatement(InsertSQL.toString());

      while ( (str = br.readLine()) != null) {
        if (count == 0) {
        } else {
          insert_str = getExcelCell(str, 19);

          ps2.setString(1, sid);
          ps2.setString(2, "0");
          ps2.setString(3, product_body);
          ps2.setString(4, brand);
          ps2.setString(5, version);

          ps2.setString(6, processString(insert_str[5], 1, ""));
          ps2.setString(7, processString(insert_str[6], 1, "N"));
          ps2.setString(8, processString(insert_str[7], 3, "0"));
          ps2.setString(9, processString(insert_str[8], 2, ""));
          ps2.setString(10, processString(insert_str[9], 2, "10"));
          ps2.setString(11, processString(insert_str[10], 5, ""));
          ps2.setString(12, processString(insert_str[11], 10, ""));
          ps2.setString(13, processString(insert_str[12], 1, ""));
          ps2.setString(14, processString(insert_str[13], 2, ""));
          ps2.setString(15, processString(insert_str[14], 1, "N"));
          ps2.setString(16, processString(insert_str[15], 5, ""));
          ps2.setString(17, processString(insert_str[16], 12, ""));
          ps2.setString(18, StringUtil.Utf8ToBig5(processString(insert_str[17], 64, "")));
          ps2.setString(19, StringUtil.Utf8ToBig5(processString(insert_str[18], 64, "")));

          ps2.executeUpdate();
        }
        count++;
      }
      br.close();
      conn.commit();
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
    return true;
  }

  public static boolean ins_bom_route_info_ASM(String product_body, String brand, String version, String file_name) {
	  Connection conn = null;
	  String InsertSQL = null;
	  Workbook workbook = null;
	  try {
		  conn = DBConnection.getConnection();
		  conn.setAutoCommit(false);
		  String sid = getInfo(product_body, brand, version);
		  String[] insert_str = new String[11];

		  del_row(sid, "tf_bom_route_tx");
		  workbook = Workbook.getWorkbook(new File(file_name));
		  Sheet sheet = workbook.getSheet(0);
		  int rowCount = sheet.getRows();

          InsertSQL = "insert into tf_bom_route_tx " +
              "(sid,tag,product_body,brand,version,backend_option,fg_with_code," +
              "pin_count,package_type,ft_route_code,ft_route,FT_ROUTE_ADD,mask_option," +
              "sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,tf_ws_comment,sales_form,id)" +
              " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval)";
          PreparedStatement ps2 = conn.prepareStatement(InsertSQL.toString());

		  for (int j=1; j<rowCount; j++) {
			  Cell[] cells = sheet.getRow(j);
			  for (int i=0; i<insert_str.length; i++) {
				  if (i < cells.length)
					  insert_str[i] = cells[i].getContents();
				  else
					  insert_str[i] = null;
				  if (insert_str[i] != null)
					  insert_str[i] = insert_str[i].replaceAll("\n", " ");
			  }
			  ps2.setString(1, sid);
			  ps2.setString(2, "0");
			  ps2.setString(3, product_body);
			  ps2.setString(4, "MX");
			  ps2.setString(5, version);

			  // backend_option
			  ps2.setString(6, processString(insert_str[4], 1, ""));
			  // fg_with_code
			  ps2.setString(7, "N");
			  // pin_count
			  ps2.setString(8, "0");
			  // package_type
			  ps2.setString(9, "NA");
			  // ft_route_code
			  ps2.setString(10, "A0");
			  // ft_route
			  ps2.setString(11, processString(insert_str[2], 5, ""));
			  // FT_ROUTE_ADD
			  ps2.setString(12, processString(insert_str[3], 10, ""));
			  // mask_option
			  ps2.setString(13, processString(insert_str[4], 1, ""));
			  // sort_route_code
			  ps2.setString(14, "NA");
			  // db_with_code
			  ps2.setString(15, "NA");
			  // ws_route
			  ps2.setString(16, processString(insert_str[6], 5, ""));
			  // ws_route_add
			  ps2.setString(17, processString(insert_str[7], 12, ""));
			  // tf_comment
			  ps2.setString(18, processString(insert_str[8], 64, ""));
			  // tf_ws_comment
			  ps2.setString(19, StringUtil.Utf8ToBig5(processString(insert_str[9], 64, "")));
			  // sales_form
			  ps2.setString(20, StringUtil.Utf8ToBig5(processString(insert_str[5], 64, "")));

			  ps2.executeUpdate();
		  }
		  conn.commit();
	  }
	  catch (Exception ex) {
		  DBConnection.rollback(conn);
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  return false;
	  }
	  finally {
		  DBConnection.close(conn);
		  workbook.close();
		  conn = null;
	  }
	  return true;
  }

  public static void del_row(String sid,
                             String table_name) {
    Connection conn = null;
    String sql = null;

    try {
      conn = DBConnection.getConnection();

      sql = "delete from " + table_name + " where sid = ? ";
      if (sid.equals("")) {
        sid = "0";
      }

      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, sid);
      //TDSLogger.println(ps.toString());
      TDSLogger.println("reset_tx");
      ps.executeUpdate();
    }
    catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    }
    finally {
      DBConnection.close(conn);
      conn = null;
    }
  }

  public static String processString(String value, int strLen, String defaultValue) {
	  String buf = null;
	  if (value == null || value.equals("")) {
		  return defaultValue;
	  } else {
		  //for chinese char, one chinese char occupy 2 byte, but java only count 1 byte
		  if (value.getBytes().length >= strLen) {
			  if (value.length() > strLen)
				  buf = value.substring(0, strLen);
			  else
				  buf = value;
			  while (buf.getBytes().length > strLen)
				  buf = buf.substring(0, buf.length() - 1);
			  return buf;
		  } else {
			  return value;
		  }
	  }
  }

  public static String[] getExcelCell(String str, int columnNum) {
	  if (str.charAt(0) == '\"')
		  str = str.substring(1);
	  String[] a = str.split(",\"");
	  String str1 = "";
	  //判斷字串中是否有","
	  for (int i = 0; i < a.length; i++) {
		  if (a.length > 1) {
			  if (a[i].charAt(a[i].length() - 1) == '\"') {
				  String str2 = a[i].substring(0, a[i].length() - 1);
				  str1 = str1 + str2.replace(',', '|') + ",";
			  }
			  else {
				  String[] b = a[i].split("\",");
				  if (b.length == 1)
					  str1 = str1 + b[0] + ",";
				  else {
					  for (int j=0; j<b.length; j++) {
						  if (j == 0)
							  str1 = str1 + b[0].replace(',', '|') + ",";
						  else {
							  str1 = str1 + b[j] + ",";
						  }
					  }
				  }
			  }
		  } else {
			  // 處理一開始即有 " 的情形
			  String[] b = str.split("\"");
			  if (b.length == 1)
				  str1 = str;
			  else {
				  str1 = b[0].replace(',', '|');
				  str1 = str1 + b[1];
			  }
		  }
	  }
	  //避免最後一個是空值找不到資料
	  int col = str1.split(",").length;
	  for (int i=col; i<columnNum; i++) {
		  str1 = str1 + ", ";
	  }
	  String[] insert_str = str1.split(",");
	  for (int k = 0; k < insert_str.length; k++) {
		  insert_str[k] = insert_str[k].replaceAll("\\|", ",").trim();
	  }
	  return insert_str;
  }

  public static Vector getVendors() {
	  Vector result = new Vector();
	  Connection conn = null;
	  String sql = "SELECT PLANT_NAME FROM BA_PLANT ORDER BY PLANT_NAME";
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sql);
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  String vendor = rs.getString("PLANT_NAME");
			  result.add(vendor);
		  }
		  return result;
	  }
	  catch (Exception ex) {
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  //flag = false;
	  }
	  finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }

  public static Vector getTesterType(int facility) {
	  Vector result = new Vector();
	  Connection conn = null;
	  StringBuffer sql = new StringBuffer();
	  sql.append("SELECT DESCRIP TESTER_TYPE FROM BA_DESCRIPTION_LIST ");
	  sql.append("WHERE TAG = 403 ");
	  sql.append("ORDER BY TESTER_TYPE");
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sql.toString());
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  String vendor = rs.getString("TESTER_TYPE");
			  result.add(vendor);
		  }
		  return result;
	  }
	  catch (Exception ex) {
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  //flag = false;
	  }
	  finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }

  public static boolean isPIMExists(String pgmID, String productBody, String option, String testMode, String packageType, String pinCount, String programName, String Site, String testerType) {
	  Connection conn = null;
	  StringBuffer sql = new StringBuffer();
	  int count = 0;
	  sql.append("SELECT COUNT(1) CNT FROM PG_TEST_PROGRAM P, PG_PLANT_RELEASE R, BA_PLANT B ");
	  sql.append("WHERE P.SID = R.PG_SID AND R.PLANT_NO = B.PLANT_NO ");
	  sql.append("AND P.PROGRAM_ID = " + pgmID + " AND P.PRODUCT_CODE = '" + productBody + option + "' ");
	  if (packageType != null)
		  sql.append("AND P.PACKAGE_TYPE = '" + packageType + "' AND P.PIN_COUNT = " + pinCount + " ");
	  sql.append("AND P.PROGRAM_NAME = '" + programName + "' AND B.PLANT_NAME = '" + Site + "' ");
	  sql.append("AND P.TEST_MODE = '" + testMode + "' AND TESTER_TYPE = '" + testerType + "' ");
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sql.toString());
		  ResultSet rs = ps.executeQuery();
		  while (rs.next()) {
			  count = rs.getInt("CNT");
		  }
	  }
	  catch (Exception ex) {
		  ex.fillInStackTrace();
		  TDSLogger.println(ex.getMessage());
		  //flag = false;
	  }
	  finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return (count > 0);
  }

  public static void main(String[] args) {
//    OIMigrationService oIMigrationService = new OIMigrationService();

  }
}
