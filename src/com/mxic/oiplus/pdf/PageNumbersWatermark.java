/*
 * for PDF Header/Footer and Wafer marks
 */
package com.mxic.oiplus.pdf;

import java.io.*;
import java.awt.*;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

public class PageNumbersWatermark extends PdfPageEventHelper {
  /** An Image that goes in the header. */
  public Image mxicLogo;
  /** The headertable. */
  public PdfPTable table;
  /** The EndPagetable */
  public PdfPTable table1;
  /** The Graphic state */
  public PdfGState gstate;
  /** A template that will hold the total number of pages. */
  public PdfTemplate tpl;
  /** The font that will be used. */
  public BaseFont helv;
  /** product body */
  public String product_body;
  /** OI Version */
  public String version;
  /** Document No, if OI approved */
  public String docno;
  /** Effective Date, if OI approved */
  public String effdate;
  /** Cover Page Flag */
  public boolean coverPageFlag;
  /** Wafer Mark Flag */
  public boolean waferMarkFlag;
  /** brand */
  public String brand;

  /**
   * Generates a document with a header containing Page x of y and with a Watermark on every page.
   * @param args no arguments needed
   */
  public PageNumbersWatermark(PdfWriter writer, Document document,String a,String b, String c, String d, String br) {
	try {
	  product_body = a;
	  version = b;
	  docno = c;
	  effdate = d;
	  brand = br;
	  coverPageFlag = false;
	  waferMarkFlag = true;
	  if ((effdate == null) || effdate.equals(""))
	    effdate = DateUtil.getDay();
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

  /**
   * Generates a document with a header containing Page x of y and with a Watermark on every page.
   * @param args no arguments needed
   */
  public PageNumbersWatermark(PdfWriter writer, Document document,String a, String br, String b, String c, String d, boolean coverPageFlag, boolean waferMarkFlag) {
    try {
      product_body = a;
      version = b;
      docno = c;
      effdate = d;
      brand = br;
      this.coverPageFlag = coverPageFlag;
      this.waferMarkFlag = waferMarkFlag;
      if ((effdate == null) || effdate.equals(""))
        effdate = DateUtil.getDay();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   *
   * @param writer PdfWriter
   * @param document Document
   * 每當document.open()時就會執行這段程式
   * 20190807:#46552 201900394 - 調整e8049+e8040 浮水印位置
   */
  public void onOpenDocument(PdfWriter writer, Document document) {
    try {
      //取得MXIC LOGO圖檔的路徑
      String logoPath = TDSResource.getProperties("TIMPdf").getValue("mxiclogo.path");
      // initialization of the header table
      mxicLogo = Image.getInstance(logoPath + File.separator + "logo.gif");
      //設定Logo的(寬,高)度
      mxicLogo.scaleAbsolute(380, 54);

      //放置MXIC LOGO,Macronix International Co. Ltd.,Confidential
      table = new PdfPTable(4);
      Phrase p1 = new Phrase();
      Phrase p2 = new Phrase();
      Chunk ck = new Chunk(mxicLogo, 0, 0);
      p1.add(ck);
// 配合企業識別系統使用規定,文件格式書寫標準化修改
//      ck = new Chunk("             Macronix International Co.,Ltd.",
//                     new Font(Font.TIMES_ROMAN, 18, Font.NORMAL));
//      p.add(ck);
      ck = new Chunk("\n\nConfidential",
                     new Font(Font.TIMES_ROMAN, 12, Font.BOLDITALIC));
      p2.add(ck);

      //對齊方式
      table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
      //背景顏色
      //table.getDefaultCell().setBackgroundColor(Color.white);
      Image tImgCover = Image.getInstance(logoPath + File.separator +"cover.jpg"); 

      tImgCover.setAbsolutePosition(0, 0);

      tImgCover.scaleAbsolute(595, 842);
      document.add(tImgCover);   
      
      //匡線寬度
      table.getDefaultCell().setBorderWidth(1);

      //setFillOpacity(float n)
      //Sets the current stroking alpha constant, specifying the constant shape or constant opacity value to be used for nonstroking operations in the transparent imaging model.
      gstate = new PdfGState();
      //浮水印的顏色深淡
      gstate.setFillOpacity(0.3f);
      gstate.setStrokeOpacity(0.3f);
      gstate.setOverPrintStroking(true);
      //將MXIC LOGO,Macronix International Co. Ltd.,Confidential的phrase放入cell之中
      PdfPCell cell = new PdfPCell(p1);
      cell.setBorder(0);
      cell.setColspan(3);
      cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
      cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
      table.addCell(cell);
      cell = new PdfPCell(p2);
      cell.setBorder(0);
      cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      cell.setVerticalAlignment(PdfPCell.ALIGN_BASELINE);
      table.addCell(cell);
      
     

      cell = new PdfPCell(new Phrase(new Chunk("Doc. NO.: " + docno,
                          new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
      cell.setBorderWidth(1);
      table.addCell(cell);
/*
      table.addCell(new Phrase(new Chunk("Doc. NO.: " + docno,
                                         new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
*/
      cell = new PdfPCell(new Phrase(new Chunk("Rev.: " + version,
                          new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
      cell.setBorderWidth(1);
      table.addCell(cell);
/*
      table.addCell(new Phrase(new Chunk("Rev.: " + version,
                                         new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
*/

      cell = new PdfPCell(new Phrase(new Chunk("Effective date: " + effdate,
    		  new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
      cell.setBorderWidth(1);
      table.addCell(cell);
//      cell = new PdfPCell(new Phrase(new Chunk("Hierarchy: 3" ,
//              new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
      cell = new PdfPCell(new Phrase(new Chunk("" ,
    		  new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
      cell.setBorderWidth(1);
      table.addCell(cell);

      String br = "";
      if (brand.equals("KH"))
    	  br = "KH ";
      PdfPCell cell1 =
        new PdfPCell(new Paragraph(new Chunk(
                            "Doc. Title: " + br + "TEST PRODUCTION INFORMATION-" + product_body,
                             new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
      cell1.setBorderWidth(1);
      cell1.setColspan(4);
      cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
      table.addCell(cell1);

      // EndPageTable set text
      table1 = new PdfPTable(1);//1為欄位數量
      table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
      table1.getDefaultCell().setBackgroundColor(Color.white);
      table1.getDefaultCell().setBorderWidth(0);
      table1.addCell(new Phrase(new Chunk("The information contained herein is the exclusive property of Macronix and shall not be distributed, reproduced, or\n",
                                          new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));
      table1.addCell(new Phrase(new Chunk("disclosed in whole or in part without prior written permission of Macronix.",
                                          new Font(Font.TIMES_ROMAN, 10, Font.NORMAL))));

      // initialization of the template
      //createTemplate(float width,float height,PdfName forcedName)

      tpl = writer.getDirectContent().createTemplate(100, 100);
      tpl.setBoundingBox(new Rectangle( -20, -20, 100, 100));
      // initialization of the font
//      helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
      helv = BaseFont.createFont("Times-Roman", BaseFont.WINANSI, false);
    } catch (Exception e) {
      throw new ExceptionConverter(e);
    }
  }

  /**
   *
   * @param writer PdfWriter
   * @param document Document
   */
  public void onEndPage(PdfWriter writer, Document document) {
    PdfContentByte cb = writer.getDirectContent();
    cb.saveState();

    table.setTotalWidth(document.right() - 30);
    table.writeSelectedRows(0, -1, document.left(), document.getPageSize().height(), cb);

    //set EndPageTable position
    table1.setTotalWidth(document.right());
    table1.writeSelectedRows(0, -1, document.left(), document.bottom(), cb);

    //draw the line of end page
    cb.moveTo(document.left(), document.bottom() + 1);
    cb.lineTo(document.right(), document.bottom() + 1);
    cb.stroke();

    // compose the footer
    String text = "Page: "+writer.getPageNumber() + " / ";
    float textSize = helv.getWidthPoint(text, 10);
//    float textBase = document.bottom() - 27;
//    float textBase = document.top()+20;
    float textBase = document.top()+20;
    cb.beginText();
    cb.setFontAndSize(helv, 10);

    float adjust = helv.getWidthPoint("0", 10);
//    cb.setTextMatrix(document.right() - textSize - adjust, textBase);
    cb.setTextMatrix(document.right()-30 - textSize, textBase);
    cb.showText(text);
    cb.endText();
//    cb.addTemplate(tpl, document.right() - adjust, textBase);
    cb.addTemplate(tpl, document.right()-30, textBase);

    cb.restoreState();
  }

  /**
   *
   * @param writer PdfWriter
   * @param document Document
   * 20190807:#46552 201900394 - 調整e8049+e8040 浮水印位置
   */

  //浮水印寫入
  public void onStartPage(PdfWriter writer, Document document) {
	if (waferMarkFlag == true) {
      java.awt.Color color = new java.awt.Color(238,48,48);//255,128,128
      PdfContentByte cb = writer.getDirectContentUnder();
      cb.saveState();
      cb.setGState(gstate);
      cb.setColorFill(color);

      cb.beginText();
      
    //背景顏色
      //table.getDefaultCell().setBackgroundColor(Color.white);
      String logoPath = TDSResource.getProperties("TIMPdf").getValue("mxiclogo.path");
      Image tImgCover;
	try {
		tImgCover = Image.getInstance(logoPath + File.separator +"cover.jpg");
		tImgCover.setAbsolutePosition(0, 0);

	      tImgCover.scaleAbsolute(595, 842);
	      
	      document.add(tImgCover);
	} catch (BadElementException | IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (DocumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();	
	} 

      
      /*20111214      
      cb.setFontAndSize(helv, 28);

      //showTextAligned(int alignment(對齊方式), String text（要顯示的字句）, float x（橫向置點）, float y（縱向置點）, float rotation（斜角度）)
      cb.showTextAligned(Element.ALIGN_LEFT,
                         "Controlled",
                         40,
                         document.getPageSize().height() - 150, 40);
      cb.setFontAndSize(helv, 50);

      //showTextAligned(int alignment(對齊方式), String text（要顯示的字句）, float x（橫向置點）, float y（縱向置點）, float rotation（斜角度）)
      cb.showTextAligned(Element.ALIGN_LEFT,
                         "Macronix Confidential & Proprietary",
                         65,
                         document.getPageSize().height() - 670, 45);
*/
      /*20180326
    //showTextAligned(int alignment(對齊方式), String text（要顯示的字句）, float x（橫向置點）, float y（縱向置點）, float rotation（斜角度）)
      cb.setFontAndSize(helv, 18);
      cb.showTextAligned(Element.ALIGN_LEFT,
                         "Controlled",
                         100,
                         document.getPageSize().height() - 100, 20);

      //showTextAligned(int alignment(對齊方式), String text（要顯示的字句）, float x（橫向置點）, float y（縱向置點）, float rotation（斜角度）)
      cb.showTextAligned(Element.ALIGN_LEFT,
                         "Macronix Confidential & Proprietary",
                         50,
                         document.getPageSize().height() - 140, 20);
  */           
    //showTextAligned(int alignment(對齊方式), String text（要顯示的字句）, float x（橫向置點）, float y（縱向置點）, float rotation（斜角度）)
      
      /*20181207cb.setFontAndSize(helv, 30);//28-->30
      cb.showTextAligned(Element.ALIGN_LEFT,
                         "Controlled",
                         80,//100-->80
                         document.getPageSize().height() - 150, 45);//25-->45,100-->150

      //showTextAligned(int alignment(對齊方式), String text（要顯示的字句）, float x（橫向置點）, float y（縱向置點）, float rotation（斜角度）)
      */
     /*20181207
      cb.setFontAndSize(helv, 148);//48-->58(118)
      cb.showTextAligned(Element.ALIGN_LEFT,
                         "Confidential",//"Macronix Confidential & Proprietary",
                         60,//50-->60
                         document.getPageSize().height() - 730, 55);//140->670 ,670->730, 20->45 //45-->55
      */
      cb.endText();
      cb.restoreState();//回復字體顏色的深度
      color = null;
	}
  }

  //寫入頁數總數 Ex. Page 1 of 10
  public void onCloseDocument(PdfWriter writer, Document document) {
    tpl.beginText();
    tpl.setFontAndSize(helv, 10);

    //initializes the current point position
    tpl.setTextMatrix(0, 0);
    tpl.showText("" + (writer.getPageNumber() - 1));
    tpl.endText();
  }
}