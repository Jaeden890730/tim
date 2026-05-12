package com.mxic.oiplus.oisearch;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.sql.Date;

import org.apache.struts.action.*;

import com.mxic.oiplus.au.AUDAO;

public class TFRouteDefinitionBeanAF extends ActionForm{
  public TFRouteDefinitionBeanAF() {
  }

  private String product_body;
  private String ws_route;
  private String ft_route;
  private String ws_route_add;
  private String ft_route_add;
  private String route_option;
  private String route_option_description;
  private String updater;
	private String lastupdate;
	private String form_no;
	private String action;

  public void setProduct_body(String product_body) {
    this.product_body= product_body;
  }

  public String getProduct_body() {
    return product_body;
  }

  public void setWs_route_add(String ws_route_add) {
    this.ws_route_add= ws_route_add;
  }

  public String getWs_route_add() {
    return ws_route_add;
  }

  public void setWs_route(String ws_route) {
    this.ws_route= ws_route;
  }

  public String getWs_route() {
    return ws_route;
  }

  public void setFt_route(String ft_route) {
    this.ft_route= ft_route;
  }

  public String getFt_route() {
    return ft_route;
  }

  public void setFt_route_add(String ft_route_add) {
    this.ft_route_add= ft_route_add;
  }

  public String getFt_route_add() {
    return ft_route_add;
  }

  public void setRoute_option(String route_option) {
    this.route_option= route_option;
  }

  public String getRoute_option() {
    return route_option;
  }

  public void setRoute_option_description(String route_option_description) {
    this.route_option_description= route_option_description;
  }

  public String getRoute_option_description() {
    return route_option_description;
  }

  public void setUpdater(String updater) {
    this.updater= updater;
  }

  public String getUpdater() {
    return updater;
  }

	public boolean getAuthorityUser() {
		if (AUDAO.isITAdmin(getUpdater())) {
			return true;
		} else {
			return false;
		}
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getForm_no() {
		return form_no;
	}

	public void setForm_no(String form_no) {
		this.form_no = form_no;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
