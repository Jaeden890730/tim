<!-- /xtrarom/OImaintain/DocLinkage.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<%@page import="com.mxic.oiplus.xtrarom.oimaintain.*"%>
<%@page import="com.mxic.oiplus.resource.TDSResource"%>
<%@page import="com.mxic.oiplus.au.*"%>
<%

String txpath = TDSResource.getProperties("TIMPdf").getValue("jpg_tx_dl.dir");

int sid = Integer.parseInt(request.getParameter("sid"));
boolean status_apply_bo = FTService.status_apply(sid);
OiMaintainStep ois = OiMaintainService.SearchFunction(String.valueOf(sid));
String creator=ois.getCreator();
String sp1=ois.getSponsor_1();
String sp2=ois.getSponsor_2();
User Auth=(User)session.getAttribute("user");
String user=Auth.getUserName();
boolean chkUser;
if(user.equals(creator)){
    chkUser=true;
} else if (user.equals(sp1)){
    chkUser=true;
} else if (user.equals(sp2)){
    chkUser=true;
} else {
    chkUser=false;
}
FTTestActionForm rs = FTService.getInfo(sid);
request.setAttribute("list1", rs);
DocLinkageActionForm[] rs2 = DocLinkageService.getInitialInfo(sid);
request.setAttribute("list2", rs2);
DocLinkageActionForm[] rs3 = DocLinkageService.getInitialInfo_comment(sid);
request.setAttribute("list3", rs3);
%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM - Document Linkage </title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script><script language="JavaScript" type="text/javascript">

<!--
function upload_data(tmp,tmp1,tmp2,tmp3,tmp4,tmp5,tmp6,tmp7){
    window.location="<html:rewrite page="/OImaintain/docLinkageUploadActionX.do"/>?sid="+tmp+"&pd_body="+tmp1+"&brand="+tmp2+"&version="+ tmp3 + "&seq="+ tmp4 + "&category="+tmp5 + "&doc_name="+tmp6 + "&comm="+tmp7;
}

function isEmpty(data){
    return ((data == null) || (data.length == 0 || data==' '));
}

function isLarge(data){
    return (data.length <= 2);
}

function delete_row(){
    if(checkdata()) {
        if(window.confirm("確定要送出嗎？")){
            document.forms[0].listControl.value = 'delete_row';
            document.forms[0].submit();
        }
  }
}

function reset_tx(){
    if(window.confirm("確定要送出嗎？")){
        document.forms[0].listControl.value = 'reset_tx';
        document.forms[0].submit();
  }
}

function update_data(tmp){
    if(checkdata1(tmp)) {
        if(window.confirm("確定要送出嗎？")){
            document.forms[0].listControl.value = 'update_data';
            document.forms[0].submit();
        }
    }
}

function submit_data(tmp){
    if(checkdata1(tmp)) {
        if(window.confirm("確定要送出嗎？")){
   	        document.forms[0].listControl.value = 'submit_data';
       	    document.forms[0].submit();
        }
    }
}

function checkdata(){
    var b1=document.form1.record_id;

    if(b1 == null){
        return false;
    }

    if (b1.length > 1) {
    	for(var i=0;i<b1.length;i++){
            if(b1[i].checked){
                return true;
	    }
    	}
    } else {
        if (b1.checked)
    	    return true;
    }
    window.alert("請點選一項目");
    return false;
}

function checkdata1(fm){
    var chkObj =fm.record_id;
    if (chkObj == null)
    	return true; // Document linkage 即將被 Yield Definition 取代，因此若無資料 OK

    for(var i=0;i<chkObj.length;i++){
        if(isEmpty(fm.doc_name[i].value)){
            window.alert("請填寫 Doc Name");
            return false;
        }
        if(isEmpty(fm.comment[i].value)){
            window.alert("請填寫 Comment");
            return false;
        }
    }
    // Document 以 doc_name and comment 為 key
    for(var i=0;i<chkObj.length;i++){
	for(var j=i+1;j<chkObj.length;j++){
	        if (fm.doc_type[i].value == fm.doc_type[j].value
	            && fm.doc_name[i].value == fm.doc_name[j].value
	            && trim(fm.comment[i].value) == trim(fm.comment[j].value)) {
        	    window.alert("相同的 Document Name: '" + fm.doc_name[i].value + "' Commnet 不可重覆！");
	            return false;
        	}
	}
    }
    return true;
}

