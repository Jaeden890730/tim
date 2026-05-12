package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class e8049AjaxServlet extends HttpServlet {

    public static String convertXMLString(String xml) {
    	return xml.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
    }
    
	
	protected void get_sort_route_code_normal(HttpServletRequest request,
			 PrintWriter out) {
		System.out.println("test");
        String productbody = request.getParameter("productbody");
        String brand = request.getParameter("brand");

        if (productbody.trim().equals(""))
        	productbody = "NO PRODUCT BODY";

        BomProductRouteBean[] ptr = OiMaintainService.GetSortRouteCode_Normal(productbody, brand);
        BomProductRouteBean[] ptrTX = OiMaintainService.GetSortRouteCodeTX_Normal(productbody, brand );

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
	
	
	

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        String ajax_type = request.getParameter("type");

        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");

        if (ajax_type.equals("get_sort_route_code_normal"))
        	get_sort_route_code_normal(request, out);

	}
}
