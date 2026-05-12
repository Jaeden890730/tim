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
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.TDSLogger;

public class MakeVendorPDF {
  public MakeVendorPDF() {
  }

  public static boolean makePDF(ProTestRouteBeanAF fm, String status) {
    boolean IdFlag = true;
    Connection conn = null;
	    try {
	    	conn = DBConnection.getConnection();
	    	IdFlag = makePDF(conn,fm,status);
	    } catch (Exception ex) {
	        //document.close();
	        DBConnection.close(conn);
	        ex.fillInStackTrace();
	        TDSLogger.println(ex.getMessage());
	        IdFlag = false;
	      } finally {
	        /*當放到weblogic時，document.close要加上try catch，如放在TOMCAT則不用。*/
	        try {
	          //document.close();
	        } catch (Exception e){
	          e.printStackTrace();
	        } finally {
	          DBConnection.close(conn);
	        }
	      }
	      return IdFlag;
  }
  
  
  public static boolean makePDF(Connection conn, ProTestRouteBeanAF fm, String status) {
    boolean IdFlag = true;
    //Connection conn = null;
    Paragraph par = null;
    String table = "_tx";
    if (status.equals("R")) table = "";

    Document document = new Document(PageSize.LETTER, 40, 40, 90, 50);
    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
    String jpgPath = pdfProp.getValue("jpg"+table+".path");
    
    
    
    
    try {
      BaseFont bfChinese =
          BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      Font BasicFont = new Font(bfChinese, 8, Font.NORMAL);
      Font SmallFont= new Font( bfChinese,6,Font.NORMAL);

      //conn = DBConnection.getConnection();
      String fileName = pdfService.getFileName(conn, fm.getProductbody(), fm.getBrand(), fm.getVersion(), fm.getVendor(), false, table);
   // for e8049 data sharing, 20090828 by Robin, TEST1 doesn't need efile 
      String textFilename = pdfProp.getProperty("pdf.dir") + File.separator + fileName;
      
      PdfWriter writer = PdfWriter.getInstance(document,
                                               new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                   File.separator + fileName + ".pdf"));
      String pro_b=fm.getProductbody();
      String version=fm.getVersion();
      String brand = fm.getBrand();
      String pkg_cmp = fm.getPackage_component()==null?"":fm.getPackage_component();
      if (pkg_cmp.equals(""))
    	  pkg_cmp = OiMaintainService.getPackageComponent(conn,pro_b, brand);//20160420
      
      String[] approve = pdfService.GetCoverPage(pro_b,brand,version,fm.getVendor(),conn);
      writer.setPageEvent(new PageNumbersWatermark(writer,document,pro_b,brand,version,approve[0],approve[1],true,true));

      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", PdfWriter.AllowPrinting);
      //lai-20110704 writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting));
      //writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));
      
      //產生另一個 dpf 檔案 (保全)
      PdfWriter writer_s = PdfWriter.getInstance(document,
              new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                  File.separator + fileName + "_s.pdf"));
      writer_s.setPageEvent(new PageNumbersWatermark(writer_s,document,pro_b,brand,version,approve[0],approve[1],true,true));
      writer_s.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));
      
      
      document.open();

      PdfPTable table1 = new PdfPTable(5);
      table1.setTotalWidth(100);
      table1.setWidthPercentage(100);

      float[] tmp = {5,12,13,12,60};//每個column的大小百分比先寫在數字陣列裡，各個數字加起來要等於100
      table1.setWidths(tmp);//再放入.setWidths(
      //從左到右寫入欄位名稱
      table1.addCell(new Phrase(new Chunk("Rev", SmallFont)));
      table1.addCell(new Phrase(new Chunk("Case Number", SmallFont)));
      table1.addCell(new Phrase(new Chunk("Effective Date", SmallFont)));
      table1.addCell(new Phrase(new Chunk("Applicant", SmallFont)));
      table1.addCell(new Phrase(new Chunk("Change Description", SmallFont)));

      //從TABLE撈資料寫入PDF TABLE時執行以下Function
      //將會用到的物件傳入，如 PDF table,Font,Connection,還有參數如Product_body,Brand,version
      table1 = pdfService.CoverPage(table1, pro_b, brand, version, SmallFont, conn, true);

      //table的動作都完成後，記得要把table加入document中:document.add(table)
      document.add(table1);
      document.newPage();
      
      //********************************第零段落****************************//
      pdfService.ProdWaferlevel(document, BasicFont, SmallFont,"0", jpgPath, fm, table, conn,textFilename);


      //********************************第一段落****************************//
      document.add(new Paragraph("1. PRODUCT ROUTE DEFINITION"));

      int section =1;
      
      int wsCnt = 0;
      int ftCnt = 0;
      if(pkg_cmp.equals("M")){
	      //wsCnt = pdfService.WS_Product_Route_Mcp(document, BasicFont, SmallFont, fm, fm.getVendor(), table, conn, textFilename);	
	      //if (wsCnt > 0) {document.newPage(); section++;}
	      ftCnt = pdfService.FT_Product_Route_Mcp(document, BasicFont, SmallFont,
	    		  			""+section, fm, fm.getVendor(), table, conn, textFilename);
	      if (ftCnt > 0) {section++;}    	  
      }else{	      
	      wsCnt = pdfService.WS_Product_Route(document, BasicFont, SmallFont,
	    		  								  fm, fm.getVendor(), table, conn, textFilename);	
	      if (wsCnt > 0) {document.newPage(); section++;}
	      ftCnt = pdfService.FT_Product_Route(document, BasicFont, SmallFont,
	                                    ""+section, fm, fm.getVendor(), table, conn, textFilename);
	      if (ftCnt > 0) {section++;}
      }

      int aviCnt = com.mxic.oiplus.pdf.pdfService.AVI_List(document, BasicFont, SmallFont, "1-"+section,
                                               ""+fm.getSid(), fm.getVendor(), table, conn);

      //********************************第二段落****************************//
      if (ftCnt>0 || aviCnt>0)document.newPage();
      document.add(new Paragraph("2. ROUTE DEFINITION："));

      if (wsCnt>0){
        par = new Paragraph(new Chunk("2-1. WS ROUTE DEFINITION", BasicFont));
        par.setSpacingAfter(5);
        document.add(par);

        PdfPTable tableWS = new PdfPTable(16);
        tableWS.setWidthPercentage(100);

        tableWS.addCell(new Phrase(new Chunk("Route", SmallFont)));
        for (int i = 1; i < 16; i++) {
          tableWS.addCell(new Phrase(new Chunk("Step" + i, SmallFont)));
        }

        tableWS = pdfService.tf_route_master_WSFT(tableWS, SmallFont, fm, fm.getVendor(), 
        										  table, "W", false, conn, textFilename);
        tableWS.setSpacingBefore(5);
        document.add(tableWS);
      }

      if (ftCnt>0){
        String seq = "1";

        PdfPTable tableFT = new PdfPTable(16);
        tableFT.setWidthPercentage(100);
        tableFT.addCell(new Phrase(new Chunk("Route", SmallFont)));
        for (int k = 1; k < 16; k++) {
          tableFT.addCell(new Phrase(new Chunk("Step" + k, SmallFont)));
        }

        if (wsCnt>0) seq = "2";
        par = null;
        par = new Paragraph(new Chunk("2-"+seq+". FT ROUTE DEFINITION", BasicFont));
        par.setSpacingAfter(5);
        document.add(par);
        tableFT = pdfService.tf_route_master_WSFT(tableFT, SmallFont, fm, fm.getVendor(), 
        										  table, "P", false, conn, textFilename);
        tableFT.setSpacingBefore(5);
        document.add(tableFT);
      }

      //********************************第三段落****************************//
      com.mxic.oiplus.pdf.pdfService.Yield(document,BasicFont, SmallFont, "3", jpgPath, fm, table, conn,textFilename);

      //********************************第四段落****************************//
      com.mxic.oiplus.pdf.pdfService.BasicInfo(document, BasicFont, SmallFont, fm, fm.getSid(), fm.getVendor(), table, conn, textFilename);

      //********************************第五段落****************************//
      pdfService.WS_FT_Test_FLow_Chart(document,BasicFont, "5", jpgPath, fm.getSid(), table, conn);

      //********************************第六段落****************************//
      section = 6;
      boolean hasData = 
    	  pdfService.WipControl(document,pro_b,BasicFont, ""+section, fm.getSid(), table, conn);
      if (hasData)
    	  section++;

      //********************************第七段落****************************//
      //pdfService.Comment_Image(document, ""+section, jpgPath, fm.getSid(), table, conn);

      MakeVendorCoverPage.makePDF(fm, document, status, conn);

    } catch (Exception ex) {
      document.close();
      //DBConnection.close(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      IdFlag = false;
    } finally {
      /*當放到weblogic時，document.close要加上try catch，如放在TOMCAT則不用。*/
      try {
        document.close();
      } catch (Exception e){
        e.printStackTrace();
      } finally {
        //DBConnection.close(conn);
      }
    }
    return IdFlag;
  }
}