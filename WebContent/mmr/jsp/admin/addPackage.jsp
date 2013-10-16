<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<link rel="stylesheet" type="text/css"
	href="<s:url value='/mmr/styles/common.css' includeContext="true"/>" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/jsp/shipping/style.css">

<head>
<sx:head/>
</head>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jquery.js"></script>

<script language="JavaScript">
	
	function submitform(val)
	{
		var bsubmit=true;
		if(document.getElementById("pgname").value=="")
			{
				alert("Please enter Package Name");
				bsubmit=false;
			}
			else if(document.getElementById("pglen").value=="")
			{
				alert("Please enter Package Length");
				bsubmit=false;
			}
			else if(document.getElementById("phwid").value=="")
			{
				alert("Please enter Package Width");
				bsubmit=false;
			}
			else if(document.getElementById("pghht").value=="")
			{
				alert("Please enter Package Height");
				bsubmit=false;
			}
			else if(document.getElementById("pgwgt").value=="")
			{
				alert("Please enter Package Weight");
				bsubmit=false;
			}
			else if(document.getElementById("pgdes").value=="")
			{
				alert("Please enter Package Description");
				bsubmit=false;
			}
			
		if(bsubmit)
		{	
			if(val=='add')
	 			document.addpackageform.action = "addnewpackage.action";
	 			//alert('add');
	 		else
	 			document.addpackageform.action = "addnewpackage.action?edit='true'";
	 			//alert('edit');
 			
 			document.addpackageform.submit();
	 	}
	}
	
	function getAccountInformation(url){
		window.open(url,'','width=760,height=540,left=100,top=100,scrollbars=1');
	}
	function resetform() {
		document.addpackageform.action = "searchcustomer.action?method=reset";
	 	document.addpackageform.submit();
	}	
	
	function addNewSearch()
	{
		document.addpackageform.action = "admin/addcustomer.action?";
	 	document.addpackageform.submit();
	}
	
	function emailNotify(custid)
	{
		if(confirm("Would you like to send account notification/message to customer?")) {
			 document.addpackageform.action = "send.customer.notification.action?&id="+custid;
			 document.addpackageform.submit();
		}
	}
	
</script>

<html> 
<head> 
 	<title><s:text name="user.form.title"/></title>
</head> 
<body> 
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/countryProvince.js"></script>

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container"> 
<s:form action="searchCustomer" name="addpackageform">
<div id="add_srch_panel">
<table>
<tr>
<td><div id="srch_crtra">
<s:if test="#session.edit != 'true'">
<mmr:message messageId="menu.add.package"/>
</s:if>
<s:else>
<mmr:message messageId="label.edit.package"/>
</s:else>
</div></td>
<td>&nbsp;</td>
</tr>
</table>
</div>

<div id="addproductactions_imgs">
<table>
<tr>
<td>&nbsp;</td>
<td>&nbsp;</td>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/cancel.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>  
</tr>
</table>
</div>
<div id="addproduct_actions">
<table>
<tr>
<td>&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td>
<s:if test="#session.edit != 'true'">
<a href="javascript: submitform('add')"><mmr:message messageId="label.btn.save"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</s:if>
<s:else>
<a href="javascript: submitform('edit')"><mmr:message messageId="label.btn.save"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</s:else>
</td>
<td>
 <s:if test="%{#session.role=='busadmin'}">
	 <a href="goToManageProducts.action"><mmr:message messageId="label.btn.cancel"/></a>&nbsp;&nbsp;&nbsp;
 </s:if>
 <s:else>
	 <a href="search.packages.action"><mmr:message messageId="label.btn.cancel"/></a>&nbsp;&nbsp;&nbsp;	   
 </s:else>
</td>
</tr>
</table>
</div>

<div id="addproduct_table">
<table width="960px" cellspacing="0" cellpadding="2" style="margin-top: 12px;">
<tr>
<td class="hdr_product"><mmr:message messageId="label.package.name"/>:</td>
<td align="left"><s:textfield id="pgname" size="24" name="packageTypes.packageName" cssClass="text_02_tf" value="%{packageTypes.packageName}"/></td>
<td>&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.length.integer"/>:</td>
<td align="left">
<s:if test="%{packageTypes.packageLength > 1}">
	<s:textfield id="pglen" size="24" name="packageTypes.packageLength" cssClass="text_02_tf_small"/>
</s:if>
<s:else>
	<s:textfield id="pglen" size="24" name="packageTypes.packageLength" cssClass="text_02_tf_small" value="1"/>
</s:else>
</td>
<td>&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.width.integer"/>:</td>
<td align="left">
<s:if test="%{packageTypes.packageWidth > 1}">
	<s:textfield id="phwid" size="24" name="packageTypes.packageWidth" cssClass="text_02_tf_small"/>
</s:if>
<s:else>
	<s:textfield id="phwid" size="24" name="packageTypes.packageWidth" cssClass="text_02_tf_small" value="1"/>
</s:else>
</td>
<td>&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.height.integer"/>:</td>
<td align="left">
<s:if test="%{packageTypes.packageHeight > 1}">
	<s:textfield id="pghht" size="24" name="packageTypes.packageHeight" cssClass="text_02_tf_small"/>
</s:if>
<s:else>
	<s:textfield id="pghht" size="24" name="packageTypes.packageHeight" cssClass="text_02_tf_small" value="1"/>
</s:else>
</td>
<td>&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.weight.integer"/>: </td>
<td align="left">
<s:if test="%{packageTypes.packageHeight > 1}">
	<s:textfield id="pgwgt" size="24" name="packageTypes.packageWeight" cssClass="text_02_tf_small"/>
</s:if>
<s:else>
	<s:textfield id="pgwgt" size="24" name="packageTypes.packageWeight" cssClass="text_02_tf_small" value="1"/>
</s:else>
</td>
</tr>
<tr>
	<td class="hdr_product"><mmr:message messageId="label.package.desc"/>:</td>
	<td align="left" colspan="5"><s:textfield id="pgdes" size="24" name="packageTypes.packageDesc" cssClass="text_02_tf_bigger"/></td>
</tr>
</table>
</div>

  
</s:form>
</div>
</body>
</html>	


