<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>


<table class="text_01" cellpadding="3" cellspacing="3" id="from_add_inner_table">
	<!--<tr>
	  	<td class="text_03" width="10%"><mmr:message messageId="label.shippingOrder.company"/>:</td>
	    <td width="20%"><s:textfield size="20" key="shippingOrder.fromAddress.abbreviationName" name="shippingOrder.fromAddress.abbreviationName" cssClass="text_02_tf"/></td>
	    <td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
	    <td width="20%"><s:textfield size="20" key="shippingOrder.fromAddress.address1" name="shippingOrder.fromAddress.address1"  cssClass="text_02_tf" /></td>				 
	</tr>-->
	<tr>
		<td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
	    <td width="20%"><s:textfield size="20" key="shippingOrder.fromAddress.address1" name="shippingOrder.fromAddress.address1"  cssClass="text_02_tf" /></td>
		<td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.address2"/>:</td>
	    <td width="20%"><s:textfield size="20" key="shippingOrder.fromAddress.address2" name="shippingOrder.fromAddress.address2"  cssClass="text_02_tf" value="%{shippingOrder.fromAddress.address2}"/></td>
		<td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.country"/>:</td>
	    <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="shippingOrder.fromAddress.countryCode" headerKey="-1"  list="#session.CountryList" 
	                  onchange="javascript:showShipFromState();"  id="firstBox" theme="simple"/></td>
	    <td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.zip"/>:</td>
	    <td width="20%">
	    	<s:textfield size="20" key="shippingOrder.postalCode" onblur="javascript:getAddressSuggestFrom();"  id="fromPostalCode" name="shippingOrder.fromAddress.postalCode"  cssClass="text_02_tf" value="%{shippingOrder.fromAddress.postalCode}"/>
	    	<img id="loading-img-from" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">
	    </td>	
	 </tr>
	<tr>   			  
		<td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.city"/>:</td>
	    <td width="20%"><s:textfield size="20" id = "fromcity" key="shippingOrder.fromAddress.city" name="shippingOrder.fromAddress.city"  cssClass="text_02_tf" value="%{shippingOrder.fromAddress.city}"/></td>
	    <td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.state"/>:</td>
	    <td width="20%" id="stateid"><s:include value="../admin/shippingFromProvienceList.jsp"/></td>
	    <td width="10%"  class="text_03"><mmr:message messageId="label.shippingOrder.phone"/>:</td>
	    <td width="20%"><s:textfield size="20" key="shippingOrder.fromAddress.phoneNo" name="shippingOrder.fromAddress.phoneNo"  cssClass="text_02_tf" /></td>        
		<td width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.attention"/>:</td>
        <td width="20%"><s:textfield size="20" key="shippingOrder.fromAddress.contactName" name="shippingOrder.fromAddress.contactName"  cssClass="text_02_tf"/></td>
     </tr>
	<tr>   		
	    <td  width="10%" class="text_03"><mmr:message messageId="label.shippingOrder.email"/>:</td>
	    <td width="20%"><s:textfield size="20" key="shippingOrder.email" name="shippingOrder.fromAddress.emailAddress"  cssClass="text_02_tf" /></td>
	    <td class="text_03" width="20%" align="left"><mmr:message messageId="shippingOrder.shipFromId.residential"/>:</td>
	    <td class="text_03" width="20%" align="left"><s:checkbox cssClass="text_01" value="%{shippingOrder.fromAddress.residential}"  name="shippingOrder.fromAddress.residential"/></td>
	    <td class="text_03"><mmr:message messageId="label.shippingOrder.instruction"/>:</td>
	    <td class="text_03" colspan="2"><s:textfield cssStyle="background-color: #F2F2F2; border-radius:4px; border: 1px solid; width:330px; height:22px;" rows="1" key="shippingOrder.fromAddress.instruction" name="shippingOrder.fromInstructions"  cssClass="text_02"/></td>
	    </td>		
	    <td class="text_03">
	    	<mmr:message messageId="label.notify.shipper"/>:<s:checkbox  cssClass="text_01" value="%{shippingOrder.fromAddress.sendNotification}"  name="shippingOrder.fromAddress.sendNotification"/>
	    </td>
	</tr>	
	</table>