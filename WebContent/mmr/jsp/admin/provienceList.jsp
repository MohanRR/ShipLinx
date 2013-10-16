<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<body>
<s:select cssClass="text_01" cssStyle="width:160px;" listKey="provinceCode" listValue="provinceName" name="address.provinceCode" headerKey="1" list="%{provinces}" id="secondBox"/>
</body>
</html>