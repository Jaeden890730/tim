/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	August 11, 2009.
//  Comment :   Initial Version : direct copy from EifTimMes01.java 
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.*;
import java.util.LinkedHashMap;

import com.ibm.mq.MQException;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.SQLStatement;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.eif.EIFService;
import com.mxic.tdsplus.util.SapEncoding;

public class EifTimSap03 extends SQLStatement {
	private StringBuffer queryBuffer;
	private Connection conn = null;
	private LogWriter log;
	private String eifName = "EIFTIMSAP03";
	private MQService mq;

	public EifTimSap03() {
	}

	public void process(String[] args) {
		try {
			conn = DBConnection.getConnection();
			
			int CCode;

			log = new LogWriter(eifName);
			
			mq = new MQService(eifName, log);

			CCode = mq.MQConnect();
			if (CCode != MQException.MQCC_OK) {
				return;
			}

			CCode = mq.OpenQueue();
			if (CCode != MQException.MQCC_FAILED) {
				boolean go = false;
				if (args.length == 0) { // 日常作業, 定期抓取近期生效資料
					if (EIFService.updateInterfaceTime("EIFTIMSAP03",
							"CURRENT_TIME") == 1) {
						queryBuffer = new StringBuffer(
								"select b.version, 'C' ctype, b.product_body, b.brand, a.main_route, a.map_route,\n"
										+ "to_char(sysdate,'yyyymmddhh24miss') datecode,\n"
										+ "get_8049oi_step_list(a.map_route) step_list\n"
										+ "from tf_main_route_xrom a, tf_information b, if_interface_time d -- 現在版\n"
										+ "where a.sid = b.sid\n"
										+ "and b.status = 'R'\n"
										+ "and a.route_type = 0\n"
										+ "and b.log_time between d.last_time and d.current_time\n"
										+ "and d.interface = 'EIFTIMSAP03'\n"
										+ "and not exists (select 1 from tf_main_route_xrom c -- 前一版\n"
										+ "	             where a.product_body = c.product_body\n"
										+ "                      and a.version = c.version+1\n"
										+ "                      and a.main_route = c.main_route\n"
										+ "                      and a.map_route = c.map_route)\n"
										+ "union\n"
										+ "select b.version, 'D' ctype, b.product_body, b.brand, a.main_route, a.map_route,\n"
										+ "to_char(sysdate,'yyyymmddhh24miss') datecode,\n"
										+ "get_8049oi_step_list(a.map_route) step_list\n"
										+ "from tf_main_route_xrom a, tf_information b, if_interface_time d -- 前一版\n"
										+ "where a.product_body = b.product_body\n"
										+ "and a.version = b.version-1\n"
										+ "and b.status = 'R'\n"
										+ "and a.route_type = 0\n"
										+ "and b.log_time between d.last_time and d.current_time\n"
										+ "and d.interface = 'EIFTIMSAP03'\n"
										+ "and not exists (select 1 from tf_main_route_xrom c -- 現在版\n"
										+ "                where a.product_body = c.product_body\n"
										+ "                      and a.version = c.version-1\n"
										+ "                      and a.main_route = c.main_route\n"
										+ "                      and a.map_route = c.map_route)\n"
										+ "order by product_body, version\n");
						log.WriterToLog(queryBuffer.toString());
						go = true;
					} // if interface time
				} else if (args[0].equals("initial")) { // for Data migration
					go = true;
					queryBuffer = new StringBuffer(
							"select b.version, 'C' ctype, b.product_body, b.brand, a.main_route, a.map_route,\n"
									+ "to_char(sysdate,'yyyymmddhh24miss') datecode\n"
									+ "from tf_main_route_xrom a, tf_current_version_vw b\n"
									+ "where a.sid = b.sid\n"
									+ "and a.route_type = 0\n"
									+ "order by product_body, version");
					log.WriterToLog(queryBuffer.toString() + " at " + new Timestamp(System.currentTimeMillis()).toString());
				}

				if (go) {
					int RouteCnt = 0;
					boolean hasData = false;
					String correlID = "M";
					String messageID = "";
					PreparedStatement stmt = conn.prepareStatement(queryBuffer
							.toString());
					ResultSet rs = stmt.executeQuery();
					hasData = rs.next();
					/*while (true) {
						if (!hasData)
							break;
						RouteCnt++;
						StringBuffer outStr = new StringBuffer();
						outStr.append(rs.getString("CTYPE")
								+ rs.getString("PRODUCT_BODY")
								+ rs.getString("BRAND")
								+ padding(rs.getString("MAIN_ROUTE"), 12)
								+ padding(rs.getString("MAP_ROUTE"), 12) + "\n");
						messageID = rs.getString("DATECODE");

						if (hasData = rs.next())
							correlID = "M";
						else
							correlID = "E";

						outputMQ(outStr, messageID, correlID);

						outStr = null;
					}*/
					while (true) {
						if (!hasData)
							break;
						RouteCnt++;
						LinkedHashMap<String, String> info = new LinkedHashMap<String, String>();
						info.put("CTYPE", rs.getString("CTYPE"));
						info.put("PRODUCT_BODY", rs.getString("PRODUCT_BODY"));
						info.put("BRAND", rs.getString("BRAND"));
						info.put("MAIN_ROUTE", rs.getString("MAIN_ROUTE"));
						info.put("MAP_ROUTE", rs.getString("MAP_ROUTE"));

						StringBuffer outStr = new StringBuffer();
						outStr = SapEncoding.formatOutput(info, eifName);

						messageID = rs.getString("DATECODE");

						if (hasData = rs.next())
							correlID = "M";
						else
							correlID = "E";

						outputMQ(outStr, messageID, correlID);

						outStr = null;
					}
					rs.close();
					stmt.close();
					rs = null;
					stmt = null;
					if (args.length == 0)
						EIFService.updateInterfaceTime("EIFTIMSAP03",
								"LAST_TIME");

				}
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			try {
				mq.CloseMQ();
				mq.DisconnectionMQ();
				log.close();
				DBConnection.close(conn);
			} catch (Exception e) {
			}
		}
	}

	private void outputMQ(StringBuffer output, String messageID, String correlID) {
		try {
			String reasonCode = "";

			int CCode = mq.MQSend(output, reasonCode, messageID, correlID);
			if (CCode != MQException.MQCC_OK) {
				log.WriterToLog(eifName + "-SNDMSG", " ACTION FAILED", output);
			} else {
				log.WriterToLog(eifName + "-SNDMSG", " ACTION SUCCESS", output);
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		}
	}

	// return 'fix length' string, padding with blank
	/*private String padding(String item, int length) {
		String str = (item == null ? "" : item);

		for (int i = 0; i < length - item.length(); i++)
			str = str + " ";

		return str;
	}*/

	public static void main(String args[]) {
		if ((args.length == 1) && args[0].equals("?")) {
			System.out.println("Usage : EifTimSap03 [initial]");
			System.out.println("[initial] : sent initial data");
			return;
		}

		EifTimSap03 eif = new EifTimSap03();
		eif.process(args);
	}

}