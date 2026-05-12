package com.mxic.oiplus.pdf;

import java.sql.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

public class PDFdiffService {
  public PDFdiffService() {
  }

  public static String getTemperature(String temperature) {
    if (temperature == null)
      return "";
    if (!temperature.equals("ROOM TEMP") && !temperature.trim().equals(""))
      return temperature + "℃";
    return temperature;
  }
  public static String getTemperatureSplit(String temperature) {
	    if (temperature == null)
	      return "";
	    String result = "";
	    String[] tmp = temperature.split(";");
	    for(int i=0;i<tmp.length;i++){
	    	if (!tmp[i].equals("NA"))
	    	   result = result + tmp[i] + "℃\n";
	    	else
	    	   result = result + tmp[i] + "\n";
	    }
	    return result;
  }
  public static String getTemperatureAEBgrade(String temperature, String flag) {
	    if (temperature == null)
	      return "";
	    String result = "NA℃";
	    String[] tmp = temperature.split(";");
	    for(int i=0;i<tmp.length;i++){
	    	if(tmp[i].startsWith(flag)){
	    		  result = tmp[i].substring(2) + "℃";	
	        }
	    }
	    return result;
}
  public static String getStringDataSplit(String data) {
	    if (data == null)
	      return "";
	    String result = "";
	    String[] tmp = data.split(";");
	    for(int i=0;i<tmp.length;i++){
	    	if (!tmp[i].equals("NA"))
	    	   result = result + tmp[i] + "\n";
	    	else
		       result = result + tmp[i] + "\n";
	    }
	    return result;
}
  
