<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>

<html> 
<head> 
	<sx:head/>
    <title><s:text name="customer.search.title"/></title> 
</head> 
<body> 
<SCRIPT language="JavaScript">

var vkey = "";
	function submitform()
	{
		var autoCompleter = dojo.widget.byId("customerName");
		if(autoCompleter!=undefined)
		{
			var value = autoCompleter.getSelectedValue();
			var key = autoCompleter.getSelectedKey();
			if (value == null || value == "") 
			{
				alert("Please select a Customer");			
			}
			else
			{
				//call some action
				document.searchform.action = "get.products.action?cid="+key;
				document.searchform.submit();
			}
		}
		else
		{
			document.searchform.action = "get.products.action";
			document.searchform.submit();
		}
	}
	
	function addNewProduct()
	{	
		var autoCompleter = dojo.widget.byId("customerName");
		if(autoCompleter!=undefined)
		{
			document.searchform.action = "addnewproduct.action";
			document.searchform.submit();
		}
		else
		{			
			document.searchform.action = "addnewproduct.action";
			document.searchform.submit();		
		}
	}
	function resetform()
	{
		document.searchform.action = "get.products.action?method=reset";
		document.searchform.submit();
	}
		
	
	function addNewProductLine()
	{
		var autoCompleter = dojo.widget.byId("customerName");
		if(autoCompleter!=undefined)
		{
			var value = autoCompleter.getSelectedValue();
			var key = autoCompleter.getSelectedKey();
			if (value == null || value == "") 
			{
				alert("Please select a Customer");			
			}
			else
			{
				//call some action
				document.searchform.action = "goToAddProductLine.action?cid="+key;
				document.searchform.submit();
			}
		}
		else
		{
			document.searchform.action = "goToAddProductLine.action";
			document.searchform.submit();
		}
	}
	
	dojo.event.topic.subscribe("/value_name", function(value, key, text, widget){
		vkey = key;		
		});
		
</SCRIPT>

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="searchCustomer" name="searchform">
<div id="srch_product_panel">
<table>
<tr>
<td><div id="srch_crtra"><mmr:message messageId="menu.search.product"/></div></td>
<td>&nbsp;</td>
</tr>
</table>


<div id="srchproductactions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0"></td>
<td><img src="<s:url value="/mmr/images/reset_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<!--<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>  
--></tr>
</table>
</div>
<div id="srchproduct_actions">
<table>
<tr>
<td><a href="javascript: submitform()"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>
<a href="javascript: addNewProduct()"><mmr:message messageId="label.search.addnew"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: resetform()"><mmr:message messageId="label.btn.reset"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<!--<td><a href="javascript: addNewProductLine()"><mmr:message messageId="label.addnew.product.line"/></a>&nbsp;&nbsp;&nbsp;</td>
--></tr>
</table>
</div>

<div id="srchproduct_table">
<s:if test="%{#session.role=='busadmin'}">
<table>
<tr>
<td class="hdr"><mmr:message messageId="label.account.customer"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td colspan="7">
<s:url id="customerList" action="listCustomers" />
<sx:autocompleter id="customerName" name="searchString" href="%{customerList}" dataFieldName="customerSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="false"  valueNotifyTopics="/value_name" cssStyle="width:250px;" preload="true"/>
</td>
</tr>
<tr>
<td class="hdr"><mmr:message messageId="label.product.name"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.product.description"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.product.hcode"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.product.code"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
<tr>
<td><s:textfield size="24" id="prod_name" key="products.productName" name="products.productName" cssClass="text_02_tf"/> </td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield size="24" id="prod_desc" key="products.productDescription" name="products.productDescription" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield size="24" id="prod_hcode" key="products.productHarmonizedCode" name="products.productHarmonizedCode" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield size="24" id="prod_code" key="products.productCode" name="products.productCode" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</s:if>
<s:else>
<table>
<tr>
<td class="hdr"><mmr:message messageId="label.product.name"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.product.description"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.product.hcode"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.product.code"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
<tr>
<td><s:textfield size="24" id="prod_name" key="products.productName" name="products.productName" cssClass="text_02_tf"/> </td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield size="24" id="prod_desc" key="products.productDescription" name="products.productDescription" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield size="24" id="prod_hcode" key="products.productHarmonizedCode" name="products.productHarmonizedCode" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield size="24" id="prod_code" key="products.productCode" name="products.productCode" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</s:else>

