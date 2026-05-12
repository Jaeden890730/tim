package com.mxic.oiplus.xtrarom.oimaintain;

import org.apache.struts.action.ActionForm;


public class addMainRouteSubForm extends ActionForm {

	private int sid;
	private String tag;
	private String product_body;
	private String route_type;
	private String version;
	private String[] main_route;
	private String[] map_route;
	private String[] remark;
	private String listControl;
	private String record_id;

	/**
	 * @return Returns the main_route.
	 */
	public String[] getMain_route() {
		return main_route;
	}
	/**
	 * @param actual_file The main_route to set.
	 */
	public void setMain_route(String[] main_route) {
		this.main_route = main_route;
	}
	/**
	 * @return Returns the map_route.
	 */
	public String[] getMap_route() {
		return map_route;
	}
	/**
	 * @param map_route The map_route to set.
	 */
	public void setMap_route(String[] map_route) {
		this.map_route = map_route;
	}
        /**
                 * @return Returns the remark.
                 */
                public String[] getRemark() {
                        return remark;
                }
                /**
                 * @param map_route The remark to set.
                 */
                public void setRemark(String[] remark) {
                        this.remark = remark;
                }

	/**
	 * @return Returns the route_type.
	 */
	public String getRoute_type() {
		return route_type;
	}
	/**
	 * @param route_type The route_type to set.
	 */
	public void setRoute_type(String route_type) {
		this.route_type = route_type;
	}

	/**
	 * @return Returns the product_body.
	 */
	public String getProduct_body() {
		return product_body;
	}
	/**
	 * @param product_body The product_body to set.
	 */
	public void setProduct_body(String product_body) {
		this.product_body = product_body;
	}

	/**
	 * @return Returns the sid.
	 */
	public int getSid() {
		return sid;
	}
	/**
	 * @param sid The sid to set.
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}

	/**
	 * @return Returns the tag.
	 */
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag The tag to set.
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return Returns the listControl.
	 */
	public String getListControl() {
		return listControl;
	}
	/**
	 * @param listControl The listControl to set.
	 */
	public void setListControl(String listControl) {
		this.listControl = listControl;
	}
	/**
	 * @return Returns the record_id.
	 */
	public String getRecord_id() {
		return record_id;
	}
	/**
	 * @param record_id The record_id to set.
	 */
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

}
