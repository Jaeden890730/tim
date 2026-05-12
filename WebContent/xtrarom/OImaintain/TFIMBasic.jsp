<!-- /xtrarom/OImaintain/TFIMBasic.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<%

int sid = Integer.parseInt(request.getParameter("sid"));
boolean status_apply_bo = FTService.status_apply(sid);

OiMaintainStep ois = OiMaintainService.SearchFunction(String.valueOf(sid));
String creator=ois.getCreator();
String sp1=ois.getSponsor_1();
String sp2=ois.getSponsor_2();
User Auth=(User)session.getAttribute("user");
String user=Auth.getUserName();
String productType = OiMaintainService.getProductType(Integer.toString(sid));

boolean chkUser;
 if(user.equals(creator)){
  chkUser=true;
 }else if(user.equals(sp1)){
	chkUser=true;
 }else if(user.equals(sp2)){
	chkUser=true;
 }else{
    chkUser=false;
 }
  TFIMBasicActionForm[] rs = TFIMBasicService.getInfo(sid);
  request.setAttribute("list1", rs);
  TFIMBasicActionForm[] rs1 = TFIMBasicService.getInitialInfo(sid);
  request.setAttribute("list2", rs1);
  String checkexisttestparameter = OiMaintainService.CheckExistTestParameter(Integer.toString(sid));
%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM Basic Information</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter-2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable.js"/>'></script>
<script type="text/javascript"></script>
<script language="JavaScript" type="text/javascript">
 <!--
function isEmpty(data){
    return ((data == null) || (data.length == 0 || data==' '));
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
      if(window.confirm("確定要送出嗎？")){
        document.forms[0].listControl.value = 'delete_row';
    document.forms[0].submit();
        }

  }

}