</div>
</div>

<div id="tab"><br/></div>
	<div id="res"><mmr:message messageId="label.search.results"/></div>
	<div id="results">	
	<s:if test="%{productList.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="productList.size()" /><mmr:message messageId="label.search.results.items"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<s:if test="%{#session.role=='busadmin'}">
	<font style="color: #000000; font-family: Arial; font-size: 11px; font-weight: bold;"><mmr:message messageId="label.customer.selected"/>:</font>&nbsp;&nbsp;<s:property value="%{#session.customerName}"/>
	</s:if>
	</div>
	</s:if>
	<s:elseif test="%{productList.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="productList.size()" /><mmr:message messageId="label.search.results.item"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<s:if test="%{#session.role=='busadmin'}">
	<font style="color: #000000; font-family: Arial; font-size: 11px; font-weight: bold;"><mmr:message messageId="label.customer.selected"/>:</font>&nbsp;&nbsp;<s:property value="%{#session.customerName}" />
	</s:if>
	</div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<s:if test="%{#session.role=='busadmin'}">
	<font style="color: #000000; font-family: Arial; font-size: 11px; font-weight: bold;"><mmr:message messageId="label.customer.selected"/>:</font>&nbsp;&nbsp;<s:property value="%{#session.customerName}" />
	</s:if>
	</div>
	</s:else>
	</div>
	
<div id="result_tbl">		
		<display:table id="product" uid="row" name="productList" pagesize="100" export="true" requestURI="get.products.action" cellspacing="0" cellpadding="4" class="srch_tbl">
		<s:hidden name="products.productId" value="%{#attr.row.productId}"/>
		<s:if test="%{#session.role=='busadmin'}">
			<display:column headerClass="tableTitle2" title="" style="text-align:center;" > 
		   		<s:a href="goToProductInventory.action?productId=%{#attr.row.productId}&cid=%{#attr.row.customerId}"> 
				<img src="<s:url value="/mmr/images/inventory.png" includeContext="true" />" alt="Edit Customer" border="0"> </s:a>
	   		</display:column>		
	   	</s:if>
		   <display:column headerClass="tableTitle2" title="" style="text-align:center;" > 
		   <s:if test="%{#session.role=='busadmin'}">
		   		<s:a href="editproduct.action?productId=%{#attr.row.productId}&cid=%{#attr.row.customerId}"> 
				<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Customer" border="0"> </s:a>
		   </s:if>
		   <s:else>
		   	<s:a href="editproduct.action?productId=%{#attr.row.productId}"> 
				<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Customer" border="0"> </s:a>
		   </s:else>
	   		</display:column>
            <display:column headerClass="tableTitle2" title="" style="text-align:center;" > 
			  	<s:a onclick="return confirm('Do you really want to delete the selected Product?')" href="deleteProduct.action?productId=%{#attr.row.productId}">
				<img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Customer Account Info" border="0"> </s:a>
            </display:column>
			<display:column headerClass="tableTitle2_product" property="productName" title="Name" style="text-align:center;"/>
			<display:column headerClass="tableTitle2_product" property="productDescription" title="Description" style="text-align:center;"/>
			<display:column headerClass="tableTitle2_product" property="productHarmonizedCode" title="Harmonized Code" style="text-align:center;"/>
			<display:column headerClass="tableTitle2_product" property="unitPrice" title="Unit Price" style="text-align:center;"/>
			<display:column headerClass="tableTitle2_product" property="originCountry" title="Origin Country" style="text-align:center;"/>
		</display:table>
</div>
<div id="res_tbl_end"></div>




   
</s:form>
</div>
		