function add_flow(sid,pd_body,brand,version){
    window.location="<html:rewrite page="/xtrarom/OImaintain/DocLinkageAddNew.jsp"/>?sid=" +sid +"&pd_body=" + pd_body +"&brand="+ brand +"&version=" + version;
}

function back(sid){
    window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid=" +sid;
}

function open_new(file_name){
    window.open("../../File/" + file_name);
    document.forms[0].listControl.value = 'reload';
    document.forms[0].submit();
}

//-->
</script></head>
<body topmargin="0" leftmargin="0">
<%@include file="../../index-menu.jsp"%>
  <table width="100%" border=0 class="bg1">
    <tr>
      <!--<td valign="top" width="159" class="bg">-->
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
                    <font size="4">Document Linkage</font>
                  </td>
                </tr>
                <tr>
                  <td height="20" colspan="2">
                    <hr width="100%" size="1" class="hr">
                  </td>
                </tr>
                <%if (status_apply_bo == true &&chkUser) {%>
                <tr>
                  <td height="20" colspan="2"><font color="#C00000">注意：若需要刪除所有資料，請先按 "Submit" 後，再做刪除動作，否則系統將再自動帶入前一版資料。</font>
                  </td>
                </tr>                
                <%}%>
              </table>
              <form name="form1" method="POST" action="<html:rewrite page="/OImaintain/docLinkageActionX.do"/>?sid=<%=String.valueOf(sid)%>&pd_body=<bean:write name="list1" property="pd_body"/>&brand=<bean:write name="list1" property="brand"/>&version=<bean:write name="list1" property="version"/>">
              <input name="listControl" type="hidden">
              <div id="myDIV1" align="center">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr bgcolor="white">
                      <td bgcolor="white" height="20" colspan="2">
                            <%if (status_apply_bo == true &&chkUser) {%>
                            <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form);">
                            <input type="button" name="AddNewFlow" value="Add Document" class="button1"  onclick="add_flow('<%=sid%>','<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>');">
                            <input type="button" name="RemoveFlow" value="Remove Document" class="button1" onclick="delete_row();">
                            <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form);">
                            <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx();">
                            <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                  <%} else {%>
                    <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                  <%}%>
                      </td>
                    </tr>
                    <tr class="list1">
                      <td height="20" align="left">
                        <font size="2">
                          <b>Product :
                                <bean:write name="list1" property="pd_body"/>
                                / Version
                                <bean:write name="list1" property="version"/>
                          </b>
                        </font>
                      </td>
                    </tr>
                  </thead>
                </table>
              </div>
              <div id="myDIV1" align="center">
                <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                  <thead>
                    <tr class="title1">
                      <td height="20">Document Type</td>
                      <td height="20">Old Document</td>
                      <td height="20">Document Name</td>
                      <td height="20">Comment</td>
                       <td height="20">New Document</td>
                          <%if (status_apply_bo == true&&chkUser) {%>
                           <td height="20">Upload</td>
                            <%} else {%>
                            <%}%>
                    </tr>
                  </thead>
                   <logic:present name="list3">
                    <logic:iterate id="result3" name="list3">
                      <tr class="list1">
                        <td class="title1" height="20">
                          COMMENT
                        </td>
                        <td colspan="4" height="20" >
                                <a target="_blank"  href='<%=txpath%><bean:write name="result3" property="file_name_comment"/>'><img border="0"  style="<bean:write name="result3" property="display_comment"/>" name="image_new" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a>
                        </td>
                         <%if (status_apply_bo == true &&chkUser) {%>
                           <td height="20">
                         <input type="button" name="upload" value="upload" class="button1" onclick="upload_data(<%=sid%>,'<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>','0','M','comment','comment');">
                        </td>
                            <%} else {%>
                            <%}%>
                      </tr>
                    </logic:iterate>
                  </logic:present>
                  <logic:present name="list2">
                    <logic:iterate id="result2" name="list2">
                      <tr class="list1">
                        <td class="title1" height="20">
                          <bean:write name="result2" property="category"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result2" property="changecolor"/>" >
                          <!--  marked 20080114input style="<bean:write name="result2" property="display_one"/>" type="radio" name="record_id" value="<bean:write name="result2" property="seq"/>,<bean:write name="result2" property="category"/>,<bean:write name="result2" property="file_name_old"/>,<bean:write name="result2" property="file_name_new"/>">-->
                          <input type="radio" name="record_id" value="<bean:write name="result2" property="seq"/>,<bean:write name="result2" property="category"/>,<bean:write name="result2" property="file_name_old"/>,<bean:write name="result2" property="file_name_new"/>">
                          <input type="hidden" name="sid" value="<%=sid%>">
                          <input type="hidden" name="pd_body" value="<bean:write name="list1" property="pd_body"/>">
                          <input type="hidden" name="brand" value="<bean:write name="list1" property="brand"/>">
                          <input type="hidden" name="version" value="<bean:write name="list1" property="version"/>">
                          <input type="hidden" name="doc_type" value="<bean:write name="result2" property="doc_type"/>"/>
                          <input type="hidden" name="seq" value="<bean:write name="result2" property="seq"/>"/>
                          <input type="hidden" name="doc_name_be" value="<bean:write name="result2" property="doc_name"/>"/>
                          <input type="hidden" name="comment_be" value="<bean:write name="result2" property="comment"/>"/>
                          <input type="hidden" name="display_old" value="<bean:write name="result2" property="display_old"/>"/>
                          <input type="hidden" name="display_new" value="<bean:write name="result2" property="display_new"/>"/>
                                <a target="_blank"  href='<%=txpath%><bean:write name="result2" property="file_name_old"/>'><img border="0"  style="<bean:write name="result2" property="display_old"/>" name="image_new" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result2" property="changecolor"/>">
                          <input type="text" name="doc_name" value="<bean:write name="result2" property="doc_name"/>"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result2" property="changecolor"/>">
                          <input type="text" name="comment" value="<bean:write name="result2" property="comment"/>"/>
                        </td>
                        <td height="20" bgcolor="<bean:write name="result2" property="changecolor"/>" >
                         <a target="_blank" href='<%=txpath%><bean:write name="result2" property="file_name_new"/>'><img border="0"  style="<bean:write name="result2" property="display_new"/>" name="image_new" src='<html:rewrite page="/image/gif.gif"/>' alt=""></a>
                        </td>
                         <%if (status_apply_bo == true &&chkUser) {%>
                           <td height="20" bgcolor="<bean:write name="result2" property="changecolor"/>">
                         <input type="button" name="upload" value="upload" class="button1" onclick="upload_data(<%=sid%>,'<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>','<bean:write name="result2" property="seq"/>','<bean:write name="result2" property="category"/>','<bean:write name="result2" property="doc_name"/>','<bean:write name="result2" property="comment"/>');">
                        </td>
                            <%} else {%>
                            <%}%>
                      </tr>
                    </logic:iterate>
                  </logic:present>
                </table>
              </div>

              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                  <td height="20" colspan="2">
                               <%if (status_apply_bo == true && chkUser) {%>
                            <input type="button" name="save" value="Save" class="button1" onclick="update_data(this.form);">
                            <input type="button" name="AddNewFlow" value="Add Document" class="button1"  onclick="add_flow('<%=sid%>','<bean:write name="list1" property="pd_body"/>','<bean:write name="list1" property="brand"/>','<bean:write name="list1" property="version"/>');">
                            <input type="button" name="RemoveFlow" value="Remove Document" class="button1" onclick="delete_row();">
                            <input type="button" name="Submit" value="Submit" class="button1" onclick="submit_data(this.form);">
                            <input type="button" name="Reset" value="Reset" class="button1" onclick="reset_tx();">
                            <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                  <%} else {%>
                    <input type="button" name="goback" value="回維護主畫面" class="button1" onclick="back('<%=sid%>');">
                  <%}%>
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
