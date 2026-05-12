/******************************************************************************************************/
//	Author	: 	J. Jeyandran
//	Date	:	September 26, 2003.
/******************************************************************************************************/

package com.mxic.oiplus.util;

import java.lang.reflect.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.mxic.oiplus.resource.*;

public class DataHandlerUtil extends SQLStatement
{
	private String ExceptionMessage;
    public Collection<Object> getData(Connection connection,String query,Class<?> loadedBean,Object[] whereConditions) throws DataHandlerException
    {
        Collection<Object> lists = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Vector<Object> param = new Vector<Object>();

        try
        {
            //			if(connection==null || query == null || className == null)
            if(connection==null || query == null)
            {
                throw new DataHandlerException("Provide Proper Parameters for getData Method.");
            }

            //			Class loadedBean = Class.forName(className);

            log("=================");
            log(query);
            log("=================");

            preparedStatement = connection.prepareStatement(query);

            if(whereConditions!=null)
            {
                int number = whereConditions.length;

                for(int i=0;i<number;i++)
                {
                    Object value = whereConditions[i];
                    param.add(value);
//                    log("Parameter ---------------> "+(i+1)+" = \'"+value+"\'");

                    if(value!=null)
                    {
                        if(value instanceof String)
                        {
                            //log("Setting as String");
                            setStatement(preparedStatement,i+1,value.toString().trim(),java.sql.Types.VARBINARY);
                        }
                        else if(value instanceof Number)
                        {
                            //log("Setting as Number");
                            setStatement(preparedStatement,i+1,value,java.sql.Types.NUMERIC);
                        }
                        else if(value instanceof java.sql.Date)
                        {
                            //log("Setting as Date");
                            setStatement(preparedStatement,i+1,value,java.sql.Types.DATE);
                        }
                        else
                        {
                            //log("Finally String");
                            setStatement(preparedStatement,i+1,value.toString().trim(),java.sql.Types.VARBINARY);
                        }
                    }
                    else
                    {
                        preparedStatement.setNull(i+1,java.sql.Types.VARBINARY);
                    }
                }
                log("Parameter --> " + param);
            }

            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            int columnCount = resultSetMetaData.getColumnCount();

            lists=new ArrayList<Object>();

            while(resultSet.next())
            {
                Object object = loadedBean.newInstance();

                for(int c=1;c<=columnCount;c++)
                {
                    String columnName = resultSetMetaData.getColumnName(c);

                    Field field = null;
                    try{
                        field = loadedBean.getField(columnName);
                    }catch(NoSuchFieldException e){
                        //TDSLogger.println(e);
                        field = null;
                    }

                    if(field==null)
                    {
                        continue;
                    }

                    int sqlType = resultSetMetaData.getColumnType(c);

                    if(sqlType == Types.VARBINARY || sqlType == Types.VARCHAR || sqlType == Types.CHAR)
                    {
                        field.set(object,resultSet.getString(c));
                    }
                    else if(sqlType == Types.NUMERIC || sqlType == Types.INTEGER || sqlType == Types.FLOAT || sqlType == Types.DOUBLE || sqlType == Types.DECIMAL || sqlType == Types.BINARY)
                    {
                        field.set(object,resultSet.getObject(c));
                    }
                    else if(sqlType == Types.DATE || sqlType == Types.TIME || sqlType == Types.TIMESTAMP)
                    {
                        Timestamp value = resultSet.getTimestamp(c);

                        if(value!=null)
                        {
                            field.set(object,new java.sql.Date(value.getTime()));
                        }
                    }
                }

                lists.add(object);
            }
        }
        catch(Exception e)
        {
            //TDSLogger.println(e);
            TDSLogger.println(e);
            throw new DataHandlerException(e.getMessage());
        }
        finally
        {
            try
            {
                if(resultSet!=null)
                {
                    resultSet.close();
                    resultSet = null;
                }
                if(preparedStatement!=null)
                {
                    preparedStatement.close();
                    preparedStatement = null;
                }
            }
            catch(Exception e)
            {
            }
        }

        if(lists.size() < 1)
        {
            log("--------------");
            log("<< Warning >> No Rows has Been Selected");
            log("--------------");
        }
        else
        {
            log("--------------");
            log("<< Message >> "+lists.size()+" Rows Selected.");
            log("--------------");
        }

        return lists;
    }

