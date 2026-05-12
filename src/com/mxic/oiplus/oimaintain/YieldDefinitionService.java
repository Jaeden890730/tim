package com.mxic.oiplus.oimaintain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import jxl.*;
import java.io.*;
import java.sql.*;
import org.apache.struts.upload.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.util.OIinformation;
import com.mxic.oiplus.resource.TDSResource;

public class YieldDefinitionService {
  public YieldDefinitionService() {
  }

  /*****************************************************************
   * get data from *_tx table
   *****************************************************************/
  public static YieldDefinitionActionForm[] getInitialInfo(int sid) {
    Connection conn = null;
    String product = "";
    String product_body = "";
    String brand = "";
    int version = 0;
    boolean check_tx_boolean = true;
    boolean check_first_boolean = true;

    try {
      StringBuffer sql = new StringBuffer();
      product = TFIMBasicService.getInfo1(sid);
      String product1[] = product.split(",");
      product_body = product1[0];
      brand = product1[1];
      version = Integer.parseInt(product1[2]);
      check_tx_boolean = check_tx(sid);
      if (check_tx_boolean) {
        // _tx 中無資料
        check_first_boolean = check_first(product_body, brand, version - 1);
        if (!check_first_boolean) {
          // 前一版有資料, 取得前一版的資料, 存入 tf_yield_tx
          int version_two = version - 1;
          sql.append("INSERT INTO TF_YIELD_tx " +
                     "(SID,PRODUCT_BODY,BRAND,VERSION,YID,SEQ,PRODUCT_CODE,TEST_MODE,AUTO_SHIP,HOLD_PE,HOLD_BIN,HOLD_BIN_CRI,AUTO_SCRAP,STOP,MRB,SAMPLING_YIELD,NOTES) ");
          sql.append("SELECT " + sid + ",PRODUCT_BODY,BRAND," + version +
                     ",YID,SEQ,PRODUCT_CODE,TEST_MODE,AUTO_SHIP,HOLD_PE,HOLD_BIN,HOLD_BIN_CRI,AUTO_SCRAP,STOP,MRB,SAMPLING_YIELD,NOTES ");
          sql.append("FROM TF_YIELD WHERE product_body='" + product_body +
                     "' and brand='" + brand + "' and version ='" + version_two + "'");
          conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ps.executeUpdate();
          ps.close();
          ps=null;
          DBConnection.close(conn);
        }
      }
      sql = new StringBuffer();
      sql.append(
          "SELECT * FROM TF_YIELD_tx a where " +
          "product_body='" +product_body +
          "' and brand='" + brand +
          "' and version ='" + version + "' order by seq");
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        YieldDefinitionActionForm bean = new YieldDefinitionActionForm();
        bean.setSid(sid);
        bean.setPd_body(product_body);
        bean.setBrand(brand);
        bean.setVersion(""+version);
        bean.setYid(rs.getInt("YID"));
        bean.setSeq(rs.getInt("SEQ"));
        bean.setProduct_code(rs.getString("product_code"));
        bean.setTest_mode(rs.getString("test_mode"));
        bean.setAuto_ship(rs.getString("auto_ship"));
        bean.setHold_pe(rs.getString("hold_pe"));
        bean.setHold_bin(rs.getString("hold_bin"));
        bean.setHold_bin_cri(rs.getString("hold_bin_cri"));
        bean.setAuto_scrap(rs.getString("auto_scrap"));
        bean.setStop(rs.getString("stop"));
        bean.setMrb(rs.getString("mrb"));
        bean.setSampling_yield(rs.getString("sampling_yield"));
        bean.setNotes(rs.getString("notes"));
        tmp.add(bean);
      }
      int previousSid = OIinformation.getPreviousVersion(sid); 
      download(previousSid);
      return (YieldDefinitionActionForm[]) tmp.toArray(new YieldDefinitionActionForm[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  /*****************************************************************
   * check if data already copy from previous version
   *****************************************************************/
  public static boolean check_tx(int sid) {
    Connection conn_check_first = null;
    boolean flag = true;
    try {
      String sql_check_first =
          "SELECT count(*) as total_count FROM TF_YIELD_TX where sid='" +
          sid + "'";
      conn_check_first = DBConnection.getConnection();
      PreparedStatement ps_check_first =
          conn_check_first.prepareStatement(sql_check_first.toString());
      ResultSet rs_leave_day = ps_check_first.executeQuery();
      while (rs_leave_day.next()) {
        if (rs_leave_day.getString("total_count").equals("0") == true) {
          flag = true;
        } else {
          flag = false;
        }
      }
      DBConnection.close(conn_check_first);
      return flag;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn_check_first);
    }
    return flag;
  }

  /*****************************************************************
   * check 前一版是否有資料
   *****************************************************************/
  public static boolean check_first(String product_body,
                                    String brand,
                                    int version) {
    Connection conn_check_first = null;
    boolean flag = true;
    try {
      String sql_check_first =
          "SELECT count(*) as total_count FROM TF_YIELD where " +
          "product_body='" + product_body +
          "' and brand='" + brand +
          "' and version='" + version + "'";
      conn_check_first = DBConnection.getConnection();
      PreparedStatement ps_check_first =
          conn_check_first.prepareStatement(sql_check_first.toString());
      ResultSet rs_leave_day = ps_check_first.executeQuery();

      while (rs_leave_day.next()) {
        if (rs_leave_day.getString("total_count").equals("0") == true) {
          flag = true;
        } else {
          flag = false;
        }
      }
      DBConnection.close(conn_check_first);
      return flag;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      flag = false;
    } finally {
      DBConnection.close(conn_check_first);
    }
    return flag;
  }

  /*****************************************************************
   * 刪除 tf_yield_tx 資料, 並重 copy 自 tf_yield 最新版
   ******************************************************************/
  public static boolean reset_tx(int sid,
                                 String brand,
                                 String version,
                                 String pd_body) {

    Connection conn = null;
    String sql = null;
    String InsSQL1 = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      sql = "delete from tf_yield_tx " +
          "where sid = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.executeUpdate();
      ps.close();
      ps = null;

      InsSQL1 = "update tf_information set TF_YIELD_DEFINITION = ? " +
          "where sid = ? ";
      PreparedStatement ps3 = conn.prepareStatement(InsSQL1);
      ps3.setString(1, "N");
      ps3.setInt(2, sid);
      ps3.executeUpdate();
      conn.commit();
    } catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
      DBConnection.close(conn);
    }
    return false;
  }


