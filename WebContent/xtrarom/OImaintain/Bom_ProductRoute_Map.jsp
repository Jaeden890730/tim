<!-- /xtrarom/OImaintain/Bom_ProductRoute_Map.jsp -->

<%@page contentType="text/html; charset=big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="com.mxic.oiplus.xtrarom.oimaintain.*" %>

<%
String t = (String) request.getAttribute("flag");
String pro_b=(String) request.getAttribute("pro_b");
String brand=(String) request.getAttribute("brand");
String entry=(String) request.getParameter("entry");
if (entry == null) entry = "false";
//String sid=(String) request.getAttribute("sid");

/*將Product_Body和Brand帶入下列2個程式後分別會傳回FW 與 FP ROUTE*/
ProTestRouteBean[] pt=OiMaintainService.RWRoute(pro_b,brand);
ProTestRouteBean[] FTpt=OiMaintainService.RPFTRoute(pro_b,brand);
RouteNameBean[] Route_Cat=OiMaintainService.GetFTRouteCat(pro_b,brand);
BomProductRouteBean[] soroute=OiMaintainService.GetSortRouteCode_Normal(pro_b,brand);
BomProductRouteBean[] sorouteTX=OiMaintainService.GetSortRouteCodeTX_Normal(pro_b,brand);
/*將傳回的FW 與 FP ROUTE分別放入2個StringBuffer*/
StringBuffer option = new StringBuffer();
StringBuffer ftoption=new StringBuffer();
StringBuffer routeaddoption = new StringBuffer();
StringBuffer ftrouteaddoption=new StringBuffer();
StringBuffer routecatoption=new StringBuffer();

if (pt != null){
    option.append("<option value='NA'>NA</option>\n");
    for(int i=0;i<pt.length;i++){
        option.append("<option value='" + pt[i].getRoutename() + "' >");
        option.append(pt[i].getRoutename());
        option.append("</option>\n");
        routeaddoption.append("<option value='" + pt[i].getRoutename() + "' >");
        routeaddoption.append(pt[i].getRoutename());
        routeaddoption.append("</option>\n");
    }
}

if (FTpt != null){
    ftoption.append("<option value='NA'>NA</option>\n");
    for(int i=0;i<FTpt.length;i++){
        ftoption.append("<option value='" + FTpt[i].getRoutename() + "' >");
        ftoption.append(FTpt[i].getRoutename());
        ftoption.append("</option>\n");
        ftrouteaddoption.append("<option value='" + FTpt[i].getRoutename() + "' >");
        ftrouteaddoption.append(FTpt[i].getRoutename());
        ftrouteaddoption.append("</option>\n");
    }
}

if (Route_Cat != null){
	routecatoption.append("<option value='NA'>NA</option>\n");
    for(int i=0;i<Route_Cat.length;i++){
        routecatoption.append("<option value='" + Route_Cat[i].getRoutecat() + "' >");
        routecatoption.append(Route_Cat[i].getRoutecat());
        routecatoption.append("</option>\n");
    }
}
%>

<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>TIM BOM VS Product Route - Normal Production Maintenance</title>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/css.css"/>'>
<link rel="stylesheet" type="text/css" href='<html:rewrite page="/css/filtergrid.css"/>'>
<script src='<html:rewrite page="/js/util.js"/>' language="javascript"></script>
<script src='<html:rewrite page="/js/areaSelect.js"/>' language="javascript"></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/tablefilter.js"/>'></script>
<script language="javascript" type="text/javascript" src='<html:rewrite page="/js/sortabletable.js"/>'></script>

<script type="text/javascript">
function getLineByItem(item){
	return item.parentNode.parentNode.cells[1].innerText;
}
function chkSortRouteCode(fm) {   
        var productbody = fm.productbody.value;
        var returnvalue = true;
        //alert("productbody="+productbody+",maskopt="+maskopt+",brand="+brand+",sortroutecode="+sortroutecode);
        <%
        
        if (soroute != null){
        %>
           for (var k = 0; k < document.getElementsByName("txtRouteCode").length; k++) {
             if(document.getElementsByName("tag")[k].value == "1"){  
        <%	
              for(int i=0;i<soroute.length;i++){
            	  System.out.println("i="+i+",sort="+soroute[i].getSortroutecode()+",route="+ soroute[i].getWsroute());
        %>
                  //alert(document.getElementsByName("txtRouteCode")[0].value+","+"<%=soroute[i].getSortroutecode()%>");
                  //alert(document.getElementsByName("maskopt")[k].value+","+"<%=soroute[i].getMaskopt()%>"+","+document.getElementsByName("txtRouteCode")[k].value+","+"<%=soroute[i].getSortroutecode()%>"+","+document.getElementsByName("wsroute")[k].value+","+"<%=soroute[i].getWsroute()%>");
                  if((document.getElementsByName("maskopt")[k].value == "<%=soroute[i].getMaskopt()%>") &&
		             (document.getElementsByName("txtRouteCode")[k].value == "<%=soroute[i].getSortroutecode()%>" )){
		                  if(document.getElementsByName("wsroute")[k].value != "<%=soroute[i].getWsroute()%>"){
		                       alert("第 "+getLineByItem(document.getElementsByName("txtRouteCode")[k])+" 行 route code "+document.getElementsByName("txtRouteCode")[k].value+" 已存在對應 Route name ( "+ "<%=soroute[i].getWsroute()%>" + " ),　請修改 route code naming !!");
		                       returnvalue = false;
		                  }
         		  }
        <%  		         
              }
        %>
            }
           }
        <%   
        }
        %>
        return returnvalue;
    
}
function checkcheck(tmp){
    if(checkdata(tmp)) {
        if(checkConsist(tmp) && chkSortRouteCode(tmp)){
            if(window.confirm("確定要送出嗎？")){
                tmp.submit();
            }
        }
    }
}

