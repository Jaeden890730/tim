package com.mxic.oiplus.oimaintain;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.service.YieldGroupItemsGUIService;

public class e8049AjaxServlet extends HttpServlet {

	public static String encoding = System.getProperty("file.encoding");
	static {
		if ("MS950".equals(encoding))  // for windows system
			encoding = "Big5";
	}
	
    public static String convertXMLString(String xml) {
    	return xml.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
    }
    
	protected void get_yield_bin_mode2(HttpServletRequest request,
                                       PrintWriter out) {
		String product_code = request.getParameter("product_code");
		String test_mode = request.getParameter("test_mode");
		String proc_type = request.getParameter("proc_type");
		int item_type = YieldDefService.getItem_type(request.getParameter("bin_cat"));

		YieldBin[] list = 
			YieldDefService.getBinList(proc_type,product_code,test_mode,
										"0",false,"1","2",item_type);
		out.println("<binlist>");
		for (int i = 0; i < list.length; i++)
		{
			if(item_type==15){
				out.println("<bin>" + list[i].getBin_no() + "-" + list[i].getSnova_id() + "-" + list[i].getVersion() + " / " + convertXMLString(list[i].getShort_name()) + "</bin>");
			}else{
				out.println("<bin>" + list[i].getBin_no() + " / " + convertXMLString(list[i].getShort_name()) + "</bin>");
			}	
		}
		out.println("</binlist>");
		out.close();
	}

	protected void get_yield_product_mode(HttpServletRequest request,
            							  PrintWriter out) {
		String fromProduct = request.getParameter("fromProduct");
		String currentProduct = request.getParameter("currentProduct");
		String proc_type = request.getParameter("proc_type");

		String[] list = 
			YieldDefService.getTestModeListFromYield(fromProduct,currentProduct,proc_type);
		out.println("<modelist>");
		for (int i = 0; i < list.length; i++)
		{
			out.println("<mode>" + list[i] + "</mode>");
		}
		out.println("</modelist>");
		out.close();
	}

	protected void get_step_list(HttpServletRequest request,
            					 PrintWriter out) {
		String route_name = request.getParameter("route_name");
		String idx = request.getParameter("idx");
		TDSLogger.println("get_step_list() route_name: " + route_name);
		if (route_name.trim().equals(""))
			route_name = "NO SUCH ROUTE";
		ProTestRouteBean[] ptr = OiMaintainService.GetRouteInfo(route_name);
		out.println("<result>");
		out.println("<idxlist>");
		out.println("<idx>"+idx+"</idx>");
		out.println("</idxlist>");

		out.println("<steplist>");
		//Action 為 Change IPN* 時, 才能選取 WSBANK or WBBANK
		//if(route_name.equals("WSBANK") || route_name.equals("WBBANK")){
		if(route_name.equals("WSBANK")){
			out.println("<step>NA</step>");			
		}else{
			if (ptr.length > 0)
				out.println("<step></step>");
			for (int i = 0; i < ptr.length; i++) {
				out.println("<step>" + ptr[i].getStepname() + "</step>");
			}
			if (ptr.length > 0)
				out.println("<step>Ship</step>");
		}
		out.println("</steplist>");
		out.println("</result>");
		out.close();
	}

