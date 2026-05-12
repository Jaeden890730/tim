package com.mxic.oiplus.oimaintain;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.util.TDSLogger;

public class EditiionCompareActionForm extends ActionForm {
  private String epnbody;
  private String brand;
  private String productbody;
  private String beoption;
  private String fgwithcode;
  private String pincount;
  private String pkgtype;
  private String ft_route_code;
  private String grade;
  private String productclass;
  private String ftroute;
  private String maskopt;
  private String dbwithcode;
  private String sortroutecode;
  private String wsroute;
  private String wsaddroute;
  private String comment;
  private String wscomment;
  private String ft_route_add;
  private String ft_route_add2;
  private String ft_route_add3;
  private String tag;
  private String id;
  private String sid;
  private String version;
  private String pd_body;
  private String status;
  private String route_name;
  private int step_seq;
  private String step_name;
  private int test_time;
  private String time_unit;
  private String rework_step;
  private int test_time2;
  private String time_unit2;
  private String temperature;
  private String remark;
  private String test_flow;
  private String tag_old;
  private String file_old;
  private String tag_new;
  private String file_new;
  private String path_old;
  private String path_new;
  private String sampling_test;
  private String sampling_cond;
  private String wafer_level;
  private String wafer_brand;
  private String biztype;
  private String wafer_grade;
  private String apply_type;
  private String ori_priority;
  private String revise_priority;
  private String checked_flag;
  private String rownum;
  private String type_flag;
  private String quality_level;
  private String quality_level_comment;
  private String mcp_flag;
  private String component_no;
  private String com_prod_body;
  private String com_mask_option;
  private String com_backend_option;
  private String ws_special_control;
  private String ft_special_control;
  private String avi;
  private String ink;

