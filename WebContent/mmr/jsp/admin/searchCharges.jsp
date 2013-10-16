<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<html> 
<head>
    <title><s:text name="user.form.title"/></title> 
</head> 
<body> 
<SCRIPT language="JavaScript">
function submitChargeForm()
{
	document.searchchargeform.action="searchCharges.action?search=true";
	document.searchchargeform.submit();
}
</SCRIPT>
<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container">
<s:form  action="searchCharges" name="searchchargeform">
<div id="srch_charge_panel">
<table>
<tr>
<td><div id="srch_crtra"><mmr:message messageId="label.search.charge"/></div></td>
<td>&nbsp;</td>
</tr>
</table>
<div id="srch_charges_imgs_actions">
<table>
<tr>
<td>&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0">&nbsp;</td>
<td valign="middle"><a href="goToAddNewCharge.action"><mmr:message messageId="label.search.addnew"/></a>&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/search_icon.png" includeContext="true" />" border="0">&nbsp;</td>
<td valign="middle"><a href="javascript: submitChargeForm()"><mmr:message messageId="label.search.btn.search"/></a>&nbsp;</td>
</tr>
</table>
</div>


<div id="srchcharge_table">
<table>
<tr>
<td class="hdr"><mmr:message messageId="label.charge.code"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;</td>
<td><s:textfield size="24" id="prod_name" name="carrierChargeCode.chargeCode" cssClass="text_02_tf"/> </td>
<td>&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.charge.code.2"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;</td>
<td><s:textfield size="24" id="prod_desc" name="carrierChargeCode.chargeCodeLevel2" cssClass="text_02_tf_big"/></td>
<td>&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.charge.name"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;</td>
<td><s:textfield size="24" id="prod_hcode" name="carrierChargeCode.chargeName" cssClass="text_02_tf"/></td>
</tr><tr>
<td class="hdr"><mmr:message messageId="label.charge.desc"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;</td>
<td><s:textfield size="24" id="prod_code" name="carrierChargeCode.chargeDesc" cssClass="text_02_tf"/></td>
<td>&nbsp;</td>
<td class="hdr"><mmr:message messageId="label.charge.group"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;</td>
<td>
<s:select id="chargegroup" name="carrierChargeCode.groupId" list="#session.listChargeGroups" cssClass="text_01_combo_bigger" headerValue="--Select--"  headerKey="-1" theme="simple"/>
</td>
</tr>
</table>
</div>
</div>
	<div id="charges_tab"><br/></div>
	
	<div id="srchusr_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srch_charges_results">	
	<s:if test="%{carrierChargecodeList.size()>1}">
	<div id="srchusr_rslt_stmnt"><br/><s:property value="carrierChargecodeList.size()" /><mmr:message messageId="label.search.results.items"/></div>
	</s:if>
	<s:elseif test="%{carrierChargecodeList.size()==1}">
	<div id="srchusr_rslt_stmnt"><br/><s:property value="carrierChargecodeList.size()" /><mmr:message messageId="label.search.results.item"/></div>
	</s:elseif>
	<s:else>
	<div id="srchusr_rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/></div>
	</s:else>
	</div>


<div id="srchusr_result_tbl">
<table width="100%" border="0" cellpadding="5" cellspacing="0" style="margin-top:7px;">
	<s:if test="%{carrierChargecodeList.size()>0}">
	<tr>
	<td class="tableTitle2"></td>
	<td class="tableTitle2"></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.charge.code"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.charge.code.2"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.charge.name"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.charge.desc"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.charge.group"/></strong></td>
	</tr>
	</s:if>
	<tr>		
            <s:iterator id="chargetable" value="carrierChargecodeList" status="rowstatus">
            <tr>
            <s:if test="#rowstatus.even == true">
	            <td class="even" width="2%">
		            <s:a href="editCharges.action?method=edit&id=%{carrierChargecodeList[#rowstatus.index].id}"> <img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit User" border="0"> </s:a>
		        </td>
		        <td class="even" width="2%">
		        	<s:a onclick="return confirm('Do you really want to delete the selected Charge?')" href="deleteCharge.action?id=%{carrierChargecodeList[#rowstatus.index].id}"> <img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete User" border="0"> </s:a>
		        </td>
	            <td class="even"><s:property value="chargeCode"/></td>
	            <td class="even"><s:property value="chargeCodeLevel2"/></td>
	            <td class="even"><s:property value="chargeName"/> </td>
	            <td class="even"><s:property value="chargeDesc"/></td>
				<td class="even"><s:property value="chargeGroup.groupName"/></td>
			</s:if>
			<s:else>
				<td class="odd" width="2%">
		            <s:a href="editCharges.action?method=edit&id=%{carrierChargecodeList[#rowstatus.index].id}"> <img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit User" border="0"> </s:a>
		        </td>
		        <td class="odd" width="2%">
		            <s:a onclick="return confirm('Do you really want to delete the selected Charge?')" href="deleteCharge.action?id=%{carrierChargecodeList[#rowstatus.index].id}"> <img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete User" border="0"> </s:a>
		        </td>
	           <td class="odd"><s:property value="chargeCode"/></td>
	            <td class="odd"><s:property value="chargeCodeLevel2"/></td>
	            <td class="odd"><s:property value="chargeName"/> </td>
	            <td class="odd"><s:property value="chargeDesc"/></td>
				<td class="odd"><s:property value="chargeGroup.groupName"/></td>
			</s:else>
            </tr>
            </s:iterator>
</tr>
</table>
<div id="srchusr_res_tbl_end"></div>
</div>
  
</s:form>
</div>
</body>
</html>