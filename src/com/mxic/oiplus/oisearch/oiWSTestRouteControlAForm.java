package com.mxic.oiplus.oisearch;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class oiWSTestRouteControlAForm extends ActionForm {
  private String listControl;
  private String ra_select;
  private String upload;
  private String remark;
  private String route_cat;
  private String chart_log_time;
  private String show_testflow;
  private String file_name_testflow;
  private String file_name_stdexcflow;
  private String search_routename;
  private String file_type;
  private String file_list;

  public String getFile_type() {
	return file_type;
  }

  public void setFile_type(String file_type) {
	this.file_type = file_type;
  }

  public String getFile_list() {
	return file_list;
  }

  public void setFile_list(String file_list) {
	this.file_list = file_list;
  }

  public String getListControl() {
    return listControl;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public void setRa_select(String ra_select) {
    this.ra_select = ra_select;
  }

  public String getRa_select() {
    return ra_select;
  }

  public void setUpload(String upload) {
    this.upload = upload;
  }

  public String getUpload() {
    return upload;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getRemark() {
    return remark;
  }
  public void setRoute_cat(String route_cat) {
    this.route_cat = route_cat;
  }

  public String getRoute_cat() {
    return route_cat;
  }
  public void setChart_log_time(String chart_log_time) {
    this.chart_log_time = chart_log_time;
  }

  public String getChart_log_time() {
    return chart_log_time;
  }

  public String getDisplay_testflow() {
    if (show_testflow.equals("show")) {
      return "";
    } else {
      return "display:none";
    }
  }

  public String getFile_name_stdexcflow() {
	return file_name_stdexcflow;
  }

  public void setFile_name_stdexcflow(String file_name_stdexcflow) {
	this.file_name_stdexcflow = file_name_stdexcflow;
  }

  public void setFile_name_testflow(String file_name_testflow) {
    this.file_name_testflow = file_name_testflow;
  }

  public String getFile_name_testflow() {
    return file_name_testflow;
  }

  public void setShow_testflow(String show_testflow) {
    this.show_testflow = show_testflow;
  }

  public String getShow_testflow() {
    return show_testflow;
  }

  public void setSearch_routename(String search_routename) {
    this.search_routename = search_routename;
  }

  public String getSearch_routename() {
    return search_routename;
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
