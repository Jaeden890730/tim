package com.mxic.oiplus.xtrarom.oimaintain;

import java.util.Vector;

public class WsTestBean {
//  BorderLayout borderLayout1 = new BorderLayout();
  private String sid;
  private String tag;
  private String pgm_id;
  private String product_body;
  private String brand;
  private String version;
  private String mask_option;
  private String test_type;
  private String tester;
  private String site;
  private String program_name;
  private String tf_comment;
  private String id;
  private String temperature;
  private Vector temperatureList;
  private String site_short_name;
  private String hw_configure;
  private String pgm_special_control;
  private String one_main_pgm_group_version;
  private String one_main_pgm_num;
public String getHw_configure() {
	return hw_configure;
}

public void setHw_configure(String hw_configure) {
	this.hw_configure = hw_configure;
}

/*
  public WsTestBean() {
    try {
      jbInit();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
*/
/*
  private void jbInit() throws Exception {
    setLayout(borderLayout1);
  }
*/
  public void setSid(String sid) {
    this.sid = sid;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setPgm_id(String pgm_id) {
    this.pgm_id = pgm_id;
  }

  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setMask_option(String mask_option) {
    this.mask_option = mask_option;
  }

  public void setTest_type(String test_type) {
    this.test_type = test_type;
  }

  public void setTester(String tester) {
    this.tester = tester;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public void setProgram_name(String program_name) {
    this.program_name = program_name;
  }

  public void setTf_comment(String tf_comment) {
    this.tf_comment = tf_comment;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSid() {
    return sid;
  }

  public String getTag() {
    return tag;
  }

  public String getPgm_id() {
    return pgm_id;
  }

  public String getProduct_body() {
    return product_body;
  }

  public String getBrand() {
    return brand;
  }

  public String getVersion() {
    return version;
  }

  public String getMask_option() {
    return mask_option;
  }

  public String getTest_type() {
    return test_type;
  }

  public String getTester() {
    return tester;
  }

  public String getSite() {
    return site;
  }

  public String getProgram_name() {
    return program_name;
  }

  public String getTf_comment() {
    return tf_comment;
  }

  public String getId() {
    return id;
  }

  public String getChangecolor(){

    if (tag.equals("1")){
      return "#FFDDFF";
    }else{
      return "#CCEEFF";
    }
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  /**
   * @return the temperatureList
   */
  public Vector getTemperatureList() {
	  return temperatureList;
  }

  /**
   * @param temperatureList the temperatureList to set
   */
  public void setTemperatureList(Vector temperatureList) {
	  this.temperatureList = temperatureList;
  }

  public String getProductType() {
	  return OiMaintainService.getProductType(sid);
  }

/**
 * @return the site_short_name
 */
public String getSite_short_name() {
	return site_short_name;
}

/**
 * @param site_short_name the site_short_name to set
 */
public void setSite_short_name(String site_short_name) {
	this.site_short_name = site_short_name;
}
public String getPgm_special_control() {
	return pgm_special_control;
}

public void setPgm_special_control(String pgm_special_control) {
	this.pgm_special_control = pgm_special_control;
}
public String getOne_main_pgm_group_version() {
	return one_main_pgm_group_version;
}

public void setOne_main_pgm_group_version(String one_main_pgm_group_version) {
	this.one_main_pgm_group_version = one_main_pgm_group_version;
}

public String getOne_main_pgm_num() {
	return one_main_pgm_num;
}

public void setOne_main_pgm_num(String one_main_pgm_num) {
	this.one_main_pgm_num = one_main_pgm_num;
}
}
