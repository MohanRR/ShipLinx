<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<!-- Start: Code to handle Export Data -->
<%@ page buffer = "16kb" %>
<!-- End: Code to handle Export Data -->


<mx:Application xmlns:mx="http://www.adobe.com/2006/mxml">

<html> 
<head> 
    <title><s:text name="customer.search.title"/></title> 
</head> 
<body> 
<SCRIPT language="JavaScript">
	function submitform()
	{
	 document.searchform.action = "searchcustomer.action";
	 document.searchform.submit();
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
	
</SCRIPT>

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>

<div class="form-container"> 
<s:form action="searchCustomer" name="searchform">
<div id="srch_panel">
<table>
<tr>
<td><div id="srch_crtra"><mmr:message messageId="label.search.criteria"/></div></td>
<td><div id="filter"><mmr:message messageId="label.search.filter.customer"/></div></td>
</tr>
</table>
</div>

<div id="srchcustactions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/reset_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<!--  <td><img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>-->  
</tr>
</table>
</div>
<div id="srchcust_actions">
<table>
<tr>
<td><a href="javascript: submitform()"><mmr:message messageId="label.search.btn.search"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: addNewSearch()"><mmr:message messageId="label.search.addnew"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: resetform()"><mmr:message messageId="label.btn.reset"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<!--   <td><a href="#"><mmr:message messageId="label.btn.save"/></a>&nbsp;&nbsp;&nbsp;</td>-->
</tr>
</table>
</div>

	<div id="srchcust_table">
		<table>
			<tr>
				<td class="hdr"><mmr:message messageId="label.customer.name"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="hdr"><mmr:message messageId="label.customer.search.accountNumber"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="hdr"><mmr:message messageId="label.address.line"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<td><s:textfield size="24" key="searchcustomer.name" name="customer.name" cssClass="text_02_tf"/> </td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><s:textfield size="24" key="searchcustomer.accountNumber" name="customer.accountNumber" cssClass="text_02_tf"/></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td><s:textfield size="24" key="label.address.line" name="customer.addressSearchString" cssClass="text_02_tf"/></td>
			</tr>
		</table>
	</div>


	
	<div id="tab"><br/></div>
	<div id="res"><mmr:message messageId="label.search.results"/></div>
	<div id="results">	
	<s:if test="%{customerList.size()>1}">
	<div id="rslt_stmnt"><br/><s:property value="customerList.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{customerList.size()==1}">
	<div id="rslt_stmnt"><br/><s:property value="customerList.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>
	
<div id="result_tbl">		
		<display:table id="customer" name="customerList" pagesize="100" export="true" requestURI="searchcustomer.action" cellspacing="0" cellpadding="4" class="srch_tbl">
		    <display:column headerClass="tableTitle2" title="" > 
  			  <a href="searchMarkup.action?method=init&customerId=${customer.id}&customerBusName=${customer.name}"><img src="<s:url value="/mmr/images/truck.png" includeContext="true" />" alt="Markups" title="Markups" border="0"> </a>		
            </display:column>
            <display:column headerClass="tableTitle2" title=""  > 
	            <a href="getcustomerinfo.action?method=update&id=${customer.id}"> 
				<img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Customer" title="Edit Customer" border="0"> </a>
	   		</display:column>
	   		<display:column headerClass="tableTitle2" title=""  >
	   		  <a href="searchuser.action?cid=${customer.id}"> 
				<img src="<s:url value="/mmr/images/user_group.png" includeContext="true" />" alt="Users" title="Users" border="0"> </a>
	   		</display:column>
            <display:column headerClass="tableTitle2" title=""  > 
			  	<a href="getCustomerAccountInfo.action?&id=${customer.id}">
				<img src="<s:url value="/mmr/images/account_detail.png" includeContext="true" />" alt="Carrier Setup" title="Carrier Setup" border="0"> </a>
            </display:column>
             <display:column headerClass="tableTitle2" title=""  > 
			  	<a href="javascript: emailNotify(${customer.id})">
				<img src="<s:url value="/mmr/images/mail-notification.png" includeContext="true" />" alt="Send access info to customer" title="Send access info to customer" border="0"> </a>
            </display:column>
            <display:column headerClass="tableTitle2" title=""  > 
	            <a href="logInAs.action?id=${customer.id}"> 
				<img src="<s:url value="/mmr/images/red_arrow.gif" includeContext="true" />" alt="Log In As" title="Log In As" border="0"></a>
	   		</display:column>
			<display:column headerClass="tableTitle2_name" property="name" title="Name"/>
			<display:column headerClass="tableTitle2_account" property="accountNumber" title="Account #"/>
			<display:column headerClass="tableTitle2_address" maxLength="25" property="address.longAddress2" title="Address"/>
			<display:column headerClass="tableTitle2_account" property="address.phoneNo" title="Phone#"/>
			<display:column headerClass="tableTitle2_email" maxLength="20" property="address.emailAddress" title="Email"/>	
			<display:column headerClass="tableTitle2" title="Status" property="active">
			</display:column>		
		</display:table>
	
	
	
</div>
<div id="res_tbl_end"></div>


   
</s:form>
</div>
		


