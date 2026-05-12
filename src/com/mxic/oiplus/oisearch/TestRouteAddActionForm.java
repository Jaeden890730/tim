package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

public class TestRouteAddActionForm extends ActionForm {
  private FormFile filename;
  private FormFile formname;
  private FormFile formname1;
  private FormFile formname2;
  private FormFile formname3;
  private FormFile file;
  private String listControl;
  private String ra_select;
  private String brand;
  private String pd_body;
  private String version;
  private String doc_name;
  private int seq;
  private String category;
  private String comment;
  private int sid;
  private String action;
  private String file_type;
  private String radioFile;
  
  public String getAction() {
	return action;
  }

  public void setAction(String action) {
	this.action = action;
  }

  public String getFile_type() {
	return file_type;
  }

  public void setFile_type(String file_type) {
	this.file_type = file_type;
  }

  public String getRadioFile() {
	return radioFile;
  }

  public void setRadioFile(String radioFile) {
	this.radioFile = radioFile;
  }

  public void setSeq(int seq) {
    this.seq = seq;
  }

  public int getSeq() {
    return seq;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getComment() {
    return comment;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getCategory() {
    return category;
  }

  public void setDoc_name(String doc_name) {
    this.doc_name = doc_name;
  }

  public String getDoc_name() {
    return doc_name;
  }

  public String getListControl() {
    return listControl;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
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

  public int getSid() {
    return sid;
  }

  public void setFilename(FormFile filename) {
	    this.filename = filename;
  }

  public FormFile getFilename() {
	  return filename;
  }

  public void setFormname(FormFile formname) {
    this.formname = formname;
  }

  public FormFile getFormname() {
    return formname;
  }
 
  public FormFile getFormname1() {
	return formname1;
  }

  public void setFormname1(FormFile formname1) {
	this.formname1 = formname1;
  }

  public FormFile getFormname2() {
	return formname2;
  }

  public void setFormname2(FormFile formname2) {
	this.formname2 = formname2;
  }

  public FormFile getFormname3() {
	return formname3;
  }

  public void setFormname3(FormFile formname3) {
	this.formname3 = formname3;
  }

  public FormFile getFile() {
    return file;
  }

  public void setFile(FormFile file) {
    this.file = file;
  }
  
  public void setRa_select(String ra_select) {
    this.ra_select = ra_select;
  }

  public String getRa_select() {
    return ra_select;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }
}
