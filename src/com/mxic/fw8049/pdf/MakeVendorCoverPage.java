package com.mxic.fw8049.pdf;

import java.sql.Connection;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.mxic.oiplus.oimaintain.EditiionCompareService;
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class MakeVendorCoverPage {
  public MakeVendorCoverPage() {
  }

  public static boolean makePDF(ProTestRouteBeanAF fm, Document document, String status, Connection conn) {
    boolean IdFlag = true;
    //Connection conn = null;
    try {
      BaseFont bfChinese = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      Font LargeFont = new Font(bfChinese, 16, Font.NORMAL);
      Font RedLargeFont = new Font(bfChinese, 16, Font.BOLD);
           RedLargeFont.setColor(0xEE,0,0);
      Font BasicFont = new Font(bfChinese, 8, Font.NORMAL);
      Font BasicBoldFont = new Font(BaseFont.createFont("Helvetica", BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 10, Font.NORMAL);
      Font SmallFont = new Font( bfChinese,6,Font.NORMAL);
      Font RedSmallFont = new Font( bfChinese,6,Font.NORMAL);
           RedSmallFont.setColor(0xEE,0,0);
      Font RedSmallFont8 = new Font( bfChinese,8,Font.NORMAL);
           RedSmallFont8.setColor(0xEE,0,0);           
      Font BasicRedFont = new Font(bfChinese, 12, Font.NORMAL);
           BasicRedFont.setColor(0xEE,0,0);

      //conn = DBConnection.getConnection();
      String pd_body=fm.getProductbody();
      String version=fm.getVersion();
      String sid = String.valueOf(fm.getSid());
      String brand = fm.getBrand();
      String site=fm.getVendor();
      //String productType = OiMaintainService.getProductType(sid);

      String sid2 = OiMaintainService.getPreviousVersionSid(conn, sid);
/*
      String[] approve = pdfService.GetCoverPage(pd_body,brand,version,fm.getVendor(),conn);
      writer.setPageEvent(new PageNumbersWatermark(writer,document,pd_body,version,approve[0],approve[1]));

      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", PdfWriter.AllowPrinting);
*/
      document.newPage();
      document.add(new Paragraph("【以下文件內容為此次進版差異說明】", RedLargeFont));

      if (Integer.parseInt(fm.getVersion()) == 0) {
        document.add(new Paragraph("本版為第一個版本，沒有差異資料。", LargeFont));
      }
      else {
    	  String pck_com = OiMaintainService.getPackageComponent(conn,pd_body, brand);  
    	boolean flag0 = EditiionCompareService.isPWLiff(conn, sid2, sid, status, 0);
        boolean flag01 = EditiionCompareService.isPWLiff(conn, sid, sid2, status, 1);   
        boolean flag02 = EditiionCompareService.isPWLiff(conn, sid, sid2, status, 2);
        boolean flag4;
        boolean flag6;
        if(pck_com.equals("M")){
        	flag4 = false;
        	flag6 = EditiionCompareService.isFTPDRMcpDiff(conn, sid, sid2, status, site);
        }else{
        	flag4 = EditiionCompareService.isWSPDRDiff(conn, sid, sid2, status, site);
        	flag6 = EditiionCompareService.isFTPDRDiff(conn, sid, sid2, status, site);
        }
        boolean flag3 = EditiionCompareService.isAVIDiff(conn, sid, sid2, status, site);
        boolean flag19 = EditiionCompareService.isYieldDiff(conn, sid, sid2, status);
        boolean flag20 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", status, "Hold");
        boolean flag201 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", status, "Dgrade");
        boolean flag21 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "FT", status, "");
        boolean flag7 = EditiionCompareService.isBADiff(conn, sid2, sid, status, 0);
        boolean flag8 = EditiionCompareService.isBADiff(conn, sid, sid2, status, 1);
        boolean flag81 = EditiionCompareService.isBADiff(conn, sid, sid2, status, 2);
        boolean flag18 = EditiionCompareService.isBACommentDiff(conn, sid, sid2, status);
        boolean flag9 = EditiionCompareService.isPDRDiff(conn, sid2, sid, status, 0);
        boolean flag10 = EditiionCompareService.isPDRDiff(conn, sid, sid2, status, 1);
        boolean flag101 = EditiionCompareService.isPDRDiff(conn, sid, sid2, status, 2);
        boolean flag11, flag12;
        if (status.equals("R")) {
          flag11 = EditiionCompareService.SearchDOC2(conn, sid, status, pd_body, brand, version, "Y");
          flag12 = EditiionCompareService.SearchDOC2(conn, sid, status, pd_body, brand, version, "T");
        } else {
          flag11 = EditiionCompareService.SearchDOC1(conn, sid, status, pd_body, brand, version, "Y");
          flag12 = EditiionCompareService.SearchDOC1(conn, sid, status, pd_body, brand, version, "T");
        }
//        document.open();

        //******************************** Summary ****************************//

        String oldV=String.valueOf(Integer.parseInt(fm.getVersion())-1);
        document.add(new Paragraph(pd_body + " / " + fm.getBrand() +
                                   " Differences between version " +  fm.getVersion() +
                                   " and "+oldV));

        if (flag0 || flag01 || flag02 || flag3  || flag4  || flag6  || 
        	flag9  || flag10 || flag101 ||
        	flag11 || flag19 || flag20 || flag201 || flag21 || 
        	flag7  || flag8 || flag81 || flag18 || 
        	flag12)
          document.add(new Paragraph("The following section(s) has difference (Wip Handling Control 不列入): ", LargeFont));
        else
          document.add(new Paragraph("本次進版依旺宏內部所需，內容與前一版並無差異 (Wip Handling Control 除外)。", LargeFont));

        if (flag0 || flag01 || flag02)
            document.add(new Paragraph("- 0. Product VS Prod Level Mapping",BasicFont));
        
        if (flag3 || flag4 || flag6)
          document.add(new Paragraph("- 1. WS & FT Product Route Definition",BasicFont));

        if (flag9 || flag10 || flag101)
          document.add(new Paragraph("- 2. Product VS Test Route Mapping",BasicFont));

        if (flag11 || flag19 || flag20 || flag201 || flag21)
          document.add(new Paragraph("- 3. Yield Criteria",BasicFont));

        if (flag7  || flag8 || flag81 || flag18)
          document.add(new Paragraph("- 4. Basic Information",BasicFont));

        if (flag12)
          document.add(new Paragraph("- 5. Test Flows",BasicFont));

        document.add(new Paragraph(" "));
        String[] approve = pdfService.GetCoverPage(pd_body, brand, version, null, conn);
		if(approve[3]!="")
			document.add(new Paragraph("ECN (" + approve[2] + ")生效，取代TECN:" + approve[3], BasicRedFont));
		document.add(new Paragraph(" "));
        
//      ********************************第零段落****************************//
        if (flag0 || flag01 || flag02){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("0. Product VS Prod Level Mapping "));

          float[] widths = {10,45,45};
          PdfPTable table1 = new PdfPTable(widths);
          table1.setSpacingBefore(5);
          table1.setWidthPercentage(100);
          table1.addCell(new Phrase(new Chunk("Diff", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Prod Level", BasicFont)));
          //table1.addCell(new Phrase(new Chunk("Brand", BasicFont)));
          //table1.addCell(new Phrase(new Chunk("BizType", BasicFont)));
          //table1.addCell(new Phrase(new Chunk("Wafer Grade", BasicFont)));
          //table1.addCell(new Phrase(new Chunk("Apply Type", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Priority", BasicFont)));
          table1 = PDFdiffService.ComparePWL(conn, table1, BasicFont, RedSmallFont8, sid2, sid, status, 0);
          table1 = PDFdiffService.ComparePWL(conn, table1, BasicFont, RedSmallFont8, sid, sid2, status, 1);
          table1 = PDFdiffService.ComparePWL(conn, table1, BasicFont, RedSmallFont8, sid, sid2, status, 2);
          document.add(table1);
        }

        //********************************第一段落****************************//
        if (flag3 || flag4 || flag6){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("1. WS & FT Proudct Route Definition"));
        }

        if (flag4){
          document.add(new Paragraph("- WS Proudct Route Definition "));
          PDFdiffService.CompareWSPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, site);
        }

        String section = null;
        if (flag6){
          if (flag4) section = "2";
          else section = "1";
          document.add(new Paragraph("- FT Product Route Definition "));
          if(pck_com.equals("M")){
        	  PDFdiffService.CompareFTPDFMcp(conn, document, SmallFont, RedSmallFont, sid, sid2, status, site, section);
          }else{  
        	  PDFdiffService.CompareFTPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, site, section);
          }	  
        }

        if (flag3) {
         if (section == null) section = "1";
         else if (section.equals("1")) section = "2";
         else if (section.equals("2")) section = "3";
         document.add(new Paragraph("- AVI Release Vendor "));
         PDFdiffService.CompareAVIPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, site, section);
       }

       //********************************第二段落****************************//
        if (flag9 || flag10 || flag101){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("2. Product VS Test Route Mapping "));

          float[] widths = {5,12,5,12,12,12,5,5,10,10,17};
          PdfPTable table1 = new PdfPTable(widths);
          table1.setWidthPercentage(100);
          table1.setSpacingBefore(5);
          table1.addCell(new Phrase(new Chunk("Diff", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Route", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Step Seq", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Step Name", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Conditions", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Temperature", BasicFont)));
          table1.addCell(new Phrase(new Chunk("抽測Test Mode", BasicFont)));
          table1.addCell(new Phrase(new Chunk("抽測條件", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Rework Step", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Condition", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Remark", BasicFont)));
          table1 = PDFdiffService.ComparePDR(conn, table1, BasicFont, RedSmallFont, sid2, sid, status, 0);
          table1 = PDFdiffService.ComparePDR(conn, table1, BasicFont, RedSmallFont, sid, sid2, status, 1);
          table1 = PDFdiffService.ComparePDR(conn, table1, BasicFont, RedSmallFont, sid, sid2, status, 2);
          document.add(table1);
        }

        //********************************第三段落****************************//
        if (flag11 || flag19 || flag20 || flag201 || flag21){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("3. Yield Criteria"));

          // for Yield table 20090519
	      PdfPTable table1 = null;
          float[] widths = {5,5,4,4,25,8,25};
	      table1 = new PdfPTable(widths);
	      table1.setWidthPercentage(100);
	      table1.setSpacingBefore(5);
	      table1 = PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "WS", status, "Hold");
	      if (table1 != null) {
	          document.add(new Paragraph("- WS Hold Yield"));
	    	  document.add(table1);
	      }
	      widths = new float[]{5,5,4,4,25,8,8,8,8,25};
	      table1 = null;
	      table1 = new PdfPTable(widths);
	      table1.setWidthPercentage(100);
	      table1.setSpacingBefore(5);
	      table1 = PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "WS", status, "Dgrade");
	      if (table1 != null) {
	          document.add(new Paragraph("- WS Dgrade Yield"));
	    	  document.add(table1);
	      }
	      widths = new float[]{5,5,4,4,25,8,12,21};
	      
	      table1 = null;
	      table1 = new PdfPTable(widths);
	      table1.setWidthPercentage(100);
	      table1.setSpacingBefore(5);
	      table1 = PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "FT", status, "");
	      if (table1 != null) {
	          document.add(new Paragraph("- FT Yield"));
	    	  document.add(table1);
	      }
          
          // for Yield table 20090112
	      PdfPTable table5 = null;
          float[] widths2 = {6,6,4,9,8,8,15,8,8,8,8,12};
	      table5 = null;
	      table5 = new PdfPTable(widths2);
	      table5.setWidthPercentage(100);
	      table5.setSpacingBefore(5);
	      table5 = PDFdiffService.CompareYield(conn, table5, SmallFont, RedSmallFont, sid, sid2, status);
	      if (table5 != null) {
	          document.add(new Paragraph("- Yield Table"));
	    	  document.add(table5);
	      }

          // old flow chart
          PDFdiffService.SearchDOC(document, BasicFont, sid, status,
                                   pd_body, brand, version, "Y",conn);
        }

        //********************************第四段落****************************//
        if (flag7 || flag8 || flag81 || flag18){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("4. Basic Information"));
          if (flag7 || flag8 || flag81) {
            PdfPTable table5 = null;
            float[] widths = {5, 5, 5, 4, 4, 7, 7,  10, 4,24, 5, 5, 10, 11};
            table5 = new PdfPTable(widths);
            table5.setWidthPercentage(100);
            table5.setSpacingBefore(5);
            table5.addCell(new Phrase(new Chunk("Diff", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Test Type", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Options", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Grade", SmallFont)));
            table5.addCell(new Phrase(new Chunk("IB Bin", SmallFont)));
            table5.addCell(new Phrase(new Chunk("DB Bin", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Binning Description",SmallFont)));
            table5.addCell(new Phrase(new Chunk("Inkless Grade",SmallFont)));
            table5.addCell(new Phrase(new Chunk("IPN Action", SmallFont)));
            table5.addCell(new Phrase(new Chunk("EPN Speed", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Test Speed", SmallFont)));
            table5.addCell(new Phrase(new Chunk("KTD Bin Flag",SmallFont)));
            table5.addCell(new Phrase(new Chunk("Down Grade", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Remark", SmallFont)));
            table5 = PDFdiffService.CompareBA(conn, table5, SmallFont, RedSmallFont, sid2, sid, status, 0);
            table5 = PDFdiffService.CompareBA(conn, table5, SmallFont, RedSmallFont, sid, sid2, status, 1);
            table5 = PDFdiffService.CompareBA(conn, table5, SmallFont, RedSmallFont, sid, sid2, status, 2);
            document.add(table5);
          }
          if (flag18) { // flag 18
            String comments =
                com.mxic.oiplus.oimaintain.TFIMBasicService.getBAComment(conn, Integer.parseInt(sid2),"R");
            document.add(new Paragraph(new Chunk("Old Comments :\n", BasicBoldFont)));
            if (comments.equals(""))
              document.add(new Paragraph(new Chunk("NA", SmallFont)));
            else
              document.add(new Paragraph(comments, SmallFont));
            comments =
                com.mxic.oiplus.oimaintain.TFIMBasicService.getBAComment(conn, Integer.parseInt(sid),status);
            document.add(new Paragraph(new Chunk("New Comments :\n", BasicBoldFont)));
            if (comments.equals(""))
              document.add(new Paragraph(new Chunk("NA", RedSmallFont)));
            else
              document.add(new Paragraph(comments, RedSmallFont));
          }
        }

        //********************************第五段落****************************//
        if (flag12){
          PDFdiffService.SearchDOC(document, BasicFont, sid, status,
                                   pd_body, brand, version, "T", conn);
        }
      }
//    lai-add-20100906-start
//    ********************************第六段落****************************//
      int section = 6;
      String table = "_tx";
      if (status.equals("R")) table = "";
      
      boolean hasData =
    	  pdfService.WipControl(document,pd_body, BasicFont, ""+section, ""+sid, table, conn);

      //lai-add-20100906-end
    } catch (Exception ex) {
      //DBConnection.close(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      IdFlag = false;
    } finally {
        //DBConnection.close(conn);
    }
    return IdFlag;
  }
}