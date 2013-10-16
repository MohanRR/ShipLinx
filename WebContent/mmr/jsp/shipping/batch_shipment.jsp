<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html> 
<head> 
    <title><s:text name="batch.shipment.title"/></title> 
    <sj:head jqueryui="true" />
    <sx:head />
</head> 
<body> 
<SCRIPT language="JavaScript">
	function upload() {
//		alert('Uploading Batch Shipment File...');
		document.uploadBatchForm.action = "batch.shipment.upload.file.action";
		document.uploadBatchForm.submit();
	}
	function createShipments() {
		document.uploadBatchForm.action = "batch.create.shipments.action";
		document.uploadBatchForm.submit();
	}	
	function processShipments() {
		document.uploadBatchForm.action = "batch.process.shipments.action";
		document.uploadBatchForm.submit();
	}
    function showState() {
		ajax_Service=ajaxFunction();
		ajax_Service.onreadystatechange=function()
		  {
			   if(ajax_Service.readyState==4)
				{
				reponse=ajax_Service.responseText;
				js_stateid=document.getElementById("stateid");
				js_stateid.innerHTML= reponse;
				}
		  }
		  firstBox = document.getElementById('firstBox');
		  url="<%=request.getContextPath()%>/markup.listService.action?value="+firstBox.value;
			//param="objName=ref_state&country_id="+country_id;
		  	ajax_Service.open("GET",url,true);
		  	ajax_Service.send(this);
	} // End function showState()	
</SCRIPT>

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="batch.shipment.upload.file" method="post" enctype="multipart/form-data" name="uploadBatchForm">
	<s:token/>
	<div id="batchupld_panel">
		<table>
		<tr>
		<td><div id="srch_crtra"><mmr:message messageId="menu.admin.batchshipment"/></div></td>
		<td>&nbsp;</td>
		</tr>
		</table>
	</div>

<div id="batchupld_srchactions_imgs">
<table>
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/upload_file.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/process_now.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="batchupld_srch_actions">
<table>
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

<td>
	<a href="javascript: upload()"><mmr:message messageId="label.upload.file"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>

<td>
	<a href="javascript: createShipments()"><mmr:message messageId="label.create.shipments"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>

<td>
<a href="javascript: processShipments()"><mmr:message messageId="label.process.shipments"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>

</tr>
</table>
</div>

<div id="batchupld_srch_table">
<table width="400px" border="0" cellpadding="4" cellspacing="0"  style="margin-top:2px; margin-left:50px;">
				<tr>
					<td class="edi_text_03" width="250px"><s:label key="Batch Shipments File"/>:</td>
					<td class="srchshipmnt_text_03"><strong><mmr:message messageId="label.track.carrier"/>:</strong></td>
					<td class="srchshipmnt_text_03"><strong><mmr:message messageId="label.markup.service"/>:</strong></td>
					
				</tr>	
				<tr>
					<td width="350px" align="left"> 
						<s:file name="upload" label="File" key="upload"  theme="simple" cssClass="text_01_select_file"/>
					</td>
					<td class="text_01">
						<s:select cssClass="text_01_combo_big" cssStyle="width:135px;" listKey="id" listValue="name" 
							name="batchShipmentInfo.carrierId" headerKey="" headerValue=""  list="#session.CARRIERS" 
								onchange="javascript:showState();"  id="firstBox" theme="simple"/>
					</td>							
					<td class="text_01" id="stateid">
						<s:select cssClass="text_01_combo_big" cssStyle="width:120px;" listKey="id" listValue="name"
							name="batchShipmentInfo.serviceId" list="#session.SERVICES" 
								headerKey="-1" id="secondBox" theme="simple"/>													
					</td>					
					
				
				</tr>				
									
			</table>
</div>
<div id="batchupld_upload_icon"></div>	
  
<div id="formResult">
	<s:include value="list_shipment.jsp"></s:include>
</div>

</s:form>
</div>
</div>
