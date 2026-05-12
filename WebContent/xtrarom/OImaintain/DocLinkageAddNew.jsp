<!-- /xtrarom/OImaintain/DocLinkageAddNew.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%
  String sid = request.getParameter("sid");
  String pd_body = request.getParameter("pd_body");
  String brand = request.getParameter("brand");
  String version = request.getParameter("version");
%>
<html:html>
<head>
<title>DocLinkageAdd</title>
</script><script language="JavaScript" type="text/javascript">

function check(){
  var fileName = document.getElementById("formname").value;

  if (document.getElementById("doc_name").value == '') {
    alert("請輸入 Document Name!");
    return false;
  }

  if (fileName == '') {
    alert("請先選擇檔案!");
    return false;
  }

  var len = fileName.length;
  if ((len < 5) || (fileName.substr(fileName.length - 4).toLowerCase().match(".png") == null)) {
    alert("請選擇 PNG 檔案!");
    return false;
  }
  return true;
}
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
                  <font size="4">Add New Document</font>
                </td>
              </tr>
              <tr>
                <td height="20" colspan="2">
                  <hr width="100%" size="1" class="hr">
                </td>
              </tr>
              <input name="listControl" type="hidden">
            </table>
            <div id="myDIV1" align="center" style="border:0;">
              <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                <thead>
                  <tr class="list1">
                    <td height="20" align="left">
                      <font size="2"><b>Product: <%=pd_body%> / Version <%=version%>                      </b></font>
                    </td>
                  </tr>
                </thead>
              </table>
            </div>
			<html:form action="/OImaintain/docLinkageAddNewActionX.do" method="post" enctype="multipart/form-data">
			  <html:hidden property="sid" value="<%=sid%>" styleId="sid" />
			  <html:hidden property="pd_body" value="<%=pd_body%>" styleId="pd_body" />
			  <html:hidden property="brand" value="<%=brand%>" styleId="brand" />
			  <html:hidden property="version" value="<%=version%>" styleId="version" />
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2 border="0">
                  <tr class="list1">
                    <td height="20" width="20%">Document Type</td>
                    </td>
                    <td align="left">
                      <html:select property="category" styleId="category">
                        <html:option value="Yield Definition">Yield Definition</html:option>
                      </html:select></td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Document Name</td>
                    </td>
                    <td align="left">
                      <html:text property="doc_name" size="40" styleClass="text1" styleId="doc_name" />
                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Comment</td>
                    </td>
                    <td align="left">
                      <html:text property="comment" size="40" styleClass="text1" styleId="comment" />
                    </td>
                  </tr>
                  <tr>
                    <td align="left" colspan="2">
                      <font color="red">
                        <html:errors/>
                      </font>
                  </tr>
                  <tr>
                    <td align="center"></td>
                    <td align="left">
                      <html:file property="formname" styleId="formname"/>
                      <html:submit styleClass="button1" onclick="return check();">上傳檔案</html:submit>
                                                          限 PNG 檔
                    </td>
                  </tr>
                </table>
              </div>
            </html:form>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<%@include file="../../index-down.jsp"%>
</body>
</html:html>