	protected void get_step_listX(HttpServletRequest request, PrintWriter out) {
		String route_name = request.getParameter("route_name");
		String idx = request.getParameter("idx");
		String proc_type = request.getParameter("proc_type");
		String sid = request.getParameter("sid");
		if (route_name.trim().equals(""))
			route_name = "NO SUCH ROUTE";
		String[][] ptr = YieldDefService.getRouteSetpList(sid, proc_type);

		out.println("<result>");
		out.println("<idxlist>");
		out.println("<idx>" + idx + "</idx>");
		out.println("</idxlist>");
		out.println("<steplist>");
		if (ptr.length > 0)
			out.println("<step></step>");
		for (int i = 0; i < ptr.length; i++) {
			if (ptr[i].equals(route_name)) {
				for (int j = 0; j < ptr[i].length; j++) {
					out.println("<step>" + ptr[i][j] + "</step>");
				}
			}
		}
		if (ptr.length > 0)
			out.println("<step>Ship</step>");
		out.println("</steplist>");
		out.println("</result>");
		out.close();
	}

/*    protected void get_by_lot_dg_list(HttpServletRequest request, PrintWriter out) {
        String sid = request.getParameter("sid");
        String route_name = request.getParameter("route_name");
        String start_step_name = request.getParameter("start_step_name");
        String idx = request.getParameter("idx");

        if (route_name.trim().equals(""))
            route_name = "NO SUCH ROUTE";
        if (start_step_name.trim().equals(""))
            start_step_name = "NO SUCH STEP";
        
        countstepbean[] ptr = OiMaintainService.GetRouteSamplingTest(sid);
        
        out.println("<result>");
        out.println("<idxlist>");
        out.println("<idx>" + idx + "</idx>");
        out.println("</idxlist>");

        out.println("<steplist>");
        
        if (ptr.length > 0)
            out.println("<step></step>");
        for (int i = 0; i < ptr.length; i++) {
            out.println("<step>" + ((ptr[i].getSampling_test()==null)?" ":ptr[i].getSampling_test()) + "</step>");
        }
        if (ptr.length > 0)
            out.println("<step></step>");

        out.println("</steplist>");
        out.println("</result>");
        out.close();
    }*/
    protected void get_dgrade_special_ipn(HttpServletRequest request, PrintWriter out) {
        String sid = request.getParameter("sid");
        String test_mode = request.getParameter("test_mode");
        String product_code = request.getParameter("product_code");

        if (test_mode.trim().equals(""))
            test_mode = "NO SUCH MODE";
        if (product_code.trim().equals(""))
            product_code = "NO SUCH CODE";
        
        
        ProTestRouteBean[] ptrb  = OiMaintainService.GetTestStepDef(sid);
        String step_name = "";
        String step_name1 = ""; /*修正by_lot_grade下拉選單，不這樣改會讓下面step_name找不到s開頭*/
        String step_def = "";
        String by_lot_dg = "";
        for (int i = 0 ; i < ptrb.length; i++) {
            step_name = ptrb[i].getStepname();
            if(ptrb[i].getStepname().startsWith("SORT"))
                step_name = ptrb[i].getStepname().replaceAll("SORT", "S");
            
            if(test_mode.startsWith(step_name)) {
                step_def =  ptrb[i].getStep_def();
            }
        }

        countstepbean[] ptr = OiMaintainService.GetRouteSamplingTest(sid);
        
        for (int i = 0 ; i < ptr.length; i++) {
            step_name1 = ptr[i].getRoutename2();
            if(ptr[i].getRoutename2().startsWith("SORT"))
                step_name1 = ptr[i].getRoutename2().replaceAll("SORT", "S");
            
            if(test_mode.startsWith(step_name1)) {
                by_lot_dg =  ptr[i].getSampling_test();
                break;
            }
        }
        
        
        
        out.println("<result>");
        out.println("<steplist>");
        if("KGD (Bottom)".equals(step_def)) {
        //if(step_def.equals("KGD (Bottom)")) {
            out.println("<step>Assign Dgrade IPN (Bottom IPN)</step>");
        } else if("KGD (Top)".equals(step_def)) {
        //} else if(step_def.equals("KGD (Top)")) {
            out.println("<step>Assign Dgrade IPN (Top IPN)</step>");
        } else if("KGD only".equals(step_def)) {
        //} else if(step_def.equals("KGD only")) {
            out.println("<step>Assign Dgrade IPN (Top IPN),Assign Dgrade IPN (Bottom IPN)</step>");
        } else {
            if(step_name.startsWith("S")) {
                out.println("<step>Follow PC assign Dgrade IPN,Dgrade different Prod Code,Wait Dgrade IPN</step>"); 
            } else 
                out.println("<step></step>");
        }
        out.println("<step1>"+by_lot_dg+"</step1>");
       
        out.println("</steplist>");
        out.println("</result>");
        out.close();
    }
   
