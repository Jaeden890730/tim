package com.mxic.oiplus.au;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.sql.*;
import java.util.*;

import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;
import com.mxic.tdsplus.resource.DBConnection;
import com.mxic.tdsplus.util.TDSLogger;

public class AUDAO {
    public AUDAO() {
    }

    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            conn.close();
        } catch (Exception e) {
            TDSLogger.println(e);
            TDSLogger.println(e);
        }
    }

    private static HashMap setRStoHashMap(ResultSet rs) throws Exception {
        HashMap hm = new HashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            String columnName = rsmd.getColumnName(i);
            hm.put(columnName, rs.getObject(columnName));
        }
        return hm;
    }

    private static Object[] translateFormArrayList(ArrayList tmp) {
        return (Object[]) tmp.toArray(new Object[0]);
    }

    public static HashMap[] getUserGroupByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        sql = "select * from au_user_group";
        if (!crit.equals("")) {
            sql = sql + " where " + crit;
        }
        //System.out.println(sql);
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            tmp.add(setRStoHashMap(rs));
        }
        return (HashMap[]) tmp.toArray(new HashMap[0]);
    }

    public static AUGroupForm[] getUserGroupBySid(Connection conn, String user_sid) throws
    Exception {
    	String sql = "";
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	ArrayList tmp = new ArrayList();
    	sql = "select g.grp_sid, g.grp_name, g.grp_desc from au_user_group u, au_group g";
   		sql = sql + " where user_sid = " + user_sid + " and g.grp_sid = u.grp_sid";
    	//System.out.println(sql);
    	ps = conn.prepareStatement(sql);
    	rs = ps.executeQuery();

    	return AUTransf.AUGroupRsToObject(rs);
    }


    public static HashMap[] getUserActionByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        //sql = "select * from au_user_action";
        sql = "select * from au_user_action";
        if (!crit.equals("")) {
            sql = sql + " where " + crit;
        }

        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            tmp.add(setRStoHashMap(rs));
        }
        return (HashMap[]) tmp.toArray(new HashMap[0]);
    }

    public static HashMap[] getGroupActionByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        sql = "select * from au_group_action";
        if (!crit.equals("")) {
            sql = sql + " where " + crit;
        }
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            tmp.add(setRStoHashMap(rs));
        }
        return (HashMap[]) tmp.toArray(new HashMap[0]);
    }

    public static UserAccountForm[] getUserAccountByCrit(Connection conn,
            String crit) throws Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        //sql = "select a.*, b.* from au_user_account a, au_user_department b where a.dept_id = b.dept_id(+) ";
        sql = "select a.*, b.* from au_user_account a, ba_mxic_dept b where a.dept_id = b.dept_id(+) ";
        if (!crit.equals("")) {
            sql = sql + " and " + crit;
        }
        //System.out.println(sql);
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();

        return AUTransf.UserAccountRsToObject(rs);
    }

    public static AUACTForm[] getAUACTByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        sql = "select * from au_actions ";

        if (!crit.equals("")) {
            sql = sql + " where " + crit;
        }
        sql = sql + " order by act_action, act_class, act_desc";
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();

        return AUTransf.AUACTRsToObject(rs);
    }

    public static AUACTForm[] getAUACTByUserID(Connection conn, String user_id) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        /* sql = "select a.* from au_actions a," +
             "(select e.* from au_user_action e union " +
         "(select c.user_sid, d.act_sid  from au_user_group c, au_group_action d " +
             "where d.grp_sid = c.grp_sid(+))) b " +
         "where b.act_sid = a.act_sid(+) and b.user_sid = '" + user_id + "'";
         */
        sql = "select a.* from au_actions a," +
              "(select e.user_sid, e.act_sid from au_user_action e union " +
              "select c.user_sid, d.act_sid  from au_user_group c, au_group_action d " +
              "where d.grp_sid = c.grp_sid) b " +
              "where b.act_sid = a.act_sid and b.user_sid = '" + user_id +
              "'";

        sql = sql + " order by a.act_action, a.act_class, a.act_desc";
        //System.out.println(sql);
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();

        return AUTransf.AUACTRsToObject(rs);
    }

    public static AUACTForm[] getAUACTListByUserID(Connection conn, String user_id) throws
    Exception {
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	ArrayList tmp = new ArrayList();

    	sql.append("select 0 flag, a.act_sid, a.act_class, a.act_action, a.act_desc from au_actions a ");
    	sql.append("where not exists (select 1 from au_user_action u ");
    	sql.append("where u.act_sid = a.act_sid ");
    	sql.append("and u.user_sid = " + user_id + ") ");
    	sql.append("and not exists (select 1 from au_user_group g, au_group_action r ");
    	sql.append("where g.grp_sid = r.grp_sid ");
    	sql.append("and r.ACT_SID = a.act_sid ");
    	sql.append("and g.user_sid = " + user_id + ") ");
    	sql.append("union ");
    	sql.append("select 1 flag, a.act_sid, a.act_class, a.act_action, a.act_desc from au_user_action u, au_actions a ");
    	sql.append("where u.act_sid = a.act_sid ");
    	sql.append("and user_sid = " + user_id + " ");
    	sql.append("and not exists (select 1 from au_user_group g, au_group_action g1 ");
    	sql.append("where g.user_sid = u.user_sid ");
    	sql.append("and g1.grp_sid = g.grp_sid ");
    	sql.append("and g1.act_sid = a.act_sid) ");
    	sql.append("union ");
    	sql.append("select distinct 2 flag, a.act_sid, a.act_class, a.act_action, a.act_desc ");
    	sql.append("from au_user_group g, au_group_action r, au_actions a ");
    	sql.append("where g.USER_SID = " + user_id + " ");
    	sql.append("and g.grp_sid = r.grp_sid ");
    	sql.append("and r.act_sid =  a.act_sid ");
    	sql.append("order by act_action, act_class, act_desc ");

    	//System.out.println(sql);
    	ps = conn.prepareStatement(sql.toString());
    	rs = ps.executeQuery();

    	return AUTransf.AUACTRsToObject2(rs);
    }


    public static AUGroupForm[] getAUGroupByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        sql = "select * from au_group ";

        if (!crit.equals("")) {
            sql = sql + " where " + crit;
        }
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();

        return AUTransf.AUGroupRsToObject(rs);
    }

    public static UserDepartmentForm[] getUserDepartmentByCrit(Connection conn,
            String crit) throws Exception {
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList tmp = new ArrayList();
        sql = "select * from au_user_department ";
        if (!crit.equals("")) {
            sql = sql + "where " + crit;
        }
        sql = sql+" order by DEPT_ID";

        //System.out.println(sql);
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();

        return AUTransf.UserDepartmentRsToObject(rs);
    }

    public static boolean deleteUserAccountByUser_id(Connection conn,
            String user_id) throws Exception {
        String sql = "";
        boolean flag = false;
        PreparedStatement ps = null;
        if (user_id != null && !user_id.equals("")) {
            sql = "delete au_user_account where user_id = '" + user_id + "'";
            ps = conn.prepareStatement(sql);
            flag = ps.execute();
        }
        return flag;
    }

    public static boolean deleteAUACTByAct_sid(Connection conn, String act_sid) throws
            Exception {
        String sql = "";
        boolean flag = false;
        PreparedStatement ps = null;
        if (act_sid != null && !act_sid.equals("")) {
            sql = "delete au_actions where act_sid = '" + act_sid + "'";
            ps = conn.prepareStatement(sql);
            flag = ps.execute();
        }
        return flag;
    }

    public static boolean deleteAUGroupByGrp_sid(Connection conn,
                                                 String grp_sid) throws
            Exception {
        String sql = "";
        boolean flag = false;
        PreparedStatement ps = null;
        if (grp_sid != null && !grp_sid.equals("")) {
            sql = "delete au_group where grp_sid = '" + grp_sid + "'";
            ps = conn.prepareStatement(sql);
            flag = ps.execute();
        }
        return flag;
    }

    public static boolean deleteUserDepartmentByDept_id(Connection conn,
            String dept_id) throws Exception {
        String sql = "";
        boolean flag = false;
        PreparedStatement ps = null;
        if (dept_id != null && !dept_id.equals("")) {
            sql = "delete au_user_department where dept_id = '" + dept_id + "'";
            ps = conn.prepareStatement(sql);
            flag = ps.execute();
        }
        return flag;
    }

    public static boolean deleteUserGroupByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        boolean flag = false;
        PreparedStatement ps = null;
        if (crit != null && !crit.equals("")) {
            sql = "delete from au_user_group where " + crit;
            //System.out.println(sql);
            ps = conn.prepareStatement(sql);
            flag = ps.execute();
        }
        return flag;
    }

    public static boolean deleteUserActionByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        boolean flag = false;
        PreparedStatement ps = null;
        if (crit != null && !crit.equals("")) {
            //sql = "delete au_user_action where " + crit;
            sql = "delete from au_user_action where " + crit;
            ps = conn.prepareStatement(sql);
            flag = ps.execute();
        }
        return flag;
    }

    public static boolean deleteGroupActionByCrit(Connection conn, String crit) throws
            Exception {
        String sql = "";
        boolean flag = false;
        PreparedStatement ps = null;
        if (crit != null && !crit.equals("")) {
            sql = "delete from au_group_action where " + crit;
            //System.out.println(sql);
            ps = conn.prepareStatement(sql);
            flag = ps.execute();
        }
        return flag;
    }

    public static int updateUserAccount(Connection conn, UserAccountForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "update au_user_account set employee_no=?, real_name=?, notes_id=?, dept_id=?, bo_user=?, bo_password=? " +
              "where user_id=?";
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareWriteUserAccount(ps, fm);

        return ps.executeUpdate();
    }

    public static int changeUserPassword(Connection conn, AULoginForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "update au_user_account set password=? " +
              "where user_id=?";
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareWriteChangePasswore(ps, fm);

        return ps.executeUpdate();
    }

    public static int updateAUACT(Connection conn, AUACTForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "update au_actions set act_class=?, act_desc=? " +
              "where act_sid=?";
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareWriteAUACT(ps, fm);

        return ps.executeUpdate();
    }

    public static int updateAUGroup(Connection conn, AUGroupForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "update au_group set grp_desc=? " +
              "where grp_sid=?";
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareWriteAUGroup(ps, fm);

        return ps.executeUpdate();
    }

    public static int updateUserDepartment(Connection conn,
                                           UserDepartmentForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "update au_user_department set dept_name=?,dept_group=? " +
              "where dept_id=?";
        //System.out.println(sql);
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareWriteUserDepartment(ps, fm);

        return ps.executeUpdate();
    }

    public static int insertUserAccount(Connection conn, UserAccountForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "insert into au_user_account (user_id,user_name,employee_no,real_name,password,notes_id,dept_id,bo_user,bo_password) " +
              "values (au_user_account_seq.nextval,?,?,?,?,?,?,?,?)";
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareInsertUserAccount(ps, fm);

        return ps.executeUpdate();
    }

    public static int insertAUACT(Connection conn, AUACTForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "insert into au_actions (act_sid,act_action,act_class,act_desc) " +
              "values (au_actions_seq.nextval,?,?,?)";
        //System.out.println(sql);
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareInsertAUACT(ps, fm);

        return ps.executeUpdate();
    }

    public static int insertAUGroup(Connection conn, AUGroupForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "insert into au_group (grp_sid,grp_name,grp_desc) " +
             "values (au_group_seq.nextval,?,?)";

        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareInsertAUGroup(ps, fm);

        return ps.executeUpdate();
    }

    public static int insertUserGroup(Connection conn, AUAssignForm fm) throws
            Exception {
        String sql = "";
        String[] grp_sid = fm.getGrp_sid();
        int flag = 0;
        PreparedStatement ps = null;
        sql = "insert into au_user_group (grp_sid,user_sid) " +
              "values (?,?)";
        ps = conn.prepareStatement(sql);
        ps.setString(2, fm.getUser_id());
        if (grp_sid != null && grp_sid.length > 0) {
            for (int i = 0; i < grp_sid.length; i++) {
                ps.setString(1, grp_sid[i]);
                flag = ps.executeUpdate();
            }
        }

        return flag;
    }

    public static int insertUserAction(Connection conn, AUAssignForm fm) throws
            Exception {
        String sql = "";
        String[] act_sid = fm.getAct_sid();
        int flag = 0;
        PreparedStatement ps = null;
        /*sql = "insert into au_user_action (act_sid,user_sid) " +
              "values (?,?)";
         */
        sql = "insert into au_user_action (act_sid,user_sid) " +
              "values (?,?)";
        ps = conn.prepareStatement(sql);
        ps.setString(2, fm.getUser_id());
        if (act_sid == null)
        	return 1;
        if (act_sid != null && act_sid.length > 0) {
            for (int i = 0; i < act_sid.length; i++) {
                ps.setString(1, act_sid[i]);
                flag = ps.executeUpdate();
            }
        }

        return flag;
    }

    public static int insertGroupAction(Connection conn, AUAssignForm fm) throws
            Exception {
        String sql = "";
        String[] act_sid = fm.getAct_sid();
        int flag = 0;
        PreparedStatement ps = null;
        sql = "insert into au_group_action (act_sid,grp_sid) " +
              "values (?,?)";

        ps = conn.prepareStatement(sql);
        ps.setString(2, fm.getGrp_id());
        if (act_sid != null && act_sid.length > 0) {
            for (int i = 0; i < act_sid.length; i++) {
                ps.setString(1, act_sid[i]);
                //System.out.println(ps.toString());
                flag = ps.executeUpdate();
            }
        }

        return flag;
    }

    public static int insertUserDepartment(Connection conn,
                                           UserDepartmentForm fm) throws
            Exception {
        String sql = "";
        PreparedStatement ps = null;
        sql = "insert into au_user_department (dept_id,dept_name,dept_group) " +
              "values (?,?,?)";
        ps = conn.prepareStatement(sql);
        ps = AUTransf.prepareInsertUserDepartment(ps, fm);

        return ps.executeUpdate();
    }

    public static int insertTDSLog(Connection conn, String user,
                                   String actionName) throws Exception {
        String sql = "";
        int flag = 0;
        PreparedStatement ps = null;
        sql = "insert into au_oi_log (action,user_name,log_time) " +
        "values (?,?,sysdate)";
        //sql = "insert into au_oi_log (action,user_name,log_time) " +
        //      "values (?,?,now())";
        ps = conn.prepareStatement(sql);
        ps.setString(2, user);
        ps.setString(1, actionName);
        flag = ps.executeUpdate();

        return flag;
    }
    
	public static boolean isITAdmin(String userId) {
		boolean result = false;
		Connection con = null;

		try {
			con = DBConnection.getConnection();
			String sql = "SELECT count(*) FROM BA_MXIC_EMP WHERE DEPT_ID IN ('MKD20','MY420') AND EMP_NO = ?";
			int count = GPRSDB.qryCnt(con, sql, new Object[] { userId });
			if (count > 0)
				result = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TDSLogger.println(e);
		} finally {
			DBConnection.close(con);
		}

		return result;
	}
	
}
