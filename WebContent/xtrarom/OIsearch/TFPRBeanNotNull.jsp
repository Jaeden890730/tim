<!-- /xtrarom/OIsearch/TFPRBeanNotNull.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>

<html:html>



<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="JavaScript" type="text/JavaScript">
</script>
</head>

<body topmargin="0" leftmargin="0">

<%@  include file="../../index-menu.jsp"%>
<table width="100%" border="0" class="bg1">
	<tr>

		<td valign="top">
		<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
			<tr>
				<td width="100%" height="490" valign="top"><br>
				<table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
					<tr>
						<td width="100%" height="25" class="title2">
						<img src="../image/arrow.gif" width="5" height="14" hspace="3" alt="">
						<font size="4"><b>TIM<font size="2">　WS Test Route</font></b></font></td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
				<form name="form1" action="<html:rewrite page="/Login/oiMainControlActionX.do"/>" method="post">
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr>
									<td height="20" align="left" >
										<input type="button" value="<Back" onclick="history.go(-1);return true;" class="button1">
									</td>
								</tr>
						</table>


						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
								<tr align="left" class="list1">
									<td height="20" align="left" class="title1">
										Product_Body
									</td>
									<td height="20" align="left" class="title1" width="12.5%">
										Brand
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Version
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Route_Name
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Step_Seq
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Step_Name
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Test_time
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Time_unit
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Temperature
              						</td>
              						<td height="20" align="left" class="title1" width="12.5%">
										Remark
              						</td>
								</tr>
				<logic:present name="list" >
		  		<logic:iterate id="tmp" name="list" >
		  						<tr class="list1">
									<td align="left" height="20"><bean:write name="tmp" property="product_body"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="brand"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="version"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="route_name"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="step_seq"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="step_name"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="test_time"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="time_unit"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="temperature"/>
									</td>
									<td align="left" height="20"><bean:write name="tmp" property="remark"/>
									</td>
								</tr>
							</td>
				  </logic:iterate>
				  </logic:present>
						</table>
				<logic:notPresent name="list" >
						<table width="95%" cellspacing="1" cellpadding="0" align="center">
	                		<tr>
	                			<td class="list1">
	                		此次搜尋共　'0'　筆資料
	                			</td>
			  				</tr>
		  				</table>
		  		</logic:notPresent>
		  		<logic:present name="list" >
		  				<table width="95%" cellspacing="1" cellpadding="0" align="center">
	                		<tr>
	                			<td class="list1">
	                		<font color="red" size=3><b>因為已經有上述資料被參考，故不能修改	</b></font>
	                			</td>
			  				</tr>
		  				</table>
		  		</logic:present>　


						<table width="95%" cellspacing="1" cellpadding="0" align="center">
								<tr>
									<td height="20" align="left" >
										<input type="button" value="<Back" onclick="history.go(-1);return true;" class="button1">
									</td>
								</tr>
						</table>
				</form>

				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<%@  include file="../../index-down.jsp"%>

</body>
</html:html>