package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

//import com.mxic.oiplus.rs.*;
import java.sql.*;
import java.util.*;

//import com.mxic.oiplus.rs.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;


public class AUService {
  private Connection conn;
  public AUService() {
  }
  public static boolean checkLogin(AULoginForm fm, Authority auth){
	UserAccountForm uafm = getUserAccountByName(fm.getUser_name());
	if(uafm != null){

                      String PassWord = MD5.getMD5String(fm.getPassword());
                      //String PassWord = fm.getPassword();
                      //System.out.println(PassWord);
                      //System.out.println(uafm.getPassword()+","+uafm.getUser_id());

	    if(PassWord.equals(uafm.getPassword())){
		auth.setUser_id(uafm.getUser_id());
		auth.setUser_name(uafm.getUser_name());
		auth.setUser_password(uafm.getPassword());
		auth.setReal_name(uafm.getReal_name());
		auth.setBo_user(uafm.getBo_user());
		auth.setBo_password(uafm.getBo_password());
		auth.setDept_id(uafm.getDept_id());
		auth.setDept_name(uafm.getDept_name());
                auth.setDept_group(uafm.getDept_group());
		auth.setEmployee_no(uafm.getEmployee_no());
		auth.setNotes_id(uafm.getNotes_id());
		auth.setAct_action(getAUACTByUser_ID(uafm.getUser_id()));
		return true;
	    }
	}
	return false;
  }
  public static int changeUserPassword(AULoginForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.changeUserPassword(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
	}finally{

	      DBConnection.close(conn);

	}
	    return flag;
  }

