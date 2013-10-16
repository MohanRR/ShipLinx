<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>



<html> 
<head> 
    <sj:head jqueryui="true" />
    <sx:head />
    <title><s:text name="edi.title"/></title> 
</head> 
<body> 
<SCRIPT language="JavaScript">
	function searchInvoice()
	{
	 document.searchform.action = "invoice.action";
	 document.searchform.submit();
	}
		
	function submitform()
	{	
	 document.searchInvoiceForm.action = "invoice.list.action";
	 document.searchInvoiceForm.submit();
	 //return false;
	}
	
	function showID(name)
	{
		alert(name);
	}
	
	function setInvoiceNum()
	{
		var inum_autocompleter = dojo.widget.byId("auto_invoice_num");
		var invnum_val = inum_autocompleter.getText();
		//alert(invnum_val);
		document.getElementById("invoice.invoiceNum").value = invnum_val;		
	}
</SCRIPT>

<div id="messages">
<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="invoice.list" name="searchInvoiceForm">

<div id="srch_panel">
<table>
<tr>
<td><div id="srch_crtra"><mmr:message messageId="label.invoice.search"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="searchinv_actions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="searchinv_actions">
<table>
<tr>
<td><a href="javascript: submitform()"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>

<div id="srch_table">
<table width="750px" style="margin-top:2px;">
	            	<tr>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.from.date" />: </td>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.to.date" />: </td>   	
		            	<s:if test="%{#session.ROLE.contains('busadmin')}">
		            		<td class="srchinv_text_03"><mmr:message messageId="label.customer.name" />: </td>
		            	</s:if>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.invoice.number"/>: </td>
		            	<td class="srchinv_text_03"><mmr:message messageId="label.invoice.paymentStatus"/>: </td> 	            	
	            	</tr>
	            	<tr>
		            	<td> <!--   <sj:datepicker name="invoice.fromInvoiceDate_web" />-->
		            	<!--  	<sx:datetimepicker name="invoice.fromInvoiceDate_web"></sx:datetimepicker>-->
		            	<s:textfield name="invoice.fromInvoiceDate_web" id="f_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="f_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');">
		            	</td>		            	
		            	<td>
		              <!--  	<sj:datepicker name="invoice.toInvoiceDate_web"/>-->
		            <!--  	<sx:datetimepicker name="invoice.toInvoiceDate_web"></sx:datetimepicker>-->
		            <s:textfield name="invoice.toInvoiceDate_web" id="t_date_c" cssStyle="width: 75px;"
											cssClass="text_02_tf" readonly="readonly"/><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
											id="t_trigger_c" style="cursor: pointer;"
											title="Date selector" border="0" onClick="selectDate('t_date_c','t_trigger_c');">
		            	</td>
		            	<s:if test="%{#session.ROLE.contains('busadmin')}">
			            	<s:url id="customerList" action="listCustomers" />
			            	<td><sx:autocompleter keyName="invoice.customerId" name="searchString" href="%{customerList}" dataFieldName="customerSearchResult" 
			            		loadOnTextChange="true" loadMinimumCount="3" cssStyle="height:20px;width:130px;" cssClass="dojoComboBox"/></td>
			            </s:if>
        					<s:url id="invoiceList" action="listInvoices" />
        					<s:hidden id="invoice.invoiceNum" name="invoice.invoiceNum"/>
			            	<td><sx:autocompleter id="auto_invoice_num" keyName="invoice.invoiceId" name="searchStringInvoices" 
			            		value="%{invoice.invoiceNum}"  href="%{invoiceList}" dataFieldName="invoiceSearchResult" loadOnTextChange="true" loadMinimumCount="3" cssStyle="height:20px;width:130px;" cssClass="dojoComboBox" onkeyup="setInvoiceNum();"/></td>
			    		            	<td>					
		            	 	<s:select cssClass="text_01_combo_big" cssStyle="width:154px;" listKey="id" listValue="statusName" name="invoice.paymentStatusList" headerKey="0" headerValue="Choose Status" list="statusList" id="secondBox"  theme="simple" />
						</td>
	            	</tr>
	            </table>
</div>
	
	
	
	<div id="formResult">
	   <s:include value="invoiceList.jsp"></s:include>
	</div>	
	
</s:form>
	
</div>
</div>
		            	