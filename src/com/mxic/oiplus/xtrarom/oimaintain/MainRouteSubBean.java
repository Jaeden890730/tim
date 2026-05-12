package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class MainRouteSubBean extends ActionForm {
	private String sid;
	private String tag;
	private String product_body;
    private String version;
	private String route_type;
	private String main_route;
	private String map_route;
	private String remark;

	public ActionErrors validate(ActionMapping actionMapping,
			HttpServletRequest httpServletRequest) {
		/** @todo: finish this method, this is just the skeleton.*/
		return null;
	}

	public void reset(ActionMapping actionMapping,
			HttpServletRequest servletRequest) {
		try {
			//轉成中文big5編碼
			servletRequest.setCharacterEncoding("big5");
		} catch (UnsupportedEncodingException ex) {
		}
	}

	public String getRoute_type() {
		return route_type;
	}

	public void setRoute_type(int route_type) {
		this.route_type = String.valueOf(route_type);
	}

	public String getMain_route() {
		return main_route;
	}

	public void setMain_route(String main_route) {
		this.main_route = main_route;
	}

	public String getMap_route() {
		return map_route;
	}

	public void setMap_route(String map_route) {
		this.map_route = map_route;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getProduct_body() {
		return product_body;
	}

	public void setProduct_body(String product_body) {
		this.product_body = product_body;
	}


       public String getVersion() {
               return version;
       }

       public void setVersion(String version) {
               this.version = version;
        }


	public String getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = String.valueOf(sid);
	}


	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

        public String getChangecolor(){

                if (tag.equals("1")){
                        return "#FFDDFF";
                }else{
                        return "#CCEEFF";
                }
	}


}