  public static HashMap[] getUserGroupByCrit(String user_sid, String grp_sid){
	Connection conn = null;
    try{
	    String crit = "";
	    if(user_sid != null && !user_sid.equals("")){
	      user_sid = "user_sid = '" + user_sid + "'";
	    }else{
	      user_sid = "";
	    }
	    if(grp_sid != null && !grp_sid.equals("")){
	      grp_sid = "grp_sid = '" + grp_sid + "'";
	    }else{
	      grp_sid = "";
	    }
	    crit = user_sid + ";" + grp_sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	     conn = DBConnection.getConnection();
	    HashMap[] rs = AUDAO.getUserGroupByCrit(conn,crit);

	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{

              DBConnection.close(conn);

	}
	    return null;
  }

  public static AUGroupForm[] getUserGroupBySid(String user_sid){
	  Connection conn = null;
	  try{
		  conn = DBConnection.getConnection();
		  AUGroupForm[] rs = AUDAO.getUserGroupBySid(conn, user_sid);

		  return rs;
	  }catch(Exception e){
		  TDSLogger.println(e);
	  }finally{

		  DBConnection.close(conn);

	  }
	  return null;
  }

  public static HashMap[] getUserActionByCrit(String user_sid, String act_sid){
        Connection conn = null;
	try{
	    String crit = "";
	    if(user_sid != null && !user_sid.equals("")){
	      user_sid = "user_sid = '" + user_sid + "'";
	    }else{
	      user_sid = "";
	    }
	    if(act_sid != null && !act_sid.equals("")){
	      act_sid = "act_sid = '" + act_sid + "'";
	    }else{
	      act_sid = "";
	    }
	    crit = user_sid + ";" + act_sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    HashMap[] rs = AUDAO.getUserActionByCrit(conn,crit);

	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{

              DBConnection.close(conn);

	}
	    return null;
  }

  public static AUACTForm[] getUserAction(String user_sid){
      Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    AUACTForm[] rs = AUDAO.getAUACTListByUserID(conn,user_sid);

	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
      }finally{

            DBConnection.close(conn);

	}
	    return null;
  }

  public static HashMap[] getGroupActionByCrit(String grp_sid, String act_sid){
        Connection conn = null;
	try{
	    String crit = "";
	    if(grp_sid != null && !grp_sid.equals("")){
	      grp_sid = "grp_sid = '" + grp_sid + "'";
	    }else{
	      grp_sid = "";
	    }
	    if(act_sid != null && !act_sid.equals("")){
	      act_sid = "act_sid = '" + act_sid + "'";
	    }else{
	      act_sid = "";
	    }
	    crit = grp_sid + ";" + act_sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    HashMap[] rs = AUDAO.getGroupActionByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static UserAccountForm[] getUserAccountByCrit(AUDisplayForm fm){
        Connection conn = null;
	try{
	    String crit = "";
	    String user_name = fm.getUser_name();
	    String dept_id = fm.getDept_id();
	    if(user_name != null && !user_name.equals("")){
	      user_name = "a.user_name like '" + user_name + "%'";
	    }else{
	      user_name = "";
	    }
	    if(dept_id != null && !dept_id.equals("")){
	      dept_id = "a.dept_id = '" + dept_id + "'";
	    }else{
	      dept_id = "";
	    }
	    crit = user_name + ";" + dept_id;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    UserAccountForm[] rs = AUDAO.getUserAccountByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }

  public static UserAccountForm[] getUserAccountByCrit(UserAccountForm fm){
        Connection conn = null;
	try{
	    String crit = "";
	    String user_name = fm.getUser_name();
	    String user_id = fm.getUser_id();
	    if(user_name != null && !user_name.equals("")){
	      user_name = "a.user_name like '" + user_name + "%'";
	    }else{
	      user_name = "";
	    }
	    if(user_id != null && !user_id.equals("")){
	      user_id = "a.user_id = '" + user_id + "'";
	    }else{
	      user_id = "";
	    }
	    crit = user_name + ";" + user_id ;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    UserAccountForm[] rs = AUDAO.getUserAccountByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static UserAccountForm getUserAccountByID(String user_id){
        Connection conn = null;
	try{
	    String crit = "";
	    if(user_id != null && !user_id.equals("")){
	      user_id = "user_id = '" + user_id + "'";
	    }else{
	      user_id = "";
	    }
	    crit = user_id ;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    UserAccountForm[] rs = AUDAO.getUserAccountByCrit(conn,crit);
	    //conn.close();
	    if(rs != null && rs.length>0){
	      return rs[0];
	    }
	    return null;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static UserAccountForm getUserAccountByName(String user_name){
        Connection conn = null;
	try{
	    String crit = "";
	    if(user_name != null && !user_name.equals("")){
	      user_name = "user_name = '" + user_name + "'";
	      crit = user_name ;
	      crit = StringUtil.FormatCrit(crit," and ");
          crit = crit + " and nvl(delete_flag, 'N') = 'N' ";
              //System.out.println(crit);
	      conn = DBConnection.getConnection();
	      UserAccountForm[] rs = AUDAO.getUserAccountByCrit(conn,crit);
	      //conn.close();
	      if(rs != null && rs.length>0){
		return rs[0];
	      }
		return null;
	    }
	    return null;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUACTForm[] getAUACTByCrit(AUDisplayForm fm){
        Connection conn = null;
	try{
	    String crit = "";
	    String act_action = fm.getAct_action();
	    if(act_action != null && !act_action.equals("")){
	      //act_action = "act_action like '" + act_action + "%'";
              act_action = "act_action like '%" + act_action + "%'";
	    }else{
	      act_action = "";
	    }
	    crit = act_action;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    AUACTForm[] rs = AUDAO.getAUACTByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUACTForm[] getAUACTByCrit(AUACTForm fm){
        Connection conn = null;
	try{
	    String crit = "";
	    String act_action = fm.getAct_action();
	    String act_sid = fm.getAct_sid();
	    if(act_action != null && !act_action.equals("")){
	      act_action = "act_action like '" + act_action + "%'";
	    }else{
	      act_action = "";
	    }
	    if(act_sid != null && !act_sid.equals("")){
	      act_sid = "act_sid = '" + act_sid + "'";
	    }else{
	      act_sid = "";
	    }
	    crit = act_action + ";" + act_sid ;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    AUACTForm[] rs = AUDAO.getAUACTByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUACTForm[] getAUACTByUser_ID(String user_id){
        Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    AUACTForm[] rs = AUDAO.getAUACTByUserID(conn,user_id);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUACTForm[] getAllAUACT(){
        Connection conn = null;
	try{
	    String crit = "";
	    conn = DBConnection.getConnection();
	    AUACTForm[] rs = AUDAO.getAUACTByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUGroupForm[] getAUGroupByCrit(AUDisplayForm fm){
        Connection conn = null;
	try{
	    String crit = "";
	    String grp_name = fm.getGrp_name();
	    if(grp_name != null && !grp_name.equals("")){
	      grp_name = "grp_name like '" + grp_name + "%'";
	    }else{
	      grp_name = "";
	    }
	    crit = grp_name;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    AUGroupForm[] rs = AUDAO.getAUGroupByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUGroupForm[] getAUGroupByCrit(AUGroupForm fm){
        Connection conn = null;
	try{
	    String crit = "";
	    String grp_name = fm.getGrp_name();
	    String grp_sid = fm.getGrp_sid();
	    if(grp_name != null && !grp_name.equals("")){
	      grp_name = "grp_name like '" + grp_name + "%'";
	    }else{
	      grp_name = "";
	    }
	    if(grp_sid != null && !grp_sid.equals("")){
	      grp_sid = "grp_sid = '" + grp_sid + "'";
	    }else{
	      grp_sid = "";
	    }
	    crit = grp_name + ";" + grp_sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    AUGroupForm[] rs = AUDAO.getAUGroupByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUGroupForm getAUGroupByID(String sid){
        Connection conn = null;
	try{
	    String crit = "";
	    if(sid != null && !sid.equals("")){
	      sid = "grp_sid = '" + sid + "'";
	    }else{
	      sid = "";
	    }
	    crit = sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    AUGroupForm[] rs = AUDAO.getAUGroupByCrit(conn,crit);
	    //conn.close();
	    if(rs != null && rs.length>0){
	      return rs[0];
	    }
	    return null;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static AUGroupForm[] getAllAUGroup(){
        Connection conn = null;
	try{
	    String crit = "";
	    conn = DBConnection.getConnection();
	    AUGroupForm[] rs = AUDAO.getAUGroupByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static UserDepartmentForm[] getUserDepartmentByCrit(AUDisplayForm fm){
        Connection conn = null;
	try{
	    String crit = "";
	    String dept_id = fm.getDept_id();
	    if(dept_id != null && !dept_id.equals("")){
	      dept_id = "dept_id like '" + dept_id + "%'";
	    }else{
	      dept_id = "";
	    }
	    crit = dept_id;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    UserDepartmentForm[] rs = AUDAO.getUserDepartmentByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static UserDepartmentForm getUserDepartmentByID(String sid){
        Connection conn = null;
	try{
	    String crit = "";
	    if(sid != null && !sid.equals("")){
	      sid = "dept_id = '" + sid + "'";
	    }else{
	      sid = "";
	    }
	    crit = sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	    conn = DBConnection.getConnection();
	    UserDepartmentForm[] rs = AUDAO.getUserDepartmentByCrit(conn,crit);
	    //conn.close();
	    if(rs != null && rs.length>0){
	      return rs[0];
	    }
	    return null;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }
  public static UserDepartmentForm[] getAllUserDepartment(){
        Connection conn = null;
	try{
	    String crit = "";
	    conn = DBConnection.getConnection();
	    UserDepartmentForm[] rs = AUDAO.getUserDepartmentByCrit(conn,crit);
	    //conn.close();
	    return rs;
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return null;
  }

  public static boolean deleteUserAccount(UserAccountForm fm){
	boolean flag = false;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.deleteUserAccountByUser_id(conn,fm.getUser_id());
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static boolean deleteAUACT(AUACTForm fm){
	boolean flag = false;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.deleteAUACTByAct_sid(conn,fm.getAct_sid());
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static boolean deleteAUGroup(AUGroupForm fm){
	boolean flag = false;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.deleteAUGroupByGrp_sid(conn,fm.getGrp_sid());
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static boolean deleteUserDepartment(UserDepartmentForm fm){
	boolean flag = false;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.deleteUserDepartmentByDept_id(conn,fm.getDept_id());
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static boolean deleteUserGroupByCrit(String user_sid, String grp_sid){
	boolean flag = false;
	String crit = "";
	    if(user_sid != null && !user_sid.equals("")){
	      user_sid = "user_sid = '" + user_sid + "'";
	    }else{
	      user_sid = "";
	    }
	    if(grp_sid != null && !grp_sid.equals("")){
	      grp_sid = "grp_sid = '" + grp_sid + "'";
	    }else{
	      grp_sid = "";
	    }
	    crit = user_sid + ";" + grp_sid;
	    crit = StringUtil.FormatCrit(crit," and ");
            System.out.println(crit);

	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.deleteUserGroupByCrit(conn,crit);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static boolean deleteUserActionByCrit(String user_sid, String act_sid){
	boolean flag = false;
	String crit = "";
	    if(user_sid != null && !user_sid.equals("")){
	      user_sid = "user_sid = '" + user_sid + "'";
	    }else{
	      user_sid = "";
	    }
	    if(act_sid != null && !act_sid.equals("")){
	      act_sid = "act_sid = '" + act_sid + "'";
	    }else{
	      act_sid = "";
	    }
	    crit = user_sid + ";" + act_sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.deleteUserActionByCrit(conn,crit);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static boolean deleteGroupActionByCrit(String grp_sid, String act_sid){
	boolean flag = false;
	String crit = "";
	    if(grp_sid != null && !grp_sid.equals("")){
	      grp_sid = "grp_sid = '" + grp_sid + "'";
	    }else{
	      grp_sid = "";
	    }
	    if(act_sid != null && !act_sid.equals("")){
	      act_sid = "act_sid = '" + act_sid + "'";
	    }else{
	      act_sid = "";
	    }
	    crit = grp_sid + ";" + act_sid;
	    crit = StringUtil.FormatCrit(crit," and ");
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.deleteGroupActionByCrit(conn,crit);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int updateUserAccount(UserAccountForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.updateUserAccount(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int updateAUACT(AUACTForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.updateAUACT(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int updateAUGroup(AUGroupForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.updateAUGroup(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int updateUserDepartment(UserDepartmentForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.updateUserDepartment(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertUserAccount(UserAccountForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.insertUserAccount(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertAUACT(AUACTForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.insertAUACT(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertAUGroup(AUGroupForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.insertAUGroup(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertUserDepartment(UserDepartmentForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.insertUserDepartment(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertUserGroup(AUAssignForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.insertUserGroup(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertGroupAction(AUAssignForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.insertGroupAction(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertUserAction(AUAssignForm fm){
	int flag = 0;
	Connection conn = null;
	try{
	    conn = DBConnection.getConnection();
	    flag = AUDAO.insertUserAction(conn,fm);
	}catch(Exception e){
	  TDSLogger.println(e);
        }finally{
              DBConnection.close(conn);
	}
	    return flag;
  }
  public static int insertTDSLog(Authority auth, String path){
      int flag = 0;
      Connection conn = null;
      if(auth!=null){
          String user = auth.getUser_name();
          try{
              conn = DBConnection.getConnection();
              flag = AUDAO.insertTDSLog(conn,user,path);
          }catch(Exception e){
            TDSLogger.println(e);
          }finally{
                DBConnection.close(conn);
          }
      }
          return flag;
  }
}
