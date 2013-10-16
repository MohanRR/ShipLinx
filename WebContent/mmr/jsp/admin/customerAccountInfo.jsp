<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<head>
<sx:head />
</head>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/jsp/shipping/style.css">
<link rel="stylesheet" type="text/css"
	href="<s:url value='/mmr/styles/common.css' includeContext="true"/>" />
<SCRIPT language="JavaScript">

	function disp(){
		if (document.getElementById('carrierName').value=='UPS') 
		{ 
			//alert("11");
			document.getElementById('UPSBlock').disabled = false; 
			document.getElementById('FEDEXBlock').disabled = true; 
			document.getElementById('UPSBlockTest').disabled = false; 
			document.getElementById('FEDEXBlockTest').disabled = true; 
		} 
		else { 
			//alert("22");
			document.getElementById('UPSBlock').disabled = true; 
			document.getElementById('FEDEXBlock').disabled = false; 
			document.getElementById('UPSBlockTest').disabled = true; 
			document.getElementById('FEDEXBlockTest').disabled = false; 
		}
	}
	function edit(id)
	{
	 document.carrierform.action = "editCarrierAccount.action?option=edit&id="+id;
	 document.carrierform.submit();
	}

	function deleteAccount(id)
	{
	 document.carrierform.action = "deleteCarrierAccount.action?id="+id;
	 document.carrierform.submit();
	}
	function submitform()
	{
	 document.carrierform.action = "saveCarrierAccount.action";
	 document.carrierform.submit();
	}
	
	function findAll()
	{
	 document.carrierform.action = "getCustomerAccountInfo.action";
	 document.carrierform.submit();
	 
	}
	function resetform() {
		document.carrierform.action = "getCustomerAccountInfo.action?method=reset";
	 	document.carrierform.submit();
	}	
	var cName = "";
	
	
</SCRIPT> 

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>

<body onmousemove="javascript:disp();">
<div class="form-container"> 
<s:form action="saveCarrierAccount" name="carrierform" >

<s:hidden name="customerCarrierId" value="%{customer.customerCarrier.carrierAccountId}"></s:hidden>



<div id="accnt_info_detail1">
<div id="accnt_info_hdr1">
<table>
<tr>
<td class="accnt_info_hdr">
Customer:
</td>
<td class="markup_tbl_font">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<s:property value="customer.name"/>
</td>
</tr>
</table>
</div>
<table width="60%" border="0" cellpadding="2" cellspacing="0">
	<tr>
		<td>&nbsp;</td>
        <td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
		<tr>
			<td class="markup_tbl_font" width="20%"><s:label key="customerCarrier.carrierName" />:</td>
			<td width="22%">
				<!--
				<s:select  disabled="#session.edit" value="%{customer.customerCarrier.carrierName}" id="carrierName"  onchange="javascript:findAll();" cssClass="text_01"
					name="customer.customerCarrier.carrierName" list="{'Federal Express','UPS'}" cssStyle="width:140px;" />
				-->
				
				<s:select cssClass="text_01_combo_big" cssStyle="width:140px;" listKey="id" disabled="#session.edit"
					listValue="name" name="customer.customerCarrier.carrierId" list="#session.CARRIERS" 
						headerKey="-1" id="carrier" theme="simple"/>					
			</td>
				
				<s:if test="%{#session.edit}">
					<s:hidden value="%{customer.customerCarrier.carrierName}" name="customer.customerCarrier.carrierName" />
				</s:if>
			 <td width="5%">&nbsp;</td>
			<td class="markup_tbl_font" width="20%">Country :</td>
			<s:if test="#session.edit">
			<td width="22%"><s:select  cssClass="text_01_combo_big" value="%{customer.customerCarrier.country}" id="countryName"
				name="customer.customerCarrier.country" list="#{'US':'United States','CA':'Canada'}" cssStyle="width:140px;"/></td>
			</s:if>
			<s:else>
			<td width="22%"><s:select cssClass="text_01_combo_big" value="%{customer.customerCarrier.country}" id="countryName"
				name="customer.customerCarrier.country" list="#{'US':'United States','CA':'Canada'}" cssStyle="width:140px;"/></td>
			</s:else>
		</tr>
		
		</table>
</div>

