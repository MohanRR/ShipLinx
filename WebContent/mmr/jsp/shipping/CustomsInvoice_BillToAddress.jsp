<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 


<SCRIPT type="text/javascript">

	
	function submitform(method)
	{
	 //alert("AHA");	
	 document.orderForm.action = "shipment.stageThree.action?method="+method;
	 document.orderForm.submit();
	}

	function showBillToState() {
		ajax_Country=ajaxFunction();
		ajax_Country.onreadystatechange=function()
		  {
			   if(ajax_Country.readyState==4)
				{
				reponse=ajax_Country.responseText;
				js_stateid=document.getElementById("billToProvince");
				js_stateid.innerHTML= reponse;
				}
		  }
		  firstBox = document.getElementById('firstBox2');
		  url=contextPath+"/shipTo.listToProvience.action?type=billTo&value="+firstBox.value;
			//param="objName=ref_state&country_id="+country_id;
			ajax_Country.open("GET",url,true);
			ajax_Country.send(this);
	} // End function showState()  

    
   
</script>


	
<div id="customs_invoice_billto_address">
		<div id="div_bill_section_one">
		
			<table  style="width:970px;" border="0" cellspacing="2" cellpadding="2" class="text_01">			
			<tr>	
				<td width="9%" class="text_03"><mmr:message messageId="label.shippingOrder.company"/>:</td>
		        <td width="19%"><s:textfield size="20" name="shippingOrder.customsInvoice.billToAddress.abbreviationName" value="%{shippingOrder.customsInvoice.billToAddress.abbreviationName}" cssClass="text_02_tf" /></td>
				<td width="8%" class="text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
		        <td width="20%"><s:textfield size="20" name="shippingOrder.customsInvoice.billToAddress.address1" value="%{shippingOrder.customsInvoice.billToAddress.address1}" cssClass="text_02_tf"/></td>
		        <td width="9%" class="text_03"><mmr:message messageId="label.shippingOrder.address2"/>:</td>
		        <td width="19%"><s:textfield size="20" name="shippingOrder.customsInvoice.billToAddress.address2" value="%{shippingOrder.customsInvoice.billToAddress.address2}" cssClass="text_02_tf"/></td>
		        <td width="25%" class="text_03"><mmr:message messageId="label.shippingOrder.zip"/>:</td>
		        <td width="20%">
		        	<s:textfield size="20" id="toPostalCode" name="shippingOrder.customsInvoice.billToAddress.postalCode" value="%{shippingOrder.customsInvoice.billToAddress.postalCode}" cssClass="text_02_tf" />		        	
		        </td>        
			</tr>
			<tr>		
				<td width="9%" class="text_03"><mmr:message messageId="label.shippingOrder.city"/>:</td>
		        <td width="19%"><s:textfield size="20" name="shippingOrder.customsInvoice.billToAddress.city" value="%{shippingOrder.customsInvoice.billToAddress.city}" cssClass="text_02_tf"/></td>
		        <td width="8%" class="text_03"><mmr:message messageId="label.shippingOrder.country"/>:</td>
		        <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="shippingOrder.customsInvoice.billToAddress.countryCode" list="#session.CountryList" onchange="javascript:showBillToState();" headerKey="-1" theme="simple" id="firstBox2"/></td>
		        <td width="9%" class="text_03"><mmr:message messageId="label.shippingOrder.state"/>:</td>
				<td width="20%" id="billToProvince"><s:include value="customsInvoiceBillToProvince.jsp"/></td>
				<td width="25%" class="text_03"><div id="accnt_number_lbl"><mmr:message messageId="label.customer.accountNumber"/>:</div></td>
		        <td width="20%"><div id="accnt_number_txt"><s:textfield id="billToAccountNum" size="20" name="shippingOrder.customsInvoice.billToAccountNum" value="%{shippingOrder.customsInvoice.billToAccountNum}" cssClass="text_02_tf" /></div></td>
			</tr>						
			</table>
		</div>
		</div>
		
	