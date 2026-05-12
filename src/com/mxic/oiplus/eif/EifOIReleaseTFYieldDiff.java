package com.mxic.oiplus.eif;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.mxic.oiplus.oimaintain.EditiionCompareService;
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.DataHandlerUtil;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.SendMail;
import com.mxic.oiplus.util.TDSLogger;

public class EifOIReleaseTFYieldDiff {

	/**
	 * 1.8049 OI 生效後, 系統自動比對是否有CP/FT Yield  table 變動。如沒有變動, 則PEIS Basic data 自動引用的, 要自動更新版本.
	 * 有差異不UPDATE
	 * 沒差異要UPDATE
	 * 需考量:
	 * CP  Hold / Dgrade  ( MX/KH )
	 * FT  Hold ( MX/KH)
	 * 2.每日早上7:00 比對 :  PEIS Basic data 自動引用的版本 vs  8049 生效版本,  如有不符時, 要將不符的產品 mail 出來.
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = null;
		ArrayList<String> list = null;

		try {
			
			conn = DBConnection.getConnection();
			
			if ((args.length == 1 ||args.length == 2)  && args[0].equals("OI")) {
				//抓取資料來源，可能一筆也可能多筆
				if(args.length == 1){
					if (EIFService.updateInterfaceTime("EifOIReleaseTFYieldDiff", "CURRENT_TIME", conn) == 1) {
						list = getRecentlyOI(conn);
					}
				}else{
					list = getRecentlyOIByPara(conn, args[1]);
				}
				
				if (list == null) {
					TDSLogger.println("OI Release: Nothing to do !!");
					if(args.length == 1)
						EIFService.updateInterfaceTime("EifOIReleaseTFYieldDiff", "LAST_TIME", conn);
					return;
				}
				//處理資料
				String oi = null;
				Iterator<String> i = list.iterator();
				while (i.hasNext()) {
					oi = (String) i.next();
					String attr[] = oi.split(","); // sid(111), product_body(6311), brand(MX), version(66), status(R), product_type(NVM)
					TDSLogger.println("Processing EifOIReleaseTFYieldDiff: " + attr[0] + " " + attr[1] + " " + attr[2] + " " + attr[3] + " " + attr[4]);
					updateYieldDiff(conn, attr[0], attr[1], attr[2], attr[3],attr[5]);
				}
				if(args.length == 1)
					EIFService.updateInterfaceTime("EifOIReleaseTFYieldDiff", "LAST_TIME", conn);
								
			}else if (args.length == 1 && args[0].equals("MAIL")) {
				//String mailto = "PhoebeChen@mxic.com.tw,ChifuLee@mxic.com.tw,PondWu@mxic.com.tw,RWGwan@mxic.com.tw,ZGLin@mxic.com.tw,IvyHsu@mxic.com.tw,TommyWang@mxic.com.tw";
				String mailto =  (String) TDSResource.getProperties("EIF").get("EifOIReleaseTFYieldDiff.mailto");
				String mailfrom = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailfrom");
				String mailsubject = "8049 OI生效版本與PEIS 自動引用版本不符 List";
				StringBuffer mailbody = new StringBuffer();
				
				HashMap[] h = isVrsnDiffSql(conn);
				if (h != null && h.length > 0) {
					mailbody.append(genMailBody(h));
					TDSLogger.println("sendMail~~~");
					SendMail.sendHtml(mailto, mailfrom, mailsubject, mailbody.toString());
				}
				
			}else {
				System.out.println("USAGE: EifOIReleaseTFYieldDiff [TYPE][SID]");
				System.out.println("   EX: EifOIReleaseTFYieldDiff OI");
				System.out.println("   EX: EifOIReleaseTFYieldDiff OI 242754");
				System.out.println("   EX: EifOIReleaseTFYieldDiff MAIL");
				System.exit(0);
			}
	
		} catch (Exception ex) {
			DBConnection.rollback(conn);
			TDSLogger.println(ex);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
	}
	
	/**
	 * @param conn
	 * @param sid, product_body, brand, version
	 * @return
	 */
	public static void updateYieldDiff(Connection conn, String sid, String product_body, String brand, String version,String product_type) {
		try {
			String sid2 = OiMaintainService.getPreviousVersionSid(conn, sid);
			String previousVersion = String.valueOf(Integer.parseInt(version)-1);
			ArrayList ags = new ArrayList();
			
			if(product_type == null )
				return ;
			ArrayList<HashMap<String,String>> al = new ArrayList();
			if (product_type.equals("NVM")) {
				/*isYieldDiff比對有差異，回傳TURE; 比對沒差異回傳FALSE*/
				boolean flag1 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", "R", "Hold");
				boolean flag2 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", "R", "Dgrade");
				boolean flag3 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "FT", "R", "");
				if (!flag1) { /*FALSE前面加上! 負負得正*/
					if (update8049CopyStatus(conn, "0", product_body, brand, version, previousVersion, "Hold", "TIM")) {
						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("FACILITY", "WS");
						hm.put("TYPE", "Hold");
						hm.put("PRODUCT_BODY", product_body);
						hm.put("BRAND", brand);
						hm.put("VERSION", version);
						hm.put("PREVIOUS_VERSION", previousVersion);
						al.add(hm);
					}
				}
				if (!flag2) {
					if (update8049CopyStatus(conn, "0", product_body, brand, version, previousVersion, "Downgrade", "TIM")) {
						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("FACILITY", "WS");
						hm.put("TYPE", "Downgrade");
						hm.put("PRODUCT_BODY", product_body);
						hm.put("BRAND", brand);
						hm.put("VERSION", version);
						hm.put("PREVIOUS_VERSION", previousVersion);
						al.add(hm);
					}
				}
				if (!flag3) {
					if (update8049CopyStatus(conn, "1", product_body, brand, version, previousVersion, "Hold", "TIM")) {
						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("FACILITY", "FT");
						hm.put("TYPE", "Hold");
						hm.put("PRODUCT_BODY", product_body);
						hm.put("BRAND", brand);
						hm.put("VERSION", version);
						hm.put("PREVIOUS_VERSION", previousVersion);
						al.add(hm);
					}
				}
			} else {
				boolean flag1 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", "R", "acs");
				boolean flag2 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", "R", "hcs");
				boolean flag3 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "FT", "R", "acs");
				boolean flag4 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "FT", "R", "hcs");
				if (!flag1 && !flag2) {
					if (update8049CopyStatus(conn, "0", product_body, brand, version, previousVersion, "Hold", "TIM")) {
						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("FACILITY", "WS");
						hm.put("TYPE", "Hold");
						hm.put("PRODUCT_BODY", product_body);
						hm.put("BRAND", brand);
						hm.put("VERSION", version);
						hm.put("PREVIOUS_VERSION", previousVersion);
						al.add(hm);
					}
				}
				if (!flag3 && !flag4) {
					if (update8049CopyStatus(conn, "1", product_body, brand, version, previousVersion, "Hold", "TIM")) {
						HashMap<String, String> hm = new HashMap<String, String>();
						hm.put("FACILITY", "FT");
						hm.put("TYPE", "Hold");
						hm.put("PRODUCT_BODY", product_body);
						hm.put("BRAND", brand);
						hm.put("VERSION", version);
						hm.put("PREVIOUS_VERSION", previousVersion);
						al.add(hm);
					}
				}
			}
			
			
			/*ArrayList<HashMap<String,String>> al = new ArrayList();
	        boolean flag1 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", "R", "Hold");
			if (!flag1) {
				if(update8049CopyStatus(conn, "0", product_body, brand, version, previousVersion, "Hold", "TIM")){
					HashMap<String,String> hm = new HashMap<String,String>();
					hm.put("FACILITY", "WS");
					hm.put("TYPE", "Hold");
					hm.put("PRODUCT_BODY", product_body);
					hm.put("BRAND", brand);
					hm.put("VERSION", version);
					hm.put("PREVIOUS_VERSION", previousVersion);
					al.add(hm);
				}
			}
			
	        boolean flag2 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "WS", "R", "Dgrade");
			if (!flag2) {
				if(update8049CopyStatus(conn, "0", product_body, brand, version, previousVersion, "Downgrade", "TIM")){
					HashMap<String,String> hm = new HashMap<String,String>();
					hm.put("FACILITY", "WS");
					hm.put("TYPE", "Downgrade");
					hm.put("PRODUCT_BODY", product_body);
					hm.put("BRAND", brand);
					hm.put("VERSION", version);
					hm.put("PREVIOUS_VERSION", previousVersion);
					al.add(hm);
				}
			}
			
	        boolean flag3 = EditiionCompareService.isYieldDiff(conn, sid, sid2, "FT", "R", "");
			if (!flag3) {
				if(update8049CopyStatus(conn, "1", product_body, brand, version, previousVersion, "Hold", "TIM")){
					HashMap<String,String> hm = new HashMap<String,String>();
					hm.put("FACILITY", "FT");
					hm.put("TYPE", "Hold");
					hm.put("PRODUCT_BODY", product_body);
					hm.put("BRAND", brand);
					hm.put("VERSION", version);
					hm.put("PREVIOUS_VERSION", previousVersion);
					al.add(hm);
				}
			}
	        
			*/
			if(al.size() > 0){
				String mailto = "PhoebeChen@mxic.com.tw";
				String mailfrom = (String) TDSResource.getProperties("EIF").get("e8049OIRelease.mailfrom");
				String mailsubject = "PEIS 8049 criteria 自動引用進版通知";
				StringBuffer mailbody = new StringBuffer();
				mailbody.append(genMailBody((HashMap[])al.toArray(new HashMap[0])));
				TDSLogger.println("sendMail~~~");
				SendMail.sendHtml(mailto, mailfrom, mailsubject, mailbody.toString());
			}else{
				//TDSLogger.println("====== 未進版 =====");
			}
		} catch (Exception ex) {
			TDSLogger.println(ex);
		} finally {
		}
	}
	
	public static boolean update8049CopyStatus(Connection conn, String facility, String productBody, String brand, String version, String previousVersion, String setType, String userName) {
		String updateSql = "UPDATE TDS.BA_8049_COPY_STATUS SET VERSION = ?, LOG_TIME = SYSDATE, USER_NAME = ? " +
	                       "WHERE FACILITY = ? AND PRODUCT_BODY = ? AND BRAND = ? AND SET_TYPE = ? AND VERSION = ?";

		Object[] wheres = new Object[7];
		wheres[0] = version;
		wheres[1] = userName;
		wheres[2] = facility;
		wheres[3] = productBody;
		wheres[4] = brand;
		wheres[5] = setType;
		wheres[6] = previousVersion;
		//wheres[6] = (Integer.parseInt(version)-1)+"";  //phoebe 確保更新的版本不會跳掉，一天內可能有差異又無差異，更新的話，會讓有差異的資訊lose 20180313
		int count = 0;
		try {
			count = new DataHandlerUtil().updateByPrepareSQL(conn, updateSql, wheres);
			if(count > 0)
				return true;
		} catch (Exception e) {
			TDSLogger.println(e);
			return false;
		}
		return false;
	}

	
	public static StringBuffer genMailBody(HashMap[] h) {
		StringBuffer m = new StringBuffer();
		if (h != null && h.length > 0) {
			
			m.append("<TABLE BORDER=\"1\">");
			m.append("<TR><TH>&nbsp;&nbsp;&nbsp;</TH>");
			
			Iterator itr = h[0].keySet().iterator();
			while (itr.hasNext()) {
				String fieldName = (String) itr.next();
				m.append("<TH>"+fieldName+"</TH>");
			}

			for (int i = 0; i < h.length; i++) {
				m.append("<TR><TD>"+(i+1)+"</TD>");
				itr = h[i].keySet().iterator();

				while (itr.hasNext()) {
					String fieldName = (String) itr.next();
					if(h[i].get(fieldName)!= null){
						m.append("<TD>"+h[i].get(fieldName)+"</TD>");
					}else{
						m.append("<TD>&nbsp;</TD>");
					}
				}
				m.append("</TR>");
			}
		}
		return m;
	}

