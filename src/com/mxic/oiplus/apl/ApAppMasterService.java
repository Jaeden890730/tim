package com.mxic.oiplus.apl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

import com.mxic.oiplus.apl.bean.APLUser;
import com.mxic.oiplus.apl.bean.ApplyForm;
import com.mxic.oiplus.eif.EifAFSyncAPLStatus;
import com.mxic.oiplus.resource.TDSResource;
import com.mxic.oiplus.util.SendMail;
import com.mxic.oiplus.util.StringUtil;
import com.mxic.oiplus.util.TDSLogger;

public class ApAppMasterService {
	
	/**
	 * 申請者送件
	 * @param con
	 * @param appNo
	 * @param nextSigner
	 * @throws Exception
	 */
	//public static void sendByApplicant(Connection con, String appNo, String nextSigner) throws Exception{
	public static void sendByApplicant(Connection con, String appNo) throws Exception{
		// 申請單已送件，可以拿到第一層主管名稱，APP Status = 會簽中
		ArrayList wheres2 = new ArrayList();
		//String sql = "UPDATE AP_APP_MASTER SET APP_STATUS = ?, PROCESS_USER = ?  WHERE APP_NO = ?";
		String sql = "UPDATE AP_APP_MASTER SET APP_STATUS = ? WHERE APP_NO = ?";
		
		wheres2.add("P");
		//wheres2.add(tp.getExecutorID());
		//wheres2.add(nextSigner);
		wheres2.add(appNo);
		int cnt = APLUtil.update(con, sql.toString(), wheres2.toArray());
	}
	
	/**
	 * 申請單退件
	 * @param con
	 * @param appNo
	 * @param empNo
	 * @throws Exception
	 */
	public static void doBackward(Connection con, String appNo) throws Exception {
		// 退件處理, 第一層主管退件, 第二層主管退件, 平行簽核人員退件
		// 申請單退件，APP Status = 退件中，是否退件或重送，由 user 在 jsp 中決定
		TDSLogger.println("Reject: " + appNo);
		String sql = "UPDATE AP_APP_MASTER SET ";
		// String formState = tp.getFormState().trim();
		ArrayList wheres2 = new ArrayList();
		// java.sql.Timestamp ts = new java.sql.Timestamp(tp.getStartTime());

		sql = sql + "APP_STATUS = ? ";
		wheres2.add("J");
		/*
		 * if (formState.equals("第一層主管退件")) { sql = sql + ",PROCESS_TIME = ? ";
		 * wheres2.add(ts); } else if (formState.equals("第二層主管退件")) { sql = sql
		 * + ",COMPLETE_TIME = ? "; wheres2.add(ts); }
		 */
		sql = sql + "WHERE APP_NO = ? ";
		wheres2.add(appNo);

		// update App Master Status to reJecting
		TDSLogger.println("Reject: Updating...");
		int cnt = APLUtil.update(con, sql.toString(), wheres2.toArray());
		TDSLogger.println("Reject: Update Completed! ");

		// sent Mail to notify applicant
		ApplyForm applyForm = ApplyForm.getApplyForm(con, appNo);
		String mailto = APLUser.getUserLoginByEmpNo(applyForm.getApplicant()
				.getEmpNo())
				+ "@mxic.com.tw";
		
		String contextPath = (String) TDSResource.getProperties("APL").get("server_ip_port_hr").toString().trim();
		ApplyFormSendMail sendMailObj = new ApplyFormSendMail();
		String  url = sendMailObj.getredirectUrl(appNo, contextPath);//更改為登入後進入表單
		url = sendMailObj.getAppIdUrl(url ,appNo);

		String mailfrom = (String) TDSResource.getProperties("APL").get(
				"mailfrom");
		
		String mailsubject = "APL 退件通知：APP_NO=" + appNo;
		Properties properties = TDSResource.getProperties("TDS");
		String testLetter = properties.getProperty("mailSubject");
		  if(testLetter ==null)
			  testLetter ="";
		  else
			  testLetter = StringUtil.unicodeToBig5(testLetter);
		mailsubject = testLetter + mailsubject;
		  
		String mailbody = "Information = \r\n "  
					+ "http://" 
					+ contextPath + "/Login/login.jsp?redirectUrl="
					+ "/apl/APLShow.do?appNo=" + appNo;
		TDSLogger.println("Reject: Send notify...");
		SendMail.send(mailto, mailfrom, mailsubject, mailbody,
				"退件通知 Notice：APP_NO=" + appNo, null);
	}

	/**
	 * Release
	 * @param con
	 * @param appNo
	 * @param empNo
	 * @throws Exception
	 */
	// just update APP_STATUS to "C" (Completed)
	// AP_APP_MASTER's trigger BF_UPDATE_AP_APP_MASTER() will change AP_APL
	// status
	// and move data to history
	//
	// Step 1: Set AP_APP_MASTER STATUS to C
	// Step 2: BF_UPDATE_AP_APP_MASTER will insert History Data
	// Step 3: Mail Notice to related users
	public static void doRelease(Connection con, String appNo, String processType) throws Exception{
		//所有平行簽核人員核准
		ArrayList wheres2 = new ArrayList();
		//java.sql.Timestamp ts = new java.sql.Timestamp(tp.getStartTime());
		ApplyForm applyForm = ApplyForm.getApplyForm(con, appNo);

		String sql = "UPDATE AP_APP_MASTER SET APP_STATUS = ? WHERE APP_NO = ?";
		wheres2.add("C");
		wheres2.add(appNo);
		int cnt = APLUtil.update(con, sql.toString(), wheres2.toArray());

		// APLUser[] users = APLUser.getNoticeList(conn, "ALL");
		APLUser[] users = APLUser.getNoticeList(con, EifAFSyncAPLStatus.getAPLType(appNo));
		String mailto = APLUser.getUserLoginByEmpNo(applyForm.getApplicant().getEmpNo())
				+ "@mxic.com.tw";
		for (int i = 0; i < users.length; i++) {
			mailto = mailto + "," + users[i].getEmail();
		}
		TDSLogger.println(appNo + " Mail to: " + mailto);
		String contextPath = (String) TDSResource.getProperties("APL").get("server_ip_port_hr").toString().trim();
		ApplyFormSendMail sendMailObj = new ApplyFormSendMail();
		String  url = sendMailObj.getredirectUrl(appNo, contextPath);//更改為登入後進入表單
		url = sendMailObj.getAppIdUrl(url ,appNo);
		
		String mailfrom = (String) TDSResource.getProperties("APL").get("mailfrom");
		String mailsubject = "APL 結案通知 (" + processType + ")：APP_NO=" + appNo;
		
		Properties properties = TDSResource.getProperties("TDS");
		String testLetter = properties.getProperty("mailSubject");
		  if(testLetter ==null)
			  testLetter ="";
		  else
			  testLetter = StringUtil.unicodeToBig5(testLetter);
		  mailsubject = testLetter + mailsubject;

		String mailbody = "Information = \r\n"
				+ "http://" 
				+ contextPath + "/Login/login.jsp?redirectUrl="
				+ "/apl/APLShow.do?appNo=" + appNo;
		SendMail.send(mailto, mailfrom, mailsubject, mailbody,
				"APL 結案通知：APP_NO=" + appNo, null);
	}
	
	public static void updateReason(Connection con, String appNo, String reason) throws Exception{
		String sql = "UPDATE AP_APP_MASTER SET REASON = ? WHERE APP_NO = ?";
		int cnt = APLUtil.update(con, sql.toString(), new Object[]{reason, appNo});
	}
}
