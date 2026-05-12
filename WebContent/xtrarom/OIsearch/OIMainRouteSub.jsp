<!-- /xtrarom/OIsearch/OIMainRouteSub.jsp -->

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
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter-2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable.js"/>'></script>
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
						<font size="4">TIM</font><font size="2">　OI Query - Main Route VS Substitution Route</font></td>
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
				<table width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
				<tr class="list1">
							<td height="20" align="left" colspan="21"><font size="2"><b>Product: <bean:write name="oiQueryStepAForm" property="product_body"/> / <bean:write name="oiQueryStepAForm" property="brand"/> / Version <bean:write name="oiQueryStepAForm" property="version"/></b></font></td>
				</tr>
				</table>
				<table id="myTable1" width="95%" cellspacing="1" cellpadding="0" align="center" class="table2">
				<thead>
						
						<tr class="list1">
  					        <td height="20" align="left" class="title1" >Main Route</td>
						    <td height="20" align="left" class="title1">Substitution Route</td>
							<td height="20" align="left" class="title1">Remark</td>
						</tr>
				</thead>
			    <tbody>			
				<logic:present name="list1" >
				<logic:iterate id="tmp1" name="list1" >
					<tr class="list1">
                                                <td height="20" align="center"> <bean:write name="tmp1" property="main_route"/></td>
                                                <td height="20" align="center"> <bean:write name="tmp1" property="map_route"/></td>
						<td height="20" align="center"> <bean:write name="tmp1" property="remark"/></td>
					</tr>
				</logic:iterate>
				</logic:present>
				</tbody>
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
<script type="text/javascript">
	  var st1 = new SortableTable(document.getElementById("myTable1"));
</script>
<%@  include file="../../index-down.jsp"%>

</body>
<script type="text/javascript">
  var props = {
    filters_row_index: 1,
    loader: true,
    loader_html: '<img src="<html:rewrite page="/image/loader.gif"/>" alt="" style="margin: 0pt 5px; vertical-align: middle;"><span>Loading...</span>',
    status_bar: false,
//        col_0: "none",
    enter_key: true
  };

      setFilterGrid("myTable1",props);
</script>
</html:html>