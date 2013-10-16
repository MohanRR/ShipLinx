
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="mmr" uri="/mmr-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<tiles:useAttribute name="moreCss" id="moreCss" classname="java.util.List" ignore="true"/>

<%@page import="com.meritconinc.mmr.model.common.MenuItemVO"%>
<%@page import="java.util.List"%>
<html>

<head>
<link rel="stylesheet" type="text/css"	href="<s:url value='%{#session.cssToLoad}' includeContext="true"/>" /> 
<title><s:property value="%{#session.business.systemName}"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="X-UA-Compatible" content="IE=6,IE=7,IE=8" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="cache-control" content="no-store">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma"        content="no-cache">
<meta http-equiv="Expires"       content="-1">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/mmr/scripts/jsCalendar/skins/aqua/theme.css">
<script src="<%=request.getContextPath()%>/mmr/scripts/jsCalendar/calendar.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/mmr/scripts/jsCalendar/calendar-setup.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/jsCalendar/lang/calendar-en.js"></script>
<script type="text/javascript">  
  var contextPath = "<%=request.getContextPath()%>";  
</script>  



<script type="text/javascript">
 
function noError(){return true;}
window.onerror = noError;
 
</script>

	
<%
String windowStatus=(String)session.getAttribute("WINDOW_STATUS");
String packType=(String)session.getAttribute("PackageType");

//Logic for Menu selected for level 1 and 2
MenuItemVO arrMenulst = new MenuItemVO();
boolean boolLastLevelCheck = false;
String strHtag="";
String strStag ="";
List lstmenu1 = null;
List lstmenu2 = null;
if(session.getAttribute("LEVEL_ONE_MENUS")!=null)
{
	lstmenu1 = (List)session.getAttribute("LEVEL_ONE_MENUS");
}
if(session.getAttribute("LEVEL_TWO_MENUS")!=null)
{
	lstmenu2 = (List)session.getAttribute("LEVEL_TWO_MENUS");
}
// First Check if User has selected any meny for Level 2.
if(lstmenu2 !=null && lstmenu2.size()>0)
{
	for(int i=0; i<lstmenu2.size();i++)
	{
		arrMenulst = (MenuItemVO) lstmenu2.get(i);		
		if(arrMenulst.isSelected())
		{
			boolLastLevelCheck=true;
			arrMenulst = (MenuItemVO) lstmenu2.get(i);
	    	strHtag= arrMenulst.getHelptag();
			strStag = arrMenulst.getSupporttag();			
		}	
	}	
}

//If User has not selected any Menu from Level 2, then Check for Level 1 which menu is selected.
if(lstmenu1 !=null && lstmenu1.size()>0)
{
	if(boolLastLevelCheck==false)
	{
		for(int i=0; i<lstmenu1.size();i++)
		{
			arrMenulst = (MenuItemVO) lstmenu1.get(i);			
			if(arrMenulst.isSelected())
			{				
				arrMenulst = (MenuItemVO) lstmenu1.get(i);
		    	strHtag= arrMenulst.getHelptag();
				strStag = arrMenulst.getSupporttag();				
			}	
		}	
	}
}

%>
<script type="text/javascript" src="<%=request.getContextPath()%>/mmr/scripts/closeWindow.js"></script>		
<body onload="getOnloadStatus('<%=windowStatus%>','<%=packType%>','<%=request.getContextPath()%>','<%=strHtag %>','<%=strStag %>');">
<div id="wrapper_new">
	<table width="100%" cellspacing="0" height="auto" cellpadding="0">
		<tr>
			<td colspan="2">	
				<s:include value="%{#session.business.headerKey}"></s:include>		
				<tiles:insertAttribute name="topnav" />
			</td>
		</tr>
		<tr>
			<td width="45%" height="auto" valign="top" align="left" id="layoutId_first">
			  	<tiles:insertAttribute name="mainBody" />
			</td>
			<td width="10%" height="auto"  valign="top" align="right"  id="layoutId_second">
			<!--  	<tiles:insertAttribute name="rightMenu" />-->				
			</td>		
		</tr>
		<tr>
		<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
		<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
		<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
		<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
		<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
		<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
		<td colspan="2">&nbsp;</td>
		</tr>
		<tr>
			<td colspan="2">
				<tiles:insertAttribute  name="footer" />
			</td>
		</tr>
	</table>
</div>	
<br/>
</body>

</html>
			