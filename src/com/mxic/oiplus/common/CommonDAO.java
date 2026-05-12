/**
 * @(#)BasicDAO.java  VER.1.00 22/10/2002.
 *
 * Copyright (c) 2002 CIM Information Technology Ltd.
 *
 * This Software is the confidential and proprietary information of
 * CIM Information Technology Ltd.You shall not disclose such
 * Confidential Information and use it only in accordance with the terms
 * of the licence agrement you entered  with CIT.
 *
 */
package com.mxic.oiplus.common;
import java.sql.*;
import java.util.*;
import com.mxic.oiplus.resource.*;
import com.mxic.oiplus.util.*;


/**
 *
 *  <p>
 *  This bean commonly accessed by most of the  subsystem,to
 *  do database operations cum business logic for BASIC tables.
 *  </p>
 *
 */
public class CommonDAO
{

 /*
  *<p>Empty constructor</p>
  */
  public CommonDAO()
  {}

/* <p>This method is used to Select records by given Aruguments </p>
 *
 * @param con The rich resource connection object is, passed as
 * an argument.
 *
 * @param prod_type The value of the Product type, passed as
 * an String.
 *
 * @param prod_type The value of the Product code, passed as
 * an String.
 *
 * @param prod_type The value of the Product name key, passed as
 * an String.
 *
 * @return results The value of the table row, returned as
 * an ArrayList.
 */
  
	public ArrayList selectAEBGrade()
	{
		ArrayList results=null;
    Connection con=null;
		try
		{
				DBConnection ODBConnection=new DBConnection();
				con=ODBConnection.getConnection();
				CommonDB OCommonDB=new CommonDB();
				results = OCommonDB.getAEBGrade(con);

		}
		catch(Exception ex)
		{
				TDSLogger.println(ex);
		}
    finally
     {
          DBConnection.close(con);
     }
		return results;
 }
	
	public ArrayList selectHWConfigureItem()
	{
		ArrayList results=null;
    Connection con=null;
		try
		{
				DBConnection ODBConnection=new DBConnection();
				con=ODBConnection.getConnection();
				CommonDB OCommonDB=new CommonDB();
				results = OCommonDB.getHWConfigureItem(con);

		}
		catch(Exception ex)
		{
				TDSLogger.println(ex);
		}
    finally
     {
          DBConnection.close(con);
     }
		return results;
 }
          
	
	public ArrayList selectSortRouteCodeItem(String productBody, String brand, String maskOption)
	{
		ArrayList results=null;
    Connection con=null;
		try
		{
				DBConnection ODBConnection=new DBConnection();
				con=ODBConnection.getConnection();
				CommonDB OCommonDB=new CommonDB();
				results = OCommonDB.getSortRouteCodeItem(con, productBody, brand, maskOption);

		}
		catch(Exception ex)
		{
				TDSLogger.println(ex);
		}
    finally
     {
          DBConnection.close(con);
     }
		return results;
 }

}//End class
