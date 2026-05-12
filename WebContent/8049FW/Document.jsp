<!-- 8049FW/Document.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.fw8049.action.*"%>
<%@page import="com.mxic.fw8049.dao.*"%>
<%
String sid = request.getParameter("sid");
String pd_body = request.getParameter("pd_body");
String brand = request.getParameter("brand");
//   OiMaintainStep ois = OiMaintainService.SearchFunction(String.valueOf(sid));
//   String creator=ois.getCreator();
// String sp1=ois.getSponsor_1();
// String sp2=ois.getSponsor_2();
//   User Auth=(User)session.getAttribute("user");
//   String user=Auth.getUserName();
//   boolean chkUser;
//  if(user.equals(creator)){
//   chkUser=true;
//  }else if(user.equals(sp1)){
// 	chkUser=true;
//  }else if(user.equals(sp2)){
// 	chkUser=true;
//  }else{
//     chkUser=false;
//  }
  List<Fw8049MainActionForm> rs = Fw8049mationDao.selectList(sid, pd_body, brand);
  request.setAttribute("list1", rs.get(0));
//   boolean flag = FTService.tf_final_exit(sid,rs.getPackage_component());
//   boolean flag1 = FTService.had_had(sid);
  
//   String showMessage = (String)request.getAttribute("message");
//   if(showMessage == null){
//         showMessage = "";
//   }
//   String showMessage1 = (String)request.getAttribute("message1");
//   if(showMessage1 == null){
//       showMessage1 = "";
//   }

%>
<html:html>
<script language="JavaScript" type="text/javascript">
<%-- var message = '<%=showMessage%>'; --%>
if(message != "" ){
    alert(message);
}

<%-- var message1 = '<%=showMessage1%>'; --%>
if(message1 != "" ){
<%--     if(window.confirm("注意，<%=showMessage1%>")){ --%>
<%--     	window.location="<html:rewrite page="/OImaintain/asignAction.do"/>?sid=<%=sid%>&pd_body=<%=rs.getPd_body()%>&brand=<%=rs.getBrand()%>&version=<%=rs.getVersion()%>&listControl=lock&message1=<%=showMessage1%>"; --%>
//       }
}
function on_pdf(tmp1,tmp2,tmp3){
	alert("該功能僅抓取目前PDF內容，若要產生新PDF，請按 產生文件列表(PDF)");
    window.location="<html:rewrite page="/OImaintain/goPDFPageAction.do"/>?pro_b="+tmp1+"&brand="+tmp2+"&version="+tmp3+"&tx=tx";
  }



 function compare(sid,pd_body,brand,version,status){

   window.location="<html:rewrite page="/OImaintain/EditiionCompare.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version +"&status=" + status;

 }
 function release_tf(sid,pd_body,brand,version,status){
      window.location="<html:rewrite page="/OImaintain/oIReleaseAction.do"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version +"&status=" + status;
   //document.forms[0].listControl.value = 'rel';
  // document.forms[0].submit();

 }

function PDFassign(tmp){
	document.forms[0].act.value = 'PDFList';
   	tmp.submit();
}
 function back(sid){

   window.location="<html:rewrite page="/fw8049/Fw8049MainAction.do"/>?sid=" +sid;

 }

 function asign(sid,flag1){
   if (flag1==false){
     //System.out.println("YY")
     if(window.confirm("注意，此文件將會被鎖住!!\n再次提醒，輸入於 Step2 之 WS/FT Comment 僅供旺宏內部使用，不會出現於各子文件中!!\n\n確定要提出申請嗎？")){
       document.forms[0].listControl.value = 'lock';
       document.forms[0].submit();

     }
   } else {
     alert("已提過申請!!");
     return;
   }
 }

</script><head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>專案管理</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
          <tr>
            <td width="100%" height="490" valign="top">
              <br>
              <form name="form1" action="<html:rewrite page="/8049fw/Fw8049MainAction.do"/>?sid=<%=sid%>&product_body=<bean:write name="list1" property="product_body"/>&brand=<bean:write name="list1" property="brand"/>&version=<bean:write name="list1" property="version"/>">
                <input type="hidden" name="sid" value="<%=sid%>">
				<input name="act" id="act" type="hidden">
                <input type="hidden" name="product_body" value="<bean:write name="list1" property="product_body"/>">
                <input type="hidden" name="brand" value="<bean:write name="list1" property="brand"/>">
                <input type="hidden" name="version" value="<bean:write name="list1" property="version"/>">
                <input type="hidden" name="status" value="<bean:write name="list1" property="status"/>">
                <input name="listControl" type="hidden">
                <input name="message1" type="hidden">
              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td width="15%" height="25" class="title2">
                    <img src="../image/arrow.gif" width="5" height="14" hspace="3" alt="">
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
                          <b>Product : <bean:write name="list1" property="product_body"/>
                          /
                          <bean:write name="list1" property="brand"/>
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
                        <input type="button" name="AddNewFlow" value="產生文件列表(PDF)" class="button1" onclick="PDFassign(this.form);">
                        <input type="button" name="AddNewFlow" value="查詢文件列表(PDF)" class="button1" 
                        title="若產生文件列表(PDF)等待時間過久(over 10mins)，可按此功能查看" 
                        onclick="on_pdf('<bean:write name="list1" property="product_body"/>',　'<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>');">
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
<%@include file="../index-down.jsp"%>
</body>
</html:html>