  /********************************************************************
   * download data into migration.dir
   *********************************************************************/
  public static boolean download(int sid) {

    String downloadPath = TDSResource.getProperties("TIMPdf").getValue("jpg_tx.path");
    String downloadFile = downloadPath + File.separator + (""+sid).trim() + ".xls";
    Connection conn = null;

    try {
      jxl.write.WritableWorkbook wwb = jxl.Workbook.createWorkbook(new File(downloadFile));
      jxl.write.WritableSheet ws = wwb.createSheet("Yield Definition",0);

      jxl.write.WritableCellFormat wcf = new jxl.write.WritableCellFormat();
      wcf.setBackground(jxl.format.Colour.ICE_BLUE);
      wcf.setAlignment(jxl.format.Alignment.CENTRE);
      wcf.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.MEDIUM);
      ws.addCell(new jxl.write.Label(0, 0, "System Id", wcf));
      ws.addCell(new jxl.write.Label(1, 0, "Product Code", wcf));
      ws.addCell(new jxl.write.Label(2, 0, "Test Mode", wcf));
      ws.addCell(new jxl.write.Label(3, 0, "Auto Ship", wcf));
      ws.addCell(new jxl.write.Label(4, 0, "Hold PE", wcf));
      ws.addCell(new jxl.write.Label(5, 0, "Hold BIN", wcf));
      ws.addCell(new jxl.write.Label(6, 0, "Hold Bin Criteria", wcf));
      ws.addCell(new jxl.write.Label(7, 0, "Auto Scrap", wcf));
      ws.addCell(new jxl.write.Label(8, 0, "Stop", wcf));
      ws.addCell(new jxl.write.Label(9, 0, "OOC", wcf));
      ws.addCell(new jxl.write.Label(10, 0, "Sampling Yield", wcf));
      ws.addCell(new jxl.write.Label(11, 0, "Notes", wcf));
      ws.setColumnView(0, 15);
      ws.setColumnView(1, 15);
      ws.setColumnView(2, 15);
      ws.setColumnView(3, 30);
      ws.setColumnView(4, 30);
      ws.setColumnView(5, 30);
      ws.setColumnView(6, 30);
      ws.setColumnView(7, 30);
      ws.setColumnView(8, 30);
      ws.setColumnView(9, 30);
      ws.setColumnView(10, 30);
      ws.setColumnView(11, 30);
      //Item Field Cell format
      jxl.write.WritableCellFormat wcfc = new jxl.write.WritableCellFormat();
      wcfc.setAlignment(jxl.format.Alignment.CENTRE);
      wcfc.setBorder(jxl.format.Border.LEFT, jxl.format.BorderLineStyle.MEDIUM);
      wcfc.setBorder(jxl.format.Border.BOTTOM, jxl.format.BorderLineStyle.THIN);
      wcfc.setShrinkToFit(true);
      wcfc.setWrap(true);

      StringBuffer sql = new StringBuffer();
      sql.append(
          "SELECT * FROM TF_YIELD a where " +
          "sid =  "+ sid +" order by seq");
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();
      int i = 0;
      while (rs.next()) {
        ws.addCell(new jxl.write.Label(0, i + 1, ""+rs.getInt("YID"), wcfc));
        ws.addCell(new jxl.write.Label(1, i + 1, rs.getString("PRODUCT_CODE"), wcfc));
        ws.addCell(new jxl.write.Label(2, i + 1, rs.getString("TEST_MODE"), wcfc));
        ws.addCell(new jxl.write.Label(3, i + 1, rs.getString("AUTO_SHIP"), wcfc));
        ws.addCell(new jxl.write.Label(4, i + 1, rs.getString("HOLD_PE"), wcfc));
        ws.addCell(new jxl.write.Label(5, i + 1, rs.getString("HOLD_BIN"), wcfc));
        ws.addCell(new jxl.write.Label(6, i + 1, rs.getString("HOLD_BIN_CRI"), wcfc));
        ws.addCell(new jxl.write.Label(7, i + 1, rs.getString("AUTO_SCRAP"), wcfc));
        ws.addCell(new jxl.write.Label(8, i + 1, rs.getString("STOP"), wcfc));
        ws.addCell(new jxl.write.Label(9, i + 1, rs.getString("MRB"), wcfc));
        ws.addCell(new jxl.write.Label(10, i + 1, rs.getString("SAMPLING_YIELD"), wcfc));
        ws.addCell(new jxl.write.Label(11, i + 1, rs.getString("NOTES"), wcfc));
        i++;
      }
      wwb.write();
      wwb.close();
      rs.close();
      ps.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    } finally {
      DBConnection.close(conn);
    }

