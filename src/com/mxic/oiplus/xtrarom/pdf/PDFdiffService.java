package com.mxic.oiplus.xtrarom.pdf;

import java.io.*;
import java.sql.*;

import java.awt.*;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import com.mxic.oiplus.xtrarom.oimaintain.*;
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

  public static PdfPTable ComparePDR(PdfPTable tabExtend,
                                     Font F,
                                     String sid,
                                     String sid2,
                                     String status,
                                     int flag) {

    EditiionCompareActionForm[] list =
        EditiionCompareService.ComparePDR(sid, sid2, status, flag, 1);
    String temperature = null;

    for (int i = 0; i < list.length; i++) {
      EditiionCompareActionForm bean = list[i];
      if (flag == 0)
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
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getSampling_cond()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()), F)));
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

      tabExtend.addCell(new Phrase(new Chunk(bean.getBody_version(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getMaskopt(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getMaskopt_rev(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getCode_no(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getPincount(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getPkgtype(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getRoute_type(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getFt_route_code(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFtroute()), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add1()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add2()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add3()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add4()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add5()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getComment()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(bean.getSortroutecode(), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsroute()), NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute1()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute2()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute3()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute4()),NormalFont)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWscomment()),NormalFont)));
      */
      /*if (flag == 0)
          tabExtend.addCell(new Phrase(new Chunk("OLD", NormalFont)));
        else
          tabExtend.addCell(new Phrase(new Chunk("NEW", NormalFont)));*/
        tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), NormalFont)));

        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getBody_version()),(bean.getBody_version_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMaskopt()),(bean.getMask_option_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMaskopt_rev()),(bean.getMask_option_rev_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getCode_no()),(bean.getCode_no_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getPincount()),(bean.getPin_count_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getPkgtype()),(bean.getPackage_code_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRoute_type()),(bean.getRoute_type_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_code()),(bean.getFt_route_code_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFtroute()),(bean.getFt_route_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add()),(bean.getFt_route_add_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add1()),(bean.getFt_route_add1_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add2()),(bean.getFt_route_add2_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add3()),(bean.getFt_route_add3_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add4()),(bean.getFt_route_add4_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getFt_route_add5()),(bean.getFt_route_add5_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getComment()),(bean.getTf_comment_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getSortroutecode()),(bean.getSort_route_code_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsroute()),(bean.getWs_route_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute()),(bean.getWs_route_add_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute1()),(bean.getWs_route_add1_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute2()),(bean.getWs_route_add2_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute3()),(bean.getWs_route_add3_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWsaddroute4()),(bean.getWs_route_add4_flag()==0?NormalFont:RedFont))));
        tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getWscomment()),(bean.getTf_ws_comment_flag()==0?NormalFont:RedFont))));

      /*PdfPCell cmt = null;
      cmt = new PdfPCell(new Phrase(NullConvert(bean.getComment()), F));
      cmt.setColspan(4);
      tabExtend.addCell(cmt);*/
    }
    return tabExtend;
  }

  public static void CompareWSPDF(Connection conn, Document doc,
                                       Font SmallFont,
                                       Font RedFont,
                                       String sid,
                                       String sid2,
                                       String status,
                                       String site) {
    try {
      WSProductRouteDefinitionForm[] list =
          EditiionCompareService.CompareWSPDR(conn, sid, sid2, status, site, 1);
      String productType = OiMaintainService.getProductType(sid);
      OiMaintainStep prodInfo = OiMaintainService.SearchFunction(sid);

      String temperature = null;
      PdfPTable table1 = null;
      Paragraph sec = null;
      String preGroupKey = "";
      String curGroupKey = null;
      String hw_configure_split = null;
      int section = 0;
      for (int i = 0; i < list.length; i++) {
        WSProductRouteDefinitionForm bean = list[i];
        curGroupKey = OiMaintainService.getGroupKey(productType, bean.getProductBody(),
            bean.getBodyVersion(), bean.getMaskOption(),bean.getMaskOptionRev(),
	         "", "", bean.getCodeNo(), bean.getWsRoute(),   0);//bean.getWsRouteAdd(),

/*
        if (productType.equals("ASM")) {
          String buf = null;
          if (list[i].getSales_form().equals("W"))
        	buf = " for wafer sale";
          else
          	buf = " for package sale";
          curGroupKey = bean.getProductBody()+ buf;
        }
        else
          curGroupKey = bean.getProductBody()+ bean.getMaskOption()+
          	bean.getDbWithCode()+ bean.getSortRouteCode();
*/
        if (!curGroupKey.equals(preGroupKey)) {
          if (section > 0) {
            doc.add(table1);
//            document.newPage();
          }
          section++;
          sec = null;
          sec = new
              Paragraph(new Chunk("1-1-" + section + " Product Group Key - " + curGroupKey, SmallFont));
          sec.setSpacingAfter(5);
          doc.add(sec);
          table1 = null;
          float[] widths = {4,5,5,5,10,5,10,5,10,7,7,15,14,18};//20-->16,23-->19,add 8 
          table1 = new PdfPTable(widths);
          table1.setSpacingBefore(0);
          table1.setWidthPercentage(100);
          table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route Type", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Tester", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Mask Opt", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Site", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Temperature", SmallFont)));
          table1.addCell(new Phrase(new Chunk("HW Configure", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Special Control", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont)));
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
        hw_configure_split = getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(hw_configure_split,SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getTfComment()), SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getWsComment()), SmallFont)));
        */
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getRoute_type()),(bean.getRoute_type_flag()==0?SmallFont:RedFont))));
        if (bean.getRoute_type().equals("Main"))
            table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getWsRoute()),(bean.getWs_route_flag()==0?SmallFont:RedFont))));
        else
        	table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getWsRouteAdd()),(bean.getWs_route_add_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTestMode()),(bean.getTest_mode_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTester()),(bean.getTester_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getMaskOption()),(bean.getMask_option_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getSite()),(bean.getSite_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPgm_id()),(bean.getPgm_id_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getProgramName()),(bean.getProgram_name_flag()==0?SmallFont:RedFont))));
        temperature = getTemperature(NullConvert(bean.getTemperature()));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(temperature),(bean.getTemperature_flag()==0?SmallFont:RedFont))));
        hw_configure_split = PDFdiffService.getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(hw_configure_split),(bean.getHw_configure_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getPgm_special_control()),(bean.getPgm_special_control_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getTfComment()),(bean.getTf_comment_flag()==0?SmallFont:RedFont))));
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getWsComment()),(bean.getTf_ws_comment_flag()==0?SmallFont:RedFont))));
        
        
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

      String preGroupKey = "";
      String curGroupKey = null;
      int subsection = 0;
      PdfPTable table1 = null;
      Paragraph sec = null;
      String hw_configure_split = null;
      String productType = OiMaintainService.getProductType(sid);
      OiMaintainStep prodInfo = OiMaintainService.SearchFunction(sid);

      for (int i = 0; i < list.length; i++) {
        FTProductRouteDefinitionForm bean = list[i];
    	curGroupKey = OiMaintainService.getGroupKey(productType, bean.getProductBody(),
            bean.getBodyVersion(),bean.getBackendOption(),bean.getMaskOptionRev(),
    		bean.getPackageCode(), bean.getPinCount(), bean.getCodeNo(), bean.getFtRoute(),  1);//, bean.getFtRouteAdd()
/*
        if (productType.equals("ASM")) {
            curGroupKey = bean.getProductBody() + bean.getBackendOption();
        } else
          curGroupKey = bean.getProductBody()+
                      bean.getPackageCode()+bean.getPinCount()+
                      bean.getFgWithCode()+bean.getFtRouteCode();
*/
        if (!curGroupKey.equals(preGroupKey)) {
          if (subsection > 0) {
            doc.add(table1);
//            document.newPage();
          }
          subsection ++;
//          doc.add(new Phrase(new Chunk("1-"+section+"-"+subsection+". Product Group Key - "+curGroupKey, SmallFont)));
          sec = null;
          sec = new Paragraph(new Chunk("1-"+section+"-"+subsection+". Product Group Key - "+curGroupKey, SmallFont));
          sec.setSpacingAfter(5);
          doc.add(sec);
          table1 = null;
          float[] widths = {4,5,4,4,4,6,4,4,8,7,8,6,5,7,7,7,7,7,7};//7-->6,10-->8,10-->8,9-->8,10-->8,add 8
          table1 = new PdfPTable(widths);

          table1.setWidthPercentage(100);
          table1.setSpacingBefore(0);
          table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route Type", SmallFont)));
          table1.addCell(new Phrase(new Chunk("FT Route", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pkg Code", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pkg Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Code No", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Temperature", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Body Size", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Tester", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Site", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Program Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Actual Program Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Special Control",SmallFont)));  //8
          table1.addCell(new Phrase(new Chunk("HW Configure", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont)));
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
        table1.addCell(new Phrase(new Chunk(bean.getCodeNo(), SmallFont)));
//        temperature = getTemperature(bean.getI_Grade());
//        table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
        temperature = getTemperature(bean.getC_Grade());
        table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getBodySize()), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getTester(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getSite(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getProgramName(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getActual_file()), SmallFont)));
        hw_configure_split = getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(hw_configure_split,SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getTfComment()), SmallFont)));
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
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getCodeNo()),(bean.getCode_no_flag()==0?SmallFont:RedFont))));
        temperature = getTemperature(bean.getC_Grade());
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(temperature),(bean.getC_grade_flag()==0?SmallFont:RedFont))));
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
        table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getFtComment()),(bean.getTf_ft_comment_flag()==0?SmallFont:RedFont))));
        
      }
      doc.add(table1);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
    }
  }
  public static void CompareFTPDF_RE(Connection conn, Document doc,
                                  Font SmallFont,
                                  Font RedFont,
                                  String sid,
                                  String sid2,
                                  String status,
                                  String site,
                                  String section) {

    try {
      FTProductReRouteDefinitionForm[] list =
          EditiionCompareService.CompareFTPDR_RE(conn, sid, sid2, status, site, 1);
      String temperature = null;

      String preGroupKey = "";
      String curGroupKey = null;
      int subsection = 0;
      PdfPTable table1 = null;
      Paragraph sec = null;
      String hw_configure_split = null;
      String productType = OiMaintainService.getProductType(sid);
      OiMaintainStep prodInfo = OiMaintainService.SearchFunction(sid);

      for (int i = 0; i < list.length; i++) {
        FTProductReRouteDefinitionForm bean = list[i];

/* 20071228 取消 group key
            curGroupKey = OiMaintainService.getGroupKey(productType, bean.getProductBody(),
                bean.getBodyVersion(), bean.getMaskOption(), bean.getMaskOptionRev(),
                bean.getPackageCode(), bean.getPinCount(), bean.getCodeNo(), bean.getFtRoute(),  1);//bean.getFtRouteAdd(),
        if (!curGroupKey.equals(preGroupKey)) {
          if (subsection > 0) {
            doc.add(table1);
          }
          subsection ++;
          sec = null;
          sec = new Paragraph(new Chunk("1-"+section+"-"+subsection+". Product Group Key - "+curGroupKey, SmallFont));
          sec.setSpacingAfter(5);
          doc.add(sec);
 */
          if (i == 0) {
          table1 = null;
          float[] widths = {4,4,5,4,4,6,4,4,8,7,8,6,5,7,7,7,7,7,7};//7-->6,10-->8,10-->8,9-->8,10-->8,add 8
          table1 = new PdfPTable(widths);

          table1.setWidthPercentage(100);
          table1.setSpacingBefore(5);
          table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route Type", SmallFont)));
          table1.addCell(new Phrase(new Chunk("FT Route", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Mode", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pkg Code", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pkg Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Recycle Code", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Temperature", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Body Size", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Tester", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Site", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM ID", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Test Program Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Actual Program Name", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Special Control",SmallFont)));  //8
          table1.addCell(new Phrase(new Chunk("HW Configure", SmallFont)));
          table1.addCell(new Phrase(new Chunk("PGM Notes", SmallFont)));
          table1.addCell(new Phrase(new Chunk("Route Comment", SmallFont)));
          
          

//          preGroupKey = curGroupKey;
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
        table1.addCell(new Phrase(new Chunk(bean.getRecycleCode(), SmallFont)));
//        temperature = getTemperature(bean.getI_Grade());
//        table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
        temperature = getTemperature(bean.getC_Grade());
        table1.addCell(new Phrase(new Chunk(temperature, SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getBodySize()), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getTester(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getSite(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(bean.getProgramName(), SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getActual_file()), SmallFont)));
        hw_configure_split = getStringDataSplit(bean.getHw_configure());
        table1.addCell(new Phrase(new Chunk(hw_configure_split,SmallFont)));
        table1.addCell(new Phrase(new Chunk(NullConvert(bean.getTfComment()), SmallFont)));
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
          table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getRecycleCode()),(bean.getRecycle_code_flag()==0?SmallFont:RedFont))));
          temperature = getTemperature(bean.getC_Grade());
          table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(temperature),(bean.getC_grade_flag()==0?SmallFont:RedFont))));
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
          table1.addCell(new Phrase(new Chunk(StringUtil.NullConvert(bean.getFtComment()),(bean.getTf_ft_comment_flag()==0?SmallFont:RedFont))));
          
      }
      doc.add(table1);
    } catch (Exception ex) {
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
    } finally {
    }
  }

  public static PdfPTable CompareMainRouteSub(Connection conn, PdfPTable tabExtend,
                                    Font F,
                                    Font RedFont,
                                    String sid,
                                    String sid2,
                                    String status,
                                    int flag,
                                    String productType) {

    TFIMBasicActionForm[] list = EditiionCompareService.CompareMainRouteSub(conn, sid, sid2, status, flag, 1);
    for (int i = 0; i < list.length; i++) {
      TFIMBasicActionForm bean = list[i];
      /*if (flag == 0)
        tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
      else
        tabExtend.addCell(new Phrase(new Chunk("NEW", F)));

      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMain_route()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMap_route()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()), F)));*/
      /*if (flag == 0)
        tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
      else
        tabExtend.addCell(new Phrase(new Chunk("NEW", F)));*/
      tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMain_route()),(bean.getMain_route_flag()==0?F:RedFont))));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMap_route()),(bean.getMap_route_flag()==0?F:RedFont))));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()),(bean.getRemark_flag()==0?F:RedFont))));

    }
    return tabExtend;
  }
  
  public static PdfPTable CompareMainRouteRework(Connection conn, PdfPTable tabExtend,
                                    Font F,
                                    Font RedFont,
                                    String sid,
                                    String sid2,
                                    String status,
                                    int flag,
                                    String productType) {

    TFIMBasicActionForm[] list = EditiionCompareService.CompareMainRouteRework(conn, sid, sid2, status, flag, 1);
    for (int i = 0; i < list.length; i++) {
      TFIMBasicActionForm bean = list[i];
      /*if (flag == 0)
        tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
      else
        tabExtend.addCell(new Phrase(new Chunk("NEW", F)));

      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMain_route()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMap_route()), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getRemark()), F)));*/
      /*if (flag == 0)
          tabExtend.addCell(new Phrase(new Chunk("OLD", F)));
        else
          tabExtend.addCell(new Phrase(new Chunk("NEW", F)));*/
      tabExtend.addCell(new Phrase(new Chunk(bean.getType_flag(), F)));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMain_route()),(bean.getMain_route_flag()==0?F:RedFont))));
      tabExtend.addCell(new Phrase(new Chunk(NullConvert(bean.getMap_route()),(bean.getMap_route_flag()==0?F:RedFont))));
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
                                  String doc_type) {

    EditiionCompareActionForm[] result = null;

    if (!status.equals("R"))
      result = EditiionCompareService.SearchUnreleasedDOC(sid, status, pd_body, brand, version, doc_type);
    else
      result = EditiionCompareService.SearchReleasedDOC(sid, status, pd_body, brand, version, doc_type);

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
                                 String version) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      conn = DBConnection.getConnection();
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
      DBConnection.close(conn);
    }
    return tabExtend;
  }

  public static PdfPTable NewVendor(PdfPTable tabExtend,
                                    Font F,
                                    String sid,
                                    String status,
                                    String pd_body,
                                    String brand,
                                    String version) {

    String sid2 = OiMaintainService.getPreviousVersionSid(sid);
    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    try {
      if (status.equals("R"))
        SelSQL.append("SELECT site from tf_test_parameter_ws where sid=? " +
                      "union SELECT site from tf_test_parameter_ft where sid=? " +
                      "union SELECT site from tf_test_parameter_pbc where sid=?");
      else
        SelSQL.append("SELECT site from tf_test_parameter_ws_tx where sid=? " +
                      "union SELECT site from tf_test_parameter_ft_tx where sid=? " +
                      "union SELECT site from tf_test_parameter_pbc_tx where sid=?");
      conn = DBConnection.getConnection();
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
                         "where b.site=? ");
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
      DBConnection.close(conn);
    }
    return tabExtend;
  }

  public static PdfPTable OldVendor(PdfPTable tabExtend,
                                    Font F,
                                    String sid,
                                    String status,
                                    String pd_body,
                                    String brand,
                                    String version) {

    StringBuffer SelSQL = new StringBuffer();
    Connection conn = null;
    String sid2 = OiMaintainService.getPreviousVersionSid(sid);
    try {
      SelSQL.append("SELECT site from tf_test_parameter_ws where sid=? " +
                    "union SELECT site from tf_test_parameter_ft where sid=? " +
                    "union SELECT site from tf_test_parameter_pbc where sid=? ");
      conn = DBConnection.getConnection();
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
      DBConnection.close(conn);
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