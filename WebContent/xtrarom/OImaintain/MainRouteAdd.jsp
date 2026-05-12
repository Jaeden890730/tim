<!-- /xtrarom/OImaintain/MainRouteAdd.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<html:html>
<%
	String pd_body = request.getParameter("pd_body");
	String sid=request.getParameter("sid");
        String brand= "MX";
	String version = request.getParameter("version");
        String route_type = request.getParameter("route_type");

	ProTestRouteBean[] CPFTpt = OiMaintainService.RWRPFTRoute(pd_body,brand);//RPFTRoute
	StringBuffer CPFTptHTML = new StringBuffer();
	if (CPFTptHTML != null) {
		for (int i=0; i<CPFTpt.length; i++) {
			CPFTptHTML.append("<option value=\"" + CPFTpt[i].getRoutename() + "\">" + CPFTpt[i].getRoutename() + "</option>");
		}
	}
	CPFTpt = null;

%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<script language="JavaScript" type="text/javascript">
var index=0;
function insertRow(){
        var objRow = table1.insertRow(table1.rows.length);
        var objCell = objRow.insertCell(0);
           objCell.innerHTML += '<td class="list1"><select name="main_route"/><%= CPFTptHTML %></select></td>';
        var objCell = objRow.insertCell(1);
           objCell.innerHTML += '<td class="list1"><select name="map_route"/><%= CPFTptHTML %></select></td>';
        var objCell = objRow.insertCell(2);
           objCell.innerHTML += '<td class="list1"><input type="text" size="70" name="remark" class="upperText" /></td>';//onkeyup="onlyKeyUpperCase(this);"
        var objCell = objRow.insertCell(3);
           objCell.innerHTML += '<td class="list1"><input type="button" name="row' + index + '" value="delete" onclick="delRow(this.parentNode.parentNode.rowIndex)"></td>';
        index++;
}

function delRow(rowIndex) {
  table1.deleteRow(rowIndex);
  return;
}

function assignAction() {
	fm = document.getElementById("form1");
        //alert("action="+document.forms[0].action);
	if (!checkData(fm))
	    return false;
	fm.submit();
}

function can(sid){
	window.location="<html:rewrite page="/OImaintain/mainRouteSubActionX.do"/>?sid=" +sid;
}

function isEmpty(data){
	return ((trim(data) == null) || (trim(data).length == 0));
}

function checkData(fm) {
	var chkObj =fm.main_route;
	if (chkObj == null)
		return true;
	if (chkObj.length == null) {// only one record
		if (isEmpty(chkObj.value)) {
			window.alert("請填選 Main Route");
			return false;
		}
	} else {
		for (i=0; i<chkObj.length; i++) {
			if (isEmpty(chkObj[i].value)) {
				window.alert("第 "+(i+1)+" 行請選 Main Route");
				return false;
			}
		}
	}
	var chkObj1 =fm.map_route;
	if (chkObj1 == null)
		return true;
	if (chkObj1.length == null) {// only one record
		if (isEmpty(chkObj1.value)) {
			window.alert("請選 Map Route");
			return false;
		}
	} else {
		for (i=0; i<chkObj1.length; i++) {
			if (isEmpty(chkObj1[i].value)) {
				window.alert("第 "+(i+1)+" 行請選 Map Route");
				return false;
			}
		}
	}
	return true;
}

</script>
<%

%>
<title>專案管理</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
          <tr>
            <td width="100%" height="490" valign="top">
              <br>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">Add Main Route Information</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <input type="button" name="Insert Row" value="Insert Row" class="button1"  onclick="insertRow();">
                    <input type="button" name="Save" value="save" class="button1"  onclick="assignAction();">
                    <input type="button" name="Cancel" value="Cancel" class="button1"   onclick="can('<%=sid%>');">
                  </td>
                </tr>

              </table>
              <form id="form1" name="form1" action="<html:rewrite page="/OImaintain/addMainRouteSubActionX.do"/>" method="post">
                <div id="myDIV1" align="center" style="border:0;">
                  <table id="table1" cellspacing=1 cellpadding=0 class=table2>
                    <thead>
                      <tr class="title1">
                        <td height="20" align="left">Main Route</td>
                        <td height="20" align="left">Substitution Route</td>
                        <td height="20" align="left">Remark</td>
                        <td height="20"></td>
                      </tr>
                    </thead>
                  </table>
                </div>
                <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                  <tr>
                    <td height="20">
                      <input type="button" name="Insert Row" value="Insert Row" class="button1"  onclick="insertRow();">
                      <input type="button" name="Save" value="save" class="button1"  onclick="assignAction();">
                      <input type="button" name="Cancel" value="Cancel" class="button1"  onclick="can('<%=sid%>');">
                      <input type="hidden" name="product_body" value="<%=pd_body %>">
                      <input type="hidden" name="brand" value="<%=brand %>">
                      <input type="hidden" name="version" value="<%=version %>">
                      <input type="hidden" name="route_type" value="<%=route_type %>">
                      <input type="hidden" name="sid" value="<%=sid %>">
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
<%@include file="../../index-down.jsp"%>
</body>
</html:html>
