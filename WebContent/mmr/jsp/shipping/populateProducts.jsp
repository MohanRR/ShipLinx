<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div id="div_bill_section_four_entry_tbl">
		<table cellpadding="0" cellspacing="0" class="text_01" width="950px">			
			<tr bgcolor="#cccccc" height="25px">
					<td class="tableTitle3">&nbsp;</td>
			        <td class="tableTitle3" align="center"><mmr:message messageId="label.product.description"/></td>
			        <td class="tableTitle3" align="center"><mmr:message messageId="label.harmonized.code"/></td>
			        <td class="tableTitle3" align="center"><mmr:message messageId="label.country.origin"/></td>	
			        <td class="tableTitle3" align="center"><mmr:message messageId="label.unit.price"/></td>
			        <td class="tableTitle3" align="center"><mmr:message messageId="label.quantity.integer"/></td>
			        <td class="tableTitle3" align="center"><mmr:message messageId="label.total.price"/></td>
			        <td class="tableTitle3">&nbsp;</td>
			</tr>
			<tr>
				<td class="tablerow3" >&nbsp;</td>
				<td class="tablerow3" align="center">
				<s:url id="productdescList" action="listProducts" >
					<s:param name="field">desc</s:param>					
				</s:url>
				<s:hidden name="custid" value="%{shippingOrder.customerId}" id="hidden_cid"/>
				<sx:autocompleter id="autoproductdesc" keyName="products.product_id" name="searchString" href="%{productdescList}" dataFieldName="productSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="false"  valueNotifyTopics="/value_desc" cssStyle="width:150px;" preload="true"/>
			<!--  <img id="loading-img-desc" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">-->
			<!--  <s:textfield id="new_prod_desc" name="new_prod_desc" cssClass="text_02_tf" theme="simple"/></td>-->
				</td>
		        <td class="tablerow3" align="center">
		        <s:url id="productdescList" action="listProducts" >
					<s:param name="field">hcode</s:param>					
				</s:url>
			<!--	<sx:autocompleter id="autoproducthcode" keyName="products.product_id" name="searchString" href="%{productdescList}" dataFieldName="productSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="true" valueNotifyTopics="/value_hcode" forceValidOption="true" cssStyle="width:150px;" preload="true"/>-->
		  	<!-- <img id="loading-img-hcode" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">-->
		        <s:textfield id="new_prod_hcode" name="new_prod_hcode" cssClass="text_02_tf_small" theme="simple" /></td>
		        </td>
		        <td class="tablerow3" align="center"><s:select id="new_productOrigin" cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="products.originCountry" list="#session.CountryList" headerKey="-1"  theme="simple"/></td>
				<td class="tablerow3" align="center"><s:textfield id="new_prod_uprice" name="products.unitPrice" cssClass="text_02_tf_small" theme="simple" onchange="javascript: assignTotal()" onkeypress="return typenumbers(event,\'0123456789.\')"/></td>
		        <td class="tablerow3" align="center"><s:textfield id="new_prod_quantity" name="new_prod_quantity" cssClass="text_02_tf_small" theme="simple" onchange="javascript: assignTotal()"  onkeypress="return typenumbers(event,\'0123456789\')"/></td>
		        <td class="tablerow3" align="center" valign="middle"><s:textfield id="new_prod_tprice" name="new_prod_tprice" cssClass="text_02_tf_small" theme="simple" readonly="true"/></td>
		        <td class="tablerow3" align="center" valign="middle">&nbsp;&nbsp; <div id="div_add_product"><!--<img src="<s:url value="/mmr/images/add_product.png" includeContext="true" />" border="0" onclick="checkToAdd()">--></div> </td>
			</tr>
			<tr><td colspan="8" valign="middle">_________________________________________________________________________________________________________________________________________</td></tr>			
			</table>
	</div>