    return true;
  }

    /********************************************************************
     * 驗證 upload 資料，若成功則置入 tf_yield_2tx
     *********************************************************************/
  public static String upload_verify(int sid,
                                     String productBody, String brand, String version,
                                     FormFile filename) {
    Connection conn = null;
    StringBuffer result = new StringBuffer();
    Workbook workbook = null;
    String temppath = TDSResource.getProperties("TIMPdf").getValue("migration.dir");
    String tempfile = temppath + filename.getFileName();

    int total_record_inserted=0;
    try {
      // 將 user upload file 存到 server
      FileOutputStream fileOutput = new FileOutputStream(tempfile);
      fileOutput.write(filename.getFileData());
      fileOutput.flush();
      fileOutput.close();
      filename.destroy();

      workbook = Workbook.getWorkbook(new File(tempfile));
      Sheet sheet = workbook.getSheet(0);
      int rowCount = sheet.getRows();
      int errtag = 0;
      long[] yid = new long[rowCount];

      String fileFormat = checkUploadFormat(sheet);
      if (!fileFormat.equals("")) {
        return fileFormat;
      }

      // 資料比對
      int maxlen[] = {0,32,16,64,64,64,64,64,64,64,64,256};
      String fields[] = {"System Id","Product Code","Test Mode",
    		  "Auto Ship","Hold PE","Hold BIN","Hold Bin Criteria","Auto Scrap",
    		  "Stop","OOC","Sampling Yield","Notes"};
      
      for (int i=1; i<rowCount; i++) {
        Cell[] cells = sheet.getRow(i);
        if (cells.length == 0)
          yid[i] = 0;
        else {
          try {
            yid[i] = Long.parseLong(cells[0].getContents());
          } catch (Exception ex) { // not Number
              yid[i] = 0;
          }
        }

        if (blankLine(cells)) {
          yid[i] = -1;
          continue; // 空白行
        }
        if (cells.length < 3) {
          result.append("<tr class=\"title1\"><td colspan=\"12\" align=\"left\">"+
                        (++errtag)+". 資料欄位不足 at row#="+(i+1)+"</td></tr>\n");
          result.append(getUploadRowStirng(cells));
          continue;
        }
        if (cells[1].getContents().trim().equals("")) {
          result.append("<tr class=\"title1\"><td colspan=\"12\" align=\"left\">"+
                        (++errtag)+". Product Code at row#="+(i+1)+" 必需要有值</td></tr>\n");
          result.append(getUploadRowStirng(cells));
          continue;
        }
        if (cells[2].getContents().trim().equals("")) {
          result.append("<tr class=\"title1\"><td colspan=\"12\" align=\"left\">"+
                        (++errtag)+". Test Mode at row#="+(i+1)+" 必需要有值</td></tr>\n");
          result.append(getUploadRowStirng(cells));
          continue;
        }
        for (int j=1;j<12;j++) //total 11 columns
        	if (cells.length > j)
        		if (cells[j].getContents().length()> maxlen[j]) {
        			result.append("<tr class=\"title1\"><td colspan=\"12\" align=\"left\">"+
        					(++errtag)+". Row#"+(i+1)+": "+fields[j]+" 欄位長度超過 "+maxlen[j]+"</td></tr>\n");
        			result.append(getUploadRowStirng(cells));
        		}
      }
      for (int i=0; i<rowCount; i++) {
        for (int j=i+1; j<rowCount; j++) {
          if (yid[i] > 0 && yid[j] > 0 && yid[i] == yid[j])
            result.append("<tr class=\"title1\"><td colspan=\"12\" align=\"left\">"+
                          (++errtag)+". System id "+yid[i]+" confilcted at row#="+(i+1)+" and row#"+(j+1)+"</td></tr>\n");
        }
      }

      if (!result.toString().equals(""))
        return result.toString();

      // 置入資料
      conn = DBConnection.getConnection();
      String sql_withid = "insert into tf_yield_2tx (SID,PRODUCT_BODY,BRAND,VERSION,YID,SEQ,PRODUCT_CODE,TEST_MODE,AUTO_SHIP,HOLD_PE,HOLD_BIN,HOLD_BIN_CRI,AUTO_SCRAP,STOP,MRB,SAMPLING_YIELD,NOTES)\n"+
          "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      conn.setAutoCommit(false);
      PreparedStatement ps = conn.prepareStatement("delete from tf_yield_2tx where product_body = ? and brand = ?");
      ps.setString(1, productBody);
      ps.setString(2, brand);
      ps.executeUpdate();
      ps = null;

      ps = conn.prepareStatement(sql_withid);
      for (int i=1; i<rowCount; i++) {
        if (yid[i] < 0) continue; // 空白行不處理
        total_record_inserted++;
        Cell[] cells = sheet.getRow(i);
        ps.setLong(1, sid);
        ps.setString(2, productBody);
        ps.setString(3, brand);
        ps.setString(4, version);
        ps.setLong(5, yid[i]); // if yid[i] == 0, trigger 會 assign 一個值
        ps.setInt(6, i);
        ps.setString(7,cells[1].getContents().trim());
        ps.setString(8,cells[2].getContents().trim());
        ps.setString(9, (cells.length>3?cells[3].getContents():null));
        ps.setString(10,(cells.length>4?cells[4].getContents():null));
        ps.setString(11,(cells.length>5?cells[5].getContents():null));
        ps.setString(12,(cells.length>6?cells[6].getContents():null));
        ps.setString(13,(cells.length>7?cells[7].getContents():null));
        ps.setString(14,(cells.length>8?cells[8].getContents():null));
        ps.setString(15,(cells.length>9?cells[9].getContents():null));
        ps.setString(16,(cells.length>10?cells[10].getContents():null));
        ps.setString(17,(cells.length>11?cells[11].getContents():null));
        ps.executeUpdate();
      }
      conn.commit();
      conn.setAutoCommit(true);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      if (result.toString().equals(""))
        return "<tr class=\"title1\"><td colspan=\"12\" align=\"left\">Data insert failure !! : "+ex.getMessage()+"</td></tr>";
      else
        return result.toString();
    } finally {
      if (conn != null)
        DBConnection.close(conn);
    }
    if (total_record_inserted == 0)
      return "<tr class=\"title1\"><td colspan=\"12\" align=\"left\">必需要有 yieid definition 資料，不能上傳空白內容！</td></tr>";
    return "success";
  }

  /*****************************************************************
   * 將資料由 tf_yield_2tx copy 至 tf_yield_tx (upload data)
   ******************************************************************/
  public static String upload_data(int sid) {
    Connection conn = null;
    String sql = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      sql = "delete from tf_yield_tx where sid = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.executeUpdate();
      ps.close();
      ps = null;

      sql = "insert into tf_yield_tx select * from tf_yield_2tx where sid = ?";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, sid);
      ps.executeUpdate();
      conn.commit();
    } catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return "Data upload failure: "+ex.getMessage();
    } finally {
      DBConnection.close(conn);
    }
    return "";
  }

   /********************************************************************
   * get Upload Rows for Error report
   *********************************************************************/
  private static String checkUploadFormat(Sheet sheet) {
    int rowCount = sheet.getRows();
    String result = "<tr class=\"title1\"><td colspan=\"12\" align=\"left\">檔案格式不正確，請使用下載檔案來更新資料！</td></tr>";

    if (rowCount > 1) {
        Cell[] cells = sheet.getRow(0);
        if (cells.length == 12) {
          if (cells[0].getContents().equals("System Id") &&
              cells[1].getContents().equals("Product Code") &&
              cells[2].getContents().equals("Test Mode") &&
              cells[3].getContents().equals("Auto Ship") &&
              cells[4].getContents().equals("Hold PE") &&
              cells[5].getContents().equals("Hold BIN") &&
              cells[6].getContents().equals("Hold Bin Criteria") &&
              cells[7].getContents().equals("Auto Scrap") &&
              cells[8].getContents().equals("Stop") &&
              cells[9].getContents().equals("OOC") &&
              cells[10].getContents().equals("Sampling Yield") &&
              cells[11].getContents().equals("Notes"))
            return "";
        }
      } else
        result = "<tr class=\"title1\"><td colspan=\"12\" align=\"left\">必需要有 yieid definition 資料，不能上傳空白內容！</td></tr>";

    return result;
  }

   /********************************************************************
   * get Upload Rows for Error report
   *********************************************************************/
  private static String getUploadRowStirng(Cell[] cells) {

    StringBuffer result = new StringBuffer("<tr>");

    for (int i=0; i<cells.length; i++) {
      result.append("<td>"+cells[i].getContents()+"</td>");
    }

    return result.append("</tr>").toString();
  }

  /********************************************************************
   * get Upload Rows for Error report
   *********************************************************************/
  private static boolean blankLine(Cell[] cells) {

    for (int i=0; i<cells.length; i++)
      if (!cells[i].getContents().trim().equals(""))
        return false;

    return true;
  }

  /*****************************************************************
   * get data from *_tx table
   *****************************************************************/
  public static YieldDefinitionActionForm[] compare(String compType, int cursid, int presid) {
    Connection conn = null;

    try {
      String sql = "call tf_compare_yield_definition("+cursid+","+presid+","+"'"+compType+"')";
      conn = DBConnection.getConnection();
      CallableStatement stm = conn.prepareCall(sql);
      stm.execute();
      SQLWarning warning = stm.getWarnings();
      stm.close();

      sql = "select * from tf_cmp_yield where sid in ("+cursid+","+presid+") order by ord, yid, type";
      PreparedStatement ps = conn.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      ArrayList tmp = new ArrayList();
      while (rs.next()) {
        YieldDefinitionActionForm bean = new YieldDefinitionActionForm();
        bean.setType(rs.getString("TYPE"));
        bean.setSid(rs.getInt("SID"));
        bean.setPd_body(rs.getString("PRODUCT_BODY"));
        bean.setBrand(rs.getString("BRAND"));
        bean.setVersion(""+rs.getInt("VERSION"));
        bean.setYid(rs.getInt("YID"));
        bean.setSeq(rs.getInt("SEQ"));
        bean.setProduct_code(rs.getString("product_code"));
        bean.setTest_mode(rs.getString("test_mode"));
        bean.setAuto_ship(rs.getString("auto_ship"));
        bean.setHold_pe(rs.getString("hold_pe"));
        bean.setHold_bin(rs.getString("hold_bin"));
        bean.setHold_bin_cri(rs.getString("hold_bin_cri"));
        bean.setAuto_scrap(rs.getString("auto_scrap"));
        bean.setStop(rs.getString("stop"));
        bean.setMrb(rs.getString("mrb"));
        bean.setSampling_yield(rs.getString("sampling_yield"));
        bean.setNotes(rs.getString("notes"));
        bean.setProduct_code_flag(rs.getInt("product_code_flag"));
        bean.setTest_mode_flag(rs.getInt("test_mode_flag"));
        bean.setAuto_ship_flag(rs.getInt("auto_ship_flag"));
        bean.setHold_pe_flag(rs.getInt("hold_pe_flag"));
        bean.setHold_bin_flag(rs.getInt("hold_bin_flag"));
        bean.setHold_bin_cri_flag(rs.getInt("hold_bin_cri_flag"));
        bean.setAuto_scrap_flag(rs.getInt("auto_scrap_flag"));
        bean.setStop_flag(rs.getInt("stop_flag"));
        bean.setMrb_flag(rs.getInt("mrb_flag"));
        bean.setSampling_yield_flag(rs.getInt("sampling_yield_flag"));
        bean.setNotes_flag(rs.getInt("notes_flag"));
        tmp.add(bean);
      }
      return (YieldDefinitionActionForm[]) tmp.toArray(new YieldDefinitionActionForm[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    } finally {
      DBConnection.close(conn);
    }
  }

  /********************************************************************
   * submit()
   *********************************************************************/
  public static boolean submit(int sid) {

    String InsSQL = null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      InsSQL = "update tf_information set TF_YIELD_DEFINITION = 'Y' where sid = ? ";
      PreparedStatement ps = conn.prepareStatement(InsSQL);
      ps.setInt(1, sid);
      ps.executeUpdate();
      ps.close();
      ps = null;
      conn.commit();
    } catch (Exception ex) {
      DBConnection.rollback(conn);
      ex.printStackTrace();
      return false;
    } finally {
      DBConnection.close(conn);
    }
    return true;
  }



  public static void main(String[] args) {
//    TFIMBasicService tFIMBasicService = new TFIMBasicService();
  }
}
