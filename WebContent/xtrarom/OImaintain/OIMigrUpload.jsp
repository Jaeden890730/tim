<!-- /xtrarom/OImaintain/OIMigrUpload.jsp -->

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
<title>OI Migration</title>
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

                  <tr class="list1">
                    <td height="20" align="left">
                      <font size="2">  <logic:present name="list2">
                    <logic:iterate id="result1" name="list2" ><b>Product Body: <bean:write name="result1" property="pd_body"/>
 / Version
<bean:write name="result1" property="version"/></b></logic:iterate>
                                </logic:present></font>
                    </td>
                  </tr>
                </thead>
              </table>
            </div>
            <div id="myDIV1" align="center" style="border:0;">
              <table id="table27" cellspacing=1 cellpadding=0 class=table2 border="0">


                <form  action="<html:rewrite page="/OImaintain/oIMigrationUploadActionX.do"/>" method="post" enctype="multipart/form-data">
                  <input type="hidden"  name="pd_body" value="<bean:write name="result1" property="pd_body"/>"/>
                  <input type="hidden"  name="brand" value="<bean:write name="result1" property="brand"/>"/>
                  <input type="hidden"  name="version" value="<bean:write name="result1" property="version"/>"/>



                  <tr>
                    <td align="left" colspan="2">
                      <font color="red">
                        <html:errors/>
                      </font>

                  </tr>
                  <tr>
                    <td align="left">XtraROM BOM VS Test Route </td>
                    <td align="left">
                       <input type="file" name="tf_bom_route_xrom"/>


                    </td>

                  </tr>
                  <tr>
                    <td align="left">XtraROM BOM ReRoute VS Test Route </td>
                    <td align="left">
                       <input type="file" name="tf_bom_reroute_xrom"/>


                    </td>

                  </tr>
                     <tr>
                    <td align="left">WS Test Parameter</td>
                    <td align="left">
                       <input type="file" name="tf_test_parameter_ws"/>


                    </td>

                  </tr>
                     <tr>
                    <td align="left">FT Test Parameter</td>
                    <td align="left">
                       <input type="file" name="tf_test_parameter_ft"/>


                    </td>

                  </tr>
                     <tr>
                    <td align="left">Burn-In/Cycling/AVI Test Parameter</td>
                    <td align="left">
                       <input type="file" name="tf_test_parameter_pbc"/>


                    </td>

                  <!--</tr>
                      <tr>
                    <td align="left">Basic Information</td>
                    <td align="left">
                       <input type="file" name="tf_basic_information"/>


                    </td>

                  </tr>-->
                       <tr>
                    <td align="left">上傳檔案</td>
                    <td align="left">

                      <input type="submit" value="上傳檔案"   class="button1">

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
</html>
