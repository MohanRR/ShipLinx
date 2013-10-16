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
	var plkey=0;
	var custkey=0; 
	var primarylockey = 0;
	var reqprimarylocId = 0;
	var reqplkey = 0;
	var reqcustkey = 0;
	
	window.onload=init;
	
	function init() 
	{
		reqprimarylocId = "<%=request.getAttribute("primarylocId") %>";
		reqplkey = "<%=request.getAttribute("plnameid") %>";
		reqcustkey = "<%=request.getAttribute("cnameid") %>";
	}	
	
	function submitform()
	{
		var bsubmit=true;
		var autoCompleter = dojo.widget.byId("customerName");
		//var autoCompleter2 = dojo.widget.byId("prodlineSelected");
		//var primaryloc_ac = dojo.widget.byId("primarylocationNames");
		
		if(document.getElementById("pname").value=="")
			{
				alert("Please enter Product Name");
				bsubmit=false;
			}
			else if(document.getElementById("pdesc").value=="")
			{
				alert("Please enter Product Description");
				bsubmit=false;
			}
			else if(document.getElementById("phcode").value=="")
			{
				alert("Please enter Product Harmonized Code");
				bsubmit=false;
			}
			else if(document.getElementById("pcode").value=="")
			{
				alert("Please enter Product Code");
				bsubmit=false;
			}
			//else if(autoCompleter2!=undefined)
			//{
				//var value2 = autoCompleter2.getSelectedValue();
				//if (value2 == null || value2 == "") 
				//{
					//alert("Please select a Product Line");	
					//bsubmit=false;		
				//}
			//}
			//else if(primaryloc_ac!=undefined)	//BusinessAdmin Role
			//{
				//var primaryvalue = primaryloc_ac.getSelectedValue();
			
				//if (primaryvalue == null || primaryvalue == "") 
				//{
					//alert("Please select a Primary Location");	
					//bsubmit=false;		
				//}
			//}
		if(bsubmit)
		{	
		//busadmin role
			//if(reqprimarylocId=='null' && reqplkey=='null' && reqcustkey=='null' && primarylockey>0 && plkey>0 && custkey>0)
			//{
	 			document.searchform.action = "updateProduct.action?cid="+custkey+"&plid="+plkey+"&primaryloc="+primarylockey;
	 			document.searchform.submit();
	 		//}
	 		//else if(reqprimarylocId!='null' && reqplkey!='null' && reqcustkey!='null' && primarylockey==0 && plkey==0 && custkey==0)
	 		//{
	 			//alert("case 2");
	 			//document.searchform.action = "updateProduct.action?cid="+reqcustkey+"&plid="+reqplkey+"&primaryloc="+reqprimarylocId;
	 			//document.searchform.submit();
	 		//}
	 		//else if(reqprimarylocId!='null' && reqplkey!='null' && reqcustkey!='null' && primarylockey>0 && plkey==0 && custkey==0)
	 		//{
	 			//alert("case 3");
	 			//document.searchform.action = "updateProduct.action?cid="+reqcustkey+"&plid="+reqplkey+"&primaryloc="+primarylockey;
	 			//document.searchform.submit();
	 		//}
	 		//else if(reqprimarylocId!='null' && reqplkey!='null' && reqcustkey!='null' && primarylockey==0 && plkey>0 && custkey==0)
	 		//{
	 			//alert("case 4");
	 			//document.searchform.action = "updateProduct.action?cid="+reqcustkey+"&plid="+plkey+"&primaryloc="+reqprimarylocId;
	 			//document.searchform.submit();
	 		//}
	 		//customer admin role
	 		//else if(reqplkey=='null' && plkey>0)
			//{
				//alert("case 5");
	 			//document.searchform.action = "updateProduct.action?cid="+custkey+"&plid="+plkey+"&primaryloc="+primarylockey;
	 			//document.searchform.submit();
	 		//}
	 		//else if(reqplkey!='null' && plkey==0)
	 		//{
	 			//alert("case 6");
	 			//document.searchform.action = "updateProduct.action?cid="+reqcustkey+"&plid="+reqplkey+"&primaryloc="+reqprimarylocId;
	 			//document.searchform.submit();
	 		//}
	 	}
	}
	
	function getAccountInformation(url){
		window.open(url,'','width=760,height=540,left=100,top=100,scrollbars=1');
	}
	function resetform() {
		document.searchform.action = "searchcustomer.action?method=reset";
	 	document.searchform.submit();
	}	
	
	function addNewSearch()
	{
		document.searchform.action = "admin/addcustomer.action?";
	 	document.searchform.submit();
	}
	
	function emailNotify(custid)
	{
		if(confirm("Would you like to send account notification/message to customer?")) {
			 document.searchform.action = "send.customer.notification.action?&id="+custid;
			 document.searchform.submit();
		}
	}
	
	dojo.event.topic.subscribe("/value_cust", function(value, key, text, widget){
		custkey = key;
		});
		
	dojo.event.topic.subscribe("/value_prodl", function(value, key, text, widget){
		plkey = key;			
		});
		
	dojo.event.topic.subscribe("/value_primary", function(value, key, text, widget){
		primarylockey = key;
		});
	
	
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
<s:form action="searchCustomer" name="searchform">
<div id="add_srch_panel">
<table>
<tr>
<td><div id="srch_crtra">
<s:if test="#session.edit != 'true'">
<mmr:message messageId="menu.add.product"/>
</s:if>
<s:else>
<mmr:message messageId="label.edit.product"/>
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
<td><a href="javascript: submitform()"><mmr:message messageId="label.btn.save"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>
 <s:if test="%{#session.role=='busadmin'}">
	 <a href="goToManageProducts.action"><mmr:message messageId="label.btn.cancel"/></a>&nbsp;&nbsp;&nbsp;
 </s:if>
 <s:else>
	 <a href="search.products.action"><mmr:message messageId="label.btn.cancel"/></a>&nbsp;&nbsp;&nbsp;	   
 </s:else>
