<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib prefix="sjt" uri="/struts-jquery-tree-tags"%>

<html> 
<head> 
    <sj:head jqueryui="true" />
    <sx:head cache="true"/>
    <title></title> 
</head> 
<SCRIPT language="JavaScript">
	function submitform(action)
	{
	 document.generateInvoiceForm.action = action;
	 document.generateInvoiceForm.submit();
	}
	function autoGen(action)
	{
		if(confirm("Would you like to run the auto-gen process?")) {
			 document.generateInvoiceForm.action = action;
			 document.generateInvoiceForm.submit();
		}
	
	}
	
</SCRIPT>

<body> 

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="invoice.create" name="generateInvoiceForm">
<s:token/>
<div id="srch_panel">
<table>
<tr>
<td><div id="srch_crtra_geninv"><mmr:message messageId="menu.admin.genInvoice"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="srchactions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/bill_selected.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/generate.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="srch_actions">
<table>
<tr>
<td><a href="javascript:submitform('invoiceable.list.action')"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript:submitform('invoice.create.action')"><mmr:message messageId="label.invoice.bill.selected"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript:autoGen('invoice.autogen.action')"><mmr:message messageId="label.invoice.auto.generate"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>

<div id="geninv_srch_table">
	<table width="700px">
	            	<tr>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.customer.name" />: </td>	
		            	<s:url id="customerList" action="listCustomers" />
		            	<td><sx:autocompleter value="%{invoice.customer.name}" keyName="invoice.customerId" name="searchString" href="%{customerList}" dataFieldName="customerSearchResult" loadOnTextChange="true" loadMinimumCount="3"/></td>
		            	<td>&nbsp;</td>
		            	<td>&nbsp;</td>	 
		            	<td>&nbsp;</td>
		            	<td>&nbsp;</td>		            	         	
	            	</tr>
	            <!--  	<tr>
	            		
		            	<td>&nbsp;</td>
		            	<td>&nbsp;</td>
		            	<td>&nbsp;</td>
		            	<td class="text_01_geninv_srch">
			            	<s:submit 
			            	id="formSubmit2"
			            	onclick="javascript:submitform('invoiceable.list.action')"
			            	value="Search" 
			            	/>
						</td>
		            	<td class="text_01_geninv">
			            	<s:submit 
			            	id="formSubmit2"
			            	onclick="javascript:submitform('invoice.create.action')"
			            	value="Bill Selected" 
			            	/>
		            	</td>
		            	<td class="text_01_geninv">
			            	<s:submit 
			            	id="formSubmit2"
			            	onclick="javascript:submitform('invoice.autogen.action')"
			            	value="Auto Generate" 
			            	/>
		            	</td>
	            	</tr>-->
	</table>
</div>
	   <div id="formResult">
	  	 <s:include value="unbilledShipments.jsp"></s:include>
	   </div>
</s:form>
</div>

</body>
</html>