  private int wafer_level_flag;
  private int revise_priority_flag;
  private int route_name_flag;
  private int step_seq_flag;
  private int step_name_flag;
  private int test_time_flag;
  private int time_unit_flag;
  private int temperature_flag;
  private int sampling_test_flag;
  private int sampling_cond_flag;
  private int rework_step_flag;
  private int test_time2_flag;
  private int time_unit2_flag;
  private int remark_flag;
  private int backend_option_flag ;
  private int fg_with_code_flag;
  private int pin_count_flag;
  private int package_type_flag;
  private int ft_route_code_flag;
  private int ft_route_flag;
  private int ft_route_add_flag;
  private int ft_route_add_flag2;
  private int ft_route_add_flag3;
  private int tf_comment_flag;
  private int mcp_flag_flag;
  private int mask_option_flag;
  private int db_with_code_flag;
  private int sort_route_code_flag;
  private int ws_route_flag;
  private int ws_route_add_flag;
  private int tf_ws_comment_flag;
  private int quality_level_flag;
  private int quality_level_comment_flag;
  private int component_no_flag;
  private int com_prod_body_flag;
  private int com_mask_option_flag;
  private int com_backend_option_flag;
  private int ws_special_control_flag;
  private int ft_special_control_flag;
  private int avi_flag;
  private int ink_flag;

  

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public String getPd_body() {
    return pd_body;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setFile_new(String file_new) {
    this.file_new = file_new;
  }

  public String getFile_new() {
    return file_new;
  }

  public void setFile_old(String file_old) {
    this.file_old = file_old;
  }

  public String getFile_old() {
    return file_old;
  }

  public void setTag_old(String tag_old) {
    this.tag_old = tag_old;
  }

  public String getTag_old() {
    return tag_old;
  }

  public void setTag_new(String tag_new) {
    this.tag_new = tag_new;
  }

  public String getTag_new() {
    return tag_new;
  }

  public void setTest_flow(String test_flow) {
    this.test_flow = test_flow;
  }

  public String getTest_flow() {
    return test_flow;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRemark() {
    return remark;
  }

  public void setStep_name(String step_name) {
	  this.step_name = step_name;
  }

  public String getStep_name() {
	  return step_name;
  }

  public void setTime_unit(String time_unit) {
	  this.time_unit = time_unit;
  }

  public String getTime_unit() {
	  return time_unit;
  }

  public void setRework_step(String rework_step) {
	  this.rework_step = rework_step;
  }

  public String getRework_step() {
	  return rework_step;
  }

  public void setTime_unit2(String time_unit2) {
	  this.time_unit2 = time_unit2;
  }

  public String getTime_unit2() {
	  return time_unit2;
  }

  public void setTest_time2(int test_time2) {
	  this.test_time2 = test_time2;
  }

  public int getTest_time2() {
	  return test_time2;
  }

  public void setStep_seq(int step_seq) {
    this.step_seq = step_seq;
  }

  public int getStep_seq() {
    return step_seq;
  }

  public void setTest_time(int test_time) {
    this.test_time = test_time;
  }

  public int getTest_time() {
    return test_time;
  }

  public void setRoute_name(String route_name) {
    this.route_name = route_name;
  }

  public String getRoute_name() {
    return route_name;
  }

  public void setEpnbody(String epnbody) {
    this.epnbody = epnbody;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setProductbody(String productbody) {
    this.productbody = productbody;
  }

  public void setBeoption(String beoption) {
    this.beoption = beoption;
  }

  public void setFgwithcode(String fgwithcode) {
    this.fgwithcode = fgwithcode;
  }

  public void setPincount(String pincount) {
    this.pincount = pincount;
  }

  public void setPkgtype(String pkgtype) {
    this.pkgtype = pkgtype;
  }

  public void setFt_route_code(String ft_route_code) {
    this.ft_route_code = ft_route_code;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public void setProductclass(String productclass) {
    this.productclass = productclass;
  }

  public void setFtroute(String ftroute) {
    this.ftroute = ftroute;
  }

  public void setMaskopt(String maskopt) {
    this.maskopt = maskopt;
  }

  public void setDbwithcode(String dbwithcode) {
    this.dbwithcode = dbwithcode;
  }

  public void setSortroutecode(String sortroutecode) {
    this.sortroutecode = sortroutecode;
  }

  public void setWsroute(String wsroute) {
    this.wsroute = wsroute;
  }

  public void setWsaddroute(String wsaddroute) {
    this.wsaddroute = wsaddroute;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setFt_route_add(String ft_route_add) {
    this.ft_route_add = ft_route_add;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getEpnbody() {
    return epnbody;
  }

  public String getBrand() {
    return brand;
  }

  public String getProductbody() {
    return productbody;
  }

  public String getBeoption() {
    return beoption;
  }

  public String getFgwithcode() {
    return fgwithcode;
  }

  public String getPincount() {
    return pincount;
  }

  public String getPkgtype() {
    return pkgtype;
  }

  public String getFt_route_add() {
    return ft_route_add;
  }

  public String getGrade() {
    return grade;
  }

  public String getProductclass() {
    return productclass;
  }

  public String getFtroute() {
    return ftroute;
  }

  public String getMaskopt() {
    return maskopt;
  }

  public String getDbwithcode() {
    return dbwithcode;
  }

  public String getSortroutecode() {
    return sortroutecode;
  }

  public String getWsroute() {
    return wsroute;
  }

  public String getWsaddroute() {
    return wsaddroute;
  }

  public String getComment() {
    return comment;
  }

  public String getFt_route_code() {
    return ft_route_code;
  }

  public String getTag() {
    return tag;
  }

  public String getId() {
    return id;
  }

  public String getSid() {
    return sid;
  }

  public String getVersion() {
    return version;
  }

  public String getChangecolor() {
    if (tag.equals("1")) {
      return "#FF99FF";
    } else {
      return "#3366FF";
    }
  }

  public String getOld_show() {
    if (tag_old.equals("1")) {
      TDSLogger.println("tag_old");
      return "display:none";
    } else {
      return "";
    }
  }

  public String getOld_show_title() {
    if (tag_old.equals("1")) {
      return " ";
    } else {
      return " Old Chart";
    }
  }

  public String getNew_show() {
    if (tag_new.equals("1")) {
      return "display:none";
    } else {
      return "";
    }
  }

  public String getNew_show_title() {
    if (tag_new.equals("1")) {
      return "";
    } else {
      return "New Chart";
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
   * @return Returns the wscomment.
   */
  public String getWscomment() {
    return wscomment;
  }

  /**
   * @param wscomment The wscomment to set.
   */
  public void setWscomment(String wscomment) {
    this.wscomment = wscomment;
  }

/**
 * @return the path_new
 */
public String getPath_new() {
	return path_new;
}

/**
 * @param path_new the path_new to set
 */
public void setPath_new(String path_new) {
	this.path_new = path_new;
}

/**
 * @return the path_old
 */
public String getPath_old() {
	return path_old;
}

/**
 * @param path_old the path_old to set
 */
public void setPath_old(String path_old) {
	this.path_old = path_old;
}

public String getSampling_test() {
	return sampling_test;
}

public void setSampling_test(String sampling_test) {
	this.sampling_test = sampling_test;
}
public String getSampling_cond() {
	return sampling_cond;
}

public void setSampling_cond(String sampling_cond) {
	this.sampling_cond = sampling_cond;
}

public String getApply_type() {
	return apply_type;
}

public void setApply_type(String apply_type) {
	this.apply_type = apply_type;
}

public String getBiztype() {
	return biztype;
}

public void setBiztype(String biztype) {
	this.biztype = biztype;
}

public String getChecked_flag() {
	return checked_flag;
}

public void setChecked_flag(String checked_flag) {
	this.checked_flag = checked_flag;
}

public String getOri_priority() {
	return ori_priority;
}

public void setOri_priority(String ori_priority) {
	this.ori_priority = ori_priority;
}

public String getRevise_priority() {
	return revise_priority;
}

public void setRevise_priority(String revise_priority) {
	this.revise_priority = revise_priority;
}

public String getRownum() {
	return rownum;
}

public void setRownum(String rownum) {
	this.rownum = rownum;
}

public String getWafer_brand() {
	return wafer_brand;
}

public void setWafer_brand(String wafer_brand) {
	this.wafer_brand = wafer_brand;
}

public String getWafer_grade() {
	return wafer_grade;
}

public void setWafer_grade(String wafer_grade) {
	this.wafer_grade = wafer_grade;
}

public String getWafer_level() {
	return wafer_level;
}

public void setWafer_level(String wafer_level) {
	this.wafer_level = wafer_level;
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
 * @return the revise_priority_flag
 */
public int getRevise_priority_flag() {
	return revise_priority_flag;
}

/**
 * @param revisePriorityFlag the revise_priority_flag to set
 */
public void setRevise_priority_flag(int revisePriorityFlag) {
	revise_priority_flag = revisePriorityFlag;
}

/**
 * @return the wafer_level_flag
 */
public int getWafer_level_flag() {
	return wafer_level_flag;
}

/**
 * @param waferLevelFlag the wafer_level_flag to set
 */
public void setWafer_level_flag(int waferLevelFlag) {
	wafer_level_flag = waferLevelFlag;
}

/**
 * @return the route_name_flag
 */
public int getRoute_name_flag() {
	return route_name_flag;
}

/**
 * @param routeNameFlag the route_name_flag to set
 */
public void setRoute_name_flag(int routeNameFlag) {
	route_name_flag = routeNameFlag;
}

/**
 * @return the step_seq_flag
 */
public int getStep_seq_flag() {
	return step_seq_flag;
}

/**
 * @param stepSeqFlag the step_seq_flag to set
 */
public void setStep_seq_flag(int stepSeqFlag) {
	step_seq_flag = stepSeqFlag;
}

/**
 * @return the step_name_flag
 */
public int getStep_name_flag() {
	return step_name_flag;
}

/**
 * @param stepNameFlag the step_name_flag to set
 */
public void setStep_name_flag(int stepNameFlag) {
	step_name_flag = stepNameFlag;
}

/**
 * @return the test_time_flag
 */
public int getTest_time_flag() {
	return test_time_flag;
}

/**
 * @param testTimeFlag the test_time_flag to set
 */
public void setTest_time_flag(int testTimeFlag) {
	test_time_flag = testTimeFlag;
}

/**
 * @return the time_unit_flag
 */
public int getTime_unit_flag() {
	return time_unit_flag;
}

/**
 * @param timeUnitFlag the time_unit_flag to set
 */
public void setTime_unit_flag(int timeUnitFlag) {
	time_unit_flag = timeUnitFlag;
}

/**
 * @return the temperature_flag
 */
public int getTemperature_flag() {
	return temperature_flag;
}

/**
 * @param temperatureFlag the temperature_flag to set
 */
public void setTemperature_flag(int temperatureFlag) {
	temperature_flag = temperatureFlag;
}

/**
 * @return the sampling_test_flag
 */
public int getSampling_test_flag() {
	return sampling_test_flag;
}

/**
 * @param samplingTestFlag the sampling_test_flag to set
 */
public void setSampling_test_flag(int samplingTestFlag) {
	sampling_test_flag = samplingTestFlag;
}
public int getSampling_cond_flag() {
	return sampling_cond_flag;
}

public void setSampling_cond_flag(int sampling_cond_flag) {
	this.sampling_cond_flag = sampling_cond_flag;
}

/**
 * @return the rework_step_flag
 */
public int getRework_step_flag() {
	return rework_step_flag;
}

/**
 * @param reworkStepFlag the rework_step_flag to set
 */
public void setRework_step_flag(int reworkStepFlag) {
	rework_step_flag = reworkStepFlag;
}

/**
 * @return the test_time2_flag
 */
public int getTest_time2_flag() {
	return test_time2_flag;
}

/**
 * @param testTime2Flag the test_time2_flag to set
 */
public void setTest_time2_flag(int testTime2Flag) {
	test_time2_flag = testTime2Flag;
}

/**
 * @return the time_unit2_flag
 */
public int getTime_unit2_flag() {
	return time_unit2_flag;
}

/**
 * @param timeUnit2Flag the time_unit2_flag to set
 */
public void setTime_unit2_flag(int timeUnit2Flag) {
	time_unit2_flag = timeUnit2Flag;
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
 * @return the backend_option_flag
 */
public int getBackend_option_flag() {
	return backend_option_flag;
}

/**
 * @param backendOptionFlag the backend_option_flag to set
 */
public void setBackend_option_flag(int backendOptionFlag) {
	backend_option_flag = backendOptionFlag;
}

/**
 * @return the fg_with_code_flag
 */
public int getFg_with_code_flag() {
	return fg_with_code_flag;
}

/**
 * @param fgWithCodeFlag the fg_with_code_flag to set
 */
public void setFg_with_code_flag(int fgWithCodeFlag) {
	fg_with_code_flag = fgWithCodeFlag;
}

/**
 * @return the pin_count_flag
 */
public int getPin_count_flag() {
	return pin_count_flag;
}

/**
 * @param pinCountFlag the pin_count_flag to set
 */
public void setPin_count_flag(int pinCountFlag) {
	pin_count_flag = pinCountFlag;
}

/**
 * @return the package_type_flag
 */
public int getPackage_type_flag() {
	return package_type_flag;
}

/**
 * @param packageTypeFlag the package_type_flag to set
 */
public void setPackage_type_flag(int packageTypeFlag) {
	package_type_flag = packageTypeFlag;
}

/**
 * @return the ft_route_code_flag
 */
public int getFt_route_code_flag() {
	return ft_route_code_flag;
}

/**
 * @param ftRouteCodeFlag the ft_route_code_flag to set
 */
public void setFt_route_code_flag(int ftRouteCodeFlag) {
	ft_route_code_flag = ftRouteCodeFlag;
}

/**
 * @return the ft_route_flag
 */
public int getFt_route_flag() {
	return ft_route_flag;
}

/**
 * @param ftRouteFlag the ft_route_flag to set
 */
public void setFt_route_flag(int ftRouteFlag) {
	ft_route_flag = ftRouteFlag;
}

/**
 * @return the ft_route_add_flag
 */
public int getFt_route_add_flag() {
	return ft_route_add_flag;
}

/**
 * @param ftRouteAddFlag the ft_route_add_flag to set
 */
public void setFt_route_add_flag(int ftRouteAddFlag) {
	ft_route_add_flag = ftRouteAddFlag;
}

/**
 * @return the tf_comment_flag
 */
public int getTf_comment_flag() {
	return tf_comment_flag;
}

/**
 * @param tfCommentFlag the tf_comment_flag to set
 */
public void setTf_comment_flag(int tfCommentFlag) {
	tf_comment_flag = tfCommentFlag;
}

/**
 * @return the mask_option_flag
 */
public int getMask_option_flag() {
	return mask_option_flag;
}

/**
 * @param maskOptionFlag the mask_option_flag to set
 */
public void setMask_option_flag(int maskOptionFlag) {
	mask_option_flag = maskOptionFlag;
}

/**
 * @return the db_with_code_flag
 */
public int getDb_with_code_flag() {
	return db_with_code_flag;
}

/**
 * @param dbWithCodeFlag the db_with_code_flag to set
 */
public void setDb_with_code_flag(int dbWithCodeFlag) {
	db_with_code_flag = dbWithCodeFlag;
}

/**
 * @return the sort_route_code_flag
 */
public int getSort_route_code_flag() {
	return sort_route_code_flag;
}

/**
 * @param sortRouteCodeFlag the sort_route_code_flag to set
 */
public void setSort_route_code_flag(int sortRouteCodeFlag) {
	sort_route_code_flag = sortRouteCodeFlag;
}

/**
 * @return the ws_route_flag
 */
public int getWs_route_flag() {
	return ws_route_flag;
}

/**
 * @param wsRouteFlag the ws_route_flag to set
 */
public void setWs_route_flag(int wsRouteFlag) {
	ws_route_flag = wsRouteFlag;
}

/**
 * @return the ws_route_add_flag
 */
public int getWs_route_add_flag() {
	return ws_route_add_flag;
}

/**
 * @param wsRouteAddFlag the ws_route_add_flag to set
 */
public void setWs_route_add_flag(int wsRouteAddFlag) {
	ws_route_add_flag = wsRouteAddFlag;
}

/**
 * @return the tf_ws_comment_flag
 */
public int getTf_ws_comment_flag() {
	return tf_ws_comment_flag;
}

/**
 * @param tfWsCommentFlag the tf_ws_comment_flag to set
 */
public void setTf_ws_comment_flag(int tfWsCommentFlag) {
	tf_ws_comment_flag = tfWsCommentFlag;
}

public String getFt_route_add2() {
    return ft_route_add2;
}

public void setFt_route_add2(String ft_route_add2) {
    this.ft_route_add2 = ft_route_add2;
}

public String getFt_route_add3() {
    return ft_route_add3;
}

public void setFt_route_add3(String ft_route_add3) {
    this.ft_route_add3 = ft_route_add3;
}

public int getFt_route_add_flag2() {
    return ft_route_add_flag2;
}

public void setFt_route_add_flag2(int ft_route_add_flag2) {
    this.ft_route_add_flag2 = ft_route_add_flag2;
}

public int getFt_route_add_flag3() {
    return ft_route_add_flag3;
}

public void setFt_route_add_flag3(int ft_route_add_flag3) {
    this.ft_route_add_flag3 = ft_route_add_flag3;
}

public String getQuality_level() {
    return quality_level;
}

public void setQuality_level(String quality_level) {
    this.quality_level = quality_level;
}

public String getQuality_level_comment() {
    return quality_level_comment;
}

public void setQuality_level_comment(String quality_level_comment) {
    this.quality_level_comment = quality_level_comment;
}

public int getQuality_level_flag() {
    return quality_level_flag;
}

public void setQuality_level_flag(int quality_level_flag) {
    this.quality_level_flag = quality_level_flag;
}

public int getQuality_level_comment_flag() {
    return quality_level_comment_flag;
}

public void setQuality_level_comment_flag(int quality_level_comment_flag) {
    this.quality_level_comment_flag = quality_level_comment_flag;
}

public String getWs_special_control() {
    return ws_special_control;
}

public void setWs_special_control(String ws_special_control) {
    this.ws_special_control = ws_special_control;
}

public String getFt_special_control() {
    return ft_special_control;
}

public void setFt_special_control(String ft_special_control) {
    this.ft_special_control = ft_special_control;
}

public String getAvi() {
    return avi;
}

public void setAvi(String avi) {
    this.avi = avi;
}

public String getInk() {
    return ink;
}

public void setInk(String ink) {
    this.ink = ink;
}

public int getWs_special_control_flag() {
    return ws_special_control_flag;
}

public void setWs_special_control_flag(int ws_special_control_flag) {
    this.ws_special_control_flag = ws_special_control_flag;
}

public int getFt_special_control_flag() {
    return ft_special_control_flag;
}

public void setFt_special_control_flag(int ft_special_control_flag) {
    this.ft_special_control_flag = ft_special_control_flag;
}

public int getAvi_flag() {
    return avi_flag;
}

public void setAvi_flag(int avi_flag) {
    this.avi_flag = avi_flag;
}

public int getInk_flag() {
    return ink_flag;
}

public void setInk_flag(int ink_flag) {
    this.ink_flag = ink_flag;
}

public String getComponent_no() {
	return component_no;
}

public void setComponent_no(String component_no) {
	this.component_no = component_no;
}

public String getCom_prod_body() {
	return com_prod_body;
}

public void setCom_prod_body(String com_prod_body) {
	this.com_prod_body = com_prod_body;
}

public String getCom_mask_option() {
	return com_mask_option;
}

public void setCom_mask_option(String com_mask_option) {
	this.com_mask_option = com_mask_option;
}

public String getCom_backend_option() {
	return com_backend_option;
}

public void setCom_backend_option(String com_backend_option) {
	this.com_backend_option = com_backend_option;
}

public int getComponent_no_flag() {
	return component_no_flag;
}

public void setComponent_no_flag(int component_no_flag) {
	this.component_no_flag = component_no_flag;
}

public int getCom_prod_body_flag() {
	return com_prod_body_flag;
}

public void setCom_prod_body_flag(int com_prod_body_flag) {
	this.com_prod_body_flag = com_prod_body_flag;
}

public int getCom_mask_option_flag() {
	return com_mask_option_flag;
}

public void setCom_mask_option_flag(int com_mask_option_flag) {
	this.com_mask_option_flag = com_mask_option_flag;
}

public int getCom_backend_option_flag() {
	return com_backend_option_flag;
}

public void setCom_backend_option_flag(int com_backend_option_flag) {
	this.com_backend_option_flag = com_backend_option_flag;
}

public String getMcp_flag() {
	return mcp_flag;
}

public void setMcp_flag(String mcp_flag) {
	this.mcp_flag = mcp_flag;
}

public int getMcp_flag_flag() {
	return mcp_flag_flag;
}

public void setMcp_flag_flag(int mcp_flag_flag) {
	this.mcp_flag_flag = mcp_flag_flag;
}
}
