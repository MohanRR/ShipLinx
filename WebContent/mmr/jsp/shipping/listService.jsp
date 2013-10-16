<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body>
	<s:select cssClass="text_01_combo_big" listKey="id"
		listValue="name" name="markup.serviceId" list="#session.SERVICES" 
		 headerKey="-1" id="service" theme="simple"/>	
</body>
</html>