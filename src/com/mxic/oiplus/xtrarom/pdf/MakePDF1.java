package com.mxic.oiplus.xtrarom.pdf;

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
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.TDSLogger;

public class MakePDF1 {
  public MakePDF1() {
  }

  public static boolean makePDF(ProTestRouteBeanAF fm) {
    boolean IdFlag = true;
    Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			IdFlag = makePDF(conn,fm);
		} catch (Exception ex) {
			// document.close();
			DBConnection.close(conn);
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			IdFlag = false;
		} finally {
			/* 當放到weblogic時，document.close要加上try catch，如放在TOMCAT則不用。 */
			try {
				// document.close();
			} catch (Exception e) {
			} finally {
				DBConnection.close(conn);
			}
		}
		return IdFlag;

	}
  
  public static boolean makePDF(Connection conn, ProTestRouteBeanAF fm) {
    boolean IdFlag = true;
    //Connection conn = null;
    Paragraph par = null;
    String product_body = fm.getProductbody();
    String brand = fm.getBrand();
    String version = fm.getVersion();

    String fileName = null;
    fileName = pdfService.getFileName(product_body, brand, version, null, false, "");

    //Document(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom)
    Document document = new Document(PageSize.LETTER, 40, 40, 90, 50);
    //PDF檔案寫入的路徑
    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
    //圖檔的路徑
    String jpgPath = pdfProp.getValue("jpg.path");
    String jpgroutePath = pdfProp.getValue("jpg_route.path");//ori-jpg.path
    try {
      //中文編碼為UniCNS-UCS2-H，字型有MHei-Medium與MSung-Light
      //createFont(字型名稱, 編碼名稱, boolean embedded)
      BaseFont bfChinese =
          BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);

      //Font(BaseFont bf, float size, int style)
      Font BasicFont = new Font(bfChinese, 8, Font.NORMAL);
      Font SmallFont = new Font(bfChinese, 6, Font.NORMAL);

      //conn = DBConnection.getConnection();
      TDSLogger.println("create file "+pdfProp.getProperty("pdf.dir") +
                                                   File.separator + fileName + ".pdf");
      PdfWriter writer = PdfWriter.getInstance(document,
                                               new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                   File.separator + fileName + ".pdf"));
      //當document執行open,close時會trigger PageNumbersWatermark 裡面的event
      //例如每開新的一頁就寫入表頭，頁數，浮水印等等）
      String[] approve = pdfService.GetCoverPage(product_body,brand,version,null,conn);
      writer.setPageEvent(new PageNumbersWatermark(writer, document, product_body, brand, version,approve[0],approve[1], false, true));

      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", PdfWriter.AllowPrinting);
      
      //產生另一個 dpf 檔案 (保全)
      PdfWriter writer_s = PdfWriter.getInstance(document,
              new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                  File.separator + fileName + "_s.pdf"));
      writer_s.setPageEvent(new PageNumbersWatermark(writer_s, document, product_body, brand, version,approve[0],approve[1], false, true));
      writer_s.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));

      //開啟document
      document.open();

      //********************************第一段落****************************//
      document.add(new Paragraph("1. PRODUCT ROUTE DEFINITION"));
      int section = 1;
      // 1-1 WS Route
      int wscnt = pdfService.WS_Product_Route(document, BasicFont, SmallFont, 
    		  								  fm, "", "", conn, "");
      if (wscnt>0) {document.newPage(); section++; }
      //1-2 FT Route
      int ftcnt = pdfService.FT_Product_Route(document, BasicFont, SmallFont, ""+section, 
    		  								  fm, "", "", conn, "");
      if (ftcnt>0) {document.newPage(); section++; }

      //1-3 Recycle Route
      int ftreCnt = pdfService.FT_Product_ReRoute(document, BasicFont, SmallFont, ""+section, ""+fm.getSid(), "", "", conn);
      if (ftreCnt>0) {section++; }

      // AVI, use NVM's AVI_List()
      int aviCnt = com.mxic.oiplus.pdf.pdfService.AVI_List(document, BasicFont, SmallFont, "1-"+section, ""+fm.getSid(), "", "", conn);

      if (ftreCnt>0 || aviCnt>0) document.newPage();
