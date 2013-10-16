<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<html> 
<head> 
    <title><s:text name="edi.title"/></title> 
</head> 
<body> 
<SCRIPT language="JavaScript">
	function processNow() {
		alert('Processing File Now...');
		document.uploadEdiform.action = "uploadAndProcess.action";
		document.uploadEdiform.submit();
	}
	function processLater() {
		document.uploadEdiform.action = "uploadAndProcessLater.action";
		document.uploadEdiform.submit();
	}	
</SCRIPT>

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="uploadedi" method="post" enctype="multipart/form-data" name="uploadEdiform">
	<s:token/>
	<div id="ediupld_panel">
		<table>
		<tr>
		<td><div id="srch_crtra"><mmr:message messageId="menu.admin.uploadedi"/></div></td>
		<td>&nbsp;</td>
		</tr>
		</table>
	</div>

<div id="ediupld_srchactions_imgs">
<table>
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/process_now.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="ediupld_srch_actions">
<table>
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>

<td>
	<a href="javascript: processLater()" onclick="return false"><mmr:message messageId="label.edi.process.later"/></a>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</td>

<td><a href="javascript: processNow()"><mmr:message messageId="label.edi.process.now"/></a>&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>

<div id="ediupld_srch_table">
<table width="400px" border="0" cellpadding="4" cellspacing="0"  style="margin-top:2px; margin-left:50px;">
				<tr>
					<td class="edi_text_03" width="250px"><strong><mmr:message messageId="label.track.carrier"/></strong></td>
					<td class="edi_text_03" width="250px"><s:label key="File"/>:</td>
				</tr>	
				<tr>
					<td class="edi_text_03" width="250px">
						<s:select cssClass="text_01_combo_big" cssStyle="width:155px;" listKey="id" 
							listValue="name" name="ediInfo.carrierId" list="#session.CARRIERS" 
							 headerKey="-1" id="carrier" theme="simple"/>							
					</td>									
					<td width="350px" align="left"> 
						<s:file name="upload" label="File" key="upload"  theme="simple" cssClass="text_01_select_file"/>
					</td>
				</tr>				
									
			</table>
</div>
<div id="ediupld_upload_icon"></div>	

</s:form>
</div>
</div>
