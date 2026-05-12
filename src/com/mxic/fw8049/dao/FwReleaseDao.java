package com.mxic.fw8049.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mxic.fwrs.applyform.FwrsNclFwAppActionForm;
import com.mxic.oiplus.util.GPRSDB;

public class FwReleaseDao {

	public static List<String> queryANoList(Connection con) throws Exception {

		List<String> result = new ArrayList<String>();

		String sql = "SELECT F.FLAG, F.NCL_FORM_NO FROM IF_FWNO F WHERE F.FLAG = ?";

		HashMap<String, String>[] rows = GPRSDB.qryHashMapBySql(con, sql,
				new Object[] { "U" });

		if (rows == null || rows.length == 0) {
			return result;
		}

		for (HashMap<String, String> row : rows) {

			String noList = row.get("NCL_FORM_NO");

			if (noList == null || noList.trim().isEmpty()) {
				continue;
			}

			String[] noArray = noList.split(",");

			for (String no : noArray) {

				if (no == null || no.trim().isEmpty()) {
					continue;
				}

				result.add(no.trim());
			}
		}

		return result;
	}

	public static List<FwrsNclFwAppActionForm> queryNclFWAppFormList(Connection con,
			List<String> noList) throws Exception {

		List<FwrsNclFwAppActionForm> result = new ArrayList<FwrsNclFwAppActionForm>();

		if (noList == null || noList.isEmpty()) {
			return result;
		}

		for (String no : noList) {

			if (no == null || no.trim().isEmpty()) {
				continue;
			}

			no = no.trim();

			FwrsNclFwAppActionForm fm = queryNclFWAppFormByFormNo(con, no);
			
			if (fm != null) {
                result.add(fm);
            }

		}

		return result;
	}

	private static FwrsNclFwAppActionForm queryNclFWAppFormByFormNo(Connection con, String no)
			throws Exception {

		String sql = "SELECT N.APP_ID, N.PROD_BODY, N.BE_OPTION, N.MAJOR, N.MINOR, N.TEST_FW_VERSION, N.HSM, N.HSM_KEY_FTF, N.NCL_FORM_NO, N.STATUS FROM FWRS_NCL_FW_APP N WHERE N.NCL_FORM_NO = ? AND N.STATUS = ?";

		HashMap<String, String>[] rows = GPRSDB.qryHashMapBySql(con, sql,
				new Object[] { no, "送EPC會簽中" });

		if (rows == null || rows.length == 0) {
			return null;
		}
		HashMap<String, String> row = rows[0];
		
		HashMap values = new HashMap();
		values.put("STATUS", "已結案");
		HashMap conditions = new HashMap();
		conditions.put("NCL_FORM_NO", row.get("NCL_FORM_NO"));
		conditions.put("STATUS", row.get("STATUS"));
	
		GPRSDB.update(con, "FWRS_NCL_FW_APP", values, conditions);
		
		FwrsNclFwAppActionForm fm = new FwrsNclFwAppActionForm();

		fm.setApp_id(row.get("APP_ID"));
		fm.setStatus(row.get("STATUS"));

		fm.setProd_body(row.get("PROD_BODY"));
		fm.setBe_option(row.get("BE_OPTION"));
		fm.setMajor(row.get("MAJOR"));
		fm.setMinor(row.get("MINOR"));
		fm.setTest_fw_version(row.get("TEST_FW_VERSION"));
		fm.setHsm(row.get("HSM"));
		fm.setNcl_form_no(row.get("NCL_FORM_NO"));
		fm.setHsm_key_ftf(row.get("HSM_KEY_FTF"));
		
		return fm;
			
	}
}
