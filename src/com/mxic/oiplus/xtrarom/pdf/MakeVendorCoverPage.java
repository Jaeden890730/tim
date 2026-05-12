package com.mxic.oiplus.xtrarom.pdf;

import java.sql.Connection;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.mxic.oiplus.xtrarom.oimaintain.EditiionCompareService;
import com.mxic.oiplus.xtrarom.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.pdf.pdfService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class MakeVendorCoverPage {
  public MakeVendorCoverPage() {
  }

  public static boolean makePDF(ProTestRouteBeanAF fm, Document document, String status) {
    boolean IdFlag = true;
    Connection conn = null;
//    Document document = new Document(PageSize.LETTER, 50, 50, 90, 60);
//    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
//    String fileName = pdfService.getFileName(fm.getProductbody(), fm.getBrand(), fm.getVersion(), fm.getVendor(), false);
    try {
      BaseFont bfChinese = BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      Font LargeFont = new Font(bfChinese, 16, Font.NORMAL);
      Font RedLargeFont = new Font(bfChinese, 16, Font.BOLD);
           RedLargeFont.setColor(0xEE,0,0);
           Font BasicFont = new Font(bfChinese, 8, Font.NORMAL);
      Font BasicBoldFont = new Font(BaseFont.createFont("Helvetica", BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 10, Font.NORMAL);
      Font SmallFont= new Font( bfChinese,6,Font.NORMAL);
      Font RedSmallFont = new Font( bfChinese,6,Font.NORMAL);
      RedSmallFont.setColor(0xEE,0,0);
      Font RedSmallFont8 = new Font( bfChinese,8,Font.NORMAL);
      RedSmallFont8.setColor(0xEE,0,0);           
      Font BasicRedFont = new Font(bfChinese, 12, Font.NORMAL);
           BasicRedFont.setColor(0xEE,0,0);

      conn = DBConnection.getConnection();
/*
      PdfWriter writer = PdfWriter.getInstance(document,
                                               new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                   File.separator + fileName + ".pdf"));
*/
      String pd_body=fm.getProductbody();
      String version=fm.getVersion();
      String sid = String.valueOf(fm.getSid());
      String brand = fm.getBrand();
      String site=fm.getVendor();
      String productType = OiMaintainService.getProductType(sid);

      String sid2 = OiMaintainService.getPreviousVersionSid(sid);
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
        boolean flag4 = EditiionCompareService.isWSPDRDiff(conn, sid, sid2, status, site);
        boolean flag6 = EditiionCompareService.isFTPDRDiff(conn, sid, sid2, status, site);
        boolean flag13 = EditiionCompareService.isFTPDR_REDiff(conn, sid, sid2, status, site);
        boolean flag3 = com.mxic.oiplus.oimaintain.EditiionCompareService.isAVIDiff(conn, sid, sid2, status, site);
        boolean flag19 = com.mxic.oiplus.oimaintain.EditiionCompareService.isYieldDiff(conn, sid, sid2, status);
        boolean flag20 = com.mxic.oiplus.oimaintain.EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", status, "hcs");
        boolean flag21 = com.mxic.oiplus.oimaintain.EditiionCompareService.isYieldDiff(conn, sid, sid2, "FT", status, "hcs");
        boolean flag20_1 = com.mxic.oiplus.oimaintain.EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", status, "acs");
        boolean flag21_1 = com.mxic.oiplus.oimaintain.EditiionCompareService.isYieldDiff(conn, sid, sid2, "FT", status, "acs");
        boolean flag9 = com.mxic.oiplus.oimaintain.EditiionCompareService.isPDRDiff(conn, sid2, sid, status, 0);
        boolean flag10 = com.mxic.oiplus.oimaintain.EditiionCompareService.isPDRDiff(conn, sid, sid2, status, 1);
        boolean flag101 = com.mxic.oiplus.oimaintain.EditiionCompareService.isPDRDiff(conn, sid, sid2, status, 2);
        boolean flag7 = com.mxic.oiplus.oimaintain.EditiionCompareService.isBADiff(conn, sid2, sid, status, 0);
        boolean flag8 = com.mxic.oiplus.oimaintain.EditiionCompareService.isBADiff(conn, sid, sid2, status, 1);
        boolean flag81 = com.mxic.oiplus.oimaintain.EditiionCompareService.isBADiff(conn, sid, sid2, status, 2);
        boolean flag18 = com.mxic.oiplus.oimaintain.EditiionCompareService.isBACommentDiff(conn, sid, sid2, status);
        boolean flag11, flag12;
        if (status.equals("R")) {
          flag11 = EditiionCompareService.SearchDOC2(conn, sid, status, pd_body, brand, version, "Y");
          flag12 = EditiionCompareService.SearchDOC2(conn, sid, status, pd_body, brand, version, "T");
        } else {
          flag11 = EditiionCompareService.SearchDOC1(conn, sid, status, pd_body, brand, version, "Y");
          flag12 = EditiionCompareService.SearchDOC1(conn, sid, status, pd_body, brand, version, "T");
        }
        boolean flag14 = EditiionCompareService.isMainRouteSubDiff(conn, sid2, sid, status, 0);
        boolean flag15 = EditiionCompareService.isMainRouteSubDiff(conn, sid2, sid, status, 1);
        boolean flag16 =  EditiionCompareService.isMainRouteReworkDiff(conn, sid2, sid, status, 0);
        boolean flag17 =  EditiionCompareService.isMainRouteReworkDiff(conn, sid2, sid, status, 1);
        boolean flag171 =  EditiionCompareService.isMainRouteReworkDiff(conn, sid2, sid, status, 2);
//        document.open();

        //******************************** Summary ****************************//
        String oldV=String.valueOf(Integer.parseInt(fm.getVersion())-1);
        document.add(new Paragraph(pd_body + " / " + fm.getBrand() +
                                   " Differences between version " +  fm.getVersion() +
                                   " and "+oldV));

        if (flag3  || flag4  || flag6  || flag12 || flag13 ||  
        	flag9  || flag10 || flag101 ||  
        	flag11 || flag19 || flag20 || flag21 || flag20_1 || flag21_1 ||
        	flag7  || flag8  || flag81 || flag18 ||
        	flag14 || flag15 || flag16 || flag17 || flag171)
          document.add(new Paragraph("The following section(s) has difference (Wip Handling Control 不列入): ", LargeFont));
        else
          document.add(new Paragraph("本次進版依旺宏內部所需，內容與前一版並無差異 (Wip Handling Control 除外)。", LargeFont));

        if (flag3  || flag4  || flag6 || flag12 || flag13)
          document.add(new Paragraph("- 1. WS & FT Product Route Definition",BasicFont));

        if (flag9  || flag10 || flag101)
          document.add(new Paragraph("- 2. Product VS Test Route Mapping",BasicFont));

        if (flag11 || flag19 || flag20 || flag21 || flag20_1 || flag21_1)
          document.add(new Paragraph("- 3. Yield Criteria",BasicFont));

        if (flag7  || flag8 || flag81 || flag18)
          document.add(new Paragraph("- 4. Basic Information",BasicFont));

        if (flag14 || flag15)
          document.add(new Paragraph("- 5. Main Route vs Substitution Route Information"));

        if (flag16 || flag17 || flag171)
          document.add(new Paragraph("- 6. Main Route vs Rework Route Information"));

        document.add(new Paragraph(" "));
        String[] approve = pdfService.GetCoverPage(pd_body, brand, version, null, conn);
		if(approve[3]!="")
			document.add(new Paragraph("ECN (" + approve[2] + ")生效，取代TECN:" + approve[3], BasicRedFont));
		document.add(new Paragraph(" "));

        //********************************第一段落****************************//
        if (flag3 || flag4 || flag6 || flag13){
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
          PDFdiffService.CompareFTPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, site, section);
        }
        if (flag13){
          if (flag4) section = "2";
          else section = "1";
          document.add(new Paragraph("- FT Product Recycle Route Definition ",LargeFont));
          PDFdiffService.CompareFTPDF_RE(conn, document, SmallFont, RedSmallFont, sid, sid2, status, site, section);
        }

        if (flag3) {
         if (section == null) section = "1";
         else if (section.equals("1")) section = "2";
         else if (section.equals("2")) section = "3";
         document.add(new Paragraph("- AVI Release Vendor "));
         com.mxic.oiplus.pdf.PDFdiffService.CompareAVIPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, site, section);
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
          table1 = com.mxic.oiplus.pdf.PDFdiffService.ComparePDR(conn, table1, BasicFont, RedSmallFont, sid2, sid, status, 0);
          table1 = com.mxic.oiplus.pdf.PDFdiffService.ComparePDR(conn, table1, BasicFont, RedSmallFont, sid, sid2, status, 1);
          table1 = com.mxic.oiplus.pdf.PDFdiffService.ComparePDR(conn, table1, BasicFont, RedSmallFont, sid, sid2, status, 2);
          document.add(table1);
        }

        //********************************第三段落****************************//
        if (flag11 || flag19 || flag20 || flag21 || flag20_1 || flag21_1) {
            document.add(new Paragraph(" "));
            document.add(new Paragraph("3. Yield Criteria"));

            // for Yield table 20090519
  	      PdfPTable table1 = null;
            //float[] widths = {5,5,4,4,25,8,10,25};
  	    float[] widths = {5,5,4,4,20,6,4,4,4,20};
  	      table1 = new PdfPTable(widths);
  	      table1.setWidthPercentage(100);
  	      table1.setSpacingBefore(5);
  	      table1 = com.mxic.oiplus.pdf.PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "WS", status, "hcs");
  	      if (table1 != null) {
  	          document.add(new Paragraph("- WS Hold Yield"));
  	    	  document.add(table1);
  	      }
  	      
  	      table1 = null;
  	      table1 = new PdfPTable(widths);
  	      table1.setWidthPercentage(100);
  	      table1.setSpacingBefore(5);
  	      table1 = com.mxic.oiplus.pdf.PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "WS", status, "acs");
  	      if (table1 != null) {
  	          document.add(new Paragraph("- WS Action Yield"));
  	    	  document.add(table1);
  	      }
          //widths = new float[10];
          //widths[0]=5;widths[1]=5;widths[2]=4;widths[3]=4;widths[4]=20;widths[5]=6;widths[6]=4;widths[7]=4;widths[8]=4;widths[9]=20;
          table1 = null;
          table1 = new PdfPTable(widths);
          table1.setWidthPercentage(100);
          table1.setSpacingBefore(5);
          table1 = com.mxic.oiplus.pdf.PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "FT", status, "hcs");
          if (table1 != null) {
              document.add(new Paragraph("- FT Hold Yield"));
              document.add(table1);
          }
          table1 = null;
          table1 = new PdfPTable(widths);
          table1.setWidthPercentage(100);
          table1.setSpacingBefore(5);
          table1 = com.mxic.oiplus.pdf.PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "FT", status, "acs");
          if (table1 != null) {
              document.add(new Paragraph("- FT Action Yield"));
              document.add(table1);
          }
            
            // for Yield table 20090112
  	      PdfPTable table5 = null;
            float[] widths2 = {6,6,4,9,8,8,15,8,8,8,8,12};
  	      table5 = null;
  	      table5 = new PdfPTable(widths2);
  	      table5.setWidthPercentage(100);
  	      table5.setSpacingBefore(5);
  	      table5 = com.mxic.oiplus.pdf.PDFdiffService.CompareYield(conn, table5, SmallFont, RedSmallFont, sid, sid2, status);
  	      if (table5 != null) {
  	          document.add(new Paragraph("- Yield Table"));
  	    	  document.add(table5);
  	      }

          PDFdiffService.SearchDOC(document, BasicFont, sid, status,
                                   pd_body, brand, version, "Y");
        }

        //********************************第四段落****************************//
        if (flag7 || flag8 || flag81 || flag18){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("4. Basic Information"));
          if (flag7 || flag8 || flag81) {
            PdfPTable table5 = null;
            float[] widths = {5, 5, 5, 4, 4, 7, 7, 10, 28, 10, 11};//XROM
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
            //table5.addCell(new Phrase(new Chunk("EPN Speed", SmallFont)));
            //table5.addCell(new Phrase(new Chunk("Test Speed", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Down Grade", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Remark", SmallFont)));
            table5 = com.mxic.oiplus.pdf.PDFdiffService.CompareBA(conn, table5, SmallFont, RedSmallFont, sid2, sid, status, 0);
            table5 = com.mxic.oiplus.pdf.PDFdiffService.CompareBA(conn, table5, SmallFont, RedSmallFont, sid, sid2,  status, 1);
            table5 = com.mxic.oiplus.pdf.PDFdiffService.CompareBA(conn, table5, SmallFont, RedSmallFont, sid, sid2,  status, 2);
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
              document.add(new Paragraph(new Chunk("NA", SmallFont)));
            else
              document.add(new Paragraph(comments, SmallFont));
          }
        }

        //********************************第四段落****************************//
        if (flag14 || flag15){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("5. Main Route vs Substitution Route Information"));
          PdfPTable table4 = null;
          float[] widths = {5,14,14,29};
          table4 = new PdfPTable(widths);
          table4.setWidthPercentage(100);
          table4.setSpacingBefore(5);
          table4.addCell(new Phrase(new Chunk("Diff", BasicFont)));
          table4.addCell(new Phrase(new Chunk("Main Route", BasicFont)));
          table4.addCell(new Phrase(new Chunk("Map Route", BasicFont)));
          table4.addCell(new Phrase(new Chunk("Remark", BasicFont)));

          table4 = PDFdiffService.CompareMainRouteSub(conn, table4, BasicFont, RedSmallFont8, sid2, sid, status, 0, productType);
          table4 = PDFdiffService.CompareMainRouteSub(conn, table4, BasicFont, RedSmallFont8, sid, sid2, status, 1, productType);
          table4 = PDFdiffService.CompareMainRouteSub(conn, table4, BasicFont, RedSmallFont8, sid, sid2, status, 2, productType);
          document.add(table4);
        }

        //********************************第五段落****************************//
        if (flag16 || flag17 || flag171){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("6. Main Route vs Rework Route Information"));
          PdfPTable table5 = null;
          float[] widths = {5,14,14,29};
          table5 = new PdfPTable(widths);
          table5.setWidthPercentage(100);
          table5.setSpacingBefore(5);
          table5.addCell(new Phrase(new Chunk("Diff", BasicFont)));
          table5.addCell(new Phrase(new Chunk("Main Route", BasicFont)));
          table5.addCell(new Phrase(new Chunk("Map Route", BasicFont)));
          table5.addCell(new Phrase(new Chunk("Remark", BasicFont)));

          table5 = PDFdiffService.CompareMainRouteRework(conn, table5, BasicFont, RedSmallFont8, sid2, sid, status, 0, productType);
          table5 = PDFdiffService.CompareMainRouteRework(conn, table5, BasicFont, RedSmallFont8, sid, sid2, status, 1, productType);
          table5 = PDFdiffService.CompareMainRouteRework(conn, table5, BasicFont, RedSmallFont8, sid, sid2, status, 2, productType);
          document.add(table5);
        }

        //********************************第五段落****************************//
        /*if (flag12){
          PDFdiffService.SearchDOC(document, BasicFont, sid, status,
                                   pd_body, brand, version, "T");
        }*/
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
//      document.close();
      //DBConnection.close(conn);
      ex.fillInStackTrace();
      TDSLogger.println(ex.getMessage());
      IdFlag = false;
    } finally {
        DBConnection.close(conn);
/*
      try {
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