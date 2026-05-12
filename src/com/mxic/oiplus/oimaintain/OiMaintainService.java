package com.mxic.oiplus.oimaintain;


import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.auto.common.DB;
import com.mxic.tdsplus.common.BADescriptionListForm;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.util.TDSLogger;

public class OiMaintainService {
  public OiMaintainService() {
  }

  public static String getSid(String productBody, String brand, String version) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT SID FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE PRODUCT_BODY = '" + productBody + "' ");
	  sqlStmt.append("AND BRAND = '" + brand + "' AND VERSION = '" + version + "'");

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("SID");
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
  public static String getBrand(String sid) {
	  String result = null;
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			getBrand(conn, sid);
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return result;
	}
  
  
  public static String getBrand(Connection conn, String sid) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT BRAND FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE SID = '" + sid + "' ");

	  //Connection conn = null;
	  try {
		  //conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("BRAND");
			  break;
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		 // DBConnection.close(conn);
		 // conn = null;
	  }
	  return result;
  }
  public static String getSidRelease(String productBody, String brand) {
      String result = null;
      StringBuffer sqlStmt = new StringBuffer();
      sqlStmt.append("SELECT SID FROM TF_CURRENT_VERSION_VW ");
      sqlStmt.append("WHERE PRODUCT_BODY = '" + productBody + "' ");
      sqlStmt.append("AND BRAND = '" + brand + "'");

      Connection conn = null;
      try {
          conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              result = rs.getString("SID");
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

  //check if any data in the given table
  //proc_type: WS/FT for yield definition table , GPAT Definition table 
  public static boolean isEmpry(String pro_b,
                                String brand,
                                String version,
                                String table_name,
                                String proc_type) throws Exception {

    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("b.product_body", pro_b);
      whereStem.put("b.brand", brand);
      whereStem.put("b.version",version);
      if (!proc_type.equals(""))
          whereStem.put("a.facility",(proc_type.equals("WS")?"0":"1"));
      sql.append("select 1 from " + table_name + " a, tf_information b\n");
      sql.append(SQLStem.getWhereStmt(whereStem));
      sql.append("\nand a.sid = b.sid\n");
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs=ps.executeQuery();
      while(rs.next()){
    	  rs.close();
    	  ps.close();
    	  rs = null;
    	  ps = null;
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

  //set submit flag
    public static boolean submit(String sid, String section) {
        return submit(sid, section, "Y");
    }
  //set submit flag
  public static boolean submit(String sid,
                               String section, String flag) {

    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    boolean result = true;
    try {
      conn=DBConnection.getConnection();
      sql.append("update tf_information set "+section+"= '"+flag+"'\n");
      sql.append("where sid = "+sid);
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ps.executeUpdate();
   	  ps.close();
   	  ps = null;
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return result;
  }

  //reset submit flag
  public static boolean unSubmit(String sid,
                               String section) {

    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    boolean result = true;
    try {
      conn=DBConnection.getConnection();
      sql.append("update tf_information set "+section+"= 'N'\n");
      sql.append("where sid = "+sid);
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ps.executeUpdate();
   	  ps.close();
   	  ps = null;
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return result;
  }

	public static OiMaintainStep SearchFunction(String sid) {
		Connection conn = null;
		OiMaintainStep oi = null;
		try {
			conn = DBConnection.getConnection();
			oi = SearchFunction(conn,sid);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return oi;
	}

  /*get all the steps by sid*/
  public static OiMaintainStep SearchFunction(Connection conn, String sid) {
    StringBuffer SelSQL = new StringBuffer();
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);

      //conn = DBConnection.getConnection();
      //SelSQL.append("SELECT * FROM tf_information  ");
      SelSQL.append("SELECT * FROM (SELECT a.*, b.package_component\n") //20151231-- 'S' as package_component
    		 .append("               FROM tf_information a, (select distinct product_body, brand, package_component from TF_PROD_EPN) b\n") //20151231, package_component 
    		 .append("               WHERE a.product_body = b.product_body(+) and a.brand = b.brand(+))");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();
      OiMaintainStep ois = null;

      while (rs.next()) {
    	ois = new OiMaintainStep();
        ois.setStatus(rs.getString("status"));
        ois.setSid(rs.getString("sid"));
        ois.setBrand(rs.getString("brand"));
        ois.setVersion(rs.getString("version"));
        ois.setProduct_body(rs.getString("product_body"));
        ois.setTf_prod_waferlevel(rs.getString("tf_prod_waferlevel"));
        ois.setTf_product_route(rs.getString("tf_product_route"));
        ois.setTf_cp_test_step(rs.getString("tf_cp_test_step"));
        ois.setTf_bom_route(rs.getString("tf_bom_route"));
        ois.setTf_bom_mcp_route(rs.getString("tf_bom_mcp_route"));
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
        ois.setProduct_type(rs.getString("product_type"));
        ois.setPackage_component(rs.getString("package_component"));
        break;
      }
      return ois;
    }catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      //DBConnection.close(conn);
     // conn = null;
    }
    return null;
  }

  public static OiMaintainStep[] SearchFunction2(String version,String br,String pro_b) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("version", version);
      whereStem.put("brand", br);
      whereStem.put("product_body", pro_b);
      conn = DBConnection.getConnection();
      //SelSQL.append("SELECT * FROM tf_information  ");
      SelSQL.append("SELECT * FROM (SELECT a.*, b.package_component\n") //20151231-- 'S' as package_component
		 .append("               FROM tf_information a, (select distinct product_body, brand, package_component from TF_PROD_EPN) b\n") //20151231, package_component 
		 .append("               WHERE a.product_body = b.product_body(+) and a.brand = b.brand(+))");
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
        ois.setTf_yield_ws(rs.getString("tf_yield_ws"));
        ois.setTf_yield_ft(rs.getString("tf_yield_ft"));
        ois.setTf_document_linkage(rs.getString("tf_document_linkage"));
        ois.setTf_test_parameter_pbc(rs.getString("tf_test_parameter_pbc"));
        ois.setTf_wip_control(rs.getString("tf_wip_control"));
        ois.setProduct_type(rs.getString("product_type"));
        ois.setPackage_component(rs.getString("package_component"));
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
      ps1.executeUpdate();
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
  /*get all the RouteCat selection for drop down boxes in jsp*/
  public static TempBean[] QueryRouteCat() {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_description where tag='21' order by id asc ");
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

  /*get all the temperature selection for drop down boxes in jsp*/
  public static TempBean[] QueryTemperature() {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_description where tag='2' AND DELETE_FLAG IS NULL  order by id asc ");
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

  /*get all rework steps from tf_description for jsp*/
  public static String[] QueryReworkStep() {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_description where tag='42' order by id asc ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
    	String tcb = rs.getString("description");
        tmp2.add(tcb);
      }
      return (String[]) tmp2.toArray(new String[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  /*get admin from tf_description for jsp*/
  public static String[] QueryAdmin() {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_description where tag='501' order by id asc ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
    	String tcb = rs.getString("description");
        tmp2.add(tcb);
      }
      return (String[]) tmp2.toArray(new String[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  /*get all sampling cond from tf_description for jsp*/
  public static String[] QuerySamplingCond() {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT * FROM tf_description where tag='52' order by id asc ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
    	String tcb = rs.getString("description");
        tmp2.add(tcb);
      }
      return (String[]) tmp2.toArray(new String[0]);
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
        trb.setVersion(rs.getString("version"));
        trb.setSid(rs.getString("sid"));
        trb.setTesttime(rs.getString("test_time"));
        trb.setTimeunit(rs.getString("time_unit"));
        trb.setRemark(rs.getString("remark"));
        trb.setRoutename(rs.getString("route_name"));
        trb.setStepname(rs.getString("step_name"));
        trb.setStepseq(rs.getString("step_seq"));
        trb.setTemperature(rs.getString("temperature"));
        trb.setSamplingtest(rs.getString("sampling_test"));
        trb.setSamplingcond(rs.getString("sampling_cond"));
        trb.setTag(rs.getString("tag"));
        trb.setReworkstep(rs.getString("rework_step"));
        trb.setTesttime2(rs.getString("test_time2"));
        trb.setTimeunit2(rs.getString("time_unit2"));
        trb.setQcactualmode(rs.getString("qc_actual_mode"));
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

    public static ProTestRouteBean[] GetTestStepDef(String sid) {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            return GetTestStepDef(conn, sid);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
        return null;
    }
  
    /* 帶出PRODUCT VS ROUTE資料的主要程式 */
    public static ProTestRouteBean[] GetTestStepDef(Connection conn, String sid) {

        try {
            ArrayList tmp2 = new ArrayList();
            HashMap tmp1 = new HashMap();

            String sql = "SELECT STEP_NAME, STEP_DEF\n" 
                    + "  FROM tf_product_route_tx\n" + " where sid = ?\n"
                    + "   AND STEP_NAME  LIKE ('SORT%') \n" 
                    + " group by STEP_NAME, STEP_DEF order by STEP_NAME ";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sid);
            ResultSet rs = ps.executeQuery();
            
            
            
            while (rs.next()) {
                ProTestRouteBean trb = new ProTestRouteBean();
                trb.setStepname(rs.getString("STEP_NAME"));
                trb.setStep_def(rs.getString("STEP_DEF"));
                if(tmp1.containsKey(trb.getStepname())) {  //有重複的資料筆
                    ProTestRouteBean trb_tmp = (ProTestRouteBean)tmp1.get(trb.getStepname());
                    if(trb_tmp.getStep_def() == null || trb_tmp.getStep_def().length() == 0) {  //之前資料的定義是空值
                        if(trb.getStep_def() != null && trb.getStep_def().length() > 0) {  //且這筆有值
                            tmp2.remove(trb_tmp);
                            tmp1.put(trb.getStepname(), trb);
                            tmp2.add(trb);
                        } 
                    } else {
                        
                    }

                } else {
                    tmp1.put(trb.getStepname(), trb);
                    tmp2.add(trb);
                }
            }
            
/*            Set set = tmp1.entrySet();
            
            Iterator iterator = set.iterator();
            
            
            while (iterator.hasNext()) {
                Map.Entry mapentry = (Map.Entry) iterator.next();
                tmp2.add((ProTestRouteBean)mapentry.getValue());
            }*/
            
/*            String j = "";
            while (rs.next()) {
                ProTestRouteBean trb = new ProTestRouteBean();
                trb.setStepname(rs.getString("STEP_NAME"));
                trb.setStep_def(rs.getString("STEP_DEF"));
                if(j.indexOf(rs.getString("STEP_NAME")+",") != -1){
                    trb.setStep_def("");
                }
                j+=rs.getString("STEP_NAME");
                j+=",";
                tmp2.add(trb);
            }*/
           
            //return (ProTestRouteBean[]) new ArrayList(Arrays.asList( h1.values().iterator())).toArray(new ProTestRouteBean[0]);
            return (ProTestRouteBean[]) tmp2.toArray(new ProTestRouteBean[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        return null;
    }

  //check if any earlier version of the product body and brand
  public static boolean CheckExistProductRoute(String pro_b,
                                               String brand,
                                               String version) throws Exception {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
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
                                             String br,
                                             String sid,
                                             String version) {

    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV = String.valueOf(Integer.parseInt(version)-1);
    try {
      conn = DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body",pro_b);
      whereStem.put("brand",br);
      whereStem.put("version", maxV);
      SqlStmt.append("insert into tf_product_route_tx " +
                     "(sid,tag,product_body,brand,version,route_name,step_seq," +
                     "step_name,temperature,sampling_test,test_time,time_unit,remark,rework_step,test_time2,time_unit2,qc_actual_mode,step_def,sampling_cond) " +
                     "select " + sid + ",0,product_body,brand," + version +
                     ",route_name,step_seq,step_name,temperature,sampling_test,test_time,time_unit,remark,rework_step,test_time2,time_unit2,qc_actual_mode,step_def,sampling_cond " +
                     "FROM tf_product_route  ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
      ps.executeUpdate();
      
      submit(sid, "TF_YIELD_WS","N");
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
//        trb.setRemark(StringUtil.Utf8ToBig5(rs.getString("remark")));
        trb.setRemark(rs.getString("remark"));
        trb.setRoutename(rs.getString("route_name"));
        trb.setStepname(rs.getString("step_name"));
        trb.setStepseq(rs.getString("step_seq"));
        trb.setTemperature(rs.getString("temperature"));
        trb.setSamplingtest(rs.getString("sampling_test"));
        trb.setSamplingcond(rs.getString("sampling_cond"));
        trb.setReworkstep(rs.getString("rework_step"));
        trb.setTesttime2(rs.getString("test_time2"));
        trb.setTimeunit2(rs.getString("time_unit2"));
        trb.setQcactualmode(rs.getString("qc_actual_mode"));
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
      String upt2 = "Update tf_product_route_tx set test_time=?,time_unit=?,temperature=?,sampling_test=? "+
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
                                        HashMap txtReworkstep,
                                        HashMap txtTesttime2,
                                        HashMap txtTimeunit2,
                                        HashMap txtqc,
                                        String[] txtRemark,
                                        HashMap txtSamplingtest,
                                        HashMap txtSamplingcond) throws Exception {
    StringBuffer UpdateSQL1 = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      UpdateSQL1.append("Update tf_product_route_tx set remark=?,test_time=?,time_unit=?,temperature=?,sampling_test=?, \n"+
      					"rework_step=?, test_time2=?, time_unit2=?, QC_ACTUAL_MODE=?,sampling_cond=?\n " +
  						"where sid=? and route_name=? and step_seq=? ");
      PreparedStatement ps1 = conn.prepareStatement(UpdateSQL1.toString());
      /* update all the columns except test_time, time_unit,remark*/
      for (i = 0; i < seq.length; i++) {
      	String timeStr = "";
    	String unitStr = "";
    	String tempStr = "";
    	String samplingtestStr = "";
    	String samplingcondStr = "";
    	String reworkStepStr = "";
    	String time2Str = "";
    	String unit2Str = "";
        String qcstr = "";
    	if (txtTesttime.containsKey(seq[i]))
  	      timeStr = (String)txtTesttime.get(seq[i]);
    	if (txtTimeunit.containsKey(seq[i]))
          unitStr = (String)txtTimeunit.get(seq[i]);
    	if (txtTemp.containsKey(seq[i]))
            tempStr = (String)txtTemp.get(seq[i]);
    	if (txtSamplingtest.containsKey(seq[i]))
            samplingtestStr = (String)txtSamplingtest.get(seq[i]);
    	if (txtSamplingcond.containsKey(seq[i]))
            samplingcondStr = (String)txtSamplingcond.get(seq[i]);
    	if (txtReworkstep.containsKey(seq[i]))
    		reworkStepStr = (String)txtReworkstep.get(seq[i]);
    	if (txtTesttime2.containsKey(seq[i]))
    	    time2Str = (String)txtTesttime2.get(seq[i]);
      	if (txtTimeunit2.containsKey(seq[i]))
            unit2Str = (String)txtTimeunit2.get(seq[i]);
        if (txtqc.containsKey(seq[i]))
            qcstr = (String)txtqc.get(seq[i]);

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
        if (samplingtestStr == null || samplingtestStr.equals(""))
            ps1.setString(5,"");
          else
            ps1.setString(5, samplingtestStr);
        if (reworkStepStr == null || reworkStepStr.equals(""))
            ps1.setString(6, "");
        else
            ps1.setString(6, StringUtil.Utf8ToBig5(reworkStepStr));
        if (time2Str == null || time2Str.equals("")) {
        	ps1.setString(7, "");
            ps1.setString(8, "");
        } else {
        	ps1.setString(7, time2Str);
            ps1.setString(8, unit2Str);
        }

        if (qcstr == null || qcstr.equals("")) {
            ps1.setString(9, "");
        } else {
            ps1.setString(9, qcstr);
        }
        if (samplingcondStr == null || samplingcondStr.equals(""))
            ps1.setString(10,"");
          else
            ps1.setString(10, samplingcondStr);
        

        ps1.setString(11, sid);
        ps1.setString(12, routename);
        ps1.setString(13, seq[i]);
        ps1.executeUpdate();
      }
      conn.commit();
      unSubmit(sid, "TF_PRODUCT_ROUTE");
      submit(sid, "TF_CP_TEST_STEP","N");
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }
  

    public static boolean UpdateTestStep(Connection conn, String sid, String step_def, String step_name)
            throws Exception {
        StringBuffer UpdateSQL1 = new StringBuffer();
       
        int i;
        try {
            if (sid == null || sid.equals("")) {
                return false;
            }
            if (step_def == null || step_def.equals("")) {
                return false;
            }
            if (step_name == null || step_name.equals("")) {
                return false;
            }
            UpdateSQL1.append("Update tf_product_route_tx set step_def=? where sid=? and step_name=? ");
            PreparedStatement ps1 = conn.prepareStatement(UpdateSQL1.toString());
            ps1.setString(1, step_def);
            ps1.setString(2, sid);
            ps1.setString(3, step_name);
            ps1.executeUpdate();
            conn.commit();
            submit(sid, "TF_YIELD_WS","N");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
        for (i = 1; i <= new ProTestRouteBean().getStepNumber(); i++) {
          ProTestRouteBean trb = new ProTestRouteBean();
          if(rs.getString("Step" + i)!=null){
            trb.setStepname(rs.getString("Step" + i));
            trb.setCount(StringUtil.DoubleDigitNum(i));
            trb.setStepseq(""+i);
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
  /* get sort route code from tf_bom_route*/
  public static BomProductRouteBean[] GetSortRouteCode(String productbody, String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", productbody);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct mask_option, sort_route_code, ws_route FROM tf_bom_route   ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      //System.out.println(SelSQL.toString());
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
  /* get sort route code from tf_bom_route_tx*/
  public static BomProductRouteBean[] GetSortRouteCodeTX(String productbody, String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", productbody);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct mask_option, sort_route_code, ws_route FROM tf_bom_route_tx   ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      //SelSQL.append(" and tag != '2' ");
      //System.out.println(SelSQL.toString());
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
  
	public static BomProductRouteBean[] GetSortRouteCodeDG(String sid, String productbody) {

		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		try {
			ArrayList tmp2 = new ArrayList();
			conn = DBConnection.getConnection();
			String sql = 				 
					 "SELECT distinct x1.brand , x1.mask_option, x1.sort_route_code, x1.ws_route, 'Dgrade-'||x2.wafer_level comment1 \n" + 
					 "FROM tf_bom_route_tx x1,tf_prod_waferlevel_tx x2 where x1.sid = ?\n" + 
					 "and x1.sid = x2.sid\n" + 
					 "and x2.wafer_brand = x1.brand\n" + 
					 /*
                    "union\n" +
                    "SELECT distinct x1.brand , x1.mask_option, x1.sort_route_code, x1.ws_route, 'Change IPN' comment1\n" + 
                    "FROM tf_bom_route_tx x1\n" + 
                    "where x1.sid = ? \n" +
					*/
                    "union\n" +
                    "SELECT distinct x1.brand , x1.mask_option, x1.sort_route_code, x1.ws_route, x2.level_name comment1\n" + 
                    "FROM tf_bom_route_tx x1, tf_changeipn_level x2\n" + 
                    "where x1.sid = ? \n" +
					 
					 "union\n" + 
					 "SELECT distinct x1.brand , x1.mask_option, x1.sort_route_code, x1.ws_route, 'Dgrade-'||x2.wafer_level comment1 \n" + 
					 "FROM tf_bom_route x1,tf_prod_waferlevel x2 where x1.sid =\n" + 
					 "(\n" + 
					 "select max(sid) as sid  from tf_information n where n.product_body = ? and brand = 'KH'\n" + 
					 ")\n" + 
					 "and x1.sid = x2.sid\n" + 
					 "and x2.wafer_brand = x1.brand \n" +
					 /*
                    "union\n" +
                    "SELECT distinct x1.brand , x1.mask_option, x1.sort_route_code, x1.ws_route, 'Change IPN' comment1\n" + 
                    "FROM tf_bom_route x1 where x1.sid =\n" + 
                    "(\n" + 
                    "select max(sid) as sid  from tf_information n where n.product_body = ? and brand = 'KH'\n" + 
                    ")";
					*/
                    "union\n" +
                    "SELECT distinct x1.brand , x1.mask_option, x1.sort_route_code, x1.ws_route, x2.level_name comment1\n" + 
                    "FROM tf_bom_route x1, tf_changeipn_level x2 where x1.sid =\n" + 
                    "(\n" + 
                    "select max(sid) as sid  from tf_information n where n.product_body = ? and brand = 'KH'\n" + 
                    ")";


			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, sid);
			ps.setString(2, sid);
			ps.setString(3, productbody);
			ps.setString(4, productbody);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				BomProductRouteBean bom = new BomProductRouteBean();
				bom.setComment(rs.getString("comment1"));
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
  
  /* get sort route code from tf_bom_route_tx*/
  public static BomProductRouteBean[] GetSortRouteCodeTX(String productbody, String brand, String version) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", productbody);
      whereStem.put("brand", brand);
      whereStem.put("version", version);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct mask_option, sort_route_code, ws_route FROM tf_bom_route_tx   ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      //SelSQL.append(" and tag != '2' ");
      //System.out.println(SelSQL.toString());
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
  public static BomProductRouteBean[] GetSortRouteCode(String productbody, String brand, String version) {
	    StringBuffer SelSQL = new StringBuffer();
	    Connection conn = null;

	    try {
	      ArrayList tmp2 = new ArrayList();
	      HashMap whereStem = new HashMap();
	      whereStem.put("product_body", productbody);
	      whereStem.put("brand", brand);
	      whereStem.put("version", version);
	      conn = DBConnection.getConnection();
	      SelSQL.append("SELECT distinct mask_option, sort_route_code, ws_route FROM tf_bom_route   ");
	      SelSQL.append(SQLStem.getWhereStmt(whereStem));
	      //System.out.println(SelSQL.toString());
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
  
  
  /* get sort route code from tf_bom_route*/
  public static TFInformationBean[] GetProductType(String productType) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    String product_type = "";

    try {
      if(productType==null || productType.equals("NVM")){
    	  product_type = " and (product_type is null or product_type = '' ) ";
      }else if(productType.equals("MROM")){
    	  product_type = " and (product_type = 'Y' )";
      }else if(productType.equals("XROM")){
    	  product_type = " and (product_type = 'N' )";
      }else if(productType.equals("MMS")){
    	  product_type = " and (product_type = 'S' )";
      }
      ArrayList tmp2 = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct product_body, process_type, product_type FROM tf_product where 1=1  ");
      SelSQL.append(product_type);
      //System.out.println(SelSQL.toString());
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
    	  TFInformationBean prmattr = new TFInformationBean();
    	  prmattr.setProduct_body(rs.getString("product_body"));
          prmattr.setProcess_type(rs.getString("process_type"));
          prmattr.setProduct_type(rs.getString("product_type"));
          tmp2.add(prmattr);
        }
        return (TFInformationBean[]) tmp2.toArray(new TFInformationBean[0]);
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
                                             String brand,
                                             String version,
                                             String pbname) {
    String SelSQL = null;
    Connection conn = null;
    boolean result = false;
    PreparedStatement ps = null;

    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      SelSQL = "delete from tf_product_route_tx where sid = "+sid;
      ps = conn.prepareStatement(SelSQL.toString());
      int i = ps.executeUpdate();

      ps.close();
      ps = null;
      if (i >= 0) {
        SelSQL = "insert into tf_product_route_tx\n" +
            "select " + sid + ",0,'" + product_body + "','"+brand+"'," + version +
            ",\n" +
            "a.route_name,a.step_seq,a.step_name,a.test_time,a.time_unit,a.temperature,a.remark,\n" +
            "a.rework_step,a.test_time2,a.time_unit2,a.sampling_test,a.sampling_cond\n" +
            "from tf_product_route a, tf_current_version_vw b\n" +
            "where a.sid = b.sid\n" +
            "and a.product_body = '" + pbname + "'\n" +
            "and a.brand = '"+brand+"'\n" +
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
  public static boolean CheckProductExist(String product,
                                          String brand) {
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
      rs.close();
      ps.close();
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

    public static countstepbean[] GetRouteSamplingTest(String sid) {
        StringBuffer SelSQL = new StringBuffer();
        Connection conn = null;

        try {
            ArrayList tmp2 = new ArrayList();
            HashMap whereStem = new HashMap();
            whereStem.put("sid", sid);
            conn = DBConnection.getConnection();
            SelSQL.append("SELECT DECODE(NVL(sampling_test,' '),'Y','By lot Dgrade','') AS SAMPLING_TEST,STEP_NAME FROM tf_product_route_tx  ");
            SelSQL.append(SQLStem.getWhereStmt(whereStem));
            PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                countstepbean trb = new countstepbean();
                trb.setSampling_test(rs.getString("SAMPLING_TEST"));
                trb.setRoutename2(rs.getString("STEP_NAME"));
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

  /* get all the fw route for selected product */
  public static ProTestRouteBean[] FWRoute(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct route_name FROM tf_product_route_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(route_name,2,1)='W' ");
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

  /* get all the fp route for selected product */
  public static ProTestRouteBean[] FPFTRoute(String pro_b,String brand) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT distinct route_name FROM tf_product_route_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append(" and substr(route_name,2,1) in ('P','Q') ");
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
                                            HashMap reworkHash,
                                            HashMap txtTime2,
                                            HashMap txtUnit2,
                                            HashMap txtqc,
                                            String[] txtRemark,
                                            HashMap txtSamplingtest,
                                            HashMap txtSamplingcond) throws Exception {
    StringBuffer InsSQL = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL.append("insert into tf_product_route_tx " +
      	"(sid,tag, product_body,brand,version,route_name,step_seq,step_name,remark" +
  		",test_time,time_unit,temperature,rework_step,test_time2,time_unit2,sampling_test, QC_ACTUAL_MODE, SAMPLING_COND) ");
      InsSQL.append("values (?,0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      PreparedStatement ps1 = conn.prepareStatement(InsSQL.toString());

      for (i = 0; i < seq.length; i++) {
      	String timeStr = "";
    	String unitStr = "";
    	String tempStr = "";
    	String samplingtestStr = "";
    	String samplingcondStr = "";
    	String reworkStr = "";
    	String timeStr2 = "";
    	String unitStr2 = "";
        String qcstr = "";
    	if (txtTime.containsKey(seq[i]))
    		timeStr = (String)txtTime.get(seq[i]);
    	if (txtUnit.containsKey(seq[i]))
    		unitStr = (String)txtUnit.get(seq[i]);
    	if (txtTemp.containsKey(seq[i]))
    		tempStr = (String)txtTemp.get(seq[i]);
    	if (txtSamplingtest.containsKey(seq[i]))
    		samplingtestStr = (String)txtSamplingtest.get(seq[i]);
    	if (txtSamplingcond.containsKey(seq[i]))
    		samplingcondStr = (String)txtSamplingcond.get(seq[i]);
    	if (reworkHash.containsKey(seq[i]))
    		reworkStr = (String)reworkHash.get(seq[i]);
    	if (txtTime2.containsKey(seq[i]))
    		timeStr2 = (String)txtTime2.get(seq[i]);
    	if (txtUnit2.containsKey(seq[i]))
    		unitStr2 = (String)txtUnit2.get(seq[i]);
        if (txtqc.containsKey(seq[i]))
            qcstr = (String)txtqc.get(seq[i]);
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
        if (reworkStr == null || reworkStr.equals(""))
            ps1.setString(12,"");
        else
        	ps1.setString(12, StringUtil.Utf8ToBig5(reworkStr));
        if (timeStr2 == null || timeStr2.equals("")) {
            ps1.setString(13,"");
            ps1.setString(14,"");
        } else {
            ps1.setString(13, timeStr2);
            ps1.setString(14, unitStr2);
        }
        if (samplingtestStr == null || samplingtestStr.equals(""))
            ps1.setString(15,"");
          else
            ps1.setString(15, samplingtestStr);
        if (qcstr == null || qcstr.equals("")) {
            ps1.setString(16,"");
        } else {
            ps1.setString(16, qcstr);
        }
        if (samplingcondStr == null || samplingcondStr.equals(""))
            ps1.setString(17,"");
          else
            ps1.setString(17, samplingcondStr);
        ps1.executeUpdate();
      }
      conn.commit();
      unSubmit(sid, "TF_PRODUCT_ROUTE");
      submit(sid, "TF_CP_TEST_STEP","N");
    } catch (Exception e) {
      e.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  //check if any earlier version of data of the given product_body and brand in tf_bom_route
  public static boolean CheckExistBomRoute(String sid,
                                           String pro_b,
                                           String brand,
                                           String version) throws Exception {

    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      whereStem.put("version",maxV);
      whereStem.put("tag", "0");
      conn = DBConnection.getConnection();
      sql.append("select * from tf_bom_route ");
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

  public static boolean CheckExistBomRouteMcp(String sid,
          String pro_b,
          String brand,
          String version) throws Exception {

		StringBuffer sql = new StringBuffer();
		Connection conn = null;
		String maxV=String.valueOf(Integer.parseInt(version)-1);
		try {
			HashMap whereStem = new HashMap();
			whereStem.put("product_body", pro_b);
			whereStem.put("brand", brand);
			whereStem.put("version",maxV);
			whereStem.put("tag", "0");
			conn = DBConnection.getConnection();
			sql.append("select * from tf_bom_route_mcp ");
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
  
  //check if any data of the give product_body and brand in tf_bom_route_tx
   public static boolean CheckExistTX(String sid,
                                      Connection conn,
                                      String pro_b,
                                      String brand) throws Exception {

     StringBuffer sql = new StringBuffer();
     try {
       HashMap whereStem = new HashMap();
       whereStem.put("sid", sid);
       sql.append("select count(1) as cnt from tf_bom_route_tx ");
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

   public static boolean CheckExistMcpTX(String sid,
           Connection conn,
           String pro_b,
           String brand) throws Exception {

		StringBuffer sql = new StringBuffer();
		try {
			HashMap whereStem = new HashMap();
			whereStem.put("sid", sid);
			sql.append("select count(1) as cnt from tf_bom_route_mcp_tx ");
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
  
//check if any data of the given product body and brand in tf_prod_waferlevel_tx
  public static boolean CheckProdWaferlevel_TX_Exist(String sid,
                                         String pro_b,
                                         String brand) throws Exception {

    StringBuffer sql = new StringBuffer();
    Connection conn=null;
    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      sql.append("select * from tf_prod_waferlevel_tx ");
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
  
//check if any earlier version data of the given product body and brand in tf_prod_waferlevel
  public static boolean CheckProdWaferlevel_Exist(String sid,
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
      sql.append("select * from tf_prod_waferlevel ");
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
                                   String br,
                                   String sid,
                                   String version) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", br);
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

  //copy the data from tf_bome_route to tf_bome_route_tx
  public static boolean RouteToRouteTx(String pro_b,
                                       String br,
                                       String sid,
                                       String version) {
    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);

    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body",pro_b);
      whereStem.put("brand",br);
      whereStem.put("version", maxV);
      whereStem.put("tag", "0");

      SqlStmt.append("insert into tf_bom_route_tx (sid,tag,product_body,brand,version,backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,id,ft_route_add,ft_route_add2,ft_route_add3,ft_route_code,tf_ws_comment,sales_form,endurance,wsspecialcontrol,quality_level,quality_level_comment,mcp_flag) ");
      SqlStmt.append("select " + sid + ",0,product_body,brand," + version + ",backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,test_seq.nextval,ft_route_add,ft_route_add2,ft_route_add3,ft_route_code,tf_ws_comment,NVL(sales_form,'NVM'),endurance,wsspecialcontrol,quality_level,quality_level_comment,mcp_flag from tf_bom_route ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
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

  //copy the data from tf_bome_route_mcp to tf_bome_route_mcp_tx
  public static boolean RouteMcpToRouteMcpTx(String pro_b,
                                       String br,
                                       String sid,
                                       String version) {
    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);

    try {
      conn=DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body",pro_b);
      whereStem.put("brand",br);
      whereStem.put("version", maxV);
      whereStem.put("tag", "0");

      SqlStmt.append("insert into tf_bom_route_mcp_tx (sid,tag,product_body,brand,version,backend_option,fg_with_code,pin_count,package_type,ft_route,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,id,ft_route_add,ft_route_code,tf_ws_comment,sales_form,endurance,wsspecialcontrol,ft_route_add2,ft_route_add3,quality_level,quality_level_comment,component_no,com_prod_body,com_mask_option,com_backend_option) ");
      SqlStmt.append("select " + sid + ",0,product_body,brand," + version + ",backend_option,fg_with_code,pin_count,package_type,ft_route,sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,test_seq.nextval,ft_route_add,ft_route_code,tf_ws_comment,NVL(sales_form,'NVM'),endurance,wsspecialcontrol,ft_route_add2,ft_route_add3,quality_level,quality_level_comment,component_no,com_prod_body,com_mask_option,com_backend_option from tf_bom_route_mcp ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
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

  // 將TF＿PROD＿EPN的DATA與tf_bom_route_tx裡的比對，如沒有重複的的就return true
  public static boolean CheckExistInBomTx(Connection conn,
                                          String brand,
                                          String pro_b,
                                          String beopt,
                                          String pin,
                                          String pkgtype,
                                          String mask,
                                          String sales_form,
                                          String version) throws Exception{

    StringBuffer SelSQL = new StringBuffer();
    SelSQL.append("SELECT count(1) as cnt FROM tf_bom_route_tx ");
    SelSQL.append("where brand=? and product_body=? and backend_option=?  ");
    SelSQL.append("and pin_count=? and package_type=? and mask_option=? and version=? and sales_form=?");
    PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
    ps.setString(1,brand);
    ps.setString(2,pro_b);
    ps.setString(3,beopt);
    ps.setString(4,pin);
    ps.setString(5,pkgtype);
    ps.setString(6,mask);
    ps.setString(7,version);
    ps.setString(8,sales_form);
    ResultSet rs = ps.executeQuery();

    while(rs.next()){
      if(rs.getInt("cnt")==0){
        return true;
      }
    }
    return false;
  }

  public static boolean CheckExistInBomMcpTx(Connection conn,
          String brand, String pro_b, String beopt, String pin,
          String pkgtype, String sales_form, String version, String epn) throws Exception{

	StringBuffer SelSQL = new StringBuffer();
	SelSQL.append("SELECT ft_route_code, com_prod_body, com_mask_option, component_no FROM tf_bom_route_mcp_tx ");
	SelSQL.append("where brand=? and product_body=? and backend_option=? \n");
	SelSQL.append("and pin_count=? and package_type=? and version=? and sales_form=?\n");
	SelSQL.append("order by ft_route_code, component_no \n");
	
	//20160218-SOPHIA-MARK-SelSQL.append("and component_no=? and com_prod_body=? and com_mask_option=? \n");
	//com_backend_option
	//20160218-SOPHIA-MARK-if(com_backend_option!=null && !com_backend_option.equals(""))
	//20160218-SOPHIA-MARK-	SelSQL.append("and com_backend_option='" + com_backend_option + "' \n");
	PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
	ps.setString(1,brand);
	ps.setString(2,pro_b);
	ps.setString(3,beopt);
	ps.setString(4,pin);
	ps.setString(5,pkgtype);
	ps.setString(6,version);
	ps.setString(7,sales_form);
	//20160218-SOPHIA-MARK-ps.setString(8,component_no);
	//20160218-SOPHIA-MARK-ps.setString(9,com_prod_body);
	//20160218-SOPHIA-MARK-ps.setString(10,com_mask_option);
	ResultSet rs = ps.executeQuery();
	ArrayList al = new ArrayList();
	while (rs.next()) {
  	  HashMap hm = new HashMap();
  	  hm.put("COMPONENT_NO", rs.getString("COMPONENT_NO")==null?"":rs.getString("COMPONENT_NO"));
  	  hm.put("COM_PROD_BODY", rs.getString("COM_PROD_BODY")==null?"":rs.getString("COM_PROD_BODY"));
  	  hm.put("COM_MASK_OPTION", rs.getString("COM_MASK_OPTION")==null?"":rs.getString("COM_MASK_OPTION"));
  	  hm.put("FT_ROUTE_CODE", rs.getString("FT_ROUTE_CODE")==null?"":rs.getString("FT_ROUTE_CODE"));
  	  al.add(hm);
    }
	HashMap hm[] = (HashMap[])al.toArray(new HashMap[0]);
	
	StringBuffer SelSQL1 = new StringBuffer();
	SelSQL1.append("select t.com_prod_body, t.com_mask_option, t.component_no\n");
	SelSQL1.append(" from tf_component_product t\n");
	SelSQL1.append("where t.prod_body =? \n");
	SelSQL1.append("  and t.backend_option =?\n");
	SelSQL1.append("  and t.epn = ? \n");
	PreparedStatement ps1 = conn.prepareStatement(SelSQL1.toString());
	ps1.setString(1,pro_b);
	ps1.setString(2,beopt);
	ps1.setString(3,epn);
	ResultSet rs1 = ps1.executeQuery();
	ArrayList al1 = new ArrayList();
	while (rs1.next()) {
  	  HashMap hm1 = new HashMap();
  	  hm1.put("COMPONENT_NO", rs1.getString("COMPONENT_NO")==null?"":rs1.getString("COMPONENT_NO"));
  	  hm1.put("COM_PROD_BODY", rs1.getString("COM_PROD_BODY")==null?"":rs1.getString("COM_PROD_BODY"));
  	  hm1.put("COM_MASK_OPTION", rs1.getString("COM_MASK_OPTION")==null?"":rs1.getString("COM_MASK_OPTION"));
  	  al1.add(hm1);
    }
	HashMap hm1[] = (HashMap[])al1.toArray(new HashMap[0]);
	
	if(hm.length >= hm1.length){
		String tmp_ft_route_code = "";
		int count = 0;
		ArrayList al2 = new ArrayList();
		for(int i=0; i<hm.length; i++){
			HashMap hm2 = new HashMap();
			hm2.put("COMPONENT_NO", hm[i].get("COMPONENT_NO"));
			hm2.put("COM_PROD_BODY", hm[i].get("COM_PROD_BODY"));
			hm2.put("COM_MASK_OPTION", hm[i].get("COM_MASK_OPTION"));
			hm2.put("FT_ROUTE_CODE", hm[i].get("FT_ROUTE_CODE"));
			al2.add(hm2);
			if(((i+1)>=hm.length) ||(hm[i+1]!= null && hm[i+1].get("FT_ROUTE_CODE")!= null && 
			   !hm[i].get("FT_ROUTE_CODE").equals(hm[i+1].get("FT_ROUTE_CODE")))){
				HashMap hm22[] = (HashMap[])al2.toArray(new HashMap[0]);
				if(hm22.length == hm1.length){
					for(int k=0; k<hm22.length; k++){
						if(hm22[k].get("COMPONENT_NO").toString().equals(hm1[k].get("COMPONENT_NO").toString()) &&
						hm22[k].get("COM_PROD_BODY").toString().equals(hm1[k].get("COM_PROD_BODY").toString()) &&
						hm22[k].get("COM_MASK_OPTION").toString().equals(hm1[k].get("COM_MASK_OPTION").toString())){
							count++;
						}
					}
					if(count == hm1.length) 
						return false;
				}
				al2 = new ArrayList();
			}	
		}
		return true;
	}else{
		return true;
	}
	
  }  
  public static boolean CheckExistInBomMcpFtRouteCode(Connection conn,
          String brand, String pro_b, String beopt, String pin,
          String pkgtype, String sales_form, String version, String ft_route_code) throws Exception{

	StringBuffer SelSQL = new StringBuffer();
	SelSQL.append("SELECT count(1) as cnt FROM tf_bom_route_mcp_tx ");
	SelSQL.append("where brand=? and product_body=? and backend_option=? \n");
	SelSQL.append("and pin_count=? and package_type=? and version=? and sales_form=? and ft_route_code=?\n");
	//20160218-SOPHIA-MARK-SelSQL.append("and component_no=? and com_prod_body=? and com_mask_option=? \n");
	//com_backend_option
	//20160218-SOPHIA-MARK-if(com_backend_option!=null && !com_backend_option.equals(""))
	//20160218-SOPHIA-MARK-	SelSQL.append("and com_backend_option='" + com_backend_option + "' \n");
	PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
	ps.setString(1,brand);
	ps.setString(2,pro_b);
	ps.setString(3,beopt);
	ps.setString(4,pin);
	ps.setString(5,pkgtype);
	ps.setString(6,version);
	ps.setString(7,sales_form);
	ps.setString(8,ft_route_code);
	//20160218-SOPHIA-MARK-ps.setString(8,component_no);
	//20160218-SOPHIA-MARK-ps.setString(9,com_prod_body);
	//20160218-SOPHIA-MARK-ps.setString(10,com_mask_option);
	ResultSet rs = ps.executeQuery();
	
	while(rs.next()){
		if(rs.getInt("cnt")==0){
			return true;
		}
	}
	return false;
  }  
  
  //copy the data from tf_prod_epn to tf_bom_route_tx
  public static boolean EPNtoBomTx(String pro_b,
                                   String br,
                                   Connection conn,
                                   String sid,
                                   String version) {

    StringBuffer ComSql = new StringBuffer();
    String InsSQL = null;
//    String maxV = String.valueOf(Integer.parseInt(version)-1);
    String productType = OiMaintainService.getProductType(conn, sid);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("a.product_body", pro_b);
      whereStem.put("brand", br);
      whereStem.put("!status", "E");
      if (productType.equals("NVM")) {
    	  ComSql.append("select distinct a.product_body,brand,backend_option,pin_count,\n" +
    	  				"package_type,mask_option,sales_form,decode(c.id,null,'NA','Typical') special_control\n" +
    	  				"from tf_prod_epn a, tf_product b, tf_description c\n");
    	  ComSql.append(SQLStem.getWhereStmt(whereStem));
    	  ComSql.append("\nand a.product_body = b.product_body" +
    			  		"\nand b.process_type = c.description (+)");
      }
      InsSQL="insert into tf_bom_route_tx (sid,tag,product_body,brand,version," +
		"backend_option,pin_count,package_type,mask_option,ft_route_code,sales_form,endurance,id) " +
		"values (?,?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval)";
      PreparedStatement Comps = conn.prepareStatement(ComSql.toString());
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      ResultSet rs=Comps.executeQuery();
      String ftRouteCode = null;
      ftRouteCode = getMaxFTRouteCode(sid);

      while(rs.next()){
        //如果TF_BOM_ROUTE_TX內沒有重複的資料，就可將該筆資料加進TF_BOM_ROUTE_TX
        if (OiMaintainService.CheckExistInBomTx(conn,
                                               rs.getString("brand"),
                                               rs.getString("product_body"),
                                               rs.getString("backend_option"),
                                               rs.getString("pin_count"),
                                               rs.getString("package_type"),
                                               rs.getString("mask_option"),
                                               rs.getString("sales_form"),
                                               version))
        {
          ps2.setString(1,sid);
          ps2.setString(2,"1");
          ps2.setString(3,rs.getString("product_body"));
          ps2.setString(4,rs.getString("brand"));
          ps2.setString(5,version);
          ps2.setString(6,rs.getString("backend_option"));
          ps2.setString(7,rs.getString("pin_count"));
          ps2.setString(8,rs.getString("package_type"));
          ps2.setString(9,rs.getString("mask_option"));
          ftRouteCode = getNextRouteCode(ftRouteCode);
          ps2.setString(10, ftRouteCode);
          ps2.setString(11,rs.getString("sales_form"));
          ps2.setString(12,rs.getString("special_control"));
          ps2.executeUpdate();
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

  public static boolean EPNtoBomMcpTx(String pro_b,
          String br,
          Connection conn,
          String sid,
          String version) {

	StringBuffer ComSql = new StringBuffer();
	String InsSQL = null;
	//String maxV = String.valueOf(Integer.parseInt(version)-1);
	String productType = OiMaintainService.getProductType(conn, sid);
	try {
		HashMap whereStem = new HashMap();
		whereStem.put("a.product_body", pro_b);
		whereStem.put("brand", br);
		whereStem.put("!status", "E");
		if (productType.equals("NVM")) {
			ComSql.append("select distinct a.product_body,a.brand,a.backend_option,a.pin_count,a.package_type,\n")
				.append("a.mask_option,a.sales_form,decode(c.id,null,'NA','Typical') special_control,a.epn\n")
				.append("from tf_prod_epn a, tf_product b, tf_description c\n");
			ComSql.append(SQLStem.getWhereStmt(whereStem));
			ComSql.append("\nand a.product_body = b.product_body\n")
				.append("and b.process_type = c.description (+)\n");
		}
		InsSQL="insert into tf_bom_route_mcp_tx (sid,tag,product_body,brand,version," +
		"backend_option,pin_count,package_type,ft_route_code,sales_form,endurance," +
		"component_no,com_prod_body,com_mask_option,com_backend_option,id) \n" +
		"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement Comps = conn.prepareStatement(ComSql.toString());
		PreparedStatement ps2 = conn.prepareStatement(InsSQL);
		ResultSet rs=Comps.executeQuery();
		String ftRouteCode = null;
		ftRouteCode = getMaxFTRouteCodeMcp(sid);
		
		while(rs.next()){
			
			int id = DBConnection.getSequence("test_seq");
			ftRouteCode = getNextRouteCode(ftRouteCode);
			String productBody = rs.getString("product_body");
			String backendOption = rs.getString("backend_option");
			String epn = rs.getString("epn");
			
			//如果TF_BOM_ROUTE_MCP_TX內沒有重複的資料，就可將該筆資料加進TF_BOM_ROUTE_MCP_TX
			if (OiMaintainService.CheckExistInBomMcpTx(conn,
			                      rs.getString("brand"),
			                      rs.getString("product_body"),
			                      rs.getString("backend_option"),
			                      rs.getString("pin_count"),
			                      rs.getString("package_type"),
			                      rs.getString("sales_form"),
			                      version,epn))
			{
			
				ps2.setString(1,sid);
				ps2.setString(2,"1");
				ps2.setString(3,productBody);
				ps2.setString(4,rs.getString("brand"));
				ps2.setString(5,version);
				ps2.setString(6,backendOption);
				ps2.setString(7,rs.getString("pin_count"));
				ps2.setString(8,rs.getString("package_type"));
				ps2.setString(9, ftRouteCode);
				ps2.setString(10,rs.getString("sales_form"));
				ps2.setString(11,rs.getString("special_control"));
				HashMap[] hm = getComponentProductList(productBody, backendOption, epn);
				for(int i=0; i<hm.length; i++){
					ps2.setString(12,hm[i].get("COMPONENT_NO").toString());
					ps2.setString(13,hm[i].get("COM_PROD_BODY").toString());
					ps2.setString(14,hm[i].get("COM_MASK_OPTION").toString());
					ps2.setString(15,hm[i].get("COM_BACKEND_OPTION").toString());				
					ps2.setInt(16,id);				
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
  //copy tf_bom_route to tf_bom_route_mcp_tx
  public static boolean RouteScpToRouteMcpTx(String pro_b,
          String br,
          Connection conn,
          String sid,
          String version) {

	StringBuffer ComSql = new StringBuffer();
	String InsSQL = null;
	String maxV = String.valueOf(Integer.parseInt(version)-1);
	String productType = OiMaintainService.getProductType(conn, sid);
	try {
		HashMap whereStem = new HashMap();
		whereStem.put("aa.product_body", pro_b);
		whereStem.put("aa.brand", br);
		whereStem.put("aa.version", maxV);
		whereStem.put("!cc.status", "E");
		if (productType.equals("NVM")) {
			ComSql.append("select distinct aa.product_body,aa.brand,aa.backend_option,aa.fg_with_code,\n")
					.append("aa.pin_count,aa.package_type,aa.ft_route,''sort_route_code,''db_with_code,''ws_route,''ws_route_add,\n")
					.append("aa.tf_comment,aa.ft_route_add,aa.ft_route_code,''tf_ws_comment,aa.sales_form,aa.endurance,\n")
					.append("''wsspecialcontrol,aa.ft_route_add2,aa.ft_route_add3,aa.quality_level,\n") 
					.append("''quality_level_comment,cc.epn\n") 
					.append("from tf_bom_route AA,  tf_prod_epn cc \n");
			ComSql.append(SQLStem.getWhereStmt(whereStem));
			ComSql.append(" and aa.tag != 2\n") 
					.append(" and aa.PRODUCT_BODY=cc.product_body\n") 
					.append(" and cc.brand = aa.brand\n") 
					.append(" and cc.backend_option=aa.backend_option\n") 
					.append(" and cc.pin_count = aa.pin_count\n") 
					.append(" and cc.package_type = aa.package_type\n"); 



		}
		InsSQL="insert into tf_bom_route_mcp_tx (sid,tag,product_body,brand,version," +
		"backend_option,fg_with_code,pin_count,package_type,ft_route,tf_comment,ft_route_add,ft_route_add2,ft_route_add3,ft_route_code,sales_form,endurance,quality_level,quality_level_comment," +
		"component_no,com_prod_body,com_mask_option,com_backend_option,id) \n" +
		"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement Comps = conn.prepareStatement(ComSql.toString());
		PreparedStatement ps2 = conn.prepareStatement(InsSQL);
		ResultSet rs=Comps.executeQuery();
		//String ftRouteCode = null;
		//ftRouteCode = getMaxFTRouteCodeMcp(sid);
		
		while(rs.next()){
			
			int id = DBConnection.getSequence("test_seq");
			//ftRouteCode = getNextRouteCode(ftRouteCode);
			String productBody = rs.getString("product_body");
			String backendOption = rs.getString("backend_option");
			String epn = rs.getString("epn");
			
			HashMap[] hm = getComponentProductList(productBody, backendOption, epn);
			//如果TF_BOM_ROUTE_MCP_TX內沒有ft_route_code重複的資料，就可將該筆資料加進TF_BOM_ROUTE_MCP_TX
			if (hm.length>0 && OiMaintainService.CheckExistInBomMcpFtRouteCode(conn,
			                      rs.getString("brand"),
			                      rs.getString("product_body"),
			                      rs.getString("backend_option"),
			                      rs.getString("pin_count"),
			                      rs.getString("package_type"),
			                      rs.getString("sales_form"),
			                      version,
			                      rs.getString("ft_route_code")))
			{
			
				ps2.setString(1,sid);
				ps2.setString(2,"3");
				ps2.setString(3,productBody);
				ps2.setString(4,rs.getString("brand"));
				ps2.setString(5,version);
				ps2.setString(6,backendOption);
				ps2.setString(7,rs.getString("fg_with_code"));
				ps2.setString(8,rs.getString("pin_count"));
				ps2.setString(9,rs.getString("package_type"));
				ps2.setString(10, rs.getString("ft_route"));
				ps2.setString(11, rs.getString("tf_comment"));
				ps2.setString(12, rs.getString("ft_route_add"));
				ps2.setString(13, rs.getString("ft_route_add2"));
				ps2.setString(14, rs.getString("ft_route_add3"));
				//ps2.setString(14, ftRouteCode);
				ps2.setString(15, rs.getString("ft_route_code"));
				ps2.setString(16,rs.getString("sales_form"));
				ps2.setString(17,rs.getString("endurance"));
				ps2.setString(18,rs.getString("quality_level"));
				ps2.setString(19,rs.getString("quality_level_comment"));
				//HashMap[] hm = getComponentProductList(productBody, backendOption, epn);
				for(int i=0; i<hm.length; i++){
					ps2.setString(20,hm[i].get("COMPONENT_NO").toString());
					ps2.setString(21,hm[i].get("COM_PROD_BODY").toString());
					ps2.setString(22,hm[i].get("COM_MASK_OPTION").toString());
					ps2.setString(23,hm[i].get("COM_BACKEND_OPTION").toString());				
					ps2.setInt(24,id);				
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
  
  public static HashMap[] getComponentProductList(String productBody, String backendOption, String epn){
	  ArrayList al = new ArrayList();
	  String sql = "select * from tf_component_product where prod_body = ? and backend_option = ? and epn = ?";
	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sql.toString());
		  ps.setString(1, productBody);
		  ps.setString(2, backendOption);
		  ps.setString(3, epn);
		  ResultSet rs = ps.executeQuery();
	      while (rs.next()) {
	    	  HashMap hm = new HashMap();
	    	  hm.put("COMPONENT_NO", rs.getString("COMPONENT_NO")==null?"":rs.getString("COMPONENT_NO"));
	    	  hm.put("COM_PROD_BODY", rs.getString("COM_PROD_BODY")==null?"":rs.getString("COM_PROD_BODY"));
	    	  hm.put("COM_MASK_OPTION", rs.getString("COM_MASK_OPTION")==null?"":rs.getString("COM_MASK_OPTION"));
	    	  hm.put("COM_BACKEND_OPTION", rs.getString("COM_BACKEND_OPTION")==null?"":rs.getString("COM_BACKEND_OPTION"));
	    	  al.add(hm);
	      }
	  } catch (Exception e) {
		  e.printStackTrace();
	      DBConnection.rollback(conn);
	  }finally {
	      DBConnection.close(conn);
	      conn = null;
	  }
	  return (HashMap[])al.toArray(new HashMap[0]);
  }
  
  //get the data of the given product_body and brand from tf_bom_route
  public static BomProductRouteBean[] BomRouteFirstVersion(String pro_b,
                                                           String br,
                                                           String sid,
                                                           String version) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      conn = DBConnection.getConnection();
//      FTTestActionForm prod = FTService.getInfo(Integer.parseInt(sid));
   	  SelSQL.append("SELECT t.* FROM tf_bom_route_tx t ");
   	  SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("order by t.product_body,t.backend_option,t.fg_with_code,t.pin_count," +
      				"t.package_type,t.ft_route,t.mask_option,t.db_with_code ");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        BomProductRouteBean bom = new BomProductRouteBean();
        bom.setTag(rs.getString("tag"));
        bom.setBrand(rs.getString("brand"));
        bom.setProductbody(rs.getString("product_body"));
        bom.setBeoption(rs.getString("backend_option"));
        bom.setFgwithcode(rs.getString("fg_with_code"));
        bom.setPincount(rs.getString("pin_count"));
        bom.setPkgtype(rs.getString("package_type"));
        bom.setFtroute(rs.getString("ft_route"));
        bom.setMaskopt(rs.getString("mask_option"));
        bom.setDbwithcode(rs.getString("db_with_code"));
        bom.setSortroutecode(rs.getString("sort_route_code"));
        bom.setWsroute(rs.getString("ws_route"));
        bom.setWsaddroute(rs.getString("ws_route_add"));
        bom.setComment(rs.getString("tf_comment"));
        bom.setTf_ws_comment(rs.getString("tf_ws_comment"));
        bom.setId(rs.getString("id"));
        bom.setFtAddroute(rs.getString("FT_ROUTE_ADD"));
        bom.setFtAddroute2(rs.getString("FT_ROUTE_ADD2"));
        bom.setFtAddroute3(rs.getString("FT_ROUTE_ADD3"));
        bom.setFt_route_code(rs.getString("ft_route_code"));
        bom.setSales_form(rs.getString("sales_form"));
        bom.setEndurance(rs.getString("endurance"));
        bom.setWsspecialcontrol(rs.getString("wsspecialcontrol"));
        bom.setQuality_level(rs.getString("quality_level"));
        bom.setQuality_level_comment(rs.getString("quality_level_comment"));
        bom.setMcp_flag(rs.getString("mcp_flag"));
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

  public static BomProductRouteBean[] BomRouteMcpFirstVersion(String pro_b,
          String br,
          String sid,
          String version) {

		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		
		try {
			ArrayList tmp2 = new ArrayList();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", sid);
			conn = DBConnection.getConnection();
			//FTTestActionForm prod = FTService.getInfo(Integer.parseInt(sid));
			SelSQL.append("SELECT t.*, decode((select count(*) from tf_information where product_body=t.com_prod_body),0,0,1) outsource_tag FROM tf_bom_route_mcp_tx t ");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			SelSQL.append("order by t.backend_option,t.pin_count,t.package_type,t.ft_route_code,t.component_no");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				BomProductRouteBean bom = new BomProductRouteBean();
				bom.setTag(rs.getString("tag"));
				bom.setBrand(rs.getString("brand"));
				bom.setProductbody(rs.getString("product_body"));
				bom.setBeoption(rs.getString("backend_option"));
				bom.setFgwithcode(rs.getString("fg_with_code"));
				bom.setPincount(rs.getString("pin_count"));
				bom.setPkgtype(rs.getString("package_type"));
				bom.setFtroute(rs.getString("ft_route"));
				//bom.setMaskopt(rs.getString("mask_option"));
				bom.setDbwithcode(rs.getString("db_with_code"));
				bom.setSortroutecode(rs.getString("sort_route_code"));
				bom.setWsroute(rs.getString("ws_route"));
				bom.setWsaddroute(rs.getString("ws_route_add"));
				bom.setComment(rs.getString("tf_comment"));
				bom.setTf_ws_comment(rs.getString("tf_ws_comment"));
				bom.setId(rs.getString("id"));
				bom.setFtAddroute(rs.getString("FT_ROUTE_ADD"));
				bom.setFtAddroute2(rs.getString("FT_ROUTE_ADD2"));
				bom.setFtAddroute3(rs.getString("FT_ROUTE_ADD3"));
				bom.setFt_route_code(rs.getString("ft_route_code"));
				bom.setSales_form(rs.getString("sales_form"));
				bom.setEndurance(rs.getString("endurance"));
				bom.setWsspecialcontrol(rs.getString("wsspecialcontrol"));
				bom.setQuality_level(rs.getString("quality_level"));
				bom.setQuality_level_comment(rs.getString("quality_level_comment"));
				//bom.setMcp_flag(rs.getString("mcp_flag"));
				bom.setComponent_no(rs.getString("component_no"));
				bom.setCom_prod_body(rs.getString("com_prod_body"));
				bom.setCom_mask_option(rs.getString("com_mask_option"));
				bom.setCom_backend_option(rs.getString("com_backend_option"));
				bom.setOutsource_tag(rs.getString("outsource_tag"));
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
  
  public static boolean submitX(String sid,String field) throws Exception {
      StringBuffer UpdateSQL2 = new StringBuffer();
      Connection conn = null;
      try {
        conn = DBConnection.getConnection();
        UpdateSQL2.append("Update tf_information set "+field+"='Y' where sid=? ");
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
      UpdateSQL2.append("update tf_information set TF_BOM_ROUTE='Y',TF_YIELD_WS='N',TF_YIELD_FT='N' where sid=? ");
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
  
  public static boolean submitBomMcpProductRoute(String sid) throws Exception {
	    StringBuffer UpdateSQL2 = new StringBuffer();
	    Connection conn = null;
	    try {
	      conn = DBConnection.getConnection();
	      UpdateSQL2.append("update tf_information set TF_BOM_MCP_ROUTE='Y',TF_YIELD_WS='N',TF_YIELD_FT='N'  where sid=? ");
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
  
//set TF_PROD_WAFERLEVEL='Y' in tf_information
  public static boolean submitProdWaferlevel(String sid) throws Exception {
    StringBuffer UpdateSQL2 = new StringBuffer();
    Connection conn = null;
    try {
      conn=DBConnection.getConnection();
      UpdateSQL2.append("update tf_information set TF_PROD_WAFERLEVEL='Y' where sid=? ");
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

  //儲存更改過的資料到tf_bom_route_tx
  public static boolean InsertBomRoute(String[] ftroutecode,
                                       String[] tag,
                                       ProTestRouteBeanAF fm,
                                       String[] epnbody,
                                       String[] brand,
                                       String[] productbody,
                                       String[] beoption,
                                       String[] fgwithcode,
                                       String[] pincount,
                                       String[] pkgtype,
                                       String[] grade,
                                       String[] productclass,
                                       String[] ftwithcode,
                                       String[] maskopt,
                                       String[] dbwithcode,
                                       String[] sortroutecode,
                                       String[] wsroute,
                                       String[] wsaddroute,
                                       String[] comment,
                                       String[] id,
                                       String[] ftAddroute,
                                       String[] ftAddroute2,
                                       String[] ftAddroute3,
                                       String[] wscomment,
                                       String[] salesform,
                                       String[] endurance,
                                       String[] wsspecialcontrol,
                                       String[] quality_level,
                                       String[] quality_level_comment,
                                       String[] mcp_flag
                                       ) {

    String InsSQL=null;

    Connection conn = null;

    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      InsSQL="update tf_bom_route_tx set ft_route=?, sort_route_code=?, ws_route=?, " +
          "ws_route_add=?, tf_comment=?, ft_route_add=?, ft_route_add2=?, ft_route_add3=? ,ft_route_code=?, tf_ws_comment=?, " +
          "sales_form=?, endurance=?, wsspecialcontrol=? , quality_level=?,quality_level_comment=?, fg_with_code=?, db_with_code=?, mcp_flag=? where id = ?  ";
//      InsSQL2="update tf_bom_route_tx set ft_route=?, sort_route_code=?, ws_route=?, " +
          //"ws_route_add=?, tf_comment=?, ft_route_add=?,tag='1',ft_route_code=?, " +
          //"tf_ws_comment=?, sales_form=?, endurance=? where id = ?  ";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
//      PreparedStatement ps3 = conn.prepareStatement(InsSQL2);
      for (int i = 0; i < id.length; i++){

        ps2.setString(1,ftwithcode[i]);
        ps2.setString(2,sortroutecode[i]);
        ps2.setString(3,wsroute[i]);
        ps2.setString(4,wsaddroute[i]);
        ps2.setString(5,StringUtil.Utf8ToBig5((comment[i].trim())));
        ps2.setString(6,ftAddroute[i]);
        ps2.setString(7,ftAddroute2[i]);
        ps2.setString(8,ftAddroute3[i]);
        ps2.setString(9,ftroutecode[i]);
        ps2.setString(10,StringUtil.Utf8ToBig5(wscomment[i].trim()));
        ps2.setString(11,salesform[i]);
        ps2.setString(12,endurance[i]);
        ps2.setString(13,wsspecialcontrol[i]);
        ps2.setString(14,quality_level[i]);
        ps2.setString(15,quality_level_comment[i]);
        ps2.setString(16,fgwithcode[i]);
        ps2.setString(17,dbwithcode[i]);
        ps2.setString(18,mcp_flag[i]);
        ps2.setString(19,id[i]);
        ps2.executeUpdate();

/*以下是不需要的，2008/05/05，因為只有 comment 是可以改的*/
//        if (tag[i].equals("0")){/* if tag of the selected data is 0 */
          /* if data with tag of 0 is changed, update the tag from 0 to 1*/
//          if (CheckBomTagZero(conn,id[i],ftwithcode[i],ftAddroute[i],
//                              sortroutecode[i],wsroute[i],wsaddroute[i],
//                              comment[i],ftroutecode[i],salesform[i])){
//            ps3.setString(1,ftwithcode[i]);
//            ps3.setString(2,sortroutecode[i]);
//            ps3.setString(3,wsroute[i]);
//            ps3.setString(4,wsaddroute[i]);
//            ps3.setString(5,StringUtil.Utf8ToBig5((comment[i].trim())));
//            ps3.setString(6,ftAddroute[i]);
//            ps3.setString(7,ftroutecode[i]);
//            ps3.setString(8,StringUtil.Utf8ToBig5(wscomment[i].trim()));
//            ps3.setString(9,salesform[i]);
//            ps3.setString(10,id[i]);
//            ps3.executeUpdate();
//          } else {/*if data with tag of 0 remains unchanged,no need to update tag*/
//            ps2.setString(1,ftwithcode[i]);
//            ps2.setString(2,sortroutecode[i]);
//            ps2.setString(3,wsroute[i]);
//            ps2.setString(4,wsaddroute[i]);
//            ps2.setString(5,StringUtil.Utf8ToBig5(comment[i].trim()));
//            ps2.setString(6,ftAddroute[i]);
//            ps2.setString(7,ftroutecode[i]);
//            ps2.setString(8,StringUtil.Utf8ToBig5(wscomment[i].trim()));
//            ps2.setString(9,salesform[i]);
//            ps2.setString(10,id[i]);
//            ps2.executeUpdate();
//          }
//        } else { /*if the data with tag of 1, no need to update tag*/
//          ps2.setString(1, ftwithcode[i]);
//          ps2.setString(2, sortroutecode[i]);
//          ps2.setString(3, wsroute[i]);
//          ps2.setString(4, wsaddroute[i]);
//          ps2.setString(5, StringUtil.Utf8ToBig5(comment[i].trim()));
//          ps2.setString(6, ftAddroute[i]);
//          ps2.setString(7, ftroutecode[i]);
//          ps2.setString(8, StringUtil.Utf8ToBig5(wscomment[i].trim()));
//          ps2.setString(9,salesform[i]);
//          ps2.setString(10,id[i]);
//          ps2.executeUpdate();
//        }

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

  public static boolean InsertBomRouteMcp(String[] ftroutecode,
          String[] tag,
          ProTestRouteBeanAF fm,
          String[] epnbody,
          String[] brand,
          String[] productbody,
          String[] beoption,
          String[] fgwithcode,
          String[] pincount,
          String[] pkgtype,
          String[] grade,
          String[] productclass,
          String[] ftwithcode,
          String[] dbwithcode,
          String[] sortroutecode,
          String[] wsroute,
          String[] wsaddroute,
          String[] comment,
          String[] id,
          String[] ftAddroute,
          String[] ftAddroute2,
          String[] ftAddroute3,
          String[] wscomment,
          String[] salesform,
          String[] endurance,
          String[] wsspecialcontrol,
          String[] quality_level,
          String[] quality_level_comment,
          String[] component_no,
          String[] com_prod_body,
          String[] com_mask_option,
          String[] com_backend_option
          ) {

		String InsSQL=null;		
		Connection conn = null;		
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			InsSQL="update tf_bom_route_mcp_tx set ft_route=?, sort_route_code=?, ws_route=?, " +
			"ws_route_add=?, tf_comment=?, ft_route_add=?, ft_route_add2=?, ft_route_add3=? ,ft_route_code=?, tf_ws_comment=?, " +
			"sales_form=?, endurance=?, wsspecialcontrol=? , quality_level=?,quality_level_comment=?, fg_with_code=?, db_with_code=?, " +
			"component_no=?, com_prod_body=?, com_mask_option=?, com_backend_option=?" +
			"where id = ? and ft_route_code = ? and component_no = ? ";
			PreparedStatement ps2 = conn.prepareStatement(InsSQL);
			for (int i = 0; i < id.length; i++){			
				ps2.setString(1,ftwithcode[i]);
				ps2.setString(2,sortroutecode[i]);
				ps2.setString(3,wsroute[i]);
				ps2.setString(4,wsaddroute[i]);
				ps2.setString(5,StringUtil.Utf8ToBig5((comment[i].trim())));
				ps2.setString(6,ftAddroute[i]);
				ps2.setString(7,ftAddroute2[i]);
				ps2.setString(8,ftAddroute3[i]);
				ps2.setString(9,ftroutecode[i]);
				ps2.setString(10,StringUtil.Utf8ToBig5(wscomment[i].trim()));
				ps2.setString(11,salesform[i]);
				ps2.setString(12,endurance[i]);
				ps2.setString(13,wsspecialcontrol[i]);
				ps2.setString(14,quality_level[i]);
				ps2.setString(15,quality_level_comment[i]);
				ps2.setString(16,fgwithcode[i]);
				ps2.setString(17,dbwithcode[i]);
				ps2.setString(18,component_no[i]);
				ps2.setString(19,com_prod_body[i]);
				ps2.setString(20,com_mask_option[i]);
				ps2.setString(21,com_backend_option[i]);
				ps2.setString(22,id[i]);
				ps2.setString(23,ftroutecode[i]);
				ps2.setString(24,component_no[i]);
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
	  
  //將user要存的tf_bom_route_tx,tag=0的資料與table內的資料比對，如果與原本的資料不同就return true
  public static boolean CheckBomTagZero(Connection conn,
                                        String id,
                                        String ft_route,
                                        String ft_route_add,
                                        String sort_route_code,
                                        String ws_route,
                                        String ws_route_add,
                                        String tf_comment,
                                        String ftroutecode,
                                        String sales_form) throws Exception{

    StringBuffer SelSQL = new StringBuffer();
    SelSQL.append("SELECT count(1) as cnt FROM tf_bom_route_tx ");
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
    SelSQL.append(" and tf_comment ");
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
    SelSQL.append(" and sales_form ");
    if (sales_form.equals("")){
      SelSQL.append(" is null ");
    } else {
      SelSQL.append("="+"'" + sales_form + "'");
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

  /* Insert the duplicated row data into the tf_bom_route_tx (function of duplicate row) */
  public static boolean InsertBomDup(ProTestRouteBeanAF fm,
                                     String brand,
                                     String productbody,
                                     String beoption,
                                     String fgwithcode,
                                     String pincount,
                                     String pkgtype,
                                     String ftwithcode,
                                     String maskopt,
                                     String dbwithcode,
                                     String sortroutecode,
                                     String wsroute,
                                     String wsaddroute,
                                     String comment,
                                     String ft_route_code,
                                     String ft_route_add,
                                     String ft_route_add2,
                                     String ft_route_add3,
                                     String ws_comment,
                                     String sales_form,
                                     String endurance,
                                     String wsspecialcontrol,
                                     String quality_level,
                                     String quality_level_comment,
                                     String mcp_flag
                                     ) {
    String InsSQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      InsSQL="insert into tf_bom_route_tx (sid,tag,product_body,brand,version," +
          "backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option," +
          "sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,ft_route_code," +
          "ft_route_add,ft_route_add2,ft_route_add3,tf_ws_comment,sales_form,id,endurance,wsspecialcontrol,quality_level,quality_level_comment, mcp_flag) " +
          "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,test_seq.nextval,?,?,?,?,?)";
      PreparedStatement ps2 = conn.prepareStatement(InsSQL);
      ps2.setString(1,fm.getSid());
      ps2.setString(2,"1");
      ps2.setString(3,productbody);
      ps2.setString(4,brand);
      ps2.setString(5,fm.getVersion());
      ps2.setString(6,beoption);
      ps2.setString(7,fgwithcode);
      ps2.setString(8,pincount);
      ps2.setString(9,pkgtype);
//      ps2.setString(10,ftwithcode); 20070302 原來就不對
      ps2.setString(10,"");
      ps2.setString(11,maskopt);
      if(mcp_flag!=null && mcp_flag.equals("MCP"))
    	  ps2.setString(12,sortroutecode);
      else
    	  ps2.setString(12,"");
      ps2.setString(13,dbwithcode);
//      ps2.setString(14,wsroute);
//      ps2.setString(15,wsaddroute);
      ps2.setString(14,"");
      ps2.setString(15,"");
      ps2.setString(16,StringUtil.Utf8ToBig5(comment != null? comment.trim():""));
      ps2.setString(17,ft_route_code);
//      ps2.setString(18,ft_route_add);
      ps2.setString(18,"");
      ps2.setString(19,"");
      ps2.setString(20,"");
      ps2.setString(21,StringUtil.Utf8ToBig5(ws_comment != null? ws_comment.trim():""));
      ps2.setString(22,sales_form);
      ps2.setString(23,endurance);
      ps2.setString(24,wsspecialcontrol);
      ps2.setString(25,quality_level);
      ps2.setString(26,quality_level_comment);
      ps2.setString(27,mcp_flag);
      ps2.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      DBConnection.rollback(conn);
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return false;
  }

  public static boolean InsertBomDupMcp(ProTestRouteBeanAF fm,
          String brand,
          String productbody,
          String beoption,
          String fgwithcode,
          String pincount,
          String pkgtype,
          String ftwithcode,
          //String maskopt,
          String dbwithcode,
          String sortroutecode,
          String wsroute,
          String wsaddroute,
          String comment,
          String ft_route_code,
          String ft_route_add,
          String ft_route_add2,
          String ft_route_add3,
          String ws_comment,
          String sales_form,
          String endurance,
          String wsspecialcontrol,
          String quality_level,
          String quality_level_comment,
          //String mcp_flag
          String component_no, 
          String com_prod_body, 
          String com_mask_option, 
          String com_backend_option,
          int id) {
		String InsSQL=null;
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			InsSQL="insert into tf_bom_route_mcp_tx (sid,tag,product_body,brand,version," +
			//"backend_option,fg_with_code,pin_count,package_type,ft_route,mask_option," +
			"backend_option,fg_with_code,pin_count,package_type,ft_route," +
			"sort_route_code,db_with_code,ws_route,ws_route_add,tf_comment,ft_route_code," +
			"ft_route_add,ft_route_add2,ft_route_add3,tf_ws_comment,sales_form,endurance,wsspecialcontrol,quality_level,quality_level_comment," +
			"component_no,com_prod_body,com_mask_option,com_backend_option,id) " +
			"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps2 = conn.prepareStatement(InsSQL);
			ps2.setString(1,fm.getSid());
			ps2.setString(2,"1");
			ps2.setString(3,productbody);
			ps2.setString(4,brand);
			ps2.setString(5,fm.getVersion());
			ps2.setString(6,beoption);
			ps2.setString(7,fgwithcode);
			ps2.setString(8,pincount);
			ps2.setString(9,pkgtype);
			ps2.setString(10,"");
			ps2.setString(11,"");
			ps2.setString(12,dbwithcode);
			ps2.setString(13,"");
			ps2.setString(14,"");
			ps2.setString(15,StringUtil.Utf8ToBig5(comment != null? comment.trim():""));
			ps2.setString(16,ft_route_code);
			ps2.setString(17,"");
			ps2.setString(18,"");
			ps2.setString(19,"");
			ps2.setString(20,StringUtil.Utf8ToBig5(ws_comment != null? ws_comment.trim():""));
			ps2.setString(21,sales_form);
			ps2.setString(22,endurance);
			ps2.setString(23,wsspecialcontrol);
			ps2.setString(24,quality_level);
			ps2.setString(25,quality_level_comment);
			ps2.setString(26,component_no);
			ps2.setString(27,com_prod_body);
			ps2.setString(28,com_mask_option);
			ps2.setString(29,com_backend_option);
			ps2.setInt(30, id);
			ps2.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			DBConnection.rollback(conn);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return false;
	}  
  
  /* check if the duplicated row already existed in the tf_bom_route_table */
  public static boolean CheckExistBomDup(String sid,
                                         String backend_option,
                                         String fg_with_code,
                                         String pin_count,
                                         String package_type,
                                         String mask_option,
                                         String sort_route_code,
                                         String db_with_code,
                                         String ft_route_code,
                                         String ft_route_add,
                                         String ft_route_add2,
                                         String ft_route_add3,
                                         String sales_form,
                                         String endurance,
                                         String wsspecialcontrol) {

    String SQL=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();

      SQL = "select count(1) as cnt from tf_bom_route_tx " +
          "where sid=? and backend_option=? and fg_with_code=? and pin_count=? " +
          " and package_type=? and mask_option=? and sort_route_code=? and db_with_code=? " +
          " and ft_route_code=? and ft_route_add=? and ft_route_add2=? and ft_route_add3=? " +
          " and sales_form=? and endurance=? and wsspecialcontrol=?";

      PreparedStatement ps2 = conn.prepareStatement(SQL);
      ps2.setString(1, sid);
      ps2.setString(2,backend_option);
      ps2.setString(3,fg_with_code);
      ps2.setString(4,pin_count);
      ps2.setString(5,package_type);
      ps2.setString(6,mask_option);
      ps2.setString(7,sort_route_code);
      ps2.setString(8,db_with_code);
      ps2.setString(9,ft_route_code);
      ps2.setString(10,ft_route_add);
      ps2.setString(11,ft_route_add2);
      ps2.setString(12,ft_route_add3);
      ps2.setString(13,sales_form);
      ps2.setString(14,endurance);
      ps2.setString(15,wsspecialcontrol);
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

  public static boolean CheckExistBomDupMcp(String sid,
          String backend_option,
          String fg_with_code,
          String pin_count,
          String package_type,
          //String mask_option,
          String sort_route_code,
          String db_with_code,
          String ft_route_code,
          String ft_route_add,
          String ft_route_add2,
          String ft_route_add3,
          String sales_form,
          String endurance,
          String wsspecialcontrol,
          String component_no, 
          String com_prod_body, 
          String com_mask_option, 
          String com_backend_option) {

		String SQL=null;
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			
			SQL = "select count(1) as cnt from tf_bom_route_mcp_tx " +
				"where sid=? and backend_option=? and fg_with_code=? and pin_count=? " +
				//" and package_type=? and mask_option=? and sort_route_code=? and db_with_code=? " +
				" and package_type=? and sort_route_code=? and db_with_code=? " +
				" and ft_route_code=? and ft_route_add=? and ft_route_add2=? and ft_route_add3=? " +
				" and sales_form=? and endurance=? and wsspecialcontrol=? " +
				" and component_no=? and com_prod_body=? and com_mask_option=? and com_backend_option=?";
			
			PreparedStatement ps2 = conn.prepareStatement(SQL);
			ps2.setString(1, sid);
			ps2.setString(2,backend_option);
			ps2.setString(3,fg_with_code);
			ps2.setString(4,pin_count);
			ps2.setString(5,package_type);
			//ps2.setString(6,mask_option);
			ps2.setString(6,sort_route_code);
			ps2.setString(7,db_with_code);
			ps2.setString(8,ft_route_code);
			ps2.setString(9,ft_route_add);
			ps2.setString(10,ft_route_add2);
			ps2.setString(11,ft_route_add3);
			ps2.setString(12,sales_form);
			ps2.setString(13,endurance);
			ps2.setString(14,wsspecialcontrol);
			ps2.setString(15,component_no);
			ps2.setString(16,com_prod_body);
			ps2.setString(17,com_mask_option);
			ps2.setString(18,com_backend_option);
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
        String os_version = OiMaintainService.GetOSVersionBySidProgramId("pg_test_program", StringUtil.FormatData3(program_name[k]), StringUtil.FormatData3(pgm_id[k]));
        String previous_hw_configure = OiMaintainService.GetHWConfigureBySidProgramId("tf_test_parameter_ws_tx",fm.getSid(),previous_program_id);
        WsTestBean[] wtb = com.mxic.oiplus.oimaintain.OiMaintainService.GetTemperatureBySidProgramId("tf_test_parameter_ws_tx",fm.getSid(),Fir[0]);
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
        	if(wtb.length ==0 )
        		ps2.setString(13, os_version == null ? "" : "NETEST_M1 UI version : " + os_version);
        	else
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
      DelSQL.append("delete from tf_bom_route_tx ");
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
  
  public static boolean DeleteBomRouteMcpTx(String id, String sid) throws Exception {
	    StringBuffer DelSQL = new StringBuffer();
	    Connection conn = null;
	    try {
	      HashMap whereStem = new HashMap();
	      whereStem.put("id", id);
	      conn = DBConnection.getConnection();
	      DelSQL.append("delete from tf_bom_route_mcp_tx ");
	      DelSQL.append("where sid = ? \n");
	      DelSQL.append("and ft_route_code in ( SELECT ft_route_code FROM tf_bom_route_mcp_tx   \n");
	      DelSQL.append(SQLStem.getWhereStmt(whereStem)+")");
	      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
	      ps1.setString(1, sid);
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
      DelSQL.append("update tf_bom_route_tx set tag = '" + flag + "' ");
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

  public static boolean setBomRouteExpireMcp(String id, String flag, String sid) throws Exception {
	    StringBuffer DelSQL = new StringBuffer();
	    Connection conn = null;
	    try {
	      HashMap whereStem = new HashMap();
	      whereStem.put("id", id);
	      conn = DBConnection.getConnection();
	      DelSQL.append("update tf_bom_route_mcp_tx set tag = '" + flag + "' ");
	      //DelSQL.append(SQLStem.getWhereStmt(whereStem));
	      DelSQL.append("where sid = ? \n");
	      DelSQL.append("and ft_route_code in ( SELECT ft_route_code FROM tf_bom_route_mcp_tx   \n");
	      DelSQL.append(SQLStem.getWhereStmt(whereStem)+")");
	      //DelSQL.append("where id in ( " + id + " ) ");
	      TDSLogger.println(DelSQL.toString());
	      PreparedStatement ps1 = conn.prepareStatement(DelSQL.toString());
	      ps1.setString(1, sid);
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
  
  //delete the data of the given sid(product_body and brand) in tf_bom_route_tx
  // and set  tf_bom_route='N' in tf_information
  public static boolean ResetBomRouteTx(String sid) throws Exception {
    String sql=null;
    String upt=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      sql = "delete from tf_bom_route_tx where sid=? ";
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

  public static boolean ResetBomRouteMcpTx(String sid) throws Exception {
    String sql=null;
    String upt=null;
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      sql = "delete from tf_bom_route_mcp_tx where sid=? ";
      upt = "update tf_information set tf_bom_mcp_route='N' where sid=?";
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
  
//updtae the data of the given product_body and brand in tf_prod_waferlevel_tx
  public static boolean UpdateProdWaferlevel(String sid,
                                          String[] wafer_level,
                                          String[] revise_priority,
                                          String[] checked_flag) throws Exception {

    StringBuffer UptSQL = new StringBuffer();
    Connection conn = null;
    int i;
    try {
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      UptSQL.append("update tf_prod_waferlevel_tx ");
      UptSQL.append("set revise_priority=?, checked_flag=? where sid=? and wafer_level=? ");
      PreparedStatement ps1 = conn.prepareStatement(UptSQL.toString());

      for (i = 0; i < wafer_level.length; i++){
        ps1.setString(1,StringUtil.Utf8ToBig5(revise_priority[i].trim()));
        ps1.setString(2,StringUtil.Utf8ToBig5(checked_flag[i]));
        ps1.setString(3,sid);
        ps1.setString(4,wafer_level[i]);
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
      SelSQL.append("SELECT * FROM tf_bom_route_tx  ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        BomProductRouteBean bom = new BomProductRouteBean();
        bom.setTag(rs.getString("tag"));
        bom.setSid(rs.getString("sid"));
        bom.setVersion(rs.getString("version"));
        bom.setBrand(rs.getString("brand"));
        bom.setProductbody(rs.getString("product_body"));
        bom.setBeoption(rs.getString("backend_option"));
        bom.setFgwithcode(rs.getString("fg_with_code"));
        bom.setPincount(rs.getString("pin_count"));
        bom.setPkgtype(rs.getString("package_type"));
        bom.setFtroute(rs.getString("ft_route"));
        bom.setMaskopt(rs.getString("mask_option"));
        bom.setDbwithcode(rs.getString("db_with_code"));
        bom.setSortroutecode(rs.getString("sort_route_code"));
        bom.setWsroute(rs.getString("ws_route"));
        bom.setWsaddroute(rs.getString("ws_route_add"));
        bom.setComment(rs.getString("tf_comment"));
        bom.setTf_ws_comment(rs.getString("tf_ws_comment"));
        bom.setId(rs.getString("id"));
        bom.setFt_route_code(rs.getString("ft_route_code"));
        bom.setFtAddroute(rs.getString("ft_route_add"));
        bom.setFtAddroute2(rs.getString("ft_route_add2"));
        bom.setFtAddroute3(rs.getString("ft_route_add3"));
        bom.setSales_form(rs.getString("sales_form"));
        bom.setEndurance(rs.getString("endurance"));
        bom.setMcp_flag(rs.getString("mcp_flag"));
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
  
  public static BomProductRouteBean[] SearchBomMcpByID(String id, String sid) {
	    StringBuffer SelSQL = new StringBuffer();
	    Connection conn = null;
	    try {
	      ArrayList tmp2 = new ArrayList();
	      HashMap whereStem = new HashMap();
	      whereStem.put("id", id);
	      conn = DBConnection.getConnection();
	      SelSQL.append("SELECT * FROM tf_bom_route_mcp_tx  ");
	      //SelSQL.append(SQLStem.getWhereStmt(whereStem));
	      SelSQL.append("where sid = ? \n");
   		  SelSQL.append("and ft_route_code in ( SELECT ft_route_code FROM tf_bom_route_mcp_tx   \n");
   		  SelSQL.append(SQLStem.getWhereStmt(whereStem)+")");
	      SelSQL.append("\norder by backend_option, ft_route_code, component_no ");
	      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
	      ps.setString(1, sid);
	      ResultSet rs = ps.executeQuery();

	      while (rs.next()) {
	        BomProductRouteBean bom = new BomProductRouteBean();
	        bom.setTag(rs.getString("tag"));
	        bom.setSid(rs.getString("sid"));
	        bom.setVersion(rs.getString("version"));
	        bom.setBrand(rs.getString("brand"));
	        bom.setProductbody(rs.getString("product_body"));
	        bom.setBeoption(rs.getString("backend_option"));
	        bom.setFgwithcode(rs.getString("fg_with_code"));
	        bom.setPincount(rs.getString("pin_count"));
	        bom.setPkgtype(rs.getString("package_type"));
	        bom.setFtroute(rs.getString("ft_route"));
	        //bom.setMaskopt(rs.getString("mask_option"));
	        bom.setDbwithcode(rs.getString("db_with_code"));
	        bom.setSortroutecode(rs.getString("sort_route_code"));
	        bom.setWsroute(rs.getString("ws_route"));
	        bom.setWsaddroute(rs.getString("ws_route_add"));
	        bom.setComment(rs.getString("tf_comment"));
	        bom.setTf_ws_comment(rs.getString("tf_ws_comment"));
	        bom.setId(rs.getString("id"));
	        bom.setFt_route_code(rs.getString("ft_route_code"));
	        bom.setFtAddroute(rs.getString("ft_route_add"));
	        bom.setFtAddroute2(rs.getString("ft_route_add2"));
	        bom.setFtAddroute3(rs.getString("ft_route_add3"));
	        bom.setSales_form(rs.getString("sales_form"));
	        bom.setEndurance(rs.getString("endurance"));
	        
			bom.setComponent_no(rs.getString("component_no"));
			bom.setCom_prod_body(rs.getString("com_prod_body"));
			bom.setCom_mask_option(rs.getString("com_mask_option"));
			bom.setCom_backend_option(rs.getString("com_backend_option"));
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
                    //"			DECODE((SELECT VERSION FROM PG_TEST_PROGRAM WHERE PROGRAM_MODE ='PROD'\n" +
                    //"         		       AND PROGRAM_ID = TF_TEST_PARAMETER_WS_TX.PGM_ID),'A',\n" +
                    "           DECODE((SELECT BB.PREVIOUS_PROGRAM_ID\n" +
                    "                                  FROM PG_TEST_PROGRAM AA, PG_PREVIOUS_PROGRAM BB\n" + 
                    "                                 WHERE AA.PROGRAM_MODE = BB.PROGRAM_MODE AND AA.PROGRAM_MODE = 'PROD' \n" + 
                    "                                   AND AA.PROGRAM_ID = BB.PROGRAM_ID\n" + 
                    "                                   AND AA.PROGRAM_ID = TF_TEST_PARAMETER_WS_TX.PGM_ID\n" + 
                    "                                   AND BB.CATEGORY IN (1,2)),null,\n" +
                    "     		   	   (SELECT B.TEMP FROM GPRS_BA_PRODUCT A, GPRS_BA_PGM_TEMP B, PG_TEST_PROGRAM C\n" + 
                    "		       		 WHERE A.PROCESS = B.PROCESS AND A.FUNCTION = B.FUNCTION\n" + 
                    "			           AND A.TYPE = B.TYPE AND A.FAMILY_CODE = B.FAMILY_CODE\n" + 
                    "         			   AND C.PROGRAM_MODE='PROD'\n" + 
                    "		        	   AND C.PROGRAM_ID = TF_TEST_PARAMETER_WS_TX.PGM_ID\n" + 
                    "         		   	   AND C.TEST_MODE = B.TEST_MODE\n" + 
                    "         		   	   AND A.PRODUCT_CODE = C.PRODUCT_CODE),\n" + 
                    "			   	   (SELECT DISTINCT B.TEMPERATURE FROM TF_TEST_PARAMETER_WS B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = TF_TEST_PARAMETER_WS_TX.PRODUCT_BODY AND B.BRAND = TF_TEST_PARAMETER_WS_TX.BRAND AND B.VERSION = TF_TEST_PARAMETER_WS_TX.VERSION -1  AND TF_TEST_PARAMETER_WS_TX.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD' AND ROWNUM = 1))) AS TEMPERATURE,\n" + 
                    "       NVL(TF_TEST_PARAMETER_WS_TX.HW_CONFIGURE,(SELECT DISTINCT B.HW_CONFIGURE FROM TF_TEST_PARAMETER_WS B, PG_PREVIOUS_PROGRAM C  WHERE B.PRODUCT_BODY = TF_TEST_PARAMETER_WS_TX.PRODUCT_BODY AND B.BRAND = TF_TEST_PARAMETER_WS_TX.BRAND AND B.VERSION = TF_TEST_PARAMETER_WS_TX.VERSION -1  AND TF_TEST_PARAMETER_WS_TX.PGM_ID = C.PROGRAM_ID AND C.CATEGORY IN (1,2) AND C.PREVIOUS_PROGRAM_ID = B.PGM_ID AND C.PROGRAM_MODE = 'PROD' AND C.PREVIOUS_PROGRAM_MODE = 'PROD' AND ROWNUM = 1 )) AS HW_CONFIGURE\n" + 
                    "  FROM TF_TEST_PARAMETER_WS_TX ");
      SelSQL.append(SQLStem.getWhereStmt(whereStem));
      SelSQL.append("order by mask_option,test_type,tester,site,program_name ");
      TDSLogger.println(SelSQL.toString());
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
      //20111130DBConnection.close(conn);
      //20111130conn = null;
    }
    return null;
  }
  
  public static WsTestBean[] SelectAllFromAll(String sid, Connection conn, String pro_b, String br, String version, String type) {

      StringBuffer SelSQL = new StringBuffer();
      try {
          ArrayList tmp2 = new ArrayList();
          HashMap whereStem = new HashMap();
          whereStem.put("sid", sid);
          SelSQL.append("SELECT * FROM tf_test_parameter_"+type);
          SelSQL.append(SQLStem.getWhereStmt(whereStem));
          
          PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
              WsTestBean wtb = new WsTestBean();
              wtb.setSid(sid);
              wtb.setPgm_id(rs.getString("pgm_id"));
              tmp2.add(wtb);
          }
          return (WsTestBean[]) tmp2.toArray(new WsTestBean[0]);
      } catch (Exception ex) {
          ex.printStackTrace();
      } finally {
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
      SelSQL.append("'"+pro_b+"'");
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
  
  public static String GetOSVersionBySidProgramId(String table, String pgm_name, String pgm_id) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		String os_version = null;
		try {
			conn = DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			whereStem.put("program_name", pgm_name);
			whereStem.put("program_id", pgm_id);
			SelSQL.append("SELECT OS_VERSION FROM " + table);
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			TDSLogger.println(SelSQL.toString());
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				os_version = rs.getString("OS_VERSION");
			}
			return os_version;
		} catch (Exception ex) {
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
        "and tester=? and site=? and program_name=?  ";//and pgm_special_control=? and one_main_pgm_group_version=?
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
// chage > 10 to >=0, include TEST1, 20080625
      SelSQL.append("WHERE v.site = p.plant_name and p.plant_no >= 0");

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
  
  public static WsTestBean[] VendorNameTX(String sid) {
	    StringBuffer SelSQL = new StringBuffer();

	    Connection conn = null;

	    try {
	      ArrayList tmp2 = new ArrayList();
	      conn = DBConnection.getConnection();
	      SelSQL.append("SELECT DISTINCT v.site, p.tim_short_name FROM " +
	                    "(SELECT DISTINCT site FROM tf_test_parameter_ws_tx ");
	      SelSQL.append(" WHERE sid = "+sid);
	      SelSQL.append(" UNION SELECT DISTINCT site FROM tf_test_parameter_ft_tx ");
	      SelSQL.append(" WHERE sid = "+sid);
	      SelSQL.append(" UNION SELECT DISTINCT site FROM tf_test_parameter_pbc_tx ");
	      SelSQL.append(" WHERE sid = "+sid+") v, ba_plant p ");
	// chage > 10 to >=0, include TEST1, 20080625
	      SelSQL.append("WHERE v.site = p.plant_name and p.plant_no >= 0");

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

  public static String getPreviousVersionSid(Connection conn, String sid) {
    String previousSid = null;
    StringBuffer sqlStmt = new StringBuffer();
    sqlStmt.append("SELECT T1.SID FROM TF_INFORMATION T1, TF_INFORMATION T2 ");
    sqlStmt.append("WHERE T1.PRODUCT_BODY = T2.PRODUCT_BODY AND T1.BRAND = T2.BRAND ");
    sqlStmt.append("AND T1. VERSION = T2.VERSION - 1 AND T2.SID = " + sid);

    //Connection conn = null;
    try {
      //conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        previousSid = rs.getString("SID");
        return previousSid;
      }
    } catch (Exception e) {
      TDSLogger.println(e);
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return null;
  }

  public static String getCreator(String sid){
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT CREATOR FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE SID = " + sid);
	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();
		  if(rs.next()) {
			  result = rs.getString("CREATOR");
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
	  
  }
  public static String getStatus(String sid){
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT STATUS FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE SID = " + sid);
	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();
		  if(rs.next()) {
			  result = rs.getString("STATUS");
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
	  
  }
  
  public static String getProductType(String sid) {
	  String result = null;
	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  result = getProductType(conn, sid);
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }
  public static String getProductType(Connection conn, String sid) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT PRODUCT_TYPE FROM TF_INFORMATION ");
	  sqlStmt.append("WHERE SID = " + sid);

	  //Connection conn = null;
	  try {
		  //conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("PRODUCT_TYPE");
			  break;
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  //DBConnection.close(conn);
		  //conn = null;
	  }
	  return result;
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

  
  
  public static String getPackageComponent(Connection conn , String productBody, String brand) {
	  String result = "";
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("select distinct package_component from tf_prod_epn \n");
	  sqlStmt.append("where product_body = '" + productBody + "' and brand = '" + brand + "'" );

	 
	  try {
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  if (rs.next()) {
			  result = rs.getString("PACKAGE_COMPONENT");
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  }
	  return result;
  }  
  
  public static String getPackageComponentOld(String productBody, String brand) {
	  String result = "";
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("select distinct package_component from tf_prod_epn \n");
	  sqlStmt.append("where product_body = '" + productBody + "' and brand = '" + brand + "'" );

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  if (rs.next()) {
			  result = rs.getString("PACKAGE_COMPONENT");
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
  public static Vector getTemperatureAEBList() {
      Vector result = new Vector();
      StringBuffer sqlStmt = new StringBuffer();
      sqlStmt.append("SELECT DESCRIPTION TEMPERATURE FROM TF_DESCRIPTION ");
      sqlStmt.append("WHERE TAG = 24  ");
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
  public static String getTemperatureString(Vector temperatureList, String temperature, int flag) {
	  StringBuffer result = new StringBuffer();
	  if(temperature!=null){
	    String temperatureArray[] = temperature.split(";");
	    for (int i=0; i<temperatureArray.length; i++) {
		    if(temperatureArray[i].indexOf("NA") < 0){
			    result.append(temperatureArray[i]);
			    if(temperatureArray[i].indexOf("ROOM TEMP") < 0 && temperatureArray[i].indexOf("NA") < 0 && !temperatureArray[i].equals("")) {
			        result.append("℃");
			    }
			    result.append("<br>");

		    
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

  public static String getVendorShortName(Connection conn, String vendor) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT TIM_SHORT_NAME FROM BA_PLANT ");
	  sqlStmt.append("WHERE PLANT_NAME = '" + vendor + "'");

	  //Connection conn = null;
	  try {
		  //conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("TIM_SHORT_NAME");
			  break;
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  //DBConnection.close(conn);
		  //conn = null;
	  }
	  return result;
  }
  public static String[] getEcrEffectTime(Connection conn, String product_body, String version, String brand) {
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

	  //Connection conn = null;
	  try {
		  //conn = DBConnection.getConnection();
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
		  //DBConnection.close(conn);
		  //conn = null;
	  }
	  return result;
  }

  public static String[] getTestModes() {
          Connection conn = null;

          try {
                  String sql = "select description test_mode from tf_description " +
                      "where tag in (0,1) " +
                      "and substr(description,1,1) in ('S','F','T') and length(description)<=6 " +
                      "order by tag,description";

                  conn = DBConnection.getConnection();
                  PreparedStatement ps = conn.prepareStatement(sql);
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

  public static String[] getGoodBinList(String product) {
            Connection conn = null;

            try {
                    String sql = "select distinct bin_no,'BIN'||trim(to_char(bin_no)) bin \n"+
                        "from ba_bin_ws where product_code like '" + product +"%'\n" +
                        "union select distinct bin_no,'BIN'||trim(to_char(bin_no)) bin \n" +
                        "from ba_bin_ft where product_code like '" + product +"%'\n" +
                        "order by bin_no";

                    conn = DBConnection.getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    ArrayList tmp = new ArrayList();
                    while (rs.next()) {
                            String testerType = rs.getString("BIN");
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
            } finally {
                    DBConnection.close(conn);
                    conn = null;
            }
            return null;
    }
  public static String[][] getDBBinList(String product,String mode, String dbbin) {
      Connection conn = null;
      boolean f = false;
      try {
          StringBuffer sql = new StringBuffer();
          String n_mode = mode.startsWith("SORT")?mode.replaceAll("SORT", "S"):mode.replaceAll("TQAE", "FT");
          
          if(n_mode.startsWith("S")) {
              sql.append( 
                      "SELECT DISTINCT C.BIN_NO, 'BIN' || TRIM(TO_CHAR(C.BIN_NO)) BIN, 'NA'  IF_BIN\n" +
                              "  FROM BA_BIN_WS C,\n" + 
                              "       (SELECT A.PRODUCT_CODE, MAX(A.VERSION) VSN\n" + 
                              "          FROM BA_BIN_WS A\n" + 
                              "         WHERE A.VERSION != 'EA'\n" + 
                              "         AND A.PRODUCT_CODE LIKE '" + product +"%'\n" +
                              "         AND A.TEST_MODE = '" + n_mode + "'\n" +
                              "         GROUP BY PRODUCT_CODE) B\n" + 
                              " WHERE C.PRODUCT_CODE LIKE '" + product +"%'\n" +
                              "   AND C.TEST_MODE = '" + n_mode + "'\n" +
                              "   AND C.PRODUCT_CODE = B.PRODUCT_CODE\n" + 
                              "   AND C.VERSION = B.VSN\n" + 
                              "   AND C.VERSION != 'EA'\n" + 
                              " ORDER BY C.BIN_NO");

          }else if(n_mode.startsWith("FT")){
              sql.append( 
                      "SELECT DISTINCT C.BIN_NO, 'BIN' || TRIM(TO_CHAR(C.BIN_NO)) BIN, 'IB' || TRIM(TO_CHAR(C.IF_BIN_NO)) IF_BIN\n" +
                              "  FROM BA_BIN_FT C,\n" + 
                              "       (SELECT A.PRODUCT_CODE, MAX(A.VERSION) VSN\n" + 
                              "          FROM BA_BIN_FT A\n" + 
                              "         WHERE A.VERSION != 'EA'\n" + 
                              "         AND A.PRODUCT_CODE LIKE '" + product +"%'\n" +
                              "         AND A.TEST_MODE = '" + n_mode + "'\n" +
                              "         GROUP BY PRODUCT_CODE) B\n" + 
                              " WHERE C.PRODUCT_CODE LIKE '" + product +"%'\n" + 
                              "   AND C.TEST_MODE = '" + n_mode + "'\n");
                              if(dbbin !=null && dbbin.length() > 0 && !dbbin.equals("null"))
                                  sql.append("   AND 'BIN'||C.BIN_NO = '" + dbbin + "'\n");    
                              sql.append("   AND C.PRODUCT_CODE = B.PRODUCT_CODE\n" + 
                              "   AND C.VERSION = B.VSN\n" + 
                              "   AND C.VERSION != 'EA'\n" + 
                              " ORDER BY C.BIN_NO");
          }else {
              return null;
          }

          conn = DBConnection.getConnection();
          PreparedStatement ps = conn.prepareStatement(sql.toString());
          ResultSet rs = ps.executeQuery();
          ArrayList tmp = new ArrayList();
          ArrayList tmp2 = new ArrayList();
          String[][] rnt = new String[2][];
          while (rs.next()) {
              String testerType = rs.getString("BIN");
              String testerType2 = rs.getString("IF_BIN");
              if(f == false) {
                  tmp.add("--------");
                  f= true;
              }
              if(!tmp.contains(testerType))
                  tmp.add(testerType);
              if(!tmp2.contains(testerType2))
                  tmp2.add(testerType2);
          }
          if (tmp.isEmpty() || tmp2.isEmpty()) {
              return null;
          } else {
              rnt[0] = (String[]) tmp.toArray(new String[0]);
              rnt[1] = (String[]) tmp2.toArray(new String[0]);
              return rnt;
          }
      } catch (Exception ex) {
          ex.fillInStackTrace();
          TDSLogger.println(ex.getMessage());
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
                  sql.append("WHERE TAG = " + Tag);
                  sql.append("  AND DELETE_FLAG is null ");
                  sql.append(" ORDER BY ID ");
                  conn = DBConnection.getConnection();
                  TDSLogger.println(sql.toString());
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
  
  public static String[] getDescriptionByDesc(String Tag) {
      Connection conn = null;

      try {
              StringBuffer sql = new StringBuffer();

              sql.append("SELECT description FROM TF_DESCRIPTION ");
              sql.append("WHERE TAG = " + Tag);
              sql.append("  AND DELETE_FLAG is null ");
              sql.append(" ORDER BY description ");
              conn = DBConnection.getConnection();
              TDSLogger.println(sql.toString());
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
  

  
  public static String[] getProdWaferLevel(String sid) {
      Connection conn = null;

      try {
              StringBuffer sql = new StringBuffer();

              sql.append("SELECT 'Dgrade-' || WAFER_LEVEL as WAFER_LEVEL, revise_priority FROM tf_prod_waferlevel_tx ");
              sql.append("WHERE sid = " + sid);
              sql.append("  AND checked_flag = 'Y' ");
              //sql.append("  UNION  ");
              //sql.append("SELECT 'Follow Hold Criteria' AS WAFER_LEVEL, 19 revise_priority FROM DUAL ");	//BE#2022000045 - 8049 Hold 及Dgrade criteria 改善需求
              sql.append("  UNION  ");
              //sql.append("SELECT 'Change IPN' AS WAFER_LEVEL, 20 revise_priority FROM DUAL, TF_DESCRIPTION T, TF_INFORMATION T2 WHERE T.TAG = 502 ");
              //sql.append("AND T2.SID = " + sid + " AND T2.PRODUCT_BODY = SUBSTR(T.DESCRIPTION,0,4) AND T.DELETE_FLAG IS NULL order by 2 desc ");
              sql.append("SELECT LEVEL_NAME, (PRIORITY+19) REVISE_PRIORITY FROM TF_CHANGEIPN_LEVEL order by 2 desc");
              conn = DBConnection.getConnection();
              TDSLogger.println(sql.toString());
              PreparedStatement ps = conn.prepareStatement(sql.toString());
              ResultSet rs = ps.executeQuery();
              ArrayList tmp = new ArrayList();
              while (rs.next()) {
                      String WAFER_LEVEL = rs.getString("WAFER_LEVEL");
                      tmp.add(WAFER_LEVEL);
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
  public static String[] getProdWaferLevelPriority(String sid) {
      Connection conn = null;

      try {
              StringBuffer sql = new StringBuffer();

              sql.append("SELECT 'Dgrade-' || WAFER_LEVEL as WAFER_LEVEL, revise_priority, revise_priority+10 revise_priority_new FROM tf_prod_waferlevel_tx ");
              sql.append("WHERE sid = " + sid);
              sql.append("  AND checked_flag = 'Y' ");
              sql.append("  UNION  ");
              sql.append("SELECT 'Follow Hold Criteria' AS WAFER_LEVEL, 19 revise_priority, 19 revise_priority_new FROM DUAL ");
              sql.append("  UNION  ");
              //sql.append("SELECT 'Change IPN' AS WAFER_LEVEL, 20 revise_priority, 20 revise_priority_new FROM DUAL, TF_DESCRIPTION T, TF_INFORMATION T2 WHERE T.TAG = 502 ");
              //sql.append("AND T2.SID = " + sid + " AND T2.PRODUCT_BODY = SUBSTR(T.DESCRIPTION,0,4) AND T.DELETE_FLAG IS NULL order by 2 desc ");
              sql.append("SELECT LEVEL_NAME, (PRIORITY+19) REVISE_PRIORITY, (PRIORITY+19) REVISE_PRIORITY_NEW FROM TF_CHANGEIPN_LEVEL ");
              sql.append("order by 2 desc");
              conn = DBConnection.getConnection();
              TDSLogger.println(sql.toString());
              PreparedStatement ps = conn.prepareStatement(sql.toString());
              ResultSet rs = ps.executeQuery();
              ArrayList tmp = new ArrayList();
              while (rs.next()) {
                      String revise_priority = rs.getString("revise_priority_new");
                      tmp.add(revise_priority);
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
  public static String[] getId(String Tag) {
      Connection conn = null;

      try {
              StringBuffer sql = new StringBuffer();

              sql.append("SELECT id FROM TF_DESCRIPTION ");
              sql.append("WHERE TAG = " + Tag);
              sql.append("  AND DELETE_FLAG is null ");
              sql.append(" ORDER BY ID ");
              conn = DBConnection.getConnection();
              TDSLogger.println(sql.toString());
              PreparedStatement ps = conn.prepareStatement(sql.toString());
              ResultSet rs = ps.executeQuery();
              ArrayList tmp = new ArrayList();
              while (rs.next()) {
                      String ID = rs.getString("ID");
                      tmp.add(ID);
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

  // for Yield Definition use
  public static String[] getDescription(String Tag, String proc_type) {
      Connection conn = null;
      int proc_loc = (proc_type.equals("WS")?40:proc_type.equals("FT_AEB")?48:proc_type.equals("WS_Dgrade")?47:45);

      try {
              StringBuffer sql = new StringBuffer();

              sql.append("SELECT a.description FROM TF_DESCRIPTION a, TF_DESCRIPTION b ");
              sql.append("\nWHERE a.TAG = " + Tag + " AND b.tag = " + proc_loc);
              sql.append("\nAND a.id = b.id ");
              sql.append("\nAND B.DELETE_FLAG IS NULL  ");//20181206
              sql.append("\nORDER BY a.ID ");
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
 
  
  //in this method, you can choose option by group 20120716- ken
  public static String[] getDescription(Connection conn, String Tag,String Tag2, String des) {
      //Connection conn = null;

      try {
              StringBuffer sql = new StringBuffer();

              sql.append("SELECT a.description FROM TF_DESCRIPTION a, TF_DESCRIPTION b ");
              sql.append("\nWHERE a.TAG = " + Tag + " AND b.tag = " + Tag2);
              sql.append("\nAND a.id = b.id ");
              sql.append("\nAND b.description in ("+des+") AND a.DELETE_FLAG is null ORDER BY a.ID ");
              //conn = DBConnection.getConnection();
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
              //DBConnection.close(conn);
              //conn = null;
      }
      return null;
  }
  
  /*not nvm && not ws*/
  public static String[] getHCSACTION(Connection conn) {
      return getDescription(conn, "44","46","'1hold_criteria_setting'");
  }
  
  public static String[] getACSACTION(Connection conn) {
      return getDescription(conn, "44","46","'2action_criteria_setting'");
  }
  
  public static String[] getHCSACTION() {
	  Connection conn = null;
	  try {  
		  conn = DBConnection.getConnection();
		  return getDescription(conn, "44","46","'1hold_criteria_setting'");
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
  
  public static String[] getACSACTION() {
	  Connection conn = null;
	  try {  
		  conn = DBConnection.getConnection();
		  return getDescription(conn, "44","46","'2action_criteria_setting'");
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

  // for Yield Definition use
  public static String[] getID(String Tag, String proc_type) {
      Connection conn = null;
      int proc_loc = (proc_type.equals("WS")?40:proc_type.equals("FT_AEB")?48:proc_type.equals("WS_Dgrade")?47:45);

      try {
              StringBuffer sql = new StringBuffer();

              sql.append("SELECT a.id FROM TF_DESCRIPTION a, TF_DESCRIPTION b ");
              sql.append("\nWHERE a.TAG = " + Tag + " AND b.tag = " + proc_loc);
              sql.append("\nAND a.id = b.id ");
              sql.append("\nAND B.DELETE_FLAG IS NULL  ");//20181206
              sql.append("\nORDER BY a.ID ");
              conn = DBConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql.toString());
              ResultSet rs = ps.executeQuery();
              ArrayList tmp = new ArrayList();
              while (rs.next()) {
                      String DESCRIPTION = rs.getString("ID");
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
  
  public static String[] getBIN_Type_ID() {
	    return getId("11");
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
  
  public static String getGroupKey(String productType, String product_body, String brand, String option, String package_code,
                  String pin_count, String with_code, String route, String add_route, String sales_form, int facility) {
          String curGroupKey = null;
          if ((add_route == null) || add_route.equals("null"))
                          add_route = "";
          if (facility == 0) {
                  if (productType.equals("ASM")) {
                          String buf = null;
                          if ((sales_form != null) && sales_form.equals("W"))
                                  buf = " for wafer sale";
                          else
                                  buf = " for package sale";
                          curGroupKey = product_body + option + buf;
                  } else
                          //curGroupKey = product_body + option + with_code +  route + add_route;
                	      curGroupKey = product_body + option + with_code +  route ;
          } else {
                  if (productType.equals("ASM"))
                          curGroupKey = product_body + option;
                  else
                          //curGroupKey = product_body + option + with_code + pin_count + package_code + route + add_route;
                	      curGroupKey = product_body + option + with_code + pin_count + package_code + route ;
          }
          return curGroupKey;
  }

  public static String getSpeedList(Connection conn, String product_body, String brand, String version, String route, String add_route, String table, String facility) {
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

          //Connection conn = null;
          try {
                  //conn = DBConnection.getConnection();
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
                  //DBConnection.close(conn);
                  //conn = null;
          }
          return speedList;
  }
  public static String getMaxsite(Connection conn, String pgm_name, String pgm_id) {
	  StringBuffer SelSQL = new StringBuffer();
		String max_site = null;
		try {
			//conn = DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			whereStem.put("program_mode", pgm_name);
			whereStem.put("program_id", pgm_id);
			SelSQL.append("SELECT MAX_SITE FROM PG_TEST_PROGRAM ");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			TDSLogger.println(SelSQL.toString());
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				max_site = rs.getString("MAX_SITE");
			}
			return max_site;
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
		}
		return null;
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
	  sqlStmt.append("SELECT MAX(FT_ROUTE_CODE) FT_ROUTE_CODE FROM TF_BOM_ROUTE_TX ");
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
  public static String getMaxSortRouteCode(String product_body, String brand) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT MAX(SORT_ROUTE_CODE) SORT_ROUTE_CODE FROM( \n");
	  sqlStmt.append("SELECT MAX(SORT_ROUTE_CODE) SORT_ROUTE_CODE FROM TF_BOM_ROUTE_TX \n");
	  sqlStmt.append("WHERE PRODUCT_BODY = '" + product_body + "'\n");
	  sqlStmt.append("and brand = '" + brand + "'\n");
	  sqlStmt.append("UNION \n");
	  sqlStmt.append("SELECT MAX(Sort_ROUTE_CODE) SORT_ROUTE_CODE FROM TF_BOM_ROUTE \n");
	  sqlStmt.append("WHERE PRODUCT_BODY = '" + product_body + "'\n");
	  sqlStmt.append("and brand = '" + brand + "'\n");
	  sqlStmt.append(")");

	  Connection conn = null;
	  try {
		  conn = DBConnection.getConnection();
		  PreparedStatement ps = conn.prepareStatement(sqlStmt.toString());
		  ResultSet rs = ps.executeQuery();

		  while (rs.next()) {
			  result = rs.getString("SORT_ROUTE_CODE");
		  }
	  } catch (Exception e) {
		  TDSLogger.println(e);
	  } finally {
		  DBConnection.close(conn);
		  conn = null;
	  }
	  return result;
  }

  public static String getMaxFTRouteCodeMcp(String sid) {
	  String result = null;
	  StringBuffer sqlStmt = new StringBuffer();
	  sqlStmt.append("SELECT MAX(FT_ROUTE_CODE) FT_ROUTE_CODE FROM TF_BOM_ROUTE_MCP_TX ");
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
  
  /* NON-COMPLETED
     Info in Database is cleared
     But the related files are not clean yet */

  public static boolean getWip(String sid, String pb_body, String optionlist, String ctrl_type) {
    StringBuffer wipSQL = new StringBuffer();
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      wipSQL.append("call TF_GET_WIP("+sid+",'"+pb_body+"','"+optionlist+"','"+ctrl_type+"')");
      PreparedStatement ps1 = conn.prepareStatement(wipSQL.toString());
      ps1.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return true;
  }

  public static boolean saveWip(String sid, String pb_body, String optionlist, String ctrl_type,
                                String steps,
                                String pgname1[], String pgname2[], String pgname3[],
                                String pgname4[], String pgname5[],
                                String p1[], String p2[], String p3[],
                                String p4[], String p5[], String flag)
  {
    StringBuffer wipSQL = new StringBuffer();
    Connection conn = null;
    try {
      // remove old data
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      wipSQL.append("DELETE FROM tf_wip_tx ");
      wipSQL.append(SQLStem.getWhereStmt(whereStem));
      conn = DBConnection.getConnection();
      conn.setAutoCommit(false);
      PreparedStatement ps1 = conn.prepareStatement(wipSQL.toString());
      PreparedStatement ps2 = null;
      PreparedStatement ps3 = null;
      ps1.executeUpdate();

      // insert new data
      wipSQL.delete(0,wipSQL.length());
      if (ctrl_type.equals("3-1")) {
        wipSQL.append("insert into tf_wip_tx values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
        ps2 = conn.prepareStatement(wipSQL.toString());
        for (int i=0; i < pgname1.length; i++) {
          ps2.setInt(1, (int) Integer.parseInt(sid));
          ps2.setString(2, optionlist);
          ps2.setString(3, ctrl_type);
          ps2.setString(4, pgname1[i]);
          ps2.setString(5, pgname2[i]);
          ps2.setString(6, pgname3[i]);
          ps2.setString(7, pgname4[i]);
          ps2.setString(8, pgname5[i]);
          if (p1[i].equals("")) ps2.setInt(9, 0); else ps2.setInt(9, 1);
          if (p2[i].equals("")) ps2.setInt(10, 0); else ps2.setInt(10, 1);
          if (p3[i].equals("")) ps2.setInt(11, 0); else ps2.setInt(11, 1);
          if (p4[i].equals("")) ps2.setInt(12, 0); else ps2.setInt(12, 1);
          if (p5[i].equals("")) ps2.setInt(13, 0); else ps2.setInt(13, 1);
          ps2.executeUpdate();
        }

      } else {
        wipSQL.append("insert into tf_wip_tx values (?,?,?,?,null,null,null,null,0,0,0,0,0)");
        ps2 = conn.prepareStatement(wipSQL.toString());
        ps2.setInt(1,(int)Integer.parseInt(sid));
        ps2.setString(2,optionlist);
        ps2.setString(3,ctrl_type);
        if (ctrl_type.equals("2-4-1"))
          ps2.setString(4,steps);
        else
          ps2.setString(4,"");
        ps2.executeUpdate();
      }

      if (flag.equals("submit")) {
        //submit
        wipSQL.delete(0,wipSQL.length());
        wipSQL.append("UPDATE TF_INFORMATION SET TF_WIP_CONTROL = 'Y' ");
        wipSQL.append(SQLStem.getWhereStmt(whereStem));
        ps3 = conn.prepareStatement(wipSQL.toString());
        ps3.executeUpdate();
      }

      //conn.setAutoCommit(true);
    } catch (Exception e) {
      DBConnection.rollback(conn);
      e.printStackTrace();
      return false;
    } finally {
      try {
        conn.commit();
      } catch (Exception e) {
        e.printStackTrace();
      }
      DBConnection.close(conn);
      conn = null;
    }
    return true;
  }
  
  /* get available FT Special Control (原需求為 Endurance Grade) */
  /* 若此 product body必需選擇 FT Special Control(tag=23)，則回傳可用之資料 (tag=22)，否則回傳 'NA'*/
  public static String[] getAvailableEndurance(String product_body) {
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;

    try {
      ArrayList tmp = new ArrayList();
      conn = DBConnection.getConnection();
      SelSQL.append("SELECT 0 id, 'NA' description\n" +
    		  		"FROM dual\n" +
    		  		"where not exists (select 1 from tf_description b, tf_product c\n" + 
    		  		"where b.tag = 23\n" +
    		  		"and b.description = c.process_type\n" +
    		  		"and c.product_body = '"+product_body+"')\n" +
    		  		"UNION\n" +
    		  		"SELECT a.id, a.description\n" + 
    		  		"FROM tf_description a\n" + 
    		  		"where a.tag = 22\n" + 
    		  		"and a.id not in (1,2)" +
    		  		"UNION\n" +
    		  		"SELECT a.id, a.description\n" +
    		  		"FROM tf_description a, tf_description b, tf_product c\n" +
    		  		"where a.tag = 22\n" + 
    		  		"and b.tag = 23\n" + 
    		  		"and b.description = c.process_type\n" +
    		  		"and c.product_body = '"+product_body+"'\n" +
    		  		"order by id\n");
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()){
        tmp.add(rs.getString("description"));
      }
      return (String[]) tmp.toArray(new String[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  
  public static String[] getWSSpecialControl() {
	    StringBuffer SelSQL = new StringBuffer();
	    Connection conn = null;

	    try {
	      ArrayList tmp = new ArrayList();
	      conn = DBConnection.getConnection();
	      SelSQL.append("SELECT 0 id, 'NA' description\n" +
	    		  		"FROM dual\n" +
	    		  		"UNION\n" +
	    		  		"SELECT a.id, a.description\n" + 
	    		  		"FROM tf_description a\n" + 
	    		  		"where a.tag = 25\n" + 
	    		  		"order by id\n");
	      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
	      ResultSet rs = ps.executeQuery();

	      while (rs.next()){
	        tmp.add(rs.getString("description"));
	      }
	      return (String[]) tmp.toArray(new String[0]);
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    } finally {
	      DBConnection.close(conn);
	      conn = null;
	    }
	    return null;
	  }
  /*GET QAULITY LEVEL FROM SAP*/
  public static String[] getSAPQualityLevel(String prodbody) {
      StringBuffer SelSQL = new StringBuffer();
      Connection conn = null;
      try {
        ArrayList tmp = new ArrayList();
        conn = DBConnection.getConnection();
        SelSQL.append(
                "select t.receive_time,\n" +
                        "       t.turnon,\n" + 
                        "       t.quality_level_comment,\n" + 
                        "       Replace(t.quality_level,'Q','') quality_level, \n" + 
                        "       t.product_body\n" + 
                        "  from tf_quality_level t where t.product_body = '"+prodbody+"' order by quality_level ");
        PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
          tmp.add(rs.getString("quality_level"));
          tmp.add(rs.getString("quality_level_comment"));
          tmp.add(rs.getString("turnon"));
        }
        if(tmp.size() > 0)
            return (String[]) tmp.toArray(new String[0]);
        else
            return new String[]{"N","N","N"};
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        DBConnection.close(conn);
        conn = null;
      }
      return new String[]{"N","N","N"};
    }
  
  /* 判斷某 OI 之 Route 之 rework step 是否定義完成 (_TX table only) 
   * 回傳未完整定義的 Route */
  public static String rwkFinished(String sid) {
	String result = "";
    StringBuffer sql = new StringBuffer();
    Connection conn = null;

    try {
      conn = DBConnection.getConnection();
      sql.append("select distinct route_name from tf_product_route_tx a\n" +
      		  "where a.sid=" + sid +
    		  "\nand a.step_name like 'SORT%'" + 
    		  "\nand a.rework_step is null");
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();

      while (rs.next()){
    	  result = result + (result.equals("")?"":",") + rs.getString("ROUTE_NAME");
      }
      rs.close();
      ps.close();
      rs = null;
      ps = null;
      return result;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return result;
  }

  /* 判斷某 OI 之 Route 中相同 test mode 之 rework step 是否定義一致 (_TX table only) 
   * 回傳未完整定義的 Route */
  public static String rwkInconsistent(String sid) {

	// 20090813, MK330 Phoebe 要求取消比對, Tommy 反應 while update 6628 data
	if (true) return "";
	
	String result = "";
    StringBuffer sql = new StringBuffer();
    Connection conn = null;

    try {
      conn = DBConnection.getConnection();
      sql.append("select distinct a.step_name, a.route_name\n"+
    		  "from tf_product_route_tx a, tf_product_route_tx b\n"+
    		  "where a.sid = "+sid+"\n"+
    		  "and a.sid = b.sid \n"+
    		  "and a.step_name = b.step_name\n"+
    		  "and not (a.test_time2 = b.test_time2\n"+
    		  "and a.time_unit2 = b.time_unit2\n"+
    		  "and a.rework_step = b.rework_step)\n"+
    		  "order by a.step_name, a.route_name");
      PreparedStatement ps = conn.prepareStatement(sql.toString());
      ResultSet rs = ps.executeQuery();

      String Step = "";
      while (rs.next()){
    	  if (!rs.getString("STEP_NAME").equals(Step))
    		  result = result + (result.equals("")?"":"<br>") + 
    		  	rs.getString("STEP_NAME")+":"+ rs.getString("ROUTE_NAME");
    	  else
    		  result = result + ","+rs.getString("ROUTE_NAME");
    	  Step = rs.getString("STEP_NAME");
      }
      rs.close();
      ps.close();
      rs = null;
      ps = null;
      return result;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return result;
  }
  
//check if any earlier version of the product body and brand
  public static boolean CheckExistProductWaferlevel(String pro_b,
                                               String brand,
                                               String version) throws Exception {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      whereStem.put("version",maxV);
      conn = DBConnection.getConnection();
      sql.append("select * from tf_prod_waferlevel ");
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
  public static boolean CheckExistProductWaferlevelTX(String sid) throws Exception {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;

    try {
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);
      conn = DBConnection.getConnection();
      sql.append("select * from tf_prod_waferlevel_tx ");
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
//check if any earlier version of the product body and brand
  public static int CheckExistProductWaferlevelCount(String pro_b,
                                               String brand,
                                               String version) throws Exception {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version)-1);
    int num_count = 0;
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      whereStem.put("version",maxV);
      conn = DBConnection.getConnection();
      sql.append("select count(*) num_count from tf_prod_waferlevel ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps1.executeQuery();
      while(rs.next()){
    	  num_count = Integer.parseInt(rs.getString("num_count"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return num_count;
  }
//check if any earlier version of the product body and brand
  public static int CheckExistProductWaferlevelTXCount(String pro_b,
                                               String brand,
                                               String version) throws Exception {
    StringBuffer sql = new StringBuffer();
    Connection conn = null;
    String maxV=String.valueOf(Integer.parseInt(version));
    int num_count = 0;
    try {
      HashMap whereStem = new HashMap();
      whereStem.put("product_body", pro_b);
      whereStem.put("brand", brand);
      whereStem.put("version",maxV);
      conn = DBConnection.getConnection();
      sql.append("select count(*) num_count from tf_prod_waferlevel_tx ");
      sql.append(SQLStem.getWhereStmt(whereStem));
      PreparedStatement ps1 = conn.prepareStatement(sql.toString());
      ResultSet rs=ps1.executeQuery();
      while(rs.next()){
    	  num_count = Integer.parseInt(rs.getString("num_count"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return num_count;
  }
  public static int getWaferlevelCount() throws Exception {
	StringBuffer sql = new StringBuffer();
	Connection conn = null;
	int num_count = 0;
	try {
		conn = DBConnection.getConnection();
		sql.append("select count(*) num_count from tf_waferlevel ");

		PreparedStatement ps1 = conn.prepareStatement(sql.toString());
		ResultSet rs=ps1.executeQuery();
		while(rs.next()){
			num_count = Integer.parseInt(rs.getString("num_count"));
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		DBConnection.close(conn);
		conn = null;
	}
	return num_count;
}
  /*帶出PRODUCT VS WAFERLEVEL資料的主要程式*/
  public static ProWaferlevelBean[] GetWaferlevel(String sid) {
    Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);

      conn = DBConnection.getConnection();
      String sql="select a.* from tf_prod_waferlevel_tx a " +
          "where a.sid = ? " +
          "order by a.revise_priority";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ProWaferlevelBean trb = new ProWaferlevelBean();
        trb.setProductbody(rs.getString("product_body"));
        trb.setBrand(rs.getString("brand"));
        trb.setVersion(rs.getString("version"));
        trb.setSid(rs.getString("sid"));
        trb.setWafer_level(rs.getString("wafer_level"));
        trb.setWafer_brand(rs.getString("wafer_brand"));
        trb.setBiztype(rs.getString("biztype"));
        trb.setWafer_grade(rs.getString("wafer_grade"));
        trb.setApply_type(rs.getString("apply_type"));
        trb.setOri_priority(rs.getString("ori_priority"));
        trb.setRevise_priority(rs.getString("revise_priority"));
        trb.setChecked_flag(rs.getString("checked_flag"));
        tmp2.add(trb);
      }
      return (ProWaferlevelBean[]) tmp2.toArray(new ProWaferlevelBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      DBConnection.close(conn);
      conn = null;
    }
    return null;
  }
  
  /*帶出PRODUCT VS WAFERLEVEL資料的主要程式*/
  public static ProWaferlevelBean[] GetWaferlevelByCheckedFlag(Connection conn, String sid, String table) {
    //Connection conn = null;

    try {
      ArrayList tmp2 = new ArrayList();
      HashMap whereStem = new HashMap();
      whereStem.put("sid", sid);

      //conn = DBConnection.getConnection();
      String sql="select aa.*, rownum from (select a.* from tf_prod_waferlevel"+ table + " a " +
          "where a.sid = ? " +
          " and checked_flag = 'Y' " +
          "order by a.revise_priority) aa";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1,sid);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        ProWaferlevelBean trb = new ProWaferlevelBean();
        trb.setProductbody(rs.getString("product_body"));
        trb.setBrand(rs.getString("brand"));
        trb.setVersion(rs.getString("version"));
        trb.setSid(rs.getString("sid"));
        trb.setWafer_level(rs.getString("wafer_level"));
        trb.setWafer_brand(rs.getString("wafer_brand"));
        trb.setBiztype(rs.getString("biztype"));
        trb.setWafer_grade(rs.getString("wafer_grade"));
        trb.setApply_type(rs.getString("apply_type"));
        trb.setOri_priority(rs.getString("ori_priority"));
        trb.setRevise_priority(rs.getString("revise_priority"));
        trb.setChecked_flag(rs.getString("checked_flag"));
        trb.setRownum(rs.getString("rownum"));
        tmp2.add(trb);
      }
      return (ProWaferlevelBean[]) tmp2.toArray(new ProWaferlevelBean[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      //DBConnection.close(conn);
      //conn = null;
    }
    return null;
  }
  
    
//insert data from tf_prod_waferlevel to tf_prod_waferlevel_tx
  public static boolean ProWaferlevelToProWaferlevelTx(String pro_b,
                                             String br,
                                             String sid,
                                             String version) {

    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV = String.valueOf(Integer.parseInt(version)-1);
    try {
      conn = DBConnection.getConnection();
      HashMap whereStem = new HashMap();
      whereStem.put("product_body",pro_b);
      whereStem.put("brand",br);
      whereStem.put("version", maxV);
      SqlStmt.append("insert into tf_prod_waferlevel_tx " +
                     "(sid,product_body,brand,version,wafer_level,wafer_brand," +
                     "biztype,wafer_grade,apply_type,ori_priority,revise_priority,checked_flag,sid1) " +
                     "select " + sid + ",product_body,brand," + version +
                     ",wafer_level,wafer_brand,biztype,wafer_grade,apply_type,ori_priority,revise_priority,checked_flag,sid1 " +
                     "FROM tf_prod_waferlevel  ");
      SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      TDSLogger.println(SqlStmt.toString());
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

    // update data in tf_prod_waferlevel_tx
    public static boolean UpdateProWaferlevelTx(String pro_b, String br, String sid, String version) {

        StringBuffer SqlStmt = new StringBuffer();
        Connection conn = null;
        String maxV = String.valueOf(Integer.parseInt(version) - 1);
        try {
            conn = DBConnection.getConnection();
            HashMap whereStem = new HashMap();
            whereStem.put("product_body", pro_b);
            whereStem.put("brand", br);
            whereStem.put("version", version);
            SqlStmt.append("update tf_prod_waferlevel_tx set checked_flag = 'Y' ");
            SqlStmt.append(SQLStem.getWhereStmt(whereStem));
            TDSLogger.println(SqlStmt.toString());
            PreparedStatement ps = conn.prepareStatement(SqlStmt.toString());
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

//insert data from tf_prod_waferlevel/tf_waferlevel to tf_prod_waferlevel_tx (tf_prod_waferlevel : 6筆, tf_waferlevel : 8筆)
  public static boolean ProWaferlevelToProWaferlevelTx_New(String pro_b,
                                             String br,
                                             String sid,
                                             String version) {

    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    String maxV = String.valueOf(Integer.parseInt(version)-1);
    try {
      conn = DBConnection.getConnection();
      String SelSQL = "delete from tf_prod_waferlevel_tx where sid = "+sid;
      PreparedStatement ps_delete = conn.prepareStatement(SelSQL.toString());
      TDSLogger.println(SelSQL.toString());
      ps_delete.executeUpdate();
      HashMap whereStem = new HashMap();
      //whereStem.put("product_body",pro_b);
      //whereStem.put("brand",br);
      //whereStem.put("version", maxV);
      SqlStmt.append("insert into tf_prod_waferlevel_tx " +
                     "(sid,product_body,brand,version,wafer_level,wafer_brand," +
                     "biztype,wafer_grade,apply_type,ori_priority,revise_priority,checked_flag,sid1) " +
                     "select " + sid + ",a.product_body,a.brand," + version +
                     ",b.wafer_level,b.wafer_brand,b.biztype,b.wafer_grade,b.apply_type,b.priority,b.priority,a.checked_flag,a.sid1 " +
                     "FROM tf_prod_waferlevel a, tf_waferlevel b  " +
                     "where a.product_body ='" + pro_b + "' " + 
                     "  and a.brand ='" + br + "' " + 
                     "  and a.version = " + maxV + " " + 
                     "  and b.sid=a.sid1 " +
                     "UNION " +
                     "select " + sid + ",'" + pro_b + "','" + br + "'," + version +
                     ",b.wafer_level,b.wafer_brand,b.biztype,b.wafer_grade,b.apply_type,b.priority,b.priority,'',b.sid " +
                     "FROM tf_waferlevel b  " + 
                     "where not exists (select 1 from tf_prod_waferlevel c " +
                     "                        where c.product_body ='" + pro_b + "' " + 
                     "                          and c.brand ='" + br + "' " + 
                     "                          and c.version = " + maxV + " " + 
                     "                          and c.sid1=b.sid)");
      //SqlStmt.append(SQLStem.getWhereStmt(whereStem));
      TDSLogger.println(SqlStmt.toString());
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
  
//insert data from tf_waferlevel to tf_prod_waferlevel_tx
  public static boolean TFWaferlevelToProWaferlevelTx(String pro_b,
                                             String br,
                                             String sid,
                                             String version) {

    StringBuffer SqlStmt = new StringBuffer();
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
      SqlStmt.append("insert into tf_prod_waferlevel_tx " +
                     "(sid,product_body,brand,version,wafer_level,wafer_brand," +
                     "biztype,wafer_grade,apply_type,ori_priority,revise_priority,checked_flag,sid1) " +
                     "select " + sid + ",'" +pro_b+"','" + br + "'," + version +
                     ",wafer_level,wafer_brand,biztype,wafer_grade,apply_type,priority,priority,'Y',sid " +
                     "FROM tf_waferlevel  ");
      TDSLogger.println(SqlStmt.toString());               
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
  public static ArrayList selectFromBADescriptionList(int tag, String stage) {
      ArrayList results = null;
      Connection con = null;
      try {
          con = DBConnection.getConnection();
          results = getBADescriptionList(con, tag, stage);

      } catch (Exception ex) {
          TDSLogger.println(ex);
      } finally {
          DBConnection.close(con);
      }
      return results;
  }
  public static ArrayList getBADescriptionList(Connection con, int tag, String stage) throws SQLException {
  int id = 0;
  if (stage.equals("first"))
      id = 0;
  else  if (stage.equals("second"))
      id = 100;
  else if (stage.equals("third"))
      id = -1;
      StringBuffer sql = new StringBuffer();
      sql.append("select id, description descrip from tim.TF_DESCRIPTION ");
      sql.append("where tag = ? and ID > ? and ID <= ? and DELETE_FLAG IS NULL ");  //and ID < ?  //ADD BY KEN FOR GPRS
      sql.append("order by id");

      ResultSet rs = null;
      Object[] wheres = new Object[3];
      wheres[0] = new Integer(tag);
      wheres[1] = new Integer(id);
      wheres[2] = new Integer(id+50);
      ArrayList data = new ArrayList();
      try {
          rs = DB.queryByPrepareSQL(con, sql.toString(), wheres);
          while (rs.next()) {
              BADescriptionListForm obj = new BADescriptionListForm();
              obj.setTag(new BigDecimal(tag));
              obj.setId(rs.getBigDecimal("id"));
              obj.setDescrip(rs.getString("descrip"));
              data.add(obj);
          }
      } catch (Exception e) {
          TDSLogger.println(e);
      } finally {
          try {
              if (rs != null)
                  rs.getStatement().close();
              rs.close();
              rs = null;
          } catch (Exception e) {}
      }
      return data;
  }
    public static String[][] getDefaultBinList(String product, String mode, String dbbin) {
        Connection conn = null;
        boolean f = false;
        try {
            ArrayList tmp = new ArrayList();
            ArrayList tmp2 = new ArrayList();
            String[][] rnt = new String[2][];

            tmp.add("------");
            tmp.add("NA");
            tmp2.add("------");
            tmp2.add("NA");

            for (int i = 1; i < 1000; i++) {
                tmp.add("BIN" + i);
            }
            //if(!mode.toUpperCase().startsWith("S"))  //因為WS的DBIN只有NA
                for (int i = 1; i <= 10; i++) {
                    tmp2.add("IB" + i);
                }

            if (tmp.isEmpty() || tmp2.isEmpty()) {
                return null;
            } else {
                rnt[0] = (String[]) tmp.toArray(new String[0]);
                rnt[1] = (String[]) tmp2.toArray(new String[0]);
                return rnt;
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
        return null;
    }
    public static String[] getDescriptionByYieldDefiniction(String Tag) {
        String[] rnt = getDescription(Tag);
        String[] rnt1 = null;
        if(rnt != null) {
            rnt1 = new String[rnt.length];
            for (int i = 0;  i < rnt.length;i++) {
                rnt1[i] = rnt[i];
            }
            return rnt1;
        } else {
            return null;
        }
    }
    
    public static boolean doublicatePIMWS(String sid) {
        Connection conn = null;
        boolean flag = false;

        try {
            conn = DBConnection.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append(
                    "SELECT COUNT(*) COUNT FROM TF_TEST_PARAMETER_WS_TX C WHERE C.SID = "+sid+" \n" +
                    "AND C.PGM_ID IN (\n" + 
                    "SELECT A.PREVIOUS_PROGRAM_ID FROM PG_PREVIOUS_PROGRAM A WHERE A.PROGRAM_ID IN ( \n" + 
                    "SELECT DISTINCT(B.PGM_ID) FROM TF_TEST_PARAMETER_WS_TX  B WHERE B.SID = "+sid+") \n" + 
                    "AND A.PROGRAM_MODE = 'PROD' \n" + 
                    //"AND A.CATEGORY IN (1,2) \n" + 連Program to be expired 也要納入檢查
                    "AND A.PREVIOUS_PROGRAM_MODE = 'PROD')");

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                if (rs.getString("COUNT").equals("0") != true) {
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
            return flag;
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
    }
    
    public static String doublicatePIMWSS(String sid) {
        Connection conn = null;
        StringBuffer s = new StringBuffer();
        try {
            conn = DBConnection.getConnection();
            StringBuffer sql = new StringBuffer();
            sql.append(
                    "SELECT DISTINCT PGM_ID FROM TF_TEST_PARAMETER_WS_TX C WHERE C.SID = "+sid+" \n" +
                    "AND C.PGM_ID IN (\n" + 
                    "SELECT A.PREVIOUS_PROGRAM_ID FROM PG_PREVIOUS_PROGRAM A WHERE A.PROGRAM_ID IN ( \n" + 
                    "SELECT DISTINCT(B.PGM_ID) FROM TF_TEST_PARAMETER_WS_TX  B WHERE B.SID = "+sid+") \n" + 
                    "AND A.PROGRAM_MODE = 'PROD' \n" + 
                    //"AND A.CATEGORY IN (1,2) \n" + 連Program to be expired 也要納入檢查
                    "AND A.PREVIOUS_PROGRAM_MODE = 'PROD')");

            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	s.append(rs.getString("PGM_ID"));
            	s.append(" ");
            }
            return  s.toString();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return "";
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
    }
    

    /**
     * 
     * @param sid : 針對該份OI去抓取所屬的BOM內的ROUTE
     * @param productbody
     * @param brand
     * @param mask_option : 有細到第五碼PRODUCT_CODE
     * @return
     * @since JB201500007 : 該需求確保在輸入WS YIELD DOWNGRADE CRITERIA時，CP與FP的ROUTE_NAME都有定義。
     */
	public static BomProductRouteBean[] GetSortRouteCodeTX(String sid, String productbody, String item_brand, String mask_option, String brand) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;

		try {
			conn = DBConnection.getConnection();
			ArrayList tmp = new ArrayList();
			HashMap whereStem = new HashMap();

			if (brand.equals("MX") && item_brand.equals("KH")) {
				SelSQL.append("SELECT distinct ws_route FROM tf_bom_route   ");
			} else {
				SelSQL.append("SELECT distinct ws_route FROM tf_bom_route_tx   ");
				whereStem.put("sid", sid);  // MX才指定到SID
			}

			whereStem.put("product_body", productbody);
			whereStem.put("brand", item_brand);
			whereStem.put("mask_option", mask_option);
			SelSQL.append(SQLStem.getWhereStmt(whereStem));

			if (brand.equals("MX") && item_brand.equals("KH"))
				SelSQL.append(" AND  VERSION IN (  select MAX(VERSION) from TF_BOM_ROUTE where product_body = '" + productbody + "' and mask_option = '" + mask_option + "' AND BRAND = 'KH') ");

			SelSQL.append(" AND WS_ROUTE IS NOT  NULL  AND TAG != 2  ");
			SelSQL.append(" ORDER BY TO_NUMBER(TRIM(TRANSLATE(WS_ROUTE,TRIM(TRANSLATE(WS_ROUTE,'1234567890','          ')),' ')) ) ");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				BomProductRouteBean bom = new BomProductRouteBean();
				bom.setWsroute(rs.getString("ws_route"));
				tmp.add(bom);
			}
			return (BomProductRouteBean[]) tmp.toArray(new BomProductRouteBean[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	
	/**
	 * @since JB201500007, 檢查TF_YIELD_DEFINITION_TX內的ROUTE_NAME有沒有不存在TF_BOM_ROUTE_TX內；要PRODUCT_CODE, ROUTE_NAME配對檢查。
	 * @param sid : OI的 SID
	 * @param idd : BOM 所勾選的 ID
	 * @return
	 * @throws Exception
	 * @author CCCHANG02
	 */
	public static String CheckExistYieldDefCount(String sid, String idd) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		int num_count = 0;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
					"SELECT DISTINCT PRODUCT_CODE, ROUTE_NAME\n" +
							"  FROM TF_YIELD_DEFINITION_TX, (SELECT BRAND FROM TF_INFORMATION WHERE SID = " + sid + ") B\n" + 
							" WHERE TF_YIELD_DEFINITION_TX.SID = " + sid + "\n" + 
							"   AND TF_YIELD_DEFINITION_TX.ROUTE_NAME IS NOT NULL \n" + 
                            "   AND ((B.BRAND = 'MX' AND\n" +
                            "       TF_YIELD_DEFINITION_TX.ACTION NOT IN\n" + 
                            "       ('Dgrade-KH', 'Dgrade-KTD', 'Dgrade-KH-T', 'Dgrade-KGD-KH')) OR\n" + 
                            "        (B.BRAND = 'KH' AND\n" + 
                            "       TF_YIELD_DEFINITION_TX.ACTION  IN\n" + 
                            "       ('Dgrade-KH', 'Dgrade-KTD', 'Dgrade-KH-T', 'Dgrade-KGD-KH')))\n" + 
                            "       AND TF_YIELD_DEFINITION_TX.ACTION LIKE 'Dgrade%'  AND DGRADEPRODCODE IS NULL \n" + 
							"MINUS\n" + 
							"SELECT DISTINCT PRODUCT_BODY || MASK_OPTION, WS_ROUTE\n" + 
							"  FROM TF_BOM_ROUTE_TX\n" + 
							" WHERE SID = " + sid + "\n" + 
							"   AND TAG != 2\n" + 
							"   AND WS_ROUTE IS NOT NULL\n");
			if (idd != null)
				sql.append("   AND ID NOT IN (" + idd + ")");
			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("(");
				tmp.append(rs.getString("PRODUCT_CODE"));
				tmp.append("-");
				tmp.append(rs.getString("ROUTE_NAME"));
				tmp.append(")");
				num_count++;
				if (num_count % 4 == 0) {
					tmp.append("\\n");
				}
			}
			if (num_count > 0)
				insertLogicCompareResult(sid, "ROUTE_Noexist_BOM",tmp.toString().substring(0, (tmp.toString().length()>4000)?4000:tmp.toString().length()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		if (num_count > 0){
			return tmp.toString();
		}else
			return null;
		
	}

	/**
	 * @since 檢查TF_YIELD_DEFINITION_TX內的ROUTE_NAME有沒有不存在TF_BOM_ROUTE_TX內；要DGRADEPRODCODE, ROUTE_NAME配對檢查。20181210 BY PHOEBE
	 * @param sid : OI的 SID
	 * @param idd : BOM 所勾選的 ID
	 * @return
	 * @throws Exception
	 * @author CCCHANG02
	 */
	public static String CheckExistYieldDefCountDGRADEPRODCODE(String sid, String idd) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		int num_count = 0;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
					"SELECT DISTINCT DGRADEPRODCODE PRODUCT_CODE, ROUTE_NAME\n" +
							"  FROM TF_YIELD_DEFINITION_TX, (SELECT BRAND FROM TF_INFORMATION WHERE SID = " + sid + ") B\n" + 
							" WHERE TF_YIELD_DEFINITION_TX.SID = " + sid + "\n" + 
							"   AND TF_YIELD_DEFINITION_TX.ROUTE_NAME IS NOT NULL \n" + 
                            "   AND ((B.BRAND = 'MX' AND\n" +
                            "       TF_YIELD_DEFINITION_TX.ACTION NOT IN\n" + 
                            "       ('Dgrade-KH', 'Dgrade-KTD', 'Dgrade-KH-T', 'Dgrade-KGD-KH')) OR\n" + 
                            "        (B.BRAND = 'KH' AND\n" + 
                            "       TF_YIELD_DEFINITION_TX.ACTION  IN\n" + 
                            "       ('Dgrade-KH', 'Dgrade-KTD', 'Dgrade-KH-T', 'Dgrade-KGD-KH')))\n" + 
                            "       AND TF_YIELD_DEFINITION_TX.ACTION LIKE 'Dgrade%' AND DGRADEPRODCODE IS NOT NULL \n" + 
							"MINUS\n" + 
							"SELECT DISTINCT PRODUCT_BODY || MASK_OPTION, WS_ROUTE\n" + 
							"  FROM TF_BOM_ROUTE_TX\n" + 
							" WHERE SID = " + sid + "\n" + 
							"   AND TAG != 2\n" + 
							"   AND WS_ROUTE IS NOT NULL\n");
			if (idd != null)
				sql.append("   AND ID NOT IN (" + idd + ")");
			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("(");
				tmp.append(rs.getString("PRODUCT_CODE"));
				tmp.append("-");
				tmp.append(rs.getString("ROUTE_NAME"));
				tmp.append(")");
				num_count++;
				if (num_count % 4 == 0) {
					tmp.append("\\n");
				}
			}
			if (num_count > 0)
				insertLogicCompareResult(sid, "ROUTE_Noexist_BOM",tmp.toString().substring(0, (tmp.toString().length()>4000)?4000:tmp.toString().length()));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		if (num_count > 0){
			return tmp.toString();
		}else
			return null;
		
	}
	/**
	 * @since 檢查TF_YIELD_DEFINITION_TX內的Yield設定有沒有有無短缺(程式有,Yield就要設定,KEY值為PRODUCT_CODE,TEST_MODE)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistYieldDefCountByWSFTTestParameter(String sid, String facility) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
				" SELECT DISTINCT PRODUCT_BODY || " + (facility.equals("WS")?"MASK_OPTION":"BACKEND_OPTION") + " PRODUCT_CODE, test_type\n"+
				"   FROM " + (facility.equals("WS")?"Tf_Test_Parameter_Ws_Tx":"Tf_Test_Parameter_Ft_Tx") +" \n" + 
				"   WHERE SID = ?\n" + 
				"   AND TAG != 2\n" + 
				"  MINUS\n" + 
				// "SELECT DISTINCT PRODUCT_CODE, test_mode\n" + //Redmine-#187525 Mark
				"SELECT DISTINCT PRODUCT_CODE, case when instr(test_mode, '/') > 0 then substr(test_mode, 1, instr(test_mode, '/')-1) else test_mode end test_mode \n" + // Redmine-#187525 Modify 
				" FROM TF_YIELD_DEFINITION_TX \n" + 
				"  WHERE SID = ?\n" + 
				"  and item = 'Yield'"
				+ "and (action not like 'Dgrade%' AND action not like 'Follow Hold Criteria%') ");
			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*提醒您, 有 "+ rs.getString("PRODUCT_CODE")+"/"+ rs.getString("TEST_TYPE")+" 程式 , 但缺少 "+rs.getString("PRODUCT_CODE")+"/"+ rs.getString("TEST_TYPE")+ " Yield criteria , 請補定義 !不可Submit\\n"); //Redmine-#161512 Modify
				insertLogicCompareResult(sid, "Have_Pgm_No_Yield","有 "+ rs.getString("PRODUCT_CODE")+"/"+ rs.getString("TEST_TYPE")+" 程式 , 但缺少 "+rs.getString("PRODUCT_CODE")+"/"+ rs.getString("TEST_TYPE")+ " Yield criteria , 請補定義 !不可Submit"); //Redmine-#161512 Modify
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
		
	}
	/**
	 * @since BOM 有 Pin Count = 0 & Pkg Type = W 且 Route 有 AVI 站點，則必須檢核有對應 Mask Option 的 AVI yield (必要) 
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistYieldDefCountByBomRouteAVI(String sid, String facility) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
				"select DISTINCT C.PRODUCT_BODY || C.MASK_OPTION PRODUCT_CODE, d.step_name \n" +
				"from tf_bom_route_tx c, tf_product_route_tx d\n" + 
				"where c.sid = d.sid\n" + 
				"AND (c.ws_route = d.route_name or c.ws_route_add = d.route_name)\n" + 
				"and c.sid =?\n" + 
				"and c.pin_count=0\n" + 
				"and c.package_type='W'\n" +
				"and d.step_name = 'AVI'\n" + 
				"and c.tag != 2\n" + 
				"AND NOT EXISTS(SELECT * FROM TF_YIELD_DEFINITION_TX A\n" + 
				"                WHERE A.SID = C.SID\n" + 
				"				 AND A.facility = ?\n" +
				"                AND A.PRODUCT_CODE = C.PRODUCT_BODY || C.MASK_OPTION\n" + 
				"                AND A.TEST_MODE = 'AVI'  \n" + 
				"				 AND (A.action not like 'Dgrade%' AND A.action not like 'Follow Hold Criteria%' ))\n");
			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_CODE"));
				tmp.append("-");
				tmp.append(rs.getString("STEP_NAME"));
				tmp.append(" yield 設定 , 請補定義 !\\n");
				insertLogicCompareResult(sid, "No_AVI_Yield","缺少 "+rs.getString("PRODUCT_CODE")+"-"+rs.getString("STEP_NAME")+" yield 設定 , 請補定義 !");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		if(tmp.length()>0)
			return "\\n"+tmp.toString();
		else
			return "";
	
	}
	/**
	 * @since BOM 有 Pin Count = 0 & Pkg Type = W,TEST_MODE=Ship 之 yield or TEST_MODE=Ship 之 Dgrade 設定 (擇一即可)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistYieldDefCountByBomRouteShip(String sid, String facility) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
				"select DISTINCT C.PRODUCT_BODY || C.MASK_OPTION PRODUCT_CODE \n" +
				"from tf_bom_route_tx c\n"+
				"where c.sid =?\n" + 
				"and c.pin_count=0\n" + 
				"and c.package_type='W'\n" +
				"and c.tag != 2\n" + 
				"and c.brand != 'KH'\n" + 
				"AND NOT EXISTS(SELECT * FROM TF_YIELD_DEFINITION_TX A\n" + 
				"                WHERE A.SID = C.SID\n" +
				"				 AND A.facility = ?\n" +
				"                AND A.PRODUCT_CODE = C.PRODUCT_BODY || C.MASK_OPTION\n" + 
				"                AND A.TEST_MODE = 'Ship'  \n" + 
				"				 AND a.item='Yield' )\n");
			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_CODE"));
				tmp.append(" 之 Ship Yield or Dgrade 設定 , 請補定義 !\\n");
				insertLogicCompareResult(sid, "No_Ship_Yield_Dgrade","缺少 "+rs.getString("PRODUCT_CODE")+" 之 Ship Yield or Dgrade 設定 , 請補定義 !");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	/**
	 * @since 1. AEB Flow  : ST1 ==>ST2 -->GDBANK  ,  Owner 會設定 ST1 , ST2 ,及 ST2/ST1 的yield criteria
	 *        2. MX Flow  : S1 ==>S2 ==>GDBANK  , Owner 會設定 S1 ,S2 , 及 S2/S1 的yield criteria
	 *        3. 問題  :   但當遇到ST1 Dgrade MXO 時 , Lot 的test flow 會變成 : ST1==>S2 ==>GDBANK  , 會漏了S2/ST1的yield 比值
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistSMSNYieldByDgrade(String sid) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(

			"SELECT distinct aa.test_mode_new, aa.product_code, aa.brand, aa.action, aa.item,\n" +
			"aa.route_name, aa.start_step, AA.TEST_MODE\n" + 
			"from\n" + 
			"(select A.*,\n" + 
			"  GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP) || '/' || SUBSTR(DECODE(INSTR(A.TEST_MODE,'/'), 0,A.TEST_MODE, SUBSTR(A.TEST_MODE,0,INSTR(A.TEST_MODE,'/')-1)),2) TEST_MODE_NEW\n" + 
			"  from tf_yield_definition_tx a\n" + 
			"  where A.SID = ?\n" + 
			"  AND A.FACILITY = 0\n" + 
			"  and A.action like 'Dgrade%'\n" + 
			"  and a.action not in ('Dgrade-KGD-KH','Dgrade-KH','Dgrade-KTD','Dgrade-KH-T')\n" +
			//"  AND A.test_mode NOT like '%/%'\n" + 
			"  AND substr(A.TEST_MODE,0,1) in ('S','F')\n" + 
			"  AND A.TEST_MODE != 'Ship'\n" + 
			"  AND A.ITEM = 'Yield'\n" + 
			"  AND GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP) IS NOT NULL\n" +
			"  AND EXISTS (select * FROM tf_yield_definition_tx C\n" +
			"               WHERE C.SID = A.SID\n" + 
			"                 AND C.PRODUCT_CODE = A.PRODUCT_CODE\n" + 
			"                 AND C.BRAND = A.BRAND\n" + 
			"                 AND C.FACILITY = A.FACILITY\n" + 
			"                 AND C.ACTION NOT LIKE 'Dgrade%'\n" + 
			"                 and c.item = 'Yield'\n" + 
			//"                 AND C.TEST_MODE LIKE DECODE(INSTR(A.TEST_MODE,'/'), 0,A.TEST_MODE, SUBSTR(A.TEST_MODE,0,INSTR(A.TEST_MODE,'/')-1)) || '/%')\n" +
			"                 AND C.TEST_MODE =GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP)||'/'|| REPLACE(tf_get_sn_step(A.sid, A.route_name, 0,replace(GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP),'S','SORT')),'SORT','')\n" +
			"             )\n"+
			")AA,\n" + 
			"(select *\n" + 
			"   from tf_yield_definition_tx B\n" + 
			"  WHERE B.SID = ?\n" + 
			"  AND B.FACILITY = 0\n" + 
			"  and B.ITEM = 'Yield'\n" + 
			"  and B.ACTION NOT LIKE 'Dgrade%'\n" + 
			")BB\n" + 
			"where AA.SID = BB.SID (+)\n" + 
			"  AND AA.PRODUCT_CODE = BB.PRODUCT_CODE (+)\n" + 
			"  AND AA.BRAND = BB.BRAND (+)\n" + 
			"  AND AA.FACILITY = BB.FACILITY (+)\n" + 
			"  AND AA.TEST_MODE_NEW = BB.TEST_MODE (+)\n" + 
			"  AND SUBSTR(AA.TEST_MODE_NEW,0,INSTR(AA.TEST_MODE_NEW,'/')-1) != 'S'||SUBSTR(AA.TEST_MODE_NEW,INSTR(AA.TEST_MODE_NEW,'/')+1)\n" +
			"  AND BB.TEST_MODE IS NULL\n"+
			"ORDER BY AA.PRODUCT_CODE, TEST_MODE_NEW");

			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			//ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_CODE"));
				tmp.append(" 之 " + rs.getString("TEST_MODE_NEW") + " Yield 設定 , 請補定義 !( 因"+rs.getString("PRODUCT_CODE")+"有設定Dgrade條件: "+rs.getString("TEST_MODE")+" Dgrade / "+rs.getString("ROUTE_NAME")+" / "+rs.getString("START_STEP")+" )\\n");
				insertLogicCompareResult(sid, "No_SMSN_Yield","缺少 "+rs.getString("PRODUCT_CODE")+" 之  " + rs.getString("TEST_MODE_NEW") + " Yield 設定 , 請補定義 !( 因"+rs.getString("PRODUCT_CODE")+"有設定Dgrade條件: "+rs.getString("TEST_MODE")+" Dgrade / "+rs.getString("ROUTE_NAME")+" / "+rs.getString("START_STEP")+" )");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	/**
	 * @since 1. AEB Flow  : ST1 ==>ST2 -->GDBANK  ,  Owner 會設定 ST1 , ST2 ,及 ST2/ST1 的yield criteria
	 *        2. MX Flow  : S1 ==>S2 ==>GDBANK  , Owner 會設定 S1 ,S2 , 及 S2/S1 的yield criteria
	 *        3. 問題  :   但當遇到ST1 Dgrade MXO 時 , Lot 的test flow 會變成 : ST1==>S2 ==>GDBANK  , 會漏了S2/ST1的yield 比值 (CheckExistSMSNYieldByDgrade)
	 *        4. 因有設定S2/S1 的Dgrade criteia , 故系統也需卡：要設定S2/ST1的Dgrade criteria(CheckExistSMSNDgradeYieldByDgrade)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistSMSNDgradeYieldByDgrade(String sid) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(


			"SELECT distinct aa.test_mode_new, aa.product_code, aa.brand, aa.action, aa.item,\n" +
			"aa.route_name, aa.start_step, AA.TEST_MODE\n" + 
			"from\n" + 
			"(select A.SID,a.product_code, a.brand, a.action, a.item, a.route_name, a.start_step,\n" + 
			"  A.TEST_MODE, A.FACILITY,\n" + 
			"  GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP) || '/' || SUBSTR(DECODE(INSTR(A.TEST_MODE,'/'), 0,A.TEST_MODE, SUBSTR(A.TEST_MODE,0,INSTR(A.TEST_MODE,'/')-1)),2) TEST_MODE_NEW\n" + 
			"  from tf_yield_definition_tx a\n" + 
			"  where A.SID = ?\n" + 
			"  AND A.FACILITY = 0\n" + 
			"  and A.action like 'Dgrade%'\n" + 
			"  and a.action not in ('Dgrade-KGD-KH','Dgrade-KH','Dgrade-KTD','Dgrade-KH-T')\n" +
			//"  AND A.test_mode NOT like '%/%'\n" + 
			"  AND substr(A.TEST_MODE,0,1) in ('S','F')\n" + 
			"  AND A.TEST_MODE != 'Ship'\n" + 
			"  AND A.ITEM = 'Yield'\n" + 
			"  AND GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP) IS NOT NULL\n" + 
			"  AND EXISTS (select * FROM tf_yield_definition_tx C\n" + 
			"               WHERE C.SID = A.SID\n" + 
			"                 AND C.PRODUCT_CODE = A.PRODUCT_CODE\n" + 
			"                 AND C.BRAND = A.BRAND\n" + 
			"                 AND C.FACILITY = A.FACILITY\n" + 
			"                 AND C.ACTION LIKE 'Dgrade%'\n" + 
			"                 and c.item = 'Yield'\n" + 
			//"                 AND C.TEST_MODE LIKE DECODE(INSTR(A.TEST_MODE,'/'), 0,A.TEST_MODE, SUBSTR(A.TEST_MODE,0,INSTR(A.TEST_MODE,'/')-1)) || '/%')\n" +
			"                 AND C.TEST_MODE =GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP)||'/'|| REPLACE(tf_get_sn_step(A.sid, A.route_name, 0,replace(GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP),'S','SORT')),'SORT','')\n" +
			"             )\n"+
			")AA,\n" + 
			"(select *\n" + 
			"   from tf_yield_definition_tx B\n" + 
			"  WHERE B.SID = ?\n" + 
			"  AND B.FACILITY = 0\n" + 
			"  and B.ITEM = 'Yield'\n" + 
			"  and B.ACTION LIKE 'Dgrade%'\n" + 
			")BB\n" + 
			"where AA.SID = BB.SID (+)\n" + 
			"  AND AA.PRODUCT_CODE = BB.PRODUCT_CODE (+)\n" + 
			"  AND AA.BRAND = BB.BRAND (+)\n" + 
			"  AND AA.FACILITY = BB.FACILITY (+)\n" + 
			"  AND AA.TEST_MODE_NEW = BB.TEST_MODE (+)\n" + 
			"  AND SUBSTR(AA.TEST_MODE_NEW,0,INSTR(AA.TEST_MODE_NEW,'/')-1) != 'S'||SUBSTR(AA.TEST_MODE_NEW,INSTR(AA.TEST_MODE_NEW,'/')+1)\n" +
			"  AND BB.TEST_MODE IS NULL\n" +
			"ORDER BY AA.PRODUCT_CODE, TEST_MODE_NEW");


			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			//ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_CODE"));
				tmp.append(" 之 " + rs.getString("TEST_MODE_NEW") + " Dgrade Yield 設定 , 請補定義 !( 因"+rs.getString("PRODUCT_CODE")+"有設定Dgrade條件: "+rs.getString("TEST_MODE")+" Dgrade / "+rs.getString("ROUTE_NAME")+" / "+rs.getString("START_STEP")+" )\\n");
				insertLogicCompareResult(sid, "No_SMSN_Dgrade_Yield","缺少 "+rs.getString("PRODUCT_CODE")+" 之  " + rs.getString("TEST_MODE_NEW") + " Dgrade Yield 設定 , 請補定義 !( 因"+rs.getString("PRODUCT_CODE")+"有設定Dgrade條件: "+rs.getString("TEST_MODE")+" Dgrade / "+rs.getString("ROUTE_NAME")+" / "+rs.getString("START_STEP")+" )");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	/**
	 * @since 1. AEB Flow  : ST1 ==>ST2 -->GDBANK  ,  Owner 會設定 ST1 , ST2 ,及 ST2/ST1 的yield criteria
	 *        2. MX Flow  : S1 ==>S2 ==>GDBANK  , Owner 會設定 S1 ,S2 , 及 S2/S1 的yield criteria
	 *        3. 問題  :   但當遇到ST1 Dgrade MXO 時 , Lot 的test flow 會變成 : ST1==>S2 ==>GDBANK  , 會漏了S2/ST1的yield 比值 (CheckExistSMSNYieldByDgrade)
	 *        4. 因有設定S2/S1 的Dgrade criteia , 故系統也需卡：要設定S2/ST1的Dgrade criteria(CheckExistSMSNDgradeYieldByDgrade)
	 *        5. 因有設定S2/S1 的SYL , 故系統也需卡：要設定S2/ST1的SYL criteria(CheckExistSMSNSYLYieldByDgrade)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistSMSNSYLYieldByDgrade(String sid) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(


			"SELECT distinct aa.test_mode_new, aa.product_code, aa.brand, aa.action, aa.item,\n" +
			"aa.route_name, aa.start_step, AA.TEST_MODE\n" + 
			"from\n" + 
			"(select A.SID,a.product_code, a.brand, a.action, a.item, a.route_name, a.start_step,\n" + 
			"  A.TEST_MODE, A.FACILITY,\n" + 
			"  GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP) || '/' || SUBSTR(DECODE(INSTR(A.TEST_MODE,'/'), 0,A.TEST_MODE, SUBSTR(A.TEST_MODE,0,INSTR(A.TEST_MODE,'/')-1)),2) TEST_MODE_NEW\n" + 
			"  from tf_yield_definition_tx a\n" + 
			"  where A.SID = ?\n" + 
			"  AND A.FACILITY = 0\n" + 
			"  and A.action like 'Dgrade%'\n" +
			"  and a.action not in ('Dgrade-KGD-KH','Dgrade-KH','Dgrade-KTD','Dgrade-KH-T')\n" +
			//"  AND A.test_mode NOT like '%/%'\n" + 
			"  AND substr(A.TEST_MODE,0,1) in ('S','F')\n" + 
			"  AND A.TEST_MODE != 'Ship'\n" + 
			"  AND A.ITEM = 'Yield'\n" + 
			"  AND GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP) IS NOT NULL\n" + 
			"  AND EXISTS (select * FROM tf_yield_definition_tx C\n" + 
			"               WHERE C.SID = A.SID\n" + 
			"                 AND C.PRODUCT_CODE = A.PRODUCT_CODE\n" + 
			"                 AND C.BRAND = 'AEB'\n" + 
			"                 AND C.FACILITY = A.FACILITY\n" + 
			"                 AND C.ACTION not LIKE 'Dgrade%'\n" + 
			"                 and c.item = 'AEB SPC Yield'\n" + 
			//"                 AND C.TEST_MODE LIKE DECODE(INSTR(A.TEST_MODE,'/'), 0,A.TEST_MODE, SUBSTR(A.TEST_MODE,0,INSTR(A.TEST_MODE,'/')-1)) || '/%')\n" +
			"                 AND C.TEST_MODE =GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP)||'/'|| REPLACE(tf_get_sn_step(A.sid, A.route_name, 0,replace(GET_8049OI_ROUTE_STARTSTEP(A.SID, A.ROUTE_NAME, A.START_STEP),'S','SORT')),'SORT','')\n" +
			"             )\n"+
			")AA,\n" + 
			"(select *\n" + 
			"   from tf_yield_definition_tx B\n" + 
			"  WHERE B.SID = ?\n" + 
			"  AND B.FACILITY = 0\n" + 
			"  and B.BRAND = 'AEB'\n" +
			"  and B.ITEM = 'AEB SPC Yield'\n" + 
			"  and B.ACTION not LIKE 'Dgrade%'\n" + 
			")BB\n" + 
			"where AA.SID = BB.SID (+)\n" + 
			"  AND AA.PRODUCT_CODE = BB.PRODUCT_CODE (+)\n" + 
			//"  AND AA.BRAND = BB.BRAND (+)\n" + 
			"  AND AA.FACILITY = BB.FACILITY (+)\n" + 
			"  AND AA.TEST_MODE_NEW = BB.TEST_MODE (+)\n" + 
			"  AND SUBSTR(AA.TEST_MODE_NEW,0,INSTR(AA.TEST_MODE_NEW,'/')-1) != 'S'||SUBSTR(AA.TEST_MODE_NEW,INSTR(AA.TEST_MODE_NEW,'/')+1)\n" +
			"  AND BB.TEST_MODE IS NULL\n" + 
			"ORDER BY AA.PRODUCT_CODE, TEST_MODE_NEW");


			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			//ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_CODE"));
				tmp.append(" 之 " + rs.getString("TEST_MODE_NEW") + " SYL Yield 設定 , 請補定義 !( 因"+rs.getString("PRODUCT_CODE")+"有設定Dgrade條件: "+rs.getString("TEST_MODE")+" Dgrade / "+rs.getString("ROUTE_NAME")+" / "+rs.getString("START_STEP")+" )\\n");
				insertLogicCompareResult(sid, "No_SMSN_SYL_Yield","缺少 "+rs.getString("PRODUCT_CODE")+" 之  " + rs.getString("TEST_MODE_NEW") + " SYL Yield 設定 , 請補定義 !( 因"+rs.getString("PRODUCT_CODE")+"有設定Dgrade條件: "+rs.getString("TEST_MODE")+" Dgrade / "+rs.getString("ROUTE_NAME")+" / "+rs.getString("START_STEP")+" )");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	/**
	 * @since 目前AEB 入庫總體檢 卡 :
	 *        1. CP 每一道Test mode 均需有SYL 的檢查, 且均需為Pass , 才能入FG
	 *        FW123: S1, S2 ==> S1 SYL 要有, (S2 SYL , S2/S1 SYL 只要有一個即可)  (AEB SPC Yield)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistCPSYLYieldByAEB(String sid) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
				"select distinct a.sid,a.product_body, a.mask_option, a.brand, B.ROUTE_NAME,\n" +
				"                b.step_seq, b.step_name,replace(b.step_name,'SORT','S') test_mode,\n" + 
				"                DECODE(tf_get_sn_step(b.sid, b.route_name, b.step_seq,''),'','',REPLACE (B.STEP_NAME||'/'||REPLACE (tf_get_sn_step(b.sid, b.route_name, b.step_seq,''),'SORT',''),'SORT','S')) SMSN\n" + 
				"         from tf_bom_route_tx a, tf_product_route_tx b\n" + 
				"         where a.sid = b.sid\n" + 
				"         and a.sid = ?\n" + 
				"         and a.WSSPECIALCONTROL like '%AEB%'\n" + 
				"         and a.ws_route = b.route_name\n" + 
				"         and b.step_name like 'S%'\n" + 
				"         and a.mcp_flag != 'MCP'\n" +
				"         and a.tag != 2 \n" +
				"         and not exists (select * from tf_yield_definition_tx c\n" + 
				"                          where c.sid = a.sid\n" + 
				"                          and c.facility = 0\n" + 
				"                          AND c.test_mode not like '%/%'\n" + 
				"                          and c.item = 'AEB SPC Yield'\n" + 
				"                          AND c.TEST_MODE = replace(b.step_name,'SORT','S')\n" + 
				"                          and a.product_body||a.mask_option= c.product_code\n" + 
				"                         )\n" + 
				"         and not exists (select * from tf_yield_definition_tx c\n" + 
				"                          where c.sid = a.sid\n" + 
				"                          and c.facility = 0\n" + 
				"                          AND c.test_mode like '%/%'\n" + 
				"                          AND c.test_mode = DECODE(tf_get_sn_step(b.sid, b.route_name, b.step_seq,''),'','',REPLACE (B.STEP_NAME||'/'||REPLACE (tf_get_sn_step(b.sid, b.route_name, b.step_seq,''),'SORT',''),'SORT','S'))\n" + 
				"                          and c.item = 'AEB SPC Yield'\n" + 
				"                          and a.product_body||a.mask_option= c.product_code\n" + 
				"                         )");

					


			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			//ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_BODY")+rs.getString("MASK_OPTION"));
				tmp.append(" 之 " + rs.getString("TEST_MODE") + (rs.getString("SMSN")==null?"":"或"+rs.getString("SMSN")) + " AEB SYL Yield 設定 , 請補定義 !( 因 "+rs.getString("ROUTE_NAME")+" 之 WS SPECIAL CONTROL 設定 *AEB* )\\n");
				insertLogicCompareResult(sid, "No_AEB_SYL_Yield","缺少 "+rs.getString("PRODUCT_BODY")+rs.getString("MASK_OPTION")+" 之  " + rs.getString("TEST_MODE") + (rs.getString("SMSN")==null?"":"或"+rs.getString("SMSN")) + " AEB SYL Yield 設定 , 請補定義 !( 因 "+rs.getString("ROUTE_NAME")+" 之 WS SPECIAL CONTROL 設定 *AEB* )\\n");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	/**
	 * @since 目前AEB 入庫總體檢 卡 :
	 *        1. CP 每一道Test mode 均需有SBL 的檢查, 且均需為Pass , 才能入FG
	 *        S1, S2 (AEBSPCBin)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistCPSBLCriteriaByAEB(String sid) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
				"SELECT distinct AA.*, replace(AA.step_name,'SORT','S') TEST_MODE, BB.YID, BB.product_code, bb.item, bb.action  FROM\n" +
				"(select distinct a.sid,a.product_body, a.mask_option, a.brand, B.ROUTE_NAME, b.step_seq, b.step_name\n" + 
				"from tf_bom_route_tx a, tf_product_route_tx b\n" + 
				"where a.sid = b.sid\n" + 
				"and a.sid = ?\n" + 
				"and a.WSSPECIALCONTROL like '%AEB%'\n" + 
				"and a.ws_route = b.route_name\n" +
				"and a.mcp_flag != 'MCP'\n" +
				"and a.tag != 2\n"+
				"and b.step_name like 'S%')AA,\n" + 
				"(select c.*\n" + 
				" from tf_yield_definition_tx c\n" + 
				"where c.sid = ?\n" + 
				"and c.facility = 0\n" + 
				"AND c.test_mode not like '%/%'\n" + 
				"AND c.TEST_MODE like 'S%'\n" + 
				"and c.item LIKE 'AEBSPCBin%' )BB\n" + 
				"WHERE aa.sid = bb.sid(+)\n" + 
				"and AA.product_body||AA.mask_option=BB.product_code (+)\n" + 
				"and replace(AA.step_name,'SORT','S') = bb.test_mode (+)\n" + 
				"AND BB.YID IS NULL\n" + 
				"order by aa.product_body, aa.mask_option, aa.step_name");

			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			//ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_BODY")+rs.getString("MASK_OPTION"));
				tmp.append(" 之 " + rs.getString("TEST_MODE") + " AEB SBL Criteria 設定 , 請補定義 !( 因 "+rs.getString("ROUTE_NAME")+" 之 WS SPECIAL CONTROL 設定 *AEB* )\\n");
				insertLogicCompareResult(sid, "No_AEB_SYL_Yield","缺少 "+rs.getString("PRODUCT_BODY")+rs.getString("MASK_OPTION")+" 之  " + rs.getString("TEST_MODE") + " AEB SYL Yield 設定 , 請補定義 !( 因 "+rs.getString("ROUTE_NAME")+" 之 WS SPECIAL CONTROL 設定 *AEB* )\\n");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	
	//BE#2022000045 - 8049 SPC Control SYL&SBL Hold 及Dgrade criteria 改善需求
	public static String checkCPSPCControlCriteriaByAEB(String sid) throws Exception {
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			//itemType# 1: Yield, 2: BINXX, 10: AEB SPC Yield, 11: AEBSPCBinXX
			//AEB SPC Yield 有定義, 但 Dgrade criteria 未定義或不完整  
			//AEBSPCBinXX 有定義, 但 Dgrade criteria 未定義或不完整  
			String sql = 
				"select aa.*\n" +
				"from (select distinct a.product_code, a.test_mode, a.item_type, a.item, a.lower_limit, a.flag1, a.flag2, a.upper_limit\n" + 
				"      from  TF_YIELD_DEFINITION_TX a\n" + 
				"      where a.SID = ?\n" + 
				"      and a.facility = 0 and a.item_type = 10 and a.action in ('SPC Control Pass' , 'SPC Control') and a.test_mode like 'S%' --'item_type 10: AEB SPC Yield'\n" + 
				"      and not exists (select b.product_code, b.test_mode, b.item, b.upper_limit\n" + 
				"                 from  TF_YIELD_DEFINITION_TX b\n" + 
				"                 where b.SID = ? and b.product_code = a.product_code and b.test_mode = a.test_mode and to_number(b.upper_limit, '999.99999') >= to_number(a.lower_limit, '999.99999')\n" + 
				"                       and b.facility = 0 and b.item_type = 1 and b.action like 'Dgrade-%' and b.test_mode like 'S%')    --'item_type 1: Yield'\n" + 
				"      union\n" + 
				"      select distinct a.product_code, a.test_mode, a.item_type, a.item, a.lower_limit, a.flag1, a.flag2, a.upper_limit\n" + 
				"      from  TF_YIELD_DEFINITION_TX a\n" + 
				"      where a.SID = ?\n" + 
				"      and a.facility = 0 and a.item_type = 11 and a.action in ('SPC Control Pass', 'SPC Control') and a.test_mode like 'S%'  --'item_type 11: AEBSPCBinXX'\n" + 
				"      and not exists (select *\n" + 
				"                 from  TF_YIELD_DEFINITION_TX b\n" + 
				"                 where b.SID = ? and b.product_code = a.product_code and b.test_mode = a.test_mode and to_number(b.lower_limit, '999.99999') <= to_number(a.upper_limit, '999.99999')\n" + 
				"                       and b.facility = 0 and b.item_type = 2 and b.action like 'Dgrade-%' and b.test_mode like 'S%'\n" + 
				"                       and b.item = upper(replace(a.item, 'AEBSPC')))\n" + 
				"      union\n" +	//AEB D05+D06, D07
				"      select distinct a.product_code, a.test_mode, a.item_type, a.item, a.lower_limit, a.flag1, a.flag2, a.upper_limit\n" + 
				"      from  TF_YIELD_DEFINITION_TX a\n" + 
				"      where a.SID = ?\n" + 
				"      and a.facility = 0 and a.item_type in (17,21) and a.action in ('SPC Control Pass', 'SPC Control') and a.test_mode like 'S%'  --'item_type 17,21: AEB D05+D06, AEB D07'\n" + 
				"      and not exists (select *\n" + 
				"           from  TF_YIELD_DEFINITION_TX b\n" + 
				"           where b.SID = ? and b.product_code = a.product_code and b.test_mode = a.test_mode and to_number(b.lower_limit, '999.99999') <= to_number(a.upper_limit, '999.99999')\n" + 
				"                 and b.facility = 0 and b.item_type in (18,22) and b.action like 'Dgrade-%' and b.test_mode like 'S%'\n" + 
				"                 and b.item = replace(a.item, 'AEB '))) aa\n" +				
				"where not exists (select distinct bb.route_name, bb.step_name, replace(bb.step_name, 'SORT', 'S') step_name\n" + 
				"                  from tf_product_route_tx bb\n" + 
				"                  where bb.sid = ? and bb.sampling_test = 'Y' and bb.step_name = replace(decode(instr(aa.test_mode, '/'), 0, aa.test_mode, SUBSTR(aa.test_mode, 1, instr(aa.test_mode, '/')-1)), 'S', 'SORT'))";

			TDSLogger.println("checkCPSPCControlCriteriaByAEB() - sid: " + sid + ", sql: " + sql);					
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			ps1.setString(3, sid);
			ps1.setString(4, sid);
			ps1.setString(5, sid);
			ps1.setString(6, sid);
			ps1.setString(7, sid);
			
			ResultSet rs = ps1.executeQuery();
			while (rs.next()) {
				//6704L/SB1/AEB SPC Yield 有定義, 但 Dgrade criteria 未定義或不完整
				String itemType = rs.getString("ITEM_TYPE");
				StringBuffer str = new StringBuffer();
				str.append(rs.getString("PRODUCT_CODE"))
					.append("/").append(rs.getString("TEST_MODE"))
					.append("/").append(rs.getString("ITEM"))
					.append(" 有定義 SPC Control Pass, 但 Dgrade criteria 未定義或不完整, 請確認!");
				if(itemType.equals("10")){	//AEB SPC Yield
					insertLogicCompareResult(sid, "SPC_Control_AEB_SYL_Yield_Undefined", str.toString());
				}else if(itemType.equals("11")){	//AEBSPCBinXX
					insertLogicCompareResult(sid, "SPC_Control_AEB_SBL_Yield_Undefined", str.toString());
				}
				tmp.append(str).append("\\n");
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
		
	}
	
	public static String checkCPWaitDgradeIPN(String sid) throws Exception {
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			//check 定義  Wait Dgrade IPN 在 DGRADE SPECIAL IPN 不完整
			//Key: product_code, test_mode(S2 與 S2/1 屬同一個 mode), action (Dgrade-XX), 筆數需相符 
			String sql = 
				"select *\n" +
				"from (select a.product_code, decode(instr(a.test_mode, '/'), 0, a.test_mode, SUBSTR(a.test_mode, 1, instr(a.test_mode, '/')-1)) test_mode, a.dgrade_special_ipn, a.action, count(*) total\n" + 
				"     from tf_yield_definition_tx a\n" + 
				"     where a.sid = ? and a.dgrade_special_ipn = 'Wait Dgrade IPN'\n" + 
				"     group by a.product_code, decode(instr(a.test_mode, '/'), 0, a.test_mode, SUBSTR(a.test_mode, 1, instr(a.test_mode, '/')-1)), a.dgrade_special_ipn, a.action) aa\n" + 
				"where exists (select *\n" + 
				"             from (select a.product_code, decode(instr(a.test_mode, '/'), 0, a.test_mode, SUBSTR(a.test_mode, 1, instr(a.test_mode, '/')-1)) test_mode, a.action, count(*) total\n" + 
				"                  from tf_yield_definition_tx a\n" + 
				"                  where a.sid = ? and a.action like 'Dgrade%'\n" + 
				"                  group by a.product_code, decode(instr(a.test_mode, '/'), 0, a.test_mode, SUBSTR(a.test_mode, 1, instr(a.test_mode, '/')-1)), a.action) bb\n" + 
				"             where bb.product_code = aa.product_code and bb.test_mode = aa.test_mode and bb.action = aa.action and bb.total != aa.total)";

			TDSLogger.println("checkCPWaitDgradeIPN() - sid: " + sid + ", sql: " + sql);
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			
			ResultSet rs = ps1.executeQuery();
			while (rs.next()) {
				//6704L/ST2/Dgrade-MXO 在 DGRADE SPECIAL IPN 定義  Wait Dgrade IPN 不完整, 請確認!
				StringBuffer str = new StringBuffer();
				str.append(rs.getString("PRODUCT_CODE"))
					.append("/").append(rs.getString("TEST_MODE"))
					.append("/").append(rs.getString("ACTION"))
					.append(" DGRADE SPECIAL IPN 定義不同, 請確認! ");
				tmp.append(str).append("\\n");
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
			
		return tmp.toString();
		
	}	
	
	public static String checkCPRegionCriteriaByAEB(String sid) throws Exception {
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			HashMap whereStem = new HashMap();
			String sql = 

				"SELECT * FROM   TF_YIELD_DEFINITION_TX a where a.SID = ?\n" +
				"and a.facility = 0 and a.item_type = 25 and a.action in ('SPC Control Pass') and a.test_mode like 'S%'\n" + 
				" and not exists (select *\n" + 
				" from  TF_YIELD_DEFINITION_TX b\n" + 
				"  where b.SID = ? and b.product_code = a.product_code and b.test_mode = a.test_mode and to_number(b.lower_limit, '999.99999') <= to_number(a.upper_limit, '999.99999')\n" + 
				"  and b.facility = 0 and b.item_type = 26 and b.action like 'Dgrade-%' and b.test_mode like 'S%'\n" + 
				"  and upper(replace(b.item, 'SpecialRegion')) = upper(replace(a.item, 'AEBSPCSpecialRegion')))";



			TDSLogger.println("checkCPRegionCriteriaByAEB() - sid: " + sid + ", sql: " + sql);					
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			
			ResultSet rs = ps1.executeQuery();
			while (rs.next()) {
				//6704L/SB1/AEB SPC Yield 有定義, 但 Dgrade criteria 未定義或不完整
				String itemType = rs.getString("ITEM_TYPE");
				StringBuffer str = new StringBuffer();
				str.append(rs.getString("PRODUCT_CODE"))
					.append("/").append(rs.getString("TEST_MODE"))
					.append("/").append(rs.getString("ITEM"))
					.append(" 有定義 SPC Control Pass, 但 Dgrade criteria 未定義或不完整, 請確認!");
				tmp.append(str).append("\\n");
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
		
	}
	
	/**
	 * @since 目前AEB 入庫總體檢 卡 :
	 *        1. FT 每一道Test mode 均需有SYL 的檢查, 且均需為Pass , 才能入FG
	 *        FT1, FT2 (SPC Dgrade)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistFTSYLYieldByAEB(String sid, String table) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
				"SELECT distinct AA.*,AA.step_name TEST_MODE, BB.YID, BB.product_code, bb.item, bb.action FROM\n" +
				"(select distinct a.sid,a.product_body, a.backend_option, a.brand, B.ROUTE_NAME, b.step_seq, b.step_name-- a.ws_route,\n" + 
				"from " +table+" a, tf_product_route_tx b\n" + 
				"where a.sid = b.sid\n" + 
				"and a.sid = ?\n" + 
				"and a.brand='MX'\n" + 
				"and a.Endurance like '%AEB%'\n" + 
				"and a.ft_route = b.route_name\n" + 
				"and a.tag != 2\n"+
				"and b.step_name like 'F%')AA,\n" + 
				"(select c.*\n" + 
				" from tf_yield_definition_tx c\n" + 
				"where c.sid = ?\n" + 
				"and c.facility = 1\n" + 
				"AND c.TEST_MODE like 'F%'\n" + 
				"and c.item = 'Yield'\n" + 
				"and c.action = 'SPC Dgrade')BB\n" + 
				"WHERE aa.sid = bb.sid(+)\n" + 
				"and AA.product_body||AA.backend_option =BB.product_code (+)\n" + 
				"and AA.step_name = bb.test_mode (+)\n" + 
				"AND BB.YID IS NULL\n" + 
				"order by aa.product_body, aa.backend_option, aa.step_name");

			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setString(2, sid);
			//ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			while (rs.next()) {
				tmp.append("*缺少 ");
				tmp.append(rs.getString("PRODUCT_BODY")+rs.getString("BACKEND_OPTION"));
				tmp.append(" 之 " + rs.getString("TEST_MODE") + " AEB SYL Yield 設定 , 請補定義 !( 因 "+rs.getString("ROUTE_NAME")+" 之 FT SPECIAL CONTROL 設定 *AEB* )\\n");
				insertLogicCompareResult(sid, "No_AEB_SYL_Yield","缺少 "+rs.getString("PRODUCT_BODY")+rs.getString("BACKEND_OPTION")+" 之  " + rs.getString("TEST_MODE") + " AEB SYL Yield 設定 , 請補定義 !( 因 "+rs.getString("ROUTE_NAME")+" 之 FT SPECIAL CONTROL 設定 *AEB* )\\n");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	/**
	 * @since 相同Dgrade route 但step給錯
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistDgradeStepBySameDgradeRoute(String sid, String facility) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		int num_count = 0;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			sql.append(
				"select distinct a.product_code, A.BRAND, A.TEST_MODE, A.ITEM, A.ACTION, A.ROUTE_NAME, A.START_STEP\n" +
				"from  TF_YIELD_DEFINITION_TX a\n" + 
				"where a.action like 'Dgrade%'\n" + 
				" and a.SID = ?\n" + 
				" and a.facility = ?\n" + 
				" and exists (SELECT * FROM TF_YIELD_DEFINITION_TX b\n" + 
				"              WHERE A.SID=b.SID\n" + 
				"              AND a.FACILITY = b.FACILITY\n" + 
				"              AND a.product_code = b.product_code\n" + 
				"              AND a.brand = b.brand\n" + 
				"              AND a.TEST_MODE = b.TEST_MODE\n" + 
				"              and a.route_name = b.route_name\n" + 
				"              AND b.action like 'Dgrade%'\n" + 
				"              and a.start_step != b.start_step\n" + 
				"              )");
		
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setInt(2, (facility.equals("WS")?0:1));
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			String tmpProductCode = "";
			String tmpBrand = "";
			String tmpTestMode = "";
			String tmpRouteName = "";
			while (rs.next()) {
				if(tmpProductCode.equals(""))
					tmp.append("*提醒您,"+rs.getString("PRODUCT_CODE")+"/"+rs.getString("BRAND")+"/"+rs.getString("TEST_MODE")+"/"+rs.getString("ITEM")+"/"+rs.getString("ACTION")+"/"+rs.getString("ROUTE_NAME")+",\\n");
				else if(tmpProductCode.equals(rs.getString("PRODUCT_CODE"))&&
						tmpBrand.equals(rs.getString("BRAND"))&&
						tmpTestMode.equals(rs.getString("TEST_MODE"))&&
						tmpRouteName.equals(rs.getString("ROUTE_NAME")))
					tmp.append(rs.getString("PRODUCT_CODE")+"/"+rs.getString("BRAND")+"/"+rs.getString("TEST_MODE")+"/"+rs.getString("ITEM")+"/"+rs.getString("ACTION")+"/"+rs.getString("ROUTE_NAME")+",\\n");
				else{
					tmp.append("之相同Key值 : prodcode / prodlevel / testmode/ routename 的 StartStep 設定不一致,請確認,仍可Submit!\\n");
					tmp.append("*提醒您,"+rs.getString("PRODUCT_CODE")+"/"+rs.getString("BRAND")+"/"+rs.getString("TEST_MODE")+"/"+rs.getString("ITEM")+"/"+rs.getString("ACTION")+"/"+rs.getString("ROUTE_NAME")+",\\n");
				}	
				num_count++;
				tmpProductCode = rs.getString("PRODUCT_CODE");
				tmpBrand = rs.getString("BRAND");
				tmpTestMode = rs.getString("TEST_MODE");
				tmpRouteName = rs.getString("ROUTE_NAME");
			}
			if(num_count>0)
				tmp.append("之相同Key值 : prodcode / prodlevel / testmode/ routename 的  StartStep 設定不一致,請確認,仍可Submit!\\n");
			if (tmp.length() > 0)
				insertLogicCompareResult(sid, "Same_RouteName_Diff_StartStep",tmp.toString().substring(0, (tmp.toString().length()>4000?4000:tmp.toString().length())));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		
		return tmp.toString();
	
	}
	/**
	 * @since 檢查TF_YIELD_DEFINITION_TX內的Yield設定有沒有有無短缺(不同 ProdCode 相同 test mode 比對 yield 設定條件要一樣)
	 * @param sid : OI的 SID
	 * @return
	 * @throws Exception
	 * @author lai
	 */
	public static String CheckExistYieldItemDefCountByProdCodeCount(String sid, String facility) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		int PRODCODE_CNT = 0;
		StringBuffer tmp = new StringBuffer();
		Connection conn = null;
		int num_count = 0;
		
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			
			//相同TEST_MODE/ACTION-->不同Upper_limit or Lower_limit
			sql.append(
				"select distinct a.product_code, a.test_mode\n" +
				"from  TF_YIELD_DEFINITION_TX a\n" + 
				"where a.item = 'Yield'\n" + 
				"and (a.action not like 'Dgrade%' AND a.action not like 'Follow Hold Criteria%')\n" + 
				"and a.SID = ?\n" + 
				"and a.facility = ?\n" + 
				"and exists (SELECT * FROM TF_YIELD_DEFINITION_TX b\n" + 
				"                WHERE A.SID=b.SID\n" + 
				"                AND a.FACILITY = b.FACILITY\n" + 
				"                AND a.TEST_MODE = b.TEST_MODE\n" + 
				"                AND (b.action not like 'Dgrade%' AND b.action not like 'Follow Hold Criteria%')\n" + 
				"                AND b.item = 'Yield'\n" + 
				"                AND a.product_code != b.product_code\n" + 
				"                AND a.action = b.action\n" + 
				"                AND (nvl(a.lower_limit,'0') != nvl(b.lower_limit,'0')\n" + 
				"                    or nvl(a.upper_limit,'100') != nvl(b.upper_limit,'100')\n" + 
				"                    )\n" + 
				"              )\n" +
				"order by test_mode\n");
			
			PreparedStatement ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setInt(2, (facility.equals("WS")?0:1));
			
			ResultSet rs = ps1.executeQuery();
			tmp = new StringBuffer();
			String tmpTestMode = "";
			while (rs.next()) {
				if(tmpTestMode.equals(""))
					tmp.append(rs.getString("PRODUCT_CODE"));
				else if(tmpTestMode.equals(rs.getString("TEST_MODE")))
					tmp.append("/" + rs.getString("PRODUCT_CODE"));
				else{
					tmp.append(" ,之" + tmpTestMode+ " 設定不一致,請確認!\\n");
					tmp.append(rs.getString("PRODUCT_CODE"));
				}	
				num_count++;
				tmpTestMode = rs.getString("TEST_MODE");
			}
			if(num_count>0)
				tmp.append(" ,之" + tmpTestMode+ " 設定不一致,請確認!\\n");
			rs.close();
			ps1.close();
	    	rs = null;
	    	ps1 = null;
			
	    	//相同TEST_MODE->不同ACTION(6640L/OOC,6640A/STOP) 
	    	sql.delete(0,sql.length());
			sql.append(
				"select a.test_mode, count(distinct product_code) prodCode_Count\n" +
				"from TF_YIELD_DEFINITION_TX a\n" + 
				"where a.item = 'Yield'\n" + 
				"and (a.action not like 'Dgrade%' AND a.action not like 'Follow Hold Criteria%')\n" + 
				"and a.SID = ?\n" + 
				"and a.facility = ?\n" + 
				"group by TEST_MODE\n" + 
				"having count(*)>1\n" +
				"ORDER BY TEST_MODE");
		
			ps1 = conn.prepareStatement(sql.toString());
			ps1.setString(1, sid);
			ps1.setInt(2, (facility.equals("WS")?0:1));
			
			rs = ps1.executeQuery();
			tmpTestMode = "";
			while (rs.next()) {
		    	int prodCodeCount = rs.getInt("PRODCODE_COUNT");
		    	sql2.delete(0,sql2.length());
				sql2.append(
					"select distinct aa.product_code, aa.test_mode\n" +
					"from  TF_YIELD_DEFINITION_TX aa\n" + 
					"where aa.SID = ?\n" + 
					"and aa.facility = ?\n" + 
					"and aa.test_mode in (\n" +
					"	select distinct a.test_mode\n" +
					"	from  TF_YIELD_DEFINITION_TX a\n" + 
					"	where a.item = 'Yield'\n" + 
					"	and (a.action not like 'Dgrade%' AND a.action not like 'Follow Hold Criteria%')\n" + 
					"	and a.SID = aa.sid\n" + 
					"	and a.facility = aa.facility\n" + 
					"	and a.test_mode = ?\n" +
					"	AND EXISTS(SELECT * FROM TF_YIELD_DEFINITION_TX c\n" + 
					"                WHERE c.SID=A.SID\n" + 
					"                AND c.FACILITY = a.FACILITY\n" + 
					"                AND c.TEST_MODE = a.TEST_MODE\n" + 
					"                AND (c.action not like 'Dgrade%' AND c.action not like 'Follow Hold Criteria%')\n" + 
					"                AND c.item = 'Yield'\n" + 
					"                AND c.product_code != a.PRODUCT_CODE\n" + 
					"             )\n" + 
					"	group by A.TEST_MODE, A.ACTION \n" +
					"	HAVING count(*) != ?\n" +
					")\n" +
					"order by test_mode\n");
			
				PreparedStatement ps2 = conn.prepareStatement(sql2.toString());
				ps2.setString(1, sid);
				ps2.setInt(2, (facility.equals("WS")?0:1));
				ps2.setString(3, rs.getString("TEST_MODE"));
				ps2.setInt(4, prodCodeCount);
			
				ResultSet rs2 = ps2.executeQuery();
				tmpTestMode = "";
				num_count = 0;
				String tmpDescrip = "";
				while (rs2.next()) {
					if(tmpTestMode.equals("")){
						tmpDescrip = rs2.getString("PRODUCT_CODE");
					}else if(tmpTestMode.equals(rs2.getString("TEST_MODE"))){
						tmpDescrip += "/" + rs2.getString("PRODUCT_CODE");
					}else{
						if(tmp.indexOf(tmpDescrip)<0){
							tmp.append("*"+tmpDescrip + " ,之" + tmpTestMode+ " 設定不一致,請確認!\\n");
							tmpDescrip = rs2.getString("PRODUCT_CODE");
						}	
					}
					num_count++;
					tmpTestMode = rs2.getString("TEST_MODE");
				}
				if(num_count>0){
					if(tmp.indexOf(tmpDescrip)<0)
						tmp.append("*"+tmpDescrip + " ,之" + tmpTestMode + " 設定不一致,請確認!\\n");
				}	
				rs2.close();
				ps2.close();
		    	rs2 = null;
		    	ps2 = null;
			}
			rs.close();
			ps1.close();
	    	rs = null;
	    	ps1 = null;
	    	if (tmp.length() > 0)
				insertLogicCompareResult(sid, "DiffProdcode_SameTestmode_noSame_Yield",tmp.toString().substring(0, (tmp.toString().length()>4000?4000:tmp.toString().length())));
	    	
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		if (tmp.length() > 0){
			return "\\n"+tmp.toString();
		}else
			return "";
		
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

	public static boolean insertLogicCompareResult(String sid,String compare_type, String compare_message) {
		String InsSQL = null;
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			InsSQL = "insert into TF_LOGIC_COMPARE_RESULT "
					+ "(sid,compare_type,compare_message,log_time) "
					+ "values (?,?,?,sysdate)";
			PreparedStatement ps2 = conn.prepareStatement(InsSQL);
			ps2.setString(1, sid);
			ps2.setString(2, compare_type);
			ps2.setString(3, compare_message);
			ps2.executeUpdate();

		} catch (Exception ex) {
			ex.printStackTrace();
			DBConnection.rollback(conn);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return false;
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
					tmp.append("漏設" + rs.getString("TEST_TYPE") + "的Bin 設定 ,請補定. ( 因 Step 3 : WS Test Parameter Information 有" + rs.getString("TEST_TYPE") + " pgm)\\n");

				} else {
					tmp.append("漏設" + rs.getString("TEST_TYPE") + "的Bin 設定 ,請補定. ( 因 Step 4 : FT Test Parameter Information 有" + rs.getString("TEST_TYPE") + " pgm)\\n");
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
}