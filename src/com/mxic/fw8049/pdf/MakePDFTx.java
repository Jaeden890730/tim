package com.mxic.fw8049.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.mxic.fw8049.action.Fw8049MainActionForm;
import com.mxic.oiplus.oimaintain.ProTestRouteBeanAF;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSProperties;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.TDSLogger;

public class MakePDFTx {

	public MakePDFTx() {
	}

	public static boolean makePDF(Fw8049MainActionForm fm) {
		boolean IdFlag = true;
		Connection conn = null;
		Paragraph par = null;
		Document document = new Document(PageSize.LETTER, 40, 40, 90, 50);
		TDSProperties pdfProp = TDSResource.getProperties("TIMPdf");
		String jpgPath = pdfProp.getValue("jpg_tx.path");

		ProTestRouteBeanAF fmtr = new ProTestRouteBeanAF();
		fmtr.setSid("" + fm.getSid());
		fmtr.setProductbody(fm.getProduct_body());
		fmtr.setBrand(fm.getBrand());
		fmtr.setVersion(fm.getVersion());
		fmtr.setVendor("");

		try {
			BaseFont bfChinese = BaseFont.createFont("MHei-Medium",
					"UniCNS-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font BasicFont = new Font(bfChinese, 8, Font.NORMAL);
			Font SmallFont = new Font(bfChinese, 6, Font.NORMAL);

			conn = DBConnection.getConnection();
			String fileName = pdfService.getFileName(conn,
					fm.getProduct_body(), fm.getBrand(), fm.getVersion(), null,
					false, "_tx");
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(pdfProp.getProperty("pdf.dir")
							+ File.separator + fileName + ".pdf"));
			String pro_b = fm.getProduct_body();
			String version = fm.getVersion();
			String br = fm.getBrand();

			writer.setPageEvent(new PageNumbersWatermark(writer, document,
					pro_b, br, version, "", "", false, true));

			// setEncryption(bit,讀者密碼,擁有者密碼,AllowCopy | AllowPrinting)//
			writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC",
					PdfWriter.AllowPrinting);
			// writer.setEncryption(PdfWriter.STRENGTH128BITS, "", "MXIC",
			// ~(PdfWriter.AllowCopy|PdfWriter.AllowPrinting|PdfWriter.AllowAssembly|PdfWriter.AllowModifyContents|PdfWriter.AllowModifyAnnotations|PdfWriter.AllowFillIn|PdfWriter.AllowScreenReaders|PdfWriter.AllowDegradedPrinting));

			// 產生另一個 dpf 檔案 (保全)
			PdfWriter writer_s = PdfWriter.getInstance(document,
					new FileOutputStream(pdfProp.getProperty("pdf.dir")
							+ File.separator + fileName + "_s.pdf"));
			writer_s.setPageEvent(new PageNumbersWatermark(writer_s, document,
					pro_b, br, version, "", "", false, true));
			writer_s.setEncryption(
					PdfWriter.STRENGTH128BITS,
					"",
					"MXIC",
					~(PdfWriter.AllowCopy | PdfWriter.AllowPrinting
							| PdfWriter.AllowAssembly
							| PdfWriter.AllowModifyContents
							| PdfWriter.AllowModifyAnnotations
							| PdfWriter.AllowFillIn
							| PdfWriter.AllowScreenReaders | PdfWriter.AllowDegradedPrinting));

			document.open();
			// ********************************第零段落****************************//
			pdfService.ProdWaferlevel(document, BasicFont, SmallFont, "0",
					jpgPath, fmtr, "_tx", conn, "");

		} catch (Exception ex) {
			document.close();
			DBConnection.close(conn);
			ex.printStackTrace();
			TDSLogger.println(ex.getMessage());
			IdFlag = false;
		} finally {
			/* 當放到weblogic時，document.close要加上try catch，如放在TOMCAT則不用。 */
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