<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

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
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
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
<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
<td><a href="adduser.action"><mmr:message messageId="label.shippingOrder.addUser"/></a>&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>


	<div id="srchusr_res"><mmr:message messageId="label.search.results"/></div>
	<div id="srchusr_results">	
	<s:if test="%{userList.size()>1}">
	<div id="srchusr_rslt_stmnt"><br/><s:property value="userList.size()" /><mmr:message messageId="label.search.results.items"/>
		<s:if test="%{criteria.customerId > 0}">
			<span style="color: #000000; margin-left: 100px;"><mmr:message messageId="label.users.for.customer"/>&nbsp;<strong><s:property value="%{session.customerName}"/></strong> </span>
		</s:if>
	</div>
	</s:if>
	<s:elseif test="%{userList.size()==1}">
	<div id="srchusr_rslt_stmnt"><br/><s:property value="userList.size()" /><mmr:message messageId="label.search.results.item"/>
		<s:if test="%{criteria.customerId > 0}">
			<span style="color: #000000; margin-left: 100px;"><mmr:message messageId="label.users.for.customer"/>&nbsp;<strong><s:property value="%{session.customerName}"/> </strong></span>
		</s:if>
	</div>
	</s:elseif>
	<s:else>
	<div id="srchusr_rslt_stmnt"><br/><mmr:message messageId="label.search.results.noitems"/>
		<s:if test="%{criteria.customerId > 0}">
			<span style="color: #000000; margin-left: 100px;"><mmr:message messageId="label.users.for.customer"/>&nbsp;<strong><s:property value="%{session.customerName}"/> </strong></span>
		</s:if>
	</div>
	</s:else>
	</div>


<div id="srchusr_result_tbl">
<table width="100%" border="0" cellpadding="5" cellspacing="0" style="margin-top:7px;">
	<tr>
	<td class="tableTitle2"></td>
	<td class="tableTitle2"></td>
	<td class="tableTitle2"></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.user.username"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.user.email"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.user.dateCreated"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.user.enabled"/></strong></td>
	<td class="tableTitle2"><strong><mmr:message messageId="label.user.roles"/></strong></td>
	</tr>
	<tr>		
            <s:iterator id="usertable" value="userList" status="rowstatus">
            <tr>
            <s:if test="#rowstatus.even == true">
	            <td class="even" width="2%">
		            <s:a href="edit.user.action?method=edit&name=%{username}"> <img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit User" border="0"> </s:a>
		        </td>
		        <td class="even" width="2%">
		        	<s:a onclick="return confirm('Do you really want to delete the selected user?')" href="delete.user.action?name=%{username}"> <img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete User" border="0"> </s:a>
		        </td>
		         <td class="even" width="2%">
		            <s:a href="logInAs.action?username=%{username}"> 
					<img src="<s:url value="/mmr/images/red_arrow.gif" includeContext="true" />" alt="Log In As" title="Log In As" border="0">
					</s:a>
	   			</td>
	            <td class="even"><s:property value="username"/></td>
	            <td class="even"><s:property value="email"/></td>
	            <td class="even"><s:date name="createdAt" format="dd/MM/yyyy" /></td>
	            <td class="even"><s:property value="enabled"/></td>
				<td class="even"><s:property value="userRole"/></td>
			</s:if>
			<s:else>
				<td class="odd" width="2%">
		            <s:a href="edit.user.action?method=edit&name=%{username}"> <img src="<s:url value="/mmr/images/edit_pencil.png" includeContext="true" />" alt="Edit User" border="0"> </s:a>
		        </td>
		        <td class="odd" width="2%">
		            <s:a onclick="return confirm('Do you really want to delete the selected user?')" href="delete.user.action?name=%{username}"> <img src="<s:url value="/mmr/images/delete.gif" includeContext="true" />" alt="Delete User" border="0"> </s:a>
		        </td>
		        <td class="odd" width="2%">
		            <s:a href="logInAs.action?username=%{username}"> 
					<img src="<s:url value="/mmr/images/red_arrow.gif" includeContext="true" />" alt="Log In As" title="Log In As" border="0">
					</s:a>
	   			</td>
	            <td class="odd"><s:property value="username"/></td>
	            <td class="odd"><s:property value="email"/></td>
	            <td class="odd"><s:date name="createdAt" format="dd/MM/yyyy" /></td>
	            <td class="odd"><s:property value="enabled"/></td>
				<td class="odd"><s:property value="userRole"/></td>
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