	protected void get_sort_route_code(HttpServletRequest request,
			 PrintWriter out) {
		System.out.println("test");
        String productbody = request.getParameter("productbody");
        String brand = request.getParameter("brand");

        if (productbody.trim().equals(""))
        	productbody = "NO PRODUCT BODY";
        if (brand.trim().equals(""))
        	brand = "NO BRAND";

        BomProductRouteBean[] ptr = OiMaintainService.GetSortRouteCode(productbody, brand);
        BomProductRouteBean[] ptrTX = OiMaintainService.GetSortRouteCodeTX(productbody, brand );
        out.println("<sortroutenamelist>");
        for (int i = 0; i < ptr.length; i++)
        {
        	out.println("<maskoption>" + ptr[i].getMaskopt() + "</maskoption>");
            out.println("<sortroutecode>" + ptr[i].getSortroutecode() + "</sortroutecode>");
            out.println("<sortroutename>" + ptr[i].getWsroute() + "</sortroutename>");
        }
        for (int i = 0; i < ptrTX.length; i++)
        {
        	for (int j = 0; j < ptr.length; j++){
        		if((!ptrTX[i].getMaskopt().equals(ptr[j].getMaskopt())) 
        			&& (!ptrTX[i].getSortroutecode().equals(ptr[j].getSortroutecode()))){
        			out.println("<maskoption>" + ptrTX[i].getMaskopt() + "</maskoption>");
        			out.println("<sortroutecode>" + ptrTX[i].getSortroutecode() + "</sortroutecode>"); 
                    out.println("<sortroutename>" + ptrTX[i].getWsroute() + "</sortroutename>");
        		}
        	}
        	
        }
        out.println("</sortroutenamelist>");
        out.close();
    }
	protected void get_db_ib_bin_list(HttpServletRequest request,PrintWriter out, int type) {  /*type is db_bin(1) or ib_bin(2)*/
		String aj_productbody = request.getParameter("aj_productbody");  //pd_body
		String aj_testmode = request.getParameter("aj_testmode");  //tester
		String msg = "";
		String aj_dbbin = "";
		
		if(type == 2) {
			aj_dbbin = request.getParameter("aj_dbbin");  //select ib_bin where db_din = ? and type = 2
		} else {
			aj_dbbin = null;
		}
		
		
		if(aj_productbody == null || aj_productbody.trim().equals("") || aj_testmode ==null || aj_testmode.trim().equals("")) {
			msg = "Pls input productbody and testmode to get bin list!\n";
		}
		
		String[][] rnt = OiMaintainService.getDBBinList(aj_productbody, aj_testmode, aj_dbbin);
		out.println("<binlist>");

		if(rnt != null && rnt[0] != null) {
			for (int i =0 ; i < rnt[0].length; i++) {
				out.println("<dbbin>" + rnt[0][i] + "</dbbin>");
			}
		} else {
			out.println("<dbbin></dbbin>");
			msg = aj_productbody+"/"+aj_testmode+" no DBBin in PEIS Data\n";
		}
		
	   if(rnt != null && rnt[1] != null) {
			for (int i =0 ; i < rnt[1].length; i++) {
				out.println("<ibbin>" + rnt[1][i] + "</ibbin>");
			}
		} else {
			out.println("<ibbin></ibbin>");
			msg = msg + aj_productbody+"/"+aj_testmode+" no IBBin in PEIS Data\n";
		}
		
		
		if(!msg.equals(""))
			out.println("<msg>" + msg +"</msg>");
		else
			out.println("<msg></msg>");
		out.println("</binlist>");
	}
	
