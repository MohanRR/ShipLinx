<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>


<html>
<head>
<title>Insert title here</title>
</head>
<body> 
<SCRIPT language="JavaScript">
 function submitinform()
 {
	//alert(document.getElementById("comment_area").value);
	//alert(document.getElementById("private").checked);
	//alert(document.viewform.orderId.value);
	var comment = document.getElementById("comment_area").value;
	comment = comment.replace(/(\r\n|\n|\r)/gm," ");
	var private= document.getElementById("private").checked;
	var orderId = document.viewform.orderId.value;
	var statusid = document.getElementById("status_id").value;
	if(statusid == -1)
	{
		alert("Please Select a Status to update")
	}
	else if(!comment.length>0)
	{
		alert("Please enter Comment");
	}	
	else
	{
		ajax_Country=ajaxFunction();
		ajax_Country.onreadystatechange=function()
		{
		   if(ajax_Country.readyState==4)
			{
				reponse=ajax_Country.responseText;
				js_stateid=document.getElementById("comment_table");
				js_stateid.innerHTML= reponse;
			}
		}
		url="add.comment.info.action?comment="+comment+"&pvt="+private+"&order_id="+orderId+"&statusId="+statusid;
		ajax_Country.open("GET",url,true);
		ajax_Country.send(this);		
	}
 }
 
 function deleteComment(id)
 {
 	var orderId = document.viewform.orderId.value;
 	 ajax_Country=ajaxFunction();
				ajax_Country.onreadystatechange=function()
				  {
					   if(ajax_Country.readyState==4)
						{
						reponse=ajax_Country.responseText;
						js_stateid=document.getElementById("comment_table");
						js_stateid.innerHTML= reponse;
						}
				  }
				  	url="deleteComment.action?commentId="+id+"&oid="+orderId;
					ajax_Country.open("GET",url,true);
					ajax_Country.send(this);
 }
 
 function updateOrder(oid)
 {
 	var orderId = document.viewform.orderId.value;
 	alert(orderId);
 }
		
</SCRIPT>

<s:if test="%{#session.ROLE.contains('busadmin')}">
	<div id="add_comment_panel">
	<table width="700px">
	<tr>
	<td><div id="add_info_shipping_srch_crtra"><mmr:message messageId="label.shippingOrder.add.comment"/></div></td>
	<td>&nbsp;</td>
	<td><font color="#000066" size="3" style="font-family: Arial; font-variant: small-caps; font-weight: bold; ">
			<mmr:message messageId="label.shippingOrderTab.order"/>&nbsp;#&nbsp;<s:property value="%{selectedOrder.id}" />
		</font>
	</td>
	</tr>
	</table>
	
		<div id="add_info_shipping_imgs">
		<table>
		<tr>
		<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
		<td><img src="<s:url value="/mmr/images/update.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
		</table>
		</div>
		
		<div id="add_info_shipping_actions">
		<table>
		<tr>
		<td><a href="javascript: submitinform()"><mmr:message messageId="label.update"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td></td>
		</tr>
		</table>
		</div>
	
		<div id="add_info_shipping_table">
		<table>
		<tr>
		<td><font style="color: #000066; font-family: Arial; font-size: 13px; font-weight: bold;"><mmr:message messageId="label.status"/>&nbsp;: </font></td>
				<td><s:select cssClass="text_01_combo_biggest" cssStyle="width:215px;" listKey="id" listValue="name" name="selectedOrder.statusId" list="#session.orderStatusOptionsList" headerKey="-1"  id="status_id" theme="simple"/></td>
				<td>&nbsp;</td>
				<td align="left">&nbsp;</td>
		</tr>
		<tr>
		<td class="hdr" valign="top" style="color: #000066; font-family: Arial; font-size: 13px; font-weight: bold;"><mmr:message messageId="label.feedback.comment"/>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td class="hdr"><s:textarea cols="50" rows="2" id="comment_area"></s:textarea> </td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td class="hdr" valign="top"><s:checkbox name="" key="" id="private"/>&nbsp;<mmr:message messageId="label.mark.private"/></td>
		</tr>
		</table>
		</div>
		
	</div>
</s:if>

	<s:include value="add_info_shipping_inner.jsp"></s:include>
   
</body>
</html>