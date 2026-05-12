package com.mxic.oiplus.eif;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;

import com.mxic.gprs.applyform.Const;
import com.mxic.oiplus.apl.APLAppSignActionForm;
import com.mxic.oiplus.apl.ApplyFormSendMail;
import com.mxic.oiplus.apl.bean.APLUser;
import com.mxic.oiplus.apl.bean.ApplyForm;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.SendMail;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.oiplus.util.TDSLogger;
import com.mxic.stoptest.util.DBUtil;

public class EifAPLSignSendEmailAlarm {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EifAPLSignSendEmailAlarm aplalarm = new EifAPLSignSendEmailAlarm();
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			aplalarm.process(con);
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			DBConnection.close(con);
		}
	}

	public void process(Connection con) {
		HashMap[] hmExecuting = null;
		try {
			hmExecuting = null;
			hmExecuting = this.getExeData(con);

			if (hmExecuting != null && hmExecuting.length > 0) {

				String mailfrom = (String) TDSResource.getProperties("APL").get("mailfrom");
				String contextPath = (String) TDSResource.getProperties("APL").get("server_ip_port_hr").toString().trim();

				for (int i = 0; i < hmExecuting.length; i++) {
					String appNo = hmExecuting[i].get("APP_NO") + "";

					ApplyFormSendMail sendMailObj = new ApplyFormSendMail();

					sendMailObj.setMailTo(APLUser.getUserLoginByEmpNo(hmExecuting[i].get("SIGNNER") + "") + "@mxic.com.tw");
					sendMailObj.setMailCC((String) TDSResource.getProperties("APL").get("apl_alarm_mailcc"));  // apl_alarm_mailcc					
					sendMailObj.setMailSubject(getSubject(appNo));
					sendMailObj.createMailBody(contextPath, appNo, con, "");
					sendMailObj.sendMailCC();

				}
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		} finally {
			// DBConnection.close(con);
		}
	}
	
	private HashMap[] getExeData(Connection con){
		HashMap[] hm = null;
		try{
			String sql = 
                    "SELECT A.APP_NO,\n" +
                    "       A.STATUS,\n" + 
                    "       A.APPLICANT    AS SIGNNER,\n" + 
                    "       B.INITIAL_TIME AS L_SIGN_DATE\n" + 
                    "  FROM AP_APP_SIGN A, AP_APP_MASTER B\n" + 
                    " WHERE A.STATUS = '申請者填單'\n" + 
                    "   AND A.APP_NO = B.APP_NO\n" + 
                    "   AND TO_DATE(TO_CHAR(B.INITIAL_TIME, 'dd-mm-yyyy hh24:mi:ss'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss') <\n" + 
                    "       TO_DATE((TO_CHAR(SYSDATE - 1, 'dd-mm-yyyy') || ' 23:59:59'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss')\n" + 
                    "UNION ALL\n" + 
                    "SELECT A.APP_NO,\n" + 
                    "       A.STATUS,\n" + 
                    "       A.APPLICANT_BOSS AS SIGNNER,\n" + 
                    "       A.APPLICANT_DATE AS L_SIGN_DATE\n" + 
                    "  FROM TIM.AP_APP_SIGN A\n" + 
                    " WHERE A.STATUS = '第一層主管審核'\n" + 
                    "   AND TO_DATE(TO_CHAR(A.APPLICANT_DATE, 'dd-mm-yyyy hh24:mi:ss'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss') <\n" + 
                    "       TO_DATE((TO_CHAR(SYSDATE - 1, 'dd-mm-yyyy') || ' 23:59:59'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss')\n" + 
                    "UNION ALL\n" + 
                    "SELECT A.APP_NO,\n" + 
                    "       A.STATUS,\n" + 
                    "       A.APPLICANT_DEPT_BOSS AS SIGNNER,\n" + 
                    "       A.APPLICANT_BOSS_DATE AS L_SIGN_DATE\n" + 
                    "  FROM TIM.AP_APP_SIGN A\n" + 
                    " WHERE A.STATUS = '第二層主管審核'\n" + 
                    "   AND TO_DATE(TO_CHAR(A.APPLICANT_BOSS_DATE, 'dd-mm-yyyy hh24:mi:ss'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss') <\n" + 
                    "       TO_DATE((TO_CHAR(SYSDATE - 1, 'dd-mm-yyyy') || ' 23:59:59'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss')\n" + 
                    "UNION ALL\n" + 
                    "SELECT A.APP_NO,\n" + 
                    "       A.STATUS,\n" + 
                    "       A.COUNTERSIGN_BOSS_1       AS SIGNNER,\n" + 
                    "       A.APPLICANT_DEPT_BOSS_DATE AS L_SIGN_DATE\n" + 
                    "  FROM TIM.AP_APP_SIGN A\n" + 
                    " WHERE A.STATUS = '平行簽核單位簽核'\n" + 
                    "   AND A.COUNTERSIGN_BOSS_1_DATE IS NULL AND A.COUNTERSIGN_BOSS_1 IS NOT NULL  \n" + 
                    "   AND TO_DATE(TO_CHAR(A.APPLICANT_DEPT_BOSS_DATE, 'dd-mm-yyyy hh24:mi:ss'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss') <\n" + 
                    "       TO_DATE((TO_CHAR(SYSDATE - 1, 'dd-mm-yyyy') || ' 23:59:59'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss')\n" + 
                    "UNION ALL\n" + 
                    "SELECT A.APP_NO,\n" + 
                    "       A.STATUS,\n" + 
                    "       A.COUNTERSIGN_BOSS_2 AS SIGNNER,\n" + 
                    "       A.APPLICANT_DEPT_BOSS_DATE AS L_SIGN_DATE\n" + 
                    "  FROM TIM.AP_APP_SIGN A\n" + 
                    " WHERE A.STATUS = '平行簽核單位簽核'\n" + 
                    "   AND A.COUNTERSIGN_BOSS_2_DATE IS NULL AND A.COUNTERSIGN_BOSS_2 IS NOT NULL \n" + 
                    "   AND TO_DATE(TO_CHAR(A.APPLICANT_DEPT_BOSS_DATE, 'dd-mm-yyyy hh24:mi:ss'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss') <\n" + 
                    "       TO_DATE((TO_CHAR(SYSDATE - 1, 'dd-mm-yyyy') || ' 23:59:59'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss')\n" + 
                    "UNION ALL\n" + 
                    "SELECT A.APP_NO,\n" + 
                    "       A.STATUS,\n" + 
                    "       A.COUNTERSIGN_BOSS_3 AS SIGNNER,\n" + 
                    "       A.APPLICANT_DEPT_BOSS_DATE AS L_SIGN_DATE\n" + 
                    "  FROM TIM.AP_APP_SIGN A\n" + 
                    " WHERE A.STATUS = '平行簽核單位簽核'\n" + 
                    "   AND A.COUNTERSIGN_BOSS_3_DATE IS NULL AND A.COUNTERSIGN_BOSS_3 IS NOT NULL \n" + 
                    "   AND TO_DATE(TO_CHAR(A.APPLICANT_DEPT_BOSS_DATE, 'dd-mm-yyyy hh24:mi:ss'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss') <\n" + 
                    "       TO_DATE((TO_CHAR(SYSDATE - 1, 'dd-mm-yyyy') || ' 23:59:59'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss')\n" + 
                    "UNION ALL\n" + 
                    "SELECT A.APP_NO,\n" + 
                    "       A.STATUS,\n" + 
                    "       A.COUNTERSIGN_BOSS_4 AS SIGNNER,\n" + 
                    "       A.APPLICANT_DEPT_BOSS_DATE AS L_SIGN_DATE\n" + 
                    "  FROM TIM.AP_APP_SIGN A\n" + 
                    " WHERE A.STATUS = '平行簽核單位簽核'\n" + 
                    "   AND A.COUNTERSIGN_BOSS_4_DATE IS NULL AND A.COUNTERSIGN_BOSS_4 IS NOT NULL \n" + 
                    "   AND TO_DATE(TO_CHAR(A.APPLICANT_DEPT_BOSS_DATE, 'dd-mm-yyyy hh24:mi:ss'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss') <\n" + 
                    "       TO_DATE((TO_CHAR(SYSDATE - 1, 'dd-mm-yyyy') || ' 23:59:59'),\n" + 
                    "               'dd-mm-yyyy hh24:mi:ss')";
			hm = DBUtil.qryHashMapBySql(con, sql, new Object[]{});
		} catch (Exception e) {
			TDSLogger.println(e);
		}finally{
			//DBConnection.close(con);
		}
		return hm;
	}
	
	
	String getSubject(String appNo) {
		String testLetter = TDSResource.getProperties("TDS").getProperty("mailSubject");
		if (testLetter == null)
			testLetter = "";
		else
			testLetter = StringUtil.unicodeToBig5(testLetter);
		String subject = "";
		String issueType = "";
		if (appNo.startsWith("A")) {
			issueType = " - 生效申請";
		} else if (appNo.startsWith("R")) {
			issueType = " - 復投申請";
		} else if (appNo.startsWith("H")) {
			issueType = " - 停投申請";
		}
		subject = testLetter + "WS/FT APL 催簽簽核通知 - " + appNo + issueType;
		return subject;
	}
}
