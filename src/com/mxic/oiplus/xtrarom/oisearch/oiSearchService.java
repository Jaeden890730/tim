package com.mxic.oiplus.xtrarom.oisearch;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.SQLStem;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.util.StringUtil;

public class oiSearchService {
    public oiSearchService() {
    }

    /* 
    public static TFDescriptionBean[] SelectStepNameWS() {
        Connection conn = null;

        try {
            String sql =
                    "select * from tf_description where tag = '0' order by description ";
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFDescriptionBean bean = new TFDescriptionBean();
                bean.setTag(rs.getString("tag"));
                bean.setId(rs.getString("id"));
                bean.setDescription(rs.getString("description"));
                tmp.add(bean);
            }

            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFDescriptionBean[]) tmp.toArray(new TFDescriptionBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }

    }

    public static TFDescriptionBean[] SelectStepNameFT() {
        Connection conn = null;

        try {
            String sql =
                    "select * from tf_description where tag = '1' order by description ";
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFDescriptionBean bean = new TFDescriptionBean();
                bean.setTag(rs.getString("tag"));
                bean.setId(rs.getString("id"));
                bean.setDescription(rs.getString("description"));
                tmp.add(bean);
            }

            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFDescriptionBean[]) tmp.toArray(new TFDescriptionBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }
    }
    */


    public static boolean DeleteProductVsDept(String product_name, String dept) {
        Connection conn = null;
        boolean flag = true;
        try {
            String sql = "delete FROM tf_route_master where product_body = '"
                         + product_name + "' and department_id = '" + dept +
                         "'";
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ps.clearParameters();
            ps.close();
            flag = true;

        } catch (Exception ex) {
            ex.fillInStackTrace();
            DBConnection.rollback(conn);
            TDSLogger.println(ex.getMessage());
            flag = false;
        } finally {
            DBConnection.close(conn);
        }
        return flag;
    }


    public static TFProdDeptBean[] SelectProdOFDept(String dept) {
        Connection conn = null;

        try {
            String sql =
                    "SELECT * FROM tf_prod_dept where department_id = '" + dept +
                    "'";
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFProdDeptBean bean = new TFProdDeptBean();
                bean.setProduct_body(rs.getString("product_body"));
                bean.setDepartment_id(rs.getString("department_id"));
                bean.setCreator(rs.getString("creator"));
                tmp.add(bean);
            }

            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFProdDeptBean[]) tmp.toArray(new TFProdDeptBean[0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }
    }

    /*
    public static boolean UpdateEmpBasicData(oiUpdateWSTestRouteAForm fm) {
        Connection conn = null;

        boolean flag = true;
        try {
            String sql = "Update tf_route_master " +
                         "Set step1 = ? " +
                         ",step2 = ? " +
                         ",step3 = ? " +
                         ",step4 = ? " +
                         ",step5 = ? " +
                         ",step6 = ? " +
                         ",step7 = ? " +
                         ",step8 = ? " +
                         ",step9 = ? " +
                         ",step10 = ? " +
                         "Where route_name ='" + fm.getTxt_routename() + "'";
            conn = DBConnection.getConnection();
            TDSLogger.println(sql);

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fm.getTxt_step1());
            ps.setString(2, fm.getTxt_step2());
            ps.setString(3, fm.getTxt_step3());
            ps.setString(4, fm.getTxt_step4());
            ps.setString(5, fm.getTxt_step5());
            ps.setString(6, fm.getTxt_step6());
            ps.setString(7, fm.getTxt_step7());
            ps.setString(8, fm.getTxt_step8());
            ps.setString(9, fm.getTxt_step9());
            ps.setString(10, fm.getTxt_step10());

            TDSLogger.println(sql);
            ps.execute();
            ps.clearParameters();
            ps.close();

            flag = true;
        } catch (Exception ex) {
            ex.fillInStackTrace();
            DBConnection.rollback(conn);
            TDSLogger.println(ex.getMessage());
            flag = false;
        } finally {
            DBConnection.close(conn);
        }
        return flag;
    }

    public static boolean DeleteTestRoute(String route_name) {
        Connection conn = null;
        boolean flag = true;
        try {
            String sql = "delete FROM tf_route_master where route_name = '"
                         + route_name + "'";
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ps.clearParameters();
            ps.close();
            flag = true;

        } catch (Exception ex) {
            ex.fillInStackTrace();
            DBConnection.rollback(conn);
            TDSLogger.println(ex.getMessage());
            flag = false;
        } finally {
            DBConnection.close(conn);
        }
        return flag;
    }
    */
    
