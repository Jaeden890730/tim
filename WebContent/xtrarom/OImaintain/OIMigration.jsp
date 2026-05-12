<!-- /xtrarom/OImaintain/OIMigration.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="java.util.*"%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">


<title>OI Migration</title>
  <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script><script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script><script type="text/javascript">
</script>

<script language="JavaScript" type="text/javascript">
 <!--
    function isEmpty(data){
      return ((data == null) || (data.length == 0 || data==' '));
    }

function migr(fm){
  //if(checkRadio("record_id","")) {
     if(checkdata1(fm)) {
      if(window.confirm("確定要送出嗎？")){
        //document.forms[0].listControl.value = 'migr';
    document.forms[0].submit();
        }
     }
  //}

}

function checkdata1(fm){


    if(isEmpty(fm.pd_body.value)){
      window.alert("請填寫Product Body");
      return false;
    }


    if(isEmpty(fm.version.value)){
      window.alert("請填寫 Version");
      return false;
    }else if(!isNumber(fm.version.value)){
      window.alert("只能填寫數字");
      return false;
    }


  return true;

}

function bt_back(){

	location.replace("<html:rewrite page="/Login/oiMainActionX.do?type=0"/>");
}

//-->

</script>





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
                    <font size="4">OI Migration</font>
                  </td>
                </tr>
                 <tr>
                <td height="20" colspan="2">
                  <hr width="100%" size="1" class="hr">
                </td>
              </tr>



              </table>
              <form name="form1" method="POST" action="<html:rewrite page="/OImaintain/oIMigrationActionX.do"/>">
              <div id="myDIV1" align="center" style="border:0;">
                <table   id="table27" cellspacing=1 cellpadding=0 class=table2 >
                  <thead align="left">
                    <tr align="left" class="titlen" >
                      <td colspan="2" align="left" height="20" ><b><font size="4">OI Migration</font></b></td>



                    </tr>

                  <tr align="left">
                    <td align="left" class="titlen"  height="20">
                     Product Body ：
                    </td>  <td align="left" class="listn"   height="20">
                    <input type="text" name="pd_body" value=""/>
                    </td>




                  </tr>
                   <tr  align="left" >
                   <input name="record_id" value="MX" type="hidden">
                   <!--<td height="20" align="left"class="titlen" >Brand ：</td>

                    <td   class="listn"  align="left" height="20"> <input type="radio" name="record_id" value="MX">MX <input type="radio" name="record_id" value="KH">KH</td>-->




                  </tr>
                    <tr  align="left" >
                     <td class="titlen"  align="left" height="20">Version</td>


                    <td class="listn"  align="left"  height="20"><input type="text" name="version" value=""/><font color="#FF0000" > 請確認 EPC 現行 OI 版本 </font></td>


                  </tr> </thead>

                </table>
              </div>

              <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                    <td height="20" colspan="2">
                    <input type="button" name="Migrade Data" value="MigradeData" class="button1" onclick="migr(this.form);">
                    <input type="button" name="Cancel" value="Cancel" class="button1" onclick="bt_back();return true;">

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
