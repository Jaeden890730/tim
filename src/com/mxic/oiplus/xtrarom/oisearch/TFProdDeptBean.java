package com.mxic.oiplus.xtrarom.oisearch;

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
public class TFProdDeptBean {
  public TFProdDeptBean() {
  }

  private String product_body;
  private String department_id;
  private String creator;

  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public String getProduct_body() {
    return product_body;
  }

  public void setDepartment_id(String department_id) {
    this.department_id = department_id;
  }

  public String getDepartment_id() {
    return department_id;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public String getCreator() {
    return creator;
  }
}
