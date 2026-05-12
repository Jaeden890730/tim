package com.mxic.oiplus.xtrarom.pdf;

import java.io.*;
import java.sql.*;
import java.util.*;

import java.awt.*;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.mxic.oiplus.xtrarom.oimaintain.FTTFormBean;
import com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService;
import com.mxic.oiplus.xtrarom.oimaintain.WsTestBean;
import com.mxic.oiplus.xtrarom.oisearch.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.pdf.PDFdiffService;

public class pdfService {
  public pdfService() {
  }

  /**
   * <p>Title:</p>
   *
   * <p>Description:Get all the mask_option in the tf_test_parameter_ws table, and save them in javabean</p>
   *
   */
  public static WsTestBean[] ToGetMask_Option(ProTestRouteBeanAF fm, Connection conn, String table) {
    StringBuffer SelSQL = new StringBuffer();
//    StringBuffer sql=new StringBuffer();

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", fm.getSid());
      if (fm.getVendor() != null)
        whereStem.put("site", fm.getVendor());
      SelSQL.append("SELECT mask_option FROM tf_test_parameter_ws"+table+"  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("group by mask_option");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        WsTestBean wtb = new WsTestBean();
        wtb.setMask_option(rs.getString("mask_option"));
        tmp2.add(wtb);
      }

      return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {

    }
    return null;
  }
  /**
   * <p>Title:</p>
   *
    * <p>Description:Get all the mask_option in the tf_test_parameter_ws table, and save them in javabean</p>
    *
    */
  public static FTTFormBean[] ToGetBackend_Option(ProTestRouteBeanAF fm, Connection conn, String table) {
    StringBuffer SelSQL = new StringBuffer();
//    StringBuffer sql=new StringBuffer();

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", fm.getSid());
      if (fm.getVendor() != null)
        whereStem.put("site", fm.getVendor());
      SelSQL.append("SELECT backend_option FROM tf_test_parameter_ft"+table+"  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("group by backend_option");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        FTTFormBean wtb = new FTTFormBean();
        wtb.setBackend_option(rs.getString("backend_option"));
        tmp2.add(wtb);
      }

      return (FTTFormBean[]) tmp2.toArray(new FTTFormBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {

    }
    return null;
  }


  /**
   * <p>Title:</p>
   *
   * <p>Description:Get all the mask_option in the tf_test_parameter_ws table filtered by vendor name </p>
   *
   */
  public static WsTestBean[] ToGetMask_OptionForVendor(ProTestRouteBeanAF fm, Connection conn, String table) {
    StringBuffer SelSQL = new StringBuffer();
//    StringBuffer sql=new StringBuffer();

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", fm.getSid());
      whereStem.put("site",fm.getVendor());
      SelSQL.append("SELECT mask_option FROM tf_test_parameter_ws"+table+"  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("group by mask_option");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()){
        WsTestBean wtb = new WsTestBean();

        wtb.setMask_option(rs.getString("mask_option"));
        tmp2.add(wtb);
      }
      return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
    }catch (Exception ex) {
      ex.printStackTrace();
    } finally {

    }
    return null;
  }

/*
  public static PdfPTable Bom_Product_Route_1stPage(PdfPTable tabExtend,
                                                    ProTestRouteBeanAF fm,
                                                    Font F,
                                                    Connection conn) {

    try {
      String sql = "select * from tf_bom_route where sid=? ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,fm.getSid());
      ResultSet rs = ps.executeQuery();

      while (rs.next()){
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("brand"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("product_body"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("backend_option"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("fg_with_code"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("pin_count"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("package_type"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("ft_route_code"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("ft_route"),F)));
        if (rs.getString("ft_route_add") != null){
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("ft_route_add"),F)));
        } else {
          tabExtend.addCell(new Phrase(new Chunk("  ",F)));
        }
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("mask_option"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("db_with_code"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("sort_route_code"),F)));
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("ws_route"),F)));
        if (rs.getString("ws_route_add") != null){
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("ws_route_add"),F)));
        } else {
          tabExtend.addCell(new Phrase(new Chunk("  ",F)));
        }
        PdfPCell cmt = null;
        if (rs.getString("tf_comment") != null){
          cmt = new PdfPCell(new Phrase(rs.getString("tf_comment"), F));
        } else {
          cmt = new PdfPCell(new Phrase(" ", F));
        }
        cmt.setColspan(4);
        tabExtend.addCell(cmt);
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      System.out.println(ex.getMessage());
      return null;
    } finally {
      return tabExtend;
    }
  }
*/

  public static String[] GetCoverPage(String pro_b,
                                      String br,
                                      String version,
                                      String vendor,
                                      Connection conn) {
    String[] rtn = {"", ""};
    try {
      String sql = "select approve_no,to_char(approve_date,'yyyy/mm/dd') approve_date " +
          "from tf_coverpage where product_body=? and brand=? and version =? ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,pro_b);
      ps.setString(2,br);
      ps.setString(3,version);
      ResultSet rs = ps.executeQuery();
      while(rs.next()){
//        rtn[0] = rs.getString("approve_no");
//        if (rtn[0] == null) rtn[0] = "";
    	if (br.equals("KH"))
    		rtn[0] = "8049K-" + pro_b;
    	else
    		rtn[0] = "8049-" + pro_b;
        rtn[1] = rs.getString("approve_date");
        if (rtn[1] == null) rtn[1] = "";
        break;
      }
      ps.clearParameters();
      ps.close();
      rs.close();

      if ((vendor != null) && (rtn[0].length() > 0)) {
    	sql = "select tim_short_name short_name from ba_plant where plant_name = '" + vendor + "'";
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next()){
        	String short_name = rs.getString("short_name");
        	rtn[0] = rtn[0] + "." + short_name;
          break;
        }
        ps.clearParameters();
        ps.close();
        rs.close();
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    return rtn;
  }

  public static PdfPTable CoverPage(PdfPTable tabExtend,
                                    String pro_b,
                                    String br,
                                    String version,
                                    Font F,
                                    Connection conn,
                                    boolean subconFlag) {

    try {
      String sql = "select TO_CHAR(approve_date,'YYYY/MM/DD') as aDate," +
          "version,approve_no,applicant,remark from tf_coverpage " +
          "where product_body=? and brand=? and ";
      if (subconFlag)
    	  sql = sql + "version=? ";
      else
    	  sql = sql + "version<=? order by version ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,pro_b);
      ps.setString(2,br);
      ps.setString(3,version);
      ResultSet rs = ps.executeQuery();
      // while ResultSet.next �ɡA�N���쪺��ƨ����W�٪����Ǽg�Jtable����
      while (rs.next()){
        if (rs.getString("version") == null){
          tabExtend.addCell(new Phrase(new Chunk(" ", F)));
        } else {
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("version"), F)));
        }
        if (rs.getString("approve_no") != null)
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("approve_no"),F)));
        else
          tabExtend.addCell(new Phrase(new Chunk(" ",F)));
        if (rs.getString("aDate") != null)
            tabExtend.addCell(new Phrase(new Chunk(rs.getString("aDate"),F)));
          else
            tabExtend.addCell(new Phrase(new Chunk(" ",F)));
