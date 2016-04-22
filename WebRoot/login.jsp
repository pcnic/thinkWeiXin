<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html> 
	<head>
		<title>login</title>
	</head>
	<body>
<s:form validate="true" action="/login.action"><!--设置了validate客户端验证 -->
    <s:textfield name="userId" label="用户名"/>
    <s:password name="passWord" label="密码"/>
    <s:submit value="登陆"/><s:reset  value="重填"/> 
</s:form> 
	</body>
</html>
