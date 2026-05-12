package com.mxic.oiplus.eif;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.mxic.gprs.eif.EifMailList;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.tdsplus.util.DataHandlerUtil;
import com.mxic.tdsplus.util.StringUtil;
import com.mxic.tdsplus.util.TDSLogger;


public class EifAPLSNOVA01 extends EifMailList {
	
	private static String eifName = "EifAPLSNOVA01";
	private static String eifPath = "/EIFDATA/FROMTDS/"+eifName+"/";
	private static String localEifPath = "C:\\develop(ccchang02)\\peis-deploy\\peis_data\\EIFDATA\\FROMTDS\\"+eifName;

	private final String UPLOAD_FILE_NAME = "EifAPLSNOVA";
	private final String FILE_ENCODING = "BIG5";

	private LogWriter log;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmsssss");

	public EifAPLSNOVA01() {
	}

	public static void main(String args[]) {
		EifAPLSNOVA01 eif = new EifAPLSNOVA01();
		try {
			eif.process();
		} catch (Exception e) {
			TDSLogger.println(e);
		}
	}

	public void process() {
		log = new LogWriter(eifName);
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			if (conn == null)
				log.WriterToLog("DB connection error " + conn);

			String dirPath = this.getEifPath();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String time = sdf.format(new Date());
			String filename = UPLOAD_FILE_NAME + "-" + time + ".csv";

			File f = new File(dirPath + File.separatorChar, filename);
			output(conn, f);

		} catch (Exception e) {
			TDSLogger.println(e);
			log.WriterToLog(e.getMessage());
			sendMailAdmin("Run " + eifName + " fail\n", StringUtil.makeStackTrace(e));
		} finally {
			DBConnection.close(conn);
			log.close();
		}
	}

	private String getEifPath() {
		String dirPath = super.isWendowsOS() ? localEifPath : eifPath;
		File filePath = new File(dirPath);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		return dirPath;
	}

	public static HashMap<String, String>[] getList(Connection conn) throws Exception{
		String sql = 
				"SELECT DISTINCT L.VENDOR_NO,\n" +
				"                TO_NUMBER(L.PIN_COUNT) PIN_COUNT,\n" + 
				"                L.PACKAGE_TYPE,\n" + 
				"                L.BODY_SIZE,\n" + 
				"                SUBSTR(L.CARRIER_TYPE, 1, 2) CARRIER_TYPE\n" + 
				"  FROM TIM.AP_APL L\n" + 
				" WHERE L.PROCESS_TYPE = 'FVI'";

		DataHandlerUtil util = new DataHandlerUtil();
		return util.getDataBySql(conn, sql);
	}

	private void output(Connection conn, File f) throws Exception {
		StringBuffer sumText = new StringBuffer();
		sumText.append("\"VENDORNO\",\"PINCOUNT\",\"PACKAGCODE\",\"BODYSIZE\",\"CARRIERTYPECODE\"\n");
		HashMap<String, String>[] list = this.getList(conn);

		for (HashMap<String, String> hm : list) {
			String vendor_no = hm.get("VENDOR_NO") == null ? "" : hm.get("VENDOR_NO");
			String pin_count = hm.get("PIN_COUNT") == null ? "" : hm.get("PIN_COUNT");
			String package_type = hm.get("PACKAGE_TYPE") == null ? "" : hm.get("PACKAGE_TYPE");
			String body_size = hm.get("BODY_SIZE") == null ? "" : hm.get("BODY_SIZE");
			String carrier_type = hm.get("CARRIER_TYPE") == null ? "" : hm.get("CARRIER_TYPE");
			sumText.append("\"" + vendor_no + "\",\"" + pin_count + "\",\"" + package_type + "\",\"" + body_size + "\",\"" + carrier_type + "\"\n");
		}

		log.WriterToLog("output [" + f.getName() + "]");
		writeFile(f, sumText.toString().toCharArray());
		log.WriterToLog(list.length+"");
	}

	private String space(int n) {
		StringBuffer sb = new StringBuffer(n);
		for (int i = 0; i < n; i++)
			sb.append(' ');
		return sb.toString();
	}

	private void writeFile(File file, char[] buffer) throws IOException {
		FileWriter rp = new FileWriter(file);
		rp.write(buffer, 0, buffer.length);
		rp.flush();
		rp.close();
	}

	private void writeFile(File file, String content) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, false), FILE_ENCODING);
		osw.write(content);
		osw.close();
	}
}
