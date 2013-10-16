<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 




<!--  <div id="pckg_btns">
	<table style="margin-top:-3px;">
	<tr>
		<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0" style="margin-bottom:-4px;">&nbsp;</td>
		<td>
			<s:set name="packageTypeId" value="shippingOrder.packageTypeId.packageTypeId"/>
			<s:submit type="image" src="%{#session.ContextPath}/mmr/images/rate_list.png" onClick="return (validateOrder(3,1))" cssStyle="margin-bottom:-4px;"/> 
                  &nbsp;<a><mmr:message messageId="menu.getRates"/></a>
		</td>
		<td>&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0"  style="margin-bottom:-4px;">&nbsp;</td>
		<td><s:a href="javascript: showPackage('addressFrom');" class="prevTab"> <img src="<%=request.getContextPath()%>/mmr/images/back.png" border="0"  style="margin-bottom:-4px;"/>
                  &nbsp;<mmr:message messageId="label.navigation.back" /> </s:a></td>
	</tr>
</table>
</div>-->
<div id="pckg_panel_hdr">
<table>
<tr><td valign="middle" align="left" class="fromAdd_header_table" colspan="9" height="30px">&nbsp;&nbsp;&nbsp;<mmr:message messageId="label.shippingOrder.additionalServices"/>:</tr>
</table>
</div>
<div id="pckg_panel">	
	<table width="1095px" cellpadding="2" cellspacing="0">
	<tr>
		<td width="30px">&nbsp;</td>
		<td class="text_03" width="155px">Type:</td>
		<td width="195px">
			<s:select cssClass="text_01_combo_big" cssStyle="width:156px;" listKey="type" listValue="name" id="packType" name="shippingOrder.packageTypeId.type" 
			list="#session.listPackages" onchange="javascript:modifyQuantity();" headerKey="-1" theme="simple"/>
		</td>
		<td class="text_03" width="190px"><mmr:message messageId="label.shippingOrder.additionalServices.quantity"/>:</td>
		<td width="200px"><s:select cssClass="text_01_combo_big" name="shippingOrder.quantity" id="quantity" onchange="modifyQuantity()" cssStyle="width: 156px;" list="{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35}"  cssClass="text_01_combo_big"></s:select></td>
		<td class="text_03" width="165px"><mmr:message messageId="label.track.reference.code"/>:</td>
		<td width="200px"><s:textfield size="20" key="shippingOrder.refCode" name="shippingOrder.referenceCode" cssClass="text_02_tf" value="%{shippingOrder.referenceCode}"/></td>
	</tr>
	<tr>
		<td width="30px">&nbsp;</td>
			<td class="text_03" width="95px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.scheduledShipDate"/></td>
			<td width="140px" align="left"><s:textfield name="shippingOrder.scheduledShipDate_web" id="f_date_c" size="10" cssStyle="width: 87px;"
				cssClass="text_02_tf" readonly="true" value="%{#session.shippingOrder.scheduledShipDate_web}"/>
				<img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif" id="f_trigger_c" style="cursor: pointer;"	title="Date selector" border="0" onClick="selectDate('f_date_c','f_trigger_c');">
			</td>
			<s:set name="cName" value="%{#session.shippingOrder.fromAddress.countryName}"/>
			<s:if test ='%{#cName ==  "CA"}'>
				<td class="text_03" width="130px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.signatureRequiredCanada"/></td>
				<td width="130px" align="left"><s:select value="%{#session.shippingOrder.signatureRequired}" name="shippingOrder.signatureRequired" list="#{'1':'No','2':'Yes'}" cssStyle="width: 156px;" cssClass="text_01_combo_big" ></s:select></td>
			</s:if>
			<s:else>
				<td class="text_03" width="130px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.signatureRequired"/></td>
				<td  width="90px" align="left"><s:select value="%{#session.shippingOrder.signatureRequired}" name="shippingOrder.signatureRequired" cssStyle="width: 156px;" list="#{'1':'No','2':'Del. Confirmation','3':'Signature','4':'Adult Signature'}" cssClass="text_01_combo_big" ></s:select></td>
			</s:else>
			<td class="text_03" width="130px" align="left"><mmr:message messageId="label.shippingOrder.dutiable.amount"/></td>
			<td width="75px" align="left"> <s:textfield id="shippingOrder.dutiableAmount" name="shippingOrder.dutiableAmount"  cssClass="text_02_tf_small" theme="simple" size="3" value="%{#session.shippingOrder.dutiableAmount}"/></td>
		</tr>
		<tr>
			<td width="30px">&nbsp;</td>	
			<td class="text_03" width="105px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.holdForPickupRequired"/></td>
			<td width="60px" align="left"><s:checkbox name="shippingOrder.holdForPickupRequired" value="%{#session.shippingOrder.holdForPickupRequired}" /></td>
			<td class="text_03" width="130px" align="left"><mmr:message messageId="label.shippingOrder.satDelivery"/></td>
			<td width="60px" align="left"><s:checkbox name="shippingOrder.satDelivery"  value="shippingOrder.satDelivery"/></td>
			<td class="text_03" width="135px"><mmr:message messageId="label.shippingOrder.docsOnly"/>:</td>
			<td width="65px"><s:checkbox name="shippingOrder.docsOnly"  value="shippingOrder.docsOnly"/></td>			
		</tr>
		
		<tr>
			<td width="30px">&nbsp;</td>	
			<td class="text_03"><mmr:message messageId="label.shippingOrder.dangerousGoods"/>:</td>
	        <td><s:select id="dg_field" name="shippingOrder.dangerousGoods" cssStyle="width: 140px;" cssClass="text_01_combo_big" list="#session.DGList" listKey="dangerousgoodsId" listValue="dangerousGoodsName" disabled="false"/></td>
		</tr>
		<tr id="hide_this_one">
			<td width="30px">&nbsp;</td>	
			<td class="text_03" width="155px"><mmr:message messageId="label.shippingOrder.additionalServices.tradeShowPickup"/></td>
			<td width="195px" align="left"> <s:checkbox name="shippingOrder.tradeShowPickup" value="%{#session.shippingOrder.tradeShowPickup}" /></td>
			<td class="text_03" width="190px"><mmr:message messageId="label.shippingOrder.additionalServices.tradeShowDelivery"/></td>
			<td width="200px" align="left"><s:checkbox name="shippingOrder.tradeShowDelivery"  value="shippingOrder.tradeShowDelivery"/></td>
			<td class="text_03" width="165px"><mmr:message messageId="label.shippingOrder.additionalServices.insidePickup"/>:</td>
			<td width="200px"><s:checkbox name="shippingOrder.insidePickup"  value="shippingOrder.insidePickup"/></td>
		</tr>
		<tr id="hide_this_two">
			<td width="30px">&nbsp;</td>	
			<td class="text_03" width="155px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.appointmentPickup"/></td>
			<td width="195px" align="left"> <s:checkbox name="shippingOrder.appointmentPickup" value="%{#session.shippingOrder.appointmentPickup}" /></td>
			<td class="text_03" width="190px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.appointmentDelivery"/></td>
			<td width="200px" align="left"><s:checkbox name="shippingOrder.appointmentDelivery"  value="shippingOrder.appointmentDelivery"/></td>
			<td class="text_03" width="165px"><mmr:message messageId="label.shippingOrder.additionalServices.fromTailgate"/>:</td>
			<td width="200px"><s:checkbox name="shippingOrder.fromTailgate"  value="shippingOrder.fromTailgate"/></td>
		</tr>
		<tr id="hide_this_three">
			<td width="30px">&nbsp;</td>	
			<td class="text_03" width="155px" align="left"><mmr:message messageId="label.shippingOrder.additionalServices.toTailgate"/></td>
			<td width="195px" align="left"> <s:checkbox name="shippingOrder.toTailgate" value="%{#session.shippingOrder.toTailgate}" /></td>
			<td class="text_03" width="190px" align="left">&nbsp;</td>
			<td width="200px" align="left">&nbsp;</td>
			<td class="text_03" width="165px">&nbsp;</td>
			<td width="200px">&nbsp;</td>
		</tr>
	</table>
	
</div>
<!--   <div id="pckg_tab"><br/></div>-->



