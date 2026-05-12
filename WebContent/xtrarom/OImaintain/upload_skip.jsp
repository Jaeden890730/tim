<!-- /xtrarom/OImaintain/upload_skip.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>

<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>專案管理</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script><script language="JavaScript" type="text/javascript">

 function go_back(sid){

window.location="<html:rewrite page="/xtrarom/OImaintain/DocLinkage.jsp"/>?sid=" +sid;

}



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
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">Document Linkage</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">

                  </td>
                </tr>

                  <form name="form1" action="">



              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>

                    <tr class="list1">
                      <td height="20" align="center">
                        <font size="4">
                          <b>   上載檔案與原有檔案相同, 上傳中止!!
                          </b>
                        </font>
                      </td>
                    </tr>

                </table>
              </div>

              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr align="center">
                  <td height="20" colspan="2" align="center">


                        <input type="button" name="goback" value="返回上一頁" class="button1" onclick="go_back('<bean:write name="list7" scope="session"/>');">


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
