package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.sql.*;
import java.util.*;

import com.mxic.oiplus.util.*;

public class AUTransf {

  public AUTransf() {
  }

  public static UserAccountForm[] UserAccountRsToObject(ResultSet rs) throws Exception{
	  if(rs != null){
	    ArrayList tmp = new ArrayList();
	    while(rs.next()){
		UserAccountForm view = new UserAccountForm();
		view.setUser_id(rs.getString("user_id"));
		view.setUser_name(rs.getString("user_name"));
		view.setEmployee_no(rs.getString("employee_no"));
		view.setReal_name(rs.getString("real_name"));
		view.setPassword(rs.getString("password"));
		view.setNotes_id(rs.getString("notes_id"));
		view.setDept_id(rs.getString("dept_id"));
		view.setDept_name(rs.getString("dept_name_e"));
        //view.setDept_group(rs.getString("dept_group"));
		view.setBo_user(rs.getString("bo_user"));
		view.setBo_password(rs.getString("bo_password"));

		tmp.add(view);
	    }
	    return ((UserAccountForm[]) tmp.toArray(new UserAccountForm[0]));
	}else{
	    return null;
	}
  }

  public static AUACTForm[] AUACTRsToObject(ResultSet rs) throws Exception{
	  if(rs != null){
	    ArrayList tmp = new ArrayList();
	    while(rs.next()){
		AUACTForm view = new AUACTForm();
		view.setAct_sid(rs.getString("act_sid"));
		view.setAct_action(rs.getString("act_action"));
		view.setAct_class(rs.getString("act_class"));
		view.setAct_desc(rs.getString("act_desc"));
		tmp.add(view);
	    }
	    return ((AUACTForm[]) tmp.toArray(new AUACTForm[0]));
	}else{
	    return null;
	}
  }

  public static AUACTForm[] AUACTRsToObject2(ResultSet rs) throws Exception{
	  if(rs != null){
	    ArrayList tmp = new ArrayList();
	    while(rs.next()){
		AUACTForm view = new AUACTForm();
		view.setAct_sid(rs.getString("act_sid"));
		view.setAct_action(rs.getString("act_action"));
		view.setAct_class(rs.getString("act_class"));
		view.setAct_desc(rs.getString("act_desc"));
		view.setReturn_flag(rs.getString("flag"));
		tmp.add(view);
	    }
	    return ((AUACTForm[]) tmp.toArray(new AUACTForm[0]));
	}else{
	    return null;
	}
  }


  public static AUGroupForm[] AUGroupRsToObject(ResultSet rs) throws Exception{
	  if(rs != null){
	    ArrayList tmp = new ArrayList();
	    while(rs.next()){
		AUGroupForm view = new AUGroupForm();
		view.setGrp_sid(rs.getString("grp_sid"));
		view.setGrp_name(rs.getString("grp_name"));
		view.setGrp_desc(rs.getString("grp_desc"));

		tmp.add(view);
	    }
	    return ((AUGroupForm[]) tmp.toArray(new AUGroupForm[0]));
	}else{
	    return null;
	}
  }

  public static UserDepartmentForm[] UserDepartmentRsToObject(ResultSet rs) throws Exception{
	  if(rs != null){
	    ArrayList tmp = new ArrayList();
	    while(rs.next()){
		UserDepartmentForm view = new UserDepartmentForm();
		view.setDept_id(rs.getString("dept_id"));
		view.setDept_name(rs.getString("dept_name"));
		view.setDept_group(rs.getString("dept_group"));

		tmp.add(view);
	    }
	    return ((UserDepartmentForm[]) tmp.toArray(new UserDepartmentForm[0]));
	}else{
	    return null;
	}
  }

  public static PreparedStatement prepareWriteUserAccount(PreparedStatement ps, UserAccountForm fm) throws Exception{
            fm.setReal_name(new String(fm.getReal_name().getBytes("ISO-8859-1"),"Big5"));
	    ps.setString(1,fm.getEmployee_no());
	    ps.setString(2,fm.getReal_name());
	    ps.setString(3,fm.getNotes_id());
	    ps.setString(4,fm.getDept_id());
	    ps.setString(5,fm.getBo_user());
	    ps.setString(6,fm.getBo_password());
	    ps.setString(7,fm.getUser_id());
	    return ps;
  }
  public static PreparedStatement prepareWriteChangePasswore(PreparedStatement ps, AULoginForm fm) throws Exception{
//Robin20060509	    ps.setString(1,MD5.getMD5String(fm.getNew_password().getBytes()));
	    ps.setString(1,MD5.getMD5String(fm.getNew_password()));
	    ps.setString(2,fm.getUser_id());
	    return ps;
  }

  public static PreparedStatement prepareWriteAUACT(PreparedStatement ps, AUACTForm fm) throws Exception{
	    ps.setString(1,fm.getAct_class());
	    ps.setString(2,fm.getAct_desc());
	    ps.setString(3,fm.getAct_sid());
	    return ps;
  }

  public static PreparedStatement prepareWriteAUGroup(PreparedStatement ps, AUGroupForm fm) throws Exception{
	    ps.setString(1,fm.getGrp_desc());
	    ps.setString(2,fm.getGrp_sid());
	    return ps;
  }

  public static PreparedStatement prepareWriteUserDepartment(PreparedStatement ps, UserDepartmentForm fm) throws Exception{
	    ps.setString(1,fm.getDept_name());
            ps.setString(2,fm.getDept_group());
	    ps.setString(3,fm.getDept_id());
            //System.out.println(fm.getDept_id());
	    return ps;
  }

  public static PreparedStatement prepareInsertUserAccount(PreparedStatement ps, UserAccountForm fm) throws Exception{
//	    fm.setReal_name(new String(fm.getReal_name().getBytes("ISO-8859-1"),"Big5"));
            ps.setString(1,fm.getUser_name());
	    ps.setString(2,fm.getEmployee_no());
	    ps.setString(3,fm.getReal_name());
//Robin 20060509	    ps.setString(4,MD5.getMD5String(fm.getPassword().getBytes()));
            ps.setString(4,MD5.getMD5String(fm.getPassword()));
	    ps.setString(5,fm.getNotes_id());
	    ps.setString(6,fm.getDept_id());
	    ps.setString(7,fm.getBo_user());
	    ps.setString(8,fm.getBo_password());

	    return ps;
  }

  public static PreparedStatement prepareInsertAUACT(PreparedStatement ps, AUACTForm fm) throws Exception{
	    ps.setString(1,fm.getAct_action());
	    ps.setString(2,fm.getAct_class());
	    ps.setString(3,fm.getAct_desc());

	    return ps;
  }

  public static PreparedStatement prepareInsertAUGroup(PreparedStatement ps, AUGroupForm fm) throws Exception{
	    ps.setString(1,fm.getGrp_name());
	    ps.setString(2,fm.getGrp_desc());

	    return ps;
  }
  public static PreparedStatement prepareInsertUserDepartment(PreparedStatement ps, UserDepartmentForm fm) throws Exception{
	    ps.setString(1,fm.getDept_id());
	    ps.setString(2,fm.getDept_name());
            ps.setString(3,fm.getDept_group());

	    return ps;
  }
}
