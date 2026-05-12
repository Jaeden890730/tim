package com.mxic.oiplus.apl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.mxic.gprs.applyform.ApplyFormService;
import com.mxic.oiplus.apl.applyform.ApplyFormDeputyService;
import com.mxic.gprs.applyform.ApplyFormUtil;
import com.mxic.gprs.applyform.Const;
import com.mxic.oiplus.apl.bean.AplEventBean;
import com.mxic.oiplus.resource.DBConnection;
import com.mxic.oiplus.util.GPRSDB;
import com.mxic.oiplus.util.TDSLogger;
 
public class APLAppSignAction extends DispatchAction{
	
	public ActionForward getSignRecordList(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		APLAppSignActionForm fm = (APLAppSignActionForm) actionForm;
		String forward = "signRecordList";
		Connection con = null;
	    try {
	    	con = DBConnection.getConnection();
	    	doFirst(request.getSession());
			doCheckAppId(request, fm);// 檢查單號有無
			doLoad(fm);// 由單號讀取資料
			doLast(request.getSession(), fm);
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:"
					+ e.getMessage().replaceAll("\n", ""));
			//if (forward.equals("apply"))
			//	forward = "fail";
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}
	
	/**
	 * 查詢
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward query(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		APLAppSignActionForm fm = (APLAppSignActionForm) actionForm;
		String forward = "sign";
		Connection con = null;
	    try {
	    	con = DBConnection.getConnection();
	    	doFirst(request.getSession());
			doCheckAppId(request, fm);// 檢查單號有無
			doLoad(fm);// 由單號讀取資料
			fm.setBtn(Const.BtnAction.QUERY.toString());
			getActBtn(fm);
			doLast(request.getSession(), fm);
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:"
					+ e.getMessage().replaceAll("\n", ""));
			//if (forward.equals("apply"))
			//	forward = "fail";
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}
	
	/**
	 * 存成草稿
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward saveAsDraft(ActionMapping actionMapping, ActionForm actionForm, 
			HttpServletRequest request,	HttpServletResponse response) {
		String forward = "sign";
		APLAppSignActionForm fm = (APLAppSignActionForm) actionForm;	 
		HashMap flow = new HashMap();
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			doFirst(request.getSession());//1.檢查USER 2.建立CONNECTION
			checkData(fm);//檢查FORM 資料
			doSave(con, flow, fm);// 儲存欄位
			doLoad(fm);//讀取表單
			getActBtn(fm);
			doLast(request.getSession(), fm);
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			//if (forward.equals("apply"))
			//	forward = "fail";
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}
	
	/**
	 * 送件
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward submit(ActionMapping actionMapping, ActionForm actionForm, 
			HttpServletRequest request,	HttpServletResponse response) {
		String forward = "sign";
		APLAppSignActionForm fm = (APLAppSignActionForm) actionForm;
		HashMap flow = new HashMap();
	    Connection con = null;
	    String isAgree = fm.getIs_agree();
	    try {
	    	con = DBConnection.getConnection();
			doFirst(request.getSession());//初期設定
			checkData(fm);// 檢查資料
			setSignTime(fm);//設定簽核時間
			flow = getNextRouteFlow(fm);//取得下站流程
			setStatus(flow, fm);//狀態切換
			doSave(con, flow, fm);// 儲存欄位
			setLogEvent(fm, flow);//儲存簽核記錄
			doLoad(fm);//讀取
			if(!(isAgree!=null && isAgree.equals("N"))){
				doSendMail(con, request.getServerName() + ":" +
						String.valueOf(request.getServerPort()) +
						request.getContextPath(), flow, fm);//寄mail
			}
			getActBtn(fm);
			doLast(request.getSession(), fm);//結束設定
			String message = "";
			if(fm.getStatus().equals("已完成")){
			}else{
				String nextStatus = flow.get("STATUS_A").toString();
				ArrayList al = new ArrayList();
				this.getMailSignerInTable(flow.get("MAIL_TO_LIST").toString(), al, fm);
				for(int i=0; i<al.size(); i++){
					message += "工作已送達[" + nextStatus + "]，執行者為[" + ApplyFormService.getUserNameByEmpNo(al.get(i).toString()) + "]。";
					if(i+1 < al.size()){
						message += "\\n";
					}
				}				
			}
			request.setAttribute("message", message);
		} catch (Exception e) {
			TDSLogger.println("submit().error - app_no: " + fm.getApp_no() + ", flow:" + flow);
			TDSLogger.println(e);
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			
			//if (forward.equals("apply"))
			//	forward = "fail";
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}
	
	public void setStatus(HashMap flow, APLAppSignActionForm fm){
		if(flow!=null &&flow.size()!=0){
			fm.setStatus(flow.get("STATUS_A")+"");
		}else{
			throw new RuntimeException("你沒有此權限簽核資料!");
		}
	}
	
	/**
	 * 取得下一站 flow
	 * @param fm
	 * @return
	 * @throws Exception
	 */
	public HashMap getNextRouteFlow(APLAppSignActionForm fm) throws Exception {
		HashMap flow = new HashMap();
		Connection con = null;
	    try {
	    	con = DBConnection.getConnection();
	    	flow = getNextRouteFlow(con, 
	    			fm.getStage(), 
	    			fm.getStatus(), 
	    			ApplyFormService.getFormName(this.getClass().getName()),//FORM
	    			getNextRouteCond(fm),//COND
	    			Const.BtnAction.SUBMIT.toString(),
	    			getNextRoutePerson(fm)//PERSON
					);
		} finally {
			DBConnection.close(con);
		}
		return flow;
	}

