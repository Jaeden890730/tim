/*
* JAVABean for Bom Product Route
*/

package com.mxic.oiplus.xtrarom.oimaintain;

public class BomProductReRouteBean{
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
  private String routetype;
  private String codeno;
  private String maskopt;
  private String maskoptrev;
  private String dbwithcode;
  private String sortroutecode;
  private String wsroute;
  private String wsaddroute;
  private String tf_ws_comment;
  private String comment;
  private String wscomment;
  private String ftcomment;
  private String speedoption;
  private String tag;
  private String id;
  private String sid;
  private String version;
  private String bodyversion;
  private String ftAddroute;
  private String recycle_code;
  private String sales_form;

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

  public void setRoutetype(String routetype) {
    this.routetype = routetype;
  }

  public void setCodeno(String codeno) {
    this.codeno = codeno;
  }

  public void setMaskopt(String maskopt) {
    this.maskopt = maskopt;
  }

  public void setMaskoptrev(String maskoptrev) {
    this.maskoptrev = maskoptrev;
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

  public void setWscomment(String wscomment) {
    this.wscomment = wscomment;
  }

  public void setFtcomment(String ftcomment) {
    this.ftcomment = ftcomment;
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

  public void setBodyersion(String bodyversion) {
    this.bodyversion = bodyversion;
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

  public String getRoutetype() {
    return routetype;
  }

  public String getCodeno() {
    return codeno;
  }

  public String getMaskopt() {
    return maskopt;
  }

  public String getMaskoptrev() {
    return maskoptrev;
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

  public String getFtcomment() {
    return ftcomment;
  }

  public String getWscomment() {
    return wscomment;
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

  public String getBodyversion() {
    return bodyversion;
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

  public String getRecycle_code() {
    return recycle_code;
  }

  public void setRecycle_code(String recycle_code) {
    this.recycle_code = recycle_code;
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
}
