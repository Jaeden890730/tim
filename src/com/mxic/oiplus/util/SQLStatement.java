/******************************************************************************************************/
//	Author	: 	J. Jeyandran
//	Date	:	May 15, 2003.
/******************************************************************************************************/

package com.mxic.oiplus.util;

import java.sql.*;

import com.mxic.oiplus.util.*;

public class SQLStatement
{
	public void setStatement(PreparedStatement preparedStatement,int index,Object value,int sqlType)
	{
		try
		{
			TDSLogger.println(index+" "+value);

			if(sqlType == Types.VARBINARY || sqlType == Types.VARCHAR)
			{
				if(value!=null)
				{
					preparedStatement.setString(index,value.toString());
				}
				else
				{
					preparedStatement.setNull(index,Types.VARCHAR);
				}
			}
			else if(sqlType == Types.NUMERIC || sqlType == Types.INTEGER || sqlType == Types.FLOAT || sqlType == Types.DOUBLE || sqlType == Types.DECIMAL || sqlType == Types.BINARY)
			{
				if(value!=null)
				{
					preparedStatement.setObject(index,value);
				}
				else
				{
					preparedStatement.setNull(index,Types.NUMERIC);
				}
			}
			else if(sqlType == Types.DATE || sqlType == Types.TIME || sqlType == Types.TIMESTAMP)
			{
				if(value!=null)
				{
					if(value instanceof Timestamp)
					{
						preparedStatement.setTimestamp(index,(Timestamp) value);
					}
					else
					{
						preparedStatement.setTimestamp(index,new Timestamp(((java.sql.Date)value).getTime()));
					}
				}
				else
				{
					preparedStatement.setTimestamp(index,null);
				}
			}
		}
		catch(SQLException sqlException)
		{
//			TDSLogger.println("Exception while setting value to PreparedStatement "+ sqlException.getMessage());
			TDSLogger.println(sqlException);
			sqlException.printStackTrace();
		}
	}
}
