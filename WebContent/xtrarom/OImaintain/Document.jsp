<!-- /xtrarom/OImaintain/Document.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.au.*"%>
<%
  int sid = Integer.parseInt(request.getParameter("sid"));
  OiMaintainStep ois = OiMaintainService.SearchFunction(String.valueOf(sid));
  String creator=ois.getCreator();
String sp1=ois.getSponsor_1();
String sp2=ois.getSponsor_2();
  User Auth=(User)session.getAttribute("user");
  String user=Auth.getUserName();
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
  FTTestActionForm rs = FTService.getInfo(sid);
  request.setAttribute("list1", rs);
  boolean flag = FTService.tf_final_exit(sid);
  boolean flag1 = FTService.had_had(sid);
%>
<html:html>
<script language="JavaScript" type="text/javascript">
 <!--


 function compare(sid,pd_body,brand,version,status){

   window.location="<html:rewrite page="/xtrarom/OImaintain/EditiionCompare.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version +"&status=" + status;

 }
 function release_tf(sid,pd_body,brand,version,status){
      window.location="<html:rewrite page="/OImaintain/oIReleaseActionX.do"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version +"&status=" + status;
   //document.forms[0].listControl.value = 'rel';
  // document.forms[0].submit();

 }

function PDFassign(tmp){
tmp.listControl.value = 'PDFList';
   tmp.submit();
}
 function back(sid){

   window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid=" +sid;

 }

 function asign(sid,flag1){
   if (flag1==false){
     //System.out.println("YY")
     if(window.confirm("確定要提出申請嗎？此文件將會被鎖住!!")){
       document.forms[0].listControl.value = 'lock';
       document.forms[0].submit();

     }

   } else {
     alert("已提過申請!!");
     return;
   }

 }



//-->

</script><head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>專案管理</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
          <tr>
            <td width="100%" height="490" valign="top">
              <br>
              <form name="form1" action="<html:rewrite page="/OImaintain/asignActionX.do"/>?sid=<%=sid%>&pd_body=<bean:write name="list1" property="pd_body"/>&brand=<bean:write name="list1" property="brand"/>&version=<bean:write name="list1" property="version"/>">
                <input type="hidden" name="sid" value="<%=sid%>">
                <input type="hidden" name="pd_body" value="<bean:write name="list1" property="pd_body"/>">
                <input type="hidden" name="brand" value="<bean:write name="list1" property="brand"/>">
                <input type="hidden" name="version" value="<bean:write name="list1" property="version"/>">
                <input type="hidden" name="status" value="<bean:write name="list1" property="status"/>">
                <input name="listControl" type="hidden">
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt="">
                    <font size="4">TIM</font>
                  </td>
                  <td noWrap height="25" width="85%" class="title4">
                    <font size="4">文件管理</font>
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
                        <font size="2">
                          <b>                            Product :
                                <bean:write name="list1" property="pd_body"/>
                                / Version <bean:write name="list1" property="version"/>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <br/>
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
<%//if (chkUser){
%>
                        <!--lai暫時mark<logic:notEqual name="list1" property="version" value="0">
                          <input type="button" name="save" value="版本差異比較(Web)" class="button1" onclick="compare('<%=sid%>','<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>','<bean:write name="list1" property="status"/>');">
                        </logic:notEqual>-->
                        <input type="button" name="AddNewFlow" value="文件列表(PDF)" class="button1" onclick="PDFassign(this.form);">
<%//}
%>
                      <%if (flag == true && chkUser) {                      %>
                        <input type="button" name="RemoveFlow" value="產生PBR/ECR申請文件" class="button1" onclick="asign('<%=sid%>',<%=flag1%>);">
                      <%}                      %>
                        <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
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
