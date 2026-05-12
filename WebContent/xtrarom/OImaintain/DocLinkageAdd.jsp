<!-- /xtrarom/OImaintain/DocLinkageAdd.jsp -->

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
  String seq = request.getParameter("seq");
  String category = request.getParameter("category");
  String doc_name = request.getParameter("doc_name");
  String comment=request.getParameter("comm");

%>
<html:html>
<head>
<title>DocLinkageAdd</title>
</head>
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
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">Document Linkage Add</font>
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
                    <td height="20" width="8%" align="left">
                      <font size="2">Product</font>
                    </td>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="2"><%=pd_body %>                          /
version
<%=version %>                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2 border="0">
                  <tr class="list1">
                    <td height="20" width="20%">Category</td>
                    <td align="left" height="20" width="80%"><%=category %>                    </td>
                  </tr>
                  <tr class="list1">
                    <td height="20" width="20%">Doc Name</td>
                    <td align="left" height="20" width="80%"><%=doc_name%>                    </td>
                  </tr>
                <form action="<html:rewrite page="/OImaintain/docLinkageAddActionX.do"/>" method="post" enctype="multipart/form-data">
                  <input type="hidden" name="sid" value="<%=sid%>">
                  <input type="hidden" name="pd_body" value="<%=pd_body%>">
                  <input type="hidden" name="brand" value="<%=brand%>">
                  <input type="hidden" name="version" value="<%=version%>">
                  <input type="hidden" name="category" value="<%=category%>">
                  <input type="hidden" name="doc_name" value="<%=doc_name%>">
                  <input type="hidden" name="seq" value="<%=seq%>">
                  <input type="hidden" name="comment" value="<%=comment%>">
                  <tr>
                    <td align="left" colspan="2">
                      <font color="red">
                        <html:errors/>
                      </font>

                  </tr>
                  <tr>
                    <td align="center">上傳檔案</td>
                    <td align="left">
                      <input type="file" name="formname"/>
                      <input type="submit" class="button1" value="上傳檔案">
                      限PNG檔
                    </td>
                  </tr>
                </form>
                </table>
              </div>
              　
            </td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
<%@include file="../../index-down.jsp"%>
</body>
</html:html>
