<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<div id="div_ship_from">
	<table style="width:650px;" border="0" cellspacing="1" cellpadding="2" class="text_01">
	
	<tr>
	<!--  	<td class="text_03" width="11%">Search Address:</td>
	    <td width="25%"><s:url id="addressList" action="listAddresses" />
	        <sx:autocompleter keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="false" valueNotifyTopics=""/>
	        <a href="javascript:searchFromAddress()" style="margin-top:20px;"><img src="<%=request.getContextPath()%>/mmr/images/magnifier_left.png" border="0"/></a>
		</td>-->
		<td class="text_03_company"><mmr:message messageId="label.shippingOrder.company"/>:</td>
		<td colspan="1">
		<s:hidden name="shippingOrder.fromAddress.abbreviationName" id="shippingOrder.fromAddress.abbreviationName"/>
		<!-- Check if the From Address exists, then set the Abbreviation Name to the company Autocompleter.  -->
			<s:if test="%{shippingOrder.fromAddress.address1 !=null}">
					<s:url id="addressList" action="listAddresses" >
					 <s:param name="shippingOrder.customerId" value="%{shippingOrder.customerId}"/>
					</s:url>
				    <sx:autocompleter preload="false" id="autoaddressf" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="false" valueNotifyTopics="/valueChangedFrom" showDownArrow="false"  value="%{shippingOrder.fromAddress.abbreviationName}" onkeyup="javascript: assignCompany('from');" cssStyle="width: 410px;" />
			</s:if>
			<s:else>
				<s:url id="addressList" action="listAddresses" >
					 <s:param name="shippingOrder.customerId" value="%{shippingOrder.customerId}"/>
					</s:url>
				    <sx:autocompleter preload="false" id="autoaddressf" keyName="address.addressId" name="searchString" href="%{addressList}" dataFieldName="addressSearchResult" loadOnTextChange="true" loadMinimumCount="3" autoComplete="false" valueNotifyTopics="/valueChangedFrom" showDownArrow="false" onkeyup="javascript: assignCompany('from');"/>
			</s:else>
		</td>
		<td class="text_03"><mmr:message messageId="label.address.button.save"/>:<s:checkbox cssClass="text_01" value="%{shippingOrder.saveFromAddress}"  name="shippingOrder.saveFromAddress"/></td>
	</tr>
	</table>
	<div id="fromAdd_inner">
		<s:include value="fromAddress_inner.jsp"/>
	</div>
</div>

