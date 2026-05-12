package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class User extends com.mxic.tdsplus.pgm.User{
	private String userName;
	private String userId;
	private String deptNo;
	private String deptName;
	private String tdsGroup;
	private String deptGroup;
	private String empNo;
	private String realName;
  
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setTdsGroup(String tdsGroup) {
		this.tdsGroup = tdsGroup;
	}
	public String getTdsGroup() {
		return tdsGroup;
	}
  public void setDeptGroup(String deptGroup) {
    this.deptGroup = deptGroup;
  }
  public String getDeptGroup() {
    return deptGroup;
  }

}
