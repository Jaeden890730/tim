<!-- /xtrarom/OImaintain/TFIMBasicAdd.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%
    int sid = Integer.parseInt(request.getParameter("sid"));
    String pd_body = request.getParameter("pd_body");
    String brand = request.getParameter("brand");
    String version = request.getParameter("version");
    String productType = OiMaintainService.getProductType(Integer.toString(sid));
%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM-e8049-Basic Information</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script>
<script language="JavaScript" type="text/javascript">
<!--
function isEmpty(data){
    return ((data == null) || (data.length == 0 || data==' '));
}

function insert_row(fm){
    if(checkdata(fm)) {
        if(window.confirm("確定要送出嗎？")){
            document.forms[0].listControl.value = 'insert_row';
      	    document.forms[0].submit();
        }
    }
}

function checkdata(fm){

    if(isEmpty(fm.tester.value)){
    	window.alert("請填寫 Test Mode");
	return false;
    }
    if(isEmpty(fm.good_bin.value)){
    	window.alert("請填寫 Good Bin !");
    	return false;
    }
    if(isEmpty(fm.fail_bin.value)){
    	window.alert("請填寫 Fail Bin !");
    	return false;
    }

    <% if (productType.equals("ASM")) {
    %>
        if (isEmpty(fm.auto_ship_yield.value)) {
            window.alert("請填寫 Auto Ship Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.stop_test_yield.value)) {
            window.alert("請填寫 Stop Test Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.auto_scrap_yield.value)) {
            window.alert("請填寫 Auto Scrap Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.mrb_yield.value)) {
            window.alert("請填寫 OOC Yield, 如無資料請填 N/A");
            return false;
        }
        if (isEmpty(fm.sample_yield.value)) {
            window.alert("請填寫 Sample Yield, 如無資料請填 N/A");
            return false;
        }
    <%
      }
    %>
    return true;
}

function can(sid){
    window.location="<html:rewrite page="/OImaintain/TFIMBasic.jsp"/>?sid=" +sid;
}

//-->
</script>
</head>
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
                    <font size="4">Basic Information Add</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
                <form name="form1"  action="<html:rewrite page="/OImaintain/tFIMBasicAddAction.do"/>?sid=<%=sid%>&pd_body=<%=pd_body %>&brand=<%=brand %>&version=<%=version %>" method="post">
                <input name="listControl" type="hidden">
                  <tr>
                    <td height="20" colspan="2">
                      <input type="button" name="save" value="Save" class="button1" onclick="insert_row(this.form)">
                      <input type="button" name="Cancel" value="Cancel" class="button1"  onclick="can('<%=sid%>');">
                    </td>
                  </tr>
              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="list1">
                      <td height="20" align="left" colspan="2">
                        <font size="2"><font size="2"><b>Product: <%=pd_body %> / <%=brand %> / Version <%=version %></b></font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2 border="0">
                  <tr class="list1">
                    <input type="hidden" name="sid" value="<%=sid%>">
                    <input type="hidden" name="pd_body" value="<%=pd_body%>">
                    <input type="hidden" name="brand" value="<%=brand%>">
                    <input type="hidden" name="version" value="<%=version%>">
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Test Mode</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" name="tester" value=""/>
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Good Bin</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size = 40 name="good_bin" value=""/>
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Fail Bin</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size = 40 name="fail_bin" value=""/>
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Remark</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size=40 name="remark" value="" />
                    </td>
                  </tr>
                  <%
                    if (productType.equals("ASM")) {
                  %>
                  <tr class="list1">
                    <td height="20" width="20%">Auto Ship Yield</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size=40 name="auto_ship_yield" value="N/A" />
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Stop Test Yield</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size=40 name="stop_test_yield" value="N/A" />
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Auto Scrap Yield</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size=40 name="auto_scrap_yield" value="N/A" />
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">OOC Yield</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size=40 name="mrb_yield" value="N/A" />
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Sample Yield</td>
                    <td align="left" height="20" width="80%">
                      <input type="text" size=40 name="sample_yield" value="N/A" />
                    </td>
                  </tr>
                  <%
                    } else {
                  %>
                  <tr>
                    <td>
                      <input type="hidden" size=40 name="auto_ship_yield" value="" />
                      <input type="hidden" size=40 name="stop_test_yield" value="" />
                      <input type="hidden" size=40 name="auto_scrap_yield" value="" />
                      <input type="hidden" size=40 name="mrb_yield" value="" />
                      <input type="hidden" size=40 name="sample_yield" value="" />
                    </td>
                  </tr>
                  <%
                    }
                  %>
                </table>
              </div>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
                    <input type="button" name="save" value="Save" class="button1" onclick="insert_row(this.form)">
                    <input type="button" name="Cancel" value="Cancel" class="button1"  onclick="can('<%=sid%>');">
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