function isEmpty(data){
    return ((data == null) || (data.length == 0 || data==' '));
}

function checkConsist(fm){
    var chkObj = fm.id;
    for (var i=0;i<chkObj.length-1;i++){
        if (fm.tag[i].value=='2') continue;
        for (var k=i+1;k<chkObj.length;k++){
        if (fm.tag[k].value=='2') continue;
	    if (fm.bodyversion[i].value==fm.bodyversion[k].value&&
	    	fm.maskopt[i].value==fm.maskopt[k].value&&
	    	fm.maskoptrev[i].value==fm.maskoptrev[k].value&&
	    	fm.codeno[i].value==fm.codeno[k].value&&
	    	fm.pincount[i].value==fm.pincount[k].value&&
	    	fm.pkgtype[i].value==fm.pkgtype[k].value //20090908, &&fm.ft_route_code[i].value==fm.ft_route_code[k].value
	    	) { 
	        if ((fm.route_cat[i].value==fm.route_cat[k].value) && (fm.route_cat[i].value!='NA')){
		    	var beginRow=i+1;
            	var endRowNum=k+1;
            	window.alert("同樣 key 值不應有相同的 Route Cat.，請 check 第"+getLineByItem(fm.route_cat[i])+"行與第"+getLineByItem(fm.route_cat[k])+"行！\n" +
            	"說明：同一Route Cat對應 route code不是唯一，請check是否使用 substitution route 建立，或與P/L PC討論！");
             	return false;
            }
	    }
//20081107, Robin if (fm.bodyversion[i].value==fm.bodyversion[k].value&&fm.maskopt[i].value==fm.maskopt[k].value&&fm.maskoptrev[i].value==fm.maskoptrev[k].value&&fm.codeno[i].value==fm.codeno[k].value&&fm.pincount[i].value==fm.pincount[k].value&&fm.pkgtype[i].value==fm.pkgtype[k].value&&fm.ft_route_code[i].value==fm.ft_route_code[k].value) {
//alert(fm.maskopt[i].value+","+fm.maskopt[k].value+","+fm.txtRouteCode[i].value+","+fm.txtRouteCode[k].value+","+fm.wsroute[i].value+","+fm.wsroute[k].value)
	    if (fm.maskopt[i].value==fm.maskopt[k].value) { // the same option
			if (fm.txtRouteCode[i].value==fm.txtRouteCode[k].value) { // the same option & route code
				//alert(fm.maskopt[i].value+","+fm.maskopt[k].value+","+fm.txtRouteCode[i].value+","+m.txtRouteCode[k].value+","+fm.wsroute[i].value+","+fm.wsroute[k].value);
				if (fm.wsroute[i].value!=fm.wsroute[k].value || fm.wsaddroute[i].value!=fm.wsaddroute[k].value || fm.wsaddroute1[i].value!=fm.wsaddroute1[k].value || fm.wsaddroute2[i].value!=fm.wsaddroute2[k].value || fm.wsaddroute3[i].value!=fm.wsaddroute3[k].value || fm.wsaddroute4[i].value!=fm.wsaddroute4[k].value){
		    		var beginRow2=i+1;
            		var endRowNum2=k+1;
            		window.alert("第"+getLineByItem(fm.route_cat[i])+"行與第"+getLineByItem(fm.route_cat[k])+"行的 WS Route/Additional Route 必須一致 (Route Code相同)");
            		return false;
          		}

		        if (fm.txtWsComment[i].value!=fm.txtWsComment[k].value){
			    	var beginRow=i+1;
        	    	var endRowNum=k+1;
            		window.alert("第"+getLineByItem(fm.route_cat[i])+"行與第"+getLineByItem(fm.route_cat[k])+"行的 WS Route Comment 必須一致 (Route Code相同)");
             		return false;
	            }
			}
        } // the same option
//20081112, Robin, if the same body & mask option & routes but diff route codes, you must have comments
	    if (fm.tag[k].value==1 || fm.tag[i].value==1) { // 新增資料才要比
	       	if ((fm.maskopt[i].value==fm.maskopt[k].value) && (fm.txtRouteCode[i].value!=fm.txtRouteCode[k].value) &&
				(fm.wsroute[i].value==fm.wsroute[k].value && fm.wsaddroute[i].value==fm.wsaddroute[k].value &&
				 fm.wsaddroute1[i].value==fm.wsaddroute1[k].value && fm.wsaddroute2[i].value==fm.wsaddroute2[k].value && fm.wsaddroute3[i].value==fm.wsaddroute3[k].value && fm.wsaddroute4[i].value==fm.wsaddroute4[k].value) &&
            	((fm.tag[k].value==1&&fm.txtWsComment[k].value == "")||(fm.tag[i].value==1&&fm.txtWsComment[i].value == ""))){
	    		var beginRow2=i+1;
				var endRowNum=k+1;
				var target=0;
			    if (fm.tag[k].value==1) target = endRowNum; else target = beginRow2;
				if (fm.txtWsComment[target-1].value == "") {
				  if (!isOldRule(fm,target-1)) { // 如果這個新 data 的 code+route 已存在上一版, 就不用輸 comment
	       			window.alert("第"+getLineByItem(fm.route_cat[i])+"行 vs 第"+getLineByItem(fm.route_cat[k])+"行：\n有相同的Mask Option+Routes，但Sort Route Code不同，因此第"+target+"行必需要有WS Comment！");
	    	   		return false;
				  }
				}
   			}
		}
        } // for k
    } // for i
    return true;
}

