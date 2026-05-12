package com.mxic.oiplus.common;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class XLSForm {
	//private String filePath = "/tmp";    
	private String filePath = "";    
	private String fileName = null;
	private HashMap sheets = new HashMap();  // key and value, 一個sheet對應到資料
	private HashMap colTitles = new HashMap(); //<string key, string[] colTitle>HashMap key and colTitle , 一個sheet對應到Title, key = sheets.key

	
	
	public String getFilePath() {
		
		if(!"".equals(filePath)){
			return filePath;
		}
		
	    String osName = System.getProperty("os.name");
	    if(osName !=null && osName.indexOf("Windows")>-1){
	    	filePath = "D:\\";
	    }else{
	    	filePath = "/tmp";
	    }
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFullFileName() {
		return this.getFilePath() + File.separator + this.getFileName() + ".XLS";
	}

	public HashMap getSheets() {
		return sheets;
	}

	public void setSheets(HashMap sheets) {
		this.sheets = sheets;
	}

	public String[] getColTitles(String sheet) {
		String[] titles = null;
		if(this.colTitles != null && this.colTitles.containsKey(sheet)){
			titles = (String[])this.colTitles.get(sheet);
		}
		return titles;
	}
	public HashMap getSheetsColTitle() {
		return colTitles;
	}

	public void setSheetsColTitle(HashMap colTitles) {
		this.colTitles = colTitles;
	}
	
	public void createXls() throws Exception {
		
		
		WritableWorkbook writeBook = null;
		writeBook = Workbook.createWorkbook(new File(this.getFullFileName()));

		Iterator sheets = this.getSheets().keySet().iterator();  //sheets 包含key and value
		WritableSheet writeSheet = null;
		Label label = null;
		while (sheets.hasNext()) {
			String sheet = (String) sheets.next();  //key
			HashMap[] data = (HashMap[]) this.getSheets().get(sheet); //value
			writeSheet = writeBook.createSheet(sheet, 0);
			String[] colTitle = this.getColTitles(sheet);
			for (int i = 0; i < data.length; i++) {
				/*
				int j = 0;
				Iterator key1 = data[i].keySet().iterator();  // value 內標頭的名稱
				while (key1.hasNext()) {
					String tableTitle = (String) key1.next();
					if (i == 0) {
						label = new Label(j, i, tableTitle);
						writeSheet.addCell(label);
					}
					label = new Label(j, i + 1, data[i].get(tableTitle) + "");
					writeSheet.addCell(label);
					j++;
				}*/
				Object[] key1 = data[i].keySet().toArray();  // value 內標頭的名稱,順序可能會改變
				if(colTitle != null) key1 = colTitle;		 // 指定標頭的名稱,指定順序
				
				for(int j = 0; j < key1.length; j++){
					String tableTitle = (String) key1[j];
					if (i == 0) {
						label = new Label(j, i, tableTitle);
						writeSheet.addCell(label);
					}
					label = new Label(j, i + 1, data[i].get(tableTitle) + "");
					writeSheet.addCell(label);
				}
			}
		}
		writeBook.write();
		writeBook.close();
	}

}
