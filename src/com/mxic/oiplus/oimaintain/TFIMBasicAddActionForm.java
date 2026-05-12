package com.mxic.oiplus.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class TFIMBasicAddActionForm extends ActionForm {
  private String record_id;
  private String listControl;
  private int sid;
  private String tag;
  private String pd_body;
  private String brand;
  private String version;
  private String tester;
  private String options;
  private String grade;
  private String good_bin;
  private String ib_bin;
  private String bin_type;
  private String inkless_grade;
  private String ktd_bin_flag;
  private String ipn_action;
  private String epn_speed;
  private String test_speed;
  private String down_grade;
  private String fail_bin;
  private String remark;
  private String auto_ship_yield;
  private String stop_test_yield;
  private String auto_scrap_yield;
  private String mrb_yield;
  private String sample_yield;

  public void setFail_bin(String fail_bin) {
    this.fail_bin = fail_bin;
  }

  public String getFail_bin() {
    return fail_bin;
  }

  public String getListControl() {
    return listControl;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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

  public int getSid() {
    return sid;
  }

  public String getChangecolor() {
    if (tag.equals("1")) {
      return "#FF99FF";
    } else {
      return "#3366FF";
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

  public void setOptions(String options) {
    this.options = options;
  }

  public String getOptions() {
    return options;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public String getGrade() {
    return grade;
  }

  public void setBin_type(String bin_type) {
    this.bin_type = bin_type;
  }

  public String getBin_type() {
    return bin_type;
  }
  
  public void setInkless_grade(String inkless_grade) {
	this.inkless_grade = inkless_grade;
  }

  public String getInkless_grade() {
	return inkless_grade;
  }

  public void setIpn_action(String ipn_action) {
    this.ipn_action = ipn_action;
  }

  public String getIpn_action() {
    return ipn_action;
  }

  public void setEpn_speed(String epn_speed) {
    this.epn_speed = epn_speed;
  }

  public String getEpn_speed() {
    return epn_speed;
  }

  public void setTest_speed(String test_speed) {
    this.test_speed = test_speed;
  }

  public String getTest_speed() {
    return test_speed;
  }

  public void setDown_grade(String down_grade) {
    this.down_grade = down_grade;
  }

  public String getDown_grade() {
    return down_grade;
  }
  public String getIb_bin() {
    return ib_bin;
  }

  public void setIb_bin(String ib_bin) {
    this.ib_bin = ib_bin;
  }

public String getKtd_bin_flag() {
	return ktd_bin_flag;
}

public void setKtd_bin_flag(String ktd_bin_flag) {
	this.ktd_bin_flag = ktd_bin_flag;
}
}