</td>
</tr>
</table>
</div>

<div id="addproduct_table">
<table width="960px" cellspacing="1" cellpadding="2">
<tr>
<td class="hdr_product"><mmr:message messageId="label.product.name"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield id="pname" size="24" key="searchproduct.name" name="products.productName" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.product.description"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield id="pdesc" size="24" key="searchproduct.desc" name="products.productDescription" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.product.hcode"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield id="phcode" size="24" key="searchproduct.hcode" name="products.productHarmonizedCode" cssClass="text_02_tf"/></td>
</tr>
<tr>
<td class="hdr_product"><mmr:message messageId="label.product.code"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><s:textfield id="pcode" size="24" key="searchproduct.code" name="products.productCode" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.product.price"/> </td>
<td><s:textfield size="24" key="searchproduct.hcode" name="products.unitPrice" cssClass="text_02_tf"/></td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td class="hdr_product"><mmr:message messageId="label.country.origin"/></td>
<td><s:select id="new_productOrigin" cssClass="text_01_combo_big" cssStyle="width:158px;" listKey="countryCode" listValue="countryName" name="products.originCountry" list="#session.CountryList" headerKey="-1"  theme="simple"/></td>
<td>&nbsp;&nbsp;&nbsp;</td>
<td></td>
</tr>
<!--<tr>
<td class="hdr_product"><mmr:message messageId="label.product.line"/></td>
<td>
<s:if test="#session.edit == 'true'">
<s:url id="prodlineList" action="listProductLines" />
<sx:autocompleter id="prodlineSelected" name="searchString" href="%{prodlineList}" dataFieldName="productLineSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="false" valueNotifyTopics="/value_prodl" cssStyle="height:20px; width: 150px;" preload="true" value="%{#request.plname}"/>
</s:if>
<s:else>
<s:url id="prodlineList" action="listProductLines" />
<sx:autocompleter id="prodlineSelected" name="searchString" href="%{prodlineList}" dataFieldName="productLineSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="false" valueNotifyTopics="/value_prodl" cssStyle="height:20px; width: 150px;" preload="true"/>
</s:else>
</td>
<td></td>
<td class="hdr_product">
<s:if test="%{#session.role=='busadmin' }">
<mmr:message messageId="label.primary.location"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</s:if>
</td>
<td>
<s:if test="%{#session.role=='busadmin'}">
	<s:if test="#session.edit != 'true'">
		<s:url id="locationsList" action="listLocations" />
		<sx:autocompleter id="primarylocationNames" name="searchString" href="%{locationsList}" dataFieldName="locationsSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="false" valueNotifyTopics="/value_primary" cssStyle="width:160px;" preload="true"/>
	</s:if>
	<s:else>
		<s:url id="locationsList" action="listLocations" />
		<sx:autocompleter id="primarylocationNames" name="searchString" href="%{locationsList}" dataFieldName="locationsSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="false" valueNotifyTopics="/value_primary" cssStyle="width:160px;" preload="true" value="%{#request.primaryloc}"/>
	</s:else>
</s:if>
<s:else>
&nbsp;
</s:else>
</td>
<td></td>
<td class="hdr_product">
<s:if test="%{#session.role=='busadmin'}">
<s:if test="#session.edit != 'true'">
<mmr:message messageId="label.account.customer"/>
</s:if>
</s:if>
<s:else>
&nbsp;
</s:else>
</td>
<td>
<s:if test="%{#session.role=='busadmin'}">
		<s:if test="#session.edit != 'true'">
		<s:url id="customerList" action="listCustomers" />
		<sx:autocompleter id="customerName" name="searchString" href="%{customerList}" dataFieldName="customerSearchResult" loadOnTextChange="true" loadMinimumCount="1" autoComplete="false"  valueNotifyTopics="/value_cust" cssStyle="width:205px;" preload="true"/>
		</s:if>
</s:if>
</td>
<td></td>
<td></td>
</tr>
--></table>
</div>

  
</s:form>
</div>
</body>
</html>	