<div id="accnt_info_detail2">
<div id="accnt_tble2"><mmr:message messageId="label.account.details"/></div>
		<table width="80%" border="0" cellpadding="5" cellspacing="0" >
			<tr>
                  <td  width="16%" class="markup_tbl_font">Live Credentials : </td>
                  <td  width="18%">&nbsp;</td>
				  <td  width="5%">&nbsp;</td>
				  <td  width="25%">&nbsp;</td>
				  <td  width="20%">&nbsp;</td>
           </tr>
        				
			<tr>
				<td width="15%" class="markup_tbl_font"  align="center"><s:label key="customerCarrier.accountNumber1" />:</td>
				<td width="19%"><s:textfield size="20" key="customer.customerCarrier.accountNumber1"
					name="customer.customerCarrier.accountNumber1" value="%{customer.customerCarrier.accountNumber1}" cssClass="text_02_tf" /></td>
				<td  width="5%">&nbsp;</td>
				<td width="20%" class="markup_tbl_font"  align="center"><s:label key="customerCarrier.accountNumber2" />:</td>
				<td width="21%"><s:textfield size="20" key="customer.customerCarrier.accountNumber2"
					name="customer.customerCarrier.accountNumber2" value="%{customer.customerCarrier.accountNumber2}" cssClass="text_02_tf" /></td>
				<td  width="5%">&nbsp;</td>
				<td width="20%" class="markup_tbl_font" align="center"><s:label key="customerCarrier.property1" />:</td>
				<td width="21%"><s:textfield size="20"  id="FEDEXBlock" key="customerCarrier.property1"
					name="customer.customerCarrier.property1" value="%{customer.customerCarrier.property1}" cssClass="text_02_tf" disabled="false"/>
				</td>				
			</tr>
			
			</tr>					
				<td width="15%" class="markup_tbl_font" align="center"><s:label key="customerCarrier.property2" />:</td>
				<td width="19%"><s:textfield size="20" key="customer.customerCarrier.property2"
					name="customer.customerCarrier.property2" cssClass="text_02_tf" value="%{customer.customerCarrier.property2}"/></td>
				<td  width="5%">&nbsp;</td>
				<td  width="20%" class="markup_tbl_font" align="center"><s:label key="customerCarrier.property3" />:</td>
				<td width="21%"><s:textfield size="20" key="customer.customerCarrier.property3" 
					name="customer.customerCarrier.property3" value="%{customer.customerCarrier.property3}" cssClass="text_02_tf" /></td>
				<td  width="5%">&nbsp;</td>
				<td  width="20%" class="markup_tbl_font" align="center"><s:label key="customerCarrier.property4" />:</td>
				<td width="21%" align="center"><s:textfield size="20" key="customer.customerCarrier.property4" 
					name="customer.customerCarrier.property4" value="%{customer.customerCarrier.property4}" cssClass="text_02_tf" /></td>
			
			</tr>
					
			<tr>		
				<td  width="15%" class="markup_tbl_font" align="center"><s:label key="customerCarrier.property5" />:</td>
				<td width="19%"><s:textfield size="20" key="customer.customerCarrier.property5" 
					name="customer.customerCarrier.property5" value="%{customer.customerCarrier.property5}" cssClass="text_02_tf" /></td>			
				<td width="5%">&nbsp;</td>
				
				<td width="20%" class="markup_tbl_font" align="center"><s:label key="customerCarrier.defaultaccount" />:</td>
				<td width="20%"  align="center">
					<s:checkbox key="customerCarrier.defaultaccount"
						name="customer.customerCarrier.defaultAccount" value="%{customer.customerCarrier.defaultAccount}" cssClass="text_02"/>
				</td>  
				<td  width="5%">&nbsp;</td>        						
				<td width="20%" class="markup_tbl_font"  align="center"><mmr:message messageId="label.carrierAccount.house"/></td>
				<td width="20%" class="markup_tbl_font"  align="center">
					<s:checkbox key="customerCarrier.live"
						name="customer.customerCarrier.live" value="%{customer.customerCarrier.live}" cssClass="text_02"/>
				</td>
			</tr>
		</table>
</div>

<div id="accnt_res"><mmr:message messageId="label.search.results"/></div>
	<div id="accnt_results">	
	<s:if test="%{customerCarrierAccountList.size()>1}">
	<div id="accnt_rslt_stmnt"><br/><s:property value="customerCarrierAccountList.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{customerCarrierAccountList.size()==1}">
	<div id="accnt_rslt_stmnt"><br/><s:property value="customerCarrierAccountList.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="accnt_rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>



<div id="accnt_srchactions_imgs">
<table>
<tr>
<td><img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/list_all.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/reset_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/save_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="accnt_srch_actions">
<table>
<tr>
<td><a href="javascript: findAll()"><mmr:message messageId="label.account.list"/></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: resetform()"><mmr:message messageId="label.btn.reset"/></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="javascript: submitform()"><mmr:message messageId="label.btn.save"/></a>&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>

<div id="accnt_bottom_table">
	<display:table requestURI="/admin/getCustomerAccountInfo.action"  id="account_table" cellspacing="0" cellpadding="4" name="customerCarrierAccountList"  uid="row">
		<display:setProperty name="paging.banner.first" value=""/>
		<display:setProperty name="paging.banner.items_name" value=""></display:setProperty>
		<display:setProperty name="paging.banner.some_items_found" value=""/>
		<display:setProperty name="paging.banner.all_items_found" value=""></display:setProperty>
		<display:column headerClass="accnt_tableTitle2_last" title="">
			<s:a title="edit" href="javascript: edit(%{customerCarrierAccountList[#attr.row_rowNum-1].carrierAccountId})" > <img src="<%=request.getContextPath()%>/mmr/images/edit_pencil.png" border="0" alt="edit"/></s:a>&nbsp;&nbsp;
			<s:a title="delete" href="javascript: if(confirm('Do you want to delete the account?')){deleteAccount(%{customerCarrierAccountList[#attr.row_rowNum-1].carrierAccountId})}" > <img src="<%=request.getContextPath()%>/mmr/images/delete.gif" border="0" alt="delete"/></s:a>
		</display:column>
		<display:column headerClass="accnt_tableTitle2_first" property="carrierName"  sortable="true" title="Carrier"/>
		<display:column headerClass="accnt_tableTitle2" property="country"  title="Country"/>
		<display:column headerClass="accnt_tableTitle2" property="accountNumber1" title="Account #1"/>
		<display:column headerClass="accnt_tableTitle2" property="accountNumber2" title="Account #2"/>
		<display:column headerClass="accnt_tableTitle2" property="defaultAccount" title="Default"/>
		<display:column headerClass="accnt_tableTitle2" property="live" title="House Acount"/>
	</display:table>	
	<div id="accnt_res_tbl_end"></div>
</div>

<s:hidden name="searchAllFlag" id="searchAll" value='true'/>
</s:form>
</div>
</div>
</body>
