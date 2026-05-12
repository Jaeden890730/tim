package com.mxic.oiplus.common;

import org.apache.struts.action.*;

/**
 * 
 * <p>
 * Title: UploadFileªºActionForm
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class DownloadFileActionForm extends ActionForm {
	private String propMainName;
	private String propKeyName;
	private String fileName;
	
	public String getPropMainName() {
		return propMainName;
	}
	public void setPropMainName(String propMainName) {
		this.propMainName = propMainName;
	}
	public String getPropKeyName() {
		return propKeyName;
	}
	public void setPropKeyName(String propKeyName) {
		this.propKeyName = propKeyName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}