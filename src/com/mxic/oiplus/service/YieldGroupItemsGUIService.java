package com.mxic.oiplus.service;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxic.oiplus.dao.YieldGroupItemsBean;
import com.mxic.oiplus.oimaintain.BomProductRouteBean;
import com.mxic.oiplus.oimaintain.OiMaintainService;
import com.mxic.oiplus.oimaintain.YieldDefService;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.tdsplus.common.ListConvertJSONFormat;

public class YieldGroupItemsGUIService {
	
	private String sid;
	private String proc_type;
	private String product_type;
	private String product_body;
	private String brand;
	private String version;
	private String hold_downgrade;
	private String insert_modify;
	private String rownumber_1;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getProc_type() {
		return proc_type;
	}

	public void setProc_type(String proc_type) {
		this.proc_type = proc_type;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getProduct_body() {
		return product_body;
	}

	public void setProduct_body(String product_body) {
		this.product_body = product_body;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHold_downgrade() {
		return hold_downgrade;
	}

	public void setHold_downgrade(String hold_downgrade) {
		this.hold_downgrade = hold_downgrade;
	}

	public String getInsert_modify() {
		return insert_modify;
	}

	public void setInsert_modify(String insert_modify) {
		this.insert_modify = insert_modify;
	}

	public String getRownumber_1() {
		return rownumber_1;
	}

	public void setRownumber_1(String rownumber_1) {
		this.rownumber_1 = rownumber_1;
	}
//=============================================================================
	public HashMap<String, String>[] getItemList(){
        String sql = 
        		"SELECT A.ID ITEM_TYPE, B.DESCRIPTION ITEM_NAME, A.DESCRIPTION ITEM_BLOCK\n" +
        				"FROM TF_DESCRIPTION A, TF_DESCRIPTION B\n" + 
        				"WHERE A.TAG = 41 AND B.TAG = 49\n" + 				//49 for Yield Group item
        				"AND A.ID = B.ID AND B.DELETE_FLAG IS NULL\n" + 
        				"ORDER BY A.ID";
		Connection conn = null;
		HashMap<String, String>[] hm = null;
        try {        	
            conn = DBConnection.getConnection();
            hm = GPRSDB.qryHashMapBySql(conn, sql, new Object[]{});
        } catch (Exception e) {
            TDSLogger.println(e.getMessage());
        } finally {
        	DBConnection.close(conn);
        }
        return hm;
    }
			
	public String[] getTestModeList(){
		return YieldDefService.getTestModeList(product_body, proc_type, "ALL");
	}
	
	public String[] getProductCodeList(){
		return YieldDefService.getProductList(product_body, proc_type);
	}
	
	public String[] getActionList(){		
		String[] actionList = null;
		if(hold_downgrade.equals("hold")){
			//actionList = OiMaintainService.getDescription("442");
			actionList = new String[]{"Hold"};
		}else if(hold_downgrade.equals("downgrade")){
		//if(proc_type.equals("WS") && product_type.equals("NVM")){
			actionList = OiMaintainService.getProdWaferLevel(sid);
			//actionListPriority = OiMaintainService.getProdWaferLevelPriority(sid);
		}else{
		    //actionList = OiMaintainService.getDescription("44");
		} 
		return actionList;
	}

	public String[] getActionListPriority(){
		String[] actionListPriority = null;
		if(proc_type.equals("WS") && product_type.equals("NVM")){
			actionListPriority = OiMaintainService.getProdWaferLevelPriority(sid);
		}
		return actionListPriority;
	}
	
	/*
	public String[] getProdWaferLevelList(){
		return YieldDefService.getProdWaferLevelList(product_body, brand, version);
	}
	*/
	
	public String[] getRouteNameList(){
		return YieldDefService.getRouteNameList(sid, proc_type);
	}
	
	public ArrayList<String> getStatusList(){
		return OiMaintainService.selectFromBADescriptionList(145,"first");
	}
	
	public String getDgradeBomRouteString(){
		BomProductRouteBean[] ptrTX = OiMaintainService.GetSortRouteCodeDG(sid, product_body);
		StringBuffer ptrTXString = new StringBuffer();
		if (ptrTX != null) {
			for (int i = 0; i < ptrTX.length; i++)
			{
				ptrTXString.append(ptrTX[i].getComment());
				ptrTXString.append(product_body);
				ptrTXString.append(ptrTX[i].getMaskopt());
				ptrTXString.append(ptrTX[i].getWsroute());
				ptrTXString.append("~");
			}
		}
		return ptrTXString.toString();
	}
	
	public String getChangeIPNProdCodeString(){
		String[] chgipnProd = OiMaintainService.getDescription("502");
		StringBuffer chgipnProdString = new StringBuffer();
		if (chgipnProd != null) {
			for (int i = 0; i < chgipnProd.length; i++)
			{
				chgipnProdString.append(chgipnProd[i]);
				chgipnProdString.append("~");
			}
			//TDSLogger.println(chgipnProdString.toString());
		}

		return chgipnProdString.toString();
	}
	
	
	
	
	public static String getGroupItemsCriteriaListToJason(String type, String brand, String productCode, String testMode, String prodLevel, String version){
		ArrayList<HashMap<String, HashMap<String, String>[]>> groupCritList = getGroupItemsCriteriaList(type, brand, productCode, testMode, prodLevel, version);
		ListConvertJSONFormat<HashMap<String, HashMap<String, String>[]>> json = new ListConvertJSONFormat<HashMap<String, HashMap<String, String>[]>>(groupCritList);
		//TDSLogger.println(json.toJSONString());
		return json.toJSONString();

	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<HashMap<String, HashMap<String, String>[]>> getGroupItemsCriteriaList(String type, String brand, String productCode, String testMode, String prodLevel, String version){
		ArrayList<HashMap<String, HashMap<String, String>[]>> groupCritList = new ArrayList<HashMap<String, HashMap<String, String>[]>>();

		String groupNoSql = "SELECT DISTINCT GROUP_NO FROM TF_YIELD_GROUPITEMS_TX T\n" +
					"WHERE T.TYPE = ? AND T.PRODUCT_CODE = ? AND T.TEST_MODE = ? AND PROD_LEVEL = ? AND T.VERSION = ?\n" + 
					"ORDER BY GROUP_NO";
		
		String itemsSql = "SELECT T.GROUP_NO, T.ITEM_NO, T.ITEM_TYPE, T.ITEM, T.RANGE_VALUES FROM TF_YIELD_GROUPITEMS_TX T\n" +
					"WHERE T.TYPE = ? AND T.PRODUCT_CODE = ? AND T.TEST_MODE = ? AND PROD_LEVEL = ? AND T.VERSION = ? AND T.GROUP_NO = ?\n" + 
					"ORDER BY GROUP_NO, ITEM_NO";
		
		String itemCritSql =
				"SELECT T2.YID, T2.SID, T2.PRODUCT_CODE, T2.BRAND PROD_LEVEL, T2.TEST_MODE, T2.ITEM_TYPE, T2.ITEM, (D.ID+10 || T2.ACTION) ACTION,\n" +	//(D.ID+10 || T2.ACTION) ACTION
				"    T2.ROUTE_NAME, T2.START_STEP, T2.BY_LOT_DG, T2.DGRADE_SPECIAL_IPN, T2.DGRADEPRODCODE, T2.REMARK, T2.GROUPITEMS_NO\n" + 
				"FROM TF_INFORMATION T1, TF_YIELD_DEFINITION_TX T2, TF_DESCRIPTION D\n" + 
				"WHERE T1.SID = T2.SID AND T1.PRODUCT_BODY = ? AND T1.BRAND = ? AND T1.VERSION = ?\n" + 
				"   AND T2.PRODUCT_CODE = ? AND T2.TEST_MODE = ? AND T2.GROUPITEMS_NO = ? AND T2.ITEM_TYPE = 30\n";
			if(type.equals("hold")){
				itemCritSql += "   AND (T2.ACTION NOT LIKE 'Dgrade%' AND T2.ACTION NOT LIKE 'Change IPN%') \n";
			}else{
				itemCritSql += "   AND (T2.ACTION LIKE 'Dgrade%' OR T2.ACTION LIKE 'Change IPN%') \n";				
			}
			itemCritSql += "   AND D.TAG = 441 AND D.DELETE_FLAG IS NULL\n" + 
				"   AND T2.ACTION = D.DESCRIPTION\n";
		if(!type.equals("hold")){
			itemCritSql +="UNION\n" +
				"SELECT T2.YID, T2.SID, T2.PRODUCT_CODE, T2.BRAND PROD_LEVEL, T2.TEST_MODE, T2.ITEM_TYPE, T2.ITEM, (D.ORI_PRIORITY+10 || T2.ACTION) ACTION,\n" +	//(D.ORI_PRIORITY+10 || T2.ACTION) ACTION
				"    T2.ROUTE_NAME, T2.START_STEP, T2.BY_LOT_DG, T2.DGRADE_SPECIAL_IPN, T2.DGRADEPRODCODE, T2.REMARK, T2.GROUPITEMS_NO\n" + 
				"FROM TF_INFORMATION T1, TF_YIELD_DEFINITION_TX T2, TF_PROD_WAFERLEVEL_TX D\n" + 
				"WHERE T1.SID = T2.SID AND T1.PRODUCT_BODY = ? AND T1.BRAND = ? AND T1.VERSION = ?\n" + 
				"   AND T2.PRODUCT_CODE = ? AND T2.TEST_MODE = ?  AND T2.GROUPITEMS_NO = ? AND T2.ITEM_TYPE = 30\n" + 
				"   AND T1.SID = D.SID AND T2.ACTION = 'Dgrade-' || D.WAFER_LEVEL";
			/*
			itemCritSql +="UNION\n" +
					"SELECT T2.YID, T2.SID, T2.PRODUCT_CODE, T2.BRAND PROD_LEVEL, T2.TEST_MODE, T2.ITEM_TYPE, T2.ITEM, (D.ORI_PRIORITY+10 || T2.ACTION) ACTION,\n" +	//(D.ORI_PRIORITY+10 || T2.ACTION) ACTION
					"    T2.ROUTE_NAME, T2.START_STEP, T2.BY_LOT_DG, T2.DGRADE_SPECIAL_IPN, T2.DGRADEPRODCODE, T2.REMARK, T2.GROUPITEMS_NO\n" + 
					"FROM TF_INFORMATION T1, TF_YIELD_DEFINITION_TX T2, TF_CHANGEIPN_LEVEL D\n" + 
					"WHERE T1.SID = T2.SID AND T1.PRODUCT_BODY = ? AND T1.BRAND = ? AND T1.VERSION = ?\n" + 
					"   AND T2.PRODUCT_CODE = ? AND T2.TEST_MODE = ?  AND T2.GROUPITEMS_NO = ? AND T2.ITEM_TYPE = 30\n" + 
					"   AND T2.ACTION = D.LEVEL_NAME";
			*/
		}
		
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			
			//
			List<String> groupNoList = GPRSDB.qryListBySql(con, groupNoSql, new Object[]{type, productCode, testMode, prodLevel, version});
			for(String groupNo:groupNoList){	
				//
				HashMap<String, String>[] items = GPRSDB.qryHashMapBySql(con, itemsSql, new Object[]{type, productCode, testMode, prodLevel, version, groupNo});
				String typeName = null;
				String groupNos = null;
				String itemNos = null;
				String itemTypes = null;
				String itemNames = null;
				String ranges = null;
				
				for(HashMap<String, String> itemCrit:items){
					if(typeName == null){
						typeName = itemCrit.get("ITEM");
						groupNos = groupNo;
						itemNos = itemCrit.get("ITEM_NO");
						itemTypes = itemCrit.get("ITEM_TYPE");
						itemNames = itemCrit.get("ITEM");
						ranges = itemCrit.get("RANGE_VALUES");
					}else{
						typeName += " & " + itemCrit.get("ITEM");
						groupNos = ";" + groupNo;
						itemNos += ";" + itemCrit.get("ITEM_NO");
						itemTypes += ";" + itemCrit.get("ITEM_TYPE");
						itemNames += ";" + itemCrit.get("ITEM");
						ranges += ";" + itemCrit.get("RANGE_VALUES");
					}
				}

				HashMap<String, String> groupItem = new HashMap<String, String>(); 
				groupItem.put("TYPE_NAME", typeName);
				groupItem.put("GROUP_NOS", groupNos);
				groupItem.put("ITEM_NOS", itemNos);
				groupItem.put("ITEM_TYPES", itemTypes);
				groupItem.put("RANGES", ranges);
				groupItem.put("ITEM_NAME", itemNames);

				@SuppressWarnings("unchecked")
				HashMap<String, String>[] groupTable = new HashMap[1];
				groupTable[0] = groupItem;
								
				Object[] cond = null;
				if(type.equals("hold")){
					cond = new Object[]{productCode.substring(0, 4), brand, version, productCode, testMode, groupNo};
				}else{
					cond = new Object[]{productCode.substring(0, 4), brand, version, productCode, testMode, groupNo,
							productCode.substring(0, 4), brand, version, productCode, testMode, groupNo};
				}
				//Item Crit
				TDSLogger.println("itemCritSql: " + itemCritSql);
				HashMap<String, String>[] itemCrits= GPRSDB.qryHashMapBySql(con, itemCritSql, cond);
			
				//items = BAYieldDefService.getDgradeAndChangeIPNLevelItems();
					
				HashMap<String, HashMap<String, String>[]> critData = new HashMap<String, HashMap<String, String>[]>();				
				critData.put("groupItems", groupTable);
				critData.put("itemCrits", itemCrits);
				
				groupCritList.add(critData);
			}
		}catch(Exception e){	
			TDSLogger.println(e);
		}finally{
			DBConnection.close(con);
		}
		return groupCritList;
	}	
	
	
	public static YieldGroupItemsBean jasonToObject(String jsonFormat){
		ObjectMapper mapper = new ObjectMapper();
		 //mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		//mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		YieldGroupItemsBean groupItems = null;
		try {
			groupItems = mapper.readValue(jsonFormat, new TypeReference<YieldGroupItemsBean>(){});			
		} catch (JsonParseException e) {
			TDSLogger.println(e);
		} catch (JsonMappingException e) {
			TDSLogger.println(e);
		} catch (IOException e) {
			TDSLogger.println(e);
		}
		
		return groupItems;
	}
	
	public static void main(String[] agrs){
		String jasonGroupItemsCrits = getGroupItemsCriteriaListToJason("hold", "MX", "6316A", "S1", "All", "11");
		TDSLogger.println(jasonGroupItemsCrits);
		
		TDSLogger.println("=============================");
		//String jsonFormat = "{\"action_type\":\"saveGroupCrits\",\"proc_type\":\"WS\",\"product_type\":\"NVM\",\"product_code\":\"6316A\",\"test_mode\":\"S1\",\"wafer_level\":\"All\",\"group_name\":[\"Yield & BIN5+BIN7 & BIN6+BIN8\",\"Yield & BIN5+BIN7 & BIN6+BIN8\",\"Yield & BIN5+BIN7 & BIN6+BIN8\",\"Yield & BIN5+BIN7 & BIN6+BIN9\",\"Yield & BIN5+BIN7 & BIN6+BIN9\",\"Yield & BIN5+BIN7 & BIN6+BIN9\"],\"group_no\":[\"1\",\"1\",\"1\",\"2\",\"2\",\"2\"],\"item_no\":[\"1\",\"2\",\"3\",\"1\",\"2\",\"3\"],\"item_type\":[\"0\",\"1\",\"1\",\"0\",\"1\",\"1\"],\"item_name\":[\"Yield\",\"BIN5+BIN7\",\"BIN6+BIN8\",\"Yield\",\"BIN5+BIN7\",\"BIN6+BIN9\"],\"range_value\":[\"35\",\"30,65\",\"25,60\",\"35,70\",\"30,65\",\"25,60\"]}";
		String jsonGroupItemsFormat = "{\"action_type\":\"saveGroupCrits\",\"dummy\":\"\",\"proc_type\":\"WS\",\"product_type\":\"NVM\",\"product_code\":\"6316A\",\"test_mode\":\"S1\",\"wafer_level\":\"All\",\"actions\":\" , , \",\"typeName1\":\"Yield & BIN5+BIN7 & BIN6+BIN8\",\"group_name\":[\"Yield & BIN5+BIN7 & BIN6+BIN8\",\"Yield & BIN5+BIN7 & BIN6+BIN8\",\"Yield & BIN5+BIN7 & BIN6+BIN8\",\"Yield & BIN5+BIN7 & BIN6+BIN9\",\"Yield & BIN5+BIN7 & BIN6+BIN9\",\"Yield & BIN5+BIN7 & BIN6+BIN9\"],\"group_no\":[\"1\",\"1\",\"1\",\"2\",\"2\",\"2\"],\"item_no\":[\"1\",\"2\",\"3\",\"1\",\"2\",\"3\"],\"item_type\":[\"0\",\"1\",\"1\",\"0\",\"1\",\"1\"],\"item_name\":[\"Yield\",\"BIN5+BIN7\",\"BIN6+BIN8\",\"Yield\",\"BIN5+BIN7\",\"BIN6+BIN9\"],\"range_value\":[\"35\",\"30,65\",\"25,60\",\"35,70\",\"30,65\",\"25,60\"],\"typeName2\":\"Yield & BIN5+BIN7 & BIN6+BIN9\"}";
		
		YieldGroupItemsBean groupItemsBean = YieldGroupItemsGUIService.jasonToObject(jsonGroupItemsFormat);		
		TDSLogger.println(groupItemsBean.getAction_type());
		TDSLogger.println(groupItemsBean.getProc_type());
		TDSLogger.println(groupItemsBean.getProduct_type());
		TDSLogger.println(groupItemsBean.getProduct_code());
		TDSLogger.println(groupItemsBean.getTest_mode());
		TDSLogger.println(groupItemsBean.getWafer_level());

		for(int i=0; i<groupItemsBean.getGroup_name().length; i++){
			TDSLogger.println(groupItemsBean.getGroup_name()[i]+";"+groupItemsBean.getGroup_no()[i]+";"+groupItemsBean.getItem_no()[i]+
					";"+groupItemsBean.getItem_type()[i]+";"+groupItemsBean.getItem_name()[i]+";"+groupItemsBean.getRange_value()[i]);
		}

	}
	
}
