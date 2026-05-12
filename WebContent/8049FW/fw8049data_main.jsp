<!-- 8049FW/fw8049data_main.jsp -->

<%@page contentType="text/html; charset=Big5"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html:html>
<head>
<c:set var="actFrm" value="${Fw8049MaintainenceActionForm}" />
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<title>8040FW</title>
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/css.css"/>'>
<link rel="stylesheet" type="text/css"
	href='<html:rewrite page="/css/filtergrid.css"/>'>
<script>
	var dirRoot = '<c:out value="${pageContext.request.contextPath}" />';
</script>
<script language="javascript" type="text/javascript"
	src='<html:rewrite page="/js/util.js"/>'></script>
<script language="javascript" type="text/javascript"
	src='<html:rewrite page="/js/prototype.js"/>'></script>
<script language="javascript" type="text/javascript"
	src='<html:rewrite page="/js/openajax.js"/>'></script>
<script language="javascript" type="text/javascript"
	src='<html:rewrite page="/js/areaSelect.js"/>'></script>
<script language="javascript" type="text/javascript"
	src='<html:rewrite page="/js/topmenu.js"/>'></script>
<script language="javascript" type="text/javascript"
	src='<html:rewrite page="/js/tablefilter-2.js"/>'></script>
<script language="javascript" type="text/javascript"
	src='<html:rewrite page="/js/sortabletable.js"/>'></script>
<script type="text/javascript">
	// 	function addRouteStep(){
	// 	    var prdBody = document.forms[0].product_body.value;
	// 		var url='<html:rewrite page="/oi8040/common/routeStepNameList.jsp?field=route_name&type=CP&product_body="/>' + prdBody;
	// 		//WITS-20250721 Mark var rtn=window.showModalDialog(url, "", "dialogHeight:420px;dialogWidth:400px;help:no;status:no");
	// 	    //WITS-20250721 Add Start
	// 		window.useModalDialog(url, "", "dialogHeight:420px;dialogWidth:400px;help:no;status:no").then(function(rtn){
	// 			//MXIC Original Start
	// 			if(rtn){
	// 		    	var stepName = document.getElementsByName('step_name');
	// 		    	for(i=0; i<stepName.length; i++){
	// 			    	if(rtn.indexOf(stepName[i].value) > 0){
	// 				    	alert("已有此 Step Name [" + stepName[i].value + "], 無法新增! ");
	// 				    	return false;
	// 			    	}
	// 		    	}
	// 		    	var result=rtn.split("===");
	// 		    	var step = result[0].split(",");
	// 		    	for(i=0; i<step.length; i++){		    	
	// 			        addStepRow("1", "", step[i], "", "", "");
	// 		    	}		    	
	// 		    } else {
	// 		      	return false;
	// 		    }
	// 			//MXIC Original End
	// 		})['catch'](promiseErrHandler);
	// 		//WITS-20250721 Add End
	// 	}

	// 	function addStep(){
	// 	    var prdBody = document.forms[0].product_body.value;
	// 		var url='<html:rewrite page="/oi8040/common/stepNameList.jsp?field=step_name&type=CP&product_body="/>' + prdBody;
	// 		//WITS-20250721 Mark var rtn=window.showModalDialog(url, "", "dialogHeight:420px;dialogWidth:400px;help:no;status:no");
	// 	    //WITS-20250721 Add Start
	// 		window.useModalDialog(url, "", "dialogHeight:420px;dialogWidth:400px;help:no;status:no").then(function(rtn){
	// 			//MXIC Original Start
	// 			if(rtn){
	// 		    	var stepName = document.getElementsByName('step_name');
	// 		    	for(i=0; i<stepName.length; i++){
	// 			    	if(stepName[i].value == rtn){
	// 				    	alert("已有此 Step Name [" + rtn + "], 無法新增! ");
	// 				    	return false;
	// 			    	}
	// 		    	}
	// 		        addStepRow("1", "", rtn, "", "", "");
	// 		    } else {
	// 		      	return false;
	// 		    }
	// 			//MXIC Original End
	// 		})['catch'](promiseErrHandler);
	// 		//WITS-20250721 Add End
	// 	}

	function checkData() {
		var reasonDetailsList = document.getElementsByName("reason");
		var annotationIssuesList = document.getElementsByName("content");
		var pendingList = document.getElementsByName("pending");
		var rowIndexList = document.getElementsByName("row_Index");

		for (var i = 0; i < reasonDetailsList.length; i++) {

			var rowIndex = rowIndexList[i].value;

			var reasonDetails = reasonDetailsList[i].value.trim();
			var annotationIssues = annotationIssuesList[i].value.trim();
			var pending = pendingList[i].value.trim();

			if (reasonDetails === "") {
				alert("第 " + rowIndex + " 列，變更原因不可空白");
				reasonDetailsList[i].focus();
				return false;
			}

			if (annotationIssues === "") {
				alert("第 " + rowIndex + " 列，變更內容(註解欄)不可空白");
				annotationIssuesList[i].focus();
				return false;
			}

			if (pending === "") {
				alert("第 " + rowIndex + " 列，待解決事項不可空白");
				pendingList[i].focus();
				return false;
			}
		}

		return true;
	}

	function doAction(action) {
		if (action == 'Reset') {
			if (window.confirm("確定要 " + action + " 嗎？")) {
				document.forms[0].act.value = 'doAction';
				document.forms[0].actionType.value = action;
				document.forms[0].submit();
			}
			return false;
		}
		if (checkData()) {
			if (window.confirm("確定要 " + action + " 嗎？")) {
				document.forms[0].act.value = action.toLowerCase();
				document.forms[0].submit();
			}
		}
	}

	// 	function deleteFlow(action){
	//         if(window.confirm("確定要 " + action + " 嗎？")){
	//         	document.forms[0].act.value = 'deleteGroupingFlow';	
	//         	document.forms[0].submit();
	//     	}
	// 	}

	// 	function getTestFlowVersion(rowIndex, stepName){
	// 	    var prdBody = document.forms[0].product_body.value;
	// 		var url='<html:rewrite page="/oi8040/common/testFlowList.jsp?field=&product_body="/>' + prdBody + '&step_name=' + stepName;
	// 		//alert(url);
	// 		//WITS-20250721 Mark var rtn=window.showModalDialog(url, "", "dialogHeight:480px;dialogWidth:800px;help:no;status:no");
	// 		//WITS-20250721 Add Start
	// 		window.useModalDialog(url, "", "dialogHeight:480px;dialogWidth:800px;help:no;status:no").then(function(rtn){
	// 			//MXIC Original Start
	// 			//var rtn=window.open(url, 'miniwin','scrollbars=1,toolbar=0,location=0,width=800;,height=620');
	// 		    if(rtn){
	// 		    	var result=rtn.split("===");
	// 		    	var flow = result[0].split(",");
	// 				//document.getElementsByName('product_code')[rowIndex-2].value = flow[0];
	// 				//document.getElementsByName('test_flow_version')[rowIndex-2].value = flow[1];
	// 				//document.getElementsByName('test_flow_status')[rowIndex-2].value = flow[2];
	// 				document.getElementsByName('product_code')[rowIndex-2].value = "檔案驗證中...";
	// 				document.getElementsByName('test_flow_version')[rowIndex-2].value = "";
	// 				document.getElementsByName('test_flow_status')[rowIndex-2].value = "";
	// 				checkTestFlowFile(rowIndex, stepName, flow[0], flow[1], flow[2], flow[3]);

	// 		    } else {
	// 		      	return false;
	// 		    }
	// 			//MXIC Original End
	// 		})['catch'](promiseErrHandler);
	// 		//WITS-20250721 Add End
	// 	}

	// 	function productBodyMaintain(){
	// 		document.forms[0].act.value = 'oiMaintainProductMain';
	// 		document.forms[0].submit();
	// 	}
