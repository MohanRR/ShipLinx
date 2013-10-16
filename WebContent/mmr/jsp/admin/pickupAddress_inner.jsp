<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>


<table class="text_01" cellpadding="3" cellspacing="3">
	<!--<tr>
	  	<td class="srchshipmnt_text_03" width="10%"><mmr:message messageId="label.shippingOrder.company"/>:</td>
	    <td width="20%"><s:textfield size="20" key="pickup.address.abbreviationName" name="pickup.address.abbreviationName" cssClass="text_02_tf"/></td>
	    <td  width="10%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
	    <td width="20%"><s:textfield size="20" key="pickup.address.address1" name="pickup.address.address1"  cssClass="text_02_tf" /></td>				 
	</tr>-->
	<tr>
		<td  width="9%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.address1"/>:</td>
	    <td width="20%"><s:textfield size="20" key="pickup.address.address1" name="pickup.address.address1"  cssClass="text_02_tf" /></td>
		<td  width="10%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.address2"/>:</td>
	    <td width="20%"><s:textfield size="20" key="pickup.address.address2" name="pickup.address.address2"  cssClass="text_02_tf" value="%{pickup.address.address2}"/></td>
		<td  width="15%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.country"/>:</td>
	    <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" name="pickup.address.countryCode" listKey="countryCode" listValue="countryName" list="#session.CountryList" 
	                  onchange="javascript:showStatePickup();"  id="firstBoxPickup" theme="simple"/></td>
	    <td  width="10%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.zip"/>:</td>
	    <td width="20%">
	    	<s:textfield size="20" key="pickup.address.postalCode" onblur="javascript:getAddressSuggestPickup();"  id="fromPostalCode" name="pickup.address.postalCode"  cssClass="text_02_tf" value="%{pickup.address.postalCode}"/>
	    	<img id="loading-img-from" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">
	    </td>	
	 </tr>
	<tr>   			  
		<td  width="9%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.city"/>:</td>
	    <td width="20%"><s:textfield size="20" key="pickup.address.city" name="pickup.address.city"  cssClass="text_02_tf" value="%{pickup.address.city}"/></td>
	    <td  width="10%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.state"/>:</td>
	    <td width="20%" id="stateidP">
	    <s:select key="pickup.address.provinceCode" name="pickup.address.provinceCode"  
		cssClass="text_01_combo_big" cssStyle="width:158px;" value="%{pickup.address.provinceCode}" 
		listKey="provinceCode" listValue="provinceName" list="#session.provinces"/>
	    </td>
	    <td width="10%"  class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.phone"/>:</td>
	    <td width="20%"><s:textfield size="20" key="pickup.address.phoneNo" name="pickup.address.phoneNo"  cssClass="text_02_tf" /></td>        
		<td width="10%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.attention"/>:</td>
        <td width="20%"><s:textfield size="20" key="pickup.address.contactName" name="pickup.address.contactName"  cssClass="text_02_tf"/></td>
     </tr>
	<tr>   		
	    <td  width="9%" class="srchshipmnt_text_03"><mmr:message messageId="label.shippingOrder.email"/>:</td>
	    <td width="20%"><s:textfield size="20" key="shippingOrder.email" name="pickup.address.emailAddress"  cssClass="text_02_tf" /></td>
	    <td class="srchshipmnt_text_03" width="10%" align="left"><mmr:message messageId="shippingOrder.shipFromId.residential"/>:</td>
	    <td class="srchshipmnt_text_03" width="20%" align="left"><s:checkbox cssClass="text_01" value="%{pickup.address.residential}"  name="pickup.address.residential"/></td>
	   	<td class="srchshipmnt_text_03"><mmr:message messageId="label.destination.country"/>:</td>
		<td class="srchshipmnt_text_03">
			<s:select cssClass="text_01" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="pickup.destinationCountryCode" list="#session.CountryList" 
	                  id="firstBoxPickup" theme="simple"/>
		</td>	
		<td class="srchshipmnt_text_03"><mmr:message messageId="label.pieces.number"/>:</td>
		<td class="srchshipmnt_text_03">
			<s:textfield size="20" key="pickup.quantity" name="pickup.quantity"  cssClass="text_02_tf_small" onkeypress="return typenumbers(event,\'0123456789\')"/>
		</td>
	</tr>
	<tr>   		
	    <td  width="9%" class="srchshipmnt_text_03"><mmr:message messageId="label.pieces.oversize"/>:</td>
	    <td width="20%"><s:textfield size="20" key="pickup.oversizeQuantity" name="pickup.oversizeQuantity"  cssClass="text_02_tf" onkeypress="return typenumbers(event,\'0123456789\')"/></td>
	    <td class="srchshipmnt_text_03" width="10%" align="left"><mmr:message messageId="label.total.weight"/>:</td>
	    <td class="srchshipmnt_text_03" width="20%" align="left"><s:textfield size="10" key="pickup.totalWeight" name="pickup.totalWeight"  cssClass="text_02_tf_small" onkeypress="return typenumbers(event,\'0123456789.\')"/></td>
	   	<td class="srchshipmnt_text_03" width="10%" align="left"><mmr:message messageId="label.weight.unit"/>:</td>
	   	<td class="srchshipmnt_text_03" width="20%" align="left">
	   		<s:select value="%{pickup.weightUnit}" name="pickup.weightUnit" list="{'LBS','KGS'}" cssStyle="width: 158px;" cssClass="text_01" ></s:select>
	   	</td>
	</tr>	
	<tr>
		<td class="srchshipmnt_text_03"><mmr:message messageId="label.pickup.instructions"/>:</td>
		<td colspan="3">
			<s:textfield cssStyle="background-color: #F2F2F2; border-radius:4px; border: 1px solid; width:362px; height:22px;" rows="1" key="pickup.instructions" name="pickup.instructions"  cssClass="text_02"/>
		</td>
	</tr>
	</table>