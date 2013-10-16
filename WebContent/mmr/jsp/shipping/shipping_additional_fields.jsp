<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
%>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>



	<div id="div_shipping_additional_fields">
	<table style="width:1080px;" border="0" cellspacing="4" cellpadding="2" class="text_01">
		<tr>
			<td width="10%" class="text_03">Zone From:</td>
	        <td width="20%"><s:textfield size="20" key="shippingOrder.fromZone" name="shippingOrder.fromZone" cssClass="text_02_tf" /></td>
			<td width="10%" class="text_03">Zone To:</td>
	        <td width="20%"><s:textfield size="20" key="shippingOrder.toZone" name="shippingOrder.toZone"  cssClass="text_02_tf"/></td>
	        <td width="10%" class="text_03">Currency:</td>
	        <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" value="%{shippingOrder.currency}" id="currency"  name="shippingOrder.currency" list="{'', 'CAD','USD'}" theme="simple" /></td>
	        <td width="10%" class="text_03">Tracking #:</td>
	        <td width="20%"><s:textfield size="20" key="shippingOrder.masterTrackingNum" name="shippingOrder.masterTrackingNum"  cssClass="text_02_tf"/></td>
		</tr>
		<tr>
			<td width="10%" class="text_03">Weight Entered:</td>
		    <td width="20%"><s:textfield size="20" key="shippingOrder.quotedWeight" name="shippingOrder.quotedWeight" cssClass="text_02_tf" /></td>
			<td width="10%" class="text_03">Entered UOM:</td>
		    <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" value="%{shippingOrder.quotedWeightUOM}" id="quotedWeightUOM"  name="shippingOrder.quotedWeightUOM" list="{'', 'LBS','KGS'}" theme="simple" /></td>
			<td width="10%" class="text_03">Weight Billed:</td>
		    <td width="20%"><s:textfield size="20" key="shippingOrder.billedWeight" name="shippingOrder.billedWeight" cssClass="text_02_tf" /></td>
			<td width="10%" class="text_03">Billed UOM:</td>
		    <td width="20%"><s:select cssClass="text_01" cssStyle="width:158px;" value="%{shippingOrder.billedWeightUOM}" id="billedWeightUOM"  name="shippingOrder.billedWeightUOM" list="{'', 'LBS','KGS'}" theme="simple" /></td>
		</tr>
		<tr>		
	        <td width="10%" class="text_03">Reference 1:</td>
	        <td width="20%"><s:textfield size="20" key="shippingOrder.referenceOne" name="shippingOrder.referenceOne"  cssClass="text_02_tf"/></td>        
	        <td width="10%" class="text_03">Reference 2:</td>
	        <td width="20%"><s:textfield size="20" key="shippingOrder.referenceTwo" name="shippingOrder.referenceTwo"  cssClass="text_02_tf"/></td> 
			<td width="10%" class="text_03">Status:</td>
			<td width="20%"> <s:select cssClass="text_01" cssStyle="width:158px;" listKey="id" listValue="name" name="shippingOrder.statusId" list="#session.orderStatusList" headerKey="-1"  id="status_id" theme="simple"/></td>
			<td width="10%">&nbsp;</td>
			<td width="20%">&nbsp;</td>
		</tr>
	</table>
	</div>
