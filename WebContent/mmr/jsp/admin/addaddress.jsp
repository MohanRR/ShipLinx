<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<html> 
<head> 

    <title><s:text name="address.form.title"/></title> 
</head> 

<body>

<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/countryProvince.js"></script>

<SCRIPT language="JavaScript">
	function submitform()
	{
	 document.addressform.action = "createAddress.action";
	 document.addressform.submit();
	}
</SCRIPT>

<div class="form-container"> 
  <div class="error"><s:fielderror /></div> 
    <s:form action="createAddress" name="addressform" theme="simple">
    <s:hidden name="addressid" value="%{address.addressId}" />
    
<div id="add_addrss_hdr">
<s:if test="#session.edit != 'true'">
<mmr:message messageId="label.shippingOrder.addAddress" />
</s:if>
<s:else>
<mmr:message messageId="label.btn.edit" />&nbsp;&nbsp;<mmr:message messageId="label.address.line" />
</s:else>
</div>
<div id="add_addrss_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;<img border="0" src="<%=request.getContextPath()%>/mmr/images/reset_icon.png" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="add_addrss_actions">
<table>
<tr>
<td><a href="javascript:submitform()"><mmr:message messageId="label.btn.save"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></td>
<s:if test="#session.edit != 'true'">
<td><a href="new.address.action"><mmr:message messageId="label.btn.reset"/></a></td>
</s:if>
<s:else>
<td><a href="addressbook.list.req.action"><mmr:message messageId="label.btn.cancel"/></a></td>
</s:else>
</tr>
</table>
</div>

	<div id="add_addrss_bttm_tbl" class="text_01">
		<table width="800px" cellpadding="2" cellspacing="0">
			<tr>
				<td width="180">&nbsp;</td>
				<td width="180">&nbsp;</td>
				<td width="50">&nbsp;</td>
				<td width="180">&nbsp;</td>
				<td width="180">&nbsp;</td>
				<td width="10">&nbsp;</td>
			</tr>
			<tr>
				<td class="text_03"><mmr:message messageId="label.addressbook.consigneeName" />:</td>
				<td><s:textfield size="24" key="addressbook.consigneeName" name="address.abbreviationName" cssClass="text_02_tf" /></td>
				<td>&nbsp;</td>
				<td class="text_03"><mmr:message messageId="label.addressbook.address1"/>:</td>
				<td><s:textfield size="24" key="addressbook.address1" name="address.address1" cssClass="text_02_tf"/></td>				
			</tr>
			<tr>
				<td class="text_03"><mmr:message messageId="label.addressbook.address2"/>:</td>
				<td><s:textfield size="24" key="addressbook.address2" name="address.address2" cssClass="text_02_tf"/></td>
				<td>&nbsp;</td>
				<td class="text_03"><mmr:message messageId="label.addressbook.postalCode"/>:</td>
				<td>
				<s:textfield size="24" id="addPostalCode" key="addressbook.postalCode" name="address.postalCode" cssClass="text_02_tf" onblur="javascript:getAddressSuggestNewAddress();"/>
				 <img id="loading-img-add" style="display:none;" src="<s:url value="/mmr/images/loading.gif" includeContext="true" />" border="0">
				</td>				
			</tr>
			<tr>
				<td class="text_03"><mmr:message messageId="label.addressbook.city"/>:</td>
				<td><s:textfield size="24" key="addressbook.city" name="address.city" cssClass="text_02_tf"/></td>
				<td>&nbsp;</td>
				<td class="text_03"><mmr:message messageId="label.addressbook.countryName"/>:</td>
				<td><s:select cssClass="text_01" cssStyle="width:160px;" listKey="countryCode" listValue="countryName" name="address.countryCode" headerKey="-1" list="#session.CountryList" onchange="javascript:showState();"  id="firstBox" theme="simple"/></td>				
			</tr>
			<tr>
				<td class="text_03"><mmr:message messageId="label.addressbook.province"/>:</td>
				<td id="stateid"><s:select cssClass="text_01" cssStyle="width:160px;" listKey="provinceCode" listValue="provinceName" name="address.provinceCode" headerKey="1" list="#session.provinces" id="secondBox"  theme="simple"  cssClass="text_01"/></td>
				<td>&nbsp;</td>
				<td class="text_03"><mmr:message messageId="label.addressbook.phone"/>:</td>
				<td><s:textfield size="24" key="addressbook.phone" name="address.phoneNo" cssClass="text_02_tf" /></td>				
			</tr>
			<tr>
				<td class="text_03"><mmr:message messageId="label.addressbook.contactName"/>:</td>
				<td><s:textfield size="24" key="addressbook.contactName" name="address.contactName" cssClass="text_02_tf" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			<tr>	
				<td class="text_03"><mmr:message messageId="label.addressbook.contactEmail"/>:</td>
				<td><s:textfield size="24" key="addressbook.contactEmail" name="address.emailAddress" cssClass="text_02_tf" /></td>
				<td>&nbsp;</td>			
				<td class="text_03"><mmr:message messageId="label.send.notification"/>:</td>			
				<td><s:checkbox key="addressbook.sendNotification" name="address.sendNotification" cssClass="text_02" /></td>			
			</tr>
			<tr>	
				<td class="text_03"><mmr:message messageId="label.addressbook.residential"/> :</td>
				<td><s:checkbox key="addressbook.residential" name="address.residential" cssClass="text_02" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>			
				<td>&nbsp;</td>			
			</tr>
			<tr>	
				<td class="text_03"><mmr:message messageId="label.addressbook.defaultFromAddress"/>:</td>
				<td><s:checkbox key="addressbook.defaultFromAddress" value="%{address.defaultFromAddress}" name="address.defaultFromAddress" cssClass="text_02" /></td>
				<td>&nbsp;</td>
				<td class="text_03"><mmr:message messageId="label.addressbook.defaultToAddress"/> :</td>
				<td><s:checkbox key="addressbook.defaultToAddress" name="address.defaultToAddress" value="%{address.defaultToAddress}"  cssClass="text_02" /></td>
			</tr>
			
			
			
			
		</table>	  
	</div>
						
    </s:form> 
    </div>
   </body>
</html>
    
    
 