</script>
</head>

<body topmargin="0" leftmargin="0">
	<%@  include file="../index-menu.jsp"%>
	<table width="100%" border=0 class="bg1">
		<tr>
			<td valign="top">

				<table width="100%" border="0" cellspacing="0" cellpadding="0"
					class="bg1" align="center">
					<tr>
						<td width="100%" height="490" valign="top"><br>
							<table width="95%" border="0" cellspacing="0" cellpadding="0"
								align="center">
								<tr>
									<td width="15%" height="25" class="title2"><img
										src="../image/arrow.gif" width="5" height="14" hspace="3"
										alt=""><font size="4">8049FW</font></td>
									<td noWrap height="25" width="85%" class="title4"><font
										size="4"></font></td>
								</tr>
								<tr>
									<td height="20" colspan="2"><hr width="100%" color=#B4761B
											size="1"></td>
								</tr>
							</table> <html:form action="/8049fw/Fw8049MaintainenceAction.do" method="post">
								<input type="hidden" name="act" id="act" value="getList" />
								<input type="hidden" name="actionType" id="actionType" value="" />
								<html:hidden property="sid" styleId="sid" />
								<html:hidden property="product_body" styleId="product_body" />
								<html:hidden property="status" styleId="status" />
								<div id="myDIV1" align="center" style="border: 0; height: 310px">
									<table width="95%" border="0" id="table28">
										<tr>
											<td><input type="button" value="Save" class="button1"
												onclick="doAction(this.value);" /> <input type="button"
												value="Submit" class="button1"
												onclick="doAction(this.value);" /> <input type="button"
												value="Delete" class="button1"
												onclick="deleteFlow(this.value);" /> <input type="button"
												name="bak1" value="回維護主畫面" class="button1"
												onclick="productBodyMaintain();" /> <input type="button"
												value="Reset" class="button1"
												onclick="doAction(this.value);" /> <input type="button"
												value="Set Expired" class="button1" onclick="" /> <input
												type="button" value="Reset Expired" class="button1"
												onclick="" /></td>
										</tr>
									</table>
									<!--Data Table-->
									<table cellspacing="1" cellpadding="0" class="table2">
									</table>
									<table id="flowTable" cellspacing="1" cellpadding="0"
										class="table2">
										<thead>
											<tr class="list1">
												<td align="left" colspan="2"><font size="2"><b>Product:
															<c:out value="${actFrm.product_body}" /> / <c:if
																test="${actFrm.brand ne 'NA'}">
																<c:out value="${actFrm.brand}" />
															</c:if> / Version <c:out value="${actFrm.version}" />
													</b></font></td>
												<td align="left" colspan="6"><font size="2"><b></b></font></td>
											</tr>
											<tr class="title1" align="left">
												<td align="left" width="40">#</td>
												<td align="left" width="120">St</td>
												<td align="left" width="120">Product Code</td>
												<td align="left" width="120">Form No</td>
												<td align="left" width="100">變更原因</td>
												<td align="left" width="120">變更內容(註解欄)</td>
												<td align="left" width="120">待解決事項</td>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
									<!--下面的按鈕-->
									<table width="95%" border="0" id="table28">
										<tr>
											<td><input type="button" value="Save" class="button1"
												onclick="doAction(this.value);" /> <input type="button"
												value="Submit" class="button1"
												onclick="doAction(this.value);" /> <input type="button"
												value="Delete" class="button1"
												onclick="deleteFlow(this.value);" /> <input type="button"
												name="bak1" value="回維護主畫面" class="button1"
												onclick="productBodyMaintain();" /> <input type="button"
												value="Reset" class="button1"
												onclick="doAction(this.value);" /> <input type="button"
												value="Set Expired" class="button1" onclick="" /> <input
												type="button" value="Reset Expired" class="button1"
												onclick="" /></td>
										</tr>
									</table>
								</div>
								<br>
							</html:form></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<script type="text/javascript">
		//20110531var st1 = new SortableTable(document.getElementById("table27"));
	</script>
	<%@  include file="../index-down.jsp"%>