    public static boolean AddNew2IFInfo(oiMainControlAForm fm) {
        Connection conn = null;
        boolean flag = true;


        try {

            String sql = "insert into tf_information (sid,product_body,brand,version,status,creator,log_time, product_type) values (test_seq.nextval,?,?,?,?,?,sysdate,?)";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fm.getTxt_productbody());
            ps.setString(2, fm.getBrand());
            ps.setString(3, fm.getVersion());
            ps.setString(4, "P");
            ps.setString(5, fm.getCreator());
            ps.setString(6, fm.getProductType());

            ps.execute();
            ps.clearParameters();
            ps.close();

            flag = true;

        } catch (Exception ex) {
            ex.fillInStackTrace();
            DBConnection.rollback(conn);
            TDSLogger.println(ex.getMessage());
            flag = false;
        } finally {
            DBConnection.close(conn);
        }
        return flag;
    }

/*
    public static boolean AddNewRoute(oiAddNewWSRouteAForm fm) {
        Connection conn = null;
        boolean flag = true;
        String strMode = RouteTypeMode(fm.getTxt_routename());
        if (strMode == "%") {
            flag = false;
            return flag;
        }

        try {

            String sql = "insert into tf_route_master (route_name,step1,step2,step3,step4,step5,step6,step7,step8,step9,step10,type) values (?,?,?,?,?,?,?,?,?,?,?,?)";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fm.getTxt_routename());
            ps.setString(2, fm.getTxt_step1());
            ps.setString(3, fm.getTxt_step2());
            ps.setString(4, fm.getTxt_step3());
            ps.setString(5, fm.getTxt_step4());
            ps.setString(6, fm.getTxt_step5());
            ps.setString(7, fm.getTxt_step6());
            ps.setString(8, fm.getTxt_step7());
            ps.setString(9, fm.getTxt_step8());
            ps.setString(10, fm.getTxt_step9());
            ps.setString(11, fm.getTxt_step10());
            ps.setString(12, strMode);

            ps.execute();
            ps.clearParameters();
            ps.close();

            flag = true;

        } catch (Exception ex) {
            ex.fillInStackTrace();
            DBConnection.rollback(conn);
            TDSLogger.println(ex.getMessage());
            flag = false;
        } finally {
            DBConnection.close(conn);
        }
        return flag;
    }
*/
    
    public static boolean CheckRouteName(String routeName) {
        Connection conn = null;
        boolean IdFlag = true;
        try {
            int countID = 0;

            String sql =
                    "SELECT COUNT(route_name) countID FROM tf_route_master where route_name = '" +
                    routeName + "'";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                countID = rs.getInt("countID");
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (countID >= 1) {
                IdFlag = true;
            } else {
                IdFlag = false;
            }

        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            IdFlag = false;
        } finally {
          DBConnection.close(conn);
        }
        return IdFlag;
    }


    public static TFProductRouteBean[] LookUpTFProductRoute(String route_name) {
        Connection conn = null;

        try {
            String sql = "SELECT b.sid, b.product_body, b.brand, b.version, b.route_name, b.step_seq, b.step_name, b.temperature, b.sampling_test, b.test_time, b.time_unit, b.remark " +
                         "FROM tf_product_route_tx b where b.route_name='" +
                         route_name + "' union " +
                         "SELECT a.sid, a.product_body, brand, a.version, a.route_name, a.step_seq, a.step_name, a.temperature, a.sampling_test, a.test_time, a.time_unit, a.remark " +
                         "FROM tf_product_route a where a.route_name='" +
                         route_name + "'";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFProductRouteBean bean = new TFProductRouteBean();
                bean.setSid(rs.getString("SID"));
                bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                bean.setBrand(rs.getString("BRAND"));
                bean.setVersion(rs.getString("VERSION"));
                bean.setRoute_name(rs.getString("route_name"));
                bean.setStep_seq(rs.getString("step_seq"));
                bean.setStep_name(rs.getString("step_name"));
                bean.setTest_time(rs.getString("test_time"));
                bean.setTime_unit(rs.getString("time_unit"));
                bean.setTemperature(rs.getString("temperature"));
                bean.setSamplingtest(rs.getString("sampling_test"));
                bean.setRemark(rs.getString("remark"));
                tmp.add(bean);
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFProductRouteBean[]) tmp.toArray(new
                        TFProductRouteBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }

    }

    /*
    public static TFRouteMasterBean[] SelectTestRoute(String type,
            String route_name) {
        Connection conn = null;
        TDSLogger.println(route_name);
        if (route_name == null) {
            return null;
        }
        try {
            StringBuffer sql = new StringBuffer();
            HashMap whereStem = new HashMap();
            whereStem.put("route_name", route_name);
            //whereStem.put("type", type);
            //String sql = "SELECT * FROM tf_route_master where route_name = '"
            // + route_name + "' and type = '" + type + "'";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            sql.append("SELECT * FROM tf_route_master ");
            sql.append(SQLStem.getWhereStmt(whereStem));
            sql.append(" order by route_name");
            TDSLogger.println(sql.toString());
            ResultSet rs = ps.executeQuery(sql.toString());
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFRouteMasterBean bean = new TFRouteMasterBean();
                bean.setType(rs.getString("type"));
                bean.setRoute_name(rs.getString("route_name"));
                bean.setStep1(rs.getString("step1"));
                bean.setStep2(rs.getString("step2"));
                bean.setStep3(rs.getString("step3"));
                bean.setStep4(rs.getString("step4"));
                bean.setStep5(rs.getString("step5"));
                bean.setStep6(rs.getString("step6"));
                bean.setStep7(rs.getString("step7"));
                bean.setStep8(rs.getString("step8"));
                bean.setStep9(rs.getString("step9"));
                bean.setStep10(rs.getString("step10"));
                tmp.add(bean);
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFRouteMasterBean[]) tmp.toArray(new TFRouteMasterBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }

    }
    
    public static TFRouteMasterBean[] SelectTheTestRoute(String route_name) {
        Connection conn = null;
        TDSLogger.println(route_name);
        if (route_name == null) {
            return null;
        }
        try {
            StringBuffer sql = new StringBuffer();
            HashMap whereStem = new HashMap();
            whereStem.put("route_name", route_name);
            //String sql = "SELECT * FROM tf_route_master where route_name = '"
            // + route_name + "' and type = '" + type + "'";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            sql.append("SELECT * FROM tf_route_master ");
            sql.append(SQLStem.getWhereStmt(whereStem));
            TDSLogger.println(sql.toString());
            ResultSet rs = ps.executeQuery(sql.toString());
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFRouteMasterBean bean = new TFRouteMasterBean();
                bean.setType(rs.getString("type"));
                bean.setRoute_name(rs.getString("route_name"));
                bean.setStep1(rs.getString("step1"));
                bean.setStep2(rs.getString("step2"));
                bean.setStep3(rs.getString("step3"));
                bean.setStep4(rs.getString("step4"));
                bean.setStep5(rs.getString("step5"));
                bean.setStep6(rs.getString("step6"));
                bean.setStep7(rs.getString("step7"));
                bean.setStep8(rs.getString("step8"));
                bean.setStep9(rs.getString("step9"));
                bean.setStep10(rs.getString("step10"));
                tmp.add(bean);
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFRouteMasterBean[]) tmp.toArray(new TFRouteMasterBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }

    }
    */


    public static IFInformationBean[] SelectIFinformation(int selectOp, String productType) {
        Connection conn = null;
        String sql = "";
        ResultSet rs;
        try {
            switch (selectOp) {
            case 1: {
                sql =
                        "SELECT * FROM ( SELECT * FROM tf_information WHERE 1 = 1  ";
                break;

            }
            case 2: {
                sql = "SELECT * FROM ( SELECT * FROM tf_information where status = 'r' or status = 'R' "; //limit 100";
                break;

            }
            case 3: {
                sql = "SELECT * FROM ( SELECT * FROM tf_information where status = 'p' or status = 'P' "; //limit 100";
                break;

            }
            case 4: {
                sql = "SELECT * FROM ( SELECT * FROM tf_information where status = 'a' or status = 'A' "; //limit 100";
                break;

            }
            default: {
                sql =
                        "SELECT * FROM ( SELECT * FROM tf_information WHERE 1 = 1  "; //limit 100";
                break;
            }
            }
            if (productType != null)
            	sql = sql + "AND PRODUCT_TYPE = '" + productType + "' ";
            sql = sql + "order by LOG_TIME desc, version desc ) WHERE ROWNUM <= 100";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();

            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                IFInformationBean bean = new IFInformationBean();
                bean.setSid(rs.getString("SID"));
                bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                bean.setBrand(rs.getString("BRAND"));
                bean.setVersion(rs.getString("VERSION"));

                if (rs.getString("STATUS").equals("P") ||
                    rs.getString("STATUS").equals("p")) {
                    bean.setStatus("處理中");
                } else if (rs.getString("STATUS").equals("A") ||
                           rs.getString("STATUS").equals("a")) {
                    bean.setStatus("會簽中");
                } else if (rs.getString("STATUS").equals("R") ||
                           rs.getString("STATUS").equals("r")) {
                    bean.setStatus("已生效");
                } else {
                    return null;
                }

                bean.setCreator(rs.getString("CREATOR"));
                bean.setSponsor_1(rs.getString("SPONSOR_1"));
                bean.setSponsor_2(rs.getString("SPONSOR_2"));
                bean.setLog_time(rs.getString("LOG_TIME"));
                bean.setTf_product_route(rs.getString("TF_PRODUCT_ROUTE"));
                bean.setTf_bom_route(rs.getString("TF_BOM_ROUTE"));
                bean.setTf_bom_reroute(rs.getString("TF_BOM_REROUTE"));
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
                bean.setTf_main_sub(rs.getString("TF_MAIN_SUB"));
                bean.setTf_main_rework(rs.getString("TF_MAIN_REWORK"));
                bean.setProduct_type(rs.getString("PRODUCT_TYPE"));
                tmp.add(bean);
            }
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (IFInformationBean[]) tmp.toArray(new IFInformationBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }

    }

    public static IFInformationBean[] SelectIFinformation(String
            product_body) {
        Connection conn = null;

        try {
            /*String sql =
                    "SELECT * FROM tf_information where product_body='" +
                    product_body + "' and product_type='XROM' " +
    //                " and status = 'R'" +
                    " order by LOG_TIME DESC, version desc"; // limit 100";*/
          StringBuffer sql = new StringBuffer();
        sql.append("select * from ");
        sql.append(" (SELECT * FROM ( ");
        sql.append("    SELECT distinct a.*, d.route_name,nvl(ceil(c.chart_log_time-a.log_time),0) as change, nvl(e.status,decode(a.status,'P','P','F') ) new_status      ");
        sql.append("      FROM tf_information a,tf_route_master c, tf_product_route d, tf_cosign_version_vw e ");
        sql.append("     where a.sid=d.sid (+) ");
        sql.append("       and a.product_body = d.product_body(+) ");
        sql.append("       and a.brand = d.brand (+) ");
        sql.append("       and a.version = d.version  (+) ");
        sql.append("       and c.route_name(+) = d.route_name ");
        sql.append("       and a.sid = e.sid (+) ");
        sql.append("      and  a.product_body = '" + product_body + "'  and a.product_type='XROM' " );
        sql.append("     order by a.LOG_TIME DESC, a.version desc ");
        sql.append("  ) aa ");
        sql.append("  union ");
        sql.append("  SELECT * FROM ( ");
        sql.append("    SELECT  distinct a.*, d.route_name,nvl(ceil(c.chart_log_time-a.log_time),0) as change, nvl(e.status,decode(a.status,'P','P','F') ) new_status   ");
        sql.append("      FROM tf_information a, tf_route_master c, tf_product_route_tx d, tf_cosign_version_vw e  ");
        sql.append("     where a.sid=d.sid  ");
        sql.append("       and a.product_body = d.product_body  ");
        sql.append("       and a.brand = d.brand  ");
        sql.append("       and a.version = d.version  ");
        sql.append("       and c.route_name = d.route_name  ");
        sql.append("       and a.sid = e.sid (+) ");
        sql.append("      and  a.product_body = '" + product_body + "'  and a.product_type='XROM' " );
        sql.append("       order by a.LOG_TIME DESC, a.version desc  ");
          sql.append("   ) bb)  ");
          sql.append(" ORDER BY cast(nvl(VERSION,0) as int) DESC       ");  
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            String sid_befor = "";
            int count = -1;
            HashMap route_change = new HashMap();
            while (rs.next()) {
                IFInformationBean bean = new IFInformationBean();
                if(rs.getInt("CHANGE") >0){
                        route_change.put(rs.getString("SID"),StringUtil.formatNull(route_change.get(rs.getString("SID")))+rs.getString("ROUTE_NAME")+";");
                }
                if(!sid_befor.equals(rs.getString("SID")))
                    count ++;

                bean.setSid(rs.getString("SID"));
                bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                bean.setBrand(rs.getString("BRAND"));
                bean.setVersion(rs.getString("VERSION"));

                if (rs.getString("NEW_STATUS").equals("P") ||
                    rs.getString("NEW_STATUS").equals("p")) {
                    bean.setStatus("處理中");
                } else if (rs.getString("NEW_STATUS").equals("A") ||
                           rs.getString("NEW_STATUS").equals("a")) {
                    bean.setStatus("會簽中");
                } else if (rs.getString("NEW_STATUS").equals("R") ||
                           rs.getString("NEW_STATUS").equals("r")) {
                    bean.setStatus("已生效");
                } else if (rs.getString("NEW_STATUS").equals("F") ||
                        rs.getString("NEW_STATUS").equals("f")) {
                    bean.setStatus("已失效");            
                } else {
                    return null;
                }

                bean.setCreator(rs.getString("CREATOR"));
                bean.setSponsor_1(rs.getString("SPONSOR_1"));
                bean.setSponsor_2(rs.getString("SPONSOR_2"));
                bean.setLog_time(rs.getString("LOG_TIME"));
                bean.setTf_product_route(rs.getString("TF_PRODUCT_ROUTE"));
                bean.setTf_bom_route(rs.getString("TF_BOM_ROUTE"));
                bean.setTf_bom_reroute(rs.getString("TF_BOM_REROUTE"));
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
                bean.setTf_main_sub(rs.getString("TF_MAIN_SUB"));
                bean.setTf_main_rework(rs.getString("TF_MAIN_REWORK"));
                bean.setProduct_type(rs.getString("PRODUCT_TYPE"));
                //tmp.add(bean);
                if(route_change.get(rs.getString("SID"))!=null)
                    bean.setRoute_change(route_change.get(rs.getString("SID")).toString());
                else
                    bean.setRoute_change("");
                if(tmp.size()>0 && tmp.size()>=count+1)
                   tmp.remove(count);

                tmp.add(count,bean);
                sid_befor = rs.getString("SID");
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (IFInformationBean[]) tmp.toArray(new IFInformationBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }
    }

  public static IFInformationBean[] SelectLastIFinformation(String
            product_body) {
        Connection conn = null;
        String maxV=null;
        try {
            String sql =
                    "SELECT max(to_number(version)) max_version FROM tf_information where product_body='" +
                    product_body + "' and product_type='XROM' ";
            TDSLogger.println(sql);
            String sql2="select * from tf_information where product_body=? and product_type='XROM' and version=?";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()){
              maxV=rs1.getString("max_version");
            }

            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1,product_body);
            ps2.setString(2,maxV);

            ResultSet rs2 = ps2.executeQuery();
            IFInformationBean tmp = new IFInformationBean();
            ArrayList list = new ArrayList();

            TDSLogger.println(maxV);
            while(rs2.next()){

                tmp.setSid(rs2.getString("SID"));
                tmp.setProduct_body(rs2.getString("PRODUCT_BODY"));
                tmp.setBrand(rs2.getString("BRAND"));
                tmp.setVersion(rs2.getString("VERSION"));

                if (rs2.getString("STATUS").equals("P") ||
                    rs2.getString("STATUS").equals("p")) {
                    tmp.setStatus("處理中");
                } else if (rs2.getString("STATUS").equals("A") ||
                           rs2.getString("STATUS").equals("a")) {
                    tmp.setStatus("會簽中");
                } else if (rs2.getString("STATUS").equals("R") ||
                           rs2.getString("STATUS").equals("r")) {
                    tmp.setStatus("已生效");
                } else {
                    return null;
                }

                tmp.setCreator(rs2.getString("CREATOR"));
                tmp.setSponsor_1(rs2.getString("SPONSOR_1"));
                tmp.setSponsor_2(rs2.getString("SPONSOR_2"));
                tmp.setLog_time(rs2.getString("LOG_TIME"));
                tmp.setTf_product_route(rs2.getString("TF_PRODUCT_ROUTE"));
                tmp.setTf_bom_route(rs2.getString("TF_BOM_ROUTE"));
                tmp.setTf_bom_reroute(rs2.getString("TF_BOM_REROUTE"));
                tmp.setTf_test_parameter_ws(rs2.getString("TF_TEST_PARAMETER_WS"));
                tmp.setTf_test_parameter_ft(rs2.getString("TF_TEST_PARAMETER_FT"));
                tmp.setTf_basic_information(rs2.getString("TF_BASIC_INFORMATION"));
                tmp.setTf_document_linkage(rs2.getString("TF_DOCUMENT_LINKAGE"));
                tmp.setTf_test_parameter_pbc(rs2.getString("TF_TEST_PARAMETER_PBC"));
                tmp.setTf_main_sub(rs2.getString("TF_MAIN_SUB"));
                tmp.setTf_main_rework(rs2.getString("TF_MAIN_REWORK"));
                tmp.setProduct_type(rs2.getString("PRODUCT_TYPE"));
                list.add(tmp);
                break;
            }

            ps.clearParameters();
            ps.close();
            rs2.close();
            if (list.isEmpty()) {
                return null;
            } else {
                return (IFInformationBean[]) list.toArray(new IFInformationBean[0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }
    }



    public static IFInformationBean[] SelectSearchIFinformation(String
            product_body) {
        Connection conn = null;

        try {
            String sql =
                    "SELECT a.*,nvl(e.status, decode(a.status, 'P', 'P', 'F')) new_status FROM tf_information  a, tf_cosign_version_vw e where a.product_body='" +
                    product_body + "' and a.product_type='XROM' and a.sid = e.sid (+) " +
                    " order by cast(nvl(a.VERSION, 0) as int) DESC "; // limit 100";
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                IFInformationBean bean = new IFInformationBean();
                bean.setSid(rs.getString("SID"));
                bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                bean.setBrand(rs.getString("BRAND"));
                bean.setVersion(rs.getString("VERSION"));

                if (rs.getString("NEW_STATUS").equals("P") ||
                    rs.getString("NEW_STATUS").equals("p")) {
                    bean.setStatus("處理中");
                } else if (rs.getString("NEW_STATUS").equals("A") ||
                           rs.getString("NEW_STATUS").equals("a")) {
                    bean.setStatus("會簽中");
                } else if (rs.getString("NEW_STATUS").equals("R") ||
                           rs.getString("NEW_STATUS").equals("r")) {
                    bean.setStatus("已生效");
                } else if (rs.getString("NEW_STATUS").equals("F") ||
                        rs.getString("NEW_STATUS").equals("f")) {
                    bean.setStatus("已失效");            
                } else {
                    return null;
                }

                bean.setCreator(rs.getString("CREATOR"));
                bean.setSponsor_1(rs.getString("SPONSOR_1"));
                bean.setSponsor_2(rs.getString("SPONSOR_2"));
                bean.setLog_time(rs.getString("LOG_TIME"));
                bean.setTf_product_route(rs.getString("TF_PRODUCT_ROUTE"));
                bean.setTf_bom_route(rs.getString("TF_BOM_ROUTE"));
                bean.setTf_bom_reroute(rs.getString("TF_BOM_REROUTE"));
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
                bean.setTf_main_sub(rs.getString("TF_MAIN_SUB"));
                bean.setTf_main_rework(rs.getString("TF_MAIN_REWORK"));
                bean.setProduct_type(rs.getString("PRODUCT_TYPE"));
                tmp.add(bean);
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (IFInformationBean[]) tmp.toArray(new IFInformationBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }
    }

    public static IFInformationBean SelectOneIFinformation(String sid, String
            product_body, String version) {
        Connection conn = null;

        try {
            String sql =
                    "SELECT * FROM tf_information where product_body='" +
                    product_body + "' and product_type='XROM' and sid='" +
                    sid + "' and version='" + version + "'";
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            IFInformationBean bean = null;
            while (rs.next()) {
            	bean = new IFInformationBean();
                bean.setSid(rs.getString("SID"));
                bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                bean.setBrand(rs.getString("BRAND"));
                bean.setVersion(rs.getString("VERSION"));

                if (rs.getString("STATUS").equals("P") ||
                    rs.getString("STATUS").equals("p")) {
                    bean.setStatus("處理中");
                } else if (rs.getString("STATUS").equals("A") ||
                           rs.getString("STATUS").equals("a")) {
                    bean.setStatus("會簽中");
                } else if (rs.getString("STATUS").equals("R") ||
                           rs.getString("STATUS").equals("r")) {
                    bean.setStatus("已生效");
                } else {
                    return null;
                }

                bean.setCreator(rs.getString("CREATOR"));
                bean.setSponsor_1(rs.getString("SPONSOR_1"));
                bean.setSponsor_2(rs.getString("SPONSOR_2"));
                bean.setLog_time(rs.getString("LOG_TIME"));
                bean.setTf_product_route(rs.getString("TF_PRODUCT_ROUTE"));
                bean.setTf_bom_route(rs.getString("TF_BOM_ROUTE"));
                bean.setTf_bom_reroute(rs.getString("TF_BOM_REROUTE"));
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
                bean.setTf_main_sub(rs.getString("TF_MAIN_SUB"));
                bean.setTf_main_rework(rs.getString("TF_MAIN_REWORK"));
                bean.setProduct_type(rs.getString("PRODUCT_TYPE"));
                break;
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            return bean;
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }

    }

    public static IFInformationBean[] SelectAllIFinformation(String productType) {
        Connection conn = null;

        try {
            StringBuffer sql = new StringBuffer();
            /*sql.append("SELECT * FROM (SELECT * FROM tf_information  ");
            if (productType != null)
            	sql.append("WHERE PRODUCT_TYPE = '" + productType + "' ");
            sql.append(" order by LOG_TIME DESC, version desc) WHERE ROWNUM <= 100 "); //limit 100";*/
            sql.append("select * from ");
            sql.append(" (SELECT * FROM ( ");
            sql.append("    SELECT distinct a.*, d.route_name,nvl(ceil(c.chart_log_time-a.log_time),0) as change, nvl(e.status,decode(a.status,'P','P','F') ) new_status   ");
            sql.append("      FROM tf_information a,tf_route_master c, tf_product_route d, tf_cosign_version_vw e ");
            sql.append("     where a.sid=d.sid (+) ");
            sql.append("       and a.product_body = d.product_body(+) ");
            sql.append("       and a.brand = d.brand (+) ");
            sql.append("       and a.version = d.version  (+) ");
            sql.append("       and c.route_name(+) = d.route_name ");
            sql.append("       and a.sid = e.sid (+) ");
            if (productType != null)
            	sql.append("   and  a.PRODUCT_TYPE = '" + productType + "' ");
            sql.append(" AND A.SID IN ( SELECT SID FROM (SELECT SID FROM TF_INFORMATION A1 WHERE A1.PRODUCT_TYPE = '" + productType + "' ORDER BY LOG_TIME DESC ) WHERE ROWNUM <= 250) ");
            sql.append("     order by a.LOG_TIME DESC, a.version desc ");
            sql.append("  ) aa ");
            sql.append("  union ");
            sql.append("  SELECT * FROM ( ");
            sql.append("    SELECT  distinct a.*, d.route_name,nvl(ceil(c.chart_log_time-a.log_time),0) as change, nvl(e.status,decode(a.status,'P','P','F') ) new_status  ");
            sql.append("      FROM tf_information a, tf_route_master c, tf_product_route_tx d, tf_cosign_version_vw e  ");
            sql.append("     where a.sid=d.sid  ");
            sql.append("       and a.product_body = d.product_body  ");
            sql.append("       and a.brand = d.brand  ");
            sql.append("       and a.version = d.version  ");
            sql.append("       and c.route_name = d.route_name  ");
            sql.append("       and a.sid = e.sid (+) ");
            if (productType != null)
            	sql.append("   and  a.PRODUCT_TYPE = '" + productType + "' ");
            sql.append(" AND A.SID IN ( SELECT SID FROM (SELECT SID FROM TF_INFORMATION A1 WHERE A1.PRODUCT_TYPE = '" + productType + "' ORDER BY LOG_TIME DESC ) WHERE ROWNUM <= 250) ");
            sql.append("       order by a.LOG_TIME DESC, a.version desc  ");
            sql.append("   ) bb)  ");
            sql.append(" order by LOG_TIME DESC, version desc  ");
            //TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql.toString());
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            String sid_befor = "";
            int count = -1;
            HashMap route_change = new HashMap();
            while (rs.next()) {
                IFInformationBean bean = new IFInformationBean();
                if(rs.getInt("CHANGE") >0){
                        route_change.put(rs.getString("SID"),StringUtil.formatNull(route_change.get(rs.getString("SID")))+rs.getString("ROUTE_NAME")+";");
                }
                if(!sid_befor.equals(rs.getString("SID"))){
                    count ++;
                	if (count == 200)
                		break;
            	}

                bean.setSid(rs.getString("SID"));
                bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                bean.setBrand(rs.getString("BRAND"));
                bean.setVersion(rs.getString("VERSION"));

                if (rs.getString("NEW_STATUS").equals("P") ||
                    rs.getString("NEW_STATUS").equals("p")) {
                    bean.setStatus("處理中");
                } else if (rs.getString("NEW_STATUS").equals("A") ||
                           rs.getString("NEW_STATUS").equals("a")) {
                    bean.setStatus("會簽中");
                } else if (rs.getString("NEW_STATUS").equals("R") ||
                           rs.getString("NEW_STATUS").equals("r")) {
                    bean.setStatus("已生效");
                } else if (rs.getString("NEW_STATUS").equals("F") ||
                        rs.getString("NEW_STATUS").equals("f")) {
                    bean.setStatus("已失效");    
                } else {
                    return null;
                }

                bean.setCreator(rs.getString("CREATOR"));
                bean.setSponsor_1(rs.getString("SPONSOR_1"));
                bean.setSponsor_2(rs.getString("SPONSOR_2"));
                bean.setLog_time(rs.getString("LOG_TIME"));
                bean.setTf_product_route(rs.getString("TF_PRODUCT_ROUTE"));
                bean.setTf_bom_route(rs.getString("TF_BOM_ROUTE"));
                bean.setTf_bom_reroute(rs.getString("TF_BOM_REROUTE"));
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
                bean.setTf_main_sub(rs.getString("TF_MAIN_SUB"));
                bean.setTf_main_rework(rs.getString("TF_MAIN_REWORK"));
                bean.setProduct_type(rs.getString("PRODUCT_TYPE"));
                if(route_change.get(rs.getString("SID"))!=null)
                    bean.setRoute_change(route_change.get(rs.getString("SID")).toString());
                else
                    bean.setRoute_change("");
                if(tmp.size()>0 && tmp.size()>=count+1)
                   tmp.remove(count);

                tmp.add(count,bean);
                sid_befor = rs.getString("SID");
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (IFInformationBean[]) tmp.toArray(new IFInformationBean[
                        0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }

    }

    /**
     * 	NVM 產品之 Wafer Sourt Route Name 為 FW 開頭，Final Test 為 FP 開頭
     *  ASM 產品之 Wafer Sourt Route Name 為 RW 開頭，Final Test 為 RP 開頭
     * @param strRouteName String
     * @return String
     * Modify content:
     * issue day:2006.03.23
     */
    public static String RouteTypeMode(String strRouteName) {
        String strMode = "";
        try {
            strRouteName.trim();
            char p = strRouteName.charAt(1);
            if (p == 'w' || p == 'W') {
              strMode = "W";
            } else if (p == 'p' || p == 'P') {
              strMode = "P";
            } else if (p == 'q' || p == 'Q') {
              strMode = "P";
            } else {
              strMode = "%";
            }
        } catch (Exception ex) {
            return "%";
        }
        return strMode;

    }

    public static void main(String[] args) {
        oiSearchService oiSearchService = new oiSearchService();
    }
}
