<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<table class="text_01" cellpadding="3" cellspacing="3" id="to_add_inner_table">
<!--  	<tr>
		<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.company"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.toAddress.abbreviationName" name="shippingOrder.toAddress.abbreviationName" cssClass="text_02_tf" /></td>
		<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.toAddress.address1" name="shippingOrder.toAddress.address1"  cssClass="text_02_tf"/></td>
      </tr>-->
	<tr>  
		<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.toAddress.address1" name="shippingOrder.toAddress.address1"  cssClass="text_02_tf"/></td>
        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address2"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.toAddress.address2" name="shippingOrder.toAddress.address2"  cssClass="text_02_tf"/></td>
        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.country"/>:</td>
        <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="shippingOrder.toAddress.countryCode" list="#session.CountryList" onchange="javascript:showShipToState();" headerKey="-1"  id="firstBox2" theme="simple"/></td>
        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.zip"/>:</td>
        <td width="20%">
        	<s:textfield size="20" key="shippingOrder.postalCode" onblur="javascript:getAddressSuggestTo();"  id="toPostalCode" name="shippingOrder.toAddress.postalCode"  cssClass="text_02_tf" />
        	<img id="loading-img-to" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">
        </td> 
    </tr>
	<tr>           
		<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.city"/>:</td>
		
        <td width="20%">
		<s:textfield size="20" id="tocity" key="shippingOrder.toAddress.city" name="shippingOrder.toAddress.city"  cssClass="text_02_tf"/>        
		</td>
        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.state"/>:</td>
        <td width="20%" id="stateid2">
			<s:include value="../admin/shippingToProvienceList.jsp"/>
        </td>					
		<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.phone"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.toAddress.phoneNo" name="shippingOrder.toAddress.phoneNo"   cssClass="text_02_tf"/></td>
	    <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.attention"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.toAddress.contactName" name="shippingOrder.toAddress.contactName"  cssClass="text_02_tf"/></td>
     </tr>
     <tr>   
        <td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.email"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.email" name="shippingOrder.toAddress.emailAddress"  cssClass="text_02_tf" /></td>
        <td class="text_03" colspan="2" width="21%">
			<mmr:message messageId="shippingOrder.shipToId.residential"/>:<s:checkbox cssClass="text_01" value="%{shippingOrder.toAddress.residential}"  name="shippingOrder.toAddress.residential"/>&nbsp;
		</td>
        <td width="5%" class="text_03"><!--  <s:checkbox cssClass="text_01" name="shippingOrder.toTailgate"/> -->
        	<mmr:message messageId="label.shippingOrder.instruction"/>:
        </td>
		<td class="text_03" colspan="2"><!-- <s:label cssClass="text_03" key="order.Tailgate"/> -->
			<s:textarea cssStyle="width:95%; background-color: #F2F2F2; border-radius:4px; border: 1px solid; width:330px; height:22px;" rows="1" key="shippingOrder.toAddress.instruction" name="shippingOrder.toInstructions"  cssClass="text_02"/>
		</td>
		<td class="text_03">
			<mmr:message messageId="label.notify.consignee"/>:<s:checkbox  cssClass="text_01" value="%{shippingOrder.toAddress.sendNotification}"  name="shippingOrder.toAddress.sendNotification"/>
		</td>
	</tr>		
	</table>
	
	