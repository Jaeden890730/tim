<!-- /xtrarom/OImaintain/WS_Test_Parameter.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>
<%@ page import="com.mxic.oiplus.au.*" %>
<style>
#bj {
    height: 600px; 
    width:  900px;
    border: solid #d0cab7 3px;
    cursor: move;
    background-color: white;
    position: absolute;
    z-index: 10;
}
#bjlabel{ 
  background-color: #d0cab7;
}


</style>

<%
String t = (String) request.getAttribute("flag");
Vector temperatureList = OiMaintainService.getTemperatureList();
WsTestBean[] result2 = (WsTestBean[])request.getAttribute("WsParam");
ArrayList al = new ArrayList();
boolean doublicatePIM = false;
String doublicatePIMS = "";
if(result2 != null && result2.length > 0){
doublicatePIM = com.mxic.oiplus.oimaintain.OiMaintainService.doublicatePIMWS(result2[0].getSid());
doublicatePIMS = com.mxic.oiplus.oimaintain.OiMaintainService.doublicatePIMWSS(result2[0].getSid());
}
String All_Tester = "";
for(int i=0; i<result2.length; i++){
   if(!al.contains(result2[i].getTester())){
       al.add(result2[i].getTester());
       All_Tester += result2[i].getTester() + ",";
   }    
}
//System.out.println("All_Tester="+All_Tester);
%>
<html:html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=big5">
    <title>WS Test Parameter Information</title>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
    <link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
    <script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
    <script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
    <script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter-2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable2.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/prototype.js"/>'></script>

    <script type="text/javascript">

    function redirectAddPGM(tmp,sid){
      window.location="<html:rewrite page="/OImaintain/addFromPGMActionX.do"/>?pgmflag="+tmp+"&sid="+sid;
    }

    function redirectAddVendor(sid){
   	  if (checkCheckBox('raSel','PGM ', 1)) {
      var tmp=getRadioValue('raSel');
      window.location="<html:rewrite page="/OImaintain/addVendorActionX.do"/>?pgm_id="+tmp+"&sid="+sid;
      }
    }

    function redirectToSubmit(tmp){
      var result = showStatTemp(tmp, 1);	
      if(result){
	      if (checkdata(tmp)){
	        if(window.confirm("確定要送出嗎？\n因變更會影響criteria檢查\n會將Step 9: Basic Information Definition 打勾(Submit)取消")){
	          tmp.flag.value = 'submit';
	          tmp.submit();
	        }
	      }
      }
    }

    function checkcheck(tmp){
      if(checkdata(tmp)) {
        if(window.confirm("確定要送出嗎？")){
          tmp.submit();
        }
      }
    }
    function isEmpty(data){
      return ((data == null) || (data.length == 0 || data==' '));
    }


    function checkdata(fm){
      var chkObj = fm.pgm_id;
      
      if(chkObj == null)
    	  return true;
      
      for(var i=0;i<chkObj.length;i++){

          if(isEmpty(fm.temp[i].value)){
            window.alert("請填寫溫度");
            return false;
          }
          //HW Configure check
			var hw_configure_content = "hw_configure_content_"+i;
			var hw_configure_radio = "hw_configure_radio_"+i;
			document.getElementById(hw_configure_content).value;
			//alert("hw_configure_content="+hw_configure_content);
			//alert("hw_configure_radio.length="+document.getElementsByName(hw_configure_radio).length);
			if(document.getElementsByName(hw_configure_radio)[1].checked==true){
			     if(isEmpty(document.getElementById(hw_configure_content).value)){
				      window.alert("第 "+(i+1)+" 行請填寫 HW Configure");
				      return false;
				 }
				 //alert("hw_configure_content="+document.getElementById(hw_configure_content).value);
			}
      }
  	<%if (doublicatePIM == true) {%>
 	alert("新舊版程式並存，無法送出 請檢查程式 <%=doublicatePIMS%>");
		return false;
	<%}%>
      return true;

    }
    
    function cleardata(spanId,option3,field){
      //alert("spanId="+spanId);
      //alert("option3="+option3);
   	  if(option3==''){
        document.getElementById(field+spanId).innerHTML = '';
   	  }else{
    	document.getElementById(field+spanId).innerHTML = option3;
      } 	
      return true;
    }
    
    function getSite(spanId){
	    var site_span = "site_span_"+spanId;
	    var site_content = "site_content_"+spanId;
	    var site_content2 = document.getElementById(site_content).value;
	    //alert("site_content="+site_content);
	    //alert("site_content2="+site_content2);
	    window.open('<html:rewrite page="/common/Site.jsp?field='+site_span+'&field1='+site_content+'&field2='+site_content2+'"/>');
	}

    function getHWConfigure(spanId){
      var hw_configure_span = "hw_configure_span_"+spanId;
      var hw_configure_content = "hw_configure_content_"+spanId;
      //alert("hw_configure_span="+hw_configure_span);
      //onclick="window.open('../common/HWConfigure.jsp','miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')" 
      //window.open('../common/HWConfigure.jsp?field='+hw_configure_span,'miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')";
      //window.open('../common/HWConfigure.jsp?field='+hw_configure_span+'&field1='+hw_configure_content,'miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
      window.open('<html:rewrite page="/common/HWConfigure.jsp?field='+hw_configure_span+'&field1='+hw_configure_content+'"/>','miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
    }
    
    function getHWConfigure_By_Tester(){
        //alert("hw_configure_span="+hw_configure_span);
        //onclick="window.open('../common/HWConfigure.jsp','miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')" 
        //window.open('../common/HWConfigure.jsp?field='+hw_configure_span,'miniwin','scrollbars=1,toolbar=0,location=0,width=800,height=200')";
        //window.open('../common/HWConfigure.jsp?field='+hw_configure_span+'&field1='+hw_configure_content,'miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
        //alert("aaa="+document.getElementById("all_tester").value);
        var all_tester=document.getElementById("all_tester").value;
        window.open('<html:rewrite page="/common/HWConfigure_By_Tester.jsp?facility=WS&tester='+all_tester+'&field=hw_configure_span_&field1=hw_configure_content_&field2=hw_configure_radio_"/>','miniwin','scrollbars=1,toolbar=0,location=0,width=600,height=300');
    }
    

    function redirectDup(){
      var tmp=getRadioValue('raSel');
      if (checkRadio5("raSel","要複製的資料")){
        window.location="<html:rewrite page="/OImaintain/duplicateRowActionX.do"/>?pgm_id="+tmp;
      }
    }

    function redirectDelete(tmpform){
      var tmp=getCheckBox('raSel');
      var tmp1=tmpform.sid.value;
      var num=getCheckedNum('raSel');
      if (checkRadio5("raSel","要刪除的資料")){
        if(window.confirm("確定要刪除這 "+num+" 筆PGM嗎？")){
          window.location="<html:rewrite page="/OImaintain/delWSTestParamActionX.do"/>?pgm_id="+tmp+"&sid="+tmp1;
        }
      }
    }

    function redirectBackToMain(tmp1){
      window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid="+tmp1;
    }

    function redirectReset(tmpform){
      var tmp1=tmpform.sid.value;	
      if(window.confirm("確定要清除嗎？")){
        window.location="<html:rewrite page="/OImaintain/resetWSParamActionX.do"/>?sid="+tmp1;
      }
    }

    function redirectPDF(){
	window.location="<html:rewrite page="/OImaintain/goPDFPageActionX.do"/>";
    }
    function showStatTemp(tmp, sendflag) {
        var chkObj = tmp.pgm_id;
        var LineItem = new Array();
        if (chkObj == null) {
            return true;
        }

        if(chkObj.length == null || chkObj.length == 0) {
        	 window.alert("筆數過少，無須統計");
        	 return true;
        }
        
        for (var i = 0; i < chkObj.length; i++) {
            if (LineItem.length == 0) { //第一筆
                LineItem[0] = new Array();
                LineItem[0][0] = tmp.test_type[i].value;
                LineItem[0][1] = tmp.temp[i].value;
                continue;
            }
            for (var j = 0; j < LineItem.length; j++) {
                if (LineItem[j][0] == tmp.test_type[i].value) {
                    if (LineItem[j][1].indexOf(tmp.temp[i].value) < 0) {
                        LineItem[j][1] = LineItem[j][1] + ',' + tmp.temp[i].value;
                    }
                    break;
                } else if ((LineItem.length - 1) == j) {
                    LineItem[j + 1] = new Array();
                    LineItem[j + 1][0] = tmp.test_type[i].value;
                    LineItem[j + 1][1] = tmp.temp[i].value;
                }
            }
        }

        var msg = '<br><br><br><table align=center cellspacing=1 cellpadding=0 class=table2><thead><tr class=title1><td colspan=11 align=center>溫度統計</td></tr><tr class=title1><td>TEST_MODE</td>'
        msg = msg + '<td>溫度</td>'

        msg = msg + '</tr></thead><tbody><tr class=list1 onMouseOver="overcolor2(this);" onMouseOut="outcolor2(this);">';

        for (var i = 0; i < LineItem.length; i++) {
            msg = msg + '<td>' + LineItem[i][0] + '</td>';
            msg = msg + '<td>' + LineItem[i][1] + '</td>';
            msg = msg + '</tr>';
            if (i != LineItem.length - 1) {
                msg = msg + '<tr class=list1 onMouseOver="overcolor2(this);" onMouseOut="outcolor2(this);">';
            }

        }
        msg = msg + '<input type="button" name="close1" value="close" onClick="showAction('+sendflag+')" class="button1"></thead></table>'
        $("bj").style.left = "150px";
        $("bj").style.top = "100px";
        $("bj").innerHTML = msg;
        showAction(sendflag);
        return false;
    }

    function showAction(sendflag) {
        if ($("bj").style.display == "none")
            $("bj").style.display = "";
        else
            $("bj").style.display = "none";
        
        if (sendflag == 1 && $("bj").style.display == "none"){
	        if (checkdata(document.forms[0])){
		        if(window.confirm("確定要送出嗎？\n因變更會影響criteria檢查\n會將Step 9: Basic Information Definition 打勾(Submit)取消")){
		        	document.forms[0].flag.value = 'submit';
		        	document.forms[0].submit();
		        }
		    }
        }    
    }

    </script>

  </head>

<body topmargin="0" leftmargin="0">
<div class="bj" id="bj" style="display: none">	
	
</div>
<%@  include file="../../index-menu.jsp"%>
<table width="100%" border=0 class="bg1">
  <tr>
    <td valign="top">

      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="bg1" align="center">
        <tr>
          <td width="100%" height="490" valign="top">
          <br>
          <table width="95%" border="0" cellspacing="0" cellpadding="0" align="center">
            <tr>
              <td width="15%" height="25" class="title2">
              <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><font size="4">TIM</font></td>
              <td noWrap height="25" width="85%" class="title4"><font size="4">WS Test Parameter Information</font></td>
            </tr>
            <tr>
              <td height="20" colspan="2"><hr width="100%" color=#B4761B size="1"></td>
            </tr>
          </table>

          <html:form action="/OImaintain/saveSubmitWSActionX.do">
            <input type="hidden" id="flag" name="flag"/><!--flag將傳到SaveSubmitBomAction.do用來判斷是要SAVE還是SUBMIT-->
            <input type="hidden" id="all_tester" name="all_tester" value="<%=All_Tester%>"/>
            <div id="myDIV1" align="center"  >
              <table width="95%" border="0" id="table28">
                <tr>
                  <td>
		<% if (t.equals("Show")) { %>
                    <input type="button" name="save" value="Save" class = "button1" onclick="checkcheck(this.form);" />
                    <input type="button" name="AddPGM" value="Add from PGM(Buyoff)" class = "button1" onclick= "redirectAddPGM('buyoff','<bean:write name="proTestRouteBeanAFX" property="sid"/>');" />
                    <input type="button" name="AddPGM" value="Add from PGM(Release)" class = "button1" onclick= "redirectAddPGM('release','<bean:write name="proTestRouteBeanAFX" property="sid"/>');" />
                    <input type="button" name="AddVendor"  value="Add Vendor" class = "button1" onclick="redirectAddVendor('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"  />
                    <input type="button" name="Delete" value="Delete Row" class = "button1" onclick="redirectDelete(this.form);"   />
                    <input type="button" name="Submit" value="Submit" class = "button1" onclick="redirectToSubmit(this.form);"   />
                    <input type="button" name="Reset" value="Reset" class = "button1" onclick="redirectReset(this.form);"   />
		<% } %>
                    <input type="button" name="bak1" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"/>
                  </td>
                </tr>
              </table>
          <!--Data Table-->
              <table cellspacing=1 cellpadding=0 class=table2>
              <tr class="list1">
                    <td align="left"colspan="10"><font size="2"><b>Product :<input type="hidden" name="sid" value="<bean:write name="proTestRouteBeanAFX" property="sid"/>"/>
                      <bean:write name="proTestRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestRouteBeanAFX" property="version"/></b></font>
                    </td>
              </tr>
              </table>    
              <table id="table27" cellspacing=1 cellpadding=0 class=table2>
                <thead>
                  
                  <tr class="title1" align="left">
                    <td align="left" >  </td>
                    <td align="left" >PGM ID</td>
                    <td align="left" >Mask Opt.</td>
                    <td align="left" >Test Mode</td>
                    <td align="left" >Temperature(℃)<br><input type="button" value="Temperature Stat." class = "button1" onclick="showStatTemp(this.form);" /></td>
                    <td align="left" >Tester</td>
                    <td align="left" >Site</td>
                    <td align="left" >PGM Name</td>
                    <td align="left" >H/W Configure限制(單位:M Bit)
                      <input type="button" name="hw_configure_by_tester" value="Upload By Tester" class = "button1" onclick="getHWConfigure_By_Tester();" />
                    </td>
                    <td align="left" >PGM Special Control</td>
                    <td align="left" >Notes</td>
                    <td align="left" >One Main PGM Group Version</td>
                    
                  </tr>
                </thead>
                <tbody>
                  <logic:present name="WsParam">
                  <%
                    WsTestBean[] result1 = (WsTestBean[])request.getAttribute("WsParam");
                    int index = 0;
                  %>
                  <logic:iterate id="result" name="WsParam" indexId="i">
                  <tr class="list1">
                    
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="checkbox" name="raSel" value="<bean:write name="result" property="pgm_id"/>"/>
                    <input type="hidden" name="tag" value="<bean:write name="result" property="tag"/>"/>
                    </td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="pgm_id" value="<bean:write name="result" property="pgm_id"/>"/><bean:write name="result" property="pgm_id"/></td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="mask_option" value="<bean:write name="result" property="mask_option"/>"/><bean:write name="result" property="mask_option"/></td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="test_type" value="<bean:write name="result" property="test_type"/>"/><bean:write name="result" property="test_type"/></td>
                    <%
                      String option = OiMaintainService.getTemperatureOption(temperatureList, result1[index].getTemperature(), 0);
                    %>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><select name="temp"><%=option %></select></td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="tester" value="<bean:write name="result" property="tester"/>"/><bean:write name="result" property="tester"/></td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="site" value="<bean:write name="result" property="site"/>"/>
                    	<input type="button" id="site_radio_<bean:write name="i"/>" name="site_radio_<bean:write name="i"/>" value="Remove Site" checked="true" onclick="getSite(<bean:write name="i"/>)"  /><BR>
                       	<span align="left" id="site_span_<bean:write name="i"/>" name="site_span_<bean:write name="i"/>" ><bean:write name="result" property="site"/></span>
                       	<input type="hidden" id="site_content_<bean:write name="i"/>" name="site_content_<bean:write name="i"/>" value="<bean:write name="result" property="site"/>" />
                    </td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="program_name" value="<bean:write name="result" property="program_name"/>"/><bean:write name="result" property="program_name"/></td>
                    
                   
                        <% String optionHW = OiMaintainService.getSplitString(result1[index].getHw_configure()); %>
                        <td  bgcolor="<bean:write name="result" property="changecolor"/>">
                        <span id="hw_configure_radio_span_<bean:write name="i"/>" name="hw_configure_radio_span_<bean:write name="i"/>" >
                        <%  if((result1[index].getHw_configure()==null) || (result1[index].getHw_configure().equals("NA"))){ %>
	                            <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="NA" checked="true" onclick="cleardata(<bean:write name="i"/>,'','hw_configure_span_')" />NA
	                            <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="HWConfigure" onclick="getHWConfigure(<bean:write name="i"/>)"  />HWConfigure
	                            <br>
	                            <span id="hw_configure_span_<bean:write name="i"/>" name="hw_configure_span_<bean:write name="i"/>" ></span>
                                <input type="hidden" id="hw_configure_content_<bean:write name="i"/>" name="hw_configure_content_<bean:write name="i"/>" value="" />
	                      <%   }else{ %>
                                <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="NA" onclick="cleardata(<bean:write name="i"/>,'','hw_configure_span_')" />NA
                                <input type="radio" id="hw_configure_radio_<bean:write name="i"/>" name="hw_configure_radio_<bean:write name="i"/>" value="HWConfigure" checked="true" onclick="getHWConfigure(<bean:write name="i"/>)"  />HWConfigure
                                <br>
                                <span id="hw_configure_span_<bean:write name="i"/>" name="hw_configure_span_<bean:write name="i"/>" ><%=optionHW %></span>
                                <input type="hidden" id="hw_configure_content_<bean:write name="i"/>" name="hw_configure_content_<bean:write name="i"/>" value="<%=optionHW %>" />
	                      <%   }    %> 
	                    </span>       
                        </td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="pgm_special_control" value="<bean:write name="result" property="pgm_special_control"/>"/><bean:write name="result" property="pgm_special_control"/></td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="text" size = "30" name="tf_comment" value="<bean:write name="result" property="tf_comment"/>"/></td>
                    <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="program_name" value="<bean:write name="result" property="one_main_pgm_group_version"/>"/><bean:write name="result" property="one_main_pgm_group_version"/></td>
                    <%index++; %>
                  </tr>
                  </logic:iterate>
                  </logic:present>
                </tbody>
              </table>
    <!--下面的按鈕-->
              <table width="95%" border="0" id="table28">
                <tr>
                  <td>
          <% if (t.equals("Show")) { %>
                    <input type="button" name="save" value="Save" class = "button1" onclick="checkcheck(this.form);" />
                    <input type="button" name="AddPGM" value="Add from PGM(Buyoff)" class = "button1" onclick= "redirectAddPGM('buyoff','<bean:write name="proTestRouteBeanAFX" property="sid"/>');" />
                    <input type="button" name="AddPGM" value="Add from PGM(Release)" class = "button1" onclick= "redirectAddPGM('release','<bean:write name="proTestRouteBeanAFX" property="sid"/>');" />
                    <input type="button" name="AddVendor"  value="Add Vendor" class = "button1" onclick="redirectAddVendor('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"  />
                    <input type="button" name="Delete" value="Delete Row" class = "button1" onclick="redirectDelete(this.form);"   />
                    <input type="button" name="Submit" value="Submit" class = "button1" onclick="redirectToSubmit(this.form);"   />
                    <input type="button" name="Reset" value="Reset" class = "button1" onclick="redirectReset(this.form);"   />
          <%}%>
                    <input type="button" name="bak1" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"/>

                  </td>
                </tr>
              </table>
            </div>
            <br>
          </html:form>
            　
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
   <script type="text/javascript">
	  //20110531var st1 = new SortableTable(document.getElementById("table27"));
</script>
<%@  include file="../../index-down.jsp"%>
</body>
<script language="JavaScript">
    $("bj").style.left = "150px";
    $("bj").style.top = "80px";
    $("bj").onmousedown = function(evt) {
        evt = event || evt;
        px = parseInt($("bj").style.left);
        py = parseInt($("bj").style.top);
        x = evt.clientX;
        y = evt.clientY;
        document.onmousemove = move;
    }


    function move(evt) {
        evt = event || evt;
        $("bj").style.left = evt.clientX - x + px + "px";
        $("bj").style.top = evt.clientY - y + py + "px";
        document.onmouseup = function() {
            document.onmousemove = null;
        }
    }
</script>
<script type="text/javascript">
  var props = {
    filters_row_index: 1,
    loader: true,
    loader_html: '<img src="<html:rewrite page="/image/loader.gif"/>" alt="" style="margin: 0pt 5px; vertical-align: middle;"><span>Loading...</span>',
    status_bar: false,
//        col_0: "none",
    enter_key: true
  };

      setFilterGrid("table27",props);
</script>
</html:html>



