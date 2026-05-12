<!-- /xtrarom/OImaintain/maintain_index.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%
int sid=1;
FTTestActionForm[] rs = FTService.getInfo(sid);
request.setAttribute("list1", rs);
%>

<html:html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>OI維護</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <!--<td valign="top" width="159" class="bg">-->
</td>      <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
          <tr>
            <td width="100%" height="490" valign="top">
              <br>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TFIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">OI Maintainence</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
              </table>
            <form name="frm2" action="">
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="2">
                          <b>Product Body: 6615 /MX</b>
                        </font>
                      </td>
                    </tr>
                    <tr class="list1">
                      <td height="20" align="left"><logic:present name="list1">
                      <logic:iterate id="result" name="list1">
                        <a href='<html:rewrite page="/OImaintain/FTTest.jsp"/>?sid=<%=sid%>&pd_body=<bean:write name="result" property="pd_body"/>&brand=<bean:write name="result" property="brand"/>&version=<bean:write name="result" property="version"/>&status=<bean:write name="result" property="status"/>'">Step 4: FT Test Parameter Information</a>
                        <br>
                        <a href='<html:rewrite page="/OImaintain/TFIMBasic.jsp"/>?sid=<%=sid%>&pd_body=<bean:write name="result" property="pd_body"/>&brand=<bean:write name="result" property="brand"/>&version=<bean:write name="result" property="version"/>&status=<bean:write name="result" property="status"/>'">Step 5: Basic Information Definition</a>
                        <br>
                        <a href='<html:rewrite page="/OImaintain/DocLinkage.jsp"/>?sid=<%=sid%>&pd_body=<bean:write name="result" property="pd_body"/>&brand=<bean:write name="result" property="brand"/>&version=<bean:write name="result" property="version"/>&status=<bean:write name="result" property="status"/>'">Step 6: Document Linkage</a>
                        <br>
                        <a href='<html:rewrite page="/OImaintain/Document.jsp"/>?sid=<%=sid%>&pd_body=<bean:write name="result" property="pd_body"/>&brand=<bean:write name="result" property="brand"/>&version=<bean:write name="result" property="version"/>&status=<bean:write name="result" property="status"/>'">Step 7: 文件管理</a>
                          <br>
                          <br>
                          <br>
                          <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                          <a href='<html:rewrite page="/OImaintain/AurthMaintain.jsp"/>?sid=<%=sid%>&pd_body=<bean:write name="result" property="pd_body"/>&brand=<bean:write name="result" property="brand"/>&version=<bean:write name="result" property="version"/>&status=<bean:write name="result" property="status"/>'">權限維護</a>
                          <br>
                          <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                          <a href='<html:rewrite page="/OImaintain/EditiionCompare.jsp"/>?sid=<%=sid%>&pd_body=<bean:write name="result" property="pd_body"/>&brand=<bean:write name="result" property="brand"/>&version=<bean:write name="result" property="version"/>&status=<bean:write name="result" property="status"/>'">版本差異比較</a>
 </logic:iterate>
                    </logic:present>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <br>
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
