package com.mxic.oiplus.xtrarom.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class TFIMBasicActionForm extends ActionForm {

  private String record_id;
  private String listControl;
  private int sid;
  private String pd_body;
  private String brand;
  private String version;
  private String tag;
  private String tester;
  private String good_bin;
  private String fail_bin;
  private String remark;
  private String tester_be;
  private String good_bin_be;
  private String fail_bin_be;
  private String remark_be;
  private String auto_ship_yield;
  private String stop_test_yield;
  private String auto_scrap_yield;
  private String mrb_yield;
  private String sample_yield;
  private String main_route;
  private String map_route;
  private String main_route_be;
  private String map_route_be;
  private String type_flag = null;
  private int main_route_flag = 0;
  private int map_route_flag = 0;
  private int remark_flag = 0;


  public void setFail_bin(String fail_bin) {
    this.fail_bin = fail_bin;
  }

  public String getFail_bin() {
    return fail_bin;
  }

  public void setFail_bin_be(String fail_bin_be) {
    this.fail_bin_be = fail_bin_be;
  }

  public String getFail_bin_be() {
    return fail_bin_be;
  }

  public String getListControl() {
    return listControl;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setRecord_id(String record_id) {
    this.record_id = record_id;
  }

  public String getRecord_id() {
    return record_id;
  }

  public void setGood_bin(String good_bin) {
    this.good_bin = good_bin;
  }

  public String getGood_bin() {
    return good_bin;
  }

  public void setGood_bin_be(String good_bin_be) {
    this.good_bin_be = good_bin_be;
  }

  public String getGood_bin_be() {
    return good_bin_be;
  }

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setTester(String tester) {
    this.tester = tester;
  }

  public void setTester_be(String tester_be) {
    this.tester_be = tester_be;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public String getBrand() {
    return brand;
  }

  public String getPd_body() {
    return pd_body;
  }

  public String getVersion() {
    return version;
  }

  public String getTag() {
    return tag;
  }

  public String getTester() {
    return tester;
  }

  public String getTester_be() {
    return tester_be;
  }

  public int getSid() {
    return sid;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark_be(String remark_be) {
    this.remark_be = remark_be;
  }

  public String getRemark_be() {
    return remark_be;
  }

  public void setMain_route(String main_route) {
    this.main_route = main_route;
  }

  public String getMain_route() {
    return main_route;
  }

  public void setMain_route_be(String main_route_be) {
    this.main_route_be = main_route_be;
  }

  public String getMain_route_be() {
    return main_route_be;
  }
  public void setMap_route(String map_route) {
    this.map_route = map_route;
  }

  public String getMap_route() {
    return map_route;
  }

  public void setMap_route_be(String map_route_be) {
    this.map_route_be = map_route_be;
  }

  public String getMap_route_be() {
    return map_route_be;
  }


  public String getChangecolor() {
    if (tag.equals("1")) {
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }

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

  /**
   * @return the auto_scrap_yield
   */
  public String getAuto_scrap_yield() {
	  return auto_scrap_yield;
  }

  /**
   * @param auto_scrap_yield the auto_scrap_yield to set
   */
  public void setAuto_scrap_yield(String auto_scrap_yield) {
	  this.auto_scrap_yield = auto_scrap_yield;
  }

  /**
   * @return the auto_ship_yield
   */
  public String getAuto_ship_yield() {
	  return auto_ship_yield;
  }

  /**
   * @param auto_ship_yield the auto_ship_yield to set
   */
  public void setAuto_ship_yield(String auto_ship_yield) {
	  this.auto_ship_yield = auto_ship_yield;
  }

  /**
   * @return the mrb_yield
   */
  public String getMrb_yield() {
	  return mrb_yield;
  }

  /**
   * @param mrb_yield the mrb_yield to set
   */
  public void setMrb_yield(String mrb_yield) {
	  this.mrb_yield = mrb_yield;
  }

  /**
   * @return the sample_yield
   */
  public String getSample_yield() {
	  return sample_yield;
  }

  /**
   * @param sample_yield the sample_yield to set
   */
  public void setSample_yield(String sample_yield) {
	  this.sample_yield = sample_yield;
  }

  /**
   * @return the stop_test_yield
   */
  public String getStop_test_yield() {
	  return stop_test_yield;
  }

  /**
   * @param stop_test_yield the stop_test_yield to set
   */
  public void setStop_test_yield(String stop_test_yield) {
	  this.stop_test_yield = stop_test_yield;
  }

/**
 * @return the type_flag
 */
public String getType_flag() {
	return type_flag;
}

/**
 * @param typeFlag the type_flag to set
 */
public void setType_flag(String typeFlag) {
	type_flag = typeFlag;
}

/**
 * @return the main_route_flag
 */
public int getMain_route_flag() {
	return main_route_flag;
}

/**
 * @param mainRouteFlag the main_route_flag to set
 */
public void setMain_route_flag(int mainRouteFlag) {
	main_route_flag = mainRouteFlag;
}

/**
 * @return the map_route_flag
 */
public int getMap_route_flag() {
	return map_route_flag;
}

/**
 * @param mapRouteFlag the map_route_flag to set
 */
public void setMap_route_flag(int mapRouteFlag) {
	map_route_flag = mapRouteFlag;
}

/**
 * @return the remark_flag
 */
public int getRemark_flag() {
	return remark_flag;
}

/**
 * @param remarkFlag the remark_flag to set
 */
public void setRemark_flag(int remarkFlag) {
	remark_flag = remarkFlag;
}
}
