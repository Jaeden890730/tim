package com.mxic.oiplus.oimaintain;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

public class OIMigrationUploadActionForm extends ActionForm {
  private FormFile tf_bom_route;
  private FormFile tf_test_parameter_ws;
  private FormFile tf_test_parameter_ft;
  private FormFile tf_test_parameter_pbc;
  private FormFile tf_basic_information;
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

  public void setTf_test_parameter_ft(FormFile tf_test_parameter_ft) {
    this.tf_test_parameter_ft = tf_test_parameter_ft;
  }

  public FormFile getTf_test_parameter_ft() {
    return tf_test_parameter_ft;
  }

  public void setTf_basic_information(FormFile tf_basic_information) {
    this.tf_basic_information = tf_basic_information;
  }

  public FormFile getTf_basic_information() {
    return tf_basic_information;
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

  public void setTf_test_parameter_ws(FormFile tf_test_parameter_ws) {
    this.tf_test_parameter_ws = tf_test_parameter_ws;
  }

  public FormFile getTf_test_parameter_ws() {
    return tf_test_parameter_ws;
  }

  public void setTf_bom_route(FormFile tf_bom_route) {
    this.tf_bom_route = tf_bom_route;
  }

  public void setFormname(FormFile formname) {
    this.formname = formname;
  }

  public FormFile getTf_bom_route() {
    return tf_bom_route;
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

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {
    /** @todo: finish this method, this is just the skeleton.*/
    return null;
  }

  public void reset(ActionMapping actionMapping,
                    HttpServletRequest servletRequest) {
  }

  /**
   * @return the tf_test_parameter_pbc
   */
  public FormFile getTf_test_parameter_pbc() {
	  return tf_test_parameter_pbc;
  }

  /**
   * @param tf_test_parameter_pbc the tf_test_parameter_pbc to set
   */
  public void setTf_test_parameter_pbc(FormFile tf_test_parameter_pbc) {
	  this.tf_test_parameter_pbc = tf_test_parameter_pbc;
  }
}
