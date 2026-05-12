package com.mxic.oiplus.eif;

import java.sql.Connection;
import java.util.HashMap;

import com.mxic.gprs.eif.LogWriter;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.DataHandlerUtil;
import com.mxic.oiplus.util.SendMail;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.tdsplus.util.DateUtil;

public class EifOIReleaseCheck {
	private static LogWriter log;
	private static String eifName = "EifOIReleaseCheck";
	private static Connection connection = null;
	private static HashMap[] hm = null;

	public static void main(String[] args) {
		TDSLogger.println("==============================================");
		TDSLogger.println(eifName + " run at " + DateUtil.getNow());
		log = new LogWriter(eifName);
		log.Println("----------START----------" + DateUtil.getNow());
		EifOIReleaseCheck oiReleaseCheck = new EifOIReleaseCheck();
		try {
			oiReleaseCheck.process();
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			TDSLogger.println(eifName + " end at " + DateUtil.getNow());
			TDSLogger.println("==============================================");
			log.Println("----------END----------" + DateUtil.getNow());
		}
	}

	public void process() {
		try {
			connection = DBConnection.getConnection();
			if (EIFService.updateInterfaceTime(eifName, "CURRENT_TIME") == 1) {
				boolean flag = selectOIReleaseCheck();
				if (flag) {
					output();
				} else
					TDSLogger.println("Nothing to do !!");
				EIFService.updateInterfaceTime(eifName, "LAST_TIME");
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			try {
				connection.close();
				connection = null;
			} catch (Exception e) {
			}
		}
	}

	private boolean selectOIReleaseCheck() {
		DataHandlerUtil dataUtil = new DataHandlerUtil();
		try {
			String sql = "select aa.*, bb.*"
					+ "  from (select product_body,"
					+ "               options,"
					+ "               c.package_type,"
					+ "               pin_count,"
					+ "               test_mode,"
					+ "               tester_type,"
					+ "               b.plant_name,"
					+ "               a.apl_status,"
					+ "               log_time"
					+ "          from ap_apl a, ba_plant b, ba_package_type c"
					+ "         where apl_status = 'W'"
					+ "           and body_size = 'NA'"
					+ "           and isexists8049 is null"
					+ "           and process_type = 'FT'"
					+ "           and a.vendor_no = b.sap_plant_no"
					+ "           and vendor_no <> '0000101008'"
					+ "           and a.package_type = c.prm2_code) aa,"
					+ "       (select distinct b.product_body,"
					+ "                        b.backend_option,"
					+ "                        b.test_type,"
					+ "                        b.pin_count,"
					+ "                        trim(to_char(b.pin_count, '009')) pincount,"
					+ "                        b.package_type,"
					+ "                        b.tester,"
					+ "                        b.site,"
					+ "                        a.brand,"
					+ "                        a.version,"
					+ "                        c.log_time"
					+ "          from tf_current_version_vw a,"
					+ "               tf_test_parameter_ft  b,"
					+ "               tf_information        c"
					+ "         where a.sid = b.sid"
					+ "           and a.sid = c.sid) bb"
					+ " where aa.product_body = bb.product_body(+)"
					+ "   and aa.options = bb.backend_option(+)"
					+ "   and aa.test_mode = bb.test_type(+)"
					+ "   and aa.tester_type = bb.tester(+)"
					+ "   and aa.pin_count = bb.pincount(+)"
					+ "   and aa.package_type = bb.package_type(+)"
					+ "   and aa.plant_name = bb.site(+)"
					+ "   and bb.log_time between sysdate - 1 and sysdate";

			hm = dataUtil.getDataBySql(connection, sql, new Object[] {});
			if (hm.length == 0)
				return false;
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
		}
		return true;
	}

	private void output() {
		try {
			StringBuffer content = new StringBuffer();
			content.append(
					"<table style='width: 95%' border='1' cellspacing='0'>")
					.append("<tr>").append("<td>PRODUCT_BODY</td>")
					.append("<td>TEST_MODE</td>").append("<td>PLANT_NAME</td>")
					.append("<td>LOG_TIME</td>").append("<td>VERSION</td>")
					.append("</tr>");
			for (int i = 0; i < hm.length; i++) {
				content.append("<tr>");
				content.append("<td>").append(hm[i].get("PRODUCT_BODY") + "")
						.append("</td>");
				content.append("<td>").append(hm[i].get("TEST_MODE") + "")
						.append("</td>");
				content.append("<td>").append(hm[i].get("PLANT_NAME") + "")
						.append("</td>");
				content.append("<td>").append(hm[i].get("LOG_TIME") + "")
						.append("</td>");
				content.append("<td>").append(hm[i].get("VERSION") + "")
						.append("</td>");
				content.append("</tr>");
			}
			content.append("</table>");
			SendMail.sendHtml("fab_peis@mxic.com.tw", "prodeif@mxic.com.tw",
					"OI Release check¥¼¥Í®Ä", content.toString());
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			TDSLogger.println("--SUCCESS!!!");
		}
	}
}