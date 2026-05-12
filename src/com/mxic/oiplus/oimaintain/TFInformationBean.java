package com.mxic.oiplus.oimaintain;

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
public class TFInformationBean {
  public TFInformationBean() {
  }

  private String product_body;
  private String process_type;
  private String product_type;


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
   * @return Returns the process_type.
   */
  public String getProcess_type() {
	  return process_type;
  }

  /**
   * @param  The process_type to set.
   */
  public void setProcess_type(String process_type) {
	  this.process_type = process_type;
  }



  public void setProduct_body(String product_body) {
    this.product_body = product_body;
  }

  public String getProduct_body() {
    return product_body;
  }

}
