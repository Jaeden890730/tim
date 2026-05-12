package com.mxic.oiplus.oimaintain;

import java.io.*;
import java.util.Vector;

import javax.servlet.http.*;

import org.apache.struts.action.*;

public class WIPLotsBean {
  String lotid;
  String ipn;
  String prodbody;
  String options;
  String prodgroup;
  String route;
  String lotstatus1;
  String hotlotflag;
  String lotowner;
  String waferqty;
  String chipqty;
  String steps;
  String stage;
  String valdata1;
  String valdata2;
  String valdata3;
  String saprwno;

  public void setLotid(String lotid) {
    this.lotid = lotid;
  }

  public String getLotid() {
    return lotid;
  }

  public void setIpn(String ipn) {
    this.ipn = ipn;
  }

  public String getIpn() {
    return ipn;
  }

  public void setProdbody(String prodbody) {
    this.prodbody = prodbody;
  }

  public String getProdbody() {
    return prodbody;
  }

  public void setOptions(String options) {
    this.options = options;
  }

  public String getOptions() {
    return options;
  }

  public void setProdgroup(String prodgroup) {
    this.prodgroup = prodgroup;
  }

  public String getProdgroup() {
    return prodgroup;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public String getRoute() {
    return route;
  }

  public void setLotstatus1(String lotstatus1) {
    this.lotstatus1 = lotstatus1;
  }

  public String getLotstatus1() {
    return lotstatus1;
  }

  public void setHotlotflag(String hotlotflag) {
    this.hotlotflag = hotlotflag;
  }

  public String getHotlotflag() {
    return hotlotflag;
  }

  public void setLotowner(String lotowner) {
    this.lotowner = lotowner;
  }

  public String getLotowner() {
    return lotowner;
  }

  public void setWaferqty(String waferqty) {
    this.waferqty = waferqty;
  }

  public String getWaferqty() {
    return waferqty;
  }

  public void setChipqty(String chipqty) {
    this.chipqty = chipqty;
  }

  public String getChipqty() {
    return chipqty;
  }

  public void setSteps(String steps) {
    this.steps = steps;
  }

  public String getSteps() {
    return steps;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getStage() {
    return stage;
  }

  public void setValdata1(String valdata1) {
    this.valdata1 = valdata1;
  }

  public String getValdata1() {
    return valdata1;
  }

  public void setValdata2(String valdata2) {
    this.valdata2 = valdata2;
  }

  public String getValdata2() {
    return valdata2;
  }

  public void setValdata3(String valdata3) {
    this.valdata3 = valdata3;
  }

  public String getValdata3() {
    return valdata3;
  }

  public void setSaprwno(String saprwno) {
    this.saprwno = saprwno;
  }

  public String getSaprwno() {
    return saprwno;
  }

}