    public void log(String text)
    {
        TDSLogger.println(text);
        //TDSLogger.println(text);
    }

    public ArrayList<String[]> queryByPrepareSQL(String preparesqlstring, Object[] objs) throws Exception{
        Connection conn = null;
        ArrayList<String[]> result = new ArrayList<String[]>();
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            rs = queryByPrepareSQL(conn, preparesqlstring, objs);
            ResultSetMetaData rsmd = rs.getMetaData();

            String[] rst = null;
            while (rs.next()) {
            	rst = new String[rsmd.getColumnCount()];
                for (int i=0; i<rsmd.getColumnCount(); i++) {
                    rst[i] = rs.getObject(i + 1).toString();
                }
                result.add(rst);
            }
            rsmd = null;
            return result;
        }catch(Exception e){
            throw e;
        }finally{
            if (rs != null) {
            	if (rs.getStatement() != null) {
            		rs.getStatement().close();
            	}
            	rs = null;
            }
            DBConnection.close(conn);
        }
    }

    public ResultSet queryByPrepareSQL(Connection conn, String preparesqlstring, Object[] objs) throws Exception{
        PreparedStatement pstmt = conn.prepareStatement(preparesqlstring);
        // set parameter values
        if(objs!=null){
            for (int i = 0; i < objs.length; i++) {
                pstmt.setObject(i + 1, objs[i]);
            }
        }
        // execute SQL statement
        return pstmt.executeQuery();
    }

    public int updateByPrepareSQL(String preparesqlstring, Object[] objs) throws Exception{
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            int rs = updateByPrepareSQL(conn, preparesqlstring, objs);
            return rs;
        }catch(Exception e){
            throw e;
        }finally{
            DBConnection.close(conn);
        }
    }

    public int updateByPrepareSQL(Connection conn, String preparesqlstring, Object[] objs) throws Exception{
        PreparedStatement pstmt = conn.prepareStatement(preparesqlstring);
        int result = 0;
        // set parameter values
        if(objs!=null){
            for (int i = 0; i < objs.length; i++) {
                pstmt.setObject(i + 1, objs[i]);
            }
        }
        // execute SQL statement
        result = pstmt.executeUpdate();
        pstmt.close();
        return result;
    }

    public HashMap<String, String>[] getDataBySql(String sql){
        Connection conn = null;
        try{
            conn = DBConnection.getConnection();
            HashMap<String, String>[] rs = getDataBySql(conn,sql);
            return rs;
        }catch(Exception e){
            TDSLogger.println(e);
        }finally{
            DBConnection.close(conn);
        }
        return null;
    }

    public HashMap<String, String>[] getDataBySql(Connection conn, String sql) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<HashMap<String, String>> tmp = new ArrayList<HashMap<String, String>>();
        TDSLogger.println(sql);
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        while(rs.next()){
            tmp.add(setRStoHashMap(rs));
        }
        ps.close();
        return (HashMap<String, String>[]) tmp.toArray(new HashMap[0]);
    }

    public HashMap<String, String>[] getDataBySql(Connection conn, String sql, Object[] whereConditions) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<HashMap<String, String>> tmp = new ArrayList<HashMap<String, String>>();
        TDSLogger.println(sql);
        ps = conn.prepareStatement(sql);
        Vector<Object> param = new Vector<Object>();
        if(whereConditions!=null)
        {
            int number = whereConditions.length;

            for(int i=0;i<number;i++)
            {
                Object value = whereConditions[i];

                //log("Parameter ---------------> "+(i+1)+" = \'"+value+"\'");
                param.add(value);

                if(value!=null)
                {
                    if(value instanceof String)
                    {
                        //log("Setting as String");
                        setStatement(ps,i+1,value.toString().trim(),java.sql.Types.VARBINARY);
                    }
                    else if(value instanceof Number)
                    {
                        //log("Setting as Number");
                        setStatement(ps,i+1,value,java.sql.Types.NUMERIC);
                    }
                    else if(value instanceof java.sql.Date)
                    {
                        //log("Setting as Date");
                        setStatement(ps,i+1,value,java.sql.Types.DATE);
                    }
                    else
                    {
                        //log("Finally String");
                        setStatement(ps,i+1,value.toString().trim(),java.sql.Types.VARBINARY);
                    }
                }
                else
                {
                	ps.setNull(i+1,java.sql.Types.VARBINARY);
                }
            }
        }
        if (param.size() > 0)
			TDSLogger.println("Parameter ==> " + param);

        rs = ps.executeQuery();
        while(rs.next()){
            tmp.add(setRStoHashMap(rs));
        }
        ps.close();
        return (HashMap<String, String>[]) tmp.toArray(new HashMap[0]);
    }

    private HashMap<String, String> setRStoHashMap(ResultSet rs) throws Exception{
        HashMap<String, String> hm = new HashMap<String, String>();
        ResultSetMetaData rsmd = rs.getMetaData();
        for(int i=1;i<=rsmd.getColumnCount();i++){
            String columnName = rsmd.getColumnName(i);
            hm.put(columnName,rs.getString(columnName));
        }
        return hm;
    }

    public String insert(String tablename, HashMap<String, Object> values){
        String flag = null;
        Connection conn=null;
        try{
            conn = DBConnection.getConnection();

            insert(conn,tablename,formatStamtment(values));
            flag = "success";

        }catch(Exception e){
            TDSLogger.println(e);
            flag = "Failure !! Message:" + e.getMessage();
            DBConnection.rollback(conn);
        }
        finally {
            DBConnection.close(conn);
        }
        return flag;
    }

    public void insertObject(Connection conn,String tablename ,Object bean)
    throws Exception{
        if((conn == null) || (bean == null) || (tablename == null)){
            return;
        }

        HashMap<String, Object> values = object2HashMap(bean);
        //insert
        insert(conn, tablename, values);
        return;
    }

    public static HashMap<String, Object> object2HashMap(Object bean){

        if(bean == null){
          return null;
        }
        HashMap<String, Object> values = new HashMap<String, Object>();
        String propname = null;
        String fieldname = null;
        Object fieldvalue = null;
        Method[] methods = bean.getClass().getDeclaredMethods();
        for(int i=0; i<methods.length; i++){
          propname = methods[i].getName();
          if(propname.startsWith("get")){
            try{
              fieldname = propname.substring(3);
              fieldvalue = methods[i].invoke(bean, null);
              if(fieldvalue != null){
                values.put(fieldname, fieldvalue);
              }
            }
            catch(Exception e){
              TDSLogger.println(e);
              values =  null;
            }
          }
        }
        return values;
      }

    private static HashMap<String, Object> formatStamtment(HashMap values){
        if(values != null && values.size()>0){
            Iterator<String> keys = values.keySet().iterator();
            ArrayList<Object> rmKeys = new ArrayList<Object>();
            while(keys.hasNext()){
                Object key = keys.next();
                Object value = values.get(key);
                if(value == null || value.equals(""))
                    rmKeys.add(key);
            }
            for(int i=0;i<rmKeys.size();i++){
                values.remove(rmKeys.get(i));
            }
        }
        return values;
    }

    /**
     * insert date into the table, with HaspMap ("filedName", Object)
     * @param conn Connection
     * @param tablename
     * @param values
     * @return true for success, false for fail
     * @throws Exception
     */
    public int insert(Connection conn, String tablename, HashMap<String, ?> values) throws Exception{
        // Generate an array of question marks for the SQL template parameters
        String [] paras = new String[values.size()];
        for (int i = 0; i < paras.length; i++) {
            paras[i] = "?";
        }
        String [] fieldnames = new String[values.size()];
        Object [] fieldvalues = new Object[values.size()];
        Iterator<String> itr = values.keySet().iterator();
        int index=0;
        while(itr.hasNext()){
            fieldnames[index++]=(String)itr.next();
            fieldvalues[index-1]=values.get(fieldnames[index-1]);
        }
        // Prepare the template
        String SQLstr = "INSERT INTO " + tablename + " ( " +
        getSQLList(fieldnames) + " ) VALUES ( " +
        getSQLList(paras) + " ) ";
        PreparedStatement pstmt = conn.prepareStatement(SQLstr);
//        TDSLogger.println(SQLstr);
        // set parameter values
        for (int i = 0; i < fieldnames.length; i++) {
            pstmt.setObject(i + 1, fieldvalues[i]);
//            TDSLogger.println(i + ":" + (String)fieldvalues[i]);
        }
        // execute SQL statement
        int succ = pstmt.executeUpdate();
        pstmt.close();
        return succ;
    }

    /**
     * Assembly "arg1,arg2,...argn" type of string  to use in
     * SQL statements from the input array of strings.
     * @param fields
     * @return
     * @throws Exception
     */
    protected static String getSQLList(String [] fields) throws Exception {

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fields.length -1; i++) {
            result.append( fields[i] );
            result.append( " , " );
        }
        result.append( fields[fields.length-1] );
        return result.toString();
    }

    public String update(String tablename, HashMap<String, Object> values, HashMap<String, Object> conditions){
        String flag = null;
        Connection conn = null;
        try{
            conn = DBConnection.getConnection();
            update(conn,tablename,formatStamtment(values),formatStamtment(conditions));
            flag = "success";

        }catch(Exception e){
            TDSLogger.println(e);
            flag = "Failure !! Message:" + e.getMessage();
            DBConnection.rollback(conn);
        }
        finally {
            DBConnection.close(conn);
        }
        return flag;
    }

    /**
     *
     * @param conn
     * @param tablename
     * @param values
     * @param conditions
     * @return
     * @throws Exception
     */
    public int update(Connection conn, String tablename, HashMap<String, Object> values, HashMap<String, ?> conditions) throws Exception{
        // Generate an array of question marks for the SQL template parameters
        String [] paras = new String[values.size()];
        for (int i = 0; i < paras.length; i++) {
            paras[i] = "?";
        }
        String [] fieldnames = new String[values.size()];
        Object [] fieldvalues = new Object[values.size()];
        Iterator<String> itr = values.keySet().iterator();
        int index=0;
        while(itr.hasNext()){
            fieldnames[index++]=(String)itr.next();
            fieldvalues[index-1]=values.get(fieldnames[index-1]);
        }
        String [] condnames = new String[conditions.size()];
        Object [] condvalues = new Object[conditions.size()];
        itr = conditions.keySet().iterator();
        index=0;
        while(itr.hasNext()){
            condnames[index++]=(String)itr.next();
            condvalues[index-1]=conditions.get(condnames[index-1]);
        }

        // Prepare the template
        String SQLstr = "UPDATE " + tablename + " SET " +
        getSETList(fieldnames) + " WHERE  (" +
        getANDList(condnames) + ") ";

        PreparedStatement pstmt = conn.prepareStatement(SQLstr);
        // set parameter values
        for (int i = 0; i < fieldnames.length ; i++) {
            pstmt.setObject(i + 1, fieldvalues[i]);
        }
        for (int i = fieldnames.length; i < fieldnames.length + condnames.length ; i++) {
            pstmt.setObject(i + 1, condvalues[i-fieldnames.length]);
        }
        // execute SQL statement
        int succ = pstmt.executeUpdate();
        pstmt.close();
        return succ;
    }

    /**
     * Assembly "arg1=? , arg2=? , ...argn=?" type of string  to use in
     * SQL statements from the input array of strings.
     * @param fields
     * @return
     * @throws Exception
     */
    protected String getSETList(String [] fields) throws Exception {

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fields.length -1; i++) {
            result.append( fields[i] + " = ? " );
            result.append( " , " );
        }
        result.append(  fields[fields.length-1] + " = ? "  );
        return result.toString();
    }

    /**
     * Assembly "arg1=? AND arg2=? AND ...argn=?" type of string  to use in
     * SQL statements from the input array of strings.
     * @param fields
     * @return
     * @throws Exception
     */
    protected String getANDList(String [] fields) throws Exception {

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < fields.length -1; i++) {
            result.append( "(" + fields[i] + " = ? )" );
            result.append( " AND " );
        }
        result.append(  "(" + fields[fields.length-1] + " = ? )"  );
        return result.toString();
    }

    public String delete(String tablename,HashMap<String, Object> conditions){
        String flag=null;
        Connection conn = null;
        try{
            conn = DBConnection.getConnection();
            if(conditions == null || conditions.size()<=0){
                conn.createStatement().execute("delete "+tablename);
            }else{
                delete(conn,tablename,formatStamtment(conditions));
            }
            flag = "success";
        }catch(Exception e){
            TDSLogger.println(e);
            flag = "Failure !! Message:" + e.getMessage();
            DBConnection.rollback(conn);
        }
        finally {
            DBConnection.close(conn);
        }
        return flag;

    }

    /**
    *
    * @param conn
    * @param tablename
    * @param conditions
    * @return
    * @throws Exception
    */
    protected int delete(Connection conn, String tablename, HashMap<String, Object> conditions) throws Exception{

        // Generate an array of question marks for the SQL template parameters
        String [] paras = new String[conditions.size()];
        for (int i = 0; i < paras.length; i++) {
            paras[i] = "?";
        }
        String [] fieldnames = new String[conditions.size()];
        Object [] fieldvalues = new Object[conditions.size()];
        Iterator<String> itr = conditions.keySet().iterator();
        int index=0;
        while(itr.hasNext()){
            fieldnames[index++]=(String)itr.next();
            fieldvalues[index-1]=conditions.get(fieldnames[index-1]);
        }

        // Prepare the template
        String SQLstr = "DELETE " + tablename + " WHERE ( " +
                        getANDList(fieldnames) + " ) ";
        PreparedStatement pstmt = conn.prepareStatement(SQLstr);
        // set parameter values
        for (int i = 0; i < fieldnames.length; i++) {
            pstmt.setObject(i + 1, fieldvalues[i]);
        }
        // execute SQL statement
        int succ = pstmt.executeUpdate();
        pstmt.close();
        return succ;
    }

    public ResultSet queryBySQL(Connection conn, String sqlstring) throws Exception{
        PreparedStatement pstmt = conn.prepareStatement(sqlstring);
        ResultSet res = pstmt.executeQuery();
        //pstmt.close();
        //pstmt = null;
        return res;
      }

    public String getFieldSqlStmt(String fieldName, Object limit) {
        if (limit == null) {
            return "";
        } else if (limit instanceof List) {
            return getFieldStmt(fieldName, (List<Object>) limit);
        } else if (limit instanceof Boolean) {
            if (fieldName.startsWith("!")) {
                return fieldName.substring(1) + " is not null";
            } else {
                return fieldName + " is null";
            }
        } else {
            String tmp = limit.toString();
            if (tmp.length() < 1) {
                return "";
            }
            if (tmp.indexOf(",") == -1) {
                return getFieldStmt(fieldName, tmp);
            }
            StringTokenizer token = new StringTokenizer(tmp, ",");
            ArrayList<Object> list = new ArrayList<Object>();
            StringBuffer result = new StringBuffer();
            String value = "";
            while (token.hasMoreTokens()) {
                value = token.nextToken();
                if (value.indexOf("*") == -1) {
                    list.add(value);
                } else {
                    result.append(getFieldStmt(fieldName, value));
                    result.append(" or ");
                }
            }
            value = getFieldStmt(fieldName, list);
            if (value.length() <= 0) {
                result.delete(result.length() - 4, result.length());
            }
            result.append(value);
            if (result.length() > 0) {
                result.insert(0, "( ");
                result.insert(result.length(), " )");
            }
            return result.toString();
        }
    }

    private String getFieldStmt(String fieldName, List<Object> queryList) {
        StringBuffer result = new StringBuffer();
        int size = queryList.size();
        if (size == 0) {
            return "";
        } else if (size == 1) {
            result.append(getFieldStmt(fieldName, queryList.get(0).toString()));
        } else if (queryList.get(0) instanceof Date && size == 2) {
            result.append(fieldName);
            result.append(" between");
            result.append(" to_date('");
            result.append(((Date) queryList.get(0)).toString());
            if (((Date) queryList.get(0)).toString().indexOf(":") < 0) {
                result
                        .append(" 00:00:00','yyyy-mm-dd HH24:MI:SS') and to_date('");
            } else {
                result.append("','yyyy-mm-dd HH24:MI:SS') and to_date('");
            }
            result.append(((Date) queryList.get(1)).toString());
            if (((Date) queryList.get(0)).toString().indexOf(":") < 0) {
                result.append(" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
            } else {
                result.append("','yyyy-mm-dd HH24:MI:SS')");
            }
        } else if (queryList.get(0) instanceof DateString && size == 2) {
            result.append(fieldName);
            result.append(" between");
            result.append(" to_date('");
            result.append(((DateString) queryList.get(0)).toString());
            if (((DateString) queryList.get(0)).toString().indexOf(":") < 0) {
                result.append(" 00:00:00','yyyy-mm-dd HH24:MI:SS') and to_date('");
            } else {
                result.append("','yyyy-mm-dd HH24:MI:SS') and to_date('");
            }
            result.append(((DateString) queryList.get(1)).toString());
            if (((DateString) queryList.get(1)).toString().indexOf(":") < 0) {
                result.append(" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
            } else {
                result.append("','yyyy-mm-dd HH24:MI:SS')");
            }
        } else {
            if (fieldName.startsWith("!")) {
                result.append(fieldName.substring(1));
                result.append(" not ");
            } else {
                result.append(fieldName);
            }
            result.append(" in (");
            for (int i = 0; i < size; i++) {
                result.append("'");
                result.append(replace(queryList.get(i)));
                result.append("',");
                //result.append(getFieldStmt(fieldName,queryList.get(i).toString()));
                //result.append(" or ");
            }
            result = result.delete(result.length() - 1, result.length());
            //result = result.delete(result.length() - 3 ,result.length());
            result.append(")");
        }
        return result.toString();
    }

    private String getFieldStmt(String fieldName, String limit) {
        StringBuffer result = new StringBuffer();
        boolean notEquit = false;
        if (fieldName.startsWith("!")) {
            result.append(fieldName.substring(1));
            notEquit = true;
        } else if (fieldName.startsWith("#")) {
            result.append(fieldName.substring(1));
            result.append(" = ");
            result.append(limit);
            return result.toString();
        } else {
            result.append(fieldName);
        }
        if (limit == null || limit.equals("*") || limit.length() < 1) {
            return "";
        }
        if (limit.indexOf("*") == -1 && limit.indexOf('%') == -1) {
            if (notEquit) {
                result.append(" <> '");
            } else {
                result.append(" = '");
            }
            result.append(replace(limit));
            result.append("'");
        } else {
            limit = limit.replace('*', '%');
            if (notEquit)
                result.append(" not ");
            result.append(" like '");
            result.append(replace(limit));
            result.append("'");
        }
        return result.toString();
    }

    private String replace(Object value) {
        return StringUtil.replace((String) value, "'", "''", true);
        //return StringUtil.replace(((String)value).trim(),"'","''",true);
    }


    public static oracle.sql.CLOB getEmptyClob(Connection conn) {
        String query1 = "select empty_clob() from dual";
        oracle.sql.CLOB clob = null;
        
        try {
        PreparedStatement pstmt = conn.prepareStatement(query1);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        clob = (oracle.sql.CLOB) rs.getObject(1);
        rs.close();
        pstmt.close();
        } catch (Exception e) {
        	TDSLogger.println(e.getMessage());
        }
        return clob;
    }
	public String getLastExceptionMessage() {
		return ExceptionMessage;
	}
}
