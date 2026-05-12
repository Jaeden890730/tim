<!-- /xtrarom/OIsearch/OIUpdateWsTestRoute.jsp -->

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

function check_step() {
	var array = new Array(10);
	array[0] = document.form1.txt_step1.value;
	array[1] = document.form1.txt_step2.value;
	array[2] = document.form1.txt_step3.value;
	array[3] = document.form1.txt_step4.value;
	array[4] = document.form1.txt_step5.value;
	array[5] = document.form1.txt_step6.value;
	array[6] = document.form1.txt_step7.value;
	array[7] = document.form1.txt_step8.value;
	array[8] = document.form1.txt_step9.value;
	array[9] = document.form1.txt_step10.value;

	var check_flag = true;
	var flag = true;
	var index = 0;
	for (i=0; i<10; i++) {
		if ((array[i] == null) || (array[i] == "") ) {
			if (flag == true)
				index = i;
			flag = false;
		} else {
			if (flag == false)
				check_flag = false;
		}
	}
	if (check_flag == false) {
		alert('Step 不連續');
		return false;
	}
/*
	for (i=0; i<index; i++) {
		for (j=0; j<i; j++) {
	    	if (array[i] == array[j]) {
				check_flag = false;
				alert('Step 中有重覆項目 : ' + array[i]);
				return false;
			}
		}
	}
*/
	return true;

}
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
						<font size="4"><b>TIM<font size="2">　Update WS/FT Test Route</font></b>
						</font>
						</td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
				<form name="form1" action="<html:rewrite page="/OIsearch/oiUpdateWSTestRouteActionX.do"/>" method="post" onsubmit="return check_step();">
						<table width="95%" cellspacing="0" cellpadding="0" align="center">
								<tr>
						<logic:present name="list" >
									<td height="20" align="left" width="53">
										<input name="update" type="submit" value="Update" class="button1">
										<input type="hidden" name="btControl" value="bt_update">
									</td>
						</logic:present>
									<td>
										<input type="button" value="Cancel Update" onclick="history.go(-1);return true;" class="button1">
									</td>
								</tr>
						</table>
						<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">　
				<logic:present name="list" >
		  		<logic:iterate id="tmp" name="list" >
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Route Name</b></font>
									</td>
									<td height="20" align="left" width="581" >
              							<bean:write name="tmp" property="route_name"/>
              							<input name="txt_routename" type = "hidden" value="<bean:write name="tmp" property="route_name"/>">
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　1</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step1">
										    <option value="<bean:write name="tmp" property="step1"/>"><bean:write name="tmp" property="step1"/></option>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　2</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step2">
										    <option value="<bean:write name="tmp" property="step2"/>"><bean:write name="tmp" property="step2"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　3</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step3">
										    <option value="<bean:write name="tmp" property="step3"/>"><bean:write name="tmp" property="step3"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　4</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step4">
										    <option value="<bean:write name="tmp" property="step4"/>"><bean:write name="tmp" property="step4"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　5</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step5">
										    <option value="<bean:write name="tmp" property="step5"/>"><bean:write name="tmp" property="step5"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　6</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step6">
										    <option value="<bean:write name="tmp" property="step6"/>"><bean:write name="tmp" property="step6"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　7</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step7">
										    <option value="<bean:write name="tmp" property="step7"/>"><bean:write name="tmp" property="step7"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　8</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step8">
										    <option value="<bean:write name="tmp" property="step8"/>"><bean:write name="tmp" property="step8"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　9</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step9">
										    <option value="<bean:write name="tmp" property="step9"/>"><bean:write name="tmp" property="step9"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
								<tr align="left" class="list1">
									<td height="20" align="left"  width="170" class="title1">
										<font size="2"><b>Step　10</b></font>
									</td>
									<td height="20" align="left" width="581" >
										<select size="1" name="txt_step10">
										    <option value="<bean:write name="tmp" property="step10"/>"><bean:write name="tmp" property="step10"/></option>
										    <option></opiton>
									    <logic:present name="stepname" >
	    		                        <logic:iterate id="tmp1" name="stepname" >
											<option value="<bean:write name="tmp1" property="description"/>"><bean:write name="tmp1" property="id"/> - <bean:write name="tmp1" property="description"/></option>
	    		                        </logic:iterate>
			                            </logic:present>
										</select>
              						</td>
								</tr>
				</logic:iterate>
				</logic:present>
				<logic:notPresent name="list" >
								<tr align="left" class="list1">
									<td height="20" align="left" class="title1">
										<font size="2"><b>您並未點選任何一筆資料</b></font>
									</td>
								</tr>
				</logic:notPresent>
						</table>　

						<table width="95%" cellspacing="0" cellpadding="0" align="center">
								<tr>
						<logic:present name="list" >
									<td height="20" align="left" width="53">
										<input name="update" type="submit" value="Update" class="button1">
										<input type="hidden" name="btControl" value="bt_update">
									</td>
						</logic:present>
									<td>
										<input type="button" value="Cancel Update" onclick="history.go(-1);return true;" class="button1">
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