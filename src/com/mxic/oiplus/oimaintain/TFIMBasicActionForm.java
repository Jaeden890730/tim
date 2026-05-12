package com.mxic.oiplus.oimaintain;

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
  private String ib_bin;
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
  private String options;
  private String grade;
  private String bin_type;
  private String inkless_grade;
  private String ktd_bin_flag;
  private String ipn_action;
  private String bin_type_str;
  private String ipn_action_str;
  private String epn_speed;
  private String test_speed;
  private String down_grade;
  private String type_flag;
  private int tester_flag = 0;
  private int options_flag = 0;
  private int grade_flag = 0;
  private int good_bin_flag = 0;
  private int ib_bin_flag = 0;
  private int bin_type_str_flag = 0;
  private int inkless_grade_flag = 0;
  private int ktd_bin_flag_flag = 0;
  private int ipn_action_str_flag = 0;
  private int epn_speed_flag = 0;
  private int test_speed_flag = 0;
  private int down_grade_flag = 0;
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

  public void setBin_type_str(String bin_type_str) {
    this.bin_type_str = bin_type_str;
  }

  public String getBin_type_str() {
    return bin_type_str;
  }

  public void setIpn_action_str(String ipn_action_str) {
    this.ipn_action_str = ipn_action_str;
  }

  public String getIpn_action_str() {
    return ipn_action_str;
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

/**
 * @return the tester_flag
 */
public int getTester_flag() {
	return tester_flag;
}

/**
 * @param testerFlag the tester_flag to set
 */
public void setTester_flag(int testerFlag) {
	tester_flag = testerFlag;
}

/**
 * @return the options_flag
 */
public int getOptions_flag() {
	return options_flag;
}

/**
 * @param optionsFlag the options_flag to set
 */
public void setOptions_flag(int optionsFlag) {
	options_flag = optionsFlag;
}

/**
 * @return the grade_flag
 */
public int getGrade_flag() {
	return grade_flag;
}

/**
 * @param gradeFlag the grade_flag to set
 */
public void setGrade_flag(int gradeFlag) {
	grade_flag = gradeFlag;
}

/**
 * @return the good_bin_flag
 */
public int getGood_bin_flag() {
	return good_bin_flag;
}

/**
 * @param goodBinFlag the good_bin_flag to set
 */
public void setGood_bin_flag(int goodBinFlag) {
	good_bin_flag = goodBinFlag;
}

/**
 * @return the bin_type_str_flag
 */
public int getBin_type_str_flag() {
	return bin_type_str_flag;
}

/**
 * @param binTypeStrFlag the bin_type_str_flag to set
 */
public void setBin_type_str_flag(int binTypeStrFlag) {
	bin_type_str_flag = binTypeStrFlag;
}

/**
 * @return the inkless_grade_flag
 */
public int getInkless_grade_flag() {
	return inkless_grade_flag;
}

/**
 * @param inklessGradeFlag the inkless_grade_flag to set
 */
public void setInkless_grade_flag(int inklessGradeFlag) {
	inkless_grade_flag = inklessGradeFlag;
}

/**
 * @return the ipn_action_str_flag
 */
public int getIpn_action_str_flag() {
	return ipn_action_str_flag;
}

/**
 * @param ipnActionStrFlag the ipn_action_str_flag to set
 */
public void setIpn_action_str_flag(int ipnActionStrFlag) {
	ipn_action_str_flag = ipnActionStrFlag;
}

/**
 * @return the epn_speed_flag
 */
public int getEpn_speed_flag() {
	return epn_speed_flag;
}

/**
 * @param epnSpeedFlag the epn_speed_flag to set
 */
public void setEpn_speed_flag(int epnSpeedFlag) {
	epn_speed_flag = epnSpeedFlag;
}

/**
 * @return the test_speed_flag
 */
public int getTest_speed_flag() {
	return test_speed_flag;
}

/**
 * @param testSpeedFlag the test_speed_flag to set
 */
public void setTest_speed_flag(int testSpeedFlag) {
	test_speed_flag = testSpeedFlag;
}

/**
 * @return the down_grade_flag
 */
public int getDown_grade_flag() {
	return down_grade_flag;
}

/**
 * @param downGradeFlag the down_grade_flag to set
 */
public void setDown_grade_flag(int downGradeFlag) {
	down_grade_flag = downGradeFlag;
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
public String getIb_bin() {
    return ib_bin;
}

public void setIb_bin(String ib_bin) {
    this.ib_bin = ib_bin;
}

public int getIb_bin_flag() {
    return ib_bin_flag;
}

public void setIb_bin_flag(int ib_bin_flag) {
    this.ib_bin_flag = ib_bin_flag;
}

public String getKtd_bin_flag() {
	return ktd_bin_flag;
}

public void setKtd_bin_flag(String ktd_bin_flag) {
	this.ktd_bin_flag = ktd_bin_flag;
}

public int getKtd_bin_flag_flag() {
	return ktd_bin_flag_flag;
}

public void setKtd_bin_flag_flag(int ktd_bin_flag_flag) {
	this.ktd_bin_flag_flag = ktd_bin_flag_flag;
}

}