function isOldRule(fm,idx) {
    var chkObj = fm.id;
    for (var i=0;i<chkObj.length;i++){
		if((fm.tag[i].value != 1) &&
		   (fm.maskopt[i].value == fm.maskopt[idx].value) &&
		   (fm.txtRouteCode[i].value == fm.txtRouteCode[idx].value) &&
		   (fm.wsroute[i].value == fm.wsroute[idx].value) &&
		   (fm.wsaddroute[i].value == fm.wsaddroute[idx].value))
		return true;
    }
	return false;
}

function checkdata(fm){
    var chkObj = fm.id;
    if (chkObj.length > 0) {
      for (var i=0;i<chkObj.length;i++){
        if (isEmpty(fm.ftroute[i].value)){
            window.alert("第 "+getLineByItem(fm.ftroute[i])+"行 FT_ROUTE 未設定");
            return false;
        }

        if(isEmpty(fm.txtRouteCode[i].value)){
            window.alert("第 "+getLineByItem(fm.ftroute[i])+"行 SORT_ROUTE_CODE 未設定");
            return false;
        }

        if (fm.ftroute[i].value.indexOf("RP") == 0)
        if(fm.txtRouteCode[i].value == "NA"){
            window.alert("第 "+getLineByItem(fm.ftroute[i])+"行, FT ROUTE 為 RP* 之 SORT_ROUTE_CODE 不能為 NA");
            return false;
        }

        if(fm.txtRouteCode[i].value.length != 2){
            window.alert("第 "+getLineByItem(fm.ftroute[i])+"行 SORT_ROUTE_CODE 必需為兩碼");
            return false;
        }

        if(isEmpty(fm.wsroute[i].value)){
            window.alert("第 "+getLineByItem(fm.ftroute[i])+"行 WS_ROUTE 未設定");
            return false;
        }
      }
    } else {
        if (isEmpty(fm.ftroute.value)){
            window.alert("FT_ROUTE 未設定");
            return false;
        }

        if(isEmpty(fm.txtRouteCode.value)){
            window.alert("SORT_ROUTE_CODE 未設定");
            return false;
        }

        if (fm.ftroute.value.indexOf("RP") == 0)
        if(fm.txtRouteCode.value == "NA"){
            window.alert("FT ROUTE 為 RP* 之 SORT_ROUTE_CODE 不能為 NA");
            return false;
        }

        if(fm.txtRouteCode.value.length != 2){
            window.alert("SORT_ROUTE_CODE 必需為兩碼");
            return false;
        }

        if(isEmpty(fm.wsroute.value)){
            window.alert("WS_ROUTE 未設定");
            return false;
        }
    }
    return true;
}

function redirectDup(){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    //if (checkRadio5("raSel","要複製的資料")){
    if (checkCheckBoxNoLimit("raSel","要複製的資料")){
        window.location="<html:rewrite page="/OImaintain/duplicateRowActionX.do"/>?id="+tmp;
    }
}

function redirectExpired(tmpform){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    var tmp1=tmpform.sid.value;
    //if (checkRadio5("raSel","要 Expire 的資料")){
    if (checkCheckBoxNoLimit("raSel","要 Expire 的資料")){
        window.location="<html:rewrite page="/OImaintain/expireBomRouteActionX.do"/>?id="+tmp+"&flag=2&sid="+tmp1;
    }
}

function redirectUnExpired(tmpform){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    var tmp1=tmpform.sid.value;
    //if (checkRadio5("raSel","要 Reset Expire 的資料")){
    if (checkCheckBoxNoLimit("raSel","要 Reset Expire 的資料")){
        window.location="<html:rewrite page="/OImaintain/expireBomRouteActionX.do"/>?id="+tmp+"&flag=0&sid="+tmp1;
    }
}

