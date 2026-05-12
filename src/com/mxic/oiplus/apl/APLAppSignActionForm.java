package com.mxic.oiplus.apl;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.mxic.gprs.applyform.ApplyFormService;
import com.mxic.oiplus.apl.applyform.ApplyFormDeputyService;
import com.mxic.gprs.applyform.ApplyFormUtil;
import com.mxic.oiplus.apl.bean.AplEventBean;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;

public class APLAppSignActionForm  extends ActionForm{
	private String stage="N/A";
	private String btn;
	private String loginEmpNo; 		// 登入者工號
	private String loginEmpName; 	// 登入者名稱
	private String comments;
	private ArrayList actBtn;
	private String process_type;
	private String initial_time;
	private String reason;
	private String is_agree;
	
	private String app_no;
	private String status;
	private String applicant;
	private String applicant_date;
	private String applicant_boss;
	private String applicant_boss_date;
	private String applicant_dept_boss;
	private String applicant_dept_boss_date;
	private String countersign_boss_1;
	private String countersign_boss_1_date;
	private String countersign_boss_2;
	private String countersign_boss_2_date;
	private String countersign_boss_3;
	private String countersign_boss_3_date;
	private String countersign_boss_4;
	private String countersign_boss_4_date;
	private String close_date;
	
	/**
	 * 申請人姓名
	 * @return
	 */
	public String getApplicantRealName() {
		if (applicant != null) {
			return ApplyFormService.getUserNameByEmpNo(applicant);
		}
		return "";
	}

	/**
	 * 讀取申請者部門
	 * 
	 * @return
	 */
	public String getApplicantDept() {
		if (applicant != null) {
			return ApplyFormService.getUserDeptByEmpNo(applicant);
		}
		return "";
	}
	
	public String getApplicantDeptChineseName() {
		if (applicant != null) {
			return ApplyFormService.getUserDeptChineseNameByEmpNo(applicant);
		}
		return "";
	}
	
