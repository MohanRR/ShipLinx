<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div id="div_ship_to">
	<table  style="width:650px;" border="0" cellspacing="1" cellpadding="2" class="text_01">
	
	<tr>
	<!--  	<td class="text_03" width="11%">Search Address:</td>
        <td width="25%">                  
	        <sx:autocompleter id="autoaddress" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="true"/>
	        <a href="javascript:searchToAddress()"><img src="<%=request.getContextPath()%>/mmr/images/magnifier_left.png" border="0"/></a>
		</td>-->
		<td class="text_03_company"><mmr:message messageId="label.shippingOrder.company"/>:</td>
		<td colspan="3">
		<s:hidden name="shippingOrder.toAddress.abbreviationName" id="shippingOrder.toAddress.abbreviationName"/>
		<s:if test="%{shippingOrder.toAddress.abbreviationName!=null}">
			    <s:url id="addressList" action="listAddresses" >
			    <s:param name="shippingOrder.customerId" value="%{shippingOrder.customerId}"/>
			    </s:url>
			    <sx:autocompleter preload="false" id="autoaddresst" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="false" valueNotifyTopics="/valueChangedTo" showDownArrow="false" value="%{shippingOrder.toAddress.abbreviationName}" onkeyup="javascript: assignCompany('to');" cssStyle="width: 403px;"/>
		</s:if>
		<s:else>
			<s:url id="addressList" action="listAddresses" >
			    <s:param name="shippingOrder.customerId" value="%{shippingOrder.customerId}"/>
			    </s:url>
			    <sx:autocompleter preload="false" id="autoaddresst" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="false" valueNotifyTopics="/valueChangedTo" showDownArrow="false" onkeyup="javascript: assignCompany('to');"/>
		</s:else>
		</td>
		<td class="text_03"><mmr:message messageId="label.address.button.save"/>:<s:checkbox cssClass="text_01" value="%{shippingOrder.saveToAddress}"  name="shippingOrder.saveToAddress"/></td>
	</tr>
	</table>
	<div id="toAdd_inner">
		<s:include value="toAddress_inner.jsp"/>
	</div>
</div>	
