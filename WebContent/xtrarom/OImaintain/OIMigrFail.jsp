<!-- /xtrarom/OImaintain/OIMigrFail.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%




%>
<html>
<head>
<title>DocLinkageAdd</title>
</head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
<table width="100%" border=0 class="bg1">
  <tr>
    <!--<td valign="top" width="159" class="bg">-->
</td>    <td valign="top">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
        <tr>
          <td width="100%" height="490" valign="top">
            <br>
            <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
              <tr>
                <td width="20%" height="25" class="title2">
                  <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                  <font size="4">OI Migration</font>
                </td>
                <td noWrap height="25" width="80%" class="title4">
                  <font size="2">Information</font>
                </td>
              </tr>
              <tr>
                <td height="20" colspan="2">
                  <hr width="100%" size="1" class="hr">
                </td>
              </tr>




            </table>
            <div id="myDIV1" align="center" style="border:0;">
              <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                <thead>

                  <tr class="list1">
                    <td height="20" align="left">
                      <font color="red" size="4"> <bean:write name="information" />!!</font>
                    </td>
                  </tr>
                </thead>
              </table>
            </div>
            <div id="myDIV1" align="center" style="border:0;">
              <table id="table27" cellspacing=1 cellpadding=0 class=table2 border="0">


                <form  action="<html:rewrite page="/xtrarom/OImaintain/OIMigration.jsp"/>" method="post">



                       <tr>

                    <td align="left">

                      <input type="submit" value="ªð¦^¤W¤@­¶"  class="button1">

                    </td>

                  </tr>


            </form>


              </table>
            </div>

            ¡@
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
<%@include file="../../index-down.jsp"%>
</body>
</html>

