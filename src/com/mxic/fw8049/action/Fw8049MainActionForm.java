package com.mxic.fw8049.action;

import java.util.List;

import com.mxic.fw8049.dao.Fw8049mationDao;
import com.mxic.oi8040.dao.TgInformationDao;
import com.mxic.oi8040.util.StringUtil;
import com.mxic.oi8040.util.TDSLogger;

public class Fw8049MainActionForm extends BaseActionForm {
	private String queryType;
	private String customerName;
	private String preVersion;
	
	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getPreVersion() {
		return preVersion;
	}

	public void setPreVersion(String preVersion) {
		this.preVersion = preVersion;
	}

	public List<Fw8049MainActionForm> getListByInfo(){
		List<Fw8049MainActionForm> list = null;
		try {
			if(StringUtil.isNull(this.getQueryType()) || this.getQueryType().equals("全部列出")){
				list = Fw8049mationDao.getAllList();
			}else{
				list = Fw8049mationDao.getList(getStatus(this.getQueryType()));
			}
		} catch (Exception e) {
			TDSLogger.println(e);
		}
		return list;
	}
	
	protected String getStatus(String queryType){
		String status = "";
		if(!StringUtil.isNull(this.getQueryType())){
			if(this.getQueryType().equals("會簽中"))
				status = "A";
			else if(this.getQueryType().equals("處理中"))
				status = "P";
			else if(this.getQueryType().equals("已生效"))
				status = "R";
			else if(this.getQueryType().equals("已失效"))
				status = "F";
		}
		return status;
	}
}
