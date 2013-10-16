<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>


<SCRIPT type="text/javascript">

	
	function submitform(method)
	{
	 //alert("AHA");	
	 document.orderForm.action = "shipment.stageThree.action?method="+method;
	 document.orderForm.submit();
	}
	
    
   
</script>


<div id="productDetails">
		
		<div id="div_bill_section_four_tbl">
		<table cellpadding="4" cellspacing="0" class="text_01" width="950px">				
			<s:iterator value="%{shippingOrder.customsInvoice.products}" status="counterIndex">	
				<s:set name="custInvId" value="%{customsInvoice.products[#counterIndex.index].customsInvoiceId}"/>
				<s:set name="prodId" value="%{customsInvoice.products[#counterIndex.index].productId"/>
				<tr>
					<td class="tablerow3" align="center"><s:property value="%{#counterIndex.index+1}"/></td>
			        <td class="tablerow3" align="center"><s:textfield id="cip[#counterIndex.index].productDesc" name="shippingOrder.customsInvoice.products[#counterIndex.index].productDesc"  cssClass="text_02_tf" theme="simple" size="3" value="%{shippingOrder.customsInvoice.products[#counterIndex.index].productDesc}" readonly="true"/></td>
			        <td class="tablerow3" align="center"><s:textfield id="cip[#counterIndex.index].productHC" name="shippingOrder.customsInvoice.products[#counterIndex.index].productHC"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{shippingOrder.customsInvoice.products[#counterIndex.index].productHC}"/></td>
			        <td class="tablerow3" align="center"><s:select id="cip[#counterIndex.index].productOrigin" cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="shippingOrder.customsInvoice.products[#counterIndex.index].productOrigin" list="#session.CountryList" onchange="javascript:showShipToState();" headerKey="-1" theme="simple"/></td>
			        <td class="tablerow3" align="center"><s:textfield id="cip[#counterIndex.index].productQuantity" name="shippingOrder.customsInvoice.products[#counterIndex.index].productQuantity"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{shippingOrder.customsInvoice.products[#counterIndex.index].productQuantity}"/></td>
			        <td class="tablerow3" align="center"><s:textfield id="cip[#counterIndex.index].productUnitPrice" name="shippingOrder.customsInvoice.products[#counterIndex.index].productUnitPrice"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{shippingOrder.customsInvoice.products[#counterIndex.index].productUnitPrice}"/></td>
			        <td class="tablerow3" align="center"><s:textfield id="cip[#counterIndex.index].totalPrice" name="customerSalesCommission"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{shippingOrder.customsInvoice.products[#counterIndex.index].productQuantity*shippingOrder.customsInvoice.products[#counterIndex.index].productUnitPrice}"/></td>
				<!-- 	<td class="tablerow3" align="center">
					<s:a href="javascript: if(confirm('Do you really want to update the account?')){updateSalesUser(%{#custInvId},%{#counterIndex.index})}">
					<img src="<s:url value="/mmr/images/update.png" includeContext="true" />" border="0" style="text-decoration: none;">
					</s:a></td>-->
					<td class="tablerow3" align="center">
					<s:a href="javascript: if(confirm('Do you really want to delete the selected Product?')){deleteProduct(%{#counterIndex.index});}">
					<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" border="0">
					</s:a></td>
				</tr>	
			</s:iterator>		
		</table>
	</div>
	
	<div id="div_bill_section_four_end">&nbsp;</div>
</div>