function redirectDelete(tmpform){
    //var tmp=getRadioValue('raSel');
    var tmp=getCheckBox('raSel');
    var tmp1=tmpform.sid.value;
    //var tag = getValueByRadio('raSel', 'tag');
    var tag = getValueByCheckbox('raSel', 'tag');
    if (tag != '1') {
        alert("無法刪除由前一版帶入資料");
    	return false;
    }
    //if (checkRadio5("raSel","要刪除的資料")){
    if (checkCheckBoxNoLimit("raSel","要刪除的資料")){
        if(window.confirm("確定要刪除勾選資料嗎？")){
            window.location="<html:rewrite page="/OImaintain/deleteBomRouteTxActionX.do"/>?id="+tmp+"&sid="+tmp1;
        }
    }
}

function redirectBackToMain(tmp1){
    window.location="<html:rewrite page="/OImaintain/searchActionX.do"/>?sid="+tmp1;
}

function redirectReset(tmp){
    if(window.confirm("確定 Reset 資料嗎？")){
        var tmp1=tmp.sid.value;
        window.location="<html:rewrite page="/OImaintain/resetBomRouteActionX.do"/>?sid="+tmp1;
    }
}

function redirectToSubmit(tmp){
    if (checkdata(tmp))
	if (checkConsist(tmp) && chkSortRouteCode(tmp)){
        	if(window.confirm("確定要送出嗎？")){
            		tmp.flag.value = 'submit';
            		tmp.submit();
       	 	}
    	}
}

function setButton() {
    //var tag = getValueByRadio('raSel', 'tag');
    var tag = getValueByCheckbox('raSel', 'tag');
    var expbtn1 = document.getElementById('exp1');
    var unexpbtn1 = document.getElementById('rse1');
    var expbtn2 = document.getElementById('exp2');
    var unexpbtn2 = document.getElementById('rse2');
    var delbtn1 = document.getElementById('del1');
    var delbtn2 = document.getElementById('del2');
    var dupbtn1 = document.getElementById('dup1');
    var dupbtn2 = document.getElementById('dup2');

    var checknum = getCheckedNum('raSel');
    if (checknum > 1){
    	dupbtn1.disabled = true;
	    dupbtn2.disabled = true;
    }else{
    	dupbtn1.disabled = false;
	    dupbtn2.disabled = false;
    }
    
    if (tag == '0') {
    	expbtn1.disabled = false;
    	unexpbtn1.disabled = true;
    	expbtn2.disabled = false;
    	unexpbtn2.disabled = true;
    	delbtn1.disabled = true;
    	delbtn2.disabled = true;
    } else if (tag == '1') {
    	expbtn1.disabled = true;
    	unexpbtn1.disabled = true;
    	expbtn2.disabled = true;
    	unexpbtn2.disabled = true;
    	delbtn1.disabled = false;
    	delbtn2.disabled = false;
    } else if (tag == '2') {
    	expbtn1.disabled = true;
    	unexpbtn1.disabled = false;
    	expbtn2.disabled = true;
    	unexpbtn2.disabled = false;
    	delbtn1.disabled = true;
    	delbtn2.disabled = true;
    } else if (tag == '3') {
    	//alert("tag=3");
    	expbtn1.disabled = true;
    	unexpbtn1.disabled = true;
    	expbtn2.disabled = true;
    	unexpbtn2.disabled = true;
    	delbtn1.disabled = true;
    	delbtn2.disabled = true;
    }
}

function onFtRouteChange(fm,idx) {
	fm.route_cat[idx-1].selectedIndex=fm.ftroute[idx-1].selectedIndex;
}

/** WITS-20250905, 非正規/標準實作
//收納改變
function chgModFieldFT(fm){
	var chkObj = fm.id;
    if (document.getElementById("ftAddroute4_title").style.display==""){
		for (var i=0;i<chkObj.length;i++){
			document.getElementsByName("ftAddroute4_cont")[i].style.display="none";
			document.getElementsByName("ftAddroute5_cont")[i].style.display="none";
			document.getElementsByName("ftAddroute6_cont")[i].style.display="none";
			document.getElementsByName("ftAddroute4_title")[0].style.display="none";
			document.getElementsByName("ftAddroute5_title")[0].style.display="none";
			document.getElementsByName("ftAddroute6_title")[0].style.display="none";
		}
	}else{
		for (var i=0;i<chkObj.length;i++){
			document.getElementsByName("ftAddroute4_cont")[i].style.display="";
			document.getElementsByName("ftAddroute5_cont")[i].style.display="";
			document.getElementsByName("ftAddroute6_cont")[i].style.display="";
			document.getElementsByName("ftAddroute4_title")[0].style.display="";
			document.getElementsByName("ftAddroute5_title")[0].style.display="";
			document.getElementsByName("ftAddroute6_title")[0].style.display="";
		}	
	}	
    
}

//收納改變
function chgModFieldWS(fm){
	var chkObj = fm.id;
    if (document.getElementById("wsAddroute3_title").style.display==""){
		for (var i=0;i<chkObj.length;i++){
			document.getElementsByName("wsAddroute3_cont")[i].style.display="none";
			document.getElementsByName("wsAddroute4_cont")[i].style.display="none";
			document.getElementsByName("wsAddroute5_cont")[i].style.display="none";
			document.getElementsByName("wsAddroute3_title")[0].style.display="none";
			document.getElementsByName("wsAddroute4_title")[0].style.display="none";
			document.getElementsByName("wsAddroute5_title")[0].style.display="none";
		}
	}else{
		for (var i=0;i<chkObj.length;i++){
			document.getElementsByName("wsAddroute3_cont")[i].style.display="";
			document.getElementsByName("wsAddroute4_cont")[i].style.display="";
			document.getElementsByName("wsAddroute5_cont")[i].style.display="";
			document.getElementsByName("wsAddroute3_title")[0].style.display="";
			document.getElementsByName("wsAddroute4_title")[0].style.display="";
			document.getElementsByName("wsAddroute5_title")[0].style.display="";
		}	
	}	
    
}
**/


