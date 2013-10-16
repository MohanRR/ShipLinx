<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html> 
<head>
    <title><s:text name="user.form.title"/></title> 
</head> 
<body> 
<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container">
<s:form>

<div id="srchusr_srchactions_imgs">
<table>
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;<img src="<s:url value="/mmr/images/DataGrid_headerSeparatorSkin.png" includeContext="true" />" border="0">&nbsp;</td>
<td><img src="<s:url value="/mmr/images/addNew_icon.png" includeContext="true" />" border="0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<div id="srchusr_srch_actions" >
<table>
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="goToAddNewPackage.action"><mmr:message messageId="menu.add.package"/></a>&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>


	<div id="srchusr_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchusr_results">	
	<s:if test="%{packageTypesList.size()>1}">
	<div id="srchusr_rslt_stmnt"><br/><s:property value="packageTypesList.size()" /><mmr:message messageId="label.search.results.items"/>
	</div>
	</s:if>
	<s:elseif test="%{packageTypesList.size()==1}">
	<div id="srchusr_rslt_stmnt"><br/><s:property value="packageTypesList.size()" /><mmr:message messageId="label.search.results.item"/>
	</div>
	</s:elseif>
	<s:else>
	<div id="srchusr_rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/>
	</div>
	</s:else>
	</div>


<div id="srchusr_result_tbl">
<table width="100%" border="0" cellpadding="5" cellspacing="0" style="margin-top:7px;">
	<s:if test="%{packageTypesList.size()>0}">
	<tr>
	<td class="tableTitle2"></td>
	<td class="tableTitle2"></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.package.name"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.package.desc"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.length.integer"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.width.integer"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.height.integer"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.weight.integer"/></strong></td>
	</tr>
	</s:if>
	<tr>		
            <s:iterator id="usertable" value="packageTypesList" status="rowstatus">
            <tr>
            <s:if test="#rowstatus.even == true">
	            <td class="even" width="2%">
		            <s:a href="editpackage.action?pid=%{packageTypeId}"> <img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Package" border="0"> </s:a>
		        </td>
		        <td class="even" width="2%">
		        	<s:a onclick="return confirm('Do you really want to delete the selected package?')" href="delete.package.action?pid=%{packageTypeId}"> <img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete Package" border="0"> </s:a>
		        </td>
	            <td class="even"><s:property value="packageName"/></td>
	            <td class="even"><s:property value="packageDesc"/></td>
	            <td class="even"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageLength"/></fmt:formatNumber>  </td>
	            <td class="even"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageWidth"/></fmt:formatNumber></td>
				<td class="even"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageHeight"/></fmt:formatNumber></td>
				<td class="even"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageWeight"/></fmt:formatNumber></td>
			</s:if>
			<s:else>
				<td class="odd" width="2%">
		             <s:a href="editpackage.action?pid=%{packageTypeId}"> <img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit Package" border="0"> </s:a>
		        </td>
		        <td class="odd" width="2%">
		            <s:a onclick="return confirm('Do you really want to delete the selected package?')" href="delete.package.action?pid=%{packageTypeId}"> <img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete Package" border="0"> </s:a>
		        </td>
	            <td class="odd"><s:property value="packageName"/></td>
	            <td class="odd"><s:property value="packageDesc"/></td>
	            <td class="odd"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageLength"/></fmt:formatNumber>  </td>
	            <td class="odd"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageWidth"/></fmt:formatNumber></td>
				<td class="odd"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageHeight"/></fmt:formatNumber></td>
				<td class="odd"><fmt:formatNumber pattern="###.##" minFractionDigits="2" maxFractionDigits="2"><s:property value="packageWeight"/></fmt:formatNumber></td>
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