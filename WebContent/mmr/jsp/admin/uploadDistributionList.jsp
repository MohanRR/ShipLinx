<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<SCRIPT language="JavaScript">
	function submitform()
	{
	alert("here");
	 document.dlistform.action = "upload.distribution.list.action";
	 document.dlistform.submit();
	}
</SCRIPT> 
<div class="form-container">
<s:form action="upload.distribution.list" method="post" enctype="multipart/form-data" name="dlistform">

<div id="right_left_new">

<div id="dlupload_text">
	<mmr:message messageId="label.shippingOrder.uploadDistribution" />:
</div>

<div id="dlupload_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/reset_icon.png" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0"></td>
</tr>
</table>
</div>
<div id="dlupload_actions">
<table>
<tr>
<td><a href="javascript:submitform()"><mmr:message messageId="label.btn.save"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
<td><a href="upload.distribution.init.action"><mmr:message messageId="label.btn.reset"/></a></td>
</tr>
</table>
</div>

<div id="dlupload_panel">
<table border="0" cellpadding="5" cellspacing="0">	
	<tr>
			<td class="edi_text_03"><s:label key="Name"/>:</td>
			<td class="edi_text_03"><s:label key="File"/>:</td>
	</tr>
	<tr>
			<td class="edi_text_03"><s:textfield size="24" cssClass="text_02_tf" key="name" name="name" theme="simple" cssStyle="width:150px;"/></td>
			<td class="edi_text_03"><s:file name="upload" label="File" key="upload"  theme="simple" cssClass="edi_text_03"/></td>
	</tr>	
</table>            
	        
</div>
<div id="dlupload_pnlicon">
&nbsp;
</div>






</div>
</s:form>
</div>