</script>
</head>

<body topmargin="0" leftmargin="0">
<%@  include file="../../index-menu2.jsp"%>
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
            <img src="../../image/arrow.gif" width="5" height="14" hspace="3" alt=""><font size="4">TIM</font>
          </td>
          <td noWrap height="25" width="85%" class="title4">
            <font size="4">TIM BOM VS Product Route - Normal Production Maintenance</font>
          </td>
        </tr>
        <tr>
          <td height="20" colspan="2"><hr width="100%" color=#B4761B size="1"></td>
        </tr>
      </table>

      <html:form action="/OImaintain/saveSubmitBomActionX.do">
        <input type="hidden" name="flag"/><!--flag將傳到SaveSubmitBomActionX.do用來判斷是要SAVE還是SUBMIT-->
        <input type="hidden" name="brand" value="<%=brand%>"/>
        <div id="myDIV1" align="center"  >
          <table width="95%" border="0" id="table28">
            <tr>
              <td>
                <% if (t.equals("Show") ) { %>
                <input type="button" id="save" name="save" value="Save" class = "button1" onclick="checkcheck(this.form);"/>
                <input type="button" id="del1" name="del1" value="Delete Row" class = "button1" onclick= "redirectDelete(this.form);"/>
                <input type="button" id="dup1" name="dup1"  value="Duplicate Row" class = "button1" onclick="redirectDup();"/>
                <input type="button" id="sub1" name="sub1" value="Submit" class = "button1" onclick="redirectToSubmit(this.form);"/>
                <input type="button" id="reset1" name="reset1" value="Reset" class = "button1" onclick="redirectReset(this.form);"/>
                <input type="button" id="exp1" name="exp1" value="set Expired" class = "button1" onclick="redirectExpired(this.form);"   />
                <input type="button" id="rse1" name="rse1" value="reset Expired" class = "button1" onclick="redirectUnExpired(this.form);"   />
                <!-- <input type="button" name="rse2" value="FT Add Route(4,5,6)" class = "button1" onclick="chgModFieldFT(this.form);" /> WITS-20250905-->
                <input type="button" id="rse2" name="rse2" value="FT Add Route(4,5,6)" class = "button1" onclick="toggleColumns('myTable1', 'hiddenCol456', 'ftAddroute4_title');"   />
                <!-- <input type="button" name="rse2" value="WS Add Route(3,4,5)" class = "button1" onclick="chgModFieldWS(this.form);" /> WITS-20250905-->
                <input type="button" name="rse2" value="WS Add Route(3,4,5)" class = "button1" onclick="toggleColumns('myTable1', 'hiddenCol345', 'wsAddroute3_title')" />
                <input type="button" id="reset1" name="reset1" value="Download" class = "button1" onClick="document.forms[document.forms.length - 1].submit();"/>
                <input type="button" id="selall1" name="selall1" value="Select All" class = "button1" onclick="SelectAllCheckBox(this.form);setButton();"/>
                <input type="button" id="cleall1" name="cleall1" value="Clear All" class = "button1" onclick="ClearAllCheckBox(this.form);setButton();"/>
		<%}%>
                <input type="button" name="bak1" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"/>
                <input type="button" name="bak1" value="維護注意事項" class = "button1" onclick="showModalDialog('bom_maintain_notice.jsp','Status:NO;dialogWidth:430px;dialogHeight:300px');"/>
              </td>
            </tr>
          </table>
          <!--Data Table-->
          <table id="table27" cellspacing=1 cellpadding=0 class=table2  >
            <tr class="list1">
              <td colspan="21" align="left"colspan="1"><font size = 2><B>Product :<input type="hidden" name="sid" value="<bean:write name="proTestRouteBeanAFX" property="sid"/>" />
              <bean:write name="proTestRouteBeanAFX" property="productbody"/> / Version <bean:write name="proTestRouteBeanAFX" property="version"/></B></font></td>
              <input type="hidden" name="productbody" value="<bean:write name="proTestRouteBeanAFX" property="productbody"/>" />
              <input type="hidden" name="chksortroutecode" value="0" />
            </tr>
