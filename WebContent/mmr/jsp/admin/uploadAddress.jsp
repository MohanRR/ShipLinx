<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<html> 
<head> 
    <title><s:text name="edi.title"/></title> 
</head> 
<body> 
<SCRIPT language="JavaScript">
	function processNow() 
	{	
		if(document.getElementById("erase_add_cb_id").checked)
		{
			document.uploadAddform.action= "uploadaddressbook.action?delete=true";
			document.uploadAddform.submit();
		}
		else{
			document.uploadAddform.action= "uploadaddressbook.action";
			document.uploadAddform.submit();
		}
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
<s:form action="uploadaddressbook" method="post" enctype="multipart/form-data" name="uploadAddform">
	<s:token/>
	
	<div id="addupld_panel">
		<table>
		<tr>
		<td><div id="srch_crtra"><mmr:message messageId="menu.upload.addressbook"/></div></td>
		<td>&nbsp;</td>
		</tr>
		</table>
	</div>


<div id="addupld_srch_table">
<table width="800px" border="0" cellpadding="4" cellspacing="0"  style="margin-top:2px; margin-left:50px;">
		<tr>
			<td colspan="2" valign="middle" id="srch_crtra">
				<mmr:message messageId="line.upload.addressbook"/>&nbsp;&nbsp;&nbsp;				
			</td>
			<td align="left">
			<s:a href="address_book_template.zip" cssStyle="text-decoration: none;"><img src="<s:url value="/mmr/images/Downloads.png" includeContext="true" />" border="0"/></s:a>			
			</td>
			<td width="50px">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
				<tr>
					<td class="edi_text_03" width="100px"><mmr:message messageId="label.upload.file"/></td>
					<td width="300px" align="left"> 
						<s:file name="upload" label="File" key="upload"  theme="simple" cssClass="text_01_select_file"/>
					</td>
					<td>
					<div id="upload_icon" style="cursor: pointer;"><img src="<s:url value="/mmr/images/BoxUpload.png" includeContext="true" />" border="0" onclick="processNow();"></div>
					</td>
					<td width="50px">&nbsp;</td>
					<td class="erase_add" width="250px" valign="middle">
						<s:checkbox name="erase_add_cb" id="erase_add_cb_id"/>&nbsp;<mmr:message messageId="label.upload.file.eraze.address"/>
						
					</td>
				</tr>				
									
			</table>
</div>
<div id="addupld_upload_icon"></div>	

<div id="addressbook_icon">&nbsp;</div>
<div id="address_instructions_hdr">
	<table>
	<tr>
	<td><mmr:message messageId="label.addressbook.instruction"/></td>
	</tr>
	</table>
	</div>
	
	<div id="address_instructions_mid">
	<table width="800px">
	<tr>
	<td width="100%"><mmr:message messageId="instructions.upload.addressbook"/></td>
	</tr>
	</table>
	</div>
	
	<div id="address_instructions_bot">&nbsp;</div>

</s:form>
</div>
</div>
