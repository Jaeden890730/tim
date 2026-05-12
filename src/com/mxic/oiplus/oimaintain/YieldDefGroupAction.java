package com.mxic.oiplus.oimaintain;

import java.net.URLDecoder;
import java.sql.Connection;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.dao.TfYieldGroupItemsTxDao;
import com.mxic.oiplus.dao.YieldGroupItemsBean;
import com.mxic.oiplus.service.YieldGroupItemsGUIService;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.tdsplus.resource.DBConnection;

public class YieldDefGroupAction extends Action {
	
	public ActionForward execute(ActionMapping actionMapping,
			                     ActionForm actionForm,
			                     HttpServletRequest request,
			                     HttpServletResponse servletResponse) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			String jsonFormat = request.getParameter("groupItemsCrit");
			//TDSLogger.println("get GroupItemsCrit list start: " + request.getCharacterEncoding() + ", jsonFormat: " + jsonFormat);
			String jsonStr = URLDecoder.decode(jsonFormat, "utf-8");
			TDSLogger.println("jsonStr: " + jsonStr);			
			
			YieldGroupItemsBean groupItemsBean = YieldGroupItemsGUIService.jasonToObject(jsonStr);			
			
			TfYieldGroupItemsTxDao.delete(conn, groupItemsBean.getHold_downgrade(), groupItemsBean.getProduct_code(),
					groupItemsBean.getTest_mode(), groupItemsBean.getWafer_level(), groupItemsBean.getVersion());
			TfYieldGroupItemsTxDao.insert(conn, groupItemsBean);
			TDSLogger.println("get GroupItemsCrit list end"); 
		//TfYieldGroupItemsTxDao.
		}catch(Exception e){
			TDSLogger.println(e);
		}finally{
			DBConnection.close(conn);
		}
		return null;
	}
}
	