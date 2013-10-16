<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>

<div id="messages">
	<jsp:include page="../common/action_messages.jsp"/>
</div>
<div class="form-container">
<s:form>
<div id="right_left_new">
<div id="bottom_table" class="text_01">
<table width="100%" border="0" cellpadding="5" cellspacing="0">
	<tr>
	<td></td>
	<td class="text_03"><strong><s:label key="distribution.list.name"/></strong></td>
	
	</tr>
	<tr>
		<td valign="top">
			<s:iterator id="addresstable" value="distributionList" status="rowstatus">
            <tr>
            <td>
            <s:set name="distributionListName" value="%{distributionList.get(#rowstatus.index).getDistributionId()}" />
            <s:a onclick="return confirm('Do you really want to delete the selected address?')" href="delete.distributionList.action?distributionListId=%{distributionListName}"> <img src="<s:url value="/mmr/images/delete.png" includeContext="true" />" alt="Delete Distribution List" border="0"> </s:a></td>
                    <td class="text_01"><s:property value="distributionId"/></td>
                    
                	</tr>
            </s:iterator>
</tr>
</table>
</div>
</div>
</s:form>
</div>