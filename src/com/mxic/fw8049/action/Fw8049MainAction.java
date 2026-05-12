package com.mxic.fw8049.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.fw8049.dao.Fw8049mationDao;
import com.mxic.fw8049.pdf.MakeCoverPage1;
import com.mxic.fw8049.pdf.MakePDFTx;
import com.mxic.oiplus.au.User;
import com.mxic.oiplus.common.TF_OI_GENLIST;
import com.mxic.oiplus.mms.oimaintain.FTService;
import com.mxic.oiplus.mms.oimaintain.FTTestActionForm;
import com.mxic.oiplus.util.DateUtil;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.util.TDSLogger;

public class Fw8049MainAction extends BaseAction {

	public ActionForward getList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {
		Fw8049MainActionForm fm = (Fw8049MainActionForm) actionForm;
		String forward = "fw8049_main";
		try {
			doCheckUser(actionForm, request.getSession());
			request.setAttribute("list", fm.getListByInfo());
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		}

		return actionMapping.findForward(forward);
	}

	public ActionForward fw8049MaintainProductMain(ActionMapping actionMapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse respone) {
		String forward = "fw8049_maintain_main";
		BaseActionForm fm = (BaseActionForm) actionForm;
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			doCheckUser(actionForm, request.getSession());
			doLoad(con, fm);

			if (fm.getStatus().equals("R")) {
				forward = "fw8049_maintain_view";
			}
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}

	public ActionForward oiSearch(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {
		BaseActionForm fm = (BaseActionForm) actionForm;
		String forward = "fw8049_oi_search";
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			doCheckUser(actionForm, request.getSession());
			request.setAttribute("list",
					Fw8049mationDao.getListByOiSearch(fm.getLoginUserName(), fm.getProduct_body()));
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}

	public ActionForward fwInsert(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {
		BaseActionForm fm = (BaseActionForm) actionForm;
		String forward = "fw8049_search";
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			doCheckUser(actionForm, request.getSession());
			List<Fw8049MainActionForm> result = Fw8049mationDao.selectList(fm.getSid(), fm.getProduct_body(),
					fm.getBrand());
			if (result == null || result.isEmpty()) {
				Fw8049MainActionForm bean2 = new Fw8049MainActionForm();
				bean2.setProduct_body(fm.getProduct_body());
				bean2.setBrand(fm.getBrand());
				bean2.setVersion("0");
				ArrayList list = new ArrayList();
				list.add(bean2);
				request.setAttribute("list", list);
				return actionMapping.findForward("add_new_version");
			}else {
				Fw8049MainActionForm bean2 = new Fw8049MainActionForm();
				bean2.setProduct_body(fm.getProduct_body());
				bean2.setBrand(fm.getBrand());
				bean2.setVersion(result.get(0).getVersion());
				ArrayList list = new ArrayList();
				list.add(bean2);
				request.setAttribute("list", list);
				return actionMapping.findForward("add_new_version");
			}
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}

	public ActionForward add_new_version(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {
		Fw8049MainActionForm fm = (Fw8049MainActionForm) actionForm;
		String forward = "new_version_success";
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			con.setAutoCommit(false);
			
			String message = "";
			doCheckUser(actionForm, request.getSession());
			
			Fw8049mationDao.updateFlag(con, fm);
			
			boolean flag = Fw8049mationDao.insert(con, fm);
			if (flag == true) {
				con.commit();
				message = "您成功的新增一筆〝" + fm.getProduct_body() + "〞";
				request.setAttribute("message", message);
				request.setAttribute("list", fm.getListByInfo());
				return actionMapping.findForward("new_version_success");
			} else {
				con.rollback();
				message = "新增〝" + fm.getProduct_body() + "〞這筆資料是失敗的，麻煩重新一次";
				request.setAttribute("message", message);
				request.setAttribute("list", fm.getListByInfo());
				return actionMapping.findForward("new_version_success");
			}
		} catch (Exception e) {
			TDSLogger.println(e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				TDSLogger.println(e1);
			}
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}

	public ActionForward PDFList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {
		User user = (User) request.getSession().getAttribute("user");
		/* 文件管理 - 文件列表 PDF */
		TF_OI_GENLIST tfoigen = new TF_OI_GENLIST();
		java.util.Date d1 = new java.util.Date();

		Fw8049MainActionForm fm = (Fw8049MainActionForm) actionForm;

		TDSLogger.println("PDFList_start");
		MakePDFTx.makePDF(fm);
		MakeCoverPage1.makePDFCoverPage(fm, null, fm.getStatus());
//		FTTestActionForm[] vl = FTService.getVendorList(fm.getSid());
//		for (int i = 0; i < vl.length; i++) {
//			fm.setVendor(vl[i].getPlant_name());
//			MakeVendorPDF.makePDF(fm, "P");
//		}
		System.out.println("結束：" + DateUtil.getNow());
		java.util.Date d2 = new java.util.Date();
		System.out.println("花費：" + DateUtil.getDiffSecond(d2, d1));
		TDSLogger.println("PDFList_success");
		tfoigen.insertData(user.getEmpNo(), fm.getSid(), fm.getProduct_body(), fm.getVersion());
		request.setAttribute("sid", fm.getSid());
		request.setAttribute("productbody", fm.getProduct_body());
		request.setAttribute("brand", fm.getBrand());

		return actionMapping.findForward("success");
	}
	
	public ActionForward update_data(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {
		Fw8049MainActionForm fm = (Fw8049MainActionForm) actionForm;
		String forward = "update_data";
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			con.setAutoCommit(false);
			
			String message = "";
			doCheckUser(actionForm, request.getSession());
			
			boolean flag = Fw8049mationDao.updateData(con, fm);
			if (flag == true) {
				con.commit();
				message = "您成功的更新一筆〝" + fm.getProduct_body() + "〞";
				request.setAttribute("message", message);
				request.setAttribute("list", fm.getListByInfo());
				return actionMapping.findForward(forward);
			} else {
				con.rollback();
				message = "新增〝" + fm.getProduct_body() + "〞這筆資料是失敗的，麻煩重新一次";
				request.setAttribute("message", message);
				request.setAttribute("list", fm.getListByInfo());
				return actionMapping.findForward(forward);
			}
		} catch (Exception e) {
			TDSLogger.println(e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				TDSLogger.println(e1);
			}
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}
		return actionMapping.findForward(forward);
	}
	
}