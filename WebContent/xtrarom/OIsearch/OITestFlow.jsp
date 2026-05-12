<!-- /xtrarom/OIsearch/OITestFlow.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@page import="com.mxic.oiplus.resource.TDSResource"%>
<%
String path = TDSResource.getProperties("TIMPdf").getValue("jpg_dl.path");
%>


<html:html>



<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>專案管理</title>
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

function open_new(file_name){

window.open("../Release/" + file_name);


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
						<font size="4">TIM</font><font size="2">　OI Query - Test Flow Chart</font>
						</td>
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
							<td height="3" align="left" colspan="13"><font size="2"><b>Product: <bean:write name="oiQueryStepAForm" property="product_body"/> / <bean:write name="oiQueryStepAForm" property="brand"/> / Version <bean:write name="oiQueryStepAForm" property="version"/>　</b></font></td>
						</tr>

						<tr class="list1">
                                                  <td height="15" align="center" class="title1" >Category</td>
							<td height="15" align="center" class="title1" >File</td>
							<td height="15" align="center" class="title1" >Doc Name</td>

							<td height="15" align="center" class="title1" >Comment</td>


						</tr>

				<logic:present name="list1" >
				<logic:iterate id="tmp1" name="list1" >
					<tr class="list1">
                                          <td height="20" align="left" >Test Flow Chart</td>

                                          <td height="20" align="center" ><a target="_blank"  href='<%=path%><bean:write name="tmp1" property="file_name"/>'><img border="0"  style="" name="image" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a></td>
                                          <td height="20" align="left" > <bean:write name="tmp1" property="doc_name"/></td>
                                          <td height="20" align="left" > <bean:write name="tmp1" property="comment"/></td>
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
