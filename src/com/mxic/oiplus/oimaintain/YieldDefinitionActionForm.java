package com.mxic.oiplus.oimaintain;

import java.io.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;

public class YieldDefinitionActionForm extends ActionForm {

  private FormFile filename;
  private String listControl;
  private int sid;
  private int yid;
  private int seq;
  private String product_code;
  private String test_mode;
  private String auto_ship;
  private String hold_pe;
  private String hold_bin;
  private String hold_bin_cri;
  private String auto_scrap;
  private String stop;
  private String mrb;
  private String sampling_yield;
  private String notes;
  private String pd_body;
  private String brand;
  private String version;

// for comparison
  private String type; //remove/insert/old, new
  private int product_code_flag;
  private int test_mode_flag;
  private int auto_ship_flag;
  private int hold_pe_flag;
  private int hold_bin_flag;
  private int hold_bin_cri_flag;
  private int auto_scrap_flag;
  private int stop_flag;
  private int mrb_flag;
  private int sampling_yield_flag;
  private int notes_flag;

  public String getListControl() {
    return listControl;
  }

  public void setListControl(String listControl) {
    this.listControl = listControl;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setSid(int sid) {
    this.sid = sid;
  }

  public int getSid() {
    return sid;
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

  private void writeObject(ObjectOutputStream oos) throws IOException {
    oos.defaultWriteObject();
  }
  private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
    ois.defaultReadObject();
  }
  public String getAuto_scrap() {
    return auto_scrap;
  }
  public void setAuto_scrap(String auto_scrap) {
    this.auto_scrap = auto_scrap;
  }
  public String getAuto_ship() {
    return auto_ship;
  }
  public String getHold_bin() {
    return hold_bin;
  }
  public String getHold_bin_cri() {
    return hold_bin_cri;
  }
  public String getHold_pe() {
    return hold_pe;
  }
  public String getMrb() {
    return mrb;
  }
  public String getNotes() {
    return notes;
  }
  public String getProduct_code() {
    return product_code;
  }
  public String getSampling_yield() {
    return sampling_yield;
  }
  public int getSeq() {
    return seq;
  }
  public String getStop() {
    return stop;
  }
  public String getTest_mode() {
    return test_mode;
  }
  public int getYid() {
    return yid;
  }
  public void setYid(int yid) {
    this.yid = yid;
  }
  public void setTest_mode(String test_mode) {
    this.test_mode = test_mode;
  }
  public void setAuto_ship(String auto_ship) {
    this.auto_ship = auto_ship;
  }
  public void setHold_bin(String hold_bin) {
    this.hold_bin = hold_bin;
  }
  public void setHold_bin_cri(String hold_bin_cri) {
    this.hold_bin_cri = hold_bin_cri;
  }
  public void setHold_pe(String hold_pe) {
    this.hold_pe = hold_pe;
  }
  public void setMrb(String mrb) {
    this.mrb = mrb;
  }
  public void setNotes(String notes) {
    this.notes = notes;
  }
  public void setProduct_code(String product_code) {
    this.product_code = product_code;
  }
  public void setSampling_yield(String sampling_yield) {
    this.sampling_yield = sampling_yield;
  }
  public void setSeq(int seq) {
    this.seq = seq;
  }
  public void setStop(String stop) {
    this.stop = stop;
  }
  public String getPd_body() {
    return pd_body;
  }
  public void setPd_body(String pd_body) {
    this.pd_body = pd_body;
  }
  public String getBrand() {
    return brand;
  }
  public void setBrand(String brand) {
    this.brand = brand;
  }
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }
  public int getAuto_scrap_flag() {
    return auto_scrap_flag;
  }
  public void setAuto_scrap_flag(int auto_scrap_flag) {
    this.auto_scrap_flag = auto_scrap_flag;
  }
  public void setAuto_ship_flag(int auto_ship_flag) {
    this.auto_ship_flag = auto_ship_flag;
  }
  public int getAuto_ship_flag() {
    return auto_ship_flag;
  }
  public void setHold_bin_cri_flag(int hold_bin_cri_flag) {
    this.hold_bin_cri_flag = hold_bin_cri_flag;
  }
  public void setHold_bin_flag(int hold_bin_flag) {
    this.hold_bin_flag = hold_bin_flag;
  }
  public int getHold_bin_flag() {
    return hold_bin_flag;
  }
  public int getHold_pe_flag() {
    return hold_pe_flag;
  }
  public void setHold_pe_flag(int hold_pe_flag) {
    this.hold_pe_flag = hold_pe_flag;
  }
  public void setMrb_flag(int mrb_flag) {
    this.mrb_flag = mrb_flag;
  }
  public int getMrb_flag() {
    return mrb_flag;
  }
  public int getNotes_flag() {
    return notes_flag;
  }
  public void setNotes_flag(int notes_flag) {
    this.notes_flag = notes_flag;
  }
  public void setProduct_code_flag(int product_code_flag) {
    this.product_code_flag = product_code_flag;
  }
  public int getProduct_code_flag() {
    return product_code_flag;
  }
  public int getSampling_yield_flag() {
    return sampling_yield_flag;
  }
  public void setSampling_yield_flag(int sampling_yield_flag) {
    this.sampling_yield_flag = sampling_yield_flag;
  }
  public void setStop_flag(int stop_flag) {
    this.stop_flag = stop_flag;
  }
  public int getStop_flag() {
    return stop_flag;
  }
  public int getTest_mode_flag() {
    return test_mode_flag;
  }
  public void setTest_mode_flag(int test_mode_flag) {
    this.test_mode_flag = test_mode_flag;
  }
  public String getAuto_scrap_color() {
    if (auto_scrap_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getAuto_ship_color() {
    if (auto_ship_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getHold_bin_color() {
    if (hold_bin_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getHold_bin_cri_color() {
    if (hold_bin_cri_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public int getHold_bin_cri_flag() {
    return hold_bin_cri_flag;
  }
  public String getHold_pe_color() {
    if (hold_pe_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getMrb_color() {
    if (mrb_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getNotes_color() {
    if (notes_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getProduct_code_color() {
    if (product_code_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getSampling_yield_color() {
    if (sampling_yield_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getStop_color() {
    if (stop_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public String getTest_mode_color() {
    if (test_mode_flag==1){
      return "#FFDDFF";
    } else {
      return "#CCEEFF";
    }
  }
  public FormFile getFilename() {
    return filename;
  }
  public void setFilename(FormFile filename) {
    this.filename = filename;
  }
}