	protected void get_route_list(HttpServletRequest request, PrintWriter out) {
		
		String idx = request.getParameter("idx");
		String item_name = request.getParameter("item_name");
		String sid = request.getParameter("sid");
		String product_code = request.getParameter("product_code");
		String brand = request.getParameter("brand");
		TDSLogger.println("get_route_list() - sid:" + sid + "product_code: " + product_code + ", item_name: " + item_name);
		String item_brand = "MX";
		if (item_name.contains("KH") || item_name.contains("KTD"))
			item_brand = "KH";
		if(product_code == null && product_code.length() != 5)
			return;

		out.println("<result>");
		out.println("<idxlist>");
		out.println("<idx>" + idx + "</idx>");
		out.println("</idxlist>");
		out.println("<routelist>");

		BomProductRouteBean[] ptr = OiMaintainService.GetSortRouteCodeTX(sid, product_code.substring(0, 4), item_brand, product_code.substring(4, 5), brand);
		if (ptr.length > 0)
			out.println("<route></route>");
		for (int i = 0; i < ptr.length; i++) {
			out.println("<route>" + ptr[i].getWsroute() + "</route>");
		}
		if(item_name.indexOf("Change IPN") > 0){
			out.println("<route>WSBANK</route>");
			//out.println("<route>WBBANK</route>");
		}
		out.println("</routelist>");
		out.println("</result>");
		out.close();
	}
	protected void get_route_listX(HttpServletRequest request, PrintWriter out) {
		
		String idx = request.getParameter("idx");
		String sid = request.getParameter("sid");
		String proc_type = request.getParameter("proc_type");
		String product_code = request.getParameter("product_code");
		if(product_code == null && product_code.length() != 5)
			return;
		String[] ptr = YieldDefService.getRouteNameList(sid, proc_type);
		out.println("<result>");
		out.println("<idxlist>");
		out.println("<idx>" + idx + "</idx>");
		out.println("</idxlist>");
		out.println("<routelist>");
		if (ptr.length > 0)
			out.println("<route></route>");
		for (int i = 0; i < ptr.length; i++) {
			out.println("<route>" + ptr[i] + "</route>");
		}
		out.println("</routelist>");
		out.println("</result>");
		out.close();
	}
	protected void getGroupItemsCritList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=Big5");
		response.setHeader("Cache-Control", "no-cache");		
        
		String type = request.getParameter("hold_downgrade");
		String brand = request.getParameter("brand");
		String productCode = request.getParameter("product_code");
		String testMode = request.getParameter("test_mode");
		String prodLevel = request.getParameter("wafer_level");
		String version = request.getParameter("version");
				
		String groupItemsCrits = YieldGroupItemsGUIService.getGroupItemsCriteriaListToJason(type, brand, productCode, testMode, prodLevel, version);
		TDSLogger.println("getGroupItemsCritList() - groupItemsCrits: " + groupItemsCrits);	
		