/*	public static HashMap[] isYieldDiffSql(Connection conn, String facility, String p_cur_sid, String p_pre_sid, String key) {

		String sql =
						"      SELECT 1 ord,\n" + 
						"             'remove' type,\n" + 
						"             a.yid,\n" + 
						"             a.sid,\n" + 
						"             a.seq,\n" + 
						"             a.facility,\n" + 
						"             a.product_code,\n" + 
						"             a.brand,\n" + 
						"             a.test_mode,\n" + 
						"             a.lower_limit,\n" + 
						"             a.flag1,\n" + 
						"             a.upper_limit,\n" + 
						"             a.flag2,\n" + 
						"             a.item_type,\n" + 
						"             a.item,\n" + 
						"             a.item_mode2,\n" + 
						"             a.item_bins2,\n" + 
						"             a.action,\n" + 
						"             a.by_lot_dg,\n" + 
						"             a.change_ipn,\n" + 
						"             a.route_name,\n" + 
						"             a.start_step,\n" + 
						"             a.remark,\n" + 
						"             a.dg_action,\n" + 
						"             a.item_seq \n" + 
						"        FROM tf_yield_definition a\n" + 
						"       WHERE a.sid = "+p_pre_sid+"\n" + 
						"         AND a.facility = "+facility+"\n" + 
                        "         AND a.action like '"+key+"'||'%'\n" + 
						"         AND NOT EXISTS (SELECT 1\n" + 
						"                FROM tf_yield_definition b\n" + 
						"               WHERE b.sid = "+p_cur_sid+"\n" + 
						"                 AND a.product_code = b.product_code\n" + 
						"                 AND nvl(a.brand,' ') = nvl(b.brand,' ')\n" + 
						"                 AND a.test_mode = b.test_mode\n" + 
						"                 AND a.item = b.item\n" + 
						"                 AND a.action = b.action\n" + 
						"                 AND a.facility = "+facility+")\n" + 
						"union all\n" + 
						"      SELECT 2 ord,\n" + 
						"             'insert' type,\n" + 
						"             a.yid,\n" + 
						"             a.sid,\n" + 
						"             a.seq,\n" + 
						"             a.facility,\n" + 
						"             a.product_code,\n" + 
						"             a.brand,\n" + 
						"             a.test_mode,\n" + 
						"             a.lower_limit,\n" + 
						"             a.flag1,\n" + 
						"             a.upper_limit,\n" + 
						"             a.flag2,\n" + 
						"             a.item_type,\n" + 
						"             a.item,\n" + 
						"             a.item_mode2,\n" + 
						"             a.item_bins2,\n" + 
						"             a.action,\n" + 
						"             a.by_lot_dg,\n" + 
						"             a.change_ipn,\n" + 
						"             a.route_name,\n" + 
						"             a.start_step,\n" + 
						"             a.remark,\n" + 
						"             a.dg_action,\n" + 
						"             a.item_seq \n" + 
						" FROM tf_yield_definition a\n" + 
						"       WHERE a.sid = "+p_cur_sid+"\n" + 
						"         AND a.facility = "+facility+"\n" + 
						"         AND a.action like '"+key+"'||'%'\n" + 
						"         AND NOT EXISTS (SELECT 1\n" + 
						"                FROM tf_yield_definition b\n" + 
						"               WHERE b.sid = "+p_pre_sid+"\n" + 
						"                 AND a.product_code = b.product_code\n" + 
						"                 AND nvl(a.brand,' ') = nvl(b.brand,' ')\n" + 
						"                 AND a.test_mode = b.test_mode\n" + 
						"                 AND a.item = b.item\n" + 
						"                 AND a.action = b.action\n" + 
						"                 AND a.facility = "+facility+")\n" + 
						"union all\n" + 
						"      SELECT 3 ord,\n" + 
						"             'old' type,\n" + 
						"             a.yid,\n" + 
						"             a.sid,\n" + 
						"             a.seq,\n" + 
						"             a.facility,\n" + 
						"             a.product_code,\n" + 
						"             a.brand,\n" + 
						"             a.test_mode,\n" + 
						"             a.lower_limit,\n" + 
						"             a.flag1,\n" + 
						"             a.upper_limit,\n" + 
						"             a.flag2,\n" + 
						"             a.item_type,\n" + 
						"             a.item,\n" + 
						"             a.item_mode2,\n" + 
						"             a.item_bins2,\n" + 
						"             a.action,\n" + 
						"             a.by_lot_dg,\n" + 
						"             a.change_ipn,\n" + 
						"             a.route_name,\n" + 
						"             a.start_step,\n" + 
						"             a.remark,\n" + 
						"             a.dg_action,\n" + 
						"             a.item_seq \n" + 
						"        FROM tf_yield_definition a, tf_yield_definition b\n" + 
						"       WHERE a.sid = "+p_pre_sid+"\n" + 
						"         AND a.facility = "+facility+"\n" + 
						"         AND b.facility = "+facility+"\n" + 
						"         AND b.sid = "+p_cur_sid+"\n" + 
						"         AND a.product_code = b.product_code\n" + 
						"         AND nvl(a.brand,' ') = nvl(b.brand,' ')\n" + 
						"         AND a.test_mode = b.test_mode\n" + 
						"         AND a.item = b.item\n" + 
						"         AND a.action = b.action\n" + 
						"         AND a.action like '"+key+"'||'%' \n" + 
						"         AND (nvl(a.item_mode2, '#') != nvl(b.item_mode2, '#') OR\n" + 
						"             nvl(a.item_bins2, '#') != nvl(b.item_bins2, '#') OR\n" + 
						"             nvl(a.lower_limit, '#') != nvl(b.lower_limit, '#') OR\n" + 
						"             nvl(a.upper_limit, '#') != nvl(b.upper_limit, '#') OR\n" + 
						"             nvl(a.flag1, '#') != nvl(b.flag1, '#') OR\n" + 
						"             nvl(a.flag2, '#') != nvl(b.flag2, '#') OR\n" + 
						"             nvl(a.by_lot_dg, '#') != nvl(b.by_lot_dg, '#') OR\n" + 
						"             nvl(a.dg_action, '#') != nvl(b.dg_action, '#') OR\n" + 
						"             nvl(a.change_ipn, '#') != nvl(b.change_ipn, '#') OR\n" + 
						"             nvl(a.route_name, '#') != nvl(b.route_name, '#') OR\n" + 
						"             nvl(a.start_step, '#') != nvl(b.start_step, '#') OR\n" + 
						"             nvl(a.remark, '#') != nvl(b.remark, '#'))";
			
		HashMap[] hm = null;
		try {

			hm = GPRSDB.qryHashMapBySql(conn, sql, new Object[] {});
			TDSLogger.println("isYieldDiffSql() - sql:\n" + sql);

		} catch (Exception ex) {
			TDSLogger.println(ex);
		} finally {
		}
		return hm;
	}*/
	
	/*
	 * 
	 */
	public static HashMap[] isVrsnDiffSql(Connection conn) {

		String sql = 
                "select a.product_body, decode(a.facility,'0','CP','1','FT') AS FACILITY, a.brand, a.set_type, b.version, a.version peis_version, c.log_time AS OI_RELEASE_DATE\n" +
                "from tds.ba_8049_copy_status a, tim.tf_current_version_vw b , tim.tf_information c\n" + 
                "where a.product_body = b.product_body and a.brand = b.brand\n" + 
                "   and a.version != b.version\n" + 
                "	and b.product_body = c.product_body\n" +
                "	and b.brand = c.brand\n" + 
                "	and b.version = c.version " + 
                "order by a.facility, a.product_body, a.brand, a.set_type, b.version";
						
		HashMap[] hm = null;
		try {

			hm = GPRSDB.qryLinkedHashMapBySql(conn, sql, new Object[] {});
			TDSLogger.println("isVersionDiffSql() - sql:\n" + sql);

		} catch (Exception ex) {
			TDSLogger.println(ex);
		} finally {
		}
		return hm;
	}
	
	/**
	 * 比對差異只要比對ba_8049_copy_status有的產品就好
	 * @param conn
	 * @return
	 */
	public static ArrayList getRecentlyOI(Connection conn) {
		String sql;
		ArrayList vl = null;

		try {  
            sql = 
                    "select distinct a.sid, a.product_body, a.brand, a.version, a.status, a.product_type\n" +
                    "from tim.tf_information a, tds.ba_8049_copy_status b,tim.if_interface_time e\n" + 
                    "where a.status in ('R')\n" + 
                    "and a.log_time between e.last_time and e.current_time\n" + 
                    "and e.interface = 'EifOIReleaseTFYieldDiff'\n" + 
                    "and b.product_body = a.product_body\n" + 
                    "and b.brand = a.brand";

            
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				if (vl == null)
					vl = new ArrayList();

				vl.add(rs.getString("sid") + "," + rs.getString("product_body") + "," + rs.getString("brand") + "," + rs.getString("version") + "," + rs.getString("status") + "," + rs.getString("product_type"));
			}
			rs.close();
			rs = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
		}
		return vl;
	}
	
	/**
	 * 單獨抓取某一份OI檢查
	 * @param conn
	 * @param sid
	 * @return
	 */
	public static ArrayList getRecentlyOIByPara(Connection conn, String sid) {
		String sql;
		ArrayList vl = null;

		try {
			sql = "select sid, product_body, brand, version, status, product_type from tim.tf_information " + 
        			" where status in ('R') " + 
        			" and sid= '" + sid + "'";

			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				if (vl == null)
					vl = new ArrayList();

				vl.add(rs.getString("sid") + "," + rs.getString("product_body") + "," + rs.getString("brand") + "," + rs.getString("version") + "," + rs.getString("status") + "," + rs.getString("product_type"));
			}
			rs.close();
			rs = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			ex.fillInStackTrace();
			TDSLogger.println(ex.getMessage());
			return null;
		} finally {
		}
		return vl;
	}
}
