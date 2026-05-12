/**
 * @(#)CommonDB.java  VER.1.00 22/10/2002.
 *
 * Copyright (c) 2002 CIM Information Technology Ltd.
 *
 * This Software is the confidential and proprietary information of
 * CIM Information Technology Ltd.You shall not disclose such
 * Confidential Information and use it only in accordance with the terms
 * of the licence agrement you entered  with CIT.
 *
 */
package com.mxic.oiplus.common;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.oiplus.util.TDSLogger;
/**
 *
 *  <p>
 *  This bean also commonly accessed by most of the  subsystem,to
 *  do only database operations for BASIC tables.
 *  </p>
 *
 */
class CommonDB
{

    /*
     *<p>Empty constructor</p>
     */
     public CommonDB()
     {}

    /*
     *<p>This method is used to Select only ProductType </p>
	 *
	 * @param con The rich resource connection object is, passed as
	 * an argument.
	 *
	 * @return data The value of the table row, returned as
	 * a ArrayList.
	 */
     public static void main(String args[])throws Exception{
       CommonDB d = new CommonDB();
       //d.getFlow_no(DBConnection.getConnection(),"S1","KYEC","0503");

     }
     
	    public ArrayList getAEBGrade(Connection con) throws SQLException
		{

		        Statement st=con.createStatement();
		        ResultSet rs=st.executeQuery("select ID,DESCRIPTION from tf_description WHERE TAG=24 ORDER BY ID");
		        ArrayList data=new ArrayList();
		        while(rs.next())
		        {
		        	AEBGradeBeanForm obj=new AEBGradeBeanForm();
                    obj.setId(rs.getString("ID"));
		        	obj.setDescription(rs.getString("DESCRIPTION"));
		        	data.add(obj);
		        }
		        if(st!=null)
		            st.close();
		        if(rs!=null)
		            rs.close();
		        return data;
        }
	    public ArrayList getHWConfigureItem(Connection con) throws SQLException
		{

		        Statement st=con.createStatement();
		        ResultSet rs=st.executeQuery("select ID,DESCRIPTION from tf_description WHERE TAG=51");
		        ArrayList data=new ArrayList();
		        while(rs.next())
		        {
		        	HWConfigureBeanForm obj=new HWConfigureBeanForm();
                    obj.setId(rs.getString("ID"));
		        	obj.setDescription(rs.getString("DESCRIPTION"));
		        	data.add(obj);
		        }
		        if(st!=null)
		            st.close();
		        if(rs!=null)
		            rs.close();
		        return data;
        }
        
	    public ArrayList getSortRouteCodeItem(Connection con, String productBody, String brand, String maskOption) throws SQLException
		{
	    	String sql = 
	    		"select distinct product_body, brand, version, db_with_code, sort_route_code, \n" +
	    		"		ws_route, ws_route_add, wsspecialcontrol, tf_ws_comment, quality_level, quality_level_comment, mcp_flag\n" +
	    		"from tf_bom_route a\n" + 
	    		"where a.product_body = '" + productBody + "' AND A.BRAND = '" + brand +"' and a.mask_option = '" + maskOption + "' and a.mcp_flag in ('MCP', 'SCP_MCP') and a.tag != 2\n" + 
	    		"      and version = (select max(b.version) from tf_bom_route b\n" + 
	    		"                    where b.product_body = '" + productBody + "' AND B.BRAND ='" + brand + "' and b.mask_option = '" + maskOption + "')";

		        Statement st=con.createStatement();
		        ResultSet rs=st.executeQuery(sql);
		        
		        ArrayList<HashMap<String,String>> data=new ArrayList<HashMap<String,String>>();
		        while(rs.next())
		        {
		        	HashMap<String,String> hm = new HashMap<String,String>();
		        	hm.put("PRODUCT_BODY", rs.getString("PRODUCT_BODY"));
		        	hm.put("BRAND", rs.getString("BRAND"));
		        	hm.put("VERSION", rs.getString("VERSION"));
		        	hm.put("DB_WITH_CODE", rs.getString("DB_WITH_CODE")==null?"":rs.getString("DB_WITH_CODE"));
		        	hm.put("SORT_ROUTE_CODE", rs.getString("SORT_ROUTE_CODE")==null?"":rs.getString("SORT_ROUTE_CODE"));
		        	hm.put("WS_ROUTE", rs.getString("WS_ROUTE")==null?"":rs.getString("WS_ROUTE"));
		        	hm.put("WS_ROUTE_ADD", rs.getString("WS_ROUTE_ADD")==null?"":rs.getString("WS_ROUTE_ADD"));
		        	hm.put("WSSPECIALCONTROL", rs.getString("WSSPECIALCONTROL")==null?"":rs.getString("WSSPECIALCONTROL"));
		        	hm.put("TF_WS_COMMENT", rs.getString("TF_WS_COMMENT")==null?"":rs.getString("TF_WS_COMMENT"));
		        	hm.put("QUALITY_LEVEL", rs.getString("QUALITY_LEVEL")==null?"":rs.getString("QUALITY_LEVEL"));
		        	hm.put("QUALITY_LEVEL_COMMENT", rs.getString("QUALITY_LEVEL_COMMENT")==null?"":rs.getString("QUALITY_LEVEL_COMMENT"));
		        	hm.put("MCP_FLAG", rs.getString("MCP_FLAG")==null?"":rs.getString("MCP_FLAG"));
		        	data.add(hm);
		        }
		        if(st!=null)
		            st.close();
		        if(rs!=null)
		            rs.close();
		        return data;
        }
}

