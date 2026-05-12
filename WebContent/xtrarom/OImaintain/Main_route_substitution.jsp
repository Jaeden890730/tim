<!-- /xtrarom/OImaintain/Main_route_substitution.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<%
String t = (String) request.getAttribute("flag");
String sid = Integer.toString(((MainRouteSubForm) request.getAttribute("MainRouteSubFormX")).getSid());
String pro_b=(String) request.getAttribute("pro_b");
String brand = "MX";
StringBuffer ftoption=new StringBuffer();
ProTestRouteBean[] CPFTpt=OiMaintainService.RWRPFTRoute(pro_b,brand);//RPFTRoute

if (CPFTpt != null){
    //ftoption.append("<option value='NA'>NA</option>\n");
    for(int i=0;i<CPFTpt.length;i++){
        ftoption.append("<option value='" + CPFTpt[i].getRoutename() + "' >");
        ftoption.append(CPFTpt[i].getRoutename());
        ftoption.append("</option>\n");

    }
}
%>

<html:html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>Main Route vs Substitution Route Information</title>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter-2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable2.js"/>'></script>
<script type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">

function isEmpty(data){
	return ((trim(data) == null) || (trim(data).length == 0));
}


function delete_row(){
/*
    var tag = getValueByRadio('record_id', 'tag');
    if (tag == '0') {
        alert("無法刪除由前一版帶入資料");
    	return false;
    }
*/
	if(checkRadio("record_id","")) {
		if(window.confirm("確定要刪除嗎？")){
			document.forms[0].listControl.value = 'delete_row';
			document.forms[0].submit();
        	}

  	}
}


function reset_tx(){
	if(window.confirm("確定要送出嗎？")){
		document.forms[0].listControl.value = 'reset_tx';
		document.forms[0].submit();
  	}
}

function update_data(tmp){
	if(checkdata1(tmp)) {
		if(window.confirm("確定要送出嗎？")){
			document.forms[0].listControl.value = 'update_data';
			document.forms[0].submit();
        	}
  	}
}

function submit_data(tmp){
	if(checkdata1(tmp)) {
		if(window.confirm("確定要送出嗎？")){
			document.forms[0].listControl.value = 'submit_data';
			document.forms[0].submit();
        	}
  	}
}

function checkdata(){
	var b1=document.form1.record_id;
	if(b1 == null){
		return true;
	}
	for(var i=0;i<b1.length;i++){
    		if(b1[i].checked){
      			//window.alert("請選擇起始日");
      			return true;
    		}
  	}
  	window.alert("請點選一項目");
  	return false;
}

function checkdata1(fm){
	var chkObj =fm.record_id;
	//var prodType = fm.productType;
	if (chkObj == null) {
	    return true;
	}

	for(var i=0;i<chkObj.length;i++){
		if(isEmpty(fm.main_route[i].value)){
			window.alert("第 "+(i+1)+" 行請選 Main Route");
			return false;
	        }


		if(isEmpty(fm.map_route[i].value)){
			window.alert("第 "+(i+1)+" 行請選 Map Route");
			return false;
		}

	}
    return true;
}

function add(sid,pd_body,version,route_type){
	window.location="<html:rewrite page="/xtrarom/OImaintain/MainRouteAdd.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&version="+ version +"&route_type="+ route_type;
}