<tr class=list1>
<td align=left colSpan=18>
<table id="myTable1" width="95%" border=0>
<thead>
            <tr class="title1" align="left">
              <td align="left" >  </td>
              <td align="left" >Line#</td>
              <td align="left" title="Status">St</td>
              <td align="left" >Body ver.</td>
              <td align="left" >Mask Opt.</td>
              <td align="left" >Mask Opt. rev.</td>
              <td align="left" >Code No</td>
              <td align="left" >Pin Count</td>
              <td align="left" >Pkg Code</td>
              <td align="left" >FT Route Code</td>
              <td align="left" >FT Route</td>
              <td align="left" >FT Add Route1</td>
              <td align="left" >FT Add Route2</td>
              <td align="left" >FT Add Route3</td>
              <td align="left" id="ftAddroute4_title" class="hiddenCol456" style="display:none;">FT Add Route4</td>
              <td align="left" id="ftAddroute5_title" class="hiddenCol456" style="display:none;">FT Add Route5</td>
              <td align="left" id="ftAddroute6_title" class="hiddenCol456" style="display:none;">FT Add Route6</td>
              <td align="left" >Route Cat.</td>
              <td align="left" >FT Comment</td>
              <td align="left" >Sort Route Code</td>
              <td align="left" >WS Route</td>
              <td align="left" >WS Add. Route1</td>
              <td align="left" >WS Add. Route2</td>
              <td align="left" id="wsAddroute3_title" class="hiddenCol345" style="display:none;">WS Add Route3</td>
              <td align="left" id="wsAddroute4_title" class="hiddenCol345" style="display:none;">WS Add Route4</td>
              <td align="left" id="wsAddroute5_title" class="hiddenCol345" style="display:none;">WS Add Route5</td>
              <td align="left" >WS Comment</td>
            </tr>
