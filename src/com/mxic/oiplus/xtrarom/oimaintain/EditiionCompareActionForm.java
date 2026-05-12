package com.mxic.oiplus.xtrarom.oimaintain;

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
  private String maskopt_rev;
  private String code_no;
  private String route_type;
  private String dbwithcode;
  private String sortroutecode;
  private String wsroute;
  private String wsaddroute;
  private String wsaddroute1;
  private String wsaddroute2;
  private String wsaddroute3;
  private String wsaddroute4;
  private String comment;
  private String wscomment;
  private String ft_route_add;
  private String ft_route_add1;
  private String ft_route_add2;
  private String ft_route_add3;
  private String ft_route_add4;
  private String ft_route_add5;
  private String tag;
  private String id;
  private String sid;
  private String version;
  private String body_version;
  private String pd_body;
  private String status;
  private String route_name;
  private int step_seq;
  private String step_name;
  private int test_time;
  private String time_unit;
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
  private String type_flag = null;
  private int body_version_flag = 0;
  private int mask_option_flag = 0;
  private int mask_option_rev_flag = 0;
  private int code_no_flag = 0;
  private int pin_count_flag = 0;
  private int package_code_flag = 0;
  private int route_type_flag = 0;
  private int ft_route_code_flag = 0;
  private int ft_route_flag = 0;
  private int ft_route_add_flag = 0;
  private int ft_route_add1_flag = 0;
  private int ft_route_add2_flag = 0;
  private int ft_route_add3_flag = 0;
  private int ft_route_add4_flag = 0;
  private int ft_route_add5_flag = 0;
  private int tf_comment_flag = 0;
  private int sort_route_code_flag = 0;
  private int ws_route_flag = 0;
  private int ws_route_add_flag = 0;
  private int ws_route_add1_flag = 0;
  private int ws_route_add2_flag = 0;
  private int ws_route_add3_flag = 0;
  private int ws_route_add4_flag = 0;
  private int tf_ws_comment_flag = 0;

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
  
  public void setMaskopt_rev(String maskopt_rev) {
	    this.maskopt_rev = maskopt_rev;
  }
  
  public void setCode_no(String code_no) {
	    this.code_no = code_no;
  }
  
  public void setRoute_type(String route_type) {
	    this.route_type = route_type;
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
  
  public void setWsaddroute1(String wsaddroute1) {
	this.wsaddroute1 = wsaddroute1;
  }
  
  public void setWsaddroute2(String wsaddroute2) {
	this.wsaddroute2 = wsaddroute2;
  }
  
  public void setWsaddroute3(String wsaddroute3) {
		this.wsaddroute3 = wsaddroute3;
  }
  
  public void setWsaddroute4(String wsaddroute4) {
		this.wsaddroute4 = wsaddroute4;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setFt_route_add(String ft_route_add) {
    this.ft_route_add = ft_route_add;
  }
  
  public void setFt_route_add1(String ft_route_add1) {
	    this.ft_route_add1 = ft_route_add1;
  }
  
  public void setFt_route_add2(String ft_route_add2) {
	    this.ft_route_add2 = ft_route_add2;
  }
  
  public void setFt_route_add3(String ft_route_add3) {
	    this.ft_route_add3 = ft_route_add3;
  }
  
  public void setFt_route_add4(String ft_route_add4) {
	    this.ft_route_add4 = ft_route_add4;
  }
  
  public void setFt_route_add5(String ft_route_add5) {
	    this.ft_route_add5 = ft_route_add5;
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
  
  public void setBody_version(String body_version) {
	    this.body_version = body_version;
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
  
  public String getFt_route_add1() {
	    return ft_route_add1;
  }
  
  public String getFt_route_add2() {
	    return ft_route_add2;
  }
  
  public String getFt_route_add3() {
	    return ft_route_add3;
  }
  
  public String getFt_route_add4() {
	    return ft_route_add4;
  }
  
  public String getFt_route_add5() {
	    return ft_route_add5;
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
  
  public String getMaskopt_rev() {
	    return maskopt_rev;
  }
  
  public String getCode_no() {
	    return code_no;
  }
  
  public String getRoute_type() {
	    return route_type;
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
  
  public String getWsaddroute1() {
	    return wsaddroute1;
  }
  
  public String getWsaddroute2() {
	    return wsaddroute2;
  }
  
  public String getWsaddroute3() {
	    return wsaddroute3;
  }
  
  public String getWsaddroute4() {
	    return wsaddroute4;
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
  
  public String getBody_version() {
	    return body_version;
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
 * @return the body_version_flag
 */
public int getBody_version_flag() {
	return body_version_flag;
}

/**
 * @param bodyVersionFlag the body_version_flag to set
 */
public void setBody_version_flag(int bodyVersionFlag) {
	body_version_flag = bodyVersionFlag;
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
 * @return the mask_option_rev_flag
 */
public int getMask_option_rev_flag() {
	return mask_option_rev_flag;
}

/**
 * @param maskOptionRevFlag the mask_option_rev_flag to set
 */
public void setMask_option_rev_flag(int maskOptionRevFlag) {
	mask_option_rev_flag = maskOptionRevFlag;
}

/**
 * @return the code_no_flag
 */
public int getCode_no_flag() {
	return code_no_flag;
}

/**
 * @param codeNoFlag the code_no_flag to set
 */
public void setCode_no_flag(int codeNoFlag) {
	code_no_flag = codeNoFlag;
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
 * @return the package_code_flag
 */
public int getPackage_code_flag() {
	return package_code_flag;
}

/**
 * @param packageCodeFlag the package_code_flag to set
 */
public void setPackage_code_flag(int packageCodeFlag) {
	package_code_flag = packageCodeFlag;
}

/**
 * @return the route_type_flag
 */
public int getRoute_type_flag() {
	return route_type_flag;
}

/**
 * @param routeTypeFlag the route_type_flag to set
 */
public void setRoute_type_flag(int routeTypeFlag) {
	route_type_flag = routeTypeFlag;
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
 * @return the ft_route_add1_flag
 */
public int getFt_route_add1_flag() {
	return ft_route_add1_flag;
}

/**
 * @param ftRouteAdd1Flag the ft_route_add1_flag to set
 */
public void setFt_route_add1_flag(int ftRouteAdd1Flag) {
	ft_route_add1_flag = ftRouteAdd1Flag;
}

/**
 * @return the ft_route_add2_flag
 */
public int getFt_route_add2_flag() {
	return ft_route_add2_flag;
}

/**
 * @param ftRouteAdd2Flag the ft_route_add2_flag to set
 */
public void setFt_route_add2_flag(int ftRouteAdd2Flag) {
	ft_route_add2_flag = ftRouteAdd2Flag;
}

/**
 * @return the ft_route_add3_flag
 */
public int getFt_route_add3_flag() {
	return ft_route_add3_flag;
}

/**
 * @param ftRouteAdd3Flag the ft_route_add3_flag to set
 */
public void setFt_route_add3_flag(int ftRouteAdd3Flag) {
	ft_route_add3_flag = ftRouteAdd3Flag;
}

/**
 * @return the ft_route_add4_flag
 */
public int getFt_route_add4_flag() {
	return ft_route_add4_flag;
}

/**
 * @param ftRouteAdd4Flag the ft_route_add4_flag to set
 */
public void setFt_route_add4_flag(int ftRouteAdd4Flag) {
	ft_route_add4_flag = ftRouteAdd4Flag;
}

/**
 * @return the ft_route_add5_flag
 */
public int getFt_route_add5_flag() {
	return ft_route_add5_flag;
}

/**
 * @param ftRouteAdd5Flag the ft_route_add5_flag to set
 */
public void setFt_route_add5_flag(int ftRouteAdd5Flag) {
	ft_route_add5_flag = ftRouteAdd5Flag;
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
 * @return the ws_route_add1_flag
 */
public int getWs_route_add1_flag() {
	return ws_route_add1_flag;
}

/**
 * @param wsRouteAdd1Flag the ws_route_add1_flag to set
 */
public void setWs_route_add1_flag(int wsRouteAdd1Flag) {
	ws_route_add1_flag = wsRouteAdd1Flag;
}

/**
 * @return the ws_route_add2_flag
 */
public int getWs_route_add2_flag() {
	return ws_route_add2_flag;
}

/**
 * @param wsRouteAdd2Flag the ws_route_add2_flag to set
 */
public void setWs_route_add2_flag(int wsRouteAdd2Flag) {
	ws_route_add2_flag = wsRouteAdd2Flag;
}

/**
 * @return the ws_route_add3_flag
 */
public int getWs_route_add3_flag() {
	return ws_route_add3_flag;
}

/**
 * @param wsRouteAdd3Flag the ws_route_add3_flag to set
 */
public void setWs_route_add3_flag(int wsRouteAdd3Flag) {
	ws_route_add3_flag = wsRouteAdd3Flag;
}

/**
 * @return the ws_route_add4_flag
 */
public int getWs_route_add4_flag() {
	return ws_route_add4_flag;
}

/**
 * @param wsRouteAdd4Flag the ws_route_add4_flag to set
 */
public void setWs_route_add4_flag(int wsRouteAdd4Flag) {
	ws_route_add4_flag = wsRouteAdd4Flag;
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
}
