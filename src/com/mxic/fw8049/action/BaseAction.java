package com.mxic.fw8049.action;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.DispatchAction;

import com.mxic.fw8049.bean.Fw8049mationBean;
import com.mxic.fw8049.dao.Fw8049mationDao;
import com.mxic.oi8040.dao.TgCPFlowTxDao;
import com.mxic.oi8040.dao.TgFTFlowTxDao;
import com.mxic.oi8040.service.ApplyFormService;
import com.mxic.stoptest.util.StringUtil;

public class BaseAction extends DispatchAction {
	
	void doCheckUser(ActionForm actionForm, HttpSession session) throws Exception {
		if (!ApplyFormService.chkSessionUser(session)) {
			//forward = "relogin";
			throw new Exception("No Session User");
		}
		BaseActionForm fm = (BaseActionForm)actionForm;
		if(!StringUtil.isNull(fm.getSid())){
			fm.setIsAuthority(Fw8049mationDao.isEditUser(fm.getSid(), fm.getLoginUserName()));
		}
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
	
//	public ActionForward oiMaintainProductMain(ActionMapping actionMapping, ActionForm actionForm, 
//			HttpServletRequest request,	HttpServletResponse respone) {
//		String forward = "product_maintain_main";
//		BaseActionForm fm = (BaseActionForm)actionForm;
//		Connection con = null;
//		try {
//			con = DBConnection.getConnection();
//			doCheckUser(actionForm, request.getSession());
//			doLoad(con, fm);
//			
//			if(fm.getStatus().equals("R")){
//				forward = "product_maintain_view";
//			}
//		} catch (Exception e) {
//			TDSLogger.println(e);
//			request.setAttribute("message", "Error:"+ e.getMessage().replaceAll("\n", ""));
//			forward = this.getActionForwardByError(e.getMessage());
//		} finally {
//			DBConnection.close(con);
//		}
//		return actionMapping.findForward(forward);				
//	}
//	
//	public ActionForward updateSponsor(ActionMapping actionMapping, ActionForm actionForm, 
//			HttpServletRequest request,	HttpServletResponse respone) {
//		String forward = "product_maintain_sponsor";
//		BaseActionForm fm = (BaseActionForm)actionForm;
//		Connection con = null;
//		try {
//			con = DBConnection.getConnection();
//			doCheckUser(actionForm, request.getSession());
//			TgInformationDao.updateSponsor(con, fm.getSid(), fm.getSponsor_1(), fm.getSponsor_2());
//		} catch (Exception e) {
//			TDSLogger.println(e);
//			request.setAttribute("message", "Error:"+ e.getMessage().replaceAll("\n", ""));
//			forward = this.getActionForwardByError(e.getMessage());
//		} finally {
//			DBConnection.close(con);
//		}
//		return actionMapping.findForward(forward);				
//	}
	
	protected void doLoad(Connection con, BaseActionForm fm){
		Fw8049mationBean bean = Fw8049mationDao.query(fm.getSid());
		fm.setProduct_body(bean.getProduct_body());
		fm.setCustomer_no(bean.getCustomer_no()!=null && bean.getCustomer_no().equals("NA")?"":bean.getCustomer_no());
		fm.setVersion(bean.getVersion());
		fm.setStatus(bean.getStatus());		
		fm.setBrand(bean.getBrand());		
		fm.setCreator(bean.getCreator());
		
//		if(TgCPFlowTxDao.finishGroupingCount(con, fm.getSid()) > 0)
//			fm.setIsRunCPGrouping(true);
//		if(TgFTFlowTxDao.finishGroupingCount(con, fm.getSid()) > 0)
//			fm.setIsRunFTGrouping(true);
	}
}