	/**
	 * 讀取申請者分機
	 * 
	 * @return
	 */
	public String getApplicantExt() {
		if (applicant != null) {
			return ApplyFormService.getUserExtByEmpNo(applicant);
		}
		return "";
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getBtn() {
		return btn;
	}

	public void setBtn(String btn) {
		this.btn = btn;
	}

	public String getLoginEmpNo() {
		return loginEmpNo;
	}

	public void setLoginEmpNo(String loginEmpNo) {
		this.loginEmpNo = loginEmpNo;
	}

	public String getLoginEmpName() {
		return loginEmpName;
	}

	public void setLoginEmpName(String loginEmpName) {
		this.loginEmpName = loginEmpName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public ArrayList getActBtn() {
		return actBtn;
	}

	public void setActBtn(ArrayList actBtn) {
		this.actBtn = actBtn;
	}

	public String getProcess_type() {
		return process_type;
	}

	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}

	public String getInitial_time() {
		return initial_time;
	}

	public void setInitial_time(String initial_time) {
		this.initial_time = initial_time;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getApp_no() {
		return app_no;
	}

	public void setApp_no(String app_no) {
		this.app_no = app_no;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getApplicant_date() {
		return applicant_date;
	}

	public void setApplicant_date(String applicant_date) {
		this.applicant_date = applicant_date;
	}

	public String getApplicant_boss() {
		return applicant_boss;
	}

	public String getApplicant_boss_info() {
		if (applicant_boss != null) {
			return ApplyFormService.getUserDeptByEmpNo(applicant_boss) + 
			"/" + applicant_boss + " " + ApplyFormService.getUserNameByEmpNo(applicant_boss);
		}
		return "";
	}

	public void setApplicant_boss(String applicant_boss) {
		this.applicant_boss = applicant_boss;
	}

	public String getApplicant_boss_date() {
		return applicant_boss_date;
	}

	public void setApplicant_boss_date(String applicant_boss_date) {
		this.applicant_boss_date = applicant_boss_date;
	}

	public String getApplicant_dept_boss() {
		return applicant_dept_boss;
	}

	public String getApplicant_dept_boss_info() {
		if (applicant_dept_boss != null) {
			return ApplyFormService.getUserDeptByEmpNo(applicant_dept_boss) + 
			"/" + applicant_dept_boss + " " + ApplyFormService.getUserNameByEmpNo(applicant_dept_boss);
		}
		return "";
	}	
	
	public void setApplicant_dept_boss(String applicant_dept_boss) {
		this.applicant_dept_boss = applicant_dept_boss;
	}

	public String getApplicant_dept_boss_date() {
		return applicant_dept_boss_date;
	}

	public void setApplicant_dept_boss_date(String applicant_dept_boss_date) {
		this.applicant_dept_boss_date = applicant_dept_boss_date;
	}

	public String getCountersign_boss_1() {
		return countersign_boss_1;
	}
	
	public String getCountersign_boss_1_info() {
		if (countersign_boss_1 != null) {
			return ApplyFormService.getUserDeptByEmpNo(countersign_boss_1) + 
				"/" + countersign_boss_1 + " " + ApplyFormService.getUserNameByEmpNo(countersign_boss_1);
		}
		return "";
	}

	public void setCountersign_boss_1(String countersign_boss_1) {
		this.countersign_boss_1 = countersign_boss_1;
	}

	public String getCountersign_boss_1_date() {
		return countersign_boss_1_date;
	}

	public void setCountersign_boss_1_date(String countersign_boss_1_date) {
		this.countersign_boss_1_date = countersign_boss_1_date;
	}

	public String getCountersign_boss_2() {
		return countersign_boss_2;
	}

	public String getCountersign_boss_2_info() {
		if (countersign_boss_2 != null) {
			return ApplyFormService.getUserDeptByEmpNo(countersign_boss_2) + 
				"/" + countersign_boss_2 + " " + ApplyFormService.getUserNameByEmpNo(countersign_boss_2);
		}
		return "";
	}

	public void setCountersign_boss_2(String countersign_boss_2) {
		this.countersign_boss_2 = countersign_boss_2;
	}

	public String getCountersign_boss_2_date() {
		return countersign_boss_2_date;
	}

	public void setCountersign_boss_2_date(String countersign_boss_2_date) {
		this.countersign_boss_2_date = countersign_boss_2_date;
	}

	public String getCountersign_boss_3() {
		return countersign_boss_3;
	}

	public String getCountersign_boss_3_info() {
		if (countersign_boss_3 != null) {
			return ApplyFormService.getUserDeptByEmpNo(countersign_boss_3) + 
				"/" + countersign_boss_3 + " " + ApplyFormService.getUserNameByEmpNo(countersign_boss_3);
		}
		return "";
	}

	public void setCountersign_boss_3(String countersign_boss_3) {
		this.countersign_boss_3 = countersign_boss_3;
	}

	public String getCountersign_boss_3_date() {
		return countersign_boss_3_date;
	}

	public void setCountersign_boss_3_date(String countersign_boss_3_date) {
		this.countersign_boss_3_date = countersign_boss_3_date;
	}

	public String getCountersign_boss_4() {
		return countersign_boss_4;
	}

	public String getCountersign_boss_4_info() {
		if (countersign_boss_4 != null) {
			return ApplyFormService.getUserDeptByEmpNo(countersign_boss_4) + 
				"/" + countersign_boss_4 + " " + ApplyFormService.getUserNameByEmpNo(countersign_boss_4);
		}
		return "";
	}

	public void setCountersign_boss_4(String countersign_boss_4) {
		this.countersign_boss_4 = countersign_boss_4;
	}

	public String getCountersign_boss_4_date() {
		return countersign_boss_4_date;
	}

	public void setCountersign_boss_4_date(String countersign_boss_4_date) {
		this.countersign_boss_4_date = countersign_boss_4_date;
	}
	
	public String getClose_date() {
		return close_date;
	}

	public void setClose_date(String close_date) {
		this.close_date = close_date;
	}

	public String getChecker(){
		String checker = null;
		if(this.getStatus().equals("第一層主管審核")) {
			if(this.getApplicant_boss()!=null && !this.getApplicant_boss().equals("")){
				if(this.getApplicant_boss().equals(this.getLoginEmpNo()) || ApplyFormDeputyService.isDeputyByEmp(this.getApplicant_boss(), this.getLoginEmpNo())){
					checker = this.getApplicant_boss();
				}
			}
		}else
		if(this.getStatus().equals("第二層主管審核")) {
			if(this.getApplicant_dept_boss()!=null && !this.getApplicant_dept_boss().equals("")){
				if(this.getApplicant_dept_boss().equals(this.getLoginEmpNo()) || ApplyFormDeputyService.isDeputyByEmp(this.getApplicant_dept_boss(), this.getLoginEmpNo())){
					checker = this.getApplicant_dept_boss();
				}
			}
		}else
		if(this.getStatus().equals("平行簽核單位簽核")) {
			if(this.getCountersign_boss_1()!=null && !this.getCountersign_boss_1().equals("")){
				if(this.getCountersign_boss_1().equals(this.getLoginEmpNo()) || ApplyFormDeputyService.isDeputyByEmp(this.getCountersign_boss_1(), this.getLoginEmpNo())){
					checker = this.getCountersign_boss_1();
				}
			}
			if(this.getCountersign_boss_2()!=null && !this.getCountersign_boss_2().equals("")){
				if(this.getCountersign_boss_2().equals(this.getLoginEmpNo()) || ApplyFormDeputyService.isDeputyByEmp(this.getCountersign_boss_2(), this.getLoginEmpNo())){
					checker = this.getCountersign_boss_2();
				}
			}
			if(this.getCountersign_boss_3()!=null && !this.getCountersign_boss_3().equals("")){
				if(this.getCountersign_boss_3().equals(this.getLoginEmpNo()) || ApplyFormDeputyService.isDeputyByEmp(this.getCountersign_boss_3(), this.getLoginEmpNo())){
					checker = this.getCountersign_boss_3();
				}
			}
			if(this.getCountersign_boss_4()!=null && !this.getCountersign_boss_4().equals("")){
				if(this.getCountersign_boss_4().equals(this.getLoginEmpNo()) || ApplyFormDeputyService.isDeputyByEmp(this.getCountersign_boss_4(), this.getLoginEmpNo())){
					checker = this.getCountersign_boss_4();
				}
			}
		}			
		if (checker != null) {
			return ApplyFormService.getUserNameByEmpNo(checker);
		}
		return "";
	}
	
	public String getIs_agree() {
		return is_agree;
	}

	public void setIs_agree(String is_agree) {
		this.is_agree = is_agree;
	}

	/**
	 * 讀取簽核紀錄
	 * 
	 * @return
	 */
	public AplEventBean[] getLogEvents() {
		if (app_no == null) {
			return new AplEventBean[0];
		}
		AplEventDao dbDao = new AplEventDao();
		return dbDao.getEventsByAppNo(app_no);
	}
	
	public AplEventBean[] getSignRecordList() {
		return this.getLogEvents();
	}
	
	/**
	 * 存簽核資料
	 * @param con
	 * @throws Exception
	 */
	public void save(Connection con) throws Exception {
		save(con, "AP_APP_SIGN");
	}
	public void save(Connection con, String tableName) throws Exception {
		save(con, tableName, null);
	}
	
	/**
	 * 存簽核資料
	 * @param con
	 * @param tableName
	 * @param columns
	 * @throws Exception
	 */
	public void save(Connection con,String tableName, String[] columns) throws Exception {
		try {
			ArrayList val = new ArrayList();
			StringBuffer sb = new StringBuffer("select column_name, data_type");
			sb.append(" from user_tab_columns where table_name =? ");
			val.add(tableName);
			if (columns != null && columns.length > 0) {
				sb.append(" and column_name in (");
				for (int i = 0; i < columns.length; i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append("?");
					val.add(columns[i].toUpperCase());
				}
				sb.append(")");
			}
			sb.append(" order by column_id");

			HashMap[] coldef = GPRSDB.qryHashMapBySql(con, sb.toString(), val
					.toArray());
			if (coldef.length == 0) {
				throw new Exception("get table columns fail");
			}
			String colName = "";
			String dataType = "";
			java.lang.reflect.Method[] methods = this.getClass().getMethods();
			val = new ArrayList();
			sb = new StringBuffer("update ");
			sb.append(tableName).append(" set ");

			// 比對 DB Table 欄位與物件 method 名稱
			for (int i = 0; i < coldef.length; i++) {
				colName = coldef[i].get("COLUMN_NAME").toString();
				dataType = coldef[i].get("DATA_TYPE").toString();
				if (colName.equals("APP_NO")) {
					continue;
				}

				for (int j = 0; j < methods.length; j++) {
					if (methods[j].getName().startsWith("get")
							&& (methods[j].getName().substring(3)
									.compareToIgnoreCase(colName) == 0)) {
						if (val.size() > 0 || !sb.toString().endsWith(" set ")) {
							sb.append(",");
						}
						Object dataObj = methods[j].invoke(this, null);
						if (dataObj == null || dataObj.equals("")) {
							sb.append(colName).append(" = null");
							break;
						}
						if (dataType.equals("DATE")
								&& !methods[j].getReturnType().getName().equals("java.sql.Date")) {
							String dateStr = dataObj.toString();
							if (dateStr != null && dateStr.length() > 0) {
								if (dateStr.length() == 10) { // 日期格式 10 碼，應為 yyyy-MM-dd																
									sb.append(colName).append(" = to_date(?, 'yyyy-mm-dd')");
									val.add(dateStr);
								} else { // 日期格式不為 10 碼，應為 yyyy-MM-dd HH:mm:ss
									sb.append(colName).append(" = to_date(?, 'yyyy-mm-dd hh24:mi:ss')");
									val.add(dateStr.substring(0, 19));
								}
							} else { // 欄位為空
								sb.append(colName).append(" = null");
							}
						} else { // 字串或數字直接填入
							sb.append(colName).append(" = ?");
							//val.add(dataObj.toString().trim());
							val.add(dataObj.toString());
						}
						break;
					}
				} // end for j
			} // end for i

			// where 條件
			sb.append(" where app_no = ?");
			val.add(this.app_no);

			// System.out.println(sb.toString());
			GPRSDB.execDML(con, sb.toString(), (Object[]) val.toArray());
		} catch (Exception e) {
			TDSLogger.println(e);
			throw e;
		}
	}
	/**
	 * 
	 * @return
	 */
	public boolean load() {
		String sql = 
			"select a.initial_time, a.process_type, a.reason, b.*\n" +
			"from ap_app_master a, ap_app_sign b\n" + 
			"where a.app_no = b.app_no\n" + 
			"    and a.app_no = ?";

		return load(sql);
	}

	/**
	 * 設定頁面上的欄位值
	 * 
	 * @param sql
	 * @return
	 */
	public boolean load(String sql) {
		Connection con = null;
		boolean result = false;

		try {
			con = DBConnection.getConnection();
			if (sql == null)
				sql = "select * from AP_APP_SIGN where app_no = ?";
			java.sql.PreparedStatement ps = null;
			java.sql.ResultSet rs = null;
			ps = con.prepareStatement(sql);
			ps.setObject(1, this.app_no);
			rs = ps.executeQuery();
			if (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				String cloumnname = null;
				Object[] value = new Object[1];
				java.lang.reflect.Method[] methods = this.getClass()
						.getMethods();

				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					cloumnname = rsmd.getColumnName(i);
					value[0] = rs.getObject(i);

					for (int j = 0; j < methods.length; j++) {
						if ((methods[j].getName().startsWith("set"))
								&& (methods[j].getName().substring(3)
										.compareToIgnoreCase(cloumnname) == 0)) {
							if (value[0] != null
									&& !value[0].getClass().getName().equals(
											methods[j].getParameterTypes()[0]
													.getName())) {

								String str = value[0].toString();
								if (value[0].getClass().getName().equals("java.sql.Date")
										|| value[0].getClass().getName().equals("java.sql.Timestamp")) {
									Timestamp date = rs.getTimestamp(i);
									str = date.toString();
									if (str.length() > 10
											&& str.substring(11).equals("00:00:00.0")) {
										str = str.substring(0, 10);
									} else if (str.length() > 19) {
										str = str.substring(0, 19);
									}
								}
								value[0] = str;
							}
							methods[j].invoke(this, value);
						}
					}
				}
				this.is_agree = "";
				this.comments = "";
				result = true;

			}
		} catch (Exception e) {
			TDSLogger.println(e);
			DBConnection.rollback(con);
		} finally {
			DBConnection.close(con);
		}
		return result;
	}
	
	/**
	 * 設定登入使用者
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		com.mxic.oiplus.au.User user = (com.mxic.oiplus.au.User) request.getSession()
				.getAttribute("user");
		if (user != null) {
			this.loginEmpNo = user.getEmpNo();
			this.loginEmpName = user.getUserName();
		} else {
			this.loginEmpNo = "";
			this.loginEmpName = "";
		}
	}
}
