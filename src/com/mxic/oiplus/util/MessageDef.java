package com.mxic.oiplus.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MessageDef {

  public static final String SUCCESS = "Success!!" ;
  public static final String FAILURE = "Failure!!" ;
  public static final String WS = "WS";
  public static final String FT = "FT";
  public static final String CYC = "CYC";
  public static final String Bin_Data = "View Bin Data";
  public static final String Bin_QC = "View QC Bin Data";
  public static final String Fail_Count = "View Fail Count Bin";
  public static final String Datalog_Bin = "View Datalog Bin";
  public static final String WS_SBA = "View WS SBA Data";
  public static final String Test_Number = "View TEST Number";
  public static final String FT_Smart = "View FT Smart Bin Data";
  public static final String WS_Smart = "View WS Smart Bin Data";
  public static final String FT_SPC = "View FT SPC Bin Data";

  public static String getWSFTCYC(String facility){
    if(facility.equals("0")){
      return WS;
    }else if(facility.equals("1")){
      return FT;
    }else{
      return CYC;
    }
  }
  public static String getDistinctTitle(String action_type, String test_type){
    if(action_type.equals("dist_bin_data") || action_type.equals("bin_data")){
      return Bin_Data;
    }else if(action_type.equals("dist_bin_eng") || action_type.equals("bin_eng")){
      if(test_type.equals("FC")){
        return Fail_Count;
      }else if(test_type.equals("DL")){
        return Datalog_Bin;
      }else if(test_type.equals("SBA")){
        return WS_SBA;
      }else if(test_type.equals("TN")){
        return Test_Number;
      }else if(test_type.equals("SMART")){
        return WS_Smart;
      }else{
        return null;
      }
    }else if(action_type.equals("dist_smartft_data")){
      return FT_Smart;
    }else if(action_type.equals("dist_bin_qc_data") || action_type.equals("bin_qc")){
      return Bin_QC;
    }else if(action_type.equals("dist_ftpc_data") || action_type.equals("ftpc_data")){
      return FT_SPC;
    }else{
      return null;
    }
  }
  public static String returnMessage(int flag){
    if(flag == 0){
      return FAILURE;
    }else if(flag == 100){
      return "";
    }else{
      return SUCCESS;
    }
  }
  public static String returnMessage(boolean flag){
    if(flag){
      return SUCCESS;
    }else{
      return FAILURE;
    }
  }
  public static boolean checkSqlFlag(int flag){
    if(flag == 0){
      return false;
    }else{
      return true;
    }
  }
  public MessageDef() {
  }
}
