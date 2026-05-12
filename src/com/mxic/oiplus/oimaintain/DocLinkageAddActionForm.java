package com.mxic.oiplus.oimaintain;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

public class DocLinkageAddActionForm extends ActionForm {
  private FormFile filename;
  private FormFile formname;
  private FormFile file;
  private String listControl;
  private String brand;
  private String pd_body;
  private String version;
  private String doc_name;
  private int seq;
  private String category;
  private String comment;
  private int sid;
  private String file_name;

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

  public void setFormname(FormFile formname) {
    this.formname = formname;
  }

  public FormFile getFilename() {
    return filename;
  }

  public FormFile getFormname() {
    return formname;
  }

  public FormFile getFile() {
    return file;
  }

  public void setFile(FormFile file) {
    this.file = file;
  }

  public String getFile_name() {
	return file_name;
  }

  public void setFile_name(String file_name) {
	this.file_name = file_name;
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