function reset_tx(){
  if(window.confirm("確定 Reset 資料嗎？")){
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
        <%
        if (!checkexisttestparameter.equals("")) {
        %>
           alert("<%=checkexisttestparameter%>");
           return false;
        <%
        }
        %>
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
    if (chkObj == null)
      return false;

    for(var i=0;i<chkObj.length;i++){
        if(isEmpty(fm.tester[i].value)){
            window.alert("請填寫 Test Mode");
            return false;
        }
        if(isEmpty(fm.good_bin[i].value)){
            window.alert("請填寫 Good Bin");
            return false;
        }
        if(isEmpty(fm.fail_bin[i].value)){
            window.alert("請填寫 Fail Bin");
            return false;
        }
        <%
        if (productType.equals("ASM")) {
        %>
        if (isEmpty(fm.auto_ship_yield[i].value)) {
            window.alert("請填寫 Auto Ship Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.stop_test_yield[i].value)) {
            window.alert("請填寫 Stop Test Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.auto_scrap_yield[i].value)) {
            window.alert("請填寫 Auto Scrap Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.mrb_yield[i].value)) {
            window.alert("請填寫 OOC Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.sample_yield[i].value)) {
            window.alert("請填寫 Sample Yield, 如無資料請填 N/A");
            return false;
        }
        <%
        }
        %>
    }
    return true;
}


 function insert_row(sid,pd_body,brand,version){

window.location="<html:rewrite page="/OImaintain/TFIMBasicAdd.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version;

}

 function back(sid){

window.location="<html:rewrite page="/OImaintain/searchAction.do"/>?sid=" +sid;

}

//-->
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <!--<td valign="top" width="159" class="bg">-->
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
                    <font size="4">Basic Information</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
                <logic:present name="list1">
                  <logic:iterate id="result" name="list1">
                  <form name="form1" method="POST" action="<html:rewrite page="/OImaintain/tFIMBasicAction.do"/>?sid=<%=sid%>&pd_body=<bean:write name="result" property="pd_body"/>&brand=<bean:write name="result" property="brand"/>&version=<bean:write name="result" property="version"/>">
                    <input name="listControl" type="hidden">

                  </logic:iterate>
                </logic:present>
                <tr>
                  <td height="20" colspan="2">
<%if (status_apply_bo == true&&chkUser) {                  %>
                    <logic:present name="list1">
                      <logic:iterate id="result" name="list1">
                        <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form);">
                        <input type="button" name="InsertRow" value="Insert Row" class="button1" onclick="insert_row('<%=sid%>','<bean:write name="result" property="pd_body"/>','<bean:write name="result" property="brand"/>','<bean:write name="result" property="version"/>');">
                        <input type="button" name="DeleteRow" value="Delete Row" class="button1" onclick="delete_row();">
                        <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form);">
                        <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx();">
<input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
</logic:iterate>                    </logic:present>
<% } else { %>
<input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                  <%}                  %>

                  </td>
                </tr>
              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="2">
                          <b>                            Product :
                            <logic:present name="list1">
                              <logic:iterate id="result" name="list1">
                                <bean:write name="result" property="pd_body"/>
                                /
                                <bean:write name="result" property="brand"/>
                                / Version
                                <bean:write name="result" property="version"/>
                              </logic:iterate>
                            </logic:present>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <br>
              <div id="myDIV1" align="center" >
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="title1">
                      <td height="20"/>
                      <td height="20">Test Mode</td>
                      <td height="20">Good Bin</td>
                      <td height="20">Fail Bin</td>
                      <td height="20">Remark</td>
                      <%
                        if (productType.equals("ASM")) {
                      %>
                        <td height="20">Auto Ship Yield</td>
                        <td height="20">Stop Test Yield</td>
                        <td height="20">Auto Scrap Yield</td>
                        <td height="20">OOC Yield</td>
                        <td height="20">Sample Yield</td>
                      <%
                        }
                      %>
                    </tr>
                  </thead>
                  <tbody>
                  <logic:present name="list2">
                    <logic:iterate id="result1" name="list2">
                      <tr class="list1">
                        <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                          <input type="radio" name="record_id" value="<bean:write name="result1" property="tester"/>">
                          <input type="hidden" name="sid" value="<bean:write name="result1" property="sid"/>">
                          <input type="hidden" name="pd_body" value="<bean:write name="result" property="pd_body"/>">
                          <input type="hidden" name="brand" value="<bean:write name="result" property="brand"/>">
                          <input type="hidden" name="version" value="<bean:write name="result" property="version"/>">
                          <input type="hidden" name="tester_be" value="<bean:write name="result1" property="tester"/>">
                          <input type="hidden" name="good_bin_be" value="<bean:write name="result1" property="good_bin"/>">
                          <input type="hidden" name="fail_bin_be" value="<bean:write name="result1" property="fail_bin"/>">
                          <input type="hidden" name="remark_be" value="<bean:write name="result1" property="remark"/>">
                          <input type="hidden" name="auto_ship_yield_be" value="<bean:write name="result1" property="auto_ship_yield"/>">
                          <input type="hidden" name="stop_test_yield_be" value="<bean:write name="result1" property="stop_test_yield"/>">
                          <input type="hidden" name="auto_scrap_yield_be" value="<bean:write name="result1" property="auto_scrap_yield"/>">
                          <input type="hidden" name="mrb_yield_be" value="<bean:write name="result1" property="mrb_yield"/>">
                          <input type="hidden" name="sample_yield_be" value="<bean:write name="result1" property="sample_yield"/>">
                          <input type="hidden" name="tag" value="<bean:write name="result1" property="tag"/>">
                        </td>
                        <%
                          if (productType.equals("NVM")) {
                        %>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="tester" size=18 value="<bean:write name="result1" property="tester"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="good_bin" size=20 value="<bean:write name="result1" property="good_bin"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="fail_bin" size=20 value="<bean:write name="result1" property="fail_bin"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="remark" size=64 value="<bean:write name="result1" property="remark"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>" style>
                            <input type="hidden" name="auto_ship_yield" size = 10 value="<bean:write name="result1" property="auto_ship_yield"/>"/>
                            <input type="hidden" name="stop_test_yield" size = 10 value="<bean:write name="result1" property="stop_test_yield"/>"/>
                            <input type="hidden" name="auto_scrap_yield" size = 10 value="<bean:write name="result1" property="auto_scrap_yield"/>"/>
                            <input type="hidden" name="mrb_yield" size = 10 value="<bean:write name="result1" property="mrb_yield"/>"/>
                            <input type="hidden" name="sample_yield" size = 10 value="<bean:write name="result1" property="sample_yield"/>"/>
                          </td>
                        <%
                          } else {
                        %>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="tester" size=5 value="<bean:write name="result1" property="tester"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="good_bin" size=14 value="<bean:write name="result1" property="good_bin"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="fail_bin" size=15 value="<bean:write name="result1" property="fail_bin"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="remark" size=30 value="<bean:write name="result1" property="remark"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="auto_ship_yield" size = 10 value="<bean:write name="result1" property="auto_ship_yield"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="stop_test_yield" size = 10 value="<bean:write name="result1" property="stop_test_yield"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="auto_scrap_yield" size = 10 value="<bean:write name="result1" property="auto_scrap_yield"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="mrb_yield" size = 10 value="<bean:write name="result1" property="mrb_yield"/>"/>
                          </td>
                          <td height="20" bgcolor="<bean:write name="result1" property="changecolor"/>">
                            <input type="text" name="sample_yield" size = 10 value="<bean:write name="result1" property="sample_yield"/>"/>
                          </td>
                        <%
                          }
                        %>
                      </tr>
                    </logic:iterate>
                  </logic:present>
                  </tbody>
                </table>
              </div>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
<%if (status_apply_bo == true&&chkUser) { %>
                    <logic:present name="list1">
                      <logic:iterate id="result" name="list1">
                        <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form);">
                        <input type="button" name="InsertRow" value="Insert Row" class="button1" onclick="insert_row('<%=sid%>','<bean:write name="result" property="pd_body"/>','<bean:write name="result" property="brand"/>','<bean:write name="result" property="version"/>');">
                        <input type="button" name="DeleteRow" value="Delete Row" class="button1" onclick="delete_row();">
                        <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form);">
                        <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx();">
                        <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                      </logic:iterate>
                    </logic:present>
<% } else { %>
                        <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
<% } %>
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
    filters_row_index: 2,
    loader: true,
    loader_html: '<img src="<html:rewrite page="/image/loader.gif"/>" alt="" style="margin: 0pt 5px; vertical-align: middle;"><span>Loading...</span>',
    status_bar: false,
//        col_0: "none",
    enter_key: true
  };

      setFilterGrid("table27",props);
</script>
</html:html>
