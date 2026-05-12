package com.mxic.oiplus.xtrarom.pdf;

import java.sql.Connection;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class MakePDFVendorDiff {
  public MakePDFVendorDiff() {
  }

  public static boolean makePDF(ProTestRouteBeanAF fm, Document document, String status) {
    boolean IdFlag = true;
    Connection conn = null;
//    Document document = new Document(PageSize.LETTER, 50, 50, 100, 72);
//    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
//    String fileName = pdfService.getFileName(fm.getProductbody(), fm.getBrand(), fm.getVendor(), null, true);
//    String fileName = fm.getProductbody() + fm.getBrand() + "V" + fm.getVersion() +"V";

    try {
      BaseFont bfChinese = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      Font BasicFont = new Font(bfChinese, 8, Font.NORMAL);

      conn = DBConnection.getConnection();
/*
      PdfWriter writer = PdfWriter.getInstance(document,
                                               new FileOutputStream(pdfProp.getProperty("pdf.dir") + File.separator + fileName + ".pdf"));
*/
      String pd_body=fm.getProductbody();
      String version=fm.getVersion();
      String sid = String.valueOf(fm.getSid());
      String brand = fm.getBrand();

//      String[] approve = pdfService.GetCoverPage(pd_body,brand,version,fm.getVendor(),conn);
//      writer.setPageEvent(new PageNumbersWatermark(writer,document,pd_body,version,approve[0],approve[1]));
      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
//      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC",
//                           PdfWriter.AllowPrinting);

      document.open();
      //********************************第一段落****************************//
      String oldV=String.valueOf(Integer.parseInt(fm.getVersion())-1);
      document.add(new Paragraph(fm.getProductbody()+ " / "+ fm.getBrand()+" Vendor Differences between version "+ fm.getVersion() +" and "+oldV));
      document.add(new Paragraph("1. All Vendors "));
      PdfPTable table1 = new PdfPTable(1);
      table1.setSpacingBefore(5);
      table1.setWidthPercentage(100);
      table1 = PDFdiffService.SubCon(table1, BasicFont, sid, status,pd_body,brand,version);
      document.add(table1);

      //********************************第二段落****************************//
      document.add(new Paragraph("2. New Vendors"));

      float[] widths = {20,80};
      PdfPTable table2 = new PdfPTable(widths);
      table2.setSpacingBefore(5);
      table2.setWidthPercentage(100);
      table2.addCell(new Phrase(new Chunk("Difference",BasicFont)));
      table2.addCell(new Phrase(new Chunk("Vendor Name",BasicFont)));
      table2=PDFdiffService.NewVendor(table2, BasicFont, sid, status,pd_body,brand,version);
      document.add(table2);

      //********************************第三段落****************************//
      if (!version.equals("0")) {
        document.add(new Paragraph("3. Old Vendors"));
        PdfPTable table3 = new PdfPTable(widths);
        table3.setSpacingBefore(5);
        table3.setWidthPercentage(100);
        table3.addCell(new Phrase(new Chunk("Difference", BasicFont)));
        table3.addCell(new Phrase(new Chunk("Vendor Name", BasicFont)));
        table3 = PDFdiffService.OldVendor(table3, BasicFont, sid, status, pd_body, brand, version);
        document.add(table3);
      }
    } catch (Exception ex) {
//      document.close();
      DBConnection.close(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      IdFlag = false;
    } finally {
        DBConnection.close(conn);
/*
      try{
        document.close();
      } catch (Exception e){
        e.printStackTrace();
      } finally {
        DBConnection.close(conn);
      }
*/
    }
    return IdFlag;
  }
}