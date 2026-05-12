package com.mxic.oiplus.oimaintain;

import java.io.*;
import java.util.Vector;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class WIPActionForm extends ActionForm {
  private int sid;
  private String option_list;
  private String ctrl_type;
  private String pgname1;
  private String pgname2;
  private String pgname3;
  private String pgname4;
  private String pgname5;
  private String p1;
  private String p2;
  private String p3;
  private String p4;
  private String p5;
  private String tag;

  public void setSid(int sid) {
    this.sid = sid;
  }

  public int getSid() {
    return sid;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public String getOption_list() {
    return option_list;
  }

  public void setOption_list(String option_list) {
    this.option_list = option_list;
  }

  public String getCtrl_type() {
    return ctrl_type;
  }

  public void setCtrl_type(String ctrl_type) {
    this.ctrl_type = ctrl_type;
  }

  public String getPgname1() {
    return pgname1;
  }

  public void setPgname1(String pgname1) {
    this.pgname1 = pgname1;
  }

  public String getPgname2() {
    return pgname2;
  }

  public void setPgname2(String pgname2) {
    this.pgname2 = pgname2;
  }

  public String getPgname3() {
    return pgname3;
  }

  public void setPgname3(String pgname3) {
    this.pgname3 = pgname3;
  }

  public String getPgname4() {
    return pgname4;
  }

  public void setPgname4(String pgname4) {
    this.pgname4 = pgname4;
  }

  public String getPgname5() {
    return pgname5;
  }

  public void setPgname5(String pgname5) {
    this.pgname5 = pgname5;
  }

  public String getP1() {
    return p1;
  }

  public void setP1(String p1) {
    this.p1 = p1;
  }

  public String getP2() {
    return p2;
  }

  public void setP2(String p2) {
    this.p2 = p2;
  }

  public String getP3() {
    return p3;
  }

  public void setP3(String p3) {
    this.p3 = p3;
  }

  public String getP4() {
    return p4;
  }

  public void setP4(String p4) {
    this.p4 = p4;
  }

  public String getP5() {
    return p5;
  }

  public void setP5(String p5) {
    this.p5 = p5;
  }

  public String getP1color(){
    if (p1 == null){
            return "#FFDDFF";
    }else{
            return "#CCEEFF";
    }
  }

  public String getP2color(){
    if (p2 == null){
            return "#FFDDFF";
    }else{
            return "#CCEEFF";
    }
  }

  public String getP3color(){
    if (p3 == null){
            return "#FFDDFF";
    }else{
            return "#CCEEFF";
    }
  }

  public String getP4color(){
    if (p4 == null){
            return "#FFDDFF";
    }else{
            return "#CCEEFF";
    }
  }

  public String getP5color(){
    if (p5 == null){
            return "#FFDDFF";
    }else{
            return "#CCEEFF";
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

