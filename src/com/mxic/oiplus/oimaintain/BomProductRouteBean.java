/*
* JAVABean for Bom Product Route
*/

package com.mxic.oiplus.oimaintain;

public class BomProductRouteBean{
  private String epnbody;
  private String brand;
  private String productbody;
  private String beoption;
  private String fgwithcode;
  private String pincount;
  private String pkgtype;
  private String speed;
  private String grade;
  private String productclass;
  private String ftroute;
  private String maskopt;
  private String dbwithcode;
  private String sortroutecode;
  private String wsroute;
  private String wsaddroute;
  private String tf_ws_comment;
  private String comment;
  private String speedoption;
  private String tag;
  private String id;
  private String sid;
  private String version;
  private String ftAddroute;
  private String ftAddroute2;
  private String ftAddroute3;
  private String ft_route_code;
  private String sales_form;
  private String endurance;
  private String wsspecialcontrol;
  private String quality_level;
  private String quality_level_comment;
  private String mcp_flag;
  
  private String component_no;
  private String com_prod_body;
  private String com_mask_option;
  private String com_backend_option;
  private String outsource_tag;
  
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

  public void setSpeed(String speed) {
    this.speed = speed;
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

  public void setTf_ws_comment(String tf_ws_comment) {
    this.tf_ws_comment = tf_ws_comment;
  }

  public void setSpeedoption(String speedoption) {
    this.speedoption = speedoption;
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

  public void setEndurance(String endurance) {
	    this.endurance = endurance;
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

  public String getSpeed() {
    return speed;
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

  public String getTf_ws_comment() {
    return tf_ws_comment;
  }

  public String getSpeedoption() {
    return speedoption;
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

  public String getEndurance() {
	    return endurance;
  }

  public String getChangecolor(){
    if (tag.equals("1")){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }

  public String getFtAddroute() {
    return ftAddroute;
  }

  public void setFtAddroute(String ftAddroute) {
    this.ftAddroute = ftAddroute;
  }

  public String getFt_route_code() {
    return ft_route_code;
  }

  public void setFt_route_code(String ft_route_code) {
    this.ft_route_code = ft_route_code;
  }

  /**
   * @return the sales_form
   */
  public String getSales_form() {
	  return sales_form;
  }

  /**
   * @param sales_form the sales_form to set
   */
  public void setSales_form(String sales_form) {
	  this.sales_form = sales_form;
  }

  public String getSalesFormRealName() {
	  if (sales_form == null)
		  return "";
	  if (sales_form.equals("W"))
		  return "Wafer";
	  else if (sales_form.equals("IC"))
		  return "Package";
	  else
		  return "";
  }

public String getFtAddroute2() {
	return ftAddroute2;
}

public void setFtAddroute2(String ftAddroute2) {
	this.ftAddroute2 = ftAddroute2;
}

public String getFtAddroute3() {
    return ftAddroute3;
}

public void setFtAddroute3(String ftAddroute3) {
    this.ftAddroute3 = ftAddroute3;
}

/**
 * @return the wsspecialcontrol
 */
public String getWsspecialcontrol() {
	return wsspecialcontrol;
}

/**
 * @param wsspecialcontrol the wsspecialcontrol to set
 */
public void setWsspecialcontrol(String wsspecialcontrol) {
	this.wsspecialcontrol = wsspecialcontrol;
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

public String getMcp_flag() {
	return mcp_flag;
}

public void setMcp_flag(String mcp_flag) {
	this.mcp_flag = mcp_flag;
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

public boolean getShowSortRouteCodeBtn(){
	if((this.getOutsource_tag()!=null && this.getOutsource_tag().equals("0"))||this.getTag().equals("0")){
		return false;
	}
	return true;
}

/**
 * 0: •~¡ wafer
 * @return
 */
public String getOutsource_tag() {
	return outsource_tag;
}

public void setOutsource_tag(String outsource_tag) {
	this.outsource_tag = outsource_tag;
}

}
