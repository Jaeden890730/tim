package com.mxic.oiplus.oimaintain;

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.dao.TfYieldGroupItemsTxDao;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;

public class YieldDefService {
	public YieldDefService() {
	}

	//reset data (data of privious version will be copied in YieldDefAction()) 
	public static boolean reset(String sid,
			                    String proc_type) {

		StringBuffer Sql = new StringBuffer();
		Connection conn = null;
		try { 
			conn = DBConnection.getConnection();
			Sql.append("delete from tf_yield_definition_tx\n" +
					   "where sid = ? and facility = ?");
			PreparedStatement ps = conn.prepareStatement(Sql.toString());
			ps.setInt(1, Integer.parseInt(sid));
			ps.setInt(2, (proc_type.equals("WS")?0:1));
			ps.executeUpdate();
			ps.close();
			ps = null;
			Sql.delete(0, Sql.length());
			Sql.append("update tf_information set tf_yield_" + proc_type + "='N'\n" +
			   "where sid = ?");
			ps = conn.prepareStatement(Sql.toString());
			ps.setInt(1, Integer.parseInt(sid));
			ps.executeUpdate();
			ps.close();
			ps = null;
			//add delete Yield Group Items on 2020/06/19
			TfYieldGroupItemsTxDao.deleteBySid(conn, sid);
		} catch (Exception ex) {
			TDSLogger.println(ex);
			DBConnection.rollback(conn);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return false;
	}

	/* copy yield info from fromProduct/fromMode to toProduct/toMode
	 1. get data count from fromProduct
	 2. if no count, return "No Data";
	 3. get max seq from toProduct
	 4. copy data to toProduct using new yid & seq
	 ps1. if fromProduct Body= toProduct Body, copy data from _TX table, otherwise, from released table
	 ps2. if toMode = '', copy fromProduct to toProduct
	*/ 
	public static String copyTo(String sid,
								String proc_type, 
								String fromProduct,
								String fromMode,
								String toProduct,
								String toMode,
								String brand) {
		StringBuffer Sql = new StringBuffer();
		if (fromMode.equals("")) {
			fromMode = "%";
			toMode = "%";
		}
			
		int cnt = 0;
		int maxSeq = 0;
		int aebretentioncount = 0;
		
		Connection conn = null;
		try { 
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// 取得 最大 sequence number 備用
			Sql.append("select nvl(max(seq),0) seq from tf_yield_definition_tx\n" +
				"where sid = ? and facility = ?");
			PreparedStatement ps = conn.prepareStatement(Sql.toString());
			ps.setInt(1, Integer.parseInt(sid));
			ps.setInt(2, (proc_type.equals("WS")?0:1));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				maxSeq = Integer.parseInt(rs.getString("seq"));
			rs.close();
			ps.close();
			rs = null; ps = null;
			
			// 取得 source 端資料
			Sql.delete(0, Sql.length());
			
			boolean bodyFlag = false;
			if (fromProduct.substring(0,4).equals(toProduct.subSequence(0,4))){
				Sql.append("select * from tf_yield_definition_tx\n" +
						"where sid = ? and facility = ?\n" +
						"and product_code= ? and test_mode like ?");
				bodyFlag = true;
			} else {
				Sql.append("select * from tf_yield_definition a, tf_current_version_vw b\n" +
						"where a.sid = b.sid and a.facility = ?\n" +
						"and a.product_code= ? and a.test_mode like ?");
				    if(brand != null && brand.length() > 0)
				        Sql.append("and b.brand = ? ");
			}   
					   
			ps = conn.prepareStatement(Sql.toString());
			if (fromProduct.substring(0,4).equals(toProduct.subSequence(0,4))) {
				ps.setInt(1, Integer.parseInt(sid));
				ps.setInt(2, (proc_type.equals("WS")?0:1));
				ps.setString(3, fromProduct);
				ps.setString(4, fromMode);
			} else {
				ps.setInt(1, (proc_type.equals("WS")?0:1));
				ps.setString(2, fromProduct);
				ps.setString(3, fromMode);
				if(brand != null && brand.length() > 0)
				    ps.setString(4, brand);
			}
			
			rs = ps.executeQuery();

			// copy data from source to destionation
			Sql.delete(0, Sql.length());
			Sql.append("insert into tf_yield_definition_tx\n" +
					" (YID, SID, SEQ, FACILITY, PRODUCT_CODE, BRAND, TEST_MODE, LOWER_LIMIT, FLAG1, UPPER_LIMIT, FLAG2, ITEM_TYPE, ITEM, ITEM_MODE2, ITEM_BINS2, ACTION, CHANGE_IPN, ROUTE_NAME, START_STEP, REMARK, DG_ACTION, ITEM_SEQ, BY_LOT_DG, DGRADE_SPECIAL_IPN, SNOVA_ID, VERSION, DGRADEPRODCODE, GROUPITEMS_NO)\n" +
					"values(tf_yield_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)\n");
			PreparedStatement psIns = conn.prepareStatement(Sql.toString());
			psIns.setInt(1,Integer.parseInt(sid));
			psIns.setInt(3,(proc_type.equals("WS")?0:1));
			psIns.setString(4, toProduct);
			
			while (rs.next()) {
				if (cnt == 0) {
					// source 端有資料, 先刪除 destination 端資料
					StringBuffer sqlDelete = new StringBuffer();
					sqlDelete.append("delete from tf_yield_definition_tx\n" +
						"where sid = ? and facility = ?\n" +
						"and product_code = ? and test_mode like ?");
					PreparedStatement psDel = conn.prepareStatement(sqlDelete.toString());
					psDel.setInt(1, Integer.parseInt(sid));
					psDel.setInt(2, (proc_type.equals("WS")?0:1));
					psDel.setString(3, toProduct);
					psDel.setString(4, toMode);
					psDel.executeUpdate();
					psDel.close();
					psDel = null;
				}
				psIns.setInt(2,++maxSeq);
				psIns.setString(5, rs.getString("BRAND"));
				if (fromMode.equals("%")) // copy full productCode or certain test mode
					psIns.setString(6, rs.getString("TEST_MODE"));
				else
					psIns.setString(6, toMode);
				psIns.setString(7, (String)StringUtil.formatNull(rs.getString("LOWER_LIMIT")));
				psIns.setString(8, (String)StringUtil.formatNull(rs.getString("FLAG1")));
				psIns.setString(9, (String)StringUtil.formatNull(rs.getString("UPPER_LIMIT")));
				psIns.setString(10,(String)StringUtil.formatNull(rs.getString("FLAG2")));
				psIns.setString(11,(String)StringUtil.formatNull(rs.getString("ITEM_TYPE")));
				if(((String)StringUtil.formatNull(rs.getString("ITEM"))).contains("AEBRetentionBIN")){
					aebretentioncount++;
					continue;  //2018/9/20 phoebechen
				}
				psIns.setString(12,(String)StringUtil.formatNull(rs.getString("ITEM")));
				psIns.setString(13,(String)StringUtil.formatNull(rs.getString("ITEM_MODE2")));
				psIns.setString(14,(String)StringUtil.formatNull(rs.getString("ITEM_BINS2")));
				psIns.setString(15,(String)StringUtil.formatNull(rs.getString("ACTION")));
				psIns.setString(16,((String)StringUtil.formatNull(rs.getString("CHANGE_IPN"))).replaceAll(fromProduct, toProduct));
				psIns.setString(17,(String)StringUtil.formatNull(rs.getString("ROUTE_NAME")));
				psIns.setString(18,(String)StringUtil.formatNull(rs.getString("START_STEP")));
				psIns.setString(19,(String)StringUtil.formatNull(rs.getString("REMARK")));
				psIns.setString(20,(String)StringUtil.formatNull(rs.getString("DG_ACTION")));
				psIns.setString(21,(String)StringUtil.formatNull(rs.getString("ITEM_SEQ")));
                psIns.setString(22,(String)StringUtil.formatNull(rs.getString("BY_LOT_DG")));
                psIns.setString(23,(String)StringUtil.formatNull(rs.getString("DGRADE_SPECIAL_IPN")));
                psIns.setString(24,(String)StringUtil.formatNull(rs.getString("SNOVA_ID")));
                psIns.setString(25,(String)StringUtil.formatNull(rs.getString("VERSION")));
                psIns.setString(26,(String)StringUtil.formatNull(rs.getString("DGRADEPRODCODE")));
                psIns.setString(27,(String)StringUtil.formatNull(rs.getString("GROUPITEMS_NO")));                
				psIns.executeUpdate();
				cnt++;
			}
			rs.close();
			ps.close();
			psIns.close();
			rs = null; ps = null; psIns = null;

			
			
			if (cnt == 0) { // 查無資料可 copy
				conn.rollback();
				return "No Data !";
			}
			
			YieldDefService.copyGroupItemsTo(conn, sid, proc_type, fromProduct, fromMode, toProduct, toMode, brand);
			
			conn.commit();
			conn.setAutoCommit(true);
			OiMaintainService.unSubmit(sid, "TF_YIELD_"+proc_type);
			if(aebretentioncount>0){
				return "HASAEBRETENTION";  //add by ken 201802918
			}else {
				return "";
			}
			
		} catch (Exception ex) {
			TDSLogger.println(ex);
			DBConnection.rollback(conn);
			return "Copy failure !!\n" + ex.getMessage();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
	}

	public static void copyGroupItemsTo(Connection conn, String sid,
			String proc_type, 
			String fromProduct,
			String fromMode,
			String toProduct,
			String toMode,
			String brand) {
		TDSLogger.println("copyGroupItemsTo() - sid: " + sid + ", proc_type: " + proc_type + 
				", fromProduct: " + fromProduct + ", fromMode: " + fromMode + 
				", toProduct: " + toProduct + ", toMode: " + toMode + ", brand: " + brand);

		if(!proc_type.equals("WS") || (brand == null || brand.equals(""))){
			return;
		}
		
		try {
			TfYieldGroupItemsTxDao.delete(conn, sid, toProduct, toMode);
			if (fromProduct.substring(0,4).equals(toProduct.subSequence(0,4))){
				if(fromMode == null || fromMode.equals("") || fromMode.equals("%"))
					TfYieldGroupItemsTxDao.copyTxGroupItems(conn, sid, fromProduct, toProduct);
				else
					TfYieldGroupItemsTxDao.copyTxGroupItems(conn, sid, fromProduct, fromMode, toProduct, toMode);
			}else{
				if(fromMode == null || fromMode.equals("") || fromMode.equals("%"))
					TfYieldGroupItemsTxDao.copyRelesedGroupItems(conn, sid, fromProduct, toProduct, brand);
				else
					TfYieldGroupItemsTxDao.copyRelesedGroupItems(conn, sid, fromProduct, fromMode, toProduct, toMode, brand);
			}
		} catch (Exception e) {
			TDSLogger.println(e);
			throw new RuntimeException("copy Group Items error: " + e.getMessage());
		}
		
	}
	
	//copy the data from tf_yield to tf_yield_tx
	public static boolean YieldToYieldTX(String sid,
			                             String product_body,
			                             String brand,
			                             String dest_version,
			                             String proc_type) {

		StringBuffer Sql = new StringBuffer();
		Connection conn = null;
		String pre_vision=String.valueOf(Integer.parseInt(dest_version)-1);
		try {
			HashMap whereStem = new HashMap();
			conn = DBConnection.getConnection();
			whereStem.put("b.product_body", product_body);
			whereStem.put("b.brand", brand);
			whereStem.put("b.version", pre_vision);
			Sql.append("insert into tf_yield_definition_tx\n"+
					"(YID,SID,SEQ,FACILITY,PRODUCT_CODE,BRAND,TEST_MODE,LOWER_LIMIT,\n" +
					"FLAG1,UPPER_LIMIT,FLAG2,ITEM_TYPE,ITEM,ITEM_MODE2,ITEM_BINS2,\n" +
                 	"ACTION,DG_ACTION,CHANGE_IPN,ROUTE_NAME,START_STEP,BY_LOT_DG,DGRADE_SPECIAL_IPN,REMARK,ITEM_SEQ,SNOVA_ID,VERSION,DGRADEPRODCODE,GROUPITEMS_NO) \n");
			Sql.append("select a.YID,"+sid+",a.SEQ,a.FACILITY,a.PRODUCT_CODE,a.BRAND,a.TEST_MODE,a.LOWER_LIMIT,\n" +
					"a.FLAG1,a.UPPER_LIMIT,a.FLAG2,a.ITEM_TYPE,ITEM,a.ITEM_MODE2,a.ITEM_BINS2,\n" +
      			 	"decode(a.ACTION,'MRB','OOC',a.ACTION) action ,a.DG_ACTION, a.CHANGE_IPN,a.ROUTE_NAME,a.START_STEP,a.BY_LOT_DG,a.DGRADE_SPECIAL_IPN,a.REMARK,a.ITEM_SEQ,a.SNOVA_ID,a.VERSION,a.DGRADEPRODCODE,a.GROUPITEMS_NO\n");
			Sql.append("from tf_yield_definition a, tf_information b\n");
			Sql.append(SQLStem.getWhereStmt(whereStem));
			Sql.append("\nand a.sid = b.sid");
			Sql.append("\nand a.facility = "+(proc_type.equals("WS")?"0":"1"));
			PreparedStatement ps = conn.prepareStatement(Sql.toString());
			ps.executeUpdate();
			ps.close();
			ps = null;
		} catch (Exception ex) {
			ex.printStackTrace();
			DBConnection.rollback(conn);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return false;
	}
	
	//copy the data from tf_yield_groupitems to tf_yield_groupitems_tx
	public static void copyYieldGroupItemsToTX(Connection conn, String sid,
			                             String product_body,
			                             String brand,
			                             String dest_version,
			                             String proc_type) throws Exception {

		String pre_vision=String.valueOf(Integer.parseInt(dest_version)-1);
        TfYieldGroupItemsTxDao.copyRelesedGroupItems(conn, sid, product_body, brand, Integer.parseInt(pre_vision));
	}

	//get the data of the given product body and brand from tf_yield_tx
	// 因 Action priority 以 tf_description.id 記錄，為方便 JSP，另外取出備用，加 10 是為了好排序
	public static YieldDefBean[] RetrieveYield(String sid,
                                               Connection conn,
                                               String table,
                                               String proc_type,
                                               String product_type,
                                               boolean have_snova_id) {

		StringBuffer SelSQL = new StringBuffer();
		try {
			ArrayList<YieldDefBean> tmp = new ArrayList<YieldDefBean>();
			ArrayList<YieldDefBean> tmp2 = new ArrayList<YieldDefBean>();
			HashMap<String, String> whereStem = new HashMap<String, String>();
			
	        /*
	        //String[] actionList_hcs = OiMaintainService.getHCSACTION(conn);
	        String hcs = "";
	        for (int i = 0; i < actionList_hcs.length; i++) {
	            hcs += actionList_hcs[i];
	        }
	        */
	        String[] actionList_acs = OiMaintainService.getACSACTION(conn);
	        String acs = "";
	        for (int i = 0; i < actionList_acs.length; i++) {
	            acs += actionList_acs[i];
	        }
	        
			whereStem.put("a.sid", sid);
			whereStem.put("facility", (proc_type.equals("WS")?"0":"1"));
			SelSQL.append("select * from ( ");
			SelSQL.append("SELECT a.sid,a.yid,a.seq,a.facility,a.product_code,a.brand,a.test_mode,\n" +
			  		"lower_limit,flag1,upper_limit,flag2,a.item,\n" +
			  		"CASE\n" +
			  		"          WHEN a.item LIKE 'AEBRetentionBIN%' THEN\n" + 
			  		"            '1'\n" + 
			  		"          WHEN a.item ='Datalog abnormal' THEN\n" + 
			  		"            '1'\n" + 
			  		"          ELSE\n" + 
			  		"            '0'\n" + 
			  		"       END item_disable," +
		  			"a.item_type,a.item_mode2,a.item_bins2,decode(a.action,'Dgrade-MXA','Dgrade-MXG',a.action) action,a.dg_action,a.change_ipn,\n" +
		  			"a.route_name,a.start_step,a.by_lot_dg,a.dgrade_special_ipn,a.dgradeprodcode,a.snova_id,a.version,a.remark,nvl(a.item_seq,0)item_seq,\n" +
					"b.id+10 actionseq, nvl(a.groupitems_no,0) groupitems_no FROM tf_yield_definition"+table+" a, tf_description b\n");
			SelSQL.append(SQLStem.getWhereStmt(whereStem));
			SelSQL.append("\nand a.action=b.description");
			if(proc_type.equals("WS") && product_type.equals("NVM")){
				SelSQL.append("\nand b.delete_flag is null");
				SelSQL.append("\nand b.tag=441\n");
				SelSQL.append("union SELECT a.sid,a.yid,a.seq,a.facility,a.product_code,a.brand,a.test_mode,\n" +
				  		"lower_limit,flag1,upper_limit,flag2,a.item,\n" +
				  		"CASE\n" +
				  		"          WHEN a.item LIKE 'AEBRetentionBIN%' THEN\n" + 
				  		"            '1'\n" + 
				  		"          WHEN a.item ='Datalog abnormal' THEN\n" + 
				  		"            '1'\n" + 
				  		"          ELSE\n" + 
				  		"            '0'\n" + 
				  		"       END item_disable," +
			  			"a.item_type,a.item_mode2,a.item_bins2,decode(a.action,'Dgrade-MXA','Dgrade-MXG',a.action) action,a.dg_action,a.change_ipn,\n" +
			  			"a.route_name,a.start_step,a.by_lot_dg,a.dgrade_special_ipn,a.dgradeprodcode,a.snova_id,a.version,a.remark,nvl(a.item_seq,0)item_seq,\n" +
						"b.ori_priority+10 actionseq, nvl(a.groupitems_no,0) groupitems_no FROM tf_yield_definition"+table+" a, tf_prod_waferlevel_tx b\n");
				SelSQL.append(SQLStem.getWhereStmt(whereStem));
				SelSQL.append("\nand a.sid=b.sid\n");
				SelSQL.append("\nand a.action='Dgrade-' || b.wafer_level)\n");
				SelSQL.append("order by product_code,brand,test_mode,cast(nvl(groupitems_no,0) as int),item,item_seq,cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)");
                //20130313SelSQL.append("order by a.product_code,a.brand,a.test_mode,a.item,a.item_seq,cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)");
			}else{
			    SelSQL.append("\nand b.tag=44\n");
			    //SelSQL.append("order by seq)");  //mark by ken for 版面正確才可以使用
			    SelSQL.append("order by product_code,brand,test_mode,cast(nvl(groupitems_no,0) as int) desc,item,item_seq,cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int))");
			}
						
			TDSLogger.println("SelSQL="+SelSQL);
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				YieldDefBean ydb = new YieldDefBean();
				ydb.setSid(Integer.parseInt(sid));
				ydb.setYid(Integer.parseInt(rs.getString("yid")));
				ydb.setSeq(Integer.parseInt(rs.getString("seq")));
				ydb.setFacility(rs.getString("facility"));
				ydb.setProduct_code(rs.getString("product_code"));
				ydb.setBrands(rs.getString("brand"));
				ydb.setTest_mode(rs.getString("test_mode"));
				ydb.setLower_limit(rs.getString("lower_limit"));
				ydb.setFlag1(rs.getString("flag1"));
				ydb.setUpper_limit(rs.getString("upper_limit"));
				ydb.setFlag2(rs.getString("flag2"));
				ydb.setItem_type(Integer.parseInt(rs.getString("item_type")));
				if(rs.getString("item").startsWith("AEBRetentionBIN")){
					if(have_snova_id)
						ydb.setItem(rs.getString("item")+"-"+rs.getString("snova_id")+"-V"+rs.getString("version"));
					else
						ydb.setItem(rs.getString("item")+"-V"+rs.getString("version"));
				}else{	
					ydb.setItem(rs.getString("item"));
				}	
				ydb.setItem_disable(rs.getString("item_disable"));
				ydb.setItem_mode2(rs.getString("item_mode2"));
				ydb.setItem_bins2(rs.getString("item_bins2"));
				ydb.setAction(rs.getString("action"));
				ydb.setDg_action(rs.getString("dg_action"));
				ydb.setActionseq(rs.getString("actionseq"));
				ydb.setChange_ipn(rs.getString("change_ipn"));
				if(rs.getString("change_ipn") != null && (rs.getString("change_ipn").length()==12 || rs.getString("change_ipn").length()==13)){
				    ydb.setChange_ipn_start_10(rs.getString("change_ipn").substring(0,10));
				    ydb.setChange_ipn_last_2(rs.getString("change_ipn").substring(10));
				}else{
					ydb.setChange_ipn_start_10("");
					ydb.setChange_ipn_last_2("");	
				}
				//System.out.println("Change_ipn_start_10="+ydb.getChange_ipn_start_10());
				//System.out.println("setChange_ipn_last_2="+ydb.getChange_ipn_last_2());
				ydb.setRoute_name(rs.getString("route_name"));
				ydb.setStart_step(rs.getString("start_step"));
				ydb.setBy_lot_dg(rs.getString("by_lot_dg"));
				ydb.setDgrade_special_ipn(rs.getString("dgrade_special_ipn"));
				ydb.setDgradeprodcode(rs.getString("dgradeprodcode"));
				ydb.setRemark(rs.getString("remark"));
				ydb.setItem_seq(Integer.parseInt(rs.getString("item_seq")));
				ydb.setProduct_type(product_type);
				ydb.setAcs(acs);
				ydb.setGroupitems_no(rs.getString("groupitems_no"));
				
				if (ydb.getAction()!= null && acs.indexOf(ydb.getAction()) != -1) {
				    tmp2.add(ydb);
				} else {
				tmp.add(ydb);
			}
			}
            for(int i = 0; i < tmp2.size(); i++) {
                tmp.add((YieldDefBean)tmp2.get(i));
            }
			rs.close();
			ps.clearBatch();
			rs = null;
			ps = null;
			return (YieldDefBean[]) tmp.toArray(new YieldDefBean[0]);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return null;
	}
	
	
    public static YieldDefBean[] RetrieveYield(String sid, Connection conn, String table, String proc_type, String product_type, String type, boolean have_snova_id) {
        YieldDefBean[] y = null;
        y = RetrieveYield(sid, conn, table, proc_type, product_type, have_snova_id);
        return GetYeildDef(conn, y,type);
    }
    
    /*for xtrarom, mrom hold, action criteria setting */
    public static YieldDefBean[] GetYeildDef(Connection conn, YieldDefBean[] y , String type) {  //type is hcs, acs
        String[] actionList_hcs = OiMaintainService.getHCSACTION(conn);
        String[] actionList_acs = OiMaintainService.getACSACTION(conn);
        ArrayList y_hcs = new ArrayList();
        ArrayList y_acs = new ArrayList();
        
        String hcs = "", acs = "";
        for (int i = 0; i < actionList_hcs.length; i++) {
            hcs += actionList_hcs[i];
        }
        for (int i = 0; i < actionList_acs.length; i++) {
            acs += actionList_acs[i];
        }
        if (y != null && y.length != 0) {
            for (int i = 0; i < y.length; i++) {
                if (acs.indexOf(y[i].getAction()) != -1) { // is action criteria setting
                    y_acs.add(y[i]);
                } else {
                    y_hcs.add(y[i]);
                }
            }
        }
        
        if (type.equals("hcs")) {
            return (YieldDefBean[]) y_hcs.toArray(new YieldDefBean[0]);
        } else if (type.equals("acs")) {
            return (YieldDefBean[]) y_acs.toArray(new YieldDefBean[0]);
        } else {
            return null;
        }
        
    }
	
	//for pdf 排序用
	public static YieldDefBean[] RetrieveYieldNVMWSHoldCriteriaPDF(String sid,
                                               Connection conn,
                                               String table,
                                               String proc_type,
                                               String product_type,
                                               String hold_dgrade_flag) {

		StringBuffer SelSQL = new StringBuffer();
		try {
			ArrayList tmp = new ArrayList();
			HashMap whereStem = new HashMap();
			whereStem.put("sid", sid);
			whereStem.put("facility", (proc_type.equals("WS")?"0":"1"));
			SelSQL.append("select aa.* from(\n");
			if(hold_dgrade_flag.equals("Hold")){
				SelSQL.append("SELECT 1 as seqnum,a.sid,a.yid,a.seq,a.facility,a.product_code,a.brand,a.test_mode,\n" +
				  		"lower_limit,flag1,upper_limit,flag2,a.item,\n" +
			  			"a.item_type,a.item_mode2,a.item_bins2,a.action,a.dg_action,a.change_ipn,\n" +
			  			"a.route_name,a.start_step,a.remark,nvl(a.item_seq,0)item_seq,\n" +
						"b.id+10 actionseq FROM tf_yield_definition"+table+" a, tf_description b\n");
				SelSQL.append(SQLStem.getWhereStmt(whereStem));
				SelSQL.append("\nand a.action=b.description");
				if(proc_type.equals("WS") && product_type.equals("NVM")){
					SelSQL.append("\nand b.tag=441\n");
				}else{
				    SelSQL.append("\nand b.tag=44\n");
				}
				SelSQL.append("and (a.action not like 'Dgrade%' and a.action !='Follow Hold Criteria' and a.action not like 'Change IPN%')\n");
			}else{
				//SelSQL.append("union\n");
				SelSQL.append("SELECT 2 as seqnum,a.sid,a.yid,a.seq,a.facility,a.product_code,a.brand,a.test_mode,\n" +
				  		"lower_limit,flag1,upper_limit,flag2,a.item,\n" +
			  			"a.item_type,a.item_mode2,a.item_bins2,a.action,a.by_lot_dg,a.dgradeprodcode,a.dg_action,a.change_ipn,\n" +
			  			"a.route_name,a.start_step,(a.remark||decode(nvl(a.dgradeprodcode,' '),' ','',';Dgrade different ProdCode:'||a.dgradeprodcode)) remark,nvl(a.item_seq,0)item_seq,\n" +
						"b.id+10 actionseq FROM tf_yield_definition"+table+" a, tf_description b\n");
				SelSQL.append(SQLStem.getWhereStmt(whereStem));
				SelSQL.append("\nand a.action=b.description");
				if(proc_type.equals("WS") && product_type.equals("NVM")){
					SelSQL.append("\nand b.tag=441\n");
					SelSQL.append("and (a.action like 'Dgrade%' or a.action ='Follow Hold Criteria' or a.action like 'Change IPN%')");
				}else if(proc_type.equals("FT") && product_type.equals("NVM")){
					SelSQL.append("\nand b.tag=445\n");
					SelSQL.append("and (a.action like 'Dgrade%' or a.action ='Follow Hold Criteria' or a.action ='SPC Dgrade')");
				}else{
				    SelSQL.append("\nand b.tag=44\n");
				    SelSQL.append("and (a.action like 'Dgrade%' or a.action ='Follow Hold Criteria')");
				}
				//SelSQL.append("and (a.action like 'Dgrade%' or a.action ='Follow Hold Criteria')");
			}
			SelSQL.append(")aa\n");
			SelSQL.append("order by seqnum,product_code,test_mode,item,item_seq,cast(nvl(lower_limit,0) as int),cast(nvl(upper_limit,0) as int)");
			TDSLogger.println("SelSQL="+SelSQL);
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				YieldDefBean ydb = new YieldDefBean();
				ydb.setSid(Integer.parseInt(sid));
				ydb.setYid(Integer.parseInt(rs.getString("yid")));
				ydb.setSeq(Integer.parseInt(rs.getString("seq")));
				ydb.setFacility(rs.getString("facility"));
				ydb.setProduct_code(rs.getString("product_code"));
				ydb.setBrands(rs.getString("brand"));
				ydb.setTest_mode(rs.getString("test_mode"));
				ydb.setLower_limit(rs.getString("lower_limit"));
				ydb.setFlag1(rs.getString("flag1"));
				ydb.setUpper_limit(rs.getString("upper_limit"));
				ydb.setFlag2(rs.getString("flag2"));
				ydb.setItem_type(Integer.parseInt(rs.getString("item_type")));
				ydb.setItem(rs.getString("item"));
				ydb.setItem_mode2(rs.getString("item_mode2"));
				ydb.setItem_bins2(rs.getString("item_bins2"));
				ydb.setAction(rs.getString("action"));
				ydb.setDg_action(rs.getString("dg_action"));
				ydb.setActionseq(rs.getString("actionseq"));
				ydb.setChange_ipn(rs.getString("change_ipn"));
				if(rs.getString("change_ipn") != null && (rs.getString("change_ipn").length()==12 || rs.getString("change_ipn").length()==13)){
				    ydb.setChange_ipn_start_10(rs.getString("change_ipn").substring(0,10));
				    ydb.setChange_ipn_last_2(rs.getString("change_ipn").substring(10));
				}else{
					ydb.setChange_ipn_start_10("");
					ydb.setChange_ipn_last_2("");	
				}
				//System.out.println("Change_ipn_start_10="+ydb.getChange_ipn_start_10());
				//System.out.println("setChange_ipn_last_2="+ydb.getChange_ipn_last_2());
				ydb.setRoute_name(rs.getString("route_name"));
				ydb.setStart_step(rs.getString("start_step"));
				ydb.setRemark(rs.getString("remark"));
				ydb.setItem_seq(Integer.parseInt(rs.getString("item_seq")));
				if(!hold_dgrade_flag.equals("Hold")){
				    ydb.setBy_lot_dg(rs.getString("by_lot_dg"));
				    ydb.setDgradeprodcode(rs.getString("dgradeprodcode"));
				}
				tmp.add(ydb);
			}
			rs.close();
			ps.clearBatch();
			rs = null;
			ps = null;
			return (YieldDefBean[]) tmp.toArray(new YieldDefBean[0]);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		return null;
	}
	
	/* get available product code list from "Test Parameter" for jsp */
	public static String[] getProductList(String product_body, String proc_type) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		
		try {
			ArrayList tmp = new ArrayList();
			conn = DBConnection.getConnection();
			SelSQL.append("SELECT distinct product_code\n" +
						  "FROM ba_product_info\n" +
						  "where product_code like '"+product_body+"%'\n" +
						  "and test_mode like '"+(proc_type.equals("WS")?"S%":"F%") + "'\n" +
						  "order by product_code");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String product_code = new String(rs.getString("product_code"));
				tmp.add(product_code);
			}
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return (String[]) tmp.toArray(new String[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	
	/* get available test mode list from PEIS BA_PRODUCT_INFO for jsp
	 * if product_body is "", return all product's test mode
	 * type = 'ALL', for all test modes, '' not include Sm/n, AVI, Ship
	 * 
	 */
	public static String[] getTestModeList(String product_body, String proc_type, String type) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		
		try {
			ArrayList tmp = new ArrayList();
			conn = DBConnection.getConnection();
			SelSQL.append("SELECT distinct test_mode\n" +
					  "FROM ba_product_info a\n" +
					  "where \n" +
					  (product_body.equals("")?"":"product_code like '"+product_body+"%'\nand ") +
					  "test_mode like '"+(proc_type.equals("WS")?"S%":"F%") + "'");
			if (!type.equals("ALL"))
				SelSQL.append("\nand instr(a.test_mode,'/') = 0");
				
			SelSQL.append("\norder by test_mode");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String test_mode = new String(rs.getString("test_mode"));
				tmp.add(test_mode);
			}
			if (type.equals("ALL") && proc_type.equals("WS")) {
				String[] ItemList = OiMaintainService.getDescriptionByDesc("31");
				if (ItemList != null) {
					for (int i = 0; i < ItemList.length; i++)
					{
						tmp.add(ItemList[i]);
					}
				}
				//tmp.add("AVI");
				//tmp.add("AVI2");
				//tmp.add("AVI3");
				//tmp.add("Ship");
			}
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return (String[]) tmp.toArray(new String[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	public static String[] getStepList(String sid) {
	       
	        Connection conn = null;
	        String[] s= null;
	        try {
	            conn = DBConnection.getConnection();
	            ProTestRouteBean[] ptrb =  OiMaintainService.GetTestStepDef(conn, sid);
	            if(ptrb != null && ptrb.length > 0) {
	                s = new String[ptrb.length];
	                for(int i = 0; i < ptrb.length; i ++) {
	                    s[i] = ptrb[i].getStepname()+"--"+ptrb[i].getStep_def();
	                }
	            }
	            return s;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        } finally {
	            DBConnection.close(conn);
	            conn = null;
	        }
	        return null;
	    }
	
	
	public static String getSpatDpatList(String prod_body) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		try {
			String rnt = "";
			conn = DBConnection.getConnection();
			SelSQL.append("SELECT DISTINCT PRODUCT_CODE||'-'||TEST_MODE AS TAG FROM TIM.GPRS_CP_DPAT_SPEC WHERE PRODUCT_CODE LIKE ? AND METHOD IN ('SPAT','DPAT') ");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ps.setString(1, prod_body + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String tag = new String(rs.getString("TAG"));
				rnt = rnt + tag + ",";
			}
			return rnt;
		} catch (Exception ex) {
			TDSLogger.println(ex);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	
	public static String getRegionList(String prod_body) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		try {
			String rnt = "";
			conn = DBConnection.getConnection();
			SelSQL.append("SELECT DISTINCT PRODUCT_CODE AS TAG FROM TDS.ba_sp_region WHERE PRODUCT_CODE LIKE ? AND DELETE_FLAG IS NULL AND VERSION IS NOT NULL");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ps.setString(1, prod_body + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String tag = new String(rs.getString("TAG"));
				rnt = rnt + tag + ",";
			}
			rs.close();
			ps.close();
			return rnt;
		} catch (Exception ex) {
			TDSLogger.println(ex);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	
	public static String getNnrList(String prod_body) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		try {
			String rnt = "";
			conn = DBConnection.getConnection();
			SelSQL.append("SELECT DISTINCT PRODUCT_CODE||'-'||TEST_MODE AS TAG FROM TIM.GPRS_CP_DPAT_SPEC WHERE PRODUCT_CODE LIKE ? AND METHOD = 'NNR' ");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ps.setString(1, prod_body + "%");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String tag = new String(rs.getString("TAG"));
				rnt = rnt + tag + ",";
			}
			return rnt;
		} catch (Exception ex) {
			TDSLogger.println(ex);
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	
	/* get available test mode list from PEIS BA_PRODUCT_INFO for jsp
	 * if product_body is "", return all product's test mode
	 * type = 'ALL', for all test modes, '' not include Sm/n, AVI, Ship
	 * 
	 */
	public static String[] getProdWaferLevelList(String product_body, String brand, String version) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		
		try {
			ArrayList tmp = new ArrayList();
			conn = DBConnection.getConnection();
			SelSQL.append("SELECT distinct wafer_level, revise_priority \n" +
					  "FROM tf_prod_waferlevel_tx \n" +
					  "where checked_flag = 'Y' \nand " +
					  "product_body = '"+product_body+"'\nand " +
					  "brand like '"+brand + "'\nand " +
					  "version = "+version+" ");
				
			SelSQL.append("\norder by revise_priority");
			
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String wafer_level = new String(rs.getString("wafer_level"));
				tmp.add(wafer_level);
			}
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return (String[]) tmp.toArray(new String[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	
	/* get test mode list from TF_YIELD_DEFINITION
	 * but if fromProduct == toProduct, get from TF_YIELD_DEFINITION_TX
	 * 
	 */
	public static String[] getTestModeListFromYield(String fromProduct, String currentProduct, String proc_type) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		
		try {
			ArrayList tmp = new ArrayList();
			conn = DBConnection.getConnection();
			if (fromProduct.subSequence(0,4).equals(currentProduct.subSequence(0,4)))
				SelSQL.append("SELECT distinct test_mode, decode(test_mode,'AVI',1,'Ship',2,0) seq\n" +
					  "FROM tf_yield_definition_tx\n" +
					  "where " +
					  "product_code = '"+fromProduct+"'\n" +
					  "and facility = "+(proc_type.equals("WS")?"0":"1"));
			else
				SelSQL.append("SELECT distinct test_mode, decode(test_mode,'AVI',1,'Ship',2,0) seq\n" +
						  "FROM tf_yield_definition a, tf_current_version_vw b\n " +
						  "where a.sid = b.sid\n" +
						  "and product_code = '"+fromProduct+"'\n" +
						  "and facility = "+(proc_type.equals("WS")?"0":"1"));
			SelSQL.append("\norder by seq, test_mode");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String test_mode = new String(rs.getString("test_mode"));
				tmp.add(test_mode);
			}
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return (String[]) tmp.toArray(new String[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	
	/* get available route mode list from product vs route table for jsp */
	public static String[] getRouteNameList(String sid, String proc_type) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		
		try {
			ArrayList tmp = new ArrayList();
			conn = DBConnection.getConnection();
			if (proc_type.equals("WS"))
				SelSQL.append("SELECT distinct a.route_name\n" +
						"FROM tf_product_route_tx a, tf_route_master b\nwhere sid="+sid+
						"\nand type = 'W'" +
						"\nand a.route_name = b.route_name" +
						"\norder by route_name");
			else if (proc_type.equals("FT"))
				SelSQL.append("SELECT distinct a.route_name\n" +
						"FROM tf_product_route_tx a, tf_route_master b\nwhere sid="+sid+
						"\nand type = 'P'" +
						"\nand a.route_name = b.route_name" +
						"\norder by route_name");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				String route_name = new String(rs.getString("route_name"));
				tmp.add(route_name);
			}
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return (String[]) tmp.toArray(new String[0]);
	    	} catch (Exception ex) {
	    		ex.printStackTrace();
	    	} finally {
	    		DBConnection.close(conn);
	    		conn = null;
	    	}
	    	return null;
	}

	/* get available route mode list from product vs route table for jsp */
	public static String[][] getRouteSetpList(String sid, String proc_type) {
		StringBuffer SelSQL = new StringBuffer();
		String[] routeList = YieldDefService.getRouteNameList(sid, proc_type);
		if (routeList == null)
			return null;

		String[][] result = new String[routeList.length][];
		String routeName = "";
		int routeNumber = 0;
		
		Connection conn = null;
		
		try {
			ArrayList tmp = null;
			conn = DBConnection.getConnection();
			if (proc_type.equals("WS"))
				SelSQL.append("SELECT distinct a.route_name, a.step_name\n" +
						"FROM tf_product_route_tx a, tf_route_master b\nwhere sid="+sid+
						"\nand type = 'W'" +
						"\nand a.route_name = b.route_name" +
						"\norder by route_name, step_name");
			else if (proc_type.equals("FT"))
				SelSQL.append("SELECT distinct a.route_name, a.step_name\n" +
						"FROM tf_product_route_tx a, tf_route_master b\nwhere sid="+sid+
						"\nand type = 'P'" +
						"\nand a.route_name = b.route_name" +
						"\norder by route_name, step_name");
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				if (routeName.equals(rs.getString("route_name")))
					tmp.add(new String(rs.getString("step_name")));
				else {
					if (routeNumber != 0)
						result[routeNumber-1] = (String[]) tmp.toArray(new String[0]);
					routeName = rs.getString("route_name");
					tmp = new ArrayList();
					tmp.add(new String(rs.getString("step_name")));
					routeNumber++;
				}
			}
			if (routeNumber >0)
				result[routeNumber-1] = (String[]) tmp.toArray(new String[0]);
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return result;
	    	} catch (Exception ex) {
	    		ex.printStackTrace();
	    	} finally {
	    		DBConnection.close(conn);
	    		conn = null;
	    	}
	    	return null;
	}

	// Update / Insert Yield data
	public static boolean Update(String sid, 
								 String proc_type, 
								 String yid[], 	// empty String if insert
								 String product_code[], 
								 String brands[], 
								 String test_mode[],
								 String lower_limit[], 
								 String flag1[], 
								 String upper_limit[], 
								 String flag2[],
								 String item[], 
								 String item_type[], 
								 String item_mode2[], 
								 String item_bins2[],
								 String action[], 
								 HashMap change_ipnHash, 
								 HashMap route_nameHash, 
								 HashMap start_stepHash, 
								 String remark[]) {

		StringBuffer sqlUpdate = new StringBuffer();
	    StringBuffer sqlInsert = new StringBuffer();
	    Connection conn = null;
	    boolean result = true;
	    int i;
	    try {
	    	conn = DBConnection.getConnection();
	    	conn.setAutoCommit(false);
	    	sqlUpdate.append("Update tf_yield_definition_tx\n"+
	    			"SET SID=?,FACILITY=?,SEQ=?,PRODUCT_CODE=?,BRAND=?,TEST_MODE=?,\n" +
	    			"LOWER_LIMIT=?,FLAG1=?,UPPER_LIMIT=?,FLAG2=?,\n" +
	    			"ITEM_TYPE=?,ITEM=?,ITEM_MODE2=?,ITEM_BINS2=?,\n" +
	    			"ACTION=?,CHANGE_IPN=?,ROUTE_NAME=?,START_STEP=?,\n" +
	    			"REMARK=?\n" +
	      		  	"where sid=? and yid=? and facility=?");
	    	sqlInsert.append("Insert into tf_yield_definition_tx\n"+
	    			"(SID,YID,FACILITY,SEQ,PRODUCT_CODE,BRAND,TEST_MODE,\n" +
	    			"LOWER_LIMIT,FLAG1,UPPER_LIMIT,FLAG2,\n" +
	    			"ITEM_TYPE,ITEM,ITEM_MODE2,ITEM_BINS2,\n" +
	    			"ACTION,CHANGE_IPN,ROUTE_NAME,START_STEP,\n" +
	    			"REMARK) VALUES (\n" +
	    		  	"?,TF_YIELD_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    	PreparedStatement ps1 = null;
	    	PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate.toString());
	    	PreparedStatement psInsert = conn.prepareStatement(sqlInsert.toString());
	    	/* update all the columns except test_time, time_unit,remark*/
	    	for (i = 0; i < yid.length; i++) {
	    		ps1 = psUpdate;
	    	    if (yid[i].equals("")) 
	    	    	ps1 = psInsert;

	    	    String change_ipn = "";
	    		String route_name = "";
	    		String start_step = "";
	    		String k = ""+i;
	    		if (change_ipnHash.containsKey(k))
	    			change_ipn = (String)change_ipnHash.get(k);
	    		if (route_nameHash.containsKey(k))
	    			route_name = (String)route_nameHash.get(k);
	    		if (start_stepHash.containsKey(k))
	    			start_step = (String)start_stepHash.get(k);
	    		
	    		ps1.setInt(1, Integer.parseInt(sid));
	    		ps1.setInt(2, (proc_type.equals("WS")?0:1));
	    		ps1.setInt(3, i);
	    		ps1.setString(4, product_code[i]);
	    		ps1.setString(5, brands[i]);
	    		ps1.setString(6, test_mode[i]);
	    		ps1.setString(7, lower_limit[i]);
	    		ps1.setString(8, flag1[i]);
	    		ps1.setString(9, upper_limit[i]);
	    		ps1.setString(10, flag2[i]);
	    		ps1.setInt(11, Integer.parseInt(item_type[i]));
	    		ps1.setString(12, item[i]);
	    		ps1.setString(13, item_mode2[i]);
	    		ps1.setString(14, item_bins2[i]);
	    		ps1.setString(15, action[i].substring(2,action[i].length()));
	    		if (change_ipn == null || change_ipn.equals("")) {
	    			ps1.setString(16,"");
	    		} else {
	    			ps1.setString(16, change_ipn);
	    		}
	    		if (route_name == null || route_name.equals("")) {
	    			ps1.setString(17,"");
	    		} else {
	    			ps1.setString(17, route_name);
	    		}
	    		if (start_step == null || start_step.equals("")) {
	    			ps1.setString(18,"");
	    		} else {
	    			ps1.setString(18, start_step);
	    		}
	    		ps1.setString(19, StringUtil.Utf8ToBig5((remark[i])));
	    			
	    		if (!yid[i].equals("")) {
	    			ps1.setInt(20, Integer.parseInt(sid));
	    			ps1.setInt(21, (Integer.parseInt(yid[i])));
	    			ps1.setInt(22, (proc_type.equals("WS")?0:1));
	    		}
	    		ps1.executeUpdate();
	    	}
	    	psUpdate.close();
	    	psInsert.close();
	    	psUpdate = null;
	    	psInsert = null;
	    	conn.commit();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	DBConnection.rollback(conn);
	    	result = false;
	    } finally {
	    	DBConnection.close(conn);
	    	conn = null;
	    }
	    return result;
	}

	// remove yield record
	public static boolean Delete(String sid, 
			 	                 String proc_type, 
			 	                 String yid[]) {

			StringBuffer sql = new StringBuffer();
			boolean result = true;

			Connection conn = null;
			try {
				conn = DBConnection.getConnection();
				conn.setAutoCommit(false);
				sql.append("delete from tf_yield_definition_tx\n"+
						   "where sid=? and yid=? and facility=?");
				PreparedStatement ps = conn.prepareStatement(sql.toString());
				/* update all the columns except test_time, time_unit,remark*/
				for (int i = 0; i < yid.length; i++) {
					if (!yid[i].equals("") && !yid[i].equals("-1")) {
						ps.setInt(1, Integer.parseInt(sid));
						ps.setInt(2, (Integer.parseInt(yid[i])));
						ps.setInt(3, (proc_type.equals("WS")?0:1));
						ps.executeUpdate();
					}
				}
				conn.commit();
				ps.close();
				ps = null;
			} catch (Exception e) {
				DBConnection.rollback(conn);
				result = false;
			} finally {
				DBConnection.close(conn);
				conn = null;
			}
			return result;
	}
	
	public static boolean Delete(String sid, 
             String proc_type) {

		StringBuffer sql = new StringBuffer();
		boolean result = true;
	
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			sql.append("delete from tf_yield_definition_tx\n"+
				   "where sid=? and facility=?");
			PreparedStatement ps = conn.prepareStatement(sql.toString());
			/* update all the columns except test_time, time_unit,remark*/
			ps.setInt(1, Integer.parseInt(sid));
			ps.setInt(2, (proc_type.equals("WS")?0:1));
			ps.executeUpdate();
			conn.commit();
			ps.close();
			ps = null;
		} catch (Exception e) {
			e.printStackTrace();
			DBConnection.rollback(conn);
			result = false;
			} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return result;
}

	/* get bin list for YieldItem.jsp
		inList : 要取得的 bin list 在 item (inList = true) 或不在 item 中
		toGet  : 要取得的 list 是屬於 "1":第一組 Bin, "2":第二組 Bin
		block_type : in tf_desctiption.tag=41, 若為 0 表示不用選 bin，1 為選 1 組，2 為選兩組 ,
		item_type : in tf_description.tag=40,45, 6=Datalog (ba_bin_ft_qa), 其它是 Bin (ba_bin_ws / ba_bin_ft)
	*/
	public static YieldBin[] getBinList(String proc_type, 
										     String product_code, 
										     String test_mode, 
										     String item,
										     boolean inList,
										     String toGet,
										     String block_type,
										     int item_type) {
		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;

		if (product_code.equals("") || test_mode.equals(""))
			return null;
		
		String bin_list = item;
		if (block_type.compareTo(toGet) < 0)
			bin_list = "0"; // 全抓即可
		else {
			if (item_type == 6)
				bin_list = bin_list.replaceAll("Datalog", "").replaceAll(" ", ",");
			else if (item_type == 7)
				bin_list = bin_list.replaceAll("SpecialPatternBin", "").replaceAll(" ", ",");
			else if (item_type == 9)
				bin_list = bin_list.replaceAll("LinePatternBin", "").replaceAll(" ", ",");
			else if (item_type == 15){
				String[] bin_list_tmp = bin_list.replaceAll("AEBRetentionBIN", "").replaceAll(" ", ",").split(",");
				if(bin_list_tmp[0].equals("0")){
					bin_list = "\nand b.bin_no in(" + bin_list + ")";
				}else{
					bin_list = "\nand (";
					for (int i = 0; i < bin_list_tmp.length; i++) {
							String[] tmp_value = bin_list_tmp[i].split("-");
							bin_list += "(b.bin_no = " + tmp_value[0];
							bin_list += " and b.snova_id = " + tmp_value[1] + ")\n";
							bin_list += "or ";
	
					}  
					bin_list = bin_list.substring(0, bin_list.length()-3);
					bin_list += ")";
				}	
			}else
				bin_list = bin_list.replaceAll("BIN", "").replaceAll(" ", ",");
		}
		
		try {
			ArrayList tmp = new ArrayList();
			conn = DBConnection.getConnection();
			if (item_type == 6){
				SelSQL.append("SELECT distinct bin_no, short_name, 'NA' AS if_bin_no, 'NA' AS snova_id, 'NA' AS version " +
						"\nFROM ba_bin_ft_qa"+
						"\nwhere product_code='"+product_code+
						"'\nand test_mode='"+test_mode+
						"'\nand bin_no "+(inList?"":"not")+" in ("+bin_list+")");
			}else if(item_type==15){	
				if(proc_type.equals("FT")){
					if(!inList){
					    SelSQL.append("SELECT distinct a.bin_no, a.short_name, a.if_bin_no, b.snova_id," + //未生效
					    			  "'V' || GET_8049_FT_RETENTION_VERSION(b.product_body, b.test_mode, b.bin_no) version " +
					    			  "\nFROM ba_bin_ft a, ba_ft_aeb_retention b " +
					    			  "\nwhere a.product_code ='"+product_code+ "'" +
					    			  "\nand a.test_mode='"+test_mode+"'" +
					    			  "\nand b.product_body = substr(a.product_code,1,4) " +
					    			  "\nand b.test_mode= a.test_mode " +
					    			  "\nand b.bin_no=a.bin_no " +
					    			  "\nand b.version is null" +
					    			  "\nminus");
					}
					SelSQL.append("\nSELECT distinct a.bin_no, a.short_name, a.if_bin_no, b.snova_id," +
								  "\n'V' || GET_8049_FT_RETENTION_VERSION(b.product_body, b.test_mode, b.bin_no) version " +
								  "\nFROM ba_bin_ft a, ba_ft_aeb_retention b " +
								  "\nwhere a.product_code ='"+product_code+ "'" +
								  "\nand a.test_mode='"+test_mode+"'" +
								  "\nand b.product_body = substr(a.product_code,1,4) " +
								  "\nand b.test_mode= a.test_mode " +
								  "\nand b.bin_no=a.bin_no " +
								  "\nand b.version is null" +
								  bin_list+
				    			  "\nUNION");
				    if(!inList){			  
				    	SelSQL.append("\nSELECT distinct a.bin_no, a.short_name, a.if_bin_no, b.snova_id" +//已生效
					    			  "\n,'V' || to_char(b.version) || '(Released)' version " +
					    			  "\nFROM ba_bin_ft a, BA_FT_AEB_RETENTION_NEWEST b" +
					    			  "\nwhere a.product_code ='"+product_code+ "'" +
					    			  "\nand a.test_mode='"+test_mode+"'" +
					    			  "\nand b.product_body = substr(a.product_code,1,4) " + 
					    			  "\nand b.test_mode= a.test_mode " + 
					    			  "\nand b.bin_no=a.bin_no " + 
					    			  "\nminus");
				    }			  
					SelSQL.append("\nSELECT distinct a.bin_no, a.short_name, a.if_bin_no, b.snova_id," +
								  "\n'V' || to_char(b.version) || '(Released)'  version " +
								  "\nFROM ba_bin_ft a, BA_FT_AEB_RETENTION_NEWEST b " +
								  "\nwhere a.product_code ='"+product_code+ "'" +
								  "\nand a.test_mode='"+test_mode+"'" +
								  "\nand b.product_body = substr(a.product_code,1,4) " +
								  "\nand b.test_mode= a.test_mode " +
								  "\nand b.bin_no=a.bin_no " +
								  bin_list +
				    			  "\norder by version");
				} else{
				    SelSQL.append("SELECT distinct bin_no, short_name, 'NA' AS if_bin_no, 'NA' AS snova_id, 'NA' AS version");
				    SelSQL.append("\nFROM ba_bin_"+proc_type+
					"\nwhere product_code='"+product_code+
					"'\nand test_mode='"+test_mode+
					"'\nand bin_no "+(inList?"":"not")+" in ("+bin_list+")");    
				}    
			}else{
				if(proc_type.equals("FT")){
				    SelSQL.append("SELECT distinct bin_no, short_name, if_bin_no, 'NA' AS snova_id, 'NA' AS version");
				} else{
				    SelSQL.append("SELECT distinct bin_no, short_name, 'NA' AS if_bin_no, 'NA' AS snova_id, 'NA' AS version");
				}
				    SelSQL.append("\nFROM ba_bin_").append(proc_type).append("\n")
				    		.append("where product_code='").append(product_code).append("'\n")
				    		.append("and test_mode='").append(test_mode).append("'\n")
				    		.append("and bin_no "+(inList?"":"not")+" in ("+bin_list+")").append("\n")
				    		.append("order by bin_no");
				
			}
			//System.out.println("SelSQL="+SelSQL.toString());
			TDSLogger.println(SelSQL);
			
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				YieldBin yb = new YieldBin();
				yb.setBin_no(rs.getString("bin_no"));
				yb.setShort_name(rs.getString("short_name"));
				yb.setIf_bin_no(rs.getString("if_bin_no"));
				yb.setSnova_id(rs.getString("snova_id"));
				yb.setVersion(rs.getString("version"));
				tmp.add(yb);
			}
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return (YieldBin[]) tmp.toArray(new YieldBin[0]);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return null;
	}
	/* get bin list for YieldItem.jsp
	inList : 要取得的 bin list 在 item (inList = true) 或不在 item 中
	toGet  : 要取得的 list 是屬於 "1":第一組 Bin, "2":第二組 Bin
	block_type : in tf_desctiption.tag=41, 若為 0 表示不用選 bin，1 為選 1 組，2 為選兩組 ,
	item_type : in tf_description.tag=40,45, 6=Datalog (ba_bin_ft_qa), 其它是 Bin (ba_bin_ws / ba_bin_ft)
*/
public static YieldBin[] getRetentionBinList(String product_code, 
									     String test_mode) {
	StringBuffer SelSQL = new StringBuffer();
	Connection conn = null;

	if (product_code.equals("") || test_mode.equals(""))
		return null;
	
	try {
		ArrayList tmp = new ArrayList();
		conn = DBConnection.getConnection();
		SelSQL.append("SELECT distinct bin_no" +
					"\nFROM BA_FT_AEB_RETENTION"+
					"\nwhere product_body='"+product_code.substring(0, 4)+
					"'\nand test_mode='"+test_mode+
					"'\n");
		
		PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
		ResultSet rs = ps.executeQuery();
		TDSLogger.println(SelSQL);
		
		while (rs.next()) {
			YieldBin yb = new YieldBin();
			yb.setBin_no(rs.getString("bin_no"));
			tmp.add(yb);
		}
		rs.close();
		ps.close();
		rs = null;
		ps = null;
		return (YieldBin[]) tmp.toArray(new YieldBin[0]);
	} catch (Exception ex) {
		ex.printStackTrace();
	} finally {
		DBConnection.close(conn);
		conn = null;
	}
	return null;
}

	/* get item type from name
	
	 */
	public static int getItem_type(String item_type_name) {

		StringBuffer SelSQL = new StringBuffer();
		Connection conn = null;
		int	item_type = -1;

		if (item_type_name == null || item_type_name.equals(""))
			return item_type;
	
		try {
			SelSQL.append("SELECT id" +
					"\nFROM tf_description"+
					"\nwhere tag in (40,45)"+
					"\nand description =?" );
			conn = DBConnection.getConnection();
			PreparedStatement ps = conn.prepareStatement(SelSQL.toString());
			ps.setString(1, item_type_name);
			ResultSet rs = ps.executeQuery();
		
			while (rs.next()) {
				item_type = Integer.parseInt(rs.getString("ID"));
				break;
			}
			rs.close();
			ps.close();
			rs = null;
			ps = null;
			return item_type;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBConnection.close(conn);
			conn = null;
		}
		return item_type;
	}

	private static String get_mode(String test_mode) {
		if (test_mode.equals("AVI"))
			return "Z1";
		if (test_mode.equals("Ship"))
			return "Z2";
		return test_mode;
	}
	
	// Update / Insert Yield data
	public static boolean Update_and_sort(String sid, 
								 String proc_type, 
								 String yid[], 	// empty String if insert
								 String product_code[], 
								 String brands[], 
								 String test_mode[],
								 String lower_limit[], 
								 String flag1[], 
								 String upper_limit[], 
								 String flag2[],
								 String item[], 
								 String item_type[], 
								 String item_mode2[], 
								 String item_bins2[],
								 String action[], 
								 String dg_action[], 
								 HashMap change_ipnHash, 
								 HashMap route_nameHash, 
								 HashMap start_stepHash, 
								 String by_lot_dg[], 
								 String dgrade_special_ipn[], 
								 String dgradeprodcode[], 
								 String remark[],
								 String groupitems_no[],
								 String item_seq[]) {

		StringBuffer sqlUpdate = new StringBuffer();
	    StringBuffer sqlInsert = new StringBuffer();
	    
	    String temp = "";
	    String[] compstr = new String[yid.length];
	    String[] change_ipn = new String[yid.length];
	    String[] route_name = new String[yid.length];
	    String[] start_step = new String[yid.length];
	    
        if(by_lot_dg == null) by_lot_dg = new String [yid.length];
        if(dgrade_special_ipn == null) dgrade_special_ipn = new String [yid.length];
        if(dgradeprodcode == null) dgradeprodcode = new String [yid.length];
        if(groupitems_no == null) groupitems_no = new String [yid.length];
	    
	    for (int i=0; i< yid.length; i++) {
			change_ipn[i] = (String)change_ipnHash.get(""+i);
   			route_name[i] = (String)route_nameHash.get(""+i);
   			start_step[i] = (String)start_stepHash.get(""+i);
   			if (change_ipn[i] == null) change_ipn[i] = "";
   	   		if (route_name[i] == null) route_name[i] = "";
  	    	if (start_step[i] == null) start_step[i] = "";
  	    	if (by_lot_dg[i] == null) by_lot_dg[i] = "";
  	    	if (dgrade_special_ipn[i] == null) dgrade_special_ipn[i] = "";
  	    	if (dgradeprodcode[i] == null) dgradeprodcode[i] = "";
  	    	if (groupitems_no[i] == null) groupitems_no[i] = "";
	    }
	    
	    for (int i=0; i< yid.length; i++) {
    		compstr[i] = product_code[i]+get_mode(test_mode[i])+action[i]+brands[i];
	    }
	    
	    for (int i=0; i< yid.length; i++) {
		    for (int j=i+1; j< yid.length; j++) {
		    	if (compstr[i].compareTo(compstr[j]) > 0) {
		    		temp = compstr[i]; compstr[i] = compstr[j]; compstr[j] = temp;
		    		temp = yid[i]; yid[i] = yid[j]; yid[j] = temp;
		    		temp = product_code[i]; product_code[i] = product_code[j]; product_code[j] = temp;
		    		temp = brands[i]; brands[i] = brands[j]; brands[j] = temp;
		    		temp = test_mode[i]; test_mode[i] = test_mode[j]; test_mode[j] = temp;
		    		temp = lower_limit[i]; lower_limit[i] = lower_limit[j]; lower_limit[j] = temp;
		    		temp = flag1[i]; flag1[i] = flag1[j]; flag1[j] = temp;
		    		temp = upper_limit[i]; upper_limit[i] = upper_limit[j]; upper_limit[j] = temp;
		    		temp = flag2[i]; flag2[i] = flag2[j]; flag2[j] = temp;
		    		temp = item[i]; item[i] = item[j]; item[j] = temp;
		    		temp = item_type[i]; item_type[i] = item_type[j]; item_type[j] = temp;
		    		temp = item_mode2[i]; item_mode2[i] = item_mode2[j]; item_mode2[j] = temp;
		    		temp = item_bins2[i]; item_bins2[i] = item_bins2[j]; item_bins2[j] = temp;
		    		temp = action[i]; action[i] = action[j]; action[j] = temp;
		    		temp = dg_action[i]; dg_action[i] = dg_action[j]; dg_action[j] = temp;
		    		temp = change_ipn[i]; change_ipn[i] = change_ipn[j]; change_ipn[j] = temp;
		    		temp = route_name[i]; route_name[i] = route_name[j]; route_name[j] = temp;
		    		temp = start_step[i]; start_step[i] = start_step[j]; start_step[j] = temp;
		    		temp = by_lot_dg[i]; by_lot_dg[i] = by_lot_dg[j]; by_lot_dg[j] = temp;
		    		temp = dgrade_special_ipn[i]; dgrade_special_ipn[i] = dgrade_special_ipn[j]; dgrade_special_ipn[j] = temp;
		    		temp = dgradeprodcode[i]; dgradeprodcode[i] = dgradeprodcode[j]; dgradeprodcode[j] = temp;
		    		temp = remark[i]; remark[i] = remark[j]; remark[j] = temp;
	    			temp = groupitems_no[i]; groupitems_no[i] = groupitems_no[j]; groupitems_no[j] = temp;
		    		if(item_seq != null){
		    		  temp = item_seq[i]; item_seq[i] = item_seq[j]; item_seq[j] = temp;
		    		}  
		    	}
		    }
	    }
	    
	    Connection conn = null;
	    boolean result = true;
	    int i;
	    try {
	    	conn = DBConnection.getConnection();
	    	conn.setAutoCommit(false);
	    	sqlUpdate.append("Update tf_yield_definition_tx\n"+
	    			" SET SID=?,FACILITY=?,SEQ=?,PRODUCT_CODE=?,BRAND=?,TEST_MODE=?,\n" +
	    			" LOWER_LIMIT=?,FLAG1=?,UPPER_LIMIT=?,FLAG2=?,\n" +
	    			" ITEM_TYPE=?,ITEM=?,ITEM_MODE2=?,ITEM_BINS2=?,\n" +
	    			" ACTION=?,DG_ACTION=?,CHANGE_IPN=?,ROUTE_NAME=?,START_STEP=?,BY_LOT_DG=?,DGRADE_SPECIAL_IPN=?,DGRADEPRODCODE=?,SNOVA_ID=?,VERSION=?,\n" +
	    			" REMARK=?,ITEM_SEQ=?,GROUPITEMS_NO=?\n" +
	      		  	" where sid=? and yid=? and facility=?");
	    	sqlInsert.append(" Insert into tf_yield_definition_tx\n"+
	    			" (SID,YID,FACILITY,SEQ,PRODUCT_CODE,BRAND,TEST_MODE,\n" +
	    			" LOWER_LIMIT,FLAG1,UPPER_LIMIT,FLAG2,\n" +
	    			" ITEM_TYPE,ITEM,ITEM_MODE2,ITEM_BINS2,\n" +
	    			" ACTION,DG_ACTION,CHANGE_IPN,ROUTE_NAME,START_STEP,BY_LOT_DG,DGRADE_SPECIAL_IPN,DGRADEPRODCODE,SNOVA_ID,VERSION,\n" +
	    			" REMARK,ITEM_SEQ,GROUPITEMS_NO) VALUES (\n" +
	    		  	" ?,TF_YIELD_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    	PreparedStatement ps1 = null;
	    	PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate.toString());
	    	PreparedStatement psInsert = conn.prepareStatement(sqlInsert.toString());
	    	/* update all the columns except test_time, time_unit,remark*/
	    	for (i = 0; i < yid.length; i++) {
	    		ps1 = psUpdate;
	    	    if (yid[i].equals("")) 
	    	    	ps1 = psInsert;

	    		ps1.setInt(1, Integer.parseInt(sid));
	    		ps1.setInt(2, (proc_type.equals("WS")?0:1));
	    		ps1.setInt(3, i);
	    		ps1.setString(4, product_code[i]);
	    		ps1.setString(5, brands[i]);
	    		ps1.setString(6, test_mode[i]);
	    		ps1.setString(7, lower_limit[i]);
	    		ps1.setString(8, flag1[i]);
	    		ps1.setString(9, upper_limit[i]);
	    		ps1.setString(10, flag2[i]);
	    		ps1.setInt(11, Integer.parseInt(item_type[i]));
	    		if(item[i].startsWith("AEBRetentionBIN")){
	    			ps1.setString(12, item[i].split("-")[0]);
	    		}else{
	    			ps1.setString(12, item[i]);
	    		}	
	    		ps1.setString(13, item_mode2[i]);
	    		ps1.setString(14, item_bins2[i]);
	    		ps1.setString(15, action[i].substring(2,action[i].length()));
	    		TDSLogger.println("dg_action[i]="+dg_action[i]);
	    		if(dg_action[i] == null || dg_action[i].length()<3){
	    			ps1.setString(16, "");
	    		}else{
	    			ps1.setString(16, dg_action[i].substring(2,dg_action[i].length()));
	    		}
	    		if (change_ipn == null || change_ipn.equals("")) {
	    			ps1.setString(17,"");
	    		} else {
	    			TDSLogger.println("change_ipn[i]="+change_ipn[i]);
	    			ps1.setString(17, change_ipn[i]);
	    		}
	    		if (route_name == null || route_name.equals("")) {
	    			ps1.setString(18,"");
	    		} else {
	    			ps1.setString(18, route_name[i]);
	    		}
	    		if (start_step == null || start_step.equals("")) {
	    			ps1.setString(19,"");
	    		} else {
	    			ps1.setString(19, start_step[i]);
	    		}
	            if (by_lot_dg == null || by_lot_dg.equals("")) {
	                    ps1.setString(20,"");
	                } else {
	                    ps1.setString(20, by_lot_dg[i]);
	            }
	            if (dgrade_special_ipn == null || dgrade_special_ipn.equals("")) {
                       ps1.setString(21,"");
                   } else {
                       ps1.setString(21, dgrade_special_ipn[i]);
                }
	            if (dgradeprodcode == null || dgradeprodcode.equals("")) {
                    ps1.setString(22,"");
                } else {
                    ps1.setString(22, dgradeprodcode[i]);
                }
	            
	            if(item[i].startsWith("AEBRetentionBIN") && !(item[i].contains("null"))){
	    			ps1.setString(23, item[i].split("-")[1]);
	    			ps1.setInt(24, Integer.parseInt(item[i].split("-")[2].replace("V", "").replace("(Released)", "")));
	    		}else{
	    			ps1.setNull(23, Types.NUMERIC);
	    			ps1.setNull(24, Types.NUMERIC);
	    		}
	    		ps1.setString(25, StringUtil.Utf8ToBig5((remark[i])));
	    		if(item_seq == null || item_seq.equals(""))
	    			ps1.setInt(26, Integer.parseInt("0"));
	    		else
	    		  ps1.setInt(26, Integer.parseInt(item_seq[i]));
	    		
	    		if(groupitems_no[i] != null && !groupitems_no[i].equals("")){
	    			ps1.setInt(27, Integer.parseInt(groupitems_no[i]));
	    		}else{
	    			ps1.setNull(27, Types.NUMERIC);
	    		}
	    		
	    		if (!yid[i].equals("")) {
	    			ps1.setInt(28, Integer.parseInt(sid));
	    			ps1.setInt(29, (Integer.parseInt(yid[i])));
	    			ps1.setInt(30, (proc_type.equals("WS")?0:1));
	    		}
	    		
	    		
	    		  
	    		ps1.executeUpdate();
	    	}
	    	psUpdate.close();
	    	psInsert.close();
	    	psUpdate = null;
	    	psInsert = null;
	    	conn.commit();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	DBConnection.rollback(conn);
	    	result = false;
	    } finally {
	    	DBConnection.close(conn);
	    	conn = null;
	    }
	    return result;
	}
	
//	 Insert Yield data
	public static boolean Insert_and_sort(String sid, 
								 String proc_type, 
								 String yid[], 	// empty String if insert
								 String product_code[], 
								 String brands[], 
								 String test_mode[],
								 String lower_limit[], 
								 String flag1[], 
								 String upper_limit[], 
								 String flag2[],
								 String item[], 
								 String item_type[], 
								 String item_mode2[], 
								 String item_bins2[],
								 String action[], 
								 String dg_action[], 
								 HashMap change_ipnHash, 
								 HashMap route_nameHash, 
								 HashMap start_stepHash, 
								 String by_lot_dg[], 
								 String dgrade_special_ipn[], 
								 String dgradeprodcode[], 
								 String remark[],
								 String groupitems_no[],
								 String item_seq[]) {

	    StringBuffer sqlInsert = new StringBuffer();
	    StringBuffer sqlInsert1 = new StringBuffer();
	    
	    String temp = "";
	    String[] compstr = new String[yid.length];
	    String[] change_ipn = new String[yid.length];
	    String[] route_name = new String[yid.length];
	    String[] start_step = new String[yid.length];
        if(by_lot_dg == null) by_lot_dg = new String [yid.length];
        if(dgrade_special_ipn == null) dgrade_special_ipn = new String [yid.length];
        if(dgradeprodcode == null) dgradeprodcode = new String [yid.length];
        if(groupitems_no == null) groupitems_no = new String [yid.length];
        
	    for (int i=0; i< yid.length; i++) {
			change_ipn[i] = (String)change_ipnHash.get(""+i);
   			route_name[i] = (String)route_nameHash.get(""+i);
   			start_step[i] = (String)start_stepHash.get(""+i);
   			if (change_ipn[i] == null) change_ipn[i] = "";
   	   		if (route_name[i] == null) route_name[i] = "";
  	    	if (start_step[i] == null) start_step[i] = "";
  	    	if (by_lot_dg[i] == null) by_lot_dg[i] = "";
  	    	if (dgrade_special_ipn[i] == null) dgrade_special_ipn[i] = "";
  	    	if (dgradeprodcode[i] == null) dgradeprodcode[i] = "";
  	    	if (groupitems_no[i] == null) groupitems_no[i] = "";
	    }
	    
	    for (int i=0; i< yid.length; i++) {
	    	compstr[i] = product_code[i]+get_mode(test_mode[i])+action[i]+brands[i];
	    }
	    for (int i=0; i< yid.length; i++) {
		    for (int j=i+1; j< yid.length; j++) {
		    	if (compstr[i].compareTo(compstr[j]) > 0) {
		    		temp = compstr[i]; compstr[i] = compstr[j]; compstr[j] = temp;
		    		temp = yid[i]; yid[i] = yid[j]; yid[j] = temp;
		    		temp = product_code[i]; product_code[i] = product_code[j]; product_code[j] = temp;
		    		temp = brands[i]; brands[i] = brands[j]; brands[j] = temp;
		    		temp = test_mode[i]; test_mode[i] = test_mode[j]; test_mode[j] = temp;
		    		temp = lower_limit[i]; lower_limit[i] = lower_limit[j]; lower_limit[j] = temp;
		    		temp = flag1[i]; flag1[i] = flag1[j]; flag1[j] = temp;
		    		temp = upper_limit[i]; upper_limit[i] = upper_limit[j]; upper_limit[j] = temp;
		    		temp = flag2[i]; flag2[i] = flag2[j]; flag2[j] = temp;
		    		temp = item[i]; item[i] = item[j]; item[j] = temp;
		    		temp = item_type[i]; item_type[i] = item_type[j]; item_type[j] = temp;
		    		temp = item_mode2[i]; item_mode2[i] = item_mode2[j]; item_mode2[j] = temp;
		    		temp = item_bins2[i]; item_bins2[i] = item_bins2[j]; item_bins2[j] = temp;
		    		temp = action[i]; action[i] = action[j]; action[j] = temp;
		    		temp = dg_action[i]; dg_action[i] = dg_action[j]; dg_action[j] = temp;
		    		temp = change_ipn[i]; change_ipn[i] = change_ipn[j]; change_ipn[j] = temp;
		    		temp = route_name[i]; route_name[i] = route_name[j]; route_name[j] = temp;
		    		temp = start_step[i]; start_step[i] = start_step[j]; start_step[j] = temp;
                    temp = by_lot_dg[i]; by_lot_dg[i] = by_lot_dg[j]; by_lot_dg[j] = temp;
                    temp = dgrade_special_ipn[i]; dgrade_special_ipn[i] = dgrade_special_ipn[j]; dgrade_special_ipn[j] = temp;
                    temp = dgradeprodcode[i]; dgradeprodcode[i] = dgradeprodcode[j]; dgradeprodcode[j] = temp;
		    		temp = remark[i]; remark[i] = remark[j]; remark[j] = temp;
	    			temp = groupitems_no[i]; groupitems_no[i] = groupitems_no[j]; groupitems_no[j] = temp;
		    		temp = item_seq[i]; item_seq[i] = item_seq[j]; item_seq[j] = temp;
		    	}
		    }
	    }
	    
	    Connection conn = null;
	    boolean result = true;
	    int i = 0;
	    try {
	    	conn = DBConnection.getConnection();
	    	conn.setAutoCommit(false);
	    	//lai-add-20131209-start
	    	StringBuffer sql = new StringBuffer();
	    	sql.append("delete from tf_yield_definition_tx\n"+
			   "where sid=? and facility=?");
	    	PreparedStatement ps = conn.prepareStatement(sql.toString());
	    	/* update all the columns except test_time, time_unit,remark*/
	    	ps.setInt(1, Integer.parseInt(sid));
	    	ps.setInt(2, (proc_type.equals("WS")?0:1));
	    	ps.executeUpdate();
	    	//lai-add-20131209-end
	    	sqlInsert.append("Insert into tf_yield_definition_tx\n"+
	    			"(SID,FACILITY,SEQ,PRODUCT_CODE,BRAND,TEST_MODE,\n" +
	    			"LOWER_LIMIT,FLAG1,UPPER_LIMIT,FLAG2,\n" +
	    			"ITEM_TYPE,ITEM,ITEM_MODE2,ITEM_BINS2,\n" +
	    			"ACTION,DG_ACTION,CHANGE_IPN,ROUTE_NAME,START_STEP,BY_LOT_DG,DGRADE_SPECIAL_IPN,DGRADEPRODCODE,SNOVA_ID,VERSION,\n" +
	    			"REMARK,ITEM_SEQ,GROUPITEMS_NO,YID) VALUES (\n" +
	    		  	"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    	sqlInsert1.append("Insert into tf_yield_definition_tx\n"+
	    			"(SID,FACILITY,SEQ,PRODUCT_CODE,BRAND,TEST_MODE,\n" +
	    			"LOWER_LIMIT,FLAG1,UPPER_LIMIT,FLAG2,\n" +
	    			"ITEM_TYPE,ITEM,ITEM_MODE2,ITEM_BINS2,\n" +
	    			"ACTION,DG_ACTION,CHANGE_IPN,ROUTE_NAME,START_STEP,BY_LOT_DG,DGRADE_SPECIAL_IPN,DGRADEPRODCODE,SNOVA_ID,VERSION,\n" +
	    			"REMARK,ITEM_SEQ,GROUPITEMS_NO,YID) VALUES (\n" +
	    		  	"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TF_YIELD_SEQ.nextval)");
	    	PreparedStatement ps1 = null;
	    	PreparedStatement psInsert = conn.prepareStatement(sqlInsert.toString());
	    	PreparedStatement psInsert1 = conn.prepareStatement(sqlInsert1.toString());
	    	/* update all the columns except test_time, time_unit,remark*/
	    	for (i = 0; i < yid.length; i++) {
	    		ps1 = psInsert;
	    	    if (yid[i].equals("")){ 
	    	    	ps1 = psInsert1;
	    	    	//continue;
	    	    }	

	    		ps1.setInt(1, Integer.parseInt(sid));
	    		
	    		ps1.setInt(2, (proc_type.equals("WS")?0:1));
	    		ps1.setInt(3, i);
	    		ps1.setString(4, product_code[i]);
	    		ps1.setString(5, brands[i]);
	    		ps1.setString(6, test_mode[i]);
	    		ps1.setString(7, lower_limit[i]);
	    		ps1.setString(8, flag1[i]);
	    		ps1.setString(9, upper_limit[i]);
	    		ps1.setString(10, flag2[i]);
	    		ps1.setInt(11, Integer.parseInt(item_type[i]));
	    		if(item[i].startsWith("AEBRetentionBIN")){
	    			ps1.setString(12, item[i].split("-")[0]);
	    		}else{
	    			ps1.setString(12, item[i]);
	    		}	
	    		ps1.setString(13, item_mode2[i]);
	    		ps1.setString(14, item_bins2[i]);
	    		ps1.setString(15, action[i].substring(2,action[i].length()));
	    		//TDSLogger.println("dg_action[i]="+dg_action[i]);
	    		if(dg_action[i] == null || dg_action[i].length()<3){
	    			ps1.setString(16, "");
	    		}else{
	    			ps1.setString(16, dg_action[i].substring(2,dg_action[i].length()));
	    		}
	    		if (change_ipn == null || change_ipn.equals("")) {
	    			ps1.setString(17,"");
	    		} else {
	    			//TDSLogger.println("change_ipn[i]="+change_ipn[i]);
	    			ps1.setString(17, change_ipn[i]);
	    		}
	    		if (route_name == null || route_name.equals("")) {
	    			ps1.setString(18,"");
	    		} else {
	    			ps1.setString(18, route_name[i]);
	    		}
	    		if (start_step == null || start_step.equals("")) {
	    			ps1.setString(19,"");
	    		} else {
	    			ps1.setString(19, start_step[i]);
	    		}
	             if (by_lot_dg == null || by_lot_dg.equals("")) {
	                    ps1.setString(20,"");
	                } else {
	                    ps1.setString(20, by_lot_dg[i]);
	                }
	            if (dgrade_special_ipn == null || dgrade_special_ipn.equals("")) {
                       ps1.setString(21,"");
                   } else {
                       ps1.setString(21, dgrade_special_ipn[i]);
                }
	            if (dgradeprodcode == null || dgradeprodcode.equals("")) {
                    ps1.setString(22,"");
                } else {
                    ps1.setString(22, dgradeprodcode[i]);
                }
	            if(item[i].startsWith("AEBRetentionBIN") && !(item[i].contains("null"))){
	    			ps1.setString(23, item[i].split("-")[1]);
	    			ps1.setInt(24, Integer.parseInt(item[i].split("-")[2].replace("V", "").replace("(Released)", "")));
	    		}else{
	    			ps1.setNull(23, Types.NUMERIC);
	    			ps1.setNull(24, Types.NUMERIC);
	    		}
	    		ps1.setString(25, StringUtil.Utf8ToBig5((remark[i])));
	    		ps1.setInt(26, Integer.parseInt(item_seq[i]));
	    		if(groupitems_no[i] != null && !groupitems_no[i].equals("")){
	    			ps1.setInt(27, Integer.parseInt(groupitems_no[i]));
	    		}else{
	    			ps1.setNull(27, Types.NUMERIC);
	    		}
	    		if (!yid[i].equals(""))
		    		  ps1.setInt(28, (Integer.parseInt(yid[i])));
		        
	    		
	    			
	    		/*if (!yid[i].equals("")) {
	    			ps1.setInt(21, Integer.parseInt(sid));
	    			ps1.setInt(22, (Integer.parseInt(yid[i])));
	    			ps1.setInt(23, (proc_type.equals("WS")?0:1));
	    		}*/
	    		/*TDSLogger.println("i="+i);
	    		if(i==56)
	    			TDSLogger.println("i="+i);*/
 	    		ps1.executeUpdate();
	    	}
	    	psInsert.close();
	    	psInsert1.close();
	    	ps.close();
	    	psInsert = null;
	    	psInsert1 = null;
	    	ps = null;
	    	conn.commit();
	    } catch (Exception e) {
	    	TDSLogger.println("index: " + i + ", item: " + item[i] + ", error: " + e.toString());
	    	DBConnection.rollback(conn);
	    	result = false;
	    } finally {
	    	DBConnection.close(conn);
	    	conn = null;
	    }
	    return result;
	}
}