</body>

<script type="text/javascript">
	/* 
	 var props = {
	 filters_row_index: 1,
	 loader: true,
	 loader_html: '<img src="<html:rewrite page="/image/loader.gif"/>" alt="" style="margin: 0pt 5px; vertical-align: middle;"><span>Loading...</span>',
	 status_bar: false,
	 //        col_0: "none",
	 enter_key: true
	 };

	 setFilterGrid("flowTable",props);
	 */

 	var dataMap = {};
	<c:if test="${not empty dataMap}">
		dataMap = {
			<c:forEach var = "entry" items="${dataMap}" varStatus="statuts">
				"${entry.key}":{
					"reason" : "${entry.value.reason}",
					"content" : "${entry.value.content}",
					"pending" : "${entry.value.pending}"
				}<c:if test="${!status.last}">,</c:if>
			</c:forEach>
		};
	</c:if>
	
	function getFwValue(key, name){
		if(!dataMap){
			return "";
		}
		if(!dataMap[key]){
			return "";
		}
		if(!dataMap[key][name]){
			return "";
		}
		return dataMap[key][name];
	}
	function addStepRow(rowIndex, nclFormNo, prodBody, formNo, reasonDetails, annotationIssues, pending) {
		 
		var bgcolor = "";
		if (nvl(formNo) != "") {
			bgcolor = "#CCEEFF";
		} else {
			bgcolor = "#FFDDFF";
		}

		var styleValue = "height:20px; padding-top:1px; padding-right:2px; padding-bottom:1px; padding-left:1px;";
		styleValue += "font-family:'Verdana', 'Arial', 'Helvetica', 'sans-serif';";
		styleValue = "font-size:12px; background-color: " + bgcolor
				+ "; border:none; text-align:center;";

		var table = document.getElementById("flowTable");
		var tr = table.insertRow(-1);
		tr.className = "list1";

		var td1 = tr.insertCell(-1);
		td1.bgColor = bgcolor;
		td1.align = "left";
		td1.innerHTML += "<input type='radio' name='hashtag'/>";
		td1.innerHTML += "<input type='hidden' name='row_Index' value='"+rowIndex+"'/>";

		var td2 = tr.insertCell(-1);
		td2.bgColor = bgcolor;
		td2.align = "left";
		td2.innerHTML = "<input type='text' name='productCode' value='st' style='" + styleValue + "' readOnly/>";
		
		var td3 = tr.insertCell(-1);
		td3.bgColor = bgcolor;
		td3.align = "left";
		td3.innerHTML = "<input type='text' name='productCode' value='" + prodBody + "' style='" + styleValue + "' readOnly/>";

		var td4 = tr.insertCell(-1);
		td4.bgColor = bgcolor;
		td4.align = "left";
		td4.innerHTML = "<input type='text' name='formNo' value='" + nclFormNo + "' style='" + styleValue + "' readOnly/>";

		var td5 = tr.insertCell(-1);
		td5.bgColor = bgcolor;
		td5.align = "left";

		if (nvl(formNo) != "") {
// 			td5.innerHTML = "<input type='text' name='reason' value='" + reasonDetails + "' style='" + styleValue + "' readOnly/>";
			td5.innerHTML = "<input type='text' name='reason' value='" + reasonDetails + "' style='" + styleValue + "' readOnly/>";
		} else {
			td5.innerHTML = "<input type='text' name='reason' value='" + getFwValue(nclFormNo, "reason")  + "' />";
		}

		var td6 = tr.insertCell(-1);
		td6.bgColor = bgcolor;
		td6.align = "left";

		if (nvl(formNo) != "") {
// 			td5.innerHTML = "<input type='text' name='content' value='" + annotationIssues + "' style='" + styleValue + "' readOnly/>";
			td6.innerHTML = "<input type='text' name='content' value='" + annotationIssues + "' style='" + styleValue + "' readOnly/>";
		} else {
			td6.innerHTML = "<input type='text' name='content' value='" + getFwValue(nclFormNo, "content")  + "' />";
		}

		var td7 = tr.insertCell(-1);
		td7.bgColor = bgcolor;
		td7.align = "left";
		if (nvl(formNo) != "") {
// 			td7.innerHTML = "<input type='text' name='pending' value='' readOnly/>";
			td7.innerHTML = "<input type='text' name='pending' value='" + pending + "' style='" + styleValue + "' readOnly/>";
		} else {
			td7.innerHTML = "<input type='text' name='pending' value='"+ getFwValue(nclFormNo, "pending")  + "'/>";
		}
	}

	function nvl(value) {
		if (value == null || value == "null" || value == "undefined" || value == "") {
			return "";
		}
		return true;
	}

	<c:forEach items="${resultList}" var="item" varStatus="st">
	console.log("annotation_issues:",
			'<c:out value="${item.content}"/>');
	addStepRow('${st.index + 1}', '<c:out value="${item.ncl_form_no}"/>',
			'<c:out value="${item.prod_body}"/>',
			'<c:out value="${item.formno}"/>',
			'<c:out value="${item.reason}"/>',
			'<c:out value="${item.content}"/>',
			'<c:out value="${item.pending}"/>');
	</c:forEach>

	function handlerFunc(t) {
		var obj = t.transport.responseText.evalJSON();
		var rowIndex = obj.data[0].rowIndex;
		//alert(obj.data[0].check_result);

		if (obj.data[0].check_result == "ok") {
			document.getElementsByName('product_code')[rowIndex - 2].value = obj.data[0].product_code;
			document.getElementsByName('test_flow_version')[rowIndex - 2].value = obj.data[0].test_flow_version;
			document.getElementsByName('test_flow_status')[rowIndex - 2].value = decodeURI(decodeURI(obj.data[0].test_flow_status));
		} else {
			document.getElementsByName('product_code')[rowIndex - 2].value = "";
			//alert(obj.data[0].error);
			alert("TestFlow File [" + obj.data[0].test_flow_file
					+ "] 內容格式有誤! error: " + obj.data[0].error);
		}
	}

	function errFunc() {
		alert("checkTestFlowFile() ajax error!!");
	}

	function checkTestFlowFile(rowIndex, stepName, prodCode, tfVer, tfStatus,
			tfFile) {
		var post = "act=checkTestFlowFile";
		post += "&rowIndex=" + rowIndex;
		post += "&stepName=" + stepName;
		post += "&product_code=" + prodCode;
		post += "&test_flow_version=" + tfVer;
		post += "&test_flow_status=" + tfStatus;
		post += "&test_flow_file=" + tfFile;

		//alert(post);
		new Ajax.Request(dirRoot + '/oi8040/TgCPFlowAction.do', {
			parameters : encodeURI(encodeURI(post)),
			onSuccess : handlerFunc,
			onFailure : errFunc
		});
	}
</script>
</html:html>
