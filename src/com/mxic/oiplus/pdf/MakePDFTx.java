package com.mxic.oiplus.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mxic.oiplus.oimaintain.AsignActionForm;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.TDSLogger;

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
      String fileName = pdfService.getFileName(conn, fm.getPd_body(), fm.getBrand(), fm.getVersion(), null, false, "_tx");
      PdfWriter writer = PdfWriter.getInstance(document,
                                               new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                   File.separator + fileName + ".pdf"));
      String pro_b=fm.getPd_body();
      String version=fm.getVersion();
      String br = fm.getBrand();
      String pkg_cmp = fm.getPackage_component()==null?"":fm.getPackage_component();
      
      writer.setPageEvent(new PageNumbersWatermark(writer, document, pro_b, br, version, "", "", false, true));

      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", PdfWriter.AllowPrinting);
      //writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));
      
      //產生另一個 dpf 檔案 (保全)
      PdfWriter writer_s = PdfWriter.getInstance(document,
              new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                  File.separator + fileName + "_s.pdf"));
      writer_s.setPageEvent(new PageNumbersWatermark(writer_s, document, pro_b, br, version, "", "", false, true));
      writer_s.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));

      document.open();
      //    ********************************第零段落****************************//
      pdfService.ProdWaferlevel(document, BasicFont, SmallFont,"0", jpgPath, fmtr, "_tx", conn,"");

      //********************************第一段落****************************//
      int section = 1;
      document.newPage();
      document.add(new Paragraph("1. PRODUCT ROUTE DEFINITION"));
      
      int wsCnt = 0;
      int ftCnt = 0;
      if(pkg_cmp.equals("M")){
	      //wsCnt = pdfService.WS_Product_Route_Mcp(document, BasicFont, SmallFont, fmtr, "", "_tx", conn, "");
	      //if (wsCnt>0) {document.newPage(); section++; }
	      ftCnt = pdfService.FT_Product_Route_Mcp(document, BasicFont, SmallFont, 
	    		  ""+section, fmtr, "", "_tx", conn, "");
	      if (ftCnt>0) {section++; }    	  
      }else{
	      wsCnt = pdfService.WS_Product_Route(document, BasicFont, SmallFont, 
	    		  								  fmtr, "", "_tx", conn, "");
	      if (wsCnt>0) {document.newPage(); section++; }
	      ftCnt = pdfService.FT_Product_Route(document, BasicFont, SmallFont, 
					  ""+section, fmtr, "", "_tx", conn, "");
	      if (ftCnt>0) {section++; }
      }
      
      
      int aviCnt = pdfService.AVI_List(document, BasicFont, SmallFont, "1-"+section, ""+fm.getSid(), "", "_tx", conn);

      //********************************第二段落****************************//
      if (ftCnt>0 || aviCnt>0) document.newPage();
      document.add(new Paragraph("2. ROUTE DEFINITION"));
      par = new Paragraph(new Chunk("2-1. WS ROUTE DEFINITION", BasicFont));
      par.setSpacingAfter(5);
      document.add(par);
      PdfPTable tableWS = new PdfPTable(16);
      tableWS.setSpacingBefore(5);
      tableWS.setWidthPercentage(100);
      tableWS.addCell(new Phrase(new Chunk("Route",SmallFont)));
      for (int i = 1; i < 16; i++) {
        tableWS.addCell(new Phrase(new Chunk("Step"+i,SmallFont)));
      }
      tableWS = pdfService.tf_route_master_WSFT(tableWS, SmallFont,
                                                fmtr, "", "_tx", "W", false, conn, "");
      document.add(tableWS);

      // FT Route
      PdfPTable tableFT = new PdfPTable(16);
      tableFT.setWidthPercentage(100);
      tableFT.addCell(new Phrase(new Chunk("Route",SmallFont)));
      for (int k = 1; k < 16; k++) {
        tableFT.addCell(new Phrase(new Chunk("Step"+k,SmallFont)));
      }
      par = new Paragraph(new Chunk("2-2. FT ROUTE DEFINITION", BasicFont));
      par.setSpacingAfter(5);
      document.add(par);
      tableFT = pdfService.tf_route_master_WSFT(tableFT, SmallFont,
                                                fmtr, "", "_tx", "P", false, conn, "");
      tableFT.setSpacingBefore(5);
      document.add(tableFT);

      //********************************第三段落****************************//
      pdfService.Yield(document, BasicFont, SmallFont,"3", jpgPath, fmtr, "_tx", conn,"");

      //********************************第四段落****************************//
      pdfService.BasicInfo(document, BasicFont, SmallFont, fmtr, ""+fm.getSid(), "", "_tx", conn, "");

      //********************************第五段落****************************//
      pdfService.WS_FT_Test_FLow_Chart(document, BasicFont,"5", jpgPath, ""+fm.getSid(), "_tx", conn);

      //********************************第六段落****************************//
      section = 6;
      boolean hasData =
    	  pdfService.WipControl(document, pro_b, BasicFont, ""+section, ""+fm.getSid(), "_tx", conn);
      if (hasData)
    	  section++;

      //********************************第七段落****************************//
      hasData = false;
    	  //pdfService.Comment_Image(document, ""+section, jpgPath, ""+fm.getSid(), "_tx", conn);
      if (hasData)
    	  section++;

      //********************************第八段落****************************//
      if(pkg_cmp.equals("M")){
    	  pdfService.BOM_Mcp_Table(document,SmallFont, ""+section, ""+fm.getSid(), "_tx", conn);
      }else{
    	  pdfService.BOM_Table(document,SmallFont, ""+section, ""+fm.getSid(), "_tx", conn);
      }
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