	public String getNextRouteCond(APLAppSignActionForm fm){
		String result="N/A";
		if(fm.getStatus().equals("第一層主管審核")) {
			result = fm.getIs_agree();
		}else
		if(fm.getStatus().equals("第二層主管審核")) {
			result = fm.getIs_agree();
		}else
		if(fm.getStatus().equals("平行簽核單位簽核")) {
			result = fm.getIs_agree();
		}			
		return result;
	}

	public void setSignTime(APLAppSignActionForm fm)throws Exception{
		if(fm.getIs_agree() == null || fm.getIs_agree().equals("")){
			if(fm.getStatus().equals("申請者填單")) {
				fm.setApplicant_date(ApplyFormUtil.getNowDate());
			}
		}else if(fm.getIs_agree() != null && fm.getIs_agree().equals("Y")){
			if(fm.getStatus().equals("第一層主管審核")) {
				fm.setApplicant_boss_date(ApplyFormUtil.getNowDate());
			}else
			if(fm.getStatus().equals("第二層主管審核")) {
				fm.setApplicant_dept_boss_date(ApplyFormUtil.getNowDate());
			}else
			if(fm.getStatus().equals("平行簽核單位簽核")) {
				if(fm.getCountersign_boss_1() != null && !fm.getCountersign_boss_1().equals("")){
					if(fm.getCountersign_boss_1().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_1(), fm.getLoginEmpNo()))
						fm.setCountersign_boss_1_date(ApplyFormUtil.getNowDate());
				}
				if(fm.getCountersign_boss_2() != null && !fm.getCountersign_boss_2().equals("")){
					if(fm.getCountersign_boss_2().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_2(), fm.getLoginEmpNo()))
						fm.setCountersign_boss_2_date(ApplyFormUtil.getNowDate());
				}
				if(fm.getCountersign_boss_3() != null && !fm.getCountersign_boss_3().equals("")){
					if(fm.getCountersign_boss_3().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_3(), fm.getLoginEmpNo()))
						fm.setCountersign_boss_3_date(ApplyFormUtil.getNowDate());
				}
				if(fm.getCountersign_boss_4() != null && !fm.getCountersign_boss_4().equals("")){
					if(fm.getCountersign_boss_4().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_4(), fm.getLoginEmpNo()))
						fm.setCountersign_boss_4_date(ApplyFormUtil.getNowDate());
				}
			}			
		}else if(fm.getIs_agree() != null && fm.getIs_agree().equals("N")){
			fm.setApplicant_date("");
			fm.setApplicant_boss_date("");
			fm.setApplicant_dept_boss_date("");
			fm.setCountersign_boss_1_date("");
			fm.setCountersign_boss_2_date("");
			fm.setCountersign_boss_3_date("");
			fm.setCountersign_boss_4_date("");			
			fm.setClose_date("");
		}
	}
		

	public void getActBtn(APLAppSignActionForm fm){
		Connection con = null;
		ArrayList btn = new ArrayList();
		ArrayList al = new ArrayList();
		String stage = fm.getStage(); 
		try {
			con = DBConnection.getConnection();
			if(stage==null ||stage.equals(""))
				stage="N/A";  
			//al.add("所有人員");
			//if(ApplyFormService.isAdminByEmpId(con,fm.getLoginEmpNo()))//是否為Admin
			//	al.add("Admin");
			
			if(fm.getLoginEmpNo().equals(fm.getApplicant())|| ApplyFormDeputyService.isDeputyByEmp(fm.getApplicant(), fm.getLoginEmpNo())){//是否為申請者
				al.add("申請者");
			}

			if(!getSignerInTable(al, fm))//去table中查找是否已有值
				 getCustomizeBtn(al, fm);//客製化

			btn = getActionBtn(con,	stage, fm.getStatus(),
					ApplyFormService.getFormName(this.getClass().getName()), al);
			
			if(fm.getStatus().equals("平行簽核單位簽核")){
				if(ApplyFormUtil.checkNull(fm.getCountersign_boss_1())){
					if((fm.getCountersign_boss_1().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_1(), fm.getLoginEmpNo())) 
						&& ApplyFormUtil.checkNull(fm.getCountersign_boss_1_date())){
						btn.clear();
					}
				}
				if(ApplyFormUtil.checkNull(fm.getCountersign_boss_2())){
					if((fm.getCountersign_boss_2().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_2(), fm.getLoginEmpNo()))
						&& ApplyFormUtil.checkNull(fm.getCountersign_boss_2_date())){
						btn.clear();
					}
				}
				if(ApplyFormUtil.checkNull(fm.getCountersign_boss_3())){
					if((fm.getCountersign_boss_3().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_3(), fm.getLoginEmpNo())) 
						&& ApplyFormUtil.checkNull(fm.getCountersign_boss_3_date())){
						btn.clear();
					}
				}
				if(ApplyFormUtil.checkNull(fm.getCountersign_boss_4())){
					if((fm.getCountersign_boss_4().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_4(), fm.getLoginEmpNo())) 
						&& ApplyFormUtil.checkNull(fm.getCountersign_boss_4_date())){
						btn.clear();
					}
				}
			}
			
		} catch (Exception e) {
			TDSLogger.println(e);
			btn.clear();
		}finally{
			DBConnection.close(con);
		}
		fm.setActBtn(btn);
	}
	
	/**
	 * 判斷table中的當站簽核人是否已經有值
	 * 有值則回TRUE，並將客製化值儲存至al中
	 * @return
	 */
	public boolean getSignerInTable(ArrayList al, APLAppSignActionForm fm){
		boolean result=false;
		if(fm.getStatus().equals("第一層主管審核")) {
			if(fm.getApplicant_boss()!=null && !fm.getApplicant_boss().equals("")){
				if(fm.getApplicant_boss().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getApplicant_boss(), fm.getLoginEmpNo())){
					result= true;
					al.add("第一層主管");
				}
			}
		}else
		if(fm.getStatus().equals("第二層主管審核")) {
			if(fm.getApplicant_dept_boss()!=null && !fm.getApplicant_dept_boss().equals("")){
				if(fm.getApplicant_dept_boss().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getApplicant_dept_boss(), fm.getLoginEmpNo())){
					result= true;
					al.add("第二層主管");
				}
			}
		}else
		if(fm.getStatus().equals("平行簽核單位簽核")) {
			if(fm.getCountersign_boss_1()!=null && !fm.getCountersign_boss_1().equals("")){
				if(fm.getCountersign_boss_1().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_1(), fm.getLoginEmpNo())){
					result= true;
					al.add("平行簽核人第一人");
				}
			}
			if(fm.getCountersign_boss_2()!=null && !fm.getCountersign_boss_2().equals("")){
				if(fm.getCountersign_boss_2().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_2(), fm.getLoginEmpNo())){
					result= true;
					al.add("平行簽核人第二人");
				}
			}
			if(fm.getCountersign_boss_3()!=null && !fm.getCountersign_boss_3().equals("")){
				if(fm.getCountersign_boss_3().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_3(), fm.getLoginEmpNo())){
					result= true;
					al.add("平行簽核人第三人");
				}
			}
			if(fm.getCountersign_boss_4()!=null && !fm.getCountersign_boss_4().equals("")){
				if(fm.getCountersign_boss_4().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_4(), fm.getLoginEmpNo())){
					result= true;
					al.add("平行簽核人第四人");
				}
			}			
		}		
		return result;
	}
	
	/**
	 * 由寄件者客制化條件取得已設定的寄件人
	 */
	public boolean getMailSignerInTable(String cond, ArrayList al, APLAppSignActionForm fm){
		boolean result=false;
		if(cond.equals("申請者")){			
			result= true;
			al.add(fm.getApplicant());
			ApplyFormDeputyService.addDeputyList(al,fm.getApplicant());
		}else
		if(cond.equals("第一層主管")){			
			result = true;
			al.add(fm.getApplicant_boss());
			ApplyFormDeputyService.addDeputyList(al,fm.getApplicant_boss());
		}else
		if(cond.equals("第二層主管")){			
			result = true;
			al.add(fm.getApplicant_dept_boss());
			ApplyFormDeputyService.addDeputyList(al,fm.getApplicant_dept_boss());
		}else 	
		if(cond.equals("全部平行簽核人")){			
			result = true;

			if(fm.getCountersign_boss_1()!=null && !fm.getCountersign_boss_1().equals("")){
			al.add(fm.getCountersign_boss_1());
				ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_1());
			}
			
			if(fm.getCountersign_boss_2()!=null && !fm.getCountersign_boss_2().equals("")){
				al.add(fm.getCountersign_boss_2());
				ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_2());
			}
			if(fm.getCountersign_boss_3()!=null && !fm.getCountersign_boss_3().equals("")){
				al.add(fm.getCountersign_boss_3());
				ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_3());
			}
			if(fm.getCountersign_boss_4()!=null && !fm.getCountersign_boss_4().equals("")){
				al.add(fm.getCountersign_boss_4());
				ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_4());
			}
		}else
		if(cond.equals("其他平行簽核人")){			
			result = true;
			if(fm.getCountersign_boss_1()!=null && !fm.getCountersign_boss_1().equals("")){
				if(fm.getCountersign_boss_1_date()==null || fm.getCountersign_boss_1_date().equals("")){
					al.add(fm.getCountersign_boss_1());
					ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_1());
			}
			}
			if(fm.getCountersign_boss_2()!=null && !fm.getCountersign_boss_2().equals("")){
				if(fm.getCountersign_boss_2_date()==null || fm.getCountersign_boss_2_date().equals("")){
					al.add(fm.getCountersign_boss_2());
					ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_2());
			}
			}
			if(fm.getCountersign_boss_3()!=null && !fm.getCountersign_boss_3().equals("")){
				if(fm.getCountersign_boss_3_date()==null || fm.getCountersign_boss_3_date().equals("")){
					al.add(fm.getCountersign_boss_3());
					ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_3());
			}
			}
			if(fm.getCountersign_boss_4()!=null && !fm.getCountersign_boss_4().equals("")){
				if(fm.getCountersign_boss_4_date()==null || fm.getCountersign_boss_4_date().equals("")){
					al.add(fm.getCountersign_boss_4());
					ApplyFormDeputyService.addDeputyList(al,fm.getCountersign_boss_4());
				}
			}
		}
		return result;
	}
	
	/**
	 * 針對不同的表當在table中無記錄簽核人的情況下
	 * 判斷此登入者是否符合當站簽核人狀況
	 * @param al
	 * @return
	 */
	public void getCustomizeBtn(ArrayList al, APLAppSignActionForm fm){
	
	}
	
	public String getNextRoutePerson(APLAppSignActionForm fm){
		String result="N/A";
		if(fm.getStatus().equals("申請者填單")) {
			result = "申請者";
		}else
		if(fm.getStatus().equals("第一層主管審核")) {
			result = "第一層主管";
		}else
		if(fm.getStatus().equals("第二層主管審核")) {
			result = "第二層主管";
		}else
		if(fm.getStatus().equals("平行簽核單位簽核")) {
			String countersign = "平行簽核人";
			if(fm.getCountersign_boss_1()!=null && !fm.getCountersign_boss_1().equals("")){
				if(fm.getCountersign_boss_1_date()==null || fm.getCountersign_boss_1_date().equals("")){
					countersign = "";
				}
				if(fm.getCountersign_boss_1().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_1(), fm.getLoginEmpNo())){
					result = "平行簽核人第一人";
				}
			}
			if(fm.getCountersign_boss_2()!=null && !fm.getCountersign_boss_2().equals("")){
				if(fm.getCountersign_boss_2_date()==null || fm.getCountersign_boss_2_date().equals("")){
					countersign = "";
				}
				if(fm.getCountersign_boss_2().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_2(), fm.getLoginEmpNo())){
					result = "平行簽核人第二人";
				}
			}
			if(fm.getCountersign_boss_3()!=null && !fm.getCountersign_boss_3().equals("")){
				if(fm.getCountersign_boss_3_date()==null || fm.getCountersign_boss_3_date().equals("")){
					countersign = "";
				}
				if(fm.getCountersign_boss_3().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_3(), fm.getLoginEmpNo())){
					result = "平行簽核人第三人";
				}
			}
			if(fm.getCountersign_boss_4()!=null && !fm.getCountersign_boss_4().equals("")){
				if(fm.getCountersign_boss_4_date()==null || fm.getCountersign_boss_4_date().equals("")){
					countersign = "";
				}
				if(fm.getCountersign_boss_4().equals(fm.getLoginEmpNo())|| ApplyFormDeputyService.isDeputyByEmp(fm.getCountersign_boss_4(), fm.getLoginEmpNo())){
					result = "平行簽核人第四人";
				}
			}
			if(!countersign.equals("")){
				result = countersign;
			}
		}	
		return result;
	}
	
	public void getCustomizeMailSigner(String cond, ArrayList al, APLAppSignActionForm fm){
	
	}
	
	void doFirst(HttpSession session) throws Exception {
		doCheckUser(session);
	}

	void doCheckUser(HttpSession session) throws Exception {
		if (!ApplyFormService.chkSessionUser(session)) {
			throw new Exception("No Session User");
		}
	}

	void doLoad(APLAppSignActionForm fm) throws Exception {
		if (fm.load() == false) {
			fm.setApp_no("");
			throw new Exception("沒有此張申請單");
		}
	}

	void doLast(HttpSession session, APLAppSignActionForm fm) throws Exception {
		//doCheckApplicant(session, fm);
	}
	  
	void doSave(Connection con, HashMap flow, APLAppSignActionForm fm) throws Exception {
		if(fm.getBtn().equals("送件")){
			if(fm.getIs_agree() == null || fm.getIs_agree().equals("")){	//申請者送件
				ApAppMasterService.sendByApplicant(con, fm.getApp_no());			
			}else if(fm.getIs_agree().equals("N")){	//退件
				ApAppMasterService.doBackward(con, fm.getApp_no());
			}else if(fm.getIs_agree().equals("Y")){	//所有平行簽核人員核准
				if(fm.getStatus().equals("已完成")){
					fm.setClose_date(ApplyFormUtil.getNowDate());
					ApAppMasterService.doRelease(con, fm.getApp_no(), fm.getProcess_type());
				}
			}
		}
		doSave(con, fm);
	}

	void doSave(Connection con, APLAppSignActionForm fm) throws Exception {
		fm.save(con);
		//ApAppMasterService.updateReason(con, fm.getApp_no(), fm.getReason());  --因為上面已經insert過reason了，再update一次會造成trigger BF_UPDATE_AP_APP_MASTER 失敗，表格AP_HIST_MASTER的主key: PK_AP_HIST_MASTER有問題
	}
	
	protected void setLogEvent(APLAppSignActionForm fm, HashMap flow)throws Exception{
		AplEventBean bean = new AplEventBean();
		bean.setApp_no(fm.getApp_no());
		//bean.setStatus(ApplyFormService.getLogEventMsg(flow));
		bean.setStatus(flow.get("STATUS_B").toString());
		bean.setComments(fm.getComments());
		String empno = fm.getLoginEmpNo();
		if(empno!=null && !empno.equals(""))
			bean.setUser_id(fm.getLoginEmpNo());
		else
			bean.setUser_id(fm.getLoginEmpName());
		bean.setIs_agree(fm.getIs_agree());
		AplEventDao dbDao = new AplEventDao();
		dbDao.insert(bean); 
	}
		


	public void doSendMail(Connection con, String contextPath, HashMap flow, APLAppSignActionForm fm) throws Exception{
		if(flow !=null && flow.size() > 0){
			TDSLogger.println("doSendMail() - flow: " + flow);
			if(flow.get("MAIL_TO_LIST") != null && !flow.get("MAIL_TO_LIST").toString().equals("")){
				ApplyFormSendMail sendMailObj = new ApplyFormSendMail();
				//set mailTo
				sendMailObj.setMailTo(getMailToList(flow.get("MAIL_TO_LIST").toString(), fm));
				
				//set mailCC
				if(flow.get("MAIL_TO_LIST_CC") != null && !flow.get("MAIL_TO_LIST_CC").toString().equals(""))
					sendMailObj.setMailCC(getMailCCList(flow.get("MAIL_TO_LIST_CC").toString(), fm));
				else
					sendMailObj.setMailCC("");
				
				//mail content
				sendMailObj.setMailSubject(getSubject(flow.get("EXECUTED_FUNCTION").toString(), flow, fm));
				sendMailObj.createMailBody(contextPath, fm.getApp_no(), con, getMailBody(flow, fm));
				
				//send Mail
				sendMailObj.sendMailCC();
				//sendMailObj.sendMail();
				//記錄 send mail log
				saveSendMailLogEven(con, sendMailObj, fm.getApp_no(), flow, fm);
			}
		}
	}
	
	/**
	 * 取得 Mail 內容
	 * @param flow
	 * @param fm
	 * @return
	 */
	protected String getMailBody(HashMap flow, APLAppSignActionForm fm){
		return "";
	}	
	
	/**
	 * 取得收件人清單
	 * @return
	 */
	protected String getMailToList(String MAIL_TO_LIST, APLAppSignActionForm fm){
		ArrayList empNoList = new ArrayList();
		String[] conds = MAIL_TO_LIST.split(";");
		for(int i=0; i<conds.length; i++){
			if(!this.getMailSignerInTable(conds[i], empNoList, fm))
				this.getCustomizeMailSigner(conds[i], empNoList, fm);
		}
		return getReceiverMailAddressList(empNoList);
	}	
	
	/**
	 * 
	 * @return
	 */
	protected String getMailCCList(String MAIL_TO_LIST_CC, APLAppSignActionForm fm){
		ArrayList empNoList = new ArrayList();
		String[] conds = MAIL_TO_LIST_CC.split(";");
		for(int i=0; i<conds.length; i++){
			if(!this.getMailSignerInTable(conds[i], empNoList, fm))
				this.getCustomizeMailSigner(conds[i], empNoList, fm);
		}
		return getReceiverMailAddressList(empNoList);
	}
	
	protected String getReceiverMailAddressList(ArrayList employeeNoList){		
		TDSLogger.println("getReceiverMailAddressList() - employeeNoList: " + employeeNoList);
		ApplyFormSendMail sendMailObj = new ApplyFormSendMail();
		String rcvrMailList = "";
		for(int i=0; i<employeeNoList.size(); i++){
			rcvrMailList += sendMailObj.getNoteId((String)employeeNoList.get(i));
			if(i<employeeNoList.size()-1){
				rcvrMailList +=",";
			}
		}
		TDSLogger.println("getReceiverMailAddressList() - rcvrMailList: " + rcvrMailList);
		return rcvrMailList;
	}	
	 
	protected String getSubject(String EXECUTED_FUNCTION, HashMap flow, APLAppSignActionForm fm){
		String subject = "";
		String issueType = "";
		if(fm.getApp_no().startsWith("A")){
			issueType = " - 生效申請：" + fm.getReason();
		}else if(fm.getApp_no().startsWith("R")){
			issueType = " - 復投申請：" + fm.getReason();
		}else if(fm.getApp_no().startsWith("H")){
			issueType = " - 停投申請：" + fm.getReason();
		}

		if(EXECUTED_FUNCTION.equals(Const.BtnAction.SUBMIT.toString()) 
				&& (flow != null && flow.get("STATUS_A").equals("已完成"))){				//結案
			//subject = "通知您，\"" + functionName + "\"已完成-請確認 (表單編號: " + fm.getApp_no() + ") ";
		}else if(EXECUTED_FUNCTION.equals(Const.BtnAction.SUBMIT.toString()) 
				&& (fm.getIs_agree().equals("") || fm.getIs_agree().equals("Y"))){		//送件
			subject = "WS/FT APL 簽核通知 - " + fm.getApp_no() + issueType;
		}else if(EXECUTED_FUNCTION.equals(Const.BtnAction.SUBMIT.toString())			//退件 
				&& fm.getIs_agree().equals("N")){		
			subject = "WS/FT APL 退件通知 - " + fm.getApp_no() + issueType;
		}
		
		return subject;
	}
	
	
	public void saveSendMailLogEven(Connection con, ApplyFormSendMail mobj, String appNo, HashMap flow, APLAppSignActionForm fm)throws Exception{
		if(mobj.getMailCC()==null)
			mobj.setMailCC("");
		String sql = "insert into AP_APP_MAILLIST(APP_NO, DOCSTAGE, STATUS, MAILTO, MAILCC, MAIL_TITLE, MAIL_DATE) " +
					" values(?,?,?,?,?,?,sysdate)";
		String status = "";
		if(flow.get("STATUS_B") != null && !flow.get("STATUS_B").toString().equals(""))
			status = flow.get("STATUS_B").toString();
		else
			status = fm.getStatus();
		
		GPRSDB.execDML(con, sql, 
			  new Object[]{appNo, fm.getStage(), status, mobj.getMailTo(), mobj.getMailCC(), mobj.getMailSubject()});
	}
	
	protected String getActionForwardByError(String message){
		String forward = "";
		if(message.equals("No Session User")){
			forward = "relogin";
		}else{
			forward="fail";
		}
		return forward;
	}
	
	void doCheckAppId(HttpServletRequest request, APLAppSignActionForm fm) throws Exception {
		if (!ApplyFormUtil.checkNull(fm.getApp_no())) {
			throw new Exception("沒有此張申請單");
		}
	}
	
	private ArrayList getActionBtn(Connection con, String stage, String status, 
			String form, ArrayList exe_person) throws Exception{
		String sql =
			"select distinct executed_function	\n" +
			"from apl_route_step_flow t		\n" + 
			"where t.docstage_b =?				\n" + 
			"   and t.status_b = ?				\n" + 
			"   and t.system_name = ?			\n" + 
			"   and t.form = ?					\n" ;

		ArrayList al = new ArrayList();
		al.add("N/A");
		al.add(status);
		al.add("APL");
		al.add(form);
		if(exe_person!=null && exe_person.size()!=0){
			sql += "   and t.executed_person in (" ;
			for(int index=0; index<exe_person.size(); index++){
				if(index>0)
					sql += ",";
				sql += "?";
				al.add(exe_person.get(index));//執行人員
			}
			sql += " )";
		}else{
			sql += "   and t.executed_person = '' ";
		}
		
		return GPRSDB.qryListBySql(con, sql,  al.toArray(new Object[]{}));
	}
	
	private static HashMap getNextRouteFlow(Connection con,String stage,String status,String form,String cond,String btn,String exe_person) throws Exception{
		HashMap[] hm =null; 
		HashMap result =new HashMap(); 
		String sql=
		"select distinct *\n" +
		"  from apl_route_step_flow t\n" + 
		" where t.docstage_b =?\n" + 
		"   and t.status_b = ?\n" + 
		"   and t.system_name = ?\n" + 
		"   and t.form = ?\n" + 
		"   and t.executed_person = ?\n" ;
		ArrayList al = new ArrayList();
		//al.add(stage);
		al.add("N/A");
		al.add(status);
		al.add("APL");
		al.add(form);
		al.add(exe_person);//執行人員
		//按鈕事件
		if(!btn.equals("")){
			sql+="   and t.executed_function=?";
			al.add(btn);
		}
		//送件流程條件
		if(!cond.equals("")){
			sql+="   and t.modify_cond=?";
			al.add(cond);
		}
		hm = GPRSDB.qryHashMapBySql(con, sql, al.toArray(new Object[]{}));
		if(hm.length>0){
			result = hm[0];
		}
		return result;
	}
	
	private void checkData(APLAppSignActionForm fm)throws Exception{
		TDSLogger.println("checkData() - web page status: " + fm.getStatus());
		
		APLAppSignActionForm appSignOriBean = new APLAppSignActionForm();
		appSignOriBean.setApp_no(fm.getApp_no());
		appSignOriBean.load();
		if(appSignOriBean!=null && ApplyFormUtil.checkNull(appSignOriBean.getStatus())){
			if(!appSignOriBean.getStatus().equals(fm.getStatus()))
				throw new Exception("現在申請單狀態[" + fm.getStatus() + "]與系統狀態[" + appSignOriBean.getStatus() + "]不符, 請重新整理頁面!");
		}
	}
}
