<!-- /8049FW/AurthMaintain.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@ page import="com.mxic.fw8049.action.*"%>
<%@ page import="com.mxic.fw8049.dao.*"%>
<%@page import="com.mxic.oiplus.au.*"%>

<html:html>
<%
String sid = request.getParameter("sid");
List<Fw8049MainActionForm> prmattr = (List<Fw8049MainActionForm>) request.getAttribute("list");
List<Fw8049MainActionForm> rs = Fw8049mationDao.getInfo(sid);
request.setAttribute("list1", rs);

// OiMaintainStep ois = OiMaintainService.SearchFunction(String.valueOf(sid));
String creator=rs.get(0).getCreator();
String sp1=rs.get(0).getSponsor_1();
String sp2=rs.get(0).getSponsor_2();
// String[] admin = com.mxic.oiplus.oimaintain.OiMaintainService.QueryAdmin();
User Auth=(User)session.getAttribute("user");
String user=Auth.getUserName();
boolean chkUser;
if(user.equals(creator)  || user.equals(sp1) || user.equals(sp2)){
	chkUser=true;
}
else{
	chkUser=false;
}
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM - 權限管理</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script type="text/javascript"></script>

<script language="JavaScript" type="text/javascript">
<!--
function isEmpty(data){
	return ((data == null) || (data.length == 0 || data==' '));
}

function update_data(tmp){
	if(tmp.sponsor_1.value=="" && tmp.sponsor_2.value=="") {
		window.alert("至少要填一個代理人");
	}else{
		if(window.confirm("確定要新增此代理人嗎？")){
			document.forms[0].act.value = 'update_data';
			document.forms[0].submit();
		}
  	}
}

function checkdata1(fm){
	if(isEmpty(fm.sponsor_1.value)){
		window.alert("請填寫代理人一");
		return false;
	}

	if(isEmpty(fm.sponsor_2.value)){
      		window.alert("請填寫代理人二");
		return false;
	}

	return true;
}

function windowback(sid){
	window.history.back();
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
	      <td width="100%" height="490" valign="top"><br>
                <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
	          <tr>
                    <td width="15%" height="25" class="title2">
                      <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                      <font size="4">TIM</font>
                    </td>
                    <td noWrap height="25" width="85%" class="title4">
                      <font size="4">權限維護</font>
                    </td>
                  </tr>
                  <tr>
                    <td height="20" colspan="2">
                      <hr width="100%" size="1" class="hr">
                    </td>
                  </tr>
                <logic:present name="list1">
                  <logic:iterate id="result" name="list1">
                  <form name="form1" action="<html:rewrite page="/8049fw/Fw8049MainAction.do"/>?sid=<%=sid%>">
                    <input name="act" type="hidden" value = "update_data">
                    <input type="hidden" name="sid" value="<%=sid%>">

                  </logic:iterate>
                </logic:present>
                <tr>
                  <td height="20" colspan="2">
<% if (chkUser){ %>
                    <input type="button" name="Update" value="Update" class="button1" onclick="update_data(this.form)">
<% } %>
 <input type="button" name="Cancel Update" value="回維護主畫面" class="button1" onclick="windowback('<%=sid%>');">
                  </td>
                </tr>
              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr align="left" class="titlen">
                      <td colspan="2" align="left" height="20">
                        <font size="2">
                          <b>Product:
                            <logic:present name="list1">
                              <logic:iterate id="result" name="list1">
                                <bean:write name="result" property="product_body"/>
                                / Version
                                <bean:write name="result" property="version"/>
                              </logic:iterate>
                            </logic:present>
                          </b>
                        </font>
                      </td>
                    </tr>
                    <logic:present name="list1">
                      <logic:iterate id="result" name="list1">
                        <tr align="left">
                          <td align="left" class="titlen" height="20">建立者</td>
                          <td align="left" class="listn" height="20">
                            <bean:write name="result" property="creator"/>
                          </td>
                        </tr>
                        <tr align="left">
                          <td height="20" align="left" class="titlen">代理人一</td>
                          <td class="listn" align="left" height="20">
                            <input type="text" name="sponsor_1" value="<bean:write name="result" property="sponsor_1"/>"/>
                          </td>
                        </tr>
                        <tr align="left">
                          <td class="titlen" align="left" height="20">代理人二</td>
                          <td class="listn" align="left" height="20">
                            <input type="text" name="sponsor_2" value="<bean:write name="result" property="sponsor_2"/>"/>
                          </td>
                        </tr>
                      </logic:iterate>
                    </logic:present>
                  </thead>
                </table>
              </div>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
<% if (chkUser){ %>
                    <input type="button" name="Update" value="Update" class="button1" onclick="update_data(this.form)">
<% } %>
 <input type="button" name="Cancel Update" value="回維護主畫面" class="button1" onclick="windowback('<%=sid%>');">
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
