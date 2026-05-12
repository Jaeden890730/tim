<!-- /xtrarom/OImaintain/DocLinkage_bak.jsp -->

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
                    <font size="4">Document Linkage </font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                      <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
    <form action="" method="post" name="form1">


              </table>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="list1">
                      <td height="20" width="20%" align="left">

                          Product

                      </td>
                       <td height="20" align="left">

                          6615 /MX /Version 3

                      </td>
                    </tr>
                     <tr bgcolor="white">
                  <td  bgcolor="white" height="20" colspan="2">
                    <input type="submit" name="save" value="Save" class="button1">
                    <input type="submit" name="AddNewFlow" value="Add New Flow" class="button1"   >
                    <input type="submit" name="RemoveFlow" value="Remove Flow" class="button1"  >

                    <input type="submit" name="Submit" value="Submit" class="button1">
                    <input type="submit" name="Reset" value="Reset" class="button1">
                    <input type="submit" name="goback" value="回維護主畫面" class="button1"  onclick="document.form1.action='maintain_index.jsp'">
                  </td>
                </tr>
                  </thead>
                </table>
              </div>
              <div id="myDIV1" align="center" style="border:0;">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="title1">
                      <td height="20" width="10%">Category </td>
                      <td height="20" width="10%">Old Doc.</td>
                      <td height="20" width="20%">Document Name</td>
                      <td height="20" width="5%">New Doc.</td>
                      <td height="20" width="20%">Comment</td>
                      <td height="20" width="35%">Upload</td>

                    </tr>
                  </thead>
                  <tr class="list1">
                    <td class="title1" height="20">
                     Yield Definition
                    </td>
                    <td height="20"><input type="image" id="image1" src='<html:rewrite page="/image/pdf.gif"/>'  /></td>
                    <td height="20"><input type="text" id="text1" value="6615 Yield"/></td>
                    <td height="20"><input type="image" id="image1" src='<html:rewrite page="/image/pdf.gif"/>'  /></td>
                    <td height="20"><input type="text" id="text1" value=""/></td>
                    <td height="20">
                    <input type="submit" name="Upload" value="Upload" class="button1">
                    </td>

                  </tr>
                   <tr bgcolor="pink" align="center" >
                    <td  class="title1"  height="20">
                     Test Flow
                    </td>
                    <td height="20"><input type="radio" name="record_id" value=""><input type="image" id="image1" src='<html:rewrite page="/image/pdf.gif"/>'  /></td>
                    <td height="20"><input type="text" id="text1" value="6615 Yield"/></td>
                    <td height="20"><input type="image" id="image1" src='<html:rewrite page="/image/pdf.gif"/>'  /></td>
                    <td height="20"><input type="text" id="text1" value=""/></td>
                    <td height="20">
                    <input type="submit" name="Upload" value="Upload" class="button1">
                    </td>

                  </tr>
                  <!--
                    <logic:present name="list">
                    <logic:iterate id="result" name="list">
                    <tr class="list1">
                    <td height="20"><input type="radio" name="record_id" value="<bean:write name="result" property="record_id"/>"  ></td>
                    <td height="20">
                    <bean:write name="result" property="c_name"/>
                    </td>
                    <td height="20">
                    <bean:write name="result" property="start_Date"/>
                    </td>
                    <td height="20">
                    <bean:write name="result" property="end_Date"/>
                    　
                    </td>
                    <td height="20">
                    <bean:write name="result" property="leave_days"/>
                    </td>
                    <td height="20">
                    <bean:write name="result" property="description"/>
                    </td>
                    <td height="20">
                    <bean:write name="result" property="status"/>
                    </td>
                    </tr>
                    </logic:iterate>
                    </logic:present>
                    <br>
                    <tr>   <td width="15%" height="25" class="title2">
                    <input type="submit" name="submit"  value="刪除">
                    </td>
                    </tr>
                  -->
                </table>
              </div>

              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td  height="20" colspan="2">
                    <input type="submit" name="save" value="Save" class="button1">
                    <input type="submit" name="AddNewFlow" value="Add New Flow" class="button1">
                    <input type="submit" name="RemoveFlow" value="Remove Flow" class="button1" >

                    <input type="submit" name="Submit" value="Submit" class="button1">
                    <input type="submit" name="Reset" value="Reset" class="button1">
                    <input type="submit" name="goback" value="回維護主畫面" class="button1"  onclick="document.form1.action='maintain_index.jsp'">
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
