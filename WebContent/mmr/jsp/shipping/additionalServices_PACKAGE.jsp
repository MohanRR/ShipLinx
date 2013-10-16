<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<div id="additionalServices">
<div id="bottom_table2_new2" class="text_01">
 <table border="0" cellspacing="0" cellpadding="2" style="padding-right:15px;padding-left:15px;" >
    <tr>
		<td  width ="700" colspan="8" style="padding-top: 7px;" class="shipto">
		<strong><mmr:message messageId="label.shippingOrder.additionalServices"/></strong>
		</td>
	</tr>
	<tr>
		<td colspan="8">&nbsp;</td>
	</tr>
    <tr>
	<td valign="top"  width ="350" colspan="4"  style="padding-right:15px;" ><!--  left side -->
		<table border="0" cellpadding="3" cellspacing="0">
			<tr>
				<td valign="middle" nowrap="nowrap"  class="text_03"><mmr:message messageId="label.shippingOrder.additionalServices.quantity"/></td>
				<td>
				<s:select name="shippingOrder.quantity" id="quantity" onchange="modifyQuantity()" cssStyle="width: 120px;" list="{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35}"  cssClass="text_01"></s:select>
				</td>
			</tr>
			<tr>
				<td valign="middle" class="text_03"><mmr:message messageId="label.shippingOrder.dangerousGoods"/></td>
				<td valign="middle">
					<s:select name="shippingOrder.dangerousGoods"
					style="width: 120px;"  cssClass="text_01" list="{'None','Limited Quantity','500 Kg Exemption','Fully Regulated'}"/>
				</td>
			</tr>
			<tr>
				<s:set name="cName" value="%{#session.shippingOrder.fromAddress.countryName}"/>
				<s:if test ='%{#cName ==  "CA"}'>
					<td colspan="1" valign="middle" class="text_03"><mmr:message messageId="label.shippingOrder.additionalServices.signatureRequiredCanada"/></td>
					<td valign="middle">
						<s:select value="%{#session.shippingOrder.signatureRequired}" name="shippingOrder.signatureRequired" list="#{'1':'No','3':'Yes','4':'Adult'}" cssStyle="width: 120px;" cssClass="text_01" ></s:select>
					</td>
				</s:if>
				<s:else>
					<td colspan="1" valign="middle" class="text_03"><mmr:message messageId="label.shippingOrder.additionalServices.signatureRequired"/></td>
					<td valign="middle">
						<s:select value="%{#session.shippingOrder.signatureRequired}" name="shippingOrder.signatureRequired" list="#{'1':'No','2':'Indirect','3':'Direct','4':'Adult'}" cssStyle="width: 120px;" cssClass="text_01" ></s:select>
					</td>   
				</s:else>
			</tr>
		</table>
		</td>
		<td valign="top"  width ="350" colspan="4"  style="padding-left:15px;" >
		<table border="0" cellpadding="3" cellspacing="0" class="text_01">
			<tr>

				<td valign="middle" class="text_03"><mmr:message messageId="label.shippingOrder.satDelivery"/></td>
				<td valign="middle" align="left">
				<s:checkbox name="shippingOrder.satDelivery"  value="%{#session.shippingOrder.satDelivery}"/>
				</td>
			</tr>
			<tr>
				<td valign="middle"  class="text_03"><mmr:message messageId="label.shippingOrder.holdForPickup"/></td>
				<td valign="middle" align="left">
				<s:checkbox name="shippingOrder.holdForPickupRequired" value="%{#session.shippingOrder.holdForPickupRequired}" /></td>
				</tr>
					<tr>
					
				<td valign="middle"  class="text_03"><mmr:message messageId="label.shippingOrder.additionalServices.scheduledShipDate"/>
				</td>
					<td valign="middle">
					<table border="0" cellpadding="0" cellspacing="0" class="text_02">
						<tbody>
							<tr>
								<td valign="middle">
								<s:textfield name="shippingOrder.scheduledShipDate" id="f_date_c" size="10"
									cssClass="text_02" readonly="readonly" cssStyle="width: 120px;" value="%{#session.shippingOrder.scheduledShipDate}"/>
								<td valign="middle"><img src="<%=request.getContextPath()%>/mmr/images/icon_Appt.gif"
									id="f_trigger_c" style="cursor: pointer;"
									title="Date selector" border="0" onClick="selectDate();"> 
								</td>
							</tr>
						</tbody>
					</table>
				</td>
					</tr>
			</table>
			</td>
	</tr>
</table>
</div>
</div>