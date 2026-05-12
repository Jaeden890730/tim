package com.mxic.oiplus.oisearch;

import java.io.*;
import java.util.Calendar;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
import com.mxic.oiplus.au.*;

public class TestRouteAddAction extends Action {
	
	public ActionForward execute(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		String forward = "success";
		String path = TDSResource.getProperties("TIMPdf").getValue("jpg_route.path")
				+ File.separator;
		TestRouteAddActionForm form = (TestRouteAddActionForm) actionForm;
		String act = form.getAction();
		String file_type = form.getFile_type();
		String listControl = request.getParameter("listControl");
		String ra_select = request.getParameter("raSelect");
		String remark = request.getParameter("remark");
		String route_cat = request.getParameter("route_cat");
		String search_routename = request.getParameter("search_routename");
		HttpSession session = request.getSession();
		//String productType = (String) session.getAttribute("productType");
		User Auth = (User) session.getAttribute("user");
		String user = Auth.getUserName();
		if (act != null && act.equals("upload")) {
			
			TFRouteMasterBean tfRMBean = oiSearchService.getRouteMasterBean(ra_select);
			String file_list = "";
			if(file_type.equals("png")){
				file_list = tfRMBean.getFile_name_testflow();
			}else if(file_type.equals("xls")){
				String stdFileName = tfRMBean.getFile_name_stdexcflow();
				if(stdFileName != null && !stdFileName.equals("")){
					File file = new File(path + stdFileName); 
					if(file.exists())
						file.delete();
				}				
			}
			
			FormFile[] formFileList = new FormFile[]{form.getFormname(), form.getFormname1(), form.getFormname2(), form.getFormname3()};			
			long time = Calendar.getInstance().getTimeInMillis();
			for(int i=0; i<formFileList.length; i++){
				if(formFileList[i] != null){
					String tmpFileName = ra_select + "_" + String.valueOf(time) + String.valueOf(i) + "." + file_type;
					if(this.doUploadFile(ra_select, file_type, path + tmpFileName, formFileList[i])){
						if(file_list != null && !file_list.equals("")){
							file_list += ";" + tmpFileName;
						}else{
							file_list = tmpFileName;
						}
					}
				}
			}				
			
			boolean flag = TestRouteAddService.update_file_name(ra_select, file_type,
					file_list, remark, route_cat, user);
			
			oiWSTestRouteControlAForm wsTRCAform = new oiWSTestRouteControlAForm();
			wsTRCAform.setListControl(listControl);
			wsTRCAform.setRa_select(ra_select);
			wsTRCAform.setRoute_cat(route_cat);
			wsTRCAform.setRemark(remark);
			wsTRCAform.setSearch_routename(search_routename);
			wsTRCAform.setFile_type(file_type);
			wsTRCAform.setFile_list(file_list);
	        request.setAttribute("list1", wsTRCAform);
	      
			forward = "upload_add";			
		} else if (act != null && act.equals("delete")) {
			String deleteFile = form.getRadioFile();
			this.doDeleteFile(ra_select, file_type, remark, route_cat, user, deleteFile);
			
			oiWSTestRouteControlAForm wsTRCAform = new oiWSTestRouteControlAForm();
			wsTRCAform.setListControl(listControl);
			wsTRCAform.setRa_select(ra_select);
			wsTRCAform.setRoute_cat(route_cat);
			wsTRCAform.setRemark(remark);
			wsTRCAform.setSearch_routename(search_routename);
			wsTRCAform.setFile_type(file_type);
			TFRouteMasterBean tfRMBean = oiSearchService.getRouteMasterBean(ra_select);
			String file_list = "";
			if(file_type.equals("png"))
				file_list = tfRMBean.getFile_name_testflow();
			else if(file_type.equals("xls"))
				file_list = tfRMBean.getFile_name_stdexcflow();			

			wsTRCAform.setFile_list(file_list);
	        request.setAttribute("list1", wsTRCAform);
	        
	        forward = "upload_add";
		} else {
			FormFile myFile = form.getFormname(); // 宣告myFile為FormFile物件

			String myFile_name = StringUtil.Utf8ToBig5(myFile.getFileName());

			int myFile_name_int = myFile_name.indexOf(".png");

			int myFile_name_str_int = myFile_name.length();
			if ((myFile_name_int == -1 && myFile_name_str_int == 0)) {
				// 未上傳檔案
				//String ra_select = request.getParameter("raSelect");// String.
																	// valueOf(
																	// TestRouteAddActionForm
																	// .
																	// getRa_select
																	// ());
				//String remark = request.getParameter("remark");
				//String route_cat = request.getParameter("route_cat");
				//String search_routename = request.getParameter("search_routename");
				//HttpSession session = request.getSession();
				session.setAttribute("list7", ra_select);
				return actionMapping.findForward("fail");
			} else if ((myFile_name_int == -1 && myFile_name_str_int > 0)) {
				// 上傳非.png檔案
				//String ra_select = request.getParameter("raSelect");// String.
																	// valueOf(
																	// TestRouteAddActionForm
																	// .
																	// getRa_select
																	// ());
				//String remark = request.getParameter("remark");
				//String route_cat = request.getParameter("route_cat");
				//String search_routename = request.getParameter("search_routename");
				//HttpSession session = request.getSession();
				session.setAttribute("list7", ra_select);
				return actionMapping.findForward("fail");
			} else {
				// String FullFileName =
				// StringUtil.Utf8ToBig5(myFile.getFileName()); //將完整檔名轉成BIG5碼
				//String ra_select = request.getParameter("raSelect");// String.
																	// valueOf(
																	// TestRouteAddActionForm
																	// .
																	// getRa_select
																	// ());
				//String remark = request.getParameter("remark");
				//String route_cat = request.getParameter("route_cat");
				//String search_routename = request.getParameter("search_routename");
				String file_name = ra_select + ".png";

				//HttpSession session = request.getSession();
				//User Auth = (User) session.getAttribute("user");
				//String productType = (String) session.getAttribute("productType");
				//String user = Auth.getUserName();
				boolean flag = TestRouteAddService.update_file_name(ra_select,
						file_name, remark, route_cat, user);

				String FullFileName = StringUtil.Utf8ToBig5(file_name); // 將完整檔名轉成BIG5碼
				String TmpFileName = new String(FullFileName + ".tmp");
				File newFile = new File(path + FullFileName);
				File tmpFile = new File(path + TmpFileName);
				try {
					if (flag) {
						// 將上傳的檔案存在/citplus/File裡
						FileOutputStream fileOutput = new FileOutputStream(
								tmpFile);
						fileOutput.write(myFile.getFileData());
						fileOutput.flush();
						fileOutput.close();
						if ((!newFile.exists())
								|| (FileUtil.Compare(newFile, tmpFile) == 1)) {
							FileUtil.Copy(tmpFile, newFile);
							forward = "success";
						} else
							forward = "upload_skip";
					} else {
						forward = "upload_fail";
					}
					myFile.destroy();
					session.setAttribute("list7", ra_select);
					session.setAttribute("search_routename", search_routename);
				} catch (Exception e) {
					TDSLogger.println(e);
					e.printStackTrace();
				} finally {
					if (tmpFile.exists())
						tmpFile.delete();
				}
			}
		}
		return actionMapping.findForward(forward);
	}
	