//      pdfService.PBC_Product_Route(document, BasicFont, SmallFont, "3", fm.getSid(), "", "", conn);

      //********************************第二段落****************************//
      // WS Route
      document.add(new Paragraph("2. ROUTE DEFINITION"));
      par = new Paragraph(new Chunk("2-1. WS ROUTE DEFINITION", BasicFont));
      par.setSpacingAfter(5);
      document.add(par);
      float[] widths = {5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,20};
      PdfPTable tableWS = new PdfPTable(widths);//12
      tableWS.addCell(new Phrase(new Chunk("Route", SmallFont)));//BasicFont
      for (int i = 1; i < 16; i++) {
        tableWS.addCell(new Phrase(new Chunk("Step" + i, SmallFont)));//BasicFont
      }
      tableWS.addCell(new Phrase(new Chunk("Remark", SmallFont)));//BasicFont
      tableWS = com.mxic.oiplus.pdf.pdfService.tf_route_master_WSFT(tableWS, 
    		  					SmallFont, fm, "", "", "W", true, conn, "");
      tableWS.setWidthPercentage(100);
      tableWS.setSpacingBefore(0);
      document.add(tableWS);

      // FT Route
      PdfPTable tableFT = new PdfPTable(widths);//12
      tableFT.setWidthPercentage(100);
      tableFT.addCell(new Phrase(new Chunk("Route", SmallFont)));//BasicFont
      for (int k = 1; k < 16; k++) {
        tableFT.addCell(new Phrase(new Chunk("Step" + k, SmallFont)));//BasicFont
      }
      tableFT.addCell(new Phrase(new Chunk("Remark", SmallFont)));//BasicFont
      par = null;
      par = new Paragraph(new Chunk("2-2. FT ROUTE DEFINITION", BasicFont));
      par.setSpacingAfter(5);
      document.add(par);
      tableFT = com.mxic.oiplus.pdf.pdfService.tf_route_master_WSFT(tableFT, 
    		  					SmallFont, fm, "", "", "P", true, conn, "");
      tableFT.setSpacingBefore(0);
      document.add(tableFT);

      //2-3
      pdfService.Main_Route_Sub(document, BasicFont, SmallFont, "2-3", ""+fm.getSid(), "", conn);
      //document.newPage();

      //2-4
      pdfService.Main_Route_Rework(document, BasicFont, SmallFont, "2-4", ""+fm.getSid(), "", conn);
      //document.newPage();

      //********************************第三段落****************************//
      com.mxic.oiplus.pdf.pdfService.Yield(document, BasicFont, SmallFont, "3", jpgPath, fm, "", conn,"");

      //********************************第四段落****************************//
      com.mxic.oiplus.pdf.pdfService.BasicInfo(document, BasicFont, SmallFont, fm, fm.getSid(), "", "", conn, "");

      //********************************第五段落****************************//
      //pdfService.WS_FT_Test_FLow_Chart(document, "5", jpgPath, fm.getSid(), "", conn);
      pdfService.WS_FT_Test_FLow_Chart_New(document, BasicFont, "5", jpgroutePath, fm.getSid(), "", conn);

      //********************************第六段落****************************//
      section = 6;
      boolean hasData = 
    	  com.mxic.oiplus.pdf.pdfService.WipControl(document,product_body,BasicFont, ""+section, ""+fm.getSid(), "", conn);
      if (hasData)
    	  section++;

      //********************************第七段落****************************//
      hasData = 
    	  com.mxic.oiplus.pdf.pdfService.Comment_Image(document, ""+section, jpgPath, fm.getSid(), "", conn);
      if (hasData)
    	  section++;

      //********************************第八段落****************************//
      pdfService.BOM_Table(document, SmallFont, ""+section, fm.getSid(), "", conn);

      //********************************第八段落****************************//
//      pdfService.BOM_RE_Table(document,SmallFont,  "7", fm.getSid(), "", conn);
    } catch (Exception ex) {
      document.close();
     // DBConnection.close(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      IdFlag = false;
    } finally {
      /*當放到weblogic時，document.close要加上try catch，如放在TOMCAT則不用。*/
      try {
        document.close();
      } catch (Exception e) {
      } finally {
       // DBConnection.close(conn);
      }
    }
    return IdFlag;
  }
}