  public static PdfPTable ComparePDR(Connection conn, PdfPTable tabExtend,
                                     Font F,
                                     Font RedFont,
                                     String sid,
                                     String sid2,
                                     String status,
                                     int flag) {

    EditiionCompareActionForm[] list =
        EditiionCompareService.ComparePDR(conn, sid, sid2, status, flag, 1);
    String temperature = null;
    String rework_step = null;

    for (int i = 0; i < list.length; i++) {
      EditiionCompareActionForm bean = list[i];
      /*if (flag == 0)
        tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
      else
        tabExtend.addCell(new Phrase(new Chunk("NEW", F)));

      tabExtend.addCell(new Phrase(new Chunk(bean.getRoute_name(), F)));
      tabExtend.addCell(new Phrase(new Chunk(String.valueOf(bean.getStep_seq()), F)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getStep_name(), F)));
      if (bean.getTest_time() == 0 && bean.getTime_unit() == null) {
        tabExtend.addCell(new Phrase(new Chunk(" ", F)));
      } else if (bean.getTest_time() == 0 && bean.getTime_unit() != null){
        tabExtend.addCell(new Phrase(new Chunk(bean.getTime_unit(), F)));
      } else if (bean.getTest_time() != 0 && bean.getTime_unit() == null){
        tabExtend.addCell(new Phrase(new Chunk(String.valueOf(bean.getTest_time()), F)));
      } else {
        tabExtend.addCell(new Phrase(new Chunk(bean.getTest_time() + " " + bean.getTime_unit(), F)));
      }

      temperature = getTemperature(NullConvert(bean.getTemperature()));
      tabExtend.addCell(new Phrase(new Chunk(temperature, F)));
      
      if(bean.getSampling_test()!=null && bean.getSampling_test().equals("Y"))
          tabExtend.addCell(new Phrase(new Chunk("抽測", F)));
      else
    	  tabExtend.addCell(new Phrase(new Chunk(" ", F)));
      
      rework_step = NullConvert(bean.getRework_step());
      tabExtend.addCell(new Phrase(new Chunk(rework_step, F)));

      if (bean.getTest_time2() == 0 && bean.getTime_unit2() == null) {
          tabExtend.addCell(new Phrase(new Chunk(" ", F)));
      } else {
          tabExtend.addCell(new Phrase(new Chunk(bean.getTest_time2() + " " + bean.getTime_unit2(), F)));
      }
      
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()), F)));
      */
        /*if (flag == 0)
          tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
        else
          tabExtend.addCell(new Phrase(new Chunk("NEW", F)));*/
        tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), F)));

        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRoute_name()),(bean.getRoute_name_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(String.valueOf(bean.getStep_seq())),(bean.getStep_seq_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getStep_name()),(bean.getStep_name_flag()==0?F:RedFont))));
        if (bean.getTest_time() == 0 && bean.getTime_unit() == null) {
          tabExtend.addCell(new Phrase(new Chunk("-" ,((bean.getTest_time_flag()==0 && bean.getTime_unit_flag()==0)?F:RedFont))));
        } else if (bean.getTest_time() == 0 && bean.getTime_unit() != null){
          tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getTime_unit()),(bean.getTime_unit_flag()==0?F:RedFont))));
        } else if (bean.getTest_time() != 0 && bean.getTime_unit() == null){
          tabExtend.addCell(new Phrase(new Chunk(NullConvert(String.valueOf(bean.getTest_time())),(bean.getTest_time_flag()==0?F:RedFont))));
        } else {
          tabExtend.addCell(new Phrase(new Chunk(NullConvert(String.valueOf(bean.getTest_time())) + " " + bean.getTime_unit() ,((bean.getTest_time_flag()==0 && bean.getTime_unit_flag()==0)?F:RedFont))));
        }

        temperature = getTemperature(NullConvert(bean.getTemperature()));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(temperature),(bean.getTemperature_flag()==0?F:RedFont))));
        
        if(bean.getSampling_test()!=null && bean.getSampling_test().equals("Y"))
          tabExtend.addCell(new Phrase(new Chunk("抽測",(bean.getSampling_test_flag()==0?F:RedFont))));
        else
      	  tabExtend.addCell(new Phrase(new Chunk(" - ", (bean.getSampling_test_flag()==0?F:RedFont))));
        
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getSampling_cond()), (bean.getSampling_cond_flag()==0?F:RedFont))));
        rework_step = NullConvert(bean.getRework_step());
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(rework_step),(bean.getRework_step_flag()==0?F:RedFont))));

        if (bean.getTest_time2() == 0 && bean.getTime_unit2() == null) {
            tabExtend.addCell(new Phrase(new Chunk("-", (bean.getTime_unit2_flag()==0?F:RedFont))));
        } else {
            tabExtend.addCell(new Phrase(new Chunk(NullConvert(String.valueOf(bean.getTest_time2())) + " " + bean.getTime_unit2() ,((bean.getTest_time2_flag()==0 && bean.getTime_unit2_flag()==0)?F:RedFont))));
        }
        
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()),(bean.getRemark_flag()==0?F:RedFont))));
    }
    return tabExtend;
  }
  
  public static PdfPTable ComparePWL(Connection conn, PdfPTable tabExtend,
          Font F,
          Font RedFont,
          String sid,
          String sid2,
          String status,
          int flag) {

	EditiionCompareActionForm[] list =
	EditiionCompareService.ComparePWL(conn, sid, sid2, status, flag, 1);
	
	for (int i = 0; i < list.length; i++) {
		EditiionCompareActionForm bean = list[i];
		/*if (flag == 0)
			tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), F)));
		else
			tabExtend.addCell(new Phrase(new Chunk("NEW", F)));*/
		tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), F)));
		
		//tabExtend.addCell(new Phrase(new Chunk(bean.getWafer_level(), F)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWafer_level()),(bean.getWafer_level_flag()==0?F:RedFont))));
		//tabExtend.addCell(new Phrase(new Chunk(String.valueOf(bean.getWafer_brand()), F)));
		//tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getBiztype()), F)));
		//tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWafer_grade()), F)));
		//tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getApply_type()), F)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRevise_priority()),(bean.getRevise_priority_flag()==0?F:RedFont))));
		//tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRevise_priority()), F)));
	}
	return tabExtend;
}

  public static PdfPTable CompareBom(Connection conn, PdfPTable
                                     tabExtend,
                                     Font NormalFont,
									 Font RedFont,
                                     String sid,
                                     String sid2,
                                     String status,
                                     int flag) {
    EditiionCompareActionForm[] list =
        EditiionCompareService.CompareBom(conn, sid, sid2, status, flag, 1);
    for (int i = 0; i < list.length; i++) {
      EditiionCompareActionForm bean = list[i];
      /*if (flag == 0)
        tabExtend.addCell(new Phrase(new Chunk("OLD", NormalFont)));
      else
        tabExtend.addCell(new Phrase(new Chunk("NEW", NormalFont)));

      tabExtend.addCell(new Phrase(new Chunk(bean.getBeoption(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getFgwithcode(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getPincount(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getPkgtype(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getFt_route_code(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFtroute()), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getComment()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getMaskopt(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getDbwithcode(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getSortroutecode(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsroute()), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWscomment()),NormalFont)));
      */
        /*if (flag == 0)
          tabExtend.addCell(new Phrase(new Chunk("OLD", NormalFont)));
        else
          tabExtend.addCell(new Phrase(new Chunk("NEW", NormalFont)));*/
        tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), NormalFont)));

        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getBeoption()),(bean.getBackend_option_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFgwithcode()),(bean.getFg_with_code_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getPincount()),(bean.getPin_count_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getPkgtype()),(bean.getPackage_type_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_code()),(bean.getFt_route_code_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFtroute()),(bean.getFt_route_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add()),(bean.getFt_route_add_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add2()),(bean.getFt_route_add_flag2()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add3()),(bean.getFt_route_add_flag3()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_special_control()), (bean.getFt_special_control_flag() == 0 ? NormalFont : RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getComment()),(bean.getTf_comment_flag()==0?NormalFont:RedFont))));
        if(bean.getBrand().equals("MX")){
        	tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getQuality_level()),(bean.getQuality_level_flag()==0?NormalFont:RedFont))));
        	tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getQuality_level_comment()),(bean.getQuality_level_comment_flag()==0?NormalFont:RedFont))));
        }	
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMcp_flag()),(bean.getMcp_flag_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMaskopt()),(bean.getMask_option_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getDbwithcode()),(bean.getDb_with_code_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getSortroutecode()),(bean.getSort_route_code_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsroute()),(bean.getWs_route_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute()),(bean.getWs_route_add_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWs_special_control()), (bean.getWs_special_control_flag() == 0 ? NormalFont : RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWscomment()),(bean.getTf_ws_comment_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getAvi()), (bean.getAvi_flag() == 0 ? NormalFont : RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getInk()), (bean.getInk_flag() == 0 ? NormalFont : RedFont))));
        
        
      

      /*PdfPCell cmt = null;
      cmt = new PdfPCell(new Phrase(NullConvert(bean.getComment()), NormalFont));
      cmt.setColspan(4);
      tabExtend.addCell(cmt);*/
    }
    return tabExtend;
  }

  public static PdfPTable CompareBomMcp(Connection conn, PdfPTable
          tabExtend,
          Font NormalFont,
			 Font RedFont,
          String sid,
          String sid2,
          String status,
          int flag) {
	EditiionCompareActionForm[] list = EditiionCompareService.CompareBomMcp(conn, sid, sid2, status, flag, 1);	
	for (int i = 0; i < list.length; i++) {
		EditiionCompareActionForm bean = list[i];
		/*if (flag == 0)
		tabExtend.addCell(new Phrase(new Chunk("OLD", NormalFont)));
		else
		tabExtend.addCell(new Phrase(new Chunk("NEW", NormalFont)));
		
		tabExtend.addCell(new Phrase(new Chunk(bean.getBeoption(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(bean.getFgwithcode(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(bean.getPincount(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(bean.getPkgtype(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(bean.getFt_route_code(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFtroute()), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add()),NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getComment()),NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(bean.getMaskopt(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(bean.getDbwithcode(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(bean.getSortroutecode(), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsroute()), NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute()),NormalFont)));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWscomment()),NormalFont)));
		*/
		/*if (flag == 0)
		tabExtend.addCell(new Phrase(new Chunk("OLD", NormalFont)));
		else
		tabExtend.addCell(new Phrase(new Chunk("NEW", NormalFont)));*/
		tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), NormalFont)));
		
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getBeoption()),(bean.getBackend_option_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFgwithcode()),(bean.getFg_with_code_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getPincount()),(bean.getPin_count_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getPkgtype()),(bean.getPackage_type_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_code()),(bean.getFt_route_code_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFtroute()),(bean.getFt_route_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add()),(bean.getFt_route_add_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add2()),(bean.getFt_route_add_flag2()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add3()),(bean.getFt_route_add_flag3()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_special_control()), (bean.getFt_special_control_flag() == 0 ? NormalFont : RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getComment()),(bean.getTf_comment_flag()==0?NormalFont:RedFont))));
		if(bean.getBrand().equals("MX")){
			tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getQuality_level()),(bean.getQuality_level_flag()==0?NormalFont:RedFont))));
			tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getQuality_level_comment()),(bean.getQuality_level_comment_flag()==0?NormalFont:RedFont))));
		}	
		//tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMaskopt()),(bean.getMask_option_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getComponent_no()),(bean.getComponent_no_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getCom_prod_body()),(bean.getCom_prod_body_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getCom_mask_option()),(bean.getCom_mask_option_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getCom_backend_option()),(bean.getCom_backend_option_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getDbwithcode()),(bean.getDb_with_code_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getSortroutecode()),(bean.getSort_route_code_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsroute()),(bean.getWs_route_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute()),(bean.getWs_route_add_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWs_special_control()), (bean.getWs_special_control_flag() == 0 ? NormalFont : RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWscomment()),(bean.getTf_ws_comment_flag()==0?NormalFont:RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getAvi()), (bean.getAvi_flag() == 0 ? NormalFont : RedFont))));
		tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getInk()), (bean.getInk_flag() == 0 ? NormalFont : RedFont))));
		
		
		
		
		/*PdfPCell cmt = null;
		cmt = new PdfPCell(new Phrase(NullConvert(bean.getComment()), NormalFont));
		cmt.setColspan(4);
		tabExtend.addCell(cmt);*/
	}
	return tabExtend;
	}  
  
  public static PdfPTable CompareYield(Connection conn, PdfPTable tabExtend,
		  								Font NormalFont,
		  								Font RedFont,
		  								String sid,
		  								String sid2,
		  								String status) {

	  YieldDefinitionActionForm[] list =
		  EditiionCompareService.CompareYield(conn, sid, sid2, status, 1);
      
	  if (list == null || list.length == 0)
		  return null;
	  
	  tabExtend.addCell(new Phrase(new Chunk("Diff",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Product Code",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Test Mode",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Auto Ship",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Hold PE",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Hold BIN",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Hold Bin Criteria",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Auto Scrap",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Stop",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("OOC",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Sampling Yield",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Notes",NormalFont)));
      for (int i = 0; i < list.length; i++) {
		  //YieldDefinitionActionForm bean = list[i];
		  tabExtend.addCell(new Phrase(new Chunk(list[i].getType(), NormalFont)));
		  tabExtend.addCell(new Phrase(new Chunk(list[i].getProduct_code(),(list[i].getProduct_code_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(list[i].getTest_mode(),(list[i].getTest_mode_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getAuto_ship()),(list[i].getAuto_ship_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getHold_pe()),(list[i].getHold_pe_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getHold_bin()),(list[i].getHold_bin_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getHold_bin_cri()),(list[i].getHold_bin_cri_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getAuto_scrap()),(list[i].getAuto_scrap_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getStop()),(list[i].getStop_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getMrb()),(list[i].getMrb_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getSampling_yield()),(list[i].getSampling_yield_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getNotes()),(list[i].getNotes_flag()==0?NormalFont:RedFont))));
	  }

	  return tabExtend;
  }

  public static PdfPTable CompareYield(Connection conn, PdfPTable tabExtend,
									   Font NormalFont,
									   Font RedFont,
									   String sid,
									   String sid2,
									   String proc_type,
									   String status,
									   String hold_dgrade_flag) {

		YieldDefBean[] list = EditiionCompareService.CompareYield(conn, sid, sid2, proc_type, status, 1, hold_dgrade_flag);
	  String productType = OiMaintainService.getProductType(conn,sid);

	  if (list == null  || list.length == 0)
		  return null;
	  tabExtend.addCell(new Phrase(new Chunk("Diff",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Product Brand",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Prod Level",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Test Mode",NormalFont)));
	  if (proc_type.equals("WS"))
		  tabExtend.addCell(new Phrase(new Chunk("Criteria (By Wafer)",NormalFont)));
	  else
		  tabExtend.addCell(new Phrase(new Chunk("Criteria",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Action",NormalFont)));
		
		
		if (productType != null && productType.equals("NVM")) {
			if (proc_type.equals("FT")) {
				tabExtend.addCell(new Phrase(new Chunk("DGrade Action", NormalFont)));
			}
			if (proc_type.equals("WS") && hold_dgrade_flag.equals("Dgrade")) {
				tabExtend.addCell(new Phrase(new Chunk("By Lot Hold", NormalFont)));
				tabExtend.addCell(new Phrase(new Chunk("Route Name", NormalFont)));
				tabExtend.addCell(new Phrase(new Chunk("Start Step", NormalFont)));
			}
		} else if (productType != null && productType.equals("XROM")) {
			tabExtend.addCell(new Phrase(new Chunk("Change IPN", NormalFont)));
			tabExtend.addCell(new Phrase(new Chunk("Route Name", NormalFont)));
			tabExtend.addCell(new Phrase(new Chunk("Start Step", NormalFont)));
		} else if (productType != null && productType.equals("MROM")) {
			if (proc_type.equals("WS")) {
				tabExtend.addCell(new Phrase(new Chunk("Change IPN", NormalFont)));
			}
		}
		
		
		
		/*if (proc_type.equals("FT") || (proc_type.equals("WS") && (productType != null && productType.equals("NVM")))) {
	      if(proc_type.equals("FT") &&  (productType != null && productType.equals("NVM")))
	          tabExtend.addCell(new Phrase(new Chunk("DGrade Action",NormalFont)));  
	  }else {
	  tabExtend.addCell(new Phrase(new Chunk("Change IPN",NormalFont)));
	  }
	  if(proc_type.equals("WS") && productType.equals("NVM") && hold_dgrade_flag.equals("Dgrade")){
	      tabExtend.addCell(new Phrase(new Chunk("By Lot Hold",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Route Name",NormalFont)));
	  tabExtend.addCell(new Phrase(new Chunk("Start Step",NormalFont)));
		} else {
		}*/
	  
	  tabExtend.addCell(new Phrase(new Chunk("Remark",NormalFont)));
	  for (int i = 0; i < list.length; i++) {
		  tabExtend.addCell(new Phrase(new Chunk(list[i].getType(), NormalFont)));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getProduct_code()),(list[i].getProduct_code_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getBrands()),(list[i].getBrand_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getTest_mode()),(list[i].getTest_mode_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getFull_items()),(list[i].getItem_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getAction()),(list[i].getAction_flag()==0?NormalFont:RedFont))));
		  
		  
			if (productType != null && productType.equals("NVM")) {
				if (proc_type.equals("FT")) {
					tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getDg_action()), (list[i].getDg_action_flag() == 0 ? NormalFont : RedFont))));
				}
				if (proc_type.equals("WS") && hold_dgrade_flag.equals("Dgrade")) {
					tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getBy_lot_dg()), (list[i].getBy_lot_dg_flag() == 0 ? NormalFont : RedFont))));
					tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getRoute_name()), (list[i].getRoute_name_flag() == 0 ? NormalFont : RedFont))));
					tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getStart_step()), (list[i].getStart_step_flag() == 0 ? NormalFont : RedFont))));
				}
			} else if (productType != null && productType.equals("XROM")) {

				tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getChange_ipn()), (list[i].getChange_ipn_flag() == 0 ? NormalFont : RedFont))));
				tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getRoute_name()), (list[i].getRoute_name_flag() == 0 ? NormalFont : RedFont))));
				tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getStart_step()), (list[i].getStart_step_flag() == 0 ? NormalFont : RedFont))));
				
			} else if (productType != null && productType.equals("MROM")) {
				if (proc_type.equals("WS")) {
					tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getChange_ipn()), (list[i].getChange_ipn_flag() == 0 ? NormalFont : RedFont))));
				}
			}
			
			
		  
		  /*if(proc_type.equals("FT") || (proc_type.equals("WS") && (productType != null && productType.equals("NVM")))) {
	          if(proc_type.equals("FT") &&  (productType != null && productType.equals("NVM")))
	              tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getDg_action()),(list[i].getDg_action_flag()==0?NormalFont:RedFont))));
	      }else {
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getChange_ipn()),(list[i].getChange_ipn_flag()==0?NormalFont:RedFont))));
	      }
	      if(proc_type.equals("WS")&& productType.equals("NVM") && hold_dgrade_flag.equals("Dgrade")){
	          tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getBy_lot_dg()),(list[i].getBy_lot_dg_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getRoute_name()),(list[i].getRoute_name_flag()==0?NormalFont:RedFont))));
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getStart_step()),(list[i].getStart_step_flag()==0?NormalFont:RedFont))));
	      }
	      else{
	      }*/

		  
		  tabExtend.addCell(new Phrase(new Chunk(StringUtil.NullConvert(list[i].getRemark()),(list[i].getRemark_flag()==0?NormalFont:RedFont))));
	  }

	  return tabExtend;
  }

  public static void CompareWSPDF(Connection conn, 
		  								Document doc,
                                       Font SmallFont,
                                       Font RedFont,
                                       String sid,
                                       String sid2,
                                       String status,
                                       String site) {
    try {
      WSProductRouteDefinitionForm[] list =
          EditiionCompareService.CompareWSPDR(conn, sid, sid2, status, site, 1);
      String productType = OiMaintainService.getProductType(conn,sid);
      OiMaintainStep prodInfo = OiMaintainService.SearchFunction(conn,sid);

      String temperature = null;
      PdfPTable table1 = null;
      Paragraph sec = null;
      String preGroupKey = "";
      String curGroupKey = null;
      String hw_configure_split = null;
      int section = 0;
      for (int i = 0; i < list.length; i++) {
        WSProductRouteDefinitionForm bean = list[i];
        curGroupKey = OiMaintainService.getGroupKey(productType, bean.getProductBody(), prodInfo.getBrand(), bean.getMaskOption(),
	            null, null, bean.getDbWithCode(), bean.getWsRoute(), bean.getWsRouteAdd(), bean.getSales_form(), 0);


        if (!curGroupKey.equals(preGroupKey)) {
          if (section > 0) {
            doc.add(table1);
          }
          section++;
          sec = null;
          sec = new
              Paragraph(new Chunk("1-1-" + section + " Product Group Key - " + curGroupKey, SmallFont));
          sec.setSpacingAfter(5);
          doc.add(sec);
          table1 = null;
          //M200712056,20080130
          if (site == null) {
            float[] widths = {4, 5, 5, 5, 10, 10,5, 10, 8, 10, 15, 15, 13};//20--<15, 23-->18 add 10
            table1 = new PdfPTable(widths);
          } else {
            float[] widths = {4, 5, 5, 5, 10, 10,5, 10, 10, 11, 15, 25};//add 10
            table1 = new PdfPTable(widths);
          }
          table1.setSpacingBefore(0);
          table1.setWidthPercentage(100);
          table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route Type", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Tester", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Site", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Temperature", SmallFont)));
          table1.addCell(new Phrase(new Chunk("HW Configure", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Special Control", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont)));
          
          //M200712056,20080130
          if (site == null)
            table1.addCell(new Phrase(new Chunk("Route Comment", SmallFont)));
          
          preGroupKey = curGroupKey;
        }
        table1.addCell(new Phrase(new Chunk(bean.getTag(), SmallFont)));
    	/*table1.addCell(new Phrase(new Chunk(bean.getRoute_type(), SmallFont)));
        if (bean.getRoute_type().equals("Main"))
            table1.addCell(new Phrase(new Chunk(NullConvert(bean.getWsRoute()), SmallFont)));
        else
        	table1.addCell(new Phrase(new Chunk(NullConvert(bean.getWsRouteAdd()), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getTestMode(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getTester(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getSite(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getProgramName(), SmallFont)));
        temperature = getTemperature(NullConvert(bean.getTemperature()));
        table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
        hw_configure_split = PDFdiffService.getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(hw_configure_split,SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getTfComment()), SmallFont)));
        
        //M200712056,20080130
        if (site == null)
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getTfWSComment()), SmallFont)));
        */
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getRoute_type()),(bean.getRoute_type_flag()==0?SmallFont:RedFont))));
        if (bean.getRoute_type().equals("Main"))
            table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getWsRoute()),(bean.getWs_route_flag()==0?SmallFont:RedFont))));
        else
        	table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getWsRouteAdd()),(bean.getWs_route_add_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTestMode()),(bean.getTest_mode_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTester()),(bean.getTester_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getSite()),(bean.getSite_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPgm_id()),(bean.getPgm_id_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getProgramName()),(bean.getProgram_name_flag()==0?SmallFont:RedFont))));
        temperature = getTemperature(NullConvert(bean.getTemperature()));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(temperature),(bean.getTemperature_flag()==0?SmallFont:RedFont))));
        hw_configure_split = PDFdiffService.getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(hw_configure_split),(bean.getHw_configure_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPgm_special_control()),(bean.getPgm_special_control_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTfComment()),(bean.getTf_comment_flag()==0?SmallFont:RedFont))));
        
        //M200712056,20080130
        if (site == null)
           table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTfWSComment()),(bean.getTf_ws_comment_flag()==0?SmallFont:RedFont))));
        
        
        
      }
      doc.add(table1);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
    }
  }

  public static void CompareFTPDF(Connection conn, Document doc,
                                  Font SmallFont,
                                  Font RedFont,
                                  String sid,
                                  String sid2,
                                  String status,
                                  String site,
                                  String section) {

    try {
      FTProductRouteDefinitionForm[] list =
          EditiionCompareService.CompareFTPDR(conn, sid, sid2, status, site, 1);
      String temperature = null;
      String i_grade_temperature = null,c_grade_temperature = null,w_grade_temperature = null,y_grade_temperature = null,j_grade_temperature = null,
              k_grade_temperature = null,l_grade_temperature = null,n_grade_temperature = null,b_grade_temperature = null,e_grade_temperature = null;

      String preGroupKey = "";
      String curGroupKey = null;
      int subsection = 0;
      PdfPTable table1 = null;
      Paragraph sec = null;
      String hw_configure_split = null;
      String productType = OiMaintainService.getProductType(conn,sid);
      OiMaintainStep prodInfo = OiMaintainService.SearchFunction(conn,sid);

      for (int i = 0; i < list.length; i++) {
        FTProductRouteDefinitionForm bean = list[i];
        curGroupKey = OiMaintainService.getGroupKey(productType, bean.getProductBody(), prodInfo.getBrand(), bean.getBackendOption(),
                bean.getPackageCode(), bean.getPinCount(), bean.getFgWithCode(), bean.getFtRoute(), bean.getFtRouteAdd(), null, 1);

        if (!curGroupKey.equals(preGroupKey)) {
          if (subsection > 0) {
            doc.add(table1);
          }
          subsection ++;
          sec = null;
          sec = new Paragraph(new Chunk("1-"+section+"-"+subsection+". Product Group Key - "+curGroupKey, SmallFont));
          sec.setSpacingAfter(5);
          doc.add(sec);
          table1 = null;
          //M200712056,20080130
          if (site == null) {
            float[] widths = {4, 5, 4, 4, 4, 4, 4, 8, 7, 6, 8, 5,5, 8, 8, 8, 7, 7, 7};//7-->6,10-->8,10-->8,9-->8,10-->8,add 8
            table1 = new PdfPTable(widths);
          } else {
            float[] widths = {4, 5, 4, 4, 4, 4, 4, 8, 7, 6, 8, 7,5, 10, 7,8, 9, 7};//10-->8,9-->8,12-->10, 12-->10,11-->8, add 8
            table1 = new PdfPTable(widths);
          }

          table1.setWidthPercentage(100);
          table1.setSpacingBefore(0);
          table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route Type", SmallFont)));
          table1.addCell(new Phrase(new Chunk("FT Route", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pkg Code", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pkg Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont)));
          if (productType.equals("NVM")) {
/*              table1.addCell(new Phrase(new Chunk("I Grade", SmallFont)));
              table1.addCell(new Phrase(new Chunk("C Grade", SmallFont)));
              table1.addCell(new Phrase(new Chunk("W Grade", SmallFont)));
              table1.addCell(new Phrase(new Chunk("Y Grade", SmallFont)));
              table1.addCell(new Phrase(new Chunk("J Grade", SmallFont)));*/
              
              table1.addCell(new Phrase(new Chunk("Noraml Grade Temperature", SmallFont)));
              table1.addCell(new Phrase(new Chunk("AEB Grade Temperature", SmallFont)));
          } else {
              table1.addCell(new Phrase(new Chunk("Temperature", SmallFont)));
          }
          table1.addCell(new Phrase(new Chunk("Body Size", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Tester", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Site", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Program Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Actual Program Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Special Control",SmallFont)));  //8
          table1.addCell(new Phrase(new Chunk("HW Configure", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont)));
          
          //M200712056,20080130
          if (site == null)
            table1.addCell(new Phrase(new Chunk("Route Comment", SmallFont)));
          

          preGroupKey = curGroupKey;
        }
        /*table1.addCell(new Phrase(new Chunk(bean.getTag(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getRoute_type(), SmallFont)));
        if (bean.getRoute_type().equals("Main"))
            table1.addCell(new Phrase(new Chunk(NullConvert(bean.getFtRoute()), SmallFont)));
        else
            table1.addCell(new Phrase(new Chunk(NullConvert(bean.getFtRouteAdd()), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getTestMode(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getPackageCode(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getPackageName(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getPinCount(), SmallFont)));
        temperature = getTemperature(bean.getI_Grade());
        table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
        if (productType.equals("NVM")) {
            temperature = getTemperature(bean.getC_Grade());
            table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
            temperature = getTemperatureSplit(bean.getS_Grade());
            table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
        }
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getBodySize()), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getTester(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getSite(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getProgramName(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getActual_file()), SmallFont)));
        hw_configure_split = PDFdiffService.getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(hw_configure_split,SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getTfComment()), SmallFont)));
        
        //M200712056,20080130
        if (site == null)
          table1.addCell(new Phrase(new Chunk(NullConvert(bean.getFtComment()), SmallFont)));
        */
        table1.addCell(new Phrase(new Chunk(bean.getTag(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getRoute_type()),(bean.getRoute_type_flag()==0?SmallFont:RedFont))));
        if (bean.getRoute_type().equals("Main"))
            table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getFtRoute()),(bean.getFt_route_flag()==0?SmallFont:RedFont))));
        else
            table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getFtRouteAdd()),(bean.getFt_route_add_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTestMode()),(bean.getTest_mode_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPackageCode()),(bean.getPackage_code_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPackageName()),(bean.getPackage_name_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPinCount()),(bean.getPin_count_flag()==0?SmallFont:RedFont))));
        if (productType.equals("NVM")) {
            i_grade_temperature = getTemperature(bean.getI_Grade());
            //if(i_grade_temperature != null && !i_grade_temperature.equals("NA℃")&& !i_grade_temperature.equals("")) {
                i_grade_temperature = "I:"+ i_grade_temperature +"\n";
            //}else {
            //    i_grade_temperature = "";
            //}
            c_grade_temperature = getTemperature(bean.getC_Grade());
            //if(c_grade_temperature != null && !c_grade_temperature.equals("NA℃")&& !c_grade_temperature.equals("")) {
                c_grade_temperature = "C:"+ c_grade_temperature +"\n";
            //}else {
            //    c_grade_temperature = "";
            //}
            w_grade_temperature = getTemperature(bean.getW_Grade());
            //if(w_grade_temperature != null && !w_grade_temperature.equals("NA℃")&& !w_grade_temperature.equals("")) {
                w_grade_temperature = "W:"+ w_grade_temperature +"\n";
            //}else {
            //    w_grade_temperature = "";
            //}
            y_grade_temperature = getTemperature(bean.getY_Grade());
            //if(y_grade_temperature != null && !y_grade_temperature.equals("NA℃")&& !y_grade_temperature.equals("")) {
                y_grade_temperature = "Y:"+ y_grade_temperature +"\n";
            //}else {
            //    y_grade_temperature = "";
            //}
            j_grade_temperature = getTemperature(bean.getJ_Grade());
            //if(j_grade_temperature != null && !j_grade_temperature.equals("NA℃")&& !j_grade_temperature.equals("")) {
                j_grade_temperature = "J:"+ j_grade_temperature +"\n";
            //}else {
            //    j_grade_temperature = "";
            //}
            k_grade_temperature = getTemperature(bean.getK_Grade());
            //if(k_grade_temperature != null && !k_grade_temperature.equals("NA℃")&& !k_grade_temperature.equals("")) {
                k_grade_temperature = "K:"+ k_grade_temperature +"\n";
            //}else {
            //    k_grade_temperature = "";
            //}
                
            l_grade_temperature = getTemperature(bean.getL_Grade());
            l_grade_temperature = "L:"+ l_grade_temperature +"\n";
                
            n_grade_temperature = getTemperature(bean.getN_Grade());
            n_grade_temperature = "N:"+ n_grade_temperature +"\n";
            
            b_grade_temperature = getTemperature(bean.getB_Grade());
            b_grade_temperature = "B:"+ b_grade_temperature +"\n";
            
            e_grade_temperature = getTemperature(bean.getE_Grade());
            e_grade_temperature = "E:"+ e_grade_temperature +"\n";
            
            String grade = i_grade_temperature +c_grade_temperature+w_grade_temperature+y_grade_temperature+j_grade_temperature+k_grade_temperature+l_grade_temperature+n_grade_temperature+b_grade_temperature+e_grade_temperature;
            Chunk c1 = new Chunk(i_grade_temperature,(bean.getI_grade_flag()==0)?SmallFont:RedFont);
            Chunk c2 = new Chunk(c_grade_temperature,(bean.getC_grade_flag()==0)?SmallFont:RedFont);
            Chunk c3 = new Chunk(w_grade_temperature,(bean.getW_grade_flag()==0)?SmallFont:RedFont);
            Chunk c4 = new Chunk(y_grade_temperature,(bean.getY_grade_flag()==0)?SmallFont:RedFont);
            Chunk c5 = new Chunk(j_grade_temperature,(bean.getJ_grade_flag()==0)?SmallFont:RedFont);
            Chunk c6 = new Chunk(k_grade_temperature,(bean.getK_grade_flag()==0)?SmallFont:RedFont);
            Chunk c7 = new Chunk(l_grade_temperature,(bean.getL_grade_flag()==0)?SmallFont:RedFont);
            Chunk c8 = new Chunk(n_grade_temperature,(bean.getN_grade_flag()==0)?SmallFont:RedFont);
            Chunk c9 = new Chunk(b_grade_temperature,(bean.getB_grade_flag()==0)?SmallFont:RedFont);
            Chunk c10 = new Chunk(e_grade_temperature,(bean.getE_grade_flag()==0)?SmallFont:RedFont);
            Phrase p = new Phrase();
            p.add(c1);
            p.add(c2);
            p.add(c3);
            p.add(c4);
            p.add(c5);
            p.add(c6);
            p.add(c7);
            p.add(c8);
            p.add(c9);
            p.add(c10);
            table1.addCell(p);
            temperature = getTemperatureSplit(bean.getS_Grade());
            table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(temperature),(bean.getS_grade_flag()==0?SmallFont:RedFont))));
        }else {
            temperature = getTemperature(bean.getI_Grade());
            table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(temperature),(bean.getI_grade_flag()==0?SmallFont:RedFont))));
        }
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getBodySize()),(bean.getBody_size_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTester()),(bean.getTester_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getSite()),(bean.getSite_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPgm_id()),(bean.getPgm_id_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getProgramName()),(bean.getProgram_name_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getActual_file()),(bean.getActual_file_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPgm_special_control()),(bean.getPgm_special_control_flag()==0?SmallFont:RedFont))));
        hw_configure_split = PDFdiffService.getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(hw_configure_split),(bean.getHw_configure_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTfComment()),(bean.getTf_comment_flag()==0?SmallFont:RedFont))));
        
        //M200712056,20080130
        if (site == null)
          table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getFtComment()),(bean.getTf_ft_comment_flag()==0?SmallFont:RedFont))));
        
      }
      doc.add(table1);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
    }
  }

	public static void CompareFTPDFMcp(Connection conn, Document doc,
			Font SmallFont, Font RedFont, String sid, String sid2,
			String status, String site, String section) {

		try {
			FTProductRouteDefinitionForm[] list = EditiionCompareService
					.CompareFTPDRMcp(conn, sid, sid2, status, site, 1);
			String temperature = null;
			String i_grade_temperature = null, c_grade_temperature = null, w_grade_temperature = null, y_grade_temperature = null, j_grade_temperature = null, k_grade_temperature = null, l_grade_temperature = null, n_grade_temperature = null, b_grade_temperature = null, e_grade_temperature = null;

			String preGroupKey = "";
			String curGroupKey = null;
			int subsection = 0;
			PdfPTable table1 = null;
			Paragraph sec = null;
			String hw_configure_split = null;
			String productType = OiMaintainService.getProductType(conn,sid);
			OiMaintainStep prodInfo = OiMaintainService.SearchFunction(conn,sid);

			for (int i = 0; i < list.length; i++) {
				FTProductRouteDefinitionForm bean = list[i];
				curGroupKey = OiMaintainService.getGroupKey(productType,
						bean.getProductBody(), prodInfo.getBrand(),
						bean.getBackendOption(), bean.getPackageCode(),
						bean.getPinCount(), bean.getFgWithCode(),
						bean.getFtRoute(), bean.getFtRouteAdd(), null, 1);

				if (!curGroupKey.equals(preGroupKey)) {
					if (subsection > 0) {
						doc.add(table1);
					}
					subsection++;
					sec = null;
					sec = new Paragraph(new Chunk("1-" + section + "-"
							+ subsection + ". Product Group Key - "
							+ curGroupKey, SmallFont));
					sec.setSpacingAfter(5);
					doc.add(sec);
					table1 = null;
					// M200712056,20080130
					if (site == null) {
						float[] widths = { 4, 5, 4, 4, 4, 4, 4, 8, 7, 6, 8, 5,
								5, 8, 8, 8, 7, 7, 7 };// 7-->6,10-->8,10-->8,9-->8,10-->8,add
													// 8
						table1 = new PdfPTable(widths);
					} else {
						float[] widths = { 4, 5, 4, 4, 4, 4, 4, 8, 7, 6, 8, 7,
								5, 10, 7,8, 9, 7 };// 10-->8,9-->8,12-->10,
													// 12-->10,11-->8, add 8
						table1 = new PdfPTable(widths);
					}

					table1.setWidthPercentage(100);
					table1.setSpacingBefore(0);
					table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));
					table1.addCell(new Phrase(
							new Chunk("Route Type", SmallFont)));
					table1.addCell(new Phrase(new Chunk("FT Route", SmallFont)));
					table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
					table1.addCell(new Phrase(new Chunk("Pkg Code", SmallFont)));
					table1.addCell(new Phrase(new Chunk("Pkg Name", SmallFont)));
					table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont)));
					if (productType.equals("NVM")) {
						/*
						 * table1.addCell(new Phrase(new Chunk("I Grade",
						 * SmallFont))); table1.addCell(new Phrase(new
						 * Chunk("C Grade", SmallFont))); table1.addCell(new
						 * Phrase(new Chunk("W Grade", SmallFont)));
						 * table1.addCell(new Phrase(new Chunk("Y Grade",
						 * SmallFont))); table1.addCell(new Phrase(new
						 * Chunk("J Grade", SmallFont)));
						 */

						table1.addCell(new Phrase(new Chunk(
								"Noraml Grade Temperature", SmallFont)));
						table1.addCell(new Phrase(new Chunk(
								"AEB Grade Temperature", SmallFont)));
					} else {
						table1.addCell(new Phrase(new Chunk("Temperature",
								SmallFont)));
					}
					table1.addCell(new Phrase(new Chunk("Body Size", SmallFont)));
					table1.addCell(new Phrase(new Chunk("Tester", SmallFont)));
					table1.addCell(new Phrase(new Chunk("Site", SmallFont)));
					table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont)));
					table1.addCell(new Phrase(new Chunk("Test Program Name",
							SmallFont)));
					table1.addCell(new Phrase(new Chunk("Actual Program Name",
							SmallFont)));
					table1.addCell(new Phrase(new Chunk("PGM Special Control",
							SmallFont)));
					table1.addCell(new Phrase(new Chunk("HW Configure",
							SmallFont)));
					table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont)));

					// M200712056,20080130
					if (site == null)
						table1.addCell(new Phrase(new Chunk("Route Comment",
								SmallFont)));

					preGroupKey = curGroupKey;
				}
				/*
				 * table1.addCell(new Phrase(new Chunk(bean.getTag(),
				 * SmallFont))); table1.addCell(new Phrase(new
				 * Chunk(bean.getRoute_type(), SmallFont))); if
				 * (bean.getRoute_type().equals("Main")) table1.addCell(new
				 * Phrase(new Chunk(NullConvert(bean.getFtRoute()),
				 * SmallFont))); else table1.addCell(new Phrase(new
				 * Chunk(NullConvert(bean.getFtRouteAdd()), SmallFont)));
				 * table1.addCell(new Phrase(new Chunk(bean.getTestMode(),
				 * SmallFont))); table1.addCell(new Phrase(new
				 * Chunk(bean.getPackageCode(), SmallFont))); table1.addCell(new
				 * Phrase(new Chunk(bean.getPackageName(), SmallFont)));
				 * table1.addCell(new Phrase(new Chunk(bean.getPinCount(),
				 * SmallFont))); temperature =
				 * getTemperature(bean.getI_Grade()); table1.addCell(new
				 * Phrase(new Chunk(temperature, SmallFont))); if
				 * (productType.equals("NVM")) { temperature =
				 * getTemperature(bean.getC_Grade()); table1.addCell(new
				 * Phrase(new Chunk(temperature, SmallFont))); temperature =
				 * getTemperatureSplit(bean.getS_Grade()); table1.addCell(new
				 * Phrase(new Chunk(temperature, SmallFont))); }
				 * table1.addCell(new Phrase(new
				 * Chunk(NullConvert(bean.getBodySize()), SmallFont)));
				 * table1.addCell(new Phrase(new Chunk(bean.getTester(),
				 * SmallFont))); table1.addCell(new Phrase(new
				 * Chunk(bean.getSite(), SmallFont))); table1.addCell(new
				 * Phrase(new Chunk(bean.getProgramName(), SmallFont)));
				 * table1.addCell(new Phrase(new
				 * Chunk(NullConvert(bean.getActual_file()), SmallFont)));
				 * hw_configure_split =
				 * PDFdiffService.getStringDataSplit(bean.getHw_configure());
				 * table1.addCell(new Phrase(new
				 * Chunk(hw_configure_split,SmallFont))); table1.addCell(new
				 * Phrase(new Chunk(NullConvert(bean.getTfComment()),
				 * SmallFont)));
				 * 
				 * //M200712056,20080130 if (site == null) table1.addCell(new
				 * Phrase(new Chunk(NullConvert(bean.getFtComment()),
				 * SmallFont)));
				 */
				table1.addCell(new Phrase(new Chunk(bean.getTag(), SmallFont)));
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getRoute_type()),
						(bean.getRoute_type_flag() == 0 ? SmallFont : RedFont))));
				if (bean.getRoute_type().equals("Main"))
					table1.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(bean.getFtRoute()), (bean
							.getFt_route_flag() == 0 ? SmallFont : RedFont))));
				else
					table1.addCell(new Phrase(
							new Chunk(StringUtil.NullConvert(bean
									.getFtRouteAdd()), (bean
									.getFt_route_add_flag() == 0 ? SmallFont
									: RedFont))));
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getTestMode()),
						(bean.getTest_mode_flag() == 0 ? SmallFont : RedFont))));
				table1.addCell(new Phrase(
						new Chunk(
								StringUtil.NullConvert(bean.getPackageCode()),
								(bean.getPackage_code_flag() == 0 ? SmallFont
										: RedFont))));
				table1.addCell(new Phrase(
						new Chunk(
								StringUtil.NullConvert(bean.getPackageName()),
								(bean.getPackage_name_flag() == 0 ? SmallFont
										: RedFont))));
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getPinCount()),
						(bean.getPin_count_flag() == 0 ? SmallFont : RedFont))));
				if (productType.equals("NVM")) {
					i_grade_temperature = getTemperature(bean.getI_Grade());
					// if(i_grade_temperature != null &&
					// !i_grade_temperature.equals("NA℃")&&
					// !i_grade_temperature.equals("")) {
					i_grade_temperature = "I:" + i_grade_temperature + "\n";
					// }else {
					// i_grade_temperature = "";
					// }
					c_grade_temperature = getTemperature(bean.getC_Grade());
					// if(c_grade_temperature != null &&
					// !c_grade_temperature.equals("NA℃")&&
					// !c_grade_temperature.equals("")) {
					c_grade_temperature = "C:" + c_grade_temperature + "\n";
					// }else {
					// c_grade_temperature = "";
					// }
					w_grade_temperature = getTemperature(bean.getW_Grade());
					// if(w_grade_temperature != null &&
					// !w_grade_temperature.equals("NA℃")&&
					// !w_grade_temperature.equals("")) {
					w_grade_temperature = "W:" + w_grade_temperature + "\n";
					// }else {
					// w_grade_temperature = "";
					// }
					y_grade_temperature = getTemperature(bean.getY_Grade());
					// if(y_grade_temperature != null &&
					// !y_grade_temperature.equals("NA℃")&&
					// !y_grade_temperature.equals("")) {
					y_grade_temperature = "Y:" + y_grade_temperature + "\n";
					// }else {
					// y_grade_temperature = "";
					// }
					j_grade_temperature = getTemperature(bean.getJ_Grade());
					// if(j_grade_temperature != null &&
					// !j_grade_temperature.equals("NA℃")&&
					// !j_grade_temperature.equals("")) {
					j_grade_temperature = "J:" + j_grade_temperature + "\n";
					// }else {
					// j_grade_temperature = "";
					// }
					k_grade_temperature = getTemperature(bean.getK_Grade());
					// if(k_grade_temperature != null &&
					// !k_grade_temperature.equals("NA℃")&&
					// !k_grade_temperature.equals("")) {
					k_grade_temperature = "K:" + k_grade_temperature + "\n";
					// }else {
					// k_grade_temperature = "";
					// }

					l_grade_temperature = getTemperature(bean.getL_Grade());
					l_grade_temperature = "L:" + l_grade_temperature + "\n";

					n_grade_temperature = getTemperature(bean.getN_Grade());
					n_grade_temperature = "N:" + n_grade_temperature + "\n";

					b_grade_temperature = getTemperature(bean.getB_Grade());
					b_grade_temperature = "B:" + b_grade_temperature + "\n";

					e_grade_temperature = getTemperature(bean.getE_Grade());
					e_grade_temperature = "E:" + e_grade_temperature + "\n";

					String grade = i_grade_temperature + c_grade_temperature
							+ w_grade_temperature + y_grade_temperature
							+ j_grade_temperature + k_grade_temperature
							+ l_grade_temperature + n_grade_temperature
							+ b_grade_temperature + e_grade_temperature;
					Chunk c1 = new Chunk(i_grade_temperature,
							(bean.getI_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c2 = new Chunk(c_grade_temperature,
							(bean.getC_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c3 = new Chunk(w_grade_temperature,
							(bean.getW_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c4 = new Chunk(y_grade_temperature,
							(bean.getY_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c5 = new Chunk(j_grade_temperature,
							(bean.getJ_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c6 = new Chunk(k_grade_temperature,
							(bean.getK_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c7 = new Chunk(l_grade_temperature,
							(bean.getL_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c8 = new Chunk(n_grade_temperature,
							(bean.getN_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c9 = new Chunk(b_grade_temperature,
							(bean.getB_grade_flag() == 0) ? SmallFont : RedFont);
					Chunk c10 = new Chunk(e_grade_temperature,
							(bean.getE_grade_flag() == 0) ? SmallFont : RedFont);
					Phrase p = new Phrase();
					p.add(c1);
					p.add(c2);
					p.add(c3);
					p.add(c4);
					p.add(c5);
					p.add(c6);
					p.add(c7);
					p.add(c8);
					p.add(c9);
					p.add(c10);
					table1.addCell(p);
					temperature = getTemperatureSplit(bean.getS_Grade());
					table1.addCell(new Phrase(
							new Chunk(StringUtil.NullConvert(temperature),
									(bean.getS_grade_flag() == 0 ? SmallFont
											: RedFont))));
				} else {
					temperature = getTemperature(bean.getI_Grade());
					table1.addCell(new Phrase(
							new Chunk(StringUtil.NullConvert(temperature),
									(bean.getI_grade_flag() == 0 ? SmallFont
											: RedFont))));
				}
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getBodySize()),
						(bean.getBody_size_flag() == 0 ? SmallFont : RedFont))));
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getTester()), (bean.getTester_flag() == 0 ? SmallFont
						: RedFont))));
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getSite()), (bean.getSite_flag() == 0 ? SmallFont
						: RedFont))));
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getPgm_id()), (bean.getPgm_id_flag() == 0 ? SmallFont
						: RedFont))));
				table1.addCell(new Phrase(
						new Chunk(
								StringUtil.NullConvert(bean.getProgramName()),
								(bean.getProgram_name_flag() == 0 ? SmallFont
										: RedFont))));
				table1.addCell(new Phrase(
						new Chunk(
								StringUtil.NullConvert(bean.getActual_file()),
								(bean.getActual_file_flag() == 0 ? SmallFont
										: RedFont))));
				table1.addCell(new Phrase(
						new Chunk(
								StringUtil.NullConvert(bean.getPgm_special_control()),
								(bean.getPgm_special_control_flag() == 0 ? SmallFont
										: RedFont))));
				hw_configure_split = PDFdiffService.getStringDataSplit(bean
						.getHw_configure());
				table1.addCell(new Phrase(new Chunk(StringUtil
						.NullConvert(hw_configure_split), (bean
						.getHw_configure_flag() == 0 ? SmallFont : RedFont))));
				table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean
						.getTfComment()),
						(bean.getTf_comment_flag() == 0 ? SmallFont : RedFont))));

				// M200712056,20080130
				if (site == null)
					table1.addCell(new Phrase(new Chunk(StringUtil
							.NullConvert(bean.getFtComment()),
							(bean.getTf_ft_comment_flag() == 0 ? SmallFont
									: RedFont))));

			}
			doc.add(table1);
		} catch (Exception ex) {
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
		} finally {
		}
	}

  public static void CompareAVIPDF(Connection conn, Document doc,
                                    Font SmallFont,
                                    Font RedFont,
                                    String sid,
                                    String sid2,
                                    String status,
                                    String site,
                                    String section) {

      try {
        FTProductRouteDefinitionForm[] list =
            EditiionCompareService.CompareAVIPDR(conn, sid, sid2, status, site, 1);
        String temperature = null;

        String preGroupKey = "";
        String curGroupKey = null;
        int subsection = 0;
        PdfPTable table1 = null;
        Paragraph sec = null;
        OiMaintainStep prodInfo = OiMaintainService.SearchFunction(conn,sid);

        for (int i = 0; i < list.length; i++) {
          FTProductRouteDefinitionForm bean = list[i];
          if (i == 0) {
            sec = null;
            sec = new Paragraph(new Chunk("", SmallFont));
            sec.setSpacingAfter(5);
            doc.add(sec);
            float[] widths = {5,20};
            table1 = new PdfPTable(widths);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(0);
            table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));
            table1.addCell(new Phrase(new Chunk("Site", SmallFont)));
          }
          table1.addCell(new Phrase(new Chunk(bean.getTag(), SmallFont)));
          //table1.addCell(new Phrase(new Chunk(bean.getSite(), SmallFont)));
          table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getSite()),(bean.getSite_flag()==0?SmallFont:RedFont))));
        }
        doc.add(table1);
      } catch (Exception ex) {
        ex.fillInStackTrace();
        TDSLogger.println(ex.getMessage());
      } finally {
      }
    }

  public static PdfPTable CompareBA(Connection conn, PdfPTable tabExtend,
                                    Font F,
                                    Font RedFont,
                                    String sid,
                                    String sid2,
                                    String status,
                                    int flag) {

    TFIMBasicActionForm[] list = EditiionCompareService.CompareBA(conn, sid, sid2, status, flag, 1);
    for (int i = 0; i < list.length; i++) {
      TFIMBasicActionForm bean = list[i];
      /*if (flag == 0)
        tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
      else
        tabExtend.addCell(new Phrase(new Chunk("NEW", F)));

      tabExtend.addCell(new Phrase(new Chunk(bean.getTester(), F)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getOptions(), F)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getGrade(), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getGood_bin()), F)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getBin_type_str(), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getInkless_grade()), F)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getIpn_action_str(), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getEpn_speed()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getTest_speed()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getDown_grade()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()), F)));
      */
        /*if (flag == 0)
          tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
        else
          tabExtend.addCell(new Phrase(new Chunk("NEW", F)));*/
        tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), F)));
        
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getTester()),(bean.getTester_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getOptions()),(bean.getOptions_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getGrade()),(bean.getGrade_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getIb_bin()),(bean.getIb_bin_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getGood_bin()),(bean.getGood_bin_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getBin_type_str()),(bean.getBin_type_str_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getInkless_grade()),(bean.getInkless_grade_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getIpn_action_str()),(bean.getIpn_action_str_flag()==0?F:RedFont))));
        String productType = OiMaintainService.getProductType(conn,sid);
        if (!productType.equals("XROM")){
        	tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getEpn_speed()),(bean.getEpn_speed_flag()==0?F:RedFont))));
        	tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getTest_speed()),(bean.getTest_speed_flag()==0?F:RedFont))));
        	tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getKtd_bin_flag()),(bean.getKtd_bin_flag_flag()==0?F:RedFont))));
        }	
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getDown_grade()),(bean.getDown_grade_flag()==0?F:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()),(bean.getRemark_flag()==0?F:RedFont))));
    }
    return tabExtend;
  }

  public static boolean SearchDOC(Document doc,
                                  Font F,
                                  String sid,
                                  String status,
                                  String pd_body,
                                  String brand,
                                  String version,
                                  String doc_type, Connection conn) {

    EditiionCompareActionForm[] result = null;
    if (!status.equals("R"))
      result = EditiionCompareService.SearchUnreleasedDOC(sid, status, pd_body, brand, version, doc_type,conn);
    else
      result = EditiionCompareService.SearchReleasedDOC(sid, status, pd_body, brand, version, doc_type,conn);

    try {
      for (int i = 0; i < result.length; i++) {
        EditiionCompareActionForm bean = result[i];
        StringBuffer buf = new StringBuffer();
        boolean file_show = false;
        doc.newPage();
        if (i == 0)
          if (!doc_type.equals("Y"))
            doc.add(new Paragraph("5. Test Flows"));

        if (bean.getTag_old().equals("0")) {
          buf = new StringBuffer();
          buf.append("OLD: " + bean.getTest_flow());
          file_show = PDFdiffService.doc_show_doc(doc, F, bean.getPath_old(), buf.toString());
          if (bean.getTag_new().equals("0"))
            doc.newPage();
        }
        if (bean.getTag_new().equals("0")) {
          buf = new StringBuffer();
          buf.append("NEW: " + bean.getTest_flow());
          file_show = PDFdiffService.doc_show_doc(doc, F, bean.getPath_new(), buf.toString());
        }
      }
    } catch (Exception ex) {
      ex.getStackTrace();
    }
    return true;
  }

  public static PdfPTable SubCon(PdfPTable tabExtend,
                                 Font F,
                                 String sid,
                                 String status,
                                 String pd_body,
                                 String brand,
                                 String version,
                                 Connection conn) {

    StringBuffer SelSQL = new StringBuffer();
    //Connection conn = null;
    try {
      //conn = DBConnection.getConnection();
      if (status.equals("R"))
        SelSQL.append("SELECT site from tf_test_parameter_ws where sid=? " +
                      "union SELECT site from tf_test_parameter_ft where sid=? " +
                      "union SELECT site from tf_test_parameter_pbc where sid=?");
      else
        SelSQL.append("SELECT site from tf_test_parameter_ws_tx where sid=? " +
                      "union SELECT site from tf_test_parameter_ft_tx where sid=? " +
                      "union SELECT site from tf_test_parameter_pbc_tx where sid=?");

      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ps.setString(1,sid);
      ps.setString(2,sid);
      ps.setString(3,sid);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        tabExtend.addCell(new Phrase(new Chunk(rs.getString("site"), F)));
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      //DBConnection.close(conn);
    }
    return tabExtend;
  }

  public static PdfPTable NewVendor(PdfPTable tabExtend,
                                    Font F,
                                    String sid,
                                    String status,
                                    String pd_body,
                                    String brand,
                                    String version,
                                    Connection conn) {

    String sid2 = OiMaintainService.getPreviousVersionSid(conn, sid);
    StringBuffer SelSQL = new StringBuffer();
    //Connection conn = null;
    try {
      if (status.equals("R"))
        SelSQL.append("SELECT site from tf_test_parameter_ws where sid=? " +
                      "union SELECT site from tf_test_parameter_ft where sid=? " +
                      "union SELECT site from tf_test_parameter_pbc where sid=?");
      else
        SelSQL.append("SELECT site from tf_test_parameter_ws_tx where sid=? " +
                      "union SELECT site from tf_test_parameter_ft_tx where sid=? " +
                      "union SELECT site from tf_test_parameter_pbc_tx where sid=?");
      //conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ps.setString(1,sid);
      ps.setString(2,sid);
      ps.setString(3,sid);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        StringBuffer sql_check = new StringBuffer();
        sql_check.append("SELECT count(*) as total_count " +
                         "FROM (SELECT site from tf_test_parameter_ws " +
                         "where sid=? " +
                         "union SELECT site from tf_test_parameter_ft " +
                         "where sid=? " +
                         "union SELECT site from tf_test_parameter_pbc " +
                         "where sid=?) b " +
                         "where  b.site=? ");
        PreparedStatement ps_check = conn.prepareStatement(sql_check.toString());
        ps_check.setString(1,sid2);
        ps_check.setString(2,sid2);
        ps_check.setString(3,sid2);
        ps_check.setString(4,rs.getString("site"));
        ResultSet rs_check = ps_check.executeQuery();
        while (rs_check.next()) {
          if (rs_check.getInt("total_count") == 0) {
            tabExtend.addCell(new Phrase(new Chunk("NEW", F)));
            tabExtend.addCell(new Phrase(new Chunk(rs.getString("site"), F)));
          }
        }
      }
    } catch (Exception ex) {
      ex.getStackTrace();
    } finally {
      //DBConnection.close(conn);
    }
    return tabExtend;
  }

  public static PdfPTable OldVendor(PdfPTable tabExtend,
                                    Font F,
                                    String sid,
                                    String status,
                                    String pd_body,
                                    String brand,
                                    String version,
                                    Connection conn) {

    String sid2 = OiMaintainService.getPreviousVersionSid(conn, sid);
    StringBuffer SelSQL = new StringBuffer();
    //Connection conn = null;
    try {
      SelSQL.append("SELECT site from tf_test_parameter_ws where sid=? " +
                    "union SELECT site from tf_test_parameter_ft where sid=? " +
                    "union SELECT site from tf_test_parameter_pbc where sid=? ");
      //conn = DBConnection.getConnection();
      PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
      ps.setString(1,sid2);
      ps.setString(2,sid2);
      ps.setString(3,sid2);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        StringBuffer sql_check = new StringBuffer();
        if (status.equals("R"))
          sql_check.append("SELECT count(*) as total_count "+
                           "FROM (SELECT site from tf_test_parameter_ws " +
                           " where sid=? " +
                           "union SELECT site from tf_test_parameter_ft " +
                           "where sid=? " +
                           "union SELECT site from tf_test_parameter_pbc " +
                           "where sid=? " +
                           ") b where  b.site=?");
        else
          sql_check.append("SELECT count(*) as total_count "+
                           "FROM (SELECT site from tf_test_parameter_ws_tx " +
                           " where sid=? " +
                           "union SELECT site from tf_test_parameter_ft_tx " +
                           "where sid=? " +
                           "union SELECT site from tf_test_parameter_pbc_tx " +
                           "where sid=? " +
                           ") b where  b.site=?");
        PreparedStatement ps_check = conn.prepareStatement(sql_check.toString());
        ps_check.setString(1,sid);
        ps_check.setString(2,sid);
        ps_check.setString(3,sid);
        ps_check.setString(4,rs.getString("site"));
        ResultSet rs_check = ps_check.executeQuery();
        while (rs_check.next()) {
          if (rs_check.getInt("total_count") == 0) {
            tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
            tabExtend.addCell(new Phrase(new Chunk(rs.getString("site"), F)));
          }
        }
      }
    } catch (Exception ex) {
      ex.getStackTrace();
    } finally {
      //DBConnection.close(conn);
    }
    return tabExtend;
  }

  public static boolean doc_show_doc(Document doc,
                                     Font BasicFont,
                                     String Path,
                                     String file_str) {

    boolean flag1 = true;
//    Font BasicFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.BLACK);
    try {
//      String[] b = StringUtil.parse2StringsStr(file_str,"|");

      //b[0]為doc_name
      //b[1]為file_addr
      //show標題
      doc.add(new Paragraph(file_str, BasicFont));
      //check是否需加"/"
      TDSLogger.println(file_str + " : " + Path);
      Image jpg1 = Image.getInstance(Path);
      jpg1.scaleAbsolute(500, 540);
      doc.add(jpg1);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      return false;
    }
    return flag1;
  }

  public static String NullConvert(String str) {
    if (str == null)
      return " ";
    else
      return str;
  }
}