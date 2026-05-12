package com.mxic.oiplus.oimaintain;

import java.io.IOException;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.util.TDSLogger;

public class InsertBomDupAction extends Action {
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest Request, HttpServletResponse servletResponse) {

    ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
    String sid=Request.getParameter("sid");
    String type=(String)Request.getSession().getAttribute("type");
    BomProductRouteBean[] bom=(BomProductRouteBean[])Request.getSession().getAttribute("bom");
    fm.setType(type);
    fm.setBom(bom);
    TDSLogger.println("InsertBomDupAction() - type: " + fm.getType());
    BomProductRouteBean[] bm = fm.getBom();
    //BomProductRouteBean[] bm = (BomProductRouteBean[]) Request.getParameter("DupRow");
    
    OiMaintainStep ois = null;
    ois = OiMaintainService.SearchFunction(sid);
    if(ois != null){
    	fm.setSid(ois.getSid());
    	fm.setBrand(ois.getBrand());
    	fm.setProductbody(ois.getProduct_body());
    	fm.setVersion(ois.getVersion());
    	fm.setCreator(ois.getCreator());
    }
        
        int s = 0;

        String[] brand = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] productbody = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] beoption = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] fgwithcode = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] pincount = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] pkgtype = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] ftwithcode = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] maskopt = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] dbwithcode = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] sortroutecode = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] wsroute = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] wsaddroute = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] comment = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] ws_comment = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] ft_route_code = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] ftAddroute = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] ftAddroute2 = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] ftAddroute3 = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] sales_form = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] endurance = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] wsspecialcontrol = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] quality_level = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] quality_level_comment = (bm != null && bm.length > 0) ? new String[bm.length] : null;
        String[] mcp_flag = (bm != null && bm.length > 0) ? new String[bm.length] : null;

        if (bm != null && bm.length > 0) {
            for (int i = 0; i < bm.length; i++) {
                s = bm.length;
            	if(fm.getType()==null || fm.getType().equals("")){
	                brand[i] = bm[i].getBrand();
	                productbody[i] = bm[i].getProductbody();         
	                beoption[i] = bm[i].getBeoption();              
	                fgwithcode[i] = bm[i].getFgwithcode();            
	                pincount[i] = bm[i].getPincount();              
	                pkgtype[i] = bm[i].getPkgtype();               
	                ftwithcode[i] = bm[i].getFgwithcode();            
	                maskopt[i] = bm[i].getMaskopt();               
	                dbwithcode[i] = bm[i].getDbwithcode();            
	                sortroutecode[i] = bm[i].getSortroutecode();         
	                wsroute[i] = bm[i].getWsroute();               
	                wsaddroute[i] = bm[i].getWsaddroute();            
	                comment[i] = bm[i].getComment();               
	                ws_comment[i] = bm[i].getTf_ws_comment();          
	                ft_route_code[i] = bm[i].getFt_route_code();         
	                ftAddroute[i] = bm[i].getFtAddroute();            
	                ftAddroute2[i] = bm[i].getFtAddroute2();           
	                ftAddroute3[i] = bm[i].getFtAddroute3();           
	                sales_form[i] = bm[i].getSales_form();            
	                endurance[i] = bm[i].getEndurance();             
	                wsspecialcontrol[i] = bm[i].getWsspecialcontrol();      
	                quality_level[i] = bm[i].getQuality_level();         
	                quality_level_comment[i] = bm[i].getQuality_level_comment();
	                mcp_flag[i] = "";
            	}else if(fm.getType().equals("MCP")){
	                brand[i] = bm[i].getBrand();
	                productbody[i] = bm[i].getProductbody();         
	                beoption[i] = " ";              
	                fgwithcode[i] = " ";            
	                pincount[i] = "0";              
	                pkgtype[i] = " ";               
	                //ftwithcode[i] = bm[i].getFgwithcode();            
	                //maskopt[i] = bm[i].getMaskopt();           
	                //dbwithcode[i] = bm[i].getDbwithcode();            
	                sortroutecode[i] = bm[i].getSortroutecode();         
	                //wsroute[i] = bm[i].getWsroute();               
	                //wsaddroute[i] = bm[i].getWsaddroute();     
	                ftwithcode[i] = "";
	                maskopt[i] = bm[i].getMaskopt();     
	                dbwithcode[i] = bm[i].getDbwithcode(); 
	                wsroute[i] = "";
	                wsaddroute[i] = "";
	                comment[i] = "";               
	                ws_comment[i] = bm[i].getTf_ws_comment();          
	                ft_route_code[i] = " ";         
	                ftAddroute[i] = "";            
	                ftAddroute2[i] = "";           
	                ftAddroute3[i] = "";           
	                sales_form[i] = bm[i].getSales_form();            
	                endurance[i] = "NA";             
	                //wsspecialcontrol[i] = bm[i].getWsspecialcontrol();      
	                //quality_level[i] = bm[i].getQuality_level();         
	                //quality_level_comment[i] = bm[i].getQuality_level_comment();
	                mcp_flag[i] = "MCP";
            	}
            }
        } else {
            brand = new String[] { Request.getParameter("brand") };
            productbody = new String[] { Request.getParameter("productbody") };
            beoption = new String[] { Request.getParameter("beoption") };
            fgwithcode = new String[] { Request.getParameter("fgwithcode") };
            pincount = new String[] { Request.getParameter("pincount") };
            pkgtype = new String[] { Request.getParameter("pkgtype") };
            ftwithcode = new String[] { Request.getParameter("ftroute") };
            maskopt = new String[] { Request.getParameter("maskopt") };
            dbwithcode = new String[] { Request.getParameter("dbwithcode") };
            sortroutecode = new String[] { Request.getParameter("txtRouteCode") };
            wsroute = new String[] { Request.getParameter("wsroute") };
            wsaddroute = new String[] { Request.getParameter("wsaddroute") };
            comment = new String[] { Request.getParameter("txtComment") };
            ws_comment = new String[] { Request.getParameter("txtWsComment") };
            ft_route_code = new String[] { Request.getParameter("ft_route_code") };
            ftAddroute = new String[] { Request.getParameter("ftAddroute") };
            ftAddroute2 = new String[] { Request.getParameter("ftAddroute2") };
            ftAddroute3 = new String[] { Request.getParameter("ftAddroute3") };
            sales_form = new String[] { Request.getParameter("sales_form") };
            endurance = new String[] { Request.getParameter("endurance") };
            wsspecialcontrol = new String[] { Request.getParameter("wsspecialcontrol") };
            quality_level = new String[] { Request.getParameter("quality_level") };
            quality_level_comment = new String[] { Request.getParameter("quality_level_comment") };
            mcp_flag = new String[] { Request.getParameter("mcp_flag") };
            s = 1;
        }

    String forward=null;

        for (int i = 0; i < s; i++) {

            boolean flag = OiMaintainService.CheckExistBomDup(fm.getSid(), beoption[i], fgwithcode[i], pincount[i], pkgtype[i], maskopt[i],
                    sortroutecode[i], dbwithcode[i], ft_route_code[i], ftAddroute[i], ftAddroute2[i], ftAddroute3[i], sales_form[i], endurance[i],
                    wsspecialcontrol[i]);

    try{
      if (!flag){
                    OiMaintainService.InsertBomDup(fm, brand[i], productbody[i], beoption[i], fgwithcode[i], pincount[i], pkgtype[i], ftwithcode[i],
                            maskopt[i], dbwithcode[i], sortroutecode[i], wsroute[i], wsaddroute[i], comment[i], ft_route_code[i], ftAddroute[i],
                            ftAddroute2[i], ftAddroute3[i], ws_comment[i], sales_form[i], endurance[i], wsspecialcontrol[i], quality_level[i],
                            quality_level_comment[i], mcp_flag[i]);
                    OiMaintainService.submit(fm.getSid(), "TF_BOM_ROUTE", "N");
        forward="InsertBomDup";
      } else {
        Request.setAttribute("closeWindow","false");
        Request.setAttribute("message","資料表中有重複的資料");
        forward="SameDataExist";
      }
    } catch (Exception e){
      e.printStackTrace();
    }

        }
        //HttpSession session = Request.getSession();
        //session.setAttribute("sid", fm.getSid());
        try {
        	if(forward.equals("InsertBomDup")){
        		servletResponse.sendRedirect(servletResponse.encodeRedirectURL(Request.getContextPath() + "/OImaintain/bomProductRouteAction.do?sid=" + sid));
        		return null;
        	}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    return actionMapping.findForward(forward);
  }
}
