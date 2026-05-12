package com.mxic.oiplus.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.SQLStem;

public class OIinformation {

    public static boolean isSubmitted(String sid, String tf_info) 
    throws Exception {

        StringBuffer sql = new StringBuffer();
        Connection conn=null;
        try {
            conn=DBConnection.getConnection();
            HashMap whereStem = new HashMap();
            whereStem.put("sid", sid);
            sql.append("select "+tf_info+" from tf_information ");
            sql.append(SQLStem.getWhereStmt(whereStem));
            PreparedStatement ps1 = conn.prepareStatement(sql.toString());
            ResultSet rs = ps1.executeQuery();
            while(rs.next()){
              String submit = (String)rs.getString(tf_info);
                return (submit.equals("Y"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
        return false;
    }

    // return the sid of previous version, if not found, return 0
    public static int getPreviousVersion(int sid) 
    {

        StringBuffer sql = new StringBuffer();
        Connection conn=null;
        try {
            conn=DBConnection.getConnection();
            sql.append(
            		"select a.sid from tf_information a\n"+
            		"where exists (select 1 from tf_information b\n"+
            		"where b.sid = ?\n"+
            		"and a.product_body = b.product_body\n"+
            		"and a.brand = b.brand\n"+
            		"and a.version = b.version-1)");
            		
            PreparedStatement ps1 = conn.prepareStatement(sql.toString());
            ps1.setInt(1, sid);
            ResultSet rs = ps1.executeQuery();
            while(rs.next()){
              int presid = rs.getInt("SID");
                return presid;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(conn);
            conn = null;
        }
        return 0;
    }

}