	private boolean doUploadFile(String route_name, String file_type, String fullFileName, FormFile formFile){
		String formFileName = formFile.getFileName();
		if ((formFileName.indexOf(file_type) == -1 || formFileName.length() == 0)) {
			return false;
		}

		File newFile = new File(fullFileName);
		File tmpFile = new File(fullFileName + ".tmp");
		try {
			FileOutputStream fileOutput = new FileOutputStream(tmpFile);
			fileOutput.write(formFile.getFileData());
			fileOutput.flush();
			fileOutput.close();
			if ((!newFile.exists())
					|| (FileUtil.Compare(newFile, tmpFile) == 1)) {
				FileUtil.Copy(tmpFile, newFile);
			}
			formFile.destroy();
		}catch(Exception e){
			TDSLogger.println(e);
		} finally {
			if (tmpFile.exists())
				tmpFile.delete();
		}
		return true;
	}
	
	private void doDeleteFile(String route_name, String file_type, String remark, String route_cat, String user_id, String deleteFile){
		String path = TDSResource.getProperties("TIMPdf").getValue("jpg_route.path")+ File.separator;

		TFRouteMasterBean tfRMBean = oiSearchService.getRouteMasterBean(route_name);
	    if(tfRMBean != null && file_type != null){
	    	String[] fileList = null;
	    	String uploadFileList = "";
	    	if(file_type.equalsIgnoreCase("png")){
	    		if(tfRMBean.getFile_name_testflow() != null && !tfRMBean.getFile_name_testflow().equals("")){
	    			fileList = tfRMBean.getFile_name_testflow().split(";");
	    		}
	    	}else if(file_type.equalsIgnoreCase("xls")){
	    		if(tfRMBean.getFile_name_stdexcflow() != null && !tfRMBean.getFile_name_stdexcflow().equals("")){
	    			fileList = tfRMBean.getFile_name_stdexcflow().split(";");
	    		}
	    	}
	    	
	    	//delete file
			for(int i=0; i<fileList.length; i++){
				if(fileList[i].equals(deleteFile)){
					File delFile = new File(path + deleteFile);
					if(delFile.exists())
						delFile.delete();
				}else{
					uploadFileList += fileList[i] + ";";
				}
			}
			if(uploadFileList.length() > 1)
				uploadFileList = uploadFileList.substring(0, uploadFileList.length()-1);
			//update
			TestRouteAddService.delete_file_name(route_name, file_type, uploadFileList, remark, route_cat, user_id);

	    }		
	}
}
