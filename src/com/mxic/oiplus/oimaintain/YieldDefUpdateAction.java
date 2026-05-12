package com.mxic.oiplus.oimaintain;

import java.util.HashMap;

import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.mxic.oiplus.util.TDSLogger;

public class YieldDefUpdateAction extends Action {
	public ActionForward execute(ActionMapping actionMapping,
			                     ActionForm actionForm,
			                     HttpServletRequest Request,
			                     HttpServletResponse servletResponse) {

		ProTestRouteBeanAF fm = (ProTestRouteBeanAF) actionForm;
		String sid= Request.getParameter("sid");
		String brand=Request.getParameter("brand");  //Add by Ken for copyto function, add brand key to query bin data. 
		String save_submit= Request.getParameter("save_submit");
		String proc_type= Request.getParameter("proc_type");
		String delete_list= Request.getParameter("yidlist");
		String nvm_ws_new= Request.getParameter("nvm_ws_new");  //這個設成Y是整個資料庫刪除，在INSERT進去，是NVM_WS新的寫法，之後都套這個 20190201 KEN

		String forward = null;
		String msg = "";
		String msg_alarm = "";

		forward = "YieldDef"+proc_type;
		if (save_submit.equals("reset")){
			YieldDefService.reset(sid, proc_type);
		} else {
			String yid[]=Request.getParameterValues("yid");
			String product_code[]=Request.getParameterValues("product_code");
			String brands[]=Request.getParameterValues("brands");
			String test_mode[]=Request.getParameterValues("test_mode");
			String lower_limit[]=Request.getParameterValues("lower_limit");
			String flag1[]=Request.getParameterValues("flag1");
			String flag2[]=Request.getParameterValues("flag2");
			String upper_limit[]=Request.getParameterValues("upper_limit");
			String item[]=Request.getParameterValues("item");
			String item_type[]=Request.getParameterValues("item_type");
			String item_mode2[]=Request.getParameterValues("item_mode2");
			String item_bins2[]=Request.getParameterValues("item_bins2");
			String action[]=Request.getParameterValues("action");
			String dg_action[]=Request.getParameterValues("dg_action");
			String txtActionSeq[]=Request.getParameterValues("txtActionSeq");
			String change_ipn[]=Request.getParameterValues("change_ipn");
			String route_name[]=Request.getParameterValues("route_name");
			String start_step[]=Request.getParameterValues("start_step");
			String by_lot_dg[]=Request.getParameterValues("by_lot_dg");
			String dgrade_special_ipn[]=Request.getParameterValues("dgrade_special_ipn");
			String dgradeprodcode[]=Request.getParameterValues("dgradeprodcode");
			String remark[]=Request.getParameterValues("remark");
			String groupitems_no[]=Request.getParameterValues("groupitems_no");
			String item_seq[]=Request.getParameterValues("item_seq");
			
			//if(nvm_ws_new != null && nvm_ws_new.equals("YES")){
				//YieldDefService.Delete(sid, proc_type); //lai-mark-20131209
			//}
			if (yid != null) { // not all rows deleted
			// 	for BAKE/UV Steps
				HashMap<String, String> change_ipnHash = new HashMap<String, String>();
				HashMap<String, String> route_nameHash = new HashMap<String, String>();
				HashMap<String, String> start_stepHash = new HashMap<String, String>();
				if (txtActionSeq != null) {
					
					for (int i=0; i<txtActionSeq.length; i++) {
					    if(change_ipn == null) change_ipn = new String[txtActionSeq.length];
					    if(change_ipn[i] == null) change_ipn[i] = "";  //add by ken for downgrade ipn , change_ipn disable
						if(nvm_ws_new != null && nvm_ws_new.equals("YES")){
							change_ipnHash.put(String.valueOf(i), change_ipn[i]);
							route_nameHash.put(String.valueOf(i), route_name[i]);
							start_stepHash.put(String.valueOf(i), start_step[i]);
						}else{	
							change_ipnHash.put(txtActionSeq[i], change_ipn[i]);
							route_nameHash.put(txtActionSeq[i], route_name[i]);
							start_stepHash.put(txtActionSeq[i], start_step[i]);
					    }
					}
				}
				TDSLogger.println("nvm_ws_new: " + nvm_ws_new);
				if(nvm_ws_new != null && nvm_ws_new.equals("YES")){
					YieldDefService.Insert_and_sort(sid, proc_type, yid, product_code, brands, test_mode,
							lower_limit, flag1, upper_limit, flag2,
							item, item_type, item_mode2, item_bins2,
							action, dg_action, change_ipnHash, route_nameHash, start_stepHash, by_lot_dg,dgrade_special_ipn,dgradeprodcode,remark, groupitems_no, item_seq);
				}else{
					YieldDefService.Update_and_sort(sid, proc_type, yid, product_code, brands, test_mode,
						lower_limit, flag1, upper_limit, flag2,
						item, item_type, item_mode2, item_bins2,
						action, dg_action, change_ipnHash, route_nameHash, start_stepHash,by_lot_dg,dgrade_special_ipn,dgradeprodcode, remark, groupitems_no, item_seq);
				}
			}
			
			
			if (!delete_list.equals("")) {
				String id[] = delete_list.split(",");
				YieldDefService.Delete(sid, proc_type, id);
			}
			
			if (save_submit.equals("copy")){
				String fromProduct= Request.getParameter("fromProduct");
				String fromMode= Request.getParameter("fromMode");
				String toProduct= Request.getParameter("toProduct");
				String toMode= Request.getParameter("toMode");
				String ss = YieldDefService.copyTo(sid, proc_type, fromProduct, fromMode, toProduct, toMode, brand);
				if("HASAEBRETENTION".equals(ss)){
					msg_alarm = "Prod code " + fromProduct + " / "+ fromMode + "含有AEB retention bin by Time criteria, 系統將不會copy 該criteria, 請user 自行設定";
				}
			}
			
			if (save_submit.equals("submit")){
				
				try {
					//msg_alarm = OiMaintainService.CheckExistYieldDefCountByWSFTTestParameter(sid,proc_type); //Redmine-#161512 Delete
					msg += OiMaintainService.CheckExistYieldDefCountByWSFTTestParameter(sid,proc_type); //Redmine-#161512 Add
					if(proc_type.equals("WS")){	
						msg += OiMaintainService.CheckExistYieldDefCountByBomRouteAVI(sid,proc_type);
						msg += OiMaintainService.CheckExistYieldDefCountByBomRouteShip(sid,proc_type);
						msg += OiMaintainService.CheckExistSMSNYieldByDgrade(sid);
						msg += OiMaintainService.CheckExistSMSNDgradeYieldByDgrade(sid);
						msg += OiMaintainService.CheckExistSMSNSYLYieldByDgrade(sid);
						msg += OiMaintainService.CheckExistCPSYLYieldByAEB(sid);
						msg += OiMaintainService.CheckExistCPSBLCriteriaByAEB(sid);
						msg_alarm += OiMaintainService.CheckExistDgradeStepBySameDgradeRoute(sid,proc_type);
						msg += OiMaintainService.checkCPSPCControlCriteriaByAEB(sid);
						msg += OiMaintainService.checkCPWaitDgradeIPN(sid);
						msg += OiMaintainService.checkCPRegionCriteriaByAEB(sid);
					}else{
						msg += OiMaintainService.CheckExistFTSYLYieldByAEB(sid,"tf_bom_route_tx");
						msg += OiMaintainService.CheckExistFTSYLYieldByAEB(sid,"tf_bom_route_mcp_tx");
					}
					msg_alarm += OiMaintainService.CheckExistYieldItemDefCountByProdCodeCount(sid,proc_type);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					TDSLogger.println(e);
				}
				if (msg != null && msg.length()!=0) {
					OiMaintainService.submit(sid, "TF_YIELD_"+proc_type, "N");
				}else{
					OiMaintainService.submit(sid, "TF_YIELD_"+proc_type);
				}
			}
		}
		
		if((msg!= null && !msg.equals("")) || (msg_alarm!= null && !msg_alarm.equals(""))){
			forward = "errmsg";
		}
		
		Request.setAttribute(actionMapping.getName(),fm);
		ActionRedirect redirect = new ActionRedirect(actionMapping.findForward(forward));
		redirect.addParameter("sid", new Integer(fm.getSid()));
		redirect.addParameter("proc_type", proc_type);
		redirect.addParameter("nvm_ws_new", nvm_ws_new);
		
		HttpSession session = Request.getSession();
		session.setAttribute("message", msg);
		session.setAttribute("message_alarm", msg_alarm);

		
		//return actionMapping.findForward(forward);
		return redirect;
	}
	
	public static void main(String[] args){
		String sid = "996099";
		try {
			String msg = OiMaintainService.checkCPWaitDgradeIPN(sid);
			TDSLogger.println(msg);
		} catch (Exception e) {
			TDSLogger.println(e);
		}
		
	}
}
	