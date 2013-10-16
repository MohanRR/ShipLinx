<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<!--  
<div id="pckg_choose">
<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0" style="margin-bottom:-3px;">
&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/fast_forward.png" includeContext="true" />" border="0" style="margin-bottom:-3px;">&nbsp;&nbsp;  
<a href="backToShipment.action?switch=true">Switch to Quote Mode</a>
</div>
-->
<div id="srch_address">
<s:if test="%{#session.ROLE.contains('busadmin')}">
	<table width="950px;" style="padding-top: 3px;">
	<tr><td valign="middle" align="center" class="enter_ship_details" colspan="6" height="20px"><mmr:message messageId="label.enter.shipping.details"/><s:if test="%{shippingOrder.id > 0}">&nbsp;---&nbsp;<mmr:message messageId="label.shippingOrderTab.order"/>#&nbsp;<font color="blue"><s:property value="shippingOrder.id" /></font></s:if>
	<s:else><mmr:message messageId="label.enter.new.shipping.details"/></s:else>
	</td></tr>
		<tr>
			<td class="text_03" align="left">
				<mmr:message messageId="label.customer.set"/>:&nbsp;&nbsp;
			</td>
			<td colspan="2">
				<s:if test="%{shippingOrder.customer!=null}"> <!-- Put the logic of condition check for customer if selected and retrieved in the called Action -->
					<font color="blue" style="font-family: Arial; font-weight: bold; font-size: 11;"><s:label key="shippingOrder.customer.name" /></font> <!-- Put the logic of displaying the customer that is set in the called Action -->
				</s:if>
				<s:else>
					<font color="red"><mmr:message messageId="label.customer.notset"/></font>
				</s:else>
			</td>		
			<td class="text_03">
				<mmr:message messageId="label.customer.changeto"/>:
			</td>
			<td class="text_03">
				<s:select key="shippingOrder.webCustomerId" cssClass="text_01_combo_big" cssStyle="height:20px; width: 150px;" 
					listKey="value" listValue="key" list="#session.customersList" onchange="changeCustomer(this.value);"/>	
				
				<!--
				<s:url id="customerList" action="listCustomersWithOrphan" />
				<sx:autocompleter keyName="shippingOrder.webCustomerId" name="searchString" 
        			href="%{customerList}" dataFieldName="customerSearchResult"
        			cssStyle="height:20px; width: 150px;" loadOnTextChange="true" loadMinimumCount="3"/>
				-->
			</td>
	
		</tr>
	</table>
</s:if>
<s:else>
	<table width="850px;">
		<tr><td valign="middle" align="center" class="enter_ship_details" colspan="4" height="20px"><mmr:message messageId="label.enter.shipping.details"/><s:if test="%{shippingOrder.id > 0}">&nbsp;---&nbsp;<mmr:message messageId="label.shippingOrderTab.order"/>#&nbsp;<font color="blue"><s:property value="shippingOrder.id" /></font></s:if>
	<s:else><mmr:message messageId="label.enter.new.shipping.details"/></s:else></td></tr>
	</table>
</s:else>
				<s:hidden name="shippingOrder.customerId" />
</div>