</thead>
          <tbody>
            <% int idx = 1; %>
            <logic:present name="BomProductRoute" >
            <logic:iterate id="result" name="BomProductRoute" >
            <input type="hidden" name="id" value="<bean:write name="result" property="id"/>"/>
	        <input type="hidden" name="tag" value="<bean:write name="result" property="tag"/>"/>
            <tr class="list1" >
              <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>"><input type="checkbox" name="raSel" value="<bean:write name="result" property="id"/>" onclick="setButton();"/></td>
              <td align="right" bgcolor="<bean:write name="result" property="changecolor"/>"><%=idx%></td>
 	          <logic:equal name="result" property="tag" value="2">
	            <td align="center" bgcolor="<bean:write name="result" property="changecolor"/>" title="Expired">E</td>
	          </logic:equal>
 	          <logic:notEqual name="result" property="tag" value="2">
	            <td align="center" bgcolor="<bean:write name="result" property="changecolor"/>" title="Processing">P</td>
	          </logic:notEqual>

                <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="bodyversion" value="<bean:write name="result" property="bodyversion"/>"/><bean:write name="result" property="bodyversion"/></td>
                <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="maskopt" value="<bean:write name="result" property="maskopt"/>"/><bean:write name="result" property="maskopt"/></td>
                <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>"><input type="hidden" name="maskoptrev" value="<bean:write name="result" property="maskoptrev"/>"/><bean:write name="result" property="maskoptrev"/></td>
                <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="codeno" value="<bean:write name="result" property="codeno"/>"/><bean:write name="result" property="codeno"/></td>
                <td align="right" bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="pincount" value="<bean:write name="result" property="pincount"/>"/><bean:write name="result" property="pincount"/></td>
                <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="pkgtype" value="<bean:write name="result" property="pkgtype"/>"/><bean:write name="result" property="pkgtype"/></td>
          
                <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="hidden" name="ft_route_code" size = 6 value="<bean:write name="result" property="ft_route_code"/>"/><bean:write name="result" property="ft_route_code"/></td>

              <td align="left"  bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="ftroute" size="1" onchange="onFtRouteChange(this.form,<%=idx++ %>);">
                  <option value="<bean:write name="result" property="ftroute"/>"><bean:write name="result" property="ftroute"/></option>
 	          <logic:equal name="result" property="tag" value="1">
                  <%=ftoption.toString()%>
 	          </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="ftAddroute" size="1">
                  <option value="<bean:write name="result" property="ftAddroute"/>"><bean:write name="result" property="ftAddroute"/></option>
                  <option> </option>
 	          <logic:equal name="result" property="tag" value="1">
                  <%=ftrouteaddoption.toString()%>
 	          </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="ftAddroute1" size="1">
                  <option value="<bean:write name="result" property="ftAddroute1"/>"><bean:write name="result" property="ftAddroute1"/></option>
                  <option> </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=ftrouteaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="ftAddroute2" size="1">
                  <option value="<bean:write name="result" property="ftAddroute2"/>"><bean:write name="result" property="ftAddroute2"/></option>
                  <option> </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=ftrouteaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>" class="ftAddroute4_cont hiddenCol456" style="display:none;">
                <select name="ftAddroute3" size="1">
                  <option value="<bean:write name="result" property="ftAddroute3"/>"><bean:write name="result" property="ftAddroute3"/></option>
                  <option> </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=ftrouteaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>" class="ftAddroute5_cont hiddenCol456" style="display:none;">
                <select name="ftAddroute4" size="1">
                  <option value="<bean:write name="result" property="ftAddroute4"/>"><bean:write name="result" property="ftAddroute4"/></option>
                  <option> </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=ftrouteaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>" class="ftAddroute6_cont hiddenCol456" style="display:none;">
                <select name="ftAddroute5" size="1">
                  <option value="<bean:write name="result" property="ftAddroute5"/>"><bean:write name="result" property="ftAddroute5"/></option>
                  <option> </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=ftrouteaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left" bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="route_cat" size="1" disabled>
                  <option value="<bean:write name="result" property="route_cat"/>"><bean:write name="result" property="route_cat"/></option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=routecatoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <input type="text" name="txtFtComment" value="<bean:write name="result" property="ftcomment"/>"/>
              </td>
 	          <logic:equal name="result" property="tag" value="1">
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="text" name="txtRouteCode" size = 6 value="<bean:write name="result" property="sortroutecode"/>"/></td>
 	          </logic:equal>
 	          <logic:notEqual name="result" property="tag" value="1">
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>" ><input type="text" name="txtRouteCode" size = 6 readonly = "true" value="<bean:write name="result" property="sortroutecode"/>"/></td>
 	          </logic:notEqual>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="wsroute" size="1">
                  <option value="<bean:write name="result" property="wsroute"/>"><bean:write name="result" property="wsroute"/></option>
 	          <logic:equal name="result" property="tag" value="1">
                  <%=option.toString()%>
 	          </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="wsaddroute" size="1">
                  <option value="<bean:write name="result" property="wsaddroute"/>"><bean:write name="result" property="wsaddroute"/></option>
                  <option>  </option>
 	          <logic:equal name="result" property="tag" value="1">
                  <%=routeaddoption.toString()%>
 	          </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <select name="wsaddroute1" size="1">
                  <option value="<bean:write name="result" property="wsaddroute1"/>"><bean:write name="result" property="wsaddroute1"/></option>
                  <option>  </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=routeaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>" class="wsAddroute3_cont hiddenCol345" style="display:none">
                <select name="wsaddroute2" size="1">
                  <option value="<bean:write name="result" property="wsaddroute2"/>"><bean:write name="result" property="wsaddroute2"/></option>
                  <option>  </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=routeaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>" class="wsAddroute4_cont hiddenCol345" style="display:none">
                <select name="wsaddroute3" size="1">
                  <option value="<bean:write name="result" property="wsaddroute3"/>"><bean:write name="result" property="wsaddroute3"/></option>
                  <option>  </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=routeaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>" class="wsAddroute5_cont hiddenCol345" style="display:none">
                <select name="wsaddroute4" size="1">
                  <option value="<bean:write name="result" property="wsaddroute4"/>"><bean:write name="result" property="wsaddroute4"/></option>
                  <option>  </option>
                   <logic:equal name="result" property="tag" value="1">
                  <%=routeaddoption.toString()%>
                   </logic:equal>
                </select>
              </td>
              <td align="left"   bgcolor="<bean:write name="result" property="changecolor"/>">
                <input type="text" name="txtWsComment" value="<bean:write name="result" property="wscomment"/>"/>
              </td>
            </tr>
            </logic:iterate>
            </logic:present>
</table></TD></TR> 
          </tbody>
          </table>
          <!--下面的按鈕-->
          <table width="95%" border="0" id="table28">
            <tr>
              <td>
                <% if (t.equals("Show") ) { %>
                  <input type="button" id="save" name="save" value="Save" class = "button1" onclick="checkcheck(this.form);" />
                  <input type="button" id="del2" name="del2" value="Delete Row" class = "button1" onclick= "redirectDelete(this.form);" />
                  <input type="button" id="dup2" name="dup2" value="Duplicate Row" class = "button1" onclick="redirectDup();"  />
                  <input type="button" id="sub2" name="sub2" value="Submit" class = "button1" onclick="redirectToSubmit(this.form);"/>
                  <input type="button" id="res2" name="res2" value="Reset" class = "button1" onclick="redirectReset(this.form);"   />
                  <input type="button" id="exp2" name="exp2" value="set Expired" class = "button1" onclick="redirectExpired(this.form);"   />
                  <input type="button" name="rse2" value="reset Expired" class = "button1" onclick="redirectUnExpired(this.form);"   />
                  <!-- <input type="button" name="rse2" value="FT Add Route(4,5,6)" class = "button1" onclick="chgModFieldFT(this.form);" /> WITS-20250905-->
                  <input type="button" name="rse2" value="FT Add Route(4,5,6)" class = "button1" onclick="toggleColumns('myTable1', 'hiddenCol456', 'ftAddroute4_title')" />
                  <!-- <input type="button" name="rse2" value="WS Add Route(3,4,5)" class = "button1" onclick="chgModFieldWS(this.form);" /> WITS-20250905-->
                  <input type="button" name="rse2" value="WS Add Route(3,4,5)" class = "button1" onclick="toggleColumns('myTable1', 'hiddenCol345', 'wsAddroute3_title')" />
                  <input type="button" id="selall2" name="selall2" value="Select All" class = "button1" onclick="SelectAllCheckBox(this.form);setButton();"/>
                  <input type="button" id="cleall2" name="cleall2" value="Clear All" class = "button1" onclick="ClearAllCheckBox(this.form);setButton();"/>
                <%}%>
               <input type="button" name="reset1" value="Download" class = "button1" onClick="document.forms[document.forms.length - 1].submit();"/>
               <input type="button" name="bak2" value="回維護主畫面" class = "button1" onclick="redirectBackToMain('<bean:write name="proTestRouteBeanAFX" property="sid"/>');"/>
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
<%@ include file="../../index-down.jsp"%>

