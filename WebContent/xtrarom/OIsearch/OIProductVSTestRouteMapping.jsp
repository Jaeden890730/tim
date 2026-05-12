<!-- /xtrarom/OIsearch/OIProductVSTestRouteMapping.jsp -->

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
function bt_save(){

 	document.forms[0].btControl.value = 'bt_save';
  	document.forms[0].submit();

}
function bt_copy(){

 	document.forms[0].btControl.value = 'bt_copy';
  	document.forms[0].submit();

}
function bt_cancle(){

	location.replace("<html:rewrite page="/xtrarom/OIsearch/OITestRouteDefinition.jsp"/>")
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
						<b>
						<font size="4">TIM</font><font size="2">　OI Query - Product VS Test Route Mapping</font></td>
					</tr>
					<tr>
						<td height="20">
							<hr width="100%" size="1" class="hr">
						</td>
					</tr>
				</table>
				　

				<form name="form1" action="" method="post">
				<input type="hidden" name="btControl">
				<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
				<logic:present name="list" >
				<logic:iterate id="tmp" name="list" >
			         	<tr class="list1">
			          		<td height="20" align="left"><font size="2" ><b></b></font></td>
			         	</tr>
			    </logic:iterate>
			    </logic:present>

			    </table>
				</form>
				<table id="myTable1" width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
						<tr class="list1">
							<td height="20" align="left" colspan="8"><font size="2"><b>Product: <bean:write name="oiQueryStepAForm" property="product_body"/> / Version <bean:write name="oiQueryStepAForm" property="version"/></b></font></td>
						</tr>
						<tr class="list1">
                                                        <td height="20" align="left" class="title1">Route</td>
                                                        <td height="20" align="left" class="title1">Step Seq</td>
                                                        <td height="20" align="left" class="title1">Step Name</td>
                                                        <td height="20" align="left" class="title1">Conditions</td>
                                                        <td height="20" align="left" class="title1">Temperature(￠J)</td>
                                                        <td height="20" align="left" class="title1">抽測Test Mode</td>
							<td height="20" align="left" class="title1">Remark</td>
						</tr>
				<logic:present name="list1" >
				<logic:iterate id="tmp1" name="list1" >
					<tr class="list1">
                                             <logic:notEqual name="tmp1" property="count" value="0">
                                                <td height="20" align="center" rowspan="<bean:write name="tmp1" property="count"/>"> <bean:write name="tmp1" property="route_name"/></td>
                                             </logic:notEqual>
                                                <td height="20" align="center"> <bean:write name="tmp1" property="step_seq"/></td>
                                                <td height="20" align="center"> <bean:write name="tmp1" property="step_name"/></td>
                                                <td height="20" align="center"> <bean:write name="tmp1" property="test_time"/> <bean:write name="tmp1" property="time_unit"/></td>
                                                <td height="20" align="center"> <bean:write name="tmp1" property="temperature"/></td>
                                                <td height="20" align="center"> <bean:write name="tmp1" property="samplingtest"/></td>
						<td height="20" align="center"> <bean:write name="tmp1" property="remark"/></td>
					</tr>
				</logic:iterate>
				</logic:present>
				</table>
				<table width="95%" cellspacing="1" cellpadding="0" align="center">
					<tr>
						<td height="20" align="left" >
							<input type="button" value="回前一畫面" onclick="history.go(-1);return true;" class="button1">
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<%@  include file="../../index-down.jsp"%>

</body>
</html:html>