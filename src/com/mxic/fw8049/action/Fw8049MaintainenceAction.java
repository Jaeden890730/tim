package com.mxic.fw8049.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.fw8049.bean.CompareResultBean;
import com.mxic.fw8049.bean.FwBasicInfoTxBean;
import com.mxic.fw8049.dao.Fw8049mationDao;
import com.mxic.oi8040.resource.DBConnection;
import com.mxic.oiplus.au.User;
import com.mxic.tdsplus.util.TDSLogger;

public class Fw8049MaintainenceAction extends BaseAction {

	public ActionForward getList(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {
		BaseActionForm fm = (BaseActionForm) actionForm;
		String forward = "main";
		Connection con = null;
		try {
			con = DBConnection.getConnection();
			doCheckUser(actionForm, request.getSession());
			doLoad(con, fm);

			Fw8049mationDao dao = new Fw8049mationDao();

			List<CompareResultBean> resultList = Fw8049mationDao
					.getCompareList(fm.getProduct_body());

			request.setAttribute("resultList", resultList);
			if (fm.getStatus().equals("R") || fm.getStatus().equals("A")) {
				forward = "main_view";
			}
		} catch (Exception e) {
			TDSLogger.println(e);
			request.setAttribute("message", "Error:"
					+ e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(con);
		}

		return actionMapping.findForward(forward);
	}

	public ActionForward save(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {

		Connection conn = null;
		String forward = "main";
		BaseActionForm fm = (BaseActionForm) actionForm;
		Fw8049mationDao dao = new Fw8049mationDao();

		try {
			doCheckUser(actionForm, request.getSession());
			User user = (User) request.getSession().getAttribute("user");
			String userName = user.getUserName();

			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			String[] productCodeList = request
					.getParameterValues("productCode");
			String[] formNoList = request.getParameterValues("formNo");
			String[] reasonList = request.getParameterValues("reason");
			String[] contentList = request.getParameterValues("content");
			String[] pendingList = request.getParameterValues("pending");

			if (productCodeList != null) {
				for (int i = 0; i < productCodeList.length; i++) {
					FwBasicInfoTxBean bean = new FwBasicInfoTxBean();

					bean.setProductCode(getArrayValue(productCodeList, i));
					bean.setFormNo(getArrayValue(formNoList, i));
					bean.setReason(getArrayValue(reasonList, i));
					bean.setContent(getArrayValue(contentList, i));
					bean.setPending(getArrayValue(pendingList, i));

					dao.mergeFwBasicInfoTx(conn, bean, userName);
				}
			}

			conn.commit();

			request.setAttribute("msg", "儲存成功");

			Map<String, FwBasicInfoTxBean> dataMap = dao
					.getFwBasicInfoTxByProductCode(conn, fm.getProduct_body());

			List<CompareResultBean> resultList = Fw8049mationDao
					.getCompareList(fm.getProduct_body());

			request.setAttribute("resultList", resultList);
			request.setAttribute("dataMap", dataMap);

		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					TDSLogger.println("conn.rollback fail：" + e1);
				}
			}
			TDSLogger.println(e);
			request.setAttribute("msg",
					"Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(conn);
		}
		return actionMapping.findForward(forward);

	}

	private String getArrayValue(String[] arr, int index) {
		if (arr == null) {
			return "";
		}
		if (index < 0 || index >= arr.length) {
			return "";
		}
		if (arr[index] == null) {
			return "";
		}
		return arr[index].trim();
	}

	public ActionForward submit(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse respone) {

		Connection conn = null;
		String forward = "main";
		BaseActionForm fm = (BaseActionForm) actionForm;
		Fw8049mationDao dao = new Fw8049mationDao();

		try {
			doCheckUser(actionForm, request.getSession());
			User user = (User) request.getSession().getAttribute("user");
			String userName = user.getUserName();

			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			String[] productCodeList = request
					.getParameterValues("productCode");
			String[] formNoList = request.getParameterValues("formNo");
			String[] reasonList = request.getParameterValues("reason");
			String[] contentList = request.getParameterValues("content");
			String[] pendingList = request.getParameterValues("pending");

			if (productCodeList != null) {
				for (int i = 0; i < productCodeList.length; i++) {
					FwBasicInfoTxBean bean = new FwBasicInfoTxBean();

					bean.setProductCode(getArrayValue(productCodeList, i));
					bean.setFormNo(getArrayValue(formNoList, i));
					bean.setReason(getArrayValue(reasonList, i));
					bean.setContent(getArrayValue(contentList, i));
					bean.setPending(getArrayValue(pendingList, i));

					dao.mergeFwBasicInfo(conn, bean, userName);
				}
			}

			conn.commit();

			request.setAttribute("msg", "儲存成功");

			Map<String, FwBasicInfoTxBean> dataMap = dao
					.getFwBasicInfoTxByProductCode(conn, fm.getProduct_body());

			List<CompareResultBean> resultList = Fw8049mationDao
					.getCompareList(fm.getProduct_body());

			request.setAttribute("resultList", resultList);
			request.setAttribute("dataMap", dataMap);

		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					TDSLogger.println("conn.rollback fail：" + e1);
				}
			}
			TDSLogger.println(e);
			request.setAttribute("msg",
					"Error:" + e.getMessage().replaceAll("\n", ""));
			forward = this.getActionForwardByError(e.getMessage());
		} finally {
			DBConnection.close(conn);
		}
		return actionMapping.findForward(forward);

	}
}