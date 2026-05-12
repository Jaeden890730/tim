package com.mxic.oiplus.oisearch;

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
import java.util.LinkedHashMap;
import java.io.*;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.SQLStem;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.oisearch.TFRouteDefinitionBeanAF;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.resource.*;
import com.mxic.tdsplus.util.SapEncoding;

public class oiSearchService {
    public oiSearchService() {
    }

    public static TFDescriptionBean[] SelectStepNameWS() {
        Connection conn = null;

        try {
            String sql =
                    "select * from tf_description where tag = '0' and nvl(delete_flag,'N') !='Y' order by description ";
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
                    "select * from tf_description where tag = '1' and nvl(delete_flag,'N') !='Y' order by description ";
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

    public static TFDescriptionBean[] SelectStepNameFTSTART() {
        Connection conn = null;

        try {
            String sql =
                    "select * from tf_description where tag = '1' and description like 'F%' order by description ";
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


    public static boolean UpdateEmpBasicData(oiTestRouteAForm fm) {
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
                         ",step11 = ? " +
                         ",step12 = ? " +
                         ",step13 = ? " +
                         ",step14 = ? " +
                         ",step15 = ? " +
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
            ps.setString(11, fm.getTxt_step11());
            ps.setString(12, fm.getTxt_step12());
            ps.setString(13, fm.getTxt_step13());
            ps.setString(14, fm.getTxt_step14());
            ps.setString(15, fm.getTxt_step15());

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
    public static boolean AddNew2IFInfo(oiMainControlAForm fm) {
        Connection conn = null;
        boolean flag = true;


        try {

            String sql = "insert into tf_information (sid,product_body,brand,version,status,creator,log_time, product_type,tf_prod_waferlevel) values (test_seq.nextval,?,?,?,?,?,sysdate,?,?)";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fm.getTxt_productbody());
            ps.setString(2, fm.getBrand());
            ps.setString(3, fm.getVersion());
            ps.setString(4, "P");
            ps.setString(5, fm.getCreator());
            ps.setString(6, fm.getProductType());
            ps.setString(7, "Y");

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


    public static boolean AddNewRoute(oiTestRouteAForm fm) {
        Connection conn = null;
        boolean flag = true;
        String strMode = RouteTypeMode(fm.getTxt_routename());
        if (strMode == "%") {
            flag = false;
            return flag;
        }

        try {

            String sql = "insert into tf_route_master (route_name,step1,step2,step3,step4,step5,step6,step7,step8,step9,step10,step11,step12,step13,step14,step15,type) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            ps.setString(12, fm.getTxt_step11());
            ps.setString(13, fm.getTxt_step12());
            ps.setString(14, fm.getTxt_step13());
            ps.setString(15, fm.getTxt_step14());
            ps.setString(16, fm.getTxt_step15());
            ps.setString(17, strMode);

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
    
    public static String CheckSameRoute(oiTestRouteAForm fm) {
        Connection conn = null;
        String  route_name = "";
        try {

            String sql =  "SELECT route_name FROM tf_route_master ";
                   sql +=  "where substr(route_name,1,1) ='F' ";
                   sql +=  "  and step1 " + (fm.getTxt_step1().equals("")?"IS NULL":"='" + fm.getTxt_step1()+"'" ); 
                   sql +=  "  and step2 " + (fm.getTxt_step2().equals("")?"IS NULL":"='" + fm.getTxt_step2()+"'" );
                   sql +=  "  and step3 " + (fm.getTxt_step3().equals("")?"IS NULL":"='" + fm.getTxt_step3()+"'" );
                   sql +=  "  and step4 " + (fm.getTxt_step4().equals("")?"IS NULL":"='" + fm.getTxt_step4()+"'" );
                   sql +=  "  and step5 " + (fm.getTxt_step5().equals("")?"IS NULL":"='" + fm.getTxt_step5()+"'" );
                   sql +=  "  and step6 " + (fm.getTxt_step6().equals("")?"IS NULL":"='" + fm.getTxt_step6()+"'" );
                   sql +=  "  and step7 " + (fm.getTxt_step7().equals("")?"IS NULL":"='" + fm.getTxt_step7()+"'" );
                   sql +=  "  and step8 " + (fm.getTxt_step8().equals("")?"IS NULL":"='" + fm.getTxt_step8()+"'" );
                   sql +=  "  and step9 " + (fm.getTxt_step9().equals("")?"IS NULL":"='" + fm.getTxt_step9()+"'" );
                   sql +=  "  and step10 " + (fm.getTxt_step10().equals("")?"IS NULL":"='" + fm.getTxt_step10()+"'" );
                   sql +=  "  and step11 " + (fm.getTxt_step11().equals("")?"IS NULL":"='" + fm.getTxt_step11()+"'" );
                   sql +=  "  and step12 " + (fm.getTxt_step12().equals("")?"IS NULL":"='" + fm.getTxt_step12()+"'" );
                   sql +=  "  and step13 " + (fm.getTxt_step13().equals("")?"IS NULL":"='" + fm.getTxt_step13()+"'" );
                   sql +=  "  and step14 " + (fm.getTxt_step14().equals("")?"IS NULL":"='" + fm.getTxt_step14()+"'" );
                   sql +=  "  and step15 " + (fm.getTxt_step15().equals("")?"IS NULL":"='" + fm.getTxt_step15()+"'" );
                    
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            TDSLogger.println("sql="+sql);

            while (rs.next()) {
            	route_name = rs.getString("ROUTE_NAME");
            }
            ps.clearParameters();
            ps.close();
            rs.close();

        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
        } finally {
          DBConnection.close(conn);
        }
        return route_name;
    }


    public static TFProductRouteBean[] LookUpTFProductRoute(String route_name, boolean active_flag) {
        Connection conn = null;

        try {
            String sql = "SELECT b.sid, b.product_body, b.brand, b.version, nvl(e.status,decode(f.status,'P','P','F') ) status, b.route_name, b.step_seq, b.step_name, b.temperature, b.sampling_test, b.test_time, b.time_unit, b.remark \n" +
                         "FROM tf_product_route_tx b, tf_cosign_version_vw e, tf_information f where b.route_name='" +
                         route_name + "' and b.sid = f.sid and b.sid = e.sid (+) \n";
                         if(!active_flag){
                              sql+= "and nvl(e.status, decode(f.status, 'P', 'P', 'F')) !='F' \n";	 
                         }
                         sql+="union " +
                         "SELECT max(a.sid) sid, a.product_body, a.brand, max(a.version) version, nvl(e.status,decode(f.status,'P','P','F') ) status, a.route_name, a.step_seq, a.step_name, a.temperature, a.sampling_test, a.test_time, a.time_unit, a.remark \n" +
                         "FROM tf_product_route a, tf_cosign_version_vw e, tf_information f where a.route_name='" +
                         route_name + "' and a.sid = f.sid and a.sid = e.sid (+) \n";
                         if(!active_flag){
                              sql+= "and nvl(e.status, decode(f.status, 'P', 'P', 'F')) !='F' \n";	 
                         }
                         sql+="group by a.product_body, a.brand, a.route_name, nvl(e.status,decode(f.status,'P','P','F') ), a.step_seq, a.step_name, a.temperature, a.sampling_test, a.test_time, a.time_unit, a.remark";

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
                if (rs.getString("STATUS").equals("P") ||
                        rs.getString("STATUS").equals("p")) {
                        bean.setStatus("處理中");
                } else if (rs.getString("STATUS").equals("A") ||
                        rs.getString("STATUS").equals("a")) {
                        bean.setStatus("會簽中");
                } else if (rs.getString("STATUS").equals("R") ||
                        rs.getString("STATUS").equals("r")) {
                        bean.setStatus("已生效");
                } else if (rs.getString("STATUS").equals("F") ||
                        rs.getString("STATUS").equals("f")) {
                        bean.setStatus("已失效");    
                } else {
                	    bean.setStatus(" ");  
                }
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
                bean.setStep11(rs.getString("step11"));
                bean.setStep12(rs.getString("step12"));
                bean.setStep13(rs.getString("step13"));
                bean.setStep14(rs.getString("step14"));
                bean.setStep15(rs.getString("step15"));
                bean.setFile_name_stdexcflow(rs.getString("stdexcflow_filename")!=null?rs.getString("stdexcflow_filename"):"");
                bean.setFile_name_testflow(rs.getString("testflow_filename")!=null?rs.getString("testflow_filename"):"");
                bean.setRoute_cat(rs.getString("route_cat"));
                bean.setRemark(rs.getString("remark"));
                bean.setChart_log_time(rs.getString("chart_log_time"));
                if(rs.getString("testflow_filename")!=null ){
                        bean.setShow_testflow("show");
                }else{
                        bean.setShow_testflow("no");
                }
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

    public static TFRouteMasterBean getRouteMasterBean(String route_name){
    	TFRouteMasterBean[] tfRMBBeanList = SelectTheTestRoute(route_name);
    	if(tfRMBBeanList != null && tfRMBBeanList.length > 0)
    		return tfRMBBeanList[0];
    	return null;
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
                bean.setStep11(rs.getString("step11"));
                bean.setStep12(rs.getString("step12"));
                bean.setStep13(rs.getString("step13"));
                bean.setStep14(rs.getString("step14"));
                bean.setStep15(rs.getString("step15"));
                bean.setFile_name_stdexcflow(rs.getString("stdexcflow_filename")!=null?rs.getString("stdexcflow_filename"):"");
                bean.setFile_name_testflow(rs.getString("testflow_filename")!=null?rs.getString("testflow_filename"):"");
                bean.setRoute_cat(rs.getString("route_cat"));
                bean.setRemark(rs.getString("remark"));
                bean.setChart_log_time(rs.getString("chart_log_time"));
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
                sql = "SELECT * FROM ( SELECT * FROM tf_information where status = 'r' or status = 'R'   "; //limit 100";
                break;

            }
            case 3: {
                sql = "SELECT * FROM ( SELECT * FROM tf_information where status = 'p' or status = 'P'   "; //limit 100";
                break;

            }
            case 4: {
                sql = "SELECT * FROM ( SELECT * FROM tf_information where status = 'a' or status = 'A'   "; //limit 100";
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
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
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
            product_body, String brand) {
        Connection conn = null;

        try {
            /*String sql =
                    "SELECT * FROM tf_information where product_body='" +
                    product_body + "' and brand='" + brand + "' " +
    //                " and status = 'R'" +
                    " order by LOG_TIME DESC, version desc"; // limit 100";*/
          StringBuffer sql = new StringBuffer();
          sql.append("select * from ");
          sql.append(" (SELECT * FROM ( ");
          sql.append("    SELECT distinct a.*, d.route_name,nvl(ceil(c.chart_log_time-a.log_time),0) as change, nvl(e.status,decode(a.status,'P','P','F') ) new_status    ");
          sql.append("      FROM tf_information a,tf_route_master c, tf_product_route d, tf_cosign_version_vw e ");
          sql.append("     where a.sid=d.sid (+) ");
          sql.append("       and a.product_body = d.product_body(+) ");
          sql.append("       and a.brand = d.brand (+) ");
          sql.append("       and a.version = d.version  (+) ");
          sql.append("       and c.route_name(+) = d.route_name ");
          sql.append("       and a.sid = e.sid (+) ");
          sql.append("      and  a.product_body = '" + product_body + "'  and a.brand='" + brand + "' " );
          sql.append("     order by a.LOG_TIME DESC, a.version desc ");
          sql.append("  ) aa ");
          sql.append("  union ");
          sql.append("  SELECT * FROM ( ");
          sql.append("    SELECT  distinct a.*, d.route_name,nvl(ceil(c.chart_log_time-a.log_time),0) as change, nvl(e.status,decode(a.status,'P','P','F') ) new_status    ");
          sql.append("      FROM tf_information a, tf_route_master c, tf_product_route_tx d, tf_cosign_version_vw e   ");
          sql.append("     where a.sid=d.sid  ");
          sql.append("       and a.product_body = d.product_body  ");
          sql.append("       and a.brand = d.brand  ");
          sql.append("       and a.version = d.version  ");
          sql.append("       and c.route_name = d.route_name  ");
          sql.append("       and a.sid = e.sid (+) ");
          sql.append("      and  a.product_body = '" + product_body + "'  and  a.brand='" + brand + "' " );
          sql.append("       order by a.LOG_TIME DESC, a.version desc  ");
          sql.append("   ) bb)  ");
            //sql.append(" order by LOG_TIME DESC, version desc  ");
            sql.append(" ORDER BY cast(nvl(VERSION,0) as int) DESC       ");
            TDSLogger.println(sql.toString());
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
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
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
            product_body,String brand) {
        Connection conn = null;
        String maxV=null;
        try {
            String sql =
                    "SELECT max(to_number(version)) max_version FROM tf_information where product_body='" +
                    product_body + "' and brand='"+brand+"' ";
            TDSLogger.println(sql);
            String sql2="select * from tf_information where product_body=? and brand=? and version=?";
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()){
              maxV=rs1.getString("max_version");
            }

            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setString(1,product_body);
            ps2.setString(2,brand);
            ps2.setString(3,maxV);

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
                tmp.setTf_test_parameter_ws(rs2.getString("TF_TEST_PARAMETER_WS"));
                tmp.setTf_test_parameter_ft(rs2.getString("TF_TEST_PARAMETER_FT"));
                tmp.setTf_basic_information(rs2.getString("TF_BASIC_INFORMATION"));
                tmp.setTf_document_linkage(rs2.getString("TF_DOCUMENT_LINKAGE"));
                tmp.setTf_test_parameter_pbc(rs2.getString("TF_TEST_PARAMETER_PBC"));
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
            product_body, String brand) {
        Connection conn = null;

        try {
            String sql =
                    "SELECT a.*,nvl(e.status, decode(a.status, 'P', 'P', 'F')) new_status FROM tf_information  a, tf_cosign_version_vw e  where a.product_body='" +
                    product_body + "' and a.brand='" + brand +
                    "' and a.sid = e.sid (+) order by cast(nvl(a.VERSION, 0) as int) DESC"; // limit 100";
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
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
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
            product_body, String brand, String version) {
        Connection conn = null;

        try {
            /*String sql =
                    "SELECT * FROM tf_information where product_body='" +
                    product_body + "' and brand='" + brand + "' and sid='" +
                    sid + "' and version='" + version + "'";
            */
            String sql = "SELECT * FROM (SELECT a.*, b.package_component\n" +
            			"               FROM tf_information a, (select distinct product_body, brand, package_component from TF_PROD_EPN) b\n" + 
            			"               WHERE a.product_body=? and a.brand=? and a.sid=? and a.version=?\n" +
            			"					and a.product_body = b.product_body(+) and a.brand = b.brand(+)\n" +
            			"				)";
            
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, product_body);
            ps.setString(2, brand);
            ps.setString(3, sid);
            ps.setString(4, version);
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
                bean.setTf_prod_waferlevel("TF_PROD_WAFERLEVEL");
                bean.setTf_product_route(rs.getString("TF_PRODUCT_ROUTE"));
                bean.setTf_bom_route(rs.getString("TF_BOM_ROUTE"));
                bean.setTf_bom_mcp_route(rs.getString("TF_BOM_MCP_ROUTE"));
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
                bean.setProduct_type(rs.getString("PRODUCT_TYPE"));
                bean.setPackage_component(rs.getString("PACKAGE_COMPONENT"));

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
            sql.append(" order by LOG_TIME DESC, version desc) WHERE ROWNUM <= 100"); //limit 100";*/
            sql.append("select * from ");
            sql.append(" (SELECT * FROM ( ");
            sql.append("    SELECT distinct a.*, d.route_name,nvl(ceil(c.chart_log_time-a.log_time),0) as change, nvl(e.status,decode(a.status,'P','P','F') ) new_status  ");
            sql.append("      FROM tf_information a,tf_route_master c, tf_product_route d, tf_cosign_version_vw e  ");
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
                if(!sid_befor.equals(rs.getString("SID"))) {
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
                bean.setTf_test_parameter_ws(rs.getString("TF_TEST_PARAMETER_WS"));
                bean.setTf_test_parameter_ft(rs.getString("TF_TEST_PARAMETER_FT"));
                bean.setTf_basic_information(rs.getString("TF_BASIC_INFORMATION"));
                bean.setTf_document_linkage(rs.getString("TF_DOCUMENT_LINKAGE"));
                bean.setTf_test_parameter_pbc(rs.getString("TF_TEST_PARAMETER_PBC"));
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
                return (IFInformationBean[]) tmp.toArray(new IFInformationBean[0]);
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

    public static TFRouteDefinitionBeanAF[] SelectRouteOption(String product_body) {
        Connection conn = null;

        try {
            String sql = null;
            if (product_body.equals(""))
              sql = "SELECT * FROM tf_route_option order by route_option"; // 抓 Route Definition
            else
              sql = "SELECT * FROM tf_prod_route_option where product_body='" +
                    product_body + "'";  // 抓 Route Definition by Product Body
            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFRouteDefinitionBeanAF bean = new TFRouteDefinitionBeanAF();
                if (!product_body.equals("")) {
                	bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                }
                bean.setWs_route(rs.getString("WS_ROUTE"));
                bean.setFt_route(rs.getString("FT_ROUTE"));
                bean.setWs_route_add(rs.getString("WS_ROUTE_ADD"));
                bean.setFt_route_add(rs.getString("FT_ROUTE_ADD"));
                bean.setRoute_option(rs.getString("ROUTE_OPTION"));
                bean.setRoute_option_description(rs.getString("OPTION_DESC"));
                tmp.add(bean);
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFRouteDefinitionBeanAF[]) tmp.toArray(new TFRouteDefinitionBeanAF[0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }
    }
    
	public static String ChkRouteOption(String route_option) {
		Connection conn = null;
		String msg = "true";
		StringBuffer prods = new StringBuffer();
		try {
			String sql = null;
			if (route_option.equals(""))
				return "false, null route option";
			else
				sql = "SELECT * FROM tf_prod_route_option where ROUTE_OPTION='" + route_option + "'";
			TDSLogger.println(sql);
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList tmp = new ArrayList();
			while (rs.next()) {
				prods.append(rs.getString("PRODUCT_BODY"));
				prods.append(",");
			}
			ps.clearParameters();
			ps.close();
			rs.close();

			if(prods.length()!=0){
				msg = "false, can't modify. product using already in " + prods.toString();
			}
			return msg;
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return "false, " + ex.getMessage();
		} finally {
			DBConnection.close(conn);
		}
	}
    
	public static String updateRouteOption(TFRouteDefinitionBeanAF item) {
		Connection conn = null;

		try {
			String s = ChkRouteOption(item.getRoute_option());
			if (!"true".equals(s)) {
				return s;
			}

            String sql = "update tf_route_option set option_desc = '" + StringUtil.Utf8ToBig5(item.getRoute_option_description()) +
                "', ft_route = '" + item.getFt_route() +
                "', ws_route = '" + item.getWs_route() +
                "', ft_route_add = '" + item.getFt_route_add() +
                "', ws_route_add = '" + item.getWs_route_add() +
                "' where route_option = '"  + item.getRoute_option()+"'";

			TDSLogger.println(sql);
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();
			ps.clearParameters();
			ps.close();
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return "false" + ex.getMessage();
		} finally {
			DBConnection.close(conn);
		}
		return "true";
	}
    
    public static String insertRouteOption(TFRouteDefinitionBeanAF item) {
        Connection conn = null;

        try {
            String sql = "insert into tf_route_option select " +
                "'" + item.getFt_route() +
                "','" + item.getWs_route() +
                "','" + item.getFt_route_add() +
                "','" + item.getWs_route_add() +
                "','" + item.getRoute_option() +
                "','" + StringUtil.Utf8ToBig5(item.getRoute_option_description()) +
                "' from dual where not exists (select 1 from tf_route_option t " +
                "where  '"+item.getFt_route()+"' = t.ft_route " +
                "      and '"+item.getWs_route()+"' = t.ws_route " +
                "      and '"+item.getRoute_option()+"' = t.Route_option " +
                "      and nvl('"+item.getFt_route_add()+"','NN') = nvl(t.ft_route_add,'NN') " +
                "      and nvl('"+item.getWs_route_add()+"','NN') = nvl(t.ws_route_add,'NN')) ";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            ps.clearParameters();
            ps.close();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return "false";
        } finally {
          DBConnection.close(conn);
        }
        return "true";
    }
    
    public static String deleteRouteOption(String[] item) {
        Connection conn = null;

        try {
			String s = ChkRouteOption(item[4]);
			if (!"true".equals(s)) {
				return  s;
			}

            String sql = null;
            sql = "DELETE FROM tf_route_option where ft_route = '" + item[0] +
                "' and ws_route = '" + item[2] +
                "' and nvl(ft_route_add,'AA') = nvl('" + item[1] +
                "','AA') and nvl(ws_route_add,'AA') = nvl('" + item[3] + "','AA')";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            ps.clearParameters();
            ps.close();
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return "false";
        } finally {
          DBConnection.close(conn);
        }
        return "true";
    }
    
    
	public static boolean insertRouteOptionLog(String route_option, String updater, String form_no, String[] items) {
		return insertRouteOptionLog(route_option, updater, form_no + "~" + items[0] + "~" + items[1] + "~" + items[2] + "~" + items[3] + "~" + items[4] + "~" + items[5]);
	}

	public static boolean insertRouteOptionLog(String route_option, String updater, String form_no, TFRouteDefinitionBeanAF tf) {
		return insertRouteOptionLog(route_option, updater, form_no + "~" + tf.getFt_route() + "~" + tf.getFt_route_add() + "~" + tf.getWs_route() + "~" + tf.getWs_route_add() + "~" + tf.getRoute_option() + "~" + tf.getRoute_option_description());
	}
    
	public static boolean insertRouteOptionLog(String route_option, String updater, String descrip) {
		String InsSQL = null;
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			InsSQL = "insert into LOG_TF_ROUTE_OPTION "
					+ "(route_option,updater,descrip,lastupdate) "
					+ "values (?,?,?,sysdate)";
			PreparedStatement ps2 = conn.prepareStatement(InsSQL);
			ps2.setString(1, route_option);
			ps2.setString(2, updater);
			ps2.setString(3, descrip);
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
	
	public static TFRouteDefinitionBeanAF[] SelectRouteOptionLog() {
		Connection conn = null;

		try {
			String sql = "SELECT * FROM TIM.LOG_TF_ROUTE_OPTION ORDER BY LASTUPDATE DESC";
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList tmp = new ArrayList();
			while (rs.next()) {
				TFRouteDefinitionBeanAF bean = new TFRouteDefinitionBeanAF();
				bean.setUpdater(rs.getString("UPDATER"));
				bean.setLastupdate(rs.getString("LASTUPDATE"));
				String[] s = rs.getString("DESCRIP").split("~");
				bean.setForm_no(s[0]);
				bean.setAction(s[1]);
				bean.setFt_route(s[2]);
				bean.setFt_route_add(s[3]);
				bean.setWs_route(s[4]);
				bean.setWs_route_add(s[5]);
				bean.setRoute_option(s[6]);
				bean.setRoute_option_description(s[7]);
				tmp.add(bean);
			}
			ps.clearParameters();
			ps.close();
			rs.close();
			if (tmp.isEmpty()) {
				return null;
			} else {
				return (TFRouteDefinitionBeanAF[]) tmp.toArray(new TFRouteDefinitionBeanAF[0]);
			}
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
			DBConnection.close(conn);
		}
	}

    public static TFRouteDefinitionBeanAF[] SelectAvailableRouteOption(String product_body, String username) {
        Connection conn = null;

        try {
            String sql = null;
            // 以下只抓已生效 version
            sql = "select distinct a.product_body,a.ft_route,a.ft_route_add,a.ws_route,a.ws_route_add,c.route_option \n" +
                "from tf_bom_route a, tf_current_version_vw b, tf_route_option c \n" +
                "where a.product_body = '"+product_body+"' \n" +
                "      and a.sid = b.sid \n" +
                "      and a.ft_route = c.ft_route and a.ws_route = c.ws_route \n" +
                "      and not exists (select 1 from tf_prod_route_option t  \n" +
                "                      where '"+product_body+"' = t.product_body  \n" +
                "                            and a.ft_route = t.ft_route  \n" +
                "                            and a.ws_route = t.ws_route  \n" +
                "                            and nvl(a.ft_route_add,'NN') = nvl(t.ft_route_add,'NN')  \n" +
                "                            and nvl(a.ws_route_add,'NN') = nvl(t.ws_route_add,'NN'))  \n" +
				"union\n" +
				"select distinct a.product_body,a.ft_route,a.ft_route_add,a.ws_route,a.ws_route_add,c.route_option\n" + 
				"from tf_bom_route_mcp a, tf_current_version_vw b, tf_route_option c\n" + 
				"where a.product_body = '"+product_body+"' \n" + 
				"      and a.sid = b.sid\n" + 
				"      and a.ft_route = c.ft_route and a.ws_route = c.ws_route\n" + 
				"      and not exists (select 1 from tf_prod_route_option t\n" + 
				"                      where '"+product_body+"' = t.product_body\n" + 
				"                            and a.ft_route = t.ft_route\n" + 
				"                            and a.ws_route = t.ws_route\n" + 
				"                            and nvl(a.ft_route_add,'NN') = nvl(t.ft_route_add,'NN')\n" + 
				"                            and nvl(a.ws_route_add,'NN') = nvl(t.ws_route_add,'NN'))" +
                "order by ft_route,ft_route_add,ws_route,ws_route_add \n";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ArrayList tmp = new ArrayList();
            while (rs.next()) {
                TFRouteDefinitionBeanAF bean = new TFRouteDefinitionBeanAF();
                bean.setProduct_body(rs.getString("PRODUCT_BODY"));
                bean.setWs_route(rs.getString("WS_ROUTE"));
                bean.setFt_route(rs.getString("FT_ROUTE"));
                bean.setWs_route_add(rs.getString("WS_ROUTE_ADD"));
                bean.setFt_route_add(rs.getString("FT_ROUTE_ADD"));
                bean.setRoute_option(rs.getString("ROUTE_OPTION"));
                bean.setUpdater(username);
                tmp.add(bean);
            }
            ps.clearParameters();
            ps.close();
            rs.close();
            if (tmp.isEmpty()) {
                return null;
            } else {
                return (TFRouteDefinitionBeanAF[]) tmp.toArray(new TFRouteDefinitionBeanAF[0]);
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            TDSLogger.println(ex.getMessage());
            return null;
        } finally {
          DBConnection.close(conn);
        }
    }

    public static boolean deleteRouteDefinition(String items) {
        Connection conn = null;

        try {
            String item[] = (items+";EOF").split(";"); // product_body;ft_route;ft_route_add;ws_route;ws_route_add
            String sql = null;
            sql = "DELETE FROM tf_prod_route_option where product_body='" + item[0] +
                "' and ft_route = '" + item[1] +
                "' and ws_route = '" + item[3] +
                "' and nvl(ft_route_add,'AA') = nvl('" + item[2] +
                "','AA') and nvl(ws_route_add,'AA') = nvl('" + item[4] + "','AA')";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            ps.clearParameters();
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

    public static boolean updateRouteDefinition(TFRouteDefinitionBeanAF item, String username) {
        Connection conn = null;

        try {
            String sql = "update tf_prod_route_option set option_desc = '" + StringUtil.Utf8ToBig5(item.getRoute_option_description()) +
                "', updater = '" + username +
                "' where ft_route = '"  + item.getFt_route() +
                "' and ws_route = '"  + item.getWs_route() +
                "' and nvl(ft_route_add,'AA') = nvl('"  + item.getFt_route_add() +
                "','AA') and nvl(ws_route_add,'AA') = nvl('"  + item.getWs_route_add() + "','AA')";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            ps.clearParameters();
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

    public static boolean insertRouteDefinition(TFRouteDefinitionBeanAF item, String username) {
        Connection conn = null;

        try {
            String sql = "insert into tf_prod_route_option select " +
                "'" + item.getProduct_body() +
                "','" + item.getFt_route() +
                "','" + item.getWs_route() +
                "','" + item.getFt_route_add() +
                "','" + item.getWs_route_add() +
                "','" + item.getRoute_option() +
                "','" + StringUtil.Utf8ToBig5(item.getRoute_option_description()) +
                "',sysdate,'" + username +
                "' from dual where not exists (select 1 from tf_prod_route_option t " +
                "where '"+item.getProduct_body()+"' = t.product_body " +
                "      and '"+item.getFt_route()+"' = t.ft_route " +
                "      and '"+item.getWs_route()+"' = t.ws_route " +
                "      and nvl('"+item.getFt_route_add()+"','NN') = nvl(t.ft_route_add,'NN') " +
                "      and nvl('"+item.getWs_route_add()+"','NN') = nvl(t.ws_route_add,'NN')) ";

            TDSLogger.println(sql);
            conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeQuery();
            ps.clearParameters();
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

    /*public static boolean SentRouteDefinitionToPRM2(String product_body) {
      Connection conn = null;
      FileOutputStream os=null;
      PrintWriter pr=null;
      TDSProperties prop = TDSResource.getProperties("TIMPdf");
      String filename;

      try {
        filename = prop.getProperty("DIR_ROUTE_OPTION")+"TIM04-"+(String)DateUtil.Timestamp2String(new java.util.Date(),"yyyyMMddkkmmss");
        os = new FileOutputStream(filename);
        pr = new PrintWriter(os);

        String sql = "SELECT * FROM tf_prod_route_option where product_body='" + product_body + "'";
        StringBuffer output = new StringBuffer();
        TDSLogger.println(sql);
        conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ArrayList tmp = new ArrayList();
        while (rs.next()) {
          output.delete(0,output.length());
          output.append(rs.getString("PRODUCT_BODY"));
          output.append(rs.getString("FT_ROUTE")+padding(12-rs.getString("FT_ROUTE").length()));
          output.append(StringUtil.NullConvert(rs.getString("FT_ROUTE_ADD"))+padding(12-StringUtil.NullConvert(rs.getString("FT_ROUTE_ADD")).length()));
          output.append(rs.getString("WS_ROUTE")+padding(12-rs.getString("WS_ROUTE").length()));
          output.append(StringUtil.NullConvert(rs.getString("WS_ROUTE_ADD"))+padding(12-StringUtil.NullConvert(rs.getString("WS_ROUTE_ADD")).length()));
          output.append(rs.getString("ROUTE_OPTION")+padding(2-rs.getString("ROUTE_OPTION").length()));
          output.append(StringUtil.unicodeToBig5(rs.getString("OPTION_DESC")+padding(64-rs.getString("OPTION_DESC").length())));
          pr.println(output.toString());
        }
        ps.clearParameters();
        ps.close();
        rs.close();
        pr.close();
        DBConnection.close(conn);
        Exec("chmod 777 "+filename);
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
        return false;
      } finally {
        DBConnection.close(conn);
      }
      return true;
    }*/

    // #217144
    public static boolean SentRouteDefinitionToPRM2(String product_body) {
		Connection conn = null;
		FileOutputStream os = null;
		PrintWriter pr = null;
		TDSProperties prop = TDSResource.getProperties("TIMPdf");
		String filename;

		try {
			filename = prop.getProperty("DIR_ROUTE_OPTION")
					+ "TIM04-"
					+ (String) DateUtil.Timestamp2String(new java.util.Date(),
							"yyyyMMddkkmmss");
			os = new FileOutputStream(filename);
			pr = new PrintWriter(os);

			String sql = "SELECT * FROM tf_prod_route_option where product_body='"
					+ product_body + "'";
			StringBuffer output = new StringBuffer();
			TDSLogger.println(sql);
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList tmp = new ArrayList();

			String eifName = "EIFPEISPRMII04";
			while (rs.next()) {
				LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
				output.delete(0, output.length());
				info.put("PRODUCT_BODY", rs.getString("PRODUCT_BODY"));
				info.put("FT_ROUTE", rs.getString("FT_ROUTE"));
				info.put("FT_ROUTE_ADD", StringUtil.NullConvert(rs.getString("FT_ROUTE_ADD")));
				info.put("WS_ROUTE", rs.getString("WS_ROUTE"));
				info.put("WS_ROUTE_ADD", StringUtil.NullConvert(rs.getString("WS_ROUTE_ADD")));
				info.put("ROUTE_OPTION", rs.getString("ROUTE_OPTION"));
				info.put("OPTION_DESC", StringUtil.unicodeToBig5(rs.getString("OPTION_DESC")));

				output = SapEncoding.formatOutput(info, eifName);
				pr.print(output.toString());
			}
			ps.clearParameters();
			ps.close();
			rs.close();
			pr.close();
			DBConnection.close(conn);
			Exec("chmod 777 " + filename);
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return false;
		} finally {
			DBConnection.close(conn);
		}
		return true;
	}
    
    // generate a 'blank' string with length = 'width'
    private static String padding(int width) {
      String str = "";
      for (int i = 0; i < width; i++)
        str = str + " ";
      return str;
    }

    public static boolean Exec(String command){
       try{
           int returnValue;
           StringBuffer bf1 = new StringBuffer();
           StringBuffer bf2 = new StringBuffer();

           SafeExec se = new SafeExec(command);
           returnValue = se.perform(bf1, bf2);
           TDSLogger.println("RCM: "+command);
           TDSLogger.println("RCM: Return Value = " + returnValue);
           TDSLogger.println("RCM: Std Output = \n" + bf1);
           TDSLogger.println("RCM: Err Output = \n" + bf2);
           if(returnValue == 0){
//               setMessage(bf1.toString());
               return true;
           }else{
//               setMessage(bf2.toString());
               return false;
           }
       }catch(Exception e){
           TDSLogger.println("RCM: "+e);
           return false;
       }
   }

   public static void main(String[] args) {
        oiSearchService oiSearchService = new oiSearchService();
    }
}
