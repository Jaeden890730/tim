package com.mxic.oiplus.util;

import java.util.*;

public class PROC {

  public PROC() {
  }
  public static void main(String[] arg) {
      PROC proc = new PROC();
      String assy = "0EERT;1DERF;2FGTY;0EDFT;3FRDE;3FBGY";
      proc.AssyTransfToProc(assy);
      TDSLogger.println("cable: " + proc.getCable());
      TDSLogger.println("contact: " + proc.getContact());
      String[] load = proc.getLoad_brd();
      String[] prb = proc.getPrb_card();
      for(int i=0;i<load.length;i++){
	  TDSLogger.println("load:" + load[i]);
      }
      for(int i=0;i<prb.length;i++){
	  TDSLogger.println("prb:" + prb[i]);
      }
  }
  private String[] load_brd;
  private String[] prb_card;
  private String cable;
  private String contact;
  private final String load_brd_value = "0";
  private final String prb_card_value = "3";
  private final String cable_value = "1";
  private final String contact_value = "2";

  public String[] getLoad_brd() {
    return load_brd;
  }
  public void setLoad_brd(String[] load_brd) {
    this.load_brd = load_brd;
  }
  public void setPrb_card(String[] prb_card) {
    this.prb_card = prb_card;
  }
  public String[] getPrb_card() {
    return prb_card;
  }
  public void setCable(String cable) {
    this.cable = cable;
  }
  public String getCable() {
    return cable;
  }
  public void setContact(String contact) {
    this.contact = contact;
  }
  public String getContact() {
    return contact;
  }

  public void AssyTransfToProc(String accessory){
      if(accessory != null && !accessory.equals("")){
	  StringTokenizer st = new StringTokenizer(accessory,";");
	  String value = "";
	  String key = "";
	  String data = "";
	  ArrayList load_brd_tmp = new ArrayList();
	  ArrayList prb_card_tmp = new ArrayList();

	   while (st.hasMoreTokens()) {
	       data = st.nextToken();
	       key = data.substring(0,1);
	       value = data.substring(1);
	       if(key.equals(load_brd_value)){
		  load_brd_tmp.add(value);
	       }else if(key.equals(cable_value)){
		  cable = value;
	       }else if(key.equals(contact_value)){
		  contact = value;
	       }else if(key.equals(prb_card_value)){
		  prb_card_tmp.add(value);
	       }
	  }

	  load_brd = ((String[]) load_brd_tmp.toArray(new String[0]));
	  prb_card = ((String[]) prb_card_tmp.toArray(new String[0]));
      }
  }
}
