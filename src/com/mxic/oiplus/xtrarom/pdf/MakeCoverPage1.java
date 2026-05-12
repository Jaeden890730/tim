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

/*MakeCoverPage1是製作廠內OI的Cover Page */
public class MakeCoverPage1 {
  public MakeCoverPage1() {
  }

  public static boolean makePDFCoverPage(ProTestRouteBeanAF fm, String vendor, String status) {
    boolean IdFlag = true;
    Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			IdFlag = makePDFCoverPage(conn, fm, vendor, status);

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
			}
			DBConnection.close(conn);
		}
		return IdFlag;

	}
  

/*所需參數為ProTestRouteBeanAF(ActionForm)與檔案名稱(String)*/
  public static boolean makePDFCoverPage(Connection conn, ProTestRouteBeanAF fm, String vendor, String status) {
    boolean IdFlag = true;
    //Connection conn = null;
    //Document(Rectangle pageSize, float marginLeft, float marginRight, float marginTop, float marginBottom)
    Document document = new Document(PageSize.LETTER, 40, 40, 90, 50);
    //PDF檔案寫入的路徑
    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
    //從參數d的ACTIONFORM中取得Product_Body,Brand,Version
    String pro_b=fm.getProductbody();
    String br=fm.getBrand();
    String version=fm.getVersion();
    String fileName = null;
    if (status.equals("R"))
    	fileName = pdfService.getFileName(pro_b, br, version, vendor, true, "");
    else
    	fileName = pdfService.getFileName(pro_b, br, version, vendor, true, "_tx");
    try {
      //中文編碼為UniCNS-UCS2-H，字型有MHei-Medium與MSung-Light
      //createFont(字型名稱, 編碼名稱, boolean embedded)
      BaseFont bfChinese =
          BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      //Font(BaseFont bf, float size, int style)
      Font SmallFont= new Font( bfChinese,8,Font.NORMAL);
      //conn = DBConnection.getConnection();

      PdfWriter writer =
          PdfWriter.getInstance(document, new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                  File.separator + fileName + ".pdf"));
      //當document執行open,close時會trigger PageNumbersWatermark 裡面的event
      //例如每開新的一頁就寫入表頭，頁數，浮水印等等）
      String[] approve = pdfService.GetCoverPage(pro_b,br,version,vendor,conn);
      writer.setPageEvent(new PageNumbersWatermark(writer,document,pro_b,br,version,approve[0],approve[1], true, true));

      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", PdfWriter.AllowPrinting);
      
      //產生另一個 dpf 檔案 (保全)
      PdfWriter writer_s =
          PdfWriter.getInstance(document, new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                  File.separator + fileName + "_s.pdf"));
      writer_s.setPageEvent(new PageNumbersWatermark(writer_s,document,pro_b,br,version,approve[0],approve[1], true, true));
      writer_s.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));

      //開啟document
      document.open();

      //********************************第一段落****************************//
      document.add(new Paragraph("Modification history"));
      PdfPTable table1 = new PdfPTable(5);
      table1.setSpacingBefore(5);
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
      table1 = pdfService.CoverPage(table1, pro_b, br, version, SmallFont, conn, false);

      //table的動作都完成後，記得要把table加入document中:document.add(table)
      document.add(table1);
      document.newPage();
      if (vendor == null) {
    	  MakePDFVendorDiff.makePDF(fm, document, status);
    	  document.newPage();
    	  MakePDFDiff.makePDF(fm.getSid(), fm.getProductbody(), fm.getBrand(), fm.getVersion(), status, document);
      }
/*
      else {
    	  MakeVendorCoverPage.makePDF(fm, document);
      }
*/
    } catch (Exception ex) {
      document.close();
     // DBConnection.close(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      IdFlag = false;
    } finally {
      /*當放到weblogic時，document.close要加上try catch，如放在TOMCAT則不用。*/
      try{
        document.close();
      }catch(Exception e){
      }
      //DBConnection.close(conn);
    }
    return IdFlag;
  }
}