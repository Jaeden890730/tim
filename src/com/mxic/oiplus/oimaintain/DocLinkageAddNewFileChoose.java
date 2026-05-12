package com.mxic.oiplus.oimaintain;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.mxic.oiplus.oisearch.TFRouteMasterBean;
import com.mxic.oiplus.oisearch.oiSearchService;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.util.TDSLogger;

public class DocLinkageAddNewFileChoose {
	
	private String route_name;

	public String getRoute_name() {
		return route_name;
	}

	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}
	
	public HashMap[] getDataList() {
		if (route_name == null)
			return null;
		HashMap[] result = getDataList(route_name);
		return result;
	}
	
	public static HashMap[] getDataList(String route_name) {
		Connection con = null;
		ArrayList tmp = new ArrayList();;
		try {
			con = DBConnection.getConnection();
			
			TFRouteMasterBean tfRMBean = oiSearchService.getRouteMasterBean(route_name);
			if(tfRMBean != null){
				String fileName = tfRMBean.getFile_name_testflow();
				if(fileName != null && !fileName.equals("")){
					String[] fileList = fileName.split(";");
					for(int i=0; i<fileList.length; i++){
						HashMap hm = new HashMap();
						hm.put("KEY", fileList[i]);
						hm.put("VALUE", fileList[i]);
						tmp.add(hm);
					}
				}
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(con);
		}
		return (HashMap[]) tmp.toArray(new HashMap[0]);
	}
}
