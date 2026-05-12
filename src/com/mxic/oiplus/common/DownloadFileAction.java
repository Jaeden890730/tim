package com.mxic.oiplus.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.mxic.oiplus.util.TDSLogger;
import com.mxic.tdsplus.resource.TDSProperties;
import com.mxic.tdsplus.resource.TDSResource;


public class DownloadFileAction extends Action {	

	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
		DownloadFileActionForm form =  (DownloadFileActionForm)	actionForm;
		FileInputStream fis = null;
		OutputStream os = null;
		try{
			TDSLogger.println("DownloadFileAction() - propMainName: " + form.getPropMainName() 
					+ ", propKeyName: " + form.getPropKeyName() 
					+ ", fileName: " + form.getFileName());
			
			File file = checkFile(form, response);
			if(file == null) return null;			
			
			fis = new FileInputStream(file);
			if (form.getFileName().toLowerCase().endsWith(".xls")) {
				response.setContentType("application/vnd.ms-excel");
			} else if (form.getFileName().toLowerCase().endsWith(".pdf")) {
				response.setContentType("application/pdf");
			} else {
				response.setContentType("application/octect-stream");
			}
			String fileName = new String(form.getFileName().getBytes("big5"), "ISO8859-1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			os = response.getOutputStream();
			int byteRead;
			while ((byteRead = fis.read()) != -1)
				os.write(byteRead);
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Exception e) {
			request.setAttribute("message", "Error:" + e.getMessage().replaceAll("\n", "").replaceAll("\\\\", "\\\\\\\\"));
			return actionMapping.findForward("msg");
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {}
			}
		}
		return null;
	}
	
	public File checkFile(DownloadFileActionForm form, HttpServletResponse response) throws IOException{
		TDSProperties tdsProperties = TDSResource.getProperties(form.getPropMainName());
		String filePath = tdsProperties.getValue(form.getPropKeyName());
		TDSLogger.println("checkFile() - filePath: " + filePath);
		if (filePath == null) {
			String msg = " Properties[" + form.getPropMainName() + "] key [" + form.getPropKeyName() + "] is null!";
			responseAlertMessage(response, msg);
			return null;
		}
			
		if(!filePath.endsWith(File.separator))
			filePath += File.separator;
			
		File file = new File(filePath + form.getFileName());
		if(!file.exists()){
			String msg = "檔案 [" + form.getFileName() + "] 不存在!";
			responseAlertMessage(response, msg);
			return null;				
		}
		return file;
	}
	
	public void responseAlertMessage(HttpServletResponse response, String message) throws IOException{
		response.setContentType("text/html; charset=big5");
		java.io.PrintWriter out = response.getWriter();
		out.print("<script>javascript:alert('" + message + "');history.back();</script>");
		out.flush();
	}
}
