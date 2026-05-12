package com.mxic.oiplus.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

public class DocLinkageActionForm extends ActionForm {
  private String record_id;
  private String upload;
  private String doc_type;
  private String listControl;
  private String listControl_upload;
  private String brand;
  private String comment;
  private String pd_body;
  private String version;
  private String tag;
  private String doc_name;
  private String seq;
  private String category;
  private String old_doc;
  private String new_doc;
  private String status;
  private String show_old;
  private String show_comment;
  private String show_new;
  private String file_name_new;
  private String file_name_old;
  private String file_name_comment;
  private String sid;
  private String changecolor;

  public void setSeq(String seq) {
	  this.seq = seq;
  }

  public String getSeq() {
    return seq;
  }

  public void setDoc_type(String doc_type) {
    this.doc_type = doc_type;
  }

  public String getDoc_type() {
    return doc_type;
  }

  public void setUpload(String upload) {
    this.upload = upload;
  }

  public String getUpload() {
    return upload;
  }

  public void setFile_name_old(String file_name_old) {
    this.file_name_old = file_name_old;
  }

  public String getFile_name_old() {
    return file_name_old;
  }

  public void setFile_name_new(String file_name_new) {
    this.file_name_new = file_name_new;
  }

  public String getFile_name_new() {
    return file_name_new;
  }

  public void setFile_name_comment(String file_name_comment) {
    this.file_name_comment = file_name_comment;
  }

  public String getFile_name_comment() {
    return file_name_comment;
  }

  public void setShow_old(String show_old) {
    this.show_old = show_old;
  }

  public String getShow_old() {
    return show_old;
  }

  public void setShow_new(String show_new) {
    this.show_new = show_new;
  }

  public String getShow_new() {
    return show_new;
  }

  public void setShow_comment(String show_comment) {
    this.show_comment = show_comment;
  }

  public String getShow_comment() {
    return show_comment;
  }

  public void setNew_doc(String new_doc) {
    this.new_doc = new_doc;
  }

  public String getNew_doc() {
    return new_doc;
  }

  public void setOld_doc(String old_doc) {
    this.old_doc = old_doc;
  }

  public String getOld_doc() {
    return old_doc;
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

  public String getListControl_upload() {
    return listControl_upload;
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

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setSid(String sid) { 
	  this.sid = sid;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public void setListControl_upload(String listControl_upload) {
    this.listControl_upload = listControl_upload;
  }

  public void setChangecolor(String changecolor) {
    this.changecolor = changecolor;
  }

  public String getBrand() {
    return brand;
  }

  public String getComment() {
    return comment;
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

  public String getSid() {
    return sid;
  }

  public String getChangecolor() {
    if (show_new.equals("show")) {
      return "#FFDDFF";
    } else {
      return "";
    }
  }

  public String getDisplay_old() {
    if (show_old.equals("show")) {
      return "";
    } else {
      return "display:none";
    }
  }

  public String getDisplay_comment() {
    if (show_comment.equals("show")) {
      return "";
    } else {
      return "display:none";
    }
  }

  public String getDisplay_new() {
    if (show_new.equals("show")) {
      return "";
    } else {
      return "display:none";
    }
  }

  public String getDisplay_one() {
    if (tag.equals("1")) {
      return "display:none";
    } else {
      return "";
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
}