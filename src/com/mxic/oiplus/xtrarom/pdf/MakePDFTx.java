package com.mxic.oiplus.xtrarom.pdf;

import java.io.*;
import java.sql.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.mxic.oiplus.oimaintain.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

public class MakePDFTx {

  public MakePDFTx() {
  }

  public static boolean makePDF(AsignActionForm fm) {
    boolean IdFlag = true;
    Connection conn = null;
    Paragraph par = null;
    Document document = new Document(PageSize.LETTER, 40, 40, 90, 50);
    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
    String jpgPath = pdfProp.getValue("jpg_tx.path");
    String jpgroutePath = pdfProp.getValue("jpg_route.path");
    String fileName = pdfService.getFileName(fm.getPd_body(), fm.getBrand(), fm.getVersion(), null, false, "_tx");
    ProTestRouteBeanAF fmtr = new ProTestRouteBeanAF();
    fmtr.setSid(""+fm.getSid());
    fmtr.setProductbody(fm.getPd_body());
    fmtr.setBrand(fm.getBrand());
    fmtr.setVersion(fm.getVersion());
    fmtr.setVendor("");

    try {
      BaseFont bfChinese = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      Font BasicFont = new Font(bfChinese, 8, Font.NORMAL);
      Font SmallFont= new Font( bfChinese, 6, Font.NORMAL);

      conn = DBConnection.getConnection();
      PdfWriter writer = PdfWriter.getInstance(document,
                                               new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                   File.separator + fileName + ".pdf"));
      String pro_b=fm.getPd_body();
      String version=fm.getVersion();
      String br = fm.getBrand();
      writer.setPageEvent(new PageNumbersWatermark(writer, document, pro_b, br, version, "", "", false, true));

      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", PdfWriter.AllowPrinting);
      
      //產生另一個 dpf 檔案 (保全)
      PdfWriter writer_s = PdfWriter.getInstance(document,
              new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                  File.separator + fileName + "_s.pdf"));
      writer_s.setPageEvent(new PageNumbersWatermark(writer_s, document, pro_b, br, version, "", "", false, true));
      writer_s.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));

      document.open();

      //********************************第一段落****************************//
      document.add(new Paragraph("1. PRODUCT ROUTE DEFINITION"));
      int section = 1;
      int wsCnt = pdfService.WS_Product_Route(document, BasicFont, SmallFont, 
    		  								  fmtr, "", "_tx", conn, "");
      if (wsCnt > 0) {document.newPage(); section++; }
      //1-2
      int ftCnt = pdfService.FT_Product_Route(document, BasicFont, SmallFont, 
    		  								  ""+section, fmtr, "", "_tx", conn, "");
      if (ftCnt > 0) {document.newPage(); section++; }
      //1-3
      int ftreCnt = pdfService.FT_Product_ReRoute(document, BasicFont, SmallFont, ""+section, ""+fm.getSid(), "", "_tx", conn);
      if (ftreCnt > 0) { section++; }
      //avi
      int aviCnt = com.mxic.oiplus.pdf.pdfService.AVI_List(document, BasicFont, SmallFont, "1-"+section, ""+fm.getSid(), "", "_tx", conn);
      if (ftreCnt>0 || aviCnt>0) document.newPage();

      //********************************第二段落****************************//
      // WS Route
      document.add(new Paragraph("2. ROUTE DEFINITION"));
      par = new Paragraph(new Chunk("2-1. WS ROUTE DEFINITION", BasicFont));
      par.setSpacingAfter(5);
      document.add(par);
      float[] widths = {5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,20};
      PdfPTable tableWS = new PdfPTable(widths);//12
      tableWS.setSpacingBefore(5);
      tableWS.setWidthPercentage(100);
      tableWS.addCell(new Phrase(new Chunk("Route",SmallFont)));//BasicFont
      for (int i = 1; i < 16; i++) {
        tableWS.addCell(new Phrase(new Chunk("Step"+i,SmallFont)));//BasicFont
      }
      tableWS.addCell(new Phrase(new Chunk("Remark",SmallFont)));//BasicFont
      tableWS = com.mxic.oiplus.pdf.pdfService.tf_route_master_WSFT(tableWS, 
    		  							SmallFont, fmtr, "", "_tx", "W", true, conn, "");
      document.add(tableWS);

      // FT Route
      PdfPTable tableFT = new PdfPTable(widths);//12
      tableFT.setWidthPercentage(100);
      tableFT.addCell(new Phrase(new Chunk("Route",SmallFont)));//BasicFont
      for (int k = 1; k < 16; k++) {
        tableFT.addCell(new Phrase(new Chunk("Step"+k,SmallFont)));//BasicFont
      }
      tableFT.addCell(new Phrase(new Chunk("Remark",SmallFont)));//BasicFont
      par = new Paragraph(new Chunk("2-2. FT ROUTE DEFINITION", BasicFont));
      par.setSpacingAfter(5);
      document.add(par);
      tableFT = com.mxic.oiplus.pdf.pdfService.tf_route_master_WSFT(tableFT, 
    		  							SmallFont, fmtr, "", "_tx", "P", true, conn, "");
      tableFT.setSpacingBefore(5);
      document.add(tableFT);

      //2-3
      pdfService.Main_Route_Sub(document, BasicFont, SmallFont, "2-3", ""+fm.getSid(), "_tx", conn);
      //document.newPage();

      //2-4
      pdfService.Main_Route_Rework(document, BasicFont, SmallFont, "2-4", ""+fm.getSid(), "_tx", conn);
      //document.newPage();

      //********************************第三段落****************************//
      com.mxic.oiplus.pdf.pdfService.Yield(document, BasicFont, SmallFont, "3", jpgPath, fmtr, "_tx", conn,"");

      //********************************第四段落****************************//
      com.mxic.oiplus.pdf.pdfService.BasicInfo(document, BasicFont, SmallFont, fmtr, ""+fm.getSid(), "", "_tx", conn, "");

      //********************************第五段落****************************//
      //pdfService.WS_FT_Test_FLow_Chart(document, "5", jpgPath, ""+fm.getSid(), "_tx", conn);
      pdfService.WS_FT_Test_FLow_Chart_New(document, BasicFont, "5", jpgroutePath, ""+fm.getSid(), "_tx", conn);

      //********************************第六段落****************************//
      section = 6;
      boolean hasData =
    	  com.mxic.oiplus.pdf.pdfService.WipControl(document,pro_b,BasicFont, ""+section, ""+fm.getSid(), "_tx", conn);
      if (hasData)
    	  section++;

      //********************************第七段落****************************//
      hasData = 
    	  com.mxic.oiplus.pdf.pdfService.Comment_Image(document, ""+section, jpgPath, ""+fm.getSid(), "_tx", conn);
      if (hasData)
    	  section++;

      //********************************第八段落****************************//
      pdfService.BOM_Table(document, SmallFont, ""+section, ""+fm.getSid(), "_tx", conn);

      //********************************第九段落****************************//
//      pdfService.BOM_RE_Table(document, SmallFont, "8", ""+fm.getSid(), "_tx", conn);
    } catch (Exception ex) {
      document.close();
      DBConnection.close(conn);
      ex.printStackTrace();
      TDSLogger.println(ex.getMessage());
      IdFlag = false;
    } finally {
      /*當放到weblogic時，document.close要加上try catch，如放在TOMCAT則不用。*/
      try {
        document.close();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        DBConnection.close(conn);
      }
    }
    return IdFlag;
  }
}