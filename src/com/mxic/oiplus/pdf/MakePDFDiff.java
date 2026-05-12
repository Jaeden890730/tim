package com.mxic.oiplus.pdf;

import java.sql.Connection;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.mxic.oiplus.oimaintain.EditiionCompareActionForm;
import com.mxic.oiplus.oimaintain.EditiionCompareService;
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.TDSLogger;

public class MakePDFDiff {
  public MakePDFDiff() {
  }

  public static boolean makePDF(Connection conn,
                                String sid,
                                String pd_body,
                                String brand,
                                String version,
                                String status,
                                Document document) {
    boolean IdFlag = true;
    //Connection conn = null;
//    Document document = new Document(PageSize.LETTER, 50, 50, 90, 60);
//    TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
//    String fileName = pdfService.getFileName(pd_body, brand, version, null, true);

    try {
      BaseFont bfChinese =
          BaseFont.createFont("MHei-Medium", "UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
      Font LargeFont = new Font(bfChinese, 16, Font.NORMAL);
      Font BasicFont = new Font(bfChinese, 7, Font.NORMAL);
      Font BasicBoldFont = new Font(BaseFont.createFont("Helvetica", BaseFont.CP1252, BaseFont.NOT_EMBEDDED), 10, Font.NORMAL);
      Font SmallFont = new Font(bfChinese, 5, Font.NORMAL);
      Font RedSmallFont = new Font(bfChinese, 5, Font.NORMAL);
           RedSmallFont.setColor(0xEE,0,0);
      Font RedSmallFont8 = new Font(bfChinese, 7, Font.NORMAL);
           RedSmallFont8.setColor(0xEE,0,0);           
      Font BasicRedFont = new Font(bfChinese, 12, Font.NORMAL);
           BasicRedFont.setColor(0xEE,0,0);

      //conn = DBConnection.getConnection();
/*
      PdfWriter writer = PdfWriter.getInstance(document,
                                               new FileOutputStream(pdfProp.getProperty("pdf.dir") +
                                                   File.separator + fileName + ".pdf"));
*/
      String sid2 = OiMaintainService.getPreviousVersionSid(conn, sid);
      //String productType = OiMaintainService.getProductType(sid);
/*
      String[] approve = null;
      if (status.equals("R") && fileType.equals(""))
    	approve = pdfService.GetCoverPage(pd_body,brand,version,null,conn);
      else
    	approve = new String[] {"", ""};
*/
//      writer.setPageEvent(new PageNumbersWatermark(writer, document, pd_body, version,approve[0],approve[1]));

      //setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
//      writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC", PdfWriter.AllowPrinting);

//      document.open();

      if (Integer.parseInt(version) == 0) {
        document.add(new Paragraph("本版為第一個版本，沒有差異資料。", LargeFont));
      } else {
    	  String pck_com = OiMaintainService.getPackageComponent(conn,pd_body, brand);
    	boolean flag0 = EditiionCompareService.isPWLiff(conn, sid2, sid, status, 0);
        boolean flag01 = EditiionCompareService.isPWLiff(conn, sid, sid2, status, 1);  
        boolean flag02 = EditiionCompareService.isPWLiff(conn, sid2, sid, status, 2);
        boolean flag4;
        boolean flag6;
        if(pck_com.equals("M")){
        	flag4 = false;
        	flag6 = EditiionCompareService.isFTPDRMcpDiff(conn, sid, sid2, status, null);
        }else{
            flag4 = EditiionCompareService.isWSPDRDiff(conn, sid, sid2, status, null);
            flag6 = EditiionCompareService.isFTPDRDiff(conn, sid, sid2, status, null);        	
        }
        boolean flag3 = EditiionCompareService.isAVIDiff(conn, sid, sid2, status, null);
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
        boolean flag11;
        boolean flag12;
        boolean flag22;
        boolean flag23;
        boolean flag231;
        if(pck_com.equals("S")){
	        flag22 = EditiionCompareService.isBomDiff(conn, sid2, sid, status, 0);
	        flag23 = EditiionCompareService.isBomDiff(conn, sid, sid2, status, 1);
	        flag231 = EditiionCompareService.isBomDiff(conn, sid, sid2, status, 2);
        }else{
	        flag22 = EditiionCompareService.isBomMcpDiff(conn, sid2, sid, status, 0);
	        flag23 = EditiionCompareService.isBomMcpDiff(conn, sid, sid2, status, 1);
	        flag231 = EditiionCompareService.isBomMcpDiff(conn, sid, sid2, status, 2);        	
        }
        if (status.equals("R")) {
          flag11 = EditiionCompareService.SearchDOC2(conn, sid, status, pd_body, brand, version, "Y");
          flag12 = EditiionCompareService.SearchDOC2(conn, sid, status, pd_body, brand, version, "T");
        } else {
          flag11 = EditiionCompareService.SearchDOC1(conn, sid, status, pd_body, brand, version, "Y");
          flag12 = EditiionCompareService.SearchDOC1(conn, sid, status, pd_body, brand, version, "T");
        }

        //******************************** Summary ****************************//
        String oldV = String.valueOf(Integer.parseInt(version) - 1);
        document.add(new Paragraph(pd_body + " / " + brand +
                                   " Differences between version " + version +
                                   " and " + oldV));

        if (flag3  || flag4  || flag6  || 
            	flag9  || flag10 || flag101 ||
            	flag11 || flag19 || flag20 || flag201 || flag21 || 
            	flag7  || flag8  || flag81 || flag18 || 
            	flag12 || flag22 || flag23 || flag231)
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
        
        if (flag22 || flag23 || flag231){
        	if(pck_com.equals("M")){
        		document.add(new Paragraph("- 6. MCP BOM vs. Product Route ",BasicFont));
        	}else{
        		document.add(new Paragraph("- 6. BOM vs. Product Route ",BasicFont));
        	}
        }

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
          document.add(new Paragraph("1. WS & FT PRODUCT ROUTE DEFINATION"));
        }

        if (flag4){
          document.add(new Paragraph("- WS Product Route Definition "));
          PDFdiffService.CompareWSPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, null);
        }

        String section = null;
        if (flag6) {
          if (flag4) section = "2";
            else section = "1";
          document.add(new Paragraph("- FT Product Route Definition "));
          if(pck_com.equals("M")){
        	  PDFdiffService.CompareFTPDFMcp(conn, document, SmallFont, RedSmallFont, sid, sid2, status, null, section);
          }else{
        	  PDFdiffService.CompareFTPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, null, section);
          }	  
        }

        if (flag3) {
          if (section == null) section = "1";
          else if (section.equals("1")) section = "2";
          else if (section.equals("2")) section = "3";
          document.add(new Paragraph("- AVI Release Vendor "));
          PDFdiffService.CompareAVIPDF(conn, document, SmallFont, RedSmallFont, sid, sid2, status, null, section);
        }

        //********************************第二段落****************************//
        if (flag9 || flag10 || flag101){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("2. Product VS Test Route Mapping "));

          float[] widths = {5,12,5,12,12,12,5,5,10,10,17};
          PdfPTable table1 = new PdfPTable(widths);
          table1.setSpacingBefore(5);
          table1.setWidthPercentage(100);
          table1.addCell(new Phrase(new Chunk("Diff", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Route", BasicFont)));
          table1.addCell(new Phrase(new Chunk("Step_Seq", BasicFont)));
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
	      
          float[] widths1 = {5,5,4,4,25,8,25};
	      table1 = new PdfPTable(widths1);
	      table1.setWidthPercentage(100);
	      table1.setSpacingBefore(5);
	      table1 = PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "WS", status, "Hold");
	      if (table1 != null) {
	          document.add(new Paragraph("- WS Hold Yield"));
	    	  document.add(table1);
	      }

	      widths1 = new float[]{5,5,4,4,25,8,8,8,8,25};
	      table1 = null;
	      table1 = new PdfPTable(widths1);
	      table1.setWidthPercentage(100);
	      table1.setSpacingBefore(5);
	      table1 = PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "WS", status, "Dgrade");
	      if (table1 != null) {
	          document.add(new Paragraph("- WS Dgrade Yield"));
	    	  document.add(table1);
	      }
	      widths1 = new float[]{5,5,4,4,25,8,12,21};
	      
	      table1 = null;
	      table1 = new PdfPTable(widths1);
	      table1.setWidthPercentage(100);
	      table1.setSpacingBefore(5);
	      table1 = PDFdiffService.CompareYield(conn, table1, SmallFont, RedSmallFont, sid, sid2, "FT", status, "");
	      if (table1 != null) {
	          document.add(new Paragraph("- FT Yield"));
	    	  document.add(table1);
	      }
          
          // for Yield table 20090112
	      float[] widths2 = {6,6,4,9,8,8,15,8,8,8,6,14};
	      PdfPTable table5 = null;
	      table5 = new PdfPTable(widths2);
	      table5.setWidthPercentage(100);
	      table5.setSpacingBefore(5);
	      table5 = PDFdiffService.CompareYield(conn, table5, SmallFont, RedSmallFont, sid, sid2, status);
          if (table5 != null) {
	          document.add(new Paragraph("- Yield Table"));
        	  document.add(table5);
          }

          //待 Yield table 全面上線後可移除以下 chart
          //取出有 diff 的 chart
          EditiionCompareActionForm[] result = null;
          if (status.equals("R"))
            result = EditiionCompareService.SearchReleasedDOC(sid, status, pd_body, brand,
                                                              version, "Y",conn);
          else
            result = EditiionCompareService.SearchUnreleasedDOC(sid, status, pd_body, brand,
                                                                version, "Y",conn);

          for (int i=0; i<result.length; i++) {
            EditiionCompareActionForm bean = result[i];
            StringBuffer buf = new StringBuffer();
            boolean file_show = false;
            document.newPage();
            if (bean.getTag_old().equals("0")) {
              buf = new StringBuffer();
              buf.append("OLD: " + bean.getTest_flow());
              file_show = PDFdiffService.doc_show_doc(document, BasicFont, bean.getPath_old(), buf.toString());
              if (bean.getTag_new().equals("0"))
                document.newPage();
            }
            if (bean.getTag_new().equals("0")) {
              buf = new StringBuffer();
              buf.append("NEW: " + bean.getTest_flow());
              file_show = PDFdiffService.doc_show_doc(document, BasicFont, bean.getPath_new(), buf.toString());
            }
          }
        }

        //********************************第四段落****************************//
        if (flag7 || flag8 || flag81 || flag18){
          document.add(new Paragraph(" "));
          document.add(new Paragraph("4. Basic Information"));
          if (flag7 || flag8 || flag81) {
            PdfPTable table5 = null;
            float[] widths = {5, 5, 5, 4, 4, 7, 7, 10, 4,24, 5, 5, 10, 11};
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
            table5.addCell(new Phrase(new Chunk("Inkless Grade", SmallFont)));
            table5.addCell(new Phrase(new Chunk("IPN Action", SmallFont)));
            table5.addCell(new Phrase(new Chunk("EPN Speed", SmallFont)));
            table5.addCell(new Phrase(new Chunk("Test Speed", SmallFont)));
            table5.addCell(new Phrase(new Chunk("KTD Bin Flag", SmallFont)));
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
              document.add(new Paragraph(new Chunk("NA", SmallFont)));
            else
              document.add(new Paragraph(comments, SmallFont));
          }
        }

        //********************************第五段落****************************//
        if (flag12){

          EditiionCompareActionForm[] result = null;
          if (status.equals("R"))
            result = EditiionCompareService.SearchReleasedDOC(sid, status, pd_body, brand, version, "T", conn);
          else
            result = EditiionCompareService.SearchUnreleasedDOC(sid, status, pd_body, brand, version, "T", conn);

          for (int i=0; i<result.length; i++) {
            EditiionCompareActionForm bean = result[i];
            StringBuffer buf = new StringBuffer();
            boolean file_show = false;
            document.newPage();
            if (i == 0)
              document.add(new Paragraph("5. Test Flows"));
            if (bean.getTag_old().equals("0")) {
              buf = new StringBuffer();
              buf.append("OLD: " + bean.getTest_flow());
              file_show = PDFdiffService.doc_show_doc(document, BasicFont, bean.getPath_old(), buf.toString());
              if (bean.getTag_new().equals("0"))
                document.newPage();
            }
            if (bean.getTag_new().equals("0")) {
              buf = new StringBuffer();
              buf.append("NEW: " + bean.getTest_flow());
              file_show = PDFdiffService.doc_show_doc(document, BasicFont, bean.getPath_new(), buf.toString());
            }
          }
        }
        //lai-add-20100202-start
//      ********************************第六段落****************************//
        if (flag22 || flag23 || flag231){

        	document.add(new Paragraph(" "));
            
            // for Yield table 20090519
  	      PdfPTable table1 = null;
          //float[] widths = {4,4,4,4,5,4,5,5,5,5,  14,5,5,5,7,7,  14,    6,10};
  	      if(pck_com.equals("M")){
  	    	  document.add(new Paragraph("6. MCP BOM vs. Product Route"));
  	    	  float[] widths = null;
  	    	  if(brand.equals("MX"))
  	    		 widths = new float[]{4,4,4,4,5,4,5,5,5,5,4,14,6,10,5,7,5,5,5,5,7,7,4,14,4,4};
  	    	  else
  	    		 widths = new float[]{4,4,4,4,5,4,5,5,5,5,4,14,5,7,5,5,5,5,7,7,4,14,4,4}; 
	  	      table1 = new PdfPTable(widths);
	  	      table1.setWidthPercentage(100);
	  	      table1.setSpacingBefore(5);
	  	      table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("BE Opt", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("FG With Code", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("Pkg Type",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Route Code", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("FT Route", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Add. Route 1",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Add. Route 2",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Add. Route 3",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Special Control",SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("FT Comment", SmallFont)));//14
	  	      if(brand.equals("MX")){
	  	    	table1.addCell(new Phrase(new Chunk("Quality Level", SmallFont)));//6
	  	    	table1.addCell(new Phrase(new Chunk("Quality Level Comment", SmallFont)));//10
	  	      }
	  	      //table1.addCell(new Phrase(new Chunk("Mask Opt", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("Component No", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("Component Product", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("COM Mask Opt.", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("COM BE Option", SmallFont)));//5
	  	      
	  	      table1.addCell(new Phrase(new Chunk("DB With Code", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("Sort Route Code", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("WS Route", SmallFont)));//7
	  	      table1.addCell(new Phrase(new Chunk("WS Add Route", SmallFont)));//7
	  	      table1.addCell(new Phrase(new Chunk("WS Special Control", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("WS Comment", SmallFont)));//14
	  	      table1.addCell(new Phrase(new Chunk("AVI", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("INK", SmallFont)));//4
	          
	  	      table1 = PDFdiffService.CompareBomMcp(conn, table1, SmallFont, RedSmallFont, sid2, sid, status, 0);
	  	      table1 = PDFdiffService.CompareBomMcp(conn, table1, SmallFont, RedSmallFont, sid, sid2, status, 1);
	  	      table1 = PDFdiffService.CompareBomMcp(conn, table1, SmallFont, RedSmallFont, sid, sid2, status, 2);   	    	
	  	      document.add(table1);	  	      
  	      }else{
  	    	  document.add(new Paragraph("6. BOM vs. Product Route"));
  	    	  float[] widths = null ;
  	    	  if(brand.equals("MX"))
  	    		  widths = new float[]{4,4,4,4,5,4,5,5,5,5,4,14,6,10,5,5,5,5,7,7,4,14,4,4};
  	    	  else
  	    		widths = new float[]{4,4,4,4,5,4,5,5,5,5,4,14,5,5,5,5,7,7,4,14,4,4};
	  	      table1 = new PdfPTable(widths);
	  	      table1.setWidthPercentage(100);
	  	      table1.setSpacingBefore(5);
	  	      table1.addCell(new Phrase(new Chunk("Diff", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("BE Opt", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("FG With Code", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("Pin Count", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("Pkg Type",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Route Code", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("FT Route", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Add. Route 1",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Add. Route 2",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Add. Route 3",SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("FT Special Control",SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("FT Comment", SmallFont)));//14
	  	      if(brand.equals("MX")){
	  	    	  table1.addCell(new Phrase(new Chunk("Quality Level", SmallFont)));//6
	  	    	  table1.addCell(new Phrase(new Chunk("Quality Level Comment", SmallFont)));//10
	  	      }
	  	      table1.addCell(new Phrase(new Chunk("MCP Flag", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("Mask Opt", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("DB With Code", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("Sort Route Code", SmallFont)));//5
	  	      table1.addCell(new Phrase(new Chunk("WS Route", SmallFont)));//7
	  	      table1.addCell(new Phrase(new Chunk("WS Add Route", SmallFont)));//7
	  	      table1.addCell(new Phrase(new Chunk("WS Special Control", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("WS Comment", SmallFont)));//14
	  	      table1.addCell(new Phrase(new Chunk("AVI", SmallFont)));//4
	  	      table1.addCell(new Phrase(new Chunk("INK", SmallFont)));//4
	          
	  	      table1 = PDFdiffService.CompareBom(conn, table1, SmallFont, RedSmallFont, sid2, sid, status, 0);
	  	      table1 = PDFdiffService.CompareBom(conn, table1, SmallFont, RedSmallFont, sid, sid2, status, 1);
	  	      table1 = PDFdiffService.CompareBom(conn, table1, SmallFont, RedSmallFont, sid, sid2, status, 2);
	  	      document.add(table1);
  	      }
  	      
        }
        //lai-add-20100202-end
        
      }
      //lai-add-20100906-start
//    ********************************第七段落****************************//
      int section = 7;
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
        //DBConnection.close(conn);
/*
      try {
        document.close();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        DBConnection.close(conn);
      }
*/
    }
    return IdFlag;
  }
}