package com.mxic.oiplus.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YieldGroupItemsBean{
	private String action_type;		//
	private String sid;
	private String proc_type;		//WS or FT
	private String hold_downgrade;	//hold or downgrade
	private String product_type;	//NVM
	private String product_code;
	private String test_mode;
	private String wafer_level;		//All or KGD or AEB
	private String version;
	private String[] group_name;
	private String[] group_no;
	private String[] item_no;
	private String[] item_name;
	private String[] item_type;
	private String[] range_value;
	
	public String getAction_type() {
		return action_type;
	}
	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getProc_type() {
		return proc_type;
	}
	public void setProc_type(String proc_type) {
		this.proc_type = proc_type;
	}
	public String getHold_downgrade() {
		return hold_downgrade;
	}
	public void setHold_downgrade(String hold_downgrade) {
		this.hold_downgrade = hold_downgrade;
	}
	public String getProduct_type() {
		return product_type;
	}
	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getTest_mode() {
		return test_mode;
	}
	public void setTest_mode(String test_mode) {
		this.test_mode = test_mode;
	}
	public String getWafer_level() {
		return wafer_level;
	}
	public void setWafer_level(String wafer_level) {
		this.wafer_level = wafer_level;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String[] getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String[] group_name) {
		this.group_name = group_name;
	}
	public String[] getGroup_no() {
		return group_no;
	}
	public void setGroup_no(String[] group_no) {
		this.group_no = group_no;
	}
	public String[] getItem_no() {
		return item_no;
	}
	public void setItem_no(String[] item_no) {
		this.item_no = item_no;
	}
	public String[] getItem_name() {
		return item_name;
	}
	public void setItem_name(String[] item_name) {
		this.item_name = item_name;
	}
	public String[] getItem_type() {
		return item_type;
	}
	public void setItem_type(String[] item_type) {
		this.item_type = item_type;
	}
	public String[] getRange_value() {
		return range_value;
	}
	public void setRange_value(String[] range_value) {
		this.range_value = range_value;
	}	
}