		//groupItemsCrits = "{\"code\":\"200\",\"message\":\"Success\",\"list\":[{\"groupItems\":[{\"GROUP_NOS\":\";1\",\"TYPE_NAME\":\"Yield & BIN3\",\"ITEM_NOS\":\"1;2\",\"RANGES\":\"33;0.5\",\"ITEM_NAME\":\"Yield;BIN3\",\"ITEM_TYPES\":\"1;2\"}],\"itemCrits\":[{\"GROUPITEMS_NO\":\"1\",\"ROUTE_NAME\":\"WSBANK\",\"ITEM_TYPE\":\"30\",\"ITEM\":\"33 <= Yield <= 100 & 0.5 <= BIN3 <= 100\",\"BY_LOT_DG\":null,\"REMARK\":\"回貨 SFG\",\"PRODUCT_CODE\":\"6310N\",\"START_STEP\":\"NA\",\"SID\":\"796792\",\"PROD_LEVEL\":\"All\",\"DGRADEPRODCODE\":null,\"DGRADE_SPECIAL_IPN\":\"Follow PC assign Dgrade IPN\",\"ACTION\":\"39Change IPN3\",\"YID\":\"135214\",\"TEST_MODE\":\"S2\"}]},{\"groupItems\":[{\"GROUP_NOS\":\";2\",\"TYPE_NAME\":\"Yield & BIN3 & BIN5\",\"ITEM_NOS\":\"1;2;3\",\"RANGES\":\"33;1;2\",\"ITEM_NAME\":\"Yield;BIN3;BIN5\",\"ITEM_TYPES\":\"1;2;2\"}],\"itemCrits\":[{\"GROUPITEMS_NO\":\"2\",\"ROUTE_NAME\":\"FW41\",\"ITEM_TYPE\":\"30\",\"ITEM\":\"0 <= Yield < 33 & 1 <= BIN3 <= 100 & 2 <= BIN5 <= 100\",\"BY_LOT_DG\":null,\"REMARK\":null,\"PRODUCT_CODE\":\"6310N\",\"START_STEP\":\"BAKE1\",\"SID\":\"796792\",\"PROD_LEVEL\":\"All\",\"DGRADEPRODCODE\":null,\"DGRADE_SPECIAL_IPN\":\"Follow PC assign Dgrade IPN\",\"ACTION\":\"37Change IPN\",\"YID\":\"135216\",\"TEST_MODE\":\"S2\"}]},{\"groupItems\":[{\"GROUP_NOS\":\";3\",\"TYPE_NAME\":\"Yield & BIN3+BIN4\",\"ITEM_NOS\":\"1;2\",\"RANGES\":\"33;10\",\"ITEM_NAME\":\"Yield;BIN3+BIN4\",\"ITEM_TYPES\":\"1;2\"}],\"itemCrits\":[{\"GROUPITEMS_NO\":\"3\",\"ROUTE_NAME\":\"FW169\",\"ITEM_TYPE\":\"30\",\"ITEM\":\"33 <= Yield <= 100 & 10 <= BIN3+BIN4 <= 100\",\"BY_LOT_DG\":null,\"REMARK\":null,\"PRODUCT_CODE\":\"6310N\",\"START_STEP\":\"BAKE\",\"SID\":\"796792\",\"PROD_LEVEL\":\"All\",\"DGRADEPRODCODE\":null,\"DGRADE_SPECIAL_IPN\":\"Follow PC assign Dgrade IPN\",\"ACTION\":\"18Dgrade-KH\",\"YID\":\"135215\",\"TEST_MODE\":\"S2\"}]}]}";

		DataOutputStream dos = new DataOutputStream(response.getOutputStream());
		dos.write(groupItemsCrits.getBytes("Big5"));
		dos.close();
		
	}
	

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        String ajax_type = request.getParameter("type");

        TDSLogger.println("type: " + ajax_type + ", encoding: " + encoding);

        if(ajax_type.equals("get_groupitems_crit_list")){        	
        	getGroupItemsCritList(request, response);
    	}else{
            PrintWriter out = response.getWriter();
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
    		
	        if (ajax_type.equals("get_yield_bin_mode1"))
	        	get_yield_bin_mode2(request, out);
	        if (ajax_type.equals("get_yield_bin_mode2"))
	        	get_yield_bin_mode2(request, out);
	        else if (ajax_type.equals("get_avl_mode"))
	        	get_yield_product_mode(request, out);
	        else if (ajax_type.equals("get_step_list"))
	        	get_step_list(request, out);
	/*        else if (ajax_type.equals("get_by_lot_dg_list"))
	            get_by_lot_dg_list(request, out);*/
	        else if (ajax_type.equals("get_sort_route_code"))
	        	get_sort_route_code(request, out);
	        else if (ajax_type.equals("get_dgrade_special_ipn"))
	            get_dgrade_special_ipn(request, out);
			else if(ajax_type.equals("get_db_bin_list"))
	            get_db_ib_bin_list(request, out, 1);
	        else if(ajax_type.equals("get_ib_bin_list"))
	            get_db_ib_bin_list(request, out, 2);
			else if (ajax_type.equals("get_route_list"))
				get_route_list(request, out);
			else if (ajax_type.equals("get_route_listX"))
				get_route_listX(request, out);
	        else if (ajax_type.equals("get_step_listX"))
	        	get_step_listX(request, out);
        }
	}
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    	    throws ServletException, IOException {
    	doGet(request, response);
    }
}
