package com.mxic.oiplus.xtrarom.oimaintain;


import java.sql.*;
import java.util.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.util.TDSLogger;

public class OiMaintainService {
  public OiMaintainService() {
  }

  /*get all the steps by sid*/
  public static OiMaintainStep SearchFunction(String sid) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      conn = DBConnection.getConnection();

      whereStem.put("sid", sid);
      SelSQL.append("SELECT * FROM tf_information  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      OiMaintainStep ois = null;

      while (rs.next()) {
    	ois = new OiMaintainStep();
        ois.setStatus(rs.getString("status"));
        ois.setSid(String.valueOf(rs.getInt("sid")));
        ois.setBrand(rs.getString("brand"));
        ois.setVersion(rs.getString("version"));
        ois.setProduct_body(rs.getString("product_body"));
        ois.setTf_product_route(rs.getString("tf_product_route"));
        ois.setTf_bom_route(rs.getString("tf_bom_route"));
        ois.setTf_bom_reroute(rs.getString("tf_bom_reroute"));
        ois.setTf_test_parameter_ws(rs.getString("tf_test_parameter_ws"));
        ois.setTf_test_parameter_ft(rs.getString("tf_test_parameter_ft"));
        ois.setTf_basic_information(rs.getString("tf_basic_information"));
        ois.setTf_yield_definition(rs.getString("tf_yield_definition"));
        ois.setTf_yield_ws(rs.getString("tf_yield_ws"));
        ois.setTf_yield_ft(rs.getString("tf_yield_ft"));
        ois.setTf_document_linkage(rs.getString("tf_document_linkage"));
        ois.setTf_wip_control(rs.getString("tf_wip_control"));
        ois.setCreator(rs.getString("creator"));
        ois.setSponsor_1(rs.getString("sponsor_1"));
        ois.setSponsor_2(rs.getString("sponsor_2"));
        ois.setTf_test_parameter_pbc(rs.getString("tf_test_parameter_pbc"));
        ois.setTf_main_sub(rs.getString("tf_main_sub"));
        ois.setTf_main_rework(rs.getString("tf_main_rework"));
        ois.setProduct_type(rs.getString("product_type"));
        break;
      }
      return ois;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  public static OiMaintainStep[] SearchFunction2(String version,String br,String pro_b) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    ProTestRouteBeanAF fm = new ProTestRouteBeanAF();
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("version", version);
      whereStem.put("brand", br);
      whereStem.put("product_body", pro_b);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_information  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        OiMaintainStep ois = new OiMaintainStep();
        ois.setSid(rs.getString("sid"));
        ois.setBrand(rs.getString("brand"));
        ois.setVersion(rs.getString("version"));
        ois.setProduct_body(rs.getString("product_body"));
        ois.setTf_product_route(rs.getString("tf_product_route"));
        ois.setTf_bom_route(rs.getString("tf_bom_route"));
        ois.setTf_test_parameter_ws(rs.getString("tf_test_parameter_ws"));
        ois.setTf_test_parameter_ft(rs.getString("tf_test_parameter_ft"));
        ois.setTf_basic_information(rs.getString("tf_basic_information"));
        ois.setTf_yield_definition(rs.getString("tf_yield_definition"));
        ois.setTf_document_linkage(rs.getString("tf_document_linkage"));
        ois.setTf_test_parameter_pbc(rs.getString("tf_test_parameter_pbc"));
        ois.setProduct_type(rs.getString("product_type"));
        tmp2.add(ois);
      }
      return (OiMaintainStep[]) tmp2.toArray(new OiMaintainStep[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /* get creator of the of a product by sid*/
  public static String SearchFunction2(String sid) {
    StringBuffer SelSQL = new StringBuffer();
    String creator=null;
    Connection conn = null;
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);

      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_information  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        creator=rs.getString("creator");
      }
      return creator;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /* NON-COMPLETED
     Info in Database is cleared
     But the related files are not clean yet */

  public static boolean DeleteOI(String sid) {
    StringBuffer DelSQL = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      // 這裡應先取得 OI 資料，包含 vendor list
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      conn = DBConnection.getConnection();
      DelSQL.append("call tf_delete_oi("+sid+")");
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      ps1.executeUpdate();

      // 這裡應先 check OI 是否已移掉
      // 然後應該有搬 file 的動作

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  /* 若 IF_TF_COVERPAGE 中沒有資料，則可以做退件 */
  public static int ReturnOItoProcess(String sid) {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    PreparedStatement ps1 = null;
    ResultSet rs = null;
    try {
      conn = DBConnection.getConnection();
      sql.append("select * from if_tf_coverpage a, tf_information b ");
      sql.append("where a.docno = '8049'||decode(b.brand,'MX','','K')||'-'||b.product_body and a.rev = b.version ");
      sql.append("and a.status != '退件' and b.sid = " + sid);
      ps1 = conn.prepareStatement(sql.toString());
      rs = ps1.executeQuery();
      while (rs.next()) {
        rs.close(); rs = null;
        ps1.close(); ps1 = null;
        return -1; // 如果有資料，表示 EPC 已經在處理了
      }
      rs.close(); rs = null;
      ps1.close(); ps1 = null;
      sql.delete(0,sql.length());
      sql.append("update tf_information set status = 'P' where sid = " + sid);
      ps1 = conn.prepareStatement(sql.toString());
      int i = ps1.executeUpdate();
      ps1.close(); ps1 = null;
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return 1;
  }

  /*get all available routes*/
  public static RouteNameBean[] GetAllRoute(String routename) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_route_master");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      TDSLogger.println(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        RouteNameBean trb = new RouteNameBean();
        trb.setRoutename(rs.getString("route_name"));
        tmp2.add(trb);
      }
      rs.close(); rs = null;
      ps.close(); ps = null;
      return (RouteNameBean[]) tmp2.toArray(new RouteNameBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /*get all the temperature selection for drop down boxes in jsp*/
  public static TempBean[] QueryTemperature() {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_description where tag='2' AND DELETE_FLAG IS NULL order by id asc ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        TempBean tcb = new TempBean();
        tcb.setTemp(rs.getString("description"));
        tmp2.add(tcb);
      }
      return (TempBean[]) tmp2.toArray(new TempBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  public static TempBean[] QueryTimeUnit() {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_temp_conditions  ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      TDSLogger.println(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        TempBean tcb = new TempBean();
        tcb.setTimeunit(rs.getString("timeunit"));
        tmp2.add(tcb);
      }
      return (TempBean[]) tmp2.toArray(new TempBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /*帶出PRODUCT VS ROUTE資料的主要程式*/
  public static ProTestRouteBean[] GetRoute(String sid) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);

      conn = DBConnection.getConnection();
      String sql="select a.*,b.* from tf_product_route_tx a ," +
          "(SELECT route_name,count( * ) i FROM tf_product_route_tx " +
          " where sid=? group by route_name) b " +
          "where a.route_name=b.route_name(+) and a.sid = ? " +
          "order by a.route_name, a.step_seq";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ps.setString(2,sid);
      ResultSet rs = ps.executeQuery();
      String j = "";
      while (rs.next()) {
        ProTestRouteBean trb = new ProTestRouteBean();
        if(j.equals(rs.getString("route_name"))){
          trb.setCount("0");
        } else {
          trb.setCount(rs.getString("i"));
          j= rs.getString("route_name");
        }
        trb.setProductbody(rs.getString("product_body"));
        trb.setBrand(rs.getString("brand"));
        trb.setSid(rs.getString("sid"));
        trb.setTesttime(rs.getString("test_time"));
        trb.setTimeunit(rs.getString("time_unit"));
        trb.setRemark(rs.getString("remark"));
        trb.setRoutename(rs.getString("route_name"));
        trb.setStepname(rs.getString("step_name"));
        trb.setStepseq(rs.getString("step_seq"));
        trb.setTemperature(rs.getString("temperature"));
        trb.setSamplingtest(rs.getString("sampling_test"));
        trb.setQcactualmode(rs.getString("qc_actual_mode"));
        trb.setTag(rs.getString("tag"));
        tmp2.add(trb);
      }
      return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  /*帶出PRODUCT VS ROUTE資料的主要程式*/
  public static ProTestReRouteBean[] GetReRoute(String sid) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);

      conn = DBConnection.getConnection();
      String sql="select a.*,b.* from tf_product_route_tx a ," +
          "(SELECT route_name,count( * ) i FROM tf_product_route_tx " +
          " where sid=? group by route_name) b " +
          "where a.route_name=b.route_name(+) and a.sid = ? " +
          "order by a.route_name, a.step_seq";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ps.setString(2,sid);
      ResultSet rs = ps.executeQuery();
      String j = "";
      while (rs.next()) {
        ProTestReRouteBean trb = new ProTestReRouteBean();
        if(j.equals(rs.getString("route_name"))){
          trb.setCount("0");
        } else {
          trb.setCount(rs.getString("i"));
          j= rs.getString("route_name");
        }
        trb.setProductbody(rs.getString("product_body"));
        trb.setBrand(rs.getString("brand"));
        trb.setSid(rs.getString("sid"));
        trb.setTesttime(rs.getString("test_time"));
        trb.setTimeunit(rs.getString("time_unit"));
        trb.setRemark(rs.getString("remark"));
        trb.setRoutename(rs.getString("route_name"));
        trb.setStepname(rs.getString("step_name"));
        trb.setStepseq(rs.getString("step_seq"));
        trb.setTemperature(rs.getString("temperature"));
        trb.setSamplingtest(rs.getString("sampling_test"));
        trb.setTag(rs.getString("tag"));
        tmp2.add(trb);
      }
      return (ProTestReRouteBean[]) tmp2.toArray(new ProTestReRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  //check if any earlier version of the product body and brand
  public static boolean CheckExistProductRoute(String pro_b,
                                               String version) throws Exception {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("version",maxV);
      conn = DBConnection.getConnection();
      sql.append("select * from tf_product_route ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps1.executeQuery();
      while(rs.next()){
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //check if any data of the given product body and brand in tf_product_route
  public static boolean CheckExistProductRouteTX(String sid) throws Exception {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;

    try {
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      conn = DBConnection.getConnection();
      sql.append("select * from tf_product_route_tx ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps1.executeQuery();
      while(rs.next()){
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //insert data from tf_product_route to tf_product_route_tx
  public static boolean ProRouteToProRouteTx(String pro_b,
                                             String sid,
                                             String brand,
                                             String version) {

    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV = String.valueOf(Integer.parseInt(version)-1);
    try {
      conn = DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body",pro_b);
      whereStem.put("brand",brand);
      whereStem.put("version", maxV);
      SqlStmt.append("insert into tf_product_route_tx " +
                     "(sid,tag,product_body,brand,version,route_name,step_seq," +
                     "step_name,temperature,sampling_test,test_time,time_unit,remark) " +
                     "select " + sid + ",0,product_body,brand," + version +
                     ",route_name,step_seq,step_name,temperature,sampling_test,test_time,time_unit,remark " +
                     "FROM tf_product_route  ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
      ps.executeUpdate();
    }catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //get data of steps of a route
  public static ProTestRouteBean[] GetRouteStep(String sid,
                                                String routename) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      whereStem.put("route_name", routename);

      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_product_route_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("order by step_seq");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        ProTestRouteBean trb = new ProTestRouteBean();
        trb.setSid(rs.getString("sid"));
        trb.setProductbody(rs.getString("product_body"));
        trb.setBrand(rs.getString("brand"));
        trb.setTesttime(rs.getString("test_time"));
        trb.setTimeunit(rs.getString("time_unit"));
        trb.setRemark(StringUtil.Utf8ToBig5(rs.getString("remark")));
        trb.setRoutename(rs.getString("route_name"));
        trb.setStepname(rs.getString("step_name"));
        trb.setStepseq(rs.getString("step_seq"));
        trb.setTemperature(rs.getString("temperature"));
        trb.setSamplingtest(rs.getString("sampling_test"));
        tmp2.add(trb);
      }
      return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  ////update condition,temperature, and remark of tf_product_route_tx
  public static boolean UpdateRouteStep(String sid,
                                        String routename,
                                        String[] seq,
                                        String[] txtTesttime,
                                        String[] txtTimeunit,
                                        String[] txtTemp,
                                        String[] txtRemark,
                                        String[] txtSamplingtest) throws Exception {
    StringBuffer UpdateSQL1 = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      UpdateSQL1.append("Update tf_product_route_tx set remark=? "+
                        "where sid=? and route_name=? and step_seq=? ");
      String upt2 = "Update tf_product_route_tx set test_time=?,time_unit=?,temperature=?,sampling_test=?  "+
          "where sid=? and route_name=? and step_seq=? ";
      PreparedStatement ps1 = conn.prepareStatement(UpdateSQL1.toString());
      PreparedStatement ps2 = conn.prepareStatement(upt2);
      /* update all the columns except test_time, time_unit,remark*/
      for (i = 0; i < seq.length; i++) {
        ps1.setString(1, StringUtil.Utf8ToBig5((txtRemark[i])));
        ps1.setString(2, sid);
        ps1.setString(3, routename);
        ps1.setString(4, seq[i]);
        ps1.executeUpdate();
      }
      /* update test_time,time_unit,remark for selected steps*/
      int rec_numnber = 0;
      if (txtTesttime != null) rec_numnber = txtTesttime.length;
      if (txtTimeunit != null) rec_numnber = txtTimeunit.length;
      if (txtTemp != null && txtTemp.length > rec_numnber) rec_numnber = txtTemp.length;

      for(int j=0;j<rec_numnber;j++){
        String Fir[]=StringUtil.parse2StringsStr(txtTimeunit[j],"#");
        int k=StringUtil.formatInt(Fir[1]);
        ps2.setString(1, txtTesttime[j]);
        if(txtTesttime == null || txtTesttime[j].equals("")
                ||txtTimeunit == null || txtTimeunit[j].equals("")){
          ps2.setString(1, "");
          ps2.setString(2, "");
        }else{
          ps2.setString(1, txtTesttime[j]);
          ps2.setString(2, StringUtil.FormatData3(txtTimeunit[j]));
        }
        if(txtTemp == null || txtTemp[j].equals(""))
            ps2.setString(3, "");
         else
            ps2.setString(3,txtTemp[j]);
        if(txtSamplingtest == null || txtSamplingtest[j].equals(""))
            ps2.setString(4, "");
         else
            ps2.setString(4,txtSamplingtest[j]);
        
        ps2.setString(5,sid);
        ps2.setString(6,routename);
        ps2.setInt(7,k+1);
        ps2.executeUpdate();
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  ////update condition,temperature, and remark of tf_product_route_tx
  public static boolean UpdateRouteStep(String sid,
                                        String routename,
                                        String[] seq,
                                        HashMap txtTesttime,
                                        HashMap txtTimeunit,
                                        HashMap txtTemp,
                                        String[] txtRemark,
                                        HashMap txtSamplingtest) throws Exception {
    StringBuffer UpdateSQL1 = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      UpdateSQL1.append("Update tf_product_route_tx set remark=?,test_time=?,time_unit=?,temperature=?,sampling_test=? "+
                        "where sid=? and route_name=? and step_seq=? ");
      PreparedStatement ps1 = conn.prepareStatement(UpdateSQL1.toString());
      /* update all the columns except test_time, time_unit,remark*/
      for (i = 0; i < seq.length; i++) {
      	String timeStr = "";
    	String unitStr = "";
    	String tempStr = "";
    	String samplingtestStr = "";
    	if (txtTesttime.containsKey(seq[i]))
  	      timeStr = (String)txtTesttime.get(seq[i]);
    	if (txtTimeunit.containsKey(seq[i]))
          unitStr = (String)txtTimeunit.get(seq[i]);
    	if (txtTemp.containsKey(seq[i]))
          tempStr = (String)txtTemp.get(seq[i]);
    	if (txtSamplingtest.containsKey(seq[i]))
            samplingtestStr = (String)txtSamplingtest.get(seq[i]);

        ps1.setString(1, StringUtil.Utf8ToBig5((txtRemark[i])));
        if (timeStr == null || timeStr.equals("")) {
          ps1.setString(2,"");
          ps1.setString(3,"");
        } else {
          ps1.setString(2, timeStr);
          ps1.setString(3, unitStr);
        }
        if (tempStr == null || tempStr.equals(""))
          ps1.setString(4,"");
        else
          ps1.setString(4, tempStr);
        
        if(samplingtestStr == null || samplingtestStr.equals(""))
            ps1.setString(5, "");
        else
            ps1.setString(5,samplingtestStr);
        
        ps1.setString(6, sid);
        ps1.setString(7, routename);
        ps1.setString(8, seq[i]);
        ps1.executeUpdate();
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  /*delete selected route for a product*/
  public static boolean DeleteRoute(String sid,
                                    String routename) throws Exception {

    StringBuffer DelSQL = new StringBuffer();
    Connection conn = null;

    try {
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      whereStem.put("route_name", routename);

      conn = DBConnection.getConnection();
      DelSQL.append("delete from tf_product_route_tx ");
      DelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      ps1.executeUpdate();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
        DBConnection.close(conn);
        conn = null;
    }
    return false;
  }

  /* get all the route infos for add new route function*/
  public static ProTestRouteBean[] GetRouteInfo(String routename) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("route_name", routename);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_route_master  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      int i;
      while (rs.next()){
        for (i = 1; i < 11; i++) {
          ProTestRouteBean trb = new ProTestRouteBean();
          if(rs.getString("Step" + i)!=null){
            trb.setStepname(rs.getString("Step" + i));
            trb.setCount(StringUtil.DoubleDigitNum(i));
            tmp2.add(trb);
          } else {
            TDSLogger.println("No More");
          }
        }
      }
      return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  

  /* copy product route from pbname to sid*/
  public static boolean CopyRouteFromProduct(String sid,
                                             String product_body,
                                             String version,
                                             String pbname) {
    String SelSQL = null;
    Connection conn = null;
    boolean result = false;
    PreparedStatement ps = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      SelSQL = "delete from tf_product_route_tx where sid = "+sid;
      ps = conn.prepareStatement(SelSQL.toString());
      int i = ps.executeUpdate();

      ps.close();
      ps = null;
      if (i >= 0) {
        SelSQL = "insert into tf_product_route_tx\n" +
            "select " + sid + ",0,'" + product_body + "','MX'," + version +
            ",\n" +
            "a.route_name,a.step_seq,a.step_name,a.test_time,a.time_unit,a.temperature,a.remark,\n" +
            "a.REWORK_STEP,a.TEST_TIME2,a.TIME_UNIT2,a.sampling_test,a.qc_actual_mode,a.step_def,sampling_cond\n" +
            "from tf_product_route a, tf_current_version_vw b\n" +
            "where a.sid = b.sid\n" +
            "and a.product_body = '" + pbname + "'\n" +
            "and a.brand = 'MX'\n" +
            "order by route_name,step_seq";

        ps = conn.prepareStatement(SelSQL.toString());
        i = ps.executeUpdate();
        conn.commit();
        conn.setAutoCommit(true);
        ps.close();
        ps = null;
        if (i >= 0)
          result = true;
      }

      return result;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return result;
  }

  /* check if the route is already defined in tf_product_route_tx */
  public static countstepbean[] CheckRouteExist(String sid,
                                                String routename) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      whereStem.put("route_name", routename);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_product_route_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        countstepbean trb = new countstepbean();
        trb.setRoutename(rs.getString("route_name"));
        tmp2.add(trb);
      }
      return (countstepbean[]) tmp2.toArray(new countstepbean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /* check if the route is already defined in tf_product_route_tx */
  public static boolean CheckProductExist(String sid,
                                          String product) {
    boolean result = false;
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      conn = DBConnection.getConnection();

      SelSQL.append("select 1 from tf_information a\n");
      SelSQL.append("where a.product_body = '"+product+"'\n");
      SelSQL.append("and a.status = 'R'");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        result = true;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
      return result;
    }
  }
  /* get all the fw route for selected product */
  public static ProTestRouteBean[] RWRPFTRoute(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct route_name FROM tf_product_route_tx\n");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(route_name,2,1) in ('P','Q','W')\n");
      SelSQL.append(" order by route_name\n");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        ProTestRouteBean ptr = new ProTestRouteBean();
        ptr.setRoutename(rs.getString("route_name"));
        tmp2.add(ptr);
      }
      return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /* get all the fw route for selected product */
  public static ProTestRouteBean[] RWRoute(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct route_name FROM tf_product_route_tx\n");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(route_name,2,1)='W'\n");
      SelSQL.append(" order by route_name\n");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        ProTestRouteBean ptr = new ProTestRouteBean();
        ptr.setRoutename(rs.getString("route_name"));
        tmp2.add(ptr);
      }
      return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

/* get all the fw reroute for selected product */
  public static ProTestReRouteBean[] RWReRoute(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct route_name FROM tf_product_route_tx\n");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(route_name,2,1)='W'\n");
      SelSQL.append(" order by route_name\n");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        ProTestReRouteBean ptr = new ProTestReRouteBean();
        ptr.setRoutename(rs.getString("route_name"));
        tmp2.add(ptr);
      }
      return (ProTestReRouteBean[]) tmp2.toArray(new ProTestReRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  /* get all the fp route for selected product */
  public static ProTestRouteBean[] RPFTRoute(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct route_name FROM tf_product_route_tx\n");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(route_name,2,1) in ('P','Q')\n");
      SelSQL.append(" order by route_name\n");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()){
        ProTestRouteBean ptr = new ProTestRouteBean();
        ptr.setRoutename(rs.getString("route_name"));
        tmp2.add(ptr);
      }
      return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  /* get all the fp reroute for selected product */
  public static ProTestReRouteBean[] RPReRoute(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct route_name FROM tf_product_route_tx\n");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(route_name,2,1) in ('P','Q')\n");
      SelSQL.append(" order by route_name\n");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()){
        ProTestReRouteBean ptr = new ProTestReRouteBean();
        ptr.setRoutename(rs.getString("route_name"));
        tmp2.add(ptr);
      }
      return (ProTestReRouteBean[]) tmp2.toArray(new ProTestReRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /* 20090908, Robin, for Bom_ProductRoute_Map.jsp
   * get all the corresponding route cat. of fp route for selected product 
   * !!! The Route sequence must be the same as in RPFTRoute()*/
  public static RouteNameBean[] GetFTRouteCat(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct a.route_name, b.route_cat\n");
      SelSQL.append("FROM tf_product_route_tx a, tf_route_master b\n");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(a.route_name,2,1) in ('P','Q')\n");
      SelSQL.append(" and a.route_name = b.route_name\n");
      SelSQL.append(" order by a.route_name\n");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()){
    	  RouteNameBean ptr = new RouteNameBean();
    	  ptr.setRoutecat(rs.getString("route_cat"));
    	  tmp2.add(ptr);
      }
      return (RouteNameBean[]) tmp2.toArray(new RouteNameBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
//lai-add-start-20070522
  /* get all the Body version for A ~ Z */
  public static ProTestRouteBean[] Bodyversion(String pro_b) {
    StringBuffer SelSQL = new StringBuffer();

    try {
        ArrayList tmp2 = new ArrayList();
        ProTestRouteBean ptr = new ProTestRouteBean();
        ptr.setBodyversion("*");
        tmp2.add(ptr);
        String alphabet = "A";
        int alp = (int)alphabet.charAt(0);
        for(int i=0;i<26;i++){
              //tmp2.add(String.valueOf((char)alp));
              ptr = new ProTestRouteBean();
              ptr.setBodyversion(String.valueOf((char)alp));
              tmp2.add(ptr);
              alp++;
        }

        return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {

    }
    return null;
  }
  /* get all the Body version for selected A ~ Z */
  public static ProTestReRouteBean[] BodyversionRe(String pro_b) {
    StringBuffer SelSQL = new StringBuffer();

    try {
        ArrayList tmp2 = new ArrayList();
        ProTestReRouteBean ptr = new ProTestReRouteBean();
        ptr.setBodyversion("*");
        tmp2.add(ptr);
        String alphabet = "A";
        int alp = (int)alphabet.charAt(0);
        for(int i=0;i<26;i++){
              //tmp2.add(String.valueOf((char)alp));
              ptr = new ProTestReRouteBean();
              ptr.setBodyversion(String.valueOf((char)alp));
              tmp2.add(ptr);
              alp++;
        }

        return (ProTestReRouteBean[]) tmp2.toArray(new ProTestReRouteBean[0]);

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {

    }
    return null;
  }

  /* get all the Body version for selected A ~ Z */
  public static ProTestRouteBean[] Maskoptionrev(String pro_b) {
    StringBuffer SelSQL = new StringBuffer();

    try {
        ArrayList tmp2 = new ArrayList();
        ProTestRouteBean ptr = new ProTestRouteBean();
        ptr.setMaskoptrev("*");
        tmp2.add(ptr);
        String alphabet = "A";
        int alp = (int)alphabet.charAt(0);
        for(int i=0;i<26;i++){
              //tmp2.add(String.valueOf((char)alp));
              ptr = new ProTestRouteBean();
              ptr.setMaskoptrev(String.valueOf((char)alp));
              tmp2.add(ptr);
              alp++;
        }

        return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {

    }
    return null;
  }
  /* get all the Body version for selected A ~ Z */
  public static ProTestReRouteBean[] MaskoptionrevRe(String pro_b) {
    StringBuffer SelSQL = new StringBuffer();

    try {
        ArrayList tmp2 = new ArrayList();
        ProTestReRouteBean ptr = new ProTestReRouteBean();
        ptr.setMaskoptrev("*");
        tmp2.add(ptr);
        String alphabet = "A";
        int alp = (int)alphabet.charAt(0);
        for(int i=0;i<26;i++){
              //tmp2.add(String.valueOf((char)alp));
              ptr = new ProTestReRouteBean();
              ptr.setMaskoptrev(String.valueOf((char)alp));
              tmp2.add(ptr);
              alp++;
        }

        return (ProTestReRouteBean[]) tmp2.toArray(new ProTestReRouteBean[0]);

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {

    }
    return null;
  }
//lai-add-end-20070522
  /* Add new route info. into tf_product_route_tx */
/*
  public static boolean InsertRouteNameStep(String sid,
                                            String routename,
                                            String pro_b,
                                            String brand,
                                            String version,
                                            String[] seq,
                                            String[] txtTime,
                                            String[] txtUnit,
                                            String[] stepname,
                                            String[] txtTemp,
                                            String[] txtRemark) throws Exception {
    StringBuffer InsSQL = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL.append("insert into tf_product_route_tx " +
                    "(sid,product_body,brand,version,route_name,step_seq,step_name,remark) ");
      InsSQL.append("values (?,?,?,?,?,?,?,?)");
      String upt="update tf_product_route_tx set test_time=?, time_unit=?, temperature=? " +
          "where sid=? and route_name=? and step_seq=? ";
      PreparedStatement ps1 = conn.prepareStatement(InsSQL.toString());
      PreparedStatement psUpt = conn.prepareStatement(upt);

      for (i = 0; i < seq.length; i++) {
        ps1.setString(1, sid);
        ps1.setString(2, pro_b);
        ps1.setString(3, brand);
        ps1.setString(4, version);
        ps1.setString(5, routename);
        ps1.setString(6, seq[i]);
        ps1.setString(7, stepname[i]);
        ps1.setString(8, StringUtil.Utf8ToBig5((txtRemark[i])));
        ps1.executeUpdate();
      }

      int rec_numnber = 0;
      if (txtUnit != null) rec_numnber = txtUnit.length;
      if (txtTemp != null && txtTemp.length > rec_numnber) rec_numnber = txtTemp.length;

      for (int j = 0; j < rec_numnber; j++){
        String Fir[] = StringUtil.parse2StringsStr(txtUnit[j], "#");
        int k = StringUtil.formatInt(Fir[1]);
        if (txtTime == null || txtTime[j].equals("")) {
          psUpt.setString(1, "");
          psUpt.setString(2, "");
        }
        else {
          psUpt.setString(1, txtTime[j]);
          psUpt.setString(2, StringUtil.FormatData3(txtUnit[j]));
        }
        if (txtTemp == null || txtTemp[j].equals(""))
          psUpt.setString(3,"");
        else
          psUpt.setString(3,txtTemp[j]);
        psUpt.setString(4,sid);
        psUpt.setString(5,routename);
        psUpt.setInt(6,k+1);
        psUpt.executeUpdate();
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
*/
  /* Add new route info. into tf_product_route_tx */
  public static boolean InsertRouteNameStep(String sid,
                                            String routename,
                                            String pro_b,
                                            String brand,
                                            String version,
                                            String[] seq,
                                            HashMap txtTime,
                                            HashMap txtUnit,
                                            String[] stepname,
                                            HashMap txtTemp,
                                            String[] txtRemark,
                                            HashMap txtSamplingtest) throws Exception {
    StringBuffer InsSQL = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL.append("insert into tf_product_route_tx " +
                    "(sid,tag, product_body,brand,version,route_name,step_seq,step_name,remark,test_time,time_unit,temperature,sampling_test) ");
      InsSQL.append("values (?,0,?,?,?,?,?,?,?,?,?,?,?)");
      PreparedStatement ps1 = conn.prepareStatement(InsSQL.toString());

      for (i = 0; i < seq.length; i++) {
    	String timeStr = "";
    	String unitStr = "";
    	String tempStr = "";
    	String samplingtestStr = "";
    	if (txtTime.containsKey(seq[i]))
    		timeStr = (String)txtTime.get(seq[i]);
    	if (txtUnit.containsKey(seq[i]))
    		unitStr = (String)txtUnit.get(seq[i]);
    	if (txtTemp.containsKey(seq[i]))
    		tempStr = (String)txtTemp.get(seq[i]);
    	if (txtSamplingtest.containsKey(seq[i]))
    		samplingtestStr = (String)txtSamplingtest.get(seq[i]);
        ps1.setString(1, sid);
        ps1.setString(2, pro_b);
        ps1.setString(3, brand);
        ps1.setString(4, version);
        ps1.setString(5, routename);
        ps1.setString(6, seq[i]);
        ps1.setString(7, stepname[i]);
        ps1.setString(8, StringUtil.Utf8ToBig5((txtRemark[i])));
        if (timeStr == null || timeStr.equals("")) {
          ps1.setString(9,"");
          ps1.setString(10,"");
        } else {
          ps1.setString(9, timeStr);
          ps1.setString(10, unitStr);
        }
        if (tempStr == null || tempStr.equals(""))
          ps1.setString(11,"");
        else
          ps1.setString(11, tempStr);
        if (samplingtestStr == null || samplingtestStr.equals(""))
            ps1.setString(12,"");
          else
            ps1.setString(12, samplingtestStr);
        ps1.executeUpdate();
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //check if any earlier version of data of the given product_body and brand in tf_bom_route_xrom
  public static boolean CheckExistBomRoute(String sid,
                                           String pro_b,
                                           String version) throws Exception {

    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("version",maxV);
      whereStem.put("tag", "0");
      conn = DBConnection.getConnection();
      sql.append("select * from tf_bom_route_xrom ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps1.executeQuery();
      while(rs.next()){
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //check if any earlier version of data of the given product_body and brand in tf_bom_reroute_xrom
  public static boolean CheckExistBomReRoute(String sid,
                                           String pro_b,
                                           String version) throws Exception {

    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("version",maxV);
      whereStem.put("tag", "0");
      conn = DBConnection.getConnection();
      sql.append("select * from tf_bom_reroute_xrom ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps1.executeQuery();
      while(rs.next()){
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  //check if any data of the give product_body and brand in tf_bom_route_xrom_tx
   public static boolean CheckExistTX(String sid,
                                      Connection conn,
                                      String pro_b) throws Exception {

     StringBuffer sql = new StringBuffer();
     try {
       HashMap whereStem = new HashMap();
       whereStem.put("sid", sid);
       sql.append("select count(1) as cnt from tf_bom_route_xrom_tx ");
       sql.append(SQLStem.getWhereStmt(whereStem));
       PreparedStatement ps1 = conn.prepareStatement(sql.toString());
       ResultSet rs=ps1.executeQuery();
       while(rs.next()){
         if (rs.getInt("cnt")>0){
           return true;
         } else {
           return false;
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
    } finally {
    }
    return false;
  }

  //check if any data of the give product_body and brand in tf_bom_reroute_xrom_tx
   public static boolean CheckExistReTX(String sid,
                                      Connection conn,
                                      String pro_b) throws Exception {

     StringBuffer sql = new StringBuffer();
     try {
       HashMap whereStem = new HashMap();
       whereStem.put("sid", sid);
       sql.append("select count(1) as cnt from tf_bom_reroute_xrom_tx ");
       sql.append(SQLStem.getWhereStmt(whereStem));
       PreparedStatement ps1 = conn.prepareStatement(sql.toString());
       ResultSet rs=ps1.executeQuery();
       while(rs.next()){
         if (rs.getInt("cnt")>0){
           return true;
         } else {
           return false;
         }
       }
     } catch (Exception e) {
       e.printStackTrace();
    } finally {
    }
    return false;
  }


  //check if any data of the given product body and brand in tf_test_parameter_ws_tx
  public static boolean CheckWS_TX_Exist(String sid,
                                         String pro_b,
                                         String brand) throws Exception {

    StringBuffer sql = new StringBuffer();
    Connection conn=null;
    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      sql.append("select * from tf_test_parameter_ws_tx ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs = ps1.executeQuery();
      while(rs.next()){
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //check if any earlier version data of the given product body and brand in tf_test_parameter_ws
  public static boolean CheckWS_Exist(String sid,
                                      String pro_b,
                                      String brand,
                                      String version) throws Exception {

    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV = String.valueOf(Integer.parseInt(version)-1);
    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      whereStem.put("version",maxV);
      sql.append("select * from tf_test_parameter_ws ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps1.executeQuery();
      while(rs.next()){
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  public static boolean ChkProdEpn(String pro_b,
                                   String sid,
                                   String version) {

    StringBuffer SelSQL = new StringBuffer();
    StringBuffer InsSQL=new StringBuffer();
    Connection conn = null;

    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", "MX");
      whereStem.put("!status", "E");
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_prod_epn  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        return true;
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //copy the data from tf_bome_route_xrom to tf_bome_route_xrom_tx
  public static boolean RouteToRouteTx(String pro_b,
                                       String sid,
                                       String version) {
    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    Vector result = new Vector();
    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body",pro_b);
      whereStem.put("version", maxV);
      whereStem.put("tag", "0");

      SqlStmt.append("insert into tf_bom_route_xrom_tx (sid,tag,product_body,version,body_version,pin_count,package_code,ft_route,mask_option,mask_option_rev,code_no,route_type,sort_route_code,ws_route,ws_route_add,ws_route_add1,ws_route_add2,ws_route_add3,ws_route_add4,ft_comment,id,ft_route_add,ft_route_add1,ft_route_add2,ft_route_add3,ft_route_add4,ft_route_add5,ft_route_code,ws_comment) ");
      SqlStmt.append("select " + sid + ",0,product_body," + version + ",body_version,pin_count,package_code,ft_route,mask_option,mask_option_rev,code_no,route_type,sort_route_code,ws_route,ws_route_add,ws_route_add1,ws_route_add2,ws_route_add3,ws_route_add4,ft_comment,test_seq.nextval,ft_route_add,ft_route_add1,ft_route_add2,ft_route_add3,ft_route_add4,ft_route_add5,ft_route_code,ws_comment from tf_bom_route_xrom ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
      ps.executeUpdate();

//      SqlStmt.append("insert into tf_bom_route_tx (sid,tag,product_body,brand,version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,id,ft_route_add,ft_route_code,tf_ws_comment, sales_form) ");
/*
      SqlStmt.append("select " + sid + " sid ,0 tag,product_body,brand," + version + " version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,test_seq.nextval id,ft_route_add,ft_route_code,tf_ws_comment, NVL(sales_form, 'nvm') sales_form from tf_bom_route ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      String ftRouteCode = null;
      while (rs.next()) {
    	  BomProductRouteBean bean = new BomProductRouteBean();
    	  bean.setSid(rs.getString("SID"));
    	  bean.setTag(rs.getString("TAG"));
    	  bean.setBrand(rs.getString("BRAND"));
    	  bean.setProductbody(rs.getString("PRODUCT_BODY"));
    	  bean.setVersion(rs.getString("VERSION"));
    	  bean.setBeoption(rs.getString("BACKEND_OPTION"));
    	  bean.setFgwithcode(rs.getString("FG_WITH_CODE"));
    	  bean.setPincount(rs.getString("PIN_COUNT"));
    	  bean.setPkgtype(rs.getString("PACKAGE_TYPE"));
    	  bean.setFtroute(rs.getString("FT_ROUTE"));
    	  bean.setMaskopt(rs.getString("MASK_OPTION"));
    	  bean.setSortroutecode(rs.getString("SORT_ROUTE_CODE"));
    	  bean.setDbwithcode(rs.getString("DB_WITH_CODE"));
    	  bean.setWsroute(rs.getString("WS_ROUTE"));
    	  bean.setWsaddroute(rs.getString("WS_ROUTE_ADD"));
    	  bean.setComment(rs.getString("TF_COMMENT"));
    	  bean.setId(rs.getString("ID"));
    	  bean.setFtAddroute(rs.getString("FT_ROUTE_ADD"));
    	  ftRouteCode = rs.getString("FT_ROUTE_CODE");
//    	  ftRouteCode = getNextRouteCode(ftRouteCode);
    	  bean.setFt_route_code(ftRouteCode);
    	  bean.setTf_ws_comment(rs.getString("TF_WS_COMMENT"));
    	  bean.setSales_form(rs.getString("SALES_FORM"));
    	  result.add(bean);
      }

      SqlStmt = new StringBuffer();
      SqlStmt.append("insert into tf_bom_route_tx (sid,tag,product_body,brand,version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,id,ft_route_add,ft_route_code,tf_ws_comment, sales_form) ");
      SqlStmt.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      ps = conn.prepareStatement(SqlStmt.toString());
      for (int i=0; i<result.size(); i++) {
    	  BomProductRouteBean bean = (BomProductRouteBean)result.get(i);
    	  ps.setString(1, bean.getSid());
    	  ps.setString(2, bean.getTag());
    	  ps.setString(3, bean.getProductbody());
    	  ps.setString(4, bean.getBrand());
    	  ps.setString(5, bean.getVersion());
    	  ps.setString(6, bean.getBeoption());
    	  ps.setString(7, bean.getFgwithcode());
    	  ps.setString(8, bean.getPincount());
    	  ps.setString(9, bean.getPkgtype());
    	  ps.setString(10, bean.getFtroute());
    	  ps.setString(11, bean.getMaskopt());
    	  ps.setString(12, bean.getSortroutecode());
    	  ps.setString(13, bean.getDbwithcode());
    	  ps.setString(14, bean.getWsroute());
    	  ps.setString(15, bean.getWsaddroute());
    	  ps.setString(16, bean.getComment());
    	  ps.setString(17, bean.getId());
    	  ps.setString(18, bean.getFtAddroute());
    	  ps.setString(19, bean.getFt_route_code());
    	  ps.setString(20, bean.getTf_ws_comment());
    	  ps.setString(21, bean.getSales_form());
    	  ps.executeUpdate();
      }
*/
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
//copy the data from tf_bome_reroute_xrom to tf_bome_reroute_xrom_tx
  public static boolean RouteToReRouteTx(String pro_b,
                                       String sid,
                                       String version) {
    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    Vector result = new Vector();
    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body",pro_b);
      whereStem.put("version", maxV);
      whereStem.put("tag", "0");

      SqlStmt.append("insert into tf_bom_reroute_xrom_tx (sid,tag,product_body,version,body_version,pin_count,package_code,ft_route,mask_option,mask_option_rev,code_no,route_type,ft_comment,id,ft_route_add,recycle_code) ");
      SqlStmt.append("select " + sid + ",0,product_body," + version + ",body_version,pin_count,package_code,ft_route,mask_option,mask_option_rev,code_no,route_type,ft_comment,test_seq.nextval,ft_route_add,recycle_code from tf_bom_reroute_xrom ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
      ps.executeUpdate();

//      SqlStmt.append("insert into tf_bom_route_tx (sid,tag,product_body,brand,version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,id,ft_route_add,ft_route_code,tf_ws_comment, sales_form) ");
/*
      SqlStmt.append("select " + sid + " sid ,0 tag,product_body,brand," + version + " version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,test_seq.nextval id,ft_route_add,ft_route_code,tf_ws_comment, NVL(sales_form, 'nvm') sales_form from tf_bom_route ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
      ResultSet rs = ps.executeQuery();
      String ftRouteCode = null;
      while (rs.next()) {
              BomProductRouteBean bean = new BomProductRouteBean();
              bean.setSid(rs.getString("SID"));
              bean.setTag(rs.getString("TAG"));
              bean.setBrand(rs.getString("BRAND"));
              bean.setProductbody(rs.getString("PRODUCT_BODY"));
              bean.setVersion(rs.getString("VERSION"));
              bean.setBeoption(rs.getString("BACKEND_OPTION"));
              bean.setFgwithcode(rs.getString("FG_WITH_CODE"));
              bean.setPincount(rs.getString("PIN_COUNT"));
              bean.setPkgtype(rs.getString("PACKAGE_TYPE"));
              bean.setFtroute(rs.getString("FT_ROUTE"));
              bean.setMaskopt(rs.getString("MASK_OPTION"));
              bean.setSortroutecode(rs.getString("SORT_ROUTE_CODE"));
              bean.setDbwithcode(rs.getString("DB_WITH_CODE"));
              bean.setWsroute(rs.getString("WS_ROUTE"));
              bean.setWsaddroute(rs.getString("WS_ROUTE_ADD"));
              bean.setComment(rs.getString("TF_COMMENT"));
              bean.setId(rs.getString("ID"));
              bean.setFtAddroute(rs.getString("FT_ROUTE_ADD"));
              ftRouteCode = rs.getString("FT_ROUTE_CODE");
//    	  ftRouteCode = getNextRouteCode(ftRouteCode);
              bean.setFt_route_code(ftRouteCode);
              bean.setTf_ws_comment(rs.getString("TF_WS_COMMENT"));
              bean.setSales_form(rs.getString("SALES_FORM"));
              result.add(bean);
      }

      SqlStmt = new StringBuffer();
      SqlStmt.append("insert into tf_bom_route_tx (sid,tag,product_body,brand,version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,id,ft_route_add,ft_route_code,tf_ws_comment, sales_form) ");
      SqlStmt.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      ps = conn.prepareStatement(SqlStmt.toString());
      for (int i=0; i<result.size(); i++) {
              BomProductRouteBean bean = (BomProductRouteBean)result.get(i);
              ps.setString(1, bean.getSid());
              ps.setString(2, bean.getTag());
              ps.setString(3, bean.getProductbody());
              ps.setString(4, bean.getBrand());
              ps.setString(5, bean.getVersion());
              ps.setString(6, bean.getBeoption());
              ps.setString(7, bean.getFgwithcode());
              ps.setString(8, bean.getPincount());
              ps.setString(9, bean.getPkgtype());
              ps.setString(10, bean.getFtroute());
              ps.setString(11, bean.getMaskopt());
              ps.setString(12, bean.getSortroutecode());
              ps.setString(13, bean.getDbwithcode());
              ps.setString(14, bean.getWsroute());
              ps.setString(15, bean.getWsaddroute());
              ps.setString(16, bean.getComment());
              ps.setString(17, bean.getId());
              ps.setString(18, bean.getFtAddroute());
              ps.setString(19, bean.getFt_route_code());
              ps.setString(20, bean.getTf_ws_comment());
              ps.setString(21, bean.getSales_form());
              ps.executeUpdate();
      }
*/
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  public static boolean CheckExistMember(String id,Connection conn) throws Exception{
    String sql = "Select count(1) as cnt from tf_bom_route_tx where id=?  ";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1,id);
    ResultSet rs = ps.executeQuery();
    if(rs != null){
      if(rs.next()){
        if(rs.getInt("cnt") > 0){
          return true;
        }
      }
    }
    return false;
  }

  // 將TF＿PROD＿EPN的DATA與tf_bom_route_xrom_tx裡的比對，如沒有重複的的就return true
  public static boolean CheckExistInBomTx(Connection conn,
                                          String pro_b,
                                          String pin,
                                          String pkgtype,
                                          String mask,
                                          String version) throws Exception{

    StringBuffer SelSQL = new StringBuffer();
    SelSQL.append("SELECT count(1) as cnt FROM tf_bom_route_xrom_tx ");
    SelSQL.append("where product_body=?  ");
    SelSQL.append("and pin_count=? and package_code=? and mask_option=? and version=? ");
    PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
    ps.setString(1,pro_b);
    ps.setString(2,pin);
    ps.setString(3,pkgtype);
    ps.setString(4,mask);
    ps.setString(5,version);
    ResultSet rs = ps.executeQuery();

    while(rs.next()){
      if(rs.getInt("cnt")==0){
        return true;
      }
    }
    return false;
  }
  // 將TF＿PROD＿EPN的DATA與tf_bom_reroute_xrom_tx裡的比對，如沒有重複的的就return true
  public static boolean CheckExistInBomReTx(Connection conn,
                                          String pro_b,
                                          String pin,
                                          String pkgtype,
                                          String mask,
                                          String version) throws Exception{

    StringBuffer SelSQL = new StringBuffer();
    SelSQL.append("SELECT count(1) as cnt FROM tf_bom_reroute_xrom_tx ");
    SelSQL.append("where product_body=?  ");
    SelSQL.append("and pin_count=? and package_code=? and mask_option=? and version=? ");
    PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
    ps.setString(1,pro_b);
    ps.setString(2,pin);
    ps.setString(3,pkgtype);
    ps.setString(4,mask);
    ps.setString(5,version);
    ResultSet rs = ps.executeQuery();

    while(rs.next()){
      if(rs.getInt("cnt")==0){
        return true;
      }
    }
    return false;
  }

  //copy the data from tf_prod_epn to tf_bom_route_xrom_tx
  public static boolean EPNtoBomTx(String pro_b,
                                   Connection conn,
                                   String sid,
                                   String version) {

    StringBuffer ComSql = new StringBuffer();
    String InsSQL = null;
    String maxV = String.valueOf(Integer.parseInt(version)-1);
    String productType = OiMaintainService.getProductType(sid);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("!status", "E");

      ComSql.append("select distinct product_body,brand," +
                    "mask_option backend_option, nvl(pin_count, 0) pin_count, nvl(package_type, 'NA') package_type, mask_option, sales_form from tf_prod_epn ");
      ComSql.append(SQLStem.getWhereStmt(whereStem));
      InsSQL="insert into tf_bom_route_xrom_tx (sid,tag,product_body,version," +
             "pin_count,package_code,mask_option,ft_route_code,id,route_type,body_version,mask_option_rev,code_no) " +
	     "values (?,?,?,?,?,?,?,?,test_seq.nextval,?,?,?,?)";

      PreparedStatement Comps = conn.prepareStatement(ComSql.toString());
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      ResultSet rs=Comps.executeQuery();
      String ftRouteCode = null;
      ftRouteCode = getMaxFTRouteCode(sid);

      while(rs.next()){
        //如果TF_BOM_ROUTE_XROM_TX內沒有重複的資料，就可將該筆資料加進TF_BOM_ROUTE_XROM_TX
        if (OiMaintainService.CheckExistInBomTx(conn,
                                               rs.getString("product_body"),
                                               rs.getString("pin_count"),
                                               rs.getString("package_type"),
                                               rs.getString("mask_option"),
                                               version))
        {
          for(int i = 0; i<1; i++){
             ps2.setString(1,sid);
             ps2.setString(2,"1");
             ps2.setString(3,rs.getString("product_body"));
             ps2.setString(4,version);
             ps2.setString(5,rs.getString("pin_count"));
             ps2.setString(6,rs.getString("package_type"));
             ps2.setString(7,rs.getString("mask_option"));
             ftRouteCode = getNextRouteCode(ftRouteCode);
             ps2.setString(8, ftRouteCode);
             if (i==0) ps2.setString(9, "0");//route_type=normal
             //if (i==1) ps2.setString(9, "1");//route_type=low temp
             ps2.setString(10, "*");//body_version
             ps2.setString(11, "*");//mask_option_rev
             ps2.setString(12, "****");//code_no
             ps2.executeUpdate();
          }
        } else {
          TDSLogger.println("NONO");
        }
      }
    }catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return false;
  }
  //copy the data from tf_prod_epn to tf_bom_reroute_xrom_tx
  public static boolean EPNtoBomReTx(String pro_b,
                                   Connection conn,
                                   String sid,
                                   String version) {

    StringBuffer ComSql = new StringBuffer();
    String InsSQL = null;
    String maxV = String.valueOf(Integer.parseInt(version)-1);
    String productType = OiMaintainService.getProductType(sid);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("!status", "E");

      ComSql.append("select distinct product_body,brand," +
                    "mask_option backend_option, nvl(pin_count, 0) pin_count, nvl(package_type, 'NA') package_type, mask_option, sales_form from tf_prod_epn ");
      ComSql.append(SQLStem.getWhereStmt(whereStem));
      InsSQL="insert into tf_bom_reroute_xrom_tx (sid,tag,product_body,version," +
             "pin_count,package_code,mask_option,recycle_code,id,route_type,body_version,mask_option_rev,code_no) " +
             "values (?,?,?,?,?,?,?,?,test_seq.nextval,?,?,?,?)";

      PreparedStatement Comps = conn.prepareStatement(ComSql.toString());
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      ResultSet rs=Comps.executeQuery();
      String ftRouteCode = null;
      ftRouteCode = getMaxFTRouteCode(sid);

      while(rs.next()){
        //如果TF_BOM_REROUTE_XROM_TX內沒有重複的資料，就可將該筆資料加進TF_BOM_REROUTE_XROM_TX
        if (OiMaintainService.CheckExistInBomReTx(conn,
                                               rs.getString("product_body"),
                                               rs.getString("pin_count"),
                                               rs.getString("package_type"),
                                               rs.getString("mask_option"),
                                               version))
        {
          for(int i = 0; i<4; i++){
             ps2.setString(1,sid);
             ps2.setString(2,"1");
             ps2.setString(3,rs.getString("product_body"));
             ps2.setString(4,version);
             ps2.setString(5,rs.getString("pin_count"));
             ps2.setString(6,rs.getString("package_type"));
             ps2.setString(7,rs.getString("mask_option"));
             ftRouteCode = getNextRouteCode(ftRouteCode);
             ps2.setString(8, ftRouteCode);
             if (i==0) ps2.setString(9, "0");//route_type=erase code
             if (i==1) ps2.setString(9, "1");//route_type=boot code
             if (i==2) ps2.setString(9, "2");//route_type=erase code+boot code
             if (i==3) ps2.setString(9, "3");//route_type=repair
             ps2.setString(10, "*");//body_version
             ps2.setString(11, "*");//mask_option_rev
             ps2.setString(12, "****");//code_no
             ps2.executeUpdate();
          }
        } else {
          TDSLogger.println("NONO");
        }
      }
    }catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //get the data of the given product_body and brand from tf_bom_route_xrom
  public static BomProductRouteBean[] BomRouteFirstVersion(String pro_b,
                                                           String sid,
                                                           String version) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      conn = DBConnection.getConnection();
      FTTestActionForm prod = FTService.getInfo(Integer.parseInt(sid));
   	  SelSQL.append("SELECT t.*, m.route_cat\n");
   	  SelSQL.append("FROM tf_bom_route_xrom_tx t, tf_route_master m\n");
   	  SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("\nand t.ft_route = m.route_name (+)");
   	  SelSQL.append("\norder by t.product_body,t.pin_count," +
      				"t.package_code,t.ft_route,t.mask_option ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        BomProductRouteBean bom = new BomProductRouteBean();
        bom.setTag(rs.getString("tag"));
        bom.setProductbody(rs.getString("product_body"));
        bom.setVersion (rs.getString("version"));
        bom.setBodyersion(rs.getString("body_version"));
        bom.setPincount(rs.getString("pin_count"));
        bom.setPkgtype(rs.getString("package_code"));
        bom.setFtroute(rs.getString("ft_route"));
        bom.setRoutetype(rs.getString("route_type"));
        bom.setMaskopt(rs.getString("mask_option"));
        bom.setMaskoptrev(rs.getString("mask_option_rev"));
        bom.setCodeno(rs.getString("code_no"));
        bom.setSortroutecode(rs.getString("sort_route_code"));
        bom.setWsroute(rs.getString("ws_route"));
        bom.setWsaddroute(rs.getString("ws_route_add"));
        bom.setWsaddroute1(rs.getString("ws_route_add1"));
        bom.setWsaddroute2(rs.getString("ws_route_add2"));
        bom.setWsaddroute3(rs.getString("ws_route_add3"));
        bom.setWsaddroute4(rs.getString("ws_route_add4"));
        bom.setFtcomment(rs.getString("ft_comment"));
        bom.setWscomment(rs.getString("ws_comment"));
        bom.setId(rs.getString("id"));
        bom.setFtAddroute(rs.getString("FT_ROUTE_ADD"));
        bom.setFtAddroute1(rs.getString("FT_ROUTE_ADD1"));
        bom.setFtAddroute2(rs.getString("FT_ROUTE_ADD2"));
        bom.setFtAddroute3(rs.getString("FT_ROUTE_ADD3"));
        bom.setFtAddroute4(rs.getString("FT_ROUTE_ADD4"));
        bom.setFtAddroute5(rs.getString("FT_ROUTE_ADD5"));
        bom.setFt_route_code(rs.getString("ft_route_code"));
        bom.setRoute_cat(rs.getString("route_cat"));
        tmp2.add(bom);
      }
      return (BomProductRouteBean[]) tmp2.toArray(new BomProductRouteBean[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  /* get sort route code from tf_bom_route_xrom*/
  public static BomProductRouteBean[] GetSortRouteCode_Normal(String productbody, String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", productbody);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct mask_option, sort_route_code, ws_route FROM tf_bom_route_xrom   ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      System.out.println(SelSQL.toString());
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
          BomProductRouteBean bom = new BomProductRouteBean();
          bom.setMaskopt(rs.getString("mask_option"));
          bom.setSortroutecode(rs.getString("sort_route_code"));
          bom.setWsroute(rs.getString("ws_route"));
          tmp2.add(bom);
        }
        return (BomProductRouteBean[]) tmp2.toArray(new BomProductRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  /* get sort route code from tf_bom_route_xrom_tx*/
  public static BomProductRouteBean[] GetSortRouteCodeTX_Normal(String productbody, String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", productbody);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct mask_option, sort_route_code, ws_route FROM tf_bom_route_xrom_tx   ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      //SelSQL.append(" and tag != '2' ");
      System.out.println(SelSQL.toString());
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
          BomProductRouteBean bom = new BomProductRouteBean();
          bom.setMaskopt(rs.getString("mask_option"));
          bom.setSortroutecode(rs.getString("sort_route_code"));
          bom.setWsroute(rs.getString("ws_route"));
          tmp2.add(bom);
        }
        return (BomProductRouteBean[]) tmp2.toArray(new BomProductRouteBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  
  

//get the data of the given product_body and brand from tf_bom_reroute_xrom
  public static BomProductReRouteBean[] BomReRouteFirstVersion(String pro_b,
                                                           String sid,
                                                           String version) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      conn = DBConnection.getConnection();
      FTTestActionForm prod = FTService.getInfo(Integer.parseInt(sid));
             SelSQL.append("SELECT t.* FROM tf_bom_reroute_xrom_tx t ");
             SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("order by t.product_body,t.pin_count," +
                                      "t.package_code,t.ft_route,t.mask_option ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        BomProductReRouteBean bom = new BomProductReRouteBean();
        bom.setTag(rs.getString("tag"));
        bom.setProductbody(rs.getString("product_body"));
        bom.setVersion (rs.getString("version"));
        bom.setBodyersion(rs.getString("body_version"));
        bom.setPincount(rs.getString("pin_count"));
        bom.setPkgtype(rs.getString("package_code"));
        bom.setFtroute(rs.getString("ft_route"));
        bom.setRoutetype(rs.getString("route_type"));
        bom.setMaskopt(rs.getString("mask_option"));
        bom.setMaskoptrev(rs.getString("mask_option_rev"));
        bom.setCodeno(rs.getString("code_no"));
        bom.setFtcomment(rs.getString("ft_comment"));
        bom.setId(rs.getString("id"));
        bom.setFtAddroute(rs.getString("FT_ROUTE_ADD"));
        bom.setRecycle_code(rs.getString("recycle_code"));
        tmp2.add(bom);
      }
      return (BomProductReRouteBean[]) tmp2.toArray(new BomProductReRouteBean[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  //set TF_PRODUCT_ROUTE='Y' in tf_information
  public static boolean submitProductTest(String sid) throws Exception {
    StringBuffer UpdateSQL2 = new StringBuffer();
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      UpdateSQL2.append("Update tf_information set TF_PRODUCT_ROUTE='Y' where sid=? ");
      PreparedStatement ps1 = conn.prepareStatement(UpdateSQL2.toString());
      ps1.setString(1,sid);
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    }finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //set TF_BOM_ROUTE='Y' in tf_information
  public static boolean submitBomProductRoute(String sid) throws Exception {
    StringBuffer UpdateSQL2 = new StringBuffer();
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      UpdateSQL2.append("update tf_information set TF_BOM_ROUTE='Y' where sid=? ");
      PreparedStatement ps1 = conn.prepareStatement(UpdateSQL2.toString());
      ps1.setString(1,sid);
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    }finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  //set TF_BOM_REROUTE='Y' in tf_information
  public static boolean submitBomProductReRoute(String sid) throws Exception {
    StringBuffer UpdateSQL2 = new StringBuffer();
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      UpdateSQL2.append("update tf_information set TF_BOM_REROUTE='Y' where sid=? ");
      PreparedStatement ps1 = conn.prepareStatement(UpdateSQL2.toString());
      ps1.setString(1,sid);
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    }finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }


  //set TF_TEST_PARAMETER_WS='Y' in tf_information
  public static boolean submitWSTestParameter(String sid) throws Exception {
    StringBuffer UpdateSQL2 = new StringBuffer();
    Connection conn = null;
    try {
      conn=DBConnection.getConnection();
      UpdateSQL2.append("update tf_information set TF_TEST_PARAMETER_WS='Y',TF_BASIC_INFORMATION = 'N' where sid=? ");
      PreparedStatement ps1 = conn.prepareStatement(UpdateSQL2.toString());
      ps1.setString(1,sid);
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    }finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //儲存更改過的資料到tf_bom_route_xrom_tx
  public static boolean InsertBomRoute(String[] ftroutecode,
                                       String[] tag,
                                       ProTestRouteBeanAF fm,
                                       String[] epnbody,
                                       String[] brand,
                                       String[] productbody,
                                       String[] codeno,
                                       String[] pincount,
                                       String[] pkgtype,
                                       String[] grade,
                                       String[] productclass,
                                       String[] ftwithcode,
                                       String[] maskopt,
                                       String[] maskoptrev,
                                       String[] sortroutecode,
                                       String[] wsroute,
                                       String[] wsaddroute,
                                       String[] wsaddroute1,
                                       String[] wsaddroute2,
                                       String[] wsaddroute3,
                                       String[] wsaddroute4,
                                       String[] ftcomment,
                                       String[] id,
                                       String[] ftAddroute,
                                       String[] ftAddroute1,
                                       String[] ftAddroute2,
                                       String[] ftAddroute3,
                                       String[] ftAddroute4,
                                       String[] ftAddroute5,
                                       String[] wscomment) {

    String InsSQL=null;
    Connection conn = null;

    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL="update tf_bom_route_xrom_tx set ft_route=?, sort_route_code=?, ws_route=?, " +
          "ws_route_add=?, ws_route_add1=?, ws_route_add2=?, ws_route_add3=?, ws_route_add4=?, ft_comment=?, ft_route_add=? , ft_route_add1=? ,ft_route_add2=? ,ft_route_add3=?, ft_route_add4=?, ft_route_add5=?, ft_route_code=?, ws_comment=? " +
          " where id = ?  ";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      for (int i = 0; i < id.length; i++){
            ps2.setString(1,ftwithcode[i]);
            ps2.setString(2,sortroutecode[i]);
            ps2.setString(3,wsroute[i]);
            ps2.setString(4,wsaddroute[i]);
            ps2.setString(5,wsaddroute1[i]);
            ps2.setString(6,wsaddroute2[i]);
            ps2.setString(7,wsaddroute3[i]);
            ps2.setString(8,wsaddroute4[i]);
            ps2.setString(9,StringUtil.Utf8ToBig5(ftcomment[i].trim()));
            ps2.setString(10,ftAddroute[i]);
            ps2.setString(11,ftAddroute1[i]);
            ps2.setString(12,ftAddroute2[i]);
            ps2.setString(13,ftAddroute3[i]);
            ps2.setString(14,ftAddroute4[i]);
            ps2.setString(15,ftAddroute5[i]);
            ps2.setString(16,ftroutecode[i]);
            ps2.setString(17,StringUtil.Utf8ToBig5(wscomment[i].trim()));
            ps2.setString(18,id[i]);
            ps2.executeUpdate();
      }
      conn.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  /*儲存更改過的資料到tf_bom_route_xrom_tx
   * 20090610, Robin, 更改已生效資料的 comment 是可以的，但更改後，若變動 tag=1，則頁面將可以更改 route info，這是不應該的，
   *                  因此取消原來變更 comment 會重設  tag 值為 1 的 rule 。
   
  public static boolean InsertBomRoute(String[] ftroutecode,
                                       String[] tag,
                                       ProTestRouteBeanAF fm,
                                       String[] epnbody,
                                       String[] brand,
                                       String[] productbody,
                                       String[] codeno,
                                       String[] pincount,
                                       String[] pkgtype,
                                       String[] grade,
                                       String[] productclass,
                                       String[] ftwithcode,
                                       String[] maskopt,
                                       String[] maskoptrev,
                                       String[] sortroutecode,
                                       String[] wsroute,
                                       String[] wsaddroute,
                                       String[] wsaddroute1,
                                       String[] ftcomment,
                                       String[] id,
                                       String[] ftAddroute,
                                       String[] ftAddroute1,
                                       String[] ftAddroute2,
                                       String[] wscomment) {

    String InsSQL=null;
    String InsSQL2=null;
    Connection conn = null;

    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL="update tf_bom_route_xrom_tx set ft_route=?, sort_route_code=?, ws_route=?, " +
          "ws_route_add=?, ws_route_add1=?, ft_comment=?, ft_route_add=? , ft_route_add1=? ,ft_route_add2=? ,ft_route_code=?, ws_comment=? " +
          " where id = ?  ";
      InsSQL2="update tf_bom_route_xrom_tx set ft_route=?, sort_route_code=?, ws_route=?, " +
          "ws_route_add=?, ws_route_add1=?, ft_comment=?, ft_route_add=?, ft_route_add1=?, ft_route_add2=?, tag='1',ft_route_code=?, " +
          "ws_comment=? where id = ?  ";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      PreparedStatement ps3 = conn.prepareStatement(InsSQL2);
      for (int i = 0; i < id.length; i++){
        if (tag[i].equals("0")){/* if tag of the selected data is 0 /
          /* if data with tag of 0 is changed, update the tag from 0 to 1/
          if (CheckBomTagZero(conn,id[i],ftwithcode[i],ftAddroute[i],ftAddroute1[i],ftAddroute2[i],
                              sortroutecode[i],wsroute[i],wsaddroute[i],wsaddroute1[i],
                              ftcomment[i],ftroutecode[i])){
            ps3.setString(1,ftwithcode[i]);
            ps3.setString(2,sortroutecode[i]);
            ps3.setString(3,wsroute[i]);
            ps3.setString(4,wsaddroute[i]);
            ps3.setString(5,wsaddroute1[i]);
            ps3.setString(6,StringUtil.Utf8ToBig5((ftcomment[i].trim())));
            ps3.setString(7,ftAddroute[i]);
            ps3.setString(8,ftAddroute1[i]);
            ps3.setString(9,ftAddroute2[i]);
            ps3.setString(10,ftroutecode[i]);
            ps3.setString(11,StringUtil.Utf8ToBig5(wscomment[i].trim()));
            ps3.setString(12,id[i]);
            ps3.executeUpdate();
          } else {/*if data with tag of 0 remains unchanged,no need to update tag/
            ps2.setString(1,ftwithcode[i]);
            ps2.setString(2,sortroutecode[i]);
            ps2.setString(3,wsroute[i]);
            ps2.setString(4,wsaddroute[i]);
            ps2.setString(5,wsaddroute1[i]);
            ps2.setString(6,StringUtil.Utf8ToBig5(ftcomment[i].trim()));
            ps2.setString(7,ftAddroute[i]);
            ps2.setString(8,ftAddroute1[i]);
            ps2.setString(9,ftAddroute2[i]);
            ps2.setString(10,ftroutecode[i]);
            ps2.setString(11,StringUtil.Utf8ToBig5(wscomment[i].trim()));
            ps2.setString(12,id[i]);
            ps2.executeUpdate();
          }
        } else { /*if the data with tag of 1, no need to update tag/
          ps2.setString(1, ftwithcode[i]);
          ps2.setString(2, sortroutecode[i]);
          ps2.setString(3, wsroute[i]);
          ps2.setString(4, wsaddroute[i]);
          ps2.setString(5, wsaddroute1[i]);
          ps2.setString(6, StringUtil.Utf8ToBig5(ftcomment[i].trim()));
          ps2.setString(7, ftAddroute[i]);
          ps2.setString(8, ftAddroute1[i]);
          ps2.setString(9, ftAddroute2[i]);
          ps2.setString(10, ftroutecode[i]);
          ps2.setString(11, StringUtil.Utf8ToBig5(wscomment[i].trim()));
          ps2.setString(12,id[i]);
          ps2.executeUpdate();
        }
      }
      conn.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  */
  
  //儲存更改過的資料到tf_bom_reroute_xrom_tx
  public static boolean InsertBomReRoute(String[] recyclecode,
                                       String[] tag,
                                       ProTestReRouteBeanAF fm,
                                       String[] epnbody,
                                       String[] brand,
                                       String[] productbody,
                                       String[] codeno,
                                       String[] pincount,
                                       String[] pkgtype,
                                       String[] grade,
                                       String[] productclass,
                                       String[] ftwithcode,
                                       String[] maskopt,
                                       String[] maskoptrev,
                                       String[] ftcomment,
                                       String[] id,
                                       String[] ftAddroute) {

    String InsSQL=null;
    String InsSQL2=null;
    Connection conn = null;

    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL="update tf_bom_reroute_xrom_tx set ft_route=?, " +
          "ft_comment=?, ft_route_add=? ,recycle_code=? " +
          " where id = ?  ";
      InsSQL2="update tf_bom_reroute_xrom_tx set ft_route=?, " +
          "ft_comment=?, ft_route_add=?,tag='1',recycle_code=? " +
          "where id = ?  ";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      PreparedStatement ps3 = conn.prepareStatement(InsSQL2);
      for (int i = 0; i < id.length; i++){
        if (tag[i].equals("0")){/* if tag of the selected data is 0 */
          /* if data with tag of 0 is changed, update the tag from 0 to 1*/
          if (CheckBomReTagZero(conn,id[i],ftwithcode[i],ftAddroute[i],
                              ftcomment[i],recyclecode[i])){
            ps3.setString(1,ftwithcode[i]);
            ps3.setString(2,StringUtil.Utf8ToBig5((ftcomment[i].trim())));
            ps3.setString(3,ftAddroute[i]);
            ps3.setString(4,recyclecode[i]);
            ps3.setString(5,id[i]);
            ps3.executeUpdate();
          } else {/*if data with tag of 0 remains unchanged,no need to update tag*/
            ps2.setString(1,ftwithcode[i]);
            ps2.setString(2,StringUtil.Utf8ToBig5(ftcomment[i].trim()));
            ps2.setString(3,ftAddroute[i]);
            ps2.setString(4,recyclecode[i]);
            ps2.setString(5,id[i]);
            ps2.executeUpdate();
          }
        } else { /*if the data with tag of 1, no need to update tag*/
          ps2.setString(1, ftwithcode[i]);
          ps2.setString(2, StringUtil.Utf8ToBig5(ftcomment[i].trim()));
          ps2.setString(3, ftAddroute[i]);
          ps2.setString(4, recyclecode[i]);
          ps2.setString(5,id[i]);
          ps2.executeUpdate();
        }
      }
      conn.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }


  //將user要存的tf_bom_route_xrom_tx,tag=0的資料與table內的資料比對，如果與原本的資料不同就return true
  public static boolean CheckBomTagZero(Connection conn,
                                        String id,
                                        String ft_route,
                                        String ft_route_add,
                                        String ft_route_add1,
                                        String ft_route_add2,
                                        String ft_route_add3,
                                        String ft_route_add4,
                                        String ft_route_add5,
                                        String sort_route_code,
                                        String ws_route,
                                        String ws_route_add,
                                        String ws_route_add1,
                                        String ws_route_add2,
                                        String ws_route_add3,
                                        String ws_route_add4,
                                        String tf_comment,
                                        String ftroutecode) throws Exception{

    StringBuffer SelSQL = new StringBuffer();
    SelSQL.append("SELECT count(1) as cnt FROM tf_bom_route_xrom_tx ");
    SelSQL.append(" where id= ");
    SelSQL.append(id);
    SelSQL.append(" and ft_route ");
    if (ft_route.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route + "'");
    }
    SelSQL.append(" and ft_route_add ");
    if(ft_route_add.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route_add + "'");
    }
    SelSQL.append(" and ft_route_add1 ");
    if(ft_route_add1.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route_add1 + "'");
    }
    SelSQL.append(" and ft_route_add2 ");
    if(ft_route_add2.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route_add2 + "'");
    }
    SelSQL.append(" and ft_route_add3 ");
    if(ft_route_add3.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route_add3 + "'");
    }
    SelSQL.append(" and ft_route_add4 ");
    if(ft_route_add4.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route_add4 + "'");
    }
    SelSQL.append(" and ft_route_add5 ");
    if(ft_route_add5.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route_add5 + "'");
    }
    SelSQL.append(" and sort_route_code ");
    if(sort_route_code.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + sort_route_code + "'");
    }
    SelSQL.append(" and ws_route ");
    if (ws_route.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ws_route + "'");
    }
    SelSQL.append(" and ws_route_add ");
    if (ws_route_add.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ws_route_add + "'");
    }
    SelSQL.append(" and ws_route_add1 ");
    if (ws_route_add1.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ws_route_add1 + "'");
    }
    SelSQL.append(" and ws_route_add2 ");
    if (ws_route_add2.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ws_route_add2 + "'");
    }
    SelSQL.append(" and ws_route_add3 ");
    if (ws_route_add3.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ws_route_add3 + "'");
    }
    SelSQL.append(" and ws_route_add4 ");
    if (ws_route_add4.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ws_route_add4 + "'");
    }
    SelSQL.append(" and ft_comment ");
    if (tf_comment.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + tf_comment + "'");
    }
    SelSQL.append(" and ft_route_code ");
    if (ftroutecode.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ftroutecode + "'");
    }

    PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
    ResultSet rs = ps.executeQuery();

    while(rs.next()){
      if (rs.getInt("cnt") == 0){
        return true;
      }
    }
    return false;
  }
  //將user要存的tf_bom_reroute_xrom_tx,tag=0的資料與table內的資料比對，如果與原本的資料不同就return true
  public static boolean CheckBomReTagZero(Connection conn,
                                        String id,
                                        String ft_route,
                                        String ft_route_add,
                                        String tf_comment,
                                        String recyclecode) throws Exception{

    StringBuffer SelSQL = new StringBuffer();
    SelSQL.append("SELECT count(1) as cnt FROM tf_bom_reroute_xrom_tx ");
    SelSQL.append(" where id= ");
    SelSQL.append(id);
    SelSQL.append(" and ft_route ");
    if (ft_route.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route + "'");
    }
    SelSQL.append(" and ft_route_add ");
    if(ft_route_add.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + ft_route_add + "'");
    }

    SelSQL.append(" and ft_comment ");
    if (tf_comment.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + tf_comment + "'");
    }
    SelSQL.append(" and recycle_code ");
    if (recyclecode.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + recyclecode + "'");
    }

    PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
    ResultSet rs = ps.executeQuery();

    while(rs.next()){
      if (rs.getInt("cnt") == 0){
        return true;
      }
    }
    return false;
  }

  /* Insert the duplicated row data into the tf_bom_route_xrom_tx (function of duplicate row) */
  public static boolean InsertBomDup(ProTestRouteBeanAF fm,
                                     String productbody,
                                     String pincount,
                                     String pkgtype,
                                     String maskopt,
                                     String maskoptrev,
                                     String codeno,
                                     String routetype,
                                     String sortroutecode,
                                     String wsroute,
                                     String wsaddroute,
                                     String wsaddroute1,
                                     String wsaddroute2,
                                     String wsaddroute3,
                                     String wsaddroute4,
                                     String comment,
                                     String ft_route_code,
                                     String ftroute,
                                     String ft_route_add,
                                     String ft_route_add1,
                                     String ft_route_add2,
                                     String ft_route_add3,
                                     String ft_route_add4,
                                     String ft_route_add5,
                                     String ws_comment) {
    String InsSQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      InsSQL="insert into tf_bom_route_xrom_tx (sid,tag,product_body,version,body_version," +
          "pin_count,package_code,ft_route,mask_option,mask_option_rev,code_no,route_type," +
          "sort_route_code,ws_route,ws_route_add,ws_route_add1,ws_route_add2,ws_route_add3,ws_route_add4,ft_comment,ft_route_code," +
          "ft_route_add,ft_route_add1,ft_route_add2,ft_route_add3,ft_route_add4,ft_route_add5,ws_comment, id) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval)";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      for(int i=0;i<1;i++){
        ps2.setString(1,fm.getSid());
        ps2.setString(2,"1");
        ps2.setString(3,productbody);
        ps2.setString(4,fm.getVersion());
        ps2.setString(5,fm.getBodyversion());
        ps2.setString(6,pincount);
        ps2.setString(7,pkgtype);
        ps2.setString(8,ftroute);
        ps2.setString(9,maskopt);
        ps2.setString(10,maskoptrev);
        ps2.setString(11,codeno);
        if (i==0) ps2.setString(12,"0");
        //if (i==1) ps2.setString(12,"1");
        ps2.setString(13,sortroutecode);
        //ps2.setString(13,"");
      //      ps2.setString(14,wsroute);
//      ps2.setString(15,wsaddroute);
        ps2.setString(14,wsroute);
        ps2.setString(15,wsaddroute);//wsaddroute
        ps2.setString(16,wsaddroute1);//wsaddroute1
        ps2.setString(17,wsaddroute2);//wsaddroute2
        ps2.setString(18,wsaddroute3);//wsaddroute3
        ps2.setString(19,wsaddroute4);//wsaddroute4
        ps2.setString(20,StringUtil.Utf8ToBig5(comment.trim()));
        ps2.setString(21,ft_route_code);
        ps2.setString(22,ft_route_add);//ft_route_add
        ps2.setString(23,ft_route_add1);//ft_route_add1
        ps2.setString(24,ft_route_add2);//ft_route_add2
        ps2.setString(25,ft_route_add3);//ft_route_add1
        ps2.setString(26,ft_route_add4);//ft_route_add2
        ps2.setString(27,ft_route_add5);//ft_route_add2
        ps2.setString(28,StringUtil.Utf8ToBig5(ws_comment.trim()));
        ps2.executeUpdate();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  /* Insert the duplicated row data into the tf_bom_reroute_xrom_tx (function of duplicate row) */
  public static boolean InsertBomReDup(ProTestReRouteBeanAF fm,
                                     String productbody,
                                     String pincount,
                                     String pkgtype,
                                     String maskopt,
                                     String maskoptrev,
                                     String codeno,
                                     String routetype,
                                     String comment,
                                     String recycle_code,
                                     String ftroute,
                                     String ft_route_add) {
    String InsSQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      InsSQL="insert into tf_bom_reroute_xrom_tx (sid,tag,product_body,version,body_version," +
          "pin_count,package_code,ft_route,mask_option,mask_option_rev,code_no,route_type," +
          "ft_comment,recycle_code," +
          "ft_route_add, id) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval)";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      for(int i=0;i<4;i++){
        ps2.setString(1,fm.getSid());
        ps2.setString(2,"1");
        ps2.setString(3,productbody);
        ps2.setString(4,fm.getVersion());
        ps2.setString(5,fm.getBodyversion());
        ps2.setString(6,pincount);
        ps2.setString(7,pkgtype);
        ps2.setString(8,ftroute);
        ps2.setString(9,maskopt);
        ps2.setString(10,maskoptrev);
        ps2.setString(11,codeno);
        if (i==0) ps2.setString(12,"0");
        if (i==1) ps2.setString(12,"1");
        if (i==2) ps2.setString(12,"2");
        if (i==3) ps2.setString(12,"3");
        ps2.setString(13,StringUtil.Utf8ToBig5(comment.trim()));
        ps2.setString(14,recycle_code);
        ps2.setString(15,ft_route_add);
        ps2.executeUpdate();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  /* check if the duplicated row already existed in the tf_bom_route_xrom table */
  public static boolean CheckExistBomDup(String sid,
                                         String body_version,
                                         String pin_count,
                                         String package_code,
                                         String mask_option,
                                         String mask_option_rev,
                                         String code_no,
                                         String route_type,
                                         String sort_route_code,
                                         String ft_route_code) {

    String SQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();

      SQL = "select count(1) as cnt from tf_bom_route_xrom_tx " +
          "where sid=? and body_version=? and pin_count=? " +
          " and package_code=? and mask_option=? and mask_option_rev=? and code_no=? and sort_route_code=?  " +
          " and ft_route_code=? ";

      PreparedStatement ps2 = conn.prepareStatement(SQL);
      ps2.setString(1, sid);
      ps2.setString(2,body_version);
      ps2.setString(3,pin_count);
      ps2.setString(4,package_code);
      ps2.setString(5,mask_option);
      ps2.setString(6,mask_option_rev);
      ps2.setString(7,code_no);
      ps2.setString(8,sort_route_code);
      ps2.setString(9,ft_route_code);
      ResultSet rs=ps2.executeQuery();
      if (rs != null){
        if (rs.next()){
          if (rs.getInt("cnt") > 0){
            return true;
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  /* check if the duplicated row already existed in the tf_bom_reroute_table */
  public static boolean CheckExistBomReDup(String sid,
                                         String body_version,
                                         String pin_count,
                                         String package_code,
                                         String mask_option,
                                         String mask_option_rev,
                                         String code_no,
                                         String route_type,
                                         String recycle_code) {

    String SQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();

      SQL = "select count(1) as cnt from tf_bom_reroute_xrom_tx " +
          "where sid=? and body_version=? and pin_count=? " +
          " and package_code=? and mask_option=? and mask_option_rev=? and code_no=?  " +
          " and recycle_code=? ";

      PreparedStatement ps2 = conn.prepareStatement(SQL);
      ps2.setString(1, sid);
      ps2.setString(2,body_version);
      ps2.setString(3,pin_count);
      ps2.setString(4,package_code);
      ps2.setString(5,mask_option);
      ps2.setString(6,mask_option_rev);
      ps2.setString(7,code_no);
      ps2.setString(8,recycle_code);
      ResultSet rs=ps2.executeQuery();
      if (rs != null){
        if (rs.next()){
          if (rs.getInt("cnt") > 0){
            return true;
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //copy the data from tf_test_parameter_ws to tf_test_parameter_ws_tx
  public static boolean WsToWsTx(String pro_b,
                                 String br,
                                 String sid,
                                 String version) {

    StringBuffer Sql = new StringBuffer();
    String InsSQL = null;
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    try {
      HashMap whereStem = new HashMap();
      conn = DBConnection.getConnection();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", br);
      Sql.append("insert into tf_test_parameter_ws_tx "+
                 "(sid,tag,pgm_id,product_body,brand,version,mask_option,test_type," +
                 "tester,site,program_name,tf_comment,temperature,id,hw_configure,pgm_special_control,one_main_pgm_group_version) ");
      Sql.append("select " + sid + ",'0',pgm_id,product_body,brand," + version +
                 ",mask_option,test_type,tester,site,program_name,tf_comment,temperature," +
                 "test_seq.nextval,hw_configure,pgm_special_control,one_main_pgm_group_version ");
      Sql.append("from tf_test_parameter_ws ");
      Sql.append(SQLStem.getWhereStmt(whereStem));
      Sql.append("and version= ");
      Sql.append(maxV);
      PreparedStatement ps = conn.prepareStatement(Sql.toString());
      ps.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  public static boolean ChkPGToWsTx(String pro_b,
                                    String br,
                                    String sid,
                                    String version) {

    StringBuffer Sql = new StringBuffer();
    String InsSQL = null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      Sql.append("select a.program_mode,SUBSTR(program_name,6,1) as mask,a.program_id," +
                 "a.product_code,a.test_mode,a.version,a.program_name,a.tester_type," +
                 "a.subsystem_type,a.package_type,a.pin_count,a.program_status,b.*,c.* ");
      Sql.append("from pg_test_program a, pg_plant_release b, ba_plant c " +
                 "where a.sid=b.pg_sid and b.plant_no=c.plant_no ");
      Sql.append(" and a.program_status not in ('53')");
      Sql.append(" and substr(product_code,1,4)=?");
      PreparedStatement ps = conn.prepareStatement(Sql.toString());

      ps.setString(1,pro_b);
      ResultSet rs=ps.executeQuery();

      while (rs.next()) {
        return true;
      }

    }catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  // NO USE ???
  public static boolean PGToWsTx(String pro_b,
                                 String br,
                                 String sid,
                                 String version) {

    StringBuffer Sql = new StringBuffer();
    String InsSQL = null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      Sql.append("select a.program_mode,SUBSTR(program_name,6,1) as mask,a.program_id," +
                 "a.product_code,a.test_mode,a.version,a.program_name,a.tester_type," +
                 "a.subsystem_type,a.package_type,a.pin_count,a.program_status,b.*,c.* ");
      Sql.append("from pg_test_program a, pg_plant_release b, ba_plant c " +
                 "where a.sid=b.pg_sid and b.plant_no=c.plant_no ");
      Sql.append(" and a.program_status not in ('53')");
      Sql.append(" and substr(product_code,1,4)=?");
      TDSLogger.println(Sql.toString());
      PreparedStatement ps = conn.prepareStatement(Sql.toString());
      ps.setString(1,pro_b);

      ResultSet rs = ps.executeQuery();
      InsSQL = "insert into tf_test_parameter_ws_tx (sid,tag,pgm_id,product_body," +
          "brand,version,mask_option,test_type,tester,site,program_name,id) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval)";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);

      while (rs.next()) {
        ps2.setString(1, sid);
        ps2.setString(2, "1");
        ps2.setString(3, rs.getString("program_id"));
        ps2.setString(4, pro_b);
        ps2.setString(5, br);
        ps2.setString(6, version);
        ps2.setString(7, rs.getString("mask"));
        ps2.setString(8, rs.getString("test_mode"));
        ps2.setString(9, rs.getString("tester_type"));
        ps2.setString(10, rs.getString("plant_name"));
        ps2.setString(11, rs.getString("program_name"));
        ps2.executeUpdate();
      }
      conn.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //insert the data that were selected from "Add From PGM" into tf_test_parameter_ws_tx
  public static boolean CopyToWsTx(ProTestRouteBeanAF fm,
                                   String[] chkbx2,
                                   String[] pgm_id,
                                   String[] mask_option,
                                   String[] test_type,
                                   String[] tester,
                                   String[] site,
                                   String[] program_name,
                                   String[] pgm_special_control,
                                   String[] one_main_pgm_group_version,
                                   String pro_b) {
    String InsSQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL="insert into tf_test_parameter_ws_tx (sid,tag,pgm_id,product_body,brand," +
          "version,mask_option,test_type,tester,site,program_name,id,hw_configure,tf_comment,temperature,pgm_special_control,one_main_pgm_group_version) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval,?,?,?,?,?)";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      for (int i = 0; i < chkbx2.length; i++){
        String Fir[] = StringUtil.parse2StringsStr(chkbx2[i],"#");
        int k=StringUtil.formatInt(Fir[1]);
        String previous_program_id = OiMaintainService.GetProviousProgramId(Fir[0]);
        String previous_hw_configure = OiMaintainService.GetHWConfigureBySidProgramId("tf_test_parameter_ws_tx",fm.getSid(),previous_program_id);
        WsTestBean[] wtb = com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService.GetTemperatureBySidProgramId("tf_test_parameter_ws_tx",fm.getSid(),Fir[0]);
        ps2.setString(1, fm.getSid());
        ps2.setString(2, "1");
        ps2.setString(3, Fir[0]);
        ps2.setString(4, fm.getProductbody());
        ps2.setString(5, fm.getBrand());
        ps2.setString(6, fm.getVersion());
        ps2.setString(7, StringUtil.FormatData3(mask_option[k]));
        ps2.setString(8, StringUtil.FormatData3(test_type[k]));
        ps2.setString(9, StringUtil.FormatData3(tester[k]));
        ps2.setString(10,StringUtil.FormatData3(site[k]));
        ps2.setString(11,StringUtil.FormatData3(program_name[k]));
        ps2.setString(12,previous_hw_configure);
        if(wtb.length >0 && wtb[0] != null && wtb[0].getHw_configure() != null){
        	ps2.setString(12,wtb[0].getHw_configure());
        }else{
        	if(wtb.length ==0)
        		ps2.setString(12,previous_hw_configure);
        	else
        		ps2.setString(12,"");
        }
        if(wtb.length >0 && wtb[0] != null && wtb[0].getTf_comment() != null){
        	ps2.setString(13,wtb[0].getTf_comment());
        }else{
        	ps2.setString(13,"");
        }	
        if(wtb.length >0 && wtb[0] != null && wtb[0].getTemperature() != null)
        	ps2.setString(14,wtb[0].getTemperature());
        else
        	ps2.setString(14,"");
        ps2.setString(15,StringUtil.FormatData3(pgm_special_control[k]));
        ps2.setString(16,StringUtil.FormatData3(one_main_pgm_group_version[k]));
        ps2.executeUpdate();
      }
      conn.commit();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //delete the selected row of data from tf_bom_route_tx
  public static boolean DeleteBomRouteTx(String id) throws Exception {
    StringBuffer DelSQL = new StringBuffer();
    Connection conn = null;
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("id", id);
      conn = DBConnection.getConnection();
      DelSQL.append("delete from tf_bom_route_xrom_tx ");
      DelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
//delete the selected row of data from tf_bom_reroute_tx
  public static boolean DeleteBomReRouteTx(String id) throws Exception {
    StringBuffer DelSQL = new StringBuffer();
    Connection conn = null;
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("id", id);
      conn = DBConnection.getConnection();
      DelSQL.append("delete from tf_bom_reroute_xrom_tx ");
      DelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //delete the selected row of data from tf_bom_route_tx
  public static boolean setBomRouteExpire(String id, String flag) throws Exception {
    StringBuffer DelSQL = new StringBuffer();
    Connection conn = null;
    try {
      //HashMap whereStem = new HashMap();
      //whereStem.put("id", id);
      conn = DBConnection.getConnection();
      DelSQL.append("update tf_bom_route_xrom_tx set tag = '" + flag + "' ");
      //DelSQL.append(SQLStem.getWhereStmt(whereStem));
      DelSQL.append("where id in ( " + id + " ) ");
      TDSLogger.println(DelSQL.toString());
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  //delete the selected row of data from tf_bom_reroute_tx
  public static boolean setBomReRouteExpire(String id, String flag) throws Exception {
    StringBuffer DelSQL = new StringBuffer();
    Connection conn = null;
    try {
      //HashMap whereStem = new HashMap();
      //whereStem.put("id", id);
      conn = DBConnection.getConnection();
      DelSQL.append("update tf_bom_reroute_xrom_tx set tag = '" + flag + "' ");
      //DelSQL.append(SQLStem.getWhereStmt(whereStem));
      DelSQL.append("where id in ( " + id + " ) ");
      TDSLogger.println(DelSQL.toString());
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //delete the data of the given sid(product_body and brand) in tf_bom_route_xrom_tx
  // and set  tf_bom_route='N' in tf_information
  public static boolean ResetBomRouteTx(String sid) throws Exception {
    String sql=null;
    String upt=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      sql = "delete from tf_bom_route_xrom_tx where sid=? ";
      upt = "update tf_information set tf_bom_route='N' where sid=?";
      PreparedStatement ps1 = conn.prepareStatement(sql);
      PreparedStatement ps2 = conn.prepareStatement(upt);
      ps1.setString(1,sid);
      ps1.executeUpdate();
      ps2.setString(1,sid);
      ps2.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  //delete the data of the given sid(product_body and brand) in tf_bom_route_xrom_tx
  // and set  tf_bom_route='N' in tf_information
  public static boolean ResetBomReRouteTx(String sid) throws Exception {
    String sql=null;
    String upt=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      sql = "delete from tf_bom_reroute_xrom_tx where sid=? ";
      upt = "update tf_information set tf_bom_reroute='N' where sid=?";
      PreparedStatement ps1 = conn.prepareStatement(sql);
      PreparedStatement ps2 = conn.prepareStatement(upt);
      ps1.setString(1,sid);
      ps1.executeUpdate();
      ps2.setString(1,sid);
      ps2.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }


  //updtae the data of the given product_body and brand in tf_test_parameter_ws_tx
  public static boolean UpdateWSParameter(String sid,
                                          String[] step_name,
                                          String[] pgm_id,
                                          String[] temp,
                                          String[] tf_comment,
                                          String[] site,
                                          String[] hw_configure) throws Exception {

    StringBuffer UptSQL = new StringBuffer();
    StringBuffer DelSQL = new StringBuffer();
    StringBuffer J=new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      UptSQL.append("update tf_test_parameter_ws_tx ");
      UptSQL.append("set temperature=?, tf_comment=?, hw_configure=? where pgm_id=? and sid = ?");
      PreparedStatement ps1 = conn.prepareStatement(UptSQL.toString());

      for (i = 0; i < pgm_id.length; i++){
    	DelSQL.delete(0, DelSQL.length());
      	DelSQL.append("delete from tf_test_parameter_ws_tx ");
        DelSQL.append("where sid = ? ");
        DelSQL.append("and pgm_id = ? ");
        DelSQL.append("and site not in ('" + site[i].trim().replaceAll(";", "','")+"')");
        PreparedStatement ps0 = conn.prepareStatement(DelSQL.toString());
        TDSLogger.println(DelSQL.toString());
      	TDSLogger.println(sid+";"+pgm_id[i].trim());
      	ps0.setString(1,sid);
        ps0.setString(2,StringUtil.Utf8ToBig5(pgm_id[i].trim()));
        //ps0.setString(3,StringUtil.Utf8ToBig5(site[i].trim()));
        ps0.executeUpdate();
          
        ps1.setString(1,temp[i]);
        ps1.setString(2,StringUtil.Utf8ToBig5(tf_comment[i].trim()));
        ps1.setString(3,StringUtil.Utf8ToBig5(hw_configure[i].trim()));
        ps1.setString(4,pgm_id[i]);
        ps1.setString(5,sid);
        ps1.executeUpdate();
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //delete the selected row data from tf_test_parameter_ws_tx
  public static boolean DeleteWSParameter(String sid, String pgm_id) throws Exception {
    StringBuffer DelSQL = new StringBuffer();
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      DelSQL.append("delete from tf_test_parameter_ws_tx ");
      DelSQL.append("where pgm_id in ("+pgm_id+") and sid = ?");
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      ps1.setString(1,sid);
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //delete the data of the given sid(product_body and brand) in tf_test_parameter_ws_tx
  // and set  tf_test_parameter_ws='N' in tf_information
  public static boolean ResetWSParameter(String sid) throws Exception {
    StringBuffer DelSQL = new StringBuffer();
    String upt = null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      upt = "update tf_information set tf_test_parameter_ws='N' where sid=? ";
      DelSQL.append("delete from tf_test_parameter_ws_tx where sid=? ");
      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
      PreparedStatement ps2 = conn.prepareStatement(upt);
      ps1.setString(1,sid);
      ps1.executeUpdate();
      ps2.setString(1,sid);
      ps2.executeUpdate();
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //This function is for "Duplicate Row": get the data that selected by the user and put them in a bean
  public static BomProductRouteBean[] SearchBomByID(String id) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("id", id);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_bom_route_xrom_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        BomProductRouteBean bom = new BomProductRouteBean();
        bom.setTag(rs.getString("tag"));
        bom.setSid(rs.getString("sid"));
        bom.setVersion(rs.getString("version"));
        bom.setBodyersion (rs.getString("body_version"));
        bom.setProductbody(rs.getString("product_body"));
        bom.setPincount(rs.getString("pin_count"));
        bom.setPkgtype(rs.getString("package_code"));
        bom.setFtroute(rs.getString("ft_route"));
        bom.setRoutetype(rs.getString("route_type"));
        bom.setMaskopt(rs.getString("mask_option"));
        bom.setMaskoptrev(rs.getString("mask_option_rev"));
        bom.setCodeno(rs.getString("code_no"));
        bom.setSortroutecode(rs.getString("sort_route_code"));
        bom.setWsroute(rs.getString("ws_route"));
        bom.setWsaddroute(rs.getString("ws_route_add"));
        bom.setFtcomment(rs.getString("ft_comment"));
        bom.setWscomment(rs.getString("ws_comment"));
        bom.setId(rs.getString("id"));
        bom.setFt_route_code(rs.getString("ft_route_code"));
        tmp2.add(bom);
      }
      return (BomProductRouteBean[]) tmp2.toArray(new BomProductRouteBean[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  //This function is for "Duplicate Row": get the data that selected by the user and put them in a bean
  public static BomProductReRouteBean[] SearchBomReByID(String id) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("id", id);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_bom_reroute_xrom_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        BomProductReRouteBean bom = new BomProductReRouteBean();
        bom.setTag(rs.getString("tag"));
        bom.setSid(rs.getString("sid"));
        bom.setVersion(rs.getString("version"));
        bom.setBodyersion (rs.getString("body_version"));
        bom.setProductbody(rs.getString("product_body"));
        bom.setPincount(rs.getString("pin_count"));
        bom.setPkgtype(rs.getString("package_code"));
        bom.setFtroute(rs.getString("ft_route"));
        bom.setRoutetype(rs.getString("route_type"));
        bom.setMaskopt(rs.getString("mask_option"));
        bom.setMaskoptrev(rs.getString("mask_option_rev"));
        bom.setCodeno(rs.getString("code_no"));
        bom.setFtcomment(rs.getString("ft_comment"));
        bom.setId(rs.getString("id"));
        bom.setRecycle_code(rs.getString("recycle_code"));
        tmp2.add(bom);
      }
      return (BomProductReRouteBean[]) tmp2.toArray(new BomProductReRouteBean[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  //get the data of the given product body and brand from tf_test_parameter_ws_tx
  public static WsTestBean[] SelectAllFromWS(String sid,
                                             Connection conn,
                                             String pro_b,
                                             String br,
                                             String version) {

    StringBuffer SelSQL = new StringBuffer();
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      SelSQL.append(                    
    		  "SELECT DISTINCT SID,\n" +
              "       GET_WSFT_OI_PGMID_VENDOR_LIST(SID, PGM_ID, '', 'tag', 'TF_TEST_PARAMETER_WS_TX') TAG,\n" +
              "       PGM_ID,\n" + 
              "       PRODUCT_BODY,\n" + 
              "       BRAND,\n" + 
              "       VERSION,\n" + 
              "       MASK_OPTION,\n" + 
              "       TEST_TYPE,\n" + 
              "       TESTER,\n" + 
              "       GET_WSFT_OI_PGMID_VENDOR_LIST(SID, PGM_ID, '', 'site', 'TF_TEST_PARAMETER_WS_TX') SITE,\n" + 
              "       PROGRAM_NAME,\n" + 
              "       PGM_SPECIAL_CONTROL,\n" + 
              "       ONE_MAIN_PGM_GROUP_VERSION,\n" +
              "       TF_COMMENT,\n" + 
              //"       ID,\n" + 
              "       NVL(TF_TEST_PARAMETER_WS_TX.TEMPERATURE,\n" + 
              //"			  DECODE((SELECT VERSION FROM PG_TEST_PROGRAM WHERE PROGRAM_MODE ='PROD'\n" +
              //"         		   AND PROGRAM_ID = TF_TEST_PARAMETER_WS_TX.PGM_ID),'A',\n" +
              "               DECODE((SELECT BB.PREVIOUS_PROGRAM_ID\n" +
              "                                  FROM PG_TEST_PROGRAM AA, PG_PREVIOUS_PROGRAM BB \n" + 
              "                                 WHERE AA.PROGRAM_MODE = BB.PROGRAM_MODE AND AA.PROGRAM_MODE = 'PROD' \n" + 
              "                                   AND AA.PROGRAM_ID = BB.PROGRAM_ID\n" + 
              "                                   AND AA.PROGRAM_ID = TF_TEST_PARAMETER_WS_TX.PGM_ID\n" + 
              "                                   AND BB.CATEGORY IN (1,2)),null,\n" +
              "     		   	 (SELECT B.TEMP FROM GPRS_BA_PRODUCT A, GPRS_BA_PGM_TEMP B, PG_TEST_PROGRAM C\n" + 
              "		       		   WHERE A.PROCESS = B.PROCESS AND A.FUNCTION = B.FUNCTION\n" + 
              "			             AND A.TYPE = B.TYPE AND A.FAMILY_CODE = B.FAMILY_CODE\n" + 
              "         			 AND C.PROGRAM_MODE='PROD'\n" + 
              "		        	     AND C.PROGRAM_ID = TF_TEST_PARAMETER_WS_TX.PGM_ID\n" + 
              "         		   	 AND C.TEST_MODE = B.TEST_MODE\n" + 
              "         		   	 AND A.PRODUCT_CODE = C.PRODUCT_CODE),\n" + 
              "					 (SELECT DISTINCT B.TEMPERATURE FROM TF_TEST_PARAMETER_WS B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = TF_TEST_PARAMETER_WS_TX.PRODUCT_BODY AND B.BRAND = TF_TEST_PARAMETER_WS_TX.BRAND AND B.VERSION = TF_TEST_PARAMETER_WS_TX.VERSION -1  AND TF_TEST_PARAMETER_WS_TX.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD'))) AS TEMPERATURE,\n" + 
              "       NVL(TF_TEST_PARAMETER_WS_TX.HW_CONFIGURE,(SELECT DISTINCT B.HW_CONFIGURE FROM TF_TEST_PARAMETER_WS B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = TF_TEST_PARAMETER_WS_TX.PRODUCT_BODY AND B.BRAND = TF_TEST_PARAMETER_WS_TX.BRAND AND B.VERSION = TF_TEST_PARAMETER_WS_TX.VERSION -1  AND TF_TEST_PARAMETER_WS_TX.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD')) AS HW_CONFIGURE\n" + 
              "  FROM TF_TEST_PARAMETER_WS_TX " );
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("order by mask_option,test_type,tester,site,program_name ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        WsTestBean wtb = new WsTestBean();
        wtb.setSid(sid);
        //wtb.setId(rs.getString("id"));
        wtb.setTag(rs.getString("tag"));
        wtb.setPgm_id(rs.getString("pgm_id"));
        wtb.setMask_option(rs.getString("mask_option"));
        wtb.setTest_type(rs.getString("test_type"));
        wtb.setTemperature(rs.getString("temperature"));
        wtb.setTester(rs.getString("tester"));
        wtb.setSite(rs.getString("site"));
        wtb.setProgram_name(rs.getString("program_name"));
        wtb.setTf_comment(rs.getString("tf_comment"));
        wtb.setHw_configure(rs.getString("hw_configure"));
        wtb.setPgm_special_control(rs.getString("pgm_special_control"));
        wtb.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
        Vector temperatureList = null;
/*
        temperatureList = getTemperatureFromProductRoute(pro_b, br, version, wtb.getTest_type());
        if (wtb.getTemperature() != null && (!wtb.getTemperature().equals(" "))) {
        	if (!temperatureList.contains(wtb.getTemperature()))
        		temperatureList.add(wtb.getTemperature());
        }
*/
        wtb.setTemperatureList(temperatureList);
        tmp2.add(wtb);
      }
      return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      //20140604DBConnection.close(conn);
      //20140604conn = null;
    }
    return null;
  }

  public static WsTestBean[] SelectAllFromWSwtConn(String sid,
                                                   String pro_b,
                                                   String br) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      conn=DBConnection.getConnection();
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      SelSQL.append("SELECT * FROM tf_test_parameter_ws_tx ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("order by mask_option,test_type,tester,site,program_name ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        WsTestBean wtb = new WsTestBean();
        wtb.setId(rs.getString("id"));
        wtb.setTag(rs.getString("tag"));
        wtb.setPgm_id(rs.getString("pgm_id"));
        wtb.setMask_option(rs.getString("mask_option"));
        wtb.setTest_type(rs.getString("test_type"));
        wtb.setTemperature(rs.getString("temperature"));
        wtb.setTester(rs.getString("tester"));
        wtb.setSite(rs.getString("site"));
        wtb.setProgram_name(rs.getString("program_name"));
        wtb.setTf_comment(rs.getString("tf_comment"));
        wtb.setHw_configure(rs.getString("hw_configure"));
        tmp2.add(wtb);
      }
      return (WsTestBean[]) tmp2.toArray(new wstestbean2[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  //check if the route name is already defined in tf_product_route_tx
  public static boolean CheckDefineRoute(Connection conn,
                                         String sid,
                                         String step_name) throws Exception{

    String sql = "Select count(1) as cnt from tf_product_route_tx where sid=? and step_name=? ";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, sid);
    ps.setString(2, step_name);
    ResultSet rs = ps.executeQuery();
    if (rs != null){
      if (rs.next()){
        if (rs.getInt("cnt") > 0){
          return true;
        }
      }
    }
    return false;
  }

  //get all the available mask options taht have a status of "buy-off" from pg_test_program for the given product_body
  public static wtbean[] GetAvailableMaskOption(String pro_b, String pgmflag) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      conn=DBConnection.getConnection();
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      /*if(pgmflag == null || pgmflag.equals("buyoff"))
        whereStem.put("program_status", "53");
      else if(pgmflag.equals("release"))
      	whereStem.put("program_status", "54");*/  
      whereStem.put("program_mode", "PROD");
      SelSQL.append("SELECT distinct SUBSTR(product_code,5,1) as mask FROM pg_test_program ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      if(pgmflag == null || pgmflag.equals("buyoff"))
    	  SelSQL.append("and program_status = '53' ");
      else if(pgmflag.equals("release"))
    	  SelSQL.append("and program_status in ('54', '64') ");
      SelSQL.append("and substr(product_code,1,4)= ");
      SelSQL.append("'"+pro_b+"' ");
      SelSQL.append("and substr(test_mode,1,1)='S' ");
      TDSLogger.println(SelSQL.toString());
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        wtbean wtb = new wtbean();
        wtb.setMask_option(rs.getString("mask"));
        tmp2.add(wtb);
      }
      return (wtbean[]) tmp2.toArray(new wtbean[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }


  //get the data from pg_test_program,pg_plant_release, and ba_plant for given product_body
  //and mask_option
  public static WsTestBean[] GetPGMByMaskOption(String pro_b,
                                                String[] chkbx,
                                                String sid,
                                                String pgmflag) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      conn=DBConnection.getConnection();
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("#a.sid", "b.pg_sid");
      whereStem.put("#b.plant_no", "c.plant_no");
      whereStem.put("program_mode","PROD");
      /*if(pgmflag == null || pgmflag.equals("buyoff"))
        whereStem.put("program_status","53");
      else if(pgmflag.equals("release"))
        whereStem.put("program_status", "54");*/  
      whereStem.put("SUBSTR(product_code,5,1)",StringUtil.FormatData(chkbx,","));
      whereStem.put("substr(product_code,1,4)",pro_b);
      SelSQL.append("select a.program_mode,SUBSTR(product_code,5,1) as mask,substr(test_mode,1,1),a.program_id,a.product_code,a.test_mode,a.version,a.program_name,a.tester_type,a.subsystem_type,a.package_type,a.pin_count,a.program_status,DECODE(A.sesq_option,'Y','KGD QT Flag=Y;','') pgm_special_control,a.one_main_pgm_group_version,b.*,c.* ");
      SelSQL.append(",GET_8049_ONE_MAIN_PGM_NUM(a.one_main_pgm_group_version, '" + pgmflag + "') one_main_pgm_num " );
      SelSQL.append("from pg_test_program  a, pg_plant_release  b, ba_plant  c ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      if(pgmflag == null || pgmflag.equals("buyoff"))
    	  SelSQL.append("and program_status = '53' ");
      else if(pgmflag.equals("release"))
    	  SelSQL.append("and program_status in ('54', '64') ");
      SelSQL.append("and substr(test_mode,1,1)='S' ");
      TDSLogger.println(SelSQL.toString());
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        if (CheckExistWS_TX(conn, sid,
                            rs.getString("program_id"),
                            rs.getString("mask"),
                            rs.getString("test_mode"),
                            rs.getString("tester_type"),
                            rs.getString("plant_name"),
                            rs.getString("program_name"),
                            rs.getString("pgm_special_control"),
                            rs.getString("one_main_pgm_group_version")))
        {
          TDSLogger.println("Duplicate!!!");
        } else {
          WsTestBean wtb = new WsTestBean();
          wtb.setPgm_id(rs.getString("program_id"));
          wtb.setMask_option(rs.getString("mask"));
          wtb.setTest_type(rs.getString("test_mode"));
          wtb.setTester(rs.getString("tester_type"));
          wtb.setSite(rs.getString("plant_name"));
          wtb.setProgram_name(rs.getString("program_name"));
          wtb.setPgm_special_control(rs.getString("pgm_special_control"));
          wtb.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
          wtb.setOne_main_pgm_num(rs.getString("one_main_pgm_num"));
          tmp2.add(wtb);
        }
      }
      return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  //將USER從add_from_pgm所選的data與已存在於tf_test_parameter_ws_tx中的data比對，如有一樣的return true
  public static boolean CheckExistWS_TX(Connection conn,
                                        String sid,
                                        String pgm_id,
                                        String mask,
                                        String test_mode,
                                        String tester,
                                        String plant,
                                        String pgm_name,
                                        String pgm_special_control,
                                        String one_main_pgm_group_version) throws Exception{

    String sql = "select count(1) as cnt from tf_test_parameter_ws_tx " +
        "where sid=? and pgm_id=? and mask_option=? and test_type=? " +
        "and tester=? and site=? and program_name=? ";//and pgm_special_control=? and one_main_pgm_group_version=?
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1,sid);
    ps.setString(2,pgm_id);
    ps.setString(3,mask);
    ps.setString(4,test_mode);
    ps.setString(5,tester);
    ps.setString(6,plant);
    ps.setString(7,pgm_name);
    //ps.setString(8,pgm_special_control);
    //ps.setString(9,one_main_pgm_group_version);
    ResultSet rs = ps.executeQuery();
    if (rs != null){
      if (rs.next()){
        if (rs.getInt("cnt") > 0){
          return true;
        }
      }
    }
    return false;
  }

  /*選擇所有VENDOR(site)，要STATUS是54,64的，去除已出現在TF_Test_PARAMETER_WS_TX裡面的*/
  public static wtbean[] VendorList(String mask_option,
                                    String test_type,
                                    String tester,
                                    String program_name,
                                    String pgm_id,
                                    String sid) {

    StringBuffer SelSQL = new StringBuffer();
    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    StringBuffer s = new StringBuffer();
    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      String getSiteName = "select * from tf_test_parameter_ws_tx " +
          "where sid=? and program_name=? and mask_option=? and test_type=? and tester=? ";
      PreparedStatement psSite = conn.prepareStatement(getSiteName);
      psSite.setString(1,sid);
      psSite.setString(2,program_name);
      psSite.setString(3,mask_option);
      psSite.setString(4,test_type);
      psSite.setString(5,tester);
      ResultSet rsSite=psSite.executeQuery();
      while(rsSite.next()){
        s.append("'"+rsSite.getString("site")+"'"+",");
      }
      String range=StringUtil.SqlInRange(s.toString());

      sql.append("select distinct plant_name " +
                 "from pg_test_program a, pg_plant_release b, ba_plant c " +
                 "where SUBSTR(program_name,6,1)= ");
      sql.append("'"+mask_option+"'");
      sql.append(" and test_mode= ");
      sql.append("'"+test_type+"'");
      sql.append(" and tester_type= ");
      sql.append("'"+tester+"'");
      sql.append(" and program_name= ");
      sql.append("'"+program_name+"'");
      sql.append(" and program_mode='PROD' and program_status  in('54','64') ");
      sql.append(" and a.sid=b.pg_sid and b.plant_no=c.plant_no and plant_name not in(");
      sql.append(range+") ");
      TDSLogger.println(sql.toString());
      PreparedStatement ps2 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps2.executeQuery();
      while(rs.next()){
        wtbean wtb = new wtbean();
        wtb.setSite(rs.getString("plant_name"));
        tmp2.add(wtb);
      }
      return (wtbean[]) tmp2.toArray(new wtbean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  //將user選的該筆tf_test_parameter_ws_tx的資料放入bean裡
  public static WsTestBean[] RowDataSelected(String pgm_id) {
    StringBuffer SelSQL = new StringBuffer();
    StringBuffer sql=new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("pgm_id", pgm_id);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_test_parameter_ws_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        WsTestBean wtb = new WsTestBean();
        wtb.setSid(rs.getString("sid"));
        wtb.setPgm_id(rs.getString("pgm_id"));
        wtb.setProduct_body(rs.getString("product_body"));
        wtb.setBrand(rs.getString("brand"));
        wtb.setVersion(rs.getString("version"));
        wtb.setMask_option(rs.getString("mask_option"));
        wtb.setTest_type(rs.getString("test_type"));
        wtb.setTester(rs.getString("tester"));
        wtb.setSite(rs.getString("site"));
        wtb.setProgram_name(rs.getString("program_name"));
        wtb.setTemperature(rs.getString("temperature"));
        wtb.setTf_comment(rs.getString("tf_comment"));
        wtb.setHw_configure(rs.getString("hw_configure"));
        wtb.setPgm_special_control(rs.getString("pgm_special_control"));
        wtb.setOne_main_pgm_group_version(rs.getString("one_main_pgm_group_version"));
        tmp2.add(wtb);
      }
      return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  /* show all the vendors name(site) of specific product/brand/version */
  public static WsTestBean[] VendorName(String sid) {
    StringBuffer SelSQL = new StringBuffer();
    StringBuffer sql=new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT DISTINCT v.site, p.tim_short_name FROM " +
                    "(SELECT DISTINCT site FROM tf_test_parameter_ws ");
      SelSQL.append(" WHERE sid = "+sid);
      SelSQL.append(" UNION SELECT DISTINCT site FROM tf_test_parameter_ft ");
      SelSQL.append(" WHERE sid = "+sid);
      SelSQL.append(" UNION SELECT DISTINCT site FROM tf_test_parameter_pbc ");
      SelSQL.append(" WHERE sid = "+sid+") v, ba_plant p ");
      SelSQL.append("WHERE v.site = p.plant_name and p.plant_no > 10");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        WsTestBean wtb = new WsTestBean();
        wtb.setSite(rs.getString("site"));
        wtb.setSite_short_name(rs.getString("tim_short_name").toLowerCase());
        tmp2.add(wtb);
      }
      return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  //將user從add vendor 功能中所選的vendor與user選的該筆資料存入tf_test_parameter_ws_tx
  public static boolean CopyVendorToWsTx(String[] chk1,
                                         String sid,
                                         String pgm_id,
                                         String product_body,
                                         String brand,
                                         String version,
                                         String mask_option,
                                         String test_type,
                                         String tester,
                                         String site,
                                         String program_name,
                                         String hw_configure,
                                         String pgm_special_control,
                                         String tf_comment,
                                         String temperature,
                                         String one_main_pgm_group_version) {
    String InsSQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      InsSQL = "insert into tf_test_parameter_ws_tx " +
          "(sid,tag,pgm_id,product_body,brand,version,mask_option,test_type,tester," +
          "site,program_name,id,hw_configure,pgm_special_control,tf_comment,temperature,one_main_pgm_group_version) values (?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval,?,?,?,?,?)";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      for(int i = 0; i < chk1.length; i++){
        //String previous_program_id = OiMaintainService.GetProviousProgramId(pgm_id); 
    	//String previous_hw_configure = OiMaintainService.GetHWConfigureBySidProgramId("tf_test_parameter_ws_tx",sid,pgm_id);  
        ps2.setString(1, sid);
        ps2.setString(2, "1");
        ps2.setString(3, pgm_id);
        ps2.setString(4, product_body);
        ps2.setString(5, brand);
        ps2.setString(6, version);
        ps2.setString(7, mask_option);
        ps2.setString(8, test_type);
        ps2.setString(9, tester);
        ps2.setString(10,chk1[i]);
        ps2.setString(11,program_name);
        ps2.setString(12,hw_configure);
        ps2.setString(13,pgm_special_control);
        ps2.setString(14,tf_comment);
        ps2.setString(15,temperature);
        ps2.setString(16,one_main_pgm_group_version);
        ps2.executeUpdate();
      }
    }catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  public static String getPreviousVersionSid(String sid) {
    String previousSid = null;
    StringBuffer sqlStmt = new StringBuffer();
    sqlStmt.append("SELECT T1.SID FROM TF_INFORMATION T1, TF_INFORMATION T2 ");
    sqlStmt.append("WHERE T1.PRODUCT_BODY = T2.PRODUCT_BODY AND T1.BRAND = T2.BRAND ");
    sqlStmt.append("AND T1. VERSION = T2.VERSION - 1 AND T2.SID = " + sid);

    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        previousSid = rs.getString("SID");
        return previousSid;
      }
    } catch (Exception e) {
      TDSLogger.println(e);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }

  public static String getProductType(String sid) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT PRODUCT_TYPE FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE SID = " + sid);

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("PRODUCT_TYPE");
			  break;
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
  
	public static String CheckExistTestParameter(String sid) {

		StringBuffer sqlStmt = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		sqlStmt.append(
                "(SELECT DISTINCT TEST_TYPE\n" +
                "  FROM TF_TEST_PARAMETER_WS_TX\n" + 
                " WHERE SID = ?\n" + 
                "UNION ALL\n" + 
                "SELECT DISTINCT TEST_TYPE\n" + 
                "  FROM TF_TEST_PARAMETER_FT_TX\n" + 
                " WHERE SID = ?)\n" + 
                "MINUS\n" + 
                "(SELECT DISTINCT REPLACE(TESTER, 'SORT', 'S') TEST_TYPE \n" + 
                "  FROM TF_BASIC_INFO_TX\n" + 
                " WHERE SID = ?)");
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
			ps.setString(1, sid);
			ps.setString(2, sid);
			ps.setString(3, sid);
			ResultSet rs = ps.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {

				if (rs.getString("TEST_TYPE").startsWith("S")) {
					tmp.append("漏設" + rs.getString("TEST_TYPE") + "的Bin 設定 ,請補定. ( 因 Step 6 : WS Test Parameter Information 有" + rs.getString("TEST_TYPE") + " pgm)\\n");

				} else {
					tmp.append("漏設" + rs.getString("TEST_TYPE") + "的Bin 設定 ,請補定. ( 因 Step 7 : FT Test Parameter Information 有" + rs.getString("TEST_TYPE") + " pgm)\\n");
				}
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		if (tmp.length() > 0)
			return "\\n" + tmp.toString();
		else
			return "";

	}

  public static String getProductType(String productBody, String brand, String version) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT PRODUCT_TYPE FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE PRODUCT_BODY = '" + productBody + "' ");
	  sqlStmt.append("AND BRAND = '" + brand + "' AND VERSION = '" + version + "'");

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("PRODUCT_TYPE");
			  break;
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }

  public static Vector getTemperatureFromProductRoute(String productBody, String brand, String version, String testMode) {
	  Vector result = new Vector();
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT DISTINCT TEMPERATURE FROM TF_PRODUCT_ROUTE_TX ");
	  sqlStmt.append("WHERE PRODUCT_BODY = '" + productBody + "' ");
	  sqlStmt.append("AND BRAND = '" + brand + "' ");
	  sqlStmt.append("AND VERSION = '" + version + "' ");
	  if (testMode.charAt(0) == 'S') {
		  sqlStmt.append("AND SUBSTR(STEP_NAME, 1, 1) || SUBSTR(STEP_NAME, 5, 1) = '" + testMode + "' ");
	  } else if (testMode.charAt(0) == 'F') {
		  sqlStmt.append("AND STEP_NAME = '" + testMode + "' ");
	  }
	  sqlStmt.append("ORDER BY TEMPERATURE");

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  if (rs.getString("TEMPERATURE") != null)
				  result.add(rs.getString("TEMPERATURE"));
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }

  public static Vector getTemperatureList() {
          Vector result = new Vector();
          StringBuffer sqlStmt = new StringBuffer();
          sqlStmt.append("SELECT DESCRIPTION TEMPERATURE FROM TF_DESCRIPTION ");
          sqlStmt.append("WHERE TAG = 2 AND DELETE_FLAG IS NULL ");
          sqlStmt.append("ORDER BY TEMPERATURE");

          Connection conn = null;
          try {
                  conn = DBConnection.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
                  ResultSet rs = ps.executeQuery();

                  while (rs.next()) {
                          if (rs.getString("TEMPERATURE") != null)
                                  result.add(rs.getString("TEMPERATURE"));
                  }
          } catch (Exception e) {
                  TDSLogger.println(e);
          } finally {
                  DBConnection.close(conn);
                  conn = null;
          }
          return result;
  }
  public static Vector getTemperatureList(String tester) {
      Vector result = new Vector();
      StringBuffer sqlStmtTester = new StringBuffer();
      sqlStmtTester.append("SELECT COUNT(*) NUM  FROM TF_DESCRIPTION ");
      sqlStmtTester.append("WHERE TAG = 62 AND DELETE_FLAG IS NULL AND DESCRIPTION = ?");
  
      
      StringBuffer sqlStmt = new StringBuffer();
      sqlStmt.append("SELECT DESCRIPTION TEMPERATURE FROM TF_DESCRIPTION ");
      sqlStmt.append("WHERE TAG = 2 AND DELETE_FLAG IS NULL ");
      sqlStmt.append("ORDER BY TEMPERATURE");

      Connection conn = null;
      try {
              boolean flag = false;
    	      conn = DBConnection.getConnection();
              PreparedStatement ps_tester = conn.prepareStatement(sqlStmtTester.toString());
              ps_tester.setString(1, tester);
              ResultSet rs_tester = ps_tester.executeQuery();

              while (rs_tester.next()) {
                  if (rs_tester.getInt("NUM") > 0)
                     flag = true;
              }
              
              PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
              ResultSet rs = ps.executeQuery();

              while (rs.next()) {
                      if (rs.getString("TEMPERATURE") != null){
                    	  if(flag){
                    		  if(rs.getString("TEMPERATURE").equals("ROOM TEMP")){
                    			  result.add(rs.getString("TEMPERATURE"));
                    		  }	  
                    	  }else{
                    		  result.add(rs.getString("TEMPERATURE"));
                    	  }
                      }        
              }
      } catch (Exception e) {
              TDSLogger.println(e);
      } finally {
              DBConnection.close(conn);
              conn = null;
      }
      return result;
}

  public static String getTemperatureOption(Vector temperatureList, String temperature, int flag) {
	  StringBuffer result = new StringBuffer();
	  TreeSet tmp1 = new TreeSet();
	  TreeSet tmp2 = new TreeSet();
	  if (flag == 1)
		  tmp2.add("NA");
	  if ((temperatureList != null) && (temperatureList.size() > 0)) {
		  for (int i=0; i<temperatureList.size(); i++) {
			  String tempStr = (String)temperatureList.get(i);
			  try {
				  Integer tempInt = new Integer(tempStr);
				  tmp1.add(tempInt);
			  } catch (NumberFormatException e) {
				  tmp2.add(tempStr);
			  }
		  }
		  Iterator key = tmp1.iterator();
		  while (key.hasNext()) {
			  Integer tempInt = (Integer)key.next();
			  String tempStr = tempInt.toString();
			  if (tempStr.equals(temperature))
				  result.append("<OPTION VALUE=\"" +  tempStr + "\" SELECTED>" + tempStr + "</OPTION>\n");
			  else
				  result.append("<OPTION VALUE=\"" +  tempStr + "\">" + tempStr + "</OPTION>\n");
		  }

		  key = tmp2.iterator();
		  while (key.hasNext()) {
			  String tempStr = (String)key.next();
			  if ((temperature == null) && tempStr.equals(" "))
				  result.append("<OPTION VALUE=\"" +  tempStr + "\" SELECTED>" + tempStr + "</OPTION>\n");
			  else if (tempStr.equals(temperature))
				  result.append("<OPTION VALUE=\"" +  tempStr + "\" SELECTED>" + tempStr + "</OPTION>\n");
			  else
				  result.append("<OPTION VALUE=\"" +  tempStr + "\">" + tempStr + "</OPTION>\n");
		  }
	  }
	  return result.toString();
  }

  public static String getVendorShortName(String vendor) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT TIM_SHORT_NAME FROM BA_PLANT ");
	  sqlStmt.append("WHERE PLANT_NAME = '" + vendor + "'");

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("TIM_SHORT_NAME");
			  break;
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
  public static String[] getEcrEffectTime(String product_body, String version, String brand) {
	  String result[] = new String[2];
	  result[0]="";
	  result[1]="";
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT to_char(EFFECTIVEDATE,'yyyymmdd hh24miss')||'000' EFFECTIVEDATE, GET_DUEDAY(TO_CHAR(EFFECTIVEDATE, 'YYYY-MM-DD'),2) - TO_DATE(EFFECTIVEDATE) DAYS ");
	  sqlStmt.append(",to_char(EFFECTIVEDATE + (GET_DUEDAY(TO_CHAR(EFFECTIVEDATE, 'YYYY-MM-DD'),2) - TO_DATE(EFFECTIVEDATE)),'yyyymmdd hh24miss' )||'000' BUFFERTIME ");
	  sqlStmt.append("  FROM IF_TF_COVERPAGE ");
	  if(brand.equals("MX"))
	      sqlStmt.append(" WHERE DOCNO ='8049-" + product_body + "' ");
	  if(brand.equals("KH"))
	      sqlStmt.append(" WHERE DOCNO ='8049K-" + product_body + "' ");
	  sqlStmt.append("   AND STATUS ='生效' ");
	  sqlStmt.append("   AND REV ='" + version + "'");

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  TDSLogger.println(sqlStmt.toString());
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result[0] = rs.getString("EFFECTIVEDATE");
			  result[1] = rs.getString("BUFFERTIME");
			  break;
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
  
  public static String getSplitString(String data) {
	  StringBuffer result = new StringBuffer();
	  if(data!=null){
	    String dataArray[] = data.split(";");
	    for (int i=0; i<dataArray.length; i++) {
		    if(!dataArray[i].equals("NA")){
			    result.append(dataArray[i]+"<br>");
		    }else{
			    result.append("");
		    }
	    }
	  }else{
		  result.append("");
	  }
	  TDSLogger.println(result.toString());  
	  return result.toString();
  }

  public static String[] getTestModes( String wstype ) {
          Connection conn = null;

          try {
                  StringBuffer sql = new StringBuffer();

                  if (wstype.equals("SORT"))
                    sql.append("SELECT decode(substr(test_mode,1,1),'S','SORT'||substr(test_mode,2),test_mode) TEST_MODE FROM BA_TEST_MODE ");
                  else
                    sql.append("SELECT TEST_MODE FROM BA_TEST_MODE ");
                  sql.append("WHERE FACILITY < 3 ORDER BY FACILITY,TEST_MODE ");
                  conn = DBConnection.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sql.toString());
                  ResultSet rs = ps.executeQuery();
                  ArrayList tmp = new ArrayList();
                  while (rs.next()) {
                          String testerType = rs.getString("TEST_MODE");
                          tmp.add(testerType);
                  }
                  if (tmp.isEmpty()) {
                          return null;
                  } else {
                          return (String[]) tmp.toArray(new String[0]);
                  }
          } catch (Exception ex) {
                  ex.fillInStackTrace();
                  TDSLogger.println(ex.getMessage());
                  // return null;
          } finally {
                  DBConnection.close(conn);
                  conn = null;
          }
          return null;
  }

  public static String[] getDescription(String Tag) {
          Connection conn = null;

          try {
                  StringBuffer sql = new StringBuffer();

                  sql.append("SELECT description FROM TF_DESCRIPTION ");
                  sql.append("WHERE TAG = " + Tag + " ORDER BY ID ");
                  conn = DBConnection.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sql.toString());
                  ResultSet rs = ps.executeQuery();
                  ArrayList tmp = new ArrayList();
                  while (rs.next()) {
                          String DESCRIPTION = rs.getString("DESCRIPTION");
                          tmp.add(DESCRIPTION);
                  }
                  if (tmp.isEmpty()) {
                          return null;
                  } else {
                          return (String[]) tmp.toArray(new String[0]);
                  }
          } catch (Exception ex) {
                  ex.fillInStackTrace();
                  TDSLogger.println(ex.getMessage());
                  // return null;
          } finally {
                  DBConnection.close(conn);
                  conn = null;
          }
          return null;
  }

  public static String[] getBIN_Type() {
    return getDescription("11");
  }

  public static String[] getIPN_Action() {
    return getDescription("12");
  }

  public static String[] getEPN_Speed() {
    return getDescription("13");
  }

  public static String[] getTest_Speed() {
    return getDescription("14");
  }

  public static String getGroupKey(String productType, String product_body, String body_version, String option, String option_rev, String package_code,
                  String pin_count, String code_no, String route, int facility) {
          String curGroupKey = null;
          String brev = null, orev = null;
          if (body_version.equals("*")) brev = ""; else brev = body_version;
          if (option_rev.equals("*")) orev = ""; else orev = option_rev;
//          if ((add_route == null) || add_route.equals("null")) add_route = "";
          if (facility == 0) {
                  curGroupKey = product_body + brev + option + orev +  route;
          } else {
                  curGroupKey = product_body + brev + option + orev + pin_count + package_code + route;
          }
          return curGroupKey;
  }

  public static String getSpeedList(String product_body, String brand, String version, String route, String add_route, String table, String facility) {
          String speedList = "";
          StringBuffer sqlStmt = new StringBuffer();
          sqlStmt.append("select distinct a.epn_speed ");
          sqlStmt.append(" from tf_basic_info" + table + " a");
          sqlStmt.append(" where a.tester in ");
          sqlStmt.append(" (select distinct decode(substr(test_mode,1,1),'S','SORT'||substr(test_mode,2),test_mode) test_mode");
          sqlStmt.append(" from tf_route_master_"+facility+"_view b");
          sqlStmt.append(" where b.route_name in ('"+route+"','"+add_route+"'))");
          sqlStmt.append(" and a.product_body = '" + product_body );
          sqlStmt.append("' and a.brand = '" + brand );
          sqlStmt.append("' and a.version = " + version );
          sqlStmt.append(" and a.epn_speed is not null order by epn_speed");

          Connection conn = null;
          try {
                  conn = DBConnection.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
                  ResultSet rs = ps.executeQuery();

                  while (rs.next()) {
                    if (!speedList.equals(""))
                      speedList = speedList + ",";
                    speedList = speedList + rs.getString("EPN_SPEED");
                  }
          } catch (Exception e) {
                  TDSLogger.println(e);
          } finally {
                  DBConnection.close(conn);
                  conn = null;
          }
          return speedList;
  }

  public static String getNextRouteCode(String oldCode) {
	  StringBuffer result = new StringBuffer();
	  if (oldCode == null)
		  return "10";
	  if (oldCode.equals("ZZ"))
		  return null;
          if (oldCode.equals("MZ"))
              oldCode = "NZ";
	  char firstCode = oldCode.charAt(0);
	  char secondCode = oldCode.charAt(1);
	  if (secondCode != 'Z') {
		  if (secondCode == '9')
			  secondCode = 'A';
		  else
			  secondCode = (char)(secondCode + 1);
	  }
	  else {
		  secondCode = '0';
		  if (firstCode == '9')
			  firstCode = 'A';
		  else
			  firstCode = (char)(firstCode + 1);
	  }
	  result.append(firstCode);
	  result.append(secondCode);
	  return result.toString();
  }

  public static String getMaxFTRouteCode(String sid) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT MAX(FT_ROUTE_CODE) FT_ROUTE_CODE FROM TF_BOM_ROUTE_XROM_TX ");
	  sqlStmt.append("WHERE SID = " + sid);

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("FT_ROUTE_CODE");
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
  public static String getMaxReRouteCode(String sid) {
          String result = null;
          StringBuffer sqlStmt = new StringBuffer();
          sqlStmt.append("SELECT MAX(RECYCLE_CODE) RECYCLE_CODE FROM TF_BOM_REROUTE_XROM_TX ");
          sqlStmt.append("WHERE SID = " + sid);

          Connection conn = null;
          try {
                  conn = DBConnection.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
                  ResultSet rs = ps.executeQuery();

                  while (rs.next()) {
                          result = rs.getString("RECYCLE_CODE");
                  }
          } catch (Exception e) {
                  TDSLogger.println(e);
          } finally {
                  DBConnection.close(conn);
                  conn = null;
          }
          return result;
  }
  public static String GetProviousProgramId(String pgm_id) {
	    StringBuffer SelSQL = new StringBuffer();
	    Connection conn = null;
	    String previous_program_id = "";
	    try {
	      conn=DBConnection.getConnection();
	      ArrayList tmp2 = new ArrayList();
	      HashMap whereStem = new HashMap();
	      whereStem.put("program_id", pgm_id);
	      SelSQL.append("SELECT previous_program_id FROM Pg_Previous_Program ");
	      SelSQL.append(SQLStem.getWhereStmt(whereStem));
	      TDSLogger.println(SelSQL.toString());
	      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
	      ResultSet rs = ps.executeQuery();

	      while (rs.next()) {
	        previous_program_id = rs.getString("PREVIOUS_PROGRAM_ID");
	      }
	      return previous_program_id;
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	    } finally {
	      DBConnection.close(conn);
	      conn = null;
	    }
	    return null;
	  }
	  
	  public static String GetHWConfigureBySidProgramId(String table, String sid,
			  String pgm_id) {
		    StringBuffer SelSQL = new StringBuffer();
		    Connection conn = null;
		    String hw_configure = "NA";
		    try {
		      conn=DBConnection.getConnection();
		      HashMap whereStem = new HashMap();
		      whereStem.put("sid", sid);
		      whereStem.put("pgm_id", pgm_id);
		      SelSQL.append("SELECT hw_configure FROM " + table );
		      SelSQL.append(SQLStem.getWhereStmt(whereStem));
		      TDSLogger.println(SelSQL.toString());
		      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
		      ResultSet rs = ps.executeQuery();

		      while (rs.next()) {
		    	  hw_configure = rs.getString("HW_CONFIGURE");
		      }
		      return hw_configure;
		    }
		    catch (Exception ex) {
		      ex.printStackTrace();
		    } finally {
		      DBConnection.close(conn);
		      conn = null;
		    }
		    return null;
		  }
	  
	// Get column length for JSP control
	  public static int getColumnLength(String table_name,
	                                	String column_name) {

	    StringBuffer sql = new StringBuffer();
	    Connection conn = null;
	    try {
	      conn=DBConnection.getConnection();
	      sql.append("select nvl(data_precision,data_length) data_length from ALL_TAB_COLUMNS\n" + 
	    		  	 "where table_name = ? and column_name = ?");
	      PreparedStatement ps = conn.prepareStatement(sql.toString());
	      ps.setString(1,table_name);
	      ps.setString(2,column_name);
	      ResultSet rs=ps.executeQuery();
	      while(rs.next()){
	    	  int length = Integer.parseInt(rs.getString("data_length"));
	    	  rs.close();
	    	  ps.close();
	    	  rs = null;
	    	  ps = null;
	    	  return length;
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      DBConnection.close(conn);
	      conn = null;
	    }
	    return -1;
	  }
	  public static WsTestBean[] GetTemperatureBySidProgramId(String table, String sid,
			  String pgm_id) {
		    StringBuffer SelSQL = new StringBuffer();
		    Connection conn = null;
		    String temperature = "NA";
		    try {
		      conn=DBConnection.getConnection();
		      ArrayList tmp2 = new ArrayList();
		      HashMap whereStem = new HashMap();
		      whereStem.put("sid", sid);
		      whereStem.put("pgm_id", pgm_id);
		      SelSQL.append("SELECT nvl(temperature,'') temperature, nvl(hw_configure,'') hw_configure, nvl(tf_comment,'') tf_comment FROM " + table );
		      SelSQL.append(SQLStem.getWhereStmt(whereStem));
		      TDSLogger.println(SelSQL.toString());
		      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
		      ResultSet rs = ps.executeQuery();

		      while (rs.next()) {
		    	  WsTestBean wtb = new WsTestBean();
		    	  wtb.setTemperature(rs.getString("TEMPERATURE"));
		    	  wtb.setHw_configure(rs.getString("hw_configure"));
		    	  wtb.setTf_comment(rs.getString("tf_comment"));
		    	  tmp2.add(wtb);
		      }
		      return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
		    }
		    catch (Exception ex) {
		      ex.printStackTrace();
		    } finally {
		      DBConnection.close(conn);
		      conn = null;
		    }
		    return null;
}
  
}