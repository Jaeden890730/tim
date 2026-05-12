package com.mxic.oiplus.xtrarom.oimaintain;

import java.awt.BorderLayout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JPanel;

import com.mxic.oiplus.au.User;

public class OiMaintainStep {
  private String sid;
  private String product_body;
  private String brand;
  private String version;
  private String status;
  private String creator;
  private String sponsor_1;
  private String sponsor_2;
  private String log_time;
  private String tf_product_route;
  private String tf_bom_route;
  private String tf_bom_reroute;
  private String tf_test_parameter_ws;
  private String tf_test_parameter_ft;
  private String tf_basic_information;
  private String tf_yield_definition;
  private String tf_yield_ws;
  private String tf_yield_ft;
  private String tf_document_linkage;
  private String tf_wip_control;
  private String product_type;
  private String tf_test_parameter_pbc;
  private String tf_main_sub;
  private String tf_main_rework;
  private User Auth;

  public void setSid(String sid) {
    this.sid = sid;
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

  public void setStatus(String status) {
    this.status = status;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public void setSponsor_1(String sponsor_1) {
    this.sponsor_1 = sponsor_1;
  }

  public void setSponsor_2(String sponsor_2) {
    this.sponsor_2 = sponsor_2;
  }

  public void setLog_time(String log_time) {
    this.log_time = log_time;
  }

  public void setTf_product_route(String tf_product_route) {
    this.tf_product_route = tf_product_route;
  }

  public void setTf_bom_route(String tf_bom_route) {
    this.tf_bom_route = tf_bom_route;
  }

  public void setTf_bom_reroute(String tf_bom_reroute) {
    this.tf_bom_reroute = tf_bom_reroute;
  }

  public void setTf_test_parameter_ws(String tf_test_parameter_ws) {
    this.tf_test_parameter_ws = tf_test_parameter_ws;
  }

  public void setTf_test_parameter_ft(String tf_test_parameter_ft) {
    this.tf_test_parameter_ft = tf_test_parameter_ft;
  }

  public void setTf_basic_information(String tf_basic_information) {
	    this.tf_basic_information = tf_basic_information;
	  }

  public void setTf_yield_definition(String tf_yield_definition) {
	    this.tf_yield_definition = tf_yield_definition;
	  }

  public void setTf_yield_ws(String tf_yield_ws) {
    this.tf_yield_ws = tf_yield_ws;
	  }

  public void setTf_yield_ft(String tf_yield_ft) {
    this.tf_yield_ft = tf_yield_ft;
	  }

  public void setTf_document_linkage(String tf_document_linkage) {
    this.tf_document_linkage = tf_document_linkage;
  }

  public void setTf_wip_control(String tf_wip_control) {
    this.tf_wip_control = tf_wip_control;
  }

  public String getSid() {
    return sid;
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

  public String getStatus() {
    return status;
  }

  public String getCreator() {
    return creator;
  }

  public String getSponsor_1() {
    return sponsor_1;
  }

  public String getSponsor_2() {
    return sponsor_2;
  }

  public String getLog_time() {
    return log_time;
  }

  public String getTf_product_route() {
    return tf_product_route;
  }

  public String getV_tf_product_route(){
    if (tf_product_route.equals("Y")){
      return "(V)";
    } else if(tf_product_route.equals("N")){
      return " ";
    } else{
      return null;
    }
  }

  public String getTf_bom_route() {
    return tf_bom_route;
  }

  public String getV_tf_bom_route(){
    if (tf_bom_route.equals("Y")){
      return "(V)";
    } else{
      return " ";
    }
  }
  public String getV_tf_bom_reroute(){
    if (tf_bom_reroute.equals("Y")){
      return "(V)";
    } else{
      return " ";
    }
  }

  public String getTf_test_parameter_ws() {
    return tf_test_parameter_ws;
  }

  public String getV_tf_test_parameter_ws(){
    if (tf_test_parameter_ws.equals("Y")){
      return "(V)";
    } else {
      return " ";
    }
  }

  public String getTf_test_parameter_ft() {
    return tf_test_parameter_ft;
  }

  public String getV_tf_test_parameter_ft(){
    if (tf_test_parameter_ft.equals("Y")){
      return "(V)";
    } else {
      return " ";
    }
  }

  public String getTf_basic_information() {
	    return tf_basic_information;
	  }

  public String getTf_yield_definition() {
	    return tf_yield_definition;
	  }

  public String getTf_yield_ws() {
	    return tf_yield_ws;
	  }

  public String getTf_yield_ft() {
	  	return tf_yield_ft;
	  }

  public String getV_tf_basic_information(){
	    if (tf_basic_information.equals("Y")){
	      return "(V)";
	    } else {
	      return " ";
	    }
	  }

  public String getV_tf_yield_definition(){
	    if (tf_yield_definition.equals("Y")){
	      return "(V)";
	    } else {
	      return " ";
	    }
	  }

  public String getV_tf_yield_ws(){
	    if (tf_yield_ws.equals("Y")){
	      return "(V)";
	    } else {
	      return " ";
	    }
	  }

  public String getV_tf_yield_ft(){
	    if (tf_yield_ft.equals("Y")){
	      return "(V)";
	    } else {
	      return " ";
	    }
	  }

  public String getTf_document_linkage() {
    return tf_document_linkage;
  }

  public String getV_tf_document_linkage(){
    if (tf_document_linkage.equals("Y")){
      return "(V)";
    } else {
      return " ";
    }
  }

  public String getTf_wip_control() {
    return tf_wip_control;
  }

  public String getV_tf_wip_control(){
    if (tf_wip_control.equals("Y")){
      return "(V)";
    } else {
      return " ";
    }
  }
  /**
   * @return Returns the product_type.
   */
  public String getProduct_type() {
	  return product_type;
  }

  /**
   * @param product_type The product_type to set.
   */
  public void setProduct_type(String product_type) {
	  this.product_type = product_type;
  }

  /**
   * @return Returns the tf_test_parameter_pbc.
   */
  public String getTf_test_parameter_pbc() {
	  return tf_test_parameter_pbc;
  }

  /**
   * @param tf_test_parameter_pbc The tf_test_parameter_pbc to set.
   */
  public void setTf_test_parameter_pbc(String tf_test_parameter_pbc) {
	  this.tf_test_parameter_pbc = tf_test_parameter_pbc;
  }

  public String getV_tf_test_parameter_pbc(){
          if (tf_test_parameter_pbc.equals("Y")){
                  return "(V)";
          } else {
                  return " ";
          }
  }

  /**
     * @return Returns the tf_main_sub.
     */
    public String getTf_main_sub() {
            return tf_main_sub;
    }

    /**
     * @param tf_main_sub The tf_main_sub to set.
     */
    public void setTf_main_sub(String tf_main_sub) {
            this.tf_main_sub = tf_main_sub;
  }

  public String getV_tf_main_sub(){
          if (tf_main_sub.equals("Y")){
                  return "(V)";
          } else {
                  return " ";
          }
  }

  /**
     * @return Returns the tf_main_rework.
     */
    public String getTf_main_rework() {
            return tf_main_rework;
    }

    /**
     * @param tf_main_rework The tf_main_rework to set.
     */
    public void setTf_main_rework(String tf_main_rework) {
            this.tf_main_rework = tf_main_rework;
  }
  public String getV_tf_main_rework(){
	  if (tf_main_rework.equals("Y")){
		  return "(V)";
	  } else {
		  return " ";
	  }
  }

  /**
   * @return the auth
   */
  public User getAuth() {
	  return Auth;
  }

  /**
   * @param auth the auth to set
   */
  public void setAuth(User auth) {
	  Auth = auth;
  }

  public String getAuthorityUser() {
	  String user=Auth.getUserName();
	  if (user.equals(creator) || user.equals(sponsor_1) || user.equals(sponsor_2)) {
	      return "true";
	  } else {
	       return "false";
	  }

  }
}