//searchAction.do?sid=<%=sid%>
function back(sid){
	window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid=" +sid;
}


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
              <form name="form1" method="post" action="<html:rewrite page="/OImaintain/saveMainRouteSubActionX.do"/>">
                <input name="listControl" type="hidden" />
                <input type="hidden" name="sid" value="<%=sid %>" />
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">Main Route vs Substitution Route Information</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
				<% if (t.equals("Show")) { %>
                <tr>
                  <td height="20" colspan="2"><font color="#C00000">注意：若需要刪除所有資料，請先按 "Submit" 後，再做刪除動作，否則系統將再自動帶入前一版資料。</font>
                  </td>
                </tr>
                <%} %>
                <tr>
                  <td height="20" colspan="2">
				   <% if (t.equals("Show")) { %>
                        <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form)">
                        <input type="button" name="Add" value="Add" class="button1" onclick="add('<%=sid%>','<bean:write name="MainRouteSubFormX" property="product_body"/>','<bean:write name="MainRouteSubFormX" property="version"/>','<bean:write name="MainRouteSubFormX" property="route_type"/>');">
                        <input type="button" name="deleteRow" value="Delete Row" class="button1" onclick="delete_row()">
                        <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form)">
                        <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx()">
                   <% } %>     <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                  </td>
                </tr>
              </table>
           <div id="myDIV1" align="center" style="border:0;">
                <table cellspacing=1 cellpadding=0 class="table2">
                  <thead>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="2">
                          <b>                            Product :
                             <bean:write name="MainRouteSubFormX" property="product_body"/>
                             / Version
                             <bean:write name="MainRouteSubFormX" property="version"/>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>

               <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class="table2">
                  <thead>
                    <tr class="title1">
                      <td height="20" width="3%" ></td>
                      <td height="20" width="20%" >Main Route</td>
                      <td height="20" width="30%" >Substitution Route</td>
                      <td height="20" width="47%" >Remark</td>

                    </tr>

                  </thead>
                  <logic:present name="mrsParam">
                    <logic:iterate id="result1" name="mrsParam" indexId="i">
                      <tr class="list1">
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="radio" name="record_id" value="<bean:write name="result1" property="main_route"/>,<bean:write name="result1" property="map_route"/>,<bean:write name="result1" property="remark"/>">
                          <input type="hidden" name="sid" value="<bean:write name="result1" property="sid"/>">
                          <input type="hidden" name="product_body" value="<bean:write name="result1" property="product_body"/>">
                          <input type="hidden" name="version" value="<bean:write name="result1" property="version"/>">
                          <input type="hidden" name="route_type" value="<bean:write name="result1" property="route_type"/>">
                          <input type="hidden" name="main_route" value="<bean:write name="result1" property="main_route"/>">
                          <input type="hidden" name="map_route" value="<bean:write name="result1" property="map_route"/>">
                          <input type="hidden" name="tag" value="<bean:write name="result1" property="tag"/>">
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="main_route"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <bean:write name="result1" property="map_route"/>
                        </td>
                        <!--<td align="left"  bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <select name="main_route" size="1">
                               <option value="<bean:write name="result1" property="main_route"/>"><bean:write name="result1" property="main_route"/></option>
                               <%//=ftoption.toString()
                               %>
                            </select>
                        </td>
                        <td align="left"  bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <select name="map_route" size="1">
                               <option value="<bean:write name="result1" property="map_route"/>"><bean:write name="result1" property="map_route"/></option>
                               <%//=ftoption.toString()
                               %>
                            </select>
                        </td>-->
                        <td width="100%" align="left" height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="text" size=50 name="remark" value="<bean:write name="result1" property="remark"/>"/>
                        </td>

                      </tr>
                    </logic:iterate>
                  </logic:present>
                </table>
              </div>

              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
				   <% if (t.equals("Show")) { %>
                        <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form)">
                        <input type="button" name="Add" value="Add" class="button1" onclick="add('<%=sid%>','<bean:write name="MainRouteSubFormX" property="product_body"/>','<bean:write name="MainRouteSubFormX" property="version"/>','<bean:write name="MainRouteSubFormX" property="route_type"/>');">
                        <input type="button" name="deleteRow" value="Delete Row" class="button1" onclick="delete_row()">
                        <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form)">
                        <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx()">
                   <% } %>     <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">

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
    <script type="text/javascript">
	  //20110531var st1 = new SortableTable(document.getElementById("table27"));
</script>
<%@include file="../../index-down.jsp"%>
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

      setFilterGrid("table27",props);
</script>
</html:html>