<script type="text/javascript">
if ('<%=entry%>' == 'true')
	showModalDialog('bom_maintain_notice.jsp','Status:NO;dialogWidth:430px;dialogHeight:300px');
</script>

</body>
<html:form action="/common/e8049Conv2Excel.do" target="_self">
  <html:hidden property="reportResult" value=""/>
  <input type="hidden" name="sid" value="<bean:write name="proTestRouteBeanAFX" property="sid"/>" />
  <input type="hidden" name="type" value="BOM_XROM_TX" />
</html:form>
</html:html>

<script type="text/javascript">

/** WITS-20250905, 檢查元素是否有特定 className */
function hasClass(element, className) {
 var elementClass = element.className || "";
 // 檢查元素的 className 是否包含指定的 className
 // 必須是完整單字 (前後有空格或字串開頭/結尾)
 return (" " + elementClass + " ").indexOf(" " + className + " ") > -1;
}

/** WITS-20250905, 輔助函式：切換元素集合的顯示狀態  */
function toggleElementsDisplay(elements, displayValue) {
 for (var i = 0; i < elements.length; i++) {
     elements[i].style.display = displayValue;
 }
}

/** WITS-20250905, 自訂函式：在指定表格中尋找具有特定 className 的元素  */
function getElementsWithClassInTable(tableId, className) {
    var result = [];
    var table = document.getElementById(tableId);
    if (!table) return result;
    
    // 只在表格內搜尋 th 元素
    var thElements = table.getElementsByTagName("th");
    // 只在表格內搜尋 td 元素
    var tdElements = table.getElementsByTagName("td");
    
    // 檢查 th 元素
    for (var i = 0; i < thElements.length; i++) {
        if (hasClass(thElements[i], className)) {
            result.push(thElements[i]);
        }
    }
    
    // 檢查 td 元素
    for (var j = 0; j < tdElements.length; j++) {
        if (hasClass(tdElements[j], className)) {
            result.push(tdElements[j]);
        }
    }
    
    return result;
}

/**
 * 可重複利用的顯示/隱藏 columns 函式.
 * tableId: 目標 table 的 id
 * className: 目標隱藏/顯示的欄位 class 名稱
 * targetId: 用來判斷目前隱藏/顯示狀態的目標元素 id (通常是該欄位的 th)
 * 
 * p.s. document.getElementById 在 Chrome 失效,可能 HTML 有問題
 */
function toggleColumns(tableId, className, targetId) {
    var targetDom = document.getElementById(targetId);
    if (!targetDom) return; // 檢查 target dom 是否存在

    var isVisible = !(targetDom.style.display == "none");
    var cells = getElementsWithClassInTable(tableId, className);
    var displayValue = isVisible ? "none" : "";
    toggleElementsDisplay(cells, displayValue);
}

var st1 = new SortableTable(document.getElementById("myTable1"),
 ["None","Number","String","String","String",
  "String","String","Number","String","String",
  "Select","Select","Select","Select","Select",
  "Text","Text","Select","Select","Select",
  "Text","Text","Text","Text","Text","Text","Text"]);
// IE does not remember input values when moving DOM elements
if (/MSIE/.test(navigator.userAgent)) {

	// backup check box values
	st1.onbeforesort = function () {
		var table = st1.element;
		var inputs = table.getElementsByTagName("INPUT");
		var l = inputs.length;
		for (var i = 0; i < l; i++) {
			if(inputs[i].type=="radio"){
				inputs[i].parentNode.parentNode._checked = inputs[i].checked;
			}
		}
	};

	// restore check box values
	st1.onsort = function () {
		var table = st1.element;
		var inputs = table.getElementsByTagName("INPUT");
		var l = inputs.length;
		for (var i = 0; i < l; i++) {
			if(inputs[i].type=="radio"){
				inputs[i].checked = inputs[i].parentNode.parentNode._checked;
			}
		}
	};

	var props = {
		    filters_row_index: 1,
		    loader: true,
		    loader_html: '<img src="<html:rewrite page="/image/loader.gif"/>" alt="" style="margin: 0pt 5px; vertical-align: middle;"><span>Loading...</span>',
		    status_bar: false,
//		        col_0: "none",
		    enter_key: true
		  };

		      setFilterGrid("myTable1",props);
}
</script>
