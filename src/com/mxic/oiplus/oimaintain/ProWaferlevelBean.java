package com.mxic.oiplus.oimaintain;

import java.awt.*;
import javax.swing.*;

public class ProWaferlevelBean {
  private String productbody;
  private String brand;
  private String sid;
  private String version;
  private String wafer_level;
  private String wafer_brand;
  private String tag;
  private String biztype;
  private String wafer_grade;
  private String apply_type;
  private String ori_priority;
  private String revise_priority;
  private String checked_flag;
  private String rownum;

  
  public String getRownum() {
	return rownum;
}

public void setRownum(String rownum) {
	this.rownum = rownum;
}

public void setProductbody(String productbody) {
    this.productbody = productbody;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }


  public void setSid(String sid) {
    this.sid = sid;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getProductbody() {
    return productbody;
  }

  public String getBrand() {
    return brand;
  }

  public String getSid() {
    return sid;
  }

  public String getVersion() {
    return version;
  }

/*

  public String getInsertvalue(){
	    if (stepname.substring(0,1).toUpperCase().equals("B") ||
	        stepname.substring(0,1).toUpperCase().equals("U") ||
	        stepname.toUpperCase().startsWith("TEMPER") ||
	        stepname.substring(0,1).toUpperCase().equals("C")){
	      return " ";
	    } else {
	      return "disabled=";
	    }
	  }
  
  public String getInsertvalue_s(){
	   if (	stepname.startsWith("SORT")||
			stepname.startsWith("FT") ||
			stepname.startsWith("TQAE")){
	      return " ";
	    } else {
	      return "disabled=";
	    }
	  }

  public String getRework_display(){
	    if (stepname.startsWith("SORT") ||
	    	stepname.startsWith("FT")){
	      return " ";
	    } else {
	      return "disabled=";
	    }
	  }

  public String getRework_uv_display(){
	    if ((stepname.startsWith("SORT")||stepname.startsWith("FT")) && reworkstep != null &&
	    	reworkstep.startsWith("UV")){
	      return " ";
	    } else {
	      return "disabled=";
	    }
	  }
*/
  /**
   * @return the tag
   */
  public String getTag() {
	  return tag;
  }

  /**
   * @param tag the tag to set
   */
  public void setTag(String tag) {
	  this.tag = tag;
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
}
