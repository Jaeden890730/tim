/******************************************************************************************************/
//	Author	: 	Robin Mao
//	Date	:	June 16, 2009.
/******************************************************************************************************/
package com.mxic.oiplus.eif;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.*;

import com.ibm.mq.MQException;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.SQLStatement;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.oiplus.eif.EIFService;

public class EifTimMes01 extends SQLStatement
{
	private StringBuffer queryBuffer;
	private Connection conn = null;
	private LogWriter log;
	private String eifName = "EIFTIMMES01";
	private MQService mq;

	public EifTimMes01() {
	}

	public void process(String[] args)
	{
		try
		{
			conn = DBConnection.getConnection();

			int CCode;

			log = new LogWriter(eifName);
			mq = new MQService(eifName,log);

			CCode = mq.MQConnect();
			if(CCode != MQException.MQCC_OK){
				return;
			}
			CCode = mq.OpenQueue();
			if(CCode != MQException.MQCC_FAILED){
				boolean go = false;
				if (args.length==0) { // 日常作業, 定期抓取近期生效資料
					if (EIFService.updateInterfaceTime("EIFTIMMES01", "CURRENT_TIME") == 1) {
						queryBuffer = new StringBuffer("select b.version, 'C' ctype, b.product_body, b.brand, a.main_route, a.map_route,\n"+ 
								"get_8049oi_step_list(a.map_route) step_list\n"+
								"from tf_main_route_xrom a, tf_information b, if_interface_time d -- 現在版\n"+
								"where a.sid = b.sid\n"+
								"and b.status = 'R'\n"+
								"and a.route_type = 0\n"+
								"and b.log_time between d.last_time and d.current_time\n"+
								"and d.interface = 'EIFTIMMES01'\n" +
								"and not exists (select 1 from tf_main_route_xrom c -- 前一版\n"+
								"	             where a.product_body = c.product_body\n"+
								"                      and a.version = c.version+1\n"+
								"                      and a.main_route = c.main_route\n"+
								"                      and a.map_route = c.map_route)\n"+
								"union\n"+                      
								"select b.version, 'D' ctype, b.product_body, b.brand, a.main_route, a.map_route,\n"+ 
								"get_8049oi_step_list(a.map_route) step_list\n"+
								"from tf_main_route_xrom a, tf_information b, if_interface_time d -- 前一版\n"+
								"where a.product_body = b.product_body\n"+
								"and a.version = b.version-1\n"+
								"and b.status = 'R'\n"+
								"and a.route_type = 0\n"+
								"and b.log_time between d.last_time and d.current_time\n"+
								"and d.interface = 'EIFTIMMES01'\n" +
								"and not exists (select 1 from tf_main_route_xrom c -- 現在版\n"+
								"                where a.product_body = c.product_body\n"+
								"                      and a.version = c.version-1\n"+
								"                      and a.main_route = c.main_route\n"+
								"                      and a.map_route = c.map_route)\n"+
							"order by product_body, version\n");
						go = true;
					} // if interface time
				} else if (args[0].equals("initial")) { // for Data migration 
					go = true;
					queryBuffer = new StringBuffer("select b.version, 'C' ctype, b.product_body, b.brand, a.main_route, a.map_route,\n"+ 
							"get_8049oi_step_list(a.map_route) step_list\n"+
							"from tf_main_route_xrom a, tf_current_version_vw b\n"+
							"where a.sid = b.sid\n"+
							"and a.route_type = 0\n"+
							"order by product_body, version");
				}
				
				if (go) {
					PreparedStatement stmt = conn.prepareStatement(queryBuffer.toString());
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						StringBuffer outStr = new StringBuffer();
						outStr.append(rs.getString("CTYPE")+";"+
								rs.getString("PRODUCT_BODY")+";"+
								rs.getString("BRAND")+";"+
								rs.getString("MAIN_ROUTE")+";"+
								rs.getString("MAP_ROUTE")+";"+
								rs.getString("STEP_LIST")+";"
						);
						outputMQ(outStr);
						outStr = null;
					}
					rs.close();
					stmt.close();
					rs = null;
					stmt = null;
					if (args.length==0)
							EIFService.updateInterfaceTime("EIFTIMMES01", "LAST_TIME");

				}
			}
		}
		catch(Exception e)
		{
			TDSLogger.println(e);
		}
		finally
		{
			try
			{
				mq.CloseMQ();
				mq.DisconnectionMQ();
				log.close();
				DBConnection.close(conn);
			}
			catch(Exception e)
			{
			}
		}
	}

	private void outputMQ(StringBuffer output)
	{
		try
		{
			String reasonCode = "";

			int CCode = mq.MQSend(output,reasonCode);
			if(CCode != MQException.MQCC_OK){
				log.WriterToLog(eifName+"-SNDMSG"," ACTION FAILED",output);
			}else{
				log.WriterToLog(eifName+"-SNDMSG"," ACTION SUCCESS",output);
			}
		}
		catch(Exception e)
		{
			TDSLogger.println(e);
		}
	}

	public static void main(String args[])
	{
		if ((args.length == 1) && args[0].equals("?"))
		{
			System.out.println("Usage : EifTimMes01 [initial]");
			System.out.println("[initial] : sent initial data");
			return;
		}

		EifTimMes01 eif = new EifTimMes01();
		eif.process(args);
	}

}