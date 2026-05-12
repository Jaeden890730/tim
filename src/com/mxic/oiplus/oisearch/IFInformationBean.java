package com.mxic.oiplus.oisearch;

import com.mxic.oiplus.au.User;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class IFInformationBean {
  public IFInformationBean() {
  }

  private String sid;
  private String product_body;
  private String brand;
  private String version;
  private String status;
  private String creator;
  private String sponsor_1;
  private String sponsor_2;
  private String log_time;
  private String tf_prod_waferlevel;
  private String tf_product_route;
  private String tf_bom_route;
  private String tf_bom_mcp_route;
  private String tf_test_parameter_ws;
  private String tf_test_parameter_ft;
  private String tf_basic_information;
  private String tf_document_linkage;
  private String product_type;
  private String tf_test_parameter_pbc;
  private String route_change;
  private User Auth;

  private String package_component;
  
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

  public void setSid(String sid) {
	  this.sid = sid;
  }

  public String getSid() {
    return sid;
  }

  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public String getProduct_body() {
    return product_body;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getBrand() {
    return brand;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCreator() {
    return creator;
  }

  public void setSponsor_1(String sponsor_1) {
    this.sponsor_1 = sponsor_1;
  }

  public String getSponsor_1() {
    return sponsor_1;
  }

  public void setSponsor_2(String sponsor_2) {
    this.sponsor_2 = sponsor_2;
  }

  public String getSponsor_2() {
    return sponsor_2;
  }

  public void setLog_time(String log_time) {
    this.log_time = log_time;
  }

  public String getLog_time() {
    return log_time;
  }

  public void setTf_product_route(String tf_product_route) {
    this.tf_product_route = tf_product_route;
  }

  public String getTf_product_route() {
    return tf_product_route;
  }

  public void setTf_bom_route(String tf_bom_route) {
    this.tf_bom_route = tf_bom_route;
  }

  public String getTf_bom_route() {
    return tf_bom_route;
  }

  public String getTf_bom_mcp_route() {
	return tf_bom_mcp_route;
}

public void setTf_bom_mcp_route(String tf_bom_mcp_route) {
	this.tf_bom_mcp_route = tf_bom_mcp_route;
}

public void setTf_test_parameter_ws(String tf_test_parameter_ws) {
    this.tf_test_parameter_ws = tf_test_parameter_ws;
  }

  public String getTf_test_parameter_ws() {
    return tf_test_parameter_ws;
  }

  public void setTf_test_parameter_ft(String tf_test_parameter_ft) {
    this.tf_test_parameter_ft = tf_test_parameter_ft;
  }

  public String getTf_test_parameter_ft() {
    return tf_test_parameter_ft;
  }

  public void setTf_basic_information(String tf_basic_information) {
    this.tf_basic_information = tf_basic_information;
  }

  public String getTf_basic_information() {
    return tf_basic_information;
  }

  public void setTf_document_linkage(String tf_document_linkage) {
    this.tf_document_linkage = tf_document_linkage;
  }

  public String getTf_document_linkage() {
    return tf_document_linkage;
  }

  public void setRoute_change(String route_change) {
    this.route_change = route_change;
  }

  public String getRoute_change() {
    return route_change;
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

  public String getTf_prod_waferlevel() {
	  return tf_prod_waferlevel;
  }
	
  public void setTf_prod_waferlevel(String tf_prod_waferlevel) {
	  this.tf_prod_waferlevel = tf_prod_waferlevel;
  }

  public String getPackage_component() {
	  return package_component;
  }

  public void setPackage_component(String package_component) {
	  this.package_component = package_component;
  }
  public String getV_tf_bom_route(){
	  if (tf_bom_route!=null && tf_bom_route.equals("Y")){
		  return "(V)";
	  } else{
		  return " ";
	  }
  }

  public String getV_tf_bom_mcp_route(){
	  if (tf_bom_mcp_route!=null && tf_bom_mcp_route.equals("Y")){
		  return "(V)";
	  } else{
		  return " ";
	  }
  }
	  
	  
}