/*
        if (rs.getString("approve_no") == null && rs.getString("aDate") != null){
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("aDate"),F)));
        } else if (rs.getString("approve_no") != null && rs.getString("aDate") == null){
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("approve_no"),F)));
        } else if (rs.getString("approve_no") == null && rs.getString("aDate") == null){
          tabExtend.addCell(new Phrase(new Chunk(" ",F)));
        } else {
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("approve_no") +" & " +rs.getString("aDate"),
                                                 F)));
        }
*/
        if (rs.getString("applicant") == null){
          tabExtend.addCell(new Phrase(new Chunk(" ",F)));
        } else {
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("applicant"),F)));
        }
        if (rs.getString("remark") == null){
          tabExtend.addCell(new Phrase(new Chunk(" ",F)));
        } else {
          tabExtend.addCell(new Phrase(new Chunk(rs.getString("remark"), F)));
        }
      }
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    return tabExtend;
  }

  /**
   * <p>Title:</p>
   *
   * <p>Description:Get all the mask_option in the tf_test_parameter_ws table filtered by vendor name </p>
   *
   */

  /*
  public static PdfPTable tf_route_master_WSFT(PdfPTable tabExtend,
                                               Font BasicFont,
                                               Font SmallFont,
                                               String sid,
                                               String table,
                                               String route_type,
                                               Connection conn) {

    try {
      StringBuffer sql2 = new StringBuffer();
      sql2.append("select '"+route_type+"' type, s1.route_name, ");
      sql2.append("s1.step_name||tf_get_step(s1.ttime,s1.temperature) step1,");
      sql2.append("s2.step_name||tf_get_step(s2.ttime,s2.temperature) step2,");
      sql2.append("s3.step_name||tf_get_step(s3.ttime,s3.temperature) step3,");
      sql2.append("s4.step_name||tf_get_step(s4.ttime,s4.temperature) step4,");
      sql2.append("s5.step_name||tf_get_step(s5.ttime,s5.temperature) step5,");
      sql2.append("s6.step_name||tf_get_step(s6.ttime,s6.temperature) step6,");
      sql2.append("s7.step_name||tf_get_step(s7.ttime,s7.temperature) step7,");
      sql2.append("s8.step_name||tf_get_step(s8.ttime,s8.temperature) step8,");
      sql2.append("s9.step_name||tf_get_step(s9.ttime,s9.temperature) step9,");
      sql2.append("s10.step_name||tf_get_step(s10.ttime,s10.temperature) step10,");
      sql2.append("s1.remark ");
      sql2.append(" from");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 1 and a.route_name = b.route_name) s1,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 2 and a.route_name = b.route_name) s2,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 3 and a.route_name = b.route_name) s3,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 4 and a.route_name = b.route_name) s4,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 5 and a.route_name = b.route_name) s5,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 6 and a.route_name = b.route_name) s6,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 7 and a.route_name = b.route_name) s7,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 8 and a.route_name = b.route_name) s8,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 9 and a.route_name = b.route_name) s9,");
      sql2.append(" (select a.route_name,a.step_name,b.remark,decode(a.test_time,null,'',trim(to_char(a.test_time)))||nvl(a.time_unit,'') ttime,a.temperature");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where a.sid = "+sid+" and a.step_seq = 10 and a.route_name = b.route_name) s10");
      sql2.append(" from tf_product_route"+table+" a ,TF_ROUTE_MASTER b");
      sql2.append(" where s2.route_name (+)= s1.route_name");
      sql2.append(" and s3.route_name(+) = s1.route_name");
      sql2.append(" and s4.route_name (+)= s1.route_name");
      sql2.append(" and s5.route_name (+)= s1.route_name");
      sql2.append(" and s6.route_name (+)= s1.route_name");
      sql2.append(" and s7.route_name (+)= s1.route_name");
      sql2.append(" and s8.route_name (+)= s1.route_name");
      sql2.append(" and s9.route_name (+)= s1.route_name");
      sql2.append(" and s10.route_name (+)= s1.route_name");
      if (route_type.equals("W"))
        sql2.append(" and substr(s1.route_name,2,1) = '"+route_type+"'");
      else
        sql2.append(" and substr(s1.route_name,2,1) in ('P')");//,'Q'

      TDSLogger.println(sql2.toString());
      PreparedStatement ps2 = conn.prepareStatement(sql2.toString());
      ResultSet rs2=ps2.executeQuery();

      while (rs2.next()) {
        TFRouteMasterBean bean = new TFRouteMasterBean();
        bean.setType(rs2.getString("type"));
        bean.setRoute_name(rs2.getString("route_name"));
        if ((rs2.getString("step1") == null) ||
            ((rs2.getString("step1").equals("")))) {
          bean.setStep1("NA");
        } else {
          bean.setStep1(rs2.getString("step1"));
        }
        if ((rs2.getString("step2") == null) ||
            ((rs2.getString("step2").equals("")))) {
          bean.setStep2("NA");
        } else {
          bean.setStep2(rs2.getString("step2"));
        }
        if ((rs2.getString("step3") == null) ||
            ((rs2.getString("step3").equals("")))) {
          bean.setStep3("NA");
        } else {
          bean.setStep3(rs2.getString("step3"));
        }
        if ((rs2.getString("step4") == null) ||
            ((rs2.getString("step4").equals("")))) {
          bean.setStep4("NA");
        } else {
          bean.setStep4(rs2.getString("step4"));
        }
        if ((rs2.getString("step5") == null) ||
            ((rs2.getString("step5").equals("")))) {
          bean.setStep5("NA");
        } else {
          bean.setStep5(rs2.getString("step5"));
        }
        if ((rs2.getString("step6") == null) ||
            ((rs2.getString("step6").equals("")))) {
          bean.setStep6("NA");
        } else {
          bean.setStep6(rs2.getString("step6"));
        }
        if ((rs2.getString("step7") == null) ||
            ((rs2.getString("step7").equals("")))) {
          bean.setStep7("NA");
        } else {
          bean.setStep7(rs2.getString("step7"));
        }
        if ((rs2.getString("step8") == null) ||
            ((rs2.getString("step8").equals("")))) {
          bean.setStep8("NA");
        } else {
          bean.setStep8(rs2.getString("step8"));
        }
        if ((rs2.getString("step9") == null) ||
            ((rs2.getString("step9").equals("")))) {
          bean.setStep9("NA");
        } else {
          bean.setStep9(rs2.getString("step9"));
        }
        if ((rs2.getString("step10") == null) ||
            ((rs2.getString("step10").equals("")))) {
          bean.setStep10("NA");
        } else {
          bean.setStep10(rs2.getString("step10"));
        }
        if ((rs2.getString("step11") == null) ||
                ((rs2.getString("step11").equals("")))) {
              bean.setStep10("NA");
            } else {
              bean.setStep10(rs2.getString("step11"));
            }
        if ((rs2.getString("step12") == null) ||
                ((rs2.getString("step12").equals("")))) {
              bean.setStep10("NA");
            } else {
              bean.setStep10(rs2.getString("step12"));
            }
        if ((rs2.getString("step13") == null) ||
                ((rs2.getString("step13").equals("")))) {
              bean.setStep10("NA");
            } else {
              bean.setStep10(rs2.getString("step13"));
            }
        if ((rs2.getString("step14") == null) ||
                ((rs2.getString("step14").equals("")))) {
              bean.setStep10("NA");
            } else {
              bean.setStep10(rs2.getString("step14"));
            }
        if ((rs2.getString("step15") == null) ||
                ((rs2.getString("step15").equals("")))) {
              bean.setStep10("NA");
            } else {
              bean.setStep10(rs2.getString("step15"));
            }
        if ((rs2.getString("remark") == null) ||
            ((rs2.getString("remark").equals("")))) {
          bean.setRemark("NA");
        } else {
          bean.setRemark(rs2.getString("remark"));
        }

        tabExtend.addCell(new Phrase(new Chunk(bean.getRoute_name(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep1(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep2(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep3(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep4(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep5(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep6(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep7(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep8(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep9(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getStep10(), SmallFont)));
        tabExtend.addCell(new Phrase(new Chunk(bean.getRemark(), SmallFont)));
      }
      rs2.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return null;
    }
    return tabExtend;
  }
  */

  // Write Product Group vs Route table
  // if vendor is empty, then output all information
  public static int WS_Product_Route(Document document,
                                         Font BasicFont,
                                         Font SmallFont,
                                         ProTestRouteBeanAF fm,
                                         String vendor,
                                         String table,
                                         Connection conn,
                                         String textFilename) {

    int result = 0;
    FileOutputStream os=null;
    PrintWriter pr=null;
//    Writer pr=null;

    try {
      PdfPTable table1 = null;
      Paragraph sec = null;
      String productType = OiMaintainService.getProductType(fm.getSid());

      String sql = "select distinct \n" +
          "'Main' route_type, 1 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add,\n" +
          "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,a.ws_comment, c.hw_configure, c.pgm_special_control  \n" +
          "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
          "where a.sid = ? \n" +
          "and trim(ws_route) is not null and ws_route != 'NA' \n" +
          "and a.ws_route = b.route_name \n" +
          "and a.sid = c.sid \n" +
          //"and c.brand = ' ' " +
          "and a.mask_option = c.mask_option \n" +
          "and b.test_mode = c.test_type \n" +
          "and a.tag != 2 \n";
      if (!vendor.equals(""))
        sql = sql + "and c.site = '" + vendor + "' \n";

      sql = sql + "union \n" +
          "select distinct \n" +
          "'Add1' route_type, 2 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add, \n" +
          "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,'' ws_comment, c.hw_configure, c.pgm_special_control \n" +
          "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
          "where a.sid = ? \n" +
          "and trim(ws_route_add) is not null and ws_route_add != 'NA' \n" +
          "and a.ws_route_add = b.route_name \n" +
          "and a.sid = c.sid \n" +
          //"and c.brand = ' ' " +
          "and a.mask_option = c.mask_option \n" +
          "and b.test_mode = c.test_type \n" +
          "and a.tag != 2 \n";
      if (!vendor.equals(""))
        sql = sql + "and c.site = '" + vendor + "' \n";

      sql = sql + "union \n" +
          "select distinct \n" +
          "'Add2' route_type, 3 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add1 ws_route_add, \n" +
          "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,'' ws_comment, c.hw_configure, c.pgm_special_control \n" +
          "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
          "where a.sid = ? \n" +
          "and trim(ws_route_add1) is not null and ws_route_add1 != 'NA' \n" +
          "and a.ws_route_add1 = b.route_name \n" +
          "and a.sid = c.sid \n" +
          //"and c.brand = ' ' " +
          "and a.mask_option = c.mask_option \n" +
          "and b.test_mode = c.test_type \n" +
          "and a.tag != 2 \n";
      if (!vendor.equals(""))
        sql = sql + "and c.site = '" + vendor + "' \n";
      
	      sql = sql + "union \n" +
	      "select distinct \n" +
	      "'Add3' route_type, 4 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add2 ws_route_add, \n" +
	      "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,'' ws_comment, c.hw_configure, c.pgm_special_control \n" +
	      "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
	      "where a.sid = ? \n" +
	      "and trim(ws_route_add2) is not null and ws_route_add2 != 'NA' \n" +
	      "and a.ws_route_add2 = b.route_name \n" +
	      "and a.sid = c.sid \n" +
	      //"and c.brand = ' ' " +
	      "and a.mask_option = c.mask_option \n" +
	      "and b.test_mode = c.test_type \n" +
	      "and a.tag != 2 \n";
	  if (!vendor.equals(""))
	    sql = sql + "and c.site = '" + vendor + "' \n";
	  
		  sql = sql + "union \n" +
	      "select distinct \n" +
	      "'Add4' route_type, 5 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add3 ws_route_add, \n" +
	      "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,'' ws_comment, c.hw_configure, c.pgm_special_control \n" +
	      "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
	      "where a.sid = ? \n" +
	      "and trim(ws_route_add3) is not null and ws_route_add3 != 'NA' \n" +
	      "and a.ws_route_add3 = b.route_name \n" +
	      "and a.sid = c.sid \n" +
	      //"and c.brand = ' ' " +
	      "and a.mask_option = c.mask_option \n" +
	      "and b.test_mode = c.test_type \n" +
	      "and a.tag != 2 \n";
	  if (!vendor.equals(""))
	    sql = sql + "and c.site = '" + vendor + "' \n";
	  
		  sql = sql + "union \n" +
	      "select distinct \n" +
	      "'Add5' route_type, 6 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add4 ws_route_add, \n" +
	      "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,'' ws_comment, c.hw_configure, c.pgm_special_control \n" +
	      "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c \n" +
	      "where a.sid = ? \n" +
	      "and trim(ws_route_add4) is not null and ws_route_add4 != 'NA' \n" +
	      "and a.ws_route_add4 = b.route_name \n" +
	      "and a.sid = c.sid \n" +
	      //"and c.brand = ' ' " +
	      "and a.mask_option = c.mask_option \n" +
	      "and b.test_mode = c.test_type \n" +
	      "and a.tag != 2 \n";
	  if (!vendor.equals(""))
	    sql = sql + "and c.site = '" + vendor + "' \n";

	// 20170516, add for subsitution route	
	      sql = sql + "union \n" +
		  "select distinct \n" +
		  "'Sub.' route_type, 7 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,e.map_route ws_route_add, \n" +
		  "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.temperature,c.tf_comment,'' ws_comment, c.hw_configure, c.pgm_special_control \n" +
		  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_ws"+table+" c, tf_main_route_xrom"+table+" e \n" +
		  "where a.sid = ? \n" +
		  "and trim(e.map_route) is not null and e.map_route != 'NA' \n" +
		  "and e.map_route = b.route_name \n" +
		  "and a.sid = c.sid \n" +
		  "and a.sid = e.sid \n" +
		  //"and c.brand = ' ' " +
		  "and a.mask_option = c.mask_option \n" +
		  "and b.test_mode = c.test_type \n" +
		  "and e.route_type = 0 \n" +
		  "and a.ws_route = e.main_route \n" +
		  "and a.tag != 2 \n";
	 if (!vendor.equals(""))
		    sql = sql + "and c.site = '" + vendor + "' \n";

      sql = sql + "union select distinct \n" +
      	  "'Main' route_type, 1 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add,\n" +
      	  "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.tf_comment,a.ws_comment, c.hw_configure, '' pgm_special_control  \n" +
      	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
      	  "where a.sid = ? \n" +
      	  "and trim(ws_route) is not null and ws_route != 'NA' \n" +
      	  "and a.ws_route = b.route_name \n" +
      	  "and a.sid = c.sid \n" +
      	  "and a.mask_option = c.backend_option \n" +
      	  "and b.test_mode = c.test_type \n" +
      	  "and a.tag != 2 \n";
      if (!vendor.equals(""))
    	  sql = sql + "and c.site = '" + vendor + "' \n";

      sql = sql + "union \n" +
          "select distinct \n" +
          "'Add1' route_type, 2 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add, \n" +
          "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.tf_comment,'' ws_comment, c.hw_configure, '' pgm_special_control \n" +
          "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
          "where a.sid = ? \n" +
          "and trim(ws_route_add) is not null and ws_route_add != 'NA' \n" +
          "and a.ws_route_add = b.route_name \n" +
          "and a.sid = c.sid \n" +
          "and a.mask_option = c.backend_option \n" +
          "and b.test_mode = c.test_type \n" +
          "and a.tag != 2 \n";
      if (!vendor.equals(""))
    	  sql = sql + "and c.site = '" + vendor + "' \n";

      sql = sql + "union \n" +
      	  "select distinct \n" +
      	  "'Add2' route_type, 3 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add1 ws_route_add, \n" +
      	  "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.tf_comment,'' ws_comment, c.hw_configure, '' pgm_special_control \n" +
      	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
      	  "where a.sid = ? \n" +
      	  "and trim(ws_route_add1) is not null and ws_route_add1 != 'NA' \n" +
      	  "and a.ws_route_add1 = b.route_name \n" +
      	  "and a.sid = c.sid \n" +
      	  "and a.mask_option = c.backend_option \n" +
      	  "and b.test_mode = c.test_type \n" +
      	  "and a.tag != 2 \n";
      if (!vendor.equals(""))
    	  sql = sql + "and c.site = '" + vendor + "' \n";
      
	      sql = sql + "union \n" +
	  	  "select distinct \n" +
	  	  "'Add3' route_type, 4 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add2 ws_route_add, \n" +
	  	  "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.tf_comment,'' ws_comment, c.hw_configure, '' pgm_special_control \n" +
	  	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
	  	  "where a.sid = ? \n" +
	  	  "and trim(ws_route_add2) is not null and ws_route_add2 != 'NA' \n" +
	  	  "and a.ws_route_add2 = b.route_name \n" +
	  	  "and a.sid = c.sid \n" +
	  	  "and a.mask_option = c.backend_option \n" +
	  	  "and b.test_mode = c.test_type \n" +
	  	  "and a.tag != 2 \n";
	  if (!vendor.equals(""))
		  sql = sql + "and c.site = '" + vendor + "' \n";
	  
		  sql = sql + "union \n" +
	  	  "select distinct \n" +
	  	  "'Add4' route_type, 5 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add3 ws_route_add, \n" +
	  	  "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.tf_comment,'' ws_comment, c.hw_configure, '' pgm_special_control \n" +
	  	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
	  	  "where a.sid = ? \n" +
	  	  "and trim(ws_route_add3) is not null and ws_route_add3 != 'NA' \n" +
	  	  "and a.ws_route_add3 = b.route_name \n" +
	  	  "and a.sid = c.sid \n" +
	  	  "and a.mask_option = c.backend_option \n" +
	  	  "and b.test_mode = c.test_type \n" +
	  	  "and a.tag != 2 \n";
	  if (!vendor.equals(""))
		  sql = sql + "and c.site = '" + vendor + "' \n";
	  
		  sql = sql + "union \n" +
	  	  "select distinct \n" +
	  	  "'Add5' route_type, 6 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,a.ws_route_add4 ws_route_add, \n" +
	  	  "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.tf_comment,'' ws_comment, c.hw_configure, '' pgm_special_control \n" +
	  	  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c \n" +
	  	  "where a.sid = ? \n" +
	  	  "and trim(ws_route_add4) is not null and ws_route_add4 != 'NA' \n" +
	  	  "and a.ws_route_add4 = b.route_name \n" +
	  	  "and a.sid = c.sid \n" +
	  	  "and a.mask_option = c.backend_option \n" +
	  	  "and b.test_mode = c.test_type \n" +
	  	  "and a.tag != 2 \n";
	  if (!vendor.equals(""))
		  sql = sql + "and c.site = '" + vendor + "' \n";

	  // 20170516, add for subsitution route	  
	  	  sql = sql + "union \n" +
		  "select distinct \n" +
		  "'Sub.' route_type, 7 route_type_seq,a.product_body,a.body_version,a.mask_option,a.mask_option_rev,a.code_no,a.ws_route,e.map_route ws_route_add, \n" +
		  "b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.tf_comment,'' ws_comment, c.hw_configure, '' pgm_special_control \n" +
		  "from tf_bom_route_xrom"+table+" a, tf_route_master_ws_view b, tf_test_parameter_pbc"+table+" c, tf_main_route_xrom"+table+" e \n" +
		  "where a.sid = ? \n" +
		  "and trim(e.map_route) is not null and e.map_route != 'NA' \n" +
		  "and e.map_route = b.route_name \n" +
		  "and a.sid = c.sid \n" +
		  "and a.sid = e.sid \n" +
		  //"and c.brand = ' ' " +
		  "and a.mask_option = c.backend_option \n" +
		  "and b.test_mode = c.test_type \n" +
		  "and e.route_type = 0 \n" +
		  "and a.ws_route = e.main_route \n" +
		  "and a.tag != 2 \n";
	  if (!vendor.equals(""))
			    sql = sql + "and c.site = '" + vendor + "' \n";
	  

	  sql = sql + "order by product_body,body_version,mask_option,mask_option_rev,ws_route,route_type_seq,ws_route_add,test_mode \n";

      TDSLogger.println(sql.toString());
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,String.valueOf(fm.getSid()));
      ps.setString(2,String.valueOf(fm.getSid()));
      ps.setString(3,String.valueOf(fm.getSid()));
      ps.setString(4,String.valueOf(fm.getSid()));
      ps.setString(5,String.valueOf(fm.getSid()));
      ps.setString(6,String.valueOf(fm.getSid()));
      ps.setString(7,String.valueOf(fm.getSid()));
      ps.setString(8,String.valueOf(fm.getSid()));
      ps.setString(9,String.valueOf(fm.getSid()));
      ps.setString(10,String.valueOf(fm.getSid()));
      ps.setString(11,String.valueOf(fm.getSid()));
      ps.setString(12,String.valueOf(fm.getSid()));
      ps.setString(13,String.valueOf(fm.getSid()));
      ps.setString(14,String.valueOf(fm.getSid()));
      ResultSet rs = ps.executeQuery();

      String preGroupKey = "";
      String curGroupKey = null;
      int section = 0;
      String maxSite = "";
      // 20090831 for e8049 data share project, Robin
      StringBuffer e8049share = new StringBuffer("");
      String heading = "8049-"+fm.getProductbody()+"."+
      					OiMaintainService.getVendorShortName(fm.getVendor())+"|"+
      					fm.getVersion()+"|"+"WS|"+fm.getBrand()+"|";
      // released OI, Subcons PDF �~���� text file
      if (table.equals("") && !vendor.equals("") && !fm.getVendor().equals("TEST1")) {
    	  os = new FileOutputStream(textFilename+"-PGM.txt");
    	  pr = new PrintWriter(os);
    	  //pr = new OutputStreamWriter(os, "Big5");
    	  //pr = new OutputStreamWriter(os);
      }
//    select ecrtime for e8049 dat share project, Sophia
      String ecrtime[] = OiMaintainService.getEcrEffectTime(fm.getProductbody(),fm.getVersion(),fm.getBrand());
      
      while(rs.next()){
        if (result == 0) document.add(new Paragraph(new Chunk("1-1. WS ROUTE", BasicFont)));
        result++;
    	  curGroupKey = OiMaintainService.getGroupKey(productType, rs.getString("product_body"),
              rs.getString("body_version"), rs.getString("mask_option"), rs.getString("mask_option_rev"),
    	      "", "", rs.getString("code_no"), rs.getString("ws_route"), 0);

        if (!curGroupKey.equals(preGroupKey)) {
          if (section > 0) {
            document.add(table1);
          }
          section ++;
          sec = null;
          sec = new Paragraph(new Chunk("1-1-"+section+". Product Group Key - "+curGroupKey, BasicFont));
          sec.setSpacingAfter(5);
          document.add(sec);
          table1 = null;
          float[] widths = {5,5,5,10,10,5,9,8,8,15,18,17};//23-->19,24-->20,add 8
          table1 = new PdfPTable(widths);
          table1.setWidthPercentage(100);
          table1.setSpacingBefore(0);
          table1.addCell(new Phrase(new Chunk("Route Type",SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route",SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Mode",SmallFont)));
          table1.addCell(new Phrase(new Chunk("Tester",SmallFont)));
          table1.addCell(new Phrase(new Chunk("Site",SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM ID",SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Name",SmallFont)));
          table1.addCell(new Phrase(new Chunk("Temperature",SmallFont)));
          table1.addCell(new Phrase(new Chunk("HW Configure",SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Special Control",SmallFont)));  //10
          table1.addCell(new Phrase(new Chunk("PGM Notes",SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route Comment",SmallFont)));
          
          preGroupKey = curGroupKey;
        }

        table1.addCell(new Phrase(new Chunk(rs.getString("route_type"),SmallFont)));
        if (rs.getString("route_type").equals("Main")) {
        	table1.addCell(new Phrase(new Chunk(rs.getString("ws_route"),SmallFont)));
        } else {
        	table1.addCell(new Phrase(new Chunk(rs.getString("ws_route_add"),SmallFont)));
        }
        table1.addCell(new Phrase(new Chunk(rs.getString("test_mode"),SmallFont)));

        String temperature = " ";
        if (rs.getString("temperature") != null){
        	temperature = rs.getString("temperature");
        	if (!temperature.equals("ROOM TEMP") && !temperature.equals(""))
        		temperature = temperature + "�J";
        }

        table1.addCell(new Phrase(new Chunk(rs.getString("tester"),SmallFont)));
        table1.addCell(new Phrase(new Chunk(rs.getString("site"),SmallFont)));
        table1.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),SmallFont)));
        table1.addCell(new Phrase(new Chunk(rs.getString("program_name"),SmallFont)));
        table1.addCell(new Phrase(new Chunk(temperature,SmallFont)));
        //      HW Configure
        String hw_configure_split = PDFdiffService.getStringDataSplit(rs.getString("hw_configure"));
        table1.addCell(new Phrase(hw_configure_split, SmallFont));
        if (rs.getString("pgm_special_control") != null){
          table1.addCell(new Phrase(rs.getString("pgm_special_control"), SmallFont));
        } else {
          table1.addCell(new Phrase(" ", SmallFont));
        }
        if (rs.getString("tf_comment") != null){
          table1.addCell(new Phrase(rs.getString("tf_comment"), SmallFont));
        } else {
          table1.addCell(new Phrase(" ", SmallFont));
        }
        if (rs.getString("ws_comment") != null){
          table1.addCell(new Phrase(rs.getString("ws_comment"), SmallFont));
        } else {
          table1.addCell(new Phrase(" ", SmallFont));
        }
        maxSite = "";
        maxSite = com.mxic.oiplus.oimaintain.OiMaintainService.getMaxsite(conn, "PROD", rs.getString("pgm_id"));


        // 20090831, output e8049share string to file
        if (pr != null) { 
        	e8049share.append(heading+curGroupKey+"|");
            e8049share.append(rs.getString("route_type")+"|");
            if (rs.getString("route_type").equals("Main")) 
            	e8049share.append(rs.getString("ws_route")+"|");
            else
            	e8049share.append(rs.getString("ws_route_add")+"|");
            e8049share.append(rs.getString("test_mode")+"||||||");
            e8049share.append(temperature+"||");
            e8049share.append(rs.getString("tester")+"|");
            e8049share.append(rs.getString("site")+"|");
            e8049share.append(rs.getString("program_name")+"||");
            if (rs.getString("tf_comment") != null)
              e8049share.append(rs.getString("tf_comment")+"|");
            else
              e8049share.append("|");
            if (rs.getString("ws_comment") != null) {
                e8049share.append(rs.getString("ws_comment"));
            }
            e8049share.append("|");
            e8049share.append("|||||");//AEB GRADE(S/P/Q/R/T)
            e8049share.append(ecrtime[0]+"|");
    		e8049share.append(ecrtime[1]+"|");
    		e8049share.append("NA�J|");//W
    		e8049share.append("NA�J|");//Y
			e8049share.append("NA�J|");//J
			e8049share.append("NA�J|");//K
			e8049share.append("NA�J|");//L
			e8049share.append("NA�J|");//N
			e8049share.append("NA�J|");//B
			e8049share.append("NA�J|");//E
			e8049share.append("NA�J|");//AEB GRADE (U)
			e8049share.append(maxSite+"|");//MAX_SITE
			if (rs.getString("pgm_special_control") != null)
        		e8049share.append(rs.getString("pgm_special_control")+"|");
        	else 
        		e8049share.append("|");

        	pr.println(e8049share.toString());
        	e8049share.delete(0, e8049share.length());
        }
      }
/* testing only
      { 
    	     Map map = java.nio.charset.Charset.availableCharsets();
    	     Iterator it = map.keySet().iterator();
    	         while (it.hasNext()) {
    	         // Get charset name
    	         String charsetName = (String)it.next();
    	         e8049share.append(charsetName+"\t");
    	        
    	         // Get charset
    	         java.nio.charset.Charset charset = java.nio.charset.Charset.forName(charsetName);
    	         e8049share.append(charset+"\n");
    	     }
  	        	pr.write(e8049share.toString());
      }
      
 */      
      if (table1 != null)
    	  document.add(table1);
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return -1;
    } finally {
    	try{
            if(pr!=null)
              pr.close();
            if(os!=null)
              os.close();
            pr=null;
            os=null;
          }catch(Exception ex){
            TDSLogger.println(ex);
          }
    }
    return result;
  }

  public static int FT_Product_Route(Document document,
                                         Font BasicFont,
                                         Font SmallFont,
                                         String section,
                                         ProTestRouteBeanAF fm,
                                         String vendor,
                                         String table,
                                         Connection conn,
                                         String textFilename) {
    int result = 0;
    FileOutputStream os=null;
    PrintWriter pr=null;

    try {
		PdfPTable table1 = null;
    	Paragraph sec = null;
    	String productType = OiMaintainService.getProductType(fm.getSid());

    	StringBuffer sql = new StringBuffer();

    	sql.append("select distinct \n");
    	sql.append("'Main' route_type,1 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,a.ft_comment,c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route) is not null and ft_route != 'NA' \n");
    	sql.append("and a.ft_route = b.route_name \n");
    	sql.append("and a.sid = c.sid and a.sid = e.sid \n");
        //sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");

    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add1' route_type,2 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment, '' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' \n");
    	sql.append("and a.ft_route_add = b.route_name \n");
    	sql.append("and a.sid = c.sid and a.sid = e.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add2' route_type,3 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add1 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add1) is not null and ft_route_add1 != 'NA' \n");
    	sql.append("and a.ft_route_add1 = b.route_name \n");
    	sql.append("and a.sid = c.sid and a.sid = e.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
        sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add1) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add3' route_type,4 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add2 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' \n");
    	sql.append("and a.ft_route_add2 = b.route_name \n");
    	sql.append("and a.sid = c.sid and a.sid = e.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add2) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add4' route_type,5 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add3 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' \n");
    	sql.append("and a.ft_route_add3 = b.route_name \n");
    	sql.append("and a.sid = c.sid and a.sid = e.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add3) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add5' route_type,6 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add4 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add4) is not null and ft_route_add4 != 'NA' \n");
    	sql.append("and a.ft_route_add4 = b.route_name \n");
    	sql.append("and a.sid = c.sid and a.sid = e.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add4) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add6' route_type,7 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add5 ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add5) is not null and ft_route_add5 != 'NA' \n");
    	sql.append("and a.ft_route_add5 = b.route_name \n");
    	sql.append("and a.sid = c.sid and a.sid = e.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add5) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Sub.' route_type,8 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,f.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e, tf_product_route"+table+" f \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.sid = f.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(f.step_name, 5) and f.qc_actual_mode = c.test_type and f.route_name = e.map_route) OR (b.test_mode = c.test_type and f.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 0 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Rwk.' route_type,9 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,f.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e, tf_product_route"+table+" f \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.sid = f.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and ((b.test_mode = 'QT' || substr(f.step_name, 5) and f.qc_actual_mode = c.test_type and f.route_name = e.map_route) OR (b.test_mode = c.test_type and f.step_name = c.test_type)) \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 1 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");

    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Main' route_type,1 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,a.ft_comment ft_comment,c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route) is not null and ft_route != 'NA' \n");
    	sql.append("and a.ft_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");

    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add1' route_type,2 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' \n");
    	sql.append("and a.ft_route_add = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add2' route_type,3 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add1 ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment, '' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add1) is not null and ft_route_add1 != 'NA' \n");
    	sql.append("and a.ft_route_add1 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add3' route_type,4 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add2 ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add2) is not null and ft_route_add2 != 'NA' \n");
    	sql.append("and a.ft_route_add2 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add4' route_type,5 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add3 ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add3) is not null and ft_route_add3 != 'NA' \n");
    	sql.append("and a.ft_route_add3 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add5' route_type,6 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add4 ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add4) is not null and ft_route_add4 != 'NA' \n");
    	sql.append("and a.ft_route_add4 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Add6' route_type,7 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,a.ft_route_add5 ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(ft_route_add5) is not null and ft_route_add5 != 'NA' \n");
    	sql.append("and a.ft_route_add5 = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	//sql.append("and c.brand = ' ' ");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");

    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Sub.' route_type,8 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 0 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");

    	sql.append("union \n");
    	sql.append("select distinct \n");
    	sql.append("'Rwk.' route_type,9 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
    	sql.append("a.ft_route,e.map_route ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
    	sql.append("from tf_bom_route_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e \n");
    	sql.append("where a.sid = ? \n");
    	sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
    	sql.append("and e.map_route = b.route_name \n");
    	sql.append("and a.sid = c.sid \n");
    	sql.append("and a.sid = e.sid \n");
    	sql.append("and a.mask_option = c.backend_option \n");
    	sql.append("and b.test_mode = c.test_type \n");
    	sql.append("and a.pin_count = c.pin_count \n");
    	sql.append("and a.package_code = d.prm2_code \n");
    	sql.append("and d.package_type = c.package_type \n");
    	sql.append("and a.tag != 2 \n");
    	sql.append("and e.route_type = 1 \n");
    	sql.append("and a.ft_route = e.main_route \n");
    	if (!vendor.equals(""))
    		sql.append("and c.site = ? \n");
    	
    	sql.append("order by product_body,body_version,backend_option,mask_option_rev,package_code,pin_count,ft_route,route_type_seq,ft_route_add,test_mode \n");
    	TDSLogger.println(sql.toString());
    	PreparedStatement ps = conn.prepareStatement(sql.toString());

    	int i=1;
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	ps.setString(i++,String.valueOf(fm.getSid()));
    	if (!vendor.equals("")) ps.setString(i++,vendor);
    	
    	ResultSet rs = ps.executeQuery();
    	
    	String preGroupKey = "";
    	String curGroupKey = null;
    	String temperature = null;
    	String hw_configure_split = null;
    	int subsection = 0;
    	String maxSite = "";
        // 20090831 for e8049 data share project, Robin
        StringBuffer e8049share = new StringBuffer("");
        String heading = "8049-"+fm.getProductbody()+"."+
        					OiMaintainService.getVendorShortName(fm.getVendor())+"|"+
        					fm.getVersion()+"|"+"FT|"+fm.getBrand()+"|";
        // released OI, Subcons PDF �~���� text file
        if (table.equals("") && !vendor.equals("") && !fm.getVendor().equals("TEST1")) {
      	  os = new FileOutputStream(textFilename+"-PGM.txt", true);
      	  pr = new PrintWriter(os);
        }
//      select ecrtime for e8049 dat share project, Sophia
        String ecrtime[] = OiMaintainService.getEcrEffectTime(fm.getProductbody(),fm.getVersion(),fm.getBrand());
        
    	while(rs.next()){
    		if (result == 0) document.add(new Paragraph(new Chunk("1-"+section+". FT ROUTE", BasicFont)));
    		result ++;
    		curGroupKey = OiMaintainService.getGroupKey(productType, rs.getString("product_body"),
    				rs.getString("body_version"), rs.getString("mask_option"), rs.getString("mask_option_rev"),
    				rs.getString("package_code"), rs.getString("pin_count"), rs.getString("code_no"),
    				rs.getString("ft_route"), 1);
    		if (!curGroupKey.equals(preGroupKey)) {
    			if (subsection > 0) {
    				document.add(table1);
    			}
    			subsection ++;
    			sec = null;
    			sec = new Paragraph(new Chunk("1-"+section+"-"+subsection+". Product Group Key - "+curGroupKey, BasicFont));
    			sec.setSpacingAfter(5);
    			document.add(sec);
    			table1 = null;
    			float[] widths = {5,4,4,4,4,4,4,5,7,10,7,4,8,8,7,7,7,7};//11-->9,11-->9,10-->8,10-->8,add 8
    			table1 = new PdfPTable(widths);
    			table1.setWidthPercentage(100);
    			table1.setSpacingBefore(0);
    			table1.addCell(new Phrase(new Chunk("Route Type",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Route",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Test Mode",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Pkg Code",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Pkg Name",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Pin Count",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Code No",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Temperature",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Body Size",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Tester",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Site",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("PGM ID",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Test Program Name",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Actual Program Name",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("PGM Special Control",SmallFont)));  //8
    			table1.addCell(new Phrase(new Chunk("HW Configure",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("PGM Notes",SmallFont)));
    			table1.addCell(new Phrase(new Chunk("Route Comment",SmallFont)));
    			
    			preGroupKey = curGroupKey;
    		}

    		table1.addCell(new Phrase(new Chunk(rs.getString("route_type"),SmallFont)));

        	if (rs.getString("route_type").equals("Main")) {
    			table1.addCell(new Phrase(new Chunk(rs.getString("ft_route"),SmallFont)));
        	} else {    			
    			table1.addCell(new Phrase(new Chunk(rs.getString("ft_route_add"),SmallFont)));
        	}
    		table1.addCell(new Phrase(new Chunk(rs.getString("test_mode"),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("package_code"),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("package_name"),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("pin_count"),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("code_no"),SmallFont)));
    		temperature = PDFdiffService.getTemperature(rs.getString("c_grade"));
    		table1.addCell(new Phrase(new Chunk(temperature,SmallFont)));
    		table1.addCell(new Phrase(new Chunk(PDFdiffService.NullConvert(rs.getString("body_size")),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("tester"),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("site"),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),SmallFont)));
    		table1.addCell(new Phrase(new Chunk(rs.getString("program_name"),SmallFont)));
    		if (rs.getString("actual_file") != null){
    			table1.addCell(new Phrase(new Chunk(rs.getString("actual_file"),SmallFont)));
    		} else {
    			table1.addCell(new Phrase(" ", SmallFont));
    		}
    		if (rs.getString("pgm_special_control") != null){
            	table1.addCell(new Phrase(new Chunk(rs.getString("pgm_special_control"),SmallFont)));
            } else {
                table1.addCell(new Phrase(" ", SmallFont));
            }
    		hw_configure_split = PDFdiffService.getStringDataSplit(rs.getString("hw_configure"));
            table1.addCell(new Phrase(new Chunk(hw_configure_split,SmallFont)));
    		if (rs.getString("tf_comment") != null){
    			table1.addCell(new Phrase(rs.getString("tf_comment"), SmallFont));
    		} else {
    			table1.addCell(new Phrase(" ", SmallFont));
    		}
    		
    		if (rs.getString("ft_comment") != null){
    			table1.addCell(new Phrase(rs.getString("ft_comment"), SmallFont));
    		} else {
    			table1.addCell(new Phrase(" ", SmallFont));
    		}
    		maxSite = "";
    	    maxSite = com.mxic.oiplus.oimaintain.OiMaintainService.getMaxsite(conn, "PROD", rs.getString("pgm_id"));
    		

    		// 20090831, output to text file
    		if (pr != null) { 
            	e8049share.append(heading+curGroupKey+"|");
            	e8049share.append(rs.getString("route_type")+"|");
            	if (rs.getString("route_type").equals("Main")) 
            		e8049share.append(rs.getString("ft_route")+"|");
            	else    			
                	e8049share.append(rs.getString("ft_route_add")+"|");
            	e8049share.append(rs.getString("test_mode")+"|");
            	e8049share.append(rs.getString("package_code")+"|");
            	e8049share.append(rs.getString("package_name")+"|");
            	e8049share.append(rs.getString("pin_count")+"|");
            	e8049share.append(rs.getString("code_no")+"|");
            	e8049share.append("|"+temperature+"|");
            	e8049share.append(PDFdiffService.NullConvert(rs.getString("body_size"))+"|");
            	e8049share.append(rs.getString("tester")+"|");
            	e8049share.append(rs.getString("site")+"|");
            	e8049share.append(rs.getString("program_name")+"|");
        		if (rs.getString("actual_file") != null)
              		e8049share.append(rs.getString("actual_file")+"|");
        		else
                	e8049share.append("|");
        		if (rs.getString("tf_comment") != null)
            		e8049share.append(rs.getString("tf_comment")+"|");
        		else 
                	e8049share.append("|");
        		if (rs.getString("ft_comment") != null)
            		e8049share.append(rs.getString("ft_comment"));
        		e8049share.append("|");
        		e8049share.append("|||||");//AEB GRADE(S/P/Q/R/T)
        		e8049share.append(ecrtime[0]+"|");
        		e8049share.append(ecrtime[1]+"|");
        		e8049share.append("NA�J|");//W
        		e8049share.append("NA�J|");//Y
				e8049share.append("NA�J|");//J
				e8049share.append("NA�J|");//K
				e8049share.append("NA�J|");//L
				e8049share.append("NA�J|");//N
				e8049share.append("NA�J|");//B
				e8049share.append("NA�J|");//E
				e8049share.append("NA�J|");//AEB GRADE (U)
				e8049share.append(maxSite+"|");//MAX_SITE
				if (rs.getString("pgm_special_control") != null)
	        		e8049share.append(rs.getString("pgm_special_control")+"|");
	        	else 
	        		e8049share.append("|");
            	pr.println(e8049share.toString());
            	e8049share.delete(0,e8049share.length());
            }
    	}
    	if (table1 != null)
    		document.add(table1);
    	ps.clearParameters();
    	ps.close();
    	rs.close();
    	} catch (Exception ex) {
    		ex.fillInStackTrace();
    		TDSLogger.println(ex.getMessage());
    		return -1;
    	} finally {
        	try{
                if(pr!=null)
                  pr.close();
                if(os!=null)
                  os.close();
                pr=null;
                os=null;
              }catch(Exception ex){
                TDSLogger.println(ex);
              }
    	}
    	return result;
  	}
  	
  public static int FT_Product_ReRoute(Document document,
                                           Font BasicFont,
                                           Font SmallFont,
                                           String section,
                                           String sid,
                                           String vendor,
                                           String table,
                                           Connection conn) {
      int result = 0;
      try {
        PdfPTable table1 = null;
        Paragraph sec = null;
        String productType = OiMaintainService.getProductType(sid);

        StringBuffer sql = new StringBuffer();

        sql.append("select distinct ");
        sql.append("'Main' route_type,1 route_type_seq, a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, ");
        sql.append("a.ft_route,a.ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,a.ft_comment,c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure  ");
        sql.append("from tf_bom_reroute_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
        sql.append("and a.ft_route = b.route_name ");
        sql.append("and a.sid = c.sid and a.sid = e.sid \n");
        //sql.append("and c.brand = ' ' ");
        sql.append("and a.mask_option = c.backend_option ");
        sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_code = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        if (!vendor.equals(""))
          sql.append("and c.site = '" + vendor + "' ");

        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add.' route_type,2 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, ");
        sql.append("a.ft_route,a.ft_route_add,e.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure ");
        sql.append("from tf_bom_reroute_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_product_route"+table+" e ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
        sql.append("and a.ft_route_add = b.route_name ");
        sql.append("and a.sid = c.sid and a.sid = e.sid \n");
        //sql.append("and c.brand = ' ' ");
        sql.append("and a.mask_option = c.backend_option ");
        sql.append("and ((b.test_mode = 'QT' || substr(e.step_name, 5) and e.qc_actual_mode = c.test_type and e.route_name = a.ft_route_add) OR (b.test_mode = c.test_type and e.step_name = c.test_type)) \n");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_code = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        if (!vendor.equals(""))
          sql.append("and c.site = '" + vendor + "' ");
// 20081029, add for subsitution route
        sql.append("union \n");
        sql.append("select distinct \n");
        sql.append("'Sub.' route_type,3 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
        sql.append("a.ft_route,e.map_route ft_route_add,f.step_name test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, c.pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
        sql.append("from tf_bom_reroute_xrom"+table+" a, tf_route_master_ft_view b, tf_test_parameter_ft"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e, tf_product_route"+table+" f ");
        sql.append("where a.sid = ? \n");
        sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
        sql.append("and e.map_route = b.route_name \n");
        sql.append("and a.sid = c.sid \n");
        sql.append("and a.sid = e.sid \n");
        sql.append("and a.sid = f.sid \n");
        sql.append("and a.mask_option = c.backend_option \n");
        sql.append("and ((b.test_mode = 'QT' || substr(f.step_name, 5) and f.qc_actual_mode = c.test_type and f.route_name = e.map_route) OR (b.test_mode = c.test_type and f.step_name = c.test_type)) \n");
        sql.append("and a.pin_count = c.pin_count \n");
        sql.append("and a.package_code = d.prm2_code \n");
        sql.append("and d.package_type = c.package_type \n");
        sql.append("and a.tag != 2 \n");
        sql.append("and e.route_type = 0 \n");
        sql.append("and a.ft_route = e.main_route \n");
        if (!vendor.equals(""))
          sql.append("and c.site = '" + vendor + "' \n");

        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Main' route_type,1 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,a.ft_comment,c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure ");
        sql.append("from tf_bom_reroute_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route) is not null and ft_route != 'NA' ");
        sql.append("and a.ft_route = b.route_name ");
        sql.append("and a.sid = c.sid ");
        //sql.append("and c.brand = ' ' ");
        sql.append("and a.mask_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_code = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        if (!vendor.equals(""))
          sql.append("and c.site = '" + vendor + "' ");

        sql.append("union ");
        sql.append("select distinct ");
        sql.append("'Add.' route_type,2 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, ");
        sql.append("a.ft_route,a.ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure ");
        sql.append("from tf_bom_reroute_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d ");
        sql.append("where a.sid = ? ");
        sql.append("and trim(ft_route_add) is not null and ft_route_add != 'NA' ");
        sql.append("and a.ft_route_add = b.route_name ");
        sql.append("and a.sid = c.sid ");
        //sql.append("and c.brand = ' ' ");
        sql.append("and a.mask_option = c.backend_option ");
        sql.append("and b.test_mode = c.test_type ");
        sql.append("and a.pin_count = c.pin_count ");
        sql.append("and a.package_code = d.prm2_code ");
        sql.append("and d.package_type = c.package_type ");
        sql.append("and a.tag != 2 ");
        if (!vendor.equals(""))
          sql.append("and c.site = '" + vendor + "' ");
  // 20081029, add for subsitution route
        sql.append("union \n");
        sql.append("select distinct \n");
        sql.append("'Sub.' route_type,3 route_type_seq,a.product_body,a.package_code,c.package_type package_name,a.pin_count,c.backend_option,a.body_version,a.code_no, \n");
        sql.append("a.ft_route,e.map_route ft_route_add,b.test_mode,c.tester,c.site,c.pgm_id,c.program_name,c.i_grade,c.c_grade,c.body_size,c.tf_comment,'' ft_comment, c.actual_file, '' pgm_special_control, a.mask_option, a.mask_option_rev, a.route_type route_type_x, c.hw_configure \n");
        sql.append("from tf_bom_reroute_xrom"+table+" a, tf_route_master_pbc_view b, tf_test_parameter_pbc"+table+" c, ba_package_type d, tf_main_route_xrom"+table+" e \n");
        sql.append("where a.sid = ? \n");
        sql.append("and trim(e.map_route) is not null and e.map_route != 'NA' \n");
        sql.append("and e.map_route = b.route_name \n");
        sql.append("and a.sid = c.sid \n");
        sql.append("and a.sid = e.sid \n");
        sql.append("and a.mask_option = c.backend_option \n");
        sql.append("and b.test_mode = c.test_type \n");
        sql.append("and a.pin_count = c.pin_count \n");
        sql.append("and a.package_code = d.prm2_code \n");
        sql.append("and d.package_type = c.package_type \n");
        sql.append("and a.tag != 2 \n");
        sql.append("and e.route_type = 0 \n");
        sql.append("and a.ft_route = e.main_route \n");
        if (!vendor.equals(""))
          sql.append("and c.site = '" + vendor + "' \n");

        sql.append("order by product_body,body_version,backend_option,package_code,pin_count,ft_route,route_type_seq,ft_route_add,test_mode ");
        TDSLogger.println(sql.toString());
        PreparedStatement ps = conn.prepareStatement(sql.toString());
        ps.setString(1,String.valueOf(sid));
        ps.setString(2,String.valueOf(sid));
        ps.setString(3,String.valueOf(sid));
        ps.setString(4,String.valueOf(sid));
        ps.setString(5,String.valueOf(sid));
        ps.setString(6,String.valueOf(sid));
        ResultSet rs = ps.executeQuery();

        String preGroupKey = "";
        String curGroupKey = null;
        String temperature = null;
        String hw_configure_split = null;
        boolean first = true;
        // ���ݭn group key (20071228, Hero said)
        while(rs.next()){
          if (result == 0) document.add(new Paragraph(new Chunk("1-"+section+". FT Recycle ROUTE", BasicFont)));
          result ++;
/*
              curGroupKey = OiMaintainService.getGroupKey(productType, rs.getString("product_body"),
                  rs.getString("body_version"), rs.getString("mask_option"), rs.getString("mask_option_rev"),
                  rs.getString("package_code"), rs.getString("pin_count"), rs.getString("code_no"),
                  rs.getString("ft_route"), 1);
          if (!curGroupKey.equals(preGroupKey)) {
            if (subsection > 0) {
              document.add(table1);
            }
            subsection ++;
            sec = null;
            sec = new Paragraph(new Chunk("1-"+section+"-"+subsection+". Product Group Key - "+curGroupKey, BasicFont));
            sec.setSpacingAfter(5);
            document.add(sec);
*/
            if (first) {
            table1 = null;
            float[] widths = {4,5,4,4,4,4,4,5,7,9,7,9,9,8,8,8,8};//11-->9,11-->9,10-->8,10-->8,add 8
            table1 = new PdfPTable(widths);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(5);
            table1.addCell(new Phrase(new Chunk("Route Type",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Route",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Test Mode",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Pkg Code",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Pkg Name",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Pin Count",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Code No",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Temperature",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Body Size",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Tester",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Site",SmallFont)));
            table1.addCell(new Phrase(new Chunk("PGM ID",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Test Program Name",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Actual Program Name",SmallFont)));
            table1.addCell(new Phrase(new Chunk("PGM Special Control",SmallFont)));  //8
            table1.addCell(new Phrase(new Chunk("HW Configure",SmallFont)));
            table1.addCell(new Phrase(new Chunk("PGM Notes",SmallFont)));
            table1.addCell(new Phrase(new Chunk("Route Comment",SmallFont)));
            
//            preGroupKey = curGroupKey;
            first = false;
          }
          table1.addCell(new Phrase(new Chunk(rs.getString("route_type"),SmallFont)));
          if (rs.getString("route_type").equals("Main"))
                  table1.addCell(new Phrase(new Chunk(rs.getString("ft_route"),SmallFont)));
          else
                  table1.addCell(new Phrase(new Chunk(rs.getString("ft_route_add"),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("test_mode"),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("package_code"),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("package_name"),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("pin_count"),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("code_no"),SmallFont)));
                  temperature = PDFdiffService.getTemperature(rs.getString("c_grade"));
                  table1.addCell(new Phrase(new Chunk(temperature,SmallFont)));

          table1.addCell(new Phrase(new Chunk(PDFdiffService.NullConvert(rs.getString("body_size")),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("tester"),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("site"),SmallFont)));
          table1.addCell(new Phrase(new Chunk(rs.getString("program_name"),SmallFont)));
          if (rs.getString("actual_file") != null){
            table1.addCell(new Phrase(new Chunk(rs.getString("actual_file"),SmallFont)));
          } else {
              table1.addCell(new Phrase(" ", SmallFont));
          }
          if (rs.getString("pgm_special_control") != null) {
			table1.addCell(new Phrase(new Chunk(rs
					.getString("pgm_special_control"), SmallFont)));
          } else {
			table1.addCell(new Phrase(" ", SmallFont));
          }
          hw_configure_split = PDFdiffService.getStringDataSplit(rs.getString("hw_configure"));
          table1.addCell(new Phrase(new Chunk(hw_configure_split,SmallFont)));
          if (rs.getString("tf_comment") != null){
            table1.addCell(new Phrase(rs.getString("tf_comment"), SmallFont));
          } else {
            table1.addCell(new Phrase(" ", SmallFont));
          }

          if (rs.getString("ft_comment") != null){
            table1.addCell(new Phrase(rs.getString("ft_comment"), SmallFont));
          } else {
            table1.addCell(new Phrase(" ", SmallFont));
          }
          
        }
        if (table1 != null)
                document.add(table1);
        ps.clearParameters();
        ps.close();
        rs.close();

      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return -1;
      } finally {
      }
      return result;
    }

  // getCount == "true" : Get Count only
  public static boolean WS_Test_Parameter(Document doc,
                                          Font BasicFont,
                                          Connection conn,ProTestRouteBeanAF fm,
                                          String table,
                                          String site,
                                          String getCount ) {
    boolean rtn = false;
    try {
      WsTestBean[] wtb=pdfService.ToGetMask_Option(fm,conn,table);
      String sql = "SELECT * FROM tf_test_parameter_ws"+table+" where sid=? and mask_option=? ";
      if (!site.equals(""))
        sql = sql + "and site=? ";
      PreparedStatement ps = conn.prepareStatement(sql);

      rtn = wtb.length > 0;
      if (getCount.equals("true"))
        return rtn;
      for(int i=0;i<wtb.length;i++){
        String SI=String.valueOf(i+1);
        ps.setString(1,fm.getSid());
        ps.setString(2,wtb[i].getMask_option());
        if (!site.equals(""))
          ps.setString(3,fm.getVendor());
        ResultSet rs = ps.executeQuery();
        doc.add(new Phrase(new Chunk("3-1-"+SI+". WS TEST PARAMETER FOR "+
                                     fm.getProductbody()+wtb[i].getMask_option(),
                                     BasicFont)));
        PdfPTable table20 = new PdfPTable(7);
        table20.setWidthPercentage(100);
        table20.addCell(new Phrase(new Chunk("Product Code",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Test Mode",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Tester",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Site",BasicFont)));
        table20.addCell(new Phrase(new Chunk("PGM Name",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Temperature",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Notes",BasicFont)));

        while(rs.next()){
          table20.addCell(new Phrase(new Chunk(rs.getString("product_body")+rs.getString("mask_option"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("test_type"),
                                               BasicFont)));
          //table20.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),
          //BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("tester"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("site"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("program_name"),
                                               BasicFont)));
          if (rs.getString("temperature") == null){
            table20.addCell(new Phrase(new Chunk(" ",BasicFont)));
          } else {
            table20.addCell(new Phrase(new Chunk(rs.getString("temperature"),BasicFont)));
          }

          if (rs.getString("tf_comment") == null){
            table20.addCell(new Phrase(new Chunk("  ", BasicFont)));
          } else {
            table20.addCell(new Phrase(new Chunk(rs.getString("tf_comment"), BasicFont)));
          }
        }
        doc.add(table20);
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return rtn;
  }

  // getCount == "true" : Get Count only
  // getCount == "<number>" for Sections
  public static boolean FT_Test_Parameter(Document doc,
                                          Font BasicFont,
                                          Connection conn,
                                          ProTestRouteBeanAF fm,
                                          String table,
                                          String site,
                                          String getCount ) {
    boolean rtn = false;
    try {
      FTTFormBean[] wtb=pdfService.ToGetBackend_Option(fm,conn,table);
      String sql = "SELECT * FROM tf_test_parameter_ft"+table+" where sid=? and backend_option=? ";
      if (!site.equals(""))
        sql = sql + "and site=? ";
      PreparedStatement ps = conn.prepareStatement(sql);
      String productType = OiMaintainService.getProductType(fm.getSid());

      rtn = wtb.length > 0;
      if (getCount.equals("true"))
        return rtn;
      for (int i = 0;i < wtb.length; i++){
        String SI = String.valueOf(i+1);
        ps.setString(1,fm.getSid());
        ps.setString(2,wtb[i].getBackend_option());
        if (!site.equals(""))
          ps.setString(3,fm.getVendor());
        ResultSet rs = ps.executeQuery();
        doc.add(new Phrase(new Chunk("3-"+getCount+"-"+SI+". FT TEST PARAMETER FOR "+
                                     fm.getProductbody()+wtb[i].getBackend_option(),
                                     BasicFont)));
        PdfPTable table20 = null;
        if (productType.equals("NVM"))
        	table20 = new PdfPTable(11);
        else
        	table20 = new PdfPTable(10);
        table20.setWidthPercentage(100);
        table20.addCell(new Phrase(new Chunk("Product Code",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Test Mode",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Pin Count",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Package Type",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Temperature",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Body Size",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Tester",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Site",BasicFont)));
        table20.addCell(new Phrase(new Chunk("PGM Name",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Notes",BasicFont)));

        while(rs.next()){
          table20.addCell(new Phrase(new Chunk(rs.getString("product_body")+rs.getString("backend_option"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("test_type"),BasicFont)));
          //table20.addCell(new Phrase(new Chunk(rs.getString("pgm_id"),
          //BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("pin_count"),BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("package_type"),BasicFont)));
       	  table20.addCell(new Phrase(new Chunk(rs.getString("i_grade"),BasicFont)));
       	  if (productType.equals("NVM")) {
       		  table20.addCell(new Phrase(new Chunk(rs.getString("c_grade"),BasicFont)));
       	  }
          table20.addCell(new Phrase(new Chunk(rs.getString("body_size"),BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("tester"),BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("site"),BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("program_name"),BasicFont)));
          if (rs.getString("tf_comment") == null){
            table20.addCell(new Phrase(new Chunk("  ", BasicFont)));
          } else {
            table20.addCell(new Phrase(new Chunk(rs.getString("tf_comment"), BasicFont)));
          }
        }
        doc.add(table20);
      }
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return rtn;
  }

  public static boolean WS_Test_Parameter_For_Diff_Vendor(Document doc,
                                                          Font BasicFont,
                                                          Connection conn,
                                                          ProTestRouteBeanAF fm,
                                                          String table ) {

    try {
      WsTestBean[] wtb=pdfService.ToGetMask_OptionForVendor(fm,conn,table);
      String sql = "SELECT * FROM tf_test_parameter_ws"+table+" where sid=? and mask_option=? and site=? ";
      PreparedStatement ps = conn.prepareStatement(sql);

      for (int i = 0;i < wtb.length; i++){
        String SI = String.valueOf(i+1);
        ps.setString(1,fm.getSid());
        ps.setString(2,wtb[i].getMask_option());
        ps.setString(3,fm.getVendor());
        ResultSet rs = ps.executeQuery();
        doc.add(new Phrase(new Chunk("3-"+SI+".WS TEST PARAMETER FOR "+fm.getProductbody()+wtb[i].getMask_option(),
                                     BasicFont)));

        Table table20 = new Table(7, 10);
        table20.setPadding(2);
        table20.setWidth(100);
        table20.setCellsFitPage(true);
        table20.addCell(new Phrase(new Chunk("Product Code",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Test Type",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Tester",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Site",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Pgm_Name",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Temperature",BasicFont)));
        table20.addCell(new Phrase(new Chunk("Notes",BasicFont)));

        while(rs.next()){
          table20.addCell(new Phrase(new Chunk(rs.getString("product_body")+rs.getString("mask_option"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("test_type"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("tester"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("site"),
                                               BasicFont)));
          table20.addCell(new Phrase(new Chunk(rs.getString("program_name"),
                                               BasicFont)));
          if (rs.getString("temperature") == null){
            table20.addCell(new Phrase(new Chunk(" ",BasicFont)));
          } else {
            table20.addCell(new Phrase(new Chunk(rs.getString("temperature"),BasicFont)));
          }

          if (rs.getString("tf_comment") == null){
            table20.addCell(new Phrase(new Chunk("  ", BasicFont)));
          } else {
            table20.addCell(new Phrase(new Chunk(rs.getString("tf_comment"), BasicFont)));
          }
        }
        doc.add(table20);
      }
    } catch (Exception ex) {
      /** @todo Handle this exception */
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }

  // output yield definition chart
  public static boolean Yield(Document doc,
                              Font BasicFont,
          					  Font SmallFont,
                              String section,
                              String Path,
                              String sid,
                              String table,
                              Connection conn) {

	    try {
		      doc.newPage();
	          doc.add(new Paragraph(section+". YIELD CRITERIA"));

		      String sql = "SELECT * FROM tf_yield"+table+" where sid=? "+
		          "order by seq";
		      PreparedStatement ps = conn.prepareStatement(sql);
		      ps.setString(1,sid);
		      ResultSet rs = ps.executeQuery();

		      int i = 0;
		      float[] widths = {6,4,9,9,11,11,9,9,9,4,19};
		      PdfPTable table5 = null;
		      table5 = new PdfPTable(widths);
		      table5.setWidthPercentage(100);
		      table5.setSpacingBefore(5);
		      table5.addCell(new Phrase(new Chunk("Product Code",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Test Mode",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Auto Ship",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Hold PE",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Hold BIN",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Hold Bin Criteria",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Auto Scrap",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Stop",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("OOC",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Sampling Yield",SmallFont)));
		      table5.addCell(new Phrase(new Chunk("Notes",SmallFont)));

		      while (rs.next()) {
		        i++;
		        table5.addCell(new Phrase(new Chunk(rs.getString("product_code"),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(rs.getString("test_mode"),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("auto_ship")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("hold_pe")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("hold_bin")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("hold_bin_cri")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("auto_scrap")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("stop")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("mrb")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("sampling_yield")),SmallFont)));
		        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("notes")),SmallFont)));
		      }
		      doc.add(table5);
		      rs.close();
		      ps.clearParameters();
		      ps.close();
		      rs = null;
		      ps = null;
		    } catch (Exception ex) {
		      ex.fillInStackTrace();
		      TDSLogger.println(ex.getMessage());
		    }
		  
    try {
    	
      String sql = "SELECT * FROM tf_document_linkage"+table+
          " a where sid = ? and doc_type='Y' ";
      if (table.equals("_tx"))
        sql = sql + "and not exists (select 1 from tf_document_linkage_tx b " +
            "where a.sid = b.sid " +
            "and b.tag = 1 " +
            //"and a.brand = ' ' " +
            //"and b.brand = ' ' " +
            "and a.brand =  b.brand " +
            "and a.doc_type = b.doc_type " +
            "and a.seq = b.seq " +
            "and a.rowid != b.rowid) ";
      sql = sql + " order by seq";
      TDSLogger.println(sql.toString());
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, sid);
      ResultSet rs = ps.executeQuery();
      int i = 0;
      while (rs.next()) {
        doc.newPage();
        doc.add(new Paragraph(section+"-"+i+" "+rs.getString("doc_name"),BasicFont));
        doc.add(new Paragraph(rs.getString("tf_comment"),BasicFont));
        Image jpg1 = Image.getInstance(Path+File.separator+rs.getString("file_name"));
        jpg1.scaleAbsolute(500, 500);
        doc.add(jpg1);
        jpg1 = null;
      }
      ps.clearParameters();
      rs.close();
      ps.close();
      rs = null;
      ps = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }

  // Draw Test Flow Chart
  public static boolean WS_FT_Test_FLow_Chart(Document doc,
                                              String section,
                                              String Path,
                                              String sid,
                                              String table,
                                              Connection conn) {

    Font BasicFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
    try {
      String sql = "SELECT * FROM tf_document_linkage" + table +
          " a where sid = ? and doc_type='T' " ;
      if (table.equals("_tx"))
        sql = sql + "and not exists (select 1 from tf_document_linkage_tx b " +
            "where a.sid = b.sid " +
            //"and a.brand = ' ' " +
            //"and b.brand = ' ' " +
            "and a.brand = b.brand " +
            "and b.tag = 1 " +
            "and a.doc_type = b.doc_type " +
            "and a.seq = b.seq " +
            "and a.rowid != b.rowid) ";
      sql = sql + "order by seq";
      TDSLogger.println(sql.toString());
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, sid);
      ResultSet rs = ps.executeQuery();

      int i = 0;
      while (rs.next()) {
        doc.newPage();
        if (i++ == 0)
          doc.add(new Paragraph(section+". WS & FT TEST FLOW"));
        doc.add(new Paragraph(section+"-"+i+" "+rs.getString("doc_name"),BasicFont));
        doc.add(new Paragraph(rs.getString("tf_comment"),BasicFont));
        Image jpg1 = Image.getInstance(Path+File.separator+rs.getString("file_name"));
        jpg1.scaleAbsolute(500, 540);
        doc.add(jpg1);
        jpg1 = null;
      }
      ps.clearParameters();
      rs.close();
      ps.close();
      rs = null;
      ps = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }

  // Draw Test Flow Chart (New)
  public static boolean WS_FT_Test_FLow_Chart_New(Document doc,
                                                  Font BasicFont,
                                              String section,
                                              String Path,
                                              String sid,
                                              String table,
                                              Connection conn) {

    try {
      String sql = "SELECT distinct a.sid, a.product_body, a.brand, a.version, a.route_name, c.testflow_filename, c.remark, c.route_cat " +
          " FROM tf_product_route"+table+" a, tf_route_master c where a.sid = ? and a.route_name = c.route_name " ;

      TDSLogger.println(sql.toString());
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, sid);
      ResultSet rs = ps.executeQuery();

      int i = 0;
      while (rs.next()) {
        doc.newPage();
        if (i++ == 0)
          doc.add(new Paragraph(section+". WS & FT TEST FLOW"));
        doc.add(new Paragraph(section+"-"+i+" "+rs.getString("route_name"),BasicFont));//+" "+rs.getString("route_cat")+" "+rs.getString("remark")
        Image jpg1 = Image.getInstance(Path+File.separator+rs.getString("testflow_filename"));
        jpg1.scaleAbsolute(500, 540);
        doc.add(jpg1);
        jpg1 = null;
      }
      ps.clearParameters();
      rs.close();
      ps.close();
      rs = null;
      ps = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }

  /*
  // Draw User Comments for whole OI
  public static boolean Comment_Image(Document doc,
                                      String section,
                                      String Path,
                                      String sid,
                                      String table,
                                      Connection conn) {

//    Font BasicFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
    try {
      String sql = "SELECT * FROM tf_document_linkage" + table +
          " a where sid = ? and doc_type='M' ";
      if (table.equals("_tx"))
        sql = sql + "and not exists (select 1 from tf_document_linkage_tx b " +
            "where a.sid = b.sid " +
            //"and a.brand = ' ' " +
            //"and b.brand = ' ' " +
            "and a.brand = b.brand " +
            "and b.tag = 1 " +
            "and a.doc_type = b.doc_type " +
            "and a.seq = b.seq " +
            "and a.rowid != b.rowid) ";
       TDSLogger.println(sql.toString());
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ResultSet rs = ps.executeQuery();

      int i = 0;
      while (rs.next()) {
        if (++i == 1){
          doc.newPage();
          doc.add(new Paragraph(section+". COMMENTS"));
        }
        Image jpg1 = Image.getInstance(Path+File.separator+rs.getString("file_name"));
        jpg1.scaleAbsolute(500, 540);
        doc.add(jpg1);
        jpg1 = null;
      }
      ps.clearParameters();
      rs.close();
      ps.close();
      rs = null;
      ps = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }
  */

  // Print out BOM table for MH000 Venson Lee
  public static boolean BOM_Table(Document doc,
                                  Font BasicFont,
                                  String section,
                                  String sid,
                                  String table,
                                  Connection conn) {

    String productType = OiMaintainService.getProductType(sid);
    try {
      doc.newPage();
      doc.add(new Paragraph(section+". BOM vs. Product Route"));

      String sql = "SELECT * FROM tf_bom_route_xrom"+table+" where sid=? and tag != 2 "+
          "order by mask_option,mask_option_rev,pin_count,package_code,ft_route_code,code_no";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ResultSet rs = ps.executeQuery();

      int i = 0;
      //float[] widths = {2,4,4,4,4,4,4,8,4,8,8,8,8,8,8,8,16,4,8,10,10,16};
      float[] widths = {4,4,4,4,4,4,8,4,8,8,8,8,8,8,8,12,4,8,8,8,8,8,8,16};
      PdfPTable table5 = null;
      table5 = new PdfPTable(widths);
      table5.setWidthPercentage(100);
      table5.setSpacingBefore(5);
      //table5.addCell(new Phrase(new Chunk("St",BasicFont))); //1
      table5.addCell(new Phrase(new Chunk("Body Version",BasicFont)));//2
      table5.addCell(new Phrase(new Chunk("Mask Opt.",BasicFont)));//3
      table5.addCell(new Phrase(new Chunk("Mask Opt. Rev.",BasicFont)));//4
      table5.addCell(new Phrase(new Chunk("Code No.",BasicFont)));//5
      table5.addCell(new Phrase(new Chunk("Pin Count",BasicFont)));//6
      table5.addCell(new Phrase(new Chunk("Pkg Type",BasicFont)));//7
      table5.addCell(new Phrase(new Chunk("Route Type",BasicFont)));//8
      table5.addCell(new Phrase(new Chunk("FT Route Code",BasicFont)));//9
      table5.addCell(new Phrase(new Chunk("FT Route",BasicFont)));//10
      table5.addCell(new Phrase(new Chunk("FT Add. Route 1",BasicFont)));//11
      table5.addCell(new Phrase(new Chunk("FT Add. Route 2",BasicFont)));//12
      table5.addCell(new Phrase(new Chunk("FT Add. Route 3",BasicFont)));//13
      table5.addCell(new Phrase(new Chunk("FT Add. Route 4",BasicFont)));//14
      table5.addCell(new Phrase(new Chunk("FT Add. Route 5",BasicFont)));//15
      table5.addCell(new Phrase(new Chunk("FT Add. Route 6",BasicFont)));//16
      table5.addCell(new Phrase(new Chunk("FT Comment",BasicFont)));//17
      table5.addCell(new Phrase(new Chunk("Sort Route Code",BasicFont)));//18
      table5.addCell(new Phrase(new Chunk("WS Route",BasicFont)));//19
      table5.addCell(new Phrase(new Chunk("WS Add. Route 1",BasicFont)));//20
      table5.addCell(new Phrase(new Chunk("WS Add. Route 2",BasicFont)));//21
      table5.addCell(new Phrase(new Chunk("WS Add. Route 3",BasicFont)));//22
      table5.addCell(new Phrase(new Chunk("WS Add. Route 4",BasicFont)));//23
      table5.addCell(new Phrase(new Chunk("WS Add. Route 5",BasicFont)));//24
      table5.addCell(new Phrase(new Chunk("WS Comment",BasicFont)));//25

      while (rs.next()) {
        i++;
        /*String tag = rs.getString("tag");
        if (tag.equals("2"))
          table5.addCell(new Phrase(new Chunk("E",BasicFont)));
        else
          table5.addCell(new Phrase(new Chunk("",BasicFont)));*/
        table5.addCell(new Phrase(new Chunk(rs.getString("body_version"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("mask_option"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("mask_option_rev"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("code_no"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("pin_count"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("package_code"),BasicFont)));
        if (rs.getString("route_type").toString().equals("0")){
           table5.addCell(new Phrase(new Chunk("Normal",BasicFont)));}
        if (rs.getString("route_type").toString().equals("1")){
           table5.addCell(new Phrase(new Chunk("Low Temp",BasicFont)));}
        table5.addCell(new Phrase(new Chunk(rs.getString("ft_route_code"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route_add")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route_add1")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route_add2")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route_add3")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route_add4")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route_add5")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_comment")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("sort_route_code"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ws_route")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ws_route_add")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ws_route_add1")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ws_route_add2")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ws_route_add3")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ws_route_add4")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ws_comment")),BasicFont)));
      }
      doc.add(table5);
      rs.close();
      ps.clearParameters();
      ps.close();
      rs = null;
      ps = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }

  // Print out Main_Route table for MH000 Venson Lee
  public static boolean Main_Route_Sub(Document doc,
                                  Font BasicFont,
                                  Font SmallFont,
                                  String section,
                                  String sid,
                                  String table,
                                  Connection conn) {

    //Font BasicFont = new Font(Font.HELVETICA, 6, Font.NORMAL, Color.BLACK);
    String productType = OiMaintainService.getProductType(sid);
    try {
      //doc.newPage();
      //doc.add(new Paragraph(section+". Main Route vs. Substitution Route"));
      doc.add(new Paragraph(new Chunk(section+". Main Route vs. Substitution Route", BasicFont)));

      String sql = "SELECT * FROM tf_main_route_xrom"+table+" where sid=? " +
                   " and route_type=0 ";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ResultSet rs = ps.executeQuery();

      int i = 0;
      float[] widths = {14,14,128};
      PdfPTable table5 = null;
      table5 = new PdfPTable(widths);
      table5.setWidthPercentage(100);
      table5.setSpacingBefore(5);
      table5.addCell(new Phrase(new Chunk("Main Route",BasicFont)));//2
      table5.addCell(new Phrase(new Chunk("Substitution Route",BasicFont)));//3
      table5.addCell(new Phrase(new Chunk("Remark",BasicFont)));//4


      while (rs.next()) {
        i++;
        table5.addCell(new Phrase(new Chunk(rs.getString("main_route"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("map_route"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("remark")),BasicFont)));

      }
      doc.add(table5);
      rs.close();
      ps.clearParameters();
      ps.close();
      rs = null;
      ps = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }
  // Print out Main_Route table for MH000 Venson Lee
public static boolean Main_Route_Rework(Document doc,
                                Font BasicFont,
                                Font SmallFont,
                                String section,
                                String sid,
                                String table,
                                Connection conn) {

  //Font BasicFont = new Font(Font.HELVETICA, 6, Font.NORMAL, Color.BLACK);
  String productType = OiMaintainService.getProductType(sid);
  try {
    //doc.newPage();
    //doc.add(new Paragraph(section+". Main Route vs. Rework Route"));
    doc.add(new Paragraph(new Chunk(section+". Main Route vs. Rework Route", BasicFont)));

    String sql = "SELECT * FROM tf_main_route_xrom"+table+" where sid=? " +
                 " and route_type=1 ";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1,sid);
    ResultSet rs = ps.executeQuery();

    int i = 0;
    float[] widths = {14,14,128};
    PdfPTable table5 = null;
    table5 = new PdfPTable(widths);
    table5.setWidthPercentage(100);
    table5.setSpacingBefore(5);
    table5.addCell(new Phrase(new Chunk("Main Route",BasicFont)));//2
    table5.addCell(new Phrase(new Chunk("Rework Route",BasicFont)));//3
    table5.addCell(new Phrase(new Chunk("Remark",BasicFont)));//4


    while (rs.next()) {
      i++;
      table5.addCell(new Phrase(new Chunk(rs.getString("main_route"),BasicFont)));
      table5.addCell(new Phrase(new Chunk(rs.getString("map_route"),BasicFont)));
      table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("remark")),BasicFont)));

    }
    doc.add(table5);
    rs.close();
    ps.clearParameters();
    ps.close();
    rs = null;
    ps = null;
  } catch (Exception ex) {
    ex.fillInStackTrace();
    TDSLogger.println(ex.getMessage());
    return false;
  }
  return false;
  }
  // Print out BOM table for MH000 Venson Lee
  public static boolean BOM_RE_Table(Document doc,
                                     Font BasicFont,
                                  String section,
                                  String sid,
                                  String table,
                                  Connection conn) {

    String productType = OiMaintainService.getProductType(sid);
    try {
      doc.newPage();
      doc.add(new Paragraph(section+". BOM Rework Route vs. Product Route"));

      String sql = "SELECT * FROM tf_bom_reroute_xrom"+table+" where sid=? and tag != 2 "+
          "order by mask_option,mask_option_rev,pin_count,package_code,code_no,recycle_code";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ResultSet rs = ps.executeQuery();

      int i = 0;
      //float[] widths = {2,4,4,4,4,4,4,25,4,8,8,16};
      float[] widths = {4,4,4,4,4,4,25,4,8,8,16};
      PdfPTable table5 = null;
      table5 = new PdfPTable(widths);
      table5.setWidthPercentage(100);
      table5.setSpacingBefore(5);
      //table5.addCell(new Phrase(new Chunk("St",BasicFont))); //1
      table5.addCell(new Phrase(new Chunk("Body Version",BasicFont)));//2
      table5.addCell(new Phrase(new Chunk("Mask Opt.",BasicFont)));//3
      table5.addCell(new Phrase(new Chunk("Mask Opt. Rev.",BasicFont)));//4
      table5.addCell(new Phrase(new Chunk("Code No.",BasicFont)));//5
      table5.addCell(new Phrase(new Chunk("Pin Count",BasicFont)));//6
      table5.addCell(new Phrase(new Chunk("Pkg Type",BasicFont)));//7
      table5.addCell(new Phrase(new Chunk("Route Type",BasicFont)));//8
      table5.addCell(new Phrase(new Chunk("Recycle Code",BasicFont)));//9
      table5.addCell(new Phrase(new Chunk("FT Route",BasicFont)));//10
      table5.addCell(new Phrase(new Chunk("FT Add. Route",BasicFont)));//11
      table5.addCell(new Phrase(new Chunk("FT Comment",BasicFont)));//12


      while (rs.next()) {
        i++;
        /*String tag = rs.getString("tag");
        if (tag.equals("2"))
          table5.addCell(new Phrase(new Chunk("E",BasicFont)));
        else
          table5.addCell(new Phrase(new Chunk("",BasicFont)));*/
        table5.addCell(new Phrase(new Chunk(rs.getString("body_version"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("mask_option"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("mask_option_rev"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("code_no"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("pin_count"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("package_code"),BasicFont)));
        if (rs.getString("route_type").toString().equals("0")){
           table5.addCell(new Phrase(new Chunk("erase code",BasicFont)));}
        if (rs.getString("route_type").toString().equals("1")){
           table5.addCell(new Phrase(new Chunk("boot code",BasicFont)));}
        if (rs.getString("route_type").toString().equals("2")){
           table5.addCell(new Phrase(new Chunk("erase code + boot code",BasicFont)));}
        if (rs.getString("route_type").toString().equals("3")){
           table5.addCell(new Phrase(new Chunk("repair",BasicFont)));}

        table5.addCell(new Phrase(new Chunk(rs.getString("recycle_code"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(rs.getString("ft_route"),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_route_add")),BasicFont)));
        table5.addCell(new Phrase(new Chunk(StringUtil.NullConvert(rs.getString("ft_comment")),BasicFont)));

      }
      doc.add(table5);
      rs.close();
      ps.clearParameters();
      ps.close();
      rs = null;
      ps = null;
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }

  public static boolean PackageMapping(Connection conn,
                                       Document doc,
                                       ProTestRouteBeanAF fm,
                                       String section) {

    Font BasicFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);

    try {
      String sql = "select distinct b.package_type, prm2_code package_code from ";
      sql += "tf_test_parameter_ft a, ba_package_type b ";
      sql += "where sid = ? and version = ? and a.package_type = b.package_type";

      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,fm.getSid());
      ps.setString(2,fm.getVersion());

      ResultSet rs = ps.executeQuery();

      int i = 0;
      PdfPTable tableWS = null;
      while (rs.next()) {
        if (i++ == 0) {
          doc.newPage();
          doc.add(new Paragraph(section+". Package Mapping"));
          tableWS = new PdfPTable(2);
          tableWS.setSpacingBefore(10);
          tableWS.setWidthPercentage(100);
          tableWS.addCell(new Phrase(new Chunk("Package Name", BasicFont)));
          tableWS.addCell(new Phrase(new Chunk("Package Code", BasicFont)));
        }
        tableWS.addCell(new Phrase(new Chunk(rs.getString("package_type"), BasicFont)));
        tableWS.addCell(new Phrase(new Chunk(rs.getString("package_code"), BasicFont)));
      }
      doc.add(tableWS);
      ps.clearParameters();
      ps.close();
      rs.close();
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return false;
  }

  public static String getFileName(String productBody, String brand, String version, String vendor, boolean diffFlag, String tx) {
	  String fileName = null;
	  String diff = null;
      String oiname = "8049";

	  if (vendor == null)
		  vendor = "";
	  else
		  vendor = OiMaintainService.getVendorShortName(vendor);
	  if (diffFlag)
		  diff = "c";
	  else
		  diff = "";

//      if (vendor.equals("MX01")) { oiname = "8046"; vendor = ""; }

	  fileName = oiname + "-" + productBody + vendor + diff + "v" + version + tx;
	  fileName = fileName.toLowerCase();
	  